webpackJsonp(["app\\views\\popup\\SCR0704\\SCR0704.controller"],{

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

/***/ 32:
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__("5395be6e21761fec11b8");


/***/ }),

/***/ "5395be6e21761fec11b8":
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
		this.user = this.userService.getUser();

		console.log(this.codes);

		this.allMsgType = data.codes.MSG_TYPE;

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
			//		const { top, left } = this.popupService.calculatePosition(width,height);

			this.width = width;
			this.height = height;
			this.top = 100;
			this.left = 50;
			this.right = 50;
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
			this.msgDetailOptions = {
				limit: 99999,
				pageSize: 99999,
				recordsCount: 0,
				selectType: 'cell',
				recid: 'fldUnqId',
				onClick: function onClick(e) {
					// prevent deselect
					var selection = w2ui[e.target].getSelection();
					if (selection.length === 1 && selection[0] === e.recid) {
						e.preventDefault();
					}
				}
			};
		}
	}, {
		key: 'getMaskCodes',
		value: function getMaskCodes() {
			var _this2 = this;

			var defaultArray = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : [];


			this.httpService.get('maskCd').then(function (data) {
				var records = data.maskOutList,
				    recordsCount = data.totalCnt;


				_this2.maskCd = records;

				_this2.maskCd.map(function (records) {
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
		key: '_getDefaultMsgDetailGridColumns',
		value: function _getDefaultMsgDetailGridColumns(msg) {
			var _this3 = this;

			var columns = [{ field: 'msgSeq', caption: 'seq', size: '40px', frozen: true }, { field: 'fldEngNm', caption: this.text.fldEngNm, size: '2%', frozen: true, attr: "align=left",
				render: function render(data) {
					var spaceTemp = '&nbsp;&nbsp;&nbsp;&nbsp;';
					var fldEngNm;

					if (data.w2ui && data.w2ui.changes && data.w2ui.changes.fldEngNm) {
						fldEngNm = data.w2ui.changes.fldEngNm;
					} else {
						fldEngNm = data.fldEngNm;
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
				},
				editable: this.isEdit ? { type: 'text' } : false
			}, { field: 'fldKorNm', caption: this.text.fldKorNm, attr: "align=left", size: '2%', frozen: true, editable: this.isEdit ? { type: 'text' } : false }, {
				field: 'dataTypeNm', caption: this.text.dataType, size: '80px', frozen: true,
				editable: this.isEdit ? {
					type: 'select',
					items: this.gridService.getSelectItemsFromCodes(this.codes.DATA_TYPE)
				} : false,
				render: function render(data, index, colIndex) {
					var dataTypeNm = w2ui[_this3.msgDetailOptions.name].getCellValue(index, colIndex);
					return _this3.codeService.getCodeValNm(_this3.codes.DATA_TYPE, dataTypeNm);
				}

			}, { field: 'msgLen', caption: this.text.msgLen, size: this.user.locale === 'en' ? '50px' : '45px', frozen: true, editable: this.isEdit ? { type: 'int' } : false }, { field: 'decimalLen', caption: this.text.decimalLen, size: this.user.locale === 'en' ? '110px' : '45px', frozen: true, editable: this.isEdit ? { type: 'int' } : false }, { field: 'fldLvNo', caption: this.text.fldLvNo, size: '50px', frozen: true, style: 'border-right: 0.5px solid black', editable: this.isEdit ? { type: 'int' } : false }, { field: 'childDtoNm', caption: this.text.childDtoNm, size: '1%', editable: this.isEdit ? { type: 'text' } : false }, { field: 'arraySizeRefVal', caption: this.text.arraySizeRefVal, size: '1%', editable: this.isEdit ? { type: 'text' } : false }, {
				field: 'privacyDscd', caption: this.text.privacyDscd, size: '1%', type: 'select', items: this.getMaskCodes(),
				render: function render(data, index, colIndex) {
					var privacyDscd = w2ui[_this3.msgDetailOptions.name].getCellValue(index, colIndex);
					return _this3.getMaskNm(privacyDscd);
				}
			}, { field: 'encYn', caption: this.text.encYn, size: '1%', editable: { type: 'select', items: this.gridService.getSelectItemsFromCodes(this.codes.YN_CD) } }, {
				field: 'alignNm', caption: this.text.alignNm, size: this.user.locale === 'en' ? '70px' : '40px',
				editable: this.isEdit ? { type: 'select', items: this.gridService.getSelectItemsFromCodes(this.codes.ALIGN_CD, [{ id: "NONE", text: " " }]) } : false,
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
				field: 'fldRmk', caption: this.text.fldRmk, size: '2%', editable: this.isEdit ? { type: 'text' } : false, attr: "align=left",
				render: function render(data, index, colIndex) {
					return data.fldRmk ? '<span title="' + data.fldRmk + '">' + data.fldRmk + '</span>' : '';
				}
			}];

			columns.push({ field: 'confirmMeta', caption: this.text.confirmMeta, size: this.user.locale === 'en' ? '110px' : '60px' });

			return columns;
		}
	}, {
		key: 'changeMsgDetailGridColumns',
		value: function changeMsgDetailGridColumns(msg) {
			var columns = this._getDefaultMsgDetailGridColumns(msg);

			if (!_.isEmpty(msg)) {
				switch (msg.msgDscd) {
					case 'CH':
						if (msg.chlDscd === 'EXTERNAL') {
							columns.push.apply(columns, [{ field: 'extrnlMsgNoYn', caption: this.text.extrnlMsgNoYn, size: '80px' }, { field: 'extrnlSrchKeyYn', caption: this.text.extrnlSrchKeyYn, size: '80px' }]);
						}
						break;
					case 'IV':
						if (msg.chlDscd === 'EXTERNAL') {
							columns.push.apply(columns, [{ field: 'extrnlSrchKeyYn', caption: this.text.extrnlSrchKeyYn, size: '80px', editable: this.isEdit ? { type: 'select', items: this.gridService.getSelectItemsFromCodes(this.codes.YN_CD) } : false }]);
						}
						break;
				}
			}

			this.msgDetailOptions.columns = columns;
		}
	}, {
		key: 'getMsgDetailGridData',
		value: function getMsgDetailGridData() {
			var _this4 = this;

			if (this.data.gridData) {
				this.displayMsgDetailGridData(this.data.gridData);
			} else {
				var encodedId = encodeURIComponent(this.data.msgLayoutId);

				this.httpService.get('/msglayouts/' + encodedId).then(function (res) {
					_this4.displayMsgDetailGridData(res);
				});
			}
		}
	}, {
		key: 'displayMsgDetailGridData',
		value: function displayMsgDetailGridData(res) {
			var _this5 = this;

			var utilService = this.utilService;
			var originRecords = res.msglayoutdtDto;


			this.changeMsgDetailGridColumns(res);

			this.selectedMsg = utilService.clone(res);
			this._selectedMsg = utilService.clone(res);

			this.changeTransactionType();
			this.setRegDttm();

			this.msgDetailOptions.recordsCount = originRecords.length;
			this.msgDetailOptions.records = originRecords.map(function (record) {
				record.fldUnqId = record.fldUnqId ? record.fldUnqId : _this5.utilService.uniqueId('TEMP_LAYOUT_DD_DTO_ID');
				return record;
			});

			if (this.selectedMsg.rsrvFldVal3) {
				this.msgLength = this.selectedMsg.rsrvFldVal3;
			} else {
				this.calcLength(this.msgDetailOptions.records);
			}
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
		value: function calcLength(record) {
			var msgLen = 0;

			var layoutLength = new Array();

			for (var i = 0; i < record.length; i++) {

				if (record[i].dataTypeNm == "LAYOUT") {
					var data = new Object();

					data.id = record[i].fldUnqId;

					if (record[i].arraySizeRefVal == undefined || isNaN(record[i].arraySizeRefVal)) {
						data.msgLen = 1;
					} else if (record[i].arraySizeRefVal != undefined && record[i].parentFldNm == undefined) {
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
		key: 'closeModal',
		value: function closeModal(isOk) {
			var _this6 = this;

			if (isOk) {
				this.$uibModalInstance.close();
			} else {
				this.$uibModalInstance.dismiss();
			}

			setTimeout(function () {
				return w2ui[_this6.msgDetailOptions.name].destroy();
			});
		}
	}]);
	return SCR0704Controller;
}();

(0, _angular.module)(_app2.default.name).controller('SCR0704Controller', SCR0704Controller);

/***/ })

},[32]);
//# sourceMappingURL=SCR0704.controller.js.map