webpackJsonp(["app\\views\\popup\\SCR0505\\SCR0505.controller"],{

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

/***/ 29:
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__("3d2964123adffd487db1");


/***/ }),

/***/ "3d2964123adffd487db1":
/***/ (function(module, exports, __webpack_require__) {

"use strict";


var _defineProperty2 = __webpack_require__("458aa791eaa10a198e29");

var _defineProperty3 = _interopRequireDefault(_defineProperty2);

var _classCallCheck2 = __webpack_require__("49c81d67dea0f8060449");

var _classCallCheck3 = _interopRequireDefault(_classCallCheck2);

var _createClass2 = __webpack_require__("5115238a033983f8f227");

var _createClass3 = _interopRequireDefault(_createClass2);

var _angular = __webpack_require__("909592b0f5247409d892");

var _app = __webpack_require__("085c418bf06f41809dc1");

var _app2 = _interopRequireDefault(_app);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var SCR0505Controller = function () {
	function SCR0505Controller($scope, $state, $uibModalInstance, httpService, utilService, gridService, popupService, codeService, data) {
		(0, _classCallCheck3.default)(this, SCR0505Controller);

		this.$scope = $scope;
		this.$state = $state;
		this.$uibModalInstance = $uibModalInstance;
		this.httpService = httpService;
		this.utilService = utilService;
		this.gridService = gridService;
		this.codeService = codeService;
		this.popupService = popupService;
		this.data = data;
		this.sysMsg;
		this.inoutMsg;

		if (!data.width) data.width = 750;
		if (!data.height) data.height = 700;

		this.initWindow(data.width, data.height);
		this.initText();
		this.initCode();
		this.initSearch();
		this.initSelect();
		this.initIntrfcGridOption();
		this.initMakeMsgId();
	}

	(0, _createClass3.default)(SCR0505Controller, [{
		key: 'initCode',
		value: function initCode() {
			this.sysMsgList = this.codeService.getCodesByCdIdFromMem('MSG_SYS_TYPE');
			this.inoutMsgList = this.codeService.getCodesByCdIdFromMem('MSG_INOUT_TYPE');
		}
	}, {
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
			this.text = $.extend({}, bxMsg.getMessages('common'), bxMsg.getMessages('manageMetaInfo'));
		}
	}, {
		key: 'initSearch',
		value: function initSearch() {
			this.searchParam = {};
			this.searchParam.intrfcId = '';
			this.searchParam.intrfcNm = '';
		}
	}, {
		key: 'initSelect',
		value: function initSelect() {
			this.select = this.gridService.getSelect(10);
		}
	}, {
		key: 'initIntrfcGridOption',
		value: function initIntrfcGridOption() {
			var _this = this,
			    _intrfcOptions;

			this.gridHeight = this.gridService.getGridHeight(this.select.pageSize);
			this.intrfcOptions = (_intrfcOptions = {
				limit: this.select.pageSize,
				pageSize: this.select.pageSize,
				multiSelect: false,
				recordsCount: 0,
				recid: 'intrfcId'
			}, (0, _defineProperty3.default)(_intrfcOptions, 'multiSelect', false), (0, _defineProperty3.default)(_intrfcOptions, 'columns', [{
				caption: 'No', size: '40px',
				render: function render(data, index) {
					var pageNumber = _this.pageNumber || 1;
					return (pageNumber - 1) * _this.intrfcOptions.limit + index + 1;
				}
			}, { field: 'intrfcId', caption: this.text.intrfcId, size: '3%' }, { field: 'intrfcNm', caption: this.text.intrfcNm, size: '3%' }, { field: 'intrfcTypeCd', caption: this.text.dataTypeNm, size: '1%' }]), (0, _defineProperty3.default)(_intrfcOptions, 'onClick', function onClick(e) {
				// prevent deselect
				var selection = w2ui[e.target].getSelection();
				if (selection.length === 1 && selection[0] === e.recid) {
					e.preventDefault();
				}
				var eTarget = e.originalEvent.target;
			}), _intrfcOptions);
		}
	}, {
		key: 'initMakeMsgId',
		value: function initMakeMsgId() {
			var data = this.data;

			this.interfaceList = this.getInterfaceList;
			this.interfaceList();
		}
	}, {
		key: 'getInterfaceList',
		value: function getInterfaceList() {
			var _this2 = this;

			var url = '/intrfccoms?intrfcId=' + this.searchParam.intrfcId + '&intrfcNm=' + this.searchParam.intrfcNm + '&pageSize=999999';

			this.httpService.get(url, this.searchParam.intrfccoms).then(function (data) {
				for (var i = 0; i < data.totalCnt; i++) {
					if (data.intrfccombsOutList[i].intrfcTypeCd == 'MCI') {
						data.intrfccombsOutList[i].intrfcTypeCd = "MCA";
					} else if (data.intrfccombsOutList[i].intrfcTypeCd == 'EAI_I') {
						data.intrfccombsOutList[i].intrfcTypeCd = "EAI";
					}
				}
				_this2.intrfcOptions.records = data.intrfccombsOutList;
			});
		}
	}, {
		key: 'resetSearch',
		value: function resetSearch() {
			this.searchParam = {};
			this.searchParam.intrfcId = '';
			this.searchParam.intrfcNm = '';
			this.interfaceList(true);
		}
	}, {
		key: 'search',
		value: function search() {
			this.interfaceList(true);
		}
	}, {
		key: 'blur',
		value: function blur($event) {
			$event.target.blur();
		}
	}, {
		key: 'makeMsg',
		value: function makeMsg() {
			if (!this._checkValid()) return;
			this.closeModal(true);
		}
	}, {
		key: 'setSysMsg',
		value: function setSysMsg() {
			if (_.isEmpty(this.addMsg)) {
				this.msgTypes = [];
				return;
			}

			switch (this.addMsg.chlDscd) {
				case 'INTERNAL':
					switch (this.addMsg.trxDscd) {
						case 'ONLINE':
							this.msgTypes = this.msgTypeList.filter(function (v) {
								return v.cdVal == 'IV' || v.cdVal == 'STH';
							});
							break;
						case 'BATCH':
							this.msgTypes = this.msgTypeList.filter(function (v) {
								return v.cdVal == 'BATH' || v.cdVal == 'BATB' || v.cdVal == 'BATT';
							});
							break;
					}
					break;
				case 'EXTERNAL':
					switch (this.addMsg.trxDscd) {
						case 'ONLINE':
							this.msgTypes = this.msgTypeList.filter(function (v) {
								return v.cdVal == 'IV' || v.cdVal == 'CH';
							});
							break;
						case 'BATCH':
							this.msgTypes = this.msgTypeList.filter(function (v) {
								return v.cdVal == 'BATH' || v.cdVal == 'BATB' || v.cdVal == 'BATT';
							});
							break;
					}
					break;
			}
		}
	}, {
		key: '_checkValid',
		value: function _checkValid() {
			var grid = w2ui[this.intrfcOptions.name];
			var selection = grid.getSelection();
			if (selection.length === 0) {
				this.openAlert(this.text.selectInterfaceId);
				return false;
			}
			if (_.isEmpty(this.sysMsg)) {
				this.openAlert(this.text.selectSystem);
				return false;
			} else if (_.isEmpty(this.inoutMsg)) {
				this.openAlert(this.text.selectInout);
				return false;
			}

			return true;
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
					var msgId = grid.get(selection[0]).intrfcId + '_' + this.sysMsg + this.inoutMsg;
					this.$uibModalInstance.close(msgId);
				}
			} else {
				this.$uibModalInstance.dismiss();
			}

			setTimeout(function () {
				return grid.destroy();
			});
		}
	}, {
		key: 'openAlert',
		value: function openAlert(alertBody) {
			this.popupService.simpleAlert(this.$scope, alertBody);
		}
	}]);
	return SCR0505Controller;
}();

(0, _angular.module)(_app2.default.name).controller('SCR0505Controller', SCR0505Controller);

/***/ })

},[29]);
//# sourceMappingURL=SCR0505.controller.js.map