import {module} from 'angular';
import App from '../../../app';

class LnbDirective {
	
	constructor(){
		this.restrict = 'E';
		this.templateUrl = 'app/common/directive/lnb/lnb.html';
		this.scope = {
			items: '='
		};
	}
	
	controller($scope, $state, $timeout) {
		$scope.$state = $state;
		$state.defaultErrorHandler((error) => {
			console.log(error);
		});

		const $lnb = $('#lnb');
		
		$scope.click = (e, item, idx) => {
			
			const isButton = e.target.localName === 'button';
		
			if (isButton) {
				close(idx);
			}
			else {
				$($lnb.find('li.on')).removeClass('on');
				const $li = $($lnb.find('li').get(idx));
				$li.addClass('on');
				checkStateAndMove(idx);
				
				if (item.state) {
					$timeout(() => {
						$state.go(item.state);	
					});
				}
			}
		};
		
		$scope.prev = () => {
			if (prevEvent) return;
			const width = getLiWidth();
			const prevLeft = parseInt($lnb.css('left'));
			let left = prevLeft + width;
			
			if (left > 0) {
				left = 0;
			}
			move(left);
		}
		
		$scope.post = () => {
			const prevLeft = parseInt($lnb.css('left'));
			const wrapWidth = getWrapWidth();
			const width = getLiWidth();
			const maxWidth = $scope.items.length * width - wrapWidth;
			let left = prevLeft - width;
			
			if (-left >= maxWidth) {
				left = -maxWidth;
			}
			move(left);
		}
		
		function close(idx){
			const items = $scope.items;
			const lastIdx = items.length - 1;
			
			items.splice(idx, 1);
			
			let _idx = idx;
			
			if (idx === lastIdx) {
				_idx = idx-1;
			}
						
			$scope._clickLi(_idx);
		}
		
		function checkStateAndMove(idx) {
			const $li = $($lnb.find('li').get(idx));
			const liOffsetLeft = $li.get(0).offsetLeft; 
			const liOffsetEnd = liOffsetLeft + getLiWidth();
			
			const wrapLeft = parseInt($lnb.css('left'));
			const wrapWidth = getWrapWidth();
			
			const isOver = wrapWidth < liOffsetEnd + wrapLeft;
			const isAbove = liOffsetLeft < -wrapLeft;  
			
			let left = 0;
			if (isOver) {
				left = -(liOffsetEnd - wrapWidth);
				move(left);
			}
			else if (isAbove) {
				left = -liOffsetLeft;
				move(left);
			}
			else {
				const distance = -(liOffsetEnd - wrapWidth); 
				
				left = distance > 0 ? 0 : distance;

				move(left);
			}
		}
		
		
		let prevEvent = null;
		
		function move(left = 0) {
			prevEvent = $lnb.stop(true, true).animate({ left }, 400, () => {
				prevEvent = null;
				checkBtnState();
			});
		}
		
		function getLiWidth() {
			const $li = $lnb.find('li');
			const width = $li.width();
			const padding = parseInt($li.css('padding-left')) + parseInt($li.css('padding-right'));
			const border = parseInt($li.css('border-left-width')) + parseInt($li.css('border-right-width'));
			
			return width + padding + border;
		}
		
		function getWrapWidth() {
			const wrapWidth = $('#lnbWrap').width();
			const btnWrapWidth = $('#lnbBtnWrap').width();
			return wrapWidth - btnWrapWidth;
		}
		
		function checkBtnState() {
			
			const $leftBtn = $('#lnbLeftBtn');
			const $rightBtn = $('#lnbRightBtn');
			
			const wrapLeft = parseInt($lnb.css('left'));
			const wrapWidth = getWrapWidth();
			
			const $lastLi = $lnb.find('li').last();

			if($lastLi.length === 0) {
				$leftBtn.attr('disabled', true);
				$rightBtn.attr('disabled', true);
				return;
			}

			const lastLiOffsetEnd = $lastLi.get(0).offsetLeft + getLiWidth();
			const isOverLastLi = wrapWidth < lastLiOffsetEnd + wrapLeft;
			
			if (wrapLeft < 0) {
				$leftBtn.attr('disabled', false);
			} 
			else {
				$leftBtn.attr('disabled', true);
			} 
			
			if (isOverLastLi) {
				$rightBtn.attr('disabled', false);
			}
			else {
				$rightBtn.attr('disabled', true);
			}
		}
		
		$scope._clickLi = (idx) => {
			setTimeout(()=>{
				$($lnb.find('li').get(idx)).click();
			});
		}
	}
	
	link($scope, element, attrs) {
		const $lnb = $('#lnb');
		const wrapWidth = $('#lnbWrap').width();
		
		$scope.$watch('items', (newVal, oldVal)=> {
			
			if ($scope.items.length === 0) {
				$scope.$state.go('main.manageMsg');
				return;
			}
			
			const newValLength = newVal.length;
			const oldValLength = oldVal.length;
			const isAdd = newValLength > oldValLength;
		
			if (isAdd) {
				const idx = newValLength - 1;
				$scope._clickLi(idx);
			}
		}, true);
	}
}

module(App.name).directive('bcLnb', ()=> new LnbDirective)