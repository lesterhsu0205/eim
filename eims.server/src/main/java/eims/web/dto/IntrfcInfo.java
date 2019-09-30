package eims.web.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import eims.web.dto.table.IntrfcMsgFieldEncodingDto;
import eims.web.dto.table.IntrfccombsDetailCCDto;
import eims.web.dto.table.IntrfccombsDetailEAIDto;
import eims.web.dto.table.IntrfccombsDetailFEPDto;
import eims.web.dto.table.IntrfccombsDetailMCIDto;
import eims.web.dto.table.IntrfccombsMappingDto;
import eims.web.dto.table.IntrfcmsglayoutdtDto;
import eims.web.dto.table.IntrfcroutinfodtDto;
import eims.web.dto.table.IntrfcsrsysdtDto;

@JsonInclude(value = Include.NON_ABSENT, content = Include.NON_EMPTY)
public class IntrfcInfo {

	String intrfcId; // 인터페이스ID
	String intrfcNm; // 인터페이스명
	String intrfcWayCd; // 인터페이스방식코드 - aptoap aptodb 등
	String workStatusCd; // 작업상태코드
	String regManId; // 등록자ID
	String regDttm; // 등록일시
	String msgTrnsfrmYn; // 전문변환여부
	String trxCd; // 거래코드 -- ***미사용***
	String bizCd; // 업무코드
	String instCd; // 기관코드
	String trxDscd; // 발생유형 - 온라인 배치
	String intrfcTypeCd; // 인터페이스타입코드
	String lv1Cd; // 레벨1코드
	String lv2Cd; // 레벨2코드
	String lv3Cd; // 레벨3코드
	String lv4Cd; // 레벨4코드
	String lv5Cd; // 레벨5코드
	String syncAsyncDscd; // 동기비동기구분코드
	String srTypeCd; // 요청응답타입코드
	String rqstExtrnlMsgNo; // 요청대외전문번호
	String rspsExtrnlMsgNo; // 응답대외전문번호
	String rspsYn; // 응답여부
	String trxTypeDscd; // 거래유형코드
	String viewId;
	String protocolDscd;
	String httpMethod;
	String contextUrl;
	String requestMediaType;
	String responseMediaType;
	String execEnvDscd;
	List<IntrfcmsglayoutdtDto> intrfcmsglayoutdtDto; //전문레이아웃정보
	List<IntrfcsrsysdtDto> intrfcsrsysdtDto; //요청응답시스템정보
	List<IntrfcroutinfodtDto> intrfcroutinfodtDto;
	List<IntrfccombsMappingDto> intrfccombsMappingReqDto; //요청매핑정보
	List<IntrfccombsMappingDto> intrfccombsMappingResDto; //응답매핑정보
	IntrfccombsDetailEAIDto eaiDetailDto; //EAI 상세 정보
	IntrfccombsDetailMCIDto mciDetailDto; //MCI 상세 정보
	IntrfccombsDetailFEPDto fepDetailDto; //FEP 상세 정보
	/** 
	 * 2018.10.19 추가
	 * */
	List<IntrfcMsgFieldEncodingDto> intrfcMsgFieldEncodingDto; //필드인코딩설정
	
	/**
	 * 2018.11.27 추가
	 */
	IntrfccombsDetailCCDto ccDetailDto ;
	
	

	public IntrfccombsDetailCCDto getCcDetailDto() {
		return ccDetailDto;
	}

	public void setCcDetailDto(IntrfccombsDetailCCDto ccDetailDto) {
		this.ccDetailDto = ccDetailDto;
	}

	public List<IntrfcMsgFieldEncodingDto> getIntrfcMsgFieldEncodingDto() {
		return intrfcMsgFieldEncodingDto;
	}

	public void setIntrfcMsgFieldEncodingDto(List<IntrfcMsgFieldEncodingDto> intrfcMsgFieldEncodingDto) {
		this.intrfcMsgFieldEncodingDto = intrfcMsgFieldEncodingDto;
	}

