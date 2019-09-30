package eims.web.dto.table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_ABSENT, content = Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MsgIdCreateDto {
	String msgLayoutId; // 전문ID
	String chlDscd; // 채널구분
	String trxDscd; //거래구분코드
	String msgDscd; // 전문구분코드
	String lv1Cd; // 레벨1코드
	String lv2Cd; // 레벨2코드
	String lv3Cd; // 레벨3코드
	String sendSysCd; //송신시스템코드
	String receiveSysCd; //수신시스템코드
	String extrnlBizNm; // 기관업무구분코드
	Integer msgVersion; // 전문버전
	String fileId; //파일ID
	String msgNumber; //전문번호
	String seq;

	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

	public String getMsgNumber() {
		return msgNumber;
	}

	public void setMsgNumber(String msgNumber) {
		this.msgNumber = msgNumber;
	}

	public String getMsgLayoutId() {
		return msgLayoutId;
	}

	public void setMsgLayoutId(String msgLayoutId) {
		this.msgLayoutId = msgLayoutId;
	}

	public String getChlDscd() {
		return chlDscd;
	}

	public void setChlDscd(String chlDscd) {
		this.chlDscd = chlDscd;
	}

	public String getTrxDscd() {
		return trxDscd;
	}

	public void setTrxDscd(String trxDscd) {
		this.trxDscd = trxDscd;
	}

	public String getMsgDscd() {
		return msgDscd;
	}

	public void setMsgDscd(String msgDscd) {
		this.msgDscd = msgDscd;
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

	public String getSendSysCd() {
		return sendSysCd;
	}

	public void setSendSysCd(String sendSysCd) {
		this.sendSysCd = sendSysCd;
	}

	public String getReceiveSysCd() {
		return receiveSysCd;
	}

	public void setReceiveSysCd(String receiveSysCd) {
		this.receiveSysCd = receiveSysCd;
	}

	public String getExtrnlBizNm() {
		return extrnlBizNm;
	}

	public void setExtrnlBizNm(String extrnlBizNm) {
		this.extrnlBizNm = extrnlBizNm;
	}

	public Integer getMsgVersion() {
		return msgVersion;
	}

	public void setMsgVersion(Integer msgVersion) {
		this.msgVersion = msgVersion;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

}