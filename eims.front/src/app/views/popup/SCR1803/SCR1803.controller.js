import { module } from 'angular';
import App from '../../../app';

class SCR1803Controller {
	
	constructor ($scope, $state, $uibModalInstance, httpService, utilService, gridService, popupService, data){
		this.$scope = $scope;
		this.$state = $state;
		this.$uibModalInstance = $uibModalInstance;
		this.httpService = httpService;
		this.utilService = utilService;
		this.gridService = gridService;
		this.popupService = popupService;
		this.data = data;
		
		if(!data.width) data.width = 750;
		if(!data.height) data.height = 700;
		
		this.initWindow(data.width, data.height);
		this.initText();
		this.initSearch();
		this.initSelect();
		this.initIntrfcGridOption();
		this.initGetEffectsList();
	}
	
	initWindow(width, height){
		const { top, left } = this.popupService.calculatePosition(width,height);
		
		this.width = width;
		this.height = height;
		this.top = top;
		this.left = left;
		this.zIndex = this.popupService.getModalZIndex();
	}
	
	initText(){
		this.text = $.extend({}, bxMsg.getMessages('common'), bxMsg.getMessages('manageMetaInfo'));	
	}
	
	initSearch(){
		this.searchParam = {};
	}
	
	initSelect(){
		this.select = this.gridService.getSelect(10);
	}
	
	initIntrfcGridOption(){
		this.gridHeight = this.gridService.getGridHeight(this.select.pageSize);
		this.intrfcOptions = {
			limit: this.select.pageSize,
			pageSize: this.select.pageSize,
			multiSelect: false,
			recordsCount: 0,
			recid: 'intrfcId',
			columns: [
				{
					caption: 'No', size: '40px',
					render: (data, index) => {
						const pageNumber = this.pageNumber || 1;
						return (pageNumber - 1) * this.intrfcOptions.limit + index + 1;
					}
				},
				{ field: 'intrfcId', caption: this.text.intrfcId, size : '3%'},
				{ field: 'intrfcNm', caption: this.text.intrfcNm, size : '3%'},
				{ field: 'intrfcType', caption: this.text.dataTypeNm , size: '1%'},
				{ field: 'msgLayoutId', caption: this.text.msgId , size: '2%'},
				{ 
					field: 'more', caption: this.text.showDetail, size: '1%',
					render: (data)=> {
						return `
							<button type="button" class="bw-btn bxd bxd-zoom-in" data-action="more"></button>
						`;
					}
				}
			],
			onClick: (e) => {
				// prevent deselect
				let selection = w2ui[e.target].getSelection();
				if(selection.length === 1 && selection[0] === e.recid) {
					e.preventDefault();
				}
				const eTarget = e.originalEvent.target;
				
				if(eTarget.localName === 'button'){
					
					let grid =  w2ui[this.intrfcOptions.name];
					let record = grid.get(e.recid);
					let state, label;
					
					if(record.intrfcType === 'MCI') {
						state = 'main.manageMciInterface';
						label = this.text.mciInterface;
					}else if(record.intrfcType === 'EAI_I') {
						state = 'main.manageEaiInterface';
						label = this.text.eaiInterface;
					}else if(record.intrfcType === 'FEP') {
						state = 'main.manageFepInterface';
						label = this.text.fepInterface;
					}
					
					this.utilService.openTab(this.$scope, {
						state: state,
						label: label
					}, {
						data: {
							intrfcId: record.intrfcId,
							msgLayoutId: record.msgLayoutId
						}
					});
					
				}
				
			},
			onDblClick: (e) => {
				let grid =  w2ui[this.intrfcOptions.name];
				let record = grid.get(e.recid);
				let state, label;
				
				if(record.intrfcType === 'MCI') {
					state = 'main.manageMciInterface';
					label = this.text.mciInterface;
				}else if(record.intrfcType === 'EAI_I') {
					state = 'main.manageEaiInterface';
					label = this.text.eaiInterface;
				}else if(record.intrfcType === 'FEP') {
					state = 'main.manageFepInterface';
					label = this.text.fepInterface;
				}
				
				this.utilService.openTab(this.$scope, {
					state: state,
					label: label
				}, {
					data: {
						intrfcId: record.intrfcId,
						msgLayoutId: record.msgLayoutId
					}
				});
			}
		};
	}
	
	initGetEffectsList(){
		const data = this.data;
		
		if(_.isEmpty(data)){
			this.getEffectList = this.getMetaEffectedInterfaceList;
		} else {
			if(this.data.metaEngNm) this.getEffectList = this.getMetaEffectedInterfaceList;
			else if(data.msgLayoutId) this.getEffectList = this.getMsglayoutEffectedInterfaceList;
			else this.getEffectList = this.getMetaEffectedInterfaceList;
		}
		
		this.getEffectList();
	}
	
	getMetaEffectedInterfaceList(){
		let url = `/metas/effects?metaEngNm=${this.data.metaEngNm}`;
		
		this.httpService.get(url, this.searchParam).then(data => {
			this.intrfcOptions.records = data;
		});
	}
	
	getMsglayoutEffectedInterfaceList(){
		let encodedId = encodeURIComponent(this.data.msgLayoutId);
		let url = `/msglayouts/${encodedId}/effects?`;
		
		this.httpService.get(url, this.searchParam).then(data => {
			this.intrfcOptions.records = data;
		});
	}
	
	resetSearch(){
		this.searchParam = {};
		this.getEffectList(true);
	}
	
	search(){
		this.getEffectList(true);
	}
	
	blur($event){
		$event.target.blur();
	}
	
	closeModal(isOk){
		const grid = w2ui[this.intrfcOptions.name];
		if(isOk){
			const selection = grid.getSelection();

			if(selection.length === 0){
				this.$uibModalInstance.dismiss();
			} else {
				this.$uibModalInstance.close(grid.get(selection[0]));
			}
		} else {
			this.$uibModalInstance.dismiss();
		}
		
		setTimeout(()=>grid.destroy());
	}

}

module(App.name).controller('SCR1803Controller', SCR1803Controller);

