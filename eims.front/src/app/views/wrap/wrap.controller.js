import { module } from 'angular';
import App from '../../app';

class WrapController {
	
	constructor($rootScope, $scope, httpService, utilService, userService, codeService, metaService) {
		this.$rootScope = $rootScope;
		this.$scope = $scope;
		this.httpService = httpService;
		this.utilService = utilService;
		this.userService = userService;
		this.codeService = codeService;
		this.metaService = metaService;
		
		this.menuMap = {
			'MENU2000' : {icon: 'bxd bxd-layer'},
			'MENU2010' : {state: 'main.manageMsg'},
			
			'MENU3100' : {icon: 'bxd bxd-agency2'},
			'MENU3110' : {state: 'main.manageMciInterface'},
			'MENU3120' : {state: 'main.manageEaiInterface'},
			'MENU3130' : {state: 'main.manageFepInterface'},

			'MENU4000' : {icon: 'bxd bxd-delivery'},
			'MENU4010' : {state: 'main.manageDeploySystem'},
			'MENU4020' : {state: 'main.manageCommSystem'},
			'MENU4030' : {state: 'main.manageAppCode'},
			'MENU4060' : {state: 'main.manageExtInstCode'},
			'MENU4070' : {state: 'main.manageMetaInfo'},
			
			'MENU5000' : {icon: 'bxd bxd-folder-o'},
			'MENU5010' : {state: 'main.manageActionHistory'},

			'MENU1000' : {icon: 'bxd bxd-setting'},		
			'MENU1010' : {state: 'main.manageUser'},
			'MENU1020' : {state: 'main.manageRole'},
			'MENU1030' : {state: 'main.managePerm'}
		};
		
		this.addedMenu = {
			label: '인터페이스 관리', 
			icon: 'bxd bxd-warning',
			hidden: true,
			children: [
				{
					label: bxMsg('menu.MENU3110'),
					state: 'main.manageMciInterfaceDetail'
				},
				{
					label:  bxMsg('menu.MENU3120'),
					state: 'main.manageEaiInterfaceDetail'
				},
				{
					label:  bxMsg('menu.MENU3130'),
					state: 'main.manageFepInterfaceDetail'
				}
			]
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
	
	registerTooltip() {
		 $('body').tooltip({
           position: {
               my: "center bottom-20",
               at: "center top",
               using: function( position, feedback ) {
                   $(this).css( position );
                   $('<div>').addClass( 'arrow')
                       .addClass( feedback.vertical )
                       .addClass( feedback.horizontal )
                       .appendTo( this );
               }
           },
           content: function() {
           	return $(this).prop('title').replace(/<br>/g, '<br />');
           }
       });
	}
	
	registerPreventKey() {
		$(document).on('keydown', function(e){
			if(e.which === 8 && !$(e.target).is('input, textarea')) {
				e.preventDefault();
			}
		})
	}
	
	toggle (item = {}) {
		this.isIcon = !this.isIcon;
		this.optimizeContentWidth();
		$(window).trigger('resize');
	}

	optimizeContentWidth() {
		
		setTimeout(()=> {
			setContentWrapWidth(getSidebarWidth());	
		});
		
		function getSidebarWidth() {
			return $('#side').width();
		}
		
		function setContentWrapWidth(width = 0) {
			$('#content').css({left: `${width+1}px`});
		}
		
		if(!_.isEmpty(w2ui)){
			setTimeout(()=>{
				Object.getOwnPropertyNames(w2ui)
					  .filter(v => v.indexOf('_toolbar') == -1)
					  .map(v => w2ui[v].resize())
			});
		}
	}
	
	initBroadcastReceiver(){
		let movetabListener = this.$rootScope.$on('WRAP:MOVETAB', (e, data) => {
			const item = this._findItemByState(data.state);
			
			if(_.isEmpty(item)){
				throw new Error('잘못된 state입니다.');
			}
			
			delete data.state;
			
			this.click(e, item);
			this.$scope.$apply();
		});
		
		this.$scope.$on('$destroy', () => {
			movetabListener();
		});
	}
	
	_findItemByState(state){
		return this.accordionItems.map(item => item.children)
								  .reduce((item1, item2) => item1.concat(item2), [])
								  .filter(item => item.state == state)[0];
	}
	
	createAccordionItems() {
		let accordionMenuList = [],
			menuList = this.userService.getUserMenu();
		
		menuList.map((menu) => {
			if(menu.parentId === null && this.menuMap[menu.id]) {
				let newMenu = {
					id: menu.id,
					label: menu.name,
					icon: this.menuMap[menu.id]['icon'],
					children: []
				};
			
				accordionMenuList.push(newMenu);
			}
		});
		
		accordionMenuList.map((accordionMenu) => {
			menuList.map((menu) => {
				if(accordionMenu.id === menu.parentId && this.menuMap[menu.id]) {
					let newMenu = {
							id: menu.id,
							label: menu.name,
							state: this.menuMap[menu.id]['state']
						};
					
					accordionMenu.children.push(newMenu);
				}
			});
		});
		
		accordionMenuList.push(this.addedMenu);
		
		return accordionMenuList; 
	}
	
	click(e, item) {
		let isExist = this.checkLnbItems(item);

		if (isExist) {
			setTimeout(()=>{
				$(`li#${item.id}`).click()
			});
		} else {
			delete item.$$hashKey;
			item.id = this.utilService.uniqueId('lnb');
			
			let param = this.utilService.getParams(item.state);
			if(param && param.data) {
				this.utilService.setParams(item.state, $.extend(param, {scope: null}));
			}else{
				this.utilService.setParams(item.state, null);
			}
			
			this.lnbItems.push(item);
		}
	}
	
	hoverClick(item) {
		let isExist = this.checkLnbItems(item);
		
		if (isExist) {
			setTimeout(()=>{
				$(`li#${item.id}`).click()
			});
		} else {
			item.id = this.utilService.uniqueId('lnb');
			
			let param = this.utilService.getParams(item.state);
			if(param && param.data) {
				this.utilService.setParams(item.state, $.extend(param, {scope: null}));
			}else{
				this.utilService.setParams(item.state, null);
			}
			
			
			this.lnbItems.push(item);
		}
	}
	
	checkLnbItems(newItem) {
		
		let isExist = false;
		
		for (let item of this.lnbItems) {
			if (newItem.state === item.state) {
				isExist = true;
				break;
			}					
		}
		
		return isExist;
	}
	
	
	hover(index){
		this.hoverItems=this.accordionItems[index].children;
	}
	
}

module(App.name).controller('WrapController', WrapController);