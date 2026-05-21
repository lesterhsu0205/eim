webpackJsonp(["app\\common\\service\\code.service"],{

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

/***/ 14:
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__("9f449174f6a2c8c1ba0a");


/***/ }),

/***/ "9f449174f6a2c8c1ba0a":
/***/ (function(module, exports, __webpack_require__) {

"use strict";


var _getIterator2 = __webpack_require__("e3915674c5aaafa0b6ee");

var _getIterator3 = _interopRequireDefault(_getIterator2);

var _map = __webpack_require__("5a3ef7522e6baeb190db");

var _map2 = _interopRequireDefault(_map);

var _classCallCheck2 = __webpack_require__("49c81d67dea0f8060449");

var _classCallCheck3 = _interopRequireDefault(_classCallCheck2);

var _createClass2 = __webpack_require__("5115238a033983f8f227");

var _createClass3 = _interopRequireDefault(_createClass2);

var _angular = __webpack_require__("909592b0f5247409d892");

var _app = __webpack_require__("085c418bf06f41809dc1");

var _app2 = _interopRequireDefault(_app);

var _lodash = __webpack_require__("e957fe55c5f181ff4c72");

var _ = _interopRequireWildcard(_lodash);

function _interopRequireWildcard(obj) { if (obj && obj.__esModule) { return obj; } else { var newObj = {}; if (obj != null) { for (var key in obj) { if (Object.prototype.hasOwnProperty.call(obj, key)) newObj[key] = obj[key]; } } newObj.default = obj; return newObj; } }

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var CodeService = function () {
	function CodeService(httpService, $q) {
		(0, _classCallCheck3.default)(this, CodeService);

		this.httpService = httpService;
		this.$q = $q;
		this.commonCodes = {};
		this.menuMap = null;
		this.init();
	}

	(0, _createClass3.default)(CodeService, [{
		key: 'init',
		value: function init() {
			this.isCommonCodeLoad = false;
			this._initCodes();
			this.setMenubyState();
		}
	}, {
		key: '_initCodes',
		value: function _initCodes() {
			var _this = this;

			var commonCodes = {};

			this.httpService.get('/codes?pageSize=99999').then(function (res) {
				if (!res.isError) {
					res.commCodeDtoList.map(function (code) {
						var groupCode = commonCodes[code.cdId];

						if (_.isEmpty(groupCode)) {
							groupCode = [];
							commonCodes[code.cdId] = groupCode;
						}

						var cdValNm = bxMsg('code.' + code.cdId + ':' + code.cdVal);
						code.cdValNm = cdValNm ? cdValNm : code.cdValNm;

						groupCode.push(code);
					});
					_this.isCommonCodeLoad = true;
					_this.commonCodes = commonCodes;
				}
			});
		}
	}, {
		key: 'getCodesList',
		value: function getCodesList(cdIds) {
			var _this2 = this;

			var defer = this.$q.defer();

			if (this.isCommonCodeLoad) {
				var result = cdIds.map(function (cdId) {
					return _this2._get(_this2.commonCodes, cdId);
				});
				defer.resolve(result);
			} else {
				this.httpService.get('/codes?pageSize=99999').then(function (res) {
					if (!res.isError) {
						var commonCodes = [];

						res.commCodeDtoList.map(function (code) {
							var groupCode = commonCodes[code.cdId];

							if (_.isEmpty(groupCode)) {
								groupCode = [];
								commonCodes[code.cdId] = groupCode;
							}

							groupCode.push(code);
						});

						var _result = cdIds.map(function (cdId) {
							return _this2._get(commonCodes, cdId);
						});
						defer.resolve(_result);
					}
				});
			}

			return defer.promise;
		}
	}, {
		key: 'getCodesByCdId',
		value: function getCodesByCdId(cdId) {
			var defer = this.$q.defer();
			defer.resolve(this.isCommonCodeLoad ? defer.resolve(this._get(this.commonCodes, cdId)) : defer.resolve(this._getCodesByCdId(cdId)));
			return defer.promise;
		}
	}, {
		key: 'getCodesByCdIdFromMem',
		value: function getCodesByCdIdFromMem(cdId) {
			return this._get(this.commonCodes, cdId);
		}
	}, {
		key: 'getCodeValList',
		value: function getCodeValList(cdId) {
			var cdListTemp = this._get(this.commonCodes, cdId);
			var cdList = [];
			for (var i = 0; i < cdListTemp.length; i++) {

				cdList.push(cdListTemp[i].cdVal);
			}

			return cdList;
		}
	}, {
		key: 'getCodeValNm',
		value: function getCodeValNm(codes, codeVal) {
			if (typeof codes === 'string') codes = this.commonCodes[codes];
			if (_.isEmpty(codes) || codes.length === 0) throw Error('코드목록이 없습니다.');
			return this._getVal(codes, codeVal).cdValNm;
		}
	}, {
		key: 'setMenubyState',
		value: function setMenubyState() {
			this.menuMap = new _map2.default();

			this.menuMap.set("main.manageMsg", "MENU2010");
			this.menuMap.set("main.manageMciInterface", "MENU3110");
			this.menuMap.set("main.manageEaiInterface", "MENU3120");
			this.menuMap.set("main.manageFepInterface", "MENU3130");
			this.menuMap.set("main.manageDeploySystem", "MENU4010");
			this.menuMap.set("main.manageCommSystem", "MENU4020");
			this.menuMap.set("main.manageAppCode", "MENU4030");
			this.menuMap.set("main.manageExtInstCode", "MENU4060");
			this.menuMap.set("main.manageMetaInfo", "MENU4070");
			this.menuMap.set("main.manageActionHistory", "MENU5010");
			this.menuMap.set("main.manageUser", "MENU1010");
			this.menuMap.set("main.manageRole", "MENU1020");
			this.menuMap.set("main.managePerm", "MENU1030");
		}
	}, {
		key: 'getMenubyState',
		value: function getMenubyState($state) {
			return this.menuMap.get($state);
		}
	}, {
		key: '_get',
		value: function _get(codes, key) {
			if (_.isEmpty(key)) throw new Error('코드값은 필수입니다.');
			return codes[key];
		}
	}, {
		key: '_getVal',
		value: function _getVal(codes, key) {
			var _iteratorNormalCompletion = true;
			var _didIteratorError = false;
			var _iteratorError = undefined;

			try {
				for (var _iterator = (0, _getIterator3.default)(codes), _step; !(_iteratorNormalCompletion = (_step = _iterator.next()).done); _iteratorNormalCompletion = true) {
					var code = _step.value;

					if (code.cdVal == key) return code;
				}

				//console.log('해당하는 코드값이 없습니다.', codes, key);
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

			return "";
		}
	}, {
		key: '_getCodesByCdId',
		value: function _getCodesByCdId(cdId) {
			return this.httpService.get('/codes/common/' + cdId);
		}
	}]);
	return CodeService;
}();

(0, _angular.module)(_app2.default.name).service('codeService', CodeService);

/***/ })

},[14]);
//# sourceMappingURL=code.service.js.map