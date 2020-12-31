import { module } from 'angular';
import App from '../../../app';

class SCR0201Controller {
	
	constructor ($scope, $state, $timeout, httpService, utilService, codeService, gridService, popupService, userService, codes){
		this.$scope = $scope;
		this.$state = $state;
		this.$timeout = $timeout;
		this.codes = codes;
		this.httpService = httpService;
		this.utilService = utilService;
		this.codeService = codeService;
		this.gridService = gridService;
		this.popupService = popupService;
		this.userService = userService;
		this.user = this.userService.getUser();
		this.roleId = null;
		this.menuId = null;
		this.isMenuParent = false;
		this.initText();	
		this.initSelect();
		this.initGrid();

		
		let count = 0;
		this.$scope.$on(`gridRendered`, () => {
			count ++;
			count === 3 && this.initPrevData();
		});
	}

    getMenuName(id) {
		var name = this.text.id;
		
/*
		for (var i = 0; this.text.length; i++) {
			if(this.text[i] == id) {
				name = this.text[i];
				break;
			}
		}
	*/
		return this.text[id];
	
	}
	initPrevData() {
		const currentStateName = this.$state.current.name;
		const param = this.utilService.getParams(currentStateName);

		if(!_.isEmpty(param)){
			if(param.scope) {
				let prevScope = param.scope.vm;
				
				// 탐색
				this.searchParam = prevScope.searchParam;
				
				// 그리드 pageSize
				this.select.pageSize = prevScope.select.pageSize;
				this.roleOptions.limit = prevScope.select.pageSize;
				this.gridHeight = prevScope.gridHeight;
				
				// 그리드
				this.roleOptions.records = prevScope.roleOptions.records;
				this.roleOptions.recordsCount = prevScope.roleOptions.recordsCount;
				
				this.$timeout(() => {
					this.pageNumber = prevScope.pageNumber;
					this.$scope.$broadcast(`resetPage`, this.pageNumber);
				});
				
				if(prevScope.selectedRole && Object.keys(prevScope.selectedRole).length > 0) {
					// 상세
					this.selectedRole = prevScope.selectedRole;
					this._selectedRole = prevScope._selectedRole;
				}
				
				if(prevScope.menuOptions) {
					this.menuOptions.records = prevScope.menuOptions.records;
					this.menuOptions.recordsCount = prevScope.menuOptions.recordsCount;
				}
				
				if(prevScope.permOptions) {
					this.permOptions.records = prevScope.permOptions.records;
					this.permOptions.recordsCount = prevScope.permOptions.recordsCount;
				}
				
				// 수정모드
					if(prevScope.isEdit){
					this.onEditMode();
				}else{
					this.offEditMode();
				}
			}
			
		}else{
			this.getGridData();
		}
		
		this.$scope.$on('$destroy', () => {
			this.utilService.setParams(currentStateName, {scope: this.$scope});
		});
	}
	initText() {
		this.text = $.extend({}, bxMsg.getMessages('common'), bxMsg.getMessages('manageRole'), bxMsg.getMessages('menu'));
		
	}

	initSelect() {
		this.select = { pageSize: 5 };
	}
	
