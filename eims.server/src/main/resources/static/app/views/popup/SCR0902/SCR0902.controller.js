webpackJsonp(["app\\views\\popup\\SCR0902\\SCR0902.controller"],{

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

/***/ 36:
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__("9bc948720d32e85dca9a");


/***/ }),

/***/ "9bc948720d32e85dca9a":
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

var SCR0902Controller = function () {
	function SCR0902Controller($scope, $state, $uibModalInstance, httpService, utilService, gridService, popupService, codeService, userService, data) {
		(0, _classCallCheck3.default)(this, SCR0902Controller);

		this.$scope = $scope;
		this.$state = $state;
		this.$uibModalInstance = $uibModalInstance;
		this.httpService = httpService;
		this.utilService = utilService;
		this.gridService = gridService;
		this.popupService = popupService;
		this.codeService = codeService;
		this.userService = userService;
		this.user = this.userService.getUser();
		this.data = data;
		this.codes = data.codes;

		this.initWindow('100%', '100%');

		this.initWindow(data.width, data.height);
		this.initText();
		this.initSearch();
		this.initSelect();
		this.initIntrfcGridOption();
		this.getInteraceList();
	}

	(0, _createClass3.default)(SCR0902Controller, [{
		key: 'initWindow',
		value: function initWindow(width, height) {
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
			this.text = $.extend({}, bxMsg.getMessages('common'), bxMsg.getMessages('manageMciInterface'), bxMsg.getMessages('manageMetaInfo'));
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
		key: 'initIntrfcGridOption',
		value: function initIntrfcGridOption() {
			var _this = this;

			this.gridHeight = this.gridService.getGridHeight(this.select.pageSize);
			this.intrfcOptions = {
				limit: this.select.pageSize,
				pageSize: this.select.pageSize,
				multiSelect: false,
				recordsCount: 0,
				recid: 'intrfcId',
				columns: [{
					caption: 'No', size: '60px',
					render: function render(data, index) {
						var pageNumber = _this.pageNumber || 1;
						return (pageNumber - 1) * _this.intrfcOptions.limit + index + 1;
					}
				}, { field: 'intrfcId', caption: this.text.intrfcId, size: '3%' }, { field: 'intrfcNm', caption: this.text.intrfcNm, size: '3%', attr: 'align = left' }, {
					field: 'lvCds', caption: this.text.lvCds, size: '1.5%', sortable: true,
					render: function render(data) {
						return '' + data.lv1Cd;
					}
				}, {
					field: 'trxDscd', caption: this.text.trxTypeDscd, size: '1.5%',
					render: function render(data) {
						return _this.codeService.getCodeValNm('TRAN_DSCD', data.trxDscd);
					}
				}, {
					field: 'intrfcWayCd', caption: this.text.intrfcWayCd, size: '2%',
					render: function render(data) {
						return _this.codeService.getCodeValNm('INTRFC_WAY_CD', data.intrfcWayCd);
					}
				}, { field: 'regManId', caption: this.text.regManId, size: '1.5%' }, { field: 'workStatusCd', caption: this.text.workStatusCd, size: '1%',
					render: function render(data, index, colIndex) {
						return _this.codeService.getCodeValNm('WORK_STATUS_CD', data.workStatusCd);
					}
				}],
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
		key: 'getInteraceList',
		value: function getInteraceList() {
			var _this2 = this;

			var goToFirst = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : false;

			var _getPageInfo = this.getPageInfo(),
			    pageNumber = _getPageInfo.pageNumber,
			    pageSize = _getPageInfo.pageSize;

			var url = '/intrfccoms?pageNumber=' + (goToFirst ? 1 : pageNumber) + '&pageSize=' + pageSize;
			if (this.data.intrfcTypeCd) url += '&intrfcTypeCd=' + this.data.intrfcTypeCd;

			this.httpService.get(url, this.searchParam).then(function (data) {
				var records = data.intrfccombsOutList,
				    recordsCount = data.totalCnt;

				_this2.intrfcOptions.records = records;
				_this2.intrfcOptions.recordsCount = recordsCount;

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
			this.getInteraceList(true);
		}
	}, {
		key: 'search',
		value: function search() {
			this.getInteraceList(true);
		}
	}, {
		key: 'blur',
		value: function blur($event) {
			$event.target.blur();
		}
	}, {
		key: 'searchApplicationCodes',
		value: function searchApplicationCodes() {
			var _this3 = this;

			this.popupService.openModal('SCR1402', { limitLvCd: 0 }).then(function (code) {
				_this3.searchParam.lv1Cd = code.appCd;
				_this3.searchParam.lv1CdNm = code.appCdNm;
			}).catch(function () {});
		}
	}, {
		key: 'openRegManPopup',
		value: function openRegManPopup() {
			var _this4 = this;

			this.popupService.openModal('SCR0102').then(function (user) {
				return _this4.searchParam.regManId = user.userId;
			}).catch(function () {});
		}
	}, {
		key: 'pageBtnClick',
		value: function pageBtnClick(num) {
			this.pageNumber = num;
			this.getInteraceList(num === 1);
		}
	}, {
		key: 'closeModal',
		value: function closeModal(isOk) {
			var grid = w2ui[this.intrfcOptions.name];

			if (isOk) {
				var selection = grid.getSelection();

				if (selection.length === 0) {
					this.$uibModalInstance.dismiss();
				} else {
					if (this.data.getDetail) {
						this.getInterface(selection[0]);
					} else {
						this.$uibModalInstance.close(grid.get(selection[0]));
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
		key: 'getInterface',
		value: function getInterface(intrfcId) {
			var _this5 = this;

			this.httpService.get('/intrfccoms/' + intrfcId).then(function (res) {
				_this5.$uibModalInstance.close(res);
			});
		}
	}]);
	return SCR0902Controller;
}();

(0, _angular.module)(_app2.default.name).controller('SCR0902Controller', SCR0902Controller);

/***/ })

},[36]);
//# sourceMappingURL=SCR0902.controller.js.map