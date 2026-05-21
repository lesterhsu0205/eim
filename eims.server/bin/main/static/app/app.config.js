webpackJsonp(["app\\app.config"],{

/***/ 0:
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__("77142c408c0dbffdb56d");


/***/ }),

/***/ "085c418bf06f41809dc1":
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
    value: true
});

var _angular = __webpack_require__("909592b0f5247409d892");

var _angularjs = __webpack_require__("89359c59299c0818527e");

var _angularjs2 = _interopRequireDefault(_angularjs);

var _oclazyload = __webpack_require__("5ad117688c8b9520fcaf");

var _oclazyload2 = _interopRequireDefault(_oclazyload);

var _bxuipAngular = __webpack_require__("eec3252e846129d51d2e");

var _bxuipAngular2 = _interopRequireDefault(_bxuipAngular);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

/**
 * Created by UI/UX Team on 2018. 1. 19..
 */

var App = (0, _angular.module)('app', [_angularjs2.default, _oclazyload2.default, _bxuipAngular2.default, 'ui.bootstrap']);

exports.default = App;

/***/ }),

/***/ "470c52d489cf8cd054ee":
/***/ (function(module, exports) {

module.exports = {
	noneUrl: 'URL does not exist.',
	noneId: 'Please enter an ID.',
	nonePWD: 'Please enter an Password.'
};

/***/ }),

/***/ "5f92a5b1d82195799c4c":
/***/ (function(module, exports, __webpack_require__) {

/**
 * Created by UI/UX Team on 2018. 2. 14..
 */
var errMsg = __webpack_require__("470c52d489cf8cd054ee");

module.exports = {
	appName: 'EIMS',
    title: 'EIMS',
    url: {
    	validateUser: 'server/user...'
    },
	errMsg: errMsg
};

/***/ }),

/***/ "77142c408c0dbffdb56d":
/***/ (function(module, exports, __webpack_require__) {

"use strict";


var _getOwnPropertyNames = __webpack_require__("dc3a14bb7bb348b98cab");

var _getOwnPropertyNames2 = _interopRequireDefault(_getOwnPropertyNames);

var _app = __webpack_require__("085c418bf06f41809dc1");

var _app2 = _interopRequireDefault(_app);

var _env = __webpack_require__("5f92a5b1d82195799c4c");

var _env2 = _interopRequireDefault(_env);

var _appRoute = __webpack_require__("a762293aaf13627f4da0");

var _appRoute2 = _interopRequireDefault(_appRoute);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

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
_app2.default.config(function ($stateProvider, $urlRouterProvider, $locationProvider, $httpProvider, $ocLazyLoadProvider) {

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

	(0, _getOwnPropertyNames2.default)(_appRoute2.default).map(function (v) {
		var _config = _appRoute2.default[v];
		$stateProvider.state(v, _config);
	});

	//    $urlRouterProvider.otherwise('/main/test');
	$urlRouterProvider.otherwise('/login');
	//    $urlRouterProvider.otherwise('/main/blank');
}); /**
     * Created by UI/UX Team on 2018. 3. 5..
     */

_app2.default.config(function ($provide) {
	$provide.decorator('textareaDirective', function ($delegate, $log) {
		var directive = $delegate[0];
		angular.extend(directive.link, {
			post: function post(scope, element, attr, ctrls) {
				element.on('compositionupdate', function (event) {
					element.triggerHandler('compositionend');
				});
			}
		});
		return $delegate;
	});
});

_app2.default.config(function ($provide) {
	$provide.decorator('inputDirective', function ($delegate, $log) {
		var directive = $delegate[0];
		angular.extend(directive.link, {
			post: function post(scope, element, attr, ctrls) {
				element.on('compositionupdate', function (event) {
					element.triggerHandler('compositionend');
				});
			}
		});
		return $delegate;
	});
});

/***/ }),

/***/ "a762293aaf13627f4da0":
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
	value: true
});
/**
 * 라우스 설정 객체.
 * state {string} state 명
 * url {string} state 명
 * templateUrl {string} template path
 * controller {string} angular 에 등록한 controller 명
 * controllerUrl {string} 지정한 controller 의 path
 * abstract {boolean} 추상 여부 - 탭과 같이 하위 여러 url 존재 시에 true 로 활성화
 * cache {boolean} 캐쉬 여부
 */
