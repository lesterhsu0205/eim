import { module } from 'angular';
import App from '../../../app';

class SCR0902Controller {
	
	constructor ($scope, $state, $uibModalInstance, httpService, utilService, 
		gridService, popupService, codeService, userService, data){
		this.$scope = $scope;
		this.$state = $state;
		this.$uibModalInstance = $uibModalInstance;
		this.httpService = httpService;
		this.utilService = utilService;
		this.gridService = gridService;
		this.popupService = popupService;
		this.codeService = codeService;
		this.userService = userService;
		this.user = this.userService.getUser();
		this.data = data;
		this.codes = data.codes;
		
		this.initWindow('100%', '100%');
		
		this.initWindow(data.width, data.height);
		this.initText();
		this.initSearch();
		this.initSelect();
		this.initIntrfcGridOption();
		this.getInteraceList();
	}
	
	initWindow(width, height){
		this.width = width;
		this.height = height;
		this.top = 100;
		this.left = 50;
		this.right = 50;
		this.zIndex = this.popupService.getModalZIndex();
	}
	
	initText(){
		this.text = $.extend({}, bxMsg.getMessages('common'), bxMsg.getMessages('manageMciInterface'), bxMsg.getMessages('manageMetaInfo'));	
	}
	
	initSearch(){
		this.searchParam = {};
	}
	
	initSelect(){
		this.select = this.gridService.getSelect(10);
	}
	
	initIntrfcGridOption(){
		this.gridHeight = this.gridService.getGridHeight(this.select.pageSize);
		this.intrfcOptions = {
			limit: this.select.pageSize,
			pageSize: this.select.pageSize,
			multiSelect: false,
			recordsCount: 0,
			recid: 'intrfcId',
			columns: [
				{
					caption: 'No', size: '60px',
					render: (data, index) => {
						const pageNumber = this.pageNumber || 1;
						return (pageNumber - 1) * this.intrfcOptions.limit + index + 1;
					}
				},
				{ field: 'intrfcId', caption: this.text.intrfcId, size: '3%' },
				{ field: 'intrfcNm', caption: this.text.intrfcNm, size: '3%', attr:'align = left' },
				{ 
					field: 'lvCds', caption: this.text.lvCds, size: '1.5%', sortable: true,
					render: (data) => {
						return `${data.lv1Cd}`
					}
				},
				{ 
					field: 'trxDscd', caption: this.text.trxTypeDscd, size: '1.5%',
					render: (data) => {		
						return this.codeService.getCodeValNm('TRAN_DSCD', data.trxDscd);
					}	
				},
				{ 
					field: 'intrfcWayCd', caption: this.text.intrfcWayCd, size: '2%',
					render: (data) => {		
						return this.codeService.getCodeValNm('INTRFC_WAY_CD', data.intrfcWayCd);
					}	
				},
				{ field: 'regManId', caption: this.text.regManId, size: '1.5%'},
				{ field: 'workStatusCd', caption: this.text.workStatusCd, size: '1%',
					render: (data,index,colIndex) => {		
						return this.codeService.getCodeValNm('WORK_STATUS_CD', data.workStatusCd);
					}
				},
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
	
	getInteraceList(goToFirst = false){
		const {pageNumber, pageSize} = this.getPageInfo();
		let url = `/intrfccoms?pageNumber=${ goToFirst ? 1 : pageNumber}&pageSize=${pageSize}`;
		if(this.data.intrfcTypeCd) url += `&intrfcTypeCd=${this.data.intrfcTypeCd}`;
		
		this.httpService.get(url, this.searchParam).then(data => {
			const { intrfccombsOutList: records, totalCnt: recordsCount } = data; 
			this.intrfcOptions.records = records;
			this.intrfcOptions.recordsCount = recordsCount;
			
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
		this.getInteraceList(true);
	}
	
	search(){
		this.getInteraceList(true);
	}
	
	blur($event){
		$event.target.blur();
	}
	
	searchApplicationCodes(){
		this.popupService.openModal('SCR1402', { limitLvCd: 0 })
						 .then((code) => {
							this.searchParam.lv1Cd = code.appCd;
							this.searchParam.lv1CdNm = code.appCdNm;
						 })
						 .catch(()=>{});
	}
	
	openRegManPopup(){
		this.popupService.openModal('SCR0102')
						 .then(user => this.searchParam.regManId = user.userId)
						 .catch(()=>{});
	}
	
	pageBtnClick(num){
		this.pageNumber = num;
		this.getInteraceList(num === 1);
	}
	
	closeModal(isOk){
		const grid = w2ui[this.intrfcOptions.name];
		
		if(isOk){
			const selection = grid.getSelection();

			if(selection.length === 0){
				this.$uibModalInstance.dismiss();
			} else {
				if(this.data.getDetail){
					this.getInterface(selection[0]);
				} else {
					this.$uibModalInstance.close(grid.get(selection[0]));
				}
			}
		} else {
			this.$uibModalInstance.dismiss();
		}
		
		setTimeout(()=>grid.destroy());
	}
	
	getInterface(intrfcId) {
		this.httpService.get(`/intrfccoms/${intrfcId}`).then(res => {
			this.$uibModalInstance.close(res);
		});
	}
}

module(App.name).controller('SCR0902Controller', SCR0902Controller);

