import { module } from 'angular';
import App from '../../../app';

class SCR0503Controller {
	
	constructor ($scope, $uibModalInstance, httpService, utilService, gridService, popupService){
		this.$scope = $scope;
		this.$uibModalInstance = $uibModalInstance;
		this.httpService = httpService;
		this.gridService = gridService;
		this.popupService = popupService;
		
		this.initWindow(640, 460);
		this.initText();
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
	
	sendFile(){
		const $importFile = $('input:file');
		const file = $importFile[0].files[0];
		
		if(file === undefined) return;
		
		this.popupService.showLoadingBar(this.$scope);
		
		this.httpService.uploadFile('/msglayouts/fileuploads', { name: 'msglayoutFile', file })
		 .then(res => {
			 if(res.isError){
				this.uploadResult = this.text.fail;
				this.uploadResultMsg += res.data.message + '\r\n';
				this.uploadResultMsg += res.data.parameters + '\r\n';
				this.uploadResultMsg += res.data.stackTrace;
				return;
			}
			
			this.uploadResult = this.text.success;
			this.uploadResultMsg = this.text.completeUpload;
				
			this.popupService.simpleAlert(this.$scope, this.text.completeUpload);
			
			this.$uibModalInstance.close(res);
		 })
		 .finally(() => {
			this.popupService.closeLoadingBar();
		 });
	}
	
	reset(){
		this.uploadResult = ' ';
		this.uploadResultMsg = ' ';
	}
	
	closeModal(isOk){
		this.$uibModalInstance.dismiss();
	}
}

module(App.name).controller('SCR0503Controller', SCR0503Controller);

