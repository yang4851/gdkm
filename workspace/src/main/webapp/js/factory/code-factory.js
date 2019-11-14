/**
 * 
 */
gdkmApp.factory('codeFactory',['$window', '$http', '$log', '$location',
   function($window, $http, $log, $location){
		var base_url = Utils.getContextPath() + "/codes";
		var codeFactory = {
				createCode : function(code) {
					var url = base_url;
					return $http.post(url, code);
				},
				getCode : function(codeId){
					var url = base_url + "/" + codeId;
					return $http.get(url);
				},
				
				/**
				 * params = {
				 *   group : '공통코드 그룹',
				 *   code : '공통코드',   // 해당 코드만 호출할 때 사용
				 *   name : '코드이름',   // 코드 이름 검색을 하는 경우 사용
				 *   userYn : '사용여부', // 사용여부에 따라 검색하는 경우 사용
				 *   page : '현재 페이지 번호', // 목록 테이블 검색하는 경우 사용
				 *   rowSize : '페이지 출력 개수', // 목록 테이블 검색하는 경우 사용  
				 * }  
				 */
				getCodeList : function(params){
					return $http.get(base_url, {
						params : params
					});
				},
				updateCode : function(code){
					var url = base_url + "/" + code.code;
					return $http.put(url, code);
				},
				updateCodePw : function(code){
					var url = base_url + "/" + code.code + "/password";
					return $http.put(url, code);
				},
				deleteCode : function(codeId){
					var url = base_url + "/" + codeId;
					return $http.delete(url);
				},
		}
		return codeFactory;
	}]);