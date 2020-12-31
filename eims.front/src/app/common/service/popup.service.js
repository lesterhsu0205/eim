import { module } from 'angular';
import App from '../../app';
import * as _ from 'lodash';

class PopupService {
	
	constructor($q, $compile, $uibModal) {
		this.$q = $q;
		this.$compile = $compile;
		this.$uibModal = $uibModal;
		this.modal = ['START'];
		this.alert = [];
		this.confirm = [];
		this.loadingBar = null;
	}


	showLoadingBar(scope) {
		const $body = $('body');
		const fontSize = 150;
		const { top, left } = this.calculatePosition(fontSize, fontSize);
		const template = `
			<div class="dim" style="z-index:99999">
				<i class="bxd bxd-setting bxd-spin" 
					style="position: relative; top:${top}px; left:${left}px; color: white; font-size:${fontSize}px; width:${fontSize}px; height:${fontSize}px;"></i>
			</div>
		`;
		
		this.loadingBar = this.$compile(template)(scope); 
		$body.append(this.loadingBar);
	}
	
	closeLoadingBar() {
		this.loadingBar && this.loadingBar.remove();
	}
	
	simpleAlert(scope, text="", close=()=>{}){
		const $body = $('body');
		const width = 460, height = 180;
		const { top, left } = this.calculatePosition(width, height);
		//const ok = bxMsg('common.confirmOk');
		const template = `
			<div class="dim" style="z-index:99999">
				<div class="simple-modal-wrap" style="top:${top}px; left:${left}px;">
					<div style="width: ${width}px;">
						<h2 class="bw-tt a-center" style="height:auto; max-height: 400px; overflow: auto;">${text}</h2>
						<div class="btn-wrap add-mg-t">
							<button id="simpleAlertConfirm" type="button" class="bw-btn-txt">OK</button>
						</div>
					</div>
				</div>
			</div>
		`;
		
		if(this.alert.length > 0){	
			const { $simpleAlert, close } = this.alert.pop();
			close();
			$simpleAlert.remove();
		}
		
		const $simpleAlert = this.$compile(template)(scope); 
		$body.append($simpleAlert);
		this.alert.push({ $simpleAlert, close });
		
		$('#simpleAlertConfirm').click(()=>{
			close();
			$simpleAlert.remove();
		});
	}
	
	detailAlert(scope, text="", detail="", param="", close=()=>{}){
		const $body = $('body');
		const width = 460, height = 180;
		const { top, left } = this.calculatePosition(width, height);
		
		text = text ? text : '';
		detail = detail ? detail : '';
		param = param ? param : '';
		
		const template = `
			<div class="dim" style="z-index:99999">
				<div class="simple-modal-wrap" style="top:${top}px; left:${left}px;">
					<div style="width: ${width}px;">
						<h2 class="bw-tt a-center" style="height:auto; max-height: 200px; overflow: auto;">${text}</h2>
						<span style="font-size: 15px; max-height: 200px; overflow: auto;">${param}</span>
						<div>
							<span id="detailToggle" class="chr-c-blue cs-p">
								상세보기 <i class="bxd bxd-toggle chr-c-blue" style="transform: rotate(180deg);font-size: 12px; width: 12px;"></i>
							</span>
						</div>
						<div id="detailWrapper" style="display: none;" class="detailWrapper">${detail}</div>
						<div class="btn-wrap add-mg-t">
							<button id="detailAlertConfirm" type="button" class="bw-btn-txt">확인</button>
						</div>
					</div>
				</div>
			</div>
		`;
		
		if(this.alert.length > 0){
			const { $simpleAlert, close } = this.alert.pop();
			close();
			$simpleAlert.remove();
		}
		
		const $detailAlert = this.$compile(template)(scope); 
		$body.append($detailAlert);
		this.alert.push({ $simpleAlert: $detailAlert, close });
		
		$('#detailAlertConfirm').click(()=>{
			close();
			$detailAlert.remove();
		});
		
		$('#detailToggle').click(() => {
			let $detail = $('#detailWrapper');
			let $toggle = $('#detailToggle>i');
			
			if($detail.is(':visible')) {
				$detail.hide();
				$toggle.css('transform', 'rotate(180deg)');
			}else {
				$detail.show();
				$toggle.css('transform', '');
			}
		});
	}
	
