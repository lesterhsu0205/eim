import { module } from 'angular';
import App from '../../app';
import { appName, errMsg } from '../../../environments/env.js';

class LoginController {
	
	constructor($scope, $state, userService, utilService, httpService, popupService) {
		this.$scope = $scope;
		this.$state = $state;
		this.userService = userService;
		this.utilService = utilService;
		this.httpService = httpService;
		this.popupService = popupService;
		this.appName = appName;
		
		this.init();
		this.initText();
	}
	
	init(){
		this.user = { locale: 'ko'};
		this.selectItems = [
			{label: '한국어', value: 'ko'},
			{label: 'English', value: 'en'}
		];
		
//		{label: 'Japanese', value: 'jp'}
	}

	initText(){
		this.text = {
			userId : bxMsg('login.userId'),
			userPassword : bxMsg('login.userPassword'),
			signIn : bxMsg('login.signIn')
		};
	}
	/**
	 * 로그인 클릭 이벤트 콜백 함수
	 */
	signIn() {
		const user = this.user;
		const { userService, utilService } = this;
		
		if (utilService.isEmpty(user.userId)) {
			this.openAlert(errMsg.noneId);
			return;
		}

		if (utilService.isEmpty(user.userPassword)) {
			this.openAlert(errMsg.nonePWD);
			return;
		}
		
		userService.requestSignIn(user)
			.then(() => this.goMain())
			.catch(res => this.openAlert(res.data.message));
	}
	
	goMain() {
		this.$state.go('main.test');
	}
	
	openAlert(alertBody){
		this.popupService.simpleAlert(this.$scope, alertBody);
	}
	
}

module(App.name).controller('LoginController', LoginController);