gdkmApp.factory('ResearchOmicsFactory',['$http', '$q', function($http, $q){
	return {
		
		getOmicsList : function(research, params) {
			var url = Utils.getContextPath() + "/research/" + research.id + "/omics";
			return $http.get(url, {
				params : params
			});
		},
		
		getOmics : function(research, omicsId) {
			var url = Utils.getContextPath() + "/research/" + research.id + "/omics/" + omicsId;
			return $http.get(url);
		}, 
		
		createOmics : function(research, omics) {
			var url = Utils.getContextPath() + "/research/" + research.id + "/omics";
			return $http.post(url, omics);
		}, 
		
		changeOmics : function(research, omics) {
			var url = Utils.getContextPath() + "/research/" + research.id + "/omics/" + omics.id;
			return $http.put(url, omics);
		}, 
		
		deleteOmics : function(research, omics) {
			var url = Utils.getContextPath() + "/research/" + research.id + "/omics/" + omics.id;
			return $http.delete(url);
		}, 
		
		doBatchProcess : function(research, list, action) {
			var deferred = $q.defer();
			var url = Utils.getContextPath() + "/research/" + research.id + "/omics";
			var params = { action : action };
			var config = { params : params };
			
			$http.put(url, list, config).then(
				function(res) { deferred.resolve(res); }, 
				function(err) { deferred.reject(err); }
			);
			
			return deferred.promise;
		}
	}
}]);