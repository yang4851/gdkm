/**
 * 
 */
gdkmApp.factory('depositFactory', [ '$http', '$log', function($http, $log) {
	var depositFactory = {
			
		getNgsDataRegist : function(item) {
			var seq;
			for (var i = 0; i < ANNOTATION_DATA.length; i++) {
				if (ANNOTATION_DATA[i]['id'] == item['id']) {
					seq = i;
				}
			}
			
			return ANNOTATION_DATA[seq];
		},
		
		getNgsDataRegistInfo : function(id){
			var targetUrl = Utils.getContextPath() + "/regist-data/"+ id;
			return $http.get(targetUrl);
		},
		
		createNgsDataRegist : function(regist) {
			var targetUrl = Utils.getContextPath() + "/regist-data";
			return $http.post(targetUrl, regist);
		},

		changeNgsDataRegist : function(regist) {
			if(Utils.isBlank(regist) || (regist.id > 0) == false)
				return $log.error('Failed to change regist data - Invalid id');
			
			var targetUrl = Utils.getContextPath() + "/regist-data/" + regist.id;
			return $http.put(targetUrl, regist);
		},
		
		/* 공개상태 변경 요청 */
		changeOpenStatus : function(registList) {
			if(!angular.isArray(registList) || registList.length == 0) 
				return $log.error('Failed to change open status: data is empty');
			
			var targetUrl = Utils.getContextPath() + "/regist-data/open-status";
			return $http.put(targetUrl, registList);
		},
		
		/* 승인상태 변경 요청 */
		changeConfirmStatus : function(registList) {
			if(!angular.isArray(registList) || registList.length == 0) 
				return $log.error('Failed to change submission status: data is empty');
			
			var targetUrl = Utils.getContextPath() + "/regist-data/confirm-status";
			return $http.put(targetUrl, registList);
		},
		
		/* 등록상태 변경 요청 */
		changeRegistStatus : function(registList) {
			if(!angular.isArray(registList) || registList.length == 0) 
				return $log.error('Failed to change regist status: data is empty');
			
			var targetUrl = Utils.getContextPath() + "/regist-data/regist-status";
			return $http.put(targetUrl, registList);
		},
		
		/* 검증 실행 요청 */
		validateNgsDataRegists : function(registList) {
			if(!angular.isArray(registList) || registList.length == 0) 
				return $log.error('Failed to validate data, data is empty');
			
			var targetUrl = Utils.getContextPath() + "/regist-data/validations";
			return $http.put(targetUrl, registList);
		},
		
		deleteNgsDataRegist : function(registId) {
			if((registId > 0) == false)
				return $log.error('Failed to delete regist data - Invalid id');
			
			var targetUrl = Utils.getContextPath() + "/regist-data/" + registId;
			return $http.delete(targetUrl);
		},
		
		getNgsDataRegistList : function(params) {
			if(Utils.isBlank(params)) {
				return $log.error('Failed to load regist data list - Invalid parameters');
			}
			
			var targetUrl = Utils.getContextPath() + "/regist-data";
			
			return $http.get(targetUrl, {
				params : params
			});
		},
		
		getNgsDataLinkedList : function(registId) {
			if(Utils.isBlank(registId)) {
				return $log.error('Failed to load linked data list - Invalid registId');
			}
			
			var targetUrl = Utils.getContextPath() + "/regist-data/" + registId + "/linked-data";
			return $http.get(targetUrl);
		},
		
		getChildNodesOfTaxonomy : function(params) {
			var targetUrl = Utils.getContextPath() + "/taxonomies/" + params.taxonId + "/tree-nodes";
			return $http.get(targetUrl, {
				params : params
			});
		},
		
		getChildNodesOfRawdata : function(params) {
			var targetUrl = Utils.getContextPath() + "/regist-data/" + params.rawRegistId + "/tree-nodes";
			return $http.get(targetUrl, {
				params : params
			});
		},
		
		createJbrowseData : function(achiveList){
			var targetUrl = Utils.getContextPath() + "/achieves/Jbrowse";
			
			return $http.post(targetUrl, achiveList,{responseType: 'text'});
		},
		
		downloadExcel : function(params){
			if(Utils.isBlank(params)) {
				return $log.error('Failed to load regist data list - Invalid parameters');
			}
			var targetUrl = Utils.getContextPath() + "/regist-data/" + params.dataType + "/list-down";
			return $http.get(targetUrl, {
				params : params,
				responseType: 'arraybuffer'
			});
		}
	}
	
	return depositFactory;
} ]);