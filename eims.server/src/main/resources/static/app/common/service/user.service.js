webpackJsonp(["app\\common\\service\\user.service"],{

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

/***/ 20:
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__("d9b86aa8f2d937b68787");


/***/ }),

/***/ "d9b86aa8f2d937b68787":
/***/ (function(module, exports, __webpack_require__) {

"use strict";


var _typeof2 = __webpack_require__("1316bd4a7881789a1fe9");

var _typeof3 = _interopRequireDefault(_typeof2);

var _classCallCheck2 = __webpack_require__("49c81d67dea0f8060449");

var _classCallCheck3 = _interopRequireDefault(_classCallCheck2);

var _createClass2 = __webpack_require__("5115238a033983f8f227");

var _createClass3 = _interopRequireDefault(_createClass2);

var _angular = __webpack_require__("909592b0f5247409d892");

var _app = __webpack_require__("085c418bf06f41809dc1");

var _app2 = _interopRequireDefault(_app);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var UserService = function () {
	function UserService($q, $state, httpService, utilService, popupService) {
		(0, _classCallCheck3.default)(this, UserService);

		this.$q = $q;
		this.$state = $state;
		this.utilService = utilService;
		this.httpService = httpService;
		this.popupService = popupService;
	}

	(0, _createClass3.default)(UserService, [{
		key: 'getUserPromise',
		value: function getUserPromise() {
			var _this = this;

			var defer = this.$q.defer();

			this.httpService.get('/logininfosso').then(function (res) {
				if (res.isError) defer.reject(res);else defer.resolve(_this._setUser(res));
			});

			return defer.promise;
		}
	}, {
		key: 'getUser',
		value: function getUser() {
			return this._user;
		}
	}, {
		key: 'requestSignIn',
		value: function requestSignIn(user) {
			var _this2 = this;

			var defer = this.$q.defer();

			this.httpService.post('/login', user).then(function (res) {
				if (res.isError) defer.reject(res);else defer.resolve(_this2._setUser(res));
			});
			return defer.promise;
		}
	}, {
		key: 'requestLogout',
		value: function requestLogout(scope) {
			var _this3 = this;

			this.httpService.post('/logout').then(function (res) {
				_this3.popupService.simpleConfirm(scope, bxMsg('common.logoutMsg'), function () {
					_this3._removeUser();
					_this3.$state.go('login');
				});
			});
		}
	}, {
		key: 'getUserMenu',
		value: function getUserMenu() {
			this._menu.map(function (menu) {
				menu.name = bxMsg('menu.' + menu.id);
			});

			return this._menu;
		}
	}, {
		key: '_setUser',
		value: function _setUser(loginUserInfo) {
			var _this4 = this;

			this._menu = loginUserInfo.menuList;
			this._user = loginUserInfo.userDto;
			this._user.locale = loginUserInfo.sessionInfo && (0, _typeof3.default)(loginUserInfo.sessionInfo) === 'object' ? loginUserInfo.sessionInfo.locale : this._getLocale();
			this._user.perm = {};

			loginUserInfo.permList.map(function (perm) {
				_this4._user.perm[perm.permId] = true;
			});

			return this._user;
		}
	}, {
		key: '_getLocale',
		value: function _getLocale() {
			var browserLocale = navigator.language || navigator.browserLanguage;
			var user = this._user;
			var locale = user && user.locale || browserLocale || 'ko';

			if (locale.indexOf('ko') !== -1) {
				locale = 'ko';
			} else if (locale.indexOf('en') !== -1) {
				locale = 'en';
			}

			return locale;
		}
	}, {
		key: '_removeUser',
		value: function _removeUser() {
			delete this._user;
		}
	}, {
		key: 'isAdmin',
		value: function isAdmin() {
			var user = this._user;
			return _.isEmpty(user) ? false : user.roleId === 'Administrator';
		}
	}]);
	return UserService;
}();

(0, _angular.module)(_app2.default.name).service('userService', UserService);

/***/ })

},[20]);
//# sourceMappingURL=user.service.js.map