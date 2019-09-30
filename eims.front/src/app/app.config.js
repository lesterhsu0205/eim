/**
 * Created by UI/UX Team on 2018. 3. 5..
 */

import App from './app';
import enviroment from '../environments/env.js';
import routeConfig from './app.route.config';
/*
App.factory('httpReponseInterceptor', ($q, $location) => {
	return {
		responseError: function(res) {
			console.log(res);
			if(res.status === 440) {
		//		alert('세션이 만료되었습니다. 로그인 페이지로 이동합니다.');
		//		location.href = location.origin + location.pathname;
				return $q.reject(res);
			}
			return $q.reject(res);
		}
	}
});
*/
App.config(($stateProvider, $urlRouterProvider, $locationProvider, $httpProvider, $ocLazyLoadProvider) => {

	// provider default header setting about get 
	if (!$httpProvider.defaults.headers.get) $httpProvider.defaults.headers.get = {};
	$httpProvider.defaults.headers.get['If-Modified-Since'] = 'Mon, 26 Jul 1997 05:00:00 GTM';
	$httpProvider.defaults.headers.get['Cache-Control'] = 'no-cache';
	$httpProvider.defaults.headers.get['Pragma'] = 'no-cache';
	//$httpProvider.interceptors.push('httpReponseInterceptor');
	
	$ocLazyLoadProvider.config({
//		debug: true,
		events: true
	});
	
	Object.getOwnPropertyNames(routeConfig).map(v => {
		const _config = routeConfig[v];
		$stateProvider
			.state(v, _config);
	});
	
//    $urlRouterProvider.otherwise('/main/test');
    $urlRouterProvider.otherwise('/login');
//    $urlRouterProvider.otherwise('/main/blank');
});

App.config(($provide) => {
	$provide.decorator('textareaDirective', function($delegate, $log) {
		var directive = $delegate[0];
		angular.extend(directive.link, {
			post: function(scope, element, attr, ctrls) {
				element.on('compositionupdate', function(event){
					element.triggerHandler('compositionend');
				});
			}
		});
		return $delegate;
	});
});

App.config(($provide) => {
	$provide.decorator('inputDirective', function($delegate, $log) {
		var directive = $delegate[0];
		angular.extend(directive.link, {
			post: function(scope, element, attr, ctrls) {
				element.on('compositionupdate', function(event){
					element.triggerHandler('compositionend');
				});
			}
		});
		return $delegate;
	});
});
