webpackJsonp(["app\\views\\wrap\\manageUser\\SCR0101.controller"],{

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

/***/ 59:
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__("91f298f38127034b7e54");


/***/ }),

/***/ "91f298f38127034b7e54":
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

var SCR0101Controller = function () {
	function SCR0101Controller($scope, $state, $timeout, httpService, utilService, gridService, popupService, codeService, userService, codes) {
		var _this = this;

		(0, _classCallCheck3.default)(this, SCR0101Controller);

		this.$scope = $scope;
		this.$state = $state;
		this.$timeout = $timeout;
		this.httpService = httpService;
		this.utilService = utilService;
		this.gridService = gridService;
		this.codeService = codeService;
		this.popupService = popupService;
		this.userService = userService;
		this.codes = codes;
		this.user = this.userService.getUser();

		this.initText();
		this.initSelect();
		this.resetSearch();
		this.resetDetail();

		this.initGrid();
		this.user = userService.getUser();

		this.$scope.$on('gridRendered', function () {
			_this.initPrevData();
		});
	}

	(0, _createClass3.default)(SCR0101Controller, [{
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

					if (prevScope.selectedUser && (0, _keys2.default)(prevScope.selectedUser).length > 0) {
						// 상세
						this.selectedUser = prevScope.selectedUser;
						this._selectedUser = prevScope._selectedUser;
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
			this.text = $.extend({}, bxMsg.getMessages('common'), bxMsg.getMessages('manageUser'));
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

			this.gridHeight = this.gridService.getGridHeight(this.select.pageSize);
			this.options = {
				limit: this.select.pageSize,
				pageSize: this.select.pageSize,
				autoComplete: false,
				recordsCount: 0,
				recid: 'userId',
				columns: [{
					caption: 'No', size: '80px',
					render: function render(data, index) {
						var pageNumber = _this3.pageNumber || 1;
						return (pageNumber - 1) * _this3.options.limit + index + 1;
					}
				}, { field: 'userId', caption: this.text.userId, size: '100px', sortable: true }, { field: 'userNm', caption: this.text.userNm, sortable: true, attr: 'align=left' }, { field: 'email', caption: this.text.email, sortable: true, attr: 'align=left' }, { field: 'dutyNm', caption: this.text.dutyNm, sortable: true }, { field: 'deptNm', caption: this.text.deptNm, sortable: true }, { field: 'roleId', caption: this.text.roleId, ortable: true }, {
					caption: bxMsg('common.edit'), size: '80px',
					render: function render(data) {
						var html = '';

						if (_this3.user.roleId === 'Administrator') {
							if (_this3.user.perm.update) {
								html += '<button type="button" class="bw-btn bxd bxd-edit2" data-action="edit"></button>';
							}

							if (_this3.user.perm.delete) {
								html += '<button type="button" class="bw-btn bxd bxd-trash" data-action="delete"></button>';
							}
						} else {
							if (_this3.user.perm.update && data.userId === _this3.user.userId) {
								html += '<button type="button" class="bw-btn bxd bxd-edit2" data-action="edit"></button>';
							}
						}

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
								return _this3.deleteGridData(recId);
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

						if (!_.isEmpty(_this3._selectedUser)) {
							return;
						}
					}

					_this3.getUser(editData.userId);
				}
			};
		}
	}, {
		key: 'getGridData',
		value: function getGridData() {
			var _this4 = this;

			var goToFirst = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : false;

			var _getPageInfo = this.getPageInfo(),
			    pageNumber = _getPageInfo.pageNumber,
			    pageSize = _getPageInfo.pageSize;

			var url = '/users?pageNumber=' + (goToFirst ? 1 : pageNumber) + '&pageSize=' + pageSize;

			this.httpService.get(url, this.searchParam).then(function (data) {
				var records = data.userOutList,
				    recordsCount = data.totalCnt;


				_this4.options.records = records;
				_this4.options.recordsCount = recordsCount;

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
		key: 'getUser',
		value: function getUser(id) {
			var _this5 = this;

			var utilService = this.utilService;

			this.httpService.get('/users/' + id).then(function (data) {
				_this5.selectedUser = utilService.clone(data);

				_this5.selectedUser.userPwdConfirm = _this5.selectedUser.userPwd;
				_this5._selectedUser = utilService.clone(_this5.selectedUser);
			});

			var $editAble = $('#none-edit, #none-edit2');

			for (var i = 0; i < $editAble.length; i++) {
				$editAble[i].style.backgroundColor = "#e2e2e2";
			}
		}
	}, {
		key: 'deleteGridData',
		value: function deleteGridData() {
			var _this6 = this;

			var id = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : '';

			var httpService = this.httpService;

			return httpService.delete('/users/' + id).then(function (data) {
				if (data.isError) return;

				_this6.resetDetail();
				_this6.getGridData();
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
		key: 'resetSearch',
		value: function resetSearch() {
			this.searchParam = {};

			if (this.user.roleId !== 'Administrator') {
				this.searchParam.userId = this.user.userId;
			}
		}
	}, {
		key: 'save',
		value: function save() {
			var _this7 = this;

			var httpService = this.httpService;
			var data = this.selectedUser;
			var isCreateUser = this._isCreateUser();

			delete data.recid;

			if (!this._checkValid(data)) return;

			var q = isCreateUser ? httpService.post('/users', data) : httpService.put('/users', data);

			q.then(function (res) {
				if (res.isError) {
					_this7.popupService.detailAlert(_this7.$scope, res.data.message, res.data.stackTrace);
					return;
				}

				_this7.getGridData();
				_this7.openAlert(bxMsg('common.saved'));
				_this7.offEditMode();

				_this7._selectedUser = _this7.selectedUser;
			});
		}
	}, {
		key: '_isCreateUser',
		value: function _isCreateUser() {
			return _.isEmpty(this._selectedUser);
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
			this._onEdit(true);
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
			this.selectedUser = {};
			this._selectedUser = {};
			this.offEditMode();

			var $editAble = $('#none-edit, #none-edit2');

			for (var i = 0; i < $editAble.length; i++) {
				$editAble[i].style.backgroundColor = "";
			}
		}
	}, {
		key: 'onEditMode',
		value: function onEditMode() {
			if (_.isEmpty(this.selectedUser)) return;
			this._onEdit();
		}
	}, {
		key: '_onEdit',
		value: function _onEdit() {
			var isCreate = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : false;

			var $forms = isCreate ? $('#searchWrap').find('input:not(.none-edit),textarea,select, div') : $('#searchWrap').find('div:not(.asterisk) > input,textarea');
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
		key: 'openRoleId',
		value: function openRoleId() {
			var _this8 = this;

			if (!this.isEdit) return;

			this.popupService.openModal('SCR0202', {
				text: this.text,
				codes: this.codes
			}).then(function (role) {
				return _this8.setRole(role);
			}).catch(function () {});
		}
	}, {
		key: 'setRole',
		value: function setRole(role) {
			this.selectedUser.roleId = role.roleId;
		}
	}, {
		key: 'openAlert',
		value: function openAlert(alertBody) {
			this.popupService.simpleAlert(this.$scope, alertBody);
		}
	}, {
		key: '_checkValid',
		value: function _checkValid(data) {
			if (_.isEmpty(data.userId)) {
				this.openAlert(bxMsg('manageUser.emptyUserId'));
				return false;
			} else if (_.isEmpty(data.userNm)) {
				this.openAlert(bxMsg('manageUser.emptyUserNm'));
				return false;
			} else if (_.isEmpty(data.roleId)) {
				this.openAlert(bxMsg('manageUser.emptyRoleId'));
				return false;
			} else if (_.isEmpty(data.userPwd)) {
				this.openAlert(bxMsg('manageUser.emptyUserPwd'));
				return false;
			} else if (data.userPwd !== data.userPwdConfirm) {
				this.openAlert(bxMsg('manageUser.notMatchPwd'));
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
	return SCR0101Controller;
}();

(0, _angular.module)(_app2.default.name).controller('SCR0101Controller', SCR0101Controller);

/***/ })

},[59]);
//# sourceMappingURL=SCR0101.controller.js.map