webpackJsonp(["app\\common\\directive\\lnb\\lnb.directive"],{

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

/***/ "6edafd9aa20d5932bbed":
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

var LnbDirective = function () {
	function LnbDirective() {
		(0, _classCallCheck3.default)(this, LnbDirective);

		this.restrict = 'E';
		this.templateUrl = 'app/common/directive/lnb/lnb.html';
		this.scope = {
			items: '='
		};
	}

	(0, _createClass3.default)(LnbDirective, [{
		key: 'controller',
		value: function controller($scope, $state, $timeout) {
			$scope.$state = $state;
			$state.defaultErrorHandler(function (error) {
				console.log(error);
			});

			var $lnb = $('#lnb');

			$scope.click = function (e, item, idx) {

				var isButton = e.target.localName === 'button';

				if (isButton) {
					close(idx);
				} else {
					$($lnb.find('li.on')).removeClass('on');
					var $li = $($lnb.find('li').get(idx));
					$li.addClass('on');
					checkStateAndMove(idx);

					if (item.state) {
						$timeout(function () {
							$state.go(item.state);
						});
					}
				}
			};

			$scope.prev = function () {
				if (prevEvent) return;
				var width = getLiWidth();
				var prevLeft = parseInt($lnb.css('left'));
				var left = prevLeft + width;

				if (left > 0) {
					left = 0;
				}
				move(left);
			};

			$scope.post = function () {
				var prevLeft = parseInt($lnb.css('left'));
				var wrapWidth = getWrapWidth();
				var width = getLiWidth();
				var maxWidth = $scope.items.length * width - wrapWidth;
				var left = prevLeft - width;

				if (-left >= maxWidth) {
					left = -maxWidth;
				}
				move(left);
			};

			function close(idx) {
				var items = $scope.items;
				var lastIdx = items.length - 1;

				items.splice(idx, 1);

				var _idx = idx;

				if (idx === lastIdx) {
					_idx = idx - 1;
				}

				$scope._clickLi(_idx);
			}

			function checkStateAndMove(idx) {
				var $li = $($lnb.find('li').get(idx));
				var liOffsetLeft = $li.get(0).offsetLeft;
				var liOffsetEnd = liOffsetLeft + getLiWidth();

				var wrapLeft = parseInt($lnb.css('left'));
				var wrapWidth = getWrapWidth();

				var isOver = wrapWidth < liOffsetEnd + wrapLeft;
				var isAbove = liOffsetLeft < -wrapLeft;

				var left = 0;
				if (isOver) {
					left = -(liOffsetEnd - wrapWidth);
					move(left);
				} else if (isAbove) {
					left = -liOffsetLeft;
					move(left);
				} else {
					var distance = -(liOffsetEnd - wrapWidth);

					left = distance > 0 ? 0 : distance;

					move(left);
				}
			}

			var prevEvent = null;

			function move() {
				var left = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : 0;

				prevEvent = $lnb.stop(true, true).animate({ left: left }, 400, function () {
					prevEvent = null;
					checkBtnState();
				});
			}

			function getLiWidth() {
				var $li = $lnb.find('li');
				var width = $li.width();
				var padding = parseInt($li.css('padding-left')) + parseInt($li.css('padding-right'));
				var border = parseInt($li.css('border-left-width')) + parseInt($li.css('border-right-width'));

				return width + padding + border;
			}

			function getWrapWidth() {
				var wrapWidth = $('#lnbWrap').width();
				var btnWrapWidth = $('#lnbBtnWrap').width();
				return wrapWidth - btnWrapWidth;
			}

			function checkBtnState() {

				var $leftBtn = $('#lnbLeftBtn');
				var $rightBtn = $('#lnbRightBtn');

				var wrapLeft = parseInt($lnb.css('left'));
				var wrapWidth = getWrapWidth();

				var $lastLi = $lnb.find('li').last();

				if ($lastLi.length === 0) {
					$leftBtn.attr('disabled', true);
					$rightBtn.attr('disabled', true);
					return;
				}

				var lastLiOffsetEnd = $lastLi.get(0).offsetLeft + getLiWidth();
				var isOverLastLi = wrapWidth < lastLiOffsetEnd + wrapLeft;

				if (wrapLeft < 0) {
					$leftBtn.attr('disabled', false);
				} else {
					$leftBtn.attr('disabled', true);
				}

				if (isOverLastLi) {
					$rightBtn.attr('disabled', false);
				} else {
					$rightBtn.attr('disabled', true);
				}
			}

			$scope._clickLi = function (idx) {
				setTimeout(function () {
					$($lnb.find('li').get(idx)).click();
				});
			};
		}
	}, {
		key: 'link',
		value: function link($scope, element, attrs) {
			var $lnb = $('#lnb');
			var wrapWidth = $('#lnbWrap').width();

			$scope.$watch('items', function (newVal, oldVal) {

				if ($scope.items.length === 0) {
					$scope.$state.go('main.manageMsg');
					return;
				}

				var newValLength = newVal.length;
				var oldValLength = oldVal.length;
				var isAdd = newValLength > oldValLength;

				if (isAdd) {
					var idx = newValLength - 1;
					$scope._clickLi(idx);
				}
			}, true);
		}
	}]);
	return LnbDirective;
}();

(0, _angular.module)(_app2.default.name).directive('bcLnb', function () {
	return new LnbDirective();
});

/***/ }),

/***/ 8:
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__("6edafd9aa20d5932bbed");


/***/ })

},[8]);
//# sourceMappingURL=lnb.directive.js.map