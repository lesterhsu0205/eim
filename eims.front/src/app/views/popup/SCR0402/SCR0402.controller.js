import { module } from 'angular';
import App from '../../../app';

class SCR0402Controller {
	
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
		this.initMenuGridOption();
		this.getMenuList();
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
			save: bxMsg('common.save'),
			cancel: bxMsg('common.cancel')
		});
	}
	
	initMenuGridOption(){
		const utilService = this.utilService;
		this.menuOptions = {
				limit: 99999,
				pageSize: 99999,
				recordsCount: 0,
				recid: 'id',
				columns: [
					{
						field: 'check', caption: '', size: '80px', 
						render: (data, index) => {
							const prtId = data.parentId;
							
							if(utilService.isEmpty(prtId)){
								return  ;
							}else {
								return `
								<input type="checkbox" id="checked" data-action="check"></input>
								`;
							}
						}
					},
					{ field: 'id', caption: this.text.menuId, sortable: true, 
						render: (data) => {
							const prtId = data.parentId;
							
							if(this.utilService.isEmpty(prtId)){
								return data.id;
							}else {
								return "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+data.id;
							}
						}
					
					},
					{ field: 'name', caption: this.text.menuNm, sortable: true}
				],
				onClick: (e) => {
					// prevent deselect
					let selection = w2ui[e.target].getSelection();
					if(selection.length === 1 && selection[0] === e.recid) {
						e.preventDefault();
					}
					
					
					const grid =  w2ui[e.target];
					const editData = grid.get(e.recid);
					const action =  $(e.originalEvent.target).attr('data-action');
					
					if(action == "check" && editData.check == false){
						const parentId = editData.parentId;
						
						editData.check = true;
						grid.records.filter(v => v.id == parentId)
									.map(v => v.check = true);
						
					}else if(action == "check" && editData.check == true){
						const parentId = editData.parentId;
						
						editData.check = false;
						grid.records.filter(v => v.id == parentId)
									.map(v => v.check = false);
					}
				},
				onDblClick: (e) => this.closeModal(true)
			};
	}
	
	getMenuList() {
		const roleId = this.data.roleId;
		
		this.httpService.get(`/roles/${roleId}/menupopups`).then(data => {
			this.menuOptions.records = this.gridService.convertDataToTreeData(data, "parentId");
			
			setTimeout(() => {
				this.gridService.expandAll(this.menuOptions, 2);
			}, 300);
		});
	}
	
	closeModal(isOk){
		const grid = w2ui[this.menuOptions.name];
		
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

module(App.name).controller('SCR0402Controller', SCR0402Controller);