	public String getTrxTypeDscd() {
		return trxTypeDscd;
	}

	public void setTrxTypeDscd(String trxTypeDscd) {
		this.trxTypeDscd = trxTypeDscd;
	}

	public List<IntrfcroutinfodtDto> getIntrfcroutinfodtDto() {
		return intrfcroutinfodtDto;
	}

	public void setIntrfcroutinfodtDto(List<IntrfcroutinfodtDto> intrfcroutinfodtDto) {
		this.intrfcroutinfodtDto = intrfcroutinfodtDto;
	}

	public String getRqstExtrnlMsgNo() {
		return rqstExtrnlMsgNo;
	}

	public void setRqstExtrnlMsgNo(String rqstExtrnlMsgNo) {
		this.rqstExtrnlMsgNo = rqstExtrnlMsgNo;
	}

	public String getRspsExtrnlMsgNo() {
		return rspsExtrnlMsgNo;
	}

	public void setRspsExtrnlMsgNo(String rspsExtrnlMsgNo) {
		this.rspsExtrnlMsgNo = rspsExtrnlMsgNo;
	}

	public String getRspsYn() {
		return rspsYn;
	}

	public void setRspsYn(String rspsYn) {
		this.rspsYn = rspsYn;
	}

	public String getSyncAsyncDscd() {
		return syncAsyncDscd;
	}

	public void setSyncAsyncDscd(String syncAsyncDscd) {
		this.syncAsyncDscd = syncAsyncDscd;
	}

	public String getSrTypeCd() {
		return srTypeCd;
	}

	public void setSrTypeCd(String srTypeCd) {
		this.srTypeCd = srTypeCd;
	}

	public String getIntrfcId() {
		return intrfcId;
	}

	public void setIntrfcId(String intrfcId) {
		this.intrfcId = intrfcId;
	}

	public String getIntrfcNm() {
		return intrfcNm;
	}

	public void setIntrfcNm(String intrfcNm) {
		this.intrfcNm = intrfcNm;
	}

	public String getIntrfcWayCd() {
		return intrfcWayCd;
	}

	public void setIntrfcWayCd(String intrfcWayCd) {
		this.intrfcWayCd = intrfcWayCd;
	}

	public String getWorkStatusCd() {
		return workStatusCd;
	}

	public void setWorkStatusCd(String workStatusCd) {
		this.workStatusCd = workStatusCd;
	}

	public String getRegManId() {
		return regManId;
	}

	public void setRegManId(String regManId) {
		this.regManId = regManId;
	}

	public String getRegDttm() {
		return regDttm;
	}

	public void setRegDttm(String regDttm) {
		this.regDttm = regDttm;
	}

	public String getMsgTrnsfrmYn() {
		return msgTrnsfrmYn;
	}

	public void setMsgTrnsfrmYn(String msgTrnsfrmYn) {
		this.msgTrnsfrmYn = msgTrnsfrmYn;
	}

	public String getTrxCd() {
		return trxCd;
	}

	public void setTrxCd(String trxCd) {
		this.trxCd = trxCd;
	}

	public String getBizCd() {
		return bizCd;
	}

	public void setBizCd(String bizCd) {
		this.bizCd = bizCd;
	}

	public String getInstCd() {
		return instCd;
	}

	public void setInstCd(String instCd) {
		this.instCd = instCd;
	}

	public String getTrxDscd() {
		return trxDscd;
	}

	public void setTrxDscd(String trxDscd) {
		this.trxDscd = trxDscd;
	}

	public String getIntrfcTypeCd() {
		return intrfcTypeCd;
	}

	public void setIntrfcTypeCd(String intrfcTypeCd) {
		this.intrfcTypeCd = intrfcTypeCd;
	}

	public String getLv1Cd() {
		return lv1Cd;
	}

	public void setLv1Cd(String lv1Cd) {
		this.lv1Cd = lv1Cd;
	}

