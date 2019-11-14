gdkmApp.controller('depositListController', function($scope, $route, $log,
		$timeout, depositFactory, notificationService, excelFactory) {

	var _activeRows = [];
	
	var _dataLabel = "Raw data";
	
	var message = MESSAGE.create(_dataLabel);
	
	$scope.params = {
		dataType: PAGE_DATA.RAWDATA,
		section: PAGE_SECTION.PUBLIC,
		openYn: '',
		registStatus: '',
		registFrom: null,
		registTo: null,
		fields : '',
		keyword : '',		
		page: 0,
		rowSize: 10,
	};
	
	$scope.modelHandler = ModelListHandler.create($timeout);
	
	$scope.pageHandler = PageHandler.create();
	
	$scope.init = function(dataType, section) {
		$scope.params.dataType = dataType;
		$scope.params.section = section;
		$scope.pageHandler.setPageChangeListener(_loadNgsDataRegist);
		
		_dataLabel = (Utils.isRawData(dataType))? "Raw data" : "Annotation";
		message = MESSAGE.create(_dataLabel);
		
		_loadNgsDataRegist();
	}
	
	/* 테이블 상세 레코드 펼침/닫힘 UI 처리 함수  */
	$scope.toggle_panel_css = function() {
		if($scope.modelHandler.size() == 0) 
			return '';
	
		return (_activeRows.length == $scope.modelHandler.size())? 'active' : ''; 
	};
	
	$scope.panel_h_css = function(rowId) {
		return (_activeRows.indexOf(rowId) < 0) ? '' : 'active';
	};
	
	$scope.panel_collapse_css = function(rowId) {
		return (_activeRows.indexOf(rowId) < 0) ? 'collapse' : '';
	};
	
	$scope.activeRow = function (rowId) {
		if (_activeRows.indexOf(rowId) < 0) {
			_activeRows.push(rowId);
		} else {
			_activeRows.splice(_activeRows.indexOf(rowId), 1);
		}
    };
	
	$scope.toggleActiveRows = function() {
		if($scope.modelHandler.size() == 0)
			return;
		
		if(_activeRows.length == $scope.modelHandler.size()) {
			_activeRows = [];
		} else {
			_activeRows = $scope.modelHandler.getItemIdList();
		}
	};
	
	/* 등록 데이터 목록 검색 요청 */
	$scope.search = function() {
		waitingDialog.show("Loading ...");
		_loadNgsDataRegist();
	};
	
	/**
	 * 선택된 목록이 제출 상태가 아닌 목록이 선택된 경우 true 반환
	 */
	$scope.disabledConfirmBtn = function() {
		var selectedList = $scope.modelHandler.getSelectedItems();
		if(selectedList.length == 0)
			return true;
		
		for(var i=0; i < selectedList.length ; i++) {
			if(selectedList[i].registStatus != REGIST_STATUS.SUBMIT)
				return true;
		}
		
		return false;
	};
	
	/**
	 * 선택된 NGS 데이터 승인/반려 상태 변경 요청
	 */
    $scope.changeConfirmStatus = function(status) {
		var selectedList = $scope.modelHandler.getSelectedItems();
		if(selectedList.length == 0) {
			return notificationService.notice(message.NOTICE017_EN);
		}
		
		for(var i=0 ; i < selectedList.length ; i++) {
			if(selectedList[i].registStatus != REGIST_STATUS.SUBMIT)
				return notificationService.notice(message.NOTICE020_EN);
		}

		if(!confirm("Are you sure " + status + " " + _dataLabel + "(s)?")) {
			return;
		}
		
		var registList = [];
		for(var i=0 ; i < selectedList.length ; i++) {
			var regist = angular.copy(selectedList[i]);
			regist.approvalStatus = status;
			regist.approvalDate = new Date();
			
			if(APPROVAL_STATUS.ACCEPT == status) {
				regist.registStatus = REGIST_STATUS.SUCCESS;
			} else {
				regist.registStatus = REGIST_STATUS.FAILED;
			}
			
			registList.push(regist);
		}
		
		waitingDialog.show("Changing ...");
		depositFactory.changeConfirmStatus(registList).then(
			function(res) {
				waitingDialog.hide();
				if(res.data.length > 0) {
					notificationService.success(message.NOTICE006_EN + status + " - " + res.data.length + " " + _dataLabel + "(s)");
					$scope.$emit("onChangeStatus", $scope.params.dataType, res.data);
				} else {
					notificationService.error("Could not " + status + " " + _dataLabel + "(s)");
				}
			}, 
			function(err) {
				waitingDialog.hide();
				notificationService.error(err.data);
				$scope.validateLoginAuthority(err);
			}
		);
	};
	
	/**
	 * 공개버튼 비활성화 상태 반환
	 * 등록상태가 승인(success)상태이면서 공개이전 목록이 선택된 경우 비활성화 시킴
	 */
	$scope.disabledOpenBtn = function() {
		var selectedList = $scope.modelHandler.getSelectedItems();
		if(selectedList.length == 0)
			return true;
		
		for(var i=0; i < selectedList.length ; i++) {
			if(selectedList[i].registStatus != REGIST_STATUS.SUCCESS)
				return true;
			
			if(selectedList[i].openYn == 'Y')
				return true;
		}
		
		return false;
	};
	
	/**
	 * 선택된 NGS 데이터 공개 상태 변경 요청
	 */
	$scope.changeOpenStatus = function() {
		var selectedList = $scope.modelHandler.getSelectedItems();
		if(selectedList.length == 0) {
			return notificationService.notice("No selected " + _dataLabel);
		}
		
		for(var i=0 ; i < selectedList.length ; i++) {
			if(selectedList[i].registStatus != REGIST_STATUS.SUCCESS)
				return notificationService.notice("It is only possible acceptted data.");
			
			if(selectedList[i].openYn == 'Y')
				return notificationService.notice("It is already opened-" + selectedList[i].registNo);
		}

		if(!confirm("Are you sure open " + _dataLabel + "?")) {
			return;
		}
		
		var registList = [];
		for(var i=0 ; i < selectedList.length ; i++) {
			var regist = angular.copy(selectedList[i]);
			regist.openYn = 'Y';
			regist.openDate = new Date();
			
			registList.push(regist);
		}
		
		waitingDialog.show("Changing ...");
		depositFactory.changeOpenStatus(registList).then(
			function(res) {
				waitingDialog.hide();
				if(res.data.length > 0) {
					notificationService.success("Successfully open " + res.data.length + " " + _dataLabel + "(s)");
					$scope.$emit("onChangeStatus", $scope.params.dataType, res.data);
				} else {
					notificationService.notice("Could not open " + _dataLabel + "(s)");
				}
			}, 
			function(err) {
				waitingDialog.hide();
				notificationService.error("Failed to open " + _dataLabel + "(s)-" + err.data);
			}
		);
	};
	
	/**
	 * 제출버튼 비활성화 상태 반환
	 * 등록상태가 검증완료(validated)인 목록이 선택된 경우만 활성화 시킴
	 */
	$scope.disabledSubmitBtn = function() {
		var selectedList = $scope.modelHandler.getSelectedItems();
		if(selectedList.length == 0)
			return true;
		
		for(var i=0; i < selectedList.length ; i++) {
			if(selectedList[i].registStatus != REGIST_STATUS.VALIDATED)
				return true;
		}
		
		return false;
	}
	
	/**
	 * 선택된 NGS 데이터 등록 상태 변경 요청(submission 실행)
	 */
	$scope.changeRegistStatus = function(status) {
		var selectedList = $scope.modelHandler.getSelectedItems();
		if(selectedList.length == 0) {
			return notificationService.notice("No selected " + _dataLabel + "");
		}
		
		for(var i=0 ; i < selectedList.length ; i++) {
			if(selectedList[i].registStatus != REGIST_STATUS.VALIDATED)
				return notificationService.notice("It is only possible verified data.");
		}

		if(!confirm("Are you sure " + status + " " + _dataLabel + "(s)?")) {
			return;
		}
		
		var registList = [];
		for(var i=0 ; i < selectedList.length ; i++) {
			var regist = angular.copy(selectedList[i]);
			regist.registStatus = status; 
			
			registList.push(regist);
		}
		
		waitingDialog.show("Changing ...");
		depositFactory.changeRegistStatus(registList).then(
			function(res) {
				waitingDialog.hide();
				if(res.data.length > 0) {
					notificationService.success("Successfully " + status + " " + res.data.length + " " + _dataLabel + "(s)");
					$scope.$emit("onChangeStatus", $scope.params.dataType, res.data);
				} else {
					notificationService.notice("Could not change status " + _dataLabel + "(s)");
				}
			}, 
			function(err) {
				waitingDialog.hide();
				notificationService.error("Failed to " + status + " " + _dataLabel + "(s)-" + err.data);
			}
		);
	};
	
	/**
	 * 검증버튼 비활성화 상태 반환
	 * 등록상태가 준비(ready) 혹은 파일검증 오류(error)상태인 목록이 선택된 경우만 활성화 시킴
	 */
	$scope.disabledVerificationBtn = function() {
		var selectedList = $scope.modelHandler.getSelectedItems();
		if(selectedList.length == 0)
			return true;
		
		for(var i=0; i < selectedList.length ; i++) {
			if(selectedList[i].registStatus != REGIST_STATUS.READY &&
					selectedList[i].registStatus != REGIST_STATUS.ERROR) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * 선택된 NGS 데이터 검증 실행 요청
	 */
	$scope.launchVerificationNgsData = function() {
		var selectedList = $scope.modelHandler.getSelectedItems();
		if(selectedList.length == 0) {
			return notificationService.notice(message.NOTICE017_EN);
		}
		
		for(var i=0 ; i < selectedList.length ; i++) {
			if(selectedList[i].registStatus != REGIST_STATUS.READY &&
					selectedList[i].registStatus != REGIST_STATUS.ERROR) {
				return notificationService.notice(message.NOTICE018_EN);
			}
		}

		if(!confirm(message.NOTICE019_EN)) {
			return;
		}
		
		waitingDialog.show("Changing ...");
		depositFactory.validateNgsDataRegists(selectedList).then(
			function(res) {
				waitingDialog.hide();
				if(res.data.length > 0) {
					notificationService.success(message.NOTICE005_EN + res.data.length);
					$scope.$emit("onChangeStatus", $scope.params.dataType, res.data);
				} else {
					notificationService.notice(message.NOTICE015_EN);
				}
			}, 
			function(err) {
				waitingDialog.hide();
				notificationService.error(err.data);
				$scope.validateLoginAuthority(err);
			}
		);
	};
	
	$scope.$on('onChangeRegistData', function(event, type, data) {
		if($scope.params.dataType == type) { 
			_loadNgsDataRegist();
		}
	});
	
	$scope.downloadExcel = function(){
		var params = {
			dataType: $scope.params.dataType,
			section: $scope.params.section,
			openYn: $scope.params.openYn,
			registStatus: $scope.params.registStatus,
			registFrom: $scope.params.registFrom,
			registTo: $scope.params.registTo,
			fields : $scope.params.fields,
			keyword : $scope.params.keyword
		}
		depositFactory.downloadExcel(params).then(
			function(res){
				var header = res.headers('Content-Disposition')
                var fileName = header.split("=")[1].replace(/\"/gi,'');
	 
                var blob = new Blob([res.data],
                    {type : 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;base64,'});
	                var objectUrl = (window.URL || window.webkitURL).createObjectURL(blob);
	                var link = angular.element('<a/>');
	                link.attr({
	                    href : objectUrl,
	                    download : fileName
	                })[0].click();
			},
			function(err){
				console.log(err);
				// notificationService.error(err.data);
				notificationService.error("Fail Excel Download");
			}
		)
    }
	
	/**
	 * 데이터가 있는지 유무 
	 */
	$scope.disabledDownloadExcelBtn = function(){
		if($scope.modelHandler.getItems()==''){
			return true;
		}else{
			return false;
		}
	}
	
    var _loadNgsDataRegist = function() {
    	$scope.params.page = $scope.pageHandler.getCurrentPage();
    	$scope.params.rowSize = $scope.pageHandler.getNumberOfRows();
    	
    	depositFactory.getNgsDataRegistList($scope.params).then(
    		function(res) {
    			waitingDialog.hide();
    			
    			$scope.modelHandler.setItems(res.data.list);
    			$scope.pageHandler.setTotal(res.data.total);
    			$scope.pageHandler.setCurrentPage(res.data.page);
    			
    			$timeout(init_uicomponents, 500);
    		},
    		
    		function(err) {
    			waitingDialog.hide();
    			$log.error(angular.toJson(err, true));
    			notificationService.notice(angular.toJson(err.data));
    			$scope.validateLoginAuthority(err);
    		}
    	);
    };
})