var routeConfig = [{
	state: 'login',
	url: '/login',
	templateUrl: 'app/views/SCR0001/SCR0001.html',
	controller: 'LoginController',
	controllerUrl: 'app/views/SCR0001/SCR0001.controller.js',
	injectUrl: [],
	abstract: false,
	cache: false,
	resolve: {
		bxMsg: function bxMsg(messageService) {
			return messageService.initLogin(['login', 'menu']);
		}
	}
}, {
	state: 'main',
	url: '/main',
	templateUrl: 'app/views/wrap/wrap.html',
	controller: 'WrapController',
	controllerUrl: 'app/views/wrap/wrap.controller.js',
	injectUrl: [],
	abstract: true,
	cache: false,
	resolve: {
		bxMsg: function bxMsg(messageService) {
			return messageService.init(['login', 'menu', 'code', 'common', 'manageApp', 'manageMsg', 'mangePermission', 'manageCommSystem', 'manageTrxCode', 'manageExtInstCode', 'manageUser', 'manageRole', 'manageDeploySystem', 'manageMetaInfo', 'manageActionHistory', 'manageMciInterface', 'manageEaiInterface', 'manageFepInterface']);
		}
	},
	children: [{
		state: 'main.blank',
		url: '/blank',
		templateUrl: 'app/views/wrap/blank/blank.html',
		controller: 'BlankController',
		controllerUrl: 'app/views/wrap/blank/blank.controller.js',
		injectUrl: [],
		abstract: false,
		cache: false
	}, {
		state: 'main.manageMsg',
		url: '/manageMsg',
		templateUrl: 'app/views/wrap/manageMsg/SCR0501.html',
		controller: 'SCR0501Controller',
		controllerUrl: 'app/views/wrap/manageMsg/SCR0501.controller.js',
		injectUrl: [],
		abstract: false,
		cache: false,
		resolve: {
			codes: function codes($q, codeService) {
				return _commonCode($q, codeService, ['GRID_PAGE_SIZE', 'TRAN_DSCD', 'MSG_TYPE', 'CHN_DSCD', 'DATA_FORMAT_CD', 'ALIGN_CD', 'FILLER_CD', 'PARAM_TYPE', 'PRIVACY_CD', 'YN_CD', 'DATA_TYPE', 'CHL_DSCD', 'WORK_STATUS_CD']);
			}
		}
	}, {
		state: 'main.manageMciInterface',
		url: '/manageMciInterface',
		templateUrl: 'app/views/wrap/manageMciInterface/SCR0701.html',
		controller: 'SCR0701Controller',
		controllerUrl: 'app/views/wrap/manageMciInterface/SCR0701.controller.js',
		injectUrl: [],
		abstract: false,
		cache: false,
		resolve: {
			codes: function codes($q, codeService) {
				return _commonCode($q, codeService, ['GRID_PAGE_SIZE', 'TRAN_DSCD', 'SYNC_DSCD', 'YN_CD', 'DUP_FILE_PROC_CD', 'RECORD_SEPARATOR_CD', 'WORK_STATUS_CD', 'SENC_RECV_DSCD', 'GEN_CYCLE_CD', 'REQ_EVENT_DSCD', 'TIMEOUT_PROC_CD', 'DEPLOY_SYS_DSCD', 'MAPPING_DSCD', 'MSG_TYPE', 'CHL_DSCD', 'BACK_APR_STAT', 'TRX_TYPE_DSCD']);
			}
		}
	}, {
		state: 'main.manageMciInterfaceDetail',
		url: '/manageMciInterfaceDetail',
		templateUrl: 'app/views/wrap/manageMciInterfaceDetail/SCR0701.html',
		controller: 'SCR0701Controller2',
		controllerUrl: 'app/views/wrap/manageMciInterfaceDetail/SCR0701.controller.js',
		injectUrl: [],
		abstract: false,
		cache: false,
		resolve: {
			codes: function codes($q, codeService) {
				return _commonCode($q, codeService, ['GRID_PAGE_SIZE', 'TRAN_DSCD', 'SYNC_DSCD', 'YN_CD', 'DUP_FILE_PROC_CD', 'RECORD_SEPARATOR_CD', 'WORK_STATUS_CD', 'SENC_RECV_DSCD', 'GEN_CYCLE_CD', 'REQ_EVENT_DSCD', 'TIMEOUT_PROC_CD', 'DEPLOY_SYS_DSCD', 'MAPPING_DSCD', 'MSG_TYPE', 'CHL_DSCD', 'BACK_APR_STAT', 'TRX_TYPE_DSCD']);
			}
		}
	}, {
		state: 'main.manageEaiInterface',
		url: '/manageEaiInterface',
		templateUrl: 'app/views/wrap/manageEaiInterface/SCR0801.html',
		controller: 'SCR0801Controller',
		controllerUrl: 'app/views/wrap/manageEaiInterface/SCR0801.controller.js',
		injectUrl: [],
		abstract: false,
		cache: false,
		resolve: {
			codes: function codes($q, codeService) {
				return _commonCode($q, codeService, ['GRID_PAGE_SIZE', 'TRAN_DSCD', 'SYNC_DSCD', 'YN_CD', 'DUP_FILE_PROC_CD', 'RECORD_SEPARATOR_CD', 'WORK_STATUS_CD', 'SENC_RECV_DSCD', 'GEN_CYCLE_CD', 'REQ_EVENT_DSCD', 'TIMEOUT_PROC_CD', 'INTRFC_WAY_CD', 'MAPPING_DSCD', 'DEPLOY_SYS_DSCD', 'MSG_TYPE', 'CHL_DSCD', 'BACK_APR_STAT', 'RECV_DB_ACT_TYPE', 'TRAN_POST_PROC', 'TRAN_BEFORE_SCRIPT_TYPE', 'TRAN_POST_SCRIPT_TYPE', 'FIELD_ENCODING', 'WIDE_HALF_CHAR_CD']);
			}
		}
	}, {
		state: 'main.manageEaiInterfaceDetail',
		url: '/manageEaiInterfaceDetail',
		templateUrl: 'app/views/wrap/manageEaiInterfaceDetail/SCR0801.html',
		controller: 'SCR0801Controller2',
		controllerUrl: 'app/views/wrap/manageEaiInterfaceDetail/SCR0801.controller.js',
		injectUrl: [],
		abstract: false,
		cache: false,
		resolve: {
			codes: function codes($q, codeService) {
				return _commonCode($q, codeService, ['GRID_PAGE_SIZE', 'TRAN_DSCD', 'SYNC_DSCD', 'YN_CD', 'DUP_FILE_PROC_CD', 'RECORD_SEPARATOR_CD', 'WORK_STATUS_CD', 'SENC_RECV_DSCD', 'GEN_CYCLE_CD', 'REQ_EVENT_DSCD', 'TIMEOUT_PROC_CD', 'INTRFC_WAY_CD', 'MAPPING_DSCD', 'DEPLOY_SYS_DSCD', 'MSG_TYPE', 'CHL_DSCD', 'BACK_APR_STAT', 'RECV_DB_ACT_TYPE', 'TRAN_POST_PROC', 'TRAN_BEFORE_SCRIPT_TYPE', 'TRAN_POST_SCRIPT_TYPE', 'FIELD_ENCODING', 'WIDE_HALF_CHAR_CD']);
			}
		}
	}, {
		state: 'main.manageFepInterface',
		url: '/manageFepInterface',
		templateUrl: 'app/views/wrap/manageFepInterface/SCR0901.html',
		controller: 'SCR0901Controller',
		controllerUrl: 'app/views/wrap/manageFepInterface/SCR0901.controller.js',
		injectUrl: [],
		abstract: false,
		cache: false,
		resolve: {
			codes: function codes($q, codeService) {
				return _commonCode($q, codeService, ['GRID_PAGE_SIZE', 'TRAN_DSCD', 'SYNC_DSCD', 'YN_CD', 'DUP_FILE_PROC_CD', 'RECORD_SEPARATOR_CD', 'WORK_STATUS_CD', 'SENC_RECV_DSCD', 'GEN_CYCLE_CD', 'REQ_EVENT_DSCD', 'TIMEOUT_PROC_CD', 'DEPLOY_SYS_DSCD', 'MAPPING_DSCD', 'MSG_TYPE', 'CHL_DSCD', 'BACK_APR_STAT', 'SYNC_DSCD_FEP', 'PROTOCOL_DSCD', 'HTTP_METHOD', 'WIDE_HALF_CHAR_CD', 'ENC_CD_MAPPING', 'FIELD_ENCODING_ONL']);
			}
		}
	}, {
		state: 'main.manageFepInterfaceDetail',
		url: '/manageFepInterfaceDetail',
		templateUrl: 'app/views/wrap/manageFepInterfaceDetail/SCR0901.html',
		controller: 'SCR0901Controller2',
		controllerUrl: 'app/views/wrap/manageFepInterfaceDetail/SCR0901.controller.js',
		injectUrl: [],
		abstract: false,
		cache: false,
		resolve: {
			codes: function codes($q, codeService) {
				return _commonCode($q, codeService, ['GRID_PAGE_SIZE', 'TRAN_DSCD', 'SYNC_DSCD', 'YN_CD', 'DUP_FILE_PROC_CD', 'RECORD_SEPARATOR_CD', 'WORK_STATUS_CD', 'SENC_RECV_DSCD', 'GEN_CYCLE_CD', 'REQ_EVENT_DSCD', 'TIMEOUT_PROC_CD', 'DEPLOY_SYS_DSCD', 'MAPPING_DSCD', 'MSG_TYPE', 'CHL_DSCD', 'BACK_APR_STAT', 'SYNC_DSCD_FEP', 'WIDE_HALF_CHAR_CD', 'ENC_CD_MAPPING', 'FIELD_ENCODING_ONL']);
			}
		}
	}, {
		state: 'main.manageDeploySystem',
		url: '/manageDeploySystem',
		templateUrl: 'app/views/wrap/manageDeploySystem/SCR1201.html',
		controller: 'SCR1201Controller',
		controllerUrl: 'app/views/wrap/manageDeploySystem/SCR1201.controller.js',
		injectUrl: [],
		abstract: false,
		cache: false,
		resolve: {
			codes: function codes($q, codeService) {
				return _commonCode($q, codeService, ['GRID_PAGE_SIZE', 'DEPLOY_SYS_DSCD']);
			}
		}
	}, {
		state: 'main.manageCommSystem',
		url: '/manageCommSystem',
		templateUrl: 'app/views/wrap/manageCommSystem/SCR1301.html',
		controller: 'SCR1301Controller',
		controllerUrl: 'app/views/wrap/manageCommSystem/SCR1301.controller.js',
		injectUrl: [],
		abstract: false,
		cache: false,
		resolve: {
			codes: function codes($q, codeService) {
				return _commonCode($q, codeService, ['GRID_PAGE_SIZE', 'YN_CD']);
			}
		}
	}, {
		state: 'main.manageAppCode',
		url: '/manageAppCode',
		templateUrl: 'app/views/wrap/manageAppCode/SCR1401.html',
		controller: 'SCR1401Controller',
		controllerUrl: 'app/views/wrap/manageAppCode/SCR1401.controller.js',
		injectUrl: [],
		abstract: false,
		cache: false,
		resolve: {
			codes: function codes($q, codeService) {
				return _commonCode($q, codeService, ['GRID_PAGE_SIZE', 'LEVEL_CD']);
			}
		}
	}, {
		state: 'main.manageExtInstCode',
		url: '/manageExtInstCode',
		templateUrl: 'app/views/wrap/manageExtInstCode/SCR1601.html',
		controller: 'SCR1601Controller',
		controllerUrl: 'app/views/wrap/manageExtInstCode/SCR1601.controller.js',
		injectUrl: [],
		abstract: false,
		cache: false,
		resolve: {
			codes: function codes($q, codeService) {
				return _commonCode($q, codeService, ['GRID_PAGE_SIZE', 'EXT_INST_TYPE']);
			}
		}
	}, {
		state: 'main.manageMetaInfo',
		url: '/manageMetaInfo',
		templateUrl: 'app/views/wrap/manageMetaInfo/SCR1801.html',
		controller: 'SCR1801Controller',
		controllerUrl: 'app/views/wrap/manageMetaInfo/SCR1801.controller.js',
		injectUrl: [],
		abstract: false,
		cache: false,
		resolve: {
			codes: function codes($q, codeService) {
				return _commonCode($q, codeService, ['GRID_PAGE_SIZE']);
			}
		}
	}, {
		state: 'main.manageActionHistory',
		url: '/manageActionHistory',
		templateUrl: 'app/views/wrap/manageActionHistory/SCR1101.html',
		controller: 'SCR1101Controller',
		controllerUrl: 'app/views/wrap/manageActionHistory/SCR1101.controller.js',
		injectUrl: [],
		abstract: false,
		cache: false,
		resolve: {
			codes: function codes($q, codeService) {
				return _commonCode($q, codeService, ['GRID_PAGE_SIZE', 'ACTION_STAT_CD', 'HISTORY_DSCD']);
			}
		}
	}, {
		state: 'main.manageUser',
		url: '/manageUser',
		templateUrl: 'app/views/wrap/manageUser/SCR0101.html',
		controller: 'SCR0101Controller',
		controllerUrl: 'app/views/wrap/manageUser/SCR0101.controller.js',
		injectUrl: [],
		abstract: false,
		cache: false,
		resolve: {
			codes: function codes($q, codeService) {
				return _commonCode($q, codeService, ['GRID_PAGE_SIZE']);
			}
		}
	}, {
		state: 'main.manageRole',
		url: '/manageRole',
		templateUrl: 'app/views/wrap/manageRole/SCR0201.html',
		controller: 'SCR0201Controller',
		controllerUrl: 'app/views/wrap/manageRole/SCR0201.controller.js',
		injectUrl: [],
		abstract: false,
		cache: false,
		resolve: {
			codes: function codes($q, codeService) {
				return _commonCode($q, codeService, ['GRID_PAGE_SIZE']);
			}
		}
	}, {
		state: 'main.managePerm',
		url: '/managePerm',
		templateUrl: 'app/views/wrap/mangePermission/SCR0301.html',
		controller: 'SCR0301Controller',
		controllerUrl: 'app/views/wrap/mangePermission/SCR0301.controller.js',
		injectUrl: [],
		abstract: false,
		cache: false,
		resolve: {
			codes: function codes($q, codeService) {
				return _commonCode($q, codeService, ['GRID_PAGE_SIZE', 'PERM_TYPE']);
			}
		}
	}]
}];

