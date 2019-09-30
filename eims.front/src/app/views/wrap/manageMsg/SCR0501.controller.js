import { module } from 'angular';
import App from '../../../app';

class SCR0501Controller {
	constructor ($scope, $compile, $state, $timeout, httpService, gridService ,
			utilService, codeService, popupService, metaService, userService, codes){
		this.$scope = $scope;
		this.$state = $state;
		this.$compile = $compile;
		this.$timeout = $timeout;
		this.codes = codes;
		this.workStatusList = codes.WORK_STATUS_CD.filter(v => v.cdVal !== 'DEPLOY_COMP');
		this.httpService = httpService; 
		this.utilService = utilService;
		this.codeService = codeService;
		this.gridService = gridService;
		this.popupService = popupService;
		this.metaService = metaService;
		this.userService = userService;
		this.user = this.userService.getUser();
		
		this.initText();
		this.initSelect();
		this.initCodeMap();

		this.allMsgType = codes.MSG_TYPE;
		this.initMsgGridOptions();
		this.initMsgDetailGridOptions();
		this.getMaskCodes();
		this.noncoreSys = this.getNoncoreSysCodes();

		let count = 0;
		this.$scope.$on(`gridRendered`, () => {
			count ++;
			if(count === 2) {
				this.initPrevData();
			}
		});
		
		$(window).on('resize', () => {
			this.$timeout(() => {
				// w2ui grid inEditMode false로 설정해주지 않으면 에디팅 되지 않음
				const grid = w2ui[this.msgDetailOptions.name];
				grid.last.inEditMode = false;
				this.msgDetailOptions.autoComplete = false;
				
				this.msgDetailOptions.name && w2ui[this.msgDetailOptions.name].refresh();	
			});
		});
	
		this.generatorLayoutdtDto = (rowData = {}, parentFldUnqId = '') => {
			let parentId = _.isEmpty(parentFldUnqId) ? this.selectedMsg.msgLayoutId : parentFldUnqId;
			if(_.isEmpty(parentId)) parentId = this.selectedMsg._msgLayoutId;
			const id = this.utilService.uniqueId('TEMP_LAYOUT_DD_DTO_ID'); 
			rowData.fldUnqId = `${parentId}.${id}`;
			
			if(!rowData.fldLvNo) rowData.fldLvNo = 0;
			if(!rowData.dataTypeNm) rowData.dataTypeNm = 'STRING';
			if(!rowData.msgLen) rowData.msgLen = 0;
			if(!rowData.decimalLen) rowData.decimalLen = 0;
			if(!rowData.alignNm) rowData.alignNm = this.getAlignNmByDataType(rowData.dataTypeNm);
			if(!rowData.extrnlMsgNoYn) rowData.extrnlMsgNoYn = 'N';
			if(!rowData.encYn) rowData.encYn = 'N';
			if(!rowData.privacyDscd) rowData.privacyDscd = '00';

			delete rowData.index;
			delete rowData.column;
			return rowData;
		}
		
		this.changeCodeFunc = (fieldNm, cdNm) => {
			return this.codeMap[fieldNm][cdNm];
		}
		
		this.getAlignNmByDataType = (dataTypeNm) => {
			let alignNm = '';
			
			if(dataTypeNm === 'STRING' || dataTypeNm === 'BYTEARRAY') {
				alignNm = 'LEFT';
			}else if(dataTypeNm === 'INTEGER' || dataTypeNm === 'BIGDECIMAL') {
				alignNm = 'RIGHT';
			}
			
			return alignNm;
		}


	}
	
	initPrevData() {
		const currentStateName = this.$state.current.name;
		const param = this.utilService.getParams(currentStateName);

		if(!_.isEmpty(param)){
			if(param.scope) {
				let prevScope = param.scope.vm;
				
				// 탐색
				this.searchParam = prevScope.searchParam;
				
				// 그리드 pageSize
				this.select.pageSize = prevScope.select.pageSize;
				this.msgOptions.limit = prevScope.select.pageSize;
				this.gridHeight = prevScope.gridHeight;
				
				// 그리드
				this.msgOptions.records = prevScope.msgOptions.records;
				this.msgOptions.recordsCount = prevScope.msgOptions.recordsCount;
				
				this.$timeout(() => {
					this.pageNumber = prevScope.pageNumber;
					this.$scope.$broadcast(`resetPage`, this.pageNumber);
				});
					
				if(prevScope.selectedMsg && Object.keys(prevScope.selectedMsg).length > 0) {
					// 상세
					this.selectedMsg = prevScope.selectedMsg;
					this._selectedMsg = prevScope._selectedMsg;
					
					// 상세 그리드					
					this.setMsgOption();
					this.setRegDttm();
					
					this.msgDetailOptions.recordsCount = prevScope.msgDetailOptions.recordsCount;
					this.msgDetailOptions.records = prevScope.msgDetailOptions.records;
				}
				
				// 수정모드
				this.isAdd = prevScope.isAdd;
				if(prevScope.isEdit){
					this.onEditMode();
				}else{
					this.offEditMode();
				}
				
				// 스크롤
				$('#main-contents').scrollTop(prevScope.scrollTop);
				
				if(prevScope.impactPopupVisible) {
					$('#msg-impact-poopup').show();
				}
			}
			
			if(param.data){
				let data = param.data;
				
				data.msgLayoutId && this.getMsgDetailGridData(data.msgLayoutId);
				this.searchParam = { msgLayoutId: data.msgLayoutId };
				this.getGridData(true);
				this.offEditMode();
			}
		} else {
			this.resetSearch();
		}
		
		this.$scope.$on('$destroy', () => {
			this.scrollTop = $('#main-contents').scrollTop();
			this.utilService.setParams(currentStateName, {scope: this.$scope});
			
			let impactPopup = $('#msg-impact-poopup');
			this.impactPopupVisible = impactPopup.is(':visible');
			impactPopup.hide();
			
			$(window).off('resize');
		});
	}
	
	initText() {
		this.text = $.extend({}, bxMsg.getMessages('common'), bxMsg.getMessages('manageMsg'));		
	}
	
	initSelect(){
		this.select = this.gridService.getSelect(this.codes['GRID_PAGE_SIZE'][1].cdVal); 
	}
	
	initCodeMap() {
		this.codeMap = {
			'privacyDscd': {},
			'dataTypeNm': {},
			'fillerVal': {}
		}; 
		
		this.codes.DATA_TYPE.map((codeObj) => {
			this.codeMap['dataTypeNm'][codeObj.cdValNm] = codeObj.cdVal;
		});
		
		this.codes.FILLER_CD.map((codeObj) => {
			this.codeMap['fillerVal'][codeObj.cdValNm] = codeObj.cdVal;
		});
		  
		this.proFepSysCd = ['FEA','FEJ','FEN','FEH','FEP'];
		this.isProFEP = false;
		this.isNoncoreSystem = false;
	}

	getMaskCodes(defaultArray = []){

		this.httpService.get('maskCd').then(data => {
			const { maskOutList: records, totalCnt: recordsCount } = data;
			
			this.maskCd = records;
			if(!_.isEmpty(this.maskCd)){
				this.maskCd.map(records => {
					defaultArray.push({
						id: records.maskCd,
						text: records.maskNm
					});
				});
			}
		});


		return defaultArray;
	}

	getMaskNm(code){
		
		var maskNm ='';
		if(!_.isEmpty(this.maskCd)){
			for(var i=0; i<this.maskCd.length; i++) {
				if(this.maskCd[i].maskCd == code) {
					maskNm = this.maskCd[i].maskNm;
					break;
				}		
			}
		}

		return maskNm;
	}

	getNoncoreSysCodes(defaultArray = []){

		this.httpService.get('/srsyss?noncoreYn=Y').then(data => {
			const { srsysbsOutList: records, totalCnt: recordsCount } = data;
			
			this.noncoreSys = records;
			if(!_.isEmpty(this.noncoreSys)){
				this.noncoreSys.map(records => {
					defaultArray.push({
						sysCd: records.sysCd,
						sysNm: records.sysNm,
						noncoreYn: records.noncoreYn,
					});
				});
			}
			return defaultArray;
		});
		
	}

