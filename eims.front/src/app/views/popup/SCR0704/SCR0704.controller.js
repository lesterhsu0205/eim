import { module } from 'angular';
import App from '../../../app';

class SCR0704Controller {
	
	constructor ($scope, $uibModalInstance, $timeout, httpService, utilService, codeService, 
		gridService, popupService, userService, data){
		this.$scope = $scope;
		this.$uibModalInstance = $uibModalInstance;
		this.$timeout = $timeout;
		this.httpService = httpService;
		this.utilService = utilService;
		this.gridService = gridService;
		this.popupService = popupService;
		this.codeService = codeService;
		this.userService = userService;
		this.data = data;
		this.codes = codeService.commonCodes;
		this.user = this.userService.getUser();

		console.log(this.codes);
		
		this.allMsgType = data.codes.MSG_TYPE;
		
		this.initWindow('100%', '100%');
		this.initText();
		this.initGridOption();
		
		this.$scope.$on(`gridRendered`, () => {
			this.getMsgDetailGridData();
		});
		
		let resizeHandler = () => {
			this.$timeout(() => {
				this.msgDetailOptions.name && w2ui[this.msgDetailOptions.name].refresh();
			});
		};
		
		$(window).on('resize', resizeHandler);
		
		this.$scope.$on('$destroy', () => {
			$(window).off('resize', resizeHandler);
		});
	}
	
