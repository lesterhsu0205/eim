package eims.web.dto.table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_ABSENT, content = Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BizcdDto {

	String bizCd; // $col.colComment
	String bizCdNm; // $col.colComment
	String bizCdDesc; // $col.colComment

	public String getBizCd() {
		return this.bizCd;
	}

	public void setBizCd(String bizCd) {
		this.bizCd = bizCd;
	}

	public String getBizCdNm() {
		return this.bizCdNm;
	}

	public void setBizCdNm(String bizCdNm) {
		this.bizCdNm = bizCdNm;
	}

	public String getBizCdDesc() {
		return this.bizCdDesc;
	}

	public void setBizCdDesc(String bizCdDesc) {
		this.bizCdDesc = bizCdDesc;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BizcdDto [");
		builder.append("bizCd=");
		builder.append(bizCd);
		builder.append(", bizCdNm=");
		builder.append(bizCdNm);
		builder.append(", bizCdDesc=");
		builder.append(bizCdDesc);
		builder.append("]");
		return builder.toString();
	}

}