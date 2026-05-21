webpackJsonp(["app\\views\\wrap\\manageDeploySystem\\SCR1201.controller"],{

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

/***/ 48:
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__("5c8a9c806b990d829d64");


/***/ }),

/***/ "5c8a9c806b990d829d64":
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

var SCR1201Controller = function () {
	function SCR1201Controller($scope, $state, $timeout, httpService, utilService, gridService, popupService, codeService, userService, codes) {
		var _this = this;

		(0, _classCallCheck3.default)(this, SCR1201Controller);

		this.$scope = $scope;
		this.$state = $state;
		this.$timeout = $timeout;
		this.codes = codes;
		this.httpService = httpService;
		this.utilService = utilService;
		this.gridService = gridService;
		this.popupService = popupService;
		this.codeService = codeService;
		this.userService = userService;
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

	(0, _createClass3.default)(SCR1201Controller, [{
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

					if (prevScope.selectedDeploySys && (0, _keys2.default)(prevScope.selectedDeploySys).length > 0) {
						// 상세
						this.selectedDeploySys = prevScope.selectedDeploySys;
						this._selectedDeploySys = prevScope._selectedDeploySys;
					}

					// 수정모드
					if (prevScope.isEdit) {
						this.onEditMode();
					} else {
						this.offEditMode();
					}
				}
			} else {
				this.getDeploySysList();
			}

			this.$scope.$on('$destroy', function () {
				_this2.utilService.setParams(currentStateName, { scope: _this2.$scope });
			});
		}
	}, {
		key: 'initText',
		value: function initText() {
			this.text = $.extend({}, bxMsg.getMessages('common'), bxMsg.getMessages('manageDeploySystem'));
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
			}, { field: 'deploySysCd', caption: this.text.deploySysCd, size: 1, sortable: true }, { field: 'deploySysNm', caption: this.text.deploySysNm, size: 2, sortable: true, attr: 'align=left' }, { field: 'deploySysUrl', caption: this.text.deploySysUrl, size: 3, sortable: true, attr: 'align=left' }, { field: 'deploySysDesc', caption: this.text.deploySysDesc, size: 3, attr: 'align=left' }];

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
				autoComplete: false,
				pageSize: 10,
				recordsCount: 0,
				recid: 'deploySysCd',
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
								return _this3.deleteDeploySys(e.recid);
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

						if (!_.isEmpty(_this3._selectedDeploySys)) {
							return;
						}
					}

					_this3.getDeploySys(e.recid);
				}
			};
		}
	}, {
		key: 'getDeploySysList',
		value: function getDeploySysList() {
			var _this4 = this;

			var goToFirst = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : false;

			var _getPageInfo = this.getPageInfo(),
			    pageNumber = _getPageInfo.pageNumber,
			    pageSize = _getPageInfo.pageSize;

			var url = '/depolysyss?pageNumber=' + (goToFirst ? 1 : pageNumber) + '&pageSize=' + pageSize;

			this.httpService.get(url, this.searchParam).then(function (data) {

				if (data.depolysysbsOutList == null && data.totalCnt == 0) {
					_this4.options.records = "";
					_this4.options.recordsCount = 0;
				} else {
					var records = data.depolysysbsOutList,
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
		key: 'getDeploySys',
		value: function getDeploySys(deploySysCd) {
			var _this5 = this;

			var utilService = this.utilService;

			this.httpService.get('/depolysyss/' + deploySysCd).then(function (data) {
				_this5.selectedDeploySys = utilService.clone(data);
				_this5._selectedDeploySys = utilService.clone(data);
			});

			var $editAble = $('#none-edit, #none-edit2');

			for (var i = 0; i < $editAble.length; i++) {
				$editAble[i].style.backgroundColor = "#e2e2e2";
			}
		}
	}, {
		key: 'deleteDeploySys',
		value: function deleteDeploySys() {
			var _this6 = this;

			var deploySysCd = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : '';

			this.httpService.delete('/depolysyss/' + deploySysCd).then(function (data) {
				if (data.isError) return;

				_this6.resetDetail();
				_this6.getDeploySysList();
			});
		}
	}, {
		key: 'search',
		value: function search() {
			this.getDeploySysList(true);
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
				this.getDeploySysList(true);
			}
		}
	}, {
		key: 'save',
		value: function save() {
			var _this7 = this;

			var httpService = this.httpService;
			var data = this.selectedDeploySys;
			var isCreateDeploySys = this._isCreateDeploySys();

			delete data.recid;

			if (!this._checkValid(data)) return;

			var q = isCreateDeploySys ? httpService.post('/depolysyss', data) : httpService.put('/depolysyss', data);

			q.then(function (res) {
				if (res.isError) {
					_this7.popupService.detailAlert(_this7.$scope, res.data.message, res.data.stackTrace);
					return;
				}

				_this7.getDeploySysList();
				_this7.openAlert(bxMsg('common.saved'));
				_this7.offEditMode();

				_this7._selectedDeploySys = _this7.selectedDeploySys;
			});
		}
	}, {
		key: '_isCreateDeploySys',
		value: function _isCreateDeploySys() {
			return _.isEmpty(this._selectedDeploySys);
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
			this.getDeploySysList(num === 1);
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
			this.selectedDeploySys = {};
			this._selectedDeploySys = {};
			this.offEditMode();

			var $editAble = $('#none-edit, #none-edit2');

			for (var i = 0; i < $editAble.length; i++) {
				$editAble[i].style.backgroundColor = "";
			}
		}
	}, {
		key: 'onEditMode',
		value: function onEditMode() {
			if (_.isEmpty(this.selectedDeploySys)) return;
			this._onEdit();
		}
	}, {
		key: '_onEdit',
		value: function _onEdit() {
			var $forms = this._isCreateDeploySys() ? $('#searchWrap').find('input,textarea,select') : $('#searchWrap').find('div:not(.asterisk) > input,textarea');
			var $editAble = $('#searchWrap input.required');

			$forms.attr('disabled', false);
			$editAble.attr('disabled', false);

			this.isEdit = true;
		}
	}, {
		key: 'offEditMode',
		value: function offEditMode() {
			var $forms = $('#searchWrap').find('input,textarea');
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
			if (_.isEmpty(data.deploySysCd)) {
				this.openAlert(this.text.emptyDeploySysCd);
				return false;
			} else if (_.isEmpty(data.deploySysNm)) {
				this.openAlert(this.text.emptyDeploySysNm);
				return false;
			} else if (_.isEmpty(data.deploySysGrpCd)) {
				this.openAlert(this.text.emptyDeploySysGrpCd);
				return false;
			} else if (_.isEmpty(data.deploySysUrl)) {
				this.openAlert(this.text.emptydeploySysUrl);
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
	return SCR1201Controller;
}();

(0, _angular.module)(_app2.default.name).controller('SCR1201Controller', SCR1201Controller);

/***/ })

},[48]);
//# sourceMappingURL=SCR1201.controller.js.map