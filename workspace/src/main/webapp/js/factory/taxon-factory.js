/**
 * 
 */
gdkmApp.factory('taxonFactory', ['$http', '$log', '$q', function($http, $log, $q) {
		
		var taxonFactory = {
			/**
			 * 테이블 형태로 페이징용 Taxonomy 목록 호출 
			 * @params : 검색조건
			 * params = {
			 * 	rank : '',
			 * 	keyword : '',
			 * 	page: 1,
			 * 	rowSize : 10
			 * };
			 */
			getTaxonList : function(params) {
				var deferred = $q.defer();
				var config = { params : params };
				
				var url = Utils.getContextPath() + "/taxonomies";
				$http.get(url, config).then(
					function(response) {
						deferred.resolve(response.data);
					}, 
					function(err) {
						$log.error(err);
						deferred.resolve({
							total: 0,
							list: [],
							page: 1,
							rowSize: 10,
						});
					}
				);
				
				return deferred.promise;
			},
	
			/**
			 * taxonId 에 해당하는 Taxonomy의 상위 노드들을 모두 포함하는 목록 호출
			 */
			getHierarchyList : function(taxonId) {
				var requestUrl = Utils.getContextPath() + "/taxonomies/" + taxonId + "/hierarchies";
				return $http.get(requestUrl);
			},
			
			/**
			 * params = {
			 * 	keyword : '', // 검색어
			 * 	parentId : '', // 상위 Taxonomy ID'
			 * 	rank : '', // 검색 대상 node rank(ex, 최상위 계통을 검색하는 경우 'superkingdom'이 되면 최상위 노드들만 반환)
			 * }
			 */
			getTaxonTreeNodes : function(params) {
				var url = Utils.getContextPath() + "/taxonomies/tree";
				var config = {
					params : params,
					responseType : 'json',
				};
				
				return $http.get(url, config);
			},
			
			getIntegratedTaxonNode : function(params){
				var url = Utils.getContextPath() + "/taxonomies/integrated-tree";
				var config = {
						params : params,
						responseType : 'json',
					};
				
				return $http.get(url, config);
			}
		}
	
		return taxonFactory;
	} 
]);