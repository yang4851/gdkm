gdkmApp.controller('userController',function($scope, $location, $window, $timeout, $log, userFactory, notificationService){
	
	$scope.searchHandler = SearchHandler.create($timeout);
	$scope.pageHandler = PageHandler.create();
	$scope.modelHandler = ModelListHandler.create($timeout);
	
	$scope.createForm = ModelForm.create($timeout);
	$scope.modelForm = ModelForm.create($timeout);
	
	$scope.idCheckUrl = Utils.getContextURL() + "/users/check/id";
	$scope.pwCheckUrl = Utils.getContextURL() + "/users/check/pw";
	
	var message = MESSAGE.create(MESSAGE_SECTION.USER_KR + '정보');
	
	/* 테이블 상세 레코드 펼침/닫힘 UI 처리 함수  */
	$scope.toggle_panel_css = function() {
		if($scope.modelHandler.size() == 0) 
			return '';
	
		return (_activeRows.length == $scope.modelHandler.size())? 'active' : ''; 
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
	
	$scope.init = function(){
		if(Utils.isBlank($scope.loginUser)){
			$scope.validateLoginAuthority(err);
		}
		$scope.searchHandler.initialize($scope.sessionHandler.getProperty('searchHandler'));
		$scope.pageHandler.initialize($scope.sessionHandler.getProperty('pageHandler'));
		
		$scope.searchHandler.setSearchListener(_loadUserList);
		$scope.pageHandler.setPageChangeListener(_loadUserList);
		
		$scope.modelForm.setModel(User());
		$scope.modelForm.setUpdateCallback(_changeUser);
		$scope.createForm.setModel(User());
		$scope.createForm.setSubmitCallback(_createUser);
		
		$scope.showUserInfo();
	}
	
	$scope.showUserList = function(){
		_loadUserList();
	}
	
	$scope.showUserInfo = function(){
		var userId = JSON.parse($scope.sessionHandler.getProperty("USER")).userId;
		userFactory.getUser(userId).then(
				function(res){
					waitingDialog.hide();
					$scope.user = res.data;
				},
				function(err){
					waitingDialog.hide();
					notificationService.error(err.data);
					$scope.validateLoginAuthority(err);
				}
			)
	}
	
	
	$scope.onClickDetailLink = function(user){
		if(Utils.isBlank(user) || user.userId <= 0){
			notificationService.error(message.NOTICE021_EN);
			return;
		}
		waitingDialog.show('Loading...');
		userFactory.getUser(user.userId).then(
			function(res){
				waitingDialog.hide();
				$scope.modelForm.setModel(res.data);
				$('#user-edit-popup').modal('show');
			},
			function(err){
				waitingDialog.hide();
				notificationService.error(err.data);
				$scope.validateLoginAuthority(err);
			}
			
		)
	}
	
	_createUser= function(user) {
		user['updateUser'] = JSON.parse($scope.sessionHandler.getProperty("USER")).userId;
		userFactory.createUser(user).then(
			function(res) {
				$scope.createForm.reset();
				$('#user-create-popup').modal('hide');
				notificationService.success(message.NOTICE001_KR);
				_loadUserList();
			}, 
			function(err) {
				$log.error(err);
				waitingDialog.hide();
				notificationService.error(err.data);
				$scope.validateLoginAuthority(err);
			}
		);
	};
	
	$scope.reload = function() {
		waitingDialog.show("Loading ...");
		_loadUserList();
	};
	
	_loadUserList = function(){
		var params = {
			page : $scope.pageHandler.getCurrentPage()
			,rowSize : $scope.pageHandler.numberOfRows
			,userId : $scope.searchHandler.user.userId
			,name : $scope.searchHandler.user.name
			,institute : $scope.searchHandler.user.institute 
		}
		
		userFactory.getUserList(params).then(
			function(res){
				waitingDialog.hide();
				$scope.modelHandler.setItems(res.data.list);
				$scope.pageHandler.setTotal(res.data.total);
				$scope.pageHandler.setCurrentPage(res.data.page);
			},
			function(err){
				waitingDialog.hide();
				notificationService.error(err.data);
				$scope.validateLoginAuthority(err);
			}
		)
	}
	
	_changeUser = function(user){
		if(Utils.isBlank(user.pw1)){
			userFactory.changeUser(user).then(
					function(res){
						waitingDialog.hide();
						$('#user-edit-popup').modal('hide');
						notificationService.success(message.NOTICE002_KR);
					},
					function(err){
						$('#user-edit-popup').modal('hide');
						$log.error(err);
						waitingDialog.hide();
						notificationService.error(err.data);
						$scope.validateLoginAuthority(err);
					}
				)
		}else{
			user.authPw = user.presentPw;
			user.password = user.pw1;
			userFactory.changeUserPw(user).then(
					function(res){
						waitingDialog.hide();
						$('#user-findPassword-popup').modal('hide');
						notificationService.success(message.NOTICE002_KR);
					},
					function(err){
						$('#user-findPassword-popup').modal('hide');
						$log.error(angular.toJson(err, true));
						waitingDialog.hide();
						notificationService.error(err.data);
						$scope.validateLoginAuthority(err);
					}
				)
		}
	}
	
	$scope.resetUserForm = function(userId){
		$scope.modelForm.reset();
		$scope.modelForm.model.userId = userId;
	}
});