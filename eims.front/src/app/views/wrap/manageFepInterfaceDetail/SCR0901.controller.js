import { module } from 'angular';
import App from '../../../app';

class SCR0901Controller2 {
	

	constructor ($scope, $state, $compile, $timeout, popupService,httpService, utilService, 
				gridService,codeService, metaService, userService, codes){
		this.$scope = $scope;
		this.$state = $state;
		this.$compile = $compile;
		this.$timeout = $timeout;
		this.httpService = httpService;
		this.utilService = utilService;
		this.gridService = gridService;
		this.codeService = codeService;
		this.popupService = popupService;
		this.metaService = metaService;
		this.userService = userService;
		this.codes = utilService.clone(codes);
		this.user = this.userService.getUser();
		
		this.intrfcTypeCd = 'FEP';
		this.useRequestMsgScroll = true;
		this.useResponseMsgScroll = true;
		
		this.initZabara();
		this.initText();	
		this.initSelect();
		this.initGrid();
		this.initGenerate();
		this.initScroll();

		let count = 0;
		
		this.$scope.$on(`gridRendered`, () => {
			count ++;
			count === 12 && this.initPrevData();
		});
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
				this.inferfaceMainGrid.limit = prevScope.select.pageSize;
				this.gridHeight = prevScope.gridHeight;
				
				// 그리드
				this.inferfaceMainGrid.records = prevScope.inferfaceMainGrid.records;
				this.inferfaceMainGrid.recordsCount = prevScope.inferfaceMainGrid.recordsCount;
				
				this.$timeout(() => {
					this.pageNumber = prevScope.pageNumber;
					this.$scope.$broadcast(`resetPage`, this.pageNumber);
				});
				
				if(prevScope.detail && Object.keys(prevScope.detail).length > 0) {
					// 상세
					this.detail = prevScope.detail;
					
					prevScope.interfaceDetailGrid && (this.interfaceDetailGrid.records = prevScope.interfaceDetailGrid.records);
					prevScope.sndReqLayoutGrid && (this.sndReqLayoutGrid.records = prevScope.sndReqLayoutGrid.records);
					prevScope.sndResLayoutGrid && (this.sndResLayoutGrid.records = prevScope.sndResLayoutGrid.records);
					prevScope.rcvReqLayoutGrid && (this.rcvReqLayoutGrid.records = prevScope.rcvReqLayoutGrid.records);
					prevScope.rcvResLayoutGrid && (this.rcvResLayoutGrid.records = prevScope.rcvResLayoutGrid.records);
					prevScope.sndMsgMapSrcGrid && (this.sndMsgMapSrcGrid.records = prevScope.sndMsgMapSrcGrid.records);
					prevScope.sendMsgMapTrgGrid && (this.sendMsgMapTrgGrid.records = prevScope.sendMsgMapTrgGrid.records);
					prevScope.rcvMsgMapSrcGrid && (this.rcvMsgMapSrcGrid.records = prevScope.rcvMsgMapSrcGrid.records);
					prevScope.rcvMsgMapTrgGrid && (this.rcvMsgMapTrgGrid.records = prevScope.rcvMsgMapTrgGrid.records);
					prevScope.deployTargetSysGrid && (this.deployTargetSysGrid.records = prevScope.deployTargetSysGrid.records);
					prevScope.refHistoryGrid && (this.refHistoryGrid.records = prevScope.refHistoryGrid.records);
					
					this.isOnline = prevScope.isOnline;
					this.isBatch = prevScope.isBatch;
					
					this.editIntrfcDetailGrid(prevScope.isEdit);
					
					// 아코디언 
					this.$timeout(() => {
						this.zabara = prevScope.zabara;
						
						if(this.zabara) {
							for(let key in this.zabara){
								if(this.zabara.hasOwnProperty(key) && this.zabara[key]) {
									let $button = $('#' + key + 'Zabara'),
									$parent = $button.closest('section'),
									$body = $('#' + key);
								
									$parent.css('background', '#ababab').css('color', '#fff');
									$button.css('transform', '').css('color', '#fff');
									$body.show();
								}
							}
						}
						
						$('#main-contents').scrollTop(prevScope.scrollTop);
					});

					// 시스템명 
					prevScope.detail.sendSysCd && this.setSysCd('SEND', prevScope.detail.sendSysCd, prevScope.detail.sendSysNm);
					prevScope.detail.receiveSysCd && this.setSysCd('RECEIVE', prevScope.detail.receiveSysCd, prevScope.detail.receiveSysNm);
				}
				
				// 수정모드
				this.isAdd = prevScope.isAdd;
				if(prevScope.isEdit){
					this.onEditMode();
				}else{
					this.offEditMode();
				}
			}
			
			if(param.data){
				this.searchParam = {};
				this.searchParam.intrfccoms = { intrfcId: param.data.intrfcId, msgLayoutId: param.data.msgLayoutId };
				this.getIntrfccoms(true);
				
				let data = param.data;
				let intrfcId = data.intrfcId;
				let deployVersion = data.deployVersion;
				let url = `/intrfccoms/deploy/${intrfcId}?intrfcId=${intrfcId}&deployVersion=${deployVersion}`;
				
				this.httpService.get(url).then(data => {
					this.setIntrfAccordion(data);
					this._openIntrfaceDetailZabara();
					this.offEditMode();
				});	
			}
		}else{
			this.initSearch();
		}
		
		this.$scope.$on('$destroy', () => {
			this.scrollTop = $('#main-contents').scrollTop();
			this.utilService.setParams(currentStateName, {scope: this.$scope});
			
			$(window).off('resize.09012');
		});
	}
	
	initScroll() {
		$('#sndMsgMapSrcGrid').on('scroll', () => {
			if(this.useRequestMsgScroll) {
				$('#sendMsgMapTrgGrid').find('.w2ui-grid-records').scrollTop($('#sndMsgMapSrcGrid').find('.w2ui-grid-records').scrollTop());
			}
		});
		$('#sendMsgMapTrgGrid').on('scroll', () => {
			if(this.useRequestMsgScroll) {
				$('#sndMsgMapSrcGrid').find('.w2ui-grid-records').scrollTop($('#sendMsgMapTrgGrid').find('.w2ui-grid-records').scrollTop());
			}
		});
		$('#rcvMsgMapSrcGrid').on('scroll', () => {
			if(this.useResponseMsgScroll) {
				$('#rcvMsgMapTrgGrid').find('.w2ui-grid-records').scrollTop($('#rcvMsgMapSrcGrid').find('.w2ui-grid-records').scrollTop());
			}
		});
		$('#rcvMsgMapTrgGrid').on('scroll', () => {
			if(this.useResponseMsgScroll) {
				$('#rcvMsgMapSrcGrid').find('.w2ui-grid-records').scrollTop($('#rcvMsgMapTrgGrid').find('.w2ui-grid-records').scrollTop());
			}
		});
	}
	
	initZabara() {
		this.zabara = {};
	}
	
	initText() {
		this.text = $.extend({}, bxMsg.getMessages('common'), bxMsg.getMessages('manageFepInterface'));
	}
	
	initSelect() {
		this.select = this.gridService.getSelect(this.codes['GRID_PAGE_SIZE'][1].cdVal);
	}
	
	initSearch(){
		this.searchParam = {};
		this.resetSearch();
	}
	
	initGenerate(){
		this.intfcDto = (rowData = {}) => {
			rowData.id = this.utilService.uniqueId('TEMP_INTFC_DETAIL');
			return rowData;
		}
		
		this.sndReqDto = (rowData = {}) => {
			rowData.id = this.utilService.uniqueId('TEMP_SND_REQ');
			return rowData;
		}
	}
	
	initGrid() {
		this.gridHeight = this.gridService.getGridHeight(this.select.pageSize);
		this.inferfaceMainGrid = {
			limit: this.select.pageSize,
			pageSize: this.select.pageSize,
			recordsCount: 0,
			recid: 'intrfcId',
			show: {columnHeaders:true, selectColumn : true},
			columns: [
				{ field: 'intrfcId', caption: this.text.intrfcId, size: '1.5%', sortable: true},
				{ field: 'intrfcNm', caption: this.text.intrfcNm, size: '2.5%', sortable: true, attr: 'align=left' },
				{ 
					field: 'lvCds', caption: this.text.lvCds, size: '0.7%', sortable: true,
					render: (data) => {
						return data.lv1Cd ? data.lv1Cd : '';
					}
				},
				{ 
					field: 'trxDscd', caption: this.text.trxDscd2, size: this.user.locale === 'en'? '110px' : '0.5%',
					render: (data) => {		
						return this.codeService.getCodeValNm('TRAN_DSCD', data.trxDscd);
					}	
				},
				{ field: 'sysCdS', caption: this.text.sysCdS, size: this.user.locale === 'en'? '90px' : '0.5%'},
				{ field: 'sysCdR', caption: this.text.sysCdR, size: this.user.locale === 'en'? '95px' : '0.5%'},
				{ field: 'instCd', caption: this.text.instCd, size: this.user.locale === 'en'? '100px' : '0.5%'},
				{ 
					caption: this.text.reqResMsgNo, size: this.user.locale === 'en'? '210px' :'0.7%',
					render: (data) =>{
						let renderText = '';
						let rqstExtrnlMsgNo = data.rqstExtrnlMsgNo ? data.rqstExtrnlMsgNo : '';
						let rspsExtrnlMsgNo = data.rspsExtrnlMsgNo ? data.rspsExtrnlMsgNo : '';
						
						if(data.rqstExtrnlMsgNo || data.rspsExtrnlMsgNo) {
							renderText = rqstExtrnlMsgNo + '/' + rspsExtrnlMsgNo;
						}

						return renderText;
					}
				},
				{ field: 'regManId', caption: this.text.regManId, size: '0.7%'},
				{ 
					field: 'workStatusCd', caption: this.text.workStatusCd, size: this.user.locale === 'en'? '120px' : '0.5%',
					render: (data) => {		
						return this.codeService.getCodeValNm('WORK_STATUS_CD', data.workStatusCd);
					}
				},
				{ field: 'regDttm', caption: this.text.regDttm, size: this.user.locale === 'en'? '110px' : '0.5%',
					render: (data) =>{
						const regDttm = data.regDttm 
						const yy = regDttm.substring(0,4);
						const mm = regDttm.substring(4,6);
						const dd = regDttm.substring(6,8);
						return yy+"/"+mm+"/"+dd;
					}	
				}
			],
			onClick: (e) => {
				const grid =  w2ui[this.inferfaceMainGrid.name];
				const eTarget = e.originalEvent.target;
				const recid = e.recid;
				const editData = grid.get(e.recid);
				
				this.pdfInterfaceId = recid;
				
				if($(eTarget).attr('data-action') == 'export'){
					this.excelExport(editData.intrfcId);	
					e.preventDefault();
					return;
				}
				
				if(eTarget.localName === 'button'){
					const action = $(eTarget).attr('data-action');
					if(action === 'edit'){
						// prevent deselect
						let selection = w2ui[e.target].getSelection();
						if(selection.length === 1 && selection[0] === e.recid && !_.isEmpty(this.detail)) {
							e.preventDefault();
							this._onEdit();
							this.$scope.$apply();
						}else{
							this.setIntrfGridData(recid, false, () => {
								this._onEdit();
							});
							this._openIntrfaceDetailZabara();
						}
						
						return;
					} else if (action === 'delete') {
						let record = grid.get(recid);
						
						if(record.regManId !== this.user.userId) {
							this.popupService.simpleAlert(this.$scope, this.text.deleteMsg);
						}else{
							this.popupService.simpleConfirm(this.$scope, this.text.confirmDelete,
									()=>this.deleteInfrcId(recid));
						}
						
						e.preventDefault();
						return;
					} else if (action === 'copy'){
						this.add();
						this.setIntrfGridData(recid, true);
						
						return;
					}
				} else {
					this.offEditMode();
				}
				
				this.$scope.$apply();
				
				// prevent deselect
				let selection = w2ui[e.target].getSelection();
				if(selection.length === 1 && selection[0] === e.recid) {
					e.preventDefault();

					if(!_.isEmpty(this.detail)){
						return;
					}
				}
				
				this.setIntrfGridData(recid);
				this._openIntrfaceDetailZabara();
			}
		};
		
		this.interfaceDetailGrid = {
			limit: 999999,
			pageSize: 999999,
			recordsCount: 0,
			multiSelect: false,
			recid: 'id',
			onClick: (e) => {
				// prevent deselect
				let selection = w2ui[e.target].getSelection();
				if(selection.length === 1 && selection[0] === e.recid) {
					e.preventDefault();
				}
				
				
				if(!this.isEdit) return;
				const grid = w2ui[e.target];
				const eTarget = e.originalEvent.target;
				
				if (eTarget.localName === 'button') {
					const action = $(eTarget).attr('data-action');
					switch(action) {
					case 'sys-search':
						this.popupService.openModal('SCR1302')
						   .then(sysCode => {
							   const rowData = grid.get(e.recid);
							   rowData.sysCd = sysCode.sysCd;
							   rowData.sysNm = sysCode.sysNm;
						   })
						   .catch(()=>{});
						break;
					case 'delete':
						this.popupService.simpleConfirm(this.$scope, this.text.confirmDelete,
								  ()=>grid.remove(e.recid));
						break;
					}
				} else {
					this._selectedIntrfcmsglayoutdtDto = {
						grid: grid,
						editData: grid.get(e.recid)
					};
				}
			},
			onUnselect: (e) => {
				if(_.isEmpty(this._selectedIntrfcmsglayoutdtDto)) return;
				if(e.recid === this._selectedIntrfcmsglayoutdtDto.editData.recid) {
					this._selectedIntrfcmsglayoutdtDto = {};
				}
			},
			onEditField: (e) =>{
				
				if(!this.isEdit) e.preventDefault();
				
				const grid = w2ui[e.target];
				const record = grid.get(e.recid);
				
				if(this.detail.trxDscd == 'ONLINE' && e.column == 4 && record.srTypeCd == "SEND"){
					e.preventDefault();
				}
				
			},
			onChange: (e) => {
				const grid = w2ui[e.target];
				e.onComplete = () => {
					grid.save();
				}
			}
		};
		this.editIntrfcDetailGrid(false);
		
		this.sndReqLayoutGrid = {
			limit: 999999,
			pageSize: 999999,
			multiSelect: false,
			columns: this.getSndRecColumns(),
			recordsCount: 0,
			recid: 'msgLayoutId',
			onClick: (e) => {
				// prevent deselect
				let selection = w2ui[e.target].getSelection();
				if(selection.length === 1 && selection[0] === e.recid) {
					e.preventDefault();
				}
				
				
				const grid = w2ui[e.target];
				const eTarget = e.originalEvent.target;
				const recid = e.recid;
				
				if(eTarget.localName === 'button'){
					switch($(eTarget).attr('data-action')){
					case 'more':
						this.popupService.openModal('SCR0704', {msgLayoutId: recid, codes: this.codes})
										 .then(()=>{})
										 .catch(()=>{});
						break;
					case 'diff':
						const msglayout = grid.get(recid);

						this.popupService.openModal('SCR0706', {
											intrfcId: msglayout.intrfcId,
											msgLayoutId: recid,
											srTypeCd: msglayout.srTypeCd,
											rqstRspsTypeCd: msglayout.rqstRspsTypeCd,
											layoutSeq: msglayout.rqstRspsSeq
										 })
										 .then(()=>{})
										 .catch(()=>{});
						break;
					case 'delete':
						if(!this.isEdit) return;
						grid.remove(recid);
						this.setIoCopy();
						
						this.sndMsgMapSrcGrid.records = this.sndMsgMapSrcGrid.records
															.filter(v => v.msgLayoutId != recid);
						this.sendMsgMapTrgGrid.records.map(v => {
							if(v.mappingTypeCd === 'PROPT' 
								&& v.srcData
								&& v.srcData.indexOf(recid) !== -1){
								v.srcData = '';
							}
						});
						
						this.setGridSeq();
						this.$scope.$apply();
						break;
					}
				}
			}
		};
		
		this.sndResLayoutGrid = {
			limit: 999999,
			pageSize: 999999,
			multiSelect: false,
			columns: this.getSndRecColumns(),
			recordsCount: 0,
			recid: 'msgLayoutId',
			onClick: (e) => {
				// prevent deselect
				let selection = w2ui[e.target].getSelection();
				if(selection.length === 1 && selection[0] === e.recid) {
					e.preventDefault();
				}
				
				
				const grid = w2ui[e.target];
				const eTarget = e.originalEvent.target;
				const recid = e.recid;
				
				if(eTarget.localName === 'button'){
					switch($(eTarget).attr('data-action')){
					case 'more':
						this.popupService.openModal('SCR0704', {msgLayoutId: recid, codes: this.codes})
										 .then(()=>{})
										 .catch(()=>{});
						break;
					case 'diff':
						const msglayout = grid.get(recid);

						this.popupService.openModal('SCR0706', {
											intrfcId: msglayout.intrfcId,
											msgLayoutId: recid,
											srTypeCd: msglayout.srTypeCd,
											rqstRspsTypeCd: msglayout.rqstRspsTypeCd,
											layoutSeq: msglayout.rqstRspsSeq
										 })
										 .then(()=>{})
										 .catch(()=>{});
						break;
					case 'delete':
						if(!this.isEdit) return;
						grid.remove(e.recid);
						this.setIoCopy();
						
						this.rcvMsgMapTrgGrid.records = this.rcvMsgMapTrgGrid.records.filter(v => v.msgLayoutId !== recid);
						this.$scope.$apply();
						break;
					}
				}
			},
		};
		
		this.rcvReqLayoutGrid = {
			limit: 999999,
			pageSize: 999999,
			multiSelect: false,
			columns: this.getSndRecColumns(),
			recordsCount: 0,
			recid: 'msgLayoutId',
			onClick: (e) => {
				// prevent deselect
				let selection = w2ui[e.target].getSelection();
				if(selection.length === 1 && selection[0] === e.recid) {
					e.preventDefault();
				}
				
				
				const grid = w2ui[e.target];
				const eTarget = e.originalEvent.target;
				const recid = e.recid;
				
				if(eTarget.localName === 'button'){
					switch($(eTarget).attr('data-action')){
					case 'more':
						this.popupService.openModal('SCR0704', {msgLayoutId: recid, codes: this.codes})
										 .then(()=>{})
										 .catch(()=>{});
						break;
					case 'diff':
						const msglayout = grid.get(recid);

						this.popupService.openModal('SCR0706', {
											intrfcId: msglayout.intrfcId,
											msgLayoutId: recid,
											srTypeCd: msglayout.srTypeCd,
											rqstRspsTypeCd: msglayout.rqstRspsTypeCd,
											layoutSeq: msglayout.rqstRspsSeq
										 })
										 .then(()=>{})
										 .catch(()=>{});
						break;
					case 'delete':
						if(!this.isEdit) return;
						grid.remove(e.recid);
						
						this.sendMsgMapTrgGrid.records = this.sendMsgMapTrgGrid.records.filter(v => v.msgLayoutId !== recid);
						this.$scope.$apply();
						break;
					}
				}
			},
		};
		
		this.rcvResLayoutGrid = {
			limit: 999999,
			pageSize: 999999,
			columns: this.getSndRecColumns(),
			recordsCount: 0,
			recid: 'msgLayoutId',
			onClick: (e) => {
				// prevent deselect
				let selection = w2ui[e.target].getSelection();
				if(selection.length === 1 && selection[0] === e.recid) {
					e.preventDefault();
				}
				
				
				const grid = w2ui[e.target];
				const eTarget = e.originalEvent.target;
				const recid = e.recid;
				
				if(eTarget.localName === 'button'){
					switch($(eTarget).attr('data-action')){
					case 'more':
						this.popupService.openModal('SCR0704', {msgLayoutId: recid, codes: this.codes})
										 .then(()=>{})
										 .catch(()=>{});
						break;
					case 'diff':
						const msglayout = grid.get(recid);

						this.popupService.openModal('SCR0706', {
											intrfcId: msglayout.intrfcId,
											msgLayoutId: recid,
											srTypeCd: msglayout.srTypeCd,
											rqstRspsTypeCd: msglayout.rqstRspsTypeCd,
											layoutSeq: msglayout.rqstRspsSeq
										 })
										 .then(()=>{})
										 .catch(()=>{});
						break;
					case 'delete':
						if(!this.isEdit) return;
						grid.remove(e.recid);
						
						this.rcvMsgMapSrcGrid.records = this.rcvMsgMapSrcGrid.records
															.filter(v => v.msgLayoutId != recid);
						this.rcvMsgMapTrgGrid.records.map(v => {
							if(v.mappingTypeCd === 'PROPT' 
								&& v.srcData
								&& v.srcData.indexOf(recid) !== -1){
								v.srcData = '';
							}
						});
						
						this.setGridSeq();
						this.$scope.$apply();
						break;
					}
				}
			},
		};
		
		this.sndMsgMapSrcGrid = {
			limit: 999999,
			pageSize: 999999,
			columns: this.getMsgSrcColumns(),
			records: [],
			recordsCount: 0,
			recid: 'fldUnqId',
			onClick: (e) => {
				// prevent deselect
				let selection = w2ui[e.target].getSelection();
				if(selection.length === 1 && selection[0] === e.recid) {
					e.preventDefault();
				}
			}
		};
		
		this.rcvMsgMapSrcGrid = {
			limit: 999999,
			pageSize: 999999,
			columns: this.getMsgSrcColumns(),
			records: [],
			recordsCount: 0,
			recid: 'fldUnqId',
			onClick: (e) => {
				// prevent deselect
				let selection = w2ui[e.target].getSelection();
				if(selection.length === 1 && selection[0] === e.recid) {
					e.preventDefault();
				}
			}
		};
		
		this.sendSrcDataOptions = [];
		this.sendMsgMapTrgSrcData = { 
			field: 'srcData', 
			caption: this.text.mappingData,
			attr : 'align=left',
			size: '3%',
			editable : { type: 'select', items: this.sendSrcDataOptions } 
		};
		
		this.sendMsgMapTrgGrid = {
			limit: 999999,
			pageSize: 999999,
			records: [],
			recordsCount: 0,
			recid: 'fldUnqId',
			onClick: (e) =>{
				// prevent deselect
				let selection = w2ui[e.target].getSelection();
				if(selection.length === 1 && selection[0] === e.recid) {
					e.preventDefault();
				}
				
				const changeDot = e.recid.replace(/\./g, '\\.');
				$(`tr#grid_${e.target}_rec_${changeDot}`).removeClass('bg-red');
				
				if(!this.isEdit){
					e.preventDefault();
					return;
				}
				
				if(e.column === 10){
					const record = w2ui[e.target].get(e.recid);
					let mappingTypeCd = record.mappingTypeCd;
					
					if(record.w2ui && record.w2ui.changes && record.w2ui.changes.mappingTypeCd){
						mappingTypeCd = record.w2ui.changes.mappingTypeCd;
					}
					
					switch(mappingTypeCd){
					case 'PROPT':
						this.sendSrcDataOptions.length = 0;
						this.sendSrcDataOptions.push(...this.sndMsgMapSrcGrid.records.map((v) => {
								return {id: v.fldUnqId, text: '[' + v.gridSeq + '] ' + v.fldUnqId};
							}	
						));
						this.sendMsgMapTrgSrcData.editable.type = 'select';
						break;
					case 'CONST':
						this.sendMsgMapTrgSrcData.editable.type = 'text';
						break;
					case 'NONE':
						e.preventDefault();
						break;
					case 'SYSDATE':
						this.sendSrcDataOptions.length = 0;
						const sysDateList = this.codeService.getCodeValList('SYS_DATE_CD');
						for (var i = 0; i < sysDateList.length; i++) {
							this.sendSrcDataOptions.push(sysDateList[i]);	
						}
						this.sendMsgMapTrgSrcData.editable.type = 'select';
						break;
					}
				}
				
			},
			onEditField: (e) => {
				if(!this.isEdit){
					e.preventDefault();
					return;
				}
				
				if(e.column === 10){
					const record = w2ui[e.target].get(e.recid);
					let mappingTypeCd = record.mappingTypeCd;
					
					if(record.w2ui && record.w2ui.changes && record.w2ui.changes.mappingTypeCd){
						mappingTypeCd = record.w2ui.changes.mappingTypeCd;
					}
					
					switch(mappingTypeCd){
					case 'PROPT':
						this.sendSrcDataOptions.length = 0;
						this.sendSrcDataOptions.push(...this.sndMsgMapSrcGrid.records.map((v) => {
								return {id: v.fldUnqId, text: '[' + v.gridSeq + '] ' + v.fldUnqId};
							}	
						));
						this.sendMsgMapTrgSrcData.editable.type = 'select';
						break;
					case 'CONST':
						this.sendMsgMapTrgSrcData.editable.type = 'text';
						break;
					case 'NONE':
						e.preventDefault();
						break;
					case 'SYSDATE':
						this.sendSrcDataOptions.length = 0;
						const sysDateList = this.codeService.getCodeValList('SYS_DATE_CD');
						for (var i = 0; i < sysDateList.length; i++) {
							this.sendSrcDataOptions.push(sysDateList[i]);	
						}
						this.sendMsgMapTrgSrcData.editable.type = 'select';
						break;
					}
				}
			},
			onChange: (e)=>{
				const grid = w2ui[e.target];
				const record = grid.get(e.recid);
			
				if(e.column === 9){
					record.srcData = '';
					record.mappingTypeCd = e.value_new;
					record.w2ui && record.w2ui.changes && delete record.w2ui.changes.srcData;
					grid.refreshCell(e.recid, 'srcData');
				}else if(e.column === 10) {
					record.srcData = e.value_new;
				}
			}
		};
		
		this.recvSrcDataOptions = [];
		this.recvMsgMapTrgSrcData = { 
			field: 'srcData', 
			caption: this.text.mappingData,
			attr : 'align=left',
			size: '3%',
			editable : { type: 'select', items: this.recvSrcDataOptions } 
		};
		
		this.rcvMsgMapTrgGrid = {
			limit: 999999,
			pageSize: 999999,
			recordsCount: 0,
			records: [],
			recid: 'fldUnqId',
			onClick: (e) =>{
				// prevent deselect
				let selection = w2ui[e.target].getSelection();
				if(selection.length === 1 && selection[0] === e.recid) {
					e.preventDefault();
				}
				
				if(!this.isEdit){
					e.preventDefault();
					return;
				}
				
				if(e.column === 10){
					const record = w2ui[e.target].get(e.recid);
					let mappingTypeCd = record.mappingTypeCd;
					
					if(record.w2ui && record.w2ui.changes && record.w2ui.changes.mappingTypeCd){
						mappingTypeCd = record.w2ui.changes.mappingTypeCd;
					}
					
					switch(mappingTypeCd){
					case 'PROPT':
						this.recvSrcDataOptions.length = 0;
						this.recvSrcDataOptions.push(...this.rcvMsgMapSrcGrid.records.map((v) => {
								return {id: v.fldUnqId, text: '[' + v.gridSeq + '] ' + v.fldUnqId};
							}	
						));
						this.recvMsgMapTrgSrcData.editable.type = 'select';
						break;
					case 'NONE':
						e.preventDefault();
						break;
					case 'CONST':
						this.recvMsgMapTrgSrcData.editable.type = 'text';
						break;
					case 'SYSDATE':
						this.recvSrcDataOptions.length = 0;
						const sysDateList = this.codeService.getCodeValList('SYS_DATE_CD');
						for (var i = 0; i < sysDateList.length; i++) {
							this.recvSrcDataOptions.push(sysDateList[i]);	
						}
						this.recvMsgMapTrgSrcData.editable.type = 'select';
						break;
					}
				}
				
				const changeDot = e.recid.replace(/\./g, '\\.');
				$(`tr#grid_${e.target}_rec_${changeDot}`).removeClass('bg-red');
			},
			onEditField: (e) => {
				if(!this.isEdit){
					e.preventDefault();
					return;
				}
				
				if(e.column === 10){
					const record = w2ui[e.target].get(e.recid);
					let mappingTypeCd = record.mappingTypeCd;
					
					if(record.w2ui && record.w2ui.changes && record.w2ui.changes.mappingTypeCd){
						mappingTypeCd = record.w2ui.changes.mappingTypeCd;
					}
					
					switch(mappingTypeCd){
					case 'PROPT':
						this.recvSrcDataOptions.length = 0;
						this.recvSrcDataOptions.push(...this.rcvMsgMapSrcGrid.records.map((v) => {
								return {id: v.fldUnqId, text: '[' + v.gridSeq + '] ' + v.fldUnqId};
							}	
						));
						this.recvMsgMapTrgSrcData.editable.type = 'select';
						break;
					case 'NONE':
						e.preventDefault();
						break;
					case 'CONST':
						this.recvMsgMapTrgSrcData.editable.type = 'text';
						break;
					case 'SYSDATE':
						this.recvSrcDataOptions.length = 0;
						const sysDateList = this.codeService.getCodeValList('SYS_DATE_CD');
						for (var i = 0; i < sysDateList.length; i++) {
							this.recvSrcDataOptions.push(sysDateList[i]);	
						}
						this.recvMsgMapTrgSrcData.editable.type = 'select';
						break;
					}
				}
			},
			onChange: (e)=>{
				const grid = w2ui[e.target];
				const record = grid.get(e.recid);
			
				if(e.column === 9){
					record.srcData = '';
					record.mappingTypeCd = e.value_new;
					record.w2ui && record.w2ui.changes && delete record.w2ui.changes.srcData;
					grid.refreshCell(e.recid, 'srcData');
				}else if(e.column === 10) {
					record.srcData = e.value_new;
				}
			}
		};
		
		this.initMsgTrgColumns();
		
		this.deployTargetSysGrid = {
			limit: 999999,
			pageSize: 999999,
			recordsCount: 0,
			recid: 'deploySysCd',
			columns: [
				{ field: 'deploySysCd', caption: this.text.sysCd, size: '1%'},
				{ field: 'deploySysNm', caption: this.text.sysNm, size: '1%', attr: 'align=left' },
				{ field: 'deployUrl', caption: this.text.deployUrl, size: '1%', attr: 'align=left' },
				{ 
					field: 'deployResultCd', caption: this.text.deployResultCd, size: '1%',
					render: (data)=> {
						if(data.deployResultCd === 'FAIL') {
							return '<span class="chr-c-orange">' + data.deployResultCd + '</span>'
						}
						
						return data.deployResultCd;
					}
				},
				{ 
					field: 'edit', caption: this.text.edit, size: '1%',
					render: (data)=> {
						if(data.deploySysCd === 'E2E' || data.deploySysCd === 'RSH' || data.deploySysCd === 'SIM') {
							return '';
						}
						
						return `
							<button type="button" class="bw-btn bxd bxd-trash" title="${this.text.delete}" data-action="delete"></button>
						`;
					}
				}
			],
			onClick: (e) => {
				// prevent deselect
				let selection = w2ui[e.target].getSelection();
				if(selection.length === 1 && selection[0] === e.recid) {
					e.preventDefault();
				}
				
				
				const grid = w2ui[e.target];
				const eTarget = e.originalEvent.target;
				
				if (eTarget.localName === 'button') {
					if(this.isEdit) grid.remove(e.recid);
				} else {
					this.selectedIntrfcdeploysysdtDto = grid.get(e.recid);
					this.$scope.$apply();
				}
			},
			onUnselect: (e) => {
				this.selectedIntrfcdeploysysdtDto = {};
			}
		};
		
		this.refHistoryGrid = {
			limit: 999999,
			pageSize: 999999,
			recordsCount: 0,
			recid: 'deployDttm',
			columns: [
				{ field: 'id', caption: '', hidden: true},
				{ 
					field: 'deployVersion', caption: this.text.deployVersion, size: '1%',
					render: (data)=> {
						let txt = '-';
						
						if(data.deployResultCd.indexOf('SUCCESS') !== -1) {
							txt = data.deployVersion;
						}
						
						return txt;
					}
				},
				{ 
					caption: this.text.deployDscd, size: '1%',
					render: (data)=> {
						let txt = '';
						
						if(data.deployResultCd.indexOf('SUCCESS') !== -1) {
							if(data.deployResultCd.indexOf('_') !== -1) {
								txt = this.text.redeploy;
							}else{
								txt = this.text.deploy;
							}
						}else{
							if(data.deployResultCd.indexOf('_') !== -1) {
								txt = this.text.redeployFail;
							}else{
								txt = this.text.deployFail;
							}
						}
						
						return txt;
					}
				},
				{ 
					caption: this.text.prevDeploy, size: '1%',
					render: (data)=> {
						let deployResultCd = data.deployResultCd;
						let idx = deployResultCd.indexOf('_')
						let version = (idx === -1) ? '-' : deployResultCd.substr(idx + 1);
						
						return version;
					}
				},
				{ 
					field: 'deployDttm', caption: this.text.deployDttm, size: '1%',
					render: (data)=> {
						return this.utilService.setRegDttm(data.deployDttm);
					}
				},
				{ field: 'deploySysCd', caption: this.text.deploySysCd, size: '1%'},
				{ 
					caption: this.text.deployResultDetail, size: this.user.locale === 'en'? '160px' : '100px',
					render: (data)=> {
						return '<button type="button" class="bw-btn bxd bxd-search2" data-action="result"></button>';
					}
				}
			],
			onClick: (e) => {
				// prevent deselect
				let selection = w2ui[e.target].getSelection();
				if(selection.length === 1 && selection[0] === e.recid) {
					e.preventDefault();
				}
				
				
				const grid = w2ui[this.refHistoryGrid.name];
				const eTarget = e.originalEvent.target;
				const recid = e.recid;
				const editData = grid.get(e.recid);
				
				if (eTarget.localName === 'button') {
					switch($(eTarget).attr('data-action')){
					case 'more':
						this.utilService.openTab(this.$scope, {
							state: 'main.manageFepInterfaceDetail',
							label: this.text.titleHeader
						}, {
							data: {
								intrfcId: this.detail.intrfcId,
								deployVersion: editData.deployVersion
							}
						});
						break;
					case 'result':
						this.popupService.openModal('SCR0708', {
							reqData: {
								intrfcId: this.detail.intrfcId,
								deployVersion: editData.deployVersion,
								deployDttm: editData.deployDttm,
								deployResultCd: editData.deployResultCd
							}
						 });
						break;
					case 'redeploy':
						this.popupService.simpleConfirm(this.$scope, this.text.confirmRedeploy,
								()=>this.redeployInterface(grid.get(e.recid)));
						break;
					}
				}
			},
			onDblClick: (e) => {
				const grid = w2ui[this.refHistoryGrid.name];
				const recid = e.recid;
				const editData = grid.get(recid);
				
				this.popupService.openModal('SCR0708', {
					reqData: {
						intrfcId: this.detail.intrfcId,
						deployVersion: editData.deployVersion,
						deployDttm: editData.deployDttm,
						deployResultCd: editData.deployResultCd
					}
				 });
			}
		};
	}
	
	editIntrfcDetailGrid(isEdit = true){
		let columns = [
			{ 
				field: 'srTypeCd', caption: this.text.srTypeCd, size: '1.5%', sortable: true,
				render: (data) => {		
					return this.codeService.getCodeValNm('SENC_RECV_DSCD', data.srTypeCd);
				}
			},
			{ 
				field: 'sysCd', caption: this.text.sysCd, size: '1.5%',
				render: (data, index) =>{
					if(index < 2) return data.sysCd;
					else return `${data.sysCd || ""}<button type="button" class="bw-btn bxd bxd-search" data-action="sys-search"></button>`;
				}
			},
			{ field: 'sysNm', caption: this.text.sysNm, size: '1.5%' },
			{ field: 'crgManNm', caption: '<i class="asterisk"></i>' + this.text.crgManNm, size: '1.5%',editable: {type:'text'} },
			{ field: 'trxCd', caption: '<i class="asterisk"></i>' + this.text.trxCd, size: '1%',editable: {type:'text'} },
		];
		
		if(this.isOnline){
			columns.splice(4, 1, { 
				field: 'trxCd', caption: '<i class="asterisk"></i>' + this.text.trxCd, size: '1%',editable: {type:'text'}				
			});
		}else if(this.isBatch){
//			columns.splice(4, 1, { 
//				field: 'filePath', caption: this.text.filePath, size: '2%', attr: 'align=left',
//				editable: {type:'text'}
//			});
			
			columns.splice(4, 1);
		}
		
		this.interfaceDetailGrid.columns = columns;
	}
	
	getSndRecColumns(){
		return [
			{
				field: 'rqstRspsSeq', caption: this.text.seq, size: '60px', render: (data, index) => index+1
			},
			{ field: 'msglayoutbsDto.msgLayoutId', caption: this.text.msgLayoutId, size: '1.5%' },
			{ field: 'msglayoutbsDto.msgNm', caption: this.text.msgNm, size: '1.5%', attr: 'align=left' },
			{ 
				field: 'msglayoutbsDto.trxDscd', caption: this.text.trxDscd, size: '1%',
				render: (data,index,colIndex) => {		
					let trxDscd;
					
					if(data && data.msglayoutbsDto && data.msglayoutbsDto.trxDscd) {
						trxDscd = data.msglayoutbsDto.trxDscd;
					}
					
					return this.codeService.getCodeValNm('TRAN_DSCD', trxDscd);
				}
			},
			{ 
				field: 'msglayoutbsDto.msgDscd', caption: this.text.msgDscd, size: '1%',
				render: (data,index,colIndex) => {		
					let msgDscd;
					
					if(data && data.msglayoutbsDto && data.msglayoutbsDto.msgDscd) {
						msgDscd = data.msglayoutbsDto.msgDscd;
					}
					
					return this.codeService.getCodeValNm('MSG_TYPE', msgDscd);
				}
			},
			{ 
				field: 'msglayoutbsDto.workStatusCd', caption: this.text.workStatusCd, size: '1%', sortable: true,
				render: (data,index,colIndex) => {
					let workStatusCd;
					
					if(data && data.msglayoutbsDto && data.msglayoutbsDto.workStatusCd) {
						workStatusCd = data.msglayoutbsDto.workStatusCd;
					}
					
					return this.codeService.getCodeValNm('WORK_STATUS_CD', workStatusCd);
				}
			},
			{ 
				field: 'more', caption: this.text.showDetail, size: '1%',
				render: (data)=> {
					return `
						<button type="button" class="bw-btn bxd bxd-zoom-in" data-action="more"></button>
					`;
				}
			},
			{ 
				field: 'diff', caption: this.text.msgDiff, size: '1%',
				render: (data)=> {
					return `
						<button type="button" class="bw-btn bxd bxd-split-vertical" title="${this.text.msgDiff}" data-action="diff"></button>
					`;
				}
			},
			{ 
				field: 'delete', caption: this.text.edit, size: '1%',
				render: (data)=> {
					return `
						<button type="button" class="bw-btn bxd bxd-trash" data-action="delete"></button>
					`;
				}
			},
		];
	}
	
	getMsgSrcColumns() {
		return  [
			{ field: 'gridSeq', caption: this.text.seq, size: '50px' },
			{ field: 'msgLayoutId', caption: this.text.msgLayoutId, size: '1.2%' },
			{ field: 'fldEngNm', caption: this.text.fldEngNm, size: '2%', attr: 'align=left' },
			{ field: 'fldKorNm', caption: this.text.fldKorNm, size: '1.3%', attr: 'align=left' },
			{ field: 'dataTypeNm', caption: this.text.dataTypeNm, size: '1%' },
			{ field: 'fldLvNo', caption: this.text.fldLvNo, size: '50px' },
			{ field: 'msgLen', caption: this.text.msgLen, size: '50px' },
		];
	}
	
	initMsgTrgColumns(){
		this.sendMsgMapTrgGrid.columns = [
			{ field: 'msgLayoutId', caption: this.text.msgLayoutId, size: '1.2%' },
			{ field: 'fldEngNm', caption: this.text.fldEngNm, size: '2%', attr: 'align=left' },
			{ field: 'fldKorNm', caption: this.text.fldKorNm, size: '1.3%', attr: 'align=left' },
			{ field: 'dataTypeNm', caption: this.text.dataTypeNm, size: '1%' },
			{ field: 'fldLvNo', caption: this.text.fldLvNo, size: '50px' },
			{ field: 'msgLen', caption: this.text.msgLen, size: '50px' },
			{ 
				field: 'mappingTypeCd', caption: this.text.mappingTypeCd, size: '1%',
				editable : { type: 'select', items: this.gridService.getSelectItemsFromCodes(this.codes.MAPPING_DSCD) },
				render: (data,index,colIndex) => {
					const mappingTypeCd = w2ui[this.sendMsgMapTrgGrid.name].getCellValue(index, colIndex);
					return this.codeService.getCodeValNm(this.codes.MAPPING_DSCD, mappingTypeCd);
				}
			},
			this.sendMsgMapTrgSrcData
		];
		
		this.rcvMsgMapTrgGrid.columns = [
			{ field: 'msgLayoutId', caption: this.text.msgLayoutId, size: '1.2%' },
			{ field: 'fldEngNm', caption: this.text.fldEngNm, size: '2%', attr: 'align=left' },
			{ field: 'fldKorNm', caption: this.text.fldKorNm, size: '1.3%', attr: 'align=left' },
			{ field: 'dataTypeNm', caption: this.text.dataTypeNm, size: '1%' },
			{ field: 'fldLvNo', caption: this.text.fldLvNo, size: '50px' },
			{ field: 'msgLen', caption: this.text.msgLen, size: '50px' },		
			{ 
				field: 'mappingTypeCd', caption: this.text.mappingTypeCd, size: '1%',
				editable : { type: 'select', items: this.gridService.getSelectItemsFromCodes(this.codes.MAPPING_DSCD) },
				render: (data,index,colIndex) => {
					const mappingTypeCd = w2ui[this.rcvMsgMapTrgGrid.name].getCellValue(index, colIndex);
					return this.codeService.getCodeValNm(this.codes.MAPPING_DSCD, mappingTypeCd);
				}
			},
			this.recvMsgMapTrgSrcData
		];
	}
	
	getIntrfccoms(goToFirst = false){
		const httpService = this.httpService;
		const { pageNumber, pageSize } = this.gridService.getPageInfo(this.select, this.pageNumber);	
		let url = `/intrfccoms?pageNumber=${ goToFirst ? 1 : pageNumber}&pageSize=${pageSize}&intrfcTypeCd=${this.intrfcTypeCd}`;
		
		httpService.get(url, this.searchParam.intrfccoms).then(res => {
			const { intrfccombsOutList: records, totalCnt: recordsCount } = res;
			
			if (res.isError) {
				this.popupService.detailAlert(this.$scope, res.data.message, res.data.stackTrace);
				return;	
			}
			
			this.inferfaceMainGrid.records = records;
			this.inferfaceMainGrid.recordsCount = recordsCount;
			
			if(!_.isEmpty(this.inferfaceMainGrid.name)) {
				w2ui[this.inferfaceMainGrid.name].selectNone();
			}
			
			if(goToFirst) {
				this.pageNumber = 1;
				this.$scope.$broadcast(`resetPage`, this.pageNumber);
			}
		});
	}
	
	setIntrfGridData(id, isCreateInterfaceId = false, afterFn){
		const httpService = this.httpService;
		
		httpService.get(`/intrfccoms/${id}`).then(data => {
			this.useSndReqToResLayoutGrid = false;
			this.useSndReqLayoutGrid = false;
			this.useSndResLayoutGrid = false;
			this.useRequestMsgScroll = true;
			this.useResponseMsgScroll = true;
			this.compulsionDeployYn = false;
			
			this.setIntrfAccordion(data, isCreateInterfaceId);
			
			afterFn && afterFn();
		});
	}
	
	setIntrfAccordion(data, isCreateInterfaceId = false){
		const { intrfcsrsysdtDto : intrfcsrsysRecords, 
			intrfcmsglayoutdtDto : intrfcmsglayoutdtDtoRecords, 
			intrfcdeploysysdtDto : intrfcdeploysysdtDtoRecords, 
			intrfcdeployhisthsDto : intrfcdeployhisthsDtoRecords,
			intrfcroutinfodtDto : intrfcroutinfodtDtoRecords } = data;
			
		if (data.isError) {
			this.openAlert(data.data.message);
			return;	
		}
		
		
		if(!isCreateInterfaceId) this.resetDetail(false);
		this.interfaceDetailGrid.records = intrfcsrsysRecords.map(intrfcsrsys => this.intfcDto(intrfcsrsys));
		
		const sends = intrfcmsglayoutdtDtoRecords.filter(v => v.srTypeCd === 'SEND');
		const receives = intrfcmsglayoutdtDtoRecords.filter(v => v.srTypeCd === 'RECEIVE');
		const sendRequest = sends.filter(v => v.rqstRspsTypeCd === 'REQUEST');
		const sendReponse = sends.filter(v => v.rqstRspsTypeCd === 'RESPONSE');
		const receiveRequest = receives.filter(v => v.rqstRspsTypeCd === 'REQUEST');
		const receiveResponse = receives.filter(v => v.rqstRspsTypeCd === 'RESPONSE');
			
		this.sndReqLayoutGrid.records = sendRequest;
		this.sndResLayoutGrid.records = sendReponse;
		this.rcvReqLayoutGrid.records = receiveRequest;
		this.rcvResLayoutGrid.records = receiveResponse;
		
		const sendMsgMapSrc = sendRequest.filter(v => !_.isEmpty(v.msglayoutbsDto))
										 .map(sr => sr.msglayoutbsDto.msglayoutdtDto)
										 .reduce((v1, v2)=> v1.concat(v2), []);

		const sendMsgMapTrg = receiveRequest.filter(v => !_.isEmpty(v.msglayoutbsDto))
											.map(rr => rr.msglayoutbsDto.msglayoutdtDto)
										    .reduce((v1, v2)=> v1.concat(v2), [])
										    .map(v => {
										    	v.mappingTypeCd = 'NONE';
										    	return v;
										    });
		const recvMsgMapSrc = receiveResponse.filter(v => !_.isEmpty(v.msglayoutbsDto))
								 .map(sr => sr.msglayoutbsDto.msglayoutdtDto)
								 .reduce((v1, v2)=> v1.concat(v2), []);
		
		const recvMsgMapTrg = sendReponse.filter(v => !_.isEmpty(v.msglayoutbsDto))
										 .map(sr => sr.msglayoutbsDto.msglayoutdtDto)
		 								 .reduce((v1, v2)=> v1.concat(v2), [])
		 								 .map(v => {
		 									v.mappingTypeCd = 'NONE';
		 									 return v;
		 								 });
		
		this.sndMsgMapSrcGrid.records = sendMsgMapSrc;
		this.sendMsgMapTrgGrid.records = sendMsgMapTrg;
		this.rcvMsgMapSrcGrid.records = recvMsgMapSrc;
		this.rcvMsgMapTrgGrid.records = recvMsgMapTrg;
		this.setGridSeq();
		
		if(data.trxDscd == 'ONLINE'){
			this.isOnline = true;
			this.isBatch = false;
		}else if(data.trxDscd == 'BATCH'){
			this.isOnline = false;
			this.isBatch = true;
		}
		
		data.intrfccombsMappingReqDto && data.intrfccombsMappingReqDto.map(reqestMapping => {
			 const result = sendMsgMapTrg.filter(v => v.fldUnqId == reqestMapping.targetData);
			 
			 if(result.length > 0){
				 result[0].mappingTypeCd = reqestMapping.mappingTypeCd;
				 result[0].srcData = reqestMapping.srcData;
			 }
		});
		
		data.intrfccombsMappingResDto && data.intrfccombsMappingResDto.map(responseMapping => {
			const result = recvMsgMapTrg.filter(v => v.fldUnqId == responseMapping.targetData);
			 
			 if(result.length > 0){
				 result[0].mappingTypeCd = responseMapping.mappingTypeCd;
				 result[0].srcData = responseMapping.srcData;
			 }
		});
		
		this.deployTargetSysGrid.records = intrfcdeploysysdtDtoRecords;
		this.refHistoryGrid.records = intrfcdeployhisthsDtoRecords.map(v => {
			v.id = v.deploySysCd + v.deployVersion;
			return v;
		});

		this.editIntrfcDetailGrid(false);
		
		this.detail = data;
		intrfcsrsysRecords.map(intrfcsrsys => {			
			if(intrfcsrsys.srTypeCd === 'SEND') {
				this.setSysCd('SEND', intrfcsrsys.sysCd, intrfcsrsys.sysNm);
			}else{
				this.setSysCd('RECEIVE', intrfcsrsys.sysCd, intrfcsrsys.sysNm);
			}
		});
		
		if(isCreateInterfaceId) this._createInterFaceId();
	}
	
	setSysCd(srTypeCd, sysCd, sysNm) {
		if(srTypeCd === 'SEND') {
			this.detail.sendSysCd = sysCd;
			this.detail.sendSysNm = sysNm;
		}else{
			this.detail.receiveSysCd = sysCd;
			this.detail.receiveSysNm = sysNm;
		}
	}
	
	deleteInfrcId(id){
		this.httpService.delete(`/intrfccoms/${id}`).then(data => {
			this.resetDetail();
			this.getIntrfccoms();
		});
	}
	
	changePageSize(){
		const pageSize = this.select.pageSize;
		this.inferfaceMainGrid.limit = pageSize;
		this.gridHeight = this.gridService.getGridHeight(this.select.pageSize);
		this.pageBtnClick(1);
		
		this.inferfaceMainGrid.name && w2ui[this.inferfaceMainGrid.name].focus();
	}

	changeTrxDscd(trxDscd){
		if(trxDscd == 'ONLINE'){
			this.isOnline = true;
			this.isBatch = false;
		}else if(trxDscd == 'BATCH'){
			this.isOnline = false;
			this.isBatch = true;
		}
		
		this.editIntrfcDetailGrid();
	}
	
	cancel(){
		this.resetDetail();
		this.offEditMode();
	}
	
	add(){
		if(!_.isEmpty(this.inferfaceMainGrid.name)) {
			w2ui[this.inferfaceMainGrid.name].selectNone();
		}
		
		this.resetDetail();
		this.detail.fepDto = {};
		this.setSysCd('SEND', '', '');
		this.setSysCd('RECEIVE', '', '');
		this.isAdd = true;
		this._onEdit();
		this._openIntrfaceDetailZabara();
		this.setDefaultDeployTarget();
	}
	
	setDefaultDeployTarget() {
		this.httpService.post(`/depolysyss/getlist`, ['FEP', 'SIM', 'RSH', 'E2E']).then(data => {
			data.map((deploySys) => {
				 deploySys.deployUrl = deploySys.deploySysUrl;
				 delete deploySys.deploySysUrl;
			});
			
			this.deployTargetSysGrid.records = data;
		});
	}
	
	_openIntrfaceDetailZabara(){
		const $interfaceDetailZabara = $('#interfaceDetailInfoZabara');
		const isClose = $interfaceDetailZabara.css('transform') !== 'none';
		
		if(isClose) setTimeout(()=>$('#interfaceDetailInfoZabara').click());
	}
	
	_openAllZabara(){
		const $interfaceDetailZabara = $('#interfaceDetailInfoZabara');
		const $sndSysMsgInfoZabara = $('#sndSysMsgInfoZabara');
		const $rcvSysMsgInfoZabara = $('#rcvSysMsgInfoZabara');
		const $msgMappingZabara = $('#msgMappingZabara');
		
		
		const isClose1 = $interfaceDetailZabara.css('transform') !== 'none';
		const isClose2 = $sndSysMsgInfoZabara.css('transform') !== 'none';
		const isClose3 = $rcvSysMsgInfoZabara.css('transform') !== 'none';
		const isClose4 = $msgMappingZabara.css('transform') !== 'none';
		
		
		if(isClose1) setTimeout(()=>$('#interfaceDetailInfoZabara').click());
		if(isClose2) setTimeout(()=>$('#sndSysMsgInfoZabara').click());
		if(isClose3) setTimeout(()=>$('#rcvSysMsgInfoZabara').click());
		if(isClose4) setTimeout(()=>$('#msgMappingZabara').click());
		
	}
	
	resetDetail(offEditMode = true) {
		this.detail = {};
		
		this.sndSysReqMsg = {};
		this.sndSysResMsg = {};
		this.rcvSysReqMsg = {};
		this.rcvSysResMsg = {};
		this.sendMsgMapSrcMap = {};
		this.recvMsgMapSrcMap = {};
		if(offEditMode) this.offEditMode();
		this.isAdd = false;
		
		this._selectedIntrfcmsglayoutdtDto = {};
		this.selectedIntrfcdeploysysdtDto = {};
		
		if(!_.isEmpty(this.interfaceDetailGrid.name)){
			this.interfaceDetailGrid.records = [];
			this.sndReqLayoutGrid.records = [];
			this.sndResLayoutGrid.records = [];
			this.rcvReqLayoutGrid.records = [];
			this.rcvResLayoutGrid.records = [];
			this.sndMsgMapSrcGrid.records = [];
			this.sendMsgMapTrgGrid.records = [];
			this.rcvMsgMapSrcGrid.records = [];
			this.rcvMsgMapTrgGrid.records = [];
			this.deployTargetSysGrid.records = [];
			this.refHistoryGrid.records = [];
		}
	}
	
	resetSearch(){
		this.searchParam.intrfccoms = {};
		this.getIntrfccoms(true);
	}
	
	blur($event){
		$event.target.blur();
	}
	
	addIntfcDetailRow(){
		if(_.isEmpty(this.detail.intrfcId)) {
			this.openAlert(this.text.addIntrfcIdMsg);
			return;
		}
		
		const grid = w2ui[this.interfaceDetailGrid.name];
		grid.add(this.intfcDto({ srTypeCd: 'RECEIVE'}));
	}
	
	addSndReqLayoutRow(){
		this.gridService.addRecord(this.sndReqLayoutGrid,
								   this.sndReqDto);
		
		const grid = w2ui[this.sndReqLayoutGrid.name];
		grid.refresh();
	}
	
	moveSndReqLayoutRow(action, optionName){
		if(!this.isEdit) return;
		
		switch(action){
		case 'up':
			this.gridService.upSelected(this[optionName]);
			this._reRenderMappingGrids(optionName);
			break;
		case 'down':
			this.gridService.downSelected(this[optionName]);
			this._reRenderMappingGrids(optionName);
			break;
		}
		
		if(optionName === 'sndReqLayoutGrid' || optionName === 'sndResLayoutGrid') {
			this.setIoCopy(true);
		}
		
		this.setGridSeq();
	}
	
	_reRenderMappingGrids(optionName){
		const grid = w2ui[this[optionName].name];
		const trgGridName = this._getTargetGridNameByOptionName(optionName);
		
		this[trgGridName].records = grid.records.map(v => v.msglayoutbsDto.msglayoutdtDto)
									  .reduce((v1,v2)=> v1.concat(v2), []);
	}
	
	onClickIoCopy(optionName){
		if(!this.isEdit) return;
		
		let grid,
			mappingRecords;
		
		switch(optionName){
		case 'sndReqLayoutGrid':
			grid = w2ui[this.sndReqLayoutGrid.name];
			grid.save();
			
			mappingRecords = [];
			
			this.rcvReqLayoutGrid.records = grid.records.map(record =>{
				let clone = this.utilService.clone(record);
				clone.srTypeCd = 'RECEIVE';
				
				clone.msglayoutbsDto.msglayoutdtDto.map(v => {
			    	v.mappingTypeCd = 'NONE';
			    	mappingRecords.push(v);
			    });
				
				return clone;
			});
			
			this.sendMsgMapTrgGrid.records = mappingRecords;
			
			break;
		case 'sndResLayoutGrid':
			grid = w2ui[this.sndResLayoutGrid.name];
			grid.save();
			
			mappingRecords = [];
			
			this.rcvResLayoutGrid.records = grid.records.map(record =>{
				let clone = this.utilService.clone(record);
				clone.srTypeCd = 'RECEIVE';
				
				mappingRecords.push(...clone.msglayoutbsDto.msglayoutdtDto);
				
				return clone;
			});
			
			this.rcvMsgMapSrcGrid.records = mappingRecords;
			this.rcvMsgMapTrgGrid.records.map( v => {
				v.mappingTypeCd = 'NONE';
				v.srcData = '';
			})
			break;
		case 'sndReqToSndResLayoutGrid':
			grid = w2ui[this.sndReqLayoutGrid.name];
			grid.save();
			
			mappingRecords = [];
			
			this.sndResLayoutGrid.records = grid.records.map(record =>{
				let clone = this.utilService.clone(record);
				clone.srTypeCd = 'SEND';
				clone.rqstRspsTypeCd = 'RESPONSE';
				
				clone.msglayoutbsDto.msglayoutdtDto.map(v => {
			    	v.mappingTypeCd = 'NONE';
			    	mappingRecords.push(v);
			    });
				
				return clone;
			});
			
			this.rcvMsgMapTrgGrid.records = mappingRecords;
			break;
		case 'sndResToRcvResLayoutGrid':
			grid = w2ui[this.sndReqLayoutGrid.name];
			grid.save();
			
			mappingRecords = [];
			
			this.rcvResLayoutGrid.records = grid.records.map(record =>{
				let clone = this.utilService.clone(record);
				clone.srTypeCd = 'RECEIVE';
				clone.rqstRspsTypeCd = 'RESPONSE';
				
				mappingRecords.push(...clone.msglayoutbsDto.msglayoutdtDto);
				
				return clone;
			});
			
			this.rcvMsgMapSrcGrid.records = mappingRecords;
			this.rcvMsgMapTrgGrid.records.map( v => {
				v.mappingTypeCd = 'NONE';
				v.srcData = '';
			})
			break;
		}
	}
	
	refreshMappingGrid() {
		if(this.detail.msgTrnsfrmYn === 'Y') {
			 w2ui[this.sndMsgMapSrcGrid.name].refresh();
			 w2ui[this.sendMsgMapTrgGrid.name].refresh();
			 w2ui[this.rcvMsgMapSrcGrid.name].refresh();
			 w2ui[this.rcvMsgMapTrgGrid.name].refresh();
		}
	}
	
	setIoCopy(isNotGridSeq) {
		if(this.detail.msgTrnsfrmYn === 'N'){
			w2ui[this.rcvReqLayoutGrid.name].hideColumn('delete');
			w2ui[this.rcvResLayoutGrid.name].hideColumn('delete');
			this.onClickIoCopy('sndReqLayoutGrid');
			this.onClickIoCopy('sndResLayoutGrid');
		}else{			
			// 송신시스템 요청전문 사용
			if(this.useSndReqLayoutGrid) {
				w2ui[this.rcvReqLayoutGrid.name].hideColumn('delete');
				this.onClickIoCopy('sndReqLayoutGrid');
			}else{
				w2ui[this.rcvReqLayoutGrid.name].showColumn('delete');
			}
			
			// 송신시스템 응답전문 사용
			if(this.useSndResLayoutGrid) {
				w2ui[this.rcvResLayoutGrid.name].hideColumn('delete');
				this.onClickIoCopy('sndResLayoutGrid');
			}else{
				w2ui[this.rcvResLayoutGrid.name].showColumn('delete');
			}
		}
		
		// 요청전문 사용
		if(this.useSndReqToResLayoutGrid) {
			w2ui[this.sndResLayoutGrid.name].hideColumn('delete');
			this.onClickIoCopy('sndReqToSndResLayoutGrid');
			
			if(this.useSndResLayoutGrid || this.detail.msgTrnsfrmYn === 'N') {
				this.onClickIoCopy('sndResToRcvResLayoutGrid');
			}
		}else{
			w2ui[this.sndResLayoutGrid.name].showColumn('delete');
		}
		
		!isNotGridSeq && this.setGridSeq();
	}
	
	onEditMode(){
		if(_.isEmpty(this.detail)) return;
		this._onEdit();
	}
	
	_onEdit(){
		const $forms = $(`#searchWrap_detail,
						  #searchWrap_sndFileDesc,
						  #searchWrap_recvFileDesc,
						  #searchWrap_timeout, 
						  #searchWrap_rcvReqMsg, 
						  #searchWrap_rcvResMsg`)
						.find('div:not(.disabled)')
						.find('input,textarea,select');
		this.isEdit = true;
		
		$forms.attr('disabled', false);
		this.editIntrfcDetailGrid();
		
		w2ui[this.sndReqLayoutGrid.name].showColumn('delete');
		w2ui[this.sndResLayoutGrid.name].showColumn('delete');

		if(this.detail.msgTrnsfrmYn !== 'N') {
			w2ui[this.rcvReqLayoutGrid.name].showColumn('delete');
			w2ui[this.rcvResLayoutGrid.name].showColumn('delete');	
		}else{
			w2ui[this.rcvReqLayoutGrid.name].hideColumn('delete');
			w2ui[this.rcvResLayoutGrid.name].hideColumn('delete');
		}
		
		w2ui[this.deployTargetSysGrid.name].showColumn('edit');
	}
	
	offEditMode(){
		const $forms = $(`#searchWrap_detail,
						  #searchWrap_sndFileDesc,
						  #searchWrap_recvFileDesc,
						  #searchWrap_timeout,
						  #searchWrap_rcvReqMsg,
						  #searchWrap_rcvResMsg`)
						.find('input,textarea,select');

		this.isEdit = false;
		this.isAdd = false;
		
		$forms.attr('disabled', true);
		
		w2ui[this.sndReqLayoutGrid.name].hideColumn('delete');
		w2ui[this.sndResLayoutGrid.name].hideColumn('delete');
		w2ui[this.rcvReqLayoutGrid.name].hideColumn('delete');
		w2ui[this.rcvResLayoutGrid.name].hideColumn('delete');
		w2ui[this.deployTargetSysGrid.name].hideColumn('edit');
	}
	
	openImportXlsxPopup(){
		this.popupService.openModal('SCR0703', {intrfcTypeCd: this.intrfcTypeCd})
						  .then((res)=>{})
						 .catch(()=>{});
	}
	
	openImportXlsxPopup2(){
		this.popupService.openModal('SCR0703', {intrfcTypeCd: this.intrfcTypeCd, definition: true})
						  .then((res)=>{})
						  .catch(()=>{});
	}
	
	openAlert(alertBody){
		this.popupService.simpleAlert(this.$scope, alertBody);
	}
	
	pageBtnClick(num) {
		this.pageNumber = num;
		this.getIntrfccoms(num === 1);
	}
	
	onClickZabara(...optionNames) {
		if(optionNames[0] === 'sndMsgMapSrcGrid'){
			this.gridService.onClickZabara(this, optionNames, this.setGridSeq.bind(this));
		}else{
			this.gridService.onClickZabara(this, optionNames);
		}
	}
	
	mapping(type){
		if(!this.isEdit) return;
		const isRequest = type === 'REQUEST';
		const trgOption = isRequest ? this.sendMsgMapTrgGrid : this.rcvMsgMapTrgGrid;
		const srcOption = isRequest ? this.sndMsgMapSrcGrid : this.rcvMsgMapSrcGrid;
		const trgGrid = w2ui[trgOption.name];
		trgGrid.save();
		
		if(trgGrid.records.length === 0) return;
		
		this._removeMappingData(trgGrid);
		
		const srcFldUnqIds = w2ui[srcOption.name].records.map(v => v.fldUnqId);
		const srcMsgLayoutIds = w2ui[srcOption.name].records.map(v => v.msgLayoutId);
		const trgMsgLayoutIds = trgGrid.records.map(v => v.msgLayoutId)
											   .reduce((v1,v2)=> {
												   if(v1.indexOf(v2) < 0) v1.push(v2);
												   return v1;
											   }, []);
		
		srcFldUnqIds.map((v, idx) =>{
			const msgIndex = srcMsgLayoutIds[idx].length;
			const field = v.slice(msgIndex);
			
			for(let i = 0 ; i < trgMsgLayoutIds.length ; i++){
				const trgFldUnqId = trgMsgLayoutIds[i].concat(field);
				const trgRecord = trgGrid.records.filter(trgV => trgV.fldUnqId === trgFldUnqId)[0];
				
				if(!_.isEmpty(trgRecord)){
					trgRecord.mappingTypeCd = 'PROPT';
					trgRecord.srcData = v;
				}
			}
		});
		
		trgGrid.records.map(v => {
			if(v.mappingTypeCd !== 'PROPT'){
				v.mappingTypeCd = "NONE";
				v.srcData = "";
			}
		});
		
		this.mappingConfirm(type, true);
	}
	
	mappingConfirm(type, isMapping){
		if(!this.isEdit) return;
		const isRequest = type === 'REQUEST';
		const gridName = isRequest ? this.sendMsgMapTrgGrid.name : this.rcvMsgMapTrgGrid.name;
		const diffRcords = isRequest
				? this._getDiffRecords(this.sndMsgMapSrcGrid, this.sendMsgMapTrgGrid)
				: this._getDiffRecords(this.rcvMsgMapSrcGrid, this.rcvMsgMapTrgGrid);

		setTimeout(()=>{
			diffRcords.map(v =>{
				const changeDot = v.fldUnqId.replace(/\./g, '\\.');
				const $tr = $(`tr#grid_${gridName}_rec_${changeDot}`);
				
				$tr.addClass('bg-red');
			});
			
			let txt = isMapping ? this.text.completeMapping : this.text.completeMappingConfirm;
			let cnt = diffRcords.length;
			(cnt !== 0) && (txt += '<br> ' + this.text.confirmError + ' <span class="chr-c-orange">' + cnt + '</span>' + this.text.cnt);
			
			this.popupService.simpleAlert(this.$scope, txt);
		}, 300);
	}
	
	_getDiffRecords(srcOption, trgOption){
		const srcGrid = w2ui[srcOption.name];
		const trgGrid = w2ui[trgOption.name];
		
		trgGrid.save();
		trgGrid.selectNone();
		
		return trgOption.records
					.filter(v => v.mappingTypeCd === 'PROPT')
					.filter(v => {
						const srcRecord = srcGrid.records.filter(srcV => srcV.fldUnqId === v.srcData)[0];
						return !(srcRecord.dataTypeNm === v.dataTypeNm && srcRecord.msgLen === v.msgLen); 
					});
	}
	
	_removeMappingData(grid){
		const trgMsgLayoutId = grid.records[0].msgLayoutId;
		
		const $tr = $(`tr[id^="grid_${grid.name}_rec_${trgMsgLayoutId}"`);
		$tr.removeClass('bg-red');
		
		grid.records.map(v => {
			v.mappingTypeCd = 'NONE';
			v.srcData = '';
		});
	}
	
	searchApplicationCodes(){
		this.popupService.openModal('SCR1402', { limitLvCd: 0 })
						 .then((code) => {
							this.searchParam.intrfccoms.lv1Cd = code.appCd;
						 })
						 .catch(()=>{});
	}
	
	openMsgLayoutPopup(){
		this.popupService.openModal('SCR0502')
						 .then(data => this.searchParam.intrfccoms.msgLayoutId = data.msgLayoutId)
						 .catch(()=>{});
	}
	
	openInstCdPopup(){
		if(!this.isEdit) return;
		this.popupService.openModal('SCR1602')
						 .then((data) => {
							 this.detail.instCd = data.instCd;
							 this.detail.instCdNm = data.instCdNm;
						 })
						 .catch(()=>{});
	}
	
	openInstCdPopup2(){
		this.popupService.openModal('SCR1602')
						 .then(data => this.searchParam.intrfccoms.instCd = data.instCd)
						 .catch(()=>{});
	}
	
	openSystemPopup(id){
		this.popupService.openModal('SCR1302')
						 .then(data =>{
							 if(id == 'snd'){
								 this.searchParam.intrfccoms.sysCdS = data.sysCd;
							 }else if(id == 'rcv'){
								 this.searchParam.intrfccoms.sysCdR = data.sysCd;
						 }})
						 .catch(()=>{});
	}
	
	openRegManPopup(){
		this.popupService.openModal('SCR0102')
						 .then(user => this.searchParam.intrfccoms.regManId = user.userId)
						 .catch(()=>{});
	}
	
	openInterfaceIdCreatePopup(){
		this.popupService.openModal('SCR0702', { intrfcTypeCd: this.intrfcTypeCd })
						 .then(intrfc => {
							 const { sndSys, rcvSys } = intrfc;
							 const detail = this.detail;
							 
							 delete intrfc.sndSys;
							 delete intrfc.rcvSys;
							 
							 detail.intrfcId = intrfc.intrfcId;
							 detail.lv1Cd = intrfc.lv1Cd;
							 detail.syncAsyncDscd = intrfc.syncAsyncDscd;
							 detail.trxDscd = intrfc.trxDscd;
							 detail.workStatusCd = 'WORKING';
							 
							 if(detail.trxDscd == 'ONLINE'){
								this.isOnline = true;
								this.isBatch = false;
							}else if(detail.trxDscd == 'BATCH'){
								this.isOnline = false;
								this.isBatch = true;
							}
							 
							this.editIntrfcDetailGrid();
							 
							 this.interfaceDetailGrid.records = [this.intfcDto({
								intrfcId: intrfc.intrfcId,
								srTypeCd: 'SEND',
								sysCd: sndSys.sysCd,
								sysNm: sndSys.sysNm,
								msglayoutbsDto: []
							 }), 
							 this.intfcDto({
								intrfcId: intrfc.intrfcId,
								srTypeCd: 'RECEIVE',
								sysCd: rcvSys.sysCd,
								sysNm: rcvSys.sysNm,
								msglayoutbsDto: []
							 })];
							 
							 this.setSysCd('SEND', sndSys.sysCd, sndSys.sysNm);
							 this.setSysCd('RECEIVE', rcvSys.sysCd, rcvSys.sysNm);
						 })
						 .catch(()=>{});
	}
	
	openDeploySystemPopup(){
		if(!this.isEdit) return;
		const grid = w2ui[this.deployTargetSysGrid.name];
		
		this.popupService.openModal('SCR1202', {deploySysGrpCd : this.intrfcTypeCd, codes: {'DEPLOY_SYS_DSCD' : this.codes['DEPLOY_SYS_DSCD']} })
						 .then(deploySys => {
							 if(!_.isEmpty(grid.get(deploySys.deploySysCd))){
								 this.openAlert(this.text.duplicateDeployTarget);
								 return;
							 }
							 
							 deploySys.deployUrl = deploySys.deploySysUrl;
							 delete deploySys.deploySysUrl;
							 grid.records.push(deploySys);
						 })
						 .catch(()=>{});
	}
	
	addMsgLayoutToSndRcvMsg(optionName){
		if(!this.isEdit) return;
		const grid = w2ui[this[optionName].name];
		const { srTypeCd, rqstRspsTypeCd } = this._getMsgWrapType(optionName);
		
		this.popupService.openModal('SCR0502', { 
			getDetail: true, codes: this.codes,
			trxDscdFilter: true, trxDscd: this.detail.trxDscd  
		})
						 .then(msglayoutbsDtoList => {
							 msglayoutbsDtoList.map((msglayoutbsDto) => {
								 if(!_.isEmpty(grid.get(msglayoutbsDto.msgLayoutId))){
									 this.openAlert(this.text.duplicateMsg);
									 return;
								 }
								 
								 grid.add({
									intrfcId : this.detail.intrfcId,
									srTypeCd : srTypeCd,
									rqstRspsTypeCd : rqstRspsTypeCd,
									msgLayoutId: msglayoutbsDto.msgLayoutId,
									msglayoutbsDto: msglayoutbsDto
								 });
								 
								 const targetGrid = this._getTargetGridByOptionName(optionName);
								 
								 msglayoutbsDto.msglayoutdtDto.map((dto, index) => {
									 dto.mappingTypeCd = 'NONE';
								 });
								 
								 if(!_.isEmpty(targetGrid)) {
									 targetGrid.add(msglayoutbsDto.msglayoutdtDto, false);
									 targetGrid.refresh();
								 }
								 
								 if(optionName === 'sndReqLayoutGrid' || optionName === 'sndResLayoutGrid') {
									 this.setIoCopy(true);
								 }
							 });
							 
							 this.setGridSeq();
						 })
						 .catch(()=>{});
	}
	
	setGridSeq() {
		this.sendMsgMapSrcMap = {};
		
		this.sndMsgMapSrcGrid.records.map((record, index) => {
			record.gridSeq = index + 1;
			this.sendMsgMapSrcMap[record.fldUnqId] = '[' + record.gridSeq + '] ' + record.fldUnqId; 
		});
			
			
		this.sendMsgMapTrgSrcData.render = (data) => {
			if(data.mappingTypeCd === 'PROPT'){
				return this.sendMsgMapSrcMap[data.srcData];
			}else{
				return data.srcData;
			}
		};
		
		this.recvMsgMapSrcMap = {};
		
		this.rcvMsgMapSrcGrid.records.map((record, index) => {
			record.gridSeq = index + 1;
			this.recvMsgMapSrcMap[record.fldUnqId] = '[' + record.gridSeq + '] ' + record.fldUnqId; 
		});
		
		this.recvMsgMapTrgSrcData.render = (data) => {
			if(data.mappingTypeCd === 'PROPT'){
				return this.recvMsgMapSrcMap[data.srcData];
			}else{
				return data.srcData;
			}
		};
	}
	
	_getMsgWrapType(optionName){
		let result = {};
		
		switch(optionName){
		case 'sndReqLayoutGrid':
			result.srTypeCd = 'SEND';
			result.rqstRspsTypeCd = 'REQUEST';
			break;
		case 'sndResLayoutGrid':
			result.srTypeCd = 'SEND';
			result.rqstRspsTypeCd = 'RESPONSE';
			break;
		case 'rcvReqLayoutGrid':
			result.srTypeCd = 'RECEIVE';
			result.rqstRspsTypeCd = 'REQUEST';
			break;
		case 'rcvResLayoutGrid':
			result.srTypeCd = 'RECEIVE';
			result.rqstRspsTypeCd = 'RESPONSE';
			break;
		}
		
		return result;
	}
	
	_getTargetGridByOptionName(optionName){
		let grid = null;
		
		switch(optionName){
		case 'sndReqLayoutGrid':
			grid = w2ui[this.sndMsgMapSrcGrid.name];
			break;
		case 'sndResLayoutGrid':
			grid = w2ui[this.rcvMsgMapTrgGrid.name];
			break;
		case 'rcvResLayoutGrid':
			grid = w2ui[this.rcvMsgMapSrcGrid.name];
			break;
		case 'rcvReqLayoutGrid':
			grid = w2ui[this.sendMsgMapTrgGrid.name];
			break;
		}

		return grid;
	}
	
	_getTargetGridNameByOptionName(optionName){
		let gridName = null;
		
		switch(optionName){
		case 'sndReqLayoutGrid':
			gridName = 'sndMsgMapSrcGrid';
			break;
		case 'sndResLayoutGrid':
			gridName = 'rcvMsgMapTrgGrid';
			break;
		case 'rcvResLayoutGrid':
			gridName = 'rcvMsgMapSrcGrid';
			break;
		case 'rcvReqLayoutGrid':
			gridName = 'sendMsgMapTrgGrid';
			break;
		}

		return gridName;
	}
	
	deployInterface(event){
		event.stopPropagation();
		
		if(this.detail.msgLayoutTranYn === 'Y' && 
				this.detail.msgTrnsfrmYn === 'Y' &&
				this.compulsionDeployYn === false) {
			this.openAlert(this.text.confirmChangedMsg2);
			return;
		}
		
		if(this.isEdit) {
			this.openAlert(this.text.deployAfterSave);
			return;
		}
		
		const deployGridRecords = w2ui[this.deployTargetSysGrid.name].records;

		if(_.isEmpty(deployGridRecords)){
			this.openAlert(this.text.emptyDeploySystem);
			return;
		}
		
		if(_.isEmpty(this.detail)){
			this.openAlert(this.text.emptyDeployTarget);
			return;
		}
		this.popupService.simpleConfirm(this.$scope,
				this.text.confirmDeploy,
				()=>this._deployInterface());
	}
	
	isRowMetaValid(row){
		const meta = this.metaService.getMetaByMetaEngNm(row.fldEngNm);
		return meta 
				&& meta.metaKorNm == row.fldKorNm
		//		&& meta.metaLen == row.msgLen
		//		&& meta.decimalLen == row.decimalLen
		//		&& meta.dataTypeNm == row.dataTypeNm
				? true : false;
	}
	
	_deployInterface(){
		this.popupService.showLoadingBar(this.$scope);
		
		let param = this.utilService.clone(this.detail);
		param.compulsionDeployYn = this.compulsionDeployYn === true ? 'Y' : 'N'
			
		this.httpService.post('/intrfccoms/deploy', param)
			.then((res)=>{
				if(res.isError){
					this.popupService.detailAlert(this.$scope, res.data.message, res.data.stackTrace, res.data.parameters);
					return;
				}

				this.setDeployResult(res.intrfcDeployResponse);
				
				let url = `/intrfccoms/deployhistorys?intrfcId=${this.detail.intrfcId}`;
				this.httpService.get(url).then(data => {
					this.refHistoryGrid.records = data.intrfcdeployhisthsOutList.map(v => {
						v.id = v.deploySysCd + v.deployVersion;
						return v;
					});
				});
				
				let filterResponse = res.intrfcDeployResponse.filter(v => v.deployStatus);
				let successCnt = 0;
				let failCnt = 0;
				
				filterResponse.map((data) => {
					if(data.deployStatus === 'SUCCESS') {
						successCnt++;
					}else if(data.deployStatus === 'FAIL') {
						failCnt++;
					}
				});
				
				this.popupService.closeLoadingBar();
				
				let alertText = this.text.completeDeploy;
				
				if(successCnt !== 0 && filterResponse.length === successCnt) {
					alertText += '<br/><span class="chr-c-green">' + this.text.successDeploy + '</span>';
				}else if(failCnt !== 0) {
					alertText += '<br/><span class="chr-c-orange">' + this.text.failDeploy + '</span>';
				}
				
				
				this.openAlert(alertText);
			})
			.finally(() => {
				this.popupService.closeLoadingBar();
			});
	}
	
	setDeployResult(intrfcDeployResponseList){
		const grid = w2ui[this.deployTargetSysGrid.name];
		
		grid.records.map(record => {
			for(let i = 0 ; i < intrfcDeployResponseList.length; i++){
				const res = intrfcDeployResponseList[i];
				
				if(record.deploySysCd == res.systemCd){
					record.deployResultCd = res.deployStatus;
					record.resultMessage = res.message;
					break;
				}
			}
		});

		if(this.$scope.$$phase !== '$apply' && this.$scope.$$phase !== '$digest') this.$scope.$apply();
	}
	
	redeployInterface(intrfcdeployhist){
		this.popupService.showLoadingBar(this.$scope);
		
		const intrfcdeployhistClone = this.utilService.clone(intrfcdeployhist);
		
		this.httpService.post('/intrfccoms/redeploy', intrfcdeployhistClone)
			.then((res)=>{
				this.popupService.closeLoadingBar();
				
				if(res.isError){
					this.popupService.detailAlert(this.$scope, res.data.message, res.data.stackTrace);
					return;
				}
				
				let url = `/intrfccoms/deployhistorys?intrfcId=${this.detail.intrfcId}`;
				this.httpService.get(url).then(data => {
					this.refHistoryGrid.records = data.intrfcdeployhisthsOutList.map(v => {
						v.id = v.deploySysCd + v.deployVersion;
						return v;
					});
				});
				
				this.popupService.closeLoadingBar();
				this.openAlert(this.text.completeRedeploy);
			})
			.finally(() => {
				this.popupService.closeLoadingBar();
			});
	}
	
	save(){
		const detail = this.detail;
		const _w2ui = w2ui; 
		const interfaceDetailGrid = _w2ui[this.interfaceDetailGrid.name];
		const sndReqLayoutGridRecords = _w2ui[this.sndReqLayoutGrid.name].records;
		const sndResLayoutGridRecords = _w2ui[this.sndResLayoutGrid.name].records;
		const rcvReqLayoutGridRecords = _w2ui[this.rcvReqLayoutGrid.name].records;
		const rcvResLayoutGridRecords = _w2ui[this.rcvResLayoutGrid.name].records;
		
		const sendMsgMapTrgGrid = _w2ui[this.sendMsgMapTrgGrid.name];
		const rcvMsgMapTrgGrid = _w2ui[this.rcvMsgMapTrgGrid.name];
		
		sendMsgMapTrgGrid.save();
		rcvMsgMapTrgGrid.save();
		
		const sendMsgMapTrgGridRecords = sendMsgMapTrgGrid.records;
		const rcvMsgMapTrgGridRecords = rcvMsgMapTrgGrid.records;
		
		const deployTargetSysGridRecords = _w2ui[this.deployTargetSysGrid.name].records;
		
		let receiveSrSeq = 1;
		let sendSrSeq = 1;
		let reqDtoMsgId = null;
		let resDtoMsgId = null;
		let reqDtoTargetDataSeq = 1;
		let resDtoTargetDataSeq = 1
		
		interfaceDetailGrid.save();
		
		if(!this._checkValid()) return;
		
		this.popupService.showLoadingBar(this.$scope);
		
		let receiveSysCd,
		sendSysCd;
		
		detail.intrfcTypeCd = this.intrfcTypeCd;
		detail.intrfcsrsysdtDto = interfaceDetailGrid.records
										.map(v => {
											v.intrfcId = detail.intrfcId
											if(v.srTypeCd === 'RECEIVE') {
												v.srSeq = receiveSrSeq++;
												receiveSysCd = v.sysCd;
											}
											else if(v.srTypeCd === 'SEND') {
												v.srSeq = sendSrSeq++;
												sendSysCd = v.sysCd;
											}
											return v;
										});
		
		detail.intrfcmsglayoutdtDto = [];
		detail.intrfcmsglayoutdtDto.push(...sndReqLayoutGridRecords.map((v,index) => {
			v.sysCd = sendSysCd;
			v.intrfcId = detail.intrfcId;
			v.srSeq = 1;
			v.rqstRspsSeq = index + 1;
			return v;
		}));
		detail.intrfcmsglayoutdtDto.push(...sndResLayoutGridRecords.map((v,index) => {
			v.sysCd = sendSysCd;
			v.intrfcId = detail.intrfcId;
			v.srSeq = 1;
			v.rqstRspsSeq = index + 1;
			return v;
		}));
		detail.intrfcmsglayoutdtDto.push(...rcvReqLayoutGridRecords.map((v,index) => {
			v.sysCd = receiveSysCd;
			v.intrfcId = detail.intrfcId;
			v.srSeq = 1;
			v.rqstRspsSeq = index + 1;
			return v;
		}));
		detail.intrfcmsglayoutdtDto.push(...rcvResLayoutGridRecords.map((v,index) => {
			v.sysCd = receiveSysCd;
			v.intrfcId = detail.intrfcId;
			v.srSeq = 1;
			v.rqstRspsSeq = index + 1;
			return v;
		}));
		
		detail.intrfccombsMappingReqDto = sendMsgMapTrgGridRecords.map((v,index)=>{
			if(reqDtoMsgId != v.msgLayoutId){
				reqDtoMsgId = v.msgLayoutId;
				reqDtoTargetDataSeq = 1;
			}
			
			let data = {
					mappingTypeCd: v.mappingTypeCd,
					reqResTypeCd: 'REQUEST',
					srcData: v.srcData,
					targetData: v.fldUnqId,
					mappingSeq: index + 1,
					targetDataSeq: reqDtoTargetDataSeq++
				};
			
			return data;
		});
		
		detail.intrfccombsMappingResDto = rcvMsgMapTrgGridRecords.map((v,index)=>{
			if(resDtoMsgId != v.msgLayoutId){
				resDtoMsgId = v.msgLayoutId;
				resDtoTargetDataSeq = 1;
			}
			
			let data = {
					mappingTypeCd: v.mappingTypeCd,
					reqResTypeCd: 'RESPONSE',
					srcData: v.srcData,
					targetData: v.fldUnqId,
					mappingSeq: index + 1,
					targetDataSeq: resDtoTargetDataSeq++
				};
			
			return data;
		});
		
		detail.intrfcdeploysysdtDto = deployTargetSysGridRecords
											.map((v, index) => {
												v.intrfcId = detail.intrfcId
												v.deploySysSeq = index + 1;
												return v;
											});
		
		const requestPromise = this.isAdd 
					? this.httpService.post('/intrfccoms', detail) 
					: this.httpService.put('/intrfccoms', detail);
					
		requestPromise.then(res =>{
			this.popupService.closeLoadingBar();
			
			if(res.isError){
				this.popupService.detailAlert(this.$scope, res.data.message, res.data.stackTrace, res.data.parameters);
				return;
			}
			
			this.getIntrfccoms();
			if(this.isAdd){
				this.setIntrfGridData(res.intrfcId);
			}else{
				this.setIntrfGridData(detail.intrfcId);
			}
			
			this.openAlert(bxMsg('common.saved'));
			this.offEditMode();
			this.detail.msgLayoutTranYn = 'N';
		});
		
		requestPromise.finally(() => {
			this.popupService.closeLoadingBar();
		});
	}
	
	_checkValid(){
		const intrfccoms = this.detail;
		const interfaceDetailGrid = w2ui[this.interfaceDetailGrid.name];
		
		if(_.isEmpty(intrfccoms.intrfcId)){
			this.openAlert(this.text.addIntrfcIdMsg);
			return false;
		} else if(_.isEmpty(intrfccoms.intrfcNm)){
			this.openAlert(this.text.emptyInterfaceName);
			return false;
		} else if(intrfccoms.trxDscd === 'ONLINE' && _.isEmpty(intrfccoms.msgTrnsfrmYn)){
			this.openAlert(this.text.emptyMsgTrnsfrmYn);
			return false;
		} else if(intrfccoms.trxDscd !== 'BATCH' && _.isEmpty(intrfccoms.syncAsyncDscd)){
			this.openAlert(this.text.emptySyncAsyncDscd);
			return false;
		} else if(_.isEmpty(intrfccoms.workStatusCd)){
			this.openAlert(this.text.emptyWorkStatusCd);
			return false;
		}
			
		let interfaceDetailGridLength = interfaceDetailGrid.records.length;
		for(let i = 0; i < interfaceDetailGridLength; i++){
			const system = interfaceDetailGrid.records[i];
			
			if(_.isEmpty(system.crgManNm)) {
				this.openAlert(this.text.receiveSendSys + system.sysCd + this.text.emptyCrgManNm);
				return false;
			}else if(intrfccoms.trxDscd === 'ONLINE' && system.srTypeCd == 'RECEIVE' && _.isEmpty(system.trxCd)){
				this.openAlert(this.text.receiveSendSys + system.sysCd + this.text.emptyTrxCd);
				return false;
			}
		}
			
		if(_.isEmpty(intrfccoms.instCd)){
			this.openAlert(this.text.emptyInstCd);
			return false;
		} 
		
		if(intrfccoms.trxDscd === 'ONLINE') {
			 if(_.isEmpty(intrfccoms.rqstExtrnlMsgNo)){
				this.openAlert(this.text.emptyRqstExtrnlMsgNo);
				return false;
			} else if(_.isEmpty(intrfccoms.fepDto.commNetworkIntrfcYn)){
				this.openAlert(this.text.emptyCommNetworkIntrfcYn);
				return false;
			}
		}
		
		if(intrfccoms.trxDscd === 'BATCH') {
			if(_.isEmpty(intrfccoms.fepDto.occurCycle)){
				this.openAlert(this.text.emptyOccurCycle);
				return false;
			}
		}
		
		return true;
	}
	
	excelExport(id, multiDownload, isFirstFile, isLastFile){
		const data = {'intrfcId' : id};
		
		if(multiDownload) {
			isFirstFile && this.popupService.showLoadingBar(this.$scope);
		}else{
			this.popupService.showLoadingBar(this.$scope);
		}
		
		this.httpService.downloadFile(`/intrfccoms/excelexport`, data).then(res => {
			if(res.isError){
				this.popupService.detailAlert(this.$scope, res.data.message, res.data.stackTrace);
			}else{
				var header = decodeURIComponent(res.headers('Content-Disposition'));
				var fileName = header.split("=")[1].replace(/\"/gi,'');
				console.log(fileName);
				
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
	
	openInterfacePopup(){
		this.popupService.openModal('SCR0902', { intrfcTypeCd: this.intrfcTypeCd, codes: this.codes  })
						 .then(intrfccom => this.setIntrfGridData(intrfccom.intrfcId, true))
						 .catch(()=>{});
	}
	
	_createInterFaceId(){
		const detail = this.detail;
		const requestBody = {
			intrfcTypeCd : this.intrfcTypeCd,
			trxDscd: detail.trxDscd,
			syncAsyncDscd: detail.syncAsyncDscd,
			lv1Cd: detail.lv1Cd,
			sendSysCd: detail.intrfcsrsysdtDto.find(v => v.srTypeCd === 'SEND').sysCd,
			receiveSysCd: detail.intrfcsrsysdtDto.find(v => v.srTypeCd === 'RECEIVE').sysCd,
		};
		
		this.httpService.post('/intrfccoms/intrfcidcreate', requestBody)
						.then(res => {
							if (res.isError) {
								this.popupService.detailAlert(this.$scope, res.data.message, res.data.stackTrace);
								return;
							}
							
							this.detail.intrfcId = res.intrfcId;
							this.detail.workStatusCd = 'WORKING';
							this.refHistoryGrid.records = [];
						});
	}
	
	refreshGrid(){
		const grid =  w2ui[this.inferfaceMainGrid.name];
		grid.refresh();
	}
	
	getInstCd(intrfccoms) {
		let txt = '';
		let instCd;
		let instCdNm;
		
		if(!_.isEmpty(intrfccoms)) {
			instCd = intrfccoms.instCd || '';
			instCdNm = intrfccoms.instCdNm || '';
			
			txt = instCd ? instCd + ' / ' + instCdNm : '';
		}
		
		return txt;
	}
	
	openTotalMsgPopup(gridName) {
		const grid =  w2ui[this[gridName]['name']];
		let msgLayoutIdList = [];
		
		grid.records.map((row) => {
			msgLayoutIdList.push(row['msglayoutbsDto']['msgLayoutId']);
		});
		
		this.popupService.openModal('SCR0707', {msgLayoutId: msgLayoutIdList, codes: this.codes, trxDscd: this.detail.trxDscd})
		 .then(()=>{})
		 .catch(()=>{});
	}
	
	xlsExport() {
		const grid = w2ui[this.inferfaceMainGrid.name];
		const selections = grid.getSelection();
		const selectionLength = selections.length;
		
		if(selectionLength === 0){
			this.popupService.simpleAlert(this.$scope, this.text.emptyInterface);
			return;
		}
		
		selections.map((id, idx) => {
			setTimeout(() => {
				this.excelExport(id, true, idx === 0, selectionLength === idx + 1);	
			}, idx * 500);
		});
	}
	
	exportDefinition() {
		let url = `/intrfccoms/export/intrfcinfos?intrfcTypeCd=${this.intrfcTypeCd}`;
		
		this.popupService.showLoadingBar(this.$scope);
		this.httpService.downloadFile(url, this.searchParam.intrfccoms, 'get').then(res => {
			if(res.isError){
				this.popupService.detailAlert(this.$scope, res.data.message, res.data.stackTrace);
			}else{
				var header = decodeURIComponent(res.headers('Content-Disposition'));
				var fileName = header.split("=")[1].replace(/\"/gi,'');
				console.log(fileName);
				
				var blob = new Blob([res.data], {type:"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"});
				this.utilService.saveFile(blob, fileName);
			}
		}).finally(() => {
			this.popupService.closeLoadingBar();
		});
	}
	
	stopPropagation(e) {
		e.stopPropagation();
	}
}

module(App.name).controller('SCR0901Controller2', SCR0901Controller2);