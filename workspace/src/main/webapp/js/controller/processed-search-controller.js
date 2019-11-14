gdkmApp.controller('ProcessedSearchController', function($scope, $timeout, 
		depositFactory, notificationService) {
	
	var message = MESSAGE.create("Annotation data");
	
	var callbackListener = null;
	
	$scope.params = {
		dataType: PAGE_DATA.PROCESSED,
		openYn: '',
		registStatus: REGIST_STATUS.SUCCESS,
		registFrom: null,
		registTo: null,
		fields : '',
		keyword : '',		
		page: 0,
		rowSize: 10,
	};
	
	$scope.modelHandler = ModelListHandler.create($timeout);
	
	$scope.pageHandler = PageHandler.create();
	
	$scope.init = function() {
		$scope.modelHandler.selectedIdList = [];
		$scope.pageHandler.setPageChangeListener(_loadNgsDataRegist);
		
		_loadNgsDataRegist();
	}
	
	/* 등록 데이터 목록 검색 요청 */
	$scope.search = function() {
		waitingDialog.show("Loading ...");
		_loadNgsDataRegist();
	};
	
	$scope.select = function() {
		var selectedList = $scope.modelHandler.getSelectedItems();
		if(selectedList.length == 0) {
			return notificationService.notice(message.NOTICE017_EN);
		}
		
		if(angular.isFunction(callbackListener))
			callbackListener(selectedList);
		
		$("#processed-search-popup").modal('hide');
	};
	
	$scope.$on('openProcessedSearchPopup', function(event, callback) {
		if(!angular.isFunction(callback))
			return notificationService.notice("Invalid callback funcation");
		
		callbackListener = callback;
		
		$scope.modelHandler.selectedIdList = [];
		$("#processed-search-popup").modal('show');
	});
	
    function _loadNgsDataRegist() {
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
    			notificationService.error(err.data);
    			console.error(angular.toJson(err, true));
    		}
    	);
    };
})