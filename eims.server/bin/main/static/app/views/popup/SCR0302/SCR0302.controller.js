webpackJsonp(["app\\views\\popup\\SCR0302\\SCR0302.controller"],{

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

/***/ 24:
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__("e0e191d7631a484a090c");


/***/ }),

/***/ "e0e191d7631a484a090c":
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

var SCR0302Controller = function () {
	function SCR0302Controller($scope, $state, $uibModalInstance, httpService, utilService, gridService, popupService, data) {
		(0, _classCallCheck3.default)(this, SCR0302Controller);

		this.$scope = $scope;
		this.$state = $state;
		this.$uibModalInstance = $uibModalInstance;
		this.httpService = httpService;
		this.utilService = utilService;
		this.gridService = gridService;
		this.popupService = popupService;
		this.data = data;

		if (!data.width) data.width = 640;
		if (!data.height) data.height = 420;

		this.initWindow(data.width, data.height);
		this.initText();
		this.initPermGridOption();
		this.getPermList();
	}

	(0, _createClass3.default)(SCR0302Controller, [{
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
			this.text = $.extend(this.data.text, {
				useYn: bxMsg('mangePermission.useYn'),
				permId: bxMsg('mangePermission.permId'),
				permNm: bxMsg('mangePermission.permNm')
			});
		}
	}, {
		key: 'initPermGridOption',
		value: function initPermGridOption() {
			var _this = this;

			this.permOptions = {
				limit: 99999,
				pageSize: 99999,
				recordsCount: 0,
				recid: 'permId',
				columns: [{
					caption: 'No', size: '80px',
					render: function render(data, index) {
						return index + 1;
					}
				}, { field: 'check', caption: this.text.useYn, editable: { type: 'checkbox' } }, { field: 'permId', caption: this.text.permId, sortable: true }, { field: 'permNm', caption: this.text.permNm, sortable: true }],
				onClick: function onClick(e) {
					// prevent deselect
					var selection = w2ui[e.target].getSelection();
					if (selection.length === 1 && selection[0] === e.recid) {
						e.preventDefault();
					}

					var gridName = e.target;
					var recId = e.recid;
					var originalEvent = e.originalEvent;

					var eTarget = originalEvent.target;
					var $eTarget = $(eTarget);
					var grid = w2ui[gridName];
					var editData = grid.get(recId);
					var action = $eTarget.attr('data-action');

					if ($eTarget.context.checked) {
						editData.check = true;
					} else {
						editData.check = false;
					}
				},
				onDblClick: function onDblClick(e) {
					return _this.closeModal(true);
				}
			};
		}
	}, {
		key: 'getPermList',
		value: function getPermList() {
			var _this2 = this;

			var roleId = this.data.roleId;
			var menuId = this.data.menuId;

			this.httpService.get('/roles/' + roleId + '/permpopups?menuId=' + menuId).then(function (data) {
				_this2.permOptions.records = data;
				_this2.permOptions.recordsCount = data.length;
			});
		}
	}, {
		key: 'closeModal',
		value: function closeModal(isOk) {
			var grid = w2ui[this.permOptions.name];

			if (isOk) {
				var records = grid.records;

				records.map(function (v) {
					delete v.w2ui;
					delete v.recid;
				});

				this.$uibModalInstance.close(records);
			} else {
				this.$uibModalInstance.dismiss();
			}

			setTimeout(function () {
				return grid.destroy();
			});
		}
	}]);
	return SCR0302Controller;
}();

(0, _angular.module)(_app2.default.name).controller('SCR0302Controller', SCR0302Controller);

/***/ })

},[24]);
//# sourceMappingURL=SCR0302.controller.js.map