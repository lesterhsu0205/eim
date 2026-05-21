webpackJsonp(["app\\views\\popup\\SCR0703\\SCR0703.controller"],{

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

/***/ "2dcbfdb4b336db579a1c":
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

var SCR0703Controller = function () {
	function SCR0703Controller($scope, $uibModalInstance, $timeout, httpService, utilService, gridService, popupService, data) {
		var _this = this;

		(0, _classCallCheck3.default)(this, SCR0703Controller);

		this.$scope = $scope;
		this.$timeout = $timeout;
		this.$uibModalInstance = $uibModalInstance;
		this.httpService = httpService;
		this.gridService = gridService;
		this.popupService = popupService;
		this.data = data;

		this.initWindow(740, 560);
		this.initText();

		this.$timeout(function () {
			var $importFile = $('input:file');

			$importFile.on('change', function (event) {
				var $importFile = $('input:file');
				var files = $importFile[0].files;

				if (files === undefined) return;
				if (files.length > 10) {
					$importFile.val('');
					_this.popupService.simpleAlert(_this.$scope, _this.text.uploadFileCount);
					return;
				}

				var fileNames = '';

				for (var i = 0; i < files.length; i++) {
					fileNames += '[' + (i + 1) + '] ' + files[i].name + '\r\n';
				}

				$('.filenames').val(fileNames);
			});
		});
	}

	(0, _createClass3.default)(SCR0703Controller, [{
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
		key: 'sendFile',
		value: function sendFile() {
			var _this2 = this;

			var $importFile = $('input:file');
			var files = $importFile[0].files;

			if (files === undefined) return;

			this.popupService.showLoadingBar(this.$scope);

			var fileList = [];

			for (var i = 0; i < files.length; i++) {
				fileList.push(files[i]);
			}

			this.httpService.uploadFileList('/intrfccoms/import/intrfcfiles', { name: 'intrfcFile', file: fileList }, { intrfcTypeCd: this.data.intrfcTypeCd }).then(function (res) {
				console.log(res);
				if (res.isError) {
					_this2.isError = true;
					_this2.uploadResult = _this2.text.fail;
					_this2.uploadResultMsg = '';
					res.data.message && (_this2.uploadResultMsg += res.data.message + '\r\n');
					res.data.parameters && (_this2.uploadResultMsg += res.data.parameters.map(function (param) {
						return param + '\r\n';
					}));
					res.data.stackTrace && (_this2.uploadResultMsg += res.data.stackTrace);
					return;
				}

				_this2.isError = false;
				_this2.testList = res.data.map(function (data) {
					var fileConts = '';
					data.message && (fileConts += data.message + '\r\n');
					data.parameter && (fileConts += data.parameter.map(function (param) {
						return param + '\r\n';
					}));

					return {
						isError: data.status == 0 ? false : true,
						fileName: data.fileName,
						fileConts: fileConts
					};
				});
			}).finally(function () {
				_this2.popupService.closeLoadingBar();
			});
		}
	}, {
		key: 'sendFile2',
		value: function sendFile2() {
			var _this3 = this;

			var $importFile = $('input:file');
			var files = $importFile[0].files;

			if (files === undefined) return;

			this.popupService.showLoadingBar(this.$scope);

			var fileList = [];

			for (var i = 0; i < files.length; i++) {
				fileList.push(files[i]);
			}

			this.httpService.uploadFileList('/intrfccoms/import/definition', { name: 'intrfcFile', file: fileList }, { intrfcTypeCd: this.data.intrfcTypeCd }).then(function (res) {
				console.log(res);
				if (res.isError) {
					_this3.isError = true;
					_this3.uploadResult = _this3.text.fail;
					_this3.uploadResultMsg = '';
					res.data.message && (_this3.uploadResultMsg += res.data.message + '\r\n');
					res.data.parameters && (_this3.uploadResultMsg += res.data.parameters.map(function (param) {
						return param + '\r\n';
					}));
					res.data.stackTrace && (_this3.uploadResultMsg += res.data.stackTrace);
					return;
				}

				_this3.isError = false;
				_this3.testList = res.data.map(function (data) {
					var fileConts = '';
					data.message && (fileConts += data.message + '\r\n');
					data.parameter && (fileConts += data.parameter.map(function (param) {
						return param + '\r\n';
					}));

					return {
						isError: data.status == 0 ? false : true,
						fileName: data.fileName,
						fileConts: fileConts
					};
				});
			}).finally(function () {
				_this3.popupService.closeLoadingBar();
			});
		}
	}, {
		key: 'toggle',
		value: function toggle(fileIndex, isError) {
			if (!isError) return;

			var $fileConts = $('#file' + fileIndex);

			if ($fileConts.is(':visible')) {
				$fileConts.hide();
			} else {
				$fileConts.show();
			}
		}
	}, {
		key: 'openFile',
		value: function openFile() {
			this.reset();
			$('input:file').click();
		}
	}, {
		key: 'reset',
		value: function reset() {
			$('.filenames').val('');
			$('input:file').val('');
			this.isError = false;
			this.testList = [];
			this.uploadResult = ' ';
			this.uploadResultMsg = ' ';
		}
	}, {
		key: 'closeModal',
		value: function closeModal(isOk) {
			this.$uibModalInstance.dismiss();
		}
	}]);
	return SCR0703Controller;
}();

(0, _angular.module)(_app2.default.name).controller('SCR0703Controller', SCR0703Controller);

/***/ }),

/***/ 31:
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__("2dcbfdb4b336db579a1c");


/***/ })

},[31]);
//# sourceMappingURL=SCR0703.controller.js.map