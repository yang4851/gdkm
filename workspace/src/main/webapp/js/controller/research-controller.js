gdkmApp.controller('ResearchController', function($scope, $timeout, notificationService, 
		ResearchFactory, ResearchAttachmentFactory, FileDownloadFactory) {
	
	const _popupIds = [
		'#research-create-popup',
		'#research-modify-popup',
		'#research-detail-popup'
	];
	
	$scope.display = { 
		accordions : [ true, true, true, true ],
	}; 
	
	$scope.message = MESSAGE.create(MESSAGE_SECTION.RESEARCH);
	
	$scope.section = PAGE_SECTION.REGIST;
	
	/**
	 * 원본 성과물 객체
	 */
	$scope.source = null;
	
	/**
	 * 생성과 수정을 위한 사본 객체
	 */
	$scope.model = null;
	
	$scope.init = function(section) {
		$scope.section = section;
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
	
	$scope.openAttachmentPopup = function(attachment) {
		$scope.$broadcast('openResearchAttachmentPopup', $scope.model, attachment);
	};
	
	$scope.openOmicsPopup = function(omics) {
		$scope.$broadcast('openResearchOmicsPopup', $scope.model, omics);
	};
	
	$scope.openNgsDataDetailPopup = function(regist) {
		$scope.$broadcast('openProcessedDetailPopup', regist);
	};
	
	$scope.openNgsDataSearchPopup = function() {
		$scope.$broadcast('openProcessedSearchPopup', function(list) {
			if(!angular.isArray(list) || list.length == 0) 
				return;
			
			$scope.$broadcast('onSelectNgsDataList', list);
		});
	};
	
	$scope.isEditable = function() {
		if(!angular.isObject($scope.source) || $scope.source.id < 1)
			return false;
		
		return ($scope.source.openYn != 'Y');
	};
	
	$scope.reset = function() {
		if($scope.source == null) { 
			$scope.model = Research();
		} else {
			$scope.model = angular.copy($scope.source);
		}
	};
	
	$scope.submit = function() {
		if(Utils.isBlank($scope.model.title))
			return notificationService.notice("Title is required.");
		
		if(Utils.isBlank($scope.model.submitter))
			return notificationService.notice("Submitter is required.");
		
		if(Utils.isBlank($scope.model.submitOrgan))
			return notificationService.notice("Submit organization is required.");
		
		if(Utils.isBlank($scope.model.content))
			return notificationService.notice("Abstract is required.");
		
		var deferred = ($scope.model.id > 0) ? ResearchFactory.changeResearch($scope.model) : 
							ResearchFactory.createResearch($scope.model);
		deferred.then(
			function(res) {
				waitingDialog.hide();
				notificationService.success($scope.message.NOTICE001_EN + "-" + res.data.registNo);
				
				$scope.$broadcast('onChangeResearch', res.data);
				$scope.$broadcast('openDetailPopup', PAGE_DATA.RESEARCH, res.data);
			}, 
			function(err) {
				waitingDialog.hide();
				
				notificationService.notice(angular.toJson(err.data));
    			$scope.validateLoginAuthority(err);
			}
		);
	};
	
	$scope.remove = function() {
		if(!$scope.isEditable())
			return;
		
		if(!confirm("Are you sure remove this data?"))
			return;
		
		if(!angular.isObject($scope.model) || $scope.model.id < 1) {
			return notificationService.notice($scope.message.NOTICE014_EN);
		}
		
		waitingDialog.show("Deleting ...");
		ResearchFactory.deleteResearch($scope.model).then(
			function(res) {
				waitingDialog.hide();
				
				notificationService.success($scope.message.NOTICE004_EN + res.data.registNo);
				$scope.$broadcast("onChangeResearch", res.data);
				closeResearchPopup();
			}, 
			function(err) {
				waitingDialog.hide();
				notificationService.notice(angular.toJson(err.data));
				$scope.validateLoginAuthority(err);
			}
		);
	};
	
	$scope.moveToEdit = function() {
		if($scope.isEditable()) {
			$scope.$broadcast('openChangePopup', PAGE_DATA.RESEARCH, $scope.source);
		}
	};
	
	$scope.moveToDetail = function() {
		if(angular.isObject($scope.source) && $scope.source.id > 0) {
			$scope.$broadcast('openDetailPopup', PAGE_DATA.RESEARCH, $scope.source);
		}
	};
	
	$scope.$on('openCreatePopup', function(event, type) {
		if($scope.section != PAGE_SECTION.REGIST)
			return;
		
		if(type != PAGE_DATA.RESEARCH)
			return;
		
		$scope.source = null; 
		$scope.model = Research();
		
		openResearchPopup(0);
	});
	
	$scope.$on('openChangePopup', function(event, type, data) {
		if($scope.section != PAGE_SECTION.REGIST)
			return;
		
		if(type != PAGE_DATA.RESEARCH)
			return;
		
		if(!angular.isObject(data) || Utils.isBlank(data['id']))
			return;
		
		waitingDialog.show("Loading ...");
		loadResearchModel(data.id, 1);
	});
	
	$scope.$on('openDetailPopup', function(event, type, data) {
		if(type != PAGE_DATA.RESEARCH)
			return;
		
		if(!angular.isObject(data) || Utils.isBlank(data['id']))
			return;
		
		waitingDialog.show("Loading ...");
		loadResearchModel(data.id, 2);
	});
	
	$scope.$on('onChangeResearchAttachment', function(event, attachment) {
		$scope.$broadcast('loadResearchAttachmentList', $scope.model);
	});
	
	$scope.$on('onChangeResearchOmics', function(event, attachment) {
		$scope.$broadcast('loadResearchOmicsList', $scope.model);
	});
	
	function loadResearchModel(researchId, popup) {
		ResearchFactory.getResearch(researchId).then(
			function(res) {
				waitingDialog.hide();
				
				$scope.source = res.data;
				$scope.model = angular.copy(res.data);
				
				$scope.$broadcast('loadResearchAttachmentList', $scope.model);
				$scope.$broadcast('loadResearchOmicsList', $scope.model);
				$scope.$broadcast('loadResearchNgsDataList', $scope.model);
				
				openResearchPopup(popup);
			}, 
			function(err) {
				waitingDialog.hide();
				
				notificationService.error(angular.toJson(err.data));
    			$scope.validateLoginAuthority(err);
			}
		);
	}
	
	/**
	 * 성과물 생성,수정,상세 팝업 활성화 
	 */
	function openResearchPopup(idx) {
		for(let i=0; i < _popupIds.length ; i++) {
			if(i != idx)
				$(_popupIds[i]).modal('hide');
		}
		
		if(idx < _popupIds.length && idx > -1)
			$(_popupIds[idx]).modal('show');
	}
	
	function closeResearchPopup() {
		for(let i=0; i < _popupIds.length ; i++) {
			$(_popupIds[i]).modal('hide');
		}
	}
});