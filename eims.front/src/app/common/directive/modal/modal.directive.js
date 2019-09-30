import {module} from 'angular';
import App from '../../../app';

class ModalDirective {
	
	constructor(){
		this.restrict = 'E';
		this.templateUrl = 'app/common/directive/modal/modal.html';
		this.transclude = true;
		this.scope = {
			modal: '=',
			dim: '=',
			open: '@',
			close: '@'
		};
	}
	
	controller($scope, utilService) {
		
		const {open, close, $parent} = $scope;
		
		if (open) {
			$scope._open = utilService.get($parent, open);	
		}
		
		if (close) {
			$scope._close = utilService.get($parent, close);	
		}	
	}
	
	link($scope, $element, attrs) {
		const $body = $('body');
		const $wrap = $($element.find('.modal-wrap'));
		const $content = $($wrap.find('ng-transclude').children());
		const $dim = $('<div class="dim"></div>');
		
		const contentWidth = $wrap.get(0).offsetWidth;
		const contentHeight = $wrap.get(0).offsetHeight;
		
		const $grids = $($element.find('div[bc-grid]'));
		const hasGrid = $grids.length > 0;

		const {_open, _close} = $scope;
		
		$scope.isShow = false;
		
		$scope.modal = {
			open: () => {
				$scope.isShow = true;
				calculatePosition();
				hasGrid && refreshGrid();
				$scope.dim && $element.before($dim);
				_open && _open();
			},
			
			close: () => {
				$scope.isShow = false;
				$scope.dim && $dim.remove();
				_close && _close();
			},
			
			destroy: () => {
				console.log('destroy', $element);
				$element.remove();
			},
		
			$content: () => $content
		};
		
		function calculatePosition() {
			const {innerWidth: width, innerHeight: height} = window;
				
			let top = Math.round(( height - contentHeight ) / 2);
			let left = Math.round(( width - contentWidth ) / 2);
			$wrap.css('top', `${top}px`);
			$wrap.css('left', `${left}px`);
		}
		
		function refreshGrid() {
			$grids.map(i => {
				const $grid = $($grids.get(i));
				const name = $grid.attr('name');
				window.w2ui[name].refresh();
			});
		}
		
		$body.append($element);
	}
}

module(App.name).directive('bcModal', ()=> new ModalDirective)