webpackJsonp(["app\\common\\service\\popup.service"],{

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

/***/ 19:
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__("7ca1491512139fa08794");


/***/ }),

/***/ "7ca1491512139fa08794":
/***/ (function(module, exports, __webpack_require__) {

"use strict";


var _classCallCheck2 = __webpack_require__("49c81d67dea0f8060449");

var _classCallCheck3 = _interopRequireDefault(_classCallCheck2);

var _createClass2 = __webpack_require__("5115238a033983f8f227");

var _createClass3 = _interopRequireDefault(_createClass2);

var _angular = __webpack_require__("909592b0f5247409d892");

var _app = __webpack_require__("085c418bf06f41809dc1");

var _app2 = _interopRequireDefault(_app);

var _lodash = __webpack_require__("e957fe55c5f181ff4c72");

var _ = _interopRequireWildcard(_lodash);

function _interopRequireWildcard(obj) { if (obj && obj.__esModule) { return obj; } else { var newObj = {}; if (obj != null) { for (var key in obj) { if (Object.prototype.hasOwnProperty.call(obj, key)) newObj[key] = obj[key]; } } newObj.default = obj; return newObj; } }

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var PopupService = function () {
	function PopupService($q, $compile, $uibModal) {
		(0, _classCallCheck3.default)(this, PopupService);

		this.$q = $q;
		this.$compile = $compile;
		this.$uibModal = $uibModal;
		this.modal = ['START'];
		this.alert = [];
		this.confirm = [];
		this.loadingBar = null;
	}

	(0, _createClass3.default)(PopupService, [{
		key: 'showLoadingBar',
		value: function showLoadingBar(scope) {
			var $body = $('body');
			var fontSize = 150;

			var _calculatePosition = this.calculatePosition(fontSize, fontSize),
			    top = _calculatePosition.top,
			    left = _calculatePosition.left;

			var template = '\n\t\t\t<div class="dim" style="z-index:99999">\n\t\t\t\t<i class="bxd bxd-setting bxd-spin" \n\t\t\t\t\tstyle="position: relative; top:' + top + 'px; left:' + left + 'px; color: white; font-size:' + fontSize + 'px; width:' + fontSize + 'px; height:' + fontSize + 'px;"></i>\n\t\t\t</div>\n\t\t';

			this.loadingBar = this.$compile(template)(scope);
			$body.append(this.loadingBar);
		}
	}, {
		key: 'closeLoadingBar',
		value: function closeLoadingBar() {
			this.loadingBar && this.loadingBar.remove();
		}
	}, {
		key: 'simpleAlert',
		value: function simpleAlert(scope) {
			var text = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : "";
			var close = arguments.length > 2 && arguments[2] !== undefined ? arguments[2] : function () {};

			var $body = $('body');
			var width = 460,
			    height = 180;

			var _calculatePosition2 = this.calculatePosition(width, height),
			    top = _calculatePosition2.top,
			    left = _calculatePosition2.left;
			//const ok = bxMsg('common.confirmOk');


			var template = '\n\t\t\t<div class="dim" style="z-index:99999">\n\t\t\t\t<div class="simple-modal-wrap" style="top:' + top + 'px; left:' + left + 'px;">\n\t\t\t\t\t<div style="width: ' + width + 'px;">\n\t\t\t\t\t\t<h2 class="bw-tt a-center" style="height:auto; max-height: 400px; overflow: auto;">' + text + '</h2>\n\t\t\t\t\t\t<div class="btn-wrap add-mg-t">\n\t\t\t\t\t\t\t<button id="simpleAlertConfirm" type="button" class="bw-btn-txt">OK</button>\n\t\t\t\t\t\t</div>\n\t\t\t\t\t</div>\n\t\t\t\t</div>\n\t\t\t</div>\n\t\t';

			if (this.alert.length > 0) {
				var _alert$pop = this.alert.pop(),
				    _$simpleAlert = _alert$pop.$simpleAlert,
				    _close = _alert$pop.close;

				_close();
				_$simpleAlert.remove();
			}

			var $simpleAlert = this.$compile(template)(scope);
			$body.append($simpleAlert);
			this.alert.push({ $simpleAlert: $simpleAlert, close: close });

			$('#simpleAlertConfirm').click(function () {
				close();
				$simpleAlert.remove();
			});
		}
	}, {
		key: 'detailAlert',
		value: function detailAlert(scope) {
			var text = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : "";
			var detail = arguments.length > 2 && arguments[2] !== undefined ? arguments[2] : "";
			var param = arguments.length > 3 && arguments[3] !== undefined ? arguments[3] : "";
			var close = arguments.length > 4 && arguments[4] !== undefined ? arguments[4] : function () {};

			var $body = $('body');
			var width = 460,
			    height = 180;

			var _calculatePosition3 = this.calculatePosition(width, height),
			    top = _calculatePosition3.top,
			    left = _calculatePosition3.left;

			text = text ? text : '';
			detail = detail ? detail : '';
			param = param ? param : '';

			var template = '\n\t\t\t<div class="dim" style="z-index:99999">\n\t\t\t\t<div class="simple-modal-wrap" style="top:' + top + 'px; left:' + left + 'px;">\n\t\t\t\t\t<div style="width: ' + width + 'px;">\n\t\t\t\t\t\t<h2 class="bw-tt a-center" style="height:auto; max-height: 200px; overflow: auto;">' + text + '</h2>\n\t\t\t\t\t\t<span style="font-size: 15px; max-height: 200px; overflow: auto;">' + param + '</span>\n\t\t\t\t\t\t<div>\n\t\t\t\t\t\t\t<span id="detailToggle" class="chr-c-blue cs-p">\n\t\t\t\t\t\t\t\tdetails <i class="bxd bxd-toggle chr-c-blue" style="transform: rotate(180deg);font-size: 12px; width: 12px;"></i>\n\t\t\t\t\t\t\t</span>\n\t\t\t\t\t\t</div>\n\t\t\t\t\t\t<div id="detailWrapper" style="display: none;" class="detailWrapper">' + detail + '</div>\n\t\t\t\t\t\t<div class="btn-wrap add-mg-t">\n\t\t\t\t\t\t\t<button id="detailAlertConfirm" type="button" class="bw-btn-txt">confirm</button>\n\t\t\t\t\t\t</div>\n\t\t\t\t\t</div>\n\t\t\t\t</div>\n\t\t\t</div>\n\t\t';

			if (this.alert.length > 0) {
				var _alert$pop2 = this.alert.pop(),
				    $simpleAlert = _alert$pop2.$simpleAlert,
				    _close2 = _alert$pop2.close;

				_close2();
				$simpleAlert.remove();
			}

			var $detailAlert = this.$compile(template)(scope);
			$body.append($detailAlert);
			this.alert.push({ $simpleAlert: $detailAlert, close: close });

			$('#detailAlertConfirm').click(function () {
				close();
				$detailAlert.remove();
			});

			$('#detailToggle').click(function () {
				var $detail = $('#detailWrapper');
				var $toggle = $('#detailToggle>i');

				if ($detail.is(':visible')) {
					$detail.hide();
					$toggle.css('transform', 'rotate(180deg)');
				} else {
					$detail.show();
					$toggle.css('transform', '');
				}
			});
		}
	}, {
		key: 'pdfTest',
		value: function pdfTest(scope, template) {
			var $body = $('body');
			var width = 460,
			    height = 180;

			var _calculatePosition4 = this.calculatePosition(width, height),
			    top = _calculatePosition4.top,
			    left = _calculatePosition4.left;

			if (this.alert.length > 0) {
				var _alert$pop3 = this.alert.pop(),
				    _$simpleAlert2 = _alert$pop3.$simpleAlert,
				    _close3 = _alert$pop3.close;

				_close3();
				_$simpleAlert2.remove();
			}

			var $simpleAlert = this.$compile(template)(scope);
			$body.append($simpleAlert);
			this.alert.push({ $simpleAlert: $simpleAlert, close: close });
		}
	}, {
		key: 'simpleConfirm',
		value: function simpleConfirm(scope) {
			var text = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : "";
			var confirm = arguments.length > 2 && arguments[2] !== undefined ? arguments[2] : function () {};
			var cancel = arguments.length > 3 && arguments[3] !== undefined ? arguments[3] : function () {};
			var confirmTxt = arguments.length > 4 && arguments[4] !== undefined ? arguments[4] : bxMsg('common.confirmOk');
			var cancelTxt = arguments.length > 5 && arguments[5] !== undefined ? arguments[5] : bxMsg('common.confirmCancel');

			var $body = $('body');
			var width = 460,
			    height = 180;

			var _calculatePosition5 = this.calculatePosition(width, height),
			    top = _calculatePosition5.top,
			    left = _calculatePosition5.left;

			var template = '\n\t\t\t<div class="dim" style="z-index:99999">\n\t\t\t\t<div class="simple-modal-wrap" style="top:' + top + 'px; left:' + left + 'px;">\n\t\t\t\t\t<div style="width: ' + width + 'px;">\n\t\t\t\t\t\t<h2 class="bw-tt a-center" style="height:auto;">' + text + '</h2>\n\t\t\t\t\t\t<div class="btn-wrap add-mg-t">\n\t\t\t\t\t\t\t<button id="simpleConfirmOk" type="button" class="bw-btn-txt on">' + confirmTxt + '</button>\n\t\t\t\t\t\t\t<button id="simpleAlertCancel" type="button" class="bw-btn-txt">' + cancelTxt + '</button>\n\t\t\t\t\t\t</div>\n\t\t\t\t\t</div>\n\t\t\t\t</div>\n\t\t\t</div>\n\t\t';

			if (this.confirm.length > 0) {
				var _confirm$pop = this.confirm.pop(),
				    _$simpleConfirm = _confirm$pop.$simpleConfirm;

				_$simpleConfirm.remove();
			}

			var $simpleConfirm = this.$compile(template)(scope);
			$body.append($simpleConfirm);
			this.confirm.push({ $simpleConfirm: $simpleConfirm, cancel: cancel });

			$('#simpleConfirmOk').click(function () {
				confirm();
				$simpleConfirm.remove();
			});

			$('#simpleAlertCancel').click(function () {
				cancel();
				$simpleConfirm.remove();
			});
		}
	}, {
		key: 'openModal',
		value: function openModal(viewId) {
			var _data = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};

			var _this = this;

			var draggable = arguments[2];
			var resizable = arguments[3];

			var defer = this.$q.defer();
			var templateUrl = 'app/views/popup/' + viewId + '/' + viewId + '.tpl.html';
			var controller = viewId + 'Controller';

			$('.ui-tooltip').remove();

			var modalInstance = this.$uibModal.open({
				templateUrl: templateUrl,
				controller: controller,
				controllerAs: 'vm',
				resolve: {
					data: function data() {
						return _data;
					}
				}
			});

			this.modal.push(modalInstance);

			modalInstance.rendered.then(function () {
				if (draggable) {
					$('.simple-modal-wrap').draggable({
						cancel: '.search-wrap, .btn-wrap, #gridWrap',
						containment: 'body',
						cursor: 'auto'
					});
				}

				if (resizable) {
					$('.simple-modal-wrap').resizable();
				}
			});

			modalInstance.result.then(function (result) {
				_this.modal.pop();
				defer.resolve(result);
				$('.ui-tooltip').remove();
			}).catch(function (cancel) {
				_this.modal.pop();
				defer.reject(cancel);
				$('.ui-tooltip').remove();
			});

			modalInstance.closed.then(function (result) {
				$('[aria-describedby]').focusout();
			});

			return defer.promise;
		}
	}, {
		key: 'calculatePosition',
		value: function calculatePosition(contentWidth, contentHeight) {
			var _window = window,
			    width = _window.innerWidth,
			    height = _window.innerHeight;


			return {
				top: Math.round((height - contentHeight) / 2),
				left: Math.round((width - contentWidth) / 2)
			};
		}
	}, {
		key: 'getModalZIndex',
		value: function getModalZIndex() {
			return 1000 + this.modal.length;
		}
	}]);
	return PopupService;
}();

(0, _angular.module)(_app2.default.name).service('popupService', PopupService);

/***/ })

},[19]);
//# sourceMappingURL=popup.service.js.map