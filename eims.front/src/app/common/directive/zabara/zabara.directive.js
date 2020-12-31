import {module} from 'angular';
import App from '../../../app';

class ZabaraDirective {
	
	constructor(){
		this.restrict = 'A';
	}
	
	controller($scope) {}
	
	link($scope, $element, attrs) {
		
		const relatedId = attrs.zabara;
		const scroll = attrs.scroll === undefined ? false : true;
		const isButton = $element[0].tagName === 'BUTTON';
		
		if (!relatedId) {
			throw new Error('zabara 사용시, id 가 필수입니다.');
		}
		
		$element.click((e)=> {
			let $target = $(e.currentTarget),
			$button = $target.find('i.zabara');
			
			const isOpen = $button.css('transform') === 'none';
			const zabara = $(`#${relatedId}`);	
			
			if (isOpen) {
				if(!isButton) {
					$target.css('background', '#fff').css('color', '#000');
					$button.css('color', '#000');
				}
				
				$button.css('transform', 'rotate(180deg)');
				zabara.stop(true, true).slideUp();
				$scope.vm.zabara && ($scope.vm.zabara[relatedId] = false);
			}
			else {
				if(!isButton) {
					$target.css('background', '#ababab').css('color', '#fff');
					$button.css('color', '#fff');
				}
				
				$button.css('transform', '');
				zabara.stop(true, true).slideDown(() => {
					if(scroll) {
						$('#main-contents').scrollTop(0);
						$('#main-contents').scrollTop($target.offset().top - 100);
					}
				});
				$scope.vm.zabara && ($scope.vm.zabara[relatedId] = true);
			}
		});
	}
}

module(App.name).directive('zabara', ()=> new ZabaraDirective);