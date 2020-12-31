import { module } from 'angular';
import App from '../../app';

class UserService {
	
	constructor($q, $state,httpService, utilService,popupService) {
		this.$q = $q;
		this.$state = $state;
		this.utilService = utilService;
		this.httpService = httpService;
		this.popupService = popupService;
	}
	
	getUserPromise(){
		const defer = this.$q.defer();

		this.httpService.get('/logininfosso')
		.then(res =>{
			if(res.isError) defer.reject(res);
			else defer.resolve(this._setUser(res));
		});
		
		return defer.promise;
	}
	
	getUser() {
		return this._user;
	}
	
	requestSignIn(user) {
		const defer = this.$q.defer();
		
		this.httpService.post('/login', user)
						.then(res => {
							if(res.isError) defer.reject(res);
							else defer.resolve(this._setUser(res));
						});
		return defer.promise;
	}

	requestSSO() {
		const defer = this.$q.defer();
		
		this.httpService.post('/sso')
						.then(res => {
							if(res.isError) defer.reject(res);
							else defer.resolve(this._setUser(res));
						});
		return defer.promise;
	}

	doLogout() {
		this.httpService.post('/logout')
		.then(res => {
			this._removeUser();
			this.$state.go('login');
		});
	}

	requestLogout(scope) {
		this.popupService.simpleConfirm(scope,
			bxMsg('common.logoutMsg'),
			()=>this.doLogout(), ()=>{}, bxMsg('common.confirmOk'), bxMsg('common.confirmCancel'));
	}
	/*
	requestLogout(scope) {
		this.httpService.post('/logout')
						.then(res => {
							this.popupService.simpleConfirm(scope, 
									bxMsg('common.logoutMsg'),
									()=>{
										this._removeUser();
										this.$state.go('login');
									});
						});
	}
	*/
	
	getUserMenu() {
		this._menu.map((menu) => {
			menu.name = bxMsg('menu.' + menu.id);
		});

		return this._menu;
	}
	
	_setUser(loginUserInfo){
		this._menu = loginUserInfo.menuList;
		this._user = loginUserInfo.userDto;
		this._user.locale = loginUserInfo.sessionInfo && typeof loginUserInfo.sessionInfo === 'object' ?  loginUserInfo.sessionInfo.locale : this._getLocale();
		this._user.perm = {};

		loginUserInfo.permList.map((perm) => {
			this._user.perm[perm.permId] = true;
		});
		
		return this._user;
	}
	
	_getLocale(){
		let browserLocale = navigator.language || navigator.browserLanguage;
		let user = this._user;
		let locale = (user && user.locale) || browserLocale || 'ko';
	
        if (locale.indexOf('ko') !== -1) {
            locale = 'ko';
        } else if (locale.indexOf('en') !== -1) {
            locale = 'en';
        }
	
	    return locale;
	}
	
	_removeUser(){
		delete this._user;
	}
	
	isAdmin(){
		const user = this._user;
		return _.isEmpty(user) ? false : user.roleId === 'Administrator';
	}
}

module(App.name).service('userService', UserService);