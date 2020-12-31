import { module } from 'angular';
import App from '../../../app';

class SCR1401Controller {
	
	constructor ($scope, $state, $timeout, httpService, utilService, popupService, codeService, gridService, userService, codes){
		this.$scope = $scope;
		this.$state = $state;
		this.$timeout = $timeout;
		this.codes = codes;
		this.httpService = httpService;
		this.utilService = utilService;
		this.codeService = codeService;
		this.gridService = gridService;
		this.userService = userService;
		this.popupService = popupService;
		this.user = this.userService.getUser();
		
		this.menuList = this.userService.getUserMenu();
		this.menuId = this.codeService.getMenubyState(this.$state.current.name);
		this.permInsert = false, this.permUpdate = false, this.permDelete = false;
		
		for (var item of this.menuList) {
			if (item.id == this.menuId) {
				if(item.permId != null) {
					if(item.permId.indexOf('insert') != -1 ) this.permInsert = true;			
					if(item.permId.indexOf('update') != -1 ) this.permUpdate = true;
					if(item.permId.indexOf('delete') != -1 ) this.permDelete = true;
					break;
				}
			}
		}

		this.initText();
		this.resetSearch(true);
		this.resetDetail();
		
		this.initGrid();
		
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
				
				// 그리드
				this.options.records = prevScope.options.records;
				this.options.recordsCount = prevScope.options.recordsCount;
				
				this.$timeout(() => {
					this.pageNumber = prevScope.pageNumber;
					this.$scope.$broadcast(`resetPage`, this.pageNumber);
				});
				
				if(prevScope.selectedAppCd && Object.keys(prevScope.selectedAppCd).length > 0) {
					// 상세
					this.selectedAppCd = prevScope.selectedAppCd;
					this._selectedAppCd = prevScope._selectedAppCd;
				}
				
				// 수정모드
					if(prevScope.isEdit){
					this.onEditMode();
				}else{
					this.offEditMode();
				}
			}
		} else {
			this.getAppList();
		}
		
		this.$scope.$on('$destroy', () => {
			this.utilService.setParams(currentStateName, {scope: this.$scope});
		});
	}
	
	initText() {
		this.text = $.extend({}, bxMsg.getMessages('common'), bxMsg.getMessages('manageApp'));
	}
	
	initGrid() {
		let columns = [{ 
			field: 'lvCd', caption: this.text.lvCd,  size: '100px', sortable: true,
			render: (data)=>{
				return 'L'+data.lvCd;
			}
		},
		{ field: 'appCd', caption: this.text.appCd, style: 'text-align : left', sortable: true,
			render: (data) => {
				return data.appCd+"("+data.appCdNm+")";
			}
		}];

		if(this.user.roleId === 'Administrator') {
			columns.push({ 
				field: 'edit', caption: bxMsg('common.edit') , size: '80px',
				render: (data)=> {
					let html = '';

					if(this.permUpdate) {
						html += '<button type="button" class="bw-btn bxd bxd-edit2" data-action="edit"></button>';
					}

					if(this.permDelete) {
						html += '<button type="button" class="bw-btn bxd bxd-trash" data-action="delete"></button>';
					}

					return html;
				}
			});
		}

		this.options = {
				limit: 99999,
				pageSize: 99999,
				recordsCount: 0,
				recid: 'appUnqKey',
				columns: columns,
				onClick: (e) => {
					const eTarget = e.originalEvent.target;
					const $eTarget = $(eTarget);
					const grid =  w2ui[e.target];
					const editData = grid.get(e.recid);
					
					if (eTarget.localName === 'button') {
						const action = $eTarget.attr('data-action');
						
						if(action === 'edit') {
							this._onEdit();
						}else{
							this.popupService.simpleConfirm(this.$scope,
									this.text.confirmTextDelete,
									()=>this.deleteApp(editData));
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
						
						if(!_.isEmpty(this._selectedAppCd)){
							return;
						}
					}
					
					this.getApp(editData);
				}
			};
	}
	
	getAppList(goToFirst = false) {
		this.httpService.get(`/apps`).then(data => {
			this.options.recordsCount = data.totalCnt;
			this.options.records = data.appcdOutList;
		});
	}
	
	getApp(app){
		const utilService = this.utilService;
		
		this.httpService.get(`/apps/${app.appCd}?lvCd=${app.lvCd}`).then(data => {
			this.selectedAppCd = utilService.clone(data);
			this._selectedAppCd = utilService.clone(data);
		});

		const $editAble = $('#none-edit');
		
		for (var i = 0; i < $editAble.length; i++) {
			$editAble[i].style.backgroundColor = "#e2e2e2";
		}
	}
	
	deleteApp(app) {
		this.httpService.delete(`/apps/${app.appCd}?lvCd=${app.lvCd}`)
						.then(data => {
							if (data.isError) return;
							
							this.resetDetail();
							this.getAppList();
						});
	}
	
	search(){
		this.getAppList(true);
	}
	
	blur($event){
		$event.target.blur();
	}
	
	resetSearch(isConst){
		if(isConst){
			this.searchParam = {};			
		}else{
			this.searchParam = {};
			this.getAppList(true);
		}
	}
	
	save() {
		const httpService = this.httpService;
		const data = this.selectedAppCd;
		const isCreateAppcd = this._isCreateAppcd();
		
		delete data.recid;
		
		if(!this._checkValid(data)) return;

		const q = isCreateAppcd
			? httpService.post('/apps', data) 
			: httpService.put('/apps', data);
		
		q.then(res => {
			if (res.isError) {
				this.popupService.detailAlert(this.$scope, res.data.message, res.data.stackTrace);
				return;	
			}
			
			this.getAppList();
			this.offEditMode();
			this.openAlert(bxMsg('common.saved'));
			
			this._selectedAppCd = this.selectedAppCd;
		});	
	}
	
	_isCreateAppcd(){
		return _.isEmpty(this._selectedAppCd);
	}
	
	cancel() {
		this.resetDetail();
	}
	
	add() {
		if(!_.isEmpty(this.options.name)) {
			w2ui[this.options.name].selectNone();
		}
		
		this.resetDetail();
		this.selectedAppCd.lvCd = '1';
		this._onEdit();
	}
	
	resetDetail() {
		this.selectedAppCd = {};
		this._selectedAppCd = {};
		this.offEditMode();
		
		const $editAble = $('#none-edit');
		
		for (var i = 0; i < $editAble.length; i++) {
			$editAble[i].style.backgroundColor = "";
		}
	}
	
	onEditMode(){
		if(_.isEmpty(this.selectedAppCd)) return;
		this._onEdit();
	}
	
	_onEdit(){
		const $forms = this._isCreateAppcd()
			? $('#searchWrap').find('input,textarea')
			: $('#searchWrap').find('div:not(.asterisk) > input,textarea');
		const $editAble = $('#searchWrap input.required,select.required');
		
		if(this._isCreateAppcd()){
			$forms.attr('disabled', false);
			$editAble.attr('disabled', false);
		}else{
			$editAble.attr('disabled', false);
		}
		
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
		if(_.isEmpty(data.appCd)){
			this.openAlert(this.text.emptyAppCode);
			return false;
		} else if(_.isEmpty(data.appCdNm)){
			this.openAlert(this.text.emptyAppCodeNm);
			return false;
		} else if(_.isEmpty(data.lvCd)){
			this.openAlert(this.text.emptyLvCd);
			return false;
		}
		
		return true;
	}

}

module(App.name).controller('SCR1401Controller', SCR1401Controller);