	initWindow(width, height){
//		const { top, left } = this.popupService.calculatePosition(width,height);
		
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
	
	initGridOption(){
		this.msgDetailOptions = {
			limit: 99999,
			pageSize: 99999,
			recordsCount: 0,
			selectType: 'cell',
			recid: 'fldUnqId',
			onClick: (e) => {
				// prevent deselect
				let selection = w2ui[e.target].getSelection();
				if(selection.length === 1 && selection[0] === e.recid) {
					e.preventDefault();
				}
			}
		}
	}
	
	getMaskCodes(defaultArray = []){

		this.httpService.get('maskCd').then(data => {
			const { maskOutList: records, totalCnt: recordsCount } = data;
			
			this.maskCd = records;
			
			this.maskCd.map(records => {
				defaultArray.push({
					id: records.maskCd,
					text: records.maskNm
				});
			});
		});


		return defaultArray;
	}

	getMaskNm(code){
		
		var maskNm ='';
		for(var i=0; i<this.maskCd.length; i++) {
			if(this.maskCd[i].maskCd == code) {
				maskNm = this.maskCd[i].maskNm;
				break;
			}		
		}

		return maskNm;
	}

	_getDefaultMsgDetailGridColumns(msg){
		let columns = [
			{ field: 'msgSeq', caption: 'seq', size: '40px', frozen: true},
			{ field: 'fldEngNm', caption: this.text.fldEngNm, size: '2%', frozen: true, attr:"align=left", 
				render:(data)=>{
					var spaceTemp = `&nbsp;&nbsp;&nbsp;&nbsp;`;
					var fldEngNm;
					
					if(data.w2ui && data.w2ui.changes && data.w2ui.changes.fldEngNm) {
						fldEngNm = data.w2ui.changes.fldEngNm;
					}else{
						fldEngNm = data.fldEngNm;
					}
					
					var space = '';
					if(data.fldLvNo == 0){
						return fldEngNm;
					}else{
						for (var i = 0; i < data.fldLvNo; i++) {
							space = space.concat(spaceTemp);
						}
						return space + fldEngNm;				
					}
					
				},
				editable: this.isEdit ? {type: 'text'} : false
			},
			{ field: 'fldKorNm', caption: this.text.fldKorNm, attr:"align=left", size: '2%', frozen: true, editable: this.isEdit ? {type: 'text'} : false },
			{ 
				field: 'dataTypeNm', caption: this.text.dataType, size: '80px', frozen: true,  
				editable: this.isEdit ? { 
					type: 'select', 
					items: this.gridService.getSelectItemsFromCodes(this.codes.DATA_TYPE)
				} : false,
				render: (data,index,colIndex) => {
					const dataTypeNm = w2ui[this.msgDetailOptions.name].getCellValue(index, colIndex);
					return this.codeService.getCodeValNm(this.codes.DATA_TYPE, dataTypeNm);
				}
				
			},
			{ field: 'msgLen', caption: this.text.msgLen, size: this.user.locale === 'en'? '50px' : '45px',  frozen: true, editable: this.isEdit ? {type: 'int'} : false },
			{ field: 'decimalLen', caption: this.text.decimalLen, size: this.user.locale === 'en'? '110px' : '45px',  frozen: true, editable: this.isEdit ? {type: 'int'} : false },
			{ field: 'fldLvNo', caption: this.text.fldLvNo, size: '50px', frozen: true , style: 'border-right: 0.5px solid black', editable: this.isEdit ? {type: 'int'} : false },
			{ field: 'childDtoNm', caption: this.text.childDtoNm, size: '1%', editable: this.isEdit ? {type: 'text'} : false},
			{ field: 'arraySizeRefVal', caption: this.text.arraySizeRefVal, size: '1%', editable: this.isEdit ? {type: 'text'} : false},
			{ 
				field: 'privacyDscd', caption: this.text.privacyDscd, size: '1%', type: 'select', items: this.getMaskCodes(),
				render: (data,index,colIndex) => {
					const privacyDscd = w2ui[this.msgDetailOptions.name].getCellValue(index, colIndex);
					return this.getMaskNm(privacyDscd); 
				}
			},
			{ field: 'encYn', caption: this.text.encYn, size: '1%', editable: { type: 'select', items: this.gridService.getSelectItemsFromCodes(this.codes.YN_CD) } },		
			{ 
				field: 'alignNm', caption: this.text.alignNm, size: this.user.locale === 'en'? '70px' : '40px',
				editable: this.isEdit ? { type: 'select', items: this.gridService.getSelectItemsFromCodes(this.codes.ALIGN_CD, [{id: "NONE", text: " "}])} : false,
				render: (data,index,colIndex) => {
					const alignNm = w2ui[this.msgDetailOptions.name].getCellValue(index, colIndex);
					return this.codeService.getCodeValNm(this.codes.ALIGN_CD, alignNm);
				}
			},
			{ 
				field: 'fillerVal', caption: this.text.fillerVal, size: '60px', 
				editable: this.isEdit ? { type: 'select', items: this.gridService.getSelectItemsFromCodes(this.codes.FILLER_CD, [{id: "", text: ""}])} : false,
					render: (data,index,colIndex) => {
						const fillerVal = w2ui[this.msgDetailOptions.name].getCellValue(index, colIndex);
						return this.codeService.getCodeValNm(this.codes.FILLER_CD, fillerVal);
					}
			},
			{ 
				field: 'fldRmk', caption: this.text.fldRmk, size: '2%', editable: this.isEdit ? { type: 'text'} : false, attr:"align=left",
				render: (data,index,colIndex) => {
					return data.fldRmk ? '<span title="' + data.fldRmk + '">' + data.fldRmk + '</span>' : '';
				}
			}
		];
		
		columns.push({ field: 'confirmMeta', caption: this.text.confirmMeta, size: this.user.locale === 'en'? '110px' : '60px' });
		
		return columns;
	}
	
	changeMsgDetailGridColumns(msg){
		let columns = this._getDefaultMsgDetailGridColumns(msg);

		if(!_.isEmpty(msg)){
			switch(msg.msgDscd){
			case 'CH':
				if(msg.chlDscd === 'EXTERNAL'){
					columns.push(...[
						{ field: 'extrnlMsgNoYn', caption: this.text.extrnlMsgNoYn, size: '80px' },
						{ field: 'extrnlSrchKeyYn', caption: this.text.extrnlSrchKeyYn, size: '80px' },
					]);
				}
				break;
			case 'IV':
				if(msg.chlDscd === 'EXTERNAL'){
					columns.push(...[
						{ field: 'extrnlSrchKeyYn', caption: this.text.extrnlSrchKeyYn, size: '80px', editable: this.isEdit ? { type: 'select', items: this.gridService.getSelectItemsFromCodes(this.codes.YN_CD)} : false },
					]);
				}
				break;
			}
		}

		this.msgDetailOptions.columns = columns;
	}
	
	getMsgDetailGridData() {
		if(this.data.gridData) {
			this.displayMsgDetailGridData(this.data.gridData);
		}else{
			let encodedId = encodeURIComponent(this.data.msgLayoutId);
			
			this.httpService.get(`/msglayouts/${encodedId}`).then(res => {
				this.displayMsgDetailGridData(res);
			});
		}	
	}
	
	displayMsgDetailGridData(res) {
		const utilService = this.utilService;
		let { msglayoutdtDto: originRecords } = res;
		
		this.changeMsgDetailGridColumns(res);
		
		this.selectedMsg = utilService.clone(res);
		this._selectedMsg = utilService.clone(res);
		
		this.changeTransactionType();
		this.setRegDttm();
		
		this.msgDetailOptions.recordsCount = originRecords.length;
		this.msgDetailOptions.records = originRecords.map(record => {
			record.fldUnqId = record.fldUnqId ? record.fldUnqId : this.utilService.uniqueId('TEMP_LAYOUT_DD_DTO_ID');
			return record;
		});
		
		if(this.selectedMsg.rsrvFldVal3){
			this.msgLength = this.selectedMsg.rsrvFldVal3;
		}else{
			this.calcLength(this.msgDetailOptions.records);
		}
	}
	
	setRegDttm(){
		const regDttm = this.selectedMsg.regDttm 
		
		if(regDttm) {
			this.selectedMsg.regDttmString = this.utilService.setRegDttm(regDttm);
		}
	}
	
	changeTransactionType(){
		if(_.isEmpty(this.selectedMsg)) {
			this.msgTypes = [];
			return;
		}
		
		switch(this.selectedMsg.chlDscd){
			case 'INTERNAL':
				switch(this.selectedMsg.trxDscd){
				case 'ONLINE':
					this.msgTypes = this.allMsgType.filter(v => v.cdVal == 'IV' || v.cdVal == 'CH' || v.cdVal == 'STH' );
					break;
				case 'BATCH':
					this.msgTypes = this.allMsgType.filter(v => v.cdVal == 'BATH' || v.cdVal == 'BATB' || v.cdVal == 'BATT' );
					break;
				}
				break;
			case 'EXTERNAL':
				switch(this.selectedMsg.trxDscd){
				case 'ONLINE':
					this.msgTypes = this.allMsgType.filter(v => v.cdVal == 'IV' || v.cdVal == 'CH');
					break;
				case 'BATCH':
					this.msgTypes = this.allMsgType.filter(v => v.cdVal == 'BATH' || v.cdVal == 'BATB' || v.cdVal == 'BATT' );
					break;
				}
				break;
		}
	}
	
	calcLength(record){
		var msgLen = 0;
		
		var layoutLength = new Array();
		
		for (var i = 0; i < record.length; i++) {
			
			if(record[i].dataTypeNm == "LAYOUT"){
				var data = new Object();
				
				data.id = record[i].fldUnqId;
				
				if(record[i].arraySizeRefVal == undefined || isNaN(record[i].arraySizeRefVal)){
					data.msgLen = 1;					
				}else if(record[i].arraySizeRefVal != undefined && record[i].parentFldNm == undefined){
					data.msgLen = Number(record[i].arraySizeRefVal);
				}else{
					
					for (var j = 0; j < layoutLength.length; j++) {
						if(layoutLength[j].id == record[i].parentFldNm){
							data.msgLen = Number(record[i].arraySizeRefVal) * Number(layoutLength[j].msgLen);							
						}
						
					}
				}
				layoutLength.push(data);
				
			}
			
		}
		
		for (var i = 0; i < record.length; i++) {
			if(record[i].parentFldNm == undefined){
				msgLen += Number(record[i].msgLen);
			}else{		
				for (var j = 0; j < layoutLength.length; j++) {
					if(record[i].parentFldNm == layoutLength[j].id){						
						msgLen += Number(record[i].msgLen) * Number(layoutLength[j].msgLen);
					}
				}	
			}
		}
		
		this.msgLength = msgLen;
		
		return this.msgLength;
	}
	
	closeModal(isOk){
		if(isOk){
			this.$uibModalInstance.close();
		} else {
			this.$uibModalInstance.dismiss();
		}
		
		setTimeout(()=>w2ui[this.msgDetailOptions.name].destroy());
	}
}

module(App.name).controller('SCR0704Controller', SCR0704Controller);

