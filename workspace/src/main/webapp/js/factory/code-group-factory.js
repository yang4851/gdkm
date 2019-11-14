/**
 * 
 */
gdkmApp.factory('codeGroupFactory',['$window', '$http', '$log', '$location',
   function($window, $http, $log, $location){
		var base_url = Utils.getContextPath() + "/codeGroups";
		var codeGroupFactory = {
				createCodeGroup : function(codeGroup) {
					var url = base_url;
					return $http.post(url, codeGroup);
				},
				getCodeGroup : function(codeGroupId){
					var url = base_url + "/" + codeGroupId;
					return $http.get(url);
				},
				getCodeGroupList : function(params){
					return $http.get(base_url, {
						params : params
					});
				},
				updateCodeGroup : function(codeGroup){
					var url = base_url + "/" + codeGroup.id;
					return $http.put(url, codeGroup);
				},
				updateCodeGroupPw : function(codeGroup){
					var url = base_url + "/" + codeGroup.id + "/password";
					return $http.put(url, codeGroup);
				},
				deleteCodeGroup : function(codeGroupId){
					var url = base_url + "/" + codeGroupId;
					return $http.delete(url);
				},
		}
		return codeGroupFactory;
	}]);