gdkmApp.controller('ResearchListController', function($scope, $timeout, notificationService, 
		ResearchFactory) {
	
	/** 
	 * 테이블의 펼쳐진 데이터 목록
	 */ 
	var _activeRows = [];
	
	/**
	 * 성과물 목록 조회 조건
	 */ 
	$scope.params = {
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
	
	$scope.init = function(section) {
		if(section == PAGE_SECTION.PUBLIC) {
			$scope.params.openYn = 'Y';
			$scope.params.registStatus = REGIST_STATUS.SUCCESS;
		}
		
		$scope.params.section = section;
		$scope.pageHandler.setPageChangeListener(_loadResearchList);
		
		_loadResearchList();
	}
	
	/**
	 * 테이블 헤더의 전체 몸록 펼침/접기 아이콘 스타일 변경  
	 */
	$scope.toggle_panel_css = function() {
		let size = $scope.modelHandler.size();
		return (size == 0 || _activeRows.length < size)? '' : 'active'; 
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
	
	/**
	 * 공개버튼 비활성화 상태 반환
	 * 등록상태가 승인(success)상태이면서 공개이전 목록이 선택된 경우 비활성화 시킴
	 */
	$scope.isOpenBtnDisabled = function() {
		var selectedList = $scope.modelHandler.getSelectedItems();
		if(selectedList.length == 0)
			return true;
		
//		for(let i=0; i < selectedList.length ; i++) {
//			if(selectedList[i].registStatus != REGIST_STATUS.SUCCESS)
//				return true;
//		}
		
		return false;
	};
	
	/**
	 * 선택된 NGS 데이터 공개 상태 변경 요청
	 */
	$scope.changeOpenStatus = function() {
		var selectedList = $scope.modelHandler.getSelectedItems();
		if(selectedList.length == 0) {
			return notificationService.notice($scope.message.NOTICE017_EN);
		}
		
		if(confirm("Are you sure change open state of data")) {
			var list = [];
			for(var i=0 ; i < selectedList.length ; i++) {
				list.push(angular.copy(selectedList[i]));
			}
			
			waitingDialog.show("Changing ...");
			ResearchFactory.doBatchProcess(list, 'open').then(
				function(res) {
					if(res.data.rowSize > 0) {
						notificationService.success("Success to change open state of " + $scope.abbreviate(res.data.list));
						_loadResearchList()
					} else {
						notificationService.notice("Failed to change open state");
						waitingDialog.hide();
					}
				}, 
				function(err) {
					waitingDialog.hide();
					notificationService.error("Failed to change open state-" + err.data);
				}
			);
		}
	};
	
	/**
	 * 엑셀로 내려받기 버튼 비활성화 상태 확인
	 */
	$scope.isDownloadBtnDisabled = function() {
		return ($scope.modelHandler.size() == 0);
	};
	
	$scope.search = function() {
		if(Utils.isNotBlank($scope.params.registFrom) && 
				Utils.isNotBlank($scope.params.registTo)) {
			if($scope.params.registFrom > $scope.params.registTo) 
				return notificationService.error("Incorrect search condition - Invalid registration period.");
		}
		
		waitingDialog.show("Loading ...");
		_loadResearchList()
	};
	
	$scope.showCreatePopup = function() {
		$scope.$emit('openCreatePopup', PAGE_DATA.RESEARCH);
	};
	
	$scope.showDetailPopup = function(model) {
		$scope.$emit('openDetailPopup', PAGE_DATA.RESEARCH, model);
	};
	
	$scope.$on('onChangeResearch', function(event, data) {
		if(angular.isObject(data))
			_loadResearchList();
	});
	
	function _loadResearchList() {
		$scope.params.page = $scope.pageHandler.getCurrentPage();
    	$scope.params.rowSize = $scope.pageHandler.getNumberOfRows();
    	
    	ResearchFactory.getResearchList($scope.params).then(
    		function(res) {
    			waitingDialog.hide();
    			
    			$scope.modelHandler.setItems(res.data.list);
    			$scope.pageHandler.setTotal(res.data.total);
    			$scope.pageHandler.setCurrentPage(res.data.page);
    			
    			$timeout(init_uicomponents, 500);
    		}, 
    		function(err) {
    			waitingDialog.hide();
    			
    			notificationService.notice(angular.toJson(err.data));
    			$scope.validateLoginAuthority(err);
    		}
    	);
	};
});