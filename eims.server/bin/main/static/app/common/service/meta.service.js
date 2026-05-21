webpackJsonp(["app\\common\\service\\meta.service"],{

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

/***/ 18:
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__("fefeb2215f9672fc3e61");


/***/ }),

/***/ "fefeb2215f9672fc3e61":
/***/ (function(module, exports, __webpack_require__) {

"use strict";


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

var MetaService = function () {
	function MetaService($q, httpService) {
		(0, _classCallCheck3.default)(this, MetaService);

		this.httpService = httpService;
		this.$q = $q;
		this.init();
	}

	(0, _createClass3.default)(MetaService, [{
		key: 'init',
		value: function init() {
			this._getMeta();
		}
	}, {
		key: '_resetMeta',
		value: function _resetMeta() {
			this.metaMap = {};
			this.metaList = [];
		}
	}, {
		key: '_getMeta',
		value: function _getMeta() {
			var _this = this;

			this.httpService.get('/metas?pageNumber=1&pageSize=99999').then(function (res) {
				_this._resetMeta();
				res.metabsOutList.map(function (v) {
					_this.metaMap[v.metaEngNm] = v;
					_this.metaList.push(v);
				});
			});
		}
	}, {
		key: 'getMetaListLikeKorNm',
		value: function getMetaListLikeKorNm(metaKorNm) {
			var pattern = new RegExp('^' + metaKorNm);

			return this.metaList.filter(function (v) {
				return pattern.test(v.metaKorNm);
			});
		}
	}, {
		key: 'getMetaListLikeEngNm',
		value: function getMetaListLikeEngNm(metaEngNm) {
			var pattern = new RegExp('^' + metaEngNm);

			return this.metaList.filter(function (v) {
				return pattern.test(v.metaEngNm);
			});
		}
	}, {
		key: 'getMetaListSameKorNm',
		value: function getMetaListSameKorNm(metaKorNm) {
			return this.metaList.filter(function (v) {
				return metaKorNm === v.metaKorNm;
			});
		}
	}, {
		key: 'getMetaByMetaEngNm',
		value: function getMetaByMetaEngNm(metaEngNm) {
			return this.metaMap[metaEngNm];
		}
	}, {
		key: '_getMetaFromServerByKorNm',
		value: function _getMetaFromServerByKorNm(metaKorNm) {
			var url = '/metas?pageNumber=1&pageSize=10';
			if (metaKorNm) url += '&metaKorNm=' + metaKorNm;

			return this.httpsService.get(url);
		}
	}, {
		key: 'syncMeta',
		value: function syncMeta() {
			var _this2 = this;

			var defer = this.$q.defer();

			this.httpService.get('/metas/syncs').then(function (res) {
				if (res.isError) {
					_this2.openAlert(res.data.message);
					defer.reject();
					return;
				}

				_this2._getMeta();
				defer.resolve();
			});

			return defer.promise;
		}
	}, {
		key: 'getMeta',
		value: function getMeta() {
			var _this3 = this;

			var defer = this.$q.defer();

			this.httpService.get('/metas?pageNumber=1&pageSize=99999').then(function (res) {
				_this3._resetMeta();
				res.metabsOutList.map(function (v) {
					_this3.metaMap[v.metaEngNm] = v;
					_this3.metaList.push(v);
				});
				defer.resolve();
			});
			return defer.promise;
		}
	}]);
	return MetaService;
}();

(0, _angular.module)(_app2.default.name).service('metaService', MetaService);

/***/ })

},[18]);
//# sourceMappingURL=meta.service.js.map