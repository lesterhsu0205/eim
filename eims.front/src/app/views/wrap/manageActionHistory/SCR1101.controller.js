import { module } from 'angular';
import App from '../../../app';

class SCR1101Controller {
	
	constructor ($scope, $state, $timeout, popupService, httpService, utilService, codeService, userService, gridService, codes){
		this.$scope = $scope;
		this.$state = $state;
		this.$timeout = $timeout;
		this.popupService = popupService;
		this.httpService = httpService;
		this.utilService = utilService;
		this.codeService = codeService;
		this.userService = userService;
		this.gridService = gridService;
		this.codes = codes;
		this.user = this.userService.getUser();
		this.baseUrl = '/actionhists';
		
		this.initText();	
		this.initSelect();
		this.initGrid();

		this.$scope.$on(`gridRendered`, () => {
			this.initPrevData();
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
				this.options.limit = prevScope.select.pageSize;
				this.gridHeight = prevScope.gridHeight;
				
				// 그리드
				this.options.records = prevScope.options.records;
				this.options.recordsCount = prevScope.options.recordsCount;
				
				this.$timeout(() => {
					this.pageNumber = prevScope.pageNumber;
					this.$scope.$broadcast(`resetPage`, this.pageNumber);
				});
				
			}
		} else {
			this.resetSearch();
		}
		
		this.$scope.$on('$destroy', () => {
			this.utilService.setParams(currentStateName, {scope: this.$scope});
		});
	}
	
	initText() {
		this.text = $.extend({}, bxMsg.getMessages('common'), bxMsg.getMessages('manageActionHistory'));
	}
	
	initSelect() {
		this.select = {
			pageSize: '20'
		};
	}
	
	initGrid() {
		this.gridHeight = this._getGridHeight();
		this.options = {
			limit: this.select.pageSize,
			autoComplete: false,
			pageSize: 10,
			recordsCount: 0,
			recid: 'id',
			columns: [
				{
					caption: 'No', size: '40px',
					render: (data, index) => {
						const pageNumber = this.pageNumber || 1;
						return (pageNumber - 1) * this.options.limit + index + 1;
					}
				},
				{ field: 'hstDscd', caption: this.text.hstDscd, size: '2%', sortable: true,
					render: (data, index, colIndex) => {
						return this.codeService.getCodeValNm('HISTORY_DSCD', data.hstDscd);
					}
				},
				{ field: 'itemId', caption: this.text.itemId, size: '3%', sortable: true },
				{ field: 'itemDesc', caption: this.text.itemDesc,size: '3%',  sortable: true, attr: 'align=left'},
				{ field: 'workDttm', caption: this.text.workDttm, size: '3%', sortable: true,
					render: (data) =>{
						return this.utilService.setRegDttm(data.workDttm);
					}
				},
				{ field: 'userId', caption: this.text.userId ,size: '1%',  sortable: true},
				{ field: 'workCttCd', caption: this.text.workCttCd, size: '1%', sortable: true,
					render: (data,index,colIndex) => {
						const msgDscd = w2ui[this.options.name].getCellValue(index, colIndex);
						return this.codeService.getCodeValNm('ACTION_STAT_CD', msgDscd);
					}
				}
			]
		};
	}
		            	
	getGridData(goToFirst = false) {
		const { pageNumber, pageSize } = this.gridService.getPageInfo(this.select, this.pageNumber);
		let url = `${this.baseUrl}?pageNumber=${ goToFirst ? 1 : pageNumber}&pageSize=${pageSize}`;
		
		var fromDt = new Date(this.searchParam.workDtFrom);
		var toDt = new Date(this.searchParam.workDtTo);
		
		var workDttmFrom = fromDt.getFullYear() + ('0'+(fromDt.getMonth()+1)).slice(-2)+('0'+fromDt.getDate()).slice(-2)+"000000";
		var workDttmTo = toDt.getFullYear() + ('0'+(toDt.getMonth()+1)).slice(-2)+('0'+toDt.getDate()).slice(-2)+"235959";

		this.searchParam.workDttmFrom = workDttmFrom;
		this.searchParam.workDttmTo = workDttmTo;
		
		this.httpService.get(url, this.searchParam).then(data => {
			const { actionhisthsOutList: records, totalCnt: recordsCount } = data;
		
			records.map((v) => {
				v.id = v.itemId + v.workDttm;
			});
			
			this.options.records = records;
			this.options.recordsCount = recordsCount;
			
			if(goToFirst) {
				this.pageNumber = 1;
				this.$scope.$broadcast(`resetPage`, this.pageNumber);
			}
		});
	}
	
	search(){
		this.getGridData(true);
	}

	blur($event){
		$event.target.blur();
	}
	
	resetSearch(){
		this.searchParam = {};	
		var today = new Date();
		today = today.getFullYear()+"/"+('0'+(today.getMonth()+1)).slice(-2)+"/"+('0'+today.getDate()).slice(-2);
		
		this.searchParam.workDtFrom = today;
		this.searchParam.workDtTo = today;

		this.getGridData(true);
	}

	change() {
		this.options.limit = this.select.pageSize;
		this.gridHeight = this._getGridHeight();
		this.pageBtnClick(1);
	}
	
	pageBtnClick(num) {
		this.pageNumber = num;
		this.getGridData(num === 1);
	}
	
	openUserId(){
		this.popupService.openModal('SCR0102')
		 .then(user => this.searchParam.userId = user.userId)
		 .catch(()=>{});
		
	}
	
	_getGridHeight(){
		return this.select.pageSize * 25 + 34;
	}
	
}

module(App.name).controller('SCR1101Controller', SCR1101Controller);