gdkmApp.controller('indexController', function($scope, $window, $location, $log, $cookies, $timeout, $cacheFactory,
		notificationService, loginFactory, codeFactory, statisticsFactory) {
	
	$scope.sessionHandler = SessionHandler.create($window);
	
	$scope.params = new SearchParams();
	var _dataLabel = MESSAGE_SECTION.NGSDATA;
	var message = MESSAGE.create(_dataLabel);
	/**
	 * 데이터가 등록된 연도 목록
	 */
	$scope.registYearList = [
		new Date().getFullYear()
	];
	
	$scope.rowSizeCodeList = [
		{ id: 10, name: '10 of each'},
		{ id: 20, name: '20 of each'},
		{ id: 30, name: '30 of each'},
		{ id: 50, name: '50 of each'},
		{ id: 100, name: '100 of each'},
	];
	
	$scope.openYnCodeList = [
		{ id: '', name: 'All'},
		{ id: 'Y', name: 'Open'},
		{ id: 'N', name: 'Close'},
	];
	
	$scope.matchCodeList = [
		{ id: 'exact', name: 'Exact-match keyword'},
		{ id: 'partial', name: 'Partial-match keyword'},
	];
	
	$scope.dataTypeCodeList = [
		{ id: '', name: 'All'},
		{ id: PAGE_DATA.RAWDATA, name: 'Raw data'},
		{ id: PAGE_DATA.PROCESSED, name: 'Annotation'},
	];
	
	$scope.registSttsCodeList = [
		{ id: '', name: 'All'},
		{ id: REGIST_STATUS.READY, name: 'Registration reception'},
		{ id: REGIST_STATUS.VALIDATING, name: 'Verifying file'},
		{ id: REGIST_STATUS.VALIDATED, name: 'Finishing Verification'},
		{ id: REGIST_STATUS.ERROR, name: 'File error'},
		{ id: REGIST_STATUS.FAILED, name: 'Rejecting submission'},
		{ id: REGIST_STATUS.SUCCESS, name: 'Finishing registration'}
	];
	
	$scope.veviewSttsCodeList = [
		{ id: '', name: 'All'},
		{ id: REGIST_STATUS.SUBMIT, name: 'Submitting'},
		{ id: REGIST_STATUS.FAILED, name: 'Rejecting submission'},
		{ id: REGIST_STATUS.SUCCESS, name: 'Finishing registration'}
	];
	
	// 성과물(논문) 등록 상태 목록
	$scope.researchSttsCodeList = [
		{ id: '', name: 'All'},
		{ id: REGIST_STATUS.READY, name: 'Registration reception'},
		{ id: REGIST_STATUS.SUCCESS, name: 'Finishing registration'}
	];
	
	$scope.rawFieldCodeList = [
		{ id: '', name: 'All'},
		{ id: 'registNo', name: 'Raw data ID'},
		{ id: 'sampleSource', name: 'Source'},
		{ id: 'sampleName', name: 'Sample name'},
		{ id: 'organismType', name: 'Organism type'},
		{ id: 'species', name: 'Species name'},
		{ id: 'ncbiTaxonId', name: 'Taxonomy ID'},
		{ id: 'platform', name: 'Sequencing platform'}
	];
	
	$scope.proccessedFieldCodeList = [
		{ id: '', name: 'All'},
		{ id: 'registNo', name: 'Annotation ID'},
		{ id: 'submitter', name: 'Submitter'},
		{ id: 'project', name: 'Project'},
		{ id: 'projectType', name: 'Project type'},
		{ id: 'assemblyMethod', name: 'Assembly methods'},
		{ id: 'refTitle', name: 'Reference title'},
		{ id: 'parentNo', name: 'Raw data ID'},
		{ id: 'species', name: 'Species name'},
		{ id: 'ncbiTaxonId', name: 'Taxonomy ID'}
	];
	
	$scope.researchFieldCodeList = [
		{ id: '', name: 'All'},
		{ id: 'registNo', name: 'Research ID'},
		{ id: 'title', name: 'Title'},
		{ id: 'submitter', name: 'Submitter'},
		{ id: 'submitOrgan', name: 'Submit organization'}
	];
	
	$scope.rankCodeList = angular.copy(NODE_RANK_LIST)
	$scope.rankCodeList.unshift({ id: '', name: 'All' });
	
	$scope.openCodeList = [
		{ id: '', name: 'All'},
		{ id: 'Y', name: 'Open'},
		{ id: 'N', name: 'Close'}
	];
	
	$scope.registDataTyeList = [
		{ id: '', name: 'Total (Data Type)'},
		{ id: 'rawdata', name: 'Raw data'},
		{ id: 'processed', name: 'Annotation'}
	];
	
	$scope.reCodeList = [];
	$scope.rtCodeList = [];
	$scope.rrCodeList = [];
	$scope.rsCodeList = [];
	$scope.rpCodeList = [];
	$scope.rnCodeList = [];
	$scope.paCodeList = [];
	$scope.pgCodeList = [];
	$scope.ffCodeList = [];
	$scope.roCodeList = [];
	
	$scope.init = function() {
		_loadCodeList('RE', $scope.reCodeList);
		_loadCodeList('RT', $scope.rtCodeList);
		_loadCodeList('RR', $scope.rrCodeList);
		_loadCodeList('RS', $scope.rsCodeList);
		_loadCodeList('RP', $scope.rpCodeList);
		_loadCodeList('RN', $scope.rnCodeList);
		_loadCodeList('PA', $scope.paCodeList);
		_loadCodeList('PG', $scope.pgCodeList);
		_loadCodeList('FF', $scope.ffCodeList);
		_loadCodeList('RO', $scope.roCodeList);
		
		statisticsFactory.getRegistYearList().then(function(result) {
			$scope.registYearList = result;
		});
		
		$scope.userId = $cookies.getObject("loginUserId");
		if(Utils.isNotNull($scope.userId)){
			$scope.cookie = true;
		}
	};
	
	$scope.loginCheck = function(){
		$scope.loginUser = $scope.sessionHandler.getProperty("USER",USER);
		return !Utils.isBlank($scope.loginUser);
	};
	
	$scope.login = function(){
		var params = {
				userId : $scope.userId,
				password : $scope.password
		}
		loginFactory.loginProcess(params).then(
			function(res){
				if($scope.cookie==true){
					$cookies.putObject('loginUserId', $scope.userId);
				}else if($scope.cookie==false){
					$cookies.remove('loginUserId', $scope.userId);
				}
				var result = res.data
				$scope.sessionHandler.setProperty("USER",angular.toJson(result));
				//$window.sessionStorage.setItem("USER", angular.toJson(result));
				$location.path("/home");
				$window.location.reload();
			},
			function(err){
				alert(err.data);
			}
		)
	};
	
	$scope.validateLoginAuthority = function(err){
		if(err.status && err.status == 401) {
			$scope.logout();
			
			// bootstrap 모달창이 닫히지 않는 경우를 방지하기 위한 코드 추가
			$('.modal-backdrop').remove();
		}
	};
	
	$scope.logout = function(){
		$timeout(function() {
			$scope.sessionHandler.removeProperty("USER",USER);
			$window.sessionStorage.removeItem("USER");
			loginFactory.logoutProcess().then(
				function(res){
					$location.path("/home");
				},
				function(err){
					$location.path("/home");
				}
			)
		}, 500);
	}
	
	//left메뉴 active class적용
	$scope.isActive = function (viewLocation) {
	     return (viewLocation === $location.path());
	};
	
	/**
	 * 메인화면 상단 메뉴바의 검색버튼 클릭 이벤트 처리
	 */
	$scope.onClickSearchBtn = function() {
		$scope.params.strain = $scope.params.species;
		
		if('/integrated-view' == $location.path()) {
			waitingDialog.show('Searching ...');
			$scope.$broadcast('onDispatchSearchEvent', angular.copy($scope.params));
		} else {
			var cache = Utils.getCache($cacheFactory);
			if(angular.isDefined(cache)) {
				cache.put(CACHE_PROP.SEARCH_PARAMS, angular.copy($scope.params));
				waitingDialog.show('Searching ...');
				$location.path('/integrated-view');
			}
		}
	};
	
	$scope.sumOfStat = function(list, field) {
		var sum = 0; 
		if(angular.isArray(list)) {
			for(var i=0; i < list.length ; i++) {
				if((list[i][field]) >= 0) {
					sum += list[i][field];
				}
			}
		}
		
		return sum;
	};
	
	$scope.calcGcContents = function(quantity) {
		try {
			return ((quantity.numberOfG + quantity.numberOfC) / quantity.totalLength) * 100; 
		} catch (exception) {
			return 0;
		}
	};
	
	$scope.openFastQcReport = function(achive) {
		if(!angular.isObject(achive) || (achive['id'] > 0) == false) {
			return $log.error("Invalid achive object");
		}
		
		var url = Utils.getContextPath() + "/achieves/" + achive.id + "/qcReport";
		$window.open(url, achive.registNo + " QC report", "width=1280, height=890, resizable=1");
	};
	
	$scope.downloadAchiveFile = function(achive) {
		if(!angular.isObject(achive) || (achive['id'] > 0) == false) {
			return notificationService.notice(message.NOTICE014_EN+" file");
//			return notificationService.notice('Invalid NGS data file');
		}
		if(!confirm("Are you sure you are downloading '" + achive.fileName + "'?")){
			return;
		}
		var url = Utils.getContextPath() + "/achieves/" + achive.id;
		$window.location.assign(url);
	};
	
	var _loadCodeList = function(group, codeList) {
		var params = {
			'group' : group,
			'isBasicForm' : true 
		};
		
		codeFactory.getCodeList(params).then(
			function(res) {
				codeList.splice(0, codeList.length);
				
				var list = res.data.list;
				for(var i=0; i < list.length ; i++) {
					codeList.push(list[i]);
				}
			}, 
			function(err) {
				$log.error(angular.toJson(err, true));
			}
		);
	};
	
	/*var _setCookie = function(cookieName, value, exdays){
		var expireDate = new Date();
		expireDate.setDate(expireDate.getDate() + exdays);
		var cookieValue = escape(value) + ((exdays == null) ? "" : "; expries=" + expireDate.toGMTString());
		document.cookie == cookieName + "=" + cookieValue;
	}
	
	var _deleteCookie = function(cookieName){
		var expireDate =  new Date();
		expireDate.setDate(expireDate.getDate() -1);
		document.cookie = cookieName + "= " + "; expires=" + expireDate.toGMTString();
	}
	
	var _getCookie = function(cookieName){
		cookieName = cookieName + "=";
		var cookieData = document.cookie;
		var start = cookieData.indexOf(cookieName);
		var cookieValue = '';
		if(start != -1){
			start += cookieName.length;
			 var end = cookieData.indexOf(';', start);
	            if(end == -1)end = cookieData.length;
	            cookieValue = cookieData.substring(start, end);
        }
        return unescape(cookieValue);
    }*/
	
	$scope.checkCookieLoginId = function(cookie){
		if(cookie==false){
			$scope.cookie = false;
		}else if(cookie==true){
			$scope.cookie = true;
		}
	}; 
	
	/**
	 * Bootstrap 툴팁을 이용하는 경우 설명글(title)의 길이가 offset 보다 작은 경우 보이지 않고 offset 보다 큰 경우만 툴팁이 보이도록 하기 위해 만든 함수 
	 * 기본 길이는 10으로 설정이 됨
	 * 
	 * ex) offset 이 10인 경우 
	 * title이 '123456' 인 경우는 툴팁이 보이지 않음 
	 * title이 '1234567890'인 경우는 툴팁으로 '1234567890'이 보임 
	 */
	$scope.tooltip = function(title, offset) {
		if(Utils.isBlank(title))
			return null;
		
		if(angular.isNumber(offset)) {
			if(offset < 1) {
				offset = 1; 
			} else if(offset > 100) {
				offset = 100; 
			}
		} else {
			offset = 10;
		}
		
		return (title.trim().length < offset) ? null : title.trim();
	};
	
	/**
	 * 등록번호가 있는 모델(NgsDataRegist, NgsRawData, NgsProcessedData, NgsDataFile, Research, ResearchAttachment, ResearchOmics) 목록을 받아 
	 * 전체 목록을 표시하지 않고 "등록번호 외 1개" 란 식으로 약어를 반환하는 함수. 
	 * 
	 * @see app-object.js 
	 */
	$scope.abbreviate = function(modelList) {
		if(angular.isArray(modelList)) {
			if(modelList.length == 0) {
				return '-';
			} else if(modelList.length > 1) {
				let registNo = $scope.getRegistNo(modelList[0]);
				return registNo + " and " + (modelList.length-1) + " other(s)";
			} else {
				return $scope.getRegistNo(modelList[0]);
			} 
		}
		
		return $scope.getRegistNo(modelList);
	};
	
	$scope.getRegistNo = function(model) {
		if(angular.isObject(model)) {
			if(Utils.isBlank(model['registNo']))
				return '';
			
			return model.registNo;
		}
		
		return model;
	};
});