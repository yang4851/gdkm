/**
 * 
 */
gdkmApp.config(function($routeProvider, $locationProvider) {
	
	$routeProvider
	
	.when('/', {
		templateUrl : 'views/home.html',
		controller : 'homeController'
	})
	// 메인화면 
	.when('/home', {
		templateUrl : 'views/home.html',
		controller : 'homeController'
	})
	//Registration
	.when('/registration/:target', {
		templateUrl : function(param){
			if(param.target == 'rawData'){
				return 'views/registration/raw-data-main.html';
			} else if(param.target == 'annotationData'){
				return 'views/registration/processed-data-main.html';
			} else if(param.target == 'research'){
				return 'views/registration/research-main.html';
			}
		}
	})
	//Integrated Views
	.when('/integrated-view', {
		templateUrl : 'views/integrated-views/main.html',
		controller : 'integratedController'
	})
	//Integrated Views
	.when('/research', {
		templateUrl : 'views/research/main.html',
		controller : 'integratedController'
	})
	//statistics
	.when('/statistics', {
		templateUrl : 'views/statistics/main.html',
		controller : 'statisticsController'
	})
	//user
	.when('/user',{
		templateUrl : 'views/user/main.html',
		controller : 'userController'
	})
	//code
	.when('/code',{
		templateUrl : 'views/code/main.html'
	})
	//menu
	.when('/menu',{
		templateUrl : 'views/menu/main.html'
	})
	//taxonomy
	.when('/taxonomy',{
		templateUrl : 'views/microbes-taxonomy/main.html'
	})
	// 임상연구 목록(target=list), 임상연구 개별등록(target=create), 임상연구 일괄등록(target=batch)
	.when('/clinic-study/:target/', {
		templateUrl : function(params){
			if(params.target == 'list'){
				return 'views/clinic/study-main.html';
			} else {
				return 'views/intro/guide-clinic-study.html';
			}
		},
		controller : 'clinicStudyController',
		scope : 'user'
	})
	// blast 실행화면
	.when('/tools/blast', {
		templateUrl : 'views/tools/blast-execute.html',
		controller : 'blastExecuteController'
	})
	// blast 결과화면
	.when('/tools/blast-result', {
		templateUrl : 'views/tools/blast-result.html',
		controller : 'blastResultController'
	})
	// ani 실행화면
	.when('/tools/ani', {
		templateUrl : 'views/tools/ani-execute.html',
		controller : 'aniExecuteController'
	})
	// ani 결과화면
	.when('/tools/ani-result', {
		templateUrl : 'views/tools/ani-result.html',
		controller : 'aniResultController'
	})
	//404error
	.when('/404error',{
		templateUrl : 'views/error/404errorContent.html'
	})
	//404error
	.when('/500error',{
		templateUrl : 'views/error/500error.html'
	})
	.otherwise({
		redirectTo:'/404error'
    });
});