import { module } from 'angular';
import App from '../../../app';

class SCR0505Controller {
	
	constructor ($scope, $state, $uibModalInstance, httpService, utilService, gridService, popupService, codeService, data){
		this.$scope = $scope;
		this.$state = $state;
		this.$uibModalInstance = $uibModalInstance;
		this.httpService = httpService;
		this.utilService = utilService;
		this.gridService = gridService;
		this.codeService = codeService;
		this.popupService = popupService;
		this.data = data;
		this.sysMsg;
		this.inoutMsg;
		
		if(!data.width) data.width = 750;
		if(!data.height) data.height = 700;
		
		this.initWindow(data.width, data.height);
		this.initText();
		this.initCode();
		this.initSearch();
		this.initSelect();
		this.initIntrfcGridOption();
		this.initMakeMsgId();
	}

	initCode(){
		this.sysMsgList = this.codeService.getCodesByCdIdFromMem('MSG_SYS_TYPE');
		this.inoutMsgList = this.codeService.getCodesByCdIdFromMem('MSG_INOUT_TYPE');
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
		this.searchParam.intrfcId='';
		this.searchParam.intrfcNm='';
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
			multiSelect : false,
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
				{ field: 'intrfcTypeCd', caption: this.text.dataTypeNm , size: '1%'}
			],
			onClick: (e) => {
				// prevent deselect
				let selection = w2ui[e.target].getSelection();
				if(selection.length === 1 && selection[0] === e.recid) {
					e.preventDefault();
				}
				const eTarget = e.originalEvent.target;
			}
		};
	}
	
	initMakeMsgId(){
		const data = this.data;

		this.interfaceList = this.getInterfaceList;
		this.interfaceList();
	}
	
	getInterfaceList(){
		let url = `/intrfccoms?intrfcId=${this.searchParam.intrfcId}&intrfcNm=${this.searchParam.intrfcNm}&pageSize=999999`;
		
		this.httpService.get(url, this.searchParam.intrfccoms).then(data => {
			for (var i = 0; i < data.totalCnt; i++) {
				if (data.intrfccombsOutList[i].intrfcTypeCd == 'MCI') {
					data.intrfccombsOutList[i].intrfcTypeCd = "MCA";
				} else if((data.intrfccombsOutList[i].intrfcTypeCd == 'EAI_I')) {
					data.intrfccombsOutList[i].intrfcTypeCd = "EAI";
				}
			}
			this.intrfcOptions.records = data.intrfccombsOutList;
		});
	}
	
	resetSearch(){
		this.searchParam = {};
		this.searchParam.intrfcId='';
		this.searchParam.intrfcNm='';
		this.interfaceList(true);
	}
	
	search(){
		this.interfaceList(true);
	}
	
	blur($event){
		$event.target.blur();
	}
	
	makeMsg() {
		if(!this._checkValid()) return;
		this.closeModal(true);
	}

	setSysMsg(){
		if(_.isEmpty(this.addMsg)) {
			this.msgTypes = [];
			return;
		}
		
		switch(this.addMsg.chlDscd){
			case 'INTERNAL':
				switch(this.addMsg.trxDscd){
				case 'ONLINE':
					this.msgTypes = this.msgTypeList.filter(v => v.cdVal == 'IV' || v.cdVal == 'STH' );
					break;
				case 'BATCH':
					this.msgTypes = this.msgTypeList.filter(v => v.cdVal == 'BATH' || v.cdVal == 'BATB' || v.cdVal == 'BATT' );
					break;
				}
				break;
			case 'EXTERNAL':
				switch(this.addMsg.trxDscd){
				case 'ONLINE':
					this.msgTypes = this.msgTypeList.filter(v => v.cdVal == 'IV' || v.cdVal == 'CH');
					break;
				case 'BATCH':
					this.msgTypes = this.msgTypeList.filter(v => v.cdVal == 'BATH' || v.cdVal == 'BATB' || v.cdVal == 'BATT' );
					break;
				}
				break;
		}
		
	}
	


	_checkValid(){
		const grid = w2ui[this.intrfcOptions.name];
		const selection = grid.getSelection();
		if(selection.length === 0){
			this.openAlert(this.text.selectInterfaceId);
			return false;
		}
		if(_.isEmpty(this.sysMsg)){
			this.openAlert(this.text.selectSystem);
			return false;
		} else if(_.isEmpty(this.inoutMsg)){
			this.openAlert(this.text.selectInout);
			return false;
		} 
		
		return true;
	}

	closeModal(isOk){
		const grid = w2ui[this.intrfcOptions.name];
		if(isOk){
			const selection = grid.getSelection();
			if(selection.length === 0){
				this.$uibModalInstance.dismiss();
			} else {
				var msgId =  grid.get(selection[0]).intrfcId + '_' + this.sysMsg +this.inoutMsg
				this.$uibModalInstance.close(msgId);				
			}
		} else {
			this.$uibModalInstance.dismiss();
		}
		
		setTimeout(()=>grid.destroy());
	}
	
	openAlert(alertBody){
		this.popupService.simpleAlert(this.$scope, alertBody);
	}
	
}

module(App.name).controller('SCR0505Controller', SCR0505Controller);

