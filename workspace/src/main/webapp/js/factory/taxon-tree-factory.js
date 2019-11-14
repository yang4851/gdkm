/**
 * 
 */
gdkmApp.factory('taxonTreeFactory', ['$http', '$log', '$q', function($http, $log, $q) {
		var base_url = Utils.getContextPath() + "/taxonomies";
		var taxonTreeFactory = {
				getTree : function(params){
					if(params.name==null){
						return $http.get(base_url + '?parentId=' + params.id + '&name=');
					}else{
						return $http.get(base_url + '?parentId=' + '&name=' + params.name);
					}
				},
				getTreeNode : function(params){
					var url = base_url + '?parentId=' + params.id + '&name=';
					return $http.get(url);
				},
		}
		return taxonTreeFactory;
	} 
]);