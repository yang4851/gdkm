/**
 * 
 */
gdkmApp.factory('achiveFactory', [ '$http', '$log', '$q', function($http, $log, $q) {
	var achiveFactory = {
			
		getNgsDataAchive : function(achiveId) {
			if((achiveId > 0) == false)
				return $log.error('Failed to load ngs file - id is null');
				
			var targetUrl = Utils.getContextPath() + "/achieves/" + achiveId;
			return $http.get(targetUrl);
		},
		
		deleteNgsDataAchive : function(ids) {
			var targetUrl = Utils.getContextPath() + "/achieves/"
			
			if(angular.isArray(ids)) {
				if(ids.length == 0)
					return $log.error('Failed to delete ngs file - id size is 0');
				
				targetUrl += ids.join(',');
			} else {
				if(Utils.isBlank(ids))
					return $log.error('Failed to delete ngs file - id is null');
				
				targetUrl += ids;
			}
			
			return $http.delete(targetUrl);
		},
		
		getNgsDataAchiveList : function(params) {
			if(Utils.isBlank(params)) {
				return $log.error('Failed to load ngs file list - parameter is null');
			}
			
			var targetUrl = Utils.getContextPath() + "/achieves";
			return $http.get(targetUrl, {
				params : params
			});
		},
		
		getNgsDataAchiveListOfRegist : function(registId) {
			if(Utils.isBlank(registId)) {
				return $log.error('Failed to load ngs file list - regist id is null');
			}
			
			var targetUrl = Utils.getContextPath() + "/regist-data/" + registId + "/achieves";
			return $http.get(targetUrl);
		},
		
		getNgsSeqQuantity : function(achiveId) {
			if(Utils.isBlank(achiveId)) {
				return $log.error('Failed to load sequence information - ngs file id is null');
			}
			
			var targetUrl = Utils.getContextPath() + "/achieves/" + achiveId + "/quantity";
			return $http.get(targetUrl);
		},
		
		getNgsSeqQcResultList : function(achiveId) {
			if(Utils.isBlank(achiveId)) {
				return $log.error('Failed to load QC reuslts - file id is null');
			}
			
			var targetUrl = Utils.getContextPath() + "/achieves/" + achiveId + "/qcResults";
			return $http.get(targetUrl);
		},
		
		getNgsSeqQcSummaryList : function(achiveId) {
			if(Utils.isBlank(achiveId)) {
				return $log.error('Failed to load QC reuslts - file id is null');
			}
			
			var targetUrl = Utils.getContextPath() + "/achieves/" + achiveId + "/qcSummary";
			return $http.get(targetUrl);
		},
		
		/**
		 * @Deprecate 별로 사용할 필요가 없어 보임
		 */
		getNgsSeqQcReportAsHtml : function(achiveId) {
			if(Utils.isBlank(achiveId)) {
				return $log.error('Failed to load QC report - file id is null');
			}
			
			var targetUrl = Utils.getContextPath() + "/achieves/" + achiveId + "/qcReport";
			return $http.get(targetUrl);
		},
		
		/* gbk 파일 분석정보 호출 함수들*/
		
		getGenbankFeatureList :function(achiveId, params) {
			var deferred = $q.defer();
			var config = { params : params };
			
			$http.get(Utils.getContextPath() + "/achieves/" + achiveId + "/features", config).then(
				function(response) {
					deferred.resolve(response.data);
				}, 
				function(err) {
					$log.error(err);
					deferred.resolve({
						total : 0,
						page : 0,
						list : [],
					});
				}
			);
			
			return deferred.promise;
		},
		
		getGenbankHeaders :function(achiveId) {
			var deferred = $q.defer();
			
			$http.get(Utils.getContextPath() + "/achieves/" + achiveId + "/feature-header").then(
				function(response) {
					deferred.resolve(response.data);
				}, 
				function(err) {
					$log.error(err);
					deferred.resolve();
				}
			);
			
			return deferred.promise;
		},
		
		getGbkFileAsPlainText :function(achiveId) {
			return $http({
				url : Utils.getContextPath() + "/achieves/" + achiveId + "/gbk",
				method: 'GET', 
				headers : { 'Content-Type' : 'text/plain' },
				transformResponse : [function(data) {
					return data;
				}]
			});
		},
		
		getIntegratedAchiveNode : function(params){
			var targetUrl = Utils.getContextPath() + "/achieves/achiveNode";
			return $http.get(targetUrl, {
				params : params
			});
		}
	}
	
	return achiveFactory;
} ]);