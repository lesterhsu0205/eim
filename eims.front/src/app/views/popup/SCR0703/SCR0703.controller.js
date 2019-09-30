import { module } from 'angular';
import App from '../../../app';

class SCR0703Controller {
	
	constructor ($scope, $uibModalInstance, $timeout, httpService, utilService, gridService, popupService, data){
		this.$scope = $scope;
		this.$timeout = $timeout;
		this.$uibModalInstance = $uibModalInstance;
		this.httpService = httpService;
		this.gridService = gridService;
		this.popupService = popupService;
		this.data = data;
		
		this.initWindow(740, 560);
		this.initText();
		
		this.$timeout(() => {
			const $importFile = $('input:file');
			
			$importFile.on('change', (event) => {
				const $importFile = $('input:file');
				const files = $importFile[0].files;
				
				if(files === undefined) return;
				if(files.length > 10) {
					$importFile.val('');
					this.popupService.simpleAlert(this.$scope, this.text.uploadFileCount);
					return;
				}
				
				let fileNames = '';
				
				for(let i = 0; i < files.length; i++) {
					fileNames += '[' + (i+1) + '] ' + files[i].name + '\r\n';
				}
				
				$('.filenames').val(fileNames);
			});
		});
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

	sendFile(){
		const $importFile = $('input:file');
		const files = $importFile[0].files;
		
		if(files === undefined) return;
		
		this.popupService.showLoadingBar(this.$scope);
		
		let fileList = [];
		
		for(let i = 0; i < files.length; i++) {
			fileList.push(files[i]);
		}
		
		this.httpService.uploadFileList('/intrfccoms/import/intrfcfiles', { name:'intrfcFile', file: fileList }, {intrfcTypeCd: this.data.intrfcTypeCd})
				 .then(res => {
					 console.log(res);
					 if(res.isError){
						this.isError = true;
						this.uploadResult = this.text.fail;
						this.uploadResultMsg = '';
						res.data.message && (this.uploadResultMsg += res.data.message + '\r\n');
						res.data.parameters && (this.uploadResultMsg += res.data.parameters.map((param) => {return param + '\r\n';}));
						res.data.stackTrace && (this.uploadResultMsg += res.data.stackTrace);
						return;
					}
					 
					 this.isError = false;
					 this.testList = res.data.map((data) => {
						 let fileConts = '';
						 data.message && (fileConts += data.message + '\r\n');
						 data.parameter && (fileConts += data.parameter.map((param) => {return param + '\r\n';}));
						 
						 return {
							isError: data.status == 0 ? false : true,
							fileName: data.fileName,
							fileConts: fileConts
						 };
					 });
				 })
				 .finally(() => {
					this.popupService.closeLoadingBar();
				});
	}
	
	sendFile2(){
		const $importFile = $('input:file');
		const files = $importFile[0].files;
		
		if(files === undefined) return;
		
		this.popupService.showLoadingBar(this.$scope);
		
		let fileList = [];
		
		for(let i = 0; i < files.length; i++) {
			fileList.push(files[i]);
		}
		
		this.httpService.uploadFileList('/intrfccoms/import/definition', { name:'intrfcFile', file: fileList }, {intrfcTypeCd: this.data.intrfcTypeCd})
				 .then(res => {
					 console.log(res);
					 if(res.isError){
						this.isError = true;
						this.uploadResult = this.text.fail;
						this.uploadResultMsg = '';
						res.data.message && (this.uploadResultMsg += res.data.message + '\r\n');
						res.data.parameters && (this.uploadResultMsg += res.data.parameters.map((param) => {return param + '\r\n';}));
						res.data.stackTrace && (this.uploadResultMsg += res.data.stackTrace);
						return;
					}
					 
					 this.isError = false;
					 this.testList = res.data.map((data) => {
						 let fileConts = '';
						 data.message && (fileConts += data.message + '\r\n');
						 data.parameter && (fileConts += data.parameter.map((param) => {return param + '\r\n';}));
						 
						 return {
							isError: data.status == 0 ? false : true,
							fileName: data.fileName,
							fileConts: fileConts
						 };
					 });
				 })
				 .finally(() => {
					this.popupService.closeLoadingBar();
				});
	}
	
	toggle(fileIndex, isError) {
		if(!isError) return;
		
		let $fileConts = $('#file' + fileIndex);
		
		if($fileConts.is(':visible')){
			$fileConts.hide();
		}else{
			$fileConts.show();
		}
	}
	
	openFile() {
		this.reset();
		$('input:file').click();
	}
	
	reset(){
		$('.filenames').val('');
		$('input:file').val('');
		this.isError = false;
		this.testList = [];
		this.uploadResult = ' ';
		this.uploadResultMsg = ' ';
	}
	
	closeModal(isOk){
		this.$uibModalInstance.dismiss();
	}
}

module(App.name).controller('SCR0703Controller', SCR0703Controller);

