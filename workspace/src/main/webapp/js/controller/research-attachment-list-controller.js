gdkmApp.controller('ResearchAttachmentListController', function($scope, $window, $timeout, notificationService, 
		ResearchAttachmentFactory, FileDownloadFactory) {
	
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
		$scope.pageHandler.setPageChangeListener(_loadAttachmentList);
	};
	
	$scope.download = function(attachment) {
		if(!angular.isObject(attachment) || attachment['id'] < 1)
			return;
		
		FileDownloadFactory.doDownloadResearchAttachment(attachment);
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
		
		if(confirm("Are you sure delete selected attachment(s)?")) {
			waitingDialog.show("Removing ...");
		
			ResearchAttachmentFactory.doBatchProcess(_research, selectedList, 'remove').then(
				function(res) {
					if(res.data.rowSize > 0) {
						notificationService.success("Success to remove " + $scope.abbreviate(res.data.list));
						_loadAttachmentList()
					} else {
						notificationService.error("Failed to remove attachment(s)");
						waitingDialog.hide();
					}
				}, 
				function(err) {
					waitingDialog.hide();
					notificationService.error("Failed to remove attachment(s)-" + err.data);
				}
			);
		}
	};
	
	/**
	 * 성과물 첨부파일 등록 팝업 활성화 요청
	 */
	$scope.$on('loadResearchAttachmentList', function(event, research) {
		if(!angular.isObject(research) || Utils.isBlank(research['id']))
			return;
		
		_research = research;
		_loadAttachmentList();
	});
	
	function _loadAttachmentList() {		
		$scope.params.page = $scope.pageHandler.getCurrentPage();
    	$scope.params.rowSize = $scope.pageHandler.getNumberOfRows();
    	
		ResearchAttachmentFactory.getAttachmentList(_research, $scope.params).then(
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