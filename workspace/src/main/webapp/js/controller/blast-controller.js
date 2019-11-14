/**
 * 
 */
gdkmApp.controller('blastExecuteController', function ($scope, $http, $location, $rootScope, $timeout, Upload, toolsFactory) {
	
$scope.blastTools = ["blastn", "blastp", "blastx", "tblastn", "tblastx"];
	
	$scope.blastDbs = ["dna_all", "protein_all"];
	
	$scope.blastEvent = {
		uploadString:"",
		uploadFileValue:"test",
		toolName:"blastn",
		dbNames:$scope.blastDbs[0],
		expect:"1e-5",
		filter:"T"
	}
	$rootScope.summary = null;
	
	$scope.params = {
		page: 0,
		rowSize: 10,
		keyword:"",
		content:""
	};
	
	$scope.typeRadio = "dna";
	$scope.dbRadio = "dna";
	$scope.tools = [];
	$scope.expects = ["1e-5", "1e-3", "1e-1", "0", "1", "10"];
	$scope.filters = ["T", "F"];
	$scope.dbList = [];
	$scope.selectDbList = [];
	$scope.uploadFiles=[];
	
	$scope.init = function(){
//		getDbList();
		$scope.changeRadioButton();
	}
	
	var getDbList = function() {
		toolsFactory.getDbList($scope.params, $scope.dbRadio).then(
    		function(res) {
    			$.each(res.data, function(index, item) {
    				var obj = {"name":item, "_check":false};
    				$scope.dbList.push(obj);
    			})
    			console.log($scope.dbList);
    		},
    		function(err) {
    			$log.error(angular.toJson(err, true));
    		}
    	);
	}
	
	// db check box 바꾸기
	$scope.changeRadioButton = function() {
		 switch($scope.dbRadio) {
	      case "dna":
	    	  $scope.dbNames = [$scope.blastDbs[0]];
	      if($scope.typeRadio === 'dna')
	    	  $scope.tools = [$scope.blastTools[0], $scope.blastTools[4]];
	      else if($scope.typeRadio === 'protein')
	    	  $scope.tools = [$scope.blastTools[3]];
	      break;
	      case "protein":
	    	  $scope.dbNames = [$scope.blastDbs[1]];
	      if($scope.typeRadio === 'dna')
	    	  $scope.tools = [$scope.blastTools[2]];
	      else if($scope.typeRadio === 'protein')
	    	  $scope.tools = [$scope.blastTools[1]];
	      break;
	      default:
	    	  $scope.dbNames = [$scope.blastDbs[0]];
	    	  $scope.tools = [$scope.blastTools[0], $scope.blastTools[4]];
	      break;
	    }
		 $scope.blastEvent.dbNames = $scope.dbNames[0];
		 $scope.blastEvent.toolName = $scope.tools[0];
		
	}
	
	// blast run
	$scope.submit = function() {
		
//		$scope.blastEvent.dbNames = [];
//		$.each($scope.selectDbList, function(index, selectDb) {
//			$scope.blastEvent.dbNames.push(selectDb.name);
//		})
		
		if($scope.dbRadio == 'dna')
			$scope.blastEvent.dbNames = ['dna_all'];
		else if ($scope.dbRaio == 'protein')
			$scope.blastEvent.dbNames = ['protein_all'];
		
		$rootScope.summary = {
			queryCount: 0,
			toolName:$scope.blastEvent.toolName,
			dbNames:$scope.blastEvent.dbNames,
			dbType:$scope.dbRadio,
			filter:$scope.blastEvent.filter,
			expect:$scope.blastEvent.expect
		}
		
		toolsFactory.postBlast($scope.blastEvent).then(
			function(res) {
				console.log(res.data);
				$rootScope.blastEvent = res.data;
				console.log($rootScope.blastEvent);
			}, function(err) {
				console.log(err);
			}
		);
		$location.path("/tools/blast-result");
	}
	
	$scope.reset = function() {
		$scope.typeRadio = "dna";
		$scope.dbRadio = "dna";
		$scope.tools = [];
		$scope.dbList = [];
		$scope.selectDbList = [];
		$scope.uploadFiles=[];
		$scope.blastEvent = {
			uploadString:"",
			uploadFileValue:"test",
			toolName:"blastn",
			dbNames:[],
			expect:"1e-5",
			filter:"T"
		}
	}
	
	$scope.onclickTestBtn = function() {
		$scope.blastEvent.uploadString = ">NZ_CP016602.1 Lactobacillus curvatus strain WiKim52, complete genome\n"
										+"AACCTCGCGGTCTCGCGACTCGTTGTACCATCCATTGTAGCACGTGTGTAGCCCAGGTCATAAGGGGCATGATGATTTGA"
										+"CGTCGTCCCCACCTTCCTCCGGTTTGTCACCGGCAGTCTCACTAGAGTGCCCAACTAAATGCTGGCAACTAGTAATAAGG"
										+"GTTGCGCTCGTTGCGGGACTTAACCCAACATCTCACGACACGAGCTGACGACAACCATGCACCACCTGTCACTTTGTCCC"
										+"CGAAGGGAAAGCTCTATCTCTAGAGTGGTCAAAGGATGTCAAGACCTGGTAAGGTTCTTCGCGTTGCTTCGAATTAAACC"
										+"ACATGCTCCACCGCTTGTGCGGGCCCCCGTCAATTCCTTTGAGTTTCAACCTTGCGGTCGTACTCCCCAGGCGGAGTGCT"
										+"TAATGCGTTAGCTGCGGCACTGAAGGGCGGAAACCCTCCAACACCTAGCACTCATCGTTTACGGCATGGACTACCAGGGT"
										+"ATCTAATCCTGTTTGCTACCCATGCTTTCGAGCCTCAGCGTCAGTTACAGACCAGACAGCCGCCTTCGCCACTGGTGTTC"
										+"TTCCATATATCTACGCATTTCACCGCTACACATGGAGTTCCACTGTCCTCTTCTGCACTCAAGTTCCCCAGTTTCCGATG"
										+"CACTTCTTCGGTTGAGCCGAAGGCTTTCACATCAGACTTAAGAAACCGCCTGCGCTCGCTTTACGCCCAATAAATCCGGA"
										+"CAACGCTTGCCACCTACGTATTACCGCGGCTGCTGGCACGTAGTTAGCCGTGGCTTTCTGGTTGGATACCGTCACTACCT"
										+"GATCAGTTACTATCAAATACGTTCTTCTCCAACAACAGAGTTTTACGATCCGAAAACCTTCTTCACTCACGCGGCGTTGC"
										+"TCCATCAGACTTTCGTCCATTGTGGAAGATTCCCTACTGCTGCCTCCCGTAGGAGTCTGGGCCGTGTCTCAGTCCCAGTG"
										+"TGGCCGATTACCCTCTCAGGTCGGCTATGCATCACGGTCTTGGTGAGCCTTTACCTCACCAACTAACTAATGCACCGCGG"
										+"GTCCATCCTAAAGTGATAGCCGAAACCATCTTTCAACCTTGCACCATGCGGTGCTAGGTTTTATGCGGTATTAGCATCTG"
										+">NZ_ALPU01000001.1 Staphylococcus sp. OJ82 155.SOJ.1_1, whole genome shotgun sequence\n"
										+"ACTTTAATGTTGTGTTCGGAATTAACGTTGACATATTGCAATTCAGTTTTCAATGTTCATTTCTTAATCACAAGACTTAA"
										+"TTATACAGGTTATAACTTGAAAAGTCAATAACTTTTTAAAATTAATTTTCGTTGTGTTTCGTATTTCTTTTTGTGTTTGT"
										+"CGTGTTGTTTCGCTCGACTAGTAATACTATACACGCATAAAAACCTTTTAACAACAGTAAAATTTACACTCTAACCGTCT"
										+"CACAACAACCTTTGAACACTAATAAGCATACTTTATTATAATAATTCATATTAAAGTTCGTTTTATAACCAGTAAACATC"
										+"TAAAATCTATCTTCAAACCATTGGAATACACTTGTTACGCGTACTTTAAATTCTTTTACATATTTTTAATTTTATTTACA"
										+"AATAATTAAAGCTACCTATATAGGTAGCTTTAATCGTTCATATATATATGTTTAATAATATACTACATCTCTGTTCGGCC"
										+"CATAATCGCTTTCGACAATGTAACTTCATCAGCGTATTCTAAATCGCCACCTACAGAAAGACCTTGAGCTAATCTCGTTA"
										+"CAGTAACACCAATTGGTTTTACTAACCTCGAGATATACATCGCTGTAGATTCACCTTCTAGGTTTGGATTCATCGCTAAG"
										+"ATAAGCTCTTTAACTTCTTCATCTTTCAAACGATTAATTAAACTGGGTATATTAATATCCTCTGGTCCAATACCATCCAT"
										+"TGGAGAAATAGAGCCATGTAACACGTGATATAAACCTTTATATTCACGCATCTTCTCCATAGCAATGACATCTTTATCAT"
										+"CCTCAACTACACAAATAATTGTGCGATCTCTCTGTTTATCTTGGCAAATGTAACATGGATCTTGTTCAGTTATATGTCCA"
										+">NZ_ALPU01000002.1 Staphylococcus sp. OJ82 155.SOJ.1_2, whole genome shotgun sequence\n"
										+"TATAAAACGCGTTCTCCTTTTTTATAAATACTTTAATTTCACTACCTAACTCACTCTACATACCCTAGTTCATTAACTAC"
										+"TTTGACCATGTCGTCATGAATGACTAAAGTTGCGTGGTTATCATAAGGTGTATGGTCTTTATTAATGATAACTAGGTTCT"
										+"GTCCTTGAAATTTCGAAATAAGTCCTGCTGCTGGTTGAACTACGAGTGACGAACCTAGGACGACTAAAGTATCGGCTTCT"
										+"TTTATTTTATCTAGTGCATTATAAATCGTATTTTGATTTAATAATTCTCCATAAAGTACAATATCAGGTCTAATAGGACT"
										+"TCCACACACTTCACATTCGCGTAAATGATGCTTCATCACATAATATTTGAAATATGCCTTATGGCAGTCAATACAATAGA"
										+"AATGATTTAACGTACCATGCAGTTCATCTACATTAAGACTCCCCGCATCAGAATGTAGACCGTCGATGTTTTGAGTGATA"
										+"ACCCCTAATGAACGTTTATCTTGTTCTAGTTGAGCAATCCATTGATGAACAAGGTTCGGCTGTTTATCAGCTAAAAGTAA"
										+"ACGTTTATGATAAAAATCAACAAATCTTACTGGGTCATCTTGAAGATAATCCGTACTTAATAAATATTCAGGCGCGTAGC"
										+">NZ_ALPU01000003.1 Staphylococcus sp. OJ82 155.SOJ.1_3, whole genome shotgun sequence\n"
										+"TGCAGGTGAAACGCCAAATACAGGTGAAACACCAAAAACAGAACAAACAGCAAGTATAGGTGAAAAATCAAATACAAACG"
										+"AAACATCAAGTAAAGAAGAAACTCAAAGCAAATCACTAGATATAATGTCACAAGAGAATTCAACAATAGAAAAAACTAAC"
										+"AGCAATTCTACTGTGAATAAAAACACCACTAATTCAAGCATTAACAAAGGTGAATCAAAACGTGCTCATAGCAACAAACT"
										+"TAATTCAGAAAATGAATTGCCGGAAACAGGTCAAGATGAAACACGTAATAGCACTTTATTCGGCACATTAATTGCAGGTT"
										+"TAGGCGGCTTACTATTGCTTGTGATGCGTCGTAGAAAAAAAGAAGAAGACGAAAACAAAATGTCATAATCAAAGTGATAT"
										+"TGTAAAGGCTCTTCATCCATTAAGGGTGGAGAGCCTTTTCTTATCAAAATTAAAACATTATATAGGAAAGCGCTTACTTT"
										+"ATTTTTTTGAGGGTATTAGACTCAATAAAGGAGGTTTTACAGTGATATATACAAATCCAAATCAAGAAGATGCAAAATAC"
										+"AAAGTAAAATCGAAATATGCTAATTTTATAGGTGGAAAATGGACTGAACCACAAGAAGGGAACTATTTTGATAATATTTC"
										+"ACCAGTTACAGGTGAAAGTTATGCCAAGATACCTAAGTCTACACAAAAAGATGTGGACTTAGCTGTTGAAGCTGCCCAGA"
										+"AAGCGCAAGTAGAATGGGGTAAAAAATCATTATCTGAAAGATCACAGTTATTATTAGATATAGCTGATCGTATTGAAGAA"
										+"AATTTAGAATATTTAGCAGTGATAGAAACGTTTGATAATGGAAAGCCAGTGAGAGAAACACTTACAGCAGATTTACCATT"
										+">NZ_ALPU01000004.1 Staphylococcus sp. OJ82 155.SOJ.1_4, whole genome shotgun sequence\n"
										+"TTTTGTACATTGAAAACTAGATATGTAAGTAAAATTTATGATTTTACCAAGCAAAACCGAGTGAATTAGAGTTTTAAAAA"
										+"GCTTGAATTCAAAAAATAGAAATAATCGCTAGTGTTCGAAAGAACACTCACAGATTAATAACATTTTAGGTTTTCAACCG"
										+"ATTTTATCGTGTTGAAGGTCGAAAAGATTAAGTTATTAAGGGCGCACGGTGGATGCCTTGGCACTAGAAGCCTACGAAGG"
										+"ACGTTACTAACGACGATATGCTTTGGGGAGCTGTAAGTAAGCTTTGATCCAGAGATTTCCGAATGGGGAAACCCAACACG"
										+"AGTTATGTCGTGTTATCGATATGTGAATACATAGCATATCTGAAGGCAGACGCGGGGAACTGAAACATCTTAGTACCCGC"
										+"AGGAAGAGAAAGAAAAATCGATTCCCTGAGTAGCGGCGAGCGAAACGGGAAGAGCCCAAACCAACGAGCTTGCTTGTTGG"
										+"GGTTGTAGGACACTCTATACGGAGTTACAAAAGAACAAATTAGACGAATCATCTGGAAAGATGAATCAAAGAAGGTAATA"
										+"ATCCTGTAGTCGAAAGTTTGTTCACTCTTGAGTGGATCCTGAGTACGACGGAACACGAGAAATTCCGTCGGAATCCGGGA"
										+"GGACCATCTCCCAAGGCTAAATACTCTCTAGTGACCGATAGTGAACCAGTACCGTGAGGGAAAGGTGAAAAGTACCCCGG"
										+"AAGGGGAGTGAAATAGAACTTGAAACCGTGTGCTTACAAGTAGTCAGAGCCCGTTAATGGGTGATGGCGTGCCTTTTGTA";
	}
	
	$scope.onclickDbList = function() {
		$.each($scope.dbList, function(index, db) {
			db._checked = false;
		})
//		getDbList();
		$("#db-list-popup").modal('show');
	}
	
	$scope.onclickModalSubmit = function() {
		console.log("onclickModalSubmit");
		var selectList = [];
		$.each($scope.dbList, function(index, db) {
			if(db._checked)
				selectList.push(db);
		})
		$scope.selectDbList = selectList;
	}
	
	$scope.onclickUploadFile = function() {
		console.log($scope.uploadFiles);
		
		if($scope.uploadFiles != null){
			
			if($scope.uploadFiles.size > 1500000) {
				alert("파일 사이즈가 1MB를 초과합니다. ");
				$scope.uploadFiles = null;
			}     
			
			let reader = new FileReader(); 
			
			reader.onload = function(e) {
				$scope.blastEvent.uploadString = reader.result;
			}
			
			reader.readAsText($scope.uploadFiles);
			
			$timeout(function () {
				console.log($scope.blastEvent.uploadString)
			}, 500 /* 1000ms 뒤에 지연된 작업이 수행된다. */, true );
		}
		
	}
	
})

