/**
 * 
 */
gdkmApp.factory('toolsFactory', ['$http', '$log', '$q', function($http, $log, $q) {
		var base_url = Utils.getContextPath() + "/taxonomies";
		var toolsFactory = {
			getDbList : function(params, dbRadio){
				if(dbRadio == null)
					return $log.error('Failed to get dbList - db type is null');
				
				var url = Utils.getContextPath() + "/blast/dblist/" + dbRadio;
				return $http.get(url, {
					params : params
				});
			},
			postBlast : function(blastEvent) {
				var url = Utils.getContextPath() + "/blast/run";
				return $http.post(url, blastEvent);
			},
			getBlastOutputTabFile : function(fileName) {
				var url = Utils.getContextURL() + "/blast/output/tab/" + fileName;
				return $http.get(url);
			},
			getFeaturesPageChange : function(subjectId, params) {
				var url = Utils.getContextURL() + "/blast/features/" + subjectId;
				
				return $http.get(url, {
					params : params
				});
			},
			postGbkFileDownload : function(features, modal) {
				
				var url = Utils.getContextURL() + "/blast/ngs-file/"+modal.achive.dataRegist.dataType
							+"/download/"+modal.achive.id+"/"+modal.subjectId;
				return $http.post(url, features);
			},
			getRegistData : function(achiveRegistNo) {
				var url = Utils.getContextURL() + "/blast/ngs-data/" + achiveRegistNo;
				return $http.get(url);
			},
			getSequence : function(achiveId, params) {
				var url = Utils.getContextURL() + "/blast/sequence/" + achiveId;
				return $http.get(url, {
					params : params
				})
			},
			getFastFileList : function(params) {
				var url = Utils.getContextURL() + "/ani/fileList";
				return $http.get(url, {
					params : params
				})
			},
			postAni : function (params) {
				var url = Utils.getContextURL() + "/ani/run";
				return $http.post(url, params);
			},
			getOutputFile : function(params) {
				var url = Utils.getContextURL() + "/ani/output/file";
				return $http.get(url, {
					params: params
				});
			},
			getOutputFileDownload : function(params) {
				var url = Utils.getContextURL() + "/ani/output/download";
				return $http.get(url, {
					params : params
				});
			},
			deleteUploadFile : function(params) {
				var url = Utils.getContextURL() + "/ani/upload"
				return $http.delete(url, {
					params : params
				});
			}
		}
		return toolsFactory;
	} 
]);
