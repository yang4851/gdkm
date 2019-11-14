gdkmApp.controller('integratedController', function($scope, $log, $timeout, $window, $cacheFactory, 
		notificationService, depositFactory, achiveFactory, taxonFactory) {
	
	$scope.display = {
		editPopupId : '',
		detailPopupId : '',
		accordions : [true, false, false, false],
	};
	
	$scope.dataLabel = null;
	$scope.params = new SearchParams();
	/**
	 * NGS 데이터 파일 객체
	 */
	$scope.model = null;
	
	/**
	 * Taxonomy 계통구조 목록
	 */
	$scope.hierarchyList = [];
	
	/**
	 * NGS 데이터 등록정보 연계데이터 목록 핸들러
	 */
	$scope.linkedHandler = ModelListHandler.create($timeout);
	
	/**
	 * NGS 데이터 등록정보 데이터파일 목록 핸들러
	 */
	$scope.achiveHandler = ModelListHandler.create($timeout);
	
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
	};
	
	/**
	 * 
	 */
	$scope.featureHeaderList = null;
	
	$scope.featureText = null;
	
	$scope.featureList = [];
	
	/**
	 * GBK feature 테이블 페이징 핸들러
	 */
	$scope.pageHandler = PageHandler.create();
	
	/**
	 * 컨트롤러 초기화  
	 */
	$scope.init = function() {
		$scope.pageHandler.setPageChangeListener($scope.reloadFeatureTable);
	};
	
	$scope.hideNgsDataPopup = function() {
		$($scope.display.createPopupId).modal('hide');
		$($scope.display.editPopupId).modal('hide');
		$($scope.display.detailPopupId).modal('hide');
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
	
	/**
	 * 상세정보 보기 화면의 NGS 데이터 파일 목록 가시화 여부 반환
	 */
	$scope.isHideAchiveList = function() {
		return true;
	};
	
	/**
	 * 등록데이터 상제정보 보기 버튼(링크) 클릭 이벤트 처리
	 */
	$scope.onClickRegistDataDetail = function(item){
		waitingDialog.show("Loading ...");
		try{
			$scope.dataLabel = 'Nothing';
			var tabSize = 2;
			var popupId = "#raw-data-detail-popup";
			
			if(Utils.isRawData(item)) {
				tabSize = 2; 
				popupId = "#raw-data-detail-popup";
				$scope.dataLabel = 'raw data';
			} else if (Utils.isProcessedData(item)) {
				tabSize = 3; 
				popupId = "#processed-data-detail-popup";
				$scope.dataLabel = 'annotation data';
			} else {
				throw "Invalid ngs data registration";
			}
			
			// 아코디온 탭 개수 변경 
			var actives = $scope.display.accordions;
			if(actives.length < tabSize) {
				actives.push(false);
			} else if(actives.length > tabSize) {
				actives.splice(tabSize, (actives.length - tabSize));
			}
			
			$scope.model = angular.copy(item);
			
			_loadNgsDataLinkedList(item);
			_loadNgsDataAchiveList(item);
			_loadHierarchyList(item.taxonomy);
			
			$timeout(function() {
				waitingDialog.hide();
				$(popupId).modal('show');
			}, 300);
			
		}catch(exception){
			waitingDialog.hide();
			$log.error(exception);
			notificationService.error(exception.data);
		}
	};
	
	/**
	 * NGS 데이터파일 상세보기 버튼(링크) 이벤트 처리 
	 * 파일종류에 따라 팝업창 종류 변경 
	 * fasta 파일 : 기본정보, 서열기본정보
	 * fastq 파일 : 기본정보, 서열기본정보, 정량정보(QC)
	 * gff 파일 : 기본정보, Feature 정보
	 * gbk 파일 : 기본정보, 유전자/CDS 목록, 스트림 데이터
	 * etc 파일 : 기본정보 
	 */
	$scope.onClickNgsFileDetail = function(item) {
		$scope.model = angular.copy(item);
		$scope.dataLabel = Utils.isRawData(item.dataRegist) ? 'raw data' : 'annotation data';
		
		if(FileUtils.isFastaData(item)) {
			_loadSeqQuantity(item);
			_openNgsFilePopup('#ngs-data-file-popup');
		} else if(FileUtils.isFastqData(item)) {
			_loadSeqQuantity(item);
			_loadSeqQcResultList(item);
			_loadSeqQcSummaryList(item);
			
			_openNgsFilePopup('#ngs-data-file-popup');
		} else if(FileUtils.isGffData(item)) { // GView 다운로드
			
			var javawsInstalled = 0;
			var javaws142Installed = 0; 
			var javaws150Installed = 0; 
			var javaws160Installed = 0; 
			isIE = "false";
			if (navigator.mimeTypes && navigator.mimeTypes.length) {
				x = navigator.mimeTypes['application/x-java-jnlp-file'];
				if (x) {
					javawsInstalled = 1;
					javaws142Installed = 1; 
					javaws150Installed = 1; 
					javaws160Installed = 1; 
				}
			} else {
				isIE = "true";
			}
			// jre 깔려있음
			if(javawsInstalled || (navigator.userAgent.indexOf("Gecko") !=-1)){
				waitingDialog.hide();
				window.open("./gview.jsp?id=" + item.id);
			}else{
				notificationService.notice("It takes a little time because jre is not installed.")
				waitingDialog.hide();
				window.open("./gview.jsp?id=" + item.id);
//				window.open("https://download.java.net/openjdk/jdk7u75/ri/jdk_ri-7u75-b13-windows-i586-18_dec_2014.zip");
//				https://www.ctis.or.kr/multi/JRE/jre-7u2-windows-x64.exe
			}
			
		} else if(FileUtils.isGbkData(item)) {
			_loadGenbankHeader(item);
			_loadFeatureList(item);
			_loadGenbankPlainText(item);
			
			_openNgsFilePopup('#ngs-data-gbk-popup');
		} else {
			_openNgsFilePopup('#ngs-data-file-popup');
		}
	};
	
	$scope.hasSeqQc = function(item) {
		var achive = (angular.isDefined(item)) ? item : $scope.model;
		if(FileUtils.isFastqData(achive)) {
			return ($scope.qc.results.length > 0);
		}
		
		return false;
	};
	
	$scope.hasSeqQuantity = function(item) {
		var achive = (angular.isDefined(item)) ? item : $scope.model;
		if(FileUtils.isFastaData(achive) || FileUtils.isFastqData(achive)) {
			return angular.isObject($scope.quantity);
		}
		
		return false;
	};
	
	$scope.hasFeatures = function(item) {
		var achive = (angular.isDefined(item)) ? item : $scope.model;
		if(FileUtils.isGbkData(achive)) {
			return angular.isObject($scope.featureHeaderList);
		}
		
		return false;
	};
	
	$scope.reloadFeatureTable = function() {
		if(FileUtils.isGbkData($scope.model)) {
			_loadFeatureList($scope.model);
		}
	};
	
	$scope.$on('integrated-ngsdata', function(event, model) {
        $scope.model = model;
    });
	
	var _openNgsFilePopup = function(popupId) {
		$timeout(function() {
			waitingDialog.hide();
			$(popupId).modal('show');
		}, 300);
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
		
		$scope.linkedHandler.setItems([]);
		
		if(registId > 0) {
			depositFactory.getNgsDataLinkedList(registId).then(
	    		function(res) {
	    			waitingDialog.hide();
	    			$scope.linkedHandler.setItems(res.data);
	    		},
	    		
	    		function(err) {
	    			waitingDialog.hide();
	    			$log.error(angular.toJson(err, true));
	    		}
	    	);
		}
    };
    
    /**
     * NGS 등록 데이터 파일 목록 호출 
     */
    var _loadNgsDataAchiveList = function(regist) {
    	if(Utils.isBlank(regist))
			return $log.error("Could not load achive data - empty reigst id");
		
		var registId = regist; 
		if(angular.isObject(regist)) {
			registId = regist.id;
		}
		
		$scope.achiveHandler.setItems([]);
		
		if(registId > 0) {
			achiveFactory.getNgsDataAchiveListOfRegist(registId).then(
	    		function(res) {
	    			$scope.achiveHandler.setItems(res.data);
	    		},
	    		
	    		function(err) {
	    			$log.error(angular.toJson(err, true));
	    		}
	    	);
		}
    };
    
    /**
     * Taxonomy 계통목록 호출
     */
    var _loadHierarchyList = function(taxonomy) {
    	if(!angular.isObject(taxonomy) || (taxonomy['taxonId'] > 1) == false)
			return $log.error("Could not load taxonomy hierarchy list");
    	
    	$scope.hierarchyList = [];
    	
    	taxonFactory.getHierarchyList(taxonomy.taxonId).then(
			function(res) {
    			$scope.hierarchyList = res.data;
    		},
    		function(err) {
    			$log.error(angular.toJson(err, true));
    		}
    	);
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
    $scope.closePopup = function(){
    	$("#gbk-sequence-popup").modal('hide');
    }
    
    $scope.popupOpen = function(item){
    	$scope.detailHeader = item;
    	$("#gbk-sequence-popup").modal('show');
    }
    /**
     * GBK 파일 HEADER 정보 호출 
     */
    var _loadGenbankHeader = function(achive) {
    	if(!angular.isObject(achive) || (achive['id'] > 0) == false) {
			return $log.error("Could not load genbank header info.");
    	}
    	
    	$scope.featureHeaderList = null;
    	
    	achiveFactory.getGenbankHeaders(achive.id).then(
			function(result) {
    			$scope.featureHeaderList = result;
    			$scope.featureHeaderRowspan = $scope.featureHeaderList.length / 4; 
    		}
    	);
    };
    
    /**
     * GBK 파일 feature 목록 호출 
     */
    var _loadFeatureList = function(achive) {
    	if(!angular.isObject(achive) || (achive['id'] > 0) == false) {
    		waitingDialog.hide();
			return $log.error("Could not load genbank feature list");
    	}
    	
    	var params = {
    		achiveId : achive.id,
    		type : '',
    		keyword : '',
    		page : $scope.pageHandler.getCurrentPage(),
    	    rowSize : $scope.pageHandler.getNumberOfRows(),
    	};
    	
    	achiveFactory.getGenbankFeatureList(achive.id, params).then(
			function(result) {
				waitingDialog.hide();
				
    			$scope.featureList = result.list;
    			$scope.pageHandler.setTotal(result.total);
    			$scope.pageHandler.setCurrentPage(result.page);
    		}
    	);
    };
    
    /**
     * GBK 파일 Pain text 호출 
     */
    var _loadGenbankPlainText = function(achive) {
    	if(!angular.isObject(achive) || (achive['id'] > 0) == false) {
			return $log.error("Could not load genbank plain text");
    	}
    	
    	$scope.featureHeaderList = null;
    	
    	achiveFactory.getGbkFileAsPlainText(achive.id).then(
			function(res) {
				$scope.featureText = res.data.substr(0, 100000);
				if(res.data.length > 100000)
					$scope.featureText += "\n........";
    		}, 
    		function(err) {
    			$scope.featureText = err.data;
    		}
    	);
    };
    
});
