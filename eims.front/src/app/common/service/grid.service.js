import { module } from 'angular';
import App from '../../app';
import * as _ from 'lodash';

class GridService {
	
	constructor(utilService, metaService, $timeout) {
		this. utilService = utilService;
		this.metaService = metaService;
		this.$timeout = $timeout;
	}

	getGridHeight(pageSize = 5){
		return pageSize * 24 + 33;
	}
	
	getNoField(limit, index, pageNumber = 1){
		return index + 1 + ( pageNumber - 1 ) * limit;
	}
	
	getSelect(pageSize = 5){
		return { pageSize };
	}
	
	getPageInfo(select, pageNumber = 1){
		return {
			pageNumber: pageNumber || 1,
			pageSize: select.pageSize
		};
	}

	addRecord(options = {},  generateFunc = () => {}, rowData = {}){
		if(_.isEmpty(options)) throw new Error('option값은 필수값입니다.');
		
		const grid = w2ui[options.name];
		const records = grid.records;
		const newRow = [];
		let isSelected = !_.isEmpty(rowData);
		
		switch(options.selectType){
			case 'cell':
			case 'column':
				const selectedIndics = [];
				const selection = grid.getSelection();
				
				if(selection.length === 0){
					newRow.push(generateFunc({}, isSelected ? rowData.recid : ''));
				} else {
					selection.map(field => {
						if(selectedIndics.indexOf(field.index) === -1){
							selectedIndics.push(field.index);
							newRow.push(generateFunc({}, isSelected ? rowData.recid : ''));
						}
					});
					
					isSelected = true;
					rowData = grid.records[selectedIndics[selectedIndics.length - 1]];
				}
				break;
			case 'row':
			default:
				newRow.push(generateFunc({}, isSelected ? rowData.recid : ''));
				break;
		}
		
		isSelected ? records.splice(records.indexOf(rowData) + 1, 0 , ...newRow) 
				: records.push(...newRow);
	}
	
	removeSelected(options){
		if(_.isEmpty(options)) throw new Error('option값은 필수값입니다.');
		
		const grid = w2ui[options.name];
		const selection = grid.getSelection();
		
		switch(options.selectType){
		case 'cell':
		case 'column':
			const recids = [];
			selection.map(v => {
				if(recids.indexOf(v.recid) === -1){
					recids.push(v.recid);
				}
			});
			grid.remove(...recids);
			break;
		case 'row':
		default:
			grid.remove(...selection);
			break;
		}
		
		grid.selectNone();
//		TODO : ROW 삭제후 AutoComplete 완성 안됨 => 수정
//		if(selection.length === 1 && selection[0].column === 2){
//			grid.moved = true;
//		}
	}
	
	upSelected(options){
		if(_.isEmpty(options)) throw new Error('option값은 필수값입니다.');
		
		const grid = w2ui[options.name];
		const records = grid.records;
		const selection = grid.getSelection();
		let selectedIndics = [];
		
		switch(options.selectType){
		case 'cell':
		case 'column':
			selection.map(field => {
				if(selectedIndics.indexOf(field.index) === -1){
					selectedIndics.push(field.index);
				}
			});
			break;
		case 'row':
		default:
			selectedIndics = selection.map(id => grid.get(id, true));
			break;
		}
		
		if(selection.length === 0 || selectedIndics[0] === 0) return;
		
		selectedIndics.map(index => {
			const target = records.splice(index, 1);
			records.splice(index - 1, 0, target[0]);
		});
		
		grid.selectNone();
		grid.select(...selection);
		
		if(selection.length === 1 && selection[0].column === 2){
			grid.moved = true;
		}
	}
	
	downSelected(options){
		if(_.isEmpty(options)) throw new Error('option값은 필수값입니다.');
		
		const grid = w2ui[options.name];
		const records = grid.records;
		const selection = grid.getSelection();
		let selectedIndics = [];
		
		switch(options.selectType){
		case 'cell':
		case 'column':
			selection.map(field => {
				if(selectedIndics.indexOf(field.index) === -1){
					selectedIndics.push(field.index);
				}
			});
			break;
		case 'row':
		default:
			selectedIndics = selection.map(id => grid.get(id, true));
			break;
		}
		
		if(selection.length === 0 || selectedIndics[selectedIndics.length - 1] === records.length - 1) {
			return;
		}
		
		selectedIndics.reverse();
		selectedIndics.map(index => {
			const target = records.splice(index, 1);
			records.splice(index + 1, 0, target[0]);
		});
		
		grid.selectNone();
		grid.select(...selection);

		if(selection.length === 1 && selection[0].column === 2){
			grid.moved = true;
		}
	}
	
