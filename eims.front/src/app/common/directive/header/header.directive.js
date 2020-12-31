import {module} from 'angular';
import App from '../../../app';

class HeaderDirective {
	
	constructor(httpService, $scope){
		this.restrict = 'E';
		this.templateUrl = 'app/common/directive/header/header.html';
		this.scope = $scope;
		
		this.httpService = httpService;


	}
	

	controller($scope, $state, userService, $location) {
		const user = userService.getUser();
		
		$scope.iconItems = [
			{
				icon: 'bxd bxd-my', label: user.userId, state: 'main.manageUser'
			}
		];


		$scope.user = { locale: user.locale};

		$scope.selectItems = [
			{label: 'Korean', value: 'ko'},
			{label: 'English', value: 'en'}
		];
		

		$scope.click = (item = {}) => {
			console.log('click::');
			if(item.state) {
				$state.go(item.state);
			}
		};

		$scope.changeLang = (item) => {
			var user = userService.getUser();
			user.locale = $scope.user.locale;	
			var url = 'http://'+$location.host()+':'+$location.port()+'/changelang?locale='+user.locale;
			window.location.href = url;
		};

		$scope.goMain = () => {
			this.$state.go('main.manageMsg');
		}


		$scope.logout = () => {
			
			userService.requestLogout($scope);
			
			console.log('click logout :)');
		}
		
		$scope.setTheme = () => {
			console.log('click set theme :)');
		}

	}
	
	link($scope, $element, attrs) {
		
	}
}

module(App.name).directive('bcHeader', ()=> new HeaderDirective)