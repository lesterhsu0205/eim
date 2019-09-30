import { module } from 'angular';
import App from '../../../app';

class SCR0702Controller {
	
	constructor ($scope, $uibModalInstance, httpService, utilService, gridService, 
			codeService, popupService, data){
		this.$scope = $scope;
		this.$uibModalInstance = $uibModalInstance;
		this.httpService = httpService;
		this.utilService = utilService;
		this.gridService = gridService;
		this.codeService = codeService;
		this.popupService = popupService;
		
		this.initWindow(840, 460);
		this.initCode();
		this.initText();
		this.intrfc = {};
		this.data = data;
		
		this.tranDscdList = utilService.clone(this.tranDscdAllList);
		
		if(data.intrfcTypeCd === 'MCI'){
			this.tranDscdList.pop();
			this.intrfc.trxDscd = 'ONLINE';
		}
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
		this.text = $.extend({}, bxMsg.getMessages('common'), bxMsg.getMessages('manageMciInterface'));
	}
	
	initCode(){
		this.tranDscdAllList = this.codeService.getCodesByCdIdFromMem('TRAN_DSCD');
		this.syncDscdList = this.codeService.getCodesByCdIdFromMem('SYNC_DSCD');
	}
	
	disabled(){
		
		const $trxDscd = $('#trxDscd').find('select');
		$trxDscd.attr('disabled', false);
		
	}
	
	closeModal(isOk){
		if(isOk){
			if(!this._checkValid()) return;
			this.createInterFace();
		} else {
			this.$uibModalInstance.dismiss();
		}
	}

	createInterFace(){
		// const requestBody = this.utilService.clone(this.intrfc);
		// requestBody.intrfcTypeCd = this.data.intrfcTypeCd;
		
		// this.httpService.post('/intrfccoms/intrfcidcreate', requestBody)
		// 				.then(res => {
		// 					if (res.isError) {
		// 						this.popupService.detailAlert(this.$scope, res.data.message, res.data.stackTrace);
		// 						return;
		// 					}
							
		// 					this.intrfc.intrfcId = res.intrfcId;
							this.intrfc.sndSys = this.sndSys;
							this.intrfc.rcvSys = this.rcvSys;
							this.$uibModalInstance.close(this.intrfc);
						// });
	}
	
	selectApplication(){
		this.popupService.openModal('SCR1402')
						.then((code) => {
							this.intrfc.lv1Cd = code.appCd;
						})
						.catch(()=>{})
	}
	
	selectSndSys(){
		this.popupService.openModal('SCR1302')
						.then((sndSys) => {
							this.sndSys = sndSys;
							this.intrfc.sendSysCd = sndSys.sysCd;
						})
						.catch(()=>{})
	}
	
	selectRcvSys(){
		this.popupService.openModal('SCR1302')
						.then((rcvSys) => {
							this.rcvSys = rcvSys;
							this.intrfc.receiveSysCd = rcvSys.sysCd;
						})
						.catch(()=>{})
	}
	
	chgTrxDscd(trxCd){
		if(this.data.intrfcTypeCd != "FEP"){
			return;
		}
		
		if(trxCd == "ONLINE"){
			this.syncDscdList = this.codeService.getCodesByCdIdFromMem('SYNC_DSCD_FEP');
		}else{
			this.syncDscdList = this.codeService.getCodesByCdIdFromMem('SYNC_DSCD');
		}
	}
	
	_checkValid(){
		if(_.isEmpty(this.intrfc.intrfcId)){
			this.openAlert(this.text.emptyIntrcId);
			return false;
		} else if(_.isEmpty(this.intrfc.lv1Cd)){
			this.openAlert(this.text.emptyLv3Cd);
			return false;
		} else if(_.isEmpty(this.sndSys)){
			this.openAlert(this.text.emptySndSys);
			return false;
		} else if(_.isEmpty(this.rcvSys)){
			this.openAlert(this.text.emptyRcvSys);
			return false;
		} else if(_.isEmpty(this.intrfc.trxDscd)){
			this.openAlert(this.text.emptyTrxDscd);
			return false;
		}
		return true;
	}
	
	openAlert(alertBody){
		this.popupService.simpleAlert(this.$scope, alertBody);
	}
}

module(App.name).controller('SCR0702Controller', SCR0702Controller);