	public String getLv2Cd() {
		return lv2Cd;
	}

	public void setLv2Cd(String lv2Cd) {
		this.lv2Cd = lv2Cd;
	}

	public String getLv3Cd() {
		return lv3Cd;
	}

	public void setLv3Cd(String lv3Cd) {
		this.lv3Cd = lv3Cd;
	}

	public String getLv4Cd() {
		return lv4Cd;
	}

	public void setLv4Cd(String lv4Cd) {
		this.lv4Cd = lv4Cd;
	}

	public String getLv5Cd() {
		return lv5Cd;
	}

	public void setLv5Cd(String lv5Cd) {
		this.lv5Cd = lv5Cd;
	}

	public List<IntrfcmsglayoutdtDto> getIntrfcmsglayoutdtDto() {
		return intrfcmsglayoutdtDto;
	}

	public void setIntrfcmsglayoutdtDto(List<IntrfcmsglayoutdtDto> intrfcmsglayoutdtDto) {
		this.intrfcmsglayoutdtDto = intrfcmsglayoutdtDto;
	}

	public List<IntrfcsrsysdtDto> getIntrfcsrsysdtDto() {
		return intrfcsrsysdtDto;
	}

	public void setIntrfcsrsysdtDto(List<IntrfcsrsysdtDto> intrfcsrsysdtDto) {
		this.intrfcsrsysdtDto = intrfcsrsysdtDto;
	}

	public List<IntrfccombsMappingDto> getIntrfccombsMappingReqDto() {
		return intrfccombsMappingReqDto;
	}

	public void setIntrfccombsMappingReqDto(List<IntrfccombsMappingDto> intrfccombsMappingReqDto) {
		this.intrfccombsMappingReqDto = intrfccombsMappingReqDto;
	}

	public List<IntrfccombsMappingDto> getIntrfccombsMappingResDto() {
		return intrfccombsMappingResDto;
	}

	public void setIntrfccombsMappingResDto(List<IntrfccombsMappingDto> intrfccombsMappingResDto) {
		this.intrfccombsMappingResDto = intrfccombsMappingResDto;
	}

	public IntrfccombsDetailEAIDto getEaiDetailDto() {
		return eaiDetailDto;
	}

	public void setEaiDetailDto(IntrfccombsDetailEAIDto eaiDetailDto) {
		this.eaiDetailDto = eaiDetailDto;
	}

	public IntrfccombsDetailMCIDto getMciDetailDto() {
		return mciDetailDto;
	}

	public void setMciDetailDto(IntrfccombsDetailMCIDto mciDetailDto) {
		this.mciDetailDto = mciDetailDto;
	}

	public IntrfccombsDetailFEPDto getFepDetailDto() {
		return fepDetailDto;
	}

	public void setFepDetailDto(IntrfccombsDetailFEPDto fepDetailDto) {
		this.fepDetailDto = fepDetailDto;
	}

	public String getViewId() {
		return viewId;
	}

	public void setViewId(String viewId) {
		this.viewId = viewId;
	}

	public String getProtocolDscd() {
		return protocolDscd;
	}

	public void setProtocolDscd(String protocolDscd) {
		this.protocolDscd = protocolDscd;
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	public String getContextUrl() {
		return contextUrl;
	}

	public void setContextUrl(String contextUrl) {
		this.contextUrl = contextUrl;
	}

	public String getRequestMediaType() {
		return requestMediaType;
	}

	public void setRequestMediaType(String requestMediaType) {
		this.requestMediaType = requestMediaType;
	}

	public String getResponseMediaType() {
		return responseMediaType;
	}

	public void setResponseMediaType(String responseMediaType) {
		this.responseMediaType = responseMediaType;
	}

	public String getExecEnvDscd() {
		return execEnvDscd;
	}

	public void setExecEnvDscd(String execEnvDscd) {
		this.execEnvDscd = execEnvDscd;
	}
	
	
	

}