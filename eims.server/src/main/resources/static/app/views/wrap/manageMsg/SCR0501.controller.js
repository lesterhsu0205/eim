webpackJsonp(["app\\views\\wrap\\manageMsg\\SCR0501.controller"],{

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

/***/ 57:
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__("d3c567f9f404d1aef125");


/***/ }),

/***/ "d3c567f9f404d1aef125":
/***/ (function(module, exports, __webpack_require__) {

"use strict";


var _typeof2 = __webpack_require__("1316bd4a7881789a1fe9");

var _typeof3 = _interopRequireDefault(_typeof2);

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

var SCR0501Controller = function () {
	function SCR0501Controller($scope, $compile, $state, $timeout, httpService, gridService, utilService, codeService, popupService, metaService, userService, codes) {
		var _this = this;

		(0, _classCallCheck3.default)(this, SCR0501Controller);

		this.$scope = $scope;
		this.$state = $state;
		this.$compile = $compile;
		this.$timeout = $timeout;
		this.codes = codes;
		this.workStatusList = codes.WORK_STATUS_CD.filter(function (v) {
			return v.cdVal !== 'DEPLOY_COMP';
		});
		this.httpService = httpService;
		this.utilService = utilService;
		this.codeService = codeService;
		this.gridService = gridService;
		this.popupService = popupService;
		this.metaService = metaService;
		this.userService = userService;
		this.user = this.userService.getUser();

		this.initText();
		this.initSelect();
		this.initCodeMap();

		this.allMsgType = codes.MSG_TYPE;
		this.initMsgGridOptions();
		this.initMsgDetailGridOptions();
		this.getMaskCodes();

		var count = 0;
		this.$scope.$on('gridRendered', function () {
			count++;
			if (count === 2) {
				_this.initPrevData();
			}
		});

		$(window).on('resize', function () {
			_this.$timeout(function () {
				// w2ui grid inEditMode false로 설정해주지 않으면 에디팅 되지 않음
				var grid = w2ui[_this.msgDetailOptions.name];
				grid.last.inEditMode = false;
				_this.msgDetailOptions.autoComplete = false;

				_this.msgDetailOptions.name && w2ui[_this.msgDetailOptions.name].refresh();
			});
		});

		this.generatorLayoutdtDto = function () {
			var rowData = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : {};
			var parentFldUnqId = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : '';

			var parentId = _.isEmpty(parentFldUnqId) ? _this.selectedMsg.msgLayoutId : parentFldUnqId;
			if (_.isEmpty(parentId)) parentId = _this.selectedMsg._msgLayoutId;
			var id = _this.utilService.uniqueId('TEMP_LAYOUT_DD_DTO_ID');
			rowData.fldUnqId = parentId + '.' + id;

			if (!rowData.fldLvNo) rowData.fldLvNo = 0;
			if (!rowData.dataTypeNm) rowData.dataTypeNm = 'STRING';
			if (!rowData.msgLen) rowData.msgLen = 0;
			if (!rowData.decimalLen) rowData.decimalLen = 0;
			if (!rowData.alignNm) rowData.alignNm = _this.getAlignNmByDataType(rowData.dataTypeNm);
			if (!rowData.extrnlMsgNoYn) rowData.extrnlMsgNoYn = 'N';
			if (!rowData.encYn) rowData.encYn = 'N';
			if (!rowData.privacyDscd) rowData.privacyDscd = '00';

			delete rowData.index;
			delete rowData.column;
			return rowData;
		};

		this.changeCodeFunc = function (fieldNm, cdNm) {
			return _this.codeMap[fieldNm][cdNm];
		};

		this.getAlignNmByDataType = function (dataTypeNm) {
			var alignNm = '';

			if (dataTypeNm === 'STRING' || dataTypeNm === 'BYTEARRAY') {
				alignNm = 'LEFT';
			} else if (dataTypeNm === 'INTEGER' || dataTypeNm === 'BIGDECIMAL') {
				alignNm = 'RIGHT';
			}

			return alignNm;
		};
	}

	(0, _createClass3.default)(SCR0501Controller, [{
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
					this.msgOptions.limit = prevScope.select.pageSize;
					this.gridHeight = prevScope.gridHeight;

					// 그리드
					this.msgOptions.records = prevScope.msgOptions.records;
					this.msgOptions.recordsCount = prevScope.msgOptions.recordsCount;

					this.$timeout(function () {
						_this2.pageNumber = prevScope.pageNumber;
						_this2.$scope.$broadcast('resetPage', _this2.pageNumber);
					});

					if (prevScope.selectedMsg && (0, _keys2.default)(prevScope.selectedMsg).length > 0) {
						// 상세
						this.selectedMsg = prevScope.selectedMsg;
						this._selectedMsg = prevScope._selectedMsg;

						// 상세 그리드					
						this.setMsgOption();
						this.setRegDttm();

						this.msgDetailOptions.recordsCount = prevScope.msgDetailOptions.recordsCount;
						this.msgDetailOptions.records = prevScope.msgDetailOptions.records;
					}

					// 수정모드
					this.isAdd = prevScope.isAdd;
					if (prevScope.isEdit) {
						this.onEditMode();
					} else {
						this.offEditMode();
					}

					// 스크롤
					$('#main-contents').scrollTop(prevScope.scrollTop);

					if (prevScope.impactPopupVisible) {
						$('#msg-impact-poopup').show();
					}
				}

				if (param.data) {
					var data = param.data;

					data.msgLayoutId && this.getMsgDetailGridData(data.msgLayoutId);
					this.searchParam = { msgLayoutId: data.msgLayoutId };
					this.getGridData(true);
					this.offEditMode();
				}
			} else {
				this.resetSearch();
			}

			this.$scope.$on('$destroy', function () {
				_this2.scrollTop = $('#main-contents').scrollTop();
				_this2.utilService.setParams(currentStateName, { scope: _this2.$scope });

				var impactPopup = $('#msg-impact-poopup');
				_this2.impactPopupVisible = impactPopup.is(':visible');
				impactPopup.hide();

				$(window).off('resize');
			});
		}
	}, {
		key: 'initText',
		value: function initText() {
			this.text = $.extend({}, bxMsg.getMessages('common'), bxMsg.getMessages('manageMsg'));
		}
	}, {
		key: 'initSelect',
		value: function initSelect() {
			this.select = this.gridService.getSelect(this.codes['GRID_PAGE_SIZE'][1].cdVal);
		}
	}, {
		key: 'initCodeMap',
		value: function initCodeMap() {
			var _this3 = this;

			this.codeMap = {
				'privacyDscd': {},
				'dataTypeNm': {},
				'fillerVal': {}
			};

			this.codes.DATA_TYPE.map(function (codeObj) {
				_this3.codeMap['dataTypeNm'][codeObj.cdValNm] = codeObj.cdVal;
			});

			this.codes.FILLER_CD.map(function (codeObj) {
				_this3.codeMap['fillerVal'][codeObj.cdValNm] = codeObj.cdVal;
			});

			this.proFepSysCd = ['FEA', 'FEJ', 'FEN', 'FEH', 'FEP'];
			this.isProFEP = false;
		}
	}, {
		key: 'getMaskCodes',
		value: function getMaskCodes() {
			var _this4 = this;

			var defaultArray = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : [];


			this.httpService.get('maskCd').then(function (data) {
				var records = data.maskOutList,
				    recordsCount = data.totalCnt;


				_this4.maskCd = records;

				_this4.maskCd.map(function (records) {
					defaultArray.push({
						id: records.maskCd,
						text: records.maskNm
					});
				});
			});

			return defaultArray;
		}
	}, {
		key: 'getMaskNm',
		value: function getMaskNm(code) {

			var maskNm = '';
			for (var i = 0; i < this.maskCd.length; i++) {
				if (this.maskCd[i].maskCd == code) {
					maskNm = this.maskCd[i].maskNm;
					break;
				}
			}

			return maskNm;
		}
	}, {
		key: 'initMsgGridOptions',
		value: function initMsgGridOptions() {
			var _this5 = this;

			this.gridHeight = this.gridService.getGridHeight(this.select.pageSize);
			this.msgOptions = {
				limit: this.select.pageSize,
				pageSize: this.select.pageSize,
				show: { columnHeaders: true, selectColumn: true },
				recordsCount: 0,
				recid: 'msgLayoutId',
				multiSelect: false,
				columns: [{
					field: 'no', caption: 'No', size: '40px',
					render: function render(data, index) {
						return _this5.gridService.getNoField(_this5.msgOptions.limit, index, _this5.pageNumber);
					}
				}, {
					field: 'lvCds', caption: this.text.lvCdsStr, size: '0.7%', sortable: true,
					render: function render(data) {
						return data.lv1Cd ? data.lv1Cd : '';
					}
				}, { field: 'msgLayoutId', caption: this.text.msgLayoutId, size: '1.5%', sortable: true, tooltip: false }, { field: 'msgNm', caption: this.text.msgName, size: '2%', attr: "align=left", sortable: true }, { field: 'msgNmSub', caption: this.text.msgNameSub, size: '2%', attr: "align=left", sortable: true }, {
					field: 'chlDscd', caption: this.text.chlDscd, size: this.user.locale === 'en' ? '110px' : '0.5%', sortable: true,
					render: function render(data, index, colIndex) {
						return _this5.codeService.getCodeValNm('CHL_DSCD', data.chlDscd);
					}
				}, { field: 'trxDscd', caption: this.text.trxDscd, size: this.user.locale === 'en' ? '120px' : '0.5%', sortable: true,
					render: function render(data, index, colIndex) {
						var trxDscd = w2ui[_this5.msgOptions.name].getCellValue(index, colIndex);
						return _this5.codeService.getCodeValNm('TRAN_DSCD', trxDscd);
					}
				}, { field: 'msgDataVal', caption: this.text.msgDataVal + '/' + this.text.dtoNm, attr: "align=left", size: this.user.locale === 'en' ? '200px' : '1.5%', sortable: true }, {
					field: 'msgDscd', caption: this.text.msgType, size: this.user.locale === 'en' ? '90px' : '0.5%', sortable: true,
					render: function render(data, index, colIndex) {
						return _this5.codeService.getCodeValNm('MSG_TYPE', data.msgDscd);
					}
				}, { field: 'regManId', caption: this.text.msgRegister, size: this.user.locale === 'en' ? '70px' : '60px', sortable: true }, {
					field: 'workStatusCd', caption: this.text.workStatusCd, size: this.user.locale === 'en' ? '110px' : '0.5%', sortable: true,
					render: function render(data) {
						return _this5.codeService.getCodeValNm('WORK_STATUS_CD', data.workStatusCd);
					}
				}, {
					field: 'regDttm', caption: this.text.msgRegisterDt, size: this.user.locale === 'en' ? '110px' : '70px', sortable: true,
					render: function render(data) {
						var regDttm = data.regDttm;
						var yy = regDttm.substring(0, 4);
						var mm = regDttm.substring(4, 6);
						var dd = regDttm.substring(6, 8);
						return yy + "/" + mm + "/" + dd;
					}
				}, {
					caption: this.text.edit, size: '90px',
					render: function render(data) {
						var html = '';

						if (_this5.user.perm.update) {
							html += '<button type="button" class="bw-btn bxd bxd-edit2" data-action="edit"></button>';
						}

						if (_this5.user.perm.delete) {
							html += '<button type="button" class="bw-btn bxd bxd-trash" data-action="delete"></button>';
						}

						return html;
					}
				}],
				onClick: function onClick(e) {
					// init selected detail row 
					_this5._selectedDetailRow = {};

					var grid = w2ui[_this5.msgOptions.name];
					var editData = grid.get(e.recid);
					var eTarget = e.originalEvent.target;

					_this5.selectedData = editData;

					if (eTarget.localName === 'button') {
						var action = $(eTarget).attr('data-action');

						if (action === 'export') {
							_this5.excelExport(editData.msgLayoutId);
							e.preventDefault();
							return;
						} else if (action === 'edit') {
							_this5._onEdit();
						} else {
							_this5.openConfirm(grid, e.recid);
							e.preventDefault();
							return;
						}
					} else {
						_this5.offEditMode();
					}
					_this5.$scope.$apply();

					// prevent deselect
					var selection = w2ui[e.target].getSelection();
					var $target = $(e.originalEvent.target);

					if ($target.find('.w2ui-grid-select-check').length === 0) {
						if (selection.length === 1 && selection[0] === e.recid) {
							e.preventDefault();

							if (!_.isEmpty(_this5._selectedMsg)) {
								return;
							}
						}
					}

					_this5.getMsgDetailGridData(editData.msgLayoutId);
				}
			};
		}
	}, {
		key: 'initMsgDetailGridOptions',
		value: function initMsgDetailGridOptions() {
			var _this6 = this;

			this.msgDetailOptions = {
				limit: this.select.pageSize,
				pageSize: this.select.pageSize,
				autoCompleteIndics: [1, 2],
				records: [],
				recordsCount: 0,
				selectType: 'cell',
				recid: 'fldUnqId',
				columns: this._getDefaultMsgDetailGridColumns(),
				onClick: function onClick(e) {
					var grid = w2ui[e.target];
					var recId = e.recid;

					_this6._selectedDetailRow = {
						grid: grid,
						editData: grid.get(recId)
					};

					// 타입이 INTEGER나 BIGDECIMAL이면 정렬을 RIGHT만 선택가능하도록 처리
					if (grid.columns[e.column]['field'] === 'alignNm') {
						if (_this6._selectedDetailRow.editData.dataTypeNm === 'INTEGER' || _this6._selectedDetailRow.editData.dataTypeNm === 'BIGDECIMAL') {
							grid.columns[e.column] = {
								field: 'alignNm', caption: _this6.text.alignNm, size: '40px',
								editable: _this6.isEdit ? { type: 'select', items: [{ id: 'RIGHT', text: 'RIGHT' }] } : false,
								render: function render(data, index, colIndex) {
									var alignNm = w2ui[_this6.msgDetailOptions.name].getCellValue(index, colIndex);
									return _this6.codeService.getCodeValNm(_this6.codes.ALIGN_CD, alignNm);
								}
							};
						} else {
							grid.columns[e.column] = {
								field: 'alignNm', caption: _this6.text.alignNm, size: '40px',
								editable: _this6.isEdit ? { type: 'select', items: _this6.gridService.getSelectItemsFromCodes(_this6.codes.ALIGN_CD, [{ id: "NONE", text: " " }]) } : false,
								render: function render(data, index, colIndex) {
									var alignNm = w2ui[_this6.msgDetailOptions.name].getCellValue(index, colIndex);
									return _this6.codeService.getCodeValNm(_this6.codes.ALIGN_CD, alignNm);
								}
							};
						}
					}
				},
				onPaste: function onPaste(e) {
					e.preventDefault();

					if (!_this6.isEdit) {
						return;
					}

					_this6.msgDetailOptions.isCopyMode = false;
					_this6.gridService.pasteForMsg(e, _this6.msgDetailOptions, _this6.generatorLayoutdtDto, _this6.changeCodeFunc);

					var grid = w2ui[e.target];

					grid.selectNone();
					_this6._selectedDetailRow = {};

					_this6.reorderSeq();
					grid.save();
					grid.refresh();
				},
				onCopy: function onCopy(e) {
					if (!_this6.isEdit) return;
					_this6.msgDetailOptions.isCopyMode = true;
					_this6._selectedDetailRow = {};
				},
				onChange: function onChange(e) {
					var grid = w2ui[e.target];
					var record = grid.get(e.recid);

					if (!_this6.isEdit && e.originalEvent.target.type === 'checkbox') {
						e.preventDefault();
					} else if (e.column == 1) {
						// 영문명 (변수명) 중복 체크 
						_this6.setParentFldNm(record);

						if (!_this6.fieldNameValid({ fldLvNo: record.fldLvNo, fldEngNm: e.value_new, parentFldNm: record.parentFldNm })) {
							_this6.popupService.simpleAlert(_this6.$scope, _this6.text.dupFieldNm);
							e.value_new = '';
							return;
						}
					}

					e.onComplete = function () {
						if (record.w2ui.changes.fldLvNo != undefined) {
							_this6.setParentFldNm(record, true);

							if (!_this6.fieldNameValid({ fldLvNo: e.value_new, fldEngNm: record.fldEngNm, parentFldNm: record.parentFldNm })) {
								_this6.popupService.simpleAlert(_this6.$scope, _this6.text.dupFieldNm);
								record.fldEngNm = '';
							}

							grid.save();
						} else if (record.w2ui.changes.dataTypeNm != undefined) {
							record.alignNm = _this6.getAlignNmByDataType(record.w2ui.changes.dataTypeNm);

							if (record.w2ui.changes.dataTypeNm == 'LAYOUT') {
								record.msgLen = 0;
								record.decimalLen = 0;
								record.w2ui.changes.decimalLen = 0;
							}

							grid.save();
						} else if (record.w2ui.changes.fldEngNm != undefined) {
							grid.save();

							// meta validate
							_this6.rowMetaValid(record);
							_this6.$scope.$apply();
						} else if (record.w2ui.changes.fldRmk != undefined) {
							grid.save();
						}
					};
				},
				onSelect: function onSelect(e) {
					if (_this6.isCopyMode) {
						var grid = w2ui[e.target];
						var recId = e.recid;

						_this6._selectedDetailRow = {
							grid: grid,
							editData: grid.get(recId)
						};
					}
				},
				onUnselect: function onUnselect(e) {
					if (_.isEmpty(_this6._selectedDetailRow) || _.isEmpty(_this6._selectedDetailRow.editData)) return;
					if (e.recid === _this6._selectedDetailRow.editData.recid) {
						_this6._selectedDetailRow = {};
					}
				},
				onEditField: function onEditField(e) {
					if (!_this6.isEdit) return;
					var grid = w2ui[e.target];
					var record = grid.get(e.recid);

					// 타입이 레이아웃일때 소수점 입력 불가  
					if (record.dataTypeNm == 'LAYOUT' && e.column == 5) {
						e.preventDefault();
					}

					// 자동완성필드이고 타입이 레이아웃이 아닌경우 자동완성기능
					if (!_this6.gridService.isAutoCompleteField(_this6.msgDetailOptions, e) || record.dataTypeNm === 'LAYOUT') return;
					_this6.gridService.autoComplete(e, _this6, _this6.msgDetailOptions);
					_this6._onEditEvent = e;
				}
			};
		}

		// 자동완성 값 선택시, 호출되는 메타데이터 입력 처리 메서드
		// this.gridService.autoComplete 에서 호출

	}, {
		key: 'onSelectAutoComplete',
		value: function onSelectAutoComplete(metaData, $editInput, column) {
			if (_.isEmpty(this._onEditEvent)) return;
			var $input = $editInput ? $editInput.get(0) : $('div[name=' + this.msgDetailOptions.name + '] .w2ui-editable input').get(0);
			var grid = w2ui[this.msgDetailOptions.name];
			var record = grid.get(this._onEditEvent.recid);

			var originalEvent = this._onEditEvent;
			delete this._onEditEvent;

			// w2ui grid inEditMode false로 설정해주지 않으면 에디팅 되지 않음
			grid.last.inEditMode = false;
			this.msgDetailOptions.autoComplete = false;

			// 순서 변경 
			//		if(grid.moved) {
			//			grid.moved = false;
			//			return;
			//		}

			this.searchItems = [];
			if (_.isEmpty(metaData)) {
				//record && ($input.value = record.fldKorNm);
				grid.editChange.call(grid, $input, originalEvent.index, originalEvent.column, originalEvent);

				if (record && record.w2ui && record.w2ui.changes) {
					if (column === 1 && record.w2ui.changes.fldEngNm) {
						record.fldEngNm = record.w2ui.changes.fldEngNm;
					} else if (column === 2 && record.w2ui.changes.fldKorNm) {
						record.fldKorNm = record.w2ui.changes.fldKorNm;
					}

					grid.save();
				}
			} else {
				this.setParentFldNm(record);

				if (!this.fieldNameValid({ fldLvNo: record.fldLvNo, fldEngNm: metaData.metaEngNm, parentFldNm: record.parentFldNm })) {
					this.popupService.simpleAlert(this.$scope, this.text.dupFieldNm);
					$input.value = '';
					grid.editChange.call(grid, $input, originalEvent.index, originalEvent.column, originalEvent);

					record.fldEngNm = '';
					grid.save();

					return;
				}

				if (column === 1) {
					$input.value = metaData.metaEngNm;
				} else if (column === 2) {
					$input.value = metaData.metaKorNm;
				}

				grid.editChange.call(grid, $input, originalEvent.index, originalEvent.column, originalEvent);

				if (column === 1) {
					!record.w2ui.changes && (record.w2ui.changes = {});
					record.w2ui.changes.fldKorNm = metaData.metaKorNm;
				} else if (column === 2) {
					record.w2ui.changes.fldEngNm = metaData.metaEngNm;
				}

				record.w2ui.changes.msgLen = metaData.metaLen;
				record.w2ui.changes.decimalLen = metaData.decimalLen;
				record.w2ui.changes.dataTypeNm = metaData.dataTypeNm;
				record.w2ui.changes.encYn = metaData.metaEncYn;
				record.w2ui.changes.alignNm = this.getAlignNmByDataType(metaData.dataTypeNm);
				record.w2ui.changes.confirmMeta = this.text.accord;
				grid.save();
			}
		}
	}, {
		key: 'changeMsgDetailGridColumns',
		value: function changeMsgDetailGridColumns(msg) {
			if (msg.chlDscd == 'EXTERNAL' || msg.trxDscd == 'BATCH' && this.isProFEP) {
				var columns = this._getFepMsgDetailGridColumns(msg);
			} else {
				var columns = this._getDefaultMsgDetailGridColumns(msg);
			}

			if (!_.isEmpty(msg)) {
				switch (msg.msgDscd) {
					case 'CH':
						if (msg.chlDscd === 'EXTERNAL') {
							columns.push.apply(columns, []);
						}
						break;
					case 'IV':
						if (msg.chlDscd === 'EXTERNAL') {
							columns.push.apply(columns, []);
						}
						break;
				}
			}

			this.msgDetailOptions.columns = columns;
		}
	}, {
		key: '_getFepMsgDetailGridColumns',
		value: function _getFepMsgDetailGridColumns() {
			var _this7 = this;

			var msg = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : {};

			return [{ field: 'msgSeq', caption: this.text.msgSeq, size: '40px', frozen: true }, { field: 'fldEngNm', caption: this.text.fldEngNm, size: '2%', frozen: true, attr: "align=left",
				render: function render(data) {
					var spaceTemp = '&nbsp;&nbsp;&nbsp;&nbsp;';
					var space = '';
					var fldEngNm = data.fldEngNm ? data.fldEngNm : '';

					if (data.fldLvNo == 0) {
						return fldEngNm;
					} else {
						for (var i = 0; i < data.fldLvNo; i++) {
							space = space.concat(spaceTemp);
						}
						return space + fldEngNm;
					}
				},
				editable: this.isEdit ? { type: 'text' } : false
			}, { field: 'fldKorNm', caption: this.text.fldKorNm, attr: "align=left", size: '2%', frozen: true, editable: this.isEdit ? { type: 'text' } : false }, {
				field: 'dataTypeNm', caption: this.text.dataType, size: '80px', frozen: true,
				editable: this.isEdit ? {
					type: 'select',
					items: this.gridService.getSelectItemsFromCodes(this.codes.DATA_TYPE)
				} : false,
				render: function render(data, index, colIndex) {
					var dataTypeNm = w2ui[_this7.msgDetailOptions.name].getCellValue(index, colIndex);
					return _this7.codeService.getCodeValNm(_this7.codes.DATA_TYPE, dataTypeNm);
				}

			}, { field: 'msgLen', caption: this.text.msgLen, size: this.user.locale === 'en' ? '50px' : '45px', frozen: true, editable: this.isEdit ? { type: 'int' } : false }, { field: 'decimalLen', caption: this.text.decimalLen, size: this.user.locale === 'en' ? '110px' : '45px', frozen: true, editable: this.isEdit ? { type: 'int' } : false }, { field: 'fldLvNo', caption: this.text.fldLvNo, size: '45px', frozen: true, style: 'border-right: 0.5px solid black', editable: this.isEdit ? { type: 'int' } : false }, { field: 'childDtoNm', caption: this.text.childDtoNm, size: '1%', editable: this.isEdit ? { type: 'text' } : false }, { field: 'arraySizeRefVal', caption: this.text.arraySizeRefVal, size: this.user.locale === 'en' ? '140px' : '1%', editable: this.isEdit ? { type: 'text' } : false }, {
				field: 'privacyDscd', caption: this.text.privacyDscd, size: '1%',
				editable: this.isEdit ? { type: 'select', items: this.getMaskCodes() } : false,
				render: function render(data, index, colIndex) {
					var privacyDscd = w2ui[_this7.msgDetailOptions.name].getCellValue(index, colIndex);
					return _this7.getMaskNm(privacyDscd);
				}
			}, {
				field: 'encYn', caption: this.text.encYn, size: '1%',
				editable: { type: 'select', items: this.gridService.getSelectItemsFromCodes(this.codes.YN_CD) }
			}, {
				field: 'paramType', caption: this.text.paramType, size: '80px',
				editable: this.isEdit ? {
					type: 'select',
					items: this.gridService.getSelectItemsFromCodes(this.codes.PARAM_TYPE)
				} : false,
				render: function render(data, index, colIndex) {
					var paramTypeNm = w2ui[_this7.msgDetailOptions.name].getCellValue(index, colIndex);
					return _this7.codeService.getCodeValNm(_this7.codes.PARAM_TYPE, paramTypeNm);
				}
			}, {
				field: 'alignNm', caption: this.text.alignNm, size: this.user.locale === 'en' ? '70px' : '40px',
				editable: this.isEdit ? { type: 'select', items: this.gridService.getSelectItemsFromCodes(this.codes.ALIGN_CD, [{ id: "NONE", text: " " }]) } : false,
				render: function render(data, index, colIndex) {
					var alignNm = w2ui[_this7.msgDetailOptions.name].getCellValue(index, colIndex);
					return _this7.codeService.getCodeValNm(_this7.codes.ALIGN_CD, alignNm);
				}
			}, {
				field: 'fillerVal', caption: this.text.fillerVal, size: '60px',
				editable: this.isEdit ? { type: 'select', items: this.gridService.getSelectItemsFromCodes(this.codes.FILLER_CD, [{ id: "", text: "" }]) } : false,
				render: function render(data, index, colIndex) {
					var fillerVal = w2ui[_this7.msgDetailOptions.name].getCellValue(index, colIndex);
					return _this7.codeService.getCodeValNm(_this7.codes.FILLER_CD, fillerVal);
				}
			}, {
				field: 'fldRmk', caption: this.text.fldRmk, size: '2%', editable: this.isEdit ? { type: 'text' } : false, attr: "align=left",
				render: function render(data, index, colIndex) {
					return data.fldRmk ? '<div title="' + data.fldRmk + '"><span>' + data.fldRmk + '</span></div>' : '';
				}
			}, {
				field: 'confirmMeta', caption: this.text.metaValidate, size: this.user.locale === 'en' ? '110px' : '60px',
				render: function render(data, index, colIndex) {
					var confirmMeta = data.confirmMeta;

					if (data.confirmMeta === _this7.text.accord) {
						confirmMeta = '<span class="chr-c-green">' + confirmMeta + '</span>';
					} else if (data.confirmMeta === _this7.text.discord) {
						confirmMeta = '<span class="chr-c-orange">' + confirmMeta + '</span>';
					}

					return confirmMeta;
				}
			}];
		}
	}, {
		key: '_getDefaultMsgDetailGridColumns',
		value: function _getDefaultMsgDetailGridColumns() {
			var _this8 = this;

			var msg = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : {};

			return [{ field: 'msgSeq', caption: this.text.msgSeq, size: '40px', frozen: true }, { field: 'fldEngNm', caption: this.text.fldEngNm, size: '2%', frozen: true, attr: "align=left",
				render: function render(data) {
					var spaceTemp = '&nbsp;&nbsp;&nbsp;&nbsp;';
					var space = '';
					var fldEngNm = data.fldEngNm ? data.fldEngNm : '';

					if (data.fldLvNo == 0) {
						return fldEngNm;
					} else {
						for (var i = 0; i < data.fldLvNo; i++) {
							space = space.concat(spaceTemp);
						}
						return space + fldEngNm;
					}
				},
				editable: this.isEdit ? { type: 'text' } : false
			}, { field: 'fldKorNm', caption: this.text.fldKorNm, attr: "align=left", size: '2%', frozen: true, editable: this.isEdit ? { type: 'text' } : false }, {
				field: 'dataTypeNm', caption: this.text.dataType, size: '80px', frozen: true,
				editable: this.isEdit ? {
					type: 'select',
					items: this.gridService.getSelectItemsFromCodes(this.codes.DATA_TYPE)
				} : false,
				render: function render(data, index, colIndex) {
					var dataTypeNm = w2ui[_this8.msgDetailOptions.name].getCellValue(index, colIndex);
					return _this8.codeService.getCodeValNm(_this8.codes.DATA_TYPE, dataTypeNm);
				}

			}, { field: 'msgLen', caption: this.text.msgLen, size: this.user.locale === 'en' ? '50px' : '45px', frozen: true, editable: this.isEdit ? { type: 'int' } : false }, { field: 'decimalLen', caption: this.text.decimalLen, size: this.user.locale === 'en' ? '110px' : '45px', frozen: true, editable: this.isEdit ? { type: 'int' } : false }, { field: 'fldLvNo', caption: this.text.fldLvNo, size: '45px', frozen: true, style: 'border-right: 0.5px solid black', editable: this.isEdit ? { type: 'int' } : false }, { field: 'childDtoNm', caption: this.text.childDtoNm, size: '1%', editable: this.isEdit ? { type: 'text' } : false }, { field: 'arraySizeRefVal', caption: this.text.arraySizeRefVal, size: this.user.locale === 'en' ? '140px' : '1%', editable: this.isEdit ? { type: 'text' } : false }, {
				field: 'privacyDscd', caption: this.text.privacyDscd, size: '1%',
				editable: this.isEdit ? { type: 'select', items: this.getMaskCodes() } : false,
				render: function render(data, index, colIndex) {
					var privacyDscd = w2ui[_this8.msgDetailOptions.name].getCellValue(index, colIndex);
					return _this8.getMaskNm(privacyDscd);
				}
			}, {
				field: 'encYn', caption: this.text.encYn, size: '1%',
				editable: false
			}, {
				field: 'paramType', caption: this.text.paramType, size: '80px',
				editable: this.isEdit ? {
					type: 'select',
					items: this.gridService.getSelectItemsFromCodes(this.codes.PARAM_TYPE)
				} : false,
				render: function render(data, index, colIndex) {
					var paramTypeNm = w2ui[_this8.msgDetailOptions.name].getCellValue(index, colIndex);
					return _this8.codeService.getCodeValNm(_this8.codes.PARAM_TYPE, paramTypeNm);
				}
			}, {
				field: 'alignNm', caption: this.text.alignNm, size: this.user.locale === 'en' ? '70px' : '40px',
				editable: this.isEdit ? { type: 'select', items: this.gridService.getSelectItemsFromCodes(this.codes.ALIGN_CD, [{ id: "NONE", text: " " }]) } : false,
				render: function render(data, index, colIndex) {
					var alignNm = w2ui[_this8.msgDetailOptions.name].getCellValue(index, colIndex);
					return _this8.codeService.getCodeValNm(_this8.codes.ALIGN_CD, alignNm);
				}
			}, {
				field: 'fillerVal', caption: this.text.fillerVal, size: '60px',
				editable: this.isEdit ? { type: 'select', items: this.gridService.getSelectItemsFromCodes(this.codes.FILLER_CD, [{ id: "", text: "" }]) } : false,
				render: function render(data, index, colIndex) {
					var fillerVal = w2ui[_this8.msgDetailOptions.name].getCellValue(index, colIndex);
					return _this8.codeService.getCodeValNm(_this8.codes.FILLER_CD, fillerVal);
				}
			}, {
				field: 'fldRmk', caption: this.text.fldRmk, size: '2%', editable: this.isEdit ? { type: 'text' } : false, attr: "align=left",
				render: function render(data, index, colIndex) {
					return data.fldRmk ? '<div title="' + data.fldRmk + '"><span>' + data.fldRmk + '</span></div>' : '';
				}
			}, {
				field: 'confirmMeta', caption: this.text.metaValidate, size: this.user.locale === 'en' ? '110px' : '60px',
				render: function render(data, index, colIndex) {
					var confirmMeta = data.confirmMeta;

					if (data.confirmMeta === _this8.text.accord) {
						confirmMeta = '<span class="chr-c-green">' + confirmMeta + '</span>';
					} else if (data.confirmMeta === _this8.text.discord) {
						confirmMeta = '<span class="chr-c-orange">' + confirmMeta + '</span>';
					}

					return confirmMeta;
				}
			}];
		}
	}, {
		key: 'getGridData',
		value: function getGridData() {
			var _this9 = this;

			var goToFirst = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : false;

			var _gridService$getPageI = this.gridService.getPageInfo(this.select, this.pageNumber),
			    pageNumber = _gridService$getPageI.pageNumber,
			    pageSize = _gridService$getPageI.pageSize;

			var url = '/msglayouts?pageNumber=' + (goToFirst ? 1 : pageNumber) + '&pageSize=' + pageSize;

			this.httpService.get(url, this.searchParam).then(function (data) {
				var records = data.msglayoutbsOutList,
				    recordsCount = data.totalCnt;


				_this9.msgOptions.records = records;
				_this9.msgOptions.recordsCount = recordsCount;

				if (!_.isEmpty(_this9.msgOptions.name)) {
					w2ui[_this9.msgOptions.name].selectNone();
				}

				if (goToFirst) {
					_this9.pageNumber = 1;
					_this9.$scope.$broadcast('resetPage', _this9.pageNumber);
				}
			});
		}
	}, {
		key: 'getMsgDetailGridData',
		value: function getMsgDetailGridData(id) {
			var _this10 = this;

			var encodedId = encodeURIComponent(id);

			this.httpService.get('/msglayouts/' + encodedId).then(function (res) {
				_this10.setMsgDetailGridData(res);
			});
		}
	}, {
		key: 'setMsgDetailGridData',
		value: function setMsgDetailGridData(res) {
			var _this11 = this;

			var utilService = this.utilService;
			var originRecords = res.msglayoutdtDto;


			this.selectedMsg = utilService.clone(res);
			this._selectedMsg = utilService.clone(res);

			this.setMsgOption();
			this.setRegDttm();

			if (!_.isEmpty(this.msgDetailOptions.name)) {
				w2ui[this.msgDetailOptions.name].selectNone();
			}

			var grid = w2ui[this.msgDetailOptions.name];
			grid.clear(true);

			this.msgDetailOptions.recordsCount = originRecords.length;

			this.msgDetailOptions.records = originRecords.map(function (record) {
				record.fldUnqId = record.fldUnqId ? record.fldUnqId : _this11.utilService.uniqueId('TEMP_LAYOUT_DD_DTO_ID');

				if (!record.fldLvNo) record.fldLvNo = 0;
				if (!record.dataTypeNm) record.dataTypeNm = 'STRING';
				if (!record.msgLen) record.msgLen = 0;
				if (!record.decimalLen) record.decimalLen = 0;

				return record;
			});

			this.calcLength(this.msgDetailOptions.records);
		}
	}, {
		key: 'excelExport',
		value: function excelExport(id, multiDownload, isFirstFile, isLastFile) {
			var _this12 = this;

			var data = { 'msgLayoutId': id };

			if (multiDownload) {
				isFirstFile && this.popupService.showLoadingBar(this.$scope);
			} else {
				this.popupService.showLoadingBar(this.$scope);
			}

			this.httpService.downloadFile('/msglayouts/excelexport', data).then(function (res) {
				if (res.isError) {
					_this12.popupService.detailAlert(_this12.$scope, res.data.message, res.data.stackTrace);
				} else {
					var header = res.headers('Content-Disposition');
					var fileName = header.split("=")[1].replace(/\"/gi, '');

					var blob = new Blob([res.data], { type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" });

					_this12.utilService.saveFile(blob, fileName);
				}
			}).finally(function () {
				if (multiDownload) {
					isLastFile && _this12.popupService.closeLoadingBar();
				} else {
					_this12.popupService.closeLoadingBar();
				}
			});
		}
	}, {
		key: 'pageBtnClick',
		value: function pageBtnClick(num) {
			this.pageNumber = num;
			this.getGridData(num === 1);
		}
	}, {
		key: 'search',
		value: function search() {
			this.getGridData(true);
			this.resetDetail();
		}
	}, {
		key: 'resetSearch',
		value: function resetSearch() {
			this.searchParam = {};
			this.getGridData(true);
			this.resetDetail();
		}
	}, {
		key: 'blur',
		value: function blur($event) {
			$event.target.blur();
		}
	}, {
		key: 'add',
		value: function add() {
			if (!_.isEmpty(this.msgOptions.name)) {
				w2ui[this.msgOptions.name].selectNone();
			}

			this.resetDetail();
			this.isAdd = true;
			this.selectedMsg._msgLayoutId = this.utilService.uniqueId('TEMP_MSG_LAYOUT_ID');
			this.selectedMsg.sharedYn = 'N';
			this.selectedMsg.custApiYn = 'N';
			this.selectedMsg.workStatusCd = 'WORKING';
			this.selectedMsg.regManId = this.user.userId;

			this._onEdit();
		}
	}, {
		key: 'save',
		value: function save() {
			var _this13 = this;

			var grid = w2ui[this.msgDetailOptions.name];

			// 영문명(변수명)이나 영문명 FullName을 수정하다가 바로 저장시, 수정된 데이터를 저장
			if (this._onEditEvent) {
				if (this._onEditEvent.column === 1) {
					this._onEditEvent && grid.set(this._onEditEvent.recid, { fldEngNm: this._onEditEvent.input.val() });
				} else if (this._onEditEvent.column === 2) {
					this._onEditEvent && grid.set(this._onEditEvent.recid, { fldKorNm: this._onEditEvent.input.val() });
				}
			}
			grid.save();

			if (!this._checkValid(this.selectedMsg, grid.records)) return;

			this.popupService.showLoadingBar(this.$scope);

			if (this.selectedMsg.custApiYn === 'N' || this.selectedMsg.custApiYn === undefined) {
				if (!this.metaValidate(true)) {
					this.popupService.closeLoadingBar();
					return;
				}
			}

			var msg = this.utilService.clone(this.selectedMsg);
			var depthArray = grid.records.map(function (v) {
				return v.fldLvNo;
			});
			var preRecord = null;

			msg.msglayoutdtDto = grid.records.map(function (layoutDtDto, index) {
				layoutDtDto.msgLayoutId = msg.msgLayoutId;
				layoutDtDto.msgVersion = msg.msgVersion;

				if (layoutDtDto.fldLvNo == 0) {
					layoutDtDto.fldUnqId = msg.msgLayoutId + '.' + layoutDtDto.fldEngNm;
					delete layoutDtDto.parentFldNm;
				} else if (Number(preRecord.fldLvNo) + 1 == layoutDtDto.fldLvNo) {
					layoutDtDto.fldUnqId = preRecord.fldUnqId + '.' + layoutDtDto.fldEngNm;
					layoutDtDto.parentFldNm = preRecord.fldUnqId;
				} else if (preRecord.fldLvNo == layoutDtDto.fldLvNo) {
					layoutDtDto.fldUnqId = preRecord.parentFldNm + '.' + layoutDtDto.fldEngNm;
					layoutDtDto.parentFldNm = preRecord.parentFldNm;
				} else if (Number(preRecord.fldLvNo) > layoutDtDto.fldLvNo) {
					for (var i = index - 1; i > -1; i--) {
						if (depthArray[i] == layoutDtDto.fldLvNo) {
							var sibling = grid.records[i];
							layoutDtDto.fldUnqId = sibling.parentFldNm + '.' + layoutDtDto.fldEngNm;
							layoutDtDto.parentFldNm = sibling.parentFldNm;
							break;
						}
					}
				}

				preRecord = layoutDtDto;

				return layoutDtDto;
			});

			var msgLength = this.calcLength();
			msg.rsrvFldVal3 = msgLength;

			var q = this.isAdd ? this.httpService.post('/msglayouts', msg) : this.httpService.put('/msglayouts', msg);

			q.then(function (res) {
				_this13.popupService.closeLoadingBar();

				if (res.isError) {
					_this13.popupService.detailAlert(_this13.$scope, res.data.message, res.data.stackTrace, res.data.parameters);
					return;
				}

				_this13.popupService.simpleAlert(_this13.$scope, bxMsg('common.saved'));
				_this13.getGridData();
				//			this.getMsgDetailGridData(this.selectedMsg.msgLayoutId);

				if (_this13.isAdd) {
					_this13.getMsgDetailGridData(res.msgLayoutId);
				} else {
					_this13.getMsgDetailGridData(_this13.selectedMsg.msgLayoutId);
				}

				_this13.offEditMode();
				_this13._selectedMsg = _this13.utilService.clone(_this13.selectedMsg);
			});

			q.finally(function () {
				_this13.popupService.closeLoadingBar();
			});
		}
	}, {
		key: 'tempSave',
		value: function tempSave() {
			var _this14 = this;

			var grid = w2ui[this.msgDetailOptions.name];
			grid.save();

			for (var i = 0; i < grid.records.length; i++) {
				var msglayout = grid.records[i];

				// 영문명
				if (_.isEmpty(msglayout.fldEngNm)) {
					this.popupService.simpleAlert(this.$scope, this.text.order + ' [' + msglayout.msgSeq + '] ' + this.text.emptyFldEngNm);
					return false;
				}
			}

			if (!this.selectedMsg.msgLayoutId) {
				this.popupService.simpleAlert(this.$scope, this.text.emptyMsgLayoutId);
				return false;
			}

			this.popupService.showLoadingBar(this.$scope);

			var msg = this.utilService.clone(this.selectedMsg);
			var depthArray = grid.records.map(function (v) {
				return v.fldLvNo;
			});
			var preRecord = null;

			msg.msglayoutdtDto = grid.records.map(function (layoutDtDto, index) {
				layoutDtDto.msgLayoutId = msg.msgLayoutId;
				layoutDtDto.msgVersion = msg.msgVersion;

				if (layoutDtDto.fldLvNo == 0) {
					layoutDtDto.fldUnqId = msg.msgLayoutId + '.' + layoutDtDto.fldEngNm;
					delete layoutDtDto.parentFldNm;
				} else if (Number(preRecord.fldLvNo) + 1 == layoutDtDto.fldLvNo) {
					layoutDtDto.fldUnqId = preRecord.fldUnqId + '.' + layoutDtDto.fldEngNm;
					layoutDtDto.parentFldNm = preRecord.fldUnqId;
				} else if (preRecord.fldLvNo == layoutDtDto.fldLvNo) {
					layoutDtDto.fldUnqId = preRecord.parentFldNm + '.' + layoutDtDto.fldEngNm;
					layoutDtDto.parentFldNm = preRecord.parentFldNm;
				} else if (Number(preRecord.fldLvNo) > layoutDtDto.fldLvNo) {
					for (var _i = index - 1; _i > -1; _i--) {
						if (depthArray[_i] == layoutDtDto.fldLvNo) {
							var sibling = grid.records[_i];
							layoutDtDto.fldUnqId = sibling.parentFldNm + '.' + layoutDtDto.fldEngNm;
							layoutDtDto.parentFldNm = sibling.parentFldNm;
							break;
						}
					}
				}

				preRecord = layoutDtDto;

				return layoutDtDto;
			});

			var msgLength = this.calcLength();
			msg.rsrvFldVal3 = msgLength;

			var q = this.isAdd ? this.httpService.post('/msglayoutstemp', msg) : this.httpService.put('/msglayoutstemp', msg);

			q.then(function (res) {
				_this14.popupService.closeLoadingBar();

				if (res.isError) {
					_this14.popupService.detailAlert(_this14.$scope, res.data.message, res.data.stackTrace, res.data.parameters);
					return;
				}

				_this14.popupService.simpleAlert(_this14.$scope, bxMsg('common.tempSaved'));
				_this14.getGridData();

				if (_this14.isAdd) {
					_this14.getMsgDetailGridData(res.msgLayoutId);
				} else {
					_this14.getMsgDetailGridData(_this14.selectedMsg.msgLayoutId);
				}

				_this14.offEditMode();
				_this14._selectedMsg = _this14.utilService.clone(_this14.selectedMsg);
			});

			q.finally(function () {
				_this14.popupService.closeLoadingBar();
			});
		}
	}, {
		key: 'calcLength',
		value: function calcLength(records) {
			var record = void 0;

			if (!records) {
				w2ui[this.msgDetailOptions.name].save();
				record = this.utilService.clone(w2ui[this.msgDetailOptions.name].records);
			} else {
				record = this.utilService.clone(records);
			}

			var msgLen = 0;

			var layoutLength = new Array();

			for (var i = 0; i < record.length; i++) {

				if (record[i].dataTypeNm == "LAYOUT") {
					var data = new Object();

					data.id = record[i].fldUnqId;

					if (record[i].arraySizeRefVal == undefined || isNaN(record[i].arraySizeRefVal)) {
						data.msgLen = 1;
						record[i].arraySizeRefVal = 1;
					}

					if (record[i].arraySizeRefVal != undefined && record[i].parentFldNm == undefined) {
						data.msgLen = Number(record[i].arraySizeRefVal);
					} else {
						for (var j = 0; j < layoutLength.length; j++) {
							if (layoutLength[j].id == record[i].parentFldNm) {
								data.msgLen = Number(record[i].arraySizeRefVal) * Number(layoutLength[j].msgLen);
							}
						}
					}

					layoutLength.push(data);
				}
			}

			for (var i = 0; i < record.length; i++) {
				if (record[i].parentFldNm == undefined) {
					msgLen += Number(record[i].msgLen);
				} else {
					for (var j = 0; j < layoutLength.length; j++) {
						if (record[i].parentFldNm == layoutLength[j].id) {
							msgLen += Number(record[i].msgLen) * Number(layoutLength[j].msgLen);
						}
					}
				}
			}

			this.msgLength = msgLen;

			return this.msgLength;
		}
	}, {
		key: '_isCreateMsgLayout',
		value: function _isCreateMsgLayout() {
			return _.isEmpty(this._selectedMsg);
		}
	}, {
		key: 'deleteGridData',
		value: function deleteGridData() {
			var _this15 = this;

			var grid = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : {};
			var id = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : '';

			var encodedId = encodeURIComponent(id);

			this.httpService.delete('/msglayouts/' + encodedId).then(function (data) {
				if (data.isError) {
					_this15.popupService.simpleAlert(_this15.$scope, data.data.message);
					return;
				}

				_this15.resetDetail();
				_this15.getGridData();
			});
		}
	}, {
		key: 'change',
		value: function change() {
			var pageSize = this.select.pageSize;
			this.msgOptions.limit = pageSize;
			this.gridHeight = this.gridService.getGridHeight(this.select.pageSize);
			this.pageBtnClick(1);
			this.msgOptions.name && w2ui[this.msgOptions.name].focus();
		}
	}, {
		key: 'resetDetail',
		value: function resetDetail() {
			this.selectedMsg = {};
			this._selectedMsg = {};
			this._selectedDetailRow = {};
			this.msgLength = '';
			this.isAdd = false;

			this.offEditMode();
			this.setMsgOption();
			if (!_.isEmpty(this.msgDetailOptions.name)) {
				this.msgDetailOptions.records = [];
			}
		}
	}, {
		key: 'onEditMode',
		value: function onEditMode() {
			if (_.isEmpty(this.selectedMsg)) return;
			this._onEdit();
		}
	}, {
		key: '_onEdit',
		value: function _onEdit() {
			var editData = this.selectedData;

			if (this.user.roleId != 'Administrator' && editData && editData.msgDscd == 'STH') {
				this.popupService.simpleAlert(this.$scope, this.text.modifyCommonHeader);
				return;
			}

			var $forms = this._isCreateMsgLayout() ? $('#searchWrap').find('div:not(.disabled) > input,textarea') : $('#searchWrap').find('div.editable > input,textarea');

			var $editable = $('#editData').find('select');

			this.isEdit = true;

			$forms.attr('disabled', false);
			$editable.attr('disabled', false);

			_.isEmpty(this.msgDetailOptions) ? this.initMsgDetailGridOptions() : this.changeMsgType();
		}
	}, {
		key: 'offEditMode',
		value: function offEditMode() {
			var $forms = $('#searchWrap').find('input,textarea,select');
			var $editable = $('#editData').find('select');

			this.isEdit = false;
			this.isAdd = false;

			$forms.attr('disabled', true);
			$editable.attr('disabled', true);

			_.isEmpty(this.msgDetailOptions) ? this.initMsgDetailGridOptions() : this.changeMsgType();
		}
	}, {
		key: 'openConfirm',
		value: function openConfirm(grid, recid) {
			var _this16 = this;

			var record = grid.get(recid);

			if (record.regManId !== this.user.userId) {
				this.popupService.simpleAlert(this.$scope, this.text.delRegister);
				return;
			}

			this.popupService.simpleConfirm(this.$scope, this.text.confirmTextDelete, function () {
				return _this16.deleteGridData(grid, recid);
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
		value: function _checkValid(msg, records) {
			var _this17 = this;

			if (!msg.msgLayoutId) {
				this.popupService.simpleAlert(this.$scope, this.text.emptyMsgLayoutId);
				return false;
			} else if (!msg.chlDscd) {
				this.popupService.simpleAlert(this.$scope, this.text.emptyChlDscd);
				return false;
			} else if (!msg.msgNm) {
				this.popupService.simpleAlert(this.$scope, this.text.emptyMsgName);
				return false;
			} else if (!msg.trxDscd) {
				this.popupService.simpleAlert(this.$scope, this.text.emptyTrxDscd);
				return false;
			} else if (!msg.msgDscd) {
				this.popupService.simpleAlert(this.$scope, this.text.emptyMsgType);
				return false;
			} else if (msg.chlDscd == 'EXTERNAL' && (msg.msgVersion === undefined || msg.msgVersion === null)) {
				this.popupService.simpleAlert(this.$scope, this.text.emptyMsgVersion);
				return false;
			} else if (!msg.jobId && msg.chlDscd == 'EXTERNAL' && msg.trxDscd === 'BATCH') {
				this.popupService.simpleAlert(this.$scope, this.text.emptyFileId);
				return false;
			} else if (!msg.lv1Cd) {
				this.popupService.simpleAlert(this.$scope, this.text.emptyLvCdsStr);
				return false;
			} else if (records && records[0] && records[0].fldLvNo != 0) {
				this.popupService.simpleAlert(this.$scope, this.text.firstFieldDepth);
				return false;
			} else if (records.length === 0) {
				this.popupService.simpleAlert(this.$scope, this.text.emptyLayout);
				return false;
			}

			if (!this.checkLang(msg.msgNm)) {
				this.popupService.simpleAlert(this.$scope, this.text.msgNmEng);
				return false;
			}

			var pre = null;

			var _loop = function _loop(i) {
				var msglayout = records[i];

				// 영문명
				if (_.isEmpty(msglayout.fldEngNm)) {
					_this17.popupService.simpleAlert(_this17.$scope, _this17.text.order + ' [' + msglayout.msgSeq + '] ' + _this17.text.emptyFldEngNm);
					return {
						v: false
					};
				}

				// 한글명 
				if (_.isEmpty(msglayout.fldKorNm)) {
					_this17.popupService.simpleAlert(_this17.$scope, _this17.text.order + ' [' + msglayout.msgSeq + '] ' + _this17.text.emptyFldKorNm);
					return {
						v: false
					};
				}

				// 타입
				if (_.isEmpty(msglayout.dataTypeNm)) {
					_this17.popupService.simpleAlert(_this17.$scope, _this17.text.order + ' [' + msglayout.msgSeq + '] ' + _this17.text.emptyDataTypeNm);
					return {
						v: false
					};
				}

				// 길이
				if (msglayout.msgLen == undefined || _.isEmpty(String(msglayout.msgLen))) {
					_this17.popupService.simpleAlert(_this17.$scope, _this17.text.order + ' [' + msglayout.msgSeq + '] ' + _this17.text.emptyMsgLen);
					return {
						v: false
					};
				}

				// Depth
				if (msglayout.fldLvNo == undefined || _.isEmpty(String(msglayout.fldLvNo))) {
					_this17.popupService.simpleAlert(_this17.$scope, _this17.text.order + ' [' + msglayout.msgSeq + '] ' + _this17.text.emptyDepth);
					return {
						v: false
					};
				}

				//마스킹정보 체크(복사 후 등록 시 제대로 된 코드 값이 안들어감)
				checkPrivacy = _this17.getMaskNm(msglayout.privacyDscd);

				if (!checkPrivacy) {
					_this17.popupService.simpleAlert(_this17.$scope, _this17.text.order + ' [' + msglayout.msgSeq + '] ' + _this17.text.empthPrivacyDscd);
					return {
						v: false
					};
				}

				if (pre && msglayout.fldLvNo - pre.fldLvNo > 1) {
					_this17.popupService.simpleAlert(_this17.$scope, _this17.text.order + ' [' + msglayout.msgSeq + '] ' + _this17.text.increaseDepth);
					return {
						v: false
					};
				}
				pre = msglayout;

				// 배열참조
				/*
    if(msglayout.dataTypeNm === 'LAYOUT') {
    	if(msglayout.childDtoNm == undefined || msglayout.childDtoNm == ''){
    		this.popupService.simpleAlert(this.$scope,"Layout 타입에 하위IO명을 입력해주세요.");
    		return false;
    	}
    }
    */

				if (msglayout.dataTypeNm === 'LAYOUT' && msglayout.arraySizeRefVal != undefined && !_.isEmpty(String(msglayout.arraySizeRefVal))) {
					var isString = isNaN(Number(msglayout.arraySizeRefVal));

					if (isString) {
						// 문자열인 경우 해당 필드가 존재하는지 확인
						// 문자열인 경우 해당 필드가 INTEGER 타입인지 확인

						var record = records.filter(function (v) {
							return v.fldEngNm === msglayout.arraySizeRefVal;
						});

						if (record.length === 0) {
							_this17.popupService.simpleAlert(_this17.$scope, _this17.text.order + ' [' + msglayout.msgSeq + '] ' + _this17.text.arraySizeRefValMsg3);
							return {
								v: false
							};
						} else {
							if (record[0].dataTypeNm !== 'INTEGER') {
								_this17.popupService.simpleAlert(_this17.$scope, _this17.text.order + ' [' + msglayout.msgSeq + '] ' + _this17.text.arraySizeRefValMsg4);
								return {
									v: false
								};
							}
						}
					} else {
						// 상수인 경우 1보다 큰지 확인
						var num = Number(msglayout.arraySizeRefVal);

						if (num < 1) {
							_this17.popupService.simpleAlert(_this17.$scope, _this17.text.order + ' [' + msglayout.msgSeq + '] ' + _this17.text.arraySizeRefValMsg2);
							return {
								v: false
							};
						}
					}
				}
			};

			for (var i = 0; i < records.length; i++) {
				var checkPrivacy;

				var _ret = _loop(i);

				if ((typeof _ret === 'undefined' ? 'undefined' : (0, _typeof3.default)(_ret)) === "object") return _ret.v;
			}

			var duplicatedFieldResult = this.checkDuplicatedFieldName(records);

			if (duplicatedFieldResult.isDuplicated) {
				this.popupService.simpleAlert(this.$scope, this.text.order + ' [' + duplicatedFieldResult.duplicatedSeq1 + ',  ' + duplicatedFieldResult.duplicatedSeq2 + '] ' + this.text.orderDupFieldNm);
				return false;
			}

			var wrongTypeResult = this.checkLayoutType(records);

			if (wrongTypeResult.isWrong) {
				this.popupService.simpleAlert(this.$scope, this.text.order + ' [' + wrongTypeResult.wrongSeq + '] ' + this.text.orderDataTypeLayout);
				return false;
			}

			return true;
		}
	}, {
		key: 'checkDuplicatedFieldName',
		value: function checkDuplicatedFieldName(records) {
			var _this18 = this;

			var result = {
				isDuplicated: false,
				duplicatedSeq1: null,
				duplicatedSeq2: null
			};

			records.map(function (record) {
				return _this18.setParentFldNm(record);
			});

			var i = void 0;
			var j = void 0;
			var length = records.length;

			for (i = 0; i < length; i++) {
				var record = records[i];

				if (result.isDuplicated) {
					break;
				}

				for (j = 0; j < length; j++) {
					var compareRecord = records[j];

					if (record.msgSeq !== compareRecord.msgSeq) {

						if (record.fldLvNo == compareRecord.fldLvNo && record.parentFldNm === compareRecord.parentFldNm && record.fldEngNm === compareRecord.fldEngNm) {
							result = {
								isDuplicated: true,
								duplicatedSeq1: record.msgSeq,
								duplicatedSeq2: compareRecord.msgSeq
							};

							break;
						}
					}
				}
			}

			return result;
		}
	}, {
		key: 'checkLayoutType',
		value: function checkLayoutType(records) {
			var result = {
				isWrong: false,
				wrongSeq: null
			};
			var grid = w2ui[this.msgDetailOptions.name];

			var i = void 0;
			var length = records.length;

			var _loop2 = function _loop2() {
				var record = records[i];

				if (record.fldLvNo != 0 && record.parentFldNm) {
					var parentRecord = grid.records.filter(function (v) {
						return v.fldUnqId == record.parentFldNm;
					})[0];

					if (parentRecord && parentRecord.dataTypeNm !== 'LAYOUT') {
						result = {
							isWrong: true,
							wrongSeq: parentRecord.msgSeq
						};

						return 'break';
					}
				}
			};

			for (i = 0; i < length; i++) {
				var _ret2 = _loop2();

				if (_ret2 === 'break') break;
			}

			return result;
		}
	}, {
		key: 'addDetailRow',
		value: function addDetailRow() {
			if (!this.isEdit) return;
			this.gridService.addRecord(this.msgDetailOptions, this.generatorLayoutdtDto);
			this.reorderSeq(true);
		}
	}, {
		key: 'deleteDetailRow',
		value: function deleteDetailRow() {
			if (!this.isEdit) return;
			this.gridService.removeSelected(this.msgDetailOptions);
			this._selectedDetailRow = {};
			this.reorderSeq();
		}
	}, {
		key: 'upDetailRow',
		value: function upDetailRow() {
			if (!this.isEdit) return;
			this.gridService.upSelected(this.msgDetailOptions);
			this.reorderSeq();
		}
	}, {
		key: 'downDetailRow',
		value: function downDetailRow() {
			if (!this.isEdit) return;
			this.gridService.downSelected(this.msgDetailOptions);
			this.reorderSeq();
		}
	}, {
		key: 'reorderSeq',
		value: function reorderSeq(clear) {
			if (_.isEmpty(this.msgDetailOptions.name)) return;
			var grid = w2ui[this.msgDetailOptions.name];
			var gridRecords = grid.records;
			var $record = $(grid.box).find('.w2ui-grid-records');
			var scrollTop = $record.scrollTop();

			if (clear && grid.records.length >= 150) {
				grid.clear();
			}

			gridRecords.map(function (record, index) {
				record.msgSeq = index + 1;
			});
			grid.refresh();

			if (clear) {
				setTimeout(function () {
					grid.scrollIntoView(scrollTop);
				}, 300);
			}
		}
	}, {
		key: 'setMsgOption',
		value: function setMsgOption() {
			if (_.isEmpty(this.selectedMsg)) {
				this.msgTypes = [];
				return;
			}

			switch (this.selectedMsg.chlDscd) {
				case 'INTERNAL':
					switch (this.selectedMsg.trxDscd) {
						case 'ONLINE':
							this.msgTypes = this.allMsgType.filter(function (v) {
								return v.cdVal == 'IV' || v.cdVal == 'STH';
							});
							break;
						case 'BATCH':
							this.msgTypes = this.allMsgType.filter(function (v) {
								return v.cdVal == 'BATH' || v.cdVal == 'BATB' || v.cdVal == 'BATT';
							});
							break;
					}
					break;
				case 'EXTERNAL':
					switch (this.selectedMsg.trxDscd) {
						case 'ONLINE':
							this.msgTypes = this.allMsgType.filter(function (v) {
								return v.cdVal == 'IV' || v.cdVal == 'CH';
							});
							break;
						case 'BATCH':
							this.msgTypes = this.allMsgType.filter(function (v) {
								return v.cdVal == 'BATH' || v.cdVal == 'BATB' || v.cdVal == 'BATT';
							});
							break;
					}
					break;
			}

			this.changeMsgType();
		}
	}, {
		key: 'changeMsgType',
		value: function changeMsgType() {
			this.changeMsgDetailGridColumns(this.selectedMsg);
		}
	}, {
		key: 'xlsImport',
		value: function xlsImport() {
			var _this19 = this;

			this.popupService.openModal('SCR0503').then(function (res) {
				var data = res.data;

				_this19.setMsgDetailGridData(data.msglayoutbsDto);
				_this19.offEditMode();

				_this19.getGridData(true);
			}).catch(function () {});
		}
	}, {
		key: 'xlsExport',
		value: function xlsExport() {
			var _this20 = this;

			var grid = w2ui[this.msgOptions.name];
			var selections = grid.getSelection();
			var selectionLength = selections.length;

			if (selectionLength === 0) {
				this.popupService.simpleAlert(this.$scope, this.text.selectMsg);
				return;
			}

			selections.map(function (msgLayoutId, idx) {
				setTimeout(function () {
					_this20.excelExport(msgLayoutId, true, idx === 0, selectionLength === idx + 1);
				}, idx * 500);
			});
		}
	}, {
		key: 'impactAnalysis',
		value: function impactAnalysis() {
			if (_.isEmpty(this.selectedMsg)) return;
			this.popupService.openModal('SCR1802', { msgLayoutId: this.selectedMsg.msgLayoutId }).then(function () {}).catch(function () {});
		}
	}, {
		key: 'metaValidate',
		value: function metaValidate(isNotShowConfirmMsg) {
			var _this21 = this;

			var grid = w2ui[this.msgDetailOptions.name];
			this.validateCnt = 0;

			grid.save();
			grid.records.map(function (v) {
				return _this21.rowMetaValid(v);
			});

			if (this.validateCnt !== 0) {
				var txt = this.text.discordMetaValidate;
				this.validateCnt !== 0 && (txt += '<br>' + this.text.discord + ' <span class="chr-c-orange">' + this.validateCnt + '</span>' + this.text.cnt);
				this.popupService.simpleAlert(this.$scope, txt);
			} else {
				if (!isNotShowConfirmMsg) {
					this.popupService.simpleAlert(this.$scope, this.text.completeMetaValidate);
				}
			}

			return this.validateCnt === 0;
		}
	}, {
		key: 'rowMetaValid',
		value: function rowMetaValid(row, validateCnt) {
			var meta = this.metaService.getMetaListSameKorNm(row.fldKorNm)[0];

			if (row.dataTypeNm === 'LAYOUT') {
				row.confirmMeta = this.text.excludeTarget;
			} else {
				//
				if (meta && meta.metaEngNm == row.fldEngNm && meta.metaKorNm == row.fldKorNm && meta.metaLen == row.msgLen && meta.decimalLen == row.decimalLen && meta.dataTypeNm == row.dataTypeNm && row.dataTypeNm !== 'BYTEARRAY' || meta && meta.metaEngNm == row.fldEngNm && meta.metaKorNm == row.fldKorNm && meta.decimalLen == row.decimalLen && meta.dataTypeNm == row.dataTypeNm && row.dataTypeNm === 'BYTEARRAY') {
					row.encYn = meta.metaEncYn;
					row.confirmMeta = this.text.accord;
				} else {
					///불일치일때 숫자로 끝나면 숫자 빼고 다시 메타 검증

					var matchesFldKorNm = row.fldKorNm ? row.fldKorNm.match(/\d+$/) : null;
					var matchesFldEngNm = row.fldEngNm ? row.fldEngNm.match(/\d+$/) : null;

					if (matchesFldKorNm && matchesFldEngNm && matchesFldKorNm[0] === matchesFldEngNm[0]) {
						meta = this.metaService.getMetaListSameKorNm(row.fldKorNm.substr(0, row.fldKorNm.indexOf(matchesFldKorNm[0])))[0];

						if (meta && meta.metaEngNm == row.fldEngNm.substr(0, row.fldEngNm.indexOf(matchesFldEngNm[0])) && meta.metaKorNm == row.fldKorNm.substr(0, row.fldKorNm.indexOf(matchesFldKorNm[0])) && meta.metaLen == row.msgLen && meta.decimalLen == row.decimalLen && meta.dataTypeNm == row.dataTypeNm && row.dataTypeNm !== 'BYTEARRAY' || meta && meta.metaEngNm == row.fldEngNm.substr(0, row.fldEngNm.indexOf(matchesFldEngNm[0])) && meta.metaKorNm == row.fldKorNm.substr(0, row.fldKorNm.indexOf(matchesFldKorNm[0])) && meta.decimalLen == row.decimalLen && meta.dataTypeNm == row.dataTypeNm && row.dataTypeNm === 'BYTEARRAY') {
							row.confirmMeta = this.text.accord;
						} else {
							this.validateCnt++;
							row.confirmMeta = this.text.discord;
						}
					} else {
						this.validateCnt++;
						row.confirmMeta = this.text.discord;
					}
				}
			}
		}
	}, {
		key: 'setParentFldNm',
		value: function setParentFldNm(record, isFldLvNoChanged) {
			var grid = w2ui[this.msgDetailOptions.name];

			var index = grid.records.indexOf(record);
			var fldLvNo = isFldLvNoChanged ? record.w2ui.changes.fldLvNo : record.fldLvNo;

			if (fldLvNo == 0) {
				record.parentFldNm = null;
				return;
			}

			for (var i = index; i >= 0; i--) {
				var tempRecord = grid.records[i];

				if (tempRecord === record) {
					continue;
				}

				if (tempRecord.fldLvNo == fldLvNo - 1) {
					record.parentFldNm = tempRecord.fldUnqId;
					break;
				}
			}
		}
	}, {
		key: 'fieldNameValid',
		value: function fieldNameValid(row) {
			var grid = w2ui[this.msgDetailOptions.name];

			if (_.isEmpty(row.fldEngNm)) {
				return true;
			}

			var records = grid.records.filter(function (record) {
				if (row.fldLvNo === 0) {
					return row.fldLvNo === record.fldLvNo && row.fldEngNm === record.fldEngNm;
				} else {
					return row.fldLvNo === record.fldLvNo && row.parentFldNm === record.parentFldNm && row.fldEngNm === record.fldEngNm;
				}
			});

			if (records.length > 0) return false;
			return true;
		}
	}, {
		key: 'openApplicationCodeModal',
		value: function openApplicationCodeModal() {
			var _this22 = this;

			if (!this.isEdit) return;
			this.popupService.openModal('SCR1402').then(function (code) {
				_this22.selectedMsg.lv1Cd = code.appCd;
			}).catch(function () {});
		}
	}, {
		key: 'openApplicationCodeModalForSearchParam',
		value: function openApplicationCodeModalForSearchParam() {
			var _this23 = this;

			this.popupService.openModal('SCR1402').then(function (code) {
				_this23.searchParam.lv1Cd = code.appCd;
			}).catch(function () {});
		}
	}, {
		key: 'openRegManPopup',
		value: function openRegManPopup() {
			var _this24 = this;

			this.popupService.openModal('SCR0102').then(function (user) {
				return _this24.searchParam.regManId = user.userId;
			}).catch(function () {});
		}
	}, {
		key: 'openMsglayoutPopup',
		value: function openMsglayoutPopup() {
			var _this25 = this;

			if (!this.isEdit) return;
			this.popupService.openModal('SCR0502', { getDetail: true, multiSelect: false }).then(function (msglayout) {
				return _this25.msgDetailOptions.records = msglayout[0].msglayoutdtDto;
			}).catch(function () {});
		}
	}, {
		key: 'openMsgLayoutIdCreatePopup',
		value: function openMsgLayoutIdCreatePopup() {
			var _this26 = this;

			this.popupService.openModal('SCR0504').then(function (addMsg) {

				_this26.selectedMsg.intrfcId = addMsg.intrfcId;
				_this26.selectedMsg.lv1Cd = addMsg.lv1Cd;
				_this26.selectedMsg.chlDscd = addMsg.chlDscd;
				_this26.selectedMsg.trxDscd = addMsg.trxDscd;
				_this26.selectedMsg.msgDscd = addMsg.msgDscd;
				_this26.selectedMsg.rsrvFldVal1 = addMsg.msgLayoutId.substring(1, 4);
				_this26.selectedMsg.rsrvFldVal2 = addMsg.msgLayoutId.substring(4, 7);
				_this26.selectedMsg.msgVersion = addMsg.msgVersion;
				_this26.selectedMsg.jobId = addMsg.fileId;
				_this26.selectedMsg.msgDataVal = addMsg.msgDataVal;
				_this26.selectedMsg.msgLayoutId = addMsg.msgLayoutId;

				if (_this26.proFepSysCd.includes(_this26.selectedMsg.rsrvFldVal1) || _this26.proFepSysCd.includes(_this26.selectedMsg.rsrvFldVal2)) {
					_this26.isProFEP = true;
				} else {
					_this26.isProFEP = false;
				}

				_this26.setMsgOption();
			}).catch(function () {});
		}
	}, {
		key: 'setRegDttm',
		value: function setRegDttm() {
			var regDttm = this.selectedMsg.regDttm;

			if (regDttm) {
				this.selectedMsg.regDttmString = this.utilService.setRegDttm(regDttm);
			}
		}
	}, {
		key: 'refreshGrid',
		value: function refreshGrid() {
			var grid = w2ui[this.msgOptions.name];
			grid.refresh();
		}
	}, {
		key: 'syncMeta',
		value: function syncMeta() {
			var _this27 = this;

			this.popupService.showLoadingBar(this.$scope);
			this.metaService.getMeta().finally(function () {
				_this27.popupService.closeLoadingBar();
				_this27.popupService.simpleAlert(_this27.$scope, _this27.text.completeSyncMeta);
			});
		}
	}, {
		key: 'syncField',
		value: function syncField() {
			var _this28 = this;

			var grid = w2ui[this.msgDetailOptions.name];
			grid.save();

			this.msgDetailOptions.records.map(function (record) {
				if (record.dataTypeNm === 'LAYOUT') {
					_this28.rowMetaValid(record);
				} else {
					var metaData = _this28.metaService.getMetaListSameKorNm(record.fldKorNm);

					if (metaData.length === 1) {
						metaData = metaData[0];

						record.fldEngNm = metaData.metaEngNm;
						record.dataTypeNm !== 'BYTEARRAY' && (record.msgLen = metaData.metaLen);
						record.decimalLen = metaData.decimalLen;
						record.dataTypeNm = metaData.dataTypeNm;
						record.alignNm = _this28.getAlignNmByDataType(metaData.dataTypeNm);
						record.confirmMeta = _this28.text.accord;
					} else {
						_this28.rowMetaValid(record);
					}
				}
			});

			grid.refresh();
			this.popupService.simpleAlert(this.$scope, this.text.completeSyncField);
		}
	}]);
	return SCR0501Controller;
}();

(0, _angular.module)(_app2.default.name).controller('SCR0501Controller', SCR0501Controller);

/***/ })

},[57]);
//# sourceMappingURL=SCR0501.controller.js.map