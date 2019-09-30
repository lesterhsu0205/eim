import { module } from 'angular';
import App from '../../../app';

class SCR0301Controller {
	
	constructor ($scope, $state, $timeout, httpService, utilService, gridService, popupService, codeService, userService, codes){
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
		
		this.initText();	
		this.initSelect();
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
				
				if(prevScope.selectedPerm && Object.keys(prevScope.selectedPerm).length > 0) {
					// 상세
					this.selectedPerm = prevScope.selectedPerm;
					this._selectedPerm = prevScope._selectedPerm;
				}
				
				// 수정모드
					if(prevScope.isEdit){
					this.onEditMode();
				}else{
					this.offEditMode();
				}
			}
			
		} else {
			this.getPerms();
		}
		
		this.$scope.$on('$destroy', () => {
			this.utilService.setParams(currentStateName, {scope: this.$scope});
		});
	}
	
	initText() {
		this.text = $.extend({}, bxMsg.getMessages('common'), bxMsg.getMessages('mangePermission'));
	}
	
	initSelect() {
		this.select = this.gridService.getSelect(this.codes['GRID_PAGE_SIZE'][1].cdVal);
	}
	
	initGrid() {
		this.gridHeight = this.gridService.getGridHeight(this.select.pageSize);
		this.options = {
				limit: this.select.pageSize,
				pageSize: this.select.pageSize,
				recordsCount: 0,
				recid: 'permId',
				columns: [
					{
						caption: 'No',
						render: (data, index) => {
							const pageNumber = this.pageNumber || 1;
							return (pageNumber - 1) * this.options.limit + index + 1;
						}
					},
					{ field: 'permId', caption: this.text.permId},
					{ field: 'permNm', caption: this.text.permNm, attr: 'align=left'},
					{ 
						field: 'permTypeCd', caption: this.text.permTypeCd,
						render: (data) => this.codeService.getCodeValNm('PERM_TYPE', data.permTypeCd)
					},
					{ field: 'permDesc', caption: this.text.permDesc, attr: 'align=left'},
					{ 
						caption: bxMsg('common.edit'),
						render: (data)=> {
							let html = '';

							if(this.user.perm.update) {
								html += '<button type="button" class="bw-btn bxd bxd-edit2" data-action="edit"></button>';
							}

							if(this.user.perm.delete) {
								html += '<button type="button" class="bw-btn bxd bxd-trash" data-action="delete"></button>';
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
									()=>this.deletePerm(recId));
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

						if(!_.isEmpty(this._selectedPerm)){
							return;
						}
					}
					
					this.getPerm(editData.permId);
				}
			};
	}

	getPerms(goToFirst = false) {
		const httpService = this.httpService;
		const {pageNumber, pageSize} = this.getPageInfo();
		const { permId } = this.searchParam;
		let url = `/perms?pageNumber=${ goToFirst ? 1 : pageNumber}&pageSize=${pageSize}`;

		if(permId) url += `&permId=${permId}`;
		
		httpService.get(url).then(data => {
			const { permDtoList: records, totalCnt: recordsCount } = data;
		
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
	
	getPerm(id){
		const utilService = this.utilService;
		
		this.httpService.get(`/perms/${id}`).then(data => {
			this.selectedPerm = utilService.clone(data);
			this._selectedPerm = utilService.clone(data);
		});
		
		const $editAble = $('#none-edit');
		
		for (var i = 0; i < $editAble.length; i++) {
			$editAble[i].style.backgroundColor = "#e2e2e2";
		}
	}
	
	deletePerm(id = '') {
		this.httpService.delete(`/perms/${id}`)
						.then(data => {
							if (data.isError) return;
							
							this.resetDetail();
							this.getPerms();
						});
	}
	
	search(){
		this.getPerms(true);
	}
	
	blur($event){
		$event.target.blur();
	}
	
	resetSearch(isConst){
		if(isConst){
			this.searchParam = {};			
		}else{
			this.searchParam = {};
			this.getPerms(true);
		}
	}
	
	save() {
		const httpService = this.httpService;
		const data = this.selectedPerm;
		
		delete data.recid;
		
		if(!this._checkValid(data)) return;

		const q = this._isCreatePerm() 
			? httpService.post('/perms', data) 
			: httpService.put('/perms', data);
		
		q.then(res => {
			if (res.isError) {
				this.popupService.detailAlert(this.$scope, res.data.message, res.data.stackTrace);
				return;	
			}
			
			this.getPerms();
			this.openAlert(bxMsg('common.saved'));
			this.offEditMode();
			
			this._selectedPerm = this.selectedPerm;
		});	
	}
	
	_isCreatePerm(){
		return _.isEmpty(this._selectedPerm);
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
		this._onEdit();
	}
	
	pageBtnClick(num) {
		this.pageNumber = num;
		this.getPerms(num === 1);
	}
	
	getPageInfo() {
		return {
			pageNumber: this.pageNumber || 1,
			pageSize: this.select.pageSize
		};
	}
	
	resetDetail() {
		this.selectedPerm = {};
		this._selectedPerm = {};
		this.offEditMode();

		const $editAble = $('#none-edit');
		for (var i = 0; i < $editAble.length; i++) {
			$editAble[i].style.backgroundColor = "";
		}
	}
	
	onEditMode(){
		if(_.isEmpty(this.selectedPerm)) return;
		this._onEdit();
	}
	
	_onEdit(){
		const $forms = this._isCreatePerm()
			? $('#searchWrap').find('input,textarea,select')
			: $('#searchWrap').find('div:not(.asterisk) > input,textarea');
		const $editAble = $('#searchWrap input.required,select.required');
			
		$forms.attr('disabled', false);
		$editAble.attr('disabled', false);
		
		this.isEdit = true;
	}
	
	offEditMode(){
		const $forms = $('#searchWrap').find('input,textarea,select');
		$forms.attr('disabled', true);
		this.isEdit = false;
	}
	
	openAlert(alertBody){
		this.popupService.simpleAlert(this.$scope, alertBody);
	}
	
	_checkValid(data){
		if(_.isEmpty(data.permId)){
			this.openAlert(bxMsg('permission.emptyId'));
			return false;
		} else if(_.isEmpty(data.permTypeCd)){
			this.openAlert(bxMsg('permission.emptyCd'));
			return false;
		}
		return true;
	}
	
	refreshGrid(){
		const grid =  w2ui[this.options.name];
		grid.refresh();
	}
}

module(App.name).controller('SCR0301Controller', SCR0301Controller);