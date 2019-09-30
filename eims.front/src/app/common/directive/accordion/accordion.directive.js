import {module} from 'angular';
import App from '../../../app';

class AccordionDirective {
	
	constructor(){
		this.restrict = 'E';
		this.templateUrl = 'app/common/directive/accordion/accordion.html';
		this.transclude = true;
		this.scope = {
			items: '='
		};
		this.replace = true;
	}
	
	controller($scope, $state, utilService) {
		
		const currentName = $state.current.name;
		
		$scope.utilService = utilService;
		
		$scope.items.map(v => {
		
			const hasChildren = v.children;
			v.id = utilService.uniqueId('acMainMenu');
			
			if (!hasChildren) return;
			
			v.children.map(cV => {
				cV.id = utilService.uniqueId('acSubMenu');	
				if (cV.state === currentName) {
					v.selected = true;
					setTimeout(() => {
						$(`li#${cV.id}`).click();
					});
				}
			});
		});
		
		$scope.toggle = (idx) => {
			const subItems = $('.bw-sub').get(idx);
			const $subItems = $(subItems);
			const isHide = $subItems.css('display') === 'none';
			
			if (isHide) {
				$subItems.stop(true, true).slideDown(()=>{
					$scope.items[idx].selected = true;
				});
			}
			else {
				$subItems.stop(true, true).slideUp(()=> {
					$scope.items[idx].selected = false;
				});
			}
		};
	}
	
	link($scope, $element, attrs) {
		
		const $parent = $scope.$parent;
		const utilService = $scope.utilService;
		
		const key = attrs.click;
		
		let _click = utilService.get($parent, key);
		
		if (_click) {
			_click = _click.bind($parent.vm || $parent);
		}
		
		$scope.click = (e, item) => {
			_click(e, item);
		}
	}
}

module(App.name).directive('bcAccordion', ()=> new AccordionDirective);