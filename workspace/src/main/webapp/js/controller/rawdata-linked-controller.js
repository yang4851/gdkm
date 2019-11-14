gdkmApp.controller('linkedController', function($scope, $log, $timeout, 
		depositFactory, notificationService) {
	
	var _dataLabel = MESSAGE_SECTION.RAWDATA
	var message = MESSAGE.create(_dataLabel);
	
	$scope.params = {
		dataType: PAGE_DATA.RAWDATA,
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
		$scope.params = {
				dataType: PAGE_DATA.RAWDATA,
				openYn: '',
				registStatus: REGIST_STATUS.SUCCESS,
				registFrom: null,
				registTo: null,
				fields : '',
				keyword : '',		
				page: 0,
				rowSize: 10,
		};
		_loadNgsDataRegist();
		$scope.modelHandler.selectedIdList = '';
		$scope.pageHandler.setPageChangeListener(_loadNgsDataRegist);
	}
	
	/* 등록 데이터 목록 검색 요청 */
	$scope.search = function() {
		waitingDialog.show("Loading ...");
		_loadNgsDataRegist();
		$scope.modelHandler.selectedIdList = '';
	};
	
	$scope.select = function() {
		var selectedList = $scope.modelHandler.getSelectedItems();
		if(selectedList.length == 0) {
			return notificationService.notice(message.NOTICE017_EN);
//			return notificationService.notice("No selected raw data");
		}
		
		var taxonomy = selectedList[0].taxonomy;
		var strain = selectedList[0].strain;
		
		for(var i=1 ; i < selectedList.length ; i++) {
			if(taxonomy.taxonId != selectedList[i].taxonomy.taxonId) {
				return notificationService.notice("Taxonomy does not match!");
			}
			
			if(strain != selectedList[i].strain) {
				return notificationService.notice("Strain does not match!");
			}
		}
		
		$scope.init();
		$scope.$emit("onSelectLinkedData", selectedList);
		$("#rawdata-search-popup").modal('hide');
	};
	
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
    			notificationService.error(err.data);
    			$log.error(angular.toJson(err, true));
    		}
    	);
    };
})