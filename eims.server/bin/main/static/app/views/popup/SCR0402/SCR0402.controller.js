webpackJsonp(["app\\views\\popup\\SCR0402\\SCR0402.controller"],{

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

/***/ 25:
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__("be8b23479f3f7da8d0fe");


/***/ }),

/***/ "be8b23479f3f7da8d0fe":
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

var SCR0402Controller = function () {
	function SCR0402Controller($scope, $state, $uibModalInstance, httpService, utilService, gridService, popupService, data) {
		(0, _classCallCheck3.default)(this, SCR0402Controller);

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
		this.initMenuGridOption();
		this.getMenuList();
	}

	(0, _createClass3.default)(SCR0402Controller, [{
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
				save: bxMsg('common.save'),
				cancel: bxMsg('common.cancel')
			});
		}
	}, {
		key: 'initMenuGridOption',
		value: function initMenuGridOption() {
			var _this = this;

			var utilService = this.utilService;
			this.menuOptions = {
				limit: 99999,
				pageSize: 99999,
				recordsCount: 0,
				recid: 'id',
				columns: [{
					field: 'check', caption: '', size: '80px',
					render: function render(data, index) {
						var prtId = data.parentId;

						if (utilService.isEmpty(prtId)) {
							return;
						} else {
							return '\n\t\t\t\t\t\t\t\t<input type="checkbox" id="checked" data-action="check"></input>\n\t\t\t\t\t\t\t\t';
						}
					}
				}, { field: 'id', caption: this.text.menuId, sortable: true,
					render: function render(data) {
						var prtId = data.parentId;

						if (_this.utilService.isEmpty(prtId)) {
							return data.id;
						} else {
							return "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + data.id;
						}
					}

				}, { field: 'name', caption: this.text.menuNm, sortable: true }],
				onClick: function onClick(e) {
					// prevent deselect
					var selection = w2ui[e.target].getSelection();
					if (selection.length === 1 && selection[0] === e.recid) {
						e.preventDefault();
					}

					var grid = w2ui[e.target];
					var editData = grid.get(e.recid);
					var action = $(e.originalEvent.target).attr('data-action');

					if (action == "check" && editData.check == false) {
						var parentId = editData.parentId;

						editData.check = true;
						grid.records.filter(function (v) {
							return v.id == parentId;
						}).map(function (v) {
							return v.check = true;
						});
					} else if (action == "check" && editData.check == true) {
						var _parentId = editData.parentId;

						editData.check = false;
						grid.records.filter(function (v) {
							return v.id == _parentId;
						}).map(function (v) {
							return v.check = false;
						});
					}
				},
				onDblClick: function onDblClick(e) {
					return _this.closeModal(true);
				}
			};
		}
	}, {
		key: 'getMenuList',
		value: function getMenuList() {
			var _this2 = this;

			var roleId = this.data.roleId;

			this.httpService.get('/roles/' + roleId + '/menupopups').then(function (data) {
				_this2.menuOptions.records = _this2.gridService.convertDataToTreeData(data, "parentId");

				setTimeout(function () {
					_this2.gridService.expandAll(_this2.menuOptions, 2);
				}, 300);
			});
		}
	}, {
		key: 'closeModal',
		value: function closeModal(isOk) {
			var grid = w2ui[this.menuOptions.name];

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
	return SCR0402Controller;
}();

(0, _angular.module)(_app2.default.name).controller('SCR0402Controller', SCR0402Controller);

/***/ })

},[25]);
//# sourceMappingURL=SCR0402.controller.js.map