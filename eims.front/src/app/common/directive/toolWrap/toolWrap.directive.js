import {module} from 'angular';
import App from '../../../app';

class ToolWrapDirective {
	
	constructor(){
		this.restrict = 'E';
		this.templateUrl = 'app/common/directive/toolWrap/toolWrap.html';
		this.transclude = true;
		this.replace = true;
	}
	
	controller($scope, utilService) {}
	
	link($scope, $element, attrs) {
		const $leftWrap = $element.find('.f-l');
		const $btnWrap = $element.find('.btn-wrap');
		
		const $left = $element.find('left');
		const $right = $element.find('right');
		
		if ($left) $leftWrap.append($left); 
		else $leftWrap.remove();
		
		if ($right) $btnWrap.append($right);
		else $btnWrap.remove();
	}
}

module(App.name).directive('bcToolWrap', ()=> new ToolWrapDirective);