import {module} from 'angular';
import App from '../../../../app';

class AccordionBindDirective {
	
	constructor(){
		this.restrict = 'A';
		this.template = `
		<div>
			<ng-transclude></ng-transclude>
		</div>`;
		this.transclude = true;
		this.replace = true;
	}
	
	controller($scope) {}
	
	link($scope, $element, attrs) {
		setTimeout(() => {
			const $h3s = $element.find('>ng-transclude>h3');
			const $divs = $element.find('>ng-transclude>div');
			
			$divs.map(i => {
				const $div = $($divs.get(i));
				const isOpen = $div.attr('data-open') === 'true';
				if (isOpen) return;
				$div.css('display', 'none');
			});
			
			
			$h3s.map(i => {
				const $h3 = $($h3s.get(i));
				$h3.click(() => {
					const $div = $($divs.get(i));
					const isClose = $div.css('display') === 'none';
					
					if (isClose) {
						$div.stop(true, true).slideDown();
					}
					else {
						const width = $div.width();
						$h3.width(width);
						$div.stop(true, true).slideUp();	
					}
				});
			});
		});
	}
}

module(App.name).directive('bcAccordionBind', ()=> new AccordionBindDirective);