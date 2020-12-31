import { module } from 'angular';

import App from '../../app';
import * as _ from 'lodash';
import errMsg from '../../../environments/errMsg';

class MessageService {
	
	constructor($q, $state, userService) {
		this.$q = $q;
		this.$state = $state;
		this.userService = userService;
		
		this.messageRoot = '/assets/json/messages';
		this.isLoad = false;
		this.userKey = "USER";
	}
	
	init(messageList){
		const defer = this.$q.defer();
		
		this.userService.getUserPromise()
						.then(user => {
							defer.resolve(bxMsg.init({
								messageRoot : this.messageRoot,
								locale : user.locale,
								messageList: messageList
							}));
						})
						.catch(err => this.$state.go('login'));
		
		return defer.promise;
	}
	
	initLogin(messageList){
		let browserLocale = navigator.language || navigator.browserLanguage;
		let locale = browserLocale || 'ko';

		if (locale.indexOf('ko') !== -1) {
            locale = 'ko';
        } else if (locale.indexOf('en') !== -1) {
            locale = 'en';
		}
		
		bxMsg.init({
			messageRoot : this.messageRoot,
			locale : locale,
			messageList: messageList
		});
	}
}

module(App.name).service('messageService', MessageService);