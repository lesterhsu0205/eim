import { module } from 'angular';
import App from '../../../app';

class SCR1301Controller {
	
	constructor ($scope, $state, $timeout, httpService, gridService, popupService, utilService, codeService, userService, codes){
		this.$scope = $scope;
		this.$state = $state;
		this.$timeout = $timeout;
		this.httpService = httpService;
		this.utilService = utilService;
		this.codeService = codeService;
		this.gridService = gridService;
		this.popupService = popupService;
		this.userService = userService;
		this.codes = utilService.clone(codes);
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
				
				if(prevScope.selectedSrsyss && Object.keys(prevScope.selectedSrsyss).length > 0) {
					// 상세
					this.selectedSrsyss = prevScope.selectedSrsyss;
					this._selectedSrsyss = prevScope._selectedSrsyss;
				}
				
				// 수정모드
					if(prevScope.isEdit){
					this.onEditMode();
				}else{
					this.offEditMode();
				}
				
			}
		} else {
			this.getSrsyssList();
		}
		
		this.$scope.$on('$destroy', () => {
			this.utilService.setParams(currentStateName, {scope: this.$scope});
		});
	}
	
	initText() {
		this.text = $.extend({}, bxMsg.getMessages('common'), bxMsg.getMessages('manageCommSystem'));
	}

	initSelect() {
		this.select = this.gridService.getSelect(this.codes['GRID_PAGE_SIZE'][1].cdVal);		
	}
	
	initGrid() {
		let columns = [{
			caption: 'No', size: '80px',
			render: (data, index) => {
				const pageNumber = this.pageNumber || 1;
				return (pageNumber - 1) * this.options.limit + index + 1;
			}
		},
		{ field: 'sysCd', caption: this.text.sysCd, size: 1,  sortable: true},
		{ field: 'sysNm', caption: this.text.sysNm, size: 1,  sortable: true},
		{ field: 'sysCdDesc', caption: this.text.sysCdDesc, size:3, attr: 'align=left'},	
		{ field: 'noncoreYn', caption: this.text.noncoreYn, size: 1,  sortable: true},];

		if(this.user.roleId === 'Administrator') {
			columns.push({ 
				caption: bxMsg('common.edit') ,  size: '80px',
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

		this.gridHeight = this.gridService.getGridHeight(this.select.pageSize);
		this.options = {
				limit: this.select.pageSize,
				pageSize: this.select.pageSize,
				recordsCount: 0,
				recid: 'sysCd',
				columns: columns,
				onClick: (e) => {
					const eTarget = e.originalEvent.target;
					const $eTarget = $(eTarget);
					
					if (eTarget.localName === 'button') {
						const action = $eTarget.attr('data-action');
						if(action === 'edit') {
							 this._onEdit();
						}else{
							this.popupService.simpleConfirm(this.$scope,
									this.text.confirmTextDelete,
									()=>this.deleteSrsyss(e.recid));
							
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
						
						if(!_.isEmpty(this._selectedSrsyss)){
							return;
						}
					}
					
					this.getSrsyss(e.recid);
				}
			};
	}
	
	getSrsyssList(goToFirst = false) {
		const {pageNumber, pageSize} = this.getPageInfo();
		let url = `/srsyss?pageNumber=${ goToFirst ? 1 : pageNumber}&pageSize=${pageSize}`;
		
		this.httpService.get(url, this.searchParam).then(data => {
			
			if(data.srsysbsOutList == null && data.totalCnt == 0){
				this.options.records = "";
				this.options.recordsCount = 0;
			}else{				
				const { srsysbsOutList: records, totalCnt: recordsCount } = data;
				
				this.options.records = records;
				this.options.recordsCount = recordsCount;
			}
			
			if(!_.isEmpty(this.options.name)) {
				w2ui[this.options.name].selectNone(); 
			}
			
			if(goToFirst) {
				this.pageNumber = 1;
				this.$scope.$broadcast(`resetPage`, this.pageNumber);
			}
		});
	}
	
	getSrsyss(sysCd){
		const utilService = this.utilService;
		
		this.httpService.get(`/srsyss/${sysCd}`).then(data => {
			this.selectedSrsyss = utilService.clone(data);
			this._selectedSrsyss = utilService.clone(data);
		});
		

		const $editAble = $('#none-edit');
		
		for (var i = 0; i < $editAble.length; i++) {
			$editAble[i].style.backgroundColor = "#e2e2e2";
		}
		
	}
	
	deleteSrsyss(sysCd = '') {
		this.httpService.delete(`/srsyss/${sysCd}`)
				.then(data => {
					if (data.isError) return;
					
					this.resetDetail();
					this.getSrsyssList();
				});
	}
	
	search(){
		this.getSrsyssList(true);
	}
	
	blur($event){
		$event.target.blur();
	}
	
	resetSearch(isConst){
		if(isConst){
			this.searchParam = {};			
		}else{
			this.searchParam = {};
			this.getSrsyssList(true);
		}
	}
	
	save() {
		const httpService = this.httpService;
		const data = this.selectedSrsyss;
		const isCreateDeploySys = this._isCreateSrsyss();
		
		delete data.recid;
		
		if(!this._checkValid(data)) return;

		const q = isCreateDeploySys 
			? httpService.post('/srsyss', data) 
			: httpService.put('/srsyss', data);
		
		q.then(res => {
			if (res.isError) {
				this.popupService.detailAlert(this.$scope, res.data.message, res.data.stackTrace);
				return;	
			}
			
			this.getSrsyssList();
			this.offEditMode();
			this.openAlert(bxMsg('common.saved'));
			
			this._selectedSrsyss = this.selectedSrsyss;
		});	
	}
	
	_isCreateSrsyss(){
		return _.isEmpty(this._selectedSrsyss);
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
		this.getSrsyssList(num === 1);
	}
	
	getPageInfo() {
		return {
			pageNumber: this.pageNumber || 1,
			pageSize: this.select.pageSize
		};
	}
	
	resetDetail() {
		this.selectedSrsyss = {};
		this._selectedSrsyss = {};
		this.offEditMode();
		
		const $editAble = $('#none-edit');
		
		for (var i = 0; i < $editAble.length; i++) {
			$editAble[i].style.backgroundColor = "";
		}
	}
	
	onEditMode(){
		if(_.isEmpty(this.selectedSrsyss)) return;
		this._onEdit();
	}
	
	_onEdit(){
		const $forms = this._isCreateSrsyss()
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
		if(_.isEmpty(data.sysCd)){
			this.openAlert(bxMsg('commSystem.emptySysCd'));
			return false;
		} else if(_.isEmpty(data.sysNm)){
			this.openAlert(bxMsg('commSystem.emptySysNm'));
			return false;
		}
		return true;
	}
	
	refreshGrid(){
		const grid =  w2ui[this.options.name];
		grid.refresh();
	}
}

module(App.name).controller('SCR1301Controller', SCR1301Controller);