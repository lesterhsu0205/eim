package eims.web.dto.table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_ABSENT, content = Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class IntrfccomdtDto {

	String intrfcId; // $col.colComment
	String trxCd; // $col.colComment
	String bizCd; // $col.colComment
	String instCd; // $col.colComment
	String trxTypeCd; // $col.colComment

	public String getIntrfcId() {
		return this.intrfcId;
	}

	public void setIntrfcId(String intrfcId) {
		this.intrfcId = intrfcId;
	}

	public String getTrxCd() {
		return this.trxCd;
	}

	public void setTrxCd(String trxCd) {
		this.trxCd = trxCd;
	}

	public String getBizCd() {
		return this.bizCd;
	}

	public void setBizCd(String bizCd) {
		this.bizCd = bizCd;
	}

	public String getInstCd() {
		return this.instCd;
	}

	public void setInstCd(String instCd) {
		this.instCd = instCd;
	}

	public String getTrxTypeCd() {
		return this.trxTypeCd;
	}

	public void setTrxTypeCd(String trxTypeCd) {
		this.trxTypeCd = trxTypeCd;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("IntrfccomdtDto [");
		builder.append("intrfcId=");
		builder.append(intrfcId);
		builder.append(", trxCd=");
		builder.append(trxCd);
		builder.append(", bizCd=");
		builder.append(bizCd);
		builder.append(", instCd=");
		builder.append(instCd);
		builder.append(", trxTypeCd=");
		builder.append(trxTypeCd);
		return builder.toString();
	}

}