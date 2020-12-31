import { module } from 'angular';
import App from '../../app';
import * as _ from 'lodash';
import { errMsg } from '../../../environments/env.js';


class HttpService {
	
	constructor($http) {
		this.$http = $http;
	}
	
	/**
	 * @param url {string} 서버 url
	 * @param param {object} 서버 param
	 * @return promise 
	 */
	get(url = '', param) {
		
		if (_.isEmpty(url)) {
			throw new Error(errMsg.noneUrl);
		}

		if(param) {
			for(let key in param) {
				if(param.hasOwnProperty(key)) {
					if(param[key] != undefined && param[key] != null) {
						url += '&' + key + '=' + encodeURIComponent(param[key]);
					}
				}
			}
		}
		
		const config = this._getConfig();
		return this.$http.get(url, config)
					.then(res => {
							this._checkAndSetEmptyArray(res.data);
							return res.data;
						}, err => { 							
							if(err.status == 440) {
								alert('Session has expired.');
								window.location.href =  '/index.html';
								return false;
							}
							return { isError: true, data: err.data, status: err.status }
						});
	}
	
	_checkAndSetEmptyArray(data){
		Object.getOwnPropertyNames(data)
			  .filter(v => v.indexOf('List') !== -1)
			  .filter(v => data[v] === null)
			  .map(v => data[v] = []);
	}
	
	/**
	 * @param url {string} 서버 url 정보
	 * @param postData {object} post body 전문 
	 * @return promise 
	 */
	post(url = '', postData = {}) {
		
		if (_.isEmpty(url)) {
			throw new Error(errMsg.noneUrl);
		}
		
		const config = this._getConfig();
		return this.$http.post(url, postData, config)
					.then(  res => res.data,
							err => { return { isError: true, data: err.data, status: err.status }});
	}
	
	delete(url = '') {
		if (_.isEmpty(url)) {
			throw new Error(errMsg.noneUrl);
		}
		
		const config = this._getConfig();
		return this.$http.delete(url, config)
					.then(	res => res, 
							err => { return { isError: true, data: err.data, status: err.status }});
	}
	
	put(url = '', postData = {}) {
		if (_.isEmpty(url)) {
			throw new Error(errMsg.noneUrl);
		}
		
		const config = this._getConfig();
		return this.$http.put(url, postData, config)
					.then(	res => res, 
							err => { return { isError: true, data: err.data, status: err.status }});
	}
	
	uploadFile(url = '', postData = {}, paramData){
		let formData = new FormData();
		formData.append(postData.name, postData.file);
		
		if(paramData) {
			for(let key in paramData){
				if(paramData.hasOwnProperty(key)){
					formData.append(key, paramData[key]);
				}
			}
		}
		
		return this.$http.post(url, formData, this._getMultiPartConfig())
				   .then(res => res, err => { return { isError: true, data: err.data, status: err.status }});
	}
	
	uploadFileList(url = '', postData = {}, paramData){
		let formData = new FormData();
		postData.file.map((f) => {
			formData.append(postData.name, f);
		});
		
		if(paramData) {
			for(let key in paramData){
				if(paramData.hasOwnProperty(key)){
					formData.append(key, paramData[key]);
				}
			}
		}
		
		return this.$http.post(url, formData, this._getMultiPartConfig())
				   .then(res => res, err => { return { isError: true, data: err.data, status: err.status }});
	}
	
	downloadFile(url = '', postData = {}, method = 'post'){
		
		if(method === 'get'){
			if(postData) {
				for(let key in postData) {
					if(postData.hasOwnProperty(key)) {
						if(postData[key]) {
							url += '&' + key + '=' + encodeURIComponent(postData[key]);
						}
					}
				}
			}
			
			return this.$http.get(url, this._getExcelExportConfig())
			   .then(res => res, err => { return { isError: true, data: err.data, status: err.status }});
		}else{
			return this.$http.post(url, postData, this._getExcelExportConfig())
			   .then(res => res, err => { return { isError: true, data: err.data, status: err.status }});
		}
		
	}
	
	
	/**
	 * http config 설정 함수 
	 */
	_getConfig() {
		return {
			headers : {
				"content-type": "application/json; charset=utf-8"
			}
		};
	}
	
	_getMultiPartConfig(){
		return {
			headers : {
				"content-type": undefined
			}
		}
	}
	
	_getExcelExportConfig(){
		return {
			responseType: 'arraybuffer'
		}
	}
	
}

module(App.name).service('httpService', HttpService);