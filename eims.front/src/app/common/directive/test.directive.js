import {module} from 'angular';
import App from '../../app';

class TestDirective {
	
	constructor(){
		console.log('call TestDirective :)');
		this.restrict = 'E';
		this.template = `
		<ul class="search-data-wrap">
			<li ng-repeat="label in searchItems">
				{{label}}
			</li>
		</ul>
		`;
		this.scope = {
			searchItems: '='
		};
	}
	
	controller($scope) {
		// dom [document object model] 이 생성되기 이전..
		console.log('testDirective Controller :)', $scope);
		
	}
	
	link($scope, element, attrs) {
		// dom 모델 생성 후 시점..
		console.log('testDriective Link:)', $scope, element, attrs);
		$scope.$watch('searchItems', ()=>{
			console.log('asdfas')
		});
	}
	
}

module(App.name).directive('testDiv', ()=> new TestDirective);