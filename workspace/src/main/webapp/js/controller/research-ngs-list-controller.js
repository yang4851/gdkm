gdkmApp.controller('ResearchNgsListController', function($scope, $window, $timeout, notificationService, 
		ResearchNgsDataFactory, FileDownloadFactory) {
	
	var _research = null;
	
	$scope.params = {
		fields : '',
		keyword : '',
		page: 0,
		rowSize: 10
	};
	
	$scope.modelHandler = ModelListHandler.create($timeout);
	
	$scope.pageHandler = PageHandler.create();
	
	$scope.init = function() {
		$scope.pageHandler.setPageChangeListener(_loadNgsDataList);
	};
	
	$scope.isDeleteBtnDisabled = function() {
		var selectedList = $scope.modelHandler.getSelectedItems();
		return (selectedList.length == 0);
	};
	
	$scope.deleteSelectedList = function() {
		var selectedList = $scope.modelHandler.getSelectedItems();
		if(selectedList.length == 0) {
			return notificationService.notice($scope.message.NOTICE017_EN);
		}
		
		if(confirm("Are you sure delete selected NGS data?")) {
			waitingDialog.show("Removing ...");
		
			ResearchNgsDataFactory.doBatchProcess(_research, selectedList, 'remove').then(
				function(res) {
					if(res.data.rowSize > 0) {
						notificationService.success("Success to remove " + $scope.abbreviate(res.data.list));
						_loadNgsDataList()
					} else {
						notificationService.error("Failed to remove NGS data");
						waitingDialog.hide();
					}
				}, 
				function(err) {
					waitingDialog.hide();
					notificationService.error("Failed to remove NGS data-" + err.data);
				}
			);
		}
	};
	
	/**
	 * 성과물 첨부파일 등록 팝업 활성화 요청
	 */
	$scope.$on('loadResearchNgsDataList', function(event, research) {
		if(!angular.isObject(research) || Utils.isBlank(research['id']))
			return;
		
		_research = research;
		_loadNgsDataList();
	});
	
	$scope.$on('onSelectNgsDataList', function(event, selectedList) {
		var ngsList = $scope.modelHandler.getItems();
		
		var appendList = selectedList.filter(function(model) { 
			return (ngsList.findIndex(function(ngs) { return (ngs.id == model.id); }) < 0);
		});
		
		console.info(_research);
		
		if(appendList.length > 0) {
			ResearchNgsDataFactory.doBatchProcess(_research, appendList, 'append').then(
				function(res) {
					_loadNgsDataList();
				}, 
				function(err) {
					console.info(err);
				}
			);
		}
	});
	
	function _loadNgsDataList() {		
		$scope.params.page = $scope.pageHandler.getCurrentPage();
    	$scope.params.rowSize = $scope.pageHandler.getNumberOfRows();
    	
		ResearchNgsDataFactory.getNgsDataList(_research, $scope.params).then(
			function(res) {
				waitingDialog.hide();
				
				$scope.modelHandler.setItems(res.data.list);
    			$scope.pageHandler.setTotal(res.data.total);
    			$scope.pageHandler.setCurrentPage(res.data.page);
				
				$timeout(init_uicomponents, 500);
			}, 
			function(err) {
				waitingDialog.hide();
				
				notificationService.error(angular.toJson(err.data));
				$scope.validateLoginAuthority(err);
			}
		);
	}
});