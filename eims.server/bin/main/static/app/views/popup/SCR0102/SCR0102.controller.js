webpackJsonp(["app\\views\\popup\\SCR0102\\SCR0102.controller"],{

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

/***/ 22:
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__("27262a221476481ee965");


/***/ }),

/***/ "27262a221476481ee965":
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

var SCR0102Controller = function () {
	function SCR0102Controller($scope, $uibModalInstance, httpService, gridService, popupService) {
		(0, _classCallCheck3.default)(this, SCR0102Controller);

		this.$scope = $scope;
		this.$uibModalInstance = $uibModalInstance;
		this.httpService = httpService;
		this.gridService = gridService;
		this.popupService = popupService;

		this.initWindow(640, 640);
		this.initText();
		this.initSearch();
		this.initSelect();
		this.initUserGridOption();
		this.getUserList();
	}

	(0, _createClass3.default)(SCR0102Controller, [{
		key: 'initWindow',
		value: function initWindow(width, height) {
			var _popupService$calcula = this.popupService.calculatePosition(width, height),
			    top = _popupService$calcula.top,
			    left = _popupService$calcula.left;

			this.width = width;
			this.height = height;
			this.top = top;
			this.left = left;
			this.zIndex = this.popupService.getModalZIndex();
		}
	}, {
		key: 'initText',
		value: function initText() {
			this.text = {
				userSearch: bxMsg('manageUser.userSearch'),
				userId: bxMsg('manageUser.userId'),
				userNm: bxMsg('manageUser.userNm'),
				email: bxMsg('manageUser.email'),
				dutyNm: bxMsg('manageUser.dutyNm'),
				deptNm: bxMsg('manageUser.deptNm'),
				roleId: bxMsg('manageUser.roleId'),
				confirmOk: bxMsg('common.confirmOk'),
				confirmCancel: bxMsg('common.confirmCancel')
			};
		}
	}, {
		key: 'initSearch',
		value: function initSearch() {
			this.searchParam = {};
		}
	}, {
		key: 'initSelect',
		value: function initSelect() {
			this.select = this.gridService.getSelect(10);
		}
	}, {
		key: 'initUserGridOption',
		value: function initUserGridOption() {
			var _this = this;

			this.gridHeight = this.gridService.getGridHeight(this.select.pageSize);
			this.userOptions = {
				limit: this.select.pageSize,
				pageSize: this.select.pageSize,
				multiSelect: false,
				recordsCount: 0,
				recid: 'userId',
				columns: [{
					caption: 'No', size: '80px',
					render: function render(data, index) {
						var pageNumber = _this.pageNumber || 1;
						return (pageNumber - 1) * _this.userOptions.limit + index + 1;
					}
				}, { field: 'userId', caption: this.text.userId, sortable: true }, { field: 'userNm', caption: this.text.userNm, sortable: true }, { field: 'deptNm', caption: this.text.deptNm, sortable: true }, { field: 'roleId', caption: this.text.roleId, sortable: true }],
				onClick: function onClick(e) {
					// prevent deselect
					var selection = w2ui[e.target].getSelection();
					if (selection.length === 1 && selection[0] === e.recid) {
						e.preventDefault();
					}
				},
				onDblClick: function onDblClick(e) {
					return _this.closeModal(true);
				}
			};
		}
	}, {
		key: 'getUserList',
		value: function getUserList() {
			var _this2 = this;

			var goToFirst = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : false;

			var _getPageInfo = this.getPageInfo(),
			    pageNumber = _getPageInfo.pageNumber,
			    pageSize = _getPageInfo.pageSize;

			var url = '/users?pageNumber=' + (goToFirst ? 1 : pageNumber) + '&pageSize=' + pageSize;

			this.httpService.get(url, this.searchParam).then(function (data) {
				var records = data.userOutList,
				    recordsCount = data.totalCnt;


				_this2.userOptions.records = records;
				_this2.userOptions.recordsCount = recordsCount;

				if (goToFirst) {
					_this2.pageNumber = 1;
					_this2.$scope.$broadcast('resetPage', _this2.pageNumber);
				}
			});
		}
	}, {
		key: 'getPageInfo',
		value: function getPageInfo() {
			return {
				pageNumber: this.pageNumber || 1,
				pageSize: this.select.pageSize
			};
		}
	}, {
		key: 'resetSearch',
		value: function resetSearch() {
			this.searchParam = {};
			this.getUserList(true);
		}
	}, {
		key: 'search',
		value: function search() {
			this.getUserList(true);
		}
	}, {
		key: 'blur',
		value: function blur($event) {
			$event.target.blur();
		}
	}, {
		key: 'pageBtnClick',
		value: function pageBtnClick(num) {
			this.pageNumber = num;
			this.getUserList(num === 1);
		}
	}, {
		key: 'closeModal',
		value: function closeModal(isOk) {
			var grid = w2ui[this.userOptions.name];

			if (isOk) {
				var selection = grid.getSelection();

				if (selection.length === 0) {
					this.$uibModalInstance.dismiss();
				} else {
					this.$uibModalInstance.close(grid.get(selection[0]));
				}
			} else {
				this.$uibModalInstance.dismiss();
			}

			setTimeout(function () {
				return grid.destroy();
			});
		}
	}]);
	return SCR0102Controller;
}();

(0, _angular.module)(_app2.default.name).controller('SCR0102Controller', SCR0102Controller);

/***/ })

},[22]);
//# sourceMappingURL=SCR0102.controller.js.map