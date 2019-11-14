/**
 * 
 */
gdkmApp.controller('aniExecuteController', function ($scope, $http, $location, $rootScope, $timeout, $log, Upload, toolsFactory, notificationService, depositFactory ) {

	var _activeRows = [];
	var _activeFilesRows = [];
	
	$scope.pageHandler = PageHandler.create();
	$scope.uploadFiles=[];
	$scope.params = {
		page: 0,
		rowSize: 10,
		keyword:"",
		content:""
	};
	
	$scope.methods = ["ANIm", "ANIb", "ANIblastall", "TETRA"];
	$scope.method = "ANIm";
	$scope.rowSizeCodeList = [
		{ id: 10, name: '10 of each'},
		{ id: 20, name: '20 of each'},
		{ id: 30, name: '30 of each'},
		{ id: 50, name: '50 of each'},
		{ id: 100, name: '100 of each'},
	];
	$scope.keywords = [
		{id:"achiveRegistNo", name:"fileId"},
		{id:"dataRegistNo", name:"annotationID"},
		{id:"fileName", name:"fileName"},
		{id:"strain", name:"strain"}
	];
	$scope.search = {id:"achiveRegistNo", content:""};
	$scope.files = []; 
	$scope.selectedFiles = [];
	$scope.checkAll = false;
	$scope.modal = {
		checkAll:false,
		changeName: ""
	};
	
	$scope.display = {
		editPopupId : '',
		detailPopupId : '',
		accordions : [true, false, false],
	};
	$scope.uploadFileResult = {};
	$scope.selectedUserFiles = [];
	/**
	 * NGS 데이터 등록정보 연계데이터 목록 핸들러
	 */
	$scope.linkedHandler = ModelListHandler.create($timeout);
	
	$scope.init = function(){
		console.log("aniController init");
		$scope.pageHandler.setPageChangeListener($scope.fileListPageChange);
		$scope.fileListPageChange();
		$scope.search.id = "achiveRegistNo";
	}

	$scope.calcGcContents = function(quantity) {
		try {
			return ((quantity.numberOfG + quantity.numberOfC) / quantity.totalLength) * 100; 
		} catch (exception) {
			return 0;
		}
	};
	
	// panel 관련 함수
    $scope.panel_h_css = function(achiveRegistNo) {
		return (_activeRows.indexOf(achiveRegistNo) < 0) ? '' : 'active';
	};
	$scope.panel_collapse_css = function(achiveRegistNo) {
		return (_activeRows.indexOf(achiveRegistNo) < 0) ? 'collapse' : '';
	};
	$scope.toggleActiveRows = function(results) {
		if(_activeRows.length == results.length) {
			_activeRows = [];
		} else {
			$.each(results, function(index, result) {
				if(_activeRows.indexOf(result.achiveRegistNo) < 0)
					_activeRows.push(result.achiveRegistNo);
			})
		}
		console.log("toggleActiveRows");
		console.log(_activeRows);
	};
	$scope.activeRow = function (achiveRegistNo) {
		if(achiveRegistNo == null)
			_activeRows = [];
		else {
			if (_activeRows.indexOf(achiveRegistNo) < 0) {
				_activeRows.push(achiveRegistNo);
			} else {
				_activeRows.splice(_activeRows.indexOf(achiveRegistNo), 1);
			}
		}
		console.log("activeRow");
		console.log(_activeRows);
    };
    // /panel 관련 함수
	
	function getFileExtension(filename) {
		return filename.split('.').pop();
	}
	
	function getFileExtensionFasta (filename) {
		var extension = getFileExtension(filename);
		if(extension == 'fasta' || extension == 'fna' || extension == 'fa' 
			|| extension == 'faa' || extension == 'ffn') {
			return true;
		}
		return false;
	}
	
	$scope.onclickUpload = function() {
		console.log($scope.uploadFiles);
		var _removeFiles = [];
		console.log($scope.selectedFiles == '');
		
		$.each($scope.uploadFiles, function(index, uploadFile) {
			var extension = getFileExtension(uploadFile.name);
			
			if(!getFileExtensionFasta(uploadFile.name)) {
				alert("fasta 파일이 아닙니다. ");
				_removeFiles.push(uploadFile);
				return;
			}
			
			$scope.doUploadFile(uploadFile);
			
		})
		$.each(_removeFiles, function(index, _removeFile) {
			$scope.uploadFiles.slice(_removeFile);
		})
	}
	
	$scope.submit = function() {
		// 라벨 같으면 안됨
		let labelSet = new Set();
		$.each($scope.selectedFiles, function(index, selectedFile) {
			labelSet.add(selectedFile.aniLabel);
		});	
		
		if(labelSet.size != $scope.selectedFiles.length) {
			alert("중복된 라벨이 존재합니다.");
			return;
		}
		
		$rootScope.response = null;
		
//		if($scope.uploadFiles != '')
//			$scope.doUploadFile();
//		else 
			$scope.runAni();
		
		$location.path("/tools/ani-result");
	}
	
	/**
	 * 파일업로드 실행
	 * 
	 * @parameter file : 실제 올려지는 파일 객체  
	 * @parameter dataFile : 서버에 저장되는 파일정보 객체				 
	 */
	$scope.doUploadFile = function(uploadFile) {
		
//		$.each($scope.uploadFiles, function(index, uploadFile) {
		waitingDialog.show("Loading ...");
		
			var uploadHandler = Upload.upload({
				url : Utils.getContextURL() + "/ani/upload/"+ $scope.method,
				headers : {
					"optional-header" : "header-value",
					"Content-Type" : uploadFile.type
				}, 
				data : {
					file : uploadFile
				}
			});
			
			uploadHandler.then(function(res) {
				console.log("전송완료: "+ uploadFile.name);
				console.log(res.data);
				$scope.uploadFileResult = res.data;
//				$scope.runAni(res.data.outputDir);
				
				var replace = uploadFile.name.replace(" ", "_").replace("."+uploadFile.name.split('.').pop(), "");
				var file = {
					fileName:uploadFile.name,
					type:"fasta",
					achiveRegistNo:replace + "_" + uploadFile.size,
					aniLabel: uploadFile.name,
					seqQuantityVO : res.data.seqQuantity,
					fileSize:uploadFile.size,
					_checked: false
				}
				console.log("uploadFile: " + file);
				
				if($scope.selectedFiles.indexOf(file) < 0)
					$scope.selectedFiles.push(file);
				
				if($scope.selectedUserFiles.indexOf(file) < 0)
					$scope.selectedUserFiles.push(file);
				
				waitingDialog.hide();
//				$scope.activeRow(file.achiveRegistNo);
			}, function(err) {
				console.log(err);
			})
//		})
		
	}
	
	$scope.runAni = function() {
		$rootScope.param = {
			method: $scope.method,
			selectedFiles: $scope.selectedFiles,
			outputDir: $scope.uploadFileResult.outputDir
		}
		
		toolsFactory.postAni($rootScope.param).then(function(res) {
			console.log("실행완료: ");
			console.log($rootScope.param);
			console.log(res.data);
			$rootScope.response = res.data;
		}, function(err) {
			console.log(err);
		});
		
	}
	
	// checkbox 관련 함수
    $scope.handleAutoSelectClick = function(items) {
	    $.each(items, function(index, item) {
	    	item._checked = $scope.checkAll;
	    })
	}

	$scope.changeCheckBox = function(items) {
	    $.each(items, function(index, item) {
	    	if(!item._checked) {
	    		$scope.checkAll = false;
	    	}
	    })
	}
	
	$scope.onclickDeleteBtn = function() {
		var _removeFiles = [];
		$.each($scope.selectedFiles, function(index, selectedFile) {
			if(selectedFile._checked)
				_removeFiles.push(selectedFile);
			selectedFile.checked = false;
		});
		
		console.log(_removeFiles);
		$.each(_removeFiles, function(index, _removeFile) { 
			console.log(_removeFile);
			$scope.selectedFiles.splice($scope.selectedFiles.indexOf(_removeFile), 1);
			
			if($scope.selectedUserFiles.indexOf(_removeFile) >= 0){
				var param = {
					output : $scope.uploadFileResult.outputDir,
					fileName : _removeFile.fileName
				}
				console.log($scope.uploadFileResult);
				console.log(param);
				toolsFactory.deleteUploadFile(param).then(
					function(res){
						console.log(res);
					}, function(err) {
						console.log(err);
					}
				)
			}
				
			console.log($scope.selectedFiles);
		});	
	}
	
	$scope.onclickChangeClassBtn = function() {
		$scope._changeFiles = [];
		$.each($scope.selectedFiles, function(index, selectedFile) {
			if(selectedFile._checked)
				$scope._changeFiles.push(selectedFile);
			selectedFile._checked = false;
		});
		$scope.checkAll = false;
		$scope.modal.checkAll = false;
		$("#ani-class-change-popup").modal('show');
		
	}
	
	// -------------------------------------------------
	// modal 관련 함수
	// -------------------------------------------------
	$scope.onclickFastaFileList = function() {
		$.each($scope.selectedFiles, function(index, selectedFile) {
			selectedFile.__checked = false;
			selectedFile._checked = false;
		})
		$scope.checkAll = false;
		$scope.modal.checkAll = false;
		$("#fasta-file-list-popup").modal('show');
	}
	
	$scope.onclickProcessedDetail = function(selectedFile) {
		$scope.selectedFileInfo = selectedFile;
		
		toolsFactory.getRegistData(selectedFile.achiveRegistNo).then(
			function(res) {
				$scope.onClickRegistDataDetail(res.data);
			}, function(err) {
				console.log(err);
			}
		)
		
//		$("#ngs-processed-data-detail-popup").modal('show');
	}
	
	// fasta file list 불러오기
	$scope.fileListPageChange = function() {
		$scope.params.page = $scope.pageHandler.getCurrentPage();
		$scope.params.rowSize = $scope.pageHandler.numberOfRows;
		
		toolsFactory.getFastFileList($scope.params).then(
			function(res) {
    			$scope.pageHandler.setTotal(res.data.total);
    			$scope.pageHandler.setCurrentPage(res.data.page);
    			$scope.files = res.data.list;
    			
				$.each($scope.files, function(index, file) {
					file.__checked = false;
				})
				
				waitingDialog.hide();
			}, function(err) {
				console.log(err);
			}
		);
		
	}
	
	// panel 함수
    $scope.toggleActiveFilesRows = function(results) {
		if(_activeFilesRows.length == results.length) {
			_activeFilesRows = [];
		} else {
			$.each(results, function(index, result) {
				if(_activeFilesRows.indexOf(achiveId) < 0)
					_activeFilesRows.push(result.achiveId);
			})
		}
	};
	$scope.activeFilesRows = function (achiveId) {
		if(achiveId == null) {
			_activeFilesRows = [];
			return;
		}
			
		if (_activeFilesRows.indexOf(achiveId) < 0) {
			_activeFilesRows.push(achiveId);
		} else {
			_activeFilesRows.splice(_activeFilesRows.indexOf(achiveId), 1);
		}
    };
    $scope.panel_h_css_files = function(achiveId) {
		return (_activeFilesRows.indexOf(achiveId) < 0) ? '' : 'active';
	};
	$scope.panel_collapse_css_files = function(achiveId) {
		return (_activeFilesRows.indexOf(achiveId) < 0) ? 'collapse' : '';
	};
	
	/* 등록 데이터 목록 검색 요청 */
	$scope.reload = function() {
		$scope.fileListPageChange();
	};
	
	$scope.onclickModalSubmit = function() {
		
		$.each($scope.files, function(index, file) {
			if(file.__checked) {
				file.aniLabel = file.dataRegistNo;
				file.__checked = false;
				file._checked = false;
				
				if($scope.selectedFiles.indexOf(file) < 0)
					$scope.selectedFiles.push(file);
			}
		})
		console.log($scope.selectedFiles);
	}
	
	// checkbox 관련 함수
    $scope.handleAutoModalSelectClick = function(items) {
    	console.log($scope.modal.checkAll);
	    $.each(items, function(index, item) {
	    	item.__checked = $scope.modal.checkAll;
	    })
	}

	$scope.changeModalCheckBox = function(items) {
	    $.each(items, function(index, item) {
	    	if(!item.__checked) {
	    		$scope.modal.checkAll = false;
	    	}
	    })
	}
	
	$scope.search = function() {
		$scope.params.searchId = $scope.search.id;
		$scope.params.searchContent = $scope.search.content;
		$scope.fileListPageChange();
	}
	
	$scope.onclickChangeClassModalSubmit = function() {
		$.each($scope._changeFiles, function(index, _changeFile) {
			_changeFile.aniClass = $scope.modal.changeName;
		})
		$scope.modal.changeName = "";
	}
	
	/**
	 * 등록데이터 상제정보 보기 버튼(링크) 클릭 이벤트 처리
	 */
	$scope.onClickRegistDataDetail = function(item){
		if(item == null)
			return;
		
		waitingDialog.show("Loading ...");
		try{
			$scope.dataLabel = 'annotation data';
			var tabSize = 3;
			var popupId = "#processed-data-detail-popup";
			
			console.log(item);
			
			$scope.model = angular.copy(item);
			
			_loadNgsDataLinkedList(item);
			
			$timeout(function() {
				waitingDialog.hide();
				$(popupId).modal('show');
			}, 300);
			
		}catch(exception){
			waitingDialog.hide();
			$log.error(exception);
			notificationService.error(exception.data);
		}
	};
	
	/**
	 * NGS 데이터 연계 데이터 목록 호출
	 */
	var _loadNgsDataLinkedList = function(regist) {
		if(Utils.isBlank(regist))
			return $log.error("Could not load linked data - empty reigst id");
		
		var registId = regist; 
		if(angular.isObject(regist)) {
			registId = regist.id;
		}
		
		$scope.linkedHandler.setItems([]);
		
		if(registId > 0) {
			depositFactory.getNgsDataLinkedList(registId).then(
	    		function(res) {
	    			waitingDialog.hide();
	    			$scope.linkedHandler.setItems(res.data);
	    		}, function(err) {
	    			waitingDialog.hide();
	    			$log.error(angular.toJson(err, true));
	    		}
	    	);
		}
    };
	
    /**
	 * 상세정보 보기 화면의 NGS 데이터 파일 목록 가시화 여부 반환
	 */
    $scope.isHideAchiveList = function() {
		return true;
	};
    
	$scope.reload = function() {
		pageChange();
	}
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
	
});

