webpackJsonp(["app\\views\\popup\\SCR0708\\SCR0708.controller"],{

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

/***/ 35:
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__("955f80de7e03324cfdb7");


/***/ }),

/***/ "955f80de7e03324cfdb7":
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

var SCR0708Controller = function () {
	function SCR0708Controller($scope, $uibModalInstance, $timeout, httpService, utilService, codeService, gridService, popupService, data) {
		var _this = this;

		(0, _classCallCheck3.default)(this, SCR0708Controller);

		this.$scope = $scope;
		this.$uibModalInstance = $uibModalInstance;
		this.$timeout = $timeout;
		this.httpService = httpService;
		this.utilService = utilService;
		this.gridService = gridService;
		this.popupService = popupService;
		this.codeService = codeService;
		this.data = data;
		this.codes = codeService.commonCodes;

		this.initWindow(960, 640);
		this.initText();
		this.initGridOption();
		setTimeout(function () {
			return _this.getDetailGrid(_this.data.reqData);
		});

		this.setDeployDscd();
	}

	(0, _createClass3.default)(SCR0708Controller, [{
		key: 'setDeployDscd',
		value: function setDeployDscd() {
			var txt = '';

			if (this.data.reqData.deployResultCd.indexOf('SUCCESS') !== -1) {
				if (this.data.reqData.deployResultCd.indexOf('_') !== -1) {
					txt = this.text.redeploy;
				} else {
					txt = this.text.deploy;
				}
			} else {
				if (this.data.reqData.deployResultCd.indexOf('_') !== -1) {
					txt = this.text.redeployFail;
				} else {
					txt = this.text.deployFail;
				}
			}

			this.deployDesd = txt;
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
			this.text = $.extend({}, bxMsg.getMessages('common'), bxMsg.getMessages('manageMciInterface'));
		}
	}, {
		key: 'initGridOption',
		value: function initGridOption() {
			var _this2 = this;

			this.options = {
				limit: 99999,
				pageSize: 99999,
				recordsCount: 0,
				recid: 'systemCd',
				columns: [{ field: 'systemCd', caption: this.text.sysCd, size: '1%' }, { field: 'systemNm', caption: this.text.sysNm, size: '1%', attr: 'align=left' }, { field: 'systemUrl', caption: this.text.deployUrl, size: '1%', attr: 'align=left' }, {
					field: 'deployStatus', caption: this.text.deployResultCd, size: '1%',
					render: function render(data) {
						if (data.deployStatus === 'FAIL') {
							return '<span class="chr-c-orange">' + data.deployStatus + '</span>';
						}

						return data.deployStatus;
					}
				}],
				onClick: function onClick(e) {
					// prevent deselect
					var selection = w2ui[e.target].getSelection();
					if (selection.length === 1 && selection[0] === e.recid) {
						e.preventDefault();
					}

					var grid = w2ui[e.target];

					_this2.selectedIntrfcdeploysysdtDto = grid.get(e.recid);
					_this2.$scope.$apply();
				}
			};
		}
	}, {
		key: 'getDetailGrid',
		value: function getDetailGrid(reqData) {
			var _this3 = this;

			this.httpService.get('/intrfccoms/deployhistoryresults?', reqData).then(function (res) {
				_this3.options.records = res.intrfcDeployResponseList;
			});
		}
	}, {
		key: 'closeModal',
		value: function closeModal(isOk) {
			var grid = w2ui[this.options.name];

			this.$uibModalInstance.dismiss();

			setTimeout(function () {
				return grid.destroy();
			});
		}
	}]);
	return SCR0708Controller;
}();

(0, _angular.module)(_app2.default.name).controller('SCR0708Controller', SCR0708Controller);

/***/ })

},[35]);
//# sourceMappingURL=SCR0708.controller.js.map