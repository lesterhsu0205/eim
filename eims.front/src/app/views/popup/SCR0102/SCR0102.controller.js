import { module } from 'angular';
import App from '../../../app';

class SCR0102Controller {
	
	constructor ($scope, $uibModalInstance, httpService, gridService, popupService){
		this.$scope = $scope;
		this.$uibModalInstance = $uibModalInstance;
		this.httpService = httpService;
		this.gridService = gridService;
		this.popupService = popupService;
		
		this.initWindow(640, 640);
		this.initText();
		this.initSearch();
		this.initSelect();
		this.initUserGridOption();
		this.getUserList();
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
			userSearch : bxMsg('manageUser.userSearch'),
			userId : bxMsg('manageUser.userId'),
			userNm : bxMsg('manageUser.userNm'),
			email : bxMsg('manageUser.email'),
			dutyNm : bxMsg('manageUser.dutyNm'),
			deptNm : bxMsg('manageUser.deptNm'),
			roleId : bxMsg('manageUser.roleId'),
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
	
	initUserGridOption(){
		this.gridHeight = this.gridService.getGridHeight(this.select.pageSize);
		this.userOptions = {
			limit: this.select.pageSize,
			pageSize: this.select.pageSize,
			multiSelect: false,
			recordsCount: 0,
			recid: 'userId',
			columns: [
				{
					caption: 'No', size: '80px',
					render: (data, index) => {
						const pageNumber = this.pageNumber || 1;
						return (pageNumber - 1) * this.userOptions.limit + index + 1;
					}
				},
				{ field: 'userId', caption: this.text.userId, sortable: true},
				{ field: 'userNm', caption: this.text.userNm, sortable: true},
				{ field: 'deptNm', caption: this.text.deptNm, sortable: true},
				{ field: 'roleId', caption: this.text.roleId, sortable: true}
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
	
	getUserList(goToFirst = false) {
		const {pageNumber, pageSize} = this.getPageInfo();
		var url = `/users?pageNumber=${ goToFirst ? 1 : pageNumber}&pageSize=${pageSize}`;
		
		this.httpService.get(url, this.searchParam).then(data => {
			const { userOutList: records, totalCnt: recordsCount } = data;
		
			this.userOptions.records = records;
			this.userOptions.recordsCount = recordsCount;
			
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
		this.getUserList(true);
	}
	
	search(){
		this.getUserList(true);
	}
	
	blur($event){
		$event.target.blur();
	}
	
	pageBtnClick(num){
		this.pageNumber = num;
		this.getUserList(num === 1);
	}
	
	closeModal(isOk){
		const grid = w2ui[this.userOptions.name];
		
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

module(App.name).controller('SCR0102Controller', SCR0102Controller);

