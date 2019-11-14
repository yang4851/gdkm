/**
 * 
 */
gdkmApp.factory('userFactory',['$window', '$http', '$log', '$location',
   function($window, $http, $log, $location){
		var base_url = Utils.getContextPath() + "/users";
		var userFactory = {
				createUser : function(user) {
					return $http.post(base_url, user);
				},
				getUser : function(userId){
					var url = base_url + "/" + userId;
					return $http.get(url);
				},
				getUserList : function(params){
					return $http.get(base_url, {
						params : params
					});
				},
				changeUser : function(user){
					var url = base_url + "/" + user.userId;
					return $http.put(url, user);
				},
				changeUserPw : function(user){
					var url = base_url + "/" + user.userId + "/password";
					return $http.put(url, user);
				},
				deleteUser : function(userId){
					var url = base_url + "/" + userId;
					return $http.delete(url);
				},
		}
		return userFactory;
	}]);