	pdfTest(scope, template){
		const $body = $('body');
		const width = 460, height = 180;
		const { top, left } = this.calculatePosition(width, height);
		
		if(this.alert.length > 0){
			const { $simpleAlert, close } = this.alert.pop();
			close();
			$simpleAlert.remove();
		}
		
		const $simpleAlert = this.$compile(template)(scope); 
		$body.append($simpleAlert);
		this.alert.push({ $simpleAlert, close });
	}
	
	simpleConfirm(scope, text="", confirm=()=>{}, cancel=()=>{}, confirmTxt = bxMsg('common.confirmOk'), cancelTxt = bxMsg('common.confirmCancel')){
		const $body = $('body');
		const width = 460, height = 180;
		const { top, left } = this.calculatePosition(width, height);
		const template = `
			<div class="dim" style="z-index:99999">
				<div class="simple-modal-wrap" style="top:${top}px; left:${left}px;">
					<div style="width: ${width}px;">
						<h2 class="bw-tt a-center" style="height:auto;">${text}</h2>
						<div class="btn-wrap add-mg-t">
							<button id="simpleConfirmOk" type="button" class="bw-btn-txt on">${confirmTxt}</button>
							<button id="simpleAlertCancel" type="button" class="bw-btn-txt">${cancelTxt}</button>
						</div>
					</div>
				</div>
			</div>
		`;
		
		if(this.confirm.length > 0){
			const { $simpleConfirm } = this.confirm.pop();
			$simpleConfirm.remove();
		}
		
		const $simpleConfirm = this.$compile(template)(scope); 
		$body.append($simpleConfirm);
		this.confirm.push({ $simpleConfirm, cancel });
		
		$('#simpleConfirmOk').click(()=>{
			confirm();
			$simpleConfirm.remove();
		});
		
		$('#simpleAlertCancel').click(()=>{
			cancel();
			$simpleConfirm.remove();
		});
	}
	
	openModal(viewId, data = {}, draggable, resizable){
		let defer = this.$q.defer();
		const templateUrl = `app/views/popup/${viewId}/${viewId}.tpl.html`;
		const controller = `${viewId}Controller`;
		
		$('.ui-tooltip').remove();
		
		const modalInstance = this.$uibModal.open({
			templateUrl: templateUrl,
			controller: controller,
			controllerAs: 'vm',
			resolve: {
				data: () => data
			}
		});
		
		this.modal.push(modalInstance);
		
		modalInstance.rendered
			.then(() => {			
				if(draggable) {
					 $('.simple-modal-wrap').draggable({
		                cancel: '.search-wrap, .btn-wrap, #gridWrap',
		                containment: 'body',
		                cursor: 'auto',
		            });
				}
				
				if(resizable) {
					 $('.simple-modal-wrap').resizable();
				}
			});
		
		modalInstance.result
			.then(result => {
				this.modal.pop();
				defer.resolve(result);
				$('.ui-tooltip').remove();
			})
			.catch(cancel => {
				this.modal.pop();
				defer.reject(cancel);
				$('.ui-tooltip').remove();
			});
		
		modalInstance.closed
			.then(result => {
				$('[aria-describedby]').focusout();
			})
			
		return defer.promise;
	}
	
	calculatePosition(contentWidth, contentHeight) {
		const {innerWidth: width, innerHeight: height} = window;
		
		return { 
			top : Math.round(( height - contentHeight ) / 2), 
			left : Math.round(( width - contentWidth ) / 2)
		}
	}
	
	getModalZIndex(){
		return 1000 + this.modal.length;
	}
}

module(App.name).service('popupService', PopupService);