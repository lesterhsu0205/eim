import {module} from 'angular';
import App from '../../../app';
import _ from 'lodash';

class GridDirective {
	
	constructor(){
		this.restrict = 'A';
	}
	
	controller($scope, $compile, utilService) {
		$scope.$compile = $compile;
		$scope.utilService = utilService;
	}
	
	link($scope, $element, attrs) {
		const utilService = $scope.utilService;
		// 시점문제로 인해 비동기 생성으로 변경
		setTimeout(()=> {
			const optionsKey = attrs.bcGrid;
			const page = attrs.page === 'true';

			if (!optionsKey) {
				throw new Error('grid option 을 기입해 주세요. ex) vm.[options]');
			}
			
			const options = utilService.get($scope, optionsKey);
			const uniqueId = utilService.uniqueId('bcGrid');
			
			if (!options) return;
			
			options.name = uniqueId;
			checkInstanceInW2ui(uniqueId);

			$element.w2grid(options);
			$scope.$broadcast(`gridRendered`);
			
			if (page) {
				const clickKey = attrs.pageClick;				
				const $compile = $scope.$compile;
				$element
					.after($compile(`
						<bc-page target="${uniqueId}" page-click="${clickKey}">
						</bc-page>
					`)($scope));
			}
			
			//options 에 대한 two-wap binding..
			$scope.$watch(optionsKey, (newVal, oldVal)=> {
				if (newVal.autoComplete) return;
				const keys = Object.getOwnPropertyNames(newVal);
				keys.map(key => {
					w2ui[uniqueId][key] = newVal[key];		
				});
				setTimeout(()=>{ if(w2ui[uniqueId]) w2ui[uniqueId].refresh(); });
				$scope.$broadcast(`load:${uniqueId}`);
			}, true);
			
		});
		
		function checkInstanceInW2ui(name) {
			const w2ui = window.w2ui;
			
			if (w2ui[name]) {
				
				Object.getOwnPropertyNames(w2ui)
					.filter(v=> new RegExp(`^${name}_?`).test(v))
					.map(v => delete w2ui[v]);
			}
		}
	}
}

module(App.name).directive('bcGrid', ()=> new GridDirective)