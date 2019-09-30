import { module } from 'angular';
import App from '../../app';
import * as _ from 'lodash';

class UtilService {
	
	constructor($rootScope) {
		this.$rootScope = $rootScope;
		//{scope: {}, data: {}} 
		this.stateParams = {};
	}

	isEmpty(value) {
		return _.isEmpty(value);
	}
	
	uniqueId(prefix = '') {
		return _.uniqueId(prefix);
	}
	
	get(obj, key) {
		
		const _key = key.replace(/\(\)$/, '');
		return _.get(obj, _key);
	}
	
	clone(obj) {
		return _.cloneDeep(obj);
	}
	
	stringByteLength(str) {
		let byte, char, i;
		const noneAsciiCodeByte = 2;
		
		// shifting 연산으로 문자열 내부, 한글추가 여부 검사
		for (byte=i=0;char=str.charCodeAt(i++); byte+=char >> 11 ? noneAsciiCodeByte : char >> 7 ? 2:1);
		
		return byte;
	}
	
	openTab($scope, route, data){
		if(this.isEmpty(route.state)) throw new Error('state는 필수값입니다.');
		if(this.isEmpty(route.label)) throw new Error('label은 필수값입니다.');
		
		this.stateParams[route.state] = data;
		this.$rootScope.$emit('WRAP:MOVETAB', route);
	}
	
	setParams(state, params){
		this.stateParams[state] = params;
	}
	
	getParams(state){
		return this.stateParams[state];
	}
	
	setRegDttm(dttm){
		const yy = dttm.substring(0,4);
		const mm = dttm.substring(4,6);
		const dd = dttm.substring(6,8);
		const hh = dttm.substring(8,10);
		const mi = dttm.substring(10,12);
		const ss = dttm.substring(12,14);
		return yy+"/"+mm+"/"+dd+"  "+hh+":"+mi+":"+ss;
	}
	
	saveFile(blob, fileName){
		if(window.navigator.msSaveOrOpenBlob) {
			window.navigator.msSaveOrOpenBlob(blob, fileName);
		}else{
			var a = window.document.createElement('a');
			
			a.href = window.URL.createObjectURL(blob, {type: 'text/plain'});
			a.download = fileName;
			
			document.body.appendChild(a);
			a.click();
			document.body.removeChild(a);
		}
	}
}

module(App.name).service('utilService', UtilService);