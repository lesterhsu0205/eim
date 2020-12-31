import { module } from 'angular';
import App from '../../../app';

class SCR0704Controller {
	
	constructor ($scope, $uibModalInstance, $timeout, httpService, utilService, 
		codeService, gridService, popupService, userService, data){
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
		this.msglayoutbsDtoMap = {};
		this.allMsgType = data.codes.MSG_TYPE;
		this.user = this.userService.getUser();
		
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
		this.width = width;
		this.height = height;
		this.top = 100;
		this.left = 20;
		this.right = 20;
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
			recid: 'fldUnqId',
			columns: this._getDefaultMsgDetailGridColumns(),
			onClick: (e) => {
				// prevent deselect
				let selection = w2ui[e.target].getSelection();
				if(selection.length === 1 && selection[0] === e.recid) {
					e.preventDefault();
				}
				
				let grid = w2ui[this.msgDetailOptions.name];
				let row = grid.get(e.recid);
				
				if(row.msgDscd && row.msgLayoutId) {
					console.log(this.msglayoutbsDtoMap[row.msgDscd + ':' + row.msgLayoutId]);
					
					this.selectedMsg = this.msglayoutbsDtoMap[row.msgDscd + ':' + row.msgLayoutId];
					this._selectedMsg = this.msglayoutbsDtoMap[row.msgDscd + ':' + row.msgLayoutId];
					
					this.changeTransactionType();
					this.setRegDttm();
					
					this.$scope.$apply();
				}
			},
			onDblClick: (e) => {
				let grid = w2ui[this.msgDetailOptions.name];
				let row = grid.get(e.recid);
				
				if(row.msgDscd) {
					e.preventDefault();
				}
			}
		}
	}
	
	_getDefaultMsgDetailGridColumns(){
		let columns = [
			{ 
				field: 'msgDscd', caption: this.text.msgType, size: '100px', frozen: true, attr:"align=left", 
				render: (data,index,colIndex) => {		
					let msgDscd;
					
					if(data && data.msgDscd) {
						msgDscd = data.msgDscd;
					}
					
					return this.codeService.getCodeValNm('MSG_TYPE', msgDscd);
				}
			},
			{ field: 'msgSeq', caption: 'seq', size: '40px', frozen: true},
			{ field: 'fldEngNm', caption: this.text.fldEngNm, size: '2%', frozen: true, attr:"align=left", 
				render:(data)=>{
					var spaceTemp = `&nbsp;&nbsp;&nbsp;&nbsp;`;
					var fldEngNm = '';
					
					if(data.w2ui && data.w2ui.changes && data.w2ui.changes.fldEngNm) {
						fldEngNm = data.w2ui.changes.fldEngNm ? data.w2ui.changes.fldEngNm : '';
					}else{
						fldEngNm = data.fldEngNm ? data.fldEngNm : '';
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
					
				}
			},
			{ field: 'fldKorNm', caption: this.text.fldKorNm, attr:"align=left", size: '2%', frozen: true },
			{ 
				field: 'dataTypeNm', caption: this.text.dataType, size: '80px', frozen: true,  
				render: (data,index,colIndex) => {
					const dataTypeNm = w2ui[this.msgDetailOptions.name].getCellValue(index, colIndex);
					return this.codeService.getCodeValNm(this.codes.DATA_TYPE, dataTypeNm);
				}
				
			},
			{ field: 'msgLen', caption: this.text.msgLen, size: this.user.locale === 'en'? '50px' : '45px',  frozen: true },
			{ field: 'decimalLen', caption: this.text.decimalLen, size:  this.user.locale === 'en'? '110px' : '45px',  frozen: true },
			{ field: 'fldLvNo', caption: this.text.fldLvNo, size: '50px', frozen: true , style: 'border-right: 0.5px solid black' },
			{ field: 'childDtoNm', caption: this.text.childDtoNm, size: '1%'},
			{ field: 'arraySizeRefVal', caption: this.text.arraySizeRefVal, size: '1%'},
			{ 
				field: 'alignNm', caption: this.text.alignNm, size: this.user.locale === 'en'? '70px' : '40px',
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
				field: 'fldRmk', caption: this.text.fldRmk, size: '2%', attr:"align=left",
				render: (data,index,colIndex) => {
					return data.fldRmk ? '<span title="' + data.fldRmk + '">' + data.fldRmk + '</span>' : '';
				}
			},
			{ field: 'confirmMeta', caption: this.text.confirmMeta, size: this.user.locale === 'en'? '110px' : '60px' }
		];

		return columns;
	}

	getMsgDetailGridData() {
		if(this.data.gridDataList) {
			this.displayMsgDetailGridData({msglayoutbsDtoList: this.data.gridDataList});
		}else{
			if(this.data.msgLayoutId.length === 0) {
				return;
			}
			
			this.httpService.post(`/msglayoutslist`, this.data.msgLayoutId).then(res => {		
				this.displayMsgDetailGridData(res);
			});
		}
	}
	
	displayMsgDetailGridData(res) {	
		let selectedFldUnqId;
		let selectedDetail = res.msglayoutbsDtoList[0];
		
		let gridData = [];
		let calcData = [];
		
		let calcLengthTxt = '';
		
		res.msglayoutbsDtoList.map((data, idx) => {
			this.msglayoutbsDtoMap[data.msgDscd + ':' + data.msgLayoutId] = data;
			
			let fldUnqId = this.utilService.uniqueId('TEMP_LAYOUT_DD_DTO_ID');
			
			gridData.push({
				fldUnqId: fldUnqId,
				msgLayoutId: data.msgLayoutId,
				msgDscd: data.msgDscd,
				w2ui: {
					children: data.msglayoutdtDto
				}
			});
			
			calcData = calcData.concat(data.msglayoutdtDto);
			
			if(idx === 0) {
				selectedFldUnqId = fldUnqId;
			}
			
			if(this.data.trxDscd === 'ONLINE' && data.msgDscd === 'IV') {
				selectedFldUnqId = fldUnqId;
				selectedDetail = data;
			}else if(this.data.trxDscd === 'BATCH' && data.msgDscd === 'BATB') {
				selectedFldUnqId = fldUnqId;
				selectedDetail = data;
			}
			
			calcLengthTxt += '<span style="margin-right: 15px !important;">' + this.codeService.getCodeValNm('MSG_TYPE', data.msgDscd) + ' : '+ this.calcLength(data.msglayoutdtDto) + '</span>';
		});

		if(this.data.trxDscd === 'ONLINE') {
			$('.msg-length-wrap').html(this.text.totalLength + ' : ' + this.calcLength(calcData));	
		}else{
			$('.msg-length-wrap').html(calcLengthTxt);	
		}
		
		this.msgDetailOptions.records = gridData;
		
		this.selectedMsg = this.utilService.clone(selectedDetail);
		this._selectedMsg = this.utilService.clone(selectedDetail);
		
		this.changeTransactionType();
		this.setRegDttm();
		
		let grid = w2ui[this.msgDetailOptions.name];
		setTimeout(()=>{
			grid.expand(selectedFldUnqId);
		}, 300);
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
	
	calcLength(records){
		let record = this.utilService.clone(records);
		
		var msgLen = 0;
		
		var layoutLength = new Array();
		
		for (var i = 0; i < record.length; i++) {
			
			if(record[i].dataTypeNm == "LAYOUT"){
				var data = new Object();
				
				data.id = record[i].fldUnqId;
				
				if(record[i].arraySizeRefVal == undefined || isNaN(record[i].arraySizeRefVal)){
					data.msgLen = 1;		
					record[i].arraySizeRefVal = 1;
				}
				
				
				if(record[i].arraySizeRefVal != undefined && record[i].parentFldNm == undefined){
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
		
		return msgLen;
	}
		
	closeModal(isOk){
		const grid = w2ui[this.msgDetailOptions.name];
	
		if(isOk){		
			this.$uibModalInstance.close();
		} else {
			this.$uibModalInstance.dismiss();
		}
		
		setTimeout(()=>grid.destroy());
	}
}

module(App.name).controller('SCR0707Controller', SCR0704Controller);

