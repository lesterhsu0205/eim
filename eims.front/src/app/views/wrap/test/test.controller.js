import { module } from 'angular';
import App from '../../../app';

class TestController {
	
	constructor($state) {
		console.log('call Test Controller');
		this.test();
		this.$state = $state;
		this.modal;
	}
	
	test() {
		console.log('call test Method:)');
	}
	
	goChild() {
		this.$state.go('abstract.child1');
	}
	
	goExample() {
		this.$state.go('main.example');
	}
	
	
	openModal() {
		this.modal.open();
	}
	
	hideModal() {
		this.modal.close();
	}
	
	a() {
		console.log(123123123);
	}
}

module(App.name).controller('TestController', TestController);