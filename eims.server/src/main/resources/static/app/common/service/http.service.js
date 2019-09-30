webpackJsonp(["app\\common\\service\\http.service"],{

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

/***/ 16:
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__("81476fbce7e3869b8a1e");


/***/ }),

/***/ "470c52d489cf8cd054ee":
/***/ (function(module, exports) {

module.exports = {
	noneUrl: 'url이 존재하지 않습니다.',
	noneId: 'ID를 입력해주세요.',
	nonePWD: '암호를 입력해주세요.'
};

/***/ }),

/***/ "5f92a5b1d82195799c4c":
/***/ (function(module, exports, __webpack_require__) {

/**
 * Created by UI/UX Team on 2018. 2. 14..
 */
var errMsg = __webpack_require__("470c52d489cf8cd054ee");

module.exports = {
	appName: 'EIMS',
    title: 'EIMS',
    url: {
    	validateUser: 'server/user...'
    },
	errMsg: errMsg
};

/***/ }),

/***/ "81476fbce7e3869b8a1e":
/***/ (function(module, exports, __webpack_require__) {

"use strict";


var _getOwnPropertyNames = __webpack_require__("dc3a14bb7bb348b98cab");

var _getOwnPropertyNames2 = _interopRequireDefault(_getOwnPropertyNames);

var _classCallCheck2 = __webpack_require__("49c81d67dea0f8060449");

var _classCallCheck3 = _interopRequireDefault(_classCallCheck2);

var _createClass2 = __webpack_require__("5115238a033983f8f227");

var _createClass3 = _interopRequireDefault(_createClass2);

var _angular = __webpack_require__("909592b0f5247409d892");

var _app = __webpack_require__("085c418bf06f41809dc1");

var _app2 = _interopRequireDefault(_app);

var _lodash = __webpack_require__("e957fe55c5f181ff4c72");

var _ = _interopRequireWildcard(_lodash);

var _env = __webpack_require__("5f92a5b1d82195799c4c");

function _interopRequireWildcard(obj) { if (obj && obj.__esModule) { return obj; } else { var newObj = {}; if (obj != null) { for (var key in obj) { if (Object.prototype.hasOwnProperty.call(obj, key)) newObj[key] = obj[key]; } } newObj.default = obj; return newObj; } }

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var HttpService = function () {
	function HttpService($http) {
		(0, _classCallCheck3.default)(this, HttpService);

		this.$http = $http;
	}

	/**
  * @param url {string} 서버 url
  * @param param {object} 서버 param
  * @return promise 
  */


	(0, _createClass3.default)(HttpService, [{
		key: 'get',
		value: function get() {
			var _this = this;

			var url = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : '';
			var param = arguments[1];


			if (_.isEmpty(url)) {
				throw new Error(_env.errMsg.noneUrl);
			}

			if (param) {
				for (var key in param) {
					if (param.hasOwnProperty(key)) {
						if (param[key] != undefined && param[key] != null) {
							url += '&' + key + '=' + encodeURIComponent(param[key]);
						}
					}
				}
			}

			var config = this._getConfig();
			return this.$http.get(url, config).then(function (res) {
				_this._checkAndSetEmptyArray(res.data);
				return res.data;
			}, function (err) {
				if (err.status == 440) {
					alert('세션이 만료되었습니다. 로그인 페이지로 이동합니다.');
					window.location.href = '/index.html';
					return false;
				}
				return { isError: true, data: err.data, status: err.status };
			});
		}
	}, {
		key: '_checkAndSetEmptyArray',
		value: function _checkAndSetEmptyArray(data) {
			(0, _getOwnPropertyNames2.default)(data).filter(function (v) {
				return v.indexOf('List') !== -1;
			}).filter(function (v) {
				return data[v] === null;
			}).map(function (v) {
				return data[v] = [];
			});
		}

		/**
   * @param url {string} 서버 url 정보
   * @param postData {object} post body 전문 
   * @return promise 
   */

	}, {
		key: 'post',
		value: function post() {
			var url = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : '';
			var postData = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};


			if (_.isEmpty(url)) {
				throw new Error(_env.errMsg.noneUrl);
			}

			var config = this._getConfig();
			return this.$http.post(url, postData, config).then(function (res) {
				return res.data;
			}, function (err) {
				return { isError: true, data: err.data, status: err.status };
			});
		}
	}, {
		key: 'delete',
		value: function _delete() {
			var url = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : '';

			if (_.isEmpty(url)) {
				throw new Error(_env.errMsg.noneUrl);
			}

			var config = this._getConfig();
			return this.$http.delete(url, config).then(function (res) {
				return res;
			}, function (err) {
				return { isError: true, data: err.data, status: err.status };
			});
		}
	}, {
		key: 'put',
		value: function put() {
			var url = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : '';
			var postData = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};

			if (_.isEmpty(url)) {
				throw new Error(_env.errMsg.noneUrl);
			}

			var config = this._getConfig();
			return this.$http.put(url, postData, config).then(function (res) {
				return res;
			}, function (err) {
				return { isError: true, data: err.data, status: err.status };
			});
		}
	}, {
		key: 'uploadFile',
		value: function uploadFile() {
			var url = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : '';
			var postData = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
			var paramData = arguments[2];

			var formData = new FormData();
			formData.append(postData.name, postData.file);

			if (paramData) {
				for (var key in paramData) {
					if (paramData.hasOwnProperty(key)) {
						formData.append(key, paramData[key]);
					}
				}
			}

			return this.$http.post(url, formData, this._getMultiPartConfig()).then(function (res) {
				return res;
			}, function (err) {
				return { isError: true, data: err.data, status: err.status };
			});
		}
	}, {
		key: 'uploadFileList',
		value: function uploadFileList() {
			var url = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : '';
			var postData = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
			var paramData = arguments[2];

			var formData = new FormData();
			postData.file.map(function (f) {
				formData.append(postData.name, f);
			});

			if (paramData) {
				for (var key in paramData) {
					if (paramData.hasOwnProperty(key)) {
						formData.append(key, paramData[key]);
					}
				}
			}

			return this.$http.post(url, formData, this._getMultiPartConfig()).then(function (res) {
				return res;
			}, function (err) {
				return { isError: true, data: err.data, status: err.status };
			});
		}
	}, {
		key: 'downloadFile',
		value: function downloadFile() {
			var url = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : '';
			var postData = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
			var method = arguments.length > 2 && arguments[2] !== undefined ? arguments[2] : 'post';


			if (method === 'get') {
				if (postData) {
					for (var key in postData) {
						if (postData.hasOwnProperty(key)) {
							if (postData[key]) {
								url += '&' + key + '=' + encodeURIComponent(postData[key]);
							}
						}
					}
				}

				return this.$http.get(url, this._getExcelExportConfig()).then(function (res) {
					return res;
				}, function (err) {
					return { isError: true, data: err.data, status: err.status };
				});
			} else {
				return this.$http.post(url, postData, this._getExcelExportConfig()).then(function (res) {
					return res;
				}, function (err) {
					return { isError: true, data: err.data, status: err.status };
				});
			}
		}

		/**
   * http config 설정 함수 
   */

	}, {
		key: '_getConfig',
		value: function _getConfig() {
			return {
				headers: {
					"content-type": "application/json; charset=utf-8"
				}
			};
		}
	}, {
		key: '_getMultiPartConfig',
		value: function _getMultiPartConfig() {
			return {
				headers: {
					"content-type": undefined
				}
			};
		}
	}, {
		key: '_getExcelExportConfig',
		value: function _getExcelExportConfig() {
			return {
				responseType: 'arraybuffer'
			};
		}
	}]);
	return HttpService;
}();

(0, _angular.module)(_app2.default.name).service('httpService', HttpService);

/***/ })

},[16]);
//# sourceMappingURL=http.service.js.map