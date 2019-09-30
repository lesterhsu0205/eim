webpackJsonp(["app\\views\\popup\\SCR0707\\SCR0707.controller"],{

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

/***/ "1b00bab8bc05a32822b5":
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

var SCR0704Controller = function () {
	function SCR0704Controller($scope, $uibModalInstance, $timeout, httpService, utilService, codeService, gridService, popupService, userService, data) {
		var _this = this;

		(0, _classCallCheck3.default)(this, SCR0704Controller);

		this.$scope = $scope;
		this.$uibModalInstance = $uibModalInstance;
		this.$timeout = $timeout;
		this.httpService = httpService;
		this.utilService = utilService;
		this.gridService = gridService;
		this.popupService = popupService;
		this.codeService = codeService;
		this.userService = userService;
		this.data = data;
		this.codes = codeService.commonCodes;
		this.msglayoutbsDtoMap = {};
		this.allMsgType = data.codes.MSG_TYPE;
		this.user = this.userService.getUser();

		this.initWindow('100%', '100%');
		this.initText();
		this.initGridOption();

		this.$scope.$on('gridRendered', function () {
			_this.getMsgDetailGridData();
		});

		var resizeHandler = function resizeHandler() {
			_this.$timeout(function () {
				_this.msgDetailOptions.name && w2ui[_this.msgDetailOptions.name].refresh();
			});
		};

		$(window).on('resize', resizeHandler);

		this.$scope.$on('$destroy', function () {
			$(window).off('resize', resizeHandler);
		});
	}

	(0, _createClass3.default)(SCR0704Controller, [{
		key: 'initWindow',
		value: function initWindow(width, height) {
			this.width = width;
			this.height = height;
			this.top = 100;
			this.left = 20;
			this.right = 20;
			this.zIndex = this.popupService.getModalZIndex();
		}
	}, {
		key: 'initText',
		value: function initText() {
			this.text = $.extend({}, bxMsg.getMessages('common'), bxMsg.getMessages('manageMsg'));
		}
	}, {
		key: 'initGridOption',
		value: function initGridOption() {
			var _this2 = this;

			this.msgDetailOptions = {
				limit: 99999,
				pageSize: 99999,
				recordsCount: 0,
				recid: 'fldUnqId',
				columns: this._getDefaultMsgDetailGridColumns(),
				onClick: function onClick(e) {
					// prevent deselect
					var selection = w2ui[e.target].getSelection();
					if (selection.length === 1 && selection[0] === e.recid) {
						e.preventDefault();
					}

					var grid = w2ui[_this2.msgDetailOptions.name];
					var row = grid.get(e.recid);

					if (row.msgDscd && row.msgLayoutId) {
						console.log(_this2.msglayoutbsDtoMap[row.msgDscd + ':' + row.msgLayoutId]);

						_this2.selectedMsg = _this2.msglayoutbsDtoMap[row.msgDscd + ':' + row.msgLayoutId];
						_this2._selectedMsg = _this2.msglayoutbsDtoMap[row.msgDscd + ':' + row.msgLayoutId];

						_this2.changeTransactionType();
						_this2.setRegDttm();

						_this2.$scope.$apply();
					}
				},
				onDblClick: function onDblClick(e) {
					var grid = w2ui[_this2.msgDetailOptions.name];
					var row = grid.get(e.recid);

					if (row.msgDscd) {
						e.preventDefault();
					}
				}
			};
		}
	}, {
		key: '_getDefaultMsgDetailGridColumns',
		value: function _getDefaultMsgDetailGridColumns() {
			var _this3 = this;

			var columns = [{
				field: 'msgDscd', caption: this.text.msgType, size: '100px', frozen: true, attr: "align=left",
				render: function render(data, index, colIndex) {
					var msgDscd = void 0;

					if (data && data.msgDscd) {
						msgDscd = data.msgDscd;
					}

					return _this3.codeService.getCodeValNm('MSG_TYPE', msgDscd);
				}
			}, { field: 'msgSeq', caption: 'seq', size: '40px', frozen: true }, { field: 'fldEngNm', caption: this.text.fldEngNm, size: '2%', frozen: true, attr: "align=left",
				render: function render(data) {
					var spaceTemp = '&nbsp;&nbsp;&nbsp;&nbsp;';
					var fldEngNm = '';

					if (data.w2ui && data.w2ui.changes && data.w2ui.changes.fldEngNm) {
						fldEngNm = data.w2ui.changes.fldEngNm ? data.w2ui.changes.fldEngNm : '';
					} else {
						fldEngNm = data.fldEngNm ? data.fldEngNm : '';
					}

					var space = '';
					if (data.fldLvNo == 0) {
						return fldEngNm;
					} else {
						for (var i = 0; i < data.fldLvNo; i++) {
							space = space.concat(spaceTemp);
						}
						return space + fldEngNm;
					}
				}
			}, { field: 'fldKorNm', caption: this.text.fldKorNm, attr: "align=left", size: '2%', frozen: true }, {
				field: 'dataTypeNm', caption: this.text.dataType, size: '80px', frozen: true,
				render: function render(data, index, colIndex) {
					var dataTypeNm = w2ui[_this3.msgDetailOptions.name].getCellValue(index, colIndex);
					return _this3.codeService.getCodeValNm(_this3.codes.DATA_TYPE, dataTypeNm);
				}

			}, { field: 'msgLen', caption: this.text.msgLen, size: this.user.locale === 'en' ? '50px' : '45px', frozen: true }, { field: 'decimalLen', caption: this.text.decimalLen, size: this.user.locale === 'en' ? '110px' : '45px', frozen: true }, { field: 'fldLvNo', caption: this.text.fldLvNo, size: '50px', frozen: true, style: 'border-right: 0.5px solid black' }, { field: 'childDtoNm', caption: this.text.childDtoNm, size: '1%' }, { field: 'arraySizeRefVal', caption: this.text.arraySizeRefVal, size: '1%' }, {
				field: 'alignNm', caption: this.text.alignNm, size: this.user.locale === 'en' ? '70px' : '40px',
				render: function render(data, index, colIndex) {
					var alignNm = w2ui[_this3.msgDetailOptions.name].getCellValue(index, colIndex);
					return _this3.codeService.getCodeValNm(_this3.codes.ALIGN_CD, alignNm);
				}
			}, {
				field: 'fillerVal', caption: this.text.fillerVal, size: '60px',
				editable: this.isEdit ? { type: 'select', items: this.gridService.getSelectItemsFromCodes(this.codes.FILLER_CD, [{ id: "", text: "" }]) } : false,
				render: function render(data, index, colIndex) {
					var fillerVal = w2ui[_this3.msgDetailOptions.name].getCellValue(index, colIndex);
					return _this3.codeService.getCodeValNm(_this3.codes.FILLER_CD, fillerVal);
				}
			}, {
				field: 'fldRmk', caption: this.text.fldRmk, size: '2%', attr: "align=left",
				render: function render(data, index, colIndex) {
					return data.fldRmk ? '<span title="' + data.fldRmk + '">' + data.fldRmk + '</span>' : '';
				}
			}, { field: 'confirmMeta', caption: this.text.confirmMeta, size: this.user.locale === 'en' ? '110px' : '60px' }];

			return columns;
		}
	}, {
		key: 'getMsgDetailGridData',
		value: function getMsgDetailGridData() {
			var _this4 = this;

			if (this.data.gridDataList) {
				this.displayMsgDetailGridData({ msglayoutbsDtoList: this.data.gridDataList });
			} else {
				if (this.data.msgLayoutId.length === 0) {
					return;
				}

				this.httpService.post('/msglayoutslist', this.data.msgLayoutId).then(function (res) {
					_this4.displayMsgDetailGridData(res);
				});
			}
		}
	}, {
		key: 'displayMsgDetailGridData',
		value: function displayMsgDetailGridData(res) {
			var _this5 = this;

			var selectedFldUnqId = void 0;
			var selectedDetail = res.msglayoutbsDtoList[0];

			var gridData = [];
			var calcData = [];

			var calcLengthTxt = '';

			res.msglayoutbsDtoList.map(function (data, idx) {
				_this5.msglayoutbsDtoMap[data.msgDscd + ':' + data.msgLayoutId] = data;

				var fldUnqId = _this5.utilService.uniqueId('TEMP_LAYOUT_DD_DTO_ID');

				gridData.push({
					fldUnqId: fldUnqId,
					msgLayoutId: data.msgLayoutId,
					msgDscd: data.msgDscd,
					w2ui: {
						children: data.msglayoutdtDto
					}
				});

				calcData = calcData.concat(data.msglayoutdtDto);

				if (idx === 0) {
					selectedFldUnqId = fldUnqId;
				}

				if (_this5.data.trxDscd === 'ONLINE' && data.msgDscd === 'IV') {
					selectedFldUnqId = fldUnqId;
					selectedDetail = data;
				} else if (_this5.data.trxDscd === 'BATCH' && data.msgDscd === 'BATB') {
					selectedFldUnqId = fldUnqId;
					selectedDetail = data;
				}

				calcLengthTxt += '<span style="margin-right: 15px !important;">' + _this5.codeService.getCodeValNm('MSG_TYPE', data.msgDscd) + ' : ' + _this5.calcLength(data.msglayoutdtDto) + '</span>';
			});

			if (this.data.trxDscd === 'ONLINE') {
				$('.msg-length-wrap').html(this.text.totalLength + ' : ' + this.calcLength(calcData));
			} else {
				$('.msg-length-wrap').html(calcLengthTxt);
			}

			this.msgDetailOptions.records = gridData;

			this.selectedMsg = this.utilService.clone(selectedDetail);
			this._selectedMsg = this.utilService.clone(selectedDetail);

			this.changeTransactionType();
			this.setRegDttm();

			var grid = w2ui[this.msgDetailOptions.name];
			setTimeout(function () {
				grid.expand(selectedFldUnqId);
			}, 300);
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
		key: 'changeTransactionType',
		value: function changeTransactionType() {
			if (_.isEmpty(this.selectedMsg)) {
				this.msgTypes = [];
				return;
			}

			switch (this.selectedMsg.chlDscd) {
				case 'INTERNAL':
					switch (this.selectedMsg.trxDscd) {
						case 'ONLINE':
							this.msgTypes = this.allMsgType.filter(function (v) {
								return v.cdVal == 'IV' || v.cdVal == 'CH' || v.cdVal == 'STH';
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
		}
	}, {
		key: 'calcLength',
		value: function calcLength(records) {
			var record = this.utilService.clone(records);

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

			return msgLen;
		}
	}, {
		key: 'closeModal',
		value: function closeModal(isOk) {
			var grid = w2ui[this.msgDetailOptions.name];

			if (isOk) {
				this.$uibModalInstance.close();
			} else {
				this.$uibModalInstance.dismiss();
			}

			setTimeout(function () {
				return grid.destroy();
			});
		}
	}]);
	return SCR0704Controller;
}();

(0, _angular.module)(_app2.default.name).controller('SCR0707Controller', SCR0704Controller);

/***/ }),

/***/ 34:
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__("1b00bab8bc05a32822b5");


/***/ })

},[34]);
//# sourceMappingURL=SCR0707.controller.js.map