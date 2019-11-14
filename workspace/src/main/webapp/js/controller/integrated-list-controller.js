gdkmApp.controller('integratedListController', function($scope, $log, $timeout, $cacheFactory,
		notificationService, depositFactory, achiveFactory, taxonFactory, excelFactory){
	
	var _activeRows = [];
	
	var _dataType = null; 
	
	var _dataLabel = MESSAGE_SECTION.RAWDATA;
	
	var message = MESSAGE.create(_dataLabel);
	
	$scope.params = null;
	
	$scope.modelHandler = ModelListHandler.create($timeout);
	
	$scope.pageHandler = PageHandler.create();
	
	$scope.init = function(dataType){
		_dataType = dataType;
		_dataLabel = (Utils.isRawData(dataType))? MESSAGE_SECTION.RAWDATA : MESSAGE_SECTION.PROCESSED;
		
		var cache = Utils.getCache($cacheFactory);
		if(angular.isUndefined(cache)) {
			$scope.params = new SearchParams();
		} else {
			$scope.params = angular.copy(cache.get(CACHE_PROP.SEARCH_PARAMS));
			if(angular.isUndefined($scope.params))
				$scope.params = new SearchParams();
		}
		
		$scope.params.dataType = _dataType;
		$scope.params.registStatus = REGIST_STATUS.SUCCESS;
		$scope.params.openYn = ($scope.$parent.loginUser == null)? 'Y' : '';
		
		$scope.pageHandler.setPageChangeListener(_loadAchiveList);
		
		_loadAchiveList();
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
	
	$scope.toggleActiveRows = function() {
		if($scope.modelHandler.size() == 0)
			return;
		
		if(_activeRows.length == $scope.modelHandler.size()) {
			_activeRows = [];
		} else {
			_activeRows = $scope.modelHandler.getItemIdList();
		}
	};
	
	$scope.disabledLinearBtn = function() {
		var selectedList = $scope.modelHandler.getSelectedItems();
		if(selectedList.length == 0) 
			return true;
		
		for(var i=0; i < selectedList.length ; i++) {
			if(!FileUtils.isGffData(selectedList[i]))
				return true;
		}
		
		return false;
	};
	
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
	
	/**
	 * 
	 * JBrowse 호출
	 */
	$scope.openLinearBrowser = function(selectedList) {
		var selectedList = $scope.modelHandler.getSelectedItems();
		console.log(selectedList);
		
		if(selectedList.length == 0) {
			return notificationService.notice(message.NOTICE017_EN);
		}
		
		depositFactory.createJbrowseData(selectedList).then(
    		function(res) {
    			console.log(res);
    			var data = "gdkm/" + res.data.path;
    			var track = res.data.track;
    			waitingDialog.show("Loading ...");
    			setTimeout(function(){
    				waitingDialog.hide();
    				window.open("./JBrowse-1.12.3/?data="+ data+"&tracks=DNA%2C"+track, "jbrowse-"+data);
    			}, 2000);
    		},
    		function(err) {
    			notificationService.error(err.data);
    			console.log(err);
    		}
    	);
		
	};
	
	/* 등록 데이터 목록 검색 요청 */
	$scope.reload = function() {
		waitingDialog.show("Loading ...");
		_loadAchiveList();
	};
	
	$scope.$on('onDispatchSearchEvent', function(event, params) {
		if(!angular.isObject(params)) {
			return $log.debug(message.NOTICE017_EN);
		}
		
		var openYn = $scope.params.openYn;
		$scope.params = angular.copy(params);
		$scope.params.dataType = _dataType;
		$scope.params.openYn = openYn;
		
		if(params.dataType == PAGE_DATA.RAWDATA && $scope.params.dataType == PAGE_DATA.PROCESSED) {
			$scope.params.registId = -1;
		} else if(params.dataType == PAGE_DATA.PROCESSED && $scope.params.dataType == PAGE_DATA.RAWDATA) {
			$scope.params.onlyLinked = 'TRUE';
		}
		
		_loadAchiveList();
	});
	
    var _loadAchiveList = function() {
    	$scope.params.page = $scope.pageHandler.getCurrentPage();
    	$scope.params.rowSize = $scope.pageHandler.getNumberOfRows();
    	
    	achiveFactory.getNgsDataAchiveList($scope.params).then(
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
    
    $scope.downloadExcel = function(dataType, openYn){
		if($scope.sessionHandler.getProperty("USER")!=null){
			var params = {
				dataType: $scope.params.dataType,
				registStatus: $scope.params.registStatus,
				registFrom: $scope.params.registFrom,
				registTo: $scope.params.registTo,
				strain : $scope.params.strain,
				keyword : $scope.params.keyword
			}
		}else{
			var params = {
				dataType: $scope.params.dataType,
				openYn: openYn,
				registStatus: $scope.params.registStatus,
				registFrom: $scope.params.registFrom,
				registTo: $scope.params.registTo,
				strain : $scope.params.strain,
				keyword : $scope.params.keyword
			}
		}
		
		depositFactory.downloadExcel(params).then(
			function(res){
				var header = res.headers('Content-Disposition')
                var fileName = header.split("=")[1].replace(/\"/gi,'');
                console.log(fileName);
	 
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
				notificationService.error(err.data);
			}
		)
    }
	
});