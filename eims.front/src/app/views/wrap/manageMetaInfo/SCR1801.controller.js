
import { module } from 'angular';
import App from '../../../app';

class SCR1801Controller {
	
	constructor ($scope, $state, $timeout, httpService, utilService,  gridService, 
			popupService, codeService, metaService, userService, codes){
		this.$scope = $scope;
		this.$state = $state;
		this.$timeout = $timeout;
		this.codes = codes;
		this.httpService = httpService;
		this.utilService = utilService;
		this.codeService = codeService;
		this.gridService = gridService;
		this.popupService = popupService;
		this.metaService = metaService;
		this.userService = userService;
		this.user = this.userService.getUser();
		
		this.initText();	
		this.initSelect();
		this.resetSearch(true);
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
				
				if(prevScope.select && Object.keys(prevScope.select).length > 0) {
					// 상세
					this.select = prevScope.select;
				}
				
				if(prevScope.impactPopupVisible) {
					$('#meta-impact-poopup').show();
				}
			}
		} else {
			this.getMetaList();
		}
		
		this.$scope.$on('$destroy', () => {
			this.utilService.setParams(currentStateName, {scope: this.$scope});
			
			let impactPopup = $('#meta-impact-poopup');
			this.impactPopupVisible = impactPopup.is(':visible');
			impactPopup.hide();
		});
	}
	initText() {
		this.text = $.extend({}, bxMsg.getMessages('common'), bxMsg.getMessages('manageMetaInfo'));
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
				multiSelect: false,
				recid: 'metaEngNm',
				columns: [
					{
						caption: 'No', size: '80px',
						render: (data, index) => {
							const pageNumber = this.pageNumber || 1;
							return (pageNumber - 1) * this.options.limit + index + 1;
						}
					},
					{ field: 'metaEngNm', caption: this.text.metaEngNm, size:2, sortable: true, attr:"align=left"},
					{ field: 'metaKorNm', caption: this.text.metaKorNm, size:2, sortable: true, attr:"align=left"},
					{ field: 'dataTypeNm', caption: this.text.dataTypeNm, size:1},
					{ field: 'metaLen', caption: this.text.metaLen, size:1},
					{ field: 'decimalLen', caption: this.text.decimalLen, size:1},
					{ 
						caption: this.text.influence,  size: '80px',
						render: (data)=> {
							return `
								<button type="button" class="bw-btn bxd bxd-chart-bar" title="${this.text.influenceModal}" data-action="reflect"></button>
							`;
						}
					}
				],
				onClick: (e) => {
					// prevent deselect
					let selection = w2ui[e.target].getSelection();
					if(selection.length === 1 && selection[0] === e.recid) {
						e.preventDefault();
					}
					
					
					const eTarget = e.originalEvent.target;

					if (eTarget.localName === 'button') {
						this.popupService.openModal('SCR1803', { metaEngNm: e.recid })
										 .then(()=>{})
										 .catch(()=>{});
					} 
				}
			};
	}
	
	getMetaList(goToFirst = false) {
		const {pageNumber, pageSize} = this.getPageInfo();
		let url = `/metas?pageNumber=${ goToFirst ? 1 : pageNumber}&pageSize=${pageSize}`;
		
		this.httpService.get(url, this.searchParam).then(data => {
			
			if(data.metabsOutList == null && data.totalCnt == 0){
				this.options.records = "";
				this.options.recordsCount = 0;
			}else{
				const { metabsOutList: records, totalCnt: recordsCount } = data;
				
				this.options.records = records;
				this.options.recordsCount = recordsCount;				
			}
			
			if(goToFirst) {
				this.pageNumber = 1;
				this.$scope.$broadcast(`resetPage`, this.pageNumber);
			}
		});
	}
	
	search(){
		this.getMetaList(true);
	}
	
	blur($event){
		$event.target.blur();
	}

	resetSearch(isConst){
		if(isConst){
			this.searchParam = {};			
		}else{
			this.searchParam = {};
			this.getMetaList(true);
		}
	}
	change() {
		this.options.limit = this.select.pageSize;
		this.gridHeight = this.gridService.getGridHeight(this.select.pageSize);
		this.pageBtnClick(1);
	}
	
	pageBtnClick(num) {
		this.pageNumber = num;
		this.getMetaList(num === 1);
	}
	
	getPageInfo() {
		return {
			pageNumber: this.pageNumber || 1,
			pageSize: this.select.pageSize
		};
	}
	
	syncMetaData(){
		this.popupService.showLoadingBar(this.$scope);
		this.metaService.syncMeta()
						.then(() => this.getMetaList(true))
						.finally(() => {
							this.popupService.closeLoadingBar();
							this.openAlert(this.text.syncMeta);
						});
	}
	
	openAlert(alertBody){
		this.popupService.simpleAlert(this.$scope, alertBody);
	}
	
}

module(App.name).controller('SCR1801Controller', SCR1801Controller);