gdkmApp.controller('blastResultController', function ($scope, $http, $rootScope, $timeout, $log, 
		toolsFactory, achiveFactory, depositFactory, taxonFactory) {
	
	$scope.blastResultTab = [];
	$scope.pageHandler = PageHandler.create();
	$scope.checkAll = false;
	
	$scope.selectGbkItems = [];
	/**
	 * NGS 데이터 등록정보 연계데이터 목록 핸들러
	 */
	$scope.linkedHandler = ModelListHandler.create($timeout);
	
	$scope.display = {
		editPopupId : '',
		detailPopupId : '',
		accordions : [true, false, false],
	};
	
	$scope.rowSizeCodeList = [
		{ id: 10, name: '10 of each'},
		{ id: 20, name: '20 of each'},
		{ id: 30, name: '30 of each'},
		{ id: 50, name: '50 of each'},
		{ id: 100, name: '100 of each'},
	];
	$scope.params = {
		page: 0,
		rowSize: 10
	};

	$scope.modal = {
		subjectId : "",
		checkAll : false,
		_activeFeaturesRows : [],
		achive : {},
		subjectStart:"",
		subjectEnd:""
	}
	var _activeRows = [];
	
	$scope.init = function(){
		if($rootScope.summary == null)
			return;
		
		waitingDialog.show("Loading ...");
		$timeout(timer, 1000);  
	}
	
	 //timer callback
    var timer = function() {
    	console.log($rootScope.blastEvent);
        if($rootScope.blastEvent == null) {
            $timeout(timer, 1000);
        } else {
        	toolsFactory.getBlastOutputTabFile($rootScope.blastEvent.fileName).then(
				function(res) {
					$scope.blastTabResults = res.data;
					let querySet = new Set();
					$.each($scope.blastTabResults, function(index, blastTabResult){
						$.each(blastTabResult.results, function(index, result) {
							querySet.add(result.queryId);
						})
					})
					$rootScope.summary.queryCount = querySet.size;
					$scope.pageHandler.setPageChangeListener(pageChange);
					
					console.log($scope.blastTabResults);
					waitingDialog.hide();
				}, function(err) {
					console.log(err);
				}
			);
        }
    }
    
    // panel 관련 함수
    $scope.panel_h_css = function(id) {
		return (_activeRows.indexOf(id) < 0) ? '' : 'active';
	};
	$scope.panel_collapse_css = function(id) {
		return (_activeRows.indexOf(id) < 0) ? 'collapse' : '';
	};
	
	$scope.toggleActiveBlastRows = function(results) {
		
		if(_activeRows.length == results.length) {
			_activeRows = [];
		} else {
			$.each(results, function(index, result) {
				if(_activeRows.indexOf(result.achive.id) < 0)
					_activeRows.push(result.achive.id);
			})
		}
	};
	$scope.activeRow = function (id) {
		if(id == null)
			_activeRows = [];
		
		if (_activeRows.indexOf(id) < 0) {
			_activeRows.push(id);
		} else {
			_activeRows.splice(_activeRows.indexOf(id), 1);
		}
    };
    
    // feature panel 함수
    $scope.toggleActiveFeaturesRows = function(results) {
		if($scope.modal._activeFeaturesRows.length == results.length) {
			$scope.modal._activeFeaturesRows = [];
		} else {
			$.each(results, function(index, result) {
				if($scope.modal._activeFeaturesRows.indexOf(result.featuresId) < 0)
					$scope.modal._activeFeaturesRows.push(result.featuresId);
			})
		}
		console.log(results.length + ", " + $scope.modal._activeFeaturesRows.length);
	};
	$scope.activeFeaturesRows = function (featuresId) {
		
		if(featuresId == null)
			$scope.modal._activeFeaturesRows = [];
		else {
			if ($scope.modal._activeFeaturesRows.indexOf(featuresId) < 0) {
				$scope.modal._activeFeaturesRows.push(featuresId);
			} else {
				$scope.modal._activeFeaturesRows.splice($scope.modal._activeFeaturesRows.indexOf(featuresId), 1);
			}
		}
    };
    $scope.panel_h_css_features = function(featuresId) {
		return ($scope.modal._activeFeaturesRows.indexOf(featuresId) < 0) ? '' : 'active';
	};
	$scope.panel_collapse_css_features = function(featuresId) {
		return ($scope.modal._activeFeaturesRows.indexOf(featuresId) < 0) ? 'collapse' : '';
	};
    // /panel 관련 함수
    
    $scope.onClickGbkDetail = function (result, achive) {
    	console.log(result);
    	
    	$scope.modal.subjectId = result.subjectId;
    	$scope.modal.checkAll = false;
    	$scope.modal.achive = achive;
    	$scope.modal.subjectStart = result.subjectStart;
    	$scope.modal.subjectEnd = result.subjectEnd;
    	
    	if(result.subjectStart>result.subjectEnd){
    		$scope.modal.subjectStart = result.subjectEnd;
    		$scope.modal.subjectEnd = result.subjectStart;
    	}
    	
    	$scope.pageHandler.numberOfRows = 10;
    	pageChange();
    	getSequence();
    	
    	$.each($scope.features, function(index, feature) {
	    	feature._checked = $scope.modal.checkAll;
	    })
	    
    	$("#blast-gbk-detail-popup").modal('show');
    }
   
    var getSequence = function() {
    	console.log($scope.modal.achive);
    	var params = {
    		subjectStart : $scope.modal.subjectStart,
    		subjectEnd : $scope.modal.subjectEnd,
    		subjectId : $scope.modal.subjectId
    	}
    	toolsFactory.getSequence($scope.modal.achive.id, params).then(
    		function(res) {
    			console.log(res.data);
    			$scope.modal.sequenceGroup = chunkString(res.data[0], 50);
    			$timeout(function() {
    				var els = document.querySelectorAll('.character-justify');
    				$.each(els, function(index, el) {
    					justify(el);
    				})
    			}, 100);
    		}, function(err) {
    			console.log(err);
    		}
    	)
    	
    }
    
    var chunkString = function(str, size) {
        const numChunks = Math.ceil(str.length / size)
        const chunks = new Array(numChunks)
        for (let i = 0, o = 0; i < numChunks; ++i, o += size) {
            chunks[i] = str.substr(o, size)
        }
        return chunks;
    }
    
    var pageChange = function() {
    	$scope.params.page = $scope.pageHandler.getCurrentPage();
    	$scope.params.rowSize = $scope.pageHandler.numberOfRows;
    	$scope.params.subjectStart = $scope.modal.subjectStart;
    	$scope.params.subjectEnd = $scope.modal.subjectEnd;
    	console.log($scope.modal.achive);
    	toolsFactory.getFeaturesPageChange($scope.modal.subjectId, $scope.params).then(
			function(res) {
				console.log(res.data);
				$scope.features = res.data.list;
				$scope.pageHandler._total = res.data.total;
				$scope.toggleActiveFeaturesRows($scope.features);
				$scope.activeFeaturesRows();
				
				$.each($scope.features, function(index, feature) {
					feature._checked = false;
					feature.length = Math.abs(feature.end - feature.start);
				})
				
				splitProgressbar($scope.features);
				console.log($scope.features);
				waitingDialog.hide();
			}, function(err) {
				console.log(err);
			}
    	);
    }
    
    // checkbox 관련 함수
    $scope.handleAutoSelectClick = function() {
//	    $scope.checkAll = !$scope.checkAll;
	    $.each($scope.features, function(index, feature) {
	    	feature._checked = $scope.modal.checkAll;
	    	console.log(feature._checked);
	    })
	}

	$scope.changeCheckBox = function() {
	    $scope.modal.checkAll = true;
	    $.each($scope.features, function(index, feature) {
	    	if(!feature._checked){
	    		$scope.modal.checkAll = false;
	    		return;
	    	}
	    })
	}
	
	$scope.downloadFile = function(type) {
		switch(type) {
		case 'fasta': break;
		case 'gbk': 
			$.each($scope.features, function(index, feature) {
				if(feature._checked){
					if($scope.selectGbkItems.indexOf(feature) < 0)
						$scope.selectGbkItems.push(feature);
				}
			})
			console.log($scope.selectGbkItems);
			console.log($scope.modal.achive);
			toolsFactory.postGbkFileDownload($scope.selectGbkItems, $scope.modal).then(
				function(res) {
					const blob = new Blob([res.data], { type: 'application/octet-stream' });
  
					var downloadUrl = window.URL.createObjectURL(blob);
					let isIEOrEdge = /msie\s|trident\/|edge\//i.test(window.navigator.userAgent);

					//explore 일떄
					if(isIEOrEdge) {
						window.navigator.msSaveOrOpenBlob(blob, $scope.modal.achive.dataRegist.registNo + "_custom.gbk");
						return ;
					}

					var link = document.createElement('a');
					link.href = downloadUrl;
					link.download = $scope.modal.achive.dataRegist.registNo + "_custom.gbk";
					link.click();

				}, function(err) {
					console.log(err);
				}
			)
			break;
		default: alert("잘못된 타입 - " + type);
		}
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
	// 자간 균등 배분 함수 
	var justify = function (el) {
		var TEXT_NODE = 3;
		el = el.firstChild;
		while(el) {
			var nextEl = el.nextSibling;
			console.log(el.nodeType);
			
			if (el.nodeType == TEXT_NODE)
				splitText(el);
			
			el = nextEl;
		}
	}

	var splitText = function (node) {
		var text = node.nodeValue.replace(/^\s*|\s(?=\s)|\s*$/g, '');
	    for(var i = 0; i < text.length; i++) {
	      var letter = document.createElement('span');
	      letter.style.display = 'inline-block';
	      letter.style.position = 'absolute';
	      letter.appendChild(document.createTextNode(text.charAt(i)));
	      node.parentNode.insertBefore(letter, node);
	  
	      var positionRatio = i / (text.length - 1);
	      var textWidth = letter.clientWidth;
	  
	      var indent = 100 * positionRatio;
	      var offset = -textWidth * positionRatio;
	      letter.style.left = indent + '%';
	      letter.style.marginLeft = offset + 'px';
	  
	      console.log(`Letter: ${text[i]}, Index : ${i}, Width: ${textWidth}, Indent: ${indent}, Offset: ${offset}`);
	    }
	  
	    node.parentNode.removeChild(node);
	}

	var splitProgressbar = function(features) {
		var gap = Math.abs($scope.modal.subjectStart - $scope.modal.subjectEnd);
		var percent = gap / 100;
		$scope.modal.geneStartPercent = null;
		$scope.modal.geneEndPercent = null;
		console.log("gap : " + gap + ", percent : " + percent);
		$.each(features, function(index, feature) {
			if(feature.type == 'gene') {
				if($scope.modal.geneStartPercent != 0 && $scope.modal.subjectStart <= feature.start) {
					$scope.modal.geneStartPercent = feature.start / percent / 100;
					console.log("$scope.modal.geneStartPercent: " + $scope.modal.geneStartPercent);
				} 
				if($scope.modal.geneEndPercent != 100 && $scope.modal.subjectEnd > feature.end) {
					$scope.modal.geneEndPercent = feature.end / percent / 100;
					console.log("$scope.modal.geneEndPercent: " + $scope.modal.geneEndPercent);
				}
			}
		});
		
		if($scope.modal.geneStartPercent == null)
			$scope.modal.geneStartPercent = 0;
		if($scope.modal.geneEndPercent == null)
			$scope.modal.geneEndPercent = 100;
		
		console.log("$scope.modal.geneStartPercent: " + $scope.modal.geneStartPercent);
		console.log("$scope.modal.geneEndPercent: " + $scope.modal.geneEndPercent);
	}
	
	$scope.progressBarWidth = function(percent) {
		console.log(percent + "%");
		console.log($scope.modal.geneEndPercent);
		return {
            "width" : percent+"% !important"
          };
	}
	
	/**
	 * FASTA 다운로드
	*/
	$scope.downloadSequence = function() {
	    if(!$scope.modal.sequenceGroup) {
	    	alert("서열정보가 없습니다.");
	    	return ;
	    }
	    dyanmicDownloadByHtmlTag();
	}

	$scope.setting = {
		element: {
			dynamicDownload: null
	    }
	}
	
	var dyanmicDownloadByHtmlTag = function() {
//		$scope.modal.achive.filename + '_custom.fasta', $scope.modal.achive.subjectId + '\n' + $scope.
		var fileName = $scope.modal.achive.fileName + '_custom.fasta';
		var text = ">"+ $scope.modal.subjectId + '\n' + $scope.modal.sequenceGroup;
	    if (!$scope.setting.element.dynamicDownload) {
	      $scope.setting.element.dynamicDownload = document.createElement('a');
	    }
	    const element = $scope.setting.element.dynamicDownload;
//	    const fileType = fileName.indexOf('.json') > -1 ? 'text/json' : 'text/plain';
	    const fileType = 'text/plain';
	    element.setAttribute('href', `data:${fileType};charset=utf-8,${encodeURIComponent(text)}`);
	    element.setAttribute('download', fileName);

	    var event = new MouseEvent("click");
	    element.dispatchEvent(event);
	  }
	
})