	paste(e, options = {}, generateFunc = () => {}, changeCodeFunc = () => {}) {
		const grid = w2ui[options.name];	
		const pasedData = this._parsePasteData(e.text, e.column, options, changeCodeFunc);
		const records = grid.records;
		const rowData = grid.get(grid.getSelection()[0]['recid']);
		
		let parentIndex = records.indexOf(rowData);
		let parentFldUnqId = records[parentIndex].fldUnqId;
		pasedData.map((data) => {
			let record = records[parentIndex];
			
			if(record && record.fldUnqId) {
				records[parentIndex] = $.extend(record, data);
			}else{
				records[parentIndex] = generateFunc(data, parentFldUnqId);
			}
			
			parentIndex = parentIndex + 1;
		});
	}
	
	pasteForMsg(e, options = {}, generateFunc = () => {}, changeCodeFunc = () => {}) {
		const grid = w2ui[options.name];	
		const pasedData = this._parsePasteData(e.text, e.column, options, changeCodeFunc);
		const records = grid.records;
		const rowData = grid.get(grid.getSelection()[0]['recid']);
		
//		for (var i = 0; i < pasedData.length; i++) {
//			if(pasedData[i].alignNm == undefined || _.isEmpty(pasedData[i].alignNm)){
//				if(pasedData[i].dataTypeNm === 'STRING' || pasedData[i].dataTypeNm === 'BYTEARRAY') {
//					pasedData[i].alignNm = 'LEFT';
//				}else if(pasedData[i].dataTypeNm === 'INTEGER' || pasedData[i].dataTypeNm === 'BIGDECIMAL') {
//					pasedData[i].alignNm = 'RIGHT';
//				}else{
//					pasedData[i].alignNm = '';
//				}
//			}
//		}
		
		
		let parentIndex = records.indexOf(rowData);
		let parentFldUnqId = records[parentIndex].fldUnqId;
		pasedData.map((data) => {
			let record = records[parentIndex];
			
			if(record && record.fldUnqId) {
				records[parentIndex] = $.extend(record, data);
			}else{
				records[parentIndex] = generateFunc(data, parentFldUnqId);
			}
			
			if(records[parentIndex].alignNm == undefined || _.isEmpty(records[parentIndex].alignNm)){
				if(records[parentIndex].dataTypeNm === 'STRING' || records[parentIndex].dataTypeNm === 'BYTEARRAY') {
					records[parentIndex].alignNm = 'LEFT';
				}else if(records[parentIndex].dataTypeNm === 'INTEGER' || records[parentIndex].dataTypeNm === 'BIGDECIMAL') {
					records[parentIndex].alignNm = 'RIGHT';
				}else{
					records[parentIndex].alignNm = '';
				}
			}
			
			if(records[parentIndex]['w2ui'] && records[parentIndex]['w2ui']['changes']) {
				let changes = records[parentIndex]['w2ui']['changes'];
				
				for(let key in changes){
					if(changes.hasOwnProperty(key)){
						if(data[key] != undefined && data[key] != null){
							changes[key] = data[key];
						}
					}
				}
			}
				
			parentIndex = parentIndex + 1;
		});
	}
	
