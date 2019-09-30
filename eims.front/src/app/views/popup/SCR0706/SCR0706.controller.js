import { module } from 'angular';
import App from '../../../app';

class SCR0706Controller {
	
	constructor ($scope, $uibModalInstance, $timeout, httpService, utilService, gridService, popupService, userService, data){
		this.$scope = $scope;
		this.$uibModalInstance = $uibModalInstance;
		this.$timeout = $timeout;
		this.httpService = httpService;
		this.gridService = gridService;
		this.utilService = utilService;
		this.popupService = popupService;
		this.userService = userService;
		this.user = this.userService.getUser();
		this.data = data;
		
		this.initWindow('100%', '100%');
		this.initText();
		this.initSelect();
		this.initGridOption();
		this.getDeployList();
		this.getMsglayout();
		
		$timeout(() => {
			$('#oldVersionOptions').on('scroll', () => {
				$('#currentVersionOptions').find('.w2ui-grid-records').scrollTop($('#oldVersionOptions').find('.w2ui-grid-records').scrollTop());
			});
			$('#currentVersionOptions').on('scroll', () => {
				$('#oldVersionOptions').find('.w2ui-grid-records').scrollTop($('#currentVersionOptions').find('.w2ui-grid-records').scrollTop());
			});
		});
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
		this.text = $.extend({}, bxMsg.getMessages('common'), bxMsg.getMessages('manageMsg'), bxMsg.getMessages('manageDeploySystem'));	
	}
	
	initSelect(){
		this.select = this.gridService.getSelect(5);
	}
	
	initGridOption(){
		this.gridHeight = this.gridService.getGridHeight(this.select.pageSize);
		this.deployOptions = {
			limit: this.select.pageSize,
			pageSize: this.select.pageSize,
			recordsCount: 0,
			recid: 'deployVersion',
			columns: [
				{ field: 'deployVersion', caption: this.text.deployVersion, size:  this.user.locale === 'en'? '120px' : '80px'},
				{ 
					field: 'deployDttm', caption: this.text.deployDttm,
					render: (data)=> {
						return this.utilService.setRegDttm(data.deployDttm);
					}
				},
				{ field: 'deploySysCd', caption: this.text.deploySys}
			],
			onClick: (e) => {
				// prevent deselect
				let selection = w2ui[e.target].getSelection();
				if(selection.length === 1 && selection[0] === e.recid) {
					e.preventDefault();
				}
				
				this.getLayoutDiff(e.recid);
				
			}
		};
		
		this.oldVersionOptions = {
			limit: 999999,
			pageSize: 999999,
			columns: this.getVersionColumns(),
			recordsCount: 0,
			recid: 'fldUnqId',
		};
		
		this.currentVersionOptions = {
			limit: 999999,
			pageSize: 999999,
			columns: this.getVersionColumns(),
			recordsCount: 0,
			recid: 'fldUnqId',
		};
	}
	
	getVersionColumns() {
		return  [
			{ 
				field: 'fldEngNm', caption: this.text.fldEngNm, size:'3%', attr:'align=left',
				render:(data)=>{
					let spaceTemp = `&nbsp;&nbsp;&nbsp;&nbsp;`;
					let space = '';
					let fldEngNm = data.fldEngNm ? data.fldEngNm : '';
					
					if(data.fldLvNo == 0){
						return fldEngNm;
					}else{
						for (var i = 0; i < data.fldLvNo; i++) {
							space = space.concat(spaceTemp);
						}
						return space + fldEngNm;				
					}
				},
			},
			{ field: 'fldKorNm', caption: this.text.fldKorNm, size:'2%', attr:'align=left' },
			{ field: 'dataTypeNm', caption: this.text.dataType, size:'2%' },
			{ field: 'fldLvNo', caption: this.text.fldLvNo, size:'1%' },
			{ field: 'arraySizeRefVal', caption: this.text.arraySizeRefVal, size: this.user.locale === 'en'? '1.7%' : '1.5%'},
			{ field: 'msgLen', caption: this.text.msgLen, size:'1%' },
		];
	}
	
	getDeployList(goToFirst = false) {
		const {pageNumber, pageSize} = this.getPageInfo();
		var url = `/intrfccoms/deployhistorys?
				deployResultCd=SUCCESS
				&pageNumber=${ goToFirst ? 1 : pageNumber}
				&pageSize=${pageSize}
				&intrfcId=${this.data.intrfcId}`;
		
		this.httpService.get(url).then(data => {
			const { intrfcdeployhisthsOutList: records, totalCnt: recordsCount } = data;
		
			this.deployOptions.records = records;
			this.deployOptions.recordsCount = recordsCount;
			
			if(goToFirst) {
				this.pageNumber = 1;
				this.$scope.$broadcast(`resetPage`, this.pageNumber);
			}
		});
	}
	
	getMsglayout() {
		const msgLayoutId = encodeURIComponent(this.data.msgLayoutId);
		this.httpService.get(`/msglayouts/${msgLayoutId}`).then(res => {
			const { msglayoutdtDto: records, msgRvsNo: msgRvsNo } = res;
			
			this.currentVersionRecords = records;
			this.currentVersionOptions.records = records;
			this.currentVersionOptions.msgRvsNo = msgRvsNo;
		});
	}
	
