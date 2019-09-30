package eims.web.dto.table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_ABSENT, content = Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExtrnlinstcdDto {

	String instCd; // $col.colComment
	String instCdNm; // $col.colComment
	String instDstnctnVal; // $col.colComment
	String instCdDesc; // $col.colComment

	public String getInstCd() {
		return this.instCd;
	}

	public void setInstCd(String instCd) {
		this.instCd = instCd;
	}

	public String getInstCdNm() {
		return this.instCdNm;
	}

	public void setInstCdNm(String instCdNm) {
		this.instCdNm = instCdNm;
	}

	public String getInstDstnctnVal() {
		return this.instDstnctnVal;
	}

	public void setInstDstnctnVal(String instDstnctnVal) {
		this.instDstnctnVal = instDstnctnVal;
	}

	public String getInstCdDesc() {
		return this.instCdDesc;
	}

	public void setInstCdDesc(String instCdDesc) {
		this.instCdDesc = instCdDesc;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ExtrnlinstcdDto [");
		builder.append("instCd=");
		builder.append(instCd);
		builder.append(", instCdNm=");
		builder.append(instCdNm);
		builder.append(", instDstnctnVal=");
		builder.append(instDstnctnVal);
		builder.append(", instCdDesc=");
		builder.append(instCdDesc);
		builder.append("]");
		return builder.toString();
	}

}