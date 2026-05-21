webpackJsonp(["app\\common\\directive\\accordion\\accordion.directive"],{

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

/***/ 3:
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__("dd5b6909c44ef7d72279");


/***/ }),

/***/ "dd5b6909c44ef7d72279":
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

var AccordionDirective = function () {
	function AccordionDirective() {
		(0, _classCallCheck3.default)(this, AccordionDirective);

		this.restrict = 'E';
		this.templateUrl = 'app/common/directive/accordion/accordion.html';
		this.transclude = true;
		this.scope = {
			items: '='
		};
		this.replace = true;
	}

	(0, _createClass3.default)(AccordionDirective, [{
		key: 'controller',
		value: function controller($scope, $state, utilService) {

			var currentName = $state.current.name;

			$scope.utilService = utilService;

			$scope.items.map(function (v) {

				var hasChildren = v.children;
				v.id = utilService.uniqueId('acMainMenu');

				if (!hasChildren) return;

				v.children.map(function (cV) {
					cV.id = utilService.uniqueId('acSubMenu');
					if (cV.state === currentName) {
						v.selected = true;
						setTimeout(function () {
							$('li#' + cV.id).click();
						});
					}
				});
			});

			$scope.toggle = function (idx) {
				var subItems = $('.bw-sub').get(idx);
				var $subItems = $(subItems);
				var isHide = $subItems.css('display') === 'none';

				if (isHide) {
					$subItems.stop(true, true).slideDown(function () {
						$scope.items[idx].selected = true;
					});
				} else {
					$subItems.stop(true, true).slideUp(function () {
						$scope.items[idx].selected = false;
					});
				}
			};
		}
	}, {
		key: 'link',
		value: function link($scope, $element, attrs) {

			var $parent = $scope.$parent;
			var utilService = $scope.utilService;

			var key = attrs.click;

			var _click = utilService.get($parent, key);

			if (_click) {
				_click = _click.bind($parent.vm || $parent);
			}

			$scope.click = function (e, item) {
				_click(e, item);
			};
		}
	}]);
	return AccordionDirective;
}();

(0, _angular.module)(_app2.default.name).directive('bcAccordion', function () {
	return new AccordionDirective();
});

/***/ })

},[3]);
//# sourceMappingURL=accordion.directive.js.map