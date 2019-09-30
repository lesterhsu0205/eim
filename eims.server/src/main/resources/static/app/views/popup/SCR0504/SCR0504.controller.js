webpackJsonp(["app\\views\\popup\\SCR0504\\SCR0504.controller"],{

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

/***/ 28:
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__("f107d5e3bdc37d58ed5c");


/***/ }),

/***/ "f107d5e3bdc37d58ed5c":
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

var SCR0504Controller = function () {
	function SCR0504Controller($scope, $uibModalInstance, httpService, utilService, gridService, userService, codeService, popupService, data) {
		(0, _classCallCheck3.default)(this, SCR0504Controller);

		this.$scope = $scope;
		this.$uibModalInstance = $uibModalInstance;
		this.httpService = httpService;
		this.utilService = utilService;
		this.gridService = gridService;
		this.codeService = codeService;
		this.popupService = popupService;
		this.userService = userService;
		this.user = this.userService.getUser();

		this.initWindow(840, 460);
		this.initCode();
		this.initText();

		this.intrfc = {};
		this.data = data;
		this.addMsg = {};
		this.addMsg.msgVersion = "1";
		this.addMsg.chlDscd = "INTERNAL";
		this.initApp();
	}

	(0, _createClass3.default)(SCR0504Controller, [{
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
			this.text = $.extend({}, bxMsg.getMessages('common'), bxMsg.getMessages('manageMsg'));
		}
	}, {
		key: 'initCode',
		value: function initCode() {
			this.tranDscdAllList = this.codeService.getCodesByCdIdFromMem('TRAN_DSCD');
			this.syncDscdList = this.codeService.getCodesByCdIdFromMem('CHL_DSCD');
			this.msgTypeList = this.codeService.getCodesByCdIdFromMem('MSG_TYPE');
		}
	}, {
		key: 'initApp',
		value: function initApp() {}
	}, {
		key: 'setMsgOption',
		value: function setMsgOption() {
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
		key: 'closeModal',
		value: function closeModal(isOk) {
			if (isOk) {
				if (this.user.roleId != 'Administrator' && this.addMsg.chlDscd == 'INTERNAL' && this.addMsg.trxDscd == 'ONLINE' && this.addMsg.msgDscd == 'STH') {
					this.popupService.simpleAlert(this.$scope, this.text.addCommonHeaderMsg);
					return;
				}

				if (!this._checkValid()) return;
				this.createMsgId();
			} else {
				this.$uibModalInstance.dismiss();
			}
		}
	}, {
		key: 'createMsgId',
		value: function createMsgId() {
			// const msgIdCreateDto = this.utilService.clone(this.addMsg);

			// this.httpService.post('/msglayouts/msgidcreate', msgIdCreateDto)
			// 				.then(res => {
			// 					if (res.isError) {
			// 						this.popupService.detailAlert(this.$scope, res.data.message, res.data.stackTrace);
			// 						return;
			// 					}
			this.$uibModalInstance.close(this.addMsg);
			// });
		}
	}, {
		key: 'makeMsgLayoutId',
		value: function makeMsgLayoutId() {
			var _this = this;

			this.popupService.openModal('SCR0505').then(function (code) {
				console.log(code);
				_this.addMsg.msgLayoutId = code;
			}).catch(function () {});
		}
	}, {
		key: 'selectApplication',
		value: function selectApplication() {
			var _this2 = this;

			this.popupService.openModal('SCR1402').then(function (code) {
				_this2.addMsg.lv1Cd = code.appCd;
			}).catch(function () {});
		}
	}, {
		key: 'selectSndSys',
		value: function selectSndSys() {
			var _this3 = this;

			this.popupService.openModal('SCR1302').then(function (sndSys) {
				_this3.addMsg.sndSysNm = sndSys.sysNm;
				_this3.addMsg.sendSysCd = sndSys.sysCd;
			}).catch(function () {});
		}
	}, {
		key: 'selectRcvSys',
		value: function selectRcvSys() {
			var _this4 = this;

			this.popupService.openModal('SCR1302').then(function (rcvSys) {
				_this4.addMsg.rcvSysNm = rcvSys.sysNm;
				_this4.addMsg.receiveSysCd = rcvSys.sysCd;
			}).catch(function () {});
		}
	}, {
		key: 'chgTrxDscd',
		value: function chgTrxDscd(trxCd) {
			if (trxCd == "ONLINE") {
				this.syncDscdList = this.codeService.getCodesByCdIdFromMem('SYNC_DSCD_FEP');
			} else {
				this.syncDscdList = this.codeService.getCodesByCdIdFromMem('SYNC_DSCD');
			}
		}
	}, {
		key: '_checkValid',
		value: function _checkValid() {
			if (_.isEmpty(this.addMsg.msgLayoutId)) {
				this.openAlert(this.text.emptyMsgLayoutId);
				return false;
			} else if (_.isEmpty(this.addMsg.lv1Cd)) {
				this.openAlert(this.text.emptyLv3Cd);
				return false;
			} else if (_.isEmpty(this.addMsg.chlDscd)) {
				this.openAlert(this.text.emptyChlDscd2);
				return false;
			} else if (_.isEmpty(this.addMsg.trxDscd)) {
				this.openAlert(this.text.emptyTrxDscd2);
				return false;
			}

			return true;
		}
	}, {
		key: 'openAlert',
		value: function openAlert(alertBody) {
			this.popupService.simpleAlert(this.$scope, alertBody);
		}
	}]);
	return SCR0504Controller;
}();

(0, _angular.module)(_app2.default.name).controller('SCR0504Controller', SCR0504Controller);

/***/ })

},[28]);
//# sourceMappingURL=SCR0504.controller.js.map