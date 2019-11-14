gdkmApp.controller('depositController', function($scope, $log, $timeout, $window, 
		notificationService, depositFactory, achiveFactory, taxonFactory, Upload) {
	
	var _dataType = PAGE_DATA.RAWDATA;
	
	var _section = PAGE_SECTION.REGIST;

	var _dataLabel = MESSAGE_SECTION.RAWDATA;
	
	var message = MESSAGE.create(_dataLabel);

	$scope.display = {
		createPopupId : '',
		editPopupId : '',
		detailPopupId : '',
		accordions : [true, true, true, true],
	};
	
	/**
	 * 원본 데이터 객체
	 */
	$scope.source = null;
	
	/**
	 * 생성,수정을 위한 사본 객체
	 */
	$scope.model = null;
	
	/**
	 * 생성,수정 폼 검증 처리 객체
	 */
	$scope.validator = null;
	
	/**
	 * 연계 데이터 목록
	 */
	$scope.linkedList = [];
	
	/**
	 * 연계 데이터 테이블 이벤트 처리 핸들러
	 */
	$scope.linkedHandler = ModelListHandler.create($timeout);
	
	/**
	 * NGS 파일 목록 
	 */
	$scope.achiveList = [];
	
	/**
	 * 업로드 중인 NGS파일 목록 
	 */
	$scope.pendingList = [];
	
	/**
	 * Taxonomy 계통구조 목록 
	 */
	$scope.hierarchyList = [];
	
	/**
	 * NGS 파일 테이블 이벤트 처리 핸들러
	 */
	$scope.achiveHandler = ModelListHandler.create($timeout);
	
	$scope.uploadHandler = UploadHandler.create($window, $timeout, Upload, achiveFactory);
	
	/**
	 * NGS 데이터 파일 서열 정량정보 객체(fasta, fastq 파일의 경우) 
	 */
	$scope.quantity = null;
	
	/**
	 * QC 실행결과 
	 */
	$scope.qc = { 
		results : [],
		summary : [], 
		reportHtml : '',
	};
	/**
	 * ng-show의 값
	 */
	$scope.isVisible = false;
	
	/**
	 * 컨트롤러 초기화 
	 * 
	 * 파라미터 dataType이 rawdata인 경우는 NGS 원시데이터의 등록/수정/상세 팝업을 위한 설정추가 
	 */
	$scope.init = function(dataType, section) {
		_dataType = dataType;
		_section = section;
		
		if(Utils.isRawData(dataType)) {
			_dataLabel = MESSAGE_SECTION.RAWDATA;			
			message = MESSAGE.create(_dataLabel);
			$scope.display.createPopupId = '#raw-data-create-popup';
			$scope.display.editPopupId = '#raw-data-edit-popup';
			$scope.display.detailPopupId = '#raw-data-detail-popup';
			
			$scope.validator = RawDataValidator.create(notificationService);
		} else {
			_dataLabel = MESSAGE_SECTION.PROCESSED;
			message = MESSAGE.create(_dataLabel);
			$scope.display.createPopupId = '#processed-data-create-popup';
			$scope.display.editPopupId = '#processed-data-edit-popup';
			$scope.display.detailPopupId = '#processed-data-detail-popup';
			
			$scope.validator = ProcessedValidator.create(notificationService);
		}
		
		$scope.uploadHandler.addUploadListener(onUploadAchiveFile);
	};
	
	/* 팝업 이벤트 처리 함수 */
	$scope.showCreateNgsDataPopup = function() {
		// 아코디언 영역이 3개가 되어야 하는데 이상인 경우는 3개만 남기고 제거
		var count = Utils.isRawData($scope.model)? 2 : 3;
		var actives = $scope.display.accordions;
		if(actives.length > count) {
			actives.splice(count, (actives.length - count));
		}
		
		$scope.display.accordions = actives;
		$scope.source = new NgsDataRegist(_dataType);
		$scope.model = angular.copy($scope.source);
		
		
		// 연계 데이터 목록, 업로드 중 파일 목록, NGS 데이터 파일 핸들러 초기화
		$scope.linkedList = [];
		$scope.linkedHandler.setItems([]);
		$scope.hierarchyList = [];
		
		$scope.hideNgsDataPopup();
		$($scope.display.createPopupId).modal('show');
	};
	
	$scope.showDetailNgsDataPopup = function(item, section) {
		var count = Utils.isRawData($scope.model)? 3 : 4;
		var actives = $scope.display.accordions;
		if(actives.length < count) {
			actives.push(false);
		}
		_section = section;
		$scope.isVisible = false;
		$scope.display.accordions = actives;
		$scope.source = item;
		$scope.model = angular.copy(item);
		
		// 연계 데이터 목록, 업로드 중 파일 목록, NGS 데이터 파일 핸들러 초기화
		$scope.linkedList = [];
		$scope.linkedHandler.setItems([]);
		
		_loadNgsDataLinkedList(item);
		_loadNgsDataAchiveList(item);
		_loadHierarchyList(item.taxonomy);
		
		$scope.hideNgsDataPopup();
		$($scope.display.detailPopupId).modal('show');
	};
	
	$scope.showEditNgsDataPopup = function(item) {
		var count = Utils.isRawData($scope.model)? 3 : 4;
		var actives = $scope.display.accordions;
		if(actives.length < count) {
			actives.push(false);
		}
		$scope.display.accordions = actives;
		$scope.source = item;
		$scope.model = angular.copy(item);
		
		// 연계 데이터 목록, 업로드 중 파일 목록, NGS 데이터 파일 핸들러 초기화
		$scope.linkedList = [];
		$scope.linkedHandler.setItems([]);
		
		$scope.uploadHandler.clear();
		$scope.uploadHandler.setRequestUrl(Utils.getContextPath() + "/regist-data/" + item.id + "/achieves");
		
		_loadNgsDataLinkedList(item);
		_loadNgsDataAchiveList(item);
		_loadHierarchyList(item.taxonomy);
		
		$scope.hideNgsDataPopup();
		$($scope.display.editPopupId).modal('show');
	};
	
	$scope.hideNgsDataPopup = function() {
		$($scope.display.createPopupId).modal('hide');
		$($scope.display.editPopupId).modal('hide');
		$($scope.display.detailPopupId).modal('hide');
	};
	
	/**
	 * 생성,수정 화면의 초기화 버튼 이벤트 처리 
	 */
	$scope.resetNgsDataForm = function() {
		$scope.model = angular.copy($scope.source);
		$scope.linkedHandler.setItems(angular.copy($scope.linkedList));
		_loadHierarchyList($scope.model.taxonomy);
	};
	
	/**
	 * NGS 등록데이터($scope.model)가 등록완료 상태이면 true 반환
	 */
	$scope.isFinishedDeposit = function(data) {
		var registStatus = null;
		
		if(angular.isObject(data)) {
			registStatus = data['registStatus']
		} else if(angular.isString(data)) {
			registStatus = data;
		} else if(angular.isObject($scope.model)) {
			registStatus = $scope.model['registStatus'];
		}
		
		return (registStatus == REGIST_STATUS.FAILED 
				|| registStatus == REGIST_STATUS.SUCCESS);
	};
	
	/**
	 * NGS 등록데이터($scope.model)가 제출 상태이면 true 반환
	 */
	$scope.isSubmitDeposit = function(data) {
		var registStatus = null;
		
		if(angular.isObject(data)) {
			registStatus = data['registStatus']
		} else if(angular.isString(data)) {
			registStatus = data;
		} else if(angular.isObject($scope.model)) {
			registStatus = $scope.model['registStatus'];
		}
		
		return (registStatus == REGIST_STATUS.SUBMIT);
	};
	
	/**
	 * NGS 등록데이터($scope.model)가 수정 가능한 상태이면 true 반환
	 */
	$scope.isEditableDeposit = function(data) {
		var registStatus = null;
		
		if(angular.isObject(data)) {
			registStatus = data['registStatus']
		} else if(angular.isString(data)) {
			registStatus = data;
		} else if(angular.isObject($scope.model)) {
			registStatus = $scope.model['registStatus'];
		}
		
		return (registStatus != REGIST_STATUS.SUBMIT
				&& registStatus != REGIST_STATUS.FAILED 
				&& registStatus != REGIST_STATUS.SUCCESS);
		
		// TODO (2018-08-06) 등록상태가 완료되었어도 수정할 수 있도록 임시로 코드 수정  
		// return (registStatus != REGIST_STATUS.SUBMIT && registStatus != REGIST_STATUS.FAILED);
	};
	
	/**
	 * NGS 등록데이터($scope.model)가 삭제 가능한 상태이면 true 반환
	 */
	$scope.isDeletableDeposit = function(data) {
		var registStatus = null;
		
		if(angular.isObject(data)) {
			registStatus = data['registStatus']
		} else if(angular.isString(data)) {
			registStatus = data;
		} else if(angular.isObject($scope.model)) {
			registStatus = $scope.model['registStatus'];
		}
		
		return (registStatus != REGIST_STATUS.VALIDATING
				&& registStatus != REGIST_STATUS.SUBMIT 
				&& registStatus != REGIST_STATUS.SUCCESS);
	};
	
	/**
	 * NGS 등록데이터($scope.model)가 제출 가능한 상태이면 true 반환
	 */
	$scope.isSubmttableDeposit = function(data) {
		var registStatus = null;
		
		if(angular.isObject(data)) {
			registStatus = data['registStatus']
		} else if(angular.isString(data)) {
			registStatus = data;
		} else if(angular.isObject($scope.model)) {
			registStatus = $scope.mode['registStatus'];
		}
		
		return (registStatus != REGIST_STATUS.VALIDATED);
	};
		
	$scope.revertRegistStatus = function() {
		if(!angular.isObject($scope.model))
			return notificationService.error("Failed to revert status!");
		
		if($scope.model.id < 1)
			return notificationService.notice(message.NOTICE014_EN);
		
		var data = angular.copy($scope.model);
		data.registStatus = REGIST_STATUS.VALIDATED;
		
		waitingDialog.show("Changing ...");
		depositFactory.changeRegistStatus([data]).then(
			function(res) {
				waitingDialog.hide();
				
				if(res.data.length > 0) {
					notificationService.success("Complete change status.");
					$scope.$broadcast("onChangeStatus", _dataType, res.data);
					$scope.hideNgsDataPopup();
				} else {
					notificationService.notice("Could not change status");
				}
			}, 
			function(err) {
				waitingDialog.hide();
				notificationService.error("Failed to change status - " + err.data);
			}
		);		
	};
	
	$scope.isRegistSection = function() {
		return (_section == PAGE_SECTION.REGIST);
	};
	/**
	 * section이 review이면서
	 * seq를 가지고 있는item만 true
	 */
	$scope.isReviewSection = function(item) {
		if($scope.hasSeqQuantity(item)){
			return (_section == PAGE_SECTION.REVIEW);
		}else{
			return false;
		}
	};
	
	/**
	 * fastq 파일이면서
	 * section이 review이고 
	 * seq를 가지고 있는 item만 true
	 */
	$scope.isFastqc = function(item){
		if(FileUtils.isFastqData(item) && $scope.isReviewSection(item)){
			return true;
		}else{
			return false;
		}
	}
	
	$scope.showReport = function(item, section){
		$scope.fileItem = item;
		if($scope.isReviewSection){
			_loadSeqQuantity(item);
			_loadSeqQcResultList(item);
			$scope.isVisible = true;
			return true;
		}else{
			$scope.isVisible = false;
			return false;
		}
	}
	
	$scope.hasSeqQuantity = function(item) {
		var achive = (angular.isDefined(item)) ? item : $scope.fileItem;
		if(FileUtils.isFastaData(achive) || FileUtils.isFastqData(achive)) {
			return true;
		}
		return false;
	};
	
    /**
     * 서열 정량정보 호출
     */
    var _loadSeqQuantity = function(achive) {
    	if(!angular.isObject(achive) || (achive['id'] > 0) == false)
			return $log.error("Could not load sequence information");
    	
    	$scope.quantity = null;
    	
    	achiveFactory.getNgsSeqQuantity(achive.id).then(
			function(res) {
    			$scope.quantity = res.data;
//    			$scope.model.fileType = achive.fileType;
    		},
    		function(err) {
    			$log.error(angular.toJson(err, true));
    		}
    	);
    };
    
    /**
     * 서열 QC 결과파일 목록 호출 
     */
    var _loadSeqQcResultList = function(achive) {
    	if(!angular.isObject(achive) || (achive['id'] > 0) == false)
			return $log.error("Could not load QC results");
    	
    	$scope.qc.results = [];
    	
    	achiveFactory.getNgsSeqQcResultList(achive.id).then(
			function(res) {
    			$scope.qc.results = res.data;
    		},
    		function(err) {
    			$log.error(angular.toJson(err, true));
    		}
    	);
    };
        
    /**
     * 서열 QC 요약정보 목록 호출 
     */
    var _loadSeqQcSummaryList = function(achive) {
    	if(!angular.isObject(achive) || (achive['id'] > 0) == false)
			return $log.error("Could not load QC summary list");
    	
    	$scope.qc.summary = [];
    	
    	achiveFactory.getNgsSeqQcSummaryList(achive.id).then(
			function(res) {
    			$scope.qc.summary = res.data;
    		},
    		function(err) {
    			$log.error(angular.toJson(err, true));
    		}
    	);
    };
    
	/**
	 * qc정보
	 */
	$scope.hasSeqQc = function(item) {
		var achive = (angular.isDefined(item)) ? item : $scope.fileItem;
		if(FileUtils.isFastqData(achive)) {
			console.log($scope.qc.results.length > 0);
			return ($scope.qc.results.length > 0);
		}
		
		return false;
	};
	
	/*
	 * 아코디온 패널의 펼침/접힘 기능 처리 함수 
	 * $scope.display.accordions 배열에 펼침 여부들이 저장 되어 있음
	 */	
	$scope.toggle_panels_css = function() {
		return ($scope.display.accordions.indexOf(false) < 0) ? 'active' : '';
	};
	
	$scope.toggleAccordions = function() {
		var expand = ($scope.display.accordions.indexOf(false) < 0) ? false : true;
		for(var i=0 ; i < $scope.display.accordions.length ; i++) {
			$scope.display.accordions[i] = expand;
		}
	};
	
	$scope.createNgsDataRegist = function() {
		if(Utils.isBlank($scope.model)) {
			return notificationService.notice(message.NOTICE014_EN);
		}
		
		$scope.model.linkedList = $scope.linkedHandler.getItems();
		if($scope.model.linkedList.length > 0) {
			let rawdata = $scope.model.linkedList[0];
			$scope.model.taxonomy = rawdata.taxonomy;
			$scope.model.strain = rawdata.strain;
		}
				if(!$scope.validator.validate($scope.model))
			return;
		
		waitingDialog.show("Creating ...");
		depositFactory.createNgsDataRegist($scope.model).then(
			function(res) {
				waitingDialog.hide();
				notificationService.success(message.NOTICE001_EN + "-" + res.data.registNo + "\nPlease upload your ngs data file(s)." );
				
				$scope.$broadcast("onChangeRegistData", _dataType, res.data);
				$scope.$emit("onChangeRegistData", _dataType, res.data);
				
				$scope.showEditNgsDataPopup(res.data);
				
				$timeout(function() {
					for(let i=0 ; i < $scope.display.accordions.length-1 ; i++) 
						$scope.display.accordions[i] = false;
					
					$scope.display.accordions[$scope.display.accordions.length-1] = true;
				}, 1000);
			}, 
			function(err) {
				waitingDialog.hide();
				notificationService.error(err.data);
				$scope.validateLoginAuthority(err);
			}
		);
	};
	
	$scope.changeNgsDataRegist = function() {
		if(Utils.isBlank($scope.model) || !($scope.model.id > 0)) {
			return notificationService.notice(message.NOTICE014_EN);
		}
		
		$scope.model.linkedList = $scope.linkedHandler.getItems();
		if($scope.model.linkedList.length > 0) {
			let rawdata = $scope.model.linkedList[0];
			$scope.model.taxonomy = rawdata.taxonomy;
			$scope.model.strain = rawdata.strain;
		}
		
		if(Utils.isBlank($scope.model.taxonomy.taxonId)){
			return notificationService.notice("Invalid Taxonomy ID");
		}
		
		if(!$scope.validator.validate($scope.model)) {
			return;
		}
		
		waitingDialog.show("Changing ...");
		depositFactory.changeNgsDataRegist($scope.model).then(
			function(res) {
				waitingDialog.hide();
				notificationService.success(message.NOTICE002_EN + " - " + res.data.registNo);
				
				$scope.$broadcast("onChangeRegistData", _dataType, res.data);
				$scope.$emit("onChangeRegistData", _dataType, res.data);
				
				$scope.showDetailNgsDataPopup(res.data);
			}, 
			function(err) {
				waitingDialog.hide();
				notificationService.notice(angular.toJson(err.data));
				$($scope.display.editPopupId).modal('hide');
				$scope.validateLoginAuthority(err);
			}
		);
	};
	
	/**
	 * NGS 데이터 등록 승인/반려 상태 변경
	 * @parameter	status 승인상태 (APPROVAL_STATUS.ACCEPT, APPROVAL_STATUS.REJECT)  
	 */
	$scope.changeConfirmStatus = function(status) {
		if(!confirm("Are you sure " + status + " " + _dataLabel + " data?")){
			return;
		}
		
		$scope.model.approvalStatus = status;
		$scope.model.approvalDate = new Date();
		 
		if(APPROVAL_STATUS.ACCEPT == status) {
			$scope.model.registStatus = REGIST_STATUS.SUCCESS;
		} else {
			$scope.model.registStatus = REGIST_STATUS.FAILED;
		}
		
		waitingDialog.show("Changing ...");
		depositFactory.changeConfirmStatus([$scope.model]).then(
			function(res) {
				waitingDialog.hide();
				notificationService.success("Successfully " + status + " " + _dataLabel);
				
				if(res.data.length > 0) {
					$scope.source = res.data[0];
					$scope.model = angular.copy(res.data[0]);
					
					$scope.$broadcast("onChangeRegistData", _dataType, res.data);
					$scope.$emit("onChangeRegistData", _dataType, res.data);
				}
			}, 
			function(err) {
				waitingDialog.hide();
			}
		);

	};
	
	/**
	 * NGS 데이터 개별 삭제
	 */
	$scope.deleteNgsDataRegist = function() {
		if(!confirm("Are you sure remove " + _dataLabel + "?"))
			return;
		
		if(Utils.isBlank($scope.model) || !($scope.model.id > 0)) {
			return notificationService.notice(message.NOTICE014_EN);
		}
		
		waitingDialog.show("Deleting ...");
		depositFactory.deleteNgsDataRegist($scope.model.id).then(
			function(res) {
				waitingDialog.hide();
				
				notificationService.success(message.NOTICE004_EN + res.data.registNo);
					
				$scope.$broadcast("onChangeRegistData", _dataType, res.data);
				$scope.$emit("onChangeRegistData", _dataType, res.data);
					
				$scope.hideNgsDataPopup();
			}, 
			function(err) {
				waitingDialog.hide();
				notificationService.notice(angular.toJson(err.data));
				$($scope.display.detailPopupId).modal('hide');
				$scope.validateLoginAuthority(err);
			}
		);
	};
	
	/**
	 * NGS 데이터 파일 삭제
	 */
	$scope.deleteAchiveFiles = function() {
		var idList = $scope.achiveHandler.getSelectedIdList();
		if(idList.length == 0) {
			return notificationService.notice("No selected " + _dataLabel + " file");
		}
		
		if(!confirm("Are you sure delete " + _dataLabel + " file?"))
			return;
		
		if(Utils.isBlank($scope.model) || !($scope.model.id > 0)) {
			return notificationService.notice(message.NOTICE014_EN);
		}
		
		waitingDialog.show("Removing ...");
		achiveFactory.deleteNgsDataAchive(idList).then(
			function(res) {
				waitingDialog.hide();
				var removedList = res.data;
				
				for(var i=0; i < removedList.length ; i++) {
					$scope.achiveHandler.remove(removedList[i]);
				}
				
				notificationService.success("Successfully delete " + removedList.length + " " + _dataLabel + " file(s)");
			}, 
			function(err) {
				waitingDialog.hide();
				notificationService.error("Failed to delete " + _dataLabel + " file(s) -" + err.data);
			}
		);
	};

	$scope.$on('onChangeStatus', function(event, type, data) {
		if(_dataType == type) {
			$scope.$broadcast("onChangeRegistData", type, data);
			$scope.$emit("onChangeRegistData", type, data);
		}
	});
	
	/**
	 * 선택된 연계데이터를 반영
	 * 연계 데이터가 여러개인 경우 Taxonomy와 Strain이 일치하지 않으면 오류 처리   
	 */
	$scope.$on('onSelectLinkedData', function(event, selectedList) {
		if(selectedList.length == 0) {
			return notificationService.notice("No selected raw data");
		}
		
		var taxonomy = selectedList[0].taxonomy;
		var strain = selectedList[0].strain;
		
		var linkedList = $scope.linkedHandler.getItems();
		
		for(var i=0 ; i < linkedList.length ; i++) {
			if(taxonomy.taxonId != linkedList[i].taxonomy.taxonId) {
				return notificationService.notice("Taxonomy does not match!");
			}
			
			if(strain != linkedList[i].strain) {
				return notificationService.notice("Strain does not match!");
			}
		}
		
		var idList = linkedList.map(function(rawdata) {return rawdata.id; })
		for(var i=0 ; i < selectedList.length ; i++) {
			if(idList.indexOf(selectedList[i].id) < 0) {
				linkedList.push(selectedList[i]);
			}
		}
		
		$scope.linkedHandler.setItems(linkedList);
		$scope.model.taxonomy = selectedList[0].taxonomy;
		$scope.model.strain = selectedList[0].strain;
	});
	
	/**
	 * 선택된 Taxonomy 데이터 적용   
	 */
	$scope.$on('onSelectTaxonomy', function(event, taxonomy) {
		if(!angular.isObject(taxonomy)) {
			return notificationService.notice("Invalid taxonomy");
		}
		
		_loadHierarchyList(taxonomy);
		$scope.model.taxonomy = taxonomy;		
	});
	
	/**
	 * UploadHandler 클래스의 비동기 업로드 이벤트 처리 
	 *  state = UPLOAD_STATE.PENDING : 업로드 중인 상태
	 *  state = UPLOAD_STATE.COMPLETE : 업로드 완료
	 *  state = UPLOAD_STATE.FAIL : 업로드 실패    
	 */
	var onUploadAchiveFile = function(state, pendingFile, data) {
		if(UPLOAD_STATE.FAIL == state) {
			_removePendingFileInList(pendingFile);
			notificationService.error("Failed to upload '" + pendingFile.fileName + "'");
		} else if(UPLOAD_STATE.PENDING == state) {
			if($scope.pendingList.indexOf(pendingFile) < 0)
				$scope.pendingList.push(pendingFile);
		} else if(UPLOAD_STATE.COMPLETE == state) {
			_removePendingFileInList(pendingFile); 
			
			$scope.achiveHandler.setItem(data);
			notificationService.success("Complete upload '" + data.fileName + "'");
			
			_reloadNgsDataAchiveListAsync();
		}
	};
	
	var _removePendingFileInList = function(pendingFile) {
		var index = $scope.pendingList.indexOf(pendingFile);
		if(index >= 0) {
			$scope.pendingList.splice(index, 1);
		}
	};
	
	/* 서버 통신 처리 함수 */
	/**
	 * NGS 데이터 연계 데이터 목록 호출
	 */
	var _loadNgsDataLinkedList = function(regist) {
		if(Utils.isBlank(regist))
			return $log.error("Could not load linked data - empty reigst id");
		
		var registId = regist; 
		if(angular.isObject(regist)) {
			registId = regist.id;
		}
		
		if(registId > 0) {
			depositFactory.getNgsDataLinkedList(registId).then(
	    		function(res) {
	    			waitingDialog.hide();
	    			$scope.linkedList = res.data;
	    			$scope.linkedHandler.setItems(angular.copy($scope.linkedList));
	    		},
	    		
	    		function(err) {
	    			waitingDialog.hide();
	    			$log.error(angular.toJson(err, true));
	    		}
	    	);
		}
    };
    
    /**
     * 성과물 파일중에 처리중인 파일이 있는 경우 5초에 한번씩 다시 확인하도록 처리 
     */
    var _reloadNgsDataAchiveListAsync = function() {
    	if(!angular.isObject($scope.model) || $scope.model.id <= 0)
    		return;
    	
    	var achiveList = $scope.achiveHandler.getItems();
    	for(let i=0; i < achiveList.length ; i++) {
    		if(achiveList[i].registStatus == REGIST_STATUS.RUNNING) {
    			$timeout(function() {
    				_loadNgsDataAchiveList($scope.model);
    				_reloadNgsDataAchiveListAsync();
    			}, 5000);
    			
    			return;
    		}
    	}
    };
    
    var _loadNgsDataAchiveList = function(regist) {
    	if(Utils.isBlank(regist))
			return $log.error("Could not load achive data - empty reigst id");
		
		var registId = regist; 
		if(angular.isObject(regist)) {
			registId = regist.id;
		}
		
		if(registId > 0) {
			achiveFactory.getNgsDataAchiveListOfRegist(registId).then(
	    		function(res) {
	    			$scope.achiveList = res.data;
	    			$scope.achiveHandler.setItems(angular.copy($scope.achiveList));
	    		},
	    		
	    		function(err) {
	    			$log.error(angular.toJson(err, true));
	    		}
	    	);
		}
    };
    
    var _loadHierarchyList = function(taxonomy) {
    	$scope.hierarchyList = [];
    	
    	if(!angular.isObject(taxonomy) || (taxonomy['taxonId'] > 1) == false)
			return $log.error("Could not load taxonomy hierarchy list");
    	
    	taxonFactory.getHierarchyList(taxonomy.taxonId).then(
			function(res) {
    			$scope.hierarchyList = res.data;
    		},
    		
    		function(err) {
    			$log.error(angular.toJson(err, true));
    		}
    	);
    };
})