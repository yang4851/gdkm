gdkmApp.controller('taxonomyController', function($scope, $log, $window,
		ivhTreeviewBfs, ivhTreeviewMgr, taxonFactory) {

	var self = this;
	
	self.data = [];
	
	self.options = {
		field : 'name',
		keyword : '' 
	};
	
	$scope.init = function() {
		_loadTaxonTree();
	}

	$scope.search = function() {
		var letterLength = self.options.keyword.replace(/ /g,'').length 
		
		// 이름으로 검색하는 경우 최소 4글자 이상이 되어야 함. 
		if(letterLength > 0) {
			if(letterLength < 4)
				return $window.alert('Not enough letter length.');
			
			if(self.options.field == 'ncbiTaxonId') {
				if(!Utils.isNumber(parseInt(self.options.keyword.trim()))) { 
					return $window.alert('Invalid taxonomy ID');
				}
			}
		}
		
		
		self.showTree = false;
		_loadTaxonTree({
			field : self.options.field,
			keyword : self.options.keyword.trim()
		});
	}

	$scope.onChangeNodeSelection = function(node, selected, tree) {
		ivhTreeviewMgr.deselectAll(tree);
		if (selected) {
			ivhTreeviewMgr.select(tree, node, {
				disableCheckboxSelectionPropagation : true
			});
		}
	};

	$scope.onChangeNodeCollapse = function(node, expanded, tree) {
		if(!isNotYetLoading(node))
			return;

		taxonFactory.getTaxonTreeNodes({parentId : node.id}).then(
			function(res) {
				_generateTaxonomyNodes(res.data, node);
			}, function(err) {
				$window.alert('Tree taxon-tree-leaf loaded failed - ' + err.data);
			}
		);
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
	
	function isNotYetLoading(node) {
		if(node.type != 'P')
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
	
	function _loadTaxonTree(params) {
		$scope.progressBar = true;
		taxonFactory.getTaxonTreeNodes(params).then(
			function(res) {
				self.data = _generateTaxonomyNodes(res.data);
				$scope.progressBar = false;
				self.showTree = true;
				
				expandFirstLeafNode(self.data);
			}, 
			function(err) {
				$window.alert('Category loaded failed');
				$scope.progressBar = false;
			}
		);
	}
	
	function _makeTaxonomyNode(taxonomy) {
		var node = {
			id: taxonomy.taxonId,
			label: taxonomy.name + " (" + taxonomy.ncbiTaxonId + ", " + taxonomy.rank + ")",
			type: ((taxonomy.numberOfChild > 0)? 'P' : 'S'), 
			data : taxonomy,
			children: []
		};
		
		if(taxonomy.numberOfChild > 0) {
			let children = taxonomy.children;
			if(angular.isArray(children) && children.length > 0) {
				for(let i=0; i < children.length ; i++) {
					let childNode = _makeTaxonomyNode(children[i]);
					node.children.push(childNode);
				}
			} else {
				node.children.push({
					id: -1,
					label: 'Loading ...',
					type: 'DEL',
					children: []
				});
			}
		}
		
		return node;
	}
	
	function _generateTaxonomyNodes(list, parent) {
		if(!angular.isArray(list)) {
			if(angular.isObject(parent))
				parnet.children = [];	
			return;
		}
		if(angular.isObject(parent)) {
			if(parent.children.length > 0 && parent.children[0].type == 'DEL') {
				parent.children.splice(0);
			}
		}
		
		var nodes = [];
		while(list.length > 0) {
			var node= _makeTaxonomyNode(list.shift());
			if(angular.isObject(parent)) {
				parent.children.push(node);
			} else {
				nodes.push(node);
			}
		}
		
		return (angular.isObject(parent))? parent : nodes;
	}

	self.showTree = true;
});
