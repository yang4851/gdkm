gdkmApp.controller('ProcessedController', function($scope, $timeout, $window, notificationService, 
		depositFactory, achiveFactory, taxonFactory) {
	
	var message = MESSAGE.create(MESSAGE_SECTION.PROCESSED);

	$scope.display = {
		createPopupId : '',
		editPopupId : '',
		detailPopupId : '',
		accordions : [true, true, true, true],
	};
	
	/**
	 * 생성,수정을 위한 사본 객체
	 */
	$scope.model = null;
	
	/**
	 * NGS 파일 목록 
	 */
	$scope.achiveList = [];
	
	/**
	 * Taxonomy 계통구조 목록 
	 */
	$scope.hierarchyList = [];
	
	/**
	 * NGS 파일 테이블 이벤트 처리 핸들러
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
		reportHtml : '',
	};
	/**
	 * ng-show의 값
	 */
	$scope.isVisible = false;
	
	/**
	 * NGS 등록데이터($scope.model)가 등록완료 상태이면 true 반환
	 */
	$scope.isFinishedDeposit = function(data) {
		return false;
	};
	
	/**
	 * NGS 등록데이터($scope.model)가 제출 상태이면 true 반환
	 */
	$scope.isSubmitDeposit = function(data) {
		return false;
	};
	
	/**
	 * NGS 등록데이터($scope.model)가 수정 가능한 상태이면 true 반환
	 */
	$scope.isEditableDeposit = function(data) {
		return false;
	};
	
	$scope.isHideAchiveList = function(data) {
		return false;
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
	
	$scope.isRegistSection = function() {
		return false;
	};
	/**
	 * section이 review이면서
	 * seq를 가지고 있는item만 true
	 */
	$scope.isReviewSection = function(item) {
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
	
	$scope.$on('openProcessedDetailPopup', function(event, regist) {
		if(!angular.isObject(regist) || regist.id < 1)
    		return;
		
		depositFactory.getNgsDataRegistInfo(regist.id).then(
			function(res) {
				$scope.model = res.data;
				_loadNgsDataAchiveList($scope.model);
				_loadHierarchyList($scope.model.taxonomy);
				
				$('#processed-data-detail-popup').modal('show');
			}, 
			function(err) {
				console.info(err);
			}
		);
	});
	
    var _loadNgsDataAchiveList = function(regist) {
    	if(Utils.isBlank(regist))
			return console.error("Could not load achive data - empty reigst id");
		
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
	    			console.error(angular.toJson(err, true));
	    		}
	    	);
		}
    };
    
    var _loadHierarchyList = function(taxonomy) {
    	$scope.hierarchyList = [];
    	
    	if(!angular.isObject(taxonomy) || (taxonomy['taxonId'] > 1) == false)
			return console.error("Could not load taxonomy hierarchy list");
    	
    	taxonFactory.getHierarchyList(taxonomy.taxonId).then(
			function(res) {
    			$scope.hierarchyList = res.data;
    		},
    		
    		function(err) {
    			console.error(angular.toJson(err, true));
    		}
    	);
    };
})