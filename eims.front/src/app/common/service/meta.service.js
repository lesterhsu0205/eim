import { module } from 'angular';
import App from '../../app';
import * as _ from 'lodash';

class MetaService {
	
	constructor($q, httpService) {
		this.httpService = httpService;
		this.$q = $q;
		this.init();
	}
	
	init(){
		this._getMeta();
	}
	
	_resetMeta(){
		this.metaMap = {};
		this.metaList = [];
	}

	_getMeta(){
		this.httpService.get('/metas?pageNumber=1&pageSize=99999')
						.then(res => {
							this._resetMeta();
							res.metabsOutList.map(v => {
								this.metaMap[v.metaEngNm] = v;
								this.metaList.push(v);
							});
						});
	}
	
	getMetaListLikeKorNm(metaKorNm){
		const pattern = new RegExp(`^${metaKorNm}`);
	
		return this.metaList.filter(v => pattern.test(v.metaKorNm));
	}

	getMetaListLikeEngNm(metaEngNm){
		const pattern = new RegExp(`^${metaEngNm}`);
	
		return this.metaList.filter(v => pattern.test(v.metaEngNm));
	}
	
	getMetaListSameKorNm(metaKorNm){	
		return this.metaList.filter(v => metaKorNm === v.metaKorNm);
	}
	
	getMetaByMetaEngNm(metaEngNm){
		return this.metaMap[metaEngNm];
	}
	
	_getMetaFromServerByKorNm(metaKorNm){
		let url = '/metas?pageNumber=1&pageSize=10';
		if(metaKorNm) url += `&metaKorNm=${metaKorNm}`;
		
		return this.httpsService.get(url);
	}
	
	syncMeta(){
		const defer = this.$q.defer();
		
		this.httpService.get(`/metas/syncs`).then(res => {
			if(res.isError){
				this.openAlert(res.data.message);
				defer.reject();
				return;
			}
			
			this._getMeta();
			defer.resolve();
		});
		
		return defer.promise;
	}
	
	getMeta(){
		const defer = this.$q.defer();
		
		this.httpService.get('/metas?pageNumber=1&pageSize=99999')
				.then(res => {
					this._resetMeta();
					res.metabsOutList.map(v => {
						this.metaMap[v.metaEngNm] = v;
						this.metaList.push(v);
					});
					defer.resolve();
				});
		return defer.promise;
	}
}

module(App.name).service('metaService', MetaService);