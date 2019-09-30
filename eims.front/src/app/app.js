/**
 * Created by UI/UX Team on 2018. 1. 19..
 */

import {module} from 'angular';

import UiRouter from '@uirouter/angularjs';
import OcLazyLoad from 'oclazyload';
import bxuipAngularJS from 'bxuip-angular.js-ui';

const App = module('app', [
    UiRouter,
    OcLazyLoad,
    bxuipAngularJS,
    'ui.bootstrap'
]);

export default App;

