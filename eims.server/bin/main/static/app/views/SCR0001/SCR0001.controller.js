webpackJsonp(["app\\views\\SCR0001\\SCR0001.controller"],{

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

/***/ "2f88dabde4fb8d45d932":
/***/ (function(module, exports, __webpack_require__) {

"use strict";


var _classCallCheck2 = __webpack_require__("49c81d67dea0f8060449");

var _classCallCheck3 = _interopRequireDefault(_classCallCheck2);

var _createClass2 = __webpack_require__("5115238a033983f8f227");

var _createClass3 = _interopRequireDefault(_createClass2);

var _angular = __webpack_require__("909592b0f5247409d892");

var _app = __webpack_require__("085c418bf06f41809dc1");

var _app2 = _interopRequireDefault(_app);

var _env = __webpack_require__("5f92a5b1d82195799c4c");

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var LoginController = function () {
	function LoginController($scope, $state, userService, utilService, httpService, popupService, $location) {
		(0, _classCallCheck3.default)(this, LoginController);

		this.$scope = $scope;
		this.$state = $state;
		this.userService = userService;
		this.utilService = utilService;
		this.httpService = httpService;
		this.popupService = popupService;
		this.appName = _env.appName;
		this.location = $location;
		this.init();
		this.initText();
	}

	(0, _createClass3.default)(LoginController, [{
		key: 'init',
		value: function init() {
			this.user = { locale: 'en' };
			this.selectItems = [{ label: 'English', value: 'en' }, { label: '한국어', value: 'ko' }];

			//		{label: 'Japanese', value: 'jp'}
		}
	}, {
		key: 'initText',
		value: function initText() {
			this.text = {
				userId: bxMsg('login.userId'),
				userPassword: bxMsg('login.userPassword'),
				signIn: bxMsg('login.signIn')
			};
		}
		/**
   * 로그인 클릭 이벤트 콜백 함수
   */

	}, {
		key: 'signIn',
		value: function signIn() {
			var _this = this;

			var user = this.user;
			var userService = this.userService,
			    utilService = this.utilService;


			if (utilService.isEmpty(user.userId)) {
				this.openAlert(_env.errMsg.noneId);
				return;
			}

			if (utilService.isEmpty(user.userPassword)) {
				this.openAlert(_env.errMsg.nonePWD);
				return;
			}

			userService.requestSignIn(user).then(function () {
				return _this.goMain();
			}).catch(function (res) {
				return _this.openAlert(res.data.message);
			});
		}

		/*
  signIn() {
  	var url = 'http://'+this.location.host()+':' + this.location.port()+'/sso';
  	window.location.href = url;
  }
  */

	}, {
		key: 'goMain',
		value: function goMain() {
			this.$state.go('main.manageMsg');
		}
	}, {
		key: 'openAlert',
		value: function openAlert(alertBody) {
			this.popupService.simpleAlert(this.$scope, alertBody);
		}
	}]);
	return LoginController;
}();

(0, _angular.module)(_app2.default.name).controller('LoginController', LoginController);

/***/ }),

/***/ 43:
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__("2f88dabde4fb8d45d932");


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

/***/ })

},[43]);
//# sourceMappingURL=SCR0001.controller.js.map