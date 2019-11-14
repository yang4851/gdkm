gdkmApp.controller('codeGroupController',function($scope, $rootScope, $location, $window, $timeout, $log, codeGroupFactory, notificationService){
	
	$scope.searchHandler = SearchHandler.create($timeout);
	$scope.pageHandler = PageHandler.create();
	$scope.modelHandler = ModelListHandler.create($timeout);
	
	$scope.createForm = ModelForm.create($timeout);
	$scope.modelForm = ModelForm.create($timeout);
	
	$scope.idCheckUrl = Utils.getContextURL() + "/codeGroups/check/id";
	var _dataLabel = MESSAGE_SECTION.CODEGROUP_KR;
	var message = MESSAGE.create(_dataLabel);
	
	$scope.init = function(){
		if(Utils.isBlank($scope.loginUser)){
			$scope.validateLoginAuthority(err);
		}
		$scope.searchHandler.initialize($scope.sessionHandler.getProperty('searchHandler'));
		$scope.pageHandler.initialize($scope.sessionHandler.getProperty('pageHandler'));
		$scope.modelHandler.initialize($scope.sessionHandler.getProperty('modelHandler'));
		
		$scope.searchHandler.setSearchListener(_loadCodeGroupList);
		$scope.pageHandler.setPageChangeListener(_loadCodeGroupList);
		
		$scope.modelForm.setModel(CodeGroup());
		$scope.modelForm.setUpdateCallback(_updateCodeGroup);
		$scope.createForm.setModel(CodeGroup());
		$scope.createForm.setSubmitCallback(_createCodeGroup);
		$scope.selectUseYn = [{value : "Y"}, {value : "N"}]; 
		
		_loadCodeGroupList();
	}
	
	$scope.onClickDetailLink = function(codeGroup){
		if(Utils.isBlank(codeGroup) || codeGroup.codeGroup <= 0){
			notificationService.error(message.NOTICE021_KR);
//			notificationService.error("선택된 정보가 잘 못 되었습니다.");
			return;
		}
		waitingDialog.show('Loading...');
		codeGroupFactory.getCodeGroup(codeGroup.codeGroup).then(
			function(res){
				waitingDialog.hide();
				$scope.modelForm.setModel(res.data);
				$('#code-group-edit-popup').modal('show');
			},
			function(err){
				$log.error(err);
				notificationService.error(err.data);
				waitingDialog.hide();
				if(err.status && err.status == (401 & 403)) {
					$timeout(function() {
						$scope.validateLoginAuthority(err);
						$location.url("/");
					}, 500);
				}
			}
			
		)
	}
	
	$scope.onClickDelete = function(){
		var codeGroupList = $scope.modelHandler.getSelectedItems();
		var answer = "";
		if(codeGroupList.length < 1){
			notificationService.error(message.NOTICE014_KR);
//			alert("선택된 그룹코드가 없습니다.")
			return;
		}else{
			answer = confirm("선택하신 그룹코드를 삭제하시겠습니까?");
		}
		
		if(answer==true){
			var codeGroupId = [];
			for(var idx in codeGroupList){
				codeGroupId.push(codeGroupList[idx].codeGroup);
			}
			waitingDialog.show('Delete...');
			codeGroupFactory.deleteCodeGroup(codeGroupId).then(
				function(res){
					notificationService.success(message.NOTICE004_KR);
//					notificationService.success("요청하신 그룹코드 "+ res.data + "건이 삭제되었습니다.");
					waitingDialog.hide();
					_loadCodeGroupList();
				},
				function(err){
					notificationService.error(err.data);
					waitingDialog.hide();
					
					if(err.status && err.status == (401 & 403)){
						$timeout(function(){
							$scope.validateLoginAuthority(err);
							$location.url("/login");
						}, 500);
					}
				}
			)
		}
	}
	
	$scope.selectCodeGroup = function(item){
		$rootScope.$emit('selectCodeGroupByCode', item);
		$('#code-group-list-popup').modal('hide');
	}
	
	_createCodeGroup= function(codeGroup) {
		codeGroupFactory.createCodeGroup(codeGroup).then(
			function(res) {
				$scope.createForm.reset();
				$('#code-group-create-popup').modal('hide');
				notificationService.success(message.NOTICE001_KR);
//				notificationService.success("그룹코드 정보가 정상적으로 생성되었습니다.");
				_loadCodeGroupList();
			}, 
			function(err) {
				$log.error(err);
				notificationService.error(err.data);
				waitingDialog.hide();
				if(err.status && err.status == (401 & 403)) {
					$timeout(function() {
						$scope.validateLoginAuthority(err);
						$location.url("/login");
					}, 500);
				}else{
					_loadCodeGroupList();
				}
				
			}
		);
	};
	
	$scope.reload = function() {
		waitingDialog.show("Loading ...");
		_loadCodeGroupList();
	};
	
	_loadCodeGroupList = function(){
		var params = {
			page : $scope.pageHandler.getCurrentPage()
			,rowSize : $scope.pageHandler.numberOfRows
			,codeGroup : $scope.searchHandler.codeGroup.id
			,name : $scope.searchHandler.codeGroup.name 
			,useYn : $scope.searchHandler.codeGroup.useYn 
		}
		codeGroupFactory.getCodeGroupList(params).then(
			function(res){
				waitingDialog.hide();
				$scope.modelHandler.setItems(res.data.list);
				$scope.pageHandler.setTotal(res.data.total);
			},
			function(err){
				waitingDialog.hide();
				notificationService.error(err.data);
				if(err.status == (401 & 403)){
					$timeout(function(){
						$scope.validateLoginAuthority(err);
					}, 500);
				}
				if(err.status == 404){
					console.log("err");
				}
			}
		)
	}
	
	_updateCodeGroup = function(codeGroup){
		codeGroupFactory.updateCodeGroup(codeGroup).then(
			function(res){
				waitingDialog.hide();
				$('#code-group-edit-popup').modal('hide');
				notificationService.success(message.NOTICE002_KR);
//				notificationService.success("그룹코드 정보가 정상적으로 수정되었습니다.");
				$scope.init();
				_loadCodeGroupList();
			},
			function(){
				$log.error(err);
				notificationService.error(err.data);
				waitingDialog.hide();
				if(err.status && err.status == (401 & 403)) {
					$timeout(function() {
						$scope.validateLoginAuthority(err);
						$location.url("/login");
					}, 500);
				}else{
					_loadCodeGroupList();
				}
			}
		)
	}
	
	$scope.resetGroupcodeForm = function(codeGroup){
		$scope.modelForm.reset();
		$scope.modelForm.model.codeGroup = codeGroup;
	}

});