	initGrid() {
		this.roleOptions = {
				limit: this.select.pageSize,
				pageSize: this.select.pageSize,
				autoComplete: false,
				recordsCount: 0,
				recid: 'roleId',
				columns: [
					{
						caption: 'No', size: '80px',
						render: (data, index) => {
							const pageNumber = this.pageNumber || 1;
							return (pageNumber - 1) * this.roleOptions.limit + index + 1;
						}
					},
					{ field: 'roleId', caption: this.text.roleId},
					{ field: 'roleNm', caption: this.text.roleNm},
					{ 
						caption: bxMsg('common.edit') ,  size: '80px',
						render: (data)=> {
							let html = '';
							html += '<button type="button" class="bw-btn bxd bxd-edit2" data-action="edit"></button>';
							html += '<button type="button" class="bw-btn bxd bxd-trash" data-action="delete"></button>';
							
							return html;
						}
					}
				],
				onClick: (e) => {
					const gridName = e.target;
					const recId = e.recid;
					const originalEvent = e.originalEvent;
					
					const eTarget = originalEvent.target;
					const $eTarget = $(eTarget);

					const grid =  w2ui[gridName];
					const editData = grid.get(recId);
					
					if (eTarget.localName === 'button') {
						const action = $eTarget.attr('data-action');
						const isEdit = action === 'edit';
						if (isEdit) {
							this._onEdit();	
						}
						else {
							this.popupService.simpleConfirm(this.$scope,
									this.text.confirmTextDelete,
									()=>this.deleteRole(recId));
							e.preventDefault();
							return;
						}
					} else {
						this.offEditMode();
					}
					this.$scope.$apply();
					
					// prevent deselect
					let selection = w2ui[e.target].getSelection();
					if(selection.length === 1 && selection[0] === e.recid) {
						e.preventDefault();

						if(!_.isEmpty(this._selectedRole)){
							return;
						}
					}
					
					this.getRole(editData.roleId);
					this.getMenuList(editData.roleId);
					this.permOptions.records = [];
					this.roleId = editData.roleId;
//					this.getPermList(editData.roleId);
				}
			};
		
		this.menuOptions = {
				limit: 9999,
				pageSize: 9999,
				recordsCount: 0,
				recid: 'id',
				columns: [
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
					{ field: name, caption: this.text.menuNm, sortable: true, 
						render: (data) => {
							const menuid = data.id;
							return this.getMenuName(menuid);
						}
					},
					{ 
						caption: bxMsg('common.edit') ,  size: '80px',
						render: (data)=> {
							let html = '';
							html += '<button type="button" class="bw-btn bxd bxd-trash" data-action="delete"></button>';

							return html;
						}
					}
				],
				onClick: (e) => {
					// prevent deselect
					let selection = w2ui[e.target].getSelection();
					if(selection.length === 1 && selection[0] === e.recid) {
						e.preventDefault();
					}
					
					
					const originalEvent = e.originalEvent;
					const grid =  w2ui[this.menuOptions.name];
					const eTarget = e.originalEvent.target;
					const recid = e.recid;
					const editData = grid.get(e.recid);
					this.menuId = editData.id;

					if(!_.isEmpty(editData.parentId)) {
						this.isMenuParent = false;
					} else {
						this.isMenuParent = true;
					}

					if (eTarget.localName === 'button') {
						this.popupService.simpleConfirm(this.$scope,
								this.text.confirmTextDelete,
								()=>this.deleteMenu(this.selectedRole.roleId, e.recid));
					}
					
					this.getPermList(this.roleId,this.menuId);
				}
			};
		
		
		this.permOptions = {
				limit: 9999,
				pageSize: 9999,
				recordsCount: 0,
				recid: 'permId',
				columns: [
					{
						caption: 'No', size: '80px',
						render: (data, index) => {
							const pageNumber = this.pageNumber || 1;
							return (pageNumber - 1) * this.permOptions.limit + index + 1;
						}
					},
					{ field: 'permId', caption: this.text.permId, sortable: true},
					{ field: 'permNm', caption: this.text.permNm, sortable: true},
					{ 
						caption: bxMsg('common.edit') ,  size: '80px',
						render: (data)=> {
							let html = '';
							html += '<button type="button" class="bw-btn bxd bxd-trash" data-action="delete"></button>';

							return html;
						}
					}
				],
				onClick: (e) => {
					// prevent deselect
					let selection = w2ui[e.target].getSelection();
					if(selection.length === 1 && selection[0] === e.recid) {
						e.preventDefault();
					}
					
					
					const originalEvent = e.originalEvent;
					const eTarget = originalEvent.target;
					
					if (eTarget.localName === 'button') {
						this.popupService.simpleConfirm(this.$scope,
								this.text.confirmTextDelete,
								()=>this.deletePerm(this.selectedRole.roleId, e.recid));
					}
				}
			};
	}
	
	getGridData(goToFirst = false) {
		const httpService = this.httpService;
		const {pageNumber, pageSize} = this.getPageInfo();
		var url = `/roles?pageNumber=${ goToFirst ? 1 : pageNumber}&pageSize=${pageSize}`;
		
		httpService.get(url).then(data => {
			const { roleDtoList: records, totalCnt: recordsCount } = data;
		
			this.roleOptions.records = records;
			this.roleOptions.recordsCount = recordsCount;
			
			if(!_.isEmpty(this.roleOptions.name)) {
				w2ui[this.roleOptions.name].selectNone();
			}
			
			if(goToFirst) {
				this.pageNumber = 1;
				this.$scope.$broadcast(`resetPage`, this.pageNumber);
			}
		});
	}
	
	getRole(id){
		const utilService = this.utilService;
		
		this.httpService.get(`/roles/${id}`).then(data => {
			this.selectedRole = utilService.clone(data);
			this._selectedRole = utilService.clone(data);
			this.offEditMode();
		});

		const $editAble = $('#none-edit');
		
		for (var i = 0; i < $editAble.length; i++) {
			$editAble[i].style.backgroundColor = "#e2e2e2";
		}
	}
	
	getMenuList(roleId){
		this.httpService.get(`/roles/${roleId}/menus`).then(data => {
			this.menuOptions.records = this.gridService.convertDataToTreeData(data, "parentId");
		});
	}
		
	getPermList(roleId, menuId){
		this.httpService.get(`/roles/${roleId}/perms?menuId=${menuId}`).then(data => {
			if (!_.isEmpty(data[0])) {
				this.permOptions.records = data;
			} else {
				this.permOptions.records = [];
			}
		});
	}
	
	deleteRole(id = '') {
		this.httpService.delete(`/roles/${id}`)
						.then(data => {
							this.resetDetail();
							this.getGridData();
						});
	}
	
	deleteMenu(roleId = '', menuId='') {
		this.httpService.delete(`/roles/${roleId}/menus/${menuId}`)
						.then(data => {
							if (data.isError) return;
							this.getMenuList(roleId);
						});
	}
	
