package eims.web.dto.table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_ABSENT, content = Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MsglayoutbsDtoMapping {

	String msgLayoutId; // 메시지레이아웃ID
	String fldEngNm; // 필드영문명
	String fldKorNm; // 필드한글명
	String dataTypeNm; // 데이터타입
	int msgSeq; // 메시지일련번호
	int msgLen; // 메시지길이
	int decimalLen; // DECIMAL길이
	String msgLayoutId2; // 메시지레이아웃ID
	String fldEngNm2; // 필드영문명
	String fldKorNm2; // 필드한글명
	String dataTypeNm2; // 데이터타입
	int msgSeq2; // 메시지일련번호
	int msgLen2; // 메시지길이
	int decimalLen2; // DECIMAL길이

	public String getMsgLayoutId() {
		return msgLayoutId;
	}

	public void setMsgLayoutId(String msgLayoutId) {
		this.msgLayoutId = msgLayoutId;
	}

	public String getFldEngNm() {
		return fldEngNm;
	}

	public void setFldEngNm(String fldEngNm) {
		this.fldEngNm = fldEngNm;
	}

	public String getFldKorNm() {
		return fldKorNm;
	}

	public void setFldKorNm(String fldKorNm) {
		this.fldKorNm = fldKorNm;
	}

	public String getDataTypeNm() {
		return dataTypeNm;
	}

	public void setDataTypeNm(String dataTypeNm) {
		this.dataTypeNm = dataTypeNm;
	}

	public int getMsgSeq() {
		return msgSeq;
	}

	public void setMsgSeq(int msgSeq) {
		this.msgSeq = msgSeq;
	}

	public int getMsgLen() {
		return msgLen;
	}

	public void setMsgLen(int msgLen) {
		this.msgLen = msgLen;
	}

	public int getDecimalLen() {
		return decimalLen;
	}

	public void setDecimalLen(int decimalLen) {
		this.decimalLen = decimalLen;
	}

	public String getMsgLayoutId2() {
		return msgLayoutId2;
	}

	public void setMsgLayoutId2(String msgLayoutId2) {
		this.msgLayoutId2 = msgLayoutId2;
	}

	public String getFldEngNm2() {
		return fldEngNm2;
	}

	public void setFldEngNm2(String fldEngNm2) {
		this.fldEngNm2 = fldEngNm2;
	}

	public String getFldKorNm2() {
		return fldKorNm2;
	}

	public void setFldKorNm2(String fldKorNm2) {
		this.fldKorNm2 = fldKorNm2;
	}

	public String getDataTypeNm2() {
		return dataTypeNm2;
	}

	public void setDataTypeNm2(String dataTypeNm2) {
		this.dataTypeNm2 = dataTypeNm2;
	}

	public int getMsgSeq2() {
		return msgSeq2;
	}

	public void setMsgSeq2(int msgSeq2) {
		this.msgSeq2 = msgSeq2;
	}

	public int getMsgLen2() {
		return msgLen2;
	}

	public void setMsgLen2(int msgLen2) {
		this.msgLen2 = msgLen2;
	}

	public int getDecimalLen2() {
		return decimalLen2;
	}

	public void setDecimalLen2(int decimalLen2) {
		this.decimalLen2 = decimalLen2;
	}

}