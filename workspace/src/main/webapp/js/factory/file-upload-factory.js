
gdkmApp.factory('FileUploadFactory', ['$http', 'notificationService', function($http, notificationService) {
	return {
		
		doUploadTempFile : function(file) {
			var url = Utils.getContextPath() + "/temporaries";
			return $http.post(url, file);
		},
		
		/**
		 * @param file	Attachment와 구조가 동일
		 * @See app-object.js#Attachment
		 */
		doDownloadTempFile : function(file) {
			$http({
				url : Utils.getContextPath() + "/temporaries/" + file.path,
				params : { 'filename' : file.name },
				method : 'GET',
				headers : {
					'Content-Type' : 'application/octet-stream',
					"Accept": 'application/octet-stream'
				},
				responseType: 'arraybuffer'
			}).then(
				function(res) {
					var header = res.headers('Content-Disposition')
					var fileName = header.split("=")[1].replace(/\"/gi,'');
					var blob = new Blob([res.data], { type : 'application/octet-stream;base64,'});
					var objectUrl = (window.URL || window.webkitURL).createObjectURL(blob);
					
					angular.element('<a/>').attr({
						href : objectUrl,
						download : fileName
					})[0].click();
				}, 
				function(err) {
					var msg = new TextDecoder("utf-8").decode(err.data);
					notificationService.error(msg);
				}
			);
		},
		
		doRemoveTempFile : function(file) {
			var url = Utils.getContextPath() + "/temporaries/" + file.path;
			return $http.delete(url);
		},
		
	};
}]);