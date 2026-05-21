webpackJsonp(["app\\common\\directive\\test.directive"],{

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

/***/ 11:
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__("edbedb031ca33caa082c");


/***/ }),

/***/ "edbedb031ca33caa082c":
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

var TestDirective = function () {
	function TestDirective() {
		(0, _classCallCheck3.default)(this, TestDirective);

		console.log('call TestDirective :)');
		this.restrict = 'E';
		this.template = '\n\t\t<ul class="search-data-wrap">\n\t\t\t<li ng-repeat="label in searchItems">\n\t\t\t\t{{label}}\n\t\t\t</li>\n\t\t</ul>\n\t\t';
		this.scope = {
			searchItems: '='
		};
	}

	(0, _createClass3.default)(TestDirective, [{
		key: 'controller',
		value: function controller($scope) {
			// dom [document object model] 이 생성되기 이전..
			console.log('testDirective Controller :)', $scope);
		}
	}, {
		key: 'link',
		value: function link($scope, element, attrs) {
			// dom 모델 생성 후 시점..
			console.log('testDriective Link:)', $scope, element, attrs);
			$scope.$watch('searchItems', function () {
				console.log('asdfas');
			});
		}
	}]);
	return TestDirective;
}();

(0, _angular.module)(_app2.default.name).directive('testDiv', function () {
	return new TestDirective();
});

/***/ })

},[11]);
//# sourceMappingURL=test.directive.js.map