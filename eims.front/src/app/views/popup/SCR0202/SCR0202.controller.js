import { module } from 'angular';
import App from '../../../app';

class SCR0202Controller {
	
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
		if(!data.height) data.height = 640;
		
		this.initWindow(data.width, data.height);
		this.initText();
		this.initSearch();
		this.initSelect();
		this.initRoleGridOption();
		this.getRoles();
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
		this.text = this.data.text;
	}
	
	initSearch(){
		this.searchParam = {};
	}
	
	initSelect(){
		this.select = this.gridService.getSelect(10);
	}
	
	initRoleGridOption(){
		this.gridHeight = this.gridService.getGridHeight(this.select.pageSize);
		this.roleOptions = {
			limit: this.select.pageSize,
			pageSize: this.select.pageSize,
			multiSelect: false,
			recordsCount: 0,
			recid: 'roleId',
			columns: [
				{
					caption: 'No', size: '80px',
					render: (data, index) => {
						const pageNumber = this.pageNumber || 1;
						return (pageNumber - 1) * this.roleOptions.limit + index + 1;
					}
				},
				{ field: 'roleId', caption: this.text.roleId, size: '100px', sortable: true},
				{ field: 'roleNm', caption: this.text.roleNm, size: '160px', sortable: true},
				{ field: 'roleDesc', caption: this.text.roleDesc}
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
	
	getRoles(goToFirst = false) {
		const { pageNumber, pageSize } = this.getPageInfo();
		
		let url = `/roles/?pageNumber=${ goToFirst ? 1 : pageNumber}&pageSize=${pageSize}`;
		
		this.httpService.get(url, this.searchParam).then(data => {
			const { roleDtoList: records, totalCnt: recordsCount } = data;
		
			this.roleOptions.records = records;
			this.roleOptions.recordsCount = recordsCount;
			
			if(goToFirst) {
				this.rolePageNumber = 1;
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
		this.getRoles(true);
	}
	
	search(){
		this.getRoles(true);
	}
	
	blur($event){
		$event.target.blur();
	}
	
	pageBtnClick(num){
		this.pageNumber = num;
		this.getRoles(num === 1);
	}
	
	closeModal(isOk){
		const grid = w2ui[this.roleOptions.name];
		
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

module(App.name).controller('SCR0202Controller', SCR0202Controller);

