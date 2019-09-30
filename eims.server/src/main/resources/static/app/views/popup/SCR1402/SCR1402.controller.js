webpackJsonp(["app\\views\\popup\\SCR1402\\SCR1402.controller"],{

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

/***/ "08cf7210e8be6a85e24b":
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

var SCR1402Controller = function () {
	function SCR1402Controller($scope, $state, $uibModalInstance, httpService, utilService, gridService, popupService, data) {
		(0, _classCallCheck3.default)(this, SCR1402Controller);

		this.$scope = $scope;
		this.$state = $state;
		this.$uibModalInstance = $uibModalInstance;
		this.httpService = httpService;
		this.utilService = utilService;
		this.gridService = gridService;
		this.popupService = popupService;
		this.data = data;
		this.data.appId = '';
		if (!data.width) data.width = 440;
		if (!data.height) data.height = 500;

		this.initWindow(data.width, data.height);
		this.initText();
		this.initAppGridOption();
		this.getAppList();
	}

	(0, _createClass3.default)(SCR1402Controller, [{
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
			this.text = $.extend({}, bxMsg.getMessages('common'), bxMsg.getMessages('manageApp'));
		}
	}, {
		key: 'initAppGridOption',
		value: function initAppGridOption() {
			var _this = this;

			this.appOptions = {
				limit: 99999,
				pageSize: 99999,
				multiSelect: false,
				recordsCount: 0,
				recid: 'appUnqKey',
				columns: [{
					field: 'lvCd', caption: this.text.lvCd, size: '100px',
					render: function render(data) {
						return 'L' + data.lvCd;
					}
				}, {
					field: 'appCd', caption: this.text.appCd, style: 'text-align : left',
					render: function render(data) {
						return data.appCd + "(" + data.appCdNm + ")";
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
		key: 'resetSearch',
		value: function resetSearch() {
			this.data.appId = '';
			this.getAppList();
		}
	}, {
		key: 'search',
		value: function search() {
			this.getAppList();
		}
	}, {
		key: 'getAppList',
		value: function getAppList() {
			var _this2 = this;

			this.httpService.get('/apps?appCd=' + this.data.appId).then(function (data) {
				_this2.appOptions.recordsCount = data.totalCnt;
				_this2.appOptions.records = data.appcdOutList;
			});
		}
	}, {
		key: 'closeModal',
		value: function closeModal(isOk) {
			var grid = w2ui[this.appOptions.name];

			if (isOk) {
				var selection = grid.getSelection();

				if (selection.length === 0) {
					this.$uibModalInstance.dismiss();
				} else {
					var select = grid.get(selection[0]);
					this.$uibModalInstance.close(select);
				}
			} else {
				this.$uibModalInstance.dismiss();
			}

			setTimeout(function () {
				return grid.destroy();
			});
		}
	}]);
	return SCR1402Controller;
}();

(0, _angular.module)(_app2.default.name).controller('SCR1402Controller', SCR1402Controller);

/***/ }),

/***/ 39:
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__("08cf7210e8be6a85e24b");


/***/ })

},[39]);
//# sourceMappingURL=SCR1402.controller.js.map