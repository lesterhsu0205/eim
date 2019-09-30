package eims.web.dto.table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_ABSENT, content = Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class IntrfcmsglayoutdtDto {

	String intrfcId; // 인터페이스ID
	String srTypeCd; // 송수신타입코드
	int srSeq; // 송수신 시퀀스
	String rqstRspsTypeCd; // 요청응답타입코드
	int rqstRspsSeq; // 요청응답시퀀스
	String sysCd; // 송수신시스템코드
	String msgLayoutId; // 메시지레이아웃ID
	MsglayoutbsDto msglayoutbsDto; //메시지레이아웃상세정보

	public MsglayoutbsDto getMsglayoutbsDto() {
		return msglayoutbsDto;
	}

	public void setMsglayoutbsDto(MsglayoutbsDto msglayoutbsDto) {
		this.msglayoutbsDto = msglayoutbsDto;
	}

	public String getIntrfcId() {
		return intrfcId;
	}

	public void setIntrfcId(String intrfcId) {
		this.intrfcId = intrfcId;
	}

	public String getSrTypeCd() {
		return srTypeCd;
	}

	public void setSrTypeCd(String srTypeCd) {
		this.srTypeCd = srTypeCd;
	}

	public int getSrSeq() {
		return srSeq;
	}

	public void setSrSeq(int srSeq) {
		this.srSeq = srSeq;
	}

	public String getRqstRspsTypeCd() {
		return rqstRspsTypeCd;
	}

	public void setRqstRspsTypeCd(String rqstRspsTypeCd) {
		this.rqstRspsTypeCd = rqstRspsTypeCd;
	}

	public int getRqstRspsSeq() {
		return rqstRspsSeq;
	}

	public void setRqstRspsSeq(int rqstRspsSeq) {
		this.rqstRspsSeq = rqstRspsSeq;
	}

	public String getSysCd() {
		return sysCd;
	}

	public void setSysCd(String sysCd) {
		this.sysCd = sysCd;
	}

	public String getMsgLayoutId() {
		return msgLayoutId;
	}

	public void setMsgLayoutId(String msgLayoutId) {
		this.msgLayoutId = msgLayoutId;
	}

	@Override
	public String toString() {
		return "IntrfcmsglayoutdtDto [intrfcId=" + intrfcId + ", srTypeCd=" + srTypeCd + ", srSeq=" + srSeq
				+ ", rqstRspsTypeCd=" + rqstRspsTypeCd + ", rqstRspsSeq=" + rqstRspsSeq + ", sysCd=" + sysCd
				+ ", msgLayoutId=" + msgLayoutId + ", msglayoutbsDto=" + msglayoutbsDto + "]";
	}
	
	

}