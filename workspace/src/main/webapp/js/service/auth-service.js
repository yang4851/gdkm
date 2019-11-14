gdkmApp.service('authService', function($q, $http, $state, $cacheFactory) {
	var API = '/api/auth', cache = $cacheFactory('authService');

	// 로그인 정보 조회
	function get(options) {
		var deferred = $q.defer();

		if (cache.get('auth')) {

			// 캐쉬에 인증정보가 있으면 인증정보 반환
			deferred.resolve(cache.get('auth'));
		} else {

			// 캐쉬에 인증정보가 없으면 백엔드에 호출함
			$http.get(API).success(function(data) {
				cache.put('auth', data.auth); // 캐쉬에 인증정보 저장
				deferred.resolve(cache.get('auth')); // 인증정보 반환
			}).error(function() {
				// 인증되지 않을경우 reject
				deferred.reject('Rejected');

				// options.redirect 파라메터 값이 설정된 경우 /login 페이지로 리다이랙트
				if (options && options.redirect) {
					$state.go('login');
				}
			});
		}

		this.get = get;
	}
});