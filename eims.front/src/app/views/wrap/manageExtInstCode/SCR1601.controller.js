import { module } from 'angular';
import App from '../../../app';

class SCR1601Controller {
	
	constructor ($scope, $state, $timeout, httpService, utilService, gridService, popupService, codeService, userService, codes){
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
				
				if(prevScope.selectedExtrnlinst && Object.keys(prevScope.selectedExtrnlinst).length > 0) {
					// 상세
					this.selectedExtrnlinst = prevScope.selectedExtrnlinst;
					this._selectedExtrnlinst = prevScope._selectedExtrnlinst;
				}
				
				// 수정모드
					if(prevScope.isEdit){
					this.onEditMode();
				}else{
					this.offEditMode();
				}
			}
		} else {
			this.getExtrnlinstList();
		}
		
		this.$scope.$on('$destroy', () => {
			this.utilService.setParams(currentStateName, {scope: this.$scope});
		});
	}
	
	initText() {
		this.text = $.extend({}, bxMsg.getMessages('common'), bxMsg.getMessages('manageExtInstCode'));
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
		{ field: 'instCd', caption: this.text.instCd, size: 2, sortable: true},
		{ field: 'instCdNm', caption: this.text.instCdNm, size: 2, sortable: true, attr: 'align=left'},
		{ 
			field: 'instDstnctnVal', caption: this.text.instDstnctnVal, size: 2,
			render: (data) => this.codeService.getCodeValNm('EXT_INST_TYPE', data.instDstnctnVal)
		},
		{ field: 'instCdDesc', caption: this.text.instCdDesc, size:3, attr:"align=left"}];

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
				pageSize: this.select.pageSize,
				recordsCount: 0,
				recid: 'instCd',
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
									()=>this.deleteExtrnlinst(e.recid, editData.instDstnctnVal));
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

						if(!_.isEmpty(this._selectedExtrnlinst)){
							return;
						}
					}
					
					this.getExtrnlinst(editData.instCd, editData.instDstnctnVal);
				}
			};
	}
	

	getExtrnlinstList(goToFirst = false) {
		const {pageNumber, pageSize} = this.getPageInfo();
		let url = `/extrnlinsts?pageNumber=${ goToFirst ? 1 : pageNumber}&pageSize=${pageSize}`;
		
		this.httpService.get(url, this.searchParam).then(data => {
			
			if(data.extrnlinstcdOutList == null && data.totalCnt == 0){
				this.options.records = "";
				this.options.recordsCount = 0;
			}else{
				const { extrnlinstcdOutList: records, totalCnt: recordsCount } = data;
				
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
	
	getExtrnlinst(instCd = '', instDstnctnVal){
		const utilService = this.utilService;
		
		let url = instDstnctnVal ? `/extrnlinsts/${instCd}?instDstnctnVal=${instDstnctnVal}` :
			`/extrnlinsts/${instCd}`;
		
		this.httpService.get(url).then(data => {
			this.selectedExtrnlinst = utilService.clone(data);
			this._selectedExtrnlinst = utilService.clone(data);
		});
		
		const $editAble = $('#none-edit');
		
		for (var i = 0; i < $editAble.length; i++) {
			$editAble[i].style.backgroundColor = "#e2e2e2";
		}
	}
	
	deleteExtrnlinst(instCd = '', instDstnctnVal = '') {
		this.httpService.delete(`/extrnlinsts/${instCd}?instDstnctnVal=${instDstnctnVal}`)
						.then(data => {
							if (data.isError) return;
							
							this.resetDetail();
							this.getExtrnlinstList();
						});
	}
	
	search(){
		this.getExtrnlinstList(true);
	}
	
	blur($event){
		$event.target.blur();
	}
	
	resetSearch(isConst){
		if(isConst){
			this.searchParam = {};			
		}else{
			this.searchParam = {};
			this.getExtrnlinstList(true);
		}
	}
	
	save() {
		const httpService = this.httpService;
		const data = this.selectedExtrnlinst;
		const isCreateDeploySys = this._isCreateDeploySys();
		
		delete data.recid;
		
		if(!this._checkValid(data)) return;

		const q = isCreateDeploySys 
			? httpService.post('/extrnlinsts', data) 
			: httpService.put('/extrnlinsts', data);
		
		q.then(res => {
			if (res.isError) {
				this.popupService.detailAlert(this.$scope, res.data.message, res.data.stackTrace);
				return;	
			}
			
			this.getExtrnlinstList();
			this.offEditMode();
			this.openAlert(bxMsg('common.saved'));
		});	
	}
	
	_isCreateDeploySys(){
		return _.isEmpty(this._selectedExtrnlinst);
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
		this.getExtrnlinstList(num === 1);
	}
	
	getPageInfo() {
		return {
			pageNumber: this.pageNumber || 1,
			pageSize: this.select.pageSize
		};
	}
	
	resetDetail() {
		this.selectedExtrnlinst = {};
		this._selectedExtrnlinst = {};
		this.offEditMode();
		
		const $editAble = $('#none-edit');
		
		for (var i = 0; i < $editAble.length; i++) {
			$editAble[i].style.backgroundColor = "";
		}
	}
	
	onEditMode(){
		if(_.isEmpty(this.selectedExtrnlinst)) return;
		this._onEdit();
	}
	
	_onEdit(){
		const $forms = this._isCreateDeploySys()
			? $('#searchWrap').find('input,textarea,select')
			: $('#searchWrap').find('div:not(.asterisk) > input,textarea,select');
		const $editAble = $('#searchWrap input.required');
			
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
		if(_.isEmpty(data.instCd)){
			this.openAlert(this.text.emptyInstCd);
			return false;
		} else if(_.isEmpty(data.instCdNm)){
			this.openAlert(this.text.emptyInstCdNm);
			return false;
		} else if(_.isEmpty(data.instDstnctnVal)){
			this.openAlert(this.text.emptyInstDstnctnVal);
			return false;
		}
		return true;
	}
	
	refreshGrid(){
		const grid =  w2ui[this.options.name];
		grid.refresh();
	}
}

module(App.name).controller('SCR1601Controller', SCR1601Controller);