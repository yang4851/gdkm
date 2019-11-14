/**
 * 
 */
gdkmApp.factory('statisticsFactory', function($http, $log, $q) {
	
	var statisticsFactory = {
		
		getRegistYearList : function() {
			var deferred = $q.defer();
			
			$http.get(Utils.getContextPath() + "/statistics/years").then(
				function(response) {
					deferred.resolve(response.data);
				}, 
				function(err) {
					$log.error(err);
					deferred.resolve([ new Date().getFullYear() ]);
				}
			);
			
			return deferred.promise;
		},
		
		getTaxonStatList : function(params) {
			var deferred = $q.defer();
			var config = { params : params };
			
			$http.get(Utils.getContextPath() + "/statistics/species", config).then(
				function(response) {
					deferred.resolve(response.data);
				}, 
				function(err) {
					$log.error(err);
					deferred.resolve([]);
				}
			);
			
			return deferred.promise;
		},
		
		getRegistStatList : function(params) {
			var deferred = $q.defer();
			var config = { params : params };
			
			$http.get(Utils.getContextPath() + "/statistics/regists", config).then(
				function(response) {
					deferred.resolve(response.data);
				}, 
				function(err) {
					$log.error(err);
					deferred.resolve([]);
				}
			);
			
			return deferred.promise;
		},
		
		getRegistSummary : function(params) {
			var deferred = $q.defer();
			var config = { params : params };
			
			$http.get(Utils.getContextPath() + "/statistics/summary", config).then(
				function(response) {
					deferred.resolve(response.data);
				}, 
				function(err) {
					$log.error(err);
					deferred.resolve({
						'taxonCount' : 0,
						'registCount' : 0,
						'lastUpdated' : null,
					});
				}
			);
			
			return deferred.promise;
		}
	};
	
	return statisticsFactory;
});