webpackJsonp(["app\\views\\wrap\\wrap.controller"],{

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

/***/ "51bc997c52d223d40803":
/***/ (function(module, exports, __webpack_require__) {

"use strict";


var _getIterator2 = __webpack_require__("e3915674c5aaafa0b6ee");

var _getIterator3 = _interopRequireDefault(_getIterator2);

var _getOwnPropertyNames = __webpack_require__("dc3a14bb7bb348b98cab");

var _getOwnPropertyNames2 = _interopRequireDefault(_getOwnPropertyNames);

var _classCallCheck2 = __webpack_require__("49c81d67dea0f8060449");

var _classCallCheck3 = _interopRequireDefault(_classCallCheck2);

var _createClass2 = __webpack_require__("5115238a033983f8f227");

var _createClass3 = _interopRequireDefault(_createClass2);

var _angular = __webpack_require__("909592b0f5247409d892");

var _app = __webpack_require__("085c418bf06f41809dc1");

var _app2 = _interopRequireDefault(_app);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var WrapController = function () {
	function WrapController($rootScope, $scope, httpService, utilService, userService, codeService, metaService) {
		(0, _classCallCheck3.default)(this, WrapController);

		this.$rootScope = $rootScope;
		this.$scope = $scope;
		this.httpService = httpService;
		this.utilService = utilService;
		this.userService = userService;
		this.codeService = codeService;
		this.metaService = metaService;

		this.menuMap = {
			'MENU2000': { icon: 'bxd bxd-layer' },
			'MENU2010': { state: 'main.manageMsg' },

			'MENU3100': { icon: 'bxd bxd-agency2' },
			'MENU3110': { state: 'main.manageMciInterface' },
			'MENU3120': { state: 'main.manageEaiInterface' },
			'MENU3130': { state: 'main.manageFepInterface' },

			'MENU4000': { icon: 'bxd bxd-delivery' },
			'MENU4010': { state: 'main.manageDeploySystem' },
			'MENU4020': { state: 'main.manageCommSystem' },
			'MENU4030': { state: 'main.manageAppCode' },
			'MENU4060': { state: 'main.manageExtInstCode' },
			'MENU4070': { state: 'main.manageMetaInfo' },

			'MENU5000': { icon: 'bxd bxd-folder-o' },
			'MENU5010': { state: 'main.manageActionHistory' },

			'MENU1000': { icon: 'bxd bxd-setting' },
			'MENU1010': { state: 'main.manageUser' },
			'MENU1020': { state: 'main.manageRole' },
			'MENU1030': { state: 'main.managePerm' }
		};

		this.addedMenu = {
			label: '인터페이스 관리',
			icon: 'bxd bxd-warning',
			hidden: true,
			children: [{
				label: bxMsg('menu.MENU3110'),
				state: 'main.manageMciInterfaceDetail'
			}, {
				label: bxMsg('menu.MENU3120'),
				state: 'main.manageEaiInterfaceDetail'
			}, {
				label: bxMsg('menu.MENU3130'),
				state: 'main.manageFepInterfaceDetail'
			}]
		};

		this.accordionItems = this.createAccordionItems();
		this.hoverItems = [];
		this.lnbItems = [];
		this.isIcon = false;
		this.optimizeContentWidth();
		this.initBroadcastReceiver();
		this.registerTooltip();
		this.registerPreventKey();
		this.codeService._initCodes();
	}

	(0, _createClass3.default)(WrapController, [{
		key: 'registerTooltip',
		value: function registerTooltip() {
			$('body').tooltip({
				position: {
					my: "center bottom-20",
					at: "center top",
					using: function using(position, feedback) {
						$(this).css(position);
						$('<div>').addClass('arrow').addClass(feedback.vertical).addClass(feedback.horizontal).appendTo(this);
					}
				},
				content: function content() {
					return $(this).prop('title').replace(/<br>/g, '<br />');
				}
			});
		}
	}, {
		key: 'registerPreventKey',
		value: function registerPreventKey() {
			$(document).on('keydown', function (e) {
				if (e.which === 8 && !$(e.target).is('input, textarea')) {
					e.preventDefault();
				}
			});
		}
	}, {
		key: 'toggle',
		value: function toggle() {
			var item = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : {};

			this.isIcon = !this.isIcon;
			this.optimizeContentWidth();
			$(window).trigger('resize');
		}
	}, {
		key: 'optimizeContentWidth',
		value: function optimizeContentWidth() {

			setTimeout(function () {
				setContentWrapWidth(getSidebarWidth());
			});

			function getSidebarWidth() {
				return $('#side').width();
			}

			function setContentWrapWidth() {
				var width = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : 0;

				$('#content').css({ left: width + 1 + 'px' });
			}

			if (!_.isEmpty(w2ui)) {
				setTimeout(function () {
					(0, _getOwnPropertyNames2.default)(w2ui).filter(function (v) {
						return v.indexOf('_toolbar') == -1;
					}).map(function (v) {
						return w2ui[v].resize();
					});
				});
			}
		}
	}, {
		key: 'initBroadcastReceiver',
		value: function initBroadcastReceiver() {
			var _this = this;

			var movetabListener = this.$rootScope.$on('WRAP:MOVETAB', function (e, data) {
				var item = _this._findItemByState(data.state);

				if (_.isEmpty(item)) {
					throw new Error('잘못된 state입니다.');
				}

				delete data.state;

				_this.click(e, item);
				_this.$scope.$apply();
			});

			this.$scope.$on('$destroy', function () {
				movetabListener();
			});
		}
	}, {
		key: '_findItemByState',
		value: function _findItemByState(state) {
			return this.accordionItems.map(function (item) {
				return item.children;
			}).reduce(function (item1, item2) {
				return item1.concat(item2);
			}, []).filter(function (item) {
				return item.state == state;
			})[0];
		}
	}, {
		key: 'createAccordionItems',
		value: function createAccordionItems() {
			var _this2 = this;

			var accordionMenuList = [],
			    menuList = this.userService.getUserMenu();

			menuList.map(function (menu) {
				if (menu.parentId === null && _this2.menuMap[menu.id]) {
					var newMenu = {
						id: menu.id,
						label: menu.name,
						icon: _this2.menuMap[menu.id]['icon'],
						children: []
					};

					accordionMenuList.push(newMenu);
				}
			});

			accordionMenuList.map(function (accordionMenu) {
				menuList.map(function (menu) {
					if (accordionMenu.id === menu.parentId && _this2.menuMap[menu.id]) {
						var newMenu = {
							id: menu.id,
							label: menu.name,
							state: _this2.menuMap[menu.id]['state']
						};

						accordionMenu.children.push(newMenu);
					}
				});
			});

			accordionMenuList.push(this.addedMenu);

			return accordionMenuList;
		}
	}, {
		key: 'click',
		value: function click(e, item) {
			var isExist = this.checkLnbItems(item);

			if (isExist) {
				setTimeout(function () {
					$('li#' + item.id).click();
				});
			} else {
				delete item.$$hashKey;
				item.id = this.utilService.uniqueId('lnb');

				var param = this.utilService.getParams(item.state);
				if (param && param.data) {
					this.utilService.setParams(item.state, $.extend(param, { scope: null }));
				} else {
					this.utilService.setParams(item.state, null);
				}

				this.lnbItems.push(item);
			}
		}
	}, {
		key: 'hoverClick',
		value: function hoverClick(item) {
			var isExist = this.checkLnbItems(item);

			if (isExist) {
				setTimeout(function () {
					$('li#' + item.id).click();
				});
			} else {
				item.id = this.utilService.uniqueId('lnb');

				var param = this.utilService.getParams(item.state);
				if (param && param.data) {
					this.utilService.setParams(item.state, $.extend(param, { scope: null }));
				} else {
					this.utilService.setParams(item.state, null);
				}

				this.lnbItems.push(item);
			}
		}
	}, {
		key: 'checkLnbItems',
		value: function checkLnbItems(newItem) {

			var isExist = false;

			var _iteratorNormalCompletion = true;
			var _didIteratorError = false;
			var _iteratorError = undefined;

			try {
				for (var _iterator = (0, _getIterator3.default)(this.lnbItems), _step; !(_iteratorNormalCompletion = (_step = _iterator.next()).done); _iteratorNormalCompletion = true) {
					var item = _step.value;

					if (newItem.state === item.state) {
						isExist = true;
						break;
					}
				}
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

			return isExist;
		}
	}, {
		key: 'hover',
		value: function hover(index) {
			this.hoverItems = this.accordionItems[index].children;
		}
	}]);
	return WrapController;
}();

(0, _angular.module)(_app2.default.name).controller('WrapController', WrapController);

/***/ }),

/***/ 62:
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__("51bc997c52d223d40803");


/***/ })

},[62]);
//# sourceMappingURL=wrap.controller.js.map