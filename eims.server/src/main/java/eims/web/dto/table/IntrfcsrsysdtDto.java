package eims.web.dto.table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_ABSENT, content = Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class IntrfcsrsysdtDto {

	String intrfcId; // 인터페이스ID
	String srTypeCd; // 송수신타입코드
	int srSeq; // 송수신 시퀀스
	String sysCd; // 송수신시스템코드
	String sysNm; // 송수신시스템명
	String trxCd; // 거래코드
	String filePath; // 파일경로
	String crgManNm; // $col.colComment

	public String getCrgManNm() {
		return crgManNm;
	}

	public void setCrgManNm(String crgManNm) {
		this.crgManNm = crgManNm;
	}

	public String getTrxCd() {
		return trxCd;
	}

	public void setTrxCd(String trxCd) {
		this.trxCd = trxCd;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getSysNm() {
		return sysNm;
	}

	public void setSysNm(String sysNm) {
		this.sysNm = sysNm;
	}

	public String getIntrfcId() {
		return this.intrfcId;
	}

	public void setIntrfcId(String intrfcId) {
		this.intrfcId = intrfcId;
	}

	public String getSrTypeCd() {
		return this.srTypeCd;
	}

	public void setSrTypeCd(String srTypeCd) {
		this.srTypeCd = srTypeCd;
	}

	public int getSrSeq() {
		return this.srSeq;
	}

	public void setSrSeq(int srSeq) {
		this.srSeq = srSeq;
	}

	public String getSysCd() {
		return this.sysCd;
	}

	public void setSysCd(String sysCd) {
		this.sysCd = sysCd;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("IntrfcsrsysdtDto [");
		builder.append("intrfcId=");
		builder.append(intrfcId);
		builder.append(", srTypeCd=");
		builder.append(srTypeCd);
		builder.append(", srSeq=");
		builder.append(srSeq);
		builder.append(", sysCd=");
		builder.append(sysCd);
		builder.append("]");
		return builder.toString();
	}

}