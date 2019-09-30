package eims.web.dto.table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_ABSENT, content = Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DepolysysbsDto {

	String deploySysCd; // $col.colComment
	String deploySysNm; // $col.colComment
	String deploySysUrl; // $col.colComment
	String deploySysDesc; // $col.colComment
	String deploySysGrpCd; // $col.colComment

	public String getDeploySysCd() {
		return this.deploySysCd;
	}

	public void setDeploySysCd(String deploySysCd) {
		this.deploySysCd = deploySysCd;
	}

	public String getDeploySysNm() {
		return this.deploySysNm;
	}

	public void setDeploySysNm(String deploySysNm) {
		this.deploySysNm = deploySysNm;
	}

	public String getDeploySysUrl() {
		return this.deploySysUrl;
	}

	public void setDeploySysUrl(String deploySysUrl) {
		this.deploySysUrl = deploySysUrl;
	}

	public String getDeploySysDesc() {
		return this.deploySysDesc;
	}

	public void setDeploySysDesc(String deploySysDesc) {
		this.deploySysDesc = deploySysDesc;
	}

	public String getDeploySysGrpCd() {
		return this.deploySysGrpCd;
	}

	public void setDeploySysGrpCd(String deploySysGrpCd) {
		this.deploySysGrpCd = deploySysGrpCd;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DepolysysbsDto [");
		builder.append("deploySysCd=");
		builder.append(deploySysCd);
		builder.append(", deploySysNm=");
		builder.append(deploySysNm);
		builder.append(", deploySysUrl=");
		builder.append(deploySysUrl);
		builder.append(", deploySysDesc=");
		builder.append(deploySysDesc);
		builder.append(", deploySysGrpCd=");
		builder.append(deploySysGrpCd);
		builder.append("]");
		return builder.toString();
	}

}