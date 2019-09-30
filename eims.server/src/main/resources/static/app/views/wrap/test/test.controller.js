webpackJsonp(["app\\views\\wrap\\test\\test.controller"],{

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

/***/ 61:
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__("7b2b7aff85bfeb09bd2a");


/***/ }),

/***/ "7b2b7aff85bfeb09bd2a":
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

var TestController = function () {
	function TestController($state) {
		(0, _classCallCheck3.default)(this, TestController);

		console.log('call Test Controller');
		this.test();
		this.$state = $state;
		this.modal;
	}

	(0, _createClass3.default)(TestController, [{
		key: 'test',
		value: function test() {
			console.log('call test Method:)');
		}
	}, {
		key: 'goChild',
		value: function goChild() {
			this.$state.go('abstract.child1');
		}
	}, {
		key: 'goExample',
		value: function goExample() {
			this.$state.go('main.example');
		}
	}, {
		key: 'openModal',
		value: function openModal() {
			this.modal.open();
		}
	}, {
		key: 'hideModal',
		value: function hideModal() {
			this.modal.close();
		}
	}, {
		key: 'a',
		value: function a() {
			console.log(123123123);
		}
	}]);
	return TestController;
}();

(0, _angular.module)(_app2.default.name).controller('TestController', TestController);

/***/ })

},[61]);
//# sourceMappingURL=test.controller.js.map