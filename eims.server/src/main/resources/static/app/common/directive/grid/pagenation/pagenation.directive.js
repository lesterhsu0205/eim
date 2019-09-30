webpackJsonp(["app\\common\\directive\\grid\\pagenation\\pagenation.directive"],{

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

/***/ 6:
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__("9d80fcaa3d0b4a9dbca3");


/***/ }),

/***/ "9d80fcaa3d0b4a9dbca3":
/***/ (function(module, exports, __webpack_require__) {

"use strict";


var _from = __webpack_require__("7873414143bda77598d4");

var _from2 = _interopRequireDefault(_from);

var _classCallCheck2 = __webpack_require__("49c81d67dea0f8060449");

var _classCallCheck3 = _interopRequireDefault(_classCallCheck2);

var _createClass2 = __webpack_require__("5115238a033983f8f227");

var _createClass3 = _interopRequireDefault(_createClass2);

var _angular = __webpack_require__("909592b0f5247409d892");

var _app = __webpack_require__("085c418bf06f41809dc1");

var _app2 = _interopRequireDefault(_app);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var PagenationDirective = function () {
	function PagenationDirective() {
		(0, _classCallCheck3.default)(this, PagenationDirective);

		this.restrict = 'E';
		this.template = '\n\t\t\t<div class="bw-paging add-mg-t">\n\t\t\t\t<div class="ctr-btn">\n\t\t\t\t\t<button type="button" \n\t\t\t\t\t\t\tclass="btn-paging bxd bxd-toggle bxd-rotate-270"\n\t\t\t\t\t\t\tng-click="first()"></button>\n\t\t\t\t\t<button type="button" \n\t\t\t\t\t\t\tclass="btn-paging bxd bxd-arrow-left"\n\t\t\t\t\t\t\tng-click="prev()"></button>\n\t\t\t\t</div>\n\t\t\t\t<div class="paging-btn">\n\t\t\t\t\t<button type="button" class="btn-paging"\n\t\t\t\t\t\t\tng-repeat="idx in items" \n\t\t\t\t\t\t\tng-class="{\'on\': idx === currentPage}"\n\t\t\t\t\t\t\tng-click="move(idx)">\n\t\t\t\t\t\t\t{{idx}}</button>\n\t\t\t\t</div>\n\t\t\t\t<div class="ctr-btn">\n\t\t\t\t\t<button type="button" class="btn-paging bxd bxd-arrow-right"\n\t\t\t\t\t\t\tng-click="post()" ></button>\n\t\t\t\t\t<button type="button" class="btn-paging bxd bxd-toggle bxd-rotate-90"\n\t\t\t\t\t\t\tng-click="last()">\n\t\t\t\t\t</button>\n\t\t\t\t</div>\n\t\t\t</div>\n\t\t';

		this.scope = {
			target: '@',
			pageClick: '='
		};
	}

	(0, _createClass3.default)(PagenationDirective, [{
		key: 'controller',
		value: function controller($scope) {
			$scope.text = {
				"first": '처음',
				"pre": "이전",
				"post": "다음",
				"last": '마지막'
			};

			var $parent = $scope.$parent;

			var target = window.w2ui[$scope.target];
			var _click = void 0;

			if ($scope.pageClick) {
				_click = $scope.pageClick.bind($parent.vm || $parent);
			}

			$scope.currentPage = 1;

			var pageSize = +target.pageSize;

			$scope.$on('load:' + $scope.target, function () {
				$scope.caculatePageList = function () {
					var rowNum = target.limit;
					var total = target.recordsCount;
					var totalPage = Math.floor((total - 1) / rowNum) + 1;
					var currentPage = $scope.currentPage;

					var fIdx = pageSize * Math.floor((currentPage - 1) / pageSize) + 1;
					var isFirstList = fIdx === 1;
					var isLastList = fIdx + pageSize - 1 >= totalPage;
					var itemsSize = isLastList ? totalPage - fIdx + 1 : pageSize;

					$scope.items = (0, _from2.default)(new Array(itemsSize), function (v, idx) {
						return fIdx + idx;
					});

					if (total === 0) {
						$scope.items = [1];
						$scope.currentPage = 1;
					}

					$scope.totalPage = totalPage;
					$scope.isFirstList = isFirstList;
					$scope.isLastList = isLastList;
				};

				$scope.caculatePageList();
			});

			$scope.$on('resetPage', function (event, data) {
				$scope.currentPage = data ? data : 1;
			});

			$scope.prevPage = 1;

			$scope.move = function (num) {
				$scope.prevPage = $scope.currentPage;
				$scope.currentPage = num;
				_click && _click(num);
			};

			$scope.prev = function () {
				if ($scope.currentPage > 1) {
					$scope.prevPage = $scope.currentPage;
					var tmpPage = $scope.currentPage - pageSize;
					if (tmpPage < 1) tmpPage = 1;
					$scope.currentPage = tmpPage;
					$scope.caculatePageList();
				}
				var num = $scope.currentPage;
				_click && _click(num);
			};

			$scope.post = function () {

				if ($scope.currentPage < $scope.totalPage) {
					$scope.prevPage = $scope.currentPage;
					var tmpPage = pageSize + $scope.currentPage;
					if (tmpPage > $scope.totalPage) tmpPage = $scope.totalPage;
					$scope.currentPage = tmpPage;
					$scope.caculatePageList();
				}
				var num = $scope.currentPage;
				_click && _click(num);
			};

			$scope.first = function () {
				$scope.prevPage = $scope.currentPage;
				$scope.currentPage = 1;
				$scope.caculatePageList();
				var num = $scope.currentPage;
				_click && _click(num);
			};

			$scope.last = function () {
				$scope.prevPage = $scope.currentPage;
				$scope.currentPage = $scope.totalPage;
				$scope.caculatePageList();
				var num = $scope.currentPage;
				_click && _click(num);
			};
		}
	}, {
		key: 'link',
		value: function link($scope, $element, attrs) {}
	}]);
	return PagenationDirective;
}();

(0, _angular.module)(_app2.default.name).directive('bcPage', function () {
	return new PagenationDirective();
});

/***/ })

},[6]);
//# sourceMappingURL=pagenation.directive.js.map