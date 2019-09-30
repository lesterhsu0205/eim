import { module } from 'angular';

import App from '../../../app';

class SCR1201Controller {
	
	constructor ($scope, $state, $timeout, httpService, utilService, gridService, popupService, codeService, userService, codes){
		this.$scope = $scope;
		this.$state = $state;
		this.$timeout = $timeout;
		this.codes = codes;
		this.httpService = httpService;
		this.utilService = utilService;
		this.gridService = gridService;
		this.popupService = popupService;
		this.codeService = codeService;
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
				
				if(prevScope.selectedDeploySys && Object.keys(prevScope.selectedDeploySys).length > 0) {
					// 상세
					this.selectedDeploySys = prevScope.selectedDeploySys;
					this._selectedDeploySys = prevScope._selectedDeploySys;
				}
				
				// 수정모드
					if(prevScope.isEdit){
					this.onEditMode();
				}else{
					this.offEditMode();
				}
				
			}
		} else {
			this.getDeploySysList();
		}
		
		this.$scope.$on('$destroy', () => {
			this.utilService.setParams(currentStateName, {scope: this.$scope});
		});
	}
	
	initText() {
		this.text = $.extend({}, bxMsg.getMessages('common'), bxMsg.getMessages('manageDeploySystem'));
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
		{ field: 'deploySysCd', caption: this.text.deploySysCd, size: 1, sortable: true},
		{ field: 'deploySysNm', caption: this.text.deploySysNm, size: 2, sortable: true, attr: 'align=left'},
		{ field: 'deploySysUrl', caption: this.text.deploySysUrl, size: 3, sortable: true, attr: 'align=left'},
		{ field: 'deploySysDesc', caption: this.text.deploySysDesc, size: 3,attr: 'align=left'}];

		if(this.user.roleId === 'Administrator') {
			columns.push({ 
				caption: bxMsg('common.edit'), size: '80px',
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
			});
		}
		
		this.gridHeight = this.gridService.getGridHeight(this.select.pageSize);
		this.options = {
				limit: this.select.pageSize,
				autoComplete: false,
				pageSize: 10,
				recordsCount: 0,
				recid: 'deploySysCd',
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
									()=>this.deleteDeploySys(e.recid));
							
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

						if(!_.isEmpty(this._selectedDeploySys)){
							return;
						}
					}
					
					this.getDeploySys(e.recid);
				}
			};
	}
	
	getDeploySysList(goToFirst = false) {
		const { pageNumber, pageSize } = this.getPageInfo();
		let url = `/depolysyss?pageNumber=${ goToFirst ? 1 : pageNumber}&pageSize=${pageSize}`;
		
		this.httpService.get(url, this.searchParam).then(data => {
			
			if(data.depolysysbsOutList == null && data.totalCnt == 0){
				this.options.records = "";
				this.options.recordsCount = 0;
			}else{				
				const { depolysysbsOutList: records, totalCnt: recordsCount } = data;
				
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
	
	getDeploySys(deploySysCd){
		const utilService = this.utilService;
		
		this.httpService.get(`/depolysyss/${deploySysCd}`).then(data => {
			this.selectedDeploySys = utilService.clone(data);
			this._selectedDeploySys = utilService.clone(data);
		});

		const $editAble = $('#none-edit, #none-edit2');
		
		for (var i = 0; i < $editAble.length; i++) {
			$editAble[i].style.backgroundColor = "#e2e2e2";
		}
	}
	
	deleteDeploySys(deploySysCd = '') {		
		this.httpService.delete(`/depolysyss/${deploySysCd}`)
				.then(data => {
					if (data.isError) return;
					
					this.resetDetail();
					this.getDeploySysList();
				});
	}
	
	search(){
		this.getDeploySysList(true);
	}
	
	blur($event){
		$event.target.blur();
	}
	
	resetSearch(isConst){
		
		if(isConst){
			this.searchParam = {};			
		}else{
			this.searchParam = {};
			this.getDeploySysList(true);
		}
		
	}
	
	save() {
		const httpService = this.httpService;
		const data = this.selectedDeploySys;
		const isCreateDeploySys = this._isCreateDeploySys();
		
		delete data.recid;
		
		if(!this._checkValid(data)) return;

		const q = isCreateDeploySys 
			? httpService.post('/depolysyss', data) 
			: httpService.put('/depolysyss', data);
		
		q.then(res => {
			if (res.isError) {
				this.popupService.detailAlert(this.$scope, res.data.message, res.data.stackTrace);
				return;	
			}
			
			this.getDeploySysList();
			this.openAlert(bxMsg('common.saved'));
			this.offEditMode();
			
			this._selectedDeploySys = this.selectedDeploySys;
		});	
	}
	
	_isCreateDeploySys(){
		return _.isEmpty(this._selectedDeploySys);
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
		this.getDeploySysList(num === 1);
	}
	
	getPageInfo() {
		return {
			pageNumber: this.pageNumber || 1,
			pageSize: this.select.pageSize
		};
	}
	
	resetDetail() {
		this.selectedDeploySys = {};
		this._selectedDeploySys = {};
		this.offEditMode();
		
		const $editAble = $('#none-edit, #none-edit2');
		
		for (var i = 0; i < $editAble.length; i++) {
			$editAble[i].style.backgroundColor = "";
		}
	}
	
	onEditMode(){
		if(_.isEmpty(this.selectedDeploySys)) return;
		this._onEdit();
	}
	
	_onEdit(){
		const $forms = this._isCreateDeploySys()
			? $('#searchWrap').find('input,textarea,select')
			: $('#searchWrap').find('div:not(.asterisk) > input,textarea');
		const $editAble = $('#searchWrap input.required');
			
		$forms.attr('disabled', false);
		$editAble.attr('disabled', false);
		
		this.isEdit = true;
	}
	
	offEditMode(){
		const $forms = $('#searchWrap').find('input,textarea');
		$forms.attr('disabled', true);
		this.isEdit = false;
	}
	
	openAlert(alertBody){
		this.popupService.simpleAlert(this.$scope, alertBody);
	}
	
	_checkValid(data){
		if(_.isEmpty(data.deploySysCd)){
			this.openAlert(this.text.emptyDeploySysCd);
			return false;
		} else if(_.isEmpty(data.deploySysNm)){
			this.openAlert(this.text.emptyDeploySysNm);
			return false;
		} else if(_.isEmpty(data.deploySysGrpCd)){
			this.openAlert(this.text.emptyDeploySysGrpCd);
			return false;
		} else if(_.isEmpty(data.deploySysUrl)){
			this.openAlert(this.text.emptydeploySysUrl);
			return false;
		}

		return true;
	}
	
	refreshGrid(){
		const grid =  w2ui[this.options.name];
		grid.refresh();
	}
}

module(App.name).controller('SCR1201Controller', SCR1201Controller);