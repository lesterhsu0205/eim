webpackJsonp(["app\\views\\wrap\\manageEaiInterface\\SCR0801.controller"],{

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

/***/ 49:
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__("d28f81e4fc1507bbab7e");


/***/ }),

/***/ "d28f81e4fc1507bbab7e":
/***/ (function(module, exports, __webpack_require__) {

"use strict";


var _isNan = __webpack_require__("626199283b8e61da823f");

var _isNan2 = _interopRequireDefault(_isNan);

var _toConsumableArray2 = __webpack_require__("483947055e3815811e75");

var _toConsumableArray3 = _interopRequireDefault(_toConsumableArray2);

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

var SCR0801Controller = function () {
	function SCR0801Controller($scope, $state, $compile, $timeout, popupService, httpService, utilService, gridService, codeService, metaService, userService, codes) {
		var _this = this;

		(0, _classCallCheck3.default)(this, SCR0801Controller);

		this.$scope = $scope;
		this.$state = $state;
		this.$compile = $compile;
		this.$timeout = $timeout;
		this.httpService = httpService;
		this.utilService = utilService;
		this.gridService = gridService;
		this.codeService = codeService;
		this.popupService = popupService;
		this.metaService = metaService;
		this.userService = userService;
		this.codes = utilService.clone(codes);
		this.user = this.userService.getUser();

		this.intrfcTypeCd = 'EAI_I';
		this.useRequestMsgScroll = true;
		this.useResponseMsgScroll = true;

		this.initZabara();
		this.initText();
		this.initSelect();
		this.initGrid();
		this.initGenerate();
		this.initScroll();

		var count = 0;
		this.$scope.$on('gridRendered', function () {
			count++;
			count === 12 && _this.initPrevData();
		});
	}

	(0, _createClass3.default)(SCR0801Controller, [{
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
					this.inferfaceMainGrid.limit = prevScope.select.pageSize;
					this.gridHeight = prevScope.gridHeight;

					// 그리드
					this.inferfaceMainGrid.records = prevScope.inferfaceMainGrid.records;
					this.inferfaceMainGrid.recordsCount = prevScope.inferfaceMainGrid.recordsCount;

					this.$timeout(function () {
						_this2.pageNumber = prevScope.pageNumber;
						_this2.$scope.$broadcast('resetPage', _this2.pageNumber);
					});

					if (prevScope.detail && (0, _keys2.default)(prevScope.detail).length > 0) {
						// 상세
						this.detail = prevScope.detail;

						prevScope.interfaceDetailGrid && (this.interfaceDetailGrid.records = prevScope.interfaceDetailGrid.records);
						prevScope.sndReqLayoutGrid && (this.sndReqLayoutGrid.records = prevScope.sndReqLayoutGrid.records);
						prevScope.sndResLayoutGrid && (this.sndResLayoutGrid.records = prevScope.sndResLayoutGrid.records);
						prevScope.rcvReqLayoutGrid && (this.rcvReqLayoutGrid.records = prevScope.rcvReqLayoutGrid.records);
						prevScope.rcvResLayoutGrid && (this.rcvResLayoutGrid.records = prevScope.rcvResLayoutGrid.records);
						prevScope.sndMsgMapSrcGrid && (this.sndMsgMapSrcGrid.records = prevScope.sndMsgMapSrcGrid.records);
						prevScope.sendMsgMapTrgGrid && (this.sendMsgMapTrgGrid.records = prevScope.sendMsgMapTrgGrid.records);
						prevScope.rcvMsgMapSrcGrid && (this.rcvMsgMapSrcGrid.records = prevScope.rcvMsgMapSrcGrid.records);
						prevScope.rcvMsgMapTrgGrid && (this.rcvMsgMapTrgGrid.records = prevScope.rcvMsgMapTrgGrid.records);
						prevScope.deployTargetSysGrid && (this.deployTargetSysGrid.records = prevScope.deployTargetSysGrid.records);
						prevScope.refHistoryGrid && (this.refHistoryGrid.records = prevScope.refHistoryGrid.records);

						// 아코디언 
						this.$timeout(function () {
							_this2.zabara = prevScope.zabara;

							if (_this2.zabara) {
								for (var key in _this2.zabara) {
									if (_this2.zabara.hasOwnProperty(key) && _this2.zabara[key]) {
										var $button = $('#' + key + 'Zabara'),
										    $parent = $button.closest('section'),
										    $body = $('#' + key);

										$parent.css('background', '#ababab').css('color', '#fff');
										$button.css('transform', '').css('color', '#fff');
										$body.show();
									}
								}
							}

							$('#main-contents').scrollTop(prevScope.scrollTop);
						});

						// 시스템명 
						prevScope.detail.sendSysCd && this.setSysCd('SEND', prevScope.detail.sendSysCd, prevScope.detail.sendSysNm);
						prevScope.detail.receiveSysCd && this.setSysCd('RECEIVE', prevScope.detail.receiveSysCd, prevScope.detail.receiveSysNm);
					}

					// 수정모드
					this.isAdd = prevScope.isAdd;
					if (prevScope.isEdit) {
						this.onEditMode();
					} else {
						this.offEditMode();
					}
				}

				if (param.data) {
					this.searchParam = {};
					this.searchParam.intrfccoms = { intrfcId: param.data.intrfcId, msgLayoutId: param.data.msgLayoutId };
					this.getIntrfccoms(true);

					this.setIntrfGridData(param.data.intrfcId);
					this._openIntrfaceDetailZabara();
				}
			} else {
				this.initSearch();
			}

			this.$scope.$on('$destroy', function () {
				_this2.scrollTop = $('#main-contents').scrollTop();
				_this2.utilService.setParams(currentStateName, { scope: _this2.$scope });
			});
		}
	}, {
		key: 'initScroll',
		value: function initScroll() {
			var _this3 = this;

			$('#sndMsgMapSrcGrid').on('scroll', function () {
				if (_this3.useRequestMsgScroll) {
					$('#sendMsgMapTrgGrid').find('.w2ui-grid-records').scrollTop($('#sndMsgMapSrcGrid').find('.w2ui-grid-records').scrollTop());
				}
			});
			$('#sendMsgMapTrgGrid').on('scroll', function () {
				if (_this3.useRequestMsgScroll) {
					$('#sndMsgMapSrcGrid').find('.w2ui-grid-records').scrollTop($('#sendMsgMapTrgGrid').find('.w2ui-grid-records').scrollTop());
				}
			});
			$('#rcvMsgMapSrcGrid').on('scroll', function () {
				if (_this3.useResponseMsgScroll) {
					$('#rcvMsgMapTrgGrid').find('.w2ui-grid-records').scrollTop($('#rcvMsgMapSrcGrid').find('.w2ui-grid-records').scrollTop());
				}
			});
			$('#rcvMsgMapTrgGrid').on('scroll', function () {
				if (_this3.useResponseMsgScroll) {
					$('#rcvMsgMapSrcGrid').find('.w2ui-grid-records').scrollTop($('#rcvMsgMapTrgGrid').find('.w2ui-grid-records').scrollTop());
				}
			});
		}
	}, {
		key: 'initZabara',
		value: function initZabara() {
			this.zabara = {};
		}
	}, {
		key: 'initText',
		value: function initText() {
			this.text = $.extend({}, bxMsg.getMessages('common'), bxMsg.getMessages('manageEaiInterface'));
		}
	}, {
		key: 'initSelect',
		value: function initSelect() {
			this.select = this.gridService.getSelect(this.codes['GRID_PAGE_SIZE'][1].cdVal);
		}
	}, {
		key: 'initSearch',
		value: function initSearch() {
			this.searchParam = {};
			this.resetSearch();
		}
	}, {
		key: 'initGenerate',
		value: function initGenerate() {
			var _this4 = this;

			this.intfcDto = function () {
				var rowData = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : {};

				rowData.id = _this4.utilService.uniqueId('TEMP_INTFC_DETAIL');
				return rowData;
			};

			this.sndReqDto = function () {
				var rowData = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : {};

				rowData.id = _this4.utilService.uniqueId('TEMP_SND_REQ');
				return rowData;
			};
		}
	}, {
		key: 'initGrid',
		value: function initGrid() {
			var _this5 = this;

			this.gridHeight = this.gridService.getGridHeight(this.select.pageSize);
			this.inferfaceMainGrid = {
				limit: this.select.pageSize,
				pageSize: this.select.pageSize,
				recordsCount: 0,
				recid: 'intrfcId',
				multiSelect: false,
				show: { columnHeaders: true, selectColumn: true },
				columns: [{ field: 'intrfcId', caption: this.text.intrfcId, size: '1.5%', sortable: true }, { field: 'intrfcNm', caption: this.text.intrfcNm, size: '2.5%', sortable: true, attr: 'align=left' }, { field: 'intrfcNmSub', caption: this.text.intrfcNmSub, size: '2.5%', sortable: true, attr: 'align=left' }, {
					field: 'lvCds', caption: this.text.lvCds, size: '0.7%',
					render: function render(data) {
						return data.lv1Cd ? data.lv1Cd : '';
					}
				}, {
					field: 'trxDscd', caption: this.text.trxDscd2, size: this.user.locale === 'en' ? '110px' : '0.5%',
					render: function render(data) {
						return _this5.codeService.getCodeValNm('TRAN_DSCD', data.trxDscd);
					}
				}, {
					field: 'intrfcWayCd', caption: this.text.trxTypeDscd, size: '0.7%', sortable: true,
					render: function render(data) {
						return _this5.codeService.getCodeValNm('INTRFC_WAY_CD', data.intrfcWayCd);
					}
				}, { field: 'sysCdS', caption: this.text.sysCdS, size: this.user.locale === 'en' ? '90px' : '0.5%', sortable: true }, { field: 'sysCdR', caption: this.text.sysCdR, size: this.user.locale === 'en' ? '95px' : '0.5%', sortable: true }, { field: 'regManId', caption: this.text.regManId, size: '0.7%', sortable: true }, {
					field: 'workStatusCd', caption: this.text.workStatusCd, size: this.user.locale === 'en' ? '120px' : '0.5%', sortable: true,
					render: function render(data) {
						return _this5.codeService.getCodeValNm('WORK_STATUS_CD', data.workStatusCd);
					}
				}, { field: 'regDttm', caption: this.text.regDttm, sortable: true, size: this.user.locale === 'en' ? '110px' : '0.5%',
					render: function render(data) {
						var regDttm = data.regDttm;
						var yy = regDttm.substring(0, 4);
						var mm = regDttm.substring(4, 6);
						var dd = regDttm.substring(6, 8);
						return yy + "/" + mm + "/" + dd;
					}
				}, {
					caption: this.text.edit, size: '120px',
					render: function render(data) {
						var html = '';

						if (_this5.user.perm.insert) {
							html += '<button type="button" class="bw-btn bxd bxd-new-file" title="' + _this5.text.copy + '" data-action="copy"></button>';
						}

						if (_this5.user.perm.update) {
							html += '<button type="button" class="bw-btn bxd bxd-edit2" title="' + _this5.text.modifiy + '" data-action="edit"></button>';
						}

						if (_this5.user.perm.delete) {
							html += '<button type="button" class="bw-btn bxd bxd-trash" title="' + _this5.text.delete + '" data-action="delete"></button>';
						}

						return html;
					}
				}],
				onClick: function onClick(e) {
					var grid = w2ui[_this5.inferfaceMainGrid.name];
					var eTarget = e.originalEvent.target;
					var recid = e.recid;
					var editData = grid.get(e.recid);

					_this5.pdfInterfaceId = recid;

					if ($(eTarget).attr('data-action') == 'export') {
						_this5.excelExport(editData.intrfcId);
						e.preventDefault();
						return;
					}
					if (eTarget.localName === 'button') {
						var action = $(eTarget).attr('data-action');
						if (action === 'edit') {
							// prevent deselect
							var _selection = w2ui[e.target].getSelection();
							if (_selection.length === 1 && _selection[0] === e.recid && !_.isEmpty(_this5.detail)) {
								e.preventDefault();
								_this5._onEdit();
								_this5.$scope.$apply();
							} else {
								_this5.setIntrfGridData(recid, false, function () {
									_this5._onEdit();
								});
								_this5._openIntrfaceDetailZabara();
							}

							return;
						} else if (action === 'delete') {
							var record = grid.get(recid);

							if (record.regManId !== _this5.user.userId) {
								_this5.popupService.simpleAlert(_this5.$scope, _this5.text.deleteMsg);
							} else {
								_this5.popupService.simpleConfirm(_this5.$scope, _this5.text.confirmDelete, function () {
									return _this5.deleteInfrcId(recid);
								});
							}

							e.preventDefault();
							return;
						} else if (action === 'copy') {
							_this5.add();
							_this5.setIntrfGridData(recid, true);

							return;
						}
					} else {
						_this5.offEditMode();
					}

					_this5.$scope.$apply();
					var selection = w2ui[e.target].getSelection();
					var $target = $(e.originalEvent.target);

					if ($target.find('.w2ui-grid-select-check').length === 0) {
						// prevent deselect
						if (selection.length === 1 && selection[0] === e.recid) {
							e.preventDefault();

							if (!_.isEmpty(_this5.detail)) {
								return;
							}
						}
					}

					_this5.setIntrfGridData(recid);
					_this5._openIntrfaceDetailZabara();
				}
			};

			this.interfaceDetailGrid = {
				limit: 999999,
				pageSize: 999999,
				recordsCount: 0,
				multiSelect: false,
				recid: 'id',
				onClick: function onClick(e) {
					// prevent deselect
					var selection = w2ui[e.target].getSelection();
					if (selection.length === 1 && selection[0] === e.recid) {
						e.preventDefault();
					}

					if (!_this5.isEdit) return;
					var grid = w2ui[e.target];
					var eTarget = e.originalEvent.target;

					if (eTarget.localName === 'button') {
						var action = $(eTarget).attr('data-action');
						switch (action) {
							case 'sys-search':
								_this5.popupService.openModal('SCR1302').then(function (sysCode) {
									var rowData = grid.get(e.recid);
									rowData.sysCd = sysCode.sysCd;
									rowData.sysNm = sysCode.sysNm;
								}).catch(function () {});
								break;
							case 'delete':
								_this5.popupService.simpleConfirm(_this5.$scope, _this5.text.confirmDelete, function () {
									return grid.remove(e.recid);
								});
								break;
						}
					} else {
						_this5._selectedIntrfcmsglayoutdtDto = {
							grid: grid,
							editData: grid.get(e.recid)
						};
					}
				},
				onUnselect: function onUnselect(e) {
					if (_.isEmpty(_this5._selectedIntrfcmsglayoutdtDto)) return;
					if (e.recid === _this5._selectedIntrfcmsglayoutdtDto.editData.recid) {
						_this5._selectedIntrfcmsglayoutdtDto = {};
					}
				},
				onEditField: function onEditField(e) {
					if (!_this5.isEdit) e.preventDefault();

					var grid = w2ui[e.target];
					var record = grid.get(e.recid);
				},
				onChange: function onChange(e) {
					var grid = w2ui[e.target];
					e.onComplete = function () {
						grid.save();
					};
				}
			};
			this.editIntrfcDetailGrid(false);

			this.sndReqLayoutGrid = {
				limit: 999999,
				pageSize: 999999,
				multiSelect: false,
				columns: this.getSndRecColumns(),
				recordsCount: 0,
				recid: 'msgLayoutId',
				onClick: function onClick(e) {
					// prevent deselect
					var selection = w2ui[e.target].getSelection();
					if (selection.length === 1 && selection[0] === e.recid) {
						e.preventDefault();
					}

					var grid = w2ui[e.target];
					var eTarget = e.originalEvent.target;
					var recid = e.recid;

					if (eTarget.localName === 'button') {
						switch ($(eTarget).attr('data-action')) {
							case 'more':
								_this5.popupService.openModal('SCR0704', { msgLayoutId: recid, codes: _this5.codes }).then(function () {}).catch(function () {});
								break;
							case 'diff':
								var msglayout = grid.get(recid);

								_this5.popupService.openModal('SCR0706', {
									intrfcId: msglayout.intrfcId,
									msgLayoutId: recid,
									srTypeCd: msglayout.srTypeCd,
									rqstRspsTypeCd: msglayout.rqstRspsTypeCd,
									layoutSeq: msglayout.rqstRspsSeq
								}).then(function () {}).catch(function () {});
								break;
							case 'delete':
								if (!_this5.isEdit) return;
								grid.remove(recid);
								_this5.setIoCopy();

								_this5.sndMsgMapSrcGrid.records = _this5.sndMsgMapSrcGrid.records.filter(function (v) {
									return v.msgLayoutId != recid;
								});
								_this5.sendMsgMapTrgGrid.records.map(function (v) {
									if (v.mappingTypeCd === 'PROPT' && v.srcData && v.srcData.indexOf(recid) !== -1) {
										v.srcData = '';
									}
								});

								_this5.setGridSeq();
								_this5.$scope.$apply();
								break;
						}
					}
				}
			};

			this.sndResLayoutGrid = {
				limit: 999999,
				pageSize: 999999,
				multiSelect: false,
				columns: this.getSndRecColumns(),
				recordsCount: 0,
				recid: 'msgLayoutId',
				onClick: function onClick(e) {
					// prevent deselect
					var selection = w2ui[e.target].getSelection();
					if (selection.length === 1 && selection[0] === e.recid) {
						e.preventDefault();
					}

					var grid = w2ui[e.target];
					var eTarget = e.originalEvent.target;
					var recid = e.recid;

					if (eTarget.localName === 'button') {
						switch ($(eTarget).attr('data-action')) {
							case 'more':
								_this5.popupService.openModal('SCR0704', { msgLayoutId: recid, codes: _this5.codes }).then(function () {}).catch(function () {});
								break;
							case 'diff':
								var msglayout = grid.get(recid);

								_this5.popupService.openModal('SCR0706', {
									intrfcId: msglayout.intrfcId,
									msgLayoutId: recid,
									srTypeCd: msglayout.srTypeCd,
									rqstRspsTypeCd: msglayout.rqstRspsTypeCd,
									layoutSeq: msglayout.rqstRspsSeq
								}).then(function () {}).catch(function () {});
								break;
							case 'delete':
								if (!_this5.isEdit) return;
								grid.remove(e.recid);
								_this5.setIoCopy();

								_this5.rcvMsgMapTrgGrid.records = _this5.rcvMsgMapTrgGrid.records.filter(function (v) {
									return v.msgLayoutId !== recid;
								});
								_this5.$scope.$apply();
								break;
						}
					}
				}
			};

			this.rcvReqLayoutGrid = {
				limit: 999999,
				pageSize: 999999,
				multiSelect: false,
				columns: this.getSndRecColumns(),
				recordsCount: 0,
				recid: 'msgLayoutId',
				onClick: function onClick(e) {
					// prevent deselect
					var selection = w2ui[e.target].getSelection();
					if (selection.length === 1 && selection[0] === e.recid) {
						e.preventDefault();
					}

					var grid = w2ui[e.target];
					var eTarget = e.originalEvent.target;
					var recid = e.recid;

					if (eTarget.localName === 'button') {
						switch ($(eTarget).attr('data-action')) {
							case 'more':
								_this5.popupService.openModal('SCR0704', { msgLayoutId: recid, codes: _this5.codes }).then(function () {}).catch(function () {});
								break;
							case 'diff':
								var msglayout = grid.get(recid);

								_this5.popupService.openModal('SCR0706', {
									intrfcId: msglayout.intrfcId,
									msgLayoutId: recid,
									srTypeCd: msglayout.srTypeCd,
									rqstRspsTypeCd: msglayout.rqstRspsTypeCd,
									layoutSeq: msglayout.rqstRspsSeq
								}).then(function () {}).catch(function () {});
								break;
							case 'delete':
								if (!_this5.isEdit) return;
								grid.remove(e.recid);

								_this5.sendMsgMapTrgGrid.records = _this5.sendMsgMapTrgGrid.records.filter(function (v) {
									return v.msgLayoutId !== recid;
								});
								_this5.$scope.$apply();
								break;
						}
					}
				}
			};

			this.rcvResLayoutGrid = {
				limit: 999999,
				pageSize: 999999,
				columns: this.getSndRecColumns(),
				recordsCount: 0,
				recid: 'msgLayoutId',
				onClick: function onClick(e) {
					// prevent deselect
					var selection = w2ui[e.target].getSelection();
					if (selection.length === 1 && selection[0] === e.recid) {
						e.preventDefault();
					}

					var grid = w2ui[e.target];
					var eTarget = e.originalEvent.target;
					var recid = e.recid;

					if (eTarget.localName === 'button') {
						switch ($(eTarget).attr('data-action')) {
							case 'more':
								_this5.popupService.openModal('SCR0704', { msgLayoutId: recid, codes: _this5.codes }).then(function () {}).catch(function () {});
								break;
							case 'diff':
								var msglayout = grid.get(recid);

								_this5.popupService.openModal('SCR0706', {
									intrfcId: msglayout.intrfcId,
									msgLayoutId: recid,
									srTypeCd: msglayout.srTypeCd,
									rqstRspsTypeCd: msglayout.rqstRspsTypeCd,
									layoutSeq: msglayout.rqstRspsSeq
								}).then(function () {}).catch(function () {});
								break;
							case 'delete':
								if (!_this5.isEdit) return;
								grid.remove(e.recid);

								_this5.rcvMsgMapSrcGrid.records = _this5.rcvMsgMapSrcGrid.records.filter(function (v) {
									return v.msgLayoutId != recid;
								});
								_this5.rcvMsgMapTrgGrid.records.map(function (v) {
									if (v.mappingTypeCd === 'PROPT' && v.srcData && v.srcData.indexOf(recid) !== -1) {
										v.srcData = '';
									}
								});

								_this5.setGridSeq();
								_this5.$scope.$apply();
								break;
						}
					}
				}
			};

			this.sndMsgMapSrcGrid = {
				limit: 999999,
				pageSize: 999999,
				columns: this.getMsgSrcColumns(),
				records: [],
				recordsCount: 0,
				recid: 'fldUnqId',
				onClick: function onClick(e) {
					// prevent deselect
					var selection = w2ui[e.target].getSelection();
					if (selection.length === 1 && selection[0] === e.recid) {
						e.preventDefault();
					}
				}
			};

			this.rcvMsgMapSrcGrid = {
				limit: 999999,
				pageSize: 999999,
				columns: this.getMsgSrcColumns(),
				records: [],
				recordsCount: 0,
				recid: 'fldUnqId',
				onClick: function onClick(e) {
					// prevent deselect
					var selection = w2ui[e.target].getSelection();
					if (selection.length === 1 && selection[0] === e.recid) {
						e.preventDefault();
					}
				}
			};

			this.sendSrcDataOptions = [];
			this.sendMsgMapTrgSrcData = {
				field: 'srcData',
				caption: this.text.mappingData,
				attr: 'align=left',
				size: '3%',
				editable: { type: 'select', items: this.sendSrcDataOptions }
			};

			this.sendMsgMapTrgGrid = {
				limit: 999999,
				pageSize: 999999,
				records: [],
				recordsCount: 0,
				recid: 'fldUnqId',
				onClick: function onClick(e) {
					var _sendSrcDataOptions;

					// prevent deselect
					var selection = w2ui[e.target].getSelection();
					if (selection.length === 1 && selection[0] === e.recid) {
						e.preventDefault();
					}

					var changeDot = e.recid.replace(/\./g, '\\.');
					$('tr#grid_' + e.target + '_rec_' + changeDot).removeClass('bg-red');

					if (!_this5.isEdit) {
						e.preventDefault();
						return;
					}
					if (e.column === 7) {
						var record = w2ui[e.target].get(e.recid);
						var mappingTypeCd = record.mappingTypeCd;

						if (record.w2ui && record.w2ui.changes && record.w2ui.changes.mappingTypeCd) {
							mappingTypeCd = record.w2ui.changes.mappingTypeCd;
						}

						switch (mappingTypeCd) {
							case 'PROPT':
								_this5.sendSrcDataOptions.length = 0;
								(_sendSrcDataOptions = _this5.sendSrcDataOptions).push.apply(_sendSrcDataOptions, (0, _toConsumableArray3.default)(_this5.sndMsgMapSrcGrid.records.map(function (v) {
									return { id: v.fldUnqId, text: '[' + v.gridSeq + '] ' + v.fldUnqId };
								})));
								_this5.sendMsgMapTrgSrcData.editable.type = 'select';
								break;
							case 'CONST':
								_this5.sendMsgMapTrgSrcData.editable.type = 'text';
								break;
							case 'NONE':
								e.preventDefault();
								break;
							case 'SYSDATE':
								_this5.sendSrcDataOptions.length = 0;
								var sysDateList = _this5.codeService.getCodeValList('SYS_DATE_CD');
								for (var i = 0; i < sysDateList.length; i++) {
									_this5.sendSrcDataOptions.push(sysDateList[i]);
								}
								_this5.sendMsgMapTrgSrcData.editable.type = 'select';
								break;
						}
					}
				},
				onEditField: function onEditField(e) {
					var _sendSrcDataOptions2;

					if (!_this5.isEdit) {
						e.preventDefault();
						return;
					}
					if (e.column === 7) {
						var record = w2ui[e.target].get(e.recid);
						var mappingTypeCd = record.mappingTypeCd;

						if (record.w2ui && record.w2ui.changes && record.w2ui.changes.mappingTypeCd) {
							mappingTypeCd = record.w2ui.changes.mappingTypeCd;
						}

						switch (mappingTypeCd) {
							case 'PROPT':
								_this5.sendSrcDataOptions.length = 0;
								(_sendSrcDataOptions2 = _this5.sendSrcDataOptions).push.apply(_sendSrcDataOptions2, (0, _toConsumableArray3.default)(_this5.sndMsgMapSrcGrid.records.map(function (v) {
									return { id: v.fldUnqId, text: '[' + v.gridSeq + '] ' + v.fldUnqId };
								})));
								_this5.sendMsgMapTrgSrcData.editable.type = 'select';
								break;
							case 'CONST':
								_this5.sendMsgMapTrgSrcData.editable.type = 'text';
								break;
							case 'NONE':
								e.preventDefault();
								break;
							case 'SYSDATE':
								_this5.sendSrcDataOptions.length = 0;
								var sysDateList = _this5.codeService.getCodeValList('SYS_DATE_CD');
								for (var i = 0; i < sysDateList.length; i++) {
									_this5.sendSrcDataOptions.push(sysDateList[i]);
								}
								_this5.sendMsgMapTrgSrcData.editable.type = 'select';
								break;
						}
					}
				},
				onChange: function onChange(e) {
					var grid = w2ui[e.target];
					var record = grid.get(e.recid);

					if (e.column === 6) {
						record.srcData = '';
						record.mappingTypeCd = e.value_new;
						record.w2ui && record.w2ui.changes && delete record.w2ui.changes.srcData;
						grid.refreshCell(e.recid, 'srcData');
					} else if (e.column === 7) {
						record.srcData = e.value_new;
					}
				}
			};

			this.recvSrcDataOptions = [];
			this.recvMsgMapTrgSrcData = {
				field: 'srcData',
				caption: this.text.mappingData,
				attr: 'align=left',
				size: '3%',
				editable: { type: 'select', items: this.recvSrcDataOptions }
			};

			this.rcvMsgMapTrgGrid = {
				limit: 999999,
				pageSize: 999999,
				recordsCount: 0,
				records: [],
				recid: 'fldUnqId',
				onClick: function onClick(e) {
					var _recvSrcDataOptions;

					// prevent deselect
					var selection = w2ui[e.target].getSelection();
					if (selection.length === 1 && selection[0] === e.recid) {
						e.preventDefault();
					}

					if (!_this5.isEdit) {
						e.preventDefault();
						return;
					}

					if (e.column === 7) {
						var record = w2ui[e.target].get(e.recid);
						var mappingTypeCd = record.mappingTypeCd;

						if (record.w2ui && record.w2ui.changes && record.w2ui.changes.mappingTypeCd) {
							mappingTypeCd = record.w2ui.changes.mappingTypeCd;
						}

						switch (mappingTypeCd) {
							case 'PROPT':
								_this5.recvSrcDataOptions.length = 0;
								(_recvSrcDataOptions = _this5.recvSrcDataOptions).push.apply(_recvSrcDataOptions, (0, _toConsumableArray3.default)(_this5.rcvMsgMapSrcGrid.records.map(function (v) {
									return { id: v.fldUnqId, text: '[' + v.gridSeq + '] ' + v.fldUnqId };
								})));
								_this5.recvMsgMapTrgSrcData.editable.type = 'select';
								break;
							case 'NONE':
								e.preventDefault();
								break;
							case 'CONST':
								_this5.recvMsgMapTrgSrcData.editable.type = 'text';
								break;
							case 'SYSDATE':
								_this5.recvSrcDataOptions.length = 0;
								var sysDateList = _this5.codeService.getCodeValList('SYS_DATE_CD');
								for (var i = 0; i < sysDateList.length; i++) {
									_this5.recvSrcDataOptions.push(sysDateList[i]);
								}
								_this5.recvMsgMapTrgSrcData.editable.type = 'select';
								break;
						}
					}

					var changeDot = e.recid.replace(/\./g, '\\.');
					$('tr#grid_' + e.target + '_rec_' + changeDot).removeClass('bg-red');
				},
				onEditField: function onEditField(e) {
					var _recvSrcDataOptions2;

					if (!_this5.isEdit) {
						e.preventDefault();
						return;
					}

					if (e.column === 7) {
						var record = w2ui[e.target].get(e.recid);
						var mappingTypeCd = record.mappingTypeCd;

						if (record.w2ui && record.w2ui.changes && record.w2ui.changes.mappingTypeCd) {
							mappingTypeCd = record.w2ui.changes.mappingTypeCd;
						}

						switch (mappingTypeCd) {
							case 'PROPT':
								_this5.recvSrcDataOptions.length = 0;
								(_recvSrcDataOptions2 = _this5.recvSrcDataOptions).push.apply(_recvSrcDataOptions2, (0, _toConsumableArray3.default)(_this5.rcvMsgMapSrcGrid.records.map(function (v) {
									return { id: v.fldUnqId, text: '[' + v.gridSeq + '] ' + v.fldUnqId };
								})));
								_this5.recvMsgMapTrgSrcData.editable.type = 'select';
								break;
							case 'NONE':
								e.preventDefault();
								break;
							case 'CONST':
								_this5.recvMsgMapTrgSrcData.editable.type = 'text';
								break;
							case 'SYSDATE':
								_this5.recvSrcDataOptions.length = 0;
								var sysDateList = _this5.codeService.getCodeValList('SYS_DATE_CD');
								for (var i = 0; i < sysDateList.length; i++) {
									_this5.recvSrcDataOptions.push(sysDateList[i]);
								}
								_this5.recvMsgMapTrgSrcData.editable.type = 'select';
								break;
						}
					}
				},
				onChange: function onChange(e) {
					var grid = w2ui[e.target];
					var record = grid.get(e.recid);

					if (e.column === 6) {
						record.srcData = '';
						record.mappingTypeCd = e.value_new;
						record.w2ui && record.w2ui.changes && delete record.w2ui.changes.srcData;
						grid.refreshCell(e.recid, 'srcData');
					} else if (e.column === 7) {
						record.srcData = e.value_new;
					}
				}
			};

			this.initMsgTrgColumns();

			this.deployTargetSysGrid = {
				limit: 999999,
				pageSize: 999999,
				recordsCount: 0,
				recid: 'deploySysCd',
				columns: [{ field: 'deploySysCd', caption: this.text.sysCd, size: '1%' }, { field: 'deploySysNm', caption: this.text.sysNm, size: '1%', attr: 'align=left' }, { field: 'deployUrl', caption: this.text.deployUrl, size: '1%', attr: 'align=left' }, {
					field: 'deployResultCd', caption: this.text.deployResultCd, size: '1%',
					render: function render(data) {
						if (data.deployResultCd === 'FAIL') {
							return '<span class="chr-c-orange">' + data.deployResultCd + '</span>';
						}

						return data.deployResultCd;
					}
				}, {
					field: 'edit', caption: this.text.edit, size: '1%',
					render: function render(data) {
						if (data.deploySysCd === 'E2E' || data.deploySysCd === 'RSH' || data.deploySysCd === 'SIM') {
							return '';
						}

						return '\n\t\t\t\t\t\t\t<button type="button" class="bw-btn bxd bxd-trash" title="' + _this5.text.delete + '" data-action="delete"></button>\n\t\t\t\t\t\t';
					}
				}],
				onClick: function onClick(e) {
					// prevent deselect
					var selection = w2ui[e.target].getSelection();
					if (selection.length === 1 && selection[0] === e.recid) {
						e.preventDefault();
					}

					var grid = w2ui[e.target];
					var eTarget = e.originalEvent.target;

					if (eTarget.localName === 'button') {
						if (_this5.isEdit) grid.remove(e.recid);
					} else {
						_this5.selectedIntrfcdeploysysdtDto = grid.get(e.recid);
						_this5.$scope.$apply();
					}
				},
				onUnselect: function onUnselect(e) {
					_this5.selectedIntrfcdeploysysdtDto = {};
				}
			};

			this.refHistoryGrid = {
				limit: 999999,
				pageSize: 999999,
				recordsCount: 0,
				recid: 'deployDttm',
				columns: [{ field: 'id', caption: '', hidden: true }, {
					field: 'deployVersion', caption: this.text.deployVersion, size: '1%',
					render: function render(data) {
						var txt = '-';

						if (data.deployResultCd.indexOf('SUCCESS') !== -1) {
							txt = data.deployVersion;
						}

						return txt;
					}
				}, {
					caption: this.text.deployDscd, size: '1%',
					render: function render(data) {
						var txt = '';

						if (data.deployResultCd.indexOf('SUCCESS') !== -1) {
							if (data.deployResultCd.indexOf('_') !== -1) {
								txt = _this5.text.redeploy;
							} else {
								txt = _this5.text.deploy;
							}
						} else {
							if (data.deployResultCd.indexOf('_') !== -1) {
								txt = _this5.text.redeployFail;
							} else {
								txt = _this5.text.deployFail;
							}
						}

						return txt;
					}
				}, {
					caption: this.text.prevDeploy, size: '1%',
					render: function render(data) {
						var deployResultCd = data.deployResultCd;
						var idx = deployResultCd.indexOf('_');
						var version = idx === -1 ? '-' : deployResultCd.substr(idx + 1);

						return version;
					}
				}, {
					field: 'deployDttm', caption: this.text.deployDttm, size: '1%',
					render: function render(data) {
						return _this5.utilService.setRegDttm(data.deployDttm);
					}
				}, { field: 'deploySysCd', caption: this.text.deploySysCd, size: '1%' }, {
					caption: this.text.deployInterfaceDetail, size: this.user.locale === 'en' ? '160px' : '120px',
					render: function render(data) {
						var txt = '';

						if (data.deployResultCd.indexOf('SUCCESS') !== -1) {
							txt = '<button type="button" class="bw-btn bxd bxd-search2" data-action="more"></button>';
						}

						return txt;
					}
				}, {
					caption: this.text.deployResultDetail, size: this.user.locale === 'en' ? '160px' : '100px',
					render: function render(data) {
						return '<button type="button" class="bw-btn bxd bxd-search2" data-action="result"></button>';
					}
				}, {
					field: 'baepo', caption: this.text.redeploy, size: '1%',
					render: function render(data) {
						var txt = '';

						if (data.deployResultCd.indexOf('SUCCESS') !== -1) {
							txt = '<button type="button" class="bw-btn bxd bxd-rocket" title="' + _this5.text.redeploy + '" data-action="redeploy"></button>';
						}

						return txt;
					}
				}],
				onClick: function onClick(e) {
					// prevent deselect
					var selection = w2ui[e.target].getSelection();
					if (selection.length === 1 && selection[0] === e.recid) {
						e.preventDefault();
					}

					var grid = w2ui[_this5.refHistoryGrid.name];
					var eTarget = e.originalEvent.target;
					var recid = e.recid;
					var editData = grid.get(e.recid);
					console.log(e.recid);
					console.log(editData);

					if (eTarget.localName === 'button') {
						switch ($(eTarget).attr('data-action')) {
							case 'more':
								_this5.utilService.openTab(_this5.$scope, {
									state: 'main.manageEaiInterfaceDetail',
									label: _this5.text.titleHeader
								}, {
									data: {
										intrfcId: _this5.detail.intrfcId,
										deployVersion: editData.deployVersion
									}
								});
								break;
							case 'result':
								_this5.popupService.openModal('SCR0708', {
									reqData: {
										intrfcId: _this5.detail.intrfcId,
										deployVersion: editData.deployVersion,
										deployDttm: editData.deployDttm,
										deployResultCd: editData.deployResultCd
									}
								});
								break;
							case 'redeploy':
								_this5.popupService.simpleConfirm(_this5.$scope, _this5.text.confirmRedeploy, function () {
									return _this5.redeployInterface(grid.get(e.recid));
								});
								break;
						}
					}
				},
				onDblClick: function onDblClick(e) {
					var grid = w2ui[_this5.refHistoryGrid.name];
					var recid = e.recid;
					var editData = grid.get(recid);

					_this5.popupService.openModal('SCR0708', {
						reqData: {
							intrfcId: _this5.detail.intrfcId,
							deployVersion: editData.deployVersion,
							deployDttm: editData.deployDttm,
							deployResultCd: editData.deployResultCd
						}
					});
				}
			};
		}
	}, {
		key: 'editIntrfcDetailGrid',
		value: function editIntrfcDetailGrid() {
			var _this6 = this;

			var isEdit = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : true;

			this.interfaceDetailGrid.columns = [{
				field: 'srTypeCd', caption: this.text.srTypeCd, size: '1.5%', sortable: true,
				render: function render(data) {
					return _this6.codeService.getCodeValNm('SENC_RECV_DSCD', data.srTypeCd);
				}
			}, {
				field: 'sysCd', caption: this.text.sysCd, size: '1.5%',
				render: function render(data, index) {
					if (index < 2) return data.sysCd;else return (data.sysCd || "") + '<button type="button" class="bw-btn bxd bxd-search" data-action="sys-search"></button>';;
				}
			}, { field: 'sysNm', caption: this.text.sysNm, size: '1.5%' }, { field: 'crgManNm', caption: '<i class="asterisk"></i>' + this.text.crgManNm, size: '1.5%', editable: { type: 'text' } }, {
				field: 'trxCd', caption: '<i class=""></i>' + this.text.trxCd, size: '1%', editable: { type: 'text' }
			}];

			if (!_.isEmpty(this.detail)) this._changeDetailColumnByTrxDscd();
		}
	}, {
		key: '_changeDetailColumnByTrxDscd0',
		value: function _changeDetailColumnByTrxDscd0() {
			this.onClickZabara('sndReqLayoutGrid', 'sndResLayoutGrid', 'rcvReqLayoutGrid', 'rcvResLayoutGrid');

			this._changeDetailColumnByTrxDscd();
		}
	}, {
		key: '_changeDetailColumnByTrxDscd',
		value: function _changeDetailColumnByTrxDscd() {
			var _this7 = this;

			var columns = this.interfaceDetailGrid.columns;

			if (this.detail && this.detail.trxDscd === 'ONLINE' && this.detail.intrfcWayCd == 'APTOAP') {
				columns.splice(4, 1, {
					field: 'trxCd', caption: '<i class=""></i>' + this.text.trxCd, size: '1%', editable: { type: 'text' }
				});
			} else {
				this.interfaceDetailGrid.columns = [{
					field: 'srTypeCd', caption: this.text.srTypeCd, size: '1.5%', sortable: true,
					render: function render(data) {
						return _this7.codeService.getCodeValNm('SENC_RECV_DSCD', data.srTypeCd);
					}
				}, {
					field: 'sysCd', caption: this.text.sysCd, size: '1.5%',
					render: function render(data, index) {
						if (index < 2) return data.sysCd;else return (data.sysCd || "") + '<button type="button" class="bw-btn bxd bxd-search" data-action="sys-search"></button>';
					}
				}, { field: 'sysNm', caption: this.text.sysNm, size: '1.5%' }, { field: 'crgManNm', caption: '<i class="asterisk"></i>' + this.text.crgManNm, size: '1.5%', editable: { type: 'text' } }];
			}
		}
	}, {
		key: 'getSndRecColumns',
		value: function getSndRecColumns() {
			var _this8 = this;

			return [{
				field: 'rqstRspsSeq', caption: this.text.seq, size: '60px', render: function render(data, index) {
					return index + 1;
				}
			}, { field: 'msglayoutbsDto.msgLayoutId', caption: this.text.msgLayoutId, size: '1.5%' }, { field: 'msglayoutbsDto.msgNm', caption: this.text.msgNm, size: '1.5%', attr: 'align=left' }, {
				field: 'msglayoutbsDto.trxDscd', caption: this.text.trxDscd, size: '1%',
				render: function render(data, index, colIndex) {
					var trxDscd = void 0;

					if (data && data.msglayoutbsDto && data.msglayoutbsDto.trxDscd) {
						trxDscd = data.msglayoutbsDto.trxDscd;
					}

					return _this8.codeService.getCodeValNm('TRAN_DSCD', trxDscd);
				}
			}, {
				field: 'msglayoutbsDto.msgDscd', caption: this.text.msgDscd, size: '1%',
				render: function render(data, index, colIndex) {
					var msgDscd = void 0;

					if (data && data.msglayoutbsDto && data.msglayoutbsDto.msgDscd) {
						msgDscd = data.msglayoutbsDto.msgDscd;
					}

					return _this8.codeService.getCodeValNm('MSG_TYPE', msgDscd);
				}
			}, {
				field: 'msglayoutbsDto.workStatusCd', caption: this.text.workStatusCd, size: '1%', sortable: true,
				render: function render(data, index, colIndex) {
					var workStatusCd = void 0;

					if (data && data.msglayoutbsDto && data.msglayoutbsDto.workStatusCd) {
						workStatusCd = data.msglayoutbsDto.workStatusCd;
					}

					return _this8.codeService.getCodeValNm('WORK_STATUS_CD', workStatusCd);
				}
			}, {
				field: 'more', caption: this.text.showDetail, size: '1%',
				render: function render(data) {
					return '\n\t\t\t\t\t\t<button type="button" class="bw-btn bxd bxd-zoom-in" data-action="more"></button>\n\t\t\t\t\t';
				}
			}, {
				field: 'diff', caption: this.text.msgDiff, size: '1%',
				render: function render(data) {
					return '\n\t\t\t\t\t\t<button type="button" class="bw-btn bxd bxd-split-vertical" title="' + _this8.text.msgDiff + '" data-action="diff"></button>\n\t\t\t\t\t';
				}
			}, {
				field: 'delete', caption: this.text.edit, size: '1%',
				render: function render(data) {
					return '\n\t\t\t\t\t\t<button type="button" class="bw-btn bxd bxd-trash" data-action="delete"></button>\n\t\t\t\t\t';
				}
			}];
		}
	}, {
		key: 'getMsgSrcColumns',
		value: function getMsgSrcColumns() {
			return [{ field: 'gridSeq', caption: this.text.seq, size: '50px' }, { field: 'msgLayoutId', caption: this.text.msgLayoutId, size: '1.2%' }, { field: 'fldEngNm', caption: this.text.fldEngNm, size: '2%', attr: 'align=left' }, { field: 'fldKorNm', caption: this.text.fldKorNm, size: '1.3%', attr: 'align=left' }, { field: 'dataTypeNm', caption: this.text.dataTypeNm, size: '1%' }, { field: 'fldLvNo', caption: this.text.fldLvNo, size: '50px' }, { field: 'msgLen', caption: this.text.msgLen, size: '50px' }];
		}
	}, {
		key: 'initMsgTrgColumns',
		value: function initMsgTrgColumns() {
			var _this9 = this;

			this.sendMsgMapTrgGrid.columns = [{ field: 'msgLayoutId', caption: this.text.msgLayoutId, size: '1.2%' }, { field: 'fldEngNm', caption: this.text.fldEngNm, size: '2%', attr: 'align=left' }, { field: 'fldKorNm', caption: this.text.fldKorNm, size: '1.3%', attr: 'align=left' }, { field: 'dataTypeNm', caption: this.text.dataTypeNm, size: '1%' }, { field: 'fldLvNo', caption: this.text.fldLvNo, size: '50px' }, { field: 'msgLen', caption: this.text.msgLen, size: '50px' }, {
				field: 'mappingTypeCd', caption: this.text.mappingTypeCd, size: '1%',
				editable: { type: 'select', items: this.gridService.getSelectItemsFromCodes(this.codes.MAPPING_DSCD) },
				render: function render(data, index, colIndex) {
					var mappingTypeCd = w2ui[_this9.sendMsgMapTrgGrid.name].getCellValue(index, colIndex);
					return _this9.codeService.getCodeValNm(_this9.codes.MAPPING_DSCD, mappingTypeCd);
				}
			}, this.sendMsgMapTrgSrcData];

			this.rcvMsgMapTrgGrid.columns = [{ field: 'msgLayoutId', caption: this.text.msgLayoutId, size: '1.2%' }, { field: 'fldEngNm', caption: this.text.fldEngNm, size: '2%', attr: 'align=left' }, { field: 'fldKorNm', caption: this.text.fldKorNm, size: '1.3%', attr: 'align=left' }, { field: 'dataTypeNm', caption: this.text.dataTypeNm, size: '1%' }, { field: 'fldLvNo', caption: this.text.fldLvNo, size: '50px' }, { field: 'msgLen', caption: this.text.msgLen, size: '50px' }, {
				field: 'mappingTypeCd', caption: this.text.mappingTypeCd, size: '1%',
				editable: { type: 'select', items: this.gridService.getSelectItemsFromCodes(this.codes.MAPPING_DSCD) },
				render: function render(data, index, colIndex) {
					var mappingTypeCd = w2ui[_this9.rcvMsgMapTrgGrid.name].getCellValue(index, colIndex);
					return _this9.codeService.getCodeValNm(_this9.codes.MAPPING_DSCD, mappingTypeCd);
				}
			}, this.recvMsgMapTrgSrcData];
		}
	}, {
		key: 'getIntrfccoms',
		value: function getIntrfccoms() {
			var _this10 = this;

			var goToFirst = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : false;

			var httpService = this.httpService;

			var _gridService$getPageI = this.gridService.getPageInfo(this.select, this.pageNumber),
			    pageNumber = _gridService$getPageI.pageNumber,
			    pageSize = _gridService$getPageI.pageSize;

			var url = '/intrfccoms?pageNumber=' + (goToFirst ? 1 : pageNumber) + '&pageSize=' + pageSize + '&intrfcTypeCd=' + this.intrfcTypeCd;

			httpService.get(url, this.searchParam.intrfccoms).then(function (res) {
				var records = res.intrfccombsOutList,
				    recordsCount = res.totalCnt;


				if (res.isError) {
					_this10.popupService.detailAlert(_this10.$scope, res.data.message, res.data.stackTrace);
					return;
				}

				_this10.inferfaceMainGrid.records = records;
				_this10.inferfaceMainGrid.recordsCount = recordsCount;

				if (!_.isEmpty(_this10.inferfaceMainGrid.name)) {
					w2ui[_this10.inferfaceMainGrid.name].selectNone();
				}

				if (goToFirst) {
					_this10.pageNumber = 1;
					_this10.$scope.$broadcast('resetPage', _this10.pageNumber);
				}
			});
		}
	}, {
		key: 'setIntrfGridData',
		value: function setIntrfGridData(id) {
			var _this11 = this;

			var isCreateInterfaceId = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : false;
			var afterFn = arguments[2];

			var httpService = this.httpService;

			httpService.get('/intrfccoms/' + id).then(function (data) {
				_this11.useSndReqToResLayoutGrid = false;
				_this11.useSndReqLayoutGrid = false;
				_this11.useSndResLayoutGrid = false;
				_this11.useRequestMsgScroll = true;
				_this11.useResponseMsgScroll = true;
				_this11.compulsionDeployYn = false;

				_this11.setIntrfAccordion(data, isCreateInterfaceId);
				_this11._changeDetailColumnByTrxDscd();

				afterFn && afterFn();
			});
		}
	}, {
		key: 'setIntrfAccordion',
		value: function setIntrfAccordion(data) {
			var _this12 = this;

			var isCreateInterfaceId = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : false;
			var intrfcsrsysRecords = data.intrfcsrsysdtDto,
			    intrfcmsglayoutdtDtoRecords = data.intrfcmsglayoutdtDto,
			    intrfcdeploysysdtDtoRecords = data.intrfcdeploysysdtDto,
			    intrfcdeployhisthsDtoRecords = data.intrfcdeployhisthsDto;


			if (data.isError) {
				this.openAlert(data.data.message);
				return;
			}

			if (!isCreateInterfaceId) this.resetDetail(false);
			this.interfaceDetailGrid.records = intrfcsrsysRecords.map(function (intrfcsrsys) {
				return _this12.intfcDto(intrfcsrsys);
			});

			var sends = intrfcmsglayoutdtDtoRecords.filter(function (v) {
				return v.srTypeCd === 'SEND';
			});
			var receives = intrfcmsglayoutdtDtoRecords.filter(function (v) {
				return v.srTypeCd === 'RECEIVE';
			});
			var sendRequest = sends.filter(function (v) {
				return v.rqstRspsTypeCd === 'REQUEST';
			});
			var sendReponse = sends.filter(function (v) {
				return v.rqstRspsTypeCd === 'RESPONSE';
			});
			var receiveRequest = receives.filter(function (v) {
				return v.rqstRspsTypeCd === 'REQUEST';
			});
			var receiveResponse = receives.filter(function (v) {
				return v.rqstRspsTypeCd === 'RESPONSE';
			});

			this.sndReqLayoutGrid.records = sendRequest;
			this.sndResLayoutGrid.records = sendReponse;
			this.rcvReqLayoutGrid.records = receiveRequest;
			this.rcvResLayoutGrid.records = receiveResponse;

			var sendMsgMapSrc = sendRequest.filter(function (v) {
				return !_.isEmpty(v.msglayoutbsDto);
			}).map(function (sr) {
				return sr.msglayoutbsDto.msglayoutdtDto;
			}).reduce(function (v1, v2) {
				return v1.concat(v2);
			}, []);

			var sendMsgMapTrg = receiveRequest.filter(function (v) {
				return !_.isEmpty(v.msglayoutbsDto);
			}).map(function (rr) {
				return rr.msglayoutbsDto.msglayoutdtDto;
			}).reduce(function (v1, v2) {
				return v1.concat(v2);
			}, []).map(function (v) {
				v.mappingTypeCd = 'NONE';
				return v;
			});
			var recvMsgMapSrc = receiveResponse.filter(function (v) {
				return !_.isEmpty(v.msglayoutbsDto);
			}).map(function (sr) {
				return sr.msglayoutbsDto.msglayoutdtDto;
			}).reduce(function (v1, v2) {
				return v1.concat(v2);
			}, []);

			var recvMsgMapTrg = sendReponse.filter(function (v) {
				return !_.isEmpty(v.msglayoutbsDto);
			}).map(function (sr) {
				return sr.msglayoutbsDto.msglayoutdtDto;
			}).reduce(function (v1, v2) {
				return v1.concat(v2);
			}, []).map(function (v) {
				v.mappingTypeCd = 'NONE';
				return v;
			});

			this.sndMsgMapSrcGrid.records = sendMsgMapSrc;
			this.sendMsgMapTrgGrid.records = sendMsgMapTrg;
			this.rcvMsgMapSrcGrid.records = recvMsgMapSrc;
			this.rcvMsgMapTrgGrid.records = recvMsgMapTrg;
			this.setGridSeq();

			data.intrfccombsMappingReqDto && data.intrfccombsMappingReqDto.map(function (reqestMapping) {
				var result = sendMsgMapTrg.filter(function (v) {
					return v.fldUnqId == reqestMapping.targetData;
				});

				if (result.length > 0) {
					result[0].mappingTypeCd = reqestMapping.mappingTypeCd;
					result[0].srcData = reqestMapping.srcData;
				}
			});

			data.intrfccombsMappingResDto && data.intrfccombsMappingResDto.map(function (responseMapping) {
				var result = recvMsgMapTrg.filter(function (v) {
					return v.fldUnqId == responseMapping.targetData;
				});

				if (result.length > 0) {
					result[0].mappingTypeCd = responseMapping.mappingTypeCd;
					result[0].srcData = responseMapping.srcData;
				}
			});

			this.deployTargetSysGrid.records = intrfcdeploysysdtDtoRecords;
			this.refHistoryGrid.records = intrfcdeployhisthsDtoRecords.map(function (v) {
				v.id = v.deploySysCd + v.deployVersion;
				return v;
			});

			this.detail = data;

			if (data.trxDscd == 'BATCH' && data.intrfcWayCd == 'DBTODB') {
				this.detail.eaiDto.recvDbActionType = 'INSERT';
			}

			intrfcsrsysRecords.map(function (intrfcsrsys) {
				if (intrfcsrsys.srTypeCd === 'SEND') {
					_this12.setSysCd('SEND', intrfcsrsys.sysCd, intrfcsrsys.sysNm);
				} else {
					_this12.setSysCd('RECEIVE', intrfcsrsys.sysCd, intrfcsrsys.sysNm);
				}
			});

			this.changeIntrfcWay();

			this.$timeout(function () {
				w2ui[_this12.sndReqLayoutGrid.name].refresh();
				w2ui[_this12.sndResLayoutGrid.name].refresh();
				w2ui[_this12.rcvReqLayoutGrid.name].refresh();
				w2ui[_this12.rcvResLayoutGrid.name].refresh();
			});

			if (isCreateInterfaceId) this._createInterFaceId();
		}
	}, {
		key: 'setSysCd',
		value: function setSysCd(srTypeCd, sysCd, sysNm) {
			if (srTypeCd === 'SEND') {
				this.detail.sendSysCd = sysCd;
				this.detail.sendSysNm = sysNm;
			} else {
				this.detail.receiveSysCd = sysCd;
				this.detail.receiveSysNm = sysNm;
			}
		}
	}, {
		key: 'deleteInfrcId',
		value: function deleteInfrcId(id) {
			var _this13 = this;

			this.httpService.delete('/intrfccoms/' + id).then(function (data) {
				_this13.resetDetail();
				_this13.getIntrfccoms();
			});
		}
	}, {
		key: 'changePageSize',
		value: function changePageSize() {
			var pageSize = this.select.pageSize;
			this.inferfaceMainGrid.limit = pageSize;
			this.gridHeight = this.gridService.getGridHeight(this.select.pageSize);
			this.pageBtnClick(1);

			this.inferfaceMainGrid.name && w2ui[this.inferfaceMainGrid.name].focus();
		}
	}, {
		key: 'cancel',
		value: function cancel() {
			this.resetDetail();
			this.offEditMode();
		}
	}, {
		key: 'add',
		value: function add() {
			if (!_.isEmpty(this.inferfaceMainGrid.name)) {
				w2ui[this.inferfaceMainGrid.name].selectNone();
			}

			this.resetDetail();
			this.detail.eaiDto = {
				recvDbActionType: 'INSERT',
				dupFileProc: 'OVERWRITE',
				errSkipYn: 'N',
				searchProcCnt: '100'
			};
			this.setSysCd('SEND', '', '');
			this.setSysCd('RECEIVE', '', '');

			this.detail.workStatusCd = 'WORKING';
			this.detail.regManId = this.user.userId;
			this.isAdd = true;
			this._onEdit();
			this._openIntrfaceDetailZabara();
			this.setDefaultDeployTarget();
		}
	}, {
		key: 'setDefaultDeployTarget',
		value: function setDefaultDeployTarget() {
			var _this14 = this;

			this.httpService.post('/depolysyss/getlist', ['EAI', 'CBK']).then(function (data) {
				data.map(function (deploySys) {
					deploySys.deployUrl = deploySys.deploySysUrl;
					delete deploySys.deploySysUrl;
				});

				_this14.deployTargetSysGrid.records = data;
			});
		}
	}, {
		key: 'setCssDeployTarget',
		value: function setCssDeployTarget() {
			var _this15 = this;

			this.httpService.post('/depolysyss/getlist', ['EAI', 'CBK', 'CSS']).then(function (data) {
				data.map(function (deploySys) {
					deploySys.deployUrl = deploySys.deploySysUrl;
					delete deploySys.deploySysUrl;
				});

				_this15.deployTargetSysGrid.records = data;
			});
		}
	}, {
		key: '_openIntrfaceDetailZabara',
		value: function _openIntrfaceDetailZabara() {
			var $interfaceDetailZabara = $('#interfaceDetailInfoZabara');
			var isClose = $interfaceDetailZabara.css('transform') !== 'none';

			if (isClose) setTimeout(function () {
				return $('#interfaceDetailInfoZabara').click();
			});
		}
	}, {
		key: 'resetDetail',
		value: function resetDetail() {
			var offEditMode = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : true;

			this.detail = {};

			this.sndSysReqMsg = {};
			this.sndSysResMsg = {};
			this.rcvSysReqMsg = {};
			this.rcvSysResMsg = {};
			this.sendMsgMapSrcMap = {};
			this.recvMsgMapSrcMap = {};
			if (offEditMode) this.offEditMode();
			this.isAdd = false;

			this._selectedIntrfcmsglayoutdtDto = {};
			this.selectedIntrfcdeploysysdtDto = {};

			if (!_.isEmpty(this.interfaceDetailGrid.name)) {
				this.interfaceDetailGrid.records = [];
				this.sndReqLayoutGrid.records = [];
				this.sndResLayoutGrid.records = [];
				this.rcvReqLayoutGrid.records = [];
				this.rcvResLayoutGrid.records = [];
				this.sndMsgMapSrcGrid.records = [];
				this.sendMsgMapTrgGrid.records = [];
				this.rcvMsgMapSrcGrid.records = [];
				this.rcvMsgMapTrgGrid.records = [];
				this.deployTargetSysGrid.records = [];
				this.refHistoryGrid.records = [];
			}
		}
	}, {
		key: 'resetSearch',
		value: function resetSearch() {
			this.searchParam.intrfccoms = {};
			this.getIntrfccoms(true);
		}
	}, {
		key: 'blur',
		value: function blur($event) {
			$event.target.blur();
		}
	}, {
		key: 'addIntfcDetailRow',
		value: function addIntfcDetailRow() {
			if (_.isEmpty(this.detail.intrfcId)) {
				this.openAlert(this.text.addIntrfcIdMsg);
				return;
			}

			var grid = w2ui[this.interfaceDetailGrid.name];
			grid.add(this.intfcDto({ srTypeCd: 'RECEIVE' }));
		}
	}, {
		key: 'addSndReqLayoutRow',
		value: function addSndReqLayoutRow() {
			this.gridService.addRecord(this.sndReqLayoutGrid, this.sndReqDto);

			var grid = w2ui[this.sndReqLayoutGrid.name];
			grid.refresh();
		}
	}, {
		key: 'moveSndReqLayoutRow',
		value: function moveSndReqLayoutRow(action, optionName) {
			if (!this.isEdit) return;

			switch (action) {
				case 'up':
					this.gridService.upSelected(this[optionName]);
					this._reRenderMappingGrids(optionName);
					break;
				case 'down':
					this.gridService.downSelected(this[optionName]);
					this._reRenderMappingGrids(optionName);
					break;
			}

			if (optionName === 'sndReqLayoutGrid' || optionName === 'sndResLayoutGrid') {
				this.setIoCopy(true);
			}

			this.setGridSeq();
		}
	}, {
		key: '_reRenderMappingGrids',
		value: function _reRenderMappingGrids(optionName) {
			var grid = w2ui[this[optionName].name];
			var trgGridName = this._getTargetGridNameByOptionName(optionName);

			this[trgGridName].records = grid.records.map(function (v) {
				return v.msglayoutbsDto.msglayoutdtDto;
			}).reduce(function (v1, v2) {
				return v1.concat(v2);
			}, []);
		}
	}, {
		key: 'onClickIoCopy',
		value: function onClickIoCopy(optionName) {
			var _this16 = this;

			if (!this.isEdit) return;

			var grid = void 0,
			    mappingRecords = void 0;

			switch (optionName) {
				case 'sndReqLayoutGrid':
					grid = w2ui[this.sndReqLayoutGrid.name];
					grid.save();

					mappingRecords = [];

					this.rcvReqLayoutGrid.records = grid.records.map(function (record) {
						var clone = _this16.utilService.clone(record);
						clone.srTypeCd = 'RECEIVE';

						clone.msglayoutbsDto.msglayoutdtDto.map(function (v) {
							v.mappingTypeCd = 'NONE';
							mappingRecords.push(v);
						});

						return clone;
					});

					this.sendMsgMapTrgGrid.records = mappingRecords;

					break;
				case 'sndResLayoutGrid':
					grid = w2ui[this.sndResLayoutGrid.name];
					grid.save();

					mappingRecords = [];

					this.rcvResLayoutGrid.records = grid.records.map(function (record) {
						var _mappingRecords;

						var clone = _this16.utilService.clone(record);
						clone.srTypeCd = 'RECEIVE';

						(_mappingRecords = mappingRecords).push.apply(_mappingRecords, (0, _toConsumableArray3.default)(clone.msglayoutbsDto.msglayoutdtDto));

						return clone;
					});

					this.rcvMsgMapSrcGrid.records = mappingRecords;
					this.rcvMsgMapTrgGrid.records.map(function (v) {
						v.mappingTypeCd = 'NONE';
						v.srcData = '';
					});
					break;
				case 'sndReqToSndResLayoutGrid':
					grid = w2ui[this.sndReqLayoutGrid.name];
					grid.save();

					mappingRecords = [];

					this.sndResLayoutGrid.records = grid.records.map(function (record) {
						var clone = _this16.utilService.clone(record);
						clone.srTypeCd = 'SEND';
						clone.rqstRspsTypeCd = 'RESPONSE';

						clone.msglayoutbsDto.msglayoutdtDto.map(function (v) {
							v.mappingTypeCd = 'NONE';
							mappingRecords.push(v);
						});

						return clone;
					});

					this.rcvMsgMapTrgGrid.records = mappingRecords;
					break;
				case 'sndResToRcvResLayoutGrid':
					grid = w2ui[this.sndReqLayoutGrid.name];
					grid.save();

					mappingRecords = [];

					this.rcvResLayoutGrid.records = grid.records.map(function (record) {
						var _mappingRecords2;

						var clone = _this16.utilService.clone(record);
						clone.srTypeCd = 'RECEIVE';
						clone.rqstRspsTypeCd = 'RESPONSE';

						(_mappingRecords2 = mappingRecords).push.apply(_mappingRecords2, (0, _toConsumableArray3.default)(clone.msglayoutbsDto.msglayoutdtDto));

						return clone;
					});

					this.rcvMsgMapSrcGrid.records = mappingRecords;
					this.rcvMsgMapTrgGrid.records.map(function (v) {
						v.mappingTypeCd = 'NONE';
						v.srcData = '';
					});
					break;
			}
		}
	}, {
		key: 'refreshMappingGrid',
		value: function refreshMappingGrid() {
			if (this.detail.msgTrnsfrmYn === 'Y') {
				w2ui[this.sndMsgMapSrcGrid.name].refresh();
				w2ui[this.sendMsgMapTrgGrid.name].refresh();
				w2ui[this.rcvMsgMapSrcGrid.name].refresh();
				w2ui[this.rcvMsgMapTrgGrid.name].refresh();
			}
		}
	}, {
		key: 'setIoCopy',
		value: function setIoCopy(isNotGridSeq) {
			/*
   if(this.detail.msgTrnsfrmYn === 'N'){
   	w2ui[this.rcvReqLayoutGrid.name].hideColumn('delete');
   	w2ui[this.rcvResLayoutGrid.name].hideColumn('delete');
   	this.onClickIoCopy('sndReqLayoutGrid');
   	this.onClickIoCopy('sndResLayoutGrid');
   }else{			
   	// 송신시스템 요청전문 사용
   	if(this.useSndReqLayoutGrid) {
   		w2ui[this.rcvReqLayoutGrid.name].hideColumn('delete');
   		this.onClickIoCopy('sndReqLayoutGrid');
   	}else{
   		w2ui[this.rcvReqLayoutGrid.name].showColumn('delete');
   	}
   	
   	// 송신시스템 응답전문 사용
   	if(this.useSndResLayoutGrid) {
   		w2ui[this.rcvResLayoutGrid.name].hideColumn('delete');
   		this.onClickIoCopy('sndResLayoutGrid');
   	}else{
   		w2ui[this.rcvResLayoutGrid.name].showColumn('delete');
   	}
   }
   */
			// 요청전문 사용
			if (this.useSndReqToResLayoutGrid) {
				w2ui[this.sndResLayoutGrid.name].hideColumn('delete');
				this.onClickIoCopy('sndReqToSndResLayoutGrid');

				if (this.useSndResLayoutGrid || this.detail.msgTrnsfrmYn === 'N') {
					this.onClickIoCopy('sndResToRcvResLayoutGrid');
				}
			} else {
				w2ui[this.sndResLayoutGrid.name].showColumn('delete');
			}

			!isNotGridSeq && this.setGridSeq();
		}
	}, {
		key: 'onEditMode',
		value: function onEditMode() {
			if (_.isEmpty(this.detail)) return;
			this._onEdit();
		}
	}, {
		key: '_onEdit',
		value: function _onEdit() {
			var $forms = $('#searchWrap_detail,\n\t\t\t\t\t\t  #searchWrap_dbDesc,\n\t\t\t\t\t\t  #searchWrap_timeout_online,\n\t\t\t\t\t\t  #searchWrap_timeout_batch, \n\t\t\t\t\t\t  #searchWrap_timeout,\n\t\t\t\t\t\t  #searchWrap_rcvReqMsg, \n\t\t\t\t\t\t  #searchWrap_rcvResMsg,\n\t\t\t\t\t\t  #searchWrap_sndFileDesc,\n\t\t\t\t\t\t  #searchWrap_recvFileDesc').find('div:not(.disabled)').find('input,select');
			var $textareas = $('#searchWrap_dbDesc').find('div:not(.disabled)').find('textarea');

			this.isEdit = true;

			$forms.attr('disabled', false);
			$textareas.attr('readonly', false);

			this.editIntrfcDetailGrid();

			w2ui[this.sndReqLayoutGrid.name].showColumn('delete');
			w2ui[this.sndResLayoutGrid.name].showColumn('delete');

			if (this.detail.msgTrnsfrmYn !== 'N') {
				w2ui[this.rcvReqLayoutGrid.name].showColumn('delete');
				w2ui[this.rcvResLayoutGrid.name].showColumn('delete');
			} else {
				//w2ui[this.rcvReqLayoutGrid.name].hideColumn('delete');
				//w2ui[this.rcvResLayoutGrid.name].hideColumn('delete');
				w2ui[this.rcvReqLayoutGrid.name].showColumn('delete');
				w2ui[this.rcvResLayoutGrid.name].showColumn('delete');
			}
		}
	}, {
		key: 'offEditMode',
		value: function offEditMode() {
			var $forms = $('#searchWrap_detail,\n\t\t\t\t\t\t  #searchWrap_dbDesc,\n\t\t\t\t\t\t  #searchWrap_timeout_online,\n\t\t\t\t\t\t  #searchWrap_timeout_batch, \n\t\t\t\t\t\t  #searchWrap_timeout,\n\t\t\t\t\t\t  #searchWrap_rcvReqMsg, \n\t\t\t\t\t\t  #searchWrap_rcvResMsg,\n\t\t\t\t\t\t  #searchWrap_sndFileDesc,\n\t\t\t\t\t\t  #searchWrap_recvFileDesc').find('input,select');
			var $textareas = $('#searchWrap_dbDesc').find('textarea');

			this.isEdit = false;
			this.isAdd = false;

			$forms.attr('disabled', true);
			$textareas.attr('readonly', true);

			w2ui[this.sndReqLayoutGrid.name].hideColumn('delete');
			w2ui[this.sndResLayoutGrid.name].hideColumn('delete');
			w2ui[this.rcvReqLayoutGrid.name].hideColumn('delete');
			w2ui[this.rcvResLayoutGrid.name].hideColumn('delete');
			w2ui[this.deployTargetSysGrid.name].hideColumn('edit');
		}
	}, {
		key: '_openAllZabara',
		value: function _openAllZabara() {
			var $interfaceDetailZabara = $('#interfaceDetailInfoZabara');
			var $sndSysMsgInfoZabara = $('#sndSysMsgInfoZabara');
			var $rcvSysMsgInfoZabara = $('#rcvSysMsgInfoZabara');
			var $msgMappingZabara = $('#msgMappingZabara');

			var isClose1 = $interfaceDetailZabara.css('transform') !== 'none';
			var isClose2 = $sndSysMsgInfoZabara.css('transform') !== 'none';
			var isClose3 = $rcvSysMsgInfoZabara.css('transform') !== 'none';
			var isClose4 = $msgMappingZabara.css('transform') !== 'none';

			if (isClose1) setTimeout(function () {
				return $('#interfaceDetailInfoZabara').click();
			});
			if (isClose2) setTimeout(function () {
				return $('#sndSysMsgInfoZabara').click();
			});
			if (isClose3) setTimeout(function () {
				return $('#rcvSysMsgInfoZabara').click();
			});
			if (isClose4) setTimeout(function () {
				return $('#msgMappingZabara').click();
			});
		}
	}, {
		key: '_closeAllZabara',
		value: function _closeAllZabara() {
			var $sndSysMsgInfoZabara = $('#sndSysMsgInfoZabara');
			var $rcvSysMsgInfoZabara = $('#rcvSysMsgInfoZabara');
			var $msgMappingZabara = $('#msgMappingZabara');

			var isClose2 = $sndSysMsgInfoZabara.css('transform') !== 'none';
			var isClose3 = $rcvSysMsgInfoZabara.css('transform') !== 'none';
			var isClose4 = $msgMappingZabara.css('transform') !== 'none';

			if (!isClose2) setTimeout(function () {
				return $('#sndSysMsgInfoZabara').click();
			});
			if (!isClose3) setTimeout(function () {
				return $('#rcvSysMsgInfoZabara').click();
			});
			if (!isClose4) setTimeout(function () {
				return $('#msgMappingZabara').click();
			});
		}
	}, {
		key: 'openImportXlsxPopup',
		value: function openImportXlsxPopup() {
			this.popupService.openModal('SCR0703', { intrfcTypeCd: this.intrfcTypeCd }).then(function (res) {}).catch(function () {});
		}
	}, {
		key: 'openImportXlsxPopup2',
		value: function openImportXlsxPopup2() {
			this.popupService.openModal('SCR0703', { intrfcTypeCd: this.intrfcTypeCd, definition: true }).then(function (res) {}).catch(function () {});
		}
	}, {
		key: 'openAlert',
		value: function openAlert(alertBody) {
			this.popupService.simpleAlert(this.$scope, alertBody);
		}
	}, {
		key: 'pageBtnClick',
		value: function pageBtnClick(num) {
			this.pageNumber = num;
			this.getIntrfccoms(num === 1);
		}
	}, {
		key: 'onClickZabara',
		value: function onClickZabara() {
			for (var _len = arguments.length, optionNames = Array(_len), _key = 0; _key < _len; _key++) {
				optionNames[_key] = arguments[_key];
			}

			if (optionNames[0] === 'sndMsgMapSrcGrid') {
				this.gridService.onClickZabara(this, optionNames, this.setGridSeq.bind(this));
			} else {
				this.gridService.onClickZabara(this, optionNames);
			}
		}
	}, {
		key: 'mapping',
		value: function mapping(type) {
			if (!this.isEdit) return;
			var isRequest = type === 'REQUEST';
			var trgOption = isRequest ? this.sendMsgMapTrgGrid : this.rcvMsgMapTrgGrid;
			var srcOption = isRequest ? this.sndMsgMapSrcGrid : this.rcvMsgMapSrcGrid;
			var trgGrid = w2ui[trgOption.name];
			trgGrid.save();

			if (trgGrid.records.length === 0) return;

			this._removeMappingData(trgGrid);

			var srcFldUnqIds = w2ui[srcOption.name].records.map(function (v) {
				return v.fldUnqId;
			});
			var srcMsgLayoutIds = w2ui[srcOption.name].records.map(function (v) {
				return v.msgLayoutId;
			});
			var trgMsgLayoutIds = trgGrid.records.map(function (v) {
				return v.msgLayoutId;
			}).reduce(function (v1, v2) {
				if (v1.indexOf(v2) < 0) v1.push(v2);
				return v1;
			}, []);

			srcFldUnqIds.map(function (v, idx) {
				var msgIndex = srcMsgLayoutIds[idx].length;
				var field = v.slice(msgIndex);

				var _loop = function _loop(i) {
					var trgFldUnqId = trgMsgLayoutIds[i].concat(field);
					var trgRecord = trgGrid.records.filter(function (trgV) {
						return trgV.fldUnqId === trgFldUnqId;
					})[0];

					if (!_.isEmpty(trgRecord)) {
						trgRecord.mappingTypeCd = 'PROPT';
						trgRecord.srcData = v;
					}
				};

				for (var i = 0; i < trgMsgLayoutIds.length; i++) {
					_loop(i);
				}
			});

			trgGrid.records.map(function (v) {
				if (v.mappingTypeCd !== 'PROPT') {
					v.mappingTypeCd = "NONE";
					v.srcData = "";
				}
			});

			this.mappingConfirm(type, true);
		}
	}, {
		key: 'mappingConfirm',
		value: function mappingConfirm(type, isMapping) {
			var _this17 = this;

			if (!this.isEdit) return;
			var isRequest = type === 'REQUEST';
			var gridName = isRequest ? this.sendMsgMapTrgGrid.name : this.rcvMsgMapTrgGrid.name;
			var diffRcords = isRequest ? this._getDiffRecords(this.sndMsgMapSrcGrid, this.sendMsgMapTrgGrid) : this._getDiffRecords(this.rcvMsgMapSrcGrid, this.rcvMsgMapTrgGrid);

			setTimeout(function () {
				diffRcords.map(function (v) {
					var changeDot = v.fldUnqId.replace(/\./g, '\\.');
					var $tr = $('tr#grid_' + gridName + '_rec_' + changeDot);

					$tr.addClass('bg-red');
				});

				var txt = isMapping ? _this17.text.completeMapping : _this17.text.completeMappingConfirm;
				var cnt = diffRcords.length;
				cnt !== 0 && (txt += '<br> ' + _this17.text.confirmError + ' <span class="chr-c-orange">' + cnt + '</span>' + _this17.text.cnt);

				_this17.popupService.simpleAlert(_this17.$scope, txt);
			}, 300);
		}
	}, {
		key: '_getDiffRecords',
		value: function _getDiffRecords(srcOption, trgOption) {
			var srcGrid = w2ui[srcOption.name];
			var trgGrid = w2ui[trgOption.name];

			trgGrid.save();
			trgGrid.selectNone();

			return trgOption.records.filter(function (v) {
				return v.mappingTypeCd === 'PROPT';
			}).filter(function (v) {
				var srcRecord = srcGrid.records.filter(function (srcV) {
					return srcV.fldUnqId === v.srcData;
				})[0];
				return !(srcRecord.dataTypeNm === v.dataTypeNm && srcRecord.msgLen === v.msgLen);
			});
		}
	}, {
		key: '_removeMappingData',
		value: function _removeMappingData(grid) {
			var trgMsgLayoutId = grid.records[0].msgLayoutId;

			var $tr = $('tr[id^="grid_' + grid.name + '_rec_' + trgMsgLayoutId + '"');
			$tr.removeClass('bg-red');

			grid.records.map(function (v) {
				v.mappingTypeCd = 'NONE';
				v.srcData = '';
			});
		}
	}, {
		key: 'searchApplicationCodes',
		value: function searchApplicationCodes() {
			var _this18 = this;

			this.popupService.openModal('SCR1402', { limitLvCd: 0 }).then(function (code) {
				_this18.searchParam.intrfccoms.lv1Cd = code.appCd;
			}).catch(function () {});
		}
	}, {
		key: 'openRegManPopup',
		value: function openRegManPopup() {
			var _this19 = this;

			this.popupService.openModal('SCR0102').then(function (user) {
				return _this19.searchParam.intrfccoms.regManId = user.userId;
			}).catch(function () {});
		}
	}, {
		key: 'openSysCdSPopup',
		value: function openSysCdSPopup() {
			var _this20 = this;

			this.popupService.openModal('SCR1302').then(function (sys) {
				return _this20.searchParam.intrfccoms.sysCdS = sys.sysCd;
			}).catch(function () {});
		}
	}, {
		key: 'openSysCdRPopup',
		value: function openSysCdRPopup() {
			var _this21 = this;

			this.popupService.openModal('SCR1302').then(function (sys) {
				return _this21.searchParam.intrfccoms.sysCdR = sys.sysCd;
			}).catch(function () {});
		}
	}, {
		key: 'openInterfaceIdCreatePopup',
		value: function openInterfaceIdCreatePopup() {
			var _this22 = this;

			this.popupService.openModal('SCR0702', { intrfcTypeCd: this.intrfcTypeCd }).then(function (intrfc) {
				var sndSys = intrfc.sndSys,
				    rcvSys = intrfc.rcvSys;

				var detail = _this22.detail;

				delete intrfc.sndSys;
				delete intrfc.rcvSys;

				detail.intrfcId = intrfc.intrfcId;
				detail.lv1Cd = intrfc.lv1Cd;
				detail.syncAsyncDscd = intrfc.syncAsyncDscd;
				detail.trxDscd = intrfc.trxDscd;

				_this22.editIntrfcDetailGrid();

				_this22.interfaceDetailGrid.records = [_this22.intfcDto({
					intrfcId: intrfc.intrfcId,
					srTypeCd: 'SEND',
					sysCd: sndSys.sysCd,
					sysNm: sndSys.sysNm,
					msglayoutbsDto: []
				}), _this22.intfcDto({
					intrfcId: intrfc.intrfcId,
					srTypeCd: 'RECEIVE',
					sysCd: rcvSys.sysCd,
					sysNm: rcvSys.sysNm,
					msglayoutbsDto: []
				})];

				_this22.setSysCd('SEND', sndSys.sysCd, sndSys.sysNm);
				_this22.setSysCd('RECEIVE', rcvSys.sysCd, rcvSys.sysNm);

				_this22.changeIntrfcWay();
				_this22._changeDetailColumnByTrxDscd();

				if (sndSys.sysCd == 'CSS' || rcvSys.sysCd == 'CSS') {
					_this22.setCssDeployTarget();
				} else {
					_this22.setDefaultDeployTarget();
				}
			}).catch(function () {});
		}
	}, {
		key: 'openDeploySystemPopup',
		value: function openDeploySystemPopup() {
			var _this23 = this;

			if (!this.isEdit) return;
			var grid = w2ui[this.deployTargetSysGrid.name];

			this.popupService.openModal('SCR1202', { deploySysGrpCd: this.intrfcTypeCd, codes: { 'DEPLOY_SYS_DSCD': this.codes['DEPLOY_SYS_DSCD'] } }).then(function (deploySys) {
				if (!_.isEmpty(grid.get(deploySys.deploySysCd))) {
					_this23.openAlert(_this23.text.duplicateDeployTarget);
					return;
				}

				deploySys.deployUrl = deploySys.deploySysUrl;
				delete deploySys.deploySysUrl;
				grid.records.push(deploySys);
			}).catch(function () {});
		}
	}, {
		key: 'addMsgLayoutToSndRcvMsg',
		value: function addMsgLayoutToSndRcvMsg(optionName) {
			var _this24 = this;

			if (!this.isEdit) return;
			var grid = w2ui[this[optionName].name];

			var _getMsgWrapType2 = this._getMsgWrapType(optionName),
			    srTypeCd = _getMsgWrapType2.srTypeCd,
			    rqstRspsTypeCd = _getMsgWrapType2.rqstRspsTypeCd;

			this.popupService.openModal('SCR0502', {
				getDetail: true, codes: this.codes,
				trxDscdFilter: true, trxDscd: this.detail.trxDscd
			}).then(function (msglayoutbsDtoList) {
				msglayoutbsDtoList.map(function (msglayoutbsDto) {
					if (!_.isEmpty(grid.get(msglayoutbsDto.msgLayoutId))) {
						_this24.openAlert(_this24.text.duplicateMsg);
						return;
					}

					grid.add({
						intrfcId: _this24.detail.intrfcId,
						srTypeCd: srTypeCd,
						rqstRspsTypeCd: rqstRspsTypeCd,
						msgLayoutId: msglayoutbsDto.msgLayoutId,
						msglayoutbsDto: msglayoutbsDto
					});

					var targetGrid = _this24._getTargetGridByOptionName(optionName);

					msglayoutbsDto.msglayoutdtDto.map(function (dto, index) {
						dto.mappingTypeCd = 'NONE';
					});

					if (!_.isEmpty(targetGrid)) {
						targetGrid.add(msglayoutbsDto.msglayoutdtDto, false);
						targetGrid.refresh();
					}

					if (optionName === 'sndReqLayoutGrid' || optionName === 'sndResLayoutGrid') {
						_this24.setIoCopy(true);
					}
				});

				_this24.setGridSeq();
			}).catch(function () {});
		}
	}, {
		key: 'setGridSeq',
		value: function setGridSeq() {
			var _this25 = this;

			this.sendMsgMapSrcMap = {};

			this.sndMsgMapSrcGrid.records.map(function (record, index) {
				record.gridSeq = index + 1;
				_this25.sendMsgMapSrcMap[record.fldUnqId] = '[' + record.gridSeq + '] ' + record.fldUnqId;
			});

			this.sendMsgMapTrgSrcData.render = function (data) {
				if (data.mappingTypeCd === 'PROPT') {
					return _this25.sendMsgMapSrcMap[data.srcData];
				} else {
					return data.srcData;
				}
			};

			this.recvMsgMapSrcMap = {};

			this.rcvMsgMapSrcGrid.records.map(function (record, index) {
				record.gridSeq = index + 1;
				_this25.recvMsgMapSrcMap[record.fldUnqId] = '[' + record.gridSeq + '] ' + record.fldUnqId;
			});

			this.recvMsgMapTrgSrcData.render = function (data) {
				if (data.mappingTypeCd === 'PROPT') {
					return _this25.recvMsgMapSrcMap[data.srcData];
				} else {
					return data.srcData;
				}
			};
		}
	}, {
		key: '_getMsgWrapType',
		value: function _getMsgWrapType(optionName) {
			var result = {};

			switch (optionName) {
				case 'sndReqLayoutGrid':
					result.srTypeCd = 'SEND';
					result.rqstRspsTypeCd = 'REQUEST';
					break;
				case 'sndResLayoutGrid':
					result.srTypeCd = 'SEND';
					result.rqstRspsTypeCd = 'RESPONSE';
					break;
				case 'rcvReqLayoutGrid':
					result.srTypeCd = 'RECEIVE';
					result.rqstRspsTypeCd = 'REQUEST';
					break;
				case 'rcvResLayoutGrid':
					result.srTypeCd = 'RECEIVE';
					result.rqstRspsTypeCd = 'RESPONSE';
					break;
			}

			return result;
		}
	}, {
		key: '_getTargetGridByOptionName',
		value: function _getTargetGridByOptionName(optionName) {
			var grid = null;

			switch (optionName) {
				case 'sndReqLayoutGrid':
					grid = w2ui[this.sndMsgMapSrcGrid.name];
					break;
				case 'sndResLayoutGrid':
					grid = w2ui[this.rcvMsgMapTrgGrid.name];
					break;
				case 'rcvResLayoutGrid':
					grid = w2ui[this.rcvMsgMapSrcGrid.name];
					break;
				case 'rcvReqLayoutGrid':
					grid = w2ui[this.sendMsgMapTrgGrid.name];
					break;
			}

			return grid;
		}
	}, {
		key: '_getTargetGridNameByOptionName',
		value: function _getTargetGridNameByOptionName(optionName) {
			var gridName = null;

			switch (optionName) {
				case 'sndReqLayoutGrid':
					gridName = 'sndMsgMapSrcGrid';
					break;
				case 'sndResLayoutGrid':
					gridName = 'rcvMsgMapTrgGrid';
					break;
				case 'rcvResLayoutGrid':
					gridName = 'rcvMsgMapSrcGrid';
					break;
				case 'rcvReqLayoutGrid':
					gridName = 'sendMsgMapTrgGrid';
					break;
			}

			return gridName;
		}
	}, {
		key: 'deployInterface',
		value: function deployInterface(event) {
			var _this26 = this;

			event.stopPropagation();

			if (this.detail.msgLayoutTranYn === 'Y' && this.detail.msgTrnsfrmYn === 'Y' && this.compulsionDeployYn === false) {
				this.openAlert(this.text.confirmChangedMsg2);
				return;
			}

			if (this.isEdit) {
				this.openAlert(this.text.deployAfterSave);
				return;
			}

			var deployGridRecords = w2ui[this.deployTargetSysGrid.name].records;

			if (_.isEmpty(deployGridRecords)) {
				this.openAlert(this.text.emptyDeploySystem);
				return;
			}

			if (_.isEmpty(this.detail)) {
				this.openAlert(this.text.emptyDeployTarget);
				return;
			}
			this.popupService.simpleConfirm(this.$scope, this.text.confirmDeploy, function () {
				return _this26._deployInterface();
			});
		}
	}, {
		key: 'isRowMetaValid',
		value: function isRowMetaValid(row) {
			var meta = this.metaService.getMetaByMetaEngNm(row.fldEngNm);
			return meta && meta.metaKorNm == row.fldKorNm
			//		&& meta.metaLen == row.msgLen
			//		&& meta.decimalLen == row.decimalLen
			//		&& meta.dataTypeNm == row.dataTypeNm
			? true : false;
		}
	}, {
		key: '_deployInterface',
		value: function _deployInterface() {
			var _this27 = this;

			this.popupService.showLoadingBar(this.$scope);
			var param = this.utilService.clone(this.detail);
			param.compulsionDeployYn = this.compulsionDeployYn === true ? 'Y' : 'N';

			this.httpService.post('/intrfccoms/deploy', param).then(function (res) {
				if (res.isError) {
					_this27.popupService.detailAlert(_this27.$scope, res.data.message, res.data.stackTrace, res.data.parameters);
					return;
				}

				_this27.setDeployResult(res.intrfcDeployResponse);

				var url = '/intrfccoms/deployhistorys?intrfcId=' + _this27.detail.intrfcId;
				_this27.httpService.get(url).then(function (data) {
					_this27.refHistoryGrid.records = data.intrfcdeployhisthsOutList.map(function (v) {
						v.id = v.deploySysCd + v.deployVersion;
						return v;
					});
				});

				var filterResponse = res.intrfcDeployResponse.filter(function (v) {
					return v.deployStatus;
				});
				var successCnt = 0;
				var failCnt = 0;

				filterResponse.map(function (data) {
					if (data.deployStatus === 'SUCCESS') {
						successCnt++;
					} else if (data.deployStatus === 'FAIL') {
						failCnt++;
					}
				});

				_this27.popupService.closeLoadingBar();

				var alertText = _this27.text.completeDeploy;

				if (successCnt !== 0 && filterResponse.length === successCnt) {
					alertText += '<br/><span class="chr-c-green">' + _this27.text.successDeploy + '</span>';
				} else if (failCnt !== 0) {
					alertText += '<br/><span class="chr-c-orange">' + _this27.text.failDeploy + '</span>';
				}

				_this27.openAlert(alertText);
			}).finally(function () {
				_this27.popupService.closeLoadingBar();
			});
		}
	}, {
		key: 'setDeployResult',
		value: function setDeployResult(intrfcDeployResponseList) {
			var grid = w2ui[this.deployTargetSysGrid.name];

			grid.records.map(function (record) {
				for (var i = 0; i < intrfcDeployResponseList.length; i++) {
					var res = intrfcDeployResponseList[i];

					if (record.deploySysCd == res.systemCd) {
						record.deployResultCd = res.deployStatus;
						record.resultMessage = res.message;
						break;
					}
				}
			});

			if (this.$scope.$$phase !== '$apply' && this.$scope.$$phase !== '$digest') this.$scope.$apply();
		}
	}, {
		key: 'redeployInterface',
		value: function redeployInterface(intrfcdeployhist) {
			var _this28 = this;

			this.popupService.showLoadingBar(this.$scope);

			var intrfcdeployhistClone = this.utilService.clone(intrfcdeployhist);

			this.httpService.post('/intrfccoms/redeploy', intrfcdeployhistClone).then(function (res) {
				_this28.popupService.closeLoadingBar();

				if (res.isError) {
					_this28.popupService.detailAlert(_this28.$scope, res.data.message, res.data.stackTrace);
					return;
				}

				var url = '/intrfccoms/deployhistorys?intrfcId=' + _this28.detail.intrfcId;
				_this28.httpService.get(url).then(function (data) {
					_this28.refHistoryGrid.records = data.intrfcdeployhisthsOutList.map(function (v) {
						v.id = v.deploySysCd + v.deployVersion;
						return v;
					});
				});

				_this28.popupService.closeLoadingBar();
				_this28.openAlert(_this28.text.completeRedeploy);
			}).finally(function () {
				_this28.popupService.closeLoadingBar();
			});
		}
	}, {
		key: 'save',
		value: function save() {
			var _detail$intrfcmsglayo,
			    _detail$intrfcmsglayo2,
			    _detail$intrfcmsglayo3,
			    _detail$intrfcmsglayo4,
			    _this29 = this;

			var detail = this.detail;
			var _w2ui = w2ui;
			var interfaceDetailGrid = _w2ui[this.interfaceDetailGrid.name];
			var sndReqLayoutGridRecords = _w2ui[this.sndReqLayoutGrid.name].records;
			var sndResLayoutGridRecords = _w2ui[this.sndResLayoutGrid.name].records;
			var rcvReqLayoutGridRecords = _w2ui[this.rcvReqLayoutGrid.name].records;
			var rcvResLayoutGridRecords = _w2ui[this.rcvResLayoutGrid.name].records;
			var sendMsgMapTrgGridRecords = _w2ui[this.sendMsgMapTrgGrid.name].records;
			var rcvMsgMapTrgGridRecords = _w2ui[this.rcvMsgMapTrgGrid.name].records;
			var deployTargetSysGridRecords = _w2ui[this.deployTargetSysGrid.name].records;
			var receiveSrSeq = 1;
			var sendSrSeq = 1;
			var reqDtoMsgId = null;
			var resDtoMsgId = null;
			var reqDtoTargetDataSeq = 1;
			var resDtoTargetDataSeq = 1;

			interfaceDetailGrid.save();

			if (!this._checkValid()) return;

			this.popupService.showLoadingBar(this.$scope);

			var receiveSysCd = void 0,
			    sendSysCd = void 0;

			detail.intrfcTypeCd = this.intrfcTypeCd;
			detail.intrfcsrsysdtDto = interfaceDetailGrid.records.map(function (v) {
				v.intrfcId = detail.intrfcId;
				if (v.srTypeCd === 'RECEIVE') {
					v.srSeq = receiveSrSeq++;
					receiveSysCd = v.sysCd;
				} else if (v.srTypeCd === 'SEND') {
					v.srSeq = sendSrSeq++;
					sendSysCd = v.sysCd;
				}
				return v;
			});

			detail.intrfcmsglayoutdtDto = [];
			(_detail$intrfcmsglayo = detail.intrfcmsglayoutdtDto).push.apply(_detail$intrfcmsglayo, (0, _toConsumableArray3.default)(sndReqLayoutGridRecords.map(function (v, index) {
				v.sysCd = sendSysCd;
				v.intrfcId = detail.intrfcId;
				v.srSeq = 1;
				v.rqstRspsSeq = index + 1;
				return v;
			})));
			(_detail$intrfcmsglayo2 = detail.intrfcmsglayoutdtDto).push.apply(_detail$intrfcmsglayo2, (0, _toConsumableArray3.default)(sndResLayoutGridRecords.map(function (v, index) {
				v.sysCd = sendSysCd;
				v.intrfcId = detail.intrfcId;
				v.srSeq = 1;
				v.rqstRspsSeq = index + 1;
				return v;
			})));
			(_detail$intrfcmsglayo3 = detail.intrfcmsglayoutdtDto).push.apply(_detail$intrfcmsglayo3, (0, _toConsumableArray3.default)(rcvReqLayoutGridRecords.map(function (v, index) {
				v.sysCd = receiveSysCd;
				v.intrfcId = detail.intrfcId;
				v.srSeq = 1;
				v.rqstRspsSeq = index + 1;
				return v;
			})));
			(_detail$intrfcmsglayo4 = detail.intrfcmsglayoutdtDto).push.apply(_detail$intrfcmsglayo4, (0, _toConsumableArray3.default)(rcvResLayoutGridRecords.map(function (v, index) {
				v.sysCd = receiveSysCd;
				v.intrfcId = detail.intrfcId;
				v.srSeq = 1;
				v.rqstRspsSeq = index + 1;
				return v;
			})));

			detail.intrfccombsMappingReqDto = sendMsgMapTrgGridRecords.map(function (v, index) {
				if (reqDtoMsgId != v.msgLayoutId) {
					reqDtoMsgId = v.msgLayoutId;
					reqDtoTargetDataSeq = 1;
				}

				return {
					mappingTypeCd: v.mappingTypeCd,
					reqResTypeCd: 'REQUEST',
					srcData: v.srcData,
					targetData: v.fldUnqId,
					mappingSeq: index + 1,
					targetDataSeq: reqDtoTargetDataSeq++
				};
			});

			detail.intrfccombsMappingResDto = rcvMsgMapTrgGridRecords.map(function (v, index) {
				if (resDtoMsgId != v.msgLayoutId) {
					resDtoMsgId = v.msgLayoutId;
					resDtoTargetDataSeq = 1;
				}

				return {
					mappingTypeCd: v.mappingTypeCd,
					reqResTypeCd: 'RESPONSE',
					srcData: v.srcData,
					targetData: v.fldUnqId,
					mappingSeq: index + 1,
					targetDataSeq: resDtoTargetDataSeq++
				};
			});

			detail.intrfcdeploysysdtDto = deployTargetSysGridRecords.map(function (v, index) {
				v.intrfcId = detail.intrfcId;
				v.deploySysSeq = index + 1;
				return v;
			});

			var requestPromise = this.isAdd ? this.httpService.post('/intrfccoms', detail) : this.httpService.put('/intrfccoms', detail);

			requestPromise.then(function (res) {
				_this29.popupService.closeLoadingBar();

				if (res.isError) {
					_this29.popupService.detailAlert(_this29.$scope, res.data.message, res.data.stackTrace, res.data.parameters);
					return;
				}

				_this29.getIntrfccoms();
				if (_this29.isAdd) {
					_this29.setIntrfGridData(res.intrfcId);
				} else {
					_this29.setIntrfGridData(detail.intrfcId);
				}

				_this29.openAlert(bxMsg('common.saved'));
				_this29.offEditMode();

				// 생성 수정시, msgLayoutTranYn를 N으로 변경 
				_this29.detail.msgLayoutTranYn = 'N';
			});

			requestPromise.finally(function () {
				_this29.popupService.closeLoadingBar();
			});
		}
	}, {
		key: 'checkLang',
		value: function checkLang(name) {
			var result = false;
			for (var i = 0; i < name.length; i++) {
				var c = name.charCodeAt(i);

				if (0xAC00 <= c && c <= 0xD8A3 || 0x3131 <= c && c <= 0X318E) {
					// 한글
					result = false;
					break;
				} else if (c >= 0x4E00 && c <= 0x9FBF || c >= 0x3400 && c <= 0x4DBF || c >= 0x20000 && c <= 0x2A6DF || c >= 0x2A700 && c <= 0x2A6DF || c >= 0x2B740 && c <= 0x2B8AF || c >= 0x2CEB0 && c <= 0x2EBEF || c >= 0x2E80 && c <= 0x2EFF || c >= 0x2F800 && c <= 0x2FA1F) {
					// 중문
					result = false;
					break;
				} else {
					result = true;
				}
			}

			return result;
		}
	}, {
		key: '_checkValid',
		value: function _checkValid() {
			var intrfccoms = this.detail;
			var interfaceDetailGrid = w2ui[this.interfaceDetailGrid.name];
			var sendMsgMapTrgGridRecords = w2ui[this.sendMsgMapTrgGrid.name];
			var rcvMsgMapTrgGridRecords = w2ui[this.rcvMsgMapTrgGrid.name];

			if (_.isEmpty(intrfccoms.intrfcId)) {
				this.openAlert(this.text.addIntrfcIdMsg);
				return false;
			} else if (_.isEmpty(intrfccoms.intrfcNm)) {
				this.openAlert(this.text.emptyInterfaceName);
				return false;
			} else if (intrfccoms.trxDscd == 'ONLINE' && _.isEmpty(intrfccoms.syncAsyncDscd)) {
				this.openAlert(this.text.emptySyncAsyncDscd);
				return false;
			} else if (_.isEmpty(intrfccoms.rspsYn) && intrfccoms.intrfcWayCd == 'APTOAP') {
				this.openAlert(this.text.emptyRspsYn);
				return false;
			} else if (_.isEmpty(intrfccoms.intrfcWayCd)) {
				this.openAlert(this.text.emptyIntrfcWayCd);
				return false;
			} else if (_.isEmpty(intrfccoms.workStatusCd)) {
				this.openAlert(this.text.emptyWorkStatusCd);
				return false;
			} else if (_.isEmpty(intrfccoms.msgTrnsfrmYn) && intrfccoms.intrfcWayCd == 'APTOAP') {
				this.openAlert(this.text.emptyMsgTrnsfrmYn);
				return false;
			} else if (this.detail.eaiDto.searchProcCnt == "" && intrfccoms.intrfcWayCd == 'DBTODB') {
				this.openAlert(this.text.emptySearchProcCnt);
				return false;
			} else if (_.isEmpty(this.detail.eaiDto.errSkipYn) && intrfccoms.intrfcWayCd == 'DBTODB') {
				this.openAlert(this.text.emptyErrSkipYn);
				return false;
			}

			if (!this.checkLang(intrfccoms.intrfcNm)) {
				this.openAlert(this.text.intrfcNmEng);
				return false;
			}

			var interfaceDetailGridLength = interfaceDetailGrid.records.length;
			for (var i = 0; i < interfaceDetailGridLength; i++) {
				var system = interfaceDetailGrid.records[i];

				if (_.isEmpty(system.crgManNm)) {
					this.openAlert(this.text.receiveSendSys + system.sysCd + this.text.emptyCrgManNm);
					return false;
				} else if (intrfccoms.trxDscd === 'ONLINE' && intrfccoms.intrfcWayCd == 'APTOAP' && system.srTypeCd == 'RECEIVE' && _.isEmpty(system.trxCd)) {
					//	this.openAlert(this.text.receiveSendSys + system.sysCd + this.text.emptyTrxCd);
					//	return false;
				}
			}
			if (intrfccoms.msgTrnsfrmYn == 'Y') {
				for (var _i = 0; _i < sendMsgMapTrgGridRecords.records.length; _i++) {
					var senmdMappingGrid = sendMsgMapTrgGridRecords.records[_i];

					if (senmdMappingGrid.dataTypeNm == 'LAYOUT') {
						if (senmdMappingGrid.mappingTypeCd == 'PROPT' && senmdMappingGrid.srcData != '') {
							continue;
						} else {
							this.openAlert(this.text.sendMappingLayout);
							return false;
						}
					}
				}

				for (var _i2 = 0; _i2 < rcvMsgMapTrgGridRecords.records.length; _i2++) {
					var revdMappingGrid = rcvMsgMapTrgGridRecords.records[_i2];

					if (revdMappingGrid.dataTypeNm == 'LAYOUT') {
						if (revdMappingGrid.mappingTypeCd == 'PROPT' && revdMappingGrid.srcData != '') {
							continue;
						} else {
							this.openAlert(this.text.revMappingLayout);
							return false;
						}
					}
				}
			}

			// BATCH - DBTODB
			if (intrfccoms.trxDscd == 'BATCH' && intrfccoms.intrfcWayCd == 'DBTODB') {
				if (_.isEmpty(intrfccoms.eaiDto.recvDbActionType)) {
					this.openAlert(this.text.emptyRecvDbActionType);
					return false;
				}

				if (_.isEmpty(intrfccoms.eaiDto.scheduleProcInterval)) {
					this.openAlert(this.text.emptyScheduleProcInterval);
					return false;
				}

				if ((0, _isNan2.default)(Number(intrfccoms.eaiDto.scheduleProcInterval))) {
					this.openAlert(this.text.typeScheduleProcInterval);
					return false;
				}
			}

			if (intrfccoms.trxDscd == 'BATCH' && intrfccoms.intrfcWayCd == 'FILETOFILE') {
				if (_.isEmpty(intrfccoms.eaiDto.dupFileProc)) {
					this.openAlert(this.text.emptyDupProc);
					return false;
				}
			}

			return true;
		}
	}, {
		key: 'excelExport',
		value: function excelExport(id, multiDownload, isFirstFile, isLastFile) {
			var _this30 = this;

			var data = { 'intrfcId': id };

			if (multiDownload) {
				isFirstFile && this.popupService.showLoadingBar(this.$scope);
			} else {
				this.popupService.showLoadingBar(this.$scope);
			}

			this.httpService.downloadFile('/intrfccoms/excelexport', data).then(function (res) {
				if (res.isError) {
					_this30.popupService.detailAlert(_this30.$scope, res.data.message, res.data.stackTrace);
				} else {
					var header = decodeURIComponent(res.headers('Content-Disposition'));
					var fileName = header.split("=")[1].replace(/\"/gi, '');
					console.log(fileName);

					var blob = new Blob([res.data], { type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" });
					_this30.utilService.saveFile(blob, fileName);
				}
			}).finally(function () {
				if (multiDownload) {
					isLastFile && _this30.popupService.closeLoadingBar();
				} else {
					_this30.popupService.closeLoadingBar();
				}
			});
		}
	}, {
		key: 'openInterfacePopup',
		value: function openInterfacePopup() {
			var _this31 = this;

			this.popupService.openModal('SCR0902', { intrfcTypeCd: this.intrfcTypeCd, codes: this.codes }).then(function (intrfccom) {
				return _this31.setIntrfGridData(intrfccom.intrfcId, true);
			}).catch(function () {});
		}
	}, {
		key: '_createInterFaceId',
		value: function _createInterFaceId() {
			var _this32 = this;

			var detail = this.detail;
			var requestBody = {
				intrfcTypeCd: this.intrfcTypeCd,
				trxDscd: detail.trxDscd,
				syncAsyncDscd: detail.syncAsyncDscd,
				lv1Cd: detail.lv1Cd,
				sendSysCd: detail.intrfcsrsysdtDto.find(function (v) {
					return v.srTypeCd === 'SEND';
				}).sysCd,
				receiveSysCd: detail.intrfcsrsysdtDto.find(function (v) {
					return v.srTypeCd === 'RECEIVE';
				}).sysCd
			};

			this.httpService.post('/intrfccoms/intrfcidcreate', requestBody).then(function (res) {
				if (res.isError) {
					_this32.popupService.detailAlert(_this32.$scope, res.data.message, res.data.stackTrace);
					return;
				}

				_this32.detail.intrfcId = res.intrfcId;
				_this32.detail.workStatusCd = 'WORKING';
				_this32.refHistoryGrid.records = [];
			});
		}
	}, {
		key: 'changeIntrfcWay',
		value: function changeIntrfcWay() {
			var detail = this.detail;

			switch (detail.trxDscd) {
				case 'ONLINE':
					this.interfaceWayList = this.codes['INTRFC_WAY_CD'].filter(function (v) {
						return v.cdVal === 'APTOAP';
					});
					break;
				case 'BATCH':
					this.interfaceWayList = this.codes['INTRFC_WAY_CD'].filter(function (v) {
						return v.cdVal === 'FILETOFILE';
					});
					break;
				default:
					this.interfaceWayList = [];
					break;
			}
		}
	}, {
		key: 'onlyNum',
		value: function onlyNum(cnt) {
			this.detail.eaiDto.searchProcCnt = cnt.replace(/[^0-9]/g, '');
		}
	}, {
		key: 'refreshGrid',
		value: function refreshGrid() {
			var grid = w2ui[this.inferfaceMainGrid.name];
			grid.refresh();
		}
	}, {
		key: 'openTotalMsgPopup',
		value: function openTotalMsgPopup(gridName) {
			var grid = w2ui[this[gridName]['name']];
			var msgLayoutIdList = [];

			grid.records.map(function (row) {
				msgLayoutIdList.push(row['msglayoutbsDto']['msgLayoutId']);
			});

			this.popupService.openModal('SCR0707', { msgLayoutId: msgLayoutIdList, codes: this.codes, trxDscd: this.detail.trxDscd }).then(function () {}).catch(function () {});
		}
	}, {
		key: 'xlsExport',
		value: function xlsExport() {
			var _this33 = this;

			var grid = w2ui[this.inferfaceMainGrid.name];
			var selections = grid.getSelection();
			var selectionLength = selections.length;

			if (selectionLength === 0) {
				this.popupService.simpleAlert(this.$scope, this.text.emptyInterface);
				return;
			}

			selections.map(function (id, idx) {
				setTimeout(function () {
					_this33.excelExport(id, true, idx === 0, selectionLength === idx + 1);
				}, idx * 500);
			});
		}
	}, {
		key: 'exportDefinition',
		value: function exportDefinition() {
			var _this34 = this;

			var url = '/intrfccoms/export/intrfcinfos?intrfcTypeCd=' + this.intrfcTypeCd;

			this.popupService.showLoadingBar(this.$scope);
			this.httpService.downloadFile(url, this.searchParam.intrfccoms, 'get').then(function (res) {
				if (res.isError) {
					_this34.popupService.detailAlert(_this34.$scope, res.data.message, res.data.stackTrace);
				} else {
					var header = decodeURIComponent(res.headers('Content-Disposition'));
					var fileName = header.split("=")[1].replace(/\"/gi, '');
					console.log(fileName);

					var blob = new Blob([res.data], { type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" });
					_this34.utilService.saveFile(blob, fileName);
				}
			}).finally(function () {
				_this34.popupService.closeLoadingBar();
			});
		}
	}, {
		key: 'stopPropagation',
		value: function stopPropagation(e) {
			e.stopPropagation();
		}
	}]);
	return SCR0801Controller;
}();

(0, _angular.module)(_app2.default.name).controller('SCR0801Controller', SCR0801Controller);

/***/ })

},[49]);
//# sourceMappingURL=SCR0801.controller.js.map