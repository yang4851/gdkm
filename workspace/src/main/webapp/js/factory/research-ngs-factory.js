gdkmApp.factory('ResearchNgsDataFactory',['$http', '$q', function($http, $q){
	return {
		
		getNgsDataList : function(research, params) {
			var url = Utils.getContextPath() + "/research/" + research.id + "/ngs";
			return $http.get(url, {
				params : params
			});
		},
		
		appendNgsData : function(research, ngs) {
			var url = Utils.getContextPath() + "/research/" + research.id + "/ngs";
			return $http.post(url, ngs);
		}, 
		
		removeNgsData : function(research, ngs) {
			var url = Utils.getContextPath() + "/research/" + research.id + "/ngs/" + ngs.id;
			return $http.delete(url);
		}, 
		
		doBatchProcess : function(research, list, action) {
			var deferred = $q.defer();
			var url = Utils.getContextPath() + "/research/" + research.id + "/ngs";
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