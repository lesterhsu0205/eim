import { module } from 'angular';
import App from '../../../app';

class SCR0101Controller {
	
	constructor ($scope, $state, $timeout, httpService, utilService, gridService, popupService, codeService, userService, codes){
		this.$scope = $scope;
		this.$state = $state;
		this.$timeout = $timeout;
		this.httpService = httpService;
		this.utilService = utilService;
		this.gridService = gridService;
		this.codeService = codeService;
		this.popupService = popupService;
		this.userService = userService;
		this.codes = codes;
		this.user = this.userService.getUser();
		
		this.initText();	
		this.initSelect();
		this.resetSearch();
		this.resetDetail();
		
		this.initGrid();
		this.user = userService.getUser();
		
		this.$scope.$on(`gridRendered`, () => {
			this.initPrevData();
		});
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
				this.options.limit = prevScope.select.pageSize;
				this.gridHeight = prevScope.gridHeight;
				
				// 그리드
				this.options.records = prevScope.options.records;
				this.options.recordsCount = prevScope.options.recordsCount;
				
				this.$timeout(() => {
					this.pageNumber = prevScope.pageNumber;
					this.$scope.$broadcast(`resetPage`, this.pageNumber);
				});
				
				if(prevScope.selectedUser && Object.keys(prevScope.selectedUser).length > 0) {
					// 상세
					this.selectedUser = prevScope.selectedUser;
					this._selectedUser = prevScope._selectedUser;
				}
				
				// 수정모드
					if(prevScope.isEdit){
					this.onEditMode();
				}else{
					this.offEditMode();
				}
			}
			
		} else {
			this.getGridData();
		}
		
		this.$scope.$on('$destroy', () => {
			this.utilService.setParams(currentStateName, {scope: this.$scope});
		});
	}
	
	initText() {
		this.text = $.extend({}, bxMsg.getMessages('common'), bxMsg.getMessages('manageUser'));
	}

	initSelect() {
		this.select = this.gridService.getSelect(this.codes['GRID_PAGE_SIZE'][1].cdVal);
	}
	
	initGrid() {
		this.gridHeight = this.gridService.getGridHeight(this.select.pageSize);
		this.options = {
				limit: this.select.pageSize,
				pageSize: this.select.pageSize,
				autoComplete: false,
				recordsCount: 0,
				recid: 'userId',
				columns: [
					{
						caption: 'No', size: '80px',
						render: (data, index) => {
							const pageNumber = this.pageNumber || 1;
							return (pageNumber - 1) * this.options.limit + index + 1;
						}
					},
					{ field: 'userId', caption: this.text.userId, size: '100px', sortable: true},
					{ field: 'userNm', caption: this.text.userNm, sortable: true, attr: 'align=left'},
					{ field: 'email', caption: this.text.email, sortable: true, attr: 'align=left'},
					{ field: 'dutyNm', caption: this.text.dutyNm, sortable: true},
					{ field: 'deptNm', caption: this.text.deptNm, sortable: true},
					{ field: 'roleId', caption: this.text.roleId, ortable: true},
					{ 
						caption: bxMsg('common.edit') ,  size: '80px',
						render: (data)=> {
							let html = '';

							if(this.user.roleId === 'Administrator') {
								if(this.user.perm.update) {
									html += '<button type="button" class="bw-btn bxd bxd-edit2" data-action="edit"></button>';
								}

								if(this.user.perm.delete) {
									html += '<button type="button" class="bw-btn bxd bxd-trash" data-action="delete"></button>';
								}
								
							}else{
								if(this.user.perm.update && data.userId === this.user.userId) {
									html += '<button type="button" class="bw-btn bxd bxd-edit2" data-action="edit"></button>';
								}
							}

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
									()=>this.deleteGridData(recId));
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
						
						if(!_.isEmpty(this._selectedUser)){
							return;
						}
					}
					
					this.getUser(editData.userId);
				}
			};
	}
	
	getGridData(goToFirst = false) {
		const {pageNumber, pageSize} = this.getPageInfo();
		var url = `/users?pageNumber=${ goToFirst ? 1 : pageNumber}&pageSize=${pageSize}`;
		
		this.httpService.get(url, this.searchParam).then(data => {
			const { userOutList: records, totalCnt: recordsCount } = data;
		
			this.options.records = records;
			this.options.recordsCount = recordsCount;
			
			if(!_.isEmpty(this.options.name)) {
				w2ui[this.options.name].selectNone();
			}
			
			if(goToFirst) {
				this.pageNumber = 1;
				this.$scope.$broadcast(`resetPage`, this.pageNumber);
			}
		});
	}
	
	getUser(id){
		const utilService = this.utilService;
		
		this.httpService.get(`/users/${id}`).then(data => {
			this.selectedUser = utilService.clone(data);
			
			this.selectedUser.userPwdConfirm =  this.selectedUser.userPwd;
			this._selectedUser = utilService.clone(this.selectedUser);
		});
		
		const $editAble = $('#none-edit, #none-edit2');
		
		for (var i = 0; i < $editAble.length; i++) {
			$editAble[i].style.backgroundColor = "#e2e2e2";
		}
	}
	
	deleteGridData(id = '') {
		const httpService = this.httpService;
		
		return httpService.delete(`/users/${id}`)
				.then(data => {
					if (data.isError) return;
					
					this.resetDetail();
					this.getGridData();
				});
	}
	
	search(){
		this.getGridData(true);
	}
	
	blur($event){
		$event.target.blur();
	}
	
	resetSearch(){
		this.searchParam = {};

		if(this.user.roleId !== 'Administrator') {
			this.searchParam.userId = this.user.userId;
		}
	}
	
	save() {
		const httpService = this.httpService;
		const data = this.selectedUser;
		const isCreateUser = this._isCreateUser();
		
		delete data.recid;
		
		if(!this._checkValid(data)) return;

		const q = isCreateUser 
			? httpService.post('/users', data) 
			: httpService.put('/users', data);
		
		q.then(res => {
			if (res.isError) {
				this.popupService.detailAlert(this.$scope, res.data.message, res.data.stackTrace);
				return;	
			}
			
			this.getGridData();
			this.openAlert(bxMsg('common.saved'));
			this.offEditMode();
			
			this._selectedUser = this.selectedUser;
		});	
	}
	
	_isCreateUser(){
		return _.isEmpty(this._selectedUser);
	}
	
	cancel() {
		this.resetDetail();
	}
	
	change() {
		this.options.limit = this.select.pageSize;
		this.gridHeight = this.gridService.getGridHeight(this.select.pageSize);
		this.pageBtnClick(1);
		this.options.name && w2ui[this.options.name].focus();
	}
	
	add() {
		if(!_.isEmpty(this.options.name)) {
			w2ui[this.options.name].selectNone();
		}
		
		this.resetDetail();
		this._onEdit(true);
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
		this.selectedUser = {};
		this._selectedUser = {};
		this.offEditMode();
		
		const $editAble = $('#none-edit, #none-edit2');
		
		for (var i = 0; i < $editAble.length; i++) {
			$editAble[i].style.backgroundColor = "";
		}
		
	}
	
	onEditMode(){
		if(_.isEmpty(this.selectedUser)) return;
		this._onEdit();
	}
	
	_onEdit(isCreate = false){
		const $forms = isCreate
			? $('#searchWrap').find('input:not(.none-edit),textarea,select, div')
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
	
	openRoleId(){
		if(!this.isEdit) return;
		
		this.popupService.openModal('SCR0202', 
									{ 
										text: this.text,
										codes: this.codes
									})
									.then((role) => this.setRole(role))
									.catch(()=>{})
	}
	
	setRole(role){
		this.selectedUser.roleId = role.roleId;
	}
	
	openAlert(alertBody){
		this.popupService.simpleAlert(this.$scope, alertBody);
	}
	
	_checkValid(data){
		if(_.isEmpty(data.userId)){
			this.openAlert(bxMsg('manageUser.emptyUserId'));
			return false;
		} else if(_.isEmpty(data.userNm)){
			this.openAlert(bxMsg('manageUser.emptyUserNm'));
			return false;
		} else if(_.isEmpty(data.roleId)){
			this.openAlert(bxMsg('manageUser.emptyRoleId'));
			return false;
		} else if(_.isEmpty(data.userPwd)){
			this.openAlert(bxMsg('manageUser.emptyUserPwd'));
			return false;
		} else if(data.userPwd !== data.userPwdConfirm){
			this.openAlert(bxMsg('manageUser.notMatchPwd'));
			return false;
		} else if(_.isEmpty(data.deptNm)) {
			this.openAlert(bxMsg('manageUser.emptydeptNm'));
			return false;
		}
		return true;
	}
	
	refreshGrid(){
		const grid =  w2ui[this.options.name];
		grid.refresh();
	}
}

module(App.name).controller('SCR0101Controller', SCR0101Controller);