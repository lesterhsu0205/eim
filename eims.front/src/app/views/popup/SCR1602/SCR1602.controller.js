import { module } from 'angular';
import App from '../../../app';

class SCR1602Controller {
	
	constructor ($scope, $state, $uibModalInstance, httpService, utilService, gridService, popupService, data){
		this.$scope = $scope;
		this.$state = $state;
		this.$uibModalInstance = $uibModalInstance;
		this.httpService = httpService;
		this.utilService = utilService;
		this.gridService = gridService;
		this.popupService = popupService;
		this.data = data;
		
		if(!data.width) data.width = 720;
		if(!data.height) data.height = 640;
		
		this.initWindow(data.width, data.height);
		this.initText();
		this.initSearch();
		this.initSelect();
		this.initInstCdGridOption();
		this.getExtrnlinstList();
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
		this.text = {
			instCdSearch : bxMsg('manageExtInstCode.instCdSearch'),
			instCd : bxMsg('manageExtInstCode.instCd'),
			instCdNm : bxMsg('manageExtInstCode.instCdNm'),
			instDstnctnVal : bxMsg('manageExtInstCode.instDstnctnVal'),
			instCdDesc : bxMsg('manageExtInstCode.instCdDesc'),
			search : bxMsg('common.search'),
			resetSearch : bxMsg('common.resetSearch'),
			select : bxMsg('common.select'),
			cancel : bxMsg('common.cancel')
		};
	}
	
	initSearch(){
		this.searchParam = {};
	}
	
	initSelect(){
		this.select = this.gridService.getSelect(10);
	}
	
	initInstCdGridOption(){
		this.gridHeight = this.gridService.getGridHeight(this.select.pageSize);
		this.instCdOptions = {
			limit: this.select.pageSize,
			pageSize: this.select.pageSize,
			multiSelect: false,
			recordsCount: 0,
			recid: 'instCd',
			columns: [
				{
					caption: 'No', size: '80px',
					render: (data, index) => {
						const pageNumber = this.pageNumber || 1;
						return (pageNumber - 1) * this.instCdOptions.limit + index + 1;
					}
				},
				{ field: 'instCd', caption: this.text.instCd, size: '160px'},
				{ field: 'instCdNm', caption: this.text.instCdNm, size: '160px'},
				{ field: 'instDstnctnVal', caption: this.text.instDstnctnVal, size: '160px'},
				{ field: 'instCdDesc', caption: this.text.instCdDesc}
			],
			onClick: (e) => {
				// prevent deselect
				let selection = w2ui[e.target].getSelection();
				if(selection.length === 1 && selection[0] === e.recid) {
					e.preventDefault();
				}
			},
			onDblClick: (e) => this.closeModal(true)
		};
	}
	
	getExtrnlinstList(goToFirst = false) {
		const {pageNumber, pageSize} = this.getPageInfo();
		let url = `/extrnlinsts?pageNumber=${ goToFirst ? 1 : pageNumber}&pageSize=${pageSize}`;
		
		this.httpService.get(url, this.searchParam).then(data => {
			const { extrnlinstcdOutList: records, totalCnt: recordsCount } = data;
		
			this.instCdOptions.records = records;
			this.instCdOptions.recordsCount = recordsCount;
			
			if(goToFirst) {
				this.pageNumber = 1;
				this.$scope.$broadcast(`resetPage`, this.pageNumber);
			}
		});
	}
	
	getPageInfo() {
		return {
			pageNumber: this.pageNumber || 1,
			pageSize: this.select.pageSize
		};
	}
	
	resetSearch(){
		this.searchParam = {};
		this.getExtrnlinstList(true);
	}
	
	search(){
		this.getExtrnlinstList(true);
	}
	
	blur($event){
		$event.target.blur();
	}
	
	pageBtnClick(num){
		this.pageNumber = num;
		this.getExtrnlinstList(num === 1);
	}
	
	closeModal(isOk){
		const grid = w2ui[this.instCdOptions.name];
		
		if(isOk){
			const selection = grid.getSelection();
			
			if(selection.length === 0){
				this.$uibModalInstance.dismiss();
			} else {
				this.$uibModalInstance.close(grid.get(selection[0]));
			}
		} else {
			this.$uibModalInstance.dismiss();
		}
		
		setTimeout(()=>grid.destroy());
	}
	
}

module(App.name).controller('SCR1602Controller', SCR1602Controller);