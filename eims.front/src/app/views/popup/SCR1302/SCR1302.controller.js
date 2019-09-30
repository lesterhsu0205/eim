import { module } from 'angular';
import App from '../../../app';

class SCR1302Controller {
	
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
		if(!data.height) data.height = 700;
		
		this.initWindow(data.width, data.height);
		this.initText();
		this.initSearch();
		this.initSelect();
		this.initSysGridOption();
		this.getSysList();
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
			sysCd : bxMsg('manageCommSystem.sysCd'),
			sysNm : bxMsg('manageCommSystem.sysNm'),
			sysCdDesc : bxMsg('manageCommSystem.sysCdDesc'),
			sysSearch : bxMsg('manageCommSystem.sysSearch'),
			shortSysCd : bxMsg('manageCommSystem.shortSysCd'),
			shortSysNm : bxMsg('manageCommSystem.shortSysNm'),
			resetSearch : bxMsg('common.resetSearch'),
			search : bxMsg('common.search'),
			select : bxMsg('common.select'),
			cancel : bxMsg('common.cancel'),
			confirmOk : bxMsg('common.confirmOk'),
			confirmCancel : bxMsg('common.confirmCancel'),
		};
	}
	
	initSearch(){
		this.searchParam = {};
	}
	
	initSelect(){
		this.select = this.gridService.getSelect(10);
	}
	
	initSysGridOption(){
		this.gridHeight = this.gridService.getGridHeight(this.select.pageSize);
		this.sysOptions = {
				limit: this.select.pageSize,
				pageSize: this.select.pageSize,
				mutiSelect: false,
				recordsCount: 0,
				recid: 'sysCd',
				columns: [
					{
						caption: 'No', size: '80px',
						render: (data, index) => {
							const pageNumber = this.pageNumber || 1;
							return (pageNumber - 1) * this.sysOptions.limit + index + 1;
						}
					},
					{ field: 'sysCd', caption: this.text.shortSysCd },
					{ field: 'sysNm', caption: this.text.shortSysNm, attr: 'align=left' },
					{ field: 'sysCdDesc', caption: this.text.sysCdDesc}
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
	
	getSysList(goToFirst = false){
		const {pageNumber, pageSize} = this.getPageInfo();
		let url = `/srsyss?pageNumber=${ goToFirst ? 1 : pageNumber}&pageSize=${pageSize}`;
		
		this.httpService.get(url, this.searchParam).then(data => {
			const { srsysbsOutList: records, totalCnt: recordsCount } = data;
		
			this.sysOptions.records = records;
			this.sysOptions.recordsCount = recordsCount;
			
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
		this.getSysList(true);
	}
	
	search(){
		this.getSysList(true);
	}
	
	blur($event){
		$event.target.blur();
	}
	
	pageBtnClick(num){
		this.pageNumber = num;
		this.getSysList(num === 1);
	}
	
	closeModal(isOk){
		const grid = w2ui[this.sysOptions.name];
		
		if(isOk){
			const selection = grid.getSelection();

			if(selection.length === 0){
				this.$uibModalInstance.dismiss();
			} else {
				delete selection[0].recid;
				this.$uibModalInstance.close(grid.get(selection[0]));
			}
		} else {
			this.$uibModalInstance.dismiss();
		}
		
		setTimeout(()=>grid.destroy());
	}

}

module(App.name).controller('SCR1302Controller', SCR1302Controller);

