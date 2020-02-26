package eims.web.dto.table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_ABSENT, content = Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class IntrfccombsListDto {

	String intrfcId; // $col.colComment
	String intrfcNm; // $col.colComment
	String intrfcNmSub; // $col.colComment
	String intrfcWayCd; // 인터페이스방식코드
	String workStatusCd; // $col.colComment
	String regManId; // 등록자ID
	String regDttm; // $col.colComment
	String msgTrnsfrmYn; // $col.colComment
	String trxCd; // $col.colComment
	String bizCd; // $col.colComment
	String instCd; // $col.colComment
	String trxDscd; // 거래타입코드
	String intrfcTypeCd; // 인터페이스타입코드
	String lv1Cd; // 레벨1코드
	String lv2Cd; // 레벨2코드
	String lv3Cd; // 레벨3코드
	String lv4Cd; // 레벨4코드
	String lv5Cd; // 레벨5코드
	String rawData;
	int srSeqAsS;
	String srTypeCdS;
	String sysCdS;
	int srSeqAsR;
	String srTypeCdR;
	String sysCdR;
	String rqstExtrnlMsgNo; //요청전문번호
	String rspsExtrnlMsgNo; //응답전문번호
	String trxTypeDscd;
	String viewId;
	String execEnvDscd;
	String syncAsyncDscd;
	String rspsYn;
	

	
	public String getIntrfcNmSub() {
		return intrfcNmSub;
	}

	public void setIntrfcNmSub(String intrfcNmSub) {
		this.intrfcNmSub = intrfcNmSub;
	}

	public String getTrxTypeDscd() {
		return trxTypeDscd;
	}

	public void setTrxTypeDscd(String trxTypeDscd) {
		this.trxTypeDscd = trxTypeDscd;
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

	public String getRawData() {
		return rawData;
	}

	public void setRawData(String rawData) {
		this.rawData = rawData;
	}

	public int getSrSeqAsS() {
		return srSeqAsS;
	}

	public void setSrSeqAsS(int srSeqAsS) {
		this.srSeqAsS = srSeqAsS;
	}

	public String getSrTypeCdS() {
		return srTypeCdS;
	}

	public void setSrTypeCdS(String srTypeCdS) {
		this.srTypeCdS = srTypeCdS;
	}

	public String getSysCdS() {
		return sysCdS;
	}

	public void setSysCdS(String sysCdS) {
		this.sysCdS = sysCdS;
	}

	public int getSrSeqAsR() {
		return srSeqAsR;
	}

	public void setSrSeqAsR(int srSeqAsR) {
		this.srSeqAsR = srSeqAsR;
	}

	public String getSrTypeCdR() {
		return srTypeCdR;
	}

	public void setSrTypeCdR(String srTypeCdR) {
		this.srTypeCdR = srTypeCdR;
	}

	public String getSysCdR() {
		return sysCdR;
	}

	public void setSysCdR(String sysCdR) {
		this.sysCdR = sysCdR;
	}

	public String getViewId() {
		return viewId;
	}

	public void setViewId(String viewId) {
		this.viewId = viewId;
	}

	public String getExecEnvDscd() {
		return execEnvDscd;
	}

	public void setExecEnvDscd(String execEnvDscd) {
		this.execEnvDscd = execEnvDscd;
	}

	public String getSyncAsyncDscd() {
		return syncAsyncDscd;
	}

	public void setSyncAsyncDscd(String syncAsyncDscd) {
		this.syncAsyncDscd = syncAsyncDscd;
	}

	public String getRspsYn() {
		return rspsYn;
	}

	public void setRspsYn(String rspsYn) {
		this.rspsYn = rspsYn;
	}
	
	

}