gdkmApp.factory('ResearchAttachmentFactory',['$http', '$q', function($http, $q){
	return {
		
		getAttachmentList : function(research, params) {
			var url = Utils.getContextPath() + "/research/" + research.id + "/attachments";
			return $http.get(url, {
				params : params
			});
		},
		
		getAttachment : function(research, attachmentId) {
			var url = Utils.getContextPath() + "/research/" + research.id + "/attachments/" + attachmentId;
			return $http.get(url);
		}, 
		
		createAttachment : function(research, attachment) {
			var url = Utils.getContextPath() + "/research/" + research.id + "/attachments";
			return $http.post(url, attachment);
		}, 
		
		changeAttachment : function(research, attachment) {
			var url = Utils.getContextPath() + "/research/" + research.id + "/attachments/" + attachment.id;
			return $http.put(url, attachment);
		}, 
		
		deleteAttachment : function(research, attachment) {
			var url = Utils.getContextPath() + "/research/" + research.id + "/attachments/" + attachment.id;
			return $http.delete(url);
		}, 
		
		doBatchProcess : function(research, list, action) {
			var deferred = $q.defer();
			var url = Utils.getContextPath() + "/research/" + research.id + "/attachments";
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