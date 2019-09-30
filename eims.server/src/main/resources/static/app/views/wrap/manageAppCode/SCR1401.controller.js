webpackJsonp(["app\\views\\wrap\\manageAppCode\\SCR1401.controller"],{

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

/***/ 46:
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__("89eb159ad1c3a86593dd");


/***/ }),

/***/ "89eb159ad1c3a86593dd":
/***/ (function(module, exports, __webpack_require__) {

"use strict";


var _keys = __webpack_require__("a7da3c296e58f6811fbd");

var _keys2 = _interopRequireDefault(_keys);

var _classCallCheck2 = __webpack_require__("49c81d67dea0f8060449");

var _classCallCheck3 = _interopRequireDefault(_classCallCheck2);

var _createClass2 = __webpack_require__("5115238a033983f8f227");

var _createClass3 = _interopRequireDefault(_createClass2);

var _angular = __webpack_require__("909592b0f5247409d892");

var _app = __webpack_require__("085c418bf06f41809dc1");

var _app2 = _interopRequireDefault(_app);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var SCR1401Controller = function () {
	function SCR1401Controller($scope, $state, $timeout, httpService, utilService, popupService, codeService, gridService, userService, codes) {
		var _this = this;

		(0, _classCallCheck3.default)(this, SCR1401Controller);

		this.$scope = $scope;
		this.$state = $state;
		this.$timeout = $timeout;
		this.codes = codes;
		this.httpService = httpService;
		this.utilService = utilService;
		this.codeService = codeService;
		this.gridService = gridService;
		this.userService = userService;
		this.popupService = popupService;
		this.user = this.userService.getUser();

		this.initText();
		this.resetSearch(true);
		this.resetDetail();

		this.initGrid();

		this.$scope.$on('gridRendered', function () {
			_this.initPrevData();
		});
	}

	(0, _createClass3.default)(SCR1401Controller, [{
		key: 'initPrevData',
		value: function initPrevData() {
			var _this2 = this;

			var currentStateName = this.$state.current.name;
			var param = this.utilService.getParams(currentStateName);

			if (!_.isEmpty(param)) {
				if (param.scope) {
					var prevScope = param.scope.vm;

					// 탐색
					this.searchParam = prevScope.searchParam;

					// 그리드
					this.options.records = prevScope.options.records;
					this.options.recordsCount = prevScope.options.recordsCount;

					this.$timeout(function () {
						_this2.pageNumber = prevScope.pageNumber;
						_this2.$scope.$broadcast('resetPage', _this2.pageNumber);
					});

					if (prevScope.selectedAppCd && (0, _keys2.default)(prevScope.selectedAppCd).length > 0) {
						// 상세
						this.selectedAppCd = prevScope.selectedAppCd;
						this._selectedAppCd = prevScope._selectedAppCd;
					}

					// 수정모드
					if (prevScope.isEdit) {
						this.onEditMode();
					} else {
						this.offEditMode();
					}
				}
			} else {
				this.getAppList();
			}

			this.$scope.$on('$destroy', function () {
				_this2.utilService.setParams(currentStateName, { scope: _this2.$scope });
			});
		}
	}, {
		key: 'initText',
		value: function initText() {
			this.text = $.extend({}, bxMsg.getMessages('common'), bxMsg.getMessages('manageApp'));
		}
	}, {
		key: 'initGrid',
		value: function initGrid() {
			var _this3 = this;

			var columns = [{
				field: 'lvCd', caption: this.text.lvCd, size: '100px',
				render: function render(data) {
					return 'L' + data.lvCd;
				}
			}, { field: 'appCd', caption: this.text.appCd, style: 'text-align : left',
				render: function render(data) {
					return data.appCd + "(" + data.appCdNm + ")";
				}
			}];

			if (this.user.roleId === 'Administrator') {
				columns.push({
					field: 'edit', caption: bxMsg('common.edit'), size: '80px',
					render: function render(data) {
						var html = '';

						if (_this3.user.perm.update) {
							html += '<button type="button" class="bw-btn bxd bxd-edit2" data-action="edit"></button>';
						}

						if (_this3.user.perm.delete) {
							html += '<button type="button" class="bw-btn bxd bxd-trash" data-action="delete"></button>';
						}

						return html;
					}
				});
			}

			this.options = {
				limit: 99999,
				pageSize: 99999,
				recordsCount: 0,
				recid: 'appUnqKey',
				columns: columns,
				onClick: function onClick(e) {
					var eTarget = e.originalEvent.target;
					var $eTarget = $(eTarget);
					var grid = w2ui[e.target];
					var editData = grid.get(e.recid);

					if (eTarget.localName === 'button') {
						var action = $eTarget.attr('data-action');

						if (action === 'edit') {
							_this3._onEdit();
						} else {
							_this3.popupService.simpleConfirm(_this3.$scope, _this3.text.confirmTextDelete, function () {
								return _this3.deleteApp(editData);
							});
							e.preventDefault();
							return;
						}
					} else {
						_this3.offEditMode();
					}

					_this3.$scope.$apply();

					// prevent deselect
					var selection = w2ui[e.target].getSelection();
					if (selection.length === 1 && selection[0] === e.recid) {
						e.preventDefault();

						if (!_.isEmpty(_this3._selectedAppCd)) {
							return;
						}
					}

					_this3.getApp(editData);
				}
			};
		}
	}, {
		key: 'getAppList',
		value: function getAppList() {
			var _this4 = this;

			var goToFirst = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : false;

			this.httpService.get('/apps').then(function (data) {
				_this4.options.recordsCount = data.totalCnt;
				_this4.options.records = data.appcdOutList;
			});
		}
	}, {
		key: 'getApp',
		value: function getApp(app) {
			var _this5 = this;

			var utilService = this.utilService;

			this.httpService.get('/apps/' + app.appCd + '?lvCd=' + app.lvCd).then(function (data) {
				_this5.selectedAppCd = utilService.clone(data);
				_this5._selectedAppCd = utilService.clone(data);
			});

			var $editAble = $('#none-edit');

			for (var i = 0; i < $editAble.length; i++) {
				$editAble[i].style.backgroundColor = "#e2e2e2";
			}
		}
	}, {
		key: 'deleteApp',
		value: function deleteApp(app) {
			var _this6 = this;

			this.httpService.delete('/apps/' + app.appCd + '?lvCd=' + app.lvCd).then(function (data) {
				if (data.isError) return;

				_this6.resetDetail();
				_this6.getAppList();
			});
		}
	}, {
		key: 'search',
		value: function search() {
			this.getAppList(true);
		}
	}, {
		key: 'blur',
		value: function blur($event) {
			$event.target.blur();
		}
	}, {
		key: 'resetSearch',
		value: function resetSearch(isConst) {
			if (isConst) {
				this.searchParam = {};
			} else {
				this.searchParam = {};
				this.getAppList(true);
			}
		}
	}, {
		key: 'save',
		value: function save() {
			var _this7 = this;

			var httpService = this.httpService;
			var data = this.selectedAppCd;
			var isCreateAppcd = this._isCreateAppcd();

			delete data.recid;

			if (!this._checkValid(data)) return;

			var q = isCreateAppcd ? httpService.post('/apps', data) : httpService.put('/apps', data);

			q.then(function (res) {
				if (res.isError) {
					_this7.popupService.detailAlert(_this7.$scope, res.data.message, res.data.stackTrace);
					return;
				}

				_this7.getAppList();
				_this7.offEditMode();
				_this7.openAlert(bxMsg('common.saved'));

				_this7._selectedAppCd = _this7.selectedAppCd;
			});
		}
	}, {
		key: '_isCreateAppcd',
		value: function _isCreateAppcd() {
			return _.isEmpty(this._selectedAppCd);
		}
	}, {
		key: 'cancel',
		value: function cancel() {
			this.resetDetail();
		}
	}, {
		key: 'add',
		value: function add() {
			if (!_.isEmpty(this.options.name)) {
				w2ui[this.options.name].selectNone();
			}

			this.resetDetail();
			this.selectedAppCd.lvCd = '1';
			this._onEdit();
		}
	}, {
		key: 'resetDetail',
		value: function resetDetail() {
			this.selectedAppCd = {};
			this._selectedAppCd = {};
			this.offEditMode();

			var $editAble = $('#none-edit');

			for (var i = 0; i < $editAble.length; i++) {
				$editAble[i].style.backgroundColor = "";
			}
		}
	}, {
		key: 'onEditMode',
		value: function onEditMode() {
			if (_.isEmpty(this.selectedAppCd)) return;
			this._onEdit();
		}
	}, {
		key: '_onEdit',
		value: function _onEdit() {
			var $forms = this._isCreateAppcd() ? $('#searchWrap').find('input,textarea') : $('#searchWrap').find('div:not(.asterisk) > input,textarea');
			var $editAble = $('#searchWrap input.required,select.required');

			if (this._isCreateAppcd()) {
				$forms.attr('disabled', false);
				$editAble.attr('disabled', false);
			} else {
				$editAble.attr('disabled', false);
			}

			this.isEdit = true;
		}
	}, {
		key: 'offEditMode',
		value: function offEditMode() {
			var $forms = $('#searchWrap input,textarea');
			$forms.attr('disabled', true);
			this.isEdit = false;
		}
	}, {
		key: 'openAlert',
		value: function openAlert(alertBody) {
			this.popupService.simpleAlert(this.$scope, alertBody);
		}
	}, {
		key: '_checkValid',
		value: function _checkValid(data) {
			if (_.isEmpty(data.appCd)) {
				this.openAlert(this.text.emptyAppCode);
				return false;
			} else if (_.isEmpty(data.appCdNm)) {
				this.openAlert(this.text.emptyAppCodeNm);
				return false;
			} else if (_.isEmpty(data.lvCd)) {
				this.openAlert(this.text.emptyLvCd);
				return false;
			}

			return true;
		}
	}]);
	return SCR1401Controller;
}();

(0, _angular.module)(_app2.default.name).controller('SCR1401Controller', SCR1401Controller);

/***/ })

},[46]);
//# sourceMappingURL=SCR1401.controller.js.map