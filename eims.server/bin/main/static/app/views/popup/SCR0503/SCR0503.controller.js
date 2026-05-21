webpackJsonp(["app\\views\\popup\\SCR0503\\SCR0503.controller"],{

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

/***/ 27:
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__("545c4eb11c0709048216");


/***/ }),

/***/ "545c4eb11c0709048216":
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

var SCR0503Controller = function () {
	function SCR0503Controller($scope, $uibModalInstance, httpService, utilService, gridService, popupService) {
		(0, _classCallCheck3.default)(this, SCR0503Controller);

		this.$scope = $scope;
		this.$uibModalInstance = $uibModalInstance;
		this.httpService = httpService;
		this.gridService = gridService;
		this.popupService = popupService;

		this.initWindow(640, 460);
		this.initText();
	}

	(0, _createClass3.default)(SCR0503Controller, [{
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
		key: 'sendFile',
		value: function sendFile() {
			var _this = this;

			var $importFile = $('input:file');
			var file = $importFile[0].files[0];

			if (file === undefined) return;

			this.popupService.showLoadingBar(this.$scope);

			this.httpService.uploadFile('/msglayouts/fileuploads', { name: 'msglayoutFile', file: file }).then(function (res) {
				if (res.isError) {
					_this.uploadResult = _this.text.fail;
					_this.uploadResultMsg += res.data.message + '\r\n';
					_this.uploadResultMsg += res.data.parameters + '\r\n';
					_this.uploadResultMsg += res.data.stackTrace;
					return;
				}

				_this.uploadResult = _this.text.success;
				_this.uploadResultMsg = _this.text.completeUpload;

				_this.popupService.simpleAlert(_this.$scope, _this.text.completeUpload);

				_this.$uibModalInstance.close(res);
			}).finally(function () {
				_this.popupService.closeLoadingBar();
			});
		}
	}, {
		key: 'reset',
		value: function reset() {
			this.uploadResult = ' ';
			this.uploadResultMsg = ' ';
		}
	}, {
		key: 'closeModal',
		value: function closeModal(isOk) {
			this.$uibModalInstance.dismiss();
		}
	}]);
	return SCR0503Controller;
}();

(0, _angular.module)(_app2.default.name).controller('SCR0503Controller', SCR0503Controller);

/***/ })

},[27]);
//# sourceMappingURL=SCR0503.controller.js.map