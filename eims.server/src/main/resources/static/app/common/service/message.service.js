webpackJsonp(["app\\common\\service\\message.service"],{

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

/***/ 17:
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__("c7e541055b4b703d49ce");


/***/ }),

/***/ "470c52d489cf8cd054ee":
/***/ (function(module, exports) {

module.exports = {
	noneUrl: 'url이 존재하지 않습니다.',
	noneId: 'ID를 입력해주세요.',
	nonePWD: '암호를 입력해주세요.'
};

/***/ }),

/***/ "c7e541055b4b703d49ce":
/***/ (function(module, exports, __webpack_require__) {

"use strict";


var _classCallCheck2 = __webpack_require__("49c81d67dea0f8060449");

var _classCallCheck3 = _interopRequireDefault(_classCallCheck2);

var _createClass2 = __webpack_require__("5115238a033983f8f227");

var _createClass3 = _interopRequireDefault(_createClass2);

var _angular = __webpack_require__("909592b0f5247409d892");

var _app = __webpack_require__("085c418bf06f41809dc1");

var _app2 = _interopRequireDefault(_app);

var _lodash = __webpack_require__("e957fe55c5f181ff4c72");

var _ = _interopRequireWildcard(_lodash);

var _errMsg = __webpack_require__("470c52d489cf8cd054ee");

var _errMsg2 = _interopRequireDefault(_errMsg);

function _interopRequireWildcard(obj) { if (obj && obj.__esModule) { return obj; } else { var newObj = {}; if (obj != null) { for (var key in obj) { if (Object.prototype.hasOwnProperty.call(obj, key)) newObj[key] = obj[key]; } } newObj.default = obj; return newObj; } }

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var MessageService = function () {
	function MessageService($q, $state, userService) {
		(0, _classCallCheck3.default)(this, MessageService);

		this.$q = $q;
		this.$state = $state;
		this.userService = userService;

		this.messageRoot = '/assets/json/messages';
		this.isLoad = false;
		this.userKey = "USER";
	}

	(0, _createClass3.default)(MessageService, [{
		key: 'init',
		value: function init(messageList) {
			var _this = this;

			var defer = this.$q.defer();

			this.userService.getUserPromise().then(function (user) {
				defer.resolve(bxMsg.init({
					messageRoot: _this.messageRoot,
					locale: user.locale,
					messageList: messageList
				}));
			}).catch(function (err) {
				return _this.$state.go('login');
			});

			return defer.promise;
		}
	}, {
		key: 'initLogin',
		value: function initLogin(messageList) {
			var browserLocale = navigator.language || navigator.browserLanguage;
			var locale = browserLocale || 'ko';

			if (locale.indexOf('ko') !== -1) {
				locale = 'ko';
			} else if (locale.indexOf('en') !== -1) {
				locale = 'en';
			}

			bxMsg.init({
				messageRoot: this.messageRoot,
				locale: locale,
				messageList: messageList
			});
		}
	}]);
	return MessageService;
}();

(0, _angular.module)(_app2.default.name).service('messageService', MessageService);

/***/ })

},[17]);
//# sourceMappingURL=message.service.js.map