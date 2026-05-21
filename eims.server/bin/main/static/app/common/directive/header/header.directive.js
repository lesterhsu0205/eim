webpackJsonp(["app\\common\\directive\\header\\header.directive"],{

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

/***/ 7:
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__("821b5ae7c54c5a7168f9");


/***/ }),

/***/ "821b5ae7c54c5a7168f9":
/***/ (function(module, exports, __webpack_require__) {

"use strict";


var _classCallCheck2 = __webpack_require__("49c81d67dea0f8060449");

var _classCallCheck3 = _interopRequireDefault(_classCallCheck2);

var _createClass2 = __webpack_require__("5115238a033983f8f227");

var _createClass3 = _interopRequireDefault(_createClass2);

var _angular = __webpack_require__("909592b0f5247409d892");

var _app = __webpack_require__("085c418bf06f41809dc1");

var _app2 = _interopRequireDefault(_app);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var HeaderDirective = function () {
	function HeaderDirective(httpService, $scope) {
		(0, _classCallCheck3.default)(this, HeaderDirective);

		this.restrict = 'E';
		this.templateUrl = 'app/common/directive/header/header.html';
		this.scope = $scope;

		this.httpService = httpService;
	}

	(0, _createClass3.default)(HeaderDirective, [{
		key: 'controller',
		value: function controller($scope, $state, userService, $location) {
			var _this = this;

			var user = userService.getUser();

			$scope.iconItems = [{
				icon: 'bxd bxd-my', label: user.userId, state: 'main.manageUser'
			}];

			$scope.user = { locale: user.locale };

			$scope.selectItems = [{ label: 'Korean', value: 'ko' }, { label: 'English', value: 'en' }];

			$scope.click = function () {
				var item = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : {};

				console.log('click::');
				if (item.state) {
					$state.go(item.state);
				}
			};

			$scope.changeLang = function (item) {
				var user = userService.getUser();
				user.locale = $scope.user.locale;
				var url = 'http://' + $location.host() + ':' + $location.port() + '/changelang?locale=' + user.locale;
				window.location.href = url;
			};

			$scope.goMain = function () {
				_this.$state.go('main.manageMsg');
			};

			$scope.logout = function () {

				userService.requestLogout($scope);

				console.log('click logout :)');
			};

			$scope.setTheme = function () {
				console.log('click set theme :)');
			};
		}
	}, {
		key: 'link',
		value: function link($scope, $element, attrs) {}
	}]);
	return HeaderDirective;
}();

(0, _angular.module)(_app2.default.name).directive('bcHeader', function () {
	return new HeaderDirective();
});

/***/ })

},[7]);
//# sourceMappingURL=header.directive.js.map