	getLayoutDiff(deployVersion){
		const data = this.data;
		const url = `/intrfccoms/layoutdiff?
			intrfcId=${data.intrfcId}
			&msgLayoutId=${data.msgLayoutId}
			&srTypeCd=${data.srTypeCd}
			&rqstRspsTypeCd=${data.rqstRspsTypeCd}
			&layoutSeq=${data.layoutSeq}
			&deployVersion=${deployVersion}`;
		
		this.httpService.get(url).then(data => {
			if (data.isError) {
				this.oldVersionOptions.records = [];
				this.popupService.simpleAlert(this.$scope, data.data.message);
				return;	
			}
			
			this.oldVersionOptions.records = data.msglayoutdtDto;
			this.oldVersionOptions.msgRvsNo = data.msgRvsNo;
			this.currentVersionOptions.records = this.utilService.clone(this.currentVersionRecords);
			
			setTimeout(()=>this.mapping(), 300);
		});
	}
	
	getPageInfo() {
		return {
			pageNumber: this.pageNumber || 1,
			pageSize: this.select.pageSize
		};
	}
	
	mapping(){
		const trgGrid = w2ui[this.currentVersionOptions.name];
		const srcGrid = w2ui[this.oldVersionOptions.name];
		
		if(this.currentVersionOptions.records.length === 0) return;
		
		let i = 0;
		
		for(i = 0; i < this.currentVersionOptions.records.length; i++) {
			let record = this.currentVersionOptions.records[i];
			let fldEngNm = record.fldEngNm;
			
			if(fldEngNm) {
				let trgPos = this.currentVersionOptions.records.indexOf(record);
				let srcRecord = this.oldVersionOptions.records.filter(rec => rec.fldEngNm === fldEngNm)[0];
				
				if(!_.isEmpty(srcRecord)){
					let srcPos = this.oldVersionOptions.records.indexOf(srcRecord);
					
					if(trgPos !== srcPos) {
						let diffPos = srcPos - trgPos;
						for(let j=0; j<diffPos; j++){
							this.currentVersionOptions.records.splice(trgPos, 0, { fldUnqId: this.utilService.uniqueId('TEMP_LAYOUT_DD_DTO_ID') });
						}
					}
				}else{
					this.oldVersionOptions.records.splice(trgPos, 0, { fldUnqId: this.utilService.uniqueId('TEMP_LAYOUT_DD_DTO_ID') });
				}
			}
		}
		
		let currentLength = this.currentVersionOptions.records.length;
		let oldLength = this.oldVersionOptions.records.length;
		
		if(currentLength < oldLength) {
			let diffLength = oldLength - currentLength;
			for(let j=0; j<diffLength; j++){
				this.currentVersionOptions.records.push({ fldUnqId: this.utilService.uniqueId('TEMP_LAYOUT_DD_DTO_ID') });
			}
		}
		
		let currentRecords = this.currentVersionOptions.records;
		let oldRecords = this.oldVersionOptions.records;
		
		trgGrid.clear();
		srcGrid.clear();
		trgGrid.records = currentRecords;
		srcGrid.records = oldRecords;
		trgGrid.refresh();
		srcGrid.refresh();
		
		for(i=0; i<currentRecords.length; i++) {
			let trgRecord = currentRecords[i];
			let srcRecord = oldRecords[i];
			let gridName;
			let changeDot;
			let $tr;
			
			if(!trgRecord.fldEngNm) {
				// 삭제
				gridName = this.oldVersionOptions.name;
				changeDot = srcRecord.fldUnqId.replace(/\./g, '\\.');
				$tr = $(`tr#grid_${gridName}_rec_${changeDot}`);
				
				$tr.addClass('bg-del');
			}
			
			if(!srcRecord.fldEngNm) {
				// 추가
				gridName = this.currentVersionOptions.name;
				changeDot = trgRecord.fldUnqId.replace(/\./g, '\\.');
				$tr = $(`tr#grid_${gridName}_rec_${changeDot}`);
				
				$tr.addClass('bg-add');
			}
			
			if(trgRecord.fldEngNm && srcRecord.fldEngNm) {
				if(!(trgRecord.dataTypeNm === srcRecord.dataTypeNm && trgRecord.msgLen === srcRecord.msgLen)) {
					// 타입 또는 길이가 다를때 
					gridName = this.oldVersionOptions.name;
					changeDot = srcRecord.fldUnqId.replace(/\./g, '\\.');
					$tr = $(`tr#grid_${gridName}_rec_${changeDot}`);
					
					$tr.addClass('bg-red');
					
					gridName = this.currentVersionOptions.name;
					changeDot = trgRecord.fldUnqId.replace(/\./g, '\\.');
					$tr = $(`tr#grid_${gridName}_rec_${changeDot}`);
					
					$tr.addClass('bg-red');
				}
			}
		}
	}
	
	pageBtnClick(num){
		this.pageNumber = num;
		this.getDeployList(num === 1);
	}
	
	closeModal(isOk){
		this.$uibModalInstance.dismiss();
		setTimeout(()=>{
			w2ui[this.deployOptions.name].destroy();
			w2ui[this.oldVersionOptions.name].destroy();
			w2ui[this.currentVersionOptions.name].destroy();
		});
	}
}

module(App.name).controller('SCR0706Controller', SCR0706Controller);
