import {module} from 'angular';
import App from '../../../../app';

class PagenationDirective {
	
	constructor(){
		this.restrict = 'E';
		this.template = `
			<div class="bw-paging add-mg-t">
				<div class="ctr-btn">
					<button type="button" 
							class="btn-paging bxd bxd-toggle bxd-rotate-270"
							ng-click="first()"></button>
					<button type="button" 
							class="btn-paging bxd bxd-arrow-left"
							ng-click="prev()"></button>
				</div>
				<div class="paging-btn">
					<button type="button" class="btn-paging"
							ng-repeat="idx in items" 
							ng-class="{'on': idx === currentPage}"
							ng-click="move(idx)">
							{{idx}}</button>
				</div>
				<div class="ctr-btn">
					<button type="button" class="btn-paging bxd bxd-arrow-right"
							ng-click="post()" ></button>
					<button type="button" class="btn-paging bxd bxd-toggle bxd-rotate-90"
							ng-click="last()">
					</button>
				</div>
			</div>
		`;
		
		this.scope = {
			target: '@',
			pageClick: '='
		};
	}
	
	controller($scope) {
		$scope.text = {
			"first": '처음',
			"pre": "이전",
			"post": "다음",
			"last": '마지막',
		};
		
		const $parent = $scope.$parent;
		
		const target = window.w2ui[$scope.target];
		let _click;
		
		if ($scope.pageClick) {
			_click = $scope.pageClick.bind($parent.vm || $parent);
		}
		
		$scope.currentPage = 1;
		
		const pageSize = +target.pageSize;
		
		$scope.$on(`load:${$scope.target}`, () => {
			$scope.caculatePageList = () => {
				const rowNum = target.limit;
				const total = target.recordsCount;			
				const totalPage = Math.floor((total-1) / rowNum) + 1;
				const {currentPage} = $scope;
				const fIdx = pageSize * Math.floor((currentPage-1) / pageSize) + 1;
				const isFirstList = fIdx === 1;
				const isLastList = fIdx + pageSize - 1 >= totalPage;
				const itemsSize = isLastList ? totalPage - fIdx + 1 : pageSize;

				$scope.items = Array.from(new Array(itemsSize), (v, idx)=> {
					return fIdx + idx;
				});
				
				if(total === 0){
					$scope.items = [1];
					$scope.currentPage = 1;
				}
			
				$scope.totalPage = totalPage;
				$scope.isFirstList = isFirstList;
				$scope.isLastList = isLastList;
			}
			
			$scope.caculatePageList();
		});
		
		$scope.$on(`resetPage`, (event, data) => {
			$scope.currentPage = data ? data : 1;
		});
	
		$scope.prevPage = 1;
		
		$scope.move = (num) => {
			$scope.prevPage = $scope.currentPage;
			$scope.currentPage = num;
			_click && _click(num);
		};
		
		$scope.prev = () => {
			if ($scope.currentPage > 1) {
				$scope.prevPage = $scope.currentPage;
				let tmpPage = $scope.currentPage - pageSize;
				if (tmpPage < 1) tmpPage = 1;
				$scope.currentPage = tmpPage;
				$scope.caculatePageList();	
			}
			const num = $scope.currentPage;
			_click && _click(num);
		};
		
		$scope.post = () => {
			
			if ($scope.currentPage < $scope.totalPage) {
				$scope.prevPage = $scope.currentPage;
				let tmpPage = pageSize + $scope.currentPage;
				if (tmpPage > $scope.totalPage) tmpPage = $scope.totalPage;
				$scope.currentPage = tmpPage;
				$scope.caculatePageList();	
			}
			const num = $scope.currentPage;
			_click && _click(num);
		}
		
		$scope.first = () => {
			$scope.prevPage = $scope.currentPage;
			$scope.currentPage = 1;
			$scope.caculatePageList();
			const num = $scope.currentPage;
			_click && _click(num);
		}
		
		$scope.last = () => {
			$scope.prevPage = $scope.currentPage;
			$scope.currentPage = $scope.totalPage;
			$scope.caculatePageList();
			const num = $scope.currentPage;
			_click && _click(num);
		}
		
	}
	
	link($scope, $element, attrs) {
		
	}
}

module(App.name).directive('bcPage', () => new PagenationDirective)