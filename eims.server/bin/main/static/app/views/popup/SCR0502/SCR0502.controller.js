webpackJsonp(["app\\views\\popup\\SCR0502\\SCR0502.controller"],{

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

/***/ 26:
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__("3b28e8737313ca792ee7");


/***/ }),

/***/ "3b28e8737313ca792ee7":
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

var SCR0502Controller = function () {
	function SCR0502Controller($scope, $uibModalInstance, httpService, gridService, codeService, popupService, userService, data) {
		(0, _classCallCheck3.default)(this, SCR0502Controller);

		this.$scope = $scope;
		this.$uibModalInstance = $uibModalInstance;
		this.httpService = httpService;
		this.gridService = gridService;
		this.popupService = popupService;
		this.codeService = codeService;
		this.userService = userService;
		this.user = this.userService.getUser();
		this.data = data;

		this.initWindow('100%', '100%');
		this.initText();
		this.initSearch();
		this.initSelect();
		this.initCode();
		this.initMsgGridOption();
		this.getMsglayoutList();
	}

	(0, _createClass3.default)(SCR0502Controller, [{
		key: 'initWindow',
		value: function initWindow(width, height) {
			var _popupService$calcula = this.popupService.calculatePosition(width, height),
			    top = _popupService$calcula.top,
			    left = _popupService$calcula.left;

			this.width = width;
			this.height = height;
			this.top = 100;
			this.left = 50;
			this.right = 50;
			this.zIndex = this.popupService.getModalZIndex();
		}
	}, {
		key: 'initText',
		value: function initText() {
			this.text = $.extend({}, bxMsg.getMessages('common'), bxMsg.getMessages('manageMsg'));
		}
	}, {
		key: 'initSearch',
		value: function initSearch() {
			this.searchParam = {};

			if (this.data.trxDscdFilter) {
				if (this.data.trxDscd === 'ONLINE') {
					this.searchParam.trxDscd = 'ONLINE';
				} else if (this.data.trxDscd === 'BATCH') {
					this.searchParam.trxDscd = 'BATCH';
				}
			}
		}
	}, {
		key: 'initSelect',
		value: function initSelect() {
			this.select = this.gridService.getSelect(10);
			this.pageNumber = 1;
			this.selected = [];
		}
	}, {
		key: 'initCode',
		value: function initCode() {
			this.msgTypeList = this.codeService.getCodesByCdIdFromMem('MSG_TYPE');
			this.chlDscdList = this.codeService.getCodesByCdIdFromMem('CHL_DSCD');
			this.tranDscdList = this.codeService.getCodesByCdIdFromMem('TRAN_DSCD');
			this.workStatusList = this.codeService.getCodesByCdIdFromMem('WORK_STATUS_CD').filter(function (v) {
				return v.cdVal !== 'DEPLOY_COMP';
			});
		}
	}, {
		key: 'initMsgGridOption',
		value: function initMsgGridOption() {
			var _this = this;

			this.gridHeight = this.gridService.getGridHeight(this.select.pageSize);
			this.msgOptions = {
				limit: this.select.pageSize,
				pageSize: this.select.pageSize,
				show: { columnHeaders: true, selectColumn: this.data.multiSelect === false ? false : true },
				multiSelect: this.data.multiSelect === false ? false : true,
				recordsCount: 0,
				recid: 'msgLayoutId',
				columns: [{
					field: 'no', caption: 'No', size: '40px',
					render: function render(data, index) {
						return _this.gridService.getNoField(_this.msgOptions.limit, index, _this.pageNumber);
					}
				}, { field: 'msgLayoutId', caption: this.text.msgLayoutId, size: '2%', sortable: true }, { field: 'msgNm', caption: this.text.msgName, attr: "align=left", size: '2%', sortable: true }, {
					field: 'chlDscd', caption: this.text.chlDscd, size: this.user.locale === 'en' ? '110px' : '1%', sortable: true,
					render: function render(data, index, colIndex) {
						return _this.codeService.getCodeValNm('CHL_DSCD', data.chlDscd);
					}
				}, { field: 'trxDscd', caption: this.text.trxDscd, size: this.user.locale === 'en' ? '120px' : '1%', sortable: true,
					render: function render(data, index, colIndex) {
						var trxDscd = w2ui[_this.msgOptions.name].getCellValue(index, colIndex);
						return _this.codeService.getCodeValNm('TRAN_DSCD', trxDscd);
					}
				}, { field: 'msgDataVal', caption: this.text.msgDataVal + '/' + this.text.extrnlDtoNm, attr: "align=left", size: this.user.locale === 'en' ? '200px' : '2%', sortable: true }, {
					field: 'msgDscd', caption: this.text.msgType, size: '1%', sortable: true,
					render: function render(data, index, colIndex) {
						return _this.codeService.getCodeValNm('MSG_TYPE', data.msgDscd);
					}
				}, { field: 'msgVersion', caption: this.text.msgVersion, size: '1%', sortable: true }, {
					field: 'workStatusCd', caption: this.text.workStatusCd, size: this.user.locale === 'en' ? '110px' : '1%', sortable: true,
					render: function render(data, index, colIndex) {
						return _this.codeService.getCodeValNm('WORK_STATUS_CD', data.workStatusCd);
					}
				}, { field: 'regManId', caption: this.text.msgRegister, size: this.user.locale === 'en' ? '70px' : '1%', sortable: true }, { field: 'regDttm', caption: this.text.msgRegisterDt, size: this.user.locale === 'en' ? '110px' : '1%', sortable: true,
					render: function render(data) {
						var regDttm = data.regDttm;
						var yy = regDttm.substring(0, 4);
						var mm = regDttm.substring(4, 6);
						var dd = regDttm.substring(6, 8);
						return yy + "/" + mm + "/" + dd;
					}
				}],
				onClick: function onClick(e) {
					// prevent deselect
					var selection = w2ui[e.target].getSelection();
					if (selection.length === 1 && selection[0] === e.recid) {
						e.preventDefault();
					}
				},
				onSelect: function onSelect(e) {
					if (e.all) {
						if (_this.changedPage) {
							_this.changedPage = false;
						}
						_this.msgOptions.records.map(function (record) {
							var idx = _this.selected.indexOf(record.recid);
							if (idx === -1) {
								_this.selected.push(record.recid);
							}
						});
					} else {
						var idx = _this.selected.indexOf(e.recid);
						if (idx === -1) {
							_this.selected.push(e.recid);
						}
					}
				},
				onUnselect: function onUnselect(e) {
					if (e.all) {
						if (_this.changedPage) {
							_this.changedPage = false;
							return;
						}
						_this.msgOptions.records.map(function (record) {
							var idx = _this.selected.indexOf(record.recid);
							if (idx !== -1) {
								_this.selected.splice(idx, 1);
							}
						});
					} else {
						var idx = _this.selected.indexOf(e.recid);
						if (idx !== -1) {
							_this.selected.splice(idx, 1);
						}
					}
				},
				onDblClick: function onDblClick(e) {
					return _this.closeModal(true);
				}
			};
		}
	}, {
		key: 'getMsglayoutList',
		value: function getMsglayoutList() {
			var _this2 = this;

			var url = '/msglayouts?pageNumber=1&pageSize=99999';

			this.httpService.get(url, this.searchParam).then(function (data) {
				var records = data.msglayoutbsOutList,
				    recordsCount = data.totalCnt;


				_this2.msgOptionRecords = records;
				_this2.msgOptions.recordsCount = recordsCount;
				_this2.setPageConts();

				_this2.pageNumber = 1;
				_this2.$scope.$broadcast('resetPage', _this2.pageNumber);
				_this2.pageBtnClick(1);
			});
		}
	}, {
		key: 'setPageConts',
		value: function setPageConts() {
			var _this3 = this;

			var startIdx = (this.pageNumber - 1) * 10;
			var endIdx = this.pageNumber * 10;

			this.msgOptions.records = this.msgOptionRecords.slice(startIdx, endIdx);
			this.changedPage = true;

			setTimeout(function () {
				var grid = w2ui[_this3.msgOptions.name];
				grid.selectNone();

				_this3.selected.map(function (msgLayoutId) {
					grid.select(msgLayoutId);
				});
			}, 300);
		}
	}, {
		key: 'resetSearch',
		value: function resetSearch() {
			this.initSearch();
			this.getMsglayoutList();
		}
	}, {
		key: 'search',
		value: function search() {
			this.getMsglayoutList();
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
			this.setPageConts();
		}
	}, {
		key: 'closeModal',
		value: function closeModal(isOk) {
			var grid = w2ui[this.msgOptions.name];

			if (isOk) {
				if (this.selected.length === 0) {
					this.$uibModalInstance.dismiss();
				} else {
					if (this.data.getDetail) {
						this.getMsglayout(this.selected);
					} else {
						var selectionsDataList = [];

						this.selected.map(function (selection) {
							selectionsDataList.push(grid.get(selection));
						});
						this.$uibModalInstance.close(selectionsDataList);
					}
				}
			} else {
				this.$uibModalInstance.dismiss();
			}

			setTimeout(function () {
				return grid.destroy();
			});
		}
	}, {
		key: 'getMsglayout',
		value: function getMsglayout(selectionList) {
			var _this4 = this;

			this.httpService.post('/msglayoutslist', selectionList).then(function (res) {
				_this4.$uibModalInstance.close(res.msglayoutbsDtoList);
			});
		}
	}, {
		key: 'openApplicationCodeModalForSearchParam',
		value: function openApplicationCodeModalForSearchParam() {
			var _this5 = this;

			this.popupService.openModal('SCR1402').then(function (code) {
				_this5.searchParam.lv1Cd = code.appCd;
			}).catch(function () {});
		}
	}, {
		key: 'openRegManPopup',
		value: function openRegManPopup() {
			var _this6 = this;

			this.popupService.openModal('SCR0102').then(function (user) {
				return _this6.searchParam.regManId = user.userId;
			}).catch(function () {});
		}
	}]);
	return SCR0502Controller;
}();

(0, _angular.module)(_app2.default.name).controller('SCR0502Controller', SCR0502Controller);

/***/ })

},[26]);
//# sourceMappingURL=SCR0502.controller.js.map