	deletePerm(roleId = '', permId='') {
		this.httpService.delete(`/roles/${roleId}/perms/${permId}?menuId=${this.menuId}`)
						.then(data => {
							if (data.isError) return;
							this.getPermList(roleId, this.menuId);
						});
	}
	
	search(){
		this.getGridData(true);
	}
	
	blur($event){
		$event.target.blur();
	}
	
	save() {
		const httpService = this.httpService;
		const isCreateRole = this._isCreateRole();
		const data = this.selectedRole;
		
		delete data.recid;
		
		if(!this._checkValid(data)) return;

		const q = isCreateRole 
			? httpService.post('/roles', data) 
			: httpService.put('/roles', data);
		
		q.then(res => {
			if (res.isError) {
				this.popupService.detailAlert(this.$scope, res.data.message, res.data.stackTrace);
				return;	
			}
			
			this.getGridData();

			if(isCreateRole) {
				this.getRole(data.roleId);
			}else{
				this.offEditMode();
			}
			
			this.openAlert(bxMsg('common.saved'));
			
			this._selectedRole = this.selectedRole;
		});	
	}
	
	_isCreateRole(){
		return _.isEmpty(this._selectedRole);
	}
	
	cancel() {
		this.resetDetail();
	}
	
	change() {
		this.roleOptions.limit = this.select.pageSize;
		this.pageBtnClick(1);
	}
	
	add() {
		if(!_.isEmpty(this.roleOptions.name)) {
			w2ui[this.roleOptions.name].selectNone();
		}
		
		this.resetDetail();
		this._onEdit();
	}
	
	onClickAddMenu(){
		if (this.utilService.isEmpty(this.selectedRole)){
			this.openAlert(bxMsg('manageRole.emptyRoleId2'));
			return;
		}

		this.popupService.openModal('SCR0402',
				{
					text: this.text,
					roleId : this.selectedRole.roleId
				})
				.then((records) => this.updateMenus(records))
				.catch(()=>{});
	}
	
	onClickAddPerm() {
		if (this.utilService.isEmpty(this.selectedRole)){
			this.openAlert(bxMsg('manageRole.emptyRoleId2'));
			return;
		}

		if (_.isEmpty(this.menuId)){
			this.openAlert(bxMsg('manageRole.emptyMenuId'));
			return;
		}

		
		
		this.popupService.openModal('SCR0302',
									{
										text: this.text,
										roleId : this.selectedRole.roleId,
										menuId : this.menuId
									})
									.then((records) => this.updatePerms(records))
									.catch(()=>{});
	}
	
	updateMenus(records){
		const roleId = this.selectedRole.roleId;
		
		this.httpService.put(`/roles/${roleId}/menus`, records)
						.then(res => {
							if(res.isError){
								this.popupService.detailAlert(this.$scope, res.data.message, res.data.stackTrace);
								return;
							}

							this.getMenuList(roleId);
							this.openAlert(bxMsg('common.saved'));
						});
	}
	
	updatePerms(records){
		const roleId = this.selectedRole.roleId;
		
		this.httpService.put(`/roles/${roleId}/perms?menuId=${this.menuId}`, records)
						.then(res => {
							if(res.isError){
								this.popupService.detailAlert(this.$scope, res.data.message, res.data.stackTrace);
								return;
							}
							
							this.getPermList(roleId,this.menuId);
							this.openAlert(bxMsg('common.saved'));
						});
	}
	
	pageBtnClick(num) {
		this.pageNumber = num;
		this.getGridData(num === 1);
	}
	
	getPageInfo() {
		return {
			pageNumber: this.pageNumber || 1,
			pageSize: this.select.pageSize
		};
	}
	
	resetDetail() {
		this.selectedRole = {};
		this._selectedRole = {};
		this.menuOptions.records = [];
		this.permOptions.records = [];
		this.offEditMode();

		const $editAble = $('#none-edit');
		for (var i = 0; i < $editAble.length; i++) {
			$editAble[i].style.backgroundColor = "";
		}
	}
	
	onEditMode(){
		if(_.isEmpty(this.selectedRole)) return;
		this._onEdit();
	}
	
	_onEdit(){
		const $forms = this._isCreateRole()
			? $('#searchWrap').find('input,textarea,select')
			: $('#searchWrap').find('div:not(.asterisk) > input,textarea');
		const $editAble = $('#searchWrap input.required,select.required');
			
		$forms.attr('disabled', false);
		$editAble.attr('disabled', false);
		
		this.isEdit = true;
	}
	
	offEditMode(){
		const $forms = $('#searchWrap input,textarea');
		$forms.attr('disabled', true);
		this.isEdit = false;
	}
	
	openAlert(alertBody){
		this.popupService.simpleAlert(this.$scope, alertBody);
	}
	
	_checkValid(data){
		if(_.isEmpty(data.roleId)){
			this.openAlert(bxMsg('manageRole.emptyRoleId'));
			return false;
		} else if(_.isEmpty(data.roleNm)){
			this.openAlert(bxMsg('manageRole.emptyRoleNm'));
			return false;
		} 
		return true;
	}
}

module(App.name).controller('SCR0201Controller', SCR0201Controller);