	initMsgGridOptions() {
		this.gridHeight = this.gridService.getGridHeight(this.select.pageSize);
		this.msgOptions = {
				limit: this.select.pageSize,
				pageSize: this.select.pageSize,
				show: {columnHeaders:true, selectColumn : true},
				recordsCount: 0,
				recid: 'msgLayoutId',
				multiSelect : false,
				columns: [
					{
						field: 'no', caption: 'No', size: '40px',
						render: (data, index) => this.gridService.getNoField(this.msgOptions.limit, index, this.pageNumber)
					},
					{ 
						field: 'lvCds', caption: this.text.lvCdsStr, size: '0.7%', sortable: true,
						render: (data) => {
							return data.lv1Cd ? data.lv1Cd : '';
						}
					},
					{ field: 'msgLayoutId', caption: this.text.msgLayoutId, size: '1.5%', sortable: true, tooltip: false},
					{ field: 'msgNm', caption: this.text.msgName, size: '2%', attr:"align=left", sortable: true},
					{ field: 'msgNmSub', caption: this.text.msgNameSub, size: '2%', attr:"align=left", sortable: true},
					{ 
						field: 'chlDscd', caption: this.text.chlDscd, size: this.user.locale === 'en'? '110px' : '0.5%', sortable: true,
						render: (data,index,colIndex) => {
							return this.codeService.getCodeValNm('CHL_DSCD', data.chlDscd);
						}
					},
					{ field: 'trxDscd', caption: this.text.trxDscd, size: this.user.locale === 'en'? '120px' : '0.5%', sortable: true,
						render: (data,index,colIndex) => {
							const trxDscd = w2ui[this.msgOptions.name].getCellValue(index, colIndex);
							return this.codeService.getCodeValNm('TRAN_DSCD', trxDscd);
						}
					},
					{ field: 'msgDataVal', caption: this.text.msgDataVal + '/' + this.text.dtoNm, attr:"align=left", size: this.user.locale === 'en'? '200px' : '1.5%', sortable: true},
					{ 
						field: 'msgDscd', caption: this.text.msgType, size: this.user.locale === 'en'? '90px' : '0.5%', sortable: true,
						render: (data,index,colIndex) => {
							return this.codeService.getCodeValNm('MSG_TYPE', data.msgDscd);
						}
					},
					{ field: 'regManId', caption: this.text.msgRegister, size: this.user.locale === 'en'? '70px' : '60px', sortable: true},
					{ 
						field: 'workStatusCd', caption: this.text.workStatusCd, size: this.user.locale === 'en'? '110px' : '0.5%', sortable: true,
						render: (data) => {		
							return this.codeService.getCodeValNm('WORK_STATUS_CD', data.workStatusCd);
						}
					},
					{ 
						field: 'regDttm', caption: this.text.msgRegisterDt, size: this.user.locale === 'en'? '110px' : '70px', sortable: true,
						render: (data) =>{
							const regDttm = data.regDttm 
							const yy = regDttm.substring(0,4);
							const mm = regDttm.substring(4,6);
							const dd = regDttm.substring(6,8);
							return yy+"/"+mm+"/"+dd;
						}
					},
					{ 
						caption: this.text.edit, size: '90px',
						render: (data)=> {
							let html = '';

							if(this.user.perm.update) {
								html += '<button type="button" class="bw-btn bxd bxd-edit2" data-action="edit"></button>';
							}

							if(this.user.perm.delete) {
								html += '<button type="button" class="bw-btn bxd bxd-trash" data-action="delete"></button>';
							}

							return html;
						}
					}
				],
				onClick: (e) => {
					// init selected detail row 
					this._selectedDetailRow = {}
					
					
					const grid =  w2ui[this.msgOptions.name];
					const editData = grid.get(e.recid);
					const eTarget = e.originalEvent.target;

					this.selectedData = editData;
					
					if (eTarget.localName === 'button') {
						const action = $(eTarget).attr('data-action');
						
						if(action === 'export') {
							this.excelExport(editData.msgLayoutId);	
							e.preventDefault();
							return;
						}else if(action === 'edit') {
							this._onEdit();
						}else {
							this.openConfirm(grid, e.recid);
							e.preventDefault();
							return;
						}
					} else {
						this.offEditMode();
					}
					this.$scope.$apply();
					
					// prevent deselect
					let selection = w2ui[e.target].getSelection();
					let $target = $(e.originalEvent.target);
					
					if($target.find('.w2ui-grid-select-check').length === 0) {
						if(selection.length === 1 && selection[0] === e.recid) {
							e.preventDefault();
							
							if(!_.isEmpty(this._selectedMsg)){
								return;
							}
						}
					}
					var sendSysCd = editData.msgLayoutId.substring(1,4);
					var recvSysCd = editData.msgLayoutId.substring(4,7);
					this.isNoncoreSystem = false;
					if(!_.isEmpty(this.noncoreSys)){
						this.noncoreSys.map(records => {	
							if( records.sysCd.includes(sendSysCd) || records.sysCd.includes(recvSysCd)) {
								this.isNoncoreSystem = true;			
							} 
						});
					}
					this.getMsgDetailGridData(editData.msgLayoutId);
				}
			};
	}
	
	initMsgDetailGridOptions() {
		this.msgDetailOptions = {
				limit: this.select.pageSize,
				pageSize: this.select.pageSize,
				autoCompleteIndics: [ 1, 2 ],
				records: [],
				recordsCount: 0,
				selectType: 'cell',
				recid: 'fldUnqId',
				columns: this._getDefaultMsgDetailGridColumns(),
				onClick: (e) => {
					const grid = w2ui[e.target];
					const recId = e.recid;
					
					this._selectedDetailRow = {
						grid: grid,
						editData: grid.get(recId)
					};
					
					// 타입이 INTEGER나 BIGDECIMAL이면 정렬을 RIGHT만 선택가능하도록 처리
					if(grid.columns[e.column]['field'] === 'alignNm') {
						if(this._selectedDetailRow.editData.dataTypeNm === 'INTEGER' || this._selectedDetailRow.editData.dataTypeNm === 'BIGDECIMAL') {
							grid.columns[e.column] = { 
								field: 'alignNm', caption: this.text.alignNm, size: '40px',
								editable: this.isEdit ? { type: 'select', items: [{id: 'RIGHT', text: 'RIGHT'}]} : false,
								render: (data,index,colIndex) => {
									const alignNm = w2ui[this.msgDetailOptions.name].getCellValue(index, colIndex);
									return this.codeService.getCodeValNm(this.codes.ALIGN_CD, alignNm);
								}
							};
						}else{
							grid.columns[e.column] = { 
								field: 'alignNm', caption: this.text.alignNm, size: '40px',
								editable: this.isEdit ? { type: 'select', items: this.gridService.getSelectItemsFromCodes(this.codes.ALIGN_CD, [{id: "NONE", text: " "}])} : false,
								render: (data,index,colIndex) => {
									const alignNm = w2ui[this.msgDetailOptions.name].getCellValue(index, colIndex);
									return this.codeService.getCodeValNm(this.codes.ALIGN_CD, alignNm);
								}
							};
						}
					}
					
				},
				onPaste: (e) => {
					e.preventDefault();
					
					if(!this.isEdit) {
						return;
					}

					this.msgDetailOptions.isCopyMode = false;
					this.gridService.pasteForMsg(e, this.msgDetailOptions, this.generatorLayoutdtDto, this.changeCodeFunc);
					
					const grid = w2ui[e.target];
					
					grid.selectNone();
					this._selectedDetailRow = {};
					
					this.reorderSeq();
					grid.save();
					grid.refresh();
				},
				onCopy: (e) => {
					if(!this.isEdit) return;
					this.msgDetailOptions.isCopyMode = true;
					this._selectedDetailRow = {};
				},
				onChange: (e) => {
					const grid = w2ui[e.target];
					const record = grid.get(e.recid);
					
					if(!this.isEdit && e.originalEvent.target.type === 'checkbox'){
						e.preventDefault();
					} else if(e.column == 1){
						// 영문명 (변수명) 중복 체크 
						this.setParentFldNm(record);
						
						if(!this.fieldNameValid({fldLvNo: record.fldLvNo, fldEngNm: e.value_new, parentFldNm: record.parentFldNm})) {
							this.popupService.simpleAlert(this.$scope, this.text.dupFieldNm);
							e.value_new = ''
							return;
						}
					}
					
					e.onComplete = () => {
						if(record.w2ui.changes.fldLvNo != undefined){
							this.setParentFldNm(record, true);
							
							if(!this.fieldNameValid({fldLvNo: e.value_new, fldEngNm: record.fldEngNm, parentFldNm: record.parentFldNm})) {
								this.popupService.simpleAlert(this.$scope, this.text.dupFieldNm);
								record.fldEngNm = '';
							}
							
							grid.save();
						}else if(record.w2ui.changes.dataTypeNm != undefined){
							record.alignNm = this.getAlignNmByDataType(record.w2ui.changes.dataTypeNm);		
							
							if(record.w2ui.changes.dataTypeNm == 'LAYOUT'){
								record.msgLen = 0;
								record.decimalLen = 0;
								record.w2ui.changes.decimalLen = 0;
							}

							grid.save();
						}else if(record.w2ui.changes.fldEngNm != undefined){
							grid.save();
							
							// meta validate
							this.rowMetaValid(record);
							this.$scope.$apply();	
						}else if(record.w2ui.changes.fldRmk != undefined){
							grid.save();
						}
					}
					
				},
				onSelect: (e) => {
					if(this.isCopyMode){
						const grid = w2ui[e.target];
						const recId = e.recid;
						
						this._selectedDetailRow = {
							grid: grid,
							editData: grid.get(recId)
						};
					}
				},
				onUnselect: (e) => {
					if(_.isEmpty(this._selectedDetailRow) || _.isEmpty(this._selectedDetailRow.editData)) return;
					if(e.recid === this._selectedDetailRow.editData.recid) {
						this._selectedDetailRow = {};
					}
				},
				onEditField: (e) => {
					if(!this.isEdit) return;
					const grid = w2ui[e.target];
					const record = grid.get(e.recid);
					
					// 타입이 레이아웃일때 소수점 입력 불가  
					if(record.dataTypeNm == 'LAYOUT' && e.column == 5){
						e.preventDefault();
					}
					
					// 자동완성필드이고 타입이 레이아웃이 아닌경우 자동완성기능
					if(!this.gridService.isAutoCompleteField(this.msgDetailOptions, e) || record.dataTypeNm === 'LAYOUT') return;
					this.gridService.autoComplete(e, this, this.msgDetailOptions);
					this._onEditEvent = e;
				}
			};
	}
	
	// 자동완성 값 선택시, 호출되는 메타데이터 입력 처리 메서드
	// this.gridService.autoComplete 에서 호출
	onSelectAutoComplete(metaData, $editInput, column){
		if(_.isEmpty(this._onEditEvent)) return;
		let $input = $editInput ? $editInput.get(0) : $(`div[name=${this.msgDetailOptions.name}] .w2ui-editable input`).get(0);
		const grid = w2ui[this.msgDetailOptions.name];
		const record = grid.get(this._onEditEvent.recid);
		
		const originalEvent = this._onEditEvent;
		delete this._onEditEvent;
		
		// w2ui grid inEditMode false로 설정해주지 않으면 에디팅 되지 않음
		grid.last.inEditMode = false;
		this.msgDetailOptions.autoComplete = false;
		
		// 순서 변경 
//		if(grid.moved) {
//			grid.moved = false;
//			return;
//		}
		
		this.searchItems = [];
		if(_.isEmpty(metaData)){
			//record && ($input.value = record.fldKorNm);
			grid.editChange.call(grid, $input, originalEvent.index, originalEvent.column, originalEvent);
			
			if(record && record.w2ui && record.w2ui.changes){
				if(column === 1 && record.w2ui.changes.fldEngNm){
					record.fldEngNm = record.w2ui.changes.fldEngNm;
				}else if(column === 2 && record.w2ui.changes.fldKorNm) {
					record.fldKorNm = record.w2ui.changes.fldKorNm;
				}

				grid.save();
			}
		} else {	
			this.setParentFldNm(record);
			
			if(!this.fieldNameValid({fldLvNo: record.fldLvNo, fldEngNm: metaData.metaEngNm, parentFldNm: record.parentFldNm})) {
				this.popupService.simpleAlert(this.$scope, this.text.dupFieldNm);
				$input.value = '';
				grid.editChange.call(grid, $input, originalEvent.index, originalEvent.column, originalEvent);
				
				record.fldEngNm = '';
				grid.save();
				
				return;
			}
			
			if(column === 1) {
				$input.value = metaData.metaEngNm;
			}else if(column === 2) {
				$input.value = metaData.metaKorNm;
			}

			grid.editChange.call(grid, $input, originalEvent.index, originalEvent.column, originalEvent);

			if(column === 1) {
				!record.w2ui.changes && (record.w2ui.changes = {});
				record.w2ui.changes.fldKorNm = metaData.metaKorNm;
			}else if(column === 2) {
				record.w2ui.changes.fldEngNm = metaData.metaEngNm;
			}

			record.w2ui.changes.msgLen = metaData.metaLen;
			record.w2ui.changes.decimalLen = metaData.decimalLen;
			record.w2ui.changes.dataTypeNm = metaData.dataTypeNm;
			record.w2ui.changes.encYn = metaData.metaEncYn;
			record.w2ui.changes.alignNm = this.getAlignNmByDataType(metaData.dataTypeNm);
			record.w2ui.changes.confirmMeta = this.text.accord;
			grid.save();
		}
	}
	
	changeMsgDetailGridColumns(msg){
		if(!_.isEmpty(msg)){
			if ( msg.chlDscd == 'EXTERNAL' || (msg.trxDscd == 'BATCH' && this.isProFEP) ) {
				var columns = this._getFepMsgDetailGridColumns(msg);
			} else {
				var columns = this._getDefaultMsgDetailGridColumns(msg);
			}
		} else {
			var columns = this._getDefaultMsgDetailGridColumns(msg);
		}
		
		if(!_.isEmpty(msg)){
			switch(msg.msgDscd){
			case 'CH':
				if(msg.chlDscd === 'EXTERNAL'){
					columns.push(...[
					]);
				}
				break;
			case 'IV':
				if(msg.chlDscd === 'EXTERNAL'){
					columns.push(...[
					]);
				}
				break;
			}
		}

		this.msgDetailOptions.columns = columns;
	}

	
	_getFepMsgDetailGridColumns(msg = {}){
		return [
			{ field: 'msgSeq', caption: this.text.msgSeq, size: '40px', frozen: true},
			{ field: 'fldEngNm', caption: this.text.fldEngNm, size: '2%', frozen: true, attr:"align=left", 
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
			{ field: 'fldLvNo', caption: this.text.fldLvNo, size: '45px', frozen: true , style: 'border-right: 0.5px solid black', editable: this.isEdit ? {type: 'int'} : false },
			{ field: 'childDtoNm', caption: this.text.childDtoNm, size: '1%', editable: this.isEdit ? {type: 'text'} : false},
			{ field: 'arraySizeRefVal', caption:  this.text.arraySizeRefVal, size: this.user.locale === 'en'? '140px' : '1%', editable: this.isEdit ? {type: 'text'} : false},
			{ 
				field: 'privacyDscd', caption: this.text.privacyDscd, size: '1%', 
				editable: this.isEdit ? { type: 'select', items: this.getMaskCodes()} : false,
				render: (data,index,colIndex) => {
					const privacyDscd = w2ui[this.msgDetailOptions.name].getCellValue(index, colIndex);
					return this.getMaskNm(privacyDscd);
				}
			},
			{ 
				field: 'encYn', caption: this.text.encYn, size: '1%', 
				editable: {  type: 'select', items: this.gridService.getSelectItemsFromCodes(this.codes.YN_CD)},
			},	
			{ 
				field: 'paramType', caption: this.text.paramType, size: '80px',  
				editable: this.isEdit ? { 
					type: 'select', 
					items: this.gridService.getSelectItemsFromCodes(this.codes.PARAM_TYPE)
				} : false,
				render: (data,index,colIndex) => {
					const paramTypeNm = w2ui[this.msgDetailOptions.name].getCellValue(index, colIndex);
					return this.codeService.getCodeValNm(this.codes.PARAM_TYPE, paramTypeNm);
				}
			},		
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
					return data.fldRmk ? '<div title="' + data.fldRmk + '"><span>' + data.fldRmk + '</span></div>' : '';
				}
			},
			{ 
				field: 'confirmMeta', caption: this.text.metaValidate, size: this.user.locale === 'en'? '110px' : '60px',
				render: (data,index,colIndex) => {
					let confirmMeta = data.confirmMeta;
					
					if(data.confirmMeta === this.text.accord) {
						confirmMeta = '<span class="chr-c-green">' + confirmMeta + '</span>';
					}else if(data.confirmMeta === this.text.discord) {
						confirmMeta = '<span class="chr-c-orange">' + confirmMeta + '</span>';
					}
					
					return confirmMeta;
				}	
			}
		];
	}

	_getDefaultMsgDetailGridColumns(msg = {}){
		return [
			{ field: 'msgSeq', caption: this.text.msgSeq, size: '40px', frozen: true},
			{ field: 'fldEngNm', caption: this.text.fldEngNm, size: '2%', frozen: true, attr:"align=left", 
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
			{ field: 'fldLvNo', caption: this.text.fldLvNo, size: '45px', frozen: true , style: 'border-right: 0.5px solid black', editable: this.isEdit ? {type: 'int'} : false },
			{ field: 'childDtoNm', caption: this.text.childDtoNm, size: '1%', editable: this.isEdit ? {type: 'text'} : false},
			{ field: 'arraySizeRefVal', caption:  this.text.arraySizeRefVal, size: this.user.locale === 'en'? '140px' : '1%', editable: this.isEdit ? {type: 'text'} : false},
			{ 
				field: 'privacyDscd', caption: this.text.privacyDscd, size: '1%', 
				editable: this.isEdit ? { type: 'select', items: this.getMaskCodes()} : false,
				render: (data,index,colIndex) => {
					const privacyDscd = w2ui[this.msgDetailOptions.name].getCellValue(index, colIndex);
					return this.getMaskNm(privacyDscd);
				}
			},
			{ 
				field: 'encYn', caption: this.text.encYn, size: '1%', editable: false
			},	
			{ 
				field: 'paramType', caption: this.text.paramType, size: '80px',  
				editable: this.isEdit ? { 
					type: 'select', 
					items: this.gridService.getSelectItemsFromCodes(this.codes.PARAM_TYPE)
				} : false,
				render: (data,index,colIndex) => {
					const paramTypeNm = w2ui[this.msgDetailOptions.name].getCellValue(index, colIndex);
					return this.codeService.getCodeValNm(this.codes.PARAM_TYPE, paramTypeNm);
				}
			},		
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
					return data.fldRmk ? '<div title="' + data.fldRmk + '"><span>' + data.fldRmk + '</span></div>' : '';
				}
			},
			{ 
				field: 'confirmMeta', caption: this.text.metaValidate, size: this.user.locale === 'en'? '110px' : '60px',
				render: (data,index,colIndex) => {
					let confirmMeta = data.confirmMeta;
					
					if(data.confirmMeta === this.text.accord) {
						confirmMeta = '<span class="chr-c-green">' + confirmMeta + '</span>';
					}else if(data.confirmMeta === this.text.discord) {
						confirmMeta = '<span class="chr-c-orange">' + confirmMeta + '</span>';
					}
					
					return confirmMeta;
				}	
			}
		];
	}

	getGridData(goToFirst = false) {
		const { pageNumber, pageSize } = this.gridService.getPageInfo(this.select, this.pageNumber);
		let url = `/msglayouts?pageNumber=${ goToFirst ? 1 : pageNumber}&pageSize=${pageSize}`;

		this.httpService.get(url, this.searchParam).then(data => {
			const { msglayoutbsOutList: records, totalCnt: recordsCount } = data;
			
			this.msgOptions.records = records;
			this.msgOptions.recordsCount = recordsCount;
			
			if(!_.isEmpty(this.msgOptions.name)) {
				w2ui[this.msgOptions.name].selectNone();
			}
			
			if(goToFirst) {
				this.pageNumber = 1;
				this.$scope.$broadcast(`resetPage`, this.pageNumber);
			}
		});
	}
	
	getMsgDetailGridData(id) {
		let encodedId = encodeURIComponent(id);
		
		this.httpService.get(`/msglayouts/${encodedId}`).then(res => {
			this.setMsgDetailGridData(res);
		});
	}
	
	setMsgDetailGridData(res) {
		const utilService = this.utilService;
		const { msglayoutdtDto: originRecords } = res;
		
		this.selectedMsg = utilService.clone(res);
		this._selectedMsg = utilService.clone(res);
		
		this.setMsgOption();
		this.setRegDttm();
		
		if(!_.isEmpty(this.msgDetailOptions.name)) {
			w2ui[this.msgDetailOptions.name].selectNone();
		}

		const grid = w2ui[this.msgDetailOptions.name];
		grid.clear(true);
		
		this.msgDetailOptions.recordsCount = originRecords.length;
		
		this.msgDetailOptions.records = originRecords.map(record => {
			record.fldUnqId = record.fldUnqId ? record.fldUnqId : this.utilService.uniqueId('TEMP_LAYOUT_DD_DTO_ID');
			
			if(!record.fldLvNo) record.fldLvNo = 0;
			if(!record.dataTypeNm) record.dataTypeNm = 'STRING';
			if(!record.msgLen) record.msgLen = 0;
			if(!record.decimalLen) record.decimalLen = 0;
			
			return record;
		});
		
		this.calcLength(this.msgDetailOptions.records);
	}
	
	excelExport(id, multiDownload, isFirstFile, isLastFile){
		const data = {'msgLayoutId' : id};
		
		if(multiDownload) {
			isFirstFile && this.popupService.showLoadingBar(this.$scope);
		}else{
			this.popupService.showLoadingBar(this.$scope);
		}
		
		this.httpService.downloadFile(`/msglayouts/excelexport`, data).then(res => {
			if(res.isError){
				this.popupService.detailAlert(this.$scope, res.data.message, res.data.stackTrace);
			}else{
				var header = res.headers('Content-Disposition');
				var fileName = header.split("=")[1].replace(/\"/gi,'');
				
				var blob = new Blob([res.data], {type:"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"});
				
				this.utilService.saveFile(blob, fileName);
			}
		})
		.finally(() => {
			if(multiDownload) {
				isLastFile && this.popupService.closeLoadingBar();
			}else{
				this.popupService.closeLoadingBar();
			}
		});
	}
	
	pageBtnClick(num) {
		this.pageNumber = num;
		this.getGridData(num === 1);
	}
	
	search(){
		this.getGridData(true);
		this.resetDetail();
	}
	
	resetSearch(){
		this.searchParam = {};
		this.getGridData(true);
		this.resetDetail();
	}
	
	blur($event){
		$event.target.blur();
	}
	
	add() {
		if(!_.isEmpty(this.msgOptions.name)) {
			w2ui[this.msgOptions.name].selectNone();
		}
		
		this.resetDetail();
		this.isAdd = true;
		this.selectedMsg._msgLayoutId = this.utilService.uniqueId('TEMP_MSG_LAYOUT_ID')
		this.selectedMsg.sharedYn = 'N';
		this.selectedMsg.custApiYn = 'N';
		this.selectedMsg.workStatusCd = 'WORKING';
		this.selectedMsg.regManId = this.user.userId;

		this._onEdit();
	}
	
	save() {
		const grid = w2ui[this.msgDetailOptions.name];
		
		// 영문명(변수명)이나 영문명 FullName을 수정하다가 바로 저장시, 수정된 데이터를 저장
		if(this._onEditEvent) {
			if(this._onEditEvent.column === 1) {
				this._onEditEvent && (grid.set(this._onEditEvent.recid, {fldEngNm : this._onEditEvent.input.val()}));
			}else if(this._onEditEvent.column === 2) {
				this._onEditEvent && (grid.set(this._onEditEvent.recid, {fldKorNm : this._onEditEvent.input.val()}));
			}
		}
		grid.save();
		
		if(!this._checkValid(this.selectedMsg, grid.records)) return;
		
		this.popupService.showLoadingBar(this.$scope);
		
		if(this.selectedMsg.custApiYn === 'N' || this.selectedMsg.custApiYn === undefined){
			if(!this.metaValidate(true)) {
				this.popupService.closeLoadingBar();
				return;
			}
		}

		const msg = this.utilService.clone(this.selectedMsg);
		const depthArray = grid.records.map(v => v.fldLvNo);
		let preRecord = null; 
		
		msg.msglayoutdtDto = grid.records.map((layoutDtDto, index) => {
			layoutDtDto.msgLayoutId = msg.msgLayoutId;
			layoutDtDto.msgVersion = msg.msgVersion;
			
			if(layoutDtDto.fldLvNo == 0){
				layoutDtDto.fldUnqId = `${msg.msgLayoutId}.${layoutDtDto.fldEngNm}`;
				delete layoutDtDto.parentFldNm;
			} else if(Number(preRecord.fldLvNo) + 1 == layoutDtDto.fldLvNo){
				layoutDtDto.fldUnqId = `${preRecord.fldUnqId}.${layoutDtDto.fldEngNm}`;
				layoutDtDto.parentFldNm = preRecord.fldUnqId;
			} else if(preRecord.fldLvNo == layoutDtDto.fldLvNo){
				layoutDtDto.fldUnqId = `${preRecord.parentFldNm}.${layoutDtDto.fldEngNm}`;
				layoutDtDto.parentFldNm = preRecord.parentFldNm;
			} else if(Number(preRecord.fldLvNo) > layoutDtDto.fldLvNo) {
				for(let i = index - 1 ; i > -1 ; i--){
					if(depthArray[i] == layoutDtDto.fldLvNo){
						const sibling = grid.records[i];
						layoutDtDto.fldUnqId = `${sibling.parentFldNm}.${layoutDtDto.fldEngNm}`;
						layoutDtDto.parentFldNm = sibling.parentFldNm;
						break;
					}
				}
			}

			preRecord = layoutDtDto;
			
			return layoutDtDto;
		});
		
		var msgLength = this.calcLength();
		msg.rsrvFldVal3 = msgLength; 
		
		const q = this.isAdd  
			? this.httpService.post('/msglayouts', msg)
			: this.httpService.put('/msglayouts', msg);
		
		q.then(res => {
			this.popupService.closeLoadingBar();
			
			if (res.isError) {
				this.popupService.detailAlert(this.$scope, res.data.message, res.data.stackTrace, res.data.parameters);
				return;	
			}
			
			this.popupService.simpleAlert(this.$scope, bxMsg('common.saved'));
			this.getGridData();
//			this.getMsgDetailGridData(this.selectedMsg.msgLayoutId);
			
			if(this.isAdd){
				this.getMsgDetailGridData(res.msgLayoutId);
			}else{
				this.getMsgDetailGridData(this.selectedMsg.msgLayoutId);
			}
				
			this.offEditMode();
			this._selectedMsg = this.utilService.clone(this.selectedMsg);
		});
		
		q.finally(() => {
			this.popupService.closeLoadingBar();
		});
	}
	
	tempSave(){
		const grid = w2ui[this.msgDetailOptions.name];
		grid.save();
		
		for(let i = 0; i < grid.records.length; i++){
			const msglayout = grid.records[i];
			
			// 영문명
			if(_.isEmpty(msglayout.fldEngNm)){
				this.popupService.simpleAlert(this.$scope, this.text.order + ' [' + msglayout.msgSeq + '] ' + this.text.emptyFldEngNm);
				return false;
			}
		}
		
		if(!this.selectedMsg.msgLayoutId){
			this.popupService.simpleAlert(this.$scope, this.text.emptyMsgLayoutId);
			return false;
		}
		
		this.popupService.showLoadingBar(this.$scope);
		
		const msg = this.utilService.clone(this.selectedMsg);
		const depthArray = grid.records.map(v => v.fldLvNo);
		let preRecord = null;
		
		msg.msglayoutdtDto = grid.records.map((layoutDtDto, index) => {
			layoutDtDto.msgLayoutId = msg.msgLayoutId;
			layoutDtDto.msgVersion = msg.msgVersion;
			
			if(layoutDtDto.fldLvNo == 0){
				layoutDtDto.fldUnqId = `${msg.msgLayoutId}.${layoutDtDto.fldEngNm}`;
				delete layoutDtDto.parentFldNm;
			} else if(Number(preRecord.fldLvNo) + 1 == layoutDtDto.fldLvNo){
				layoutDtDto.fldUnqId = `${preRecord.fldUnqId}.${layoutDtDto.fldEngNm}`;
				layoutDtDto.parentFldNm = preRecord.fldUnqId;
			} else if(preRecord.fldLvNo == layoutDtDto.fldLvNo){
				layoutDtDto.fldUnqId = `${preRecord.parentFldNm}.${layoutDtDto.fldEngNm}`;
				layoutDtDto.parentFldNm = preRecord.parentFldNm;
			} else if(Number(preRecord.fldLvNo) > layoutDtDto.fldLvNo) {
				for(let i = index - 1 ; i > -1 ; i--){
					if(depthArray[i] == layoutDtDto.fldLvNo){
						const sibling = grid.records[i];
						layoutDtDto.fldUnqId = `${sibling.parentFldNm}.${layoutDtDto.fldEngNm}`;
						layoutDtDto.parentFldNm = sibling.parentFldNm;
						break;
					}
				}
			}

			preRecord = layoutDtDto;
			
			return layoutDtDto;
		});
		
		var msgLength = this.calcLength();
		msg.rsrvFldVal3 = msgLength; 
		
		const q = this.isAdd  
			? this.httpService.post('/msglayoutstemp', msg)
			: this.httpService.put('/msglayoutstemp', msg);
		
		q.then(res => {
			this.popupService.closeLoadingBar();
			
			if (res.isError) {
				this.popupService.detailAlert(this.$scope, res.data.message, res.data.stackTrace, res.data.parameters);
				return;	
			}
			
			this.popupService.simpleAlert(this.$scope, bxMsg('common.tempSaved'));
			this.getGridData();
			
			if(this.isAdd){
				this.getMsgDetailGridData(res.msgLayoutId);
			}else{
				this.getMsgDetailGridData(this.selectedMsg.msgLayoutId);
			}
				
			this.offEditMode();
			this._selectedMsg = this.utilService.clone(this.selectedMsg);
		});
		
		q.finally(() => {
			this.popupService.closeLoadingBar();
		});
	}
	
	calcLength(records){
		let record;
		
		if(!records) {
			w2ui[this.msgDetailOptions.name].save();
			record = this.utilService.clone(w2ui[this.msgDetailOptions.name].records);
		}else{
			record = this.utilService.clone(records);
		}
		
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
		
		this.msgLength = msgLen;
		
		return this.msgLength;
	}
	
	_isCreateMsgLayout(){
		return _.isEmpty(this._selectedMsg);
	}
	
	deleteGridData(grid = {}, id = '') {
		let encodedId = encodeURIComponent(id);
			
		this.httpService.delete(`/msglayouts/${encodedId}`)
			.then(data => {
				if (data.isError) {
					this.popupService.simpleAlert(this.$scope, data.data.message);
					return;	
				}
				
				this.resetDetail();
				this.getGridData();
			});
	}
	
	change() {
		const pageSize = this.select.pageSize;
		this.msgOptions.limit = pageSize;
		this.gridHeight = this.gridService.getGridHeight(this.select.pageSize);
		this.pageBtnClick(1);
		this.msgOptions.name && w2ui[this.msgOptions.name].focus();
	}
	
	resetDetail() {
		this.selectedMsg = {};
		this._selectedMsg = {};
		this._selectedDetailRow = {};
		this.msgLength = '';
		this.isAdd = false;
		
		this.offEditMode();
		this.setMsgOption();
		if(!_.isEmpty(this.msgDetailOptions.name)){
			this.msgDetailOptions.records = [];
		}
	}
	
	onEditMode(){
		if(_.isEmpty(this.selectedMsg)) return;
		this._onEdit();
	}
	
	_onEdit(){
		const editData = this.selectedData;
		
		if(this.user.roleId != 'Administrator' && editData && editData.msgDscd == 'STH'){
			this.popupService.simpleAlert(this.$scope, this.text.modifyCommonHeader);
			return;
		}
		
		const $forms = this._isCreateMsgLayout()
			? $('#searchWrap').find('div:not(.disabled) > input,textarea')
			: $('#searchWrap').find('div.editable > input,textarea');
		
			const $editable = $('#editData').find('select');
			
			this.isEdit = true;
			
			$forms.attr('disabled', false);
			$editable.attr('disabled', false);
		
		_.isEmpty(this.msgDetailOptions) 
			? this.initMsgDetailGridOptions()
			: this.changeMsgType();
	}
	
	offEditMode(){
		const $forms = $('#searchWrap').find('input,textarea,select');
		const $editable = $('#editData').find('select');
		
		this.isEdit = false;
		this.isAdd = false;
		
		$forms.attr('disabled', true);
		$editable.attr('disabled', true);
		
		_.isEmpty(this.msgDetailOptions)
			? this.initMsgDetailGridOptions()
			: this.changeMsgType();
	}
	
	openConfirm(grid, recid) {
		let record = grid.get(recid);
		
		if(record.regManId !== this.user.userId) {
			this.popupService.simpleAlert(this.$scope, this.text.delRegister);
			return;
		}
		
		this.popupService.simpleConfirm(this.$scope,
				this.text.confirmTextDelete,
				()=>this.deleteGridData(grid, recid));
	}
	checkLang(name) {
		var result = false;
		for(var i = 0; i < name.length; i++) {
			var c = name.charCodeAt(i);
			
			if( (0xAC00 <= c && c <=0xD8A3) || (0x3131 <= c && c <=0X318E) ) {
				// 한글
				result = false;
				break;
			} else if ( (c >= 0x4E00 && c <= 0x9FBF) || (c >= 0x3400 && c <= 0x4DBF) || (c >= 0x20000 && c <= 0x2A6DF) 
			|| (c >= 0x2A700 && c <= 0x2A6DF) || (c >= 0x2B740 && c <= 0x2B8AF) || (c >= 0x2CEB0&& c <= 0x2EBEF) || (c >= 0x2E80 && c <= 0x2EFF)
			|| (c >= 0x2F800 && c <= 0x2FA1F) )  {
				// 중문
				result = false;
				break;
			} else {
				result = true;
			}
		}
		
		return result;
	}	
	
	_checkValid(msg, records){
		if(!msg.msgLayoutId){
			this.popupService.simpleAlert(this.$scope, this.text.emptyMsgLayoutId);
			return false;
		}else if(!msg.chlDscd){
			this.popupService.simpleAlert(this.$scope, this.text.emptyChlDscd);
			return false;
		}else if(!msg.msgNm){
			this.popupService.simpleAlert(this.$scope, this.text.emptyMsgName);
			return false;
		} else if(!msg.trxDscd){
			this.popupService.simpleAlert(this.$scope, this.text.emptyTrxDscd);
			return false;
		} else if(!msg.msgDscd){
			this.popupService.simpleAlert(this.$scope, this.text.emptyMsgType);
			return false;
		} else if(msg.chlDscd == 'EXTERNAL' && (msg.msgVersion === undefined || msg.msgVersion === null)){
			this.popupService.simpleAlert(this.$scope, this.text.emptyMsgVersion);
			return false;
		}  else if(!msg.jobId && msg.chlDscd == 'EXTERNAL' && msg.trxDscd === 'BATCH'){
			this.popupService.simpleAlert(this.$scope, this.text.emptyFileId);
			return false;
		} else if(!msg.lv1Cd){
			this.popupService.simpleAlert(this.$scope, this.text.emptyLvCdsStr);
			return false;
		} else if(records && records[0] && records[0].fldLvNo != 0){
			this.popupService.simpleAlert(this.$scope, this.text.firstFieldDepth);
			return false;
		} else if(records.length === 0){
			this.popupService.simpleAlert(this.$scope, this.text.emptyLayout);
			return false;
		} 

		if (!this.checkLang(msg.msgNm)) {
			this.popupService.simpleAlert(this.$scope, this.text.msgNmEng);
			return false;
		} 

		let pre = null;

		for(let i = 0; i < records.length; i++){
			const msglayout = records[i];
			

			// 영문명
			if(_.isEmpty(msglayout.fldEngNm)){
				this.popupService.simpleAlert(this.$scope, this.text.order + ' [' + msglayout.msgSeq + '] ' + this.text.emptyFldEngNm);
				return false;
			}
			
			// 한글명 
			if(_.isEmpty(msglayout.fldKorNm)){
				this.popupService.simpleAlert(this.$scope, this.text.order + ' [' + msglayout.msgSeq + '] ' + this.text.emptyFldKorNm);
				return false;
			}
			
			// 타입
			if(_.isEmpty(msglayout.dataTypeNm)){
				this.popupService.simpleAlert(this.$scope, this.text.order + ' [' + msglayout.msgSeq + '] ' + this.text.emptyDataTypeNm);
				return false;
			}
			
			// 길이
			if(msglayout.msgLen == undefined || _.isEmpty(String(msglayout.msgLen))){
				this.popupService.simpleAlert(this.$scope, this.text.order + ' [' + msglayout.msgSeq + '] ' + this.text.emptyMsgLen);
				return false;
			}
			
			// Depth
			if(msglayout.fldLvNo == undefined || _.isEmpty(String(msglayout.fldLvNo))){
				this.popupService.simpleAlert(this.$scope, this.text.order + ' [' + msglayout.msgSeq + '] ' + this.text.emptyDepth);
				return false;
			}

			//마스킹정보 체크(복사 후 등록 시 제대로 된 코드 값이 안들어감)
			var checkPrivacy = this.getMaskNm(msglayout.privacyDscd);
			if(!checkPrivacy) {
				this.popupService.simpleAlert(this.$scope, this.text.order + ' [' + msglayout.msgSeq + '] ' + this.text.empthPrivacyDscd);
				return false;
			}
			
			if(pre && msglayout.fldLvNo - pre.fldLvNo > 1){
				this.popupService.simpleAlert(this.$scope, this.text.order + ' [' + msglayout.msgSeq + '] ' + this.text.increaseDepth);
				return false;
			}
			pre = msglayout;
			
			// 배열참조
			/*
			if(msglayout.dataTypeNm === 'LAYOUT') {
				if(msglayout.childDtoNm == undefined || msglayout.childDtoNm == ''){
					this.popupService.simpleAlert(this.$scope,"Layout 타입에 하위IO명을 입력해주세요.");
					return false;
				}
			}
			*/

			if(msglayout.dataTypeNm === 'LAYOUT' && msglayout.arraySizeRefVal != undefined && !_.isEmpty(String(msglayout.arraySizeRefVal))){
				let isString = isNaN(Number(msglayout.arraySizeRefVal));
				
				if(isString) {
					// 문자열인 경우 해당 필드가 존재하는지 확인
					// 문자열인 경우 해당 필드가 INTEGER 타입인지 확인
					
					let record = records.filter(v => v.fldEngNm === msglayout.arraySizeRefVal);
					
					if(record.length === 0){
						this.popupService.simpleAlert(this.$scope, this.text.order + ' [' + msglayout.msgSeq + '] ' + this.text.arraySizeRefValMsg3);
						return false;
					}else{
						if(record[0].dataTypeNm !== 'INTEGER') {
							this.popupService.simpleAlert(this.$scope, this.text.order + ' [' + msglayout.msgSeq + '] ' + this.text.arraySizeRefValMsg4);
							return false;
						}
					}
				}else{
					// 상수인 경우 1보다 큰지 확인
					let num = Number(msglayout.arraySizeRefVal);
					
					if(num < 1) {
						this.popupService.simpleAlert(this.$scope, this.text.order + ' [' + msglayout.msgSeq + '] ' + this.text.arraySizeRefValMsg2);
						return false;
					}
				}
			}
		}

		let duplicatedFieldResult = this.checkDuplicatedFieldName(records);
		
		if(duplicatedFieldResult.isDuplicated){
			this.popupService.simpleAlert(this.$scope, this.text.order + ' [' + duplicatedFieldResult.duplicatedSeq1 + ',  ' + duplicatedFieldResult.duplicatedSeq2 + '] ' + this.text.orderDupFieldNm);
			return false;
		}
		
		let wrongTypeResult = this.checkLayoutType(records);
		
		if(wrongTypeResult.isWrong){
			this.popupService.simpleAlert(this.$scope, this.text.order + ' [' + wrongTypeResult.wrongSeq + '] ' + this.text.orderDataTypeLayout);
			return false;
		}
		
		return true;
	}
	
	checkDuplicatedFieldName(records){
		let result = {
			isDuplicated: false,
			duplicatedSeq1: null,
			duplicatedSeq2: null
		};
		
		records.map((record) => this.setParentFldNm(record));
		
		let i;
		let j;
		let length = records.length;
		
		for(i = 0; i < length; i++) {
			let record = records[i];
			
			if(result.isDuplicated) {
				break;
			}
			
			for(j = 0; j < length; j++) {
				let compareRecord = records[j];
				
				if(record.msgSeq !== compareRecord.msgSeq){
					
					if(record.fldLvNo == compareRecord.fldLvNo
					 && record.parentFldNm === compareRecord.parentFldNm
					 && record.fldEngNm === compareRecord.fldEngNm){
						result = {
							isDuplicated: true,
							duplicatedSeq1: record.msgSeq,
							duplicatedSeq2: compareRecord.msgSeq
						};
							
						break;
					}
				}
			}
		}
		
		return result;
	}
	
	checkLayoutType(records) {
		let result = {
				isWrong: false,
				wrongSeq: null,
			};
		let grid = w2ui[this.msgDetailOptions.name];
		

		let i;
		let length = records.length;
		
		for(i = 0; i < length; i++) {
			let record = records[i];
			
			if(record.fldLvNo != 0 && record.parentFldNm) {
				let parentRecord = grid.records.filter(v => v.fldUnqId == record.parentFldNm)[0];
				
				if(parentRecord && parentRecord.dataTypeNm !== 'LAYOUT') {
					result = {
						isWrong: true,
						wrongSeq: parentRecord.msgSeq,
					};
					
					break;
				}
			}
		}
		
		return result;
	}
	
	addDetailRow(){
		if(!this.isEdit) return;
		this.gridService.addRecord(this.msgDetailOptions, this.generatorLayoutdtDto);
		this.reorderSeq(true);
	}
	
	deleteDetailRow(){
		if(!this.isEdit) return;
		this.gridService.removeSelected(this.msgDetailOptions);
		this._selectedDetailRow = {};
		this.reorderSeq();
	}
	
	upDetailRow(){
		if(!this.isEdit) return;
		this.gridService.upSelected(this.msgDetailOptions);
		this.reorderSeq();
	}
	
	downDetailRow(){
		if(!this.isEdit) return;
		this.gridService.downSelected(this.msgDetailOptions);
		this.reorderSeq();
	}
	
	reorderSeq(clear){
		if(_.isEmpty(this.msgDetailOptions.name)) return;
		const grid = w2ui[this.msgDetailOptions.name];
		let gridRecords = grid.records;
		let $record = $(grid.box).find('.w2ui-grid-records');
		var scrollTop = $record.scrollTop();
		
		if(clear && grid.records.length >= 150) {
			grid.clear();
		}
		
		gridRecords.map((record, index) => {
			record.msgSeq = index + 1;
		});
		grid.refresh();
		
		if(clear) {
			setTimeout(()=>{
				grid.scrollIntoView(scrollTop);
			}, 300);
		}
	}
	
	setMsgOption(){
		if(_.isEmpty(this.selectedMsg)) {
			this.msgTypes = [];
			return;
		}
		
		switch(this.selectedMsg.chlDscd){
			case 'INTERNAL':
				switch(this.selectedMsg.trxDscd){
				case 'ONLINE':
					this.msgTypes = this.allMsgType.filter(v => v.cdVal == 'IV' || v.cdVal == 'STH' );
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
		
		this.changeMsgType();
	}
	
	changeMsgType(){
		this.changeMsgDetailGridColumns(this.selectedMsg);
	}
	
	xlsImport(){
		this.popupService.openModal('SCR0503')
						 .then((res)=>{
							 let data = res.data;

							 this.setMsgDetailGridData(data.msglayoutbsDto);
							 this.offEditMode();

							 this.getGridData(true);
						 })
						 .catch(()=>{});
	}
	
	xlsExport() {
		const grid = w2ui[this.msgOptions.name];
		const selections = grid.getSelection();
		const selectionLength = selections.length;
		
		if(selectionLength === 0){
			this.popupService.simpleAlert(this.$scope, this.text.selectMsg);
			return;
		}
		
		selections.map((msgLayoutId, idx) => {
			setTimeout(() => {
				this.excelExport(msgLayoutId, true, idx === 0, selectionLength === idx + 1);	
			}, idx * 500);
		});
	}
	
	impactAnalysis(){
		if(_.isEmpty(this.selectedMsg)) return;
		this.popupService.openModal('SCR1802', { msgLayoutId: this.selectedMsg.msgLayoutId })
						 .then(()=>{})
						 .catch(()=>{});
	}
		
	metaValidate(isNotShowConfirmMsg){
		const grid = w2ui[this.msgDetailOptions.name];
		this.validateCnt = 0;
		this.noncoreCnt  = 0;
		
		grid.save();
		grid.records.map(v => this.rowMetaValid(v));
		
		if(this.noncoreCnt !== 0) {
			this.popupService.simpleAlert(this.$scope, this.text.noncoreTermValidate);
			return false;
		}

		if(this.validateCnt !== 0) {
			let txt = this.text.discordMetaValidate;
			(this.validateCnt !== 0) && (txt += '<br>' + this.text.discord + ' <span class="chr-c-orange">' + this.validateCnt + '</span>' + this.text.cnt);
			this.popupService.simpleAlert(this.$scope, txt);
		}else{
			if(!isNotShowConfirmMsg) {
				this.popupService.simpleAlert(this.$scope, this.text.completeMetaValidate);
			}
		}
			
		return this.validateCnt === 0;
	}
	
	rowMetaValid(row, validateCnt, noncoreCnt){
		let meta = this.metaService.getMetaListSameKorNm(row.fldKorNm)[0];
		
		if(row.dataTypeNm === 'LAYOUT') {
			row.confirmMeta = this.text.excludeTarget;
		}else{
			//
			if((meta &&  meta.metaEngNm == row.fldEngNm &&  meta.metaKorNm == row.fldKorNm && 
				meta.metaLen == row.msgLen &&  meta.decimalLen == row.decimalLen &&  meta.dataTypeNm == row.dataTypeNm && row.dataTypeNm !== 'BYTEARRAY') 
				|| 
			   (meta && meta.metaEngNm == row.fldEngNm && meta.metaKorNm == row.fldKorNm && 
				meta.decimalLen == row.decimalLen && meta.dataTypeNm == row.dataTypeNm && row.dataTypeNm === 'BYTEARRAY')) {
				row.encYn = meta.metaEncYn;
				row.confirmMeta = this.text.accord;
				if(!this.isNoncoreSystem) {
					if(meta.metaTermType == 'N') {
						this.noncoreCnt++;
					}
				}
			}else{  
				///불일치일때 숫자로 끝나면 숫자 빼고 다시 메타 검증
								
				let matchesFldKorNm = row.fldKorNm ? row.fldKorNm.match(/\d+$/) : null;
				let matchesFldEngNm = row.fldEngNm ? row.fldEngNm.match(/\d+$/) : null; 
				
				if(matchesFldKorNm && matchesFldEngNm && matchesFldKorNm[0] === matchesFldEngNm[0]) {
					meta = this.metaService.getMetaListSameKorNm(row.fldKorNm.substr(0, row.fldKorNm.indexOf(matchesFldKorNm[0])))[0];
					
					if((meta &&  meta.metaEngNm == row.fldEngNm.substr(0, row.fldEngNm.indexOf(matchesFldEngNm[0])) &&  meta.metaKorNm == row.fldKorNm.substr(0, row.fldKorNm.indexOf(matchesFldKorNm[0])) && 
						meta.metaLen == row.msgLen && meta.decimalLen == row.decimalLen && meta.dataTypeNm == row.dataTypeNm && row.dataTypeNm !== 'BYTEARRAY') 
						|| 
					   (meta && meta.metaEngNm == row.fldEngNm.substr(0, row.fldEngNm.indexOf(matchesFldEngNm[0])) && meta.metaKorNm == row.fldKorNm.substr(0, row.fldKorNm.indexOf(matchesFldKorNm[0])) && 
						meta.decimalLen == row.decimalLen && meta.dataTypeNm == row.dataTypeNm && row.dataTypeNm === 'BYTEARRAY')) {
							row.confirmMeta = this.text.accord;
					}else{
						this.validateCnt ++;
						row.confirmMeta = this.text.discord;
					}
				}else{
					this.validateCnt ++;
					row.confirmMeta = this.text.discord;
				}
			}
		}
	}
	
	setParentFldNm(record, isFldLvNoChanged) {
		const grid = w2ui[this.msgDetailOptions.name];
		
		let index = grid.records.indexOf(record);
		let fldLvNo = isFldLvNoChanged ? record.w2ui.changes.fldLvNo : record.fldLvNo;
		
		if(fldLvNo == 0) {
			record.parentFldNm = null;
			return;
		}
		
		for(let i = index; i >= 0; i--) {
			let tempRecord = grid.records[i];
			
			if(tempRecord === record){
				continue;
			}
			
			if(tempRecord.fldLvNo == fldLvNo - 1) {
				record.parentFldNm = tempRecord.fldUnqId;
				break;
			}
		}
		
	}
	
	fieldNameValid(row){
		let grid = w2ui[this.msgDetailOptions.name];
		
		if(_.isEmpty(row.fldEngNm)) {
			return true;
		}
		
		let records = grid.records.filter(record => {
			if(row.fldLvNo === 0) {
				return (row.fldLvNo === record.fldLvNo) && (row.fldEngNm === record.fldEngNm);
			}else {
				return (row.fldLvNo === record.fldLvNo) && (row.parentFldNm === record.parentFldNm) && (row.fldEngNm === record.fldEngNm);
			}
		});
		
		if(records.length > 0) return false;
		return true;
	}
	
	openApplicationCodeModal(){
		if(!this.isEdit) return;
		this.popupService.openModal('SCR1402')
						 .then((code) => {
							this.selectedMsg.lv1Cd = code.appCd;
						 })
						 .catch(()=>{});
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
	
	openMsglayoutPopup(){
		if(!this.isEdit) return;
		this.popupService.openModal('SCR0502', { getDetail: true, multiSelect: false })
						 .then(msglayout => this.msgDetailOptions.records = msglayout[0].msglayoutdtDto )
						 .catch(()=>{});
	}

	openMsgLayoutIdCreatePopup(){
		this.popupService.openModal('SCR0504')
						.then( addMsg => {
							 
							this.selectedMsg.intrfcId = addMsg.intrfcId;
							this.selectedMsg.lv1Cd = addMsg.lv1Cd;
							this.selectedMsg.chlDscd = addMsg.chlDscd;
							this.selectedMsg.trxDscd = addMsg.trxDscd;
							this.selectedMsg.msgDscd = addMsg.msgDscd;
							this.selectedMsg.rsrvFldVal1 = addMsg.msgLayoutId.substring(1,4);
							this.selectedMsg.rsrvFldVal2 = addMsg.msgLayoutId.substring(4,7);
							this.selectedMsg.msgVersion = addMsg.msgVersion;
							this.selectedMsg.jobId = addMsg.fileId;
							this.selectedMsg.msgDataVal = addMsg.msgDataVal;
							this.selectedMsg.msgLayoutId = addMsg.msgLayoutId;
							 
							if( this.proFepSysCd.includes(this.selectedMsg.rsrvFldVal1) || this.proFepSysCd.includes(this.selectedMsg.rsrvFldVal2)) {
								this.isProFEP = true;
							} else {
								this.isProFEP = false;
							}

							// 송수신 시스템이 논코어 시스템 이면 isNoncoreSystem = true
							this.isNoncoreSystem = false;	
							
							this.noncoreSys.map(records => {	
								if( records.sysCd.includes(this.selectedMsg.rsrvFldVal1) || records.sysCd.includes(this.selectedMsg.rsrvFldVal2)){
									this.isNoncoreSystem = true;			
								} 
							});

							this.setMsgOption();
							 
						})
             			.catch(()=>{});
	};
	
	setRegDttm(){
		const regDttm = this.selectedMsg.regDttm 
		
		if(regDttm) {
			this.selectedMsg.regDttmString = this.utilService.setRegDttm(regDttm);
		}
	}
	
	refreshGrid(){
		const grid =  w2ui[this.msgOptions.name];
		grid.refresh();
	}
	
	syncMeta(){
		this.popupService.showLoadingBar(this.$scope);
		this.metaService.getMeta()
			.finally(() => {
				this.popupService.closeLoadingBar();
				this.popupService.simpleAlert(this.$scope, this.text.completeSyncMeta);
			});
	}
	
	syncField(){
		let grid = w2ui[this.msgDetailOptions.name];
		grid.save();
		
		this.msgDetailOptions.records.map((record) => {
			if(record.dataTypeNm === 'LAYOUT') {
				this.rowMetaValid(record);
			}else{
				let metaData = this.metaService.getMetaListSameKorNm(record.fldKorNm);
				
				if(metaData.length === 1) {
					metaData = metaData[0];

					record.fldEngNm = metaData.metaEngNm;
					record.dataTypeNm !== 'BYTEARRAY' && (record.msgLen = metaData.metaLen);
					record.decimalLen = metaData.decimalLen;
					record.dataTypeNm = metaData.dataTypeNm;
					record.alignNm = this.getAlignNmByDataType(metaData.dataTypeNm);
					record.confirmMeta = this.text.accord;
				}else{
					this.rowMetaValid(record);
				}	
			}
		});
		
		grid.refresh();
		this.popupService.simpleAlert(this.$scope, this.text.completeSyncField);
	}
}

module(App.name).controller('SCR0501Controller', SCR0501Controller);