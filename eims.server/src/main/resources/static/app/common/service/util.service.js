webpackJsonp(["app\\common\\service\\util.service"],{

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

/***/ 21:
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__("ef02cf0e7de3bdf8367d");


/***/ }),

/***/ "ef02cf0e7de3bdf8367d":
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

var UtilService = function () {
	function UtilService($rootScope) {
		(0, _classCallCheck3.default)(this, UtilService);

		this.$rootScope = $rootScope;
		//{scope: {}, data: {}} 
		this.stateParams = {};
	}

	(0, _createClass3.default)(UtilService, [{
		key: 'isEmpty',
		value: function isEmpty(value) {
			return _.isEmpty(value);
		}
	}, {
		key: 'uniqueId',
		value: function uniqueId() {
			var prefix = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : '';

			return _.uniqueId(prefix);
		}
	}, {
		key: 'get',
		value: function get(obj, key) {

			var _key = key.replace(/\(\)$/, '');
			return _.get(obj, _key);
		}
	}, {
		key: 'clone',
		value: function clone(obj) {
			return _.cloneDeep(obj);
		}
	}, {
		key: 'stringByteLength',
		value: function stringByteLength(str) {
			var byte = void 0,
			    char = void 0,
			    i = void 0;
			var noneAsciiCodeByte = 2;

			// shifting 연산으로 문자열 내부, 한글추가 여부 검사
			for (byte = i = 0; char = str.charCodeAt(i++); byte += char >> 11 ? noneAsciiCodeByte : char >> 7 ? 2 : 1) {}

			return byte;
		}
	}, {
		key: 'openTab',
		value: function openTab($scope, route, data) {
			if (this.isEmpty(route.state)) throw new Error('state는 필수값입니다.');
			if (this.isEmpty(route.label)) throw new Error('label은 필수값입니다.');

			this.stateParams[route.state] = data;
			this.$rootScope.$emit('WRAP:MOVETAB', route);
		}
	}, {
		key: 'setParams',
		value: function setParams(state, params) {
			this.stateParams[state] = params;
		}
	}, {
		key: 'getParams',
		value: function getParams(state) {
			return this.stateParams[state];
		}
	}, {
		key: 'setRegDttm',
		value: function setRegDttm(dttm) {
			var yy = dttm.substring(0, 4);
			var mm = dttm.substring(4, 6);
			var dd = dttm.substring(6, 8);
			var hh = dttm.substring(8, 10);
			var mi = dttm.substring(10, 12);
			var ss = dttm.substring(12, 14);
			return yy + "/" + mm + "/" + dd + "  " + hh + ":" + mi + ":" + ss;
		}
	}, {
		key: 'saveFile',
		value: function saveFile(blob, fileName) {
			if (window.navigator.msSaveOrOpenBlob) {
				window.navigator.msSaveOrOpenBlob(blob, fileName);
			} else {
				var a = window.document.createElement('a');

				a.href = window.URL.createObjectURL(blob, { type: 'text/plain' });
				a.download = fileName;

				document.body.appendChild(a);
				a.click();
				document.body.removeChild(a);
			}
		}
	}]);
	return UtilService;
}();

(0, _angular.module)(_app2.default.name).service('utilService', UtilService);

/***/ })

},[21]);
//# sourceMappingURL=util.service.js.map