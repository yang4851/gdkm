gdkmApp.controller('ResearchAttachmentController', function($scope, $window, $timeout, notificationService, 
		ResearchAttachmentFactory, FileUploadFactory, FileDownloadFactory, Upload) {
	
	$scope.research = null;
	
	/**
	 * 원본 성과물 객체
	 */
	$scope.source = null;
	
	/**
	 * 생성과 수정을 위한 사본 객체
	 */
	$scope.model = null;

	$scope.uploadFile = null;
	
	$scope.uploadHandler = UploadHandler.create($window, $timeout, Upload);
	
	$scope.init = function() {
		$scope.uploadHandler.setRequestUrl(Utils.getContextPath() + "/temporaries");
		$scope.uploadHandler.addUploadListener(onUploadTempFile);
	};
	
	$scope.isEditable = function() {
		if($scope.section != PAGE_SECTION.REGIST)
			return false;
		
		if(!angular.isObject($scope.research) || Utils.isBlank($scope.research['id']))
			return false;
		
		return ($scope.research.openYn != 'Y');
	};
	
	$scope.isSubmitDisabled = function() {
		if($scope.uploadFile != null)
			return true;
		
		if(!angular.isObject($scope.research) || Utils.isBlank($scope.research['id']))
			return true;
		
		if(!angular.isObject($scope.model) || Utils.isBlank($scope.model['name']))
			return true;
			
		return false;
	};
	
	$scope.isUploadable = function() {
		if(!angular.isObject($scope.research) || Utils.isBlank($scope.research['id']))
			return false;
		
		if(!angular.isObject($scope.model) || Utils.isNotBlank($scope.model['name']))
			return false;
		
		return ($scope.uploadFile == null);
	};
	
	$scope.isUploading = function() {
		if(!angular.isObject($scope.research) || Utils.isBlank($scope.research['id']))
			return false;
		
		return ($scope.uploadFile != null);
	};

	$scope.isUploaded = function() {
		if($scope.uploadFile != null) 
			return false;
		
		if(!angular.isObject($scope.research) || Utils.isBlank($scope.research['id']))
			return false;
		
		if(!angular.isObject($scope.model) || Utils.isBlank($scope.model['name']))
			return false;
		
		return true;
	};
	
	$scope.isRegistered = function() {
		return (angular.isObject($scope.model)
				&& Utils.isNotBlank($scope.model['id'])
				&& $scope.model.id > 0);
	};
	
	/**
	 * 등록|수정 폼 초기화
	 */
	$scope.reset = function() {
		if($scope.source == null) { 
			$scope.model.description = '';
			$scope.model.research = $scope.research;
		} else {
			$scope.model = angular.copy($scope.source);
		}
	};
	
	/**
	 * 성과물 정보 저장 서버 요청 
	 */
	$scope.submit = function() {
		if(Utils.isBlank($scope.model.name))
			return notificationService.notice("File is required.");
		
		waitingDialog.show("Saving ...");
		var deferred = ($scope.model.id > 0) ? ResearchAttachmentFactory.changeAttachment($scope.research, $scope.model) : 
							ResearchAttachmentFactory.createAttachment($scope.research, $scope.model);
		deferred.then(
			function(res) {
				notificationService.success($scope.message.NOTICE001_EN + "-" + res.data.registNo);
				
				$scope.$emit('onChangeResearchAttachment', res.data);
				$scope.source = res.data;
				$scope.model = angular.copy(res.data);
				
				waitingDialog.hide();
				$('#research-attachment-popup').modal('hide');
			}, 
			function(err) {
				waitingDialog.hide();
				
				notificationService.notice(angular.toJson(err.data));
    			$scope.validateLoginAuthority(err);
			}
		);
	};
	
	$scope.download = function(file) {
		if($scope.isRegistered()) {
			FileDownloadFactory.doDownloadResearchAttachment($scope.model);
		} else {
			FileUploadFactory.doDownloadTempFile(file);
		}
	};
	
	$scope.removeFile = function(file) {
		if(!confirm("Are you sure delete file?\nDeleted file cannot be recovered."))
			return;
		
		waitingDialog.show("Removing ...");
		FileUploadFactory.doRemoveTempFile(file).then(
			function(res){
				$scope.model.name = '';
				$scope.model.type = '';
				$scope.model.size = 0;
				
				waitingDialog.hide();
				console.debug(res);
			},
			function(err){
				$scope.model.name = '';
				$scope.model.type = '';
				$scope.model.size = 0;
				
				waitingDialog.hide();
				console.debug(err);
			}
		);
	};
	
	/**
	 * 성과물 첨부파일 등록 팝업 활성화 요청
	 */
	$scope.$on('openResearchAttachmentPopup', function(event, research, attachment) {
		if(!angular.isObject(research) || Utils.isBlank(research['id']))
			return;
		
		$scope.uploadHandler.clear();
		$scope.research = research;
		
		if(!angular.isObject(attachment) || Utils.isBlank(attachment['id'])) {
			$scope.source = null;
			$scope.model = ResearchAttachment();
			$scope.model.research = $scope.research; 
		} else {
			$scope.source = attachment;
			$scope.model = angular.copy(attachment);
		}
		
		$('#research-attachment-popup').modal('show');
	});
	
	function loadResearchAttachment(research, attachment) {
		ResearchAttachmentFactory.getAttachment(research, attachment.id).then(
			function(res) {
				waitingDialog.hide();
				
				$scope.source = res.data;
				$scope.model = angular.copy(res.data);
				
				openAttachmentPopup(popup);
				$scope.$broadcast("onLoadResearchData", PAGE_DATA.RESEARCH, res.data);
			}, 
			function(err) {
				waitingDialog.hide();
				
				notificationService.notice(angular.toJson(err.data));
    			$scope.validateLoginAuthority(err);
			}
		);
	}
	
	function onUploadTempFile(state, file, data) {
		if(UPLOAD_STATE.FAIL == state) {
			$scope.uploadFile = null;
		} else if(UPLOAD_STATE.PENDING == state) {
			$scope.uploadFile = file;
		} else if(UPLOAD_STATE.COMPLETE == state) {
			$scope.uploadFile = null;
			$scope.model.name = data.name;
			$scope.model.type = ContentTypeUtils.getType(data.type);
			$scope.model.size = data.size;
			$scope.model.path = data.path;
		}
	}
});