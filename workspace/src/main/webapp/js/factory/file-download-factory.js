/**
 * 
 */
gdkmApp.factory('FileDownloadFactory', ['$http', 'notificationService', function($http, notificationService) {
	
	function doDownloadFile(url, params) {
		$http({
			url : url,
			params : params,
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
	}
	
	return {
		
		doDownloadResearchAttachment : function(attachment) {
			var url = Utils.getContextPath()
					+ "/research/" + attachment.research.id
					+ "/attachments/" + attachment.id;
			var params = { 'filename' : attachment.name };
			
			doDownloadFile(url, params);
		},
		
		doDownloadResearchOmicsFile : function(omics) {
			var url = Utils.getContextPath()
					+ "/research/" + omics.research.id
					+ "/omics/" + omics.id;
			var params = { 'filename' : omics.name };
			
			doDownloadFile(url, params);
		}
	};
	
}]);