function _commonCode($q, codeService, codeIds) {
	var q = $q.defer();
	var codes = {};

	codeService.getCodesList(codeIds).then(function (data) {
		for (var i in codeIds) {
			codes[codeIds[i]] = data[i];
		}

		q.resolve(codes);
	});

	return q.promise;
}

/**
 * 앱 라우드 객체
 */
var appRoute = {};

/**
 * 앱 라우트 setting 함수.
 */
(function flatRouteConfig(config) {

	config.map(function (v) {
		var state = v.state,
		    url = v.url,
		    templateUrl = v.templateUrl,
		    controller = v.controller,
		    controllerUrl = v.controllerUrl,
		    injectUrl = v.injectUrl,
		    abstract = v.abstract,
		    cache = v.cache,
		    children = v.children,
		    resolve = v.resolve;


		appRoute[state] = {
			url: url,
			templateUrl: templateUrl,
			controller: controller,
			controllerAs: 'vm',
			abstract: abstract,
			resolve: $.extend(resolve, {
				lazyLoad: function lazyLoad($ocLazyLoad) {
					return $ocLazyLoad.load({
						files: [].concat(controllerUrl, injectUrl),
						cache: cache
					});
				}
			})
		};

		if (children) flatRouteConfig(children);
	});
})(routeConfig);

/**
 * 기본 모듈로 appRoute 를 export
 */
exports.default = appRoute;

/***/ })

},[0]);
//# sourceMappingURL=app.config.js.map