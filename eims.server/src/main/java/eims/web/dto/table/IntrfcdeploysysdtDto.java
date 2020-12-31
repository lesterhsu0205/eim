package eims.web.dto.table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_ABSENT, content = Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class IntrfcdeploysysdtDto {

	String intrfcId; // $col.colComment
	String deploySysCd; // $col.colComment
	String deployUrl;
	int deploySysSeq; // $col.colComment
	String deploySysNm;
	String deployResultCd; // $col.colComment

	public String getDeploySysNm() {
		return deploySysNm;
	}

	public void setDeploySysNm(String deploySysNm) {
		this.deploySysNm = deploySysNm;
	}

	public String getDeployUrl() {
		return deployUrl;
	}

	public void setDeployUrl(String deployUrl) {
		this.deployUrl = deployUrl;
	}

	public String getIntrfcId() {
		return this.intrfcId;
	}

	public void setIntrfcId(String intrfcId) {
		this.intrfcId = intrfcId;
	}

	public String getDeploySysCd() {
		return this.deploySysCd;
	}

	public void setDeploySysCd(String deploySysCd) {
		this.deploySysCd = deploySysCd;
	}

	public int getDeploySysSeq() {
		return this.deploySysSeq;
	}

	public void setDeploySysSeq(int deploySysSeq) {
		this.deploySysSeq = deploySysSeq;
	}

	public String getDeployResultCd() {
		return this.deployResultCd;
	}

	public void setDeployResultCd(String deployResultCd) {
		this.deployResultCd = deployResultCd;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("IntrfcdeploysysdtDto [");
		builder.append("intrfcId=");
		builder.append(intrfcId);
		builder.append(", deploySysCd=");
		builder.append(deploySysCd);
		builder.append(", deploySysSeq=");
		builder.append(deploySysSeq);
		builder.append(", deployResultCd=");
		builder.append(deployResultCd);
		builder.append("]");
		return builder.toString();
	}

}