/**
 * 
 */
gdkmApp.controller('aniResultController', function ($scope, $http, $location, $rootScope, $timeout, $log, Upload, 
		uiGridConstants, toolsFactory, depositFactory, notificationService ) {

	$scope.coverage = [];
	$scope.lengths = [];
	$scope.identity = [];
	$scope.hadamard = [];
	$scope.similarity = [];
	$scope.tetraCorrelations = [];
	
	/**
	 * NGS 데이터 등록정보 연계데이터 목록 핸들러
	 */
	$scope.linkedHandler = ModelListHandler.create($timeout);
	
	$scope.init = function(){
		console.log("aniResultController init");
		waitingDialog.show("Loading ...");
		
		if($rootScope.param == null)
			return;
		
		$scope.startTime = new Date().getTime();
		
		$timeout(timer, 1000);
	}
	$scope.display = {
		editPopupId : '',
		detailPopupId : '',
		accordions : [true, false, false],
	};
	
	// grid
	$scope.coverageGridOptions = {
			rowHeight: 42,
			headerRowHeight: 42,
			enableSorting: false,
			enableColumnMenus: false,
			enableColumnResizing: true,
			enableVerticalScrollbar: uiGridConstants.scrollbars.NEVER,
			columnDefs: [ 
				{ 
					field: 'col-0', 
					width: 200, 
					enableColumnResizing: true, 
					displayName: '',
					cellTemplate: '<div class="ui-grid-cell-contents text-center" style="padding-top: 12px; font-weight: bold;"><a role="button" ng-click="grid.appScope.onClickCell(row, col)">{{ COL_FIELD }}</a></div>',
					pinnedLeft : true
				}
			], 
			data: []
	};
	$scope.lengthsGridOptions = {
			rowHeight: 42,
			headerRowHeight: 42,
			enableSorting: false,
			enableColumnMenus: false,
			enableColumnResizing: true,
			enableVerticalScrollbar: uiGridConstants.scrollbars.NEVER,
			columnDefs: [ 
				{ 
					field: 'col-0', 
					width: 200, 
					enableColumnResizing: true, 
					displayName: '',
					cellTemplate: '<div class="ui-grid-cell-contents text-center" style="padding-top: 12px; font-weight: bold;"><a role="button" ng-click="grid.appScope.onClickCell(row, col)">{{ COL_FIELD }}</a></div>',
					pinnedLeft : true
				}
				], 
				data: []
	};
	$scope.identityGridOptions = {
			rowHeight: 42,
			headerRowHeight: 42,
			enableSorting: false,
			enableColumnMenus: false,
			enableColumnResizing: true,
			enableVerticalScrollbar: uiGridConstants.scrollbars.NEVER,
			columnDefs: [ 
				{ 
					field: 'col-0', 
					width: 200, 
					enableColumnResizing: true, 
					displayName: '',
					cellTemplate: '<div class="ui-grid-cell-contents text-center" style="padding-top: 12px; font-weight: bold;"><a role="button" ng-click="grid.appScope.onClickCell(row, col)">{{ COL_FIELD }}</a></div>',
					pinnedLeft : true
				}
				], 
				data: []
	};
	$scope.hadamardGridOptions = {
			rowHeight: 42,
			headerRowHeight: 42,
			enableSorting: false,
			enableColumnMenus: false,
			enableColumnResizing: true,
			enableVerticalScrollbar: uiGridConstants.scrollbars.NEVER,
			columnDefs: [ 
				{ 
					field: 'col-0', 
					width: 200, 
					enableColumnResizing: true, 
					displayName: '',
					cellTemplate: '<div class="ui-grid-cell-contents text-center" style="padding-top: 12px; font-weight: bold;"><a role="button" ng-click="grid.appScope.onClickCell(row, col)">{{ COL_FIELD }}</a></div>',
					pinnedLeft : true
				}
				], 
				data: []
	};
	$scope.similarityGridOptions = {
			rowHeight: 42,
			headerRowHeight: 42,
			enableSorting: false,
			enableColumnMenus: false,
			enableColumnResizing: true,
			enableVerticalScrollbar: uiGridConstants.scrollbars.NEVER,
			columnDefs: [ 
				{ 
					field: 'col-0', 
					width: 200, 
					enableColumnResizing: true, 
					displayName: '',
					cellTemplate: '<div class="ui-grid-cell-contents text-center" style="padding-top: 12px; font-weight: bold;"><a role="button" ng-click="grid.appScope.onClickCell(row, col)">{{ COL_FIELD }}</a></div>',
					pinnedLeft : true
				}
				], 
				data: []
	};
	$scope.tetraCorrelationsGridOptions = {
			rowHeight: 42,
			headerRowHeight: 42,
			enableSorting: false,
			enableColumnMenus: false,
			enableColumnResizing: true,
			enableVerticalScrollbar: uiGridConstants.scrollbars.NEVER,
			columnDefs: [ 
				{ 
					field: 'col-0', 
					width: 200, 
					enableColumnResizing: true, 
					displayName: '',
					cellTemplate: '<div class="ui-grid-cell-contents text-center" style="padding-top: 12px; font-weight: bold;"><a role="button" ng-click="grid.appScope.onClickCell(row, col)">{{ COL_FIELD }}</a></div>',
					pinnedLeft : true
				}
				], 
				data: []
	};
	
	//timer callback
    var timer = function() {
    	
    	
        if($rootScope.response == null) {
            $timeout(timer, 1000);
        } else {
        	$scope.endTime = new Date().getTime();
        	var params = {
        		outputDir: $rootScope.response.outputDir,
        		method: $rootScope.response.method
        	}
			toolsFactory.getOutputFile(params).then(
				function(res) {
					$.each(res.data, function(index, maps) {
						if(index.includes("coverage")) {
							$scope.coverage = maps;
							$scope.load($scope.coverage.sources, $scope.coverage.targets, $scope.coverageGridOptions);
						}else if(index.includes("lengths")) {
							$scope.lengths = maps;
							$scope.load($scope.lengths.sources, $scope.lengths.targets, $scope.lengthsGridOptions);
						}else if(index.includes("identity")) {
							$scope.identity = maps;
							$scope.load($scope.identity.sources, $scope.identity.targets, $scope.identityGridOptions);
						}else if(index.includes("hadamard")) {
							$scope.hadamard = maps;
							$scope.load($scope.hadamard.sources, $scope.hadamard.targets, $scope.hadamardGridOptions);
						}else if(index.includes("similarity")) {
							$scope.similarity = maps;
							$scope.load($scope.similarity.sources, $scope.similarity.targets, $scope.similarityGridOptions);
						}else if(index.includes("correlations")) {
							$scope.tetraCorrelations = maps;
							$scope.load($scope.tetraCorrelations.sources, $scope.tetraCorrelations.targets, $scope.tetraCorrelationsGridOptions);
						}
					})
					
					console.log(res.data);
					waitingDialog.hide();
				}, function(err) {
					
					console.log(err);
				}
			);
        }
    }
	
	$scope.onClickFileDetail = function(achiveRegistNo) {
		console.log(achiveRegistNo);
		
		toolsFactory.getRegistData(achiveRegistNo).then(
			function(res){
				_onClickRegistDataDetail(res.data);
			}, function(err) {
				console.log(err);
			}
		)
		
//		$scope.selectedFileInfo = null;
//		console.log(achiveRegistNo);
//		$.each($rootScope.response.selectedFiles, function(index, selectedFile) {
//			console.log(selectedFile.achiveRegistNo);
//			if(selectedFile.achiveRegistNo == achiveRegistNo)
//				$scope.selectedFileInfo = selectedFile;
//		})
//		console.log($scope.selectedFileInfo);
//		$("#ngs-processed-data-detail-popup").modal('show');
	}
	
	/**
	 * 등록데이터 상제정보 보기 버튼(링크) 클릭 이벤트 처리
	 */
	var _onClickRegistDataDetail = function(item){
		if(item == null)
			return;
		
		waitingDialog.show("Loading ...");
		try{
			$scope.dataLabel = 'annotation data';
			var tabSize = 3;
			var popupId = "#processed-data-detail-popup";
			
			console.log(item);
			
			$scope.model = angular.copy(item);
			
			_loadNgsDataLinkedList(item);
			
			$timeout(function() {
				waitingDialog.hide();
				$(popupId).modal('show');
			}, 300);
			
		}catch(exception){
			waitingDialog.hide();
			$log.error(exception);
			notificationService.error(exception.data);
		}
	};
	
	/**
	 * NGS 데이터 연계 데이터 목록 호출
	 */
	var _loadNgsDataLinkedList = function(regist) {
		if(Utils.isBlank(regist))
			return $log.error("Could not load linked data - empty reigst id");
		
		var registId = regist; 
		if(angular.isObject(regist)) {
			registId = regist.id;
		}
		
		$scope.linkedHandler.setItems([]);
		
		if(registId > 0) {
			depositFactory.getNgsDataLinkedList(registId).then(
	    		function(res) {
	    			waitingDialog.hide();
	    			$scope.linkedHandler.setItems(res.data);
	    		}, function(err) {
	    			waitingDialog.hide();
	    			$log.error(angular.toJson(err, true));
	    		}
	    	);
		}
    };
	
    /**
	 * 상세정보 보기 화면의 NGS 데이터 파일 목록 가시화 여부 반환
	 */
    $scope.isHideAchiveList = function() {
		return true;
	};
    
	$scope.reload = function() {
		pageChange();
	}
	
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
	
	$scope.downloadOutputFile = function(type, kind) {
		
		if(type == 'tab') {
			// 텍스트 다운로드
	    	var params = {
	    		outputDir: $rootScope.response.outputDir,
	    		name: $rootScope.response.method + "_" + kind +".tab"
	    	}
	    	toolsFactory.getOutputFileDownload(params).then(
				function(res) {
					const blob = new Blob([res.data], { type: 'application/octet-stream' });
  
					var downloadUrl = window.URL.createObjectURL(blob);
					let isIEOrEdge = /msie\s|trident\/|edge\//i.test(window.navigator.userAgent);

					//explore 일떄
					if(isIEOrEdge) {
						window.navigator.msSaveOrOpenBlob(blob, params.name);
						return ;
					}

					var link = document.createElement('a');
					link.href = downloadUrl;
					link.download = params.name;
					link.click();

				}, function(err) {
					console.log(err);
				}
			);
		} else {
			var targetUrl = Utils.getContextURL() + "/ani/output/image?outputDir="+$rootScope.response.outputDir+"&name="+kind
							+ "&method="+$rootScope.response.method;
			
			var a = $("<a>")
		    .attr("href", targetUrl)
		    .attr("download", $rootScope.response.method + "_" + kind + ".png")
		    .appendTo("body");

			a[0].click();
	
			a.remove();
			
		}
	}
	
	$scope.coverageGridOptions.onRegisterApi = function(gridApi){
		console.log(gridApi);
		$scope.gridApi = gridApi;
	};
	$scope.lengthsGridOptions.onRegisterApi = function(gridApi){
		console.log(gridApi);
		$scope.gridApi = gridApi;
	};
	$scope.identityGridOptions.onRegisterApi = function(gridApi){
		console.log(gridApi);
		$scope.gridApi = gridApi;
	};
	$scope.hadamardGridOptions.onRegisterApi = function(gridApi){
		console.log(gridApi);
		$scope.gridApi = gridApi;
	};
	$scope.similarityGridOptions.onRegisterApi = function(gridApi){
		console.log(gridApi);
		$scope.gridApi = gridApi;
	};
	$scope.coverageGridOptions.onRegisterApi = function(gridApi){
		console.log(gridApi);
		$scope.gridApi = gridApi;
	};
	$scope.tetraCorrelationsGridOptions.onRegisterApi = function(gridApi){
		console.log(gridApi);
		$scope.gridApi = gridApi;
	};
	
	$scope.load = function(source, target, gridOptions) {
		
		for(let i=0; i < source.length ; i++) {
			
			var columnDef = { 
					field : 'col-' + (i+1),
					width : 200,
					enableColumnResizing : true,
					displayName : source[i],
					headerCellTemplate: '<div class="ui-grid-cell-contents text-center"><a role="button" ng-click="grid.appScope.onClickCell(row, col)">{{ col.displayName CUSTOM_FILTERS }}</a></div>',
					cellTemplate : '<div class="ui-grid-cell-contents text-center" style="padding-top: 12px;">{{ COL_FIELD }}</div>'
				};
			
			gridOptions.columnDefs.push(columnDef);
		}
		
		for(let i=0; i < target.length ; i++) {
//			var record = { 'col-0' : labels[i] }
			var record = {};
			var cols = target[i];
			for(let j=0; j < cols.length ; j++) {
				var field = 'col-' + j;
				record[field] = cols[j];
			}
			
			gridOptions.data.push(record);
		}
		gridOptions.minRowsToShow = Math.max(10, gridOptions.data.length);
		$timeout(function () {
			$scope.gridApi.grid.handleWindowResize();  
			$scope.gridApi.core.refresh();
		}, 1000);
	};
	
	$scope.onClickCell = function(row, col) {
		console.info(row);
		console.info(col);
		if(row != null) {
			$scope.onClickFileDetail(row.entity['col-0']);
		} else if(col != null) {
			$scope.onClickFileDetail(col.displayName);
		}
	};

});
