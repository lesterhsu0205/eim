webpackJsonp(["app\\views\\wrap\\manageCommSystem\\SCR1301.controller"],{

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

/***/ 47:
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__("66d80308c03360eaa964");


/***/ }),

/***/ "66d80308c03360eaa964":
/***/ (function(module, exports, __webpack_require__) {

"use strict";


var _keys = __webpack_require__("a7da3c296e58f6811fbd");

var _keys2 = _interopRequireDefault(_keys);

var _getIterator2 = __webpack_require__("e3915674c5aaafa0b6ee");

var _getIterator3 = _interopRequireDefault(_getIterator2);

var _classCallCheck2 = __webpack_require__("49c81d67dea0f8060449");

var _classCallCheck3 = _interopRequireDefault(_classCallCheck2);

var _createClass2 = __webpack_require__("5115238a033983f8f227");

var _createClass3 = _interopRequireDefault(_createClass2);

var _angular = __webpack_require__("909592b0f5247409d892");

var _app = __webpack_require__("085c418bf06f41809dc1");

var _app2 = _interopRequireDefault(_app);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var SCR1301Controller = function () {
	function SCR1301Controller($scope, $state, $timeout, httpService, gridService, popupService, utilService, codeService, userService, codes) {
		var _this = this;

		(0, _classCallCheck3.default)(this, SCR1301Controller);

		this.$scope = $scope;
		this.$state = $state;
		this.$timeout = $timeout;
		this.httpService = httpService;
		this.utilService = utilService;
		this.codeService = codeService;
		this.gridService = gridService;
		this.popupService = popupService;
		this.userService = userService;
		this.codes = utilService.clone(codes);
		this.user = this.userService.getUser();

		this.menuList = this.userService.getUserMenu();
		this.menuId = this.codeService.getMenubyState(this.$state.current.name);
		this.permInsert = false, this.permUpdate = false, this.permDelete = false;

		var _iteratorNormalCompletion = true;
		var _didIteratorError = false;
		var _iteratorError = undefined;

		try {
			for (var _iterator = (0, _getIterator3.default)(this.menuList), _step; !(_iteratorNormalCompletion = (_step = _iterator.next()).done); _iteratorNormalCompletion = true) {
				var item = _step.value;

				if (item.id == this.menuId) {
					if (item.permId != null) {
						if (item.permId.indexOf('insert') != -1) this.permInsert = true;
						if (item.permId.indexOf('update') != -1) this.permUpdate = true;
						if (item.permId.indexOf('delete') != -1) this.permDelete = true;
						break;
					}
				}
			}
		} catch (err) {
			_didIteratorError = true;
			_iteratorError = err;
		} finally {
			try {
				if (!_iteratorNormalCompletion && _iterator.return) {
					_iterator.return();
				}
			} finally {
				if (_didIteratorError) {
					throw _iteratorError;
				}
			}
		}

		this.initText();
		this.initSelect();
		this.resetSearch(true);
		this.resetDetail();
		this.initGrid();

		this.$scope.$on('gridRendered', function () {
			_this.initPrevData();
		});
	}

	(0, _createClass3.default)(SCR1301Controller, [{
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

					// 그리드 pageSize
					this.select.pageSize = prevScope.select.pageSize;
					this.options.limit = prevScope.select.pageSize;
					this.gridHeight = prevScope.gridHeight;

					// 그리드
					this.options.records = prevScope.options.records;
					this.options.recordsCount = prevScope.options.recordsCount;

					this.$timeout(function () {
						_this2.pageNumber = prevScope.pageNumber;
						_this2.$scope.$broadcast('resetPage', _this2.pageNumber);
					});

					if (prevScope.selectedSrsyss && (0, _keys2.default)(prevScope.selectedSrsyss).length > 0) {
						// 상세
						this.selectedSrsyss = prevScope.selectedSrsyss;
						this._selectedSrsyss = prevScope._selectedSrsyss;
					}

					// 수정모드
					if (prevScope.isEdit) {
						this.onEditMode();
					} else {
						this.offEditMode();
					}
				}
			} else {
				this.getSrsyssList();
			}

			this.$scope.$on('$destroy', function () {
				_this2.utilService.setParams(currentStateName, { scope: _this2.$scope });
			});
		}
	}, {
		key: 'initText',
		value: function initText() {
			this.text = $.extend({}, bxMsg.getMessages('common'), bxMsg.getMessages('manageCommSystem'));
		}
	}, {
		key: 'initSelect',
		value: function initSelect() {
			this.select = this.gridService.getSelect(this.codes['GRID_PAGE_SIZE'][1].cdVal);
		}
	}, {
		key: 'initGrid',
		value: function initGrid() {
			var _this3 = this;

			var columns = [{
				caption: 'No', size: '80px',
				render: function render(data, index) {
					var pageNumber = _this3.pageNumber || 1;
					return (pageNumber - 1) * _this3.options.limit + index + 1;
				}
			}, { field: 'sysCd', caption: this.text.sysCd, size: 1, sortable: true }, { field: 'sysNm', caption: this.text.sysNm, size: 1, sortable: true }, { field: 'sysCdDesc', caption: this.text.sysCdDesc, size: 3, attr: 'align=left' }, { field: 'noncoreYn', caption: this.text.noncoreYn, size: 1, sortable: true }];

			if (this.user.roleId === 'Administrator') {
				columns.push({
					caption: bxMsg('common.edit'), size: '80px',
					render: function render(data) {
						var html = '';

						if (_this3.permUpdate) {
							html += '<button type="button" class="bw-btn bxd bxd-edit2" data-action="edit"></button>';
						}

						if (_this3.permDelete) {
							html += '<button type="button" class="bw-btn bxd bxd-trash" data-action="delete"></button>';
						}

						return html;
					}
				});
			}

			this.gridHeight = this.gridService.getGridHeight(this.select.pageSize);
			this.options = {
				limit: this.select.pageSize,
				pageSize: this.select.pageSize,
				recordsCount: 0,
				recid: 'sysCd',
				columns: columns,
				onClick: function onClick(e) {
					var eTarget = e.originalEvent.target;
					var $eTarget = $(eTarget);

					if (eTarget.localName === 'button') {
						var action = $eTarget.attr('data-action');
						if (action === 'edit') {
							_this3._onEdit();
						} else {
							_this3.popupService.simpleConfirm(_this3.$scope, _this3.text.confirmTextDelete, function () {
								return _this3.deleteSrsyss(e.recid);
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

						if (!_.isEmpty(_this3._selectedSrsyss)) {
							return;
						}
					}

					_this3.getSrsyss(e.recid);
				}
			};
		}
	}, {
		key: 'getSrsyssList',
		value: function getSrsyssList() {
			var _this4 = this;

			var goToFirst = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : false;

			var _getPageInfo = this.getPageInfo(),
			    pageNumber = _getPageInfo.pageNumber,
			    pageSize = _getPageInfo.pageSize;

			var url = '/srsyss?pageNumber=' + (goToFirst ? 1 : pageNumber) + '&pageSize=' + pageSize;

			this.httpService.get(url, this.searchParam).then(function (data) {

				if (data.srsysbsOutList == null && data.totalCnt == 0) {
					_this4.options.records = "";
					_this4.options.recordsCount = 0;
				} else {
					var records = data.srsysbsOutList,
					    recordsCount = data.totalCnt;


					_this4.options.records = records;
					_this4.options.recordsCount = recordsCount;
				}

				if (!_.isEmpty(_this4.options.name)) {
					w2ui[_this4.options.name].selectNone();
				}

				if (goToFirst) {
					_this4.pageNumber = 1;
					_this4.$scope.$broadcast('resetPage', _this4.pageNumber);
				}
			});
		}
	}, {
		key: 'getSrsyss',
		value: function getSrsyss(sysCd) {
			var _this5 = this;

			var utilService = this.utilService;

			this.httpService.get('/srsyss/' + sysCd).then(function (data) {
				_this5.selectedSrsyss = utilService.clone(data);
				_this5._selectedSrsyss = utilService.clone(data);
			});

			var $editAble = $('#none-edit');

			for (var i = 0; i < $editAble.length; i++) {
				$editAble[i].style.backgroundColor = "#e2e2e2";
			}
		}
	}, {
		key: 'deleteSrsyss',
		value: function deleteSrsyss() {
			var _this6 = this;

			var sysCd = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : '';

			this.httpService.delete('/srsyss/' + sysCd).then(function (data) {
				if (data.isError) return;

				_this6.resetDetail();
				_this6.getSrsyssList();
			});
		}
	}, {
		key: 'search',
		value: function search() {
			this.getSrsyssList(true);
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
				this.getSrsyssList(true);
			}
		}
	}, {
		key: 'save',
		value: function save() {
			var _this7 = this;

			var httpService = this.httpService;
			var data = this.selectedSrsyss;
			var isCreateDeploySys = this._isCreateSrsyss();

			delete data.recid;

			if (!this._checkValid(data)) return;

			var q = isCreateDeploySys ? httpService.post('/srsyss', data) : httpService.put('/srsyss', data);

			q.then(function (res) {
				if (res.isError) {
					_this7.popupService.detailAlert(_this7.$scope, res.data.message, res.data.stackTrace);
					return;
				}

				_this7.getSrsyssList();
				_this7.offEditMode();
				_this7.openAlert(bxMsg('common.saved'));

				_this7._selectedSrsyss = _this7.selectedSrsyss;
			});
		}
	}, {
		key: '_isCreateSrsyss',
		value: function _isCreateSrsyss() {
			return _.isEmpty(this._selectedSrsyss);
		}
	}, {
		key: 'cancel',
		value: function cancel() {
			this.resetDetail();
		}
	}, {
		key: 'change',
		value: function change() {
			this.options.limit = this.select.pageSize;
			this.gridHeight = this.gridService.getGridHeight(this.select.pageSize);
			this.pageBtnClick(1);
			this.options.name && w2ui[this.options.name].focus();
		}
	}, {
		key: 'add',
		value: function add() {
			if (!_.isEmpty(this.options.name)) {
				w2ui[this.options.name].selectNone();
			}

			this.resetDetail();
			this._onEdit();
		}
	}, {
		key: 'pageBtnClick',
		value: function pageBtnClick(num) {
			this.pageNumber = num;
			this.getSrsyssList(num === 1);
		}
	}, {
		key: 'getPageInfo',
		value: function getPageInfo() {
			return {
				pageNumber: this.pageNumber || 1,
				pageSize: this.select.pageSize
			};
		}
	}, {
		key: 'resetDetail',
		value: function resetDetail() {
			this.selectedSrsyss = {};
			this._selectedSrsyss = {};
			this.offEditMode();

			var $editAble = $('#none-edit');

			for (var i = 0; i < $editAble.length; i++) {
				$editAble[i].style.backgroundColor = "";
			}
		}
	}, {
		key: 'onEditMode',
		value: function onEditMode() {
			if (_.isEmpty(this.selectedSrsyss)) return;
			this._onEdit();
		}
	}, {
		key: '_onEdit',
		value: function _onEdit() {
			var $forms = this._isCreateSrsyss() ? $('#searchWrap').find('input,textarea,select') : $('#searchWrap').find('div:not(.asterisk) > input,textarea');
			var $editAble = $('#searchWrap input.required,select.required');

			$forms.attr('disabled', false);
			$editAble.attr('disabled', false);

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
			if (_.isEmpty(data.sysCd)) {
				this.openAlert(bxMsg('commSystem.emptySysCd'));
				return false;
			} else if (_.isEmpty(data.sysNm)) {
				this.openAlert(bxMsg('commSystem.emptySysNm'));
				return false;
			}
			return true;
		}
	}, {
		key: 'refreshGrid',
		value: function refreshGrid() {
			var grid = w2ui[this.options.name];
			grid.refresh();
		}
	}]);
	return SCR1301Controller;
}();

(0, _angular.module)(_app2.default.name).controller('SCR1301Controller', SCR1301Controller);

/***/ })

},[47]);
//# sourceMappingURL=SCR1301.controller.js.map