import { module } from 'angular';
import App from '../../../app';

class SCR0504Controller {
	
	constructor ($scope, $uibModalInstance, httpService, utilService, gridService, userService,
			codeService, popupService, data){
		this.$scope = $scope;
		this.$uibModalInstance = $uibModalInstance;
		this.httpService = httpService;
		this.utilService = utilService;
		this.gridService = gridService;
		this.codeService = codeService;
		this.popupService = popupService;
		this.userService = userService;
		this.user = this.userService.getUser();
		
		this.initWindow(840, 460);
		this.initCode();
		this.initText();
		
		this.intrfc = {};
		this.data = data;
		this.addMsg = {};
		this.addMsg.msgVersion = "1";
		this.addMsg.chlDscd ="INTERNAL";
		this.initApp();
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
		this.text = $.extend({}, bxMsg.getMessages('common'), bxMsg.getMessages('manageMsg'));		
	}
	
	initCode(){
		this.tranDscdAllList = this.codeService.getCodesByCdIdFromMem('TRAN_DSCD');
		this.syncDscdList = this.codeService.getCodesByCdIdFromMem('CHL_DSCD');
		this.msgTypeList = this.codeService.getCodesByCdIdFromMem('MSG_TYPE');
	}

	initApp() {
	}
	
	setMsgOption(){
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
	
	closeModal(isOk){
		if(isOk){			
			if(this.user.roleId != 'Administrator' && this.addMsg.chlDscd == 'INTERNAL' && this.addMsg.trxDscd == 'ONLINE' && this.addMsg.msgDscd == 'STH'){
				this.popupService.simpleAlert(this.$scope, this.text.addCommonHeaderMsg);
				return;
			}
			
			if(!this._checkValid()) return;
			this.createMsgId();
		} else {
			this.$uibModalInstance.dismiss();
		}
	}

	createMsgId(){
		// const msgIdCreateDto = this.utilService.clone(this.addMsg);
		
		// this.httpService.post('/msglayouts/msgidcreate', msgIdCreateDto)
		// 				.then(res => {
		// 					if (res.isError) {
		// 						this.popupService.detailAlert(this.$scope, res.data.message, res.data.stackTrace);
		// 						return;
		// 					}
							this.$uibModalInstance.close(this.addMsg);
						// });
	}
	
	makeMsgLayoutId(){
		this.popupService.openModal('SCR0505')
						.then((code) => {
							console.log(code);
							this.addMsg.msgLayoutId = code;
						})
						.catch(()=>{})
	}
	
	selectApplication(){
		this.popupService.openModal('SCR1402')
						.then((code) => {
							this.addMsg.lv1Cd = code.appCd;
						})
						.catch(()=>{})
	}
	
	selectSndSys(){
		this.popupService.openModal('SCR1302')
						.then((sndSys) => {
							this.addMsg.sndSysNm = sndSys.sysNm;
							this.addMsg.sendSysCd = sndSys.sysCd;
						})
						.catch(()=>{})
	}
	
	selectRcvSys(){
		this.popupService.openModal('SCR1302')
						.then((rcvSys) => {
							this.addMsg.rcvSysNm = rcvSys.sysNm;
							this.addMsg.receiveSysCd = rcvSys.sysCd;
						})
						.catch(()=>{})
	}
	
	chgTrxDscd(trxCd){
		if(trxCd == "ONLINE"){
			this.syncDscdList = this.codeService.getCodesByCdIdFromMem('SYNC_DSCD_FEP');
		}else{
			this.syncDscdList = this.codeService.getCodesByCdIdFromMem('SYNC_DSCD');
		}
	}
	
	_checkValid(){
		if(_.isEmpty(this.addMsg.msgLayoutId)){
			this.openAlert(this.text.emptyMsgLayoutId);
			return false;
		} else if(_.isEmpty(this.addMsg.lv1Cd)){
			this.openAlert(this.text.emptyLv3Cd);
			return false;
		} else if(_.isEmpty(this.addMsg.chlDscd)){
			this.openAlert(this.text.emptyChlDscd2);
			return false;
		} else if(_.isEmpty(this.addMsg.trxDscd)){
			this.openAlert(this.text.emptyTrxDscd2);
			return false;
		} 
		
		return true;
	}
	
	openAlert(alertBody){
		this.popupService.simpleAlert(this.$scope, alertBody);
	}
}

module(App.name).controller('SCR0504Controller', SCR0504Controller);

