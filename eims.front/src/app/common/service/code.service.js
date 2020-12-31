import { module } from 'angular';
import App from '../../app';
import * as _ from 'lodash';

class CodeService {
	
	constructor(httpService, $q) {
		this.httpService = httpService;
		this.$q = $q;
		this.commonCodes = {};
		this.menuMap = null;
		this.init();
	}
	
	init(){
		this.isCommonCodeLoad = false;
		this._initCodes();
		this.setMenubyState();
	}

	_initCodes(){
		const commonCodes = {};

		this.httpService.get('/codes?pageSize=99999')
			.then(res => {
				if(!res.isError){
					res.commCodeDtoList.map( code =>{
						var groupCode = commonCodes[code.cdId];
						
						if(_.isEmpty(groupCode)){
							groupCode = [];
							commonCodes[code.cdId] = groupCode;
						}
						
						let cdValNm = bxMsg('code.' + code.cdId + ':' + code.cdVal);
						code.cdValNm = cdValNm ? cdValNm : code.cdValNm;
						
						groupCode.push(code);
					});
					this.isCommonCodeLoad = true;
					this.commonCodes = commonCodes;
				}
			});
	}

	getCodesList(cdIds){
		const defer = this.$q.defer();
		
		if(this.isCommonCodeLoad){
			const result = cdIds.map(cdId => this._get(this.commonCodes, cdId));
			defer.resolve(result);
		} else {
			this.httpService.get('/codes?pageSize=99999')
				.then(res => {
					if(!res.isError){
						let commonCodes = [];
						
						res.commCodeDtoList.map( code =>{
							var groupCode = commonCodes[code.cdId];
							
							if(_.isEmpty(groupCode)){
								groupCode = [];
								commonCodes[code.cdId] = groupCode;
							}
							
							groupCode.push(code);
						});
						
						const result = cdIds.map(cdId => this._get(commonCodes, cdId));
						defer.resolve(result)
					}
				});
		}
		
		return defer.promise;
	}
	
	getCodesByCdId(cdId){
		const defer = this.$q.defer();
		defer.resolve(this.isCommonCodeLoad 
				? defer.resolve(this._get(this.commonCodes, cdId))
				: defer.resolve(this._getCodesByCdId(cdId)));
		return defer.promise;
	}
	
	getCodesByCdIdFromMem(cdId){
		return this._get(this.commonCodes, cdId);
	}
	
	getCodeValList(cdId){
		const cdListTemp = this._get(this.commonCodes, cdId);
		const cdList = [];
		for (var i = 0; i < cdListTemp.length; i++) {
			
			cdList.push(cdListTemp[i].cdVal);
			
		}
		
		return cdList;
	}
	
	getCodeValNm(codes, codeVal){
		if(typeof codes === 'string') codes = this.commonCodes[codes];
		if(_.isEmpty(codes) || codes.length === 0) throw Error('코드목록이 없습니다.');
		return this._getVal(codes, codeVal).cdValNm;
	}

	setMenubyState() {
		this.menuMap = new Map();

		this.menuMap.set("main.manageMsg" , "MENU2010");
		this.menuMap.set("main.manageMciInterface" , "MENU3110");
		this.menuMap.set("main.manageEaiInterface" , "MENU3120");
		this.menuMap.set("main.manageFepInterface" , "MENU3130");
		this.menuMap.set("main.manageDeploySystem" , "MENU4010");
		this.menuMap.set("main.manageCommSystem" , "MENU4020");
		this.menuMap.set("main.manageAppCode" , "MENU4030");
		this.menuMap.set("main.manageExtInstCode" , "MENU4060");
		this.menuMap.set("main.manageMetaInfo" , "MENU4070");
		this.menuMap.set("main.manageActionHistory" , "MENU5010");
		this.menuMap.set("main.manageUser" , "MENU1010");
		this.menuMap.set("main.manageRole" , "MENU1020");
		this.menuMap.set("main.managePerm" , "MENU1030");
	}

	getMenubyState($state) {
		return this.menuMap.get($state);
	}
	
	_get(codes, key){
		if(_.isEmpty(key)) throw new Error('코드값은 필수입니다.');
		return codes[key];  
	}
	
	_getVal(codes, key){
		for(let code of codes){
			if(code.cdVal == key) return code;
		}

		//console.log('해당하는 코드값이 없습니다.', codes, key);
		
		return "";
	}
	
	_getCodesByCdId(cdId){
		return this.httpService.get(`/codes/common/${cdId}`);
	}
}

module(App.name).service('codeService', CodeService);