	_parsePasteData(text, startIndex, options, changeCodeFunc){
		const {columns, recid} = options;
		const hasRecId = recid !== null;
		const columnKeys = columns.map(v => v.field === 'recid' ? null : v.field)
								.filter(v => v);

		return text.trim()
			.split(/\n/)
			.map(v => {
				let obj = {};

				v.split(/\t/)
					.map((v, i) => {
						const _v = v.trim().replace(/"/g, '');
						const key = columnKeys[i + startIndex];
						
						if(key === 'dataTypeNm'){ 
							obj[key] = changeCodeFunc('dataTypeNm', _v);
						}else if(key === 'fillerVal'){ 
							obj[key] = changeCodeFunc('fillerVal', _v);
						}else{
							obj[key] = _v;
						}
					});
				
				return obj;
			})
			.filter(obj => {
				const keys = Object.getOwnPropertyNames(obj);
				let i = 0;
				const tmp = hasRecId ? 0 : 1; 
				
				keys.map((k, idx) => {
					const v = obj[k];
					const _i = idx + tmp;
					if (columns[_i].caption === v) i++;
				});
			
				const isHeader = i === keys.length;
				
				return !isHeader;
			});
	}
	
	isAutoCompleteField(options, e, empty){
		if(_.isEmpty(options.autoCompleteIndics)) return false;
		return options.autoCompleteIndics.indexOf(e.column) !== -1;
	}
	
	autoComplete(e, controller, options){
		let first = true;
		let prevVal = '';
		let target = e.target;
		let selector = `div[name=${target}] .w2ui-editable input`;
		let column = e.column;

		let $searchDataWrap = $('<ul>')
			.addClass('search-data-wrap')
			.attr('style', 'width:100%;')
			.click('li', (e, item) => {
				let meta = $(e.target).data('meta');
				controller.onSelectAutoComplete(meta, null, column);
			});

		let offsetTop;
		if(e.originalEvent.type === 'keydown'){
			offsetTop = $('#grid_' + e.target + '_data_' + e.index + '_' + column).offset().top + 1;
		}else if(e.originalEvent.type === 'click'){
			offsetTop = $(e.originalEvent.target).offset().top;
		}
		
		setTimeout(() => {
			const $input = $(selector);
			const $parent = $input.parent();
			
			if($input.length === 0) return
			options.autoComplete = true;
			$input.data('keep-open', true);
			$input.parent().css('position', 'static');
			$input.after($searchDataWrap);
			
			$input.keydown((e) => {
				if (e.keyCode === 13) {
					let $activeLi = $searchDataWrap.find('li.active');
					let meta = $activeLi.data('meta');
					
					controller.onSelectAutoComplete(meta, null, column);
					e.preventDefault();
				}
			});
			
			$input.keyup((e)=> {
				const val = e.target.value;
				const upKeyCode = 38;
				const downKeyCode = 40;
				const $li = $searchDataWrap.find('li');
				const $activeLi = $searchDataWrap.find('li.active');
				const hasActive = $activeLi.length > 0;
				const idx = $li.index($activeLi);
				
				if (controller.searchItems && e.keyCode === downKeyCode) {
					// ie에서 처음에 두번 호출되는 현상이 발생
					if(first) {
						if(!!navigator.userAgent.match(/Trident/g) || !!navigator.userAgent.match(/MSIE/g)) {
							first = false;
							return;
						}
					}
					
					if (hasActive) {
						let $nextLi = $($li.get(idx + 1));
						let nextLiHeight = $nextLi.height();
						let searchDataWrapHeight = $searchDataWrap.height();
						
						if($nextLi.length){
							$activeLi.removeClass('active');
							$nextLi.addClass('active');
							
							if($nextLi.position().top + nextLiHeight > searchDataWrapHeight){
								$searchDataWrap.scrollTop($searchDataWrap.scrollTop() + nextLiHeight);
							}	
						}
						
					}
					else {
						$($li.get(0)).addClass('active');
					}
					
					return;
				}
				if (controller.searchItems && e.keyCode === upKeyCode) {
					if (hasActive) {
						let $prevLi = $($li.get(idx - 1));
						let prevLiHeight = $prevLi.height();
						
						if(idx !== 0){
							$activeLi.removeClass('active');
							$prevLi.addClass('active');
							
							if($prevLi.position().top < 0){
								$searchDataWrap.scrollTop($searchDataWrap.scrollTop() - prevLiHeight);
							}	
						}
						
					}
					else {
						$($li.get(0)).addClass('active');
					}
					
					return;
				}
				
				if (prevVal === val || controller.utilService.isEmpty(val)) return;
				
				// 자동 완성 데이터 처리 
				controller.searchItems = column === 1 ? this.metaService.getMetaListLikeEngNm(val) : this.metaService.getMetaListLikeKorNm(val);
				
				// first 처리
				first = true;
				
				// 팝업 렌더 
				let $lis = [];
				controller.searchItems.map((item, idx) => {
					let $li = $('<li>').data('meta', item).html(column === 1 ? item.metaEngNm : item.metaKorNm);
					$lis.push($li);
				});
				
				$searchDataWrap.html($lis);
				$searchDataWrap.find('li').first().addClass('active');
				
				// top 처리 
				let height = $searchDataWrap.height();
				if(offsetTop > 500 && $('#msgDetailWrap').offset().top - offsetTop < -200){
					$searchDataWrap.css('top', -(height + 2));
				}else{
					$searchDataWrap.css('top', 25);
				}

			});
			
			let clicky;
			$parent.mousedown((e) => {
				clicky = $(e.target);
			});
			
			$parent.mouseup((e) => {
				clicky = null;
			});
			
			$input.blur((e) =>{
				if(clicky) {
					$(e.target).focus();
					return;
				}
				
				setTimeout(()=> controller.onSelectAutoComplete(null, $input, column),300);
			});
		});
	}
	
	_isEmptyGridAndRowData(grid, rowData){
		return _.isEmpty(grid) || _.isEmpty(rowData);
	}
	
	convertDataToTreeData(data = [], recidNm){
		const utilService = this.utilService;
		let condition;
		let treeData = [];
		
		data.map(v => {
			utilService.isEmpty(v[recidNm]) 
				? treeData.push(v) 
				: this._findAndApply(treeData, v, condition, recidNm);
			condition = false;
		});
		
		return treeData;
	}
	
	convertDataToTreeData2(data = [], recidNm, selfId){
		const utilService = this.utilService;
		let condition;
		let treeData = [];
		
		data.map(v => {
			utilService.isEmpty(v[recidNm]) 
				? treeData.push(v) 
				: this._findAndApply2(treeData, v, condition, recidNm);
			condition = false;
		});
		
		
		/*
		data.map(v => {
			treeData.push(v);
		});
		*/
		return treeData;
	}
	
	_findAndApply(targets = [], record, condition,recidNm){
		if (condition) return;
		const parentFieldName = record[recidNm];
		
		for (let target of targets) {
			const targetId = target.id; 
			const w2ui = target.w2ui;
			
			if (targetId === parentFieldName) {
				if (!w2ui) {
					target.w2ui = {
						children:[]
					};
				}
				target.w2ui.children.push(record);
				condition = true;
				break;
			} else {
				if (!w2ui) continue;
				else this._findAndApply(target.w2ui.children, record ,condition);
			}
		}
		
	}
	
	_findAndApply2(targets = [], record, condition,recidNm){
		if (condition) return;
		const parentFieldName = record[recidNm];
		
		for (let target of targets) {
			const targetId = target.appCd; 
			const w2ui = target.w2ui;
			
			if (targetId === parentFieldName) {
				
				if (!w2ui) {
					target.w2ui = {
						children:[]
					};
				}
				target.w2ui.children.push(record);
				condition = true;
				break;
			} else {
				if (!w2ui) continue;
				else this._findAndApply2(target.w2ui.children, record ,condition,recidNm);
			}
		}
		
	}
	
	expandAll(options, level){
		const grid = w2ui[options.name];
		const recordsClone = [];
		
		recordsClone.push(...grid.records);
		recordsClone.map(node => this._expandSub(grid, node, level));
	}
	
	_expandSub(grid, node, level){
		if(level && node.lvCd > level) {
			return;
		}
		
		grid.expand(node.recid);
		
		if(node.w2ui && node.w2ui.children){
			const childrenClone = [];
			
			childrenClone.push(...node.w2ui.children);
			childrenClone.map(subNode => {
				subNode.w2ui.parent_recid = node.recid;
				this._expandSub(grid, subNode, level);
			});
		}
	}
	
	collapseAll(options){
		const grid = w2ui[options.name];
		
		grid.records.filter(node => _.isEmpty(node.w2ui) || _.isEmpty(node.w2ui.parent_recid))
					.map(node => grid.collapse(node.recid));
	}
	
	getSelectItemsFromCodes(codes, defaultArray = []){
		codes.map(code => {
			defaultArray.push({
				id: code.cdVal,
				text: code.cdValNm
			});
		});
		return defaultArray;
	}
	
	convertTreeDataToArray(treeData = [], parentFld){
		const target = [];
		treeData.filter(v => this._isRootNode(v))
				.map(node => this._registChildrenToTarget(target, node, parentFld));
		return target;
	}
	
	_isRootNode(node){
		return _.isEmpty(node.w2ui) || node.w2ui.parent_recid == undefined;
	}
	
	_registChildrenToTarget(target, node, parentFld){
		target.push(node);
		
		if(!_.isEmpty(node.w2ui) && !_.isEmpty(node.w2ui.children)){
			node.w2ui.children.map(child => {
				this._registChildrenToTarget(target, child, parentFld);
				child.parentFldNm = node[parentFld];
			});
		}
		
		this._removeW2uiData(node);
	}
	
	_removeW2uiData(node){
		delete node.w2ui;
	}
	
	syncScroll(option1, option2){
		setTimeout(()=>{
			const $option1 = $(`#grid_${option1.name}_records`);
			const $option2 = $(`#grid_${option2.name}_records`);
			
			$option1.on("scroll", (e) => {
				$option2.scrollTop($option1.scrollTop());
			});
		}, 500);
	}
	
	onClickZabara(that, optionNames, afterFn){
		optionNames.map(optionName => {
			const option = that[optionName];
			const grid = w2ui[option.name];
			
			grid.refresh();
			option.records = grid.records;
			
			afterFn && afterFn();
		});
	}
	
	changeBooleanToYN(target){
		let regExp = new RegExp('Yn$');
		
		Object.getOwnPropertyNames(target)
			  .filter(v => regExp.test(v))
			  .map(v => target[v] = target[v] === 'Y' || target[v] === true  ? 'Y' : 'N');
	}
	
	changeYNToBoolean(target){
		let regExp = new RegExp('Yn$');
		
		Object.getOwnPropertyNames(target)
			  .filter(v => regExp.test(v))
			  .map(v => target[v] = target[v] === 'Y' || target[v] === true ? true : false);
	}

}

module(App.name).service('gridService', GridService);