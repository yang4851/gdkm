/**
 * 
 */
gdkmApp.factory('loginFactory',['$http','$log','$location',
    function($http, $log, $location){
		
		var loginFactory = {
			loginProcess : function(params){
				var url = Utils.getContextPath() + "/login";
				return $http.post(url, params);
			},
			logoutProcess : function(){
				var url = Utils.getContextPath() + "/logout";
				return $http.post(url);
			}
		};
		
		return loginFactory;
	}]);