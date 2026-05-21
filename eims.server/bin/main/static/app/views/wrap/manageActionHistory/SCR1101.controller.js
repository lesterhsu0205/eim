webpackJsonp(["app\\views\\wrap\\manageActionHistory\\SCR1101.controller"],{

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

/***/ 45:
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__("88ac9638d26ab21783db");


/***/ }),

/***/ "88ac9638d26ab21783db":
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

var SCR1101Controller = function () {
	function SCR1101Controller($scope, $state, $timeout, popupService, httpService, utilService, codeService, userService, gridService, codes) {
		var _this = this;

		(0, _classCallCheck3.default)(this, SCR1101Controller);

		this.$scope = $scope;
		this.$state = $state;
		this.$timeout = $timeout;
		this.popupService = popupService;
		this.httpService = httpService;
		this.utilService = utilService;
		this.codeService = codeService;
		this.userService = userService;
		this.gridService = gridService;
		this.codes = codes;
		this.user = this.userService.getUser();
		this.baseUrl = '/actionhists';

		this.initText();
		this.initSelect();
		this.initGrid();

		this.$scope.$on('gridRendered', function () {
			_this.initPrevData();
		});
	}

	(0, _createClass3.default)(SCR1101Controller, [{
		key: 'initPrevData',
		value: function initPrevData() {
			var _this2 = this;

			var currentStateName = this.$state.current.name;
			var param = this.utilService.getParams(currentStateName);

			if (!_.isEmpty(param)) {
				if (param.scope) {
					var prevScope = param.scope.vm;

					// 탐색
					this.searchParam = prevScope.searchParam;

					// 그리드 pageSize
					this.select.pageSize = prevScope.select.pageSize;
					this.options.limit = prevScope.select.pageSize;
					this.gridHeight = prevScope.gridHeight;

					// 그리드
					this.options.records = prevScope.options.records;
					this.options.recordsCount = prevScope.options.recordsCount;

					this.$timeout(function () {
						_this2.pageNumber = prevScope.pageNumber;
						_this2.$scope.$broadcast('resetPage', _this2.pageNumber);
					});
				}
			} else {
				this.resetSearch();
			}

			this.$scope.$on('$destroy', function () {
				_this2.utilService.setParams(currentStateName, { scope: _this2.$scope });
			});
		}
	}, {
		key: 'initText',
		value: function initText() {
			this.text = $.extend({}, bxMsg.getMessages('common'), bxMsg.getMessages('manageActionHistory'));
		}
	}, {
		key: 'initSelect',
		value: function initSelect() {
			this.select = {
				pageSize: '20'
			};
		}
	}, {
		key: 'initGrid',
		value: function initGrid() {
			var _this3 = this;

			this.gridHeight = this._getGridHeight();
			this.options = {
				limit: this.select.pageSize,
				autoComplete: false,
				pageSize: 10,
				recordsCount: 0,
				recid: 'id',
				columns: [{
					caption: 'No', size: '40px',
					render: function render(data, index) {
						var pageNumber = _this3.pageNumber || 1;
						return (pageNumber - 1) * _this3.options.limit + index + 1;
					}
				}, { field: 'hstDscd', caption: this.text.hstDscd, size: '2%', sortable: true,
					render: function render(data, index, colIndex) {
						return _this3.codeService.getCodeValNm('HISTORY_DSCD', data.hstDscd);
					}
				}, { field: 'itemId', caption: this.text.itemId, size: '3%', sortable: true }, { field: 'itemDesc', caption: this.text.itemDesc, size: '3%', sortable: true, attr: 'align=left' }, { field: 'workDttm', caption: this.text.workDttm, size: '3%', sortable: true,
					render: function render(data) {
						return _this3.utilService.setRegDttm(data.workDttm);
					}
				}, { field: 'userId', caption: this.text.userId, size: '1%', sortable: true }, { field: 'workCttCd', caption: this.text.workCttCd, size: '1%', sortable: true,
					render: function render(data, index, colIndex) {
						var msgDscd = w2ui[_this3.options.name].getCellValue(index, colIndex);
						return _this3.codeService.getCodeValNm('ACTION_STAT_CD', msgDscd);
					}
				}]
			};
		}
	}, {
		key: 'getGridData',
		value: function getGridData() {
			var _this4 = this;

			var goToFirst = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : false;

			var _gridService$getPageI = this.gridService.getPageInfo(this.select, this.pageNumber),
			    pageNumber = _gridService$getPageI.pageNumber,
			    pageSize = _gridService$getPageI.pageSize;

			var url = this.baseUrl + '?pageNumber=' + (goToFirst ? 1 : pageNumber) + '&pageSize=' + pageSize;

			var fromDt = new Date(this.searchParam.workDtFrom);
			var toDt = new Date(this.searchParam.workDtTo);

			var workDttmFrom = fromDt.getFullYear() + ('0' + (fromDt.getMonth() + 1)).slice(-2) + ('0' + fromDt.getDate()).slice(-2) + "000000";
			var workDttmTo = toDt.getFullYear() + ('0' + (toDt.getMonth() + 1)).slice(-2) + ('0' + toDt.getDate()).slice(-2) + "235959";

			this.searchParam.workDttmFrom = workDttmFrom;
			this.searchParam.workDttmTo = workDttmTo;

			this.httpService.get(url, this.searchParam).then(function (data) {
				var records = data.actionhisthsOutList,
				    recordsCount = data.totalCnt;


				records.map(function (v) {
					v.id = v.itemId + v.workDttm;
				});

				_this4.options.records = records;
				_this4.options.recordsCount = recordsCount;

				if (goToFirst) {
					_this4.pageNumber = 1;
					_this4.$scope.$broadcast('resetPage', _this4.pageNumber);
				}
			});
		}
	}, {
		key: 'search',
		value: function search() {
			this.getGridData(true);
		}
	}, {
		key: 'blur',
		value: function blur($event) {
			$event.target.blur();
		}
	}, {
		key: 'resetSearch',
		value: function resetSearch() {
			this.searchParam = {};
			var today = new Date();
			today = today.getFullYear() + "/" + ('0' + (today.getMonth() + 1)).slice(-2) + "/" + ('0' + today.getDate()).slice(-2);

			this.searchParam.workDtFrom = today;
			this.searchParam.workDtTo = today;

			this.getGridData(true);
		}
	}, {
		key: 'change',
		value: function change() {
			this.options.limit = this.select.pageSize;
			this.gridHeight = this._getGridHeight();
			this.pageBtnClick(1);
		}
	}, {
		key: 'pageBtnClick',
		value: function pageBtnClick(num) {
			this.pageNumber = num;
			this.getGridData(num === 1);
		}
	}, {
		key: 'openUserId',
		value: function openUserId() {
			var _this5 = this;

			this.popupService.openModal('SCR0102').then(function (user) {
				return _this5.searchParam.userId = user.userId;
			}).catch(function () {});
		}
	}, {
		key: '_getGridHeight',
		value: function _getGridHeight() {
			return this.select.pageSize * 25 + 34;
		}
	}]);
	return SCR1101Controller;
}();

(0, _angular.module)(_app2.default.name).controller('SCR1101Controller', SCR1101Controller);

/***/ })

},[45]);
//# sourceMappingURL=SCR1101.controller.js.map