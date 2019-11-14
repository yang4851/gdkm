gdkmApp.controller('integratedSearchController', function($rootScope, $scope, $log, $window, $timeout, $cacheFactory, 
		ivhTreeviewMgr, notificationService, depositFactory, achiveFactory, taxonFactory) {
	
	var self = this;

	var message = MESSAGE.create();
	
	self.data = [];
	
	self.selected = {};
	
	self.showTree = true;

	$scope.model = null;
	
	$scope.params = {
		openYn : '',
		dataType : '',
		match : 'partial',
		registStatus : REGIST_STATUS.SUCCESS,
		view : 'integrated',
		fields : '',
		keyword : '',
	};
	
	$scope.init = function() {
		var cache = Utils.getCache($cacheFactory);
		if(angular.isUndefined(cache)) {
			$scope.params = new SearchParams();
		} else {
			$scope.params = angular.copy(cache.get(CACHE_PROP.SEARCH_PARAMS));
			if(angular.isUndefined($scope.params))
				$scope.params = new SearchParams();
		}
		
		$scope.params.registStatus = REGIST_STATUS.SUCCESS;
		$scope.params.openYn = ($scope.loginUser == null)? 'Y' : '';
		
		_loadTaxonTree($scope.params);
	}
	
	$scope.search = function() {
		$scope.params.strain = $scope.params.species;
		$scope.params.gene = null; 
		$scope.params.product = null; 
		
		$scope.$broadcast('onDispatchSearchEvent', angular.copy($scope.params));
		
		var letterLength = $scope.params.keyword.replace(/ /g,'').length 
		
		if(letterLength > 0) {
			if(letterLength < 4)
				return $window.alert('Not enough letter length.');
			
			if($scope.params.fields == 'ncbiTaxonId') {
				if(!Utils.isNumber(parseInt($scope.params.keyword.trim()))) { 
					return $window.alert('Invalid taxonomy ID');
				}
			}
		}

		self.showTree = false;
		_loadTaxonTree($scope.params);
	}
	
	$scope.isAllSearch = function() {
		return (!$scope.isRawDataSearch() && !$scope.isProcessedSearch());
	};
	
	$scope.isRawDataSearch = function() {
		return ($scope.params.dataType == PAGE_DATA.RAWDATA);
	};
	
	$scope.isProcessedSearch = function() {
		return ($scope.params.dataType == PAGE_DATA.PROCESSED);
	};
	
	$scope.$on('onDispatchSearchEvent', function(event, params) {
		if(!angular.isObject(params)) {
			return $log.debug("search parameter is null");
		}
		
		var openYn = $scope.params.openYn;
		$scope.params = angular.copy(params);
		$scope.params.openYn = openYn;
		
		_loadTaxonTree($scope.params);
	});

	$scope.onChangeNodeSelection = function(node, selected, tree) {
		ivhTreeviewMgr.deselectAll(tree);
		if (selected) {
			ivhTreeviewMgr.select(tree, node, {
				disableCheckboxSelectionPropagation : true
			});
		}
		self.selected = {
			node : node,
			selected : selected,
			tree : tree
		};
	};

	$scope.onChangeNodeCollapse = function(node, expanded, tree) {
		if(!isNotYetLoading(node))
			return;
		
		var params = angular.copy($scope.params);
		
		if(node['type']==TREE_NODE_TYPE.TAXONOMY){
			params['taxonId'] = node.id;
			
			depositFactory.getChildNodesOfTaxonomy(params).then(
					function(res) {
						_generateTreeNodes(res.data, node);
					}, function(err) {
						$window.alert('Tree taxon-tree-leaf loaded failed - ' + err.data);
					}
			);
		}else if(node['type']==TREE_NODE_TYPE.RAWDATA){
			params['rawRegistId'] = node.id;
			
			depositFactory.getChildNodesOfRawdata(params).then(
					function(res) {
						_generateTreeNodes(res.data, node);
					}, function(err) {
						$window.alert('Tree taxon-tree-leaf loaded failed - ' + err.data);
					}
			);
		}else if(node['type']==TREE_NODE_TYPE.PROCESSED){
			params['registId'] = node.id;
			
			achiveFactory.getIntegratedAchiveNode(params).then(
					function(res) {
						_generateTreeNodes(res.data, node);
					}, function(err) {
						$window.alert('Tree taxon-tree-leaf loaded failed - ' + err.data);
					}
			);
		}
	};
	
	$scope.getSelectedList = function() {
		var selectedList = [];
		ivhTreeviewBfs(self.data, function(node) {
			if(node.selected && angular.isObject(node.data)) {
				selectedList.push(node.data);
			}
		});
		
		return selectedList;
	};
	
	$scope.hasSelectedNode = function() {
		return ($scope.getSelectedList().length > 0);
	};
	
	$scope.onClickSelectButton = function() {
		var selectedList = $scope.getSelectedList();
		if(selectedList.length == 0) {
			return notificationService.notice("No selected taxonomy");
		}
		
		$scope.$emit("onSelectTaxonomy", selectedList[0]);
		$("#taxonomy-search-popup").modal('hide');
	};
	
	$scope.isTreeNodeSelected = function() {
		try {
			return (self.selected.node.type != TREE_NODE_TYPE.TAXONOMY);
		} catch(exception) {
			return false;
		}
	};
	
	$scope.taxonTreeDetailPopup = function(){
		try{
			var id = self.selected.node.id;
		}catch(exception){
			alert(message.NOTICE022_EN);
			return;
		}
		var type = self.selected.node.type;
		
		if(type == TREE_NODE_TYPE.TAXONOMY){
//			alert("[TAXONOMY 상세보기 정보]는 서비스 준비중입니다. \n 하위랭크의 데이터정보 및 파일정보를 이용해주세요.");
			return;
		}else if(type == TREE_NODE_TYPE.RAWDATA || type == TREE_NODE_TYPE.PROCESSED){
			depositFactory.getNgsDataRegistInfo(id).then(
				function(res){
					$rootScope.$broadcast('integrated-ngsdata', res.data);
					if(type == TREE_NODE_TYPE.RAWDATA){
						$('#raw-data-detail-popup').modal('show');
					}else if(type == TREE_NODE_TYPE.PROCESSED){
						$('#processed-data-detail-popup').modal('show');
					}
				},
				function(err){
					notificationService.error(err.data);
				}
			)
		}else if(type == TREE_NODE_TYPE.ARCHIVE){
				$scope.$parent.onClickNgsFileDetail(self.selected.node.data);
		}
	}
	
	var _openNgsFilePopup = function(popupId) {
		$timeout(function() {
			waitingDialog.hide();
			$(popupId).modal('show');
		}, 300);
	};
	
	function isNotYetLoading(node) {
		if(node.type != TREE_NODE_TYPE.TAXONOMY 
				&& node.type != TREE_NODE_TYPE.RAWDATA 
				&& node.type != TREE_NODE_TYPE.PROCESSED)
			return false;
		
		if(node.children.length > 0 && node.children[0].type == 'DEL')
			return true
		
		return false;
	}
	
	/**
	 * 검색된 계통트리에서 첫번째 마지막 노드가 펼쳐지게 하기위한 재귀함수 
	 * 
	 * @param nodes
	 * @returns
	 */
	function expandFirstLeafNode(nodes) {
		if(!angular.isArray(nodes) || nodes.length == 0)
			return;
		
		if(angular.isArray(nodes[0].children) && nodes[0].children.length > 0) {
			if(angular.isObject(nodes[0].children[0].data)) {
				ivhTreeviewMgr.expand(self.data, nodes[0]);
				expandFirstLeafNode(nodes[0].children);
			}
		}
	}
	
	/**
	 * 통합뷰어 NGS Browse 트리뷰 노드 비동기 방식 호출  
	 * 
	 * @param params = {
	 * 			"dataType": "",  	// All, Raw data, Annotation
	 *  		"openYn": "",	 	// 공개여부(Default=Y)
	 *    		"registStatus": "",	// 등록상태(Default=success)
	 *      	"registFrom": null,	// 등록기간(시작)
	 *      	"registTo": null,	// 등록기간(끝)
	 *      	"fields": "",		// 원시,가공 데이터의 검색 필드
	 *      	"species": "",		// 종명
	 *      	"strain": "",		// 균주
	 *      	"keyword": "",		// 검색어
	 *      	"match": "exact",	// 검색어 일치여부 
	 *      	"page": 1,			// 시작 페이지
	 *      	"rowSize": 10		// 페이지당 레코드 개수
	 *       }
}
	 * @returns
	 */
	function _loadTaxonTree(params) {
		$scope.progressBar = true;
		taxonFactory.getIntegratedTaxonNode(params).then(
			function(res) {
				$scope.progressBar = false;
				self.data = _generateTreeNodes(res.data);
				self.showTree = true;
			}, 
			function(err) {
				$scope.progressBar = false;
			}
		);
	}
	
	var LOADING_NODE = {
			id: -1,
			label: 'Loading ...',
			type: 'DEL',
			children: []
		};
	
	function _makeTreeNode(data) {
		var node = angular.copy(LOADING_NODE); 
		
		if(data['dataType']=='rawdata'){
			node.id = data.id;
			node.label = data.registNo + " (Raw data)";
			node.data = data;
			node.type = TREE_NODE_TYPE.RAWDATA;
			node.children = [LOADING_NODE];
		} else if(data['dataType']=='processed'){
			node.id = data.id;
			node.label = data.registNo + " (Annotation)";
			node.data = data;
			node.type = TREE_NODE_TYPE.PROCESSED;
			node.children = [LOADING_NODE];
		} else if(Utils.isNotBlank(data['fileName'])){
			node.id = data.id;
			node.label = data.registNo + " (" + data.fileType + ")";
			node.data = data;
			node.type = TREE_NODE_TYPE.ARCHIVE;
		} else {
			node.id = data.taxonId,
			node.label = data.name + " (NCBI taxon ID : " + data.ncbiTaxonId + ")",
			node.data = data;
			node.type = TREE_NODE_TYPE.TAXONOMY;
			node.children = [LOADING_NODE];
		}
		
		return node;
	}
	
	function _generateTreeNodes(list, parent) {		
		var node = [];
		var nodeList = node;
		
		if(angular.isObject(parent)) {
			if(parent.children.length > 0 && parent.children[0].type == 'DEL') {
				parent.children.splice(0);
			}
			
			node = parent;
			nodeList = parent.children;
		}
		
		for(var i=0; i < list.length ; i++) {
			nodeList.push(_makeTreeNode(list[i]));
		}
		
		return node;
	}
});
