webpackJsonp(["app\\common\\directive\\grid\\grid.directive"],{

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

/***/ "44bc3d601ac8c100399b":
/***/ (function(module, exports, __webpack_require__) {

"use strict";


var _getOwnPropertyNames = __webpack_require__("dc3a14bb7bb348b98cab");

var _getOwnPropertyNames2 = _interopRequireDefault(_getOwnPropertyNames);

var _classCallCheck2 = __webpack_require__("49c81d67dea0f8060449");

var _classCallCheck3 = _interopRequireDefault(_classCallCheck2);

var _createClass2 = __webpack_require__("5115238a033983f8f227");

var _createClass3 = _interopRequireDefault(_createClass2);

var _angular = __webpack_require__("909592b0f5247409d892");

var _app = __webpack_require__("085c418bf06f41809dc1");

var _app2 = _interopRequireDefault(_app);

var _lodash = __webpack_require__("e957fe55c5f181ff4c72");

var _lodash2 = _interopRequireDefault(_lodash);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var GridDirective = function () {
	function GridDirective() {
		(0, _classCallCheck3.default)(this, GridDirective);

		this.restrict = 'A';
	}

	(0, _createClass3.default)(GridDirective, [{
		key: 'controller',
		value: function controller($scope, $compile, utilService) {
			$scope.$compile = $compile;
			$scope.utilService = utilService;
		}
	}, {
		key: 'link',
		value: function link($scope, $element, attrs) {
			var utilService = $scope.utilService;
			// 시점문제로 인해 비동기 생성으로 변경
			setTimeout(function () {
				var optionsKey = attrs.bcGrid;
				var page = attrs.page === 'true';

				if (!optionsKey) {
					throw new Error('grid option 을 기입해 주세요. ex) vm.[options]');
				}

				var options = utilService.get($scope, optionsKey);
				var uniqueId = utilService.uniqueId('bcGrid');

				if (!options) return;

				options.name = uniqueId;
				checkInstanceInW2ui(uniqueId);

				$element.w2grid(options);
				$scope.$broadcast('gridRendered');

				if (page) {
					var clickKey = attrs.pageClick;
					var $compile = $scope.$compile;
					$element.after($compile('\n\t\t\t\t\t\t<bc-page target="' + uniqueId + '" page-click="' + clickKey + '">\n\t\t\t\t\t\t</bc-page>\n\t\t\t\t\t')($scope));
				}

				//options 에 대한 two-wap binding..
				$scope.$watch(optionsKey, function (newVal, oldVal) {
					if (newVal.autoComplete) return;
					var keys = (0, _getOwnPropertyNames2.default)(newVal);
					keys.map(function (key) {
						w2ui[uniqueId][key] = newVal[key];
					});
					setTimeout(function () {
						if (w2ui[uniqueId]) w2ui[uniqueId].refresh();
					});
					$scope.$broadcast('load:' + uniqueId);
				}, true);
			});

			function checkInstanceInW2ui(name) {
				var w2ui = window.w2ui;

				if (w2ui[name]) {

					(0, _getOwnPropertyNames2.default)(w2ui).filter(function (v) {
						return new RegExp('^' + name + '_?').test(v);
					}).map(function (v) {
						return delete w2ui[v];
					});
				}
			}
		}
	}]);
	return GridDirective;
}();

(0, _angular.module)(_app2.default.name).directive('bcGrid', function () {
	return new GridDirective();
});

/***/ }),

/***/ 5:
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__("44bc3d601ac8c100399b");


/***/ })

},[5]);
//# sourceMappingURL=grid.directive.js.map