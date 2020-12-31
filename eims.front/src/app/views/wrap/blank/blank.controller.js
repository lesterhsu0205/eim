import { module } from 'angular';
import App from '../../../app';

class BlankController {
	
	constructor($scope) {}
	
}

module(App.name).controller('BlankController', BlankController);