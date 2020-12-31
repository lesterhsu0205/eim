import { module } from 'angular';
import App from '../../../app';

class SCR0502Controller {
	
	constructor ($scope, $uibModalInstance, httpService, gridService, codeService, 
		popupService, userService, data){
		this.$scope = $scope;
		this.$uibModalInstance = $uibModalInstance;
		this.httpService = httpService;
		this.gridService = gridService;
		this.popupService = popupService;
		this.codeService = codeService;
		this.userService = userService;
		this.user = this.userService.getUser();
		this.data = data;

		this.initWindow('100%', '100%');
		this.initText();
		this.initSearch();
		this.initSelect();
		this.initCode();
		this.initMsgGridOption();
		this.getMsglayoutList();
	}
	
	initWindow(width, height){
		const { top, left } = this.popupService.calculatePosition(width, height);
		
		this.width = width;
		this.height = height;
		this.top = 100;
		this.left = 50;
		this.right = 50;
		this.zIndex = this.popupService.getModalZIndex();
	}
	
	initText(){
		this.text = $.extend({}, bxMsg.getMessages('common'), bxMsg.getMessages('manageMsg'));		
	}
	
	initSearch(){
		this.searchParam = {};

		if(this.data.trxDscdFilter) {
			if(this.data.trxDscd === 'ONLINE'){
				this.searchParam.trxDscd = 'ONLINE';
			} else if(this.data.trxDscd === 'BATCH'){
				this.searchParam.trxDscd = 'BATCH';
			}	
		}
	}
	
	initSelect(){
		this.select = this.gridService.getSelect(10);
		this.pageNumber = 1;
		this.selected = [];
	}
	
	initCode(){
		this.msgTypeList = this.codeService.getCodesByCdIdFromMem('MSG_TYPE');
		this.chlDscdList = this.codeService.getCodesByCdIdFromMem('CHL_DSCD');
		this.tranDscdList = this.codeService.getCodesByCdIdFromMem('TRAN_DSCD');
		this.workStatusList = this.codeService.getCodesByCdIdFromMem('WORK_STATUS_CD').filter(v => v.cdVal !== 'DEPLOY_COMP');
	}

	initMsgGridOption(){
		this.gridHeight = this.gridService.getGridHeight(this.select.pageSize);
		this.msgOptions = {
			limit: this.select.pageSize,
			pageSize: this.select.pageSize,
			show: {columnHeaders:true, selectColumn : this.data.multiSelect === false ? false : true},
			multiSelect: this.data.multiSelect === false ? false : true,
			recordsCount: 0,
			recid: 'msgLayoutId',
			columns: [
				{
					field: 'no', caption: 'No', size: '40px',
					render: (data, index) => this.gridService.getNoField(this.msgOptions.limit, index, this.pageNumber)
				},
				{ field: 'msgLayoutId', caption: this.text.msgLayoutId, size: '2%', sortable: true},
				{ field: 'msgNm', caption: this.text.msgName, attr:"align=left", size: '2%',sortable: true},
				{ 
					field: 'chlDscd', caption: this.text.chlDscd, size: this.user.locale === 'en'? '110px' : '1%',sortable: true,
					render: (data,index,colIndex) => {
						return this.codeService.getCodeValNm('CHL_DSCD', data.chlDscd);
					}
				},
				{ field: 'trxDscd', caption: this.text.trxDscd, size: this.user.locale === 'en'? '120px' : '1%', sortable: true,
					render: (data,index,colIndex) => {
						const trxDscd = w2ui[this.msgOptions.name].getCellValue(index, colIndex);
						return this.codeService.getCodeValNm('TRAN_DSCD', trxDscd);
					}
				},
				{ field: 'msgDataVal', caption: this.text.msgDataVal + '/' + this.text.extrnlDtoNm, attr:"align=left", size: this.user.locale === 'en'? '200px' : '2%', sortable: true},
				{ 
					field: 'msgDscd', caption: this.text.msgType, size: '1%', sortable: true,
					render: (data,index,colIndex) => {							
						return this.codeService.getCodeValNm('MSG_TYPE', data.msgDscd);
					}
				},
				{ field: 'msgVersion', caption: this.text.msgVersion, size: '1%', sortable: true},
				{ 
					field: 'workStatusCd', caption: this.text.workStatusCd, size: this.user.locale === 'en'? '110px' : '1%', sortable: true,
					render: (data,index,colIndex) => {
						return this.codeService.getCodeValNm('WORK_STATUS_CD', data.workStatusCd);
					}
				},
				{ field: 'regManId', caption: this.text.msgRegister, size: this.user.locale === 'en'? '70px' : '1%', sortable: true},
				{ field: 'regDttm', caption: this.text.msgRegisterDt, size:this.user.locale === 'en'? '110px' :  '1%', sortable: true,
					render: (data) =>{
						const regDttm = data.regDttm 
						const yy = regDttm.substring(0,4);
						const mm = regDttm.substring(4,6);
						const dd = regDttm.substring(6,8);
						return yy+"/"+mm+"/"+dd;
					}
				},
			],
			onClick: (e) =>{
				// prevent deselect
				let selection = w2ui[e.target].getSelection();
				if(selection.length === 1 && selection[0] === e.recid) {
					e.preventDefault();
				}
			},
			onSelect: (e) => {
				if(e.all){
					if(this.changedPage){
						this.changedPage = false;
					}
					this.msgOptions.records.map((record) => {
						let idx = this.selected.indexOf(record.recid);
						if(idx === -1) {
							this.selected.push(record.recid);
						}
					});
				}else{
					let idx = this.selected.indexOf(e.recid);
					if(idx === -1) {
						this.selected.push(e.recid);
					}
				}
			},
			onUnselect: (e) => {
				if(e.all){
					if(this.changedPage){
						this.changedPage = false;
						return;
					}
					this.msgOptions.records.map((record) => {
						let idx = this.selected.indexOf(record.recid);
						if(idx !== -1) {
							this.selected.splice(idx, 1);
						}
					});
				}else{
					let idx = this.selected.indexOf(e.recid);
					if(idx !== -1) {
						this.selected.splice(idx, 1);
					}
				}
			},
			onDblClick: (e) => this.closeModal(true)
		};
	}
	
	getMsglayoutList() {
		let url = `/msglayouts?pageNumber=1&pageSize=99999`;
		
		this.httpService.get(url, this.searchParam).then(data => {
			const { msglayoutbsOutList: records, totalCnt: recordsCount } = data;
			
			this.msgOptionRecords = records;
			this.msgOptions.recordsCount = recordsCount;
			this.setPageConts();
			
			this.pageNumber = 1;
			this.$scope.$broadcast(`resetPage`, this.pageNumber);
			this.pageBtnClick(1);
		});
	}
	
	setPageConts() {
		let startIdx = (this.pageNumber - 1) * 10;
		let endIdx = this.pageNumber * 10;

		this.msgOptions.records = this.msgOptionRecords.slice(startIdx, endIdx);
		this.changedPage = true;
		
		setTimeout(()=>{
			let grid = w2ui[this.msgOptions.name];
			grid.selectNone();
			
			this.selected.map((msgLayoutId) => {
				grid.select(msgLayoutId);
			});
		}, 300);
	}
	
	resetSearch(){
		this.initSearch();
		this.getMsglayoutList();
	}
	
	search(){
		this.getMsglayoutList();
	}
	
	blur($event){
		$event.target.blur();
	}
	
	pageBtnClick(num){
		this.pageNumber = num;
		this.setPageConts();
	}
	
	closeModal(isOk){
		const grid = w2ui[this.msgOptions.name];
		
		if(isOk){
			if(this.selected.length === 0){
				this.$uibModalInstance.dismiss();
			} else {
				if(this.data.getDetail){
					this.getMsglayout(this.selected);
				} else {
					let selectionsDataList = [];
					
					this.selected.map((selection) => {
						selectionsDataList.push(grid.get(selection));
					});
					this.$uibModalInstance.close(selectionsDataList);
				}
				
			}
		} else {
			this.$uibModalInstance.dismiss();
		}
		
		setTimeout(()=>grid.destroy());
	}
	
	getMsglayout(selectionList) {
		this.httpService.post(`/msglayoutslist`, selectionList).then(res => {
			this.$uibModalInstance.close(res.msglayoutbsDtoList);
		});
	}
	
	openApplicationCodeModalForSearchParam(){
		this.popupService.openModal('SCR1402')
						 .then((code) => {
							this.searchParam.lv1Cd = code.appCd;
						 })
						 .catch(()=>{});
	}
	
	openRegManPopup(){
		this.popupService.openModal('SCR0102')
						 .then(user => this.searchParam.regManId = user.userId)
						 .catch(()=>{});
	}
	
}

module(App.name).controller('SCR0502Controller', SCR0502Controller);