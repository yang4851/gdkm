gdkmApp.factory('ResearchFactory',['$http', '$q', function($http, $q){
	return {
		
		getResearchList : function(params) {
			var url = Utils.getContextPath() + "/research";
			return $http.get(url, {
				params : params
			});
		},
		
		getResearch : function(researchId) {
			var url = Utils.getContextPath() + "/research/" + researchId;
			return $http.get(url);
		}, 
		
		createResearch : function(research) {
			var url = Utils.getContextPath() + "/research";
			return $http.post(url, research);
		}, 
		
		changeResearch : function(research) {
			var url = Utils.getContextPath() + "/research/" + research.id;
			return $http.put(url, research);
		}, 
		
		deleteResearch : function(research) {
			var url = Utils.getContextPath() + "/research/" + research.id;
			return $http.delete(url);
		}, 
		
		doBatchProcess : function(list, action) {
			var deferred = $q.defer();
			var url = Utils.getContextPath() + "/research";
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