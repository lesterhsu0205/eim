import {module} from 'angular';
import App from '../../../app';

class HeaderDirective {
	
	constructor(httpService, $scope){
		this.restrict = 'E';
		this.templateUrl = 'app/common/directive/header/header.html';
		this.scope = $scope;
		
		this.httpService = httpService;
	}
	
	controller($scope, $state, userService) {
		const user = userService.getUser();
		
		$scope.iconItems = [
			{
				icon: 'bxd bxd-my', label: user.userId, state: 'main.manageUser'
			}
		];
		
		
		$scope.click = (item = {}) => {
			console.log('click::');
			if(item.state) {
				$state.go(item.state);
			}
		};
		
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