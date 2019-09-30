webpackJsonp(["app\\views\\popup\\SCR1802\\SCR1802.controller"],{

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

/***/ 41:
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__("e76b9a74006cbee659da");


/***/ }),

/***/ "e76b9a74006cbee659da":
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

var SCR1802Controller = function () {
	function SCR1802Controller($scope, $state, $uibModalInstance, httpService, utilService, gridService, popupService, data) {
		(0, _classCallCheck3.default)(this, SCR1802Controller);

		this.$scope = $scope;
		this.$state = $state;
		this.$uibModalInstance = $uibModalInstance;
		this.httpService = httpService;
		this.utilService = utilService;
		this.gridService = gridService;
		this.popupService = popupService;
		this.data = data;

		if (!data.width) data.width = 750;
		if (!data.height) data.height = 700;

		this.initWindow(data.width, data.height);
		this.initText();
		this.initSearch();
		this.initSelect();
		this.initIntrfcGridOption();
		this.initGetEffectsList();
	}

	(0, _createClass3.default)(SCR1802Controller, [{
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
		}
	}, {
		key: 'initSelect',
		value: function initSelect() {
			this.select = this.gridService.getSelect(10);
		}
	}, {
		key: 'initIntrfcGridOption',
		value: function initIntrfcGridOption() {
			var _this = this;

			this.gridHeight = this.gridService.getGridHeight(this.select.pageSize);
			this.intrfcOptions = {
				limit: this.select.pageSize,
				pageSize: this.select.pageSize,
				multiSelect: false,
				recordsCount: 0,
				recid: 'intrfcId',
				columns: [{
					caption: 'No', size: '40px',
					render: function render(data, index) {
						var pageNumber = _this.pageNumber || 1;
						return (pageNumber - 1) * _this.intrfcOptions.limit + index + 1;
					}
				}, { field: 'intrfcId', caption: this.text.intrfcId, size: '3%' }, { field: 'intrfcNm', caption: this.text.intrfcNm, size: '3%', attr: "align=left" }, { field: 'intrfcType', caption: this.text.dataTypeNm, size: '1%' }, { field: 'msgLayoutId', caption: this.text.msgId, size: '2%' }, {
					field: 'more', caption: this.text.showDetail, size: '1%',
					render: function render(data) {
						return '\n\t\t\t\t\t\t\t<button type="button" class="bw-btn bxd bxd-zoom-in" data-action="more"></button>\n\t\t\t\t\t\t';
					}
				}],
				onClick: function onClick(e) {
					// prevent deselect
					var selection = w2ui[e.target].getSelection();
					if (selection.length === 1 && selection[0] === e.recid) {
						e.preventDefault();
					}
					var eTarget = e.originalEvent.target;

					if (eTarget.localName === 'button') {

						var grid = w2ui[_this.intrfcOptions.name];
						var record = grid.get(e.recid);
						var state = void 0,
						    label = void 0;

						if (record.intrfcType === 'MCI') {
							state = 'main.manageMciInterface';
							label = _this.text.mciInterface;
						} else if (record.intrfcType === 'EAI_I') {
							state = 'main.manageEaiInterface';
							label = _this.text.eaiInterface;
						} else if (record.intrfcType === 'FEP') {
							state = 'main.manageFepInterface';
							label = _this.text.fepInterface;
						}

						_this.utilService.openTab(_this.$scope, {
							state: state,
							label: label
						}, {
							data: {
								intrfcId: record.intrfcId,
								msgLayoutId: record.msgLayoutId
							}
						});
					}
				},
				onDblClick: function onDblClick(e) {
					var grid = w2ui[_this.intrfcOptions.name];
					var record = grid.get(e.recid);
					var state = void 0,
					    label = void 0;

					if (record.intrfcType === 'MCI') {
						state = 'main.manageMciInterface';
						label = _this.text.mciInterface;
					} else if (record.intrfcType === 'EAI_I') {
						state = 'main.manageEaiInterface';
						label = _this.text.eaiInterface;
					} else if (record.intrfcType === 'FEP') {
						state = 'main.manageFepInterface';
						label = _this.text.fepInterface;
					}

					_this.utilService.openTab(_this.$scope, {
						state: state,
						label: label
					}, {
						data: {
							intrfcId: record.intrfcId,
							msgLayoutId: record.msgLayoutId
						}
					});
				}
			};
		}
	}, {
		key: 'initGetEffectsList',
		value: function initGetEffectsList() {
			var data = this.data;

			if (_.isEmpty(data)) {
				this.getEffectList = this.getMetaEffectedInterfaceList;
			} else {
				if (this.data.metaEngNm) this.getEffectList = this.getMetaEffectedInterfaceList;else if (data.msgLayoutId) this.getEffectList = this.getMsglayoutEffectedInterfaceList;else this.getEffectList = this.getMetaEffectedInterfaceList;
			}

			this.getEffectList();
		}
	}, {
		key: 'getMetaEffectedInterfaceList',
		value: function getMetaEffectedInterfaceList() {
			var _this2 = this;

			var url = '/metas/effects?metaEngNm=' + this.data.metaEngNm;

			this.httpService.get(url, this.searchParam).then(function (data) {
				_this2.intrfcOptions.records = data;
			});
		}
	}, {
		key: 'getMsglayoutEffectedInterfaceList',
		value: function getMsglayoutEffectedInterfaceList() {
			var _this3 = this;

			var encodedId = encodeURIComponent(this.data.msgLayoutId);
			var url = '/msglayouts/' + encodedId + '/effects?';

			this.httpService.get(url, this.searchParam).then(function (data) {
				_this3.intrfcOptions.records = data;
			});
		}
	}, {
		key: 'resetSearch',
		value: function resetSearch() {
			this.searchParam = {};
			this.getEffectList(true);
		}
	}, {
		key: 'search',
		value: function search() {
			this.getEffectList(true);
		}
	}, {
		key: 'blur',
		value: function blur($event) {
			$event.target.blur();
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
					this.$uibModalInstance.close(grid.get(selection[0]));
				}
			} else {
				this.$uibModalInstance.dismiss();
			}

			setTimeout(function () {
				return grid.destroy();
			});
		}
	}]);
	return SCR1802Controller;
}();

(0, _angular.module)(_app2.default.name).controller('SCR1802Controller', SCR1802Controller);

/***/ })

},[41]);
//# sourceMappingURL=SCR1802.controller.js.map