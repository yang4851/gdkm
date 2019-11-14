gdkmApp.directive('leaf', function (tree, ivhTreeviewMgr,$window,taxonTreeFactory) {
        return { 
            restrict: 'AE', 
            link: function (scope, element, attrs) {
                 
                element.on('click', function () {
                    var root = scope.trvw.root();
                    if (scope.node.type == 'S') { 
                        return;
                    }
 
                    if (scope.node.children!==null & scope.node.children.length > 0) {
                        if (scope.node.children[0].type != 'DEL') {
                            return;//Loaded Parent, not need to load again
                        }
                    }
 
                    var params = {
                        id: scope.node.id
                    };
                    var url = "";
                    var fakechild = true;
 
 
                    switch (scope.node.type) {
                        case 'C1': 
                            url = 'api/your URL/';
                            fakechild = false;
                            break;
                        case 'S':  //Is the last node. need to stop
                            break;
                    }
 
                    taxonTreeFactory.getTreeNode(params).then(
          				  function(res){
          					loadCompleted(res);
        				  },
        				  function(err){
        					  loadFailed(err);
        				  }
                    );
 
 
                    function loadCompleted(result) {
                        if (result.status == 200) { 
 
                            if (result.data != "") {
                                tree.genNode(result.data.list, scope.node, fakechild); 
                            } else {
                                if (scope.node.children[0].type == 'DEL') {
                                    delete scope.node.children 
                                }  
                            }
                        } else {
                            $window.alert('Tree taxon-tree-leaf loaded failed - ' + scope.node.type + ' ' + result.error);
                        }
 
 
                    }
 
                    function loadFailed(result) {
                       ivhTreeviewMgr.collapse(root, scope.node); 
                      $window.alert('Tree taxon-tree-leaf loaded failed - ' + scope.node.type + ' ' + result.error);
                    }
 
 
                  
                });
            }
        };
    });