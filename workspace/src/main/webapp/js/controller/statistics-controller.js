gdkmApp.controller('statisticsController', function($scope, $location, $log, $timeout,  
		notificationService, statisticsFactory, excelFactory) {
	
	/**
	 * 종별 등록현황 목록
	 */
	$scope.taxonStatList = [];
	
	$scope.taxonChart = {
			type : "ColumnChart",
			cssStyle : "height:100%; width:100%;",
			options : {
				title : 'Data Registration',
				width: '100%',
				height: 500,
				bars : 'horizontal',
				legend: {
					position: 'left'
				},
				hAxis: {
					slantedText: true,
					slantedTextAngle: 20
				},
			},
			data : {
				cols : [
					{ id: 'taxonomy',	type: 'string',	label: 'Species' },
					{ id: 'count1',		type: 'number',	label: 'Count1' },
					{ id: 'tooltip1',	type: 'string', role: 'annotation' },
					{ id: 'count2',		type: 'number',	label: 'Count2' },
					{ id: 'tooltip2',	type: 'string', role: 'annotation' }
				],
				rows : [
				    { c : [ {v: 'Lactobacillus acidophilus NCFM'}, {v: 58}, {v: '58 건'}, {v: 20}, {v: '20 건' }] },
				    { c : [ {v: 'Lactobacillus acidophilus ATCC 4796'}, {v: 50}, {v: '50 건'}, {v: 30}, {v: '30 건'}] }
				]
			}
	};
	
	
	
	/**
	 * 연도별 데이터 등록현황 목록
	 */
	$scope.yearStatList = [];
	
	$scope.yearChart = {
			type : "ColumnChart",
			cssStyle : "height:100%; width:100%;",
			options : {
				title : 'Year',
				width: 1148,
				height: 500,
				bars : 'horizontal',
				legend: {
					position: 'right'
				},
			},
			data : {
				cols : [
					{ id: 'period',	  type: 'string', label: 'Year' },
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
	 * 월별 데이터 등록현황 목록
	 */
	$scope.monthStatList = [];	

	$scope.monthChart = {
			type : "ColumnChart",
			cssStyle : "height:100%; width:100%;",
			options : {
				title : 'Month',
				width: 1148,
				height: 500,
				bars : 'horizontal',
				legend: {
					position: 'right'
				},
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
	
	$scope.areaChart = {
			type : "AreaChart",
//			cssStyle : "width: 570px; height: 264px;",
			options : {
				title : "Number of genome released",
				width : 568, 
				height: 300,
				chartArea:{left:50,top:50,width:'68%',height:'70%'},
				legend: { position: 'right' },
				isStacked : true,
			},
			data : {
				cols: [
					{ id: "period", label: "Year", type: "string" },
					{ id: "rawCount", label: "Raw data", type: "number" },
					{ id: "proCount", label: "Annotation data", type: "number" },
				], 
				rows: [
					{ c: [ {v: "Total"}, {v: 0 }, {v: 0 }, {v: 0 } ] }
				]	
			}
	};
	
	/**
	 * 통계정보 검색 조건 
	 */
	$scope.statParams = {
		dataType : '',
		fromYear : '',
		toYear : ''
	};
	
	$scope.init = function() {
		_loadTaxonStatList();
		_loadYearStatList();
		_loadMonthStatList();
	};
	
	$scope.search = function() {
		_loadYearStatList();
		_loadMonthStatList();
	};
	
	/**
	 * chart를 png로 다운로드 하기위한 URI  
	 */
	$scope.chartURI = '';
	
	var _loadTaxonStatList = function() {
		var params = {
			registStatus : REGIST_STATUS.SUCCESS,
//			openYn : angular.isObject($scope.loginUser) ? '' : 'Y',
		};
		
		statisticsFactory.getTaxonStatList(params).then(
			function(result) {
			$scope.taxonStatList = result;
			}, function(err){
				notificationService.error(err.data);
			});
	};
	
	var _loadYearStatList = function() {
		var params = angular.copy($scope.statParams);
		params['period'] = 'yearly';
		params['registStatus'] = REGIST_STATUS.SUCCESS;
//		params['openYn'] = angular.isObject($scope.loginUser) ? '' : 'Y';
		
		statisticsFactory.getRegistStatList(params).then(function(result) {
			$scope.yearStatList = result;
			
			var rows = [];
			var areas= [];
			var columns = [];
			
			var rawCount = 0; 
			var processedCount = 0; 
			
			columns[0] = { v: "" };
			columns[1] = { v: rawCount }; 
			columns[2] = { v: processedCount };
			areas.push({ c: columns });
			console.log(result);
			for(var i=0; i < result.length ; i++) {
				columns = []; 
				columns[0] = { v: result[i].period };
				columns[1] = { v: result[i].rawCount }; 
				columns[2] = { v: result[i].rawCount + '' };
				columns[3] = { v: result[i].processedCount };
				columns[4] = { v: result[i].processedCount + '' };
				rows.push({ c: columns });
				
				rawCount += result[i].rawCount;
				processedCount += result[i].processedCount;
				
				columns = [];
				columns[0] = { v: result[i].period };
				columns[1] = { v: rawCount }; 
				columns[2] = { v: processedCount };
				areas.push({ c: columns });
			}
			
			columns = [];
			columns[0] = { v: "Total" };
			columns[1] = { v: rawCount }; 
			columns[2] = { v: processedCount };
			areas.push({ c: columns });
			
			$scope.yearChart.data.rows = rows;
			$scope.yearChart.options.title = "Data registration yearly";
			
			$scope.areaChart.data.rows = areas;
			$scope.areaChart.options.title = "Number of genome released";
		});
	};
	
	var _loadMonthStatList = function() {
		var params = angular.copy($scope.statParams);
		params['period'] = 'monthly';
		params['registStatus'] = REGIST_STATUS.SUCCESS;
//		params['openYn'] = angular.isObject($scope.loginUser) ? '' : 'Y';
		
		statisticsFactory.getRegistStatList(params).then(function(result) {
			$scope.monthStatList = result;
			
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
			
			$scope.monthChart.data.rows = rows;
			$scope.monthChart.options.title = "Data registration monthly";
		});
	};
	
	$scope.excelDownload = function(tableId, sheetName){
    	var exportHref=excelFactory.tableToExcel(tableId, sheetName);
        $timeout(function(){
        	var a = document.createElement('a');
        	a.download = sheetName+".xls";
        	a.href=exportHref;
        	a.click();
        },100);
    }
	
	$scope.pngDownload = function(){
		$timeout(function(){
        	var a = document.createElement('a');
        	a.download = "Experiment(NGS).png";
        	a.href=$scope.chartURI;
        	a.click();
        },100);
	}
	
	$scope.initChartURI = function(chartWrapper) {
        $scope.chartURI = chartWrapper.getChart().getImageURI();
    };
    
	/**
	 * 데이터가 있는지 유무 
	 */
	$scope.disabledDownloadExcelBtn = function(){
		if(Utils.isBlank($scope.taxonStatList)){
			return true;
		}else{
			return false;
		}
	}
});