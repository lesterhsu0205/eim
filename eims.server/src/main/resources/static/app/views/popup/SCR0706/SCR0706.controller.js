webpackJsonp(["app\\views\\popup\\SCR0706\\SCR0706.controller"],{

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

/***/ 33:
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__("9450d29091c0b4d0c1ec");


/***/ }),

/***/ "9450d29091c0b4d0c1ec":
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

var SCR0706Controller = function () {
	function SCR0706Controller($scope, $uibModalInstance, $timeout, httpService, utilService, gridService, popupService, userService, data) {
		(0, _classCallCheck3.default)(this, SCR0706Controller);

		this.$scope = $scope;
		this.$uibModalInstance = $uibModalInstance;
		this.$timeout = $timeout;
		this.httpService = httpService;
		this.gridService = gridService;
		this.utilService = utilService;
		this.popupService = popupService;
		this.userService = userService;
		this.user = this.userService.getUser();
		this.data = data;

		this.initWindow('100%', '100%');
		this.initText();
		this.initSelect();
		this.initGridOption();
		this.getDeployList();
		this.getMsglayout();

		$timeout(function () {
			$('#oldVersionOptions').on('scroll', function () {
				$('#currentVersionOptions').find('.w2ui-grid-records').scrollTop($('#oldVersionOptions').find('.w2ui-grid-records').scrollTop());
			});
			$('#currentVersionOptions').on('scroll', function () {
				$('#oldVersionOptions').find('.w2ui-grid-records').scrollTop($('#currentVersionOptions').find('.w2ui-grid-records').scrollTop());
			});
		});
	}

	(0, _createClass3.default)(SCR0706Controller, [{
		key: 'initWindow',
		value: function initWindow(width, height) {
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
			this.text = $.extend({}, bxMsg.getMessages('common'), bxMsg.getMessages('manageMsg'), bxMsg.getMessages('manageDeploySystem'));
		}
	}, {
		key: 'initSelect',
		value: function initSelect() {
			this.select = this.gridService.getSelect(5);
		}
	}, {
		key: 'initGridOption',
		value: function initGridOption() {
			var _this = this;

			this.gridHeight = this.gridService.getGridHeight(this.select.pageSize);
			this.deployOptions = {
				limit: this.select.pageSize,
				pageSize: this.select.pageSize,
				recordsCount: 0,
				recid: 'deployVersion',
				columns: [{ field: 'deployVersion', caption: this.text.deployVersion, size: this.user.locale === 'en' ? '120px' : '80px' }, {
					field: 'deployDttm', caption: this.text.deployDttm,
					render: function render(data) {
						return _this.utilService.setRegDttm(data.deployDttm);
					}
				}, { field: 'deploySysCd', caption: this.text.deploySys }],
				onClick: function onClick(e) {
					// prevent deselect
					var selection = w2ui[e.target].getSelection();
					if (selection.length === 1 && selection[0] === e.recid) {
						e.preventDefault();
					}

					_this.getLayoutDiff(e.recid);
				}
			};

			this.oldVersionOptions = {
				limit: 999999,
				pageSize: 999999,
				columns: this.getVersionColumns(),
				recordsCount: 0,
				recid: 'fldUnqId'
			};

			this.currentVersionOptions = {
				limit: 999999,
				pageSize: 999999,
				columns: this.getVersionColumns(),
				recordsCount: 0,
				recid: 'fldUnqId'
			};
		}
	}, {
		key: 'getVersionColumns',
		value: function getVersionColumns() {
			return [{
				field: 'fldEngNm', caption: this.text.fldEngNm, size: '3%', attr: 'align=left',
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
				}
			}, { field: 'fldKorNm', caption: this.text.fldKorNm, size: '2%', attr: 'align=left' }, { field: 'dataTypeNm', caption: this.text.dataType, size: '2%' }, { field: 'fldLvNo', caption: this.text.fldLvNo, size: '1%' }, { field: 'arraySizeRefVal', caption: this.text.arraySizeRefVal, size: this.user.locale === 'en' ? '1.7%' : '1.5%' }, { field: 'msgLen', caption: this.text.msgLen, size: '1%' }];
		}
	}, {
		key: 'getDeployList',
		value: function getDeployList() {
			var _this2 = this;

			var goToFirst = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : false;

			var _getPageInfo = this.getPageInfo(),
			    pageNumber = _getPageInfo.pageNumber,
			    pageSize = _getPageInfo.pageSize;

			var url = '/intrfccoms/deployhistorys?\n\t\t\t\tdeployResultCd=SUCCESS\n\t\t\t\t&pageNumber=' + (goToFirst ? 1 : pageNumber) + '\n\t\t\t\t&pageSize=' + pageSize + '\n\t\t\t\t&intrfcId=' + this.data.intrfcId;

			this.httpService.get(url).then(function (data) {
				var records = data.intrfcdeployhisthsOutList,
				    recordsCount = data.totalCnt;


				_this2.deployOptions.records = records;
				_this2.deployOptions.recordsCount = recordsCount;

				if (goToFirst) {
					_this2.pageNumber = 1;
					_this2.$scope.$broadcast('resetPage', _this2.pageNumber);
				}
			});
		}
	}, {
		key: 'getMsglayout',
		value: function getMsglayout() {
			var _this3 = this;

			var msgLayoutId = encodeURIComponent(this.data.msgLayoutId);
			this.httpService.get('/msglayouts/' + msgLayoutId).then(function (res) {
				var records = res.msglayoutdtDto,
				    msgRvsNo = res.msgRvsNo;


				_this3.currentVersionRecords = records;
				_this3.currentVersionOptions.records = records;
				_this3.currentVersionOptions.msgRvsNo = msgRvsNo;
			});
		}
	}, {
		key: 'getLayoutDiff',
		value: function getLayoutDiff(deployVersion) {
			var _this4 = this;

			var data = this.data;
			var url = '/intrfccoms/layoutdiff?\n\t\t\tintrfcId=' + data.intrfcId + '\n\t\t\t&msgLayoutId=' + data.msgLayoutId + '\n\t\t\t&srTypeCd=' + data.srTypeCd + '\n\t\t\t&rqstRspsTypeCd=' + data.rqstRspsTypeCd + '\n\t\t\t&layoutSeq=' + data.layoutSeq + '\n\t\t\t&deployVersion=' + deployVersion;

			this.httpService.get(url).then(function (data) {
				if (data.isError) {
					_this4.oldVersionOptions.records = [];
					_this4.popupService.simpleAlert(_this4.$scope, data.data.message);
					return;
				}

				_this4.oldVersionOptions.records = data.msglayoutdtDto;
				_this4.oldVersionOptions.msgRvsNo = data.msgRvsNo;
				_this4.currentVersionOptions.records = _this4.utilService.clone(_this4.currentVersionRecords);

				setTimeout(function () {
					return _this4.mapping();
				}, 300);
			});
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
		key: 'mapping',
		value: function mapping() {
			var _this5 = this;

			var trgGrid = w2ui[this.currentVersionOptions.name];
			var srcGrid = w2ui[this.oldVersionOptions.name];

			if (this.currentVersionOptions.records.length === 0) return;

			var i = 0;

			var _loop = function _loop() {
				var record = _this5.currentVersionOptions.records[i];
				var fldEngNm = record.fldEngNm;

				if (fldEngNm) {
					var trgPos = _this5.currentVersionOptions.records.indexOf(record);
					var _srcRecord = _this5.oldVersionOptions.records.filter(function (rec) {
						return rec.fldEngNm === fldEngNm;
					})[0];

					if (!_.isEmpty(_srcRecord)) {
						var srcPos = _this5.oldVersionOptions.records.indexOf(_srcRecord);

						if (trgPos !== srcPos) {
							var diffPos = srcPos - trgPos;
							for (var _j = 0; _j < diffPos; _j++) {
								_this5.currentVersionOptions.records.splice(trgPos, 0, { fldUnqId: _this5.utilService.uniqueId('TEMP_LAYOUT_DD_DTO_ID') });
							}
						}
					} else {
						_this5.oldVersionOptions.records.splice(trgPos, 0, { fldUnqId: _this5.utilService.uniqueId('TEMP_LAYOUT_DD_DTO_ID') });
					}
				}
			};

			for (i = 0; i < this.currentVersionOptions.records.length; i++) {
				_loop();
			}

			var currentLength = this.currentVersionOptions.records.length;
			var oldLength = this.oldVersionOptions.records.length;

			if (currentLength < oldLength) {
				var diffLength = oldLength - currentLength;
				for (var j = 0; j < diffLength; j++) {
					this.currentVersionOptions.records.push({ fldUnqId: this.utilService.uniqueId('TEMP_LAYOUT_DD_DTO_ID') });
				}
			}

			var currentRecords = this.currentVersionOptions.records;
			var oldRecords = this.oldVersionOptions.records;

			trgGrid.clear();
			srcGrid.clear();
			trgGrid.records = currentRecords;
			srcGrid.records = oldRecords;
			trgGrid.refresh();
			srcGrid.refresh();

			for (i = 0; i < currentRecords.length; i++) {
				var trgRecord = currentRecords[i];
				var srcRecord = oldRecords[i];
				var gridName = void 0;
				var changeDot = void 0;
				var $tr = void 0;

				if (!trgRecord.fldEngNm) {
					// 삭제
					gridName = this.oldVersionOptions.name;
					changeDot = srcRecord.fldUnqId.replace(/\./g, '\\.');
					$tr = $('tr#grid_' + gridName + '_rec_' + changeDot);

					$tr.addClass('bg-del');
				}

				if (!srcRecord.fldEngNm) {
					// 추가
					gridName = this.currentVersionOptions.name;
					changeDot = trgRecord.fldUnqId.replace(/\./g, '\\.');
					$tr = $('tr#grid_' + gridName + '_rec_' + changeDot);

					$tr.addClass('bg-add');
				}

				if (trgRecord.fldEngNm && srcRecord.fldEngNm) {
					if (!(trgRecord.dataTypeNm === srcRecord.dataTypeNm && trgRecord.msgLen === srcRecord.msgLen)) {
						// 타입 또는 길이가 다를때 
						gridName = this.oldVersionOptions.name;
						changeDot = srcRecord.fldUnqId.replace(/\./g, '\\.');
						$tr = $('tr#grid_' + gridName + '_rec_' + changeDot);

						$tr.addClass('bg-red');

						gridName = this.currentVersionOptions.name;
						changeDot = trgRecord.fldUnqId.replace(/\./g, '\\.');
						$tr = $('tr#grid_' + gridName + '_rec_' + changeDot);

						$tr.addClass('bg-red');
					}
				}
			}
		}
	}, {
		key: 'pageBtnClick',
		value: function pageBtnClick(num) {
			this.pageNumber = num;
			this.getDeployList(num === 1);
		}
	}, {
		key: 'closeModal',
		value: function closeModal(isOk) {
			var _this6 = this;

			this.$uibModalInstance.dismiss();
			setTimeout(function () {
				w2ui[_this6.deployOptions.name].destroy();
				w2ui[_this6.oldVersionOptions.name].destroy();
				w2ui[_this6.currentVersionOptions.name].destroy();
			});
		}
	}]);
	return SCR0706Controller;
}();

(0, _angular.module)(_app2.default.name).controller('SCR0706Controller', SCR0706Controller);

/***/ })

},[33]);
//# sourceMappingURL=SCR0706.controller.js.map