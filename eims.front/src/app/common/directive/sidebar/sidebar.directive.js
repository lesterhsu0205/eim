import {module} from 'angular';
import App from '../../../app';

class SidebarDirective {
	
	constructor(){
		this.restrict = 'E';
		this.templateUrl = 'app/common/directive/sidebar/sidebar.html';
		this.transclude = true;
		this.scope = {
			isIcon: '='
		};
	}
	
	controller($scope) {}
	
	link($scope, element, attrs) {
		
		const $side = $(element.find('#side'));
		const $sideContent = $(element.find('side-content'));	
		
		$side.append($sideContent);
	}
	
}

module(App.name).directive('bcSidebar', ()=> new SidebarDirective)