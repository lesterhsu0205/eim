webpackJsonp(["app\\app.route.config"],{

/***/ 2:
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__("a762293aaf13627f4da0");


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
		state: 'main.test',
		url: '/test',
		templateUrl: 'app/views/wrap/test/test.html',
		controller: 'TestController',
		controllerUrl: 'app/views/wrap/test/test.controller.js',
		injectUrl: [],
		abstract: false,
		cache: true
	}, {
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
				return _commonCode($q, codeService, ['GRID_PAGE_SIZE']);
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

},[2]);
//# sourceMappingURL=app.route.config.js.map