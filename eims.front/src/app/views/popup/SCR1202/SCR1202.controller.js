import { module } from 'angular';
import App from '../../../app';

class SCR1202Controller {
	
	constructor ($scope, $state, $uibModalInstance, httpService, utilService, gridService, popupService, data){
		this.$scope = $scope;
		this.$state = $state;
		this.$uibModalInstance = $uibModalInstance;
		this.httpService = httpService;
		this.utilService = utilService;
		this.gridService = gridService;
		this.popupService = popupService;
		this.data = data;
		
		if(!data.width) data.width = 960;
		if(!data.height) data.height = 640;
		
		this.initWindow(data.width, data.height);
		this.initText();
		this.initSearch();
		this.initSelect();
		this.initDeploySysGridOption();
		this.getDeploySysList();
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
			modalHeader : bxMsg('manageDeploySystem.modalHeader'),
			deploySysCd : bxMsg('manageDeploySystem.deploySysCd'),
			deploySysNm : bxMsg('manageDeploySystem.deploySysNm'),
			deploySysGrpCd : bxMsg('manageDeploySystem.deploySysGrpCd'),
			deploySysDesc : bxMsg('manageDeploySystem.deploySysDesc'),
			deploySysUrl: bxMsg('manageDeploySystem.deploySysUrl'),
			resetSearch : bxMsg('common.resetSearch'),
			search : bxMsg('common.search'),
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
	
	initDeploySysGridOption(){
		this.gridHeight = this.gridService.getGridHeight(this.select.pageSize);
		this.deploySysOptions = {
			limit: this.select.pageSize,
			pageSize: this.select.pageSize,
			multiSelect: false,
			recordsCount: 0,
			recid: 'deploySysCd',
			columns: [
				{
					caption: 'No', size: '40px',
					render: (data, index) => {
						const pageNumber = this.pageNumber || 1;
						return (pageNumber - 1) * this.deploySysOptions.limit + index + 1;
					}
				},
				{ field: 'deploySysCd', caption: this.text.deploySysCd, size: '1%'},
				{ field: 'deploySysNm', caption: this.text.deploySysNm, size: '1%'},
				{ field: 'deploySysUrl', caption: this.text.deploySysUrl, size: '2%'},
				{ field: 'deploySysDesc', caption: this.text.deploySysDesc, size: '2%'}
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
	
	getDeploySysList(goToFirst = false) {
		const { pageNumber, pageSize } = this.getPageInfo();
		let url = `/depolysyss?pageNumber=${ goToFirst ? 1 : pageNumber}&pageSize=${pageSize}`;
		
		this.httpService.get(url, this.searchParam).then(data => {
			const { depolysysbsOutList: records, totalCnt: recordsCount } = data;
		
			this.deploySysOptions.records = records;
			this.deploySysOptions.recordsCount = recordsCount;
			
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
		this.getDeploySysList(true);
	}
	
	search(){
		this.getDeploySysList(true);
	}
	
	blur($event){
		$event.target.blur();
	}
	
	pageBtnClick(num){
		this.pageNumber = num;
		this.getDeploySysList(num === 1);
	}
	
	closeModal(isOk){
		const grid = w2ui[this.deploySysOptions.name];
		
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

module(App.name).controller('SCR1202Controller', SCR1202Controller);

