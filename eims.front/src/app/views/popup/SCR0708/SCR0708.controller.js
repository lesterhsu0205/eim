import { module } from 'angular';
import App from '../../../app';

class SCR0708Controller {
	
	constructor ($scope, $uibModalInstance, $timeout, httpService, utilService, codeService, gridService, popupService, data){
		this.$scope = $scope;
		this.$uibModalInstance = $uibModalInstance;
		this.$timeout = $timeout;
		this.httpService = httpService;
		this.utilService = utilService;
		this.gridService = gridService;
		this.popupService = popupService;
		this.codeService = codeService;
		this.data = data;
		this.codes = codeService.commonCodes;
		
		this.initWindow(960, 640);
		this.initText();
		this.initGridOption();
		setTimeout(()=>this.getDetailGrid(this.data.reqData));
		
		this.setDeployDscd();
	}
	
	setDeployDscd() {
		let txt = '';
		
		if(this.data.reqData.deployResultCd.indexOf('SUCCESS') !== -1) {
			if(this.data.reqData.deployResultCd.indexOf('_') !== -1) {
				txt = this.text.redeploy;
			}else{
				txt = this.text.deploy;
			}
		}else{
			if(this.data.reqData.deployResultCd.indexOf('_') !== -1) {
				txt = this.text.redeployFail;
			}else{
				txt = this.text.deployFail;
			}
		}
		
		this.deployDesd = txt;
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
	
	initGridOption(){
		this.options = {
			limit: 99999,
			pageSize: 99999,
			recordsCount: 0,
			recid: 'systemCd',
			columns: [
				{ field: 'systemCd', caption: this.text.sysCd, size: '1%'},
				{ field: 'systemNm', caption: this.text.sysNm, size: '1%', attr: 'align=left' },
				{ field: 'systemUrl', caption: this.text.deployUrl, size: '1%', attr: 'align=left' },
				{ 
					field: 'deployStatus', caption: this.text.deployResultCd, size: '1%',
					render: (data)=> {
						if(data.deployStatus === 'FAIL') {
							return '<span class="chr-c-orange">' + data.deployStatus + '</span>'
						}
						
						
						return data.deployStatus;
					}
				}	
			],
			onClick: (e) => {
				// prevent deselect
				let selection = w2ui[e.target].getSelection();
				if(selection.length === 1 && selection[0] === e.recid) {
					e.preventDefault();
				}
				
				const grid = w2ui[e.target];
				
				this.selectedIntrfcdeploysysdtDto = grid.get(e.recid);
				this.$scope.$apply();
			}
		}
	}

	getDetailGrid(reqData) {
		this.httpService.get(`/intrfccoms/deployhistoryresults?`, reqData).then(res => {		
			this.options.records = res.intrfcDeployResponseList;
		});
	}
		
	closeModal(isOk){
		const grid = w2ui[this.options.name];
	
		this.$uibModalInstance.dismiss();
		
		setTimeout(()=>grid.destroy());
	}
}

module(App.name).controller('SCR0708Controller', SCR0708Controller);

