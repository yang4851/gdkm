gdkmApp.controller('homeController', function($scope, $location, $log, $timeout, $cacheFactory, 
		notificationService, statisticsFactory, excelFactory) {
	
	/**
	 * 종별 등록현황 목록
	 */
	$scope.taxonStatList = [];
	
	$scope.taxonChart = {
			type : "AreaChart",
			cssStyle : "height: 515px; width: 280px;",
			options : {
				width: 515,
				height: 280,
				legend: { position: 'none' },
				hAxis: {
					slantedText: true,
					slantedTextAngle: 20
				},
				isStacked : true,
			},
			data : {
				cols: [
					{ id: "taxon", label: "Species", type: "string" },
					{ id: "count", label: "Count", type: "number" }
				], 
				rows: [
					{ c: [ {v: ""}, {v: 0 } ] }
				]	
			}
	};
	
	/**
	 * 연도및 데이터별 등록현황 목록
	 */
	$scope.registStatList = [];
	
	$scope.registChart = {
		type : "ColumnChart",
		cssStyle : "height:570px; width:340px;",
		options : {
			width: 570,
			height: 340,
			bars : 'horizontal',
			legend: { position: 'right' },
			chartArea:{left:50,top:50,width:'70%',height:'75%'}
		},
		data : {
			cols : [
				{ id: 'period',	  type: 'string', label: 'Month' },
				{ id: 'rawCount', type: 'number', label: 'Raw data' },
				{ id: 'rawTip1',  type: 'string', role:  'annotation' },
				{ id: 'proCount', type: 'number', label: 'Annotation data' },
				{ id: 'proTip2',  type: 'string', role:  'annotation' },
			],
			rows : [
			    { c : [ {v: new Date().getFullYear()}, {v: 0}, {v: 0} ] }
			]
		}
	};
	
	/**
	 * 데이터 등록현황 요약정보
	 */
	$scope.registStatSummary = {
		'taxonCount' : 0,
		'registCount' : 0,
		'lastUpdated' : null,	
	};
	
	/**
	 * 통계정보 검색 조건 
	 */
	$scope.statParams = {
		dataType : '',
		year : ''
	};
	
	$scope.params = new SearchParams();
	
	$scope.init = function() {
		var params = {
			registStatus : REGIST_STATUS.SUCCESS,
//			openYn : angular.isObject($scope.loginUser) ? '' : 'Y',
		};
		
		_loadTaxonStatList(params);
		_loadRegistStatList();
		_loadMonthlyStatList();
		_loadRegistSummary(params);
	};
	
	/**
	 * 메인화면의 통합검색 버튼 클릭 이벤트 처리
	 * 
	 * integrated-view로 라우팅처리
	 * integratedController의 init() 함수에서 캐시를 읽어들여 처리 
	 */
	$scope.onClickSearchBtn = function() {
		var cache = Utils.getCache($cacheFactory);
		if(angular.isDefined(cache)) {
			$scope.params.product = $scope.params.gene;
			cache.put(CACHE_PROP.SEARCH_PARAMS, angular.copy($scope.params));
			
			waitingDialog.show('Searching ...');
			$location.path('/integrated-view');
		}
	};
	
	$scope.onClickStatisticsBtn = function() {
		_loadRegistStatList();
		_loadMonthlyStatList();
	};
	
	var itoStr = function(num){
        num < 10 ? num = '0'+num : num;
        return num.toString();
    };
	
	$scope.onClickStatisticsDownloadBtn = function(tableId){
		
        var exportHref=excelFactory.tableToExcel(tableId,'DataRegistrationStatistics');
        $timeout(function(){
        	var a = document.createElement('a');
        	a.download = "Statistics.xls";
        	a.href=exportHref;
        	a.click();
        },100); // trigger download
        
		/*var dt = new Date();
        var year = itoStr( dt.getFullYear() );
        var month = itoStr( dt.getMonth() + 1 );
        var day =  itoStr( dt.getDate() );
        var hour = itoStr( dt.getHours() );
        var mins = itoStr( dt.getMinutes() );

	    var postfix = year + month + day + "_" + hour + mins;
	    var fileName = "Statistics_"+ postfix + ".xls";

        var a = document.createElement('a');
        var data_type = 'data:application/vnd.ms-excel';
        var table_div = $("#tblExport");
        var table_html = table_div.html().replace(/ /g, '%20');

	    a.href = data_type + ', ' + table_html;
	    a.download = fileName;
	    a.click();*/
	    
    };
	
	var _loadTaxonStatList = function(params) {
		
		statisticsFactory.getTaxonStatList(params).then(
			function(result) {
				$scope.taxonStatList = result;
				
				var rows = [];
				var columns = [];
				
				columns[0] = { v: "" };
				columns[1] = { v: 0 };
				rows.push({ c: columns });
				
				for(var i=0; i < result.length ; i++) {
					columns = []; 
					columns[0] = { v: result[i].taxonomy.name };
					columns[1] = { v: result[i].fileCount }; 
					rows.push({ c: columns });
				}
				
				columns = [];
				columns[0] = { v: "" };
				columns[1] = { v: 0 };
				rows.push({ c: columns });
				
				$scope.taxonChart.data.rows = rows;
			}
		);
	};
	
	var _loadRegistStatList = function() {
		params = {
				fromYear : $scope.statParams.year,
				toYear : $scope.statParams.year,
				dataType : $scope.statParams.dataType,
				registStatus : REGIST_STATUS.SUCCESS,
//				openYn : angular.isObject($scope.loginUser) ? '' : 'Y',
			};

		statisticsFactory.getRegistStatList(params).then(
			function(result) {
				$scope.registStatList = result;
			}
		);
	};
	
	var _loadMonthlyStatList = function() {
		var params = {
				fromYear : $scope.statParams.year,
				toYear : $scope.statParams.year,
				period : 'monthly',
				registStatus : REGIST_STATUS.SUCCESS,
//				openYn : angular.isObject($scope.loginUser) ? '' : 'Y',
			};
		
		statisticsFactory.getRegistStatList(params).then(
			function(result) {
				var rows = [];
				for(var i=0; i < result.length ; i++) {
					var columns = []; 
					columns[0] = { v: result[i].period };
					columns[1] = { v: result[i].rawCount }; 
					columns[2] = { v: result[i].rawCount + '' };
					columns[3] = { v: result[i].processedCount };
					columns[4] = { v: result[i].processedCount + '' };
					rows.push({ c: columns });
				}
				
				$scope.registChart.data.rows = rows;
			}
		);
	};
	
	var _loadRegistSummary = function(params) {
		statisticsFactory.getRegistSummary(params).then(
			function(result) {
				$scope.registStatSummary = result;
			}
		);
	};
	
	/**
	 * 데이터가 있는지 유무 
	 */
	$scope.disabledDownloadExcelBtn = function(){
		if(Utils.isBlank($scope.registStatList)){
			return true;
		}else{
			return false;
		}
	}
});