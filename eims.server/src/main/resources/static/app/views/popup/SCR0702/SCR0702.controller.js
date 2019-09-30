webpackJsonp(["app\\views\\popup\\SCR0702\\SCR0702.controller"],{

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

/***/ 30:
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__("33bc7fe6c6f752aaff32");


/***/ }),

/***/ "33bc7fe6c6f752aaff32":
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

var SCR0702Controller = function () {
	function SCR0702Controller($scope, $uibModalInstance, httpService, utilService, gridService, codeService, popupService, data) {
		(0, _classCallCheck3.default)(this, SCR0702Controller);

		this.$scope = $scope;
		this.$uibModalInstance = $uibModalInstance;
		this.httpService = httpService;
		this.utilService = utilService;
		this.gridService = gridService;
		this.codeService = codeService;
		this.popupService = popupService;

		this.initWindow(840, 460);
		this.initCode();
		this.initText();
		this.intrfc = {};
		this.data = data;

		this.tranDscdList = utilService.clone(this.tranDscdAllList);

		if (data.intrfcTypeCd === 'MCI') {
			this.tranDscdList.pop();
			this.intrfc.trxDscd = 'ONLINE';
		}
	}

	(0, _createClass3.default)(SCR0702Controller, [{
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
			this.text = $.extend({}, bxMsg.getMessages('common'), bxMsg.getMessages('manageMciInterface'));
		}
	}, {
		key: 'initCode',
		value: function initCode() {
			this.tranDscdAllList = this.codeService.getCodesByCdIdFromMem('TRAN_DSCD');
			this.syncDscdList = this.codeService.getCodesByCdIdFromMem('SYNC_DSCD');
		}
	}, {
		key: 'disabled',
		value: function disabled() {

			var $trxDscd = $('#trxDscd').find('select');
			$trxDscd.attr('disabled', false);
		}
	}, {
		key: 'closeModal',
		value: function closeModal(isOk) {
			if (isOk) {
				if (!this._checkValid()) return;
				this.createInterFace();
			} else {
				this.$uibModalInstance.dismiss();
			}
		}
	}, {
		key: 'createInterFace',
		value: function createInterFace() {
			// const requestBody = this.utilService.clone(this.intrfc);
			// requestBody.intrfcTypeCd = this.data.intrfcTypeCd;

			// this.httpService.post('/intrfccoms/intrfcidcreate', requestBody)
			// 				.then(res => {
			// 					if (res.isError) {
			// 						this.popupService.detailAlert(this.$scope, res.data.message, res.data.stackTrace);
			// 						return;
			// 					}

			// 					this.intrfc.intrfcId = res.intrfcId;
			this.intrfc.sndSys = this.sndSys;
			this.intrfc.rcvSys = this.rcvSys;
			this.$uibModalInstance.close(this.intrfc);
			// });
		}
	}, {
		key: 'selectApplication',
		value: function selectApplication() {
			var _this = this;

			this.popupService.openModal('SCR1402').then(function (code) {
				_this.intrfc.lv1Cd = code.appCd;
			}).catch(function () {});
		}
	}, {
		key: 'selectSndSys',
		value: function selectSndSys() {
			var _this2 = this;

			this.popupService.openModal('SCR1302').then(function (sndSys) {
				_this2.sndSys = sndSys;
				_this2.intrfc.sendSysCd = sndSys.sysCd;
			}).catch(function () {});
		}
	}, {
		key: 'selectRcvSys',
		value: function selectRcvSys() {
			var _this3 = this;

			this.popupService.openModal('SCR1302').then(function (rcvSys) {
				_this3.rcvSys = rcvSys;
				_this3.intrfc.receiveSysCd = rcvSys.sysCd;
			}).catch(function () {});
		}
	}, {
		key: 'chgTrxDscd',
		value: function chgTrxDscd(trxCd) {
			if (this.data.intrfcTypeCd != "FEP") {
				return;
			}

			if (trxCd == "ONLINE") {
				this.syncDscdList = this.codeService.getCodesByCdIdFromMem('SYNC_DSCD_FEP');
			} else {
				this.syncDscdList = this.codeService.getCodesByCdIdFromMem('SYNC_DSCD');
			}
		}
	}, {
		key: '_checkValid',
		value: function _checkValid() {
			if (_.isEmpty(this.intrfc.intrfcId)) {
				this.openAlert(this.text.emptyIntrcId);
				return false;
			} else if (_.isEmpty(this.intrfc.lv1Cd)) {
				this.openAlert(this.text.emptyLv3Cd);
				return false;
			} else if (_.isEmpty(this.sndSys)) {
				this.openAlert(this.text.emptySndSys);
				return false;
			} else if (_.isEmpty(this.rcvSys)) {
				this.openAlert(this.text.emptyRcvSys);
				return false;
			} else if (_.isEmpty(this.intrfc.trxDscd)) {
				this.openAlert(this.text.emptyTrxDscd);
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
	return SCR0702Controller;
}();

(0, _angular.module)(_app2.default.name).controller('SCR0702Controller', SCR0702Controller);

/***/ })

},[30]);
//# sourceMappingURL=SCR0702.controller.js.map