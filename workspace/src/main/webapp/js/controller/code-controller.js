gdkmApp.controller('codeController',function($scope, $rootScope, $location, $window, $timeout, $log, codeFactory, codeGroupFactory, notificationService){
	
	$scope.searchHandler = SearchHandler.create($timeout);
	$scope.pageHandler = PageHandler.create();
	$scope.modelHandler = ModelListHandler.create($timeout);
	
	$scope.createForm = ModelForm.create($timeout);
	$scope.modelForm = ModelForm.create($timeout);
	
	$scope.idCheckUrl = Utils.getContextURL() + "/codes/check/id";
	var _dataLabel = MESSAGE_SECTION.CODE_KR;
	var message = MESSAGE.create(_dataLabel);
	
	$scope.init = function(){
		if(Utils.isBlank($scope.loginUser)){
			$scope.validateLoginAuthority(err);
		}
		$scope.searchHandler.initialize($scope.sessionHandler.getProperty('searchHandler'));
		$scope.pageHandler.initialize($scope.sessionHandler.getProperty('pageHandler'));
		$scope.modelHandler.initialize($scope.sessionHandler.getProperty('modelHandler'));
		
		$scope.searchHandler.setSearchListener(_loadCodeList);
		$scope.pageHandler.setPageChangeListener(_loadCodeList);
		
		$scope.modelForm.setModel(Code());
		$scope.modelForm.setUpdateCallback(_updateCode);
		$scope.createForm.setModel(Code());
		$scope.createForm.setSubmitCallback(_createCode);
		$scope.selectUseYn = [{value : "Y"}, {value : "N"}]; 
		_loadCodeGroupList();
		_loadCodeList();
	}
	
	$scope.onClickDetailLink = function(code){
		if(Utils.isBlank(code) || code.code <= 0){
			notificationService.error(message.NOTICE021_KR);
//			notificationService.error("선택된 정보가 잘 못 되었습니다.");
			return;
		}
		waitingDialog.show('Loading...');
		codeFactory.getCode(code.code).then(
			function(res){
				waitingDialog.hide();
				$scope.modelForm.setModel(res.data);
				$('#code-edit-popup').modal('show');
			},
			function(err){
				$log.error(err);
				notificationService.error(err.data);
				waitingDialog.hide();
				if(err.status && err.status == (401 & 403)) {
					$timeout(function() {
						$scope.validateLoginAuthority(err);
						$location.url("/login");
					}, 500);
				}
			}
			
		)
	}
	
	$scope.onClickDelete = function(){
		var codeList = $scope.modelHandler.getSelectedItems();
		var answer = "";
		if(codeList.length < 1){
			notificationService.error(message.NOTICE014_KR);
//			alert("선택된 코드가 없습니다.")
			return;
		}else{
			answer = confirm("선택하신 코드를 삭제하시겠습니까?");
		}
		
		if(answer==true){
			var codeId = [];
			for(var idx in codeList){
				codeId.push(codeList[idx].code);
			}
			
			waitingDialog.show('Delete...');
			codeFactory.deleteCode(codeId).then(
				function(res){
					notificationService.success(message.NOTICE004_KR);
//					notificationService.success("요청하신 코드 "+ res.data + "건이 삭제되었습니다.");
					waitingDialog.hide();
					_loadCodeList();
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
	
	$rootScope.$on('selectCodeGroupByCode',function(event, args){
		$scope.createForm.model.group = args.codeGroup;
		$scope.modelForm.model.group = args.codeGroup;
	});
	
	_createCode= function(code) {
		codeFactory.createCode(code).then(
			function(res) {
				$scope.createForm.reset();
				$('#code-create-popup').modal('hide');
				notificationService.success(message.NOTICE001_KR);
//				notificationService.success("코드 정보가 정상적으로 생성되었습니다.");
				_loadCodeList();
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
				}
			}
		);
	};
	
	$scope.reload = function() {
		waitingDialog.show("Loading ...");
		
		_loadCodeList();
	};
	
	var _loadCodeList = function(){
		var params = {
			page : $scope.pageHandler.getCurrentPage()
			,rowSize : $scope.pageHandler.numberOfRows
			,group : $scope.searchHandler.code.codeGroup.id
			,code : $scope.searchHandler.code.id
			,name : $scope.searchHandler.code.name
			,useYn : $scope.searchHandler.code.useYn
		}
		
		codeFactory.getCodeList(params).then(
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
						$location.url("/login");
					}, 500);
				}
				if(err.status == 404){
					console.log("err");
				}
			}
		)
	}
	
	var _loadCodeGroupList = function(){
		codeGroupFactory.getCodeGroupList().then(
			function(res){
				$scope.codeGroupList = res.data.list; 
			},
			function(err){
				waitingDialog.hide();
				notificationService.error(err.data);
				if(err.status == (401 & 403)){
					$timeout(function(){
						$scope.validateLoginAuthority(err);
						$location.url("/login");
					}, 500);
				}
				if(err.status == 404){
					console.log("err");
				}
			}
		)
	}
	
	_updateCode = function(code){
//		code['code'] = code['id'];
		codeFactory.updateCode(code).then(
			function(res){
				waitingDialog.hide();
				$('#code-edit-popup').modal('hide');
				notificationService.success(message.NOTICE002_KR);
//				notificationService.success("코드 정보가 정상적으로 수정되었습니다.");
				$scope.init();
				_loadCodeList();
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
				}
			}
		)
	}
});