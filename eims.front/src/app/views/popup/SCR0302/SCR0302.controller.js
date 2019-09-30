import { module } from 'angular';
import App from '../../../app';

class SCR0302Controller {
	
	constructor ($scope, $state, $uibModalInstance, httpService, utilService, gridService, popupService, data){
		this.$scope = $scope;
		this.$state = $state;
		this.$uibModalInstance = $uibModalInstance;
		this.httpService = httpService;
		this.utilService = utilService;
		this.gridService = gridService;
		this.popupService = popupService;
		this.data = data;
		
		if(!data.width) data.width = 640;
		if(!data.height) data.height = 420;
		
		this.initWindow(data.width, data.height);
		this.initText();
		this.initPermGridOption();
		this.getPermList();
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
		this.text = $.extend(this.data.text, {
			useYn: bxMsg('mangePermission.useYn'),
			permId: bxMsg('mangePermission.permId'),
			permNm: bxMsg('mangePermission.permNm')
		});
	}
	
	initPermGridOption(){
		this.permOptions = {
				limit: 99999,
				pageSize: 99999,
				recordsCount: 0,
				recid: 'permId',
				columns: [
					{
						caption: 'No', size: '80px',
						render: (data, index) => {
							return index + 1;
						}
					},
					{ field: 'check', caption: this.text.useYn, editable: { type: 'checkbox'}},
					{ field: 'permId', caption: this.text.permId, sortable: true},
					{ field: 'permNm', caption: this.text.permNm, sortable: true},
				],
				onClick: (e) => {
					// prevent deselect
					let selection = w2ui[e.target].getSelection();
					if(selection.length === 1 && selection[0] === e.recid) {
						e.preventDefault();
					}
					
					
					const gridName = e.target;
					const recId = e.recid;
					const originalEvent = e.originalEvent;
					
					const eTarget = originalEvent.target;
					const $eTarget = $(eTarget);
					const grid =  w2ui[gridName];
					const editData = grid.get(recId);
					const action = $eTarget.attr('data-action');
					
					if(action == "check" || editData.check == false){
						editData.check = true;
					}else if(action == "check" || editData.check == true){
						editData.check = false;
					}
				},
				onDblClick: (e) => this.closeModal(true)
			};
	}
	
	getPermList() {
		const roleId = this.data.roleId;
		
		this.httpService.get(`/roles/${roleId}/permpopups`).then(data => {
			this.permOptions.records = data;
			this.permOptions.recordsCount = data.length;
		});
	}
	
	closeModal(isOk){
		const grid = w2ui[this.permOptions.name];
		
		if(isOk){
			const records = grid.records;
			
			records.map(v => {
				delete v.w2ui;
				delete v.recid;
			});
			
			this.$uibModalInstance.close(records);
		} else {
			this.$uibModalInstance.dismiss();
		}
		
		setTimeout(()=>grid.destroy());
	}
}

module(App.name).controller('SCR0302Controller', SCR0302Controller);

