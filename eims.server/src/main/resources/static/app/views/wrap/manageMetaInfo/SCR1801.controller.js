webpackJsonp(["app\\views\\wrap\\manageMetaInfo\\SCR1801.controller"],{

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

/***/ 56:
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__("ed41355a247defc0a775");


/***/ }),

/***/ "ed41355a247defc0a775":
/***/ (function(module, exports, __webpack_require__) {

"use strict";


var _keys = __webpack_require__("a7da3c296e58f6811fbd");

var _keys2 = _interopRequireDefault(_keys);

var _classCallCheck2 = __webpack_require__("49c81d67dea0f8060449");

var _classCallCheck3 = _interopRequireDefault(_classCallCheck2);

var _createClass2 = __webpack_require__("5115238a033983f8f227");

var _createClass3 = _interopRequireDefault(_createClass2);

var _angular = __webpack_require__("909592b0f5247409d892");

var _app = __webpack_require__("085c418bf06f41809dc1");

var _app2 = _interopRequireDefault(_app);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var SCR1801Controller = function () {
	function SCR1801Controller($scope, $state, $timeout, httpService, utilService, gridService, popupService, codeService, metaService, userService, codes) {
		var _this = this;

		(0, _classCallCheck3.default)(this, SCR1801Controller);

		this.$scope = $scope;
		this.$state = $state;
		this.$timeout = $timeout;
		this.codes = codes;
		this.httpService = httpService;
		this.utilService = utilService;
		this.codeService = codeService;
		this.gridService = gridService;
		this.popupService = popupService;
		this.metaService = metaService;
		this.userService = userService;
		this.user = this.userService.getUser();

		this.initText();
		this.initSelect();
		this.resetSearch(true);
		this.initGrid();

		this.$scope.$on('gridRendered', function () {
			_this.initPrevData();
		});
	}

	(0, _createClass3.default)(SCR1801Controller, [{
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

					if (prevScope.select && (0, _keys2.default)(prevScope.select).length > 0) {
						// 상세
						this.select = prevScope.select;
					}

					if (prevScope.impactPopupVisible) {
						$('#meta-impact-poopup').show();
					}
				}
			} else {
				this.getMetaList();
			}

			this.$scope.$on('$destroy', function () {
				_this2.utilService.setParams(currentStateName, { scope: _this2.$scope });

				var impactPopup = $('#meta-impact-poopup');
				_this2.impactPopupVisible = impactPopup.is(':visible');
				impactPopup.hide();
			});
		}
	}, {
		key: 'initText',
		value: function initText() {
			this.text = $.extend({}, bxMsg.getMessages('common'), bxMsg.getMessages('manageMetaInfo'));
		}
	}, {
		key: 'initSelect',
		value: function initSelect() {
			this.select = this.gridService.getSelect(this.codes['GRID_PAGE_SIZE'][1].cdVal);
		}
	}, {
		key: 'initGrid',
		value: function initGrid() {
			var _this3 = this;

			this.gridHeight = this.gridService.getGridHeight(this.select.pageSize);
			this.options = {
				limit: this.select.pageSize,
				pageSize: this.select.pageSize,
				recordsCount: 0,
				multiSelect: false,
				recid: 'metaEngNm',
				columns: [{
					caption: 'No', size: '80px',
					render: function render(data, index) {
						var pageNumber = _this3.pageNumber || 1;
						return (pageNumber - 1) * _this3.options.limit + index + 1;
					}
				}, { field: 'metaEngNm', caption: this.text.metaEngNm, size: 2, sortable: true, attr: "align=left" }, { field: 'metaKorNm', caption: this.text.metaKorNm, size: 2, sortable: true, attr: "align=left" }, { field: 'dataTypeNm', caption: this.text.dataTypeNm, size: 1 }, { field: 'metaLen', caption: this.text.metaLen, size: 1 }, { field: 'decimalLen', caption: this.text.decimalLen, size: 1 }, {
					caption: this.text.influence, size: '80px',
					render: function render(data) {
						return '\n\t\t\t\t\t\t\t\t<button type="button" class="bw-btn bxd bxd-chart-bar" title="' + _this3.text.influenceModal + '" data-action="reflect"></button>\n\t\t\t\t\t\t\t';
					}
				}],
				onClick: function onClick(e) {
					// prevent deselect
					var selection = w2ui[e.target].getSelection();
					if (selection.length === 1 && selection[0] === e.recid) {
						e.preventDefault();
					}

					var eTarget = e.originalEvent.target;

					if (eTarget.localName === 'button') {
						_this3.popupService.openModal('SCR1803', { metaEngNm: e.recid }).then(function () {}).catch(function () {});
					}
				}
			};
		}
	}, {
		key: 'getMetaList',
		value: function getMetaList() {
			var _this4 = this;

			var goToFirst = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : false;

			var _getPageInfo = this.getPageInfo(),
			    pageNumber = _getPageInfo.pageNumber,
			    pageSize = _getPageInfo.pageSize;

			var url = '/metas?pageNumber=' + (goToFirst ? 1 : pageNumber) + '&pageSize=' + pageSize;

			this.httpService.get(url, this.searchParam).then(function (data) {

				if (data.metabsOutList == null && data.totalCnt == 0) {
					_this4.options.records = "";
					_this4.options.recordsCount = 0;
				} else {
					var records = data.metabsOutList,
					    recordsCount = data.totalCnt;


					_this4.options.records = records;
					_this4.options.recordsCount = recordsCount;
				}

				if (goToFirst) {
					_this4.pageNumber = 1;
					_this4.$scope.$broadcast('resetPage', _this4.pageNumber);
				}
			});
		}
	}, {
		key: 'search',
		value: function search() {
			this.getMetaList(true);
		}
	}, {
		key: 'blur',
		value: function blur($event) {
			$event.target.blur();
		}
	}, {
		key: 'resetSearch',
		value: function resetSearch(isConst) {
			if (isConst) {
				this.searchParam = {};
			} else {
				this.searchParam = {};
				this.getMetaList(true);
			}
		}
	}, {
		key: 'change',
		value: function change() {
			this.options.limit = this.select.pageSize;
			this.gridHeight = this.gridService.getGridHeight(this.select.pageSize);
			this.pageBtnClick(1);
		}
	}, {
		key: 'pageBtnClick',
		value: function pageBtnClick(num) {
			this.pageNumber = num;
			this.getMetaList(num === 1);
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
		key: 'syncMetaData',
		value: function syncMetaData() {
			var _this5 = this;

			this.popupService.showLoadingBar(this.$scope);
			this.metaService.syncMeta().then(function () {
				return _this5.getMetaList(true);
			}).finally(function () {
				_this5.popupService.closeLoadingBar();
				_this5.openAlert(_this5.text.syncMeta);
			});
		}
	}, {
		key: 'openAlert',
		value: function openAlert(alertBody) {
			this.popupService.simpleAlert(this.$scope, alertBody);
		}
	}]);
	return SCR1801Controller;
}();

(0, _angular.module)(_app2.default.name).controller('SCR1801Controller', SCR1801Controller);

/***/ })

},[56]);
//# sourceMappingURL=SCR1801.controller.js.map