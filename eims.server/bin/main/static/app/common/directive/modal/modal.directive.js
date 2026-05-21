webpackJsonp(["app\\common\\directive\\modal\\modal.directive"],{

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

/***/ "182f45adae7710da0b0a":
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

var ModalDirective = function () {
	function ModalDirective() {
		(0, _classCallCheck3.default)(this, ModalDirective);

		this.restrict = 'E';
		this.templateUrl = 'app/common/directive/modal/modal.html';
		this.transclude = true;
		this.scope = {
			modal: '=',
			dim: '=',
			open: '@',
			close: '@'
		};
	}

	(0, _createClass3.default)(ModalDirective, [{
		key: 'controller',
		value: function controller($scope, utilService) {
			var open = $scope.open,
			    close = $scope.close,
			    $parent = $scope.$parent;


			if (open) {
				$scope._open = utilService.get($parent, open);
			}

			if (close) {
				$scope._close = utilService.get($parent, close);
			}
		}
	}, {
		key: 'link',
		value: function link($scope, $element, attrs) {
			var $body = $('body');
			var $wrap = $($element.find('.modal-wrap'));
			var _$content = $($wrap.find('ng-transclude').children());
			var $dim = $('<div class="dim"></div>');

			var contentWidth = $wrap.get(0).offsetWidth;
			var contentHeight = $wrap.get(0).offsetHeight;

			var $grids = $($element.find('div[bc-grid]'));
			var hasGrid = $grids.length > 0;

			var _open = $scope._open,
			    _close = $scope._close;


			$scope.isShow = false;

			$scope.modal = {
				open: function open() {
					$scope.isShow = true;
					calculatePosition();
					hasGrid && refreshGrid();
					$scope.dim && $element.before($dim);
					_open && _open();
				},

				close: function close() {
					$scope.isShow = false;
					$scope.dim && $dim.remove();
					_close && _close();
				},

				destroy: function destroy() {
					console.log('destroy', $element);
					$element.remove();
				},

				$content: function $content() {
					return _$content;
				}
			};

			function calculatePosition() {
				var _window = window,
				    width = _window.innerWidth,
				    height = _window.innerHeight;


				var top = Math.round((height - contentHeight) / 2);
				var left = Math.round((width - contentWidth) / 2);
				$wrap.css('top', top + 'px');
				$wrap.css('left', left + 'px');
			}

			function refreshGrid() {
				$grids.map(function (i) {
					var $grid = $($grids.get(i));
					var name = $grid.attr('name');
					window.w2ui[name].refresh();
				});
			}

			$body.append($element);
		}
	}]);
	return ModalDirective;
}();

(0, _angular.module)(_app2.default.name).directive('bcModal', function () {
	return new ModalDirective();
});

/***/ }),

/***/ 9:
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__("182f45adae7710da0b0a");


/***/ })

},[9]);
//# sourceMappingURL=modal.directive.js.map