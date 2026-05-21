webpackJsonp(["app\\views\\wrap\\manageRole\\SCR0201.controller"],{

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

/***/ 58:
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__("ad21a786161560657e04");


/***/ }),

/***/ "ad21a786161560657e04":
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

var SCR0201Controller = function () {
	function SCR0201Controller($scope, $state, $timeout, httpService, utilService, codeService, gridService, popupService, userService, codes) {
		var _this = this;

		(0, _classCallCheck3.default)(this, SCR0201Controller);

		this.$scope = $scope;
		this.$state = $state;
		this.$timeout = $timeout;
		this.codes = codes;
		this.httpService = httpService;
		this.utilService = utilService;
		this.codeService = codeService;
		this.gridService = gridService;
		this.popupService = popupService;
		this.userService = userService;
		this.user = this.userService.getUser();
		this.roleId = null;
		this.menuId = null;
		this.isMenuParent = false;
		this.initText();
		this.initSelect();
		this.initGrid();

		var count = 0;
		this.$scope.$on('gridRendered', function () {
			count++;
			count === 3 && _this.initPrevData();
		});
	}

	(0, _createClass3.default)(SCR0201Controller, [{
		key: 'getMenuName',
		value: function getMenuName(id) {
			var name = this.text.id;

			/*
   		for (var i = 0; this.text.length; i++) {
   			if(this.text[i] == id) {
   				name = this.text[i];
   				break;
   			}
   		}
   	*/
			return this.text[id];
		}
	}, {
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
					this.roleOptions.limit = prevScope.select.pageSize;
					this.gridHeight = prevScope.gridHeight;

					// 그리드
					this.roleOptions.records = prevScope.roleOptions.records;
					this.roleOptions.recordsCount = prevScope.roleOptions.recordsCount;

					this.$timeout(function () {
						_this2.pageNumber = prevScope.pageNumber;
						_this2.$scope.$broadcast('resetPage', _this2.pageNumber);
					});

					if (prevScope.selectedRole && (0, _keys2.default)(prevScope.selectedRole).length > 0) {
						// 상세
						this.selectedRole = prevScope.selectedRole;
						this._selectedRole = prevScope._selectedRole;
					}

					if (prevScope.menuOptions) {
						this.menuOptions.records = prevScope.menuOptions.records;
						this.menuOptions.recordsCount = prevScope.menuOptions.recordsCount;
					}

					if (prevScope.permOptions) {
						this.permOptions.records = prevScope.permOptions.records;
						this.permOptions.recordsCount = prevScope.permOptions.recordsCount;
					}

					// 수정모드
					if (prevScope.isEdit) {
						this.onEditMode();
					} else {
						this.offEditMode();
					}
				}
			} else {
				this.getGridData();
			}

			this.$scope.$on('$destroy', function () {
				_this2.utilService.setParams(currentStateName, { scope: _this2.$scope });
			});
		}
	}, {
		key: 'initText',
		value: function initText() {
			this.text = $.extend({}, bxMsg.getMessages('common'), bxMsg.getMessages('manageRole'), bxMsg.getMessages('menu'));
		}
	}, {
		key: 'initSelect',
		value: function initSelect() {
			this.select = { pageSize: 5 };
		}
	}, {
		key: 'initGrid',
		value: function initGrid() {
			var _this3 = this;

			this.roleOptions = {
				limit: this.select.pageSize,
				pageSize: this.select.pageSize,
				autoComplete: false,
				recordsCount: 0,
				recid: 'roleId',
				columns: [{
					caption: 'No', size: '80px',
					render: function render(data, index) {
						var pageNumber = _this3.pageNumber || 1;
						return (pageNumber - 1) * _this3.roleOptions.limit + index + 1;
					}
				}, { field: 'roleId', caption: this.text.roleId }, { field: 'roleNm', caption: this.text.roleNm }, {
					caption: bxMsg('common.edit'), size: '80px',
					render: function render(data) {
						var html = '';
						html += '<button type="button" class="bw-btn bxd bxd-edit2" data-action="edit"></button>';
						html += '<button type="button" class="bw-btn bxd bxd-trash" data-action="delete"></button>';

						return html;
					}
				}],
				onClick: function onClick(e) {
					var gridName = e.target;
					var recId = e.recid;
					var originalEvent = e.originalEvent;

					var eTarget = originalEvent.target;
					var $eTarget = $(eTarget);

					var grid = w2ui[gridName];
					var editData = grid.get(recId);

					if (eTarget.localName === 'button') {
						var action = $eTarget.attr('data-action');
						var isEdit = action === 'edit';
						if (isEdit) {
							_this3._onEdit();
						} else {
							_this3.popupService.simpleConfirm(_this3.$scope, _this3.text.confirmTextDelete, function () {
								return _this3.deleteRole(recId);
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

						if (!_.isEmpty(_this3._selectedRole)) {
							return;
						}
					}

					_this3.getRole(editData.roleId);
					_this3.getMenuList(editData.roleId);
					_this3.permOptions.records = [];
					_this3.roleId = editData.roleId;
					//					this.getPermList(editData.roleId);
				}
			};

			this.menuOptions = {
				limit: 9999,
				pageSize: 9999,
				recordsCount: 0,
				recid: 'id',
				columns: [{ field: 'id', caption: this.text.menuId, sortable: true,
					render: function render(data) {
						var prtId = data.parentId;

						if (_this3.utilService.isEmpty(prtId)) {
							return data.id;
						} else {
							return "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + data.id;
						}
					}

				}, { field: name, caption: this.text.menuNm, sortable: true,
					render: function render(data) {
						var menuid = data.id;
						return _this3.getMenuName(menuid);
					}
				}, {
					caption: bxMsg('common.edit'), size: '80px',
					render: function render(data) {
						var html = '';
						html += '<button type="button" class="bw-btn bxd bxd-trash" data-action="delete"></button>';

						return html;
					}
				}],
				onClick: function onClick(e) {
					// prevent deselect
					var selection = w2ui[e.target].getSelection();
					if (selection.length === 1 && selection[0] === e.recid) {
						e.preventDefault();
					}

					var originalEvent = e.originalEvent;
					var grid = w2ui[_this3.menuOptions.name];
					var eTarget = e.originalEvent.target;
					var recid = e.recid;
					var editData = grid.get(e.recid);
					_this3.menuId = editData.id;

					if (!_.isEmpty(editData.parentId)) {
						_this3.isMenuParent = false;
					} else {
						_this3.isMenuParent = true;
					}

					if (eTarget.localName === 'button') {
						_this3.popupService.simpleConfirm(_this3.$scope, _this3.text.confirmTextDelete, function () {
							return _this3.deleteMenu(_this3.selectedRole.roleId, e.recid);
						});
					}

					_this3.getPermList(_this3.roleId, _this3.menuId);
				}
			};

			this.permOptions = {
				limit: 9999,
				pageSize: 9999,
				recordsCount: 0,
				recid: 'permId',
				columns: [{
					caption: 'No', size: '80px',
					render: function render(data, index) {
						var pageNumber = _this3.pageNumber || 1;
						return (pageNumber - 1) * _this3.permOptions.limit + index + 1;
					}
				}, { field: 'permId', caption: this.text.permId, sortable: true }, { field: 'permNm', caption: this.text.permNm, sortable: true }, {
					caption: bxMsg('common.edit'), size: '80px',
					render: function render(data) {
						var html = '';
						html += '<button type="button" class="bw-btn bxd bxd-trash" data-action="delete"></button>';

						return html;
					}
				}],
				onClick: function onClick(e) {
					// prevent deselect
					var selection = w2ui[e.target].getSelection();
					if (selection.length === 1 && selection[0] === e.recid) {
						e.preventDefault();
					}

					var originalEvent = e.originalEvent;
					var eTarget = originalEvent.target;

					if (eTarget.localName === 'button') {
						_this3.popupService.simpleConfirm(_this3.$scope, _this3.text.confirmTextDelete, function () {
							return _this3.deletePerm(_this3.selectedRole.roleId, e.recid);
						});
					}
				}
			};
		}
	}, {
		key: 'getGridData',
		value: function getGridData() {
			var _this4 = this;

			var goToFirst = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : false;

			var httpService = this.httpService;

			var _getPageInfo = this.getPageInfo(),
			    pageNumber = _getPageInfo.pageNumber,
			    pageSize = _getPageInfo.pageSize;

			var url = '/roles?pageNumber=' + (goToFirst ? 1 : pageNumber) + '&pageSize=' + pageSize;

			httpService.get(url).then(function (data) {
				var records = data.roleDtoList,
				    recordsCount = data.totalCnt;


				_this4.roleOptions.records = records;
				_this4.roleOptions.recordsCount = recordsCount;

				if (!_.isEmpty(_this4.roleOptions.name)) {
					w2ui[_this4.roleOptions.name].selectNone();
				}

				if (goToFirst) {
					_this4.pageNumber = 1;
					_this4.$scope.$broadcast('resetPage', _this4.pageNumber);
				}
			});
		}
	}, {
		key: 'getRole',
		value: function getRole(id) {
			var _this5 = this;

			var utilService = this.utilService;

			this.httpService.get('/roles/' + id).then(function (data) {
				_this5.selectedRole = utilService.clone(data);
				_this5._selectedRole = utilService.clone(data);
				_this5.offEditMode();
			});

			var $editAble = $('#none-edit');

			for (var i = 0; i < $editAble.length; i++) {
				$editAble[i].style.backgroundColor = "#e2e2e2";
			}
		}
	}, {
		key: 'getMenuList',
		value: function getMenuList(roleId) {
			var _this6 = this;

			this.httpService.get('/roles/' + roleId + '/menus').then(function (data) {
				_this6.menuOptions.records = _this6.gridService.convertDataToTreeData(data, "parentId");
			});
		}
	}, {
		key: 'getPermList',
		value: function getPermList(roleId, menuId) {
			var _this7 = this;

			this.httpService.get('/roles/' + roleId + '/perms?menuId=' + menuId).then(function (data) {
				if (!_.isEmpty(data[0])) {
					_this7.permOptions.records = data;
				} else {
					_this7.permOptions.records = [];
				}
			});
		}
	}, {
		key: 'deleteRole',
		value: function deleteRole() {
			var _this8 = this;

			var id = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : '';

			this.httpService.delete('/roles/' + id).then(function (data) {
				_this8.resetDetail();
				_this8.getGridData();
			});
		}
	}, {
		key: 'deleteMenu',
		value: function deleteMenu() {
			var _this9 = this;

			var roleId = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : '';
			var menuId = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : '';

			this.httpService.delete('/roles/' + roleId + '/menus/' + menuId).then(function (data) {
				if (data.isError) return;
				_this9.getMenuList(roleId);
			});
		}
	}, {
		key: 'deletePerm',
		value: function deletePerm() {
			var _this10 = this;

			var roleId = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : '';
			var permId = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : '';

			this.httpService.delete('/roles/' + roleId + '/perms/' + permId + '?menuId=' + this.menuId).then(function (data) {
				if (data.isError) return;
				_this10.getPermList(roleId, _this10.menuId);
			});
		}
	}, {
		key: 'search',
		value: function search() {
			this.getGridData(true);
		}
	}, {
		key: 'blur',
		value: function blur($event) {
			$event.target.blur();
		}
	}, {
		key: 'save',
		value: function save() {
			var _this11 = this;

			var httpService = this.httpService;
			var isCreateRole = this._isCreateRole();
			var data = this.selectedRole;

			delete data.recid;

			if (!this._checkValid(data)) return;

			var q = isCreateRole ? httpService.post('/roles', data) : httpService.put('/roles', data);

			q.then(function (res) {
				if (res.isError) {
					_this11.popupService.detailAlert(_this11.$scope, res.data.message, res.data.stackTrace);
					return;
				}

				_this11.getGridData();

				if (isCreateRole) {
					_this11.getRole(data.roleId);
				} else {
					_this11.offEditMode();
				}

				_this11.openAlert(bxMsg('common.saved'));

				_this11._selectedRole = _this11.selectedRole;
			});
		}
	}, {
		key: '_isCreateRole',
		value: function _isCreateRole() {
			return _.isEmpty(this._selectedRole);
		}
	}, {
		key: 'cancel',
		value: function cancel() {
			this.resetDetail();
		}
	}, {
		key: 'change',
		value: function change() {
			this.roleOptions.limit = this.select.pageSize;
			this.pageBtnClick(1);
		}
	}, {
		key: 'add',
		value: function add() {
			if (!_.isEmpty(this.roleOptions.name)) {
				w2ui[this.roleOptions.name].selectNone();
			}

			this.resetDetail();
			this._onEdit();
		}
	}, {
		key: 'onClickAddMenu',
		value: function onClickAddMenu() {
			var _this12 = this;

			if (this.utilService.isEmpty(this.selectedRole)) {
				this.openAlert(bxMsg('manageRole.emptyRoleId2'));
				return;
			}

			this.popupService.openModal('SCR0402', {
				text: this.text,
				roleId: this.selectedRole.roleId
			}).then(function (records) {
				return _this12.updateMenus(records);
			}).catch(function () {});
		}
	}, {
		key: 'onClickAddPerm',
		value: function onClickAddPerm() {
			var _this13 = this;

			if (this.utilService.isEmpty(this.selectedRole)) {
				this.openAlert(bxMsg('manageRole.emptyRoleId2'));
				return;
			}

			if (_.isEmpty(this.menuId)) {
				this.openAlert(bxMsg('manageRole.emptyMenuId'));
				return;
			}

			this.popupService.openModal('SCR0302', {
				text: this.text,
				roleId: this.selectedRole.roleId,
				menuId: this.menuId
			}).then(function (records) {
				return _this13.updatePerms(records);
			}).catch(function () {});
		}
	}, {
		key: 'updateMenus',
		value: function updateMenus(records) {
			var _this14 = this;

			var roleId = this.selectedRole.roleId;

			this.httpService.put('/roles/' + roleId + '/menus', records).then(function (res) {
				if (res.isError) {
					_this14.popupService.detailAlert(_this14.$scope, res.data.message, res.data.stackTrace);
					return;
				}

				_this14.getMenuList(roleId);
				_this14.openAlert(bxMsg('common.saved'));
			});
		}
	}, {
		key: 'updatePerms',
		value: function updatePerms(records) {
			var _this15 = this;

			var roleId = this.selectedRole.roleId;

			this.httpService.put('/roles/' + roleId + '/perms?menuId=' + this.menuId, records).then(function (res) {
				if (res.isError) {
					_this15.popupService.detailAlert(_this15.$scope, res.data.message, res.data.stackTrace);
					return;
				}

				_this15.getPermList(roleId, _this15.menuId);
				_this15.openAlert(bxMsg('common.saved'));
			});
		}
	}, {
		key: 'pageBtnClick',
		value: function pageBtnClick(num) {
			this.pageNumber = num;
			this.getGridData(num === 1);
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
			this.selectedRole = {};
			this._selectedRole = {};
			this.menuOptions.records = [];
			this.permOptions.records = [];
			this.offEditMode();

			var $editAble = $('#none-edit');
			for (var i = 0; i < $editAble.length; i++) {
				$editAble[i].style.backgroundColor = "";
			}
		}
	}, {
		key: 'onEditMode',
		value: function onEditMode() {
			if (_.isEmpty(this.selectedRole)) return;
			this._onEdit();
		}
	}, {
		key: '_onEdit',
		value: function _onEdit() {
			var $forms = this._isCreateRole() ? $('#searchWrap').find('input,textarea,select') : $('#searchWrap').find('div:not(.asterisk) > input,textarea');
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
			if (_.isEmpty(data.roleId)) {
				this.openAlert(bxMsg('manageRole.emptyRoleId'));
				return false;
			} else if (_.isEmpty(data.roleNm)) {
				this.openAlert(bxMsg('manageRole.emptyRoleNm'));
				return false;
			}
			return true;
		}
	}]);
	return SCR0201Controller;
}();

(0, _angular.module)(_app2.default.name).controller('SCR0201Controller', SCR0201Controller);

/***/ })

},[58]);
//# sourceMappingURL=SCR0201.controller.js.map