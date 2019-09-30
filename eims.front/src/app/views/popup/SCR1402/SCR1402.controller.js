import { module } from 'angular';
import App from '../../../app';

class SCR1402Controller {
	
	constructor ($scope, $state, $uibModalInstance, httpService, utilService, gridService, popupService, data){
		this.$scope = $scope;
		this.$state = $state;
		this.$uibModalInstance = $uibModalInstance;
		this.httpService = httpService;
		this.utilService = utilService;
		this.gridService = gridService;
		this.popupService = popupService;
		this.data = data;
		this.data.appId = '';
		if(!data.width) data.width = 440;
		if(!data.height) data.height = 500;

		this.initWindow(data.width, data.height);
		this.initText();
		this.initAppGridOption();
		this.getAppList();
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
		this.text = $.extend({}, bxMsg.getMessages('common'), bxMsg.getMessages('manageApp'));	
	}
	
	initAppGridOption(){
		this.appOptions = {
				limit: 99999,
				pageSize: 99999,
				multiSelect: false,
				recordsCount: 0,
				recid: 'appUnqKey',
				columns: [
					{ 
						field: 'lvCd', caption: this.text.lvCd,  size: '100px',
						render: (data)=>{
							return 'L'+data.lvCd;
						}
					},
					{ 
						field: 'appCd', caption: this.text.appCd, style: 'text-align : left',
						render: (data) => {
							return data.appCd+"("+data.appCdNm+")";
						}
					}
				],
				onClick: (e) => {
					// prevent deselect
					let selection = w2ui[e.target].getSelection();
					if(selection.length === 1 && selection[0] === e.recid) {
						e.preventDefault();
					}
				},
				onDblClick: (e) => this.closeModal(true)
			};
	}
	resetSearch(){
		this.data.appId = '';
		this.getAppList();
	}
	search() {
		this.getAppList();
	}
	getAppList(){
		this.httpService.get(`/apps?appCd=${this.data.appId}`).then(data => {
			this.appOptions.recordsCount = data.totalCnt;
			this.appOptions.records = data.appcdOutList;
		});
	}
	
	closeModal(isOk){
		const grid = w2ui[this.appOptions.name];
		
		if(isOk){
			const selection = grid.getSelection();

			if(selection.length === 0){
				this.$uibModalInstance.dismiss();
			} else {
				let select = grid.get(selection[0]);
				this.$uibModalInstance.close(select);
			}
		} else {
			this.$uibModalInstance.dismiss();
		}

		setTimeout(()=>grid.destroy());
	}
}

module(App.name).controller('SCR1402Controller', SCR1402Controller);

