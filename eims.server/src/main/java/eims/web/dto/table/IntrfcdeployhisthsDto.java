package eims.web.dto.table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_ABSENT, content = Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class IntrfcdeployhisthsDto {

	String intrfcId; // $col.colComment
	int deployVersion; // $col.colComment
	String deployDttm; // $col.colComment
	String deploySysCd; // $col.colComment
	String deployResultCd; // $col.colComment
	String rawData;
	String resultRawData;

	public String getResultRawData() {
		return resultRawData;
	}

	public void setResultRawData(String resultRawData) {
		this.resultRawData = resultRawData;
	}

	public String getRawData() {
		return rawData;
	}

	public void setRawData(String rawData) {
		this.rawData = rawData;
	}

	public String getIntrfcId() {
		return this.intrfcId;
	}

	public void setIntrfcId(String intrfcId) {
		this.intrfcId = intrfcId;
	}

	public int getDeployVersion() {
		return this.deployVersion;
	}

	public void setDeployVersion(int deployVersion) {
		this.deployVersion = deployVersion;
	}

	public String getDeployDttm() {
		return this.deployDttm;
	}

	public void setDeployDttm(String deployDttm) {
		this.deployDttm = deployDttm;
	}

	public String getDeploySysCd() {
		return this.deploySysCd;
	}

	public void setDeploySysCd(String deploySysCd) {
		this.deploySysCd = deploySysCd;
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
		builder.append("IntrfcdeployhisthsDto [");
		builder.append("intrfcId=");
		builder.append(intrfcId);
		builder.append(", deployVersion=");
		builder.append(deployVersion);
		builder.append(", deployDttm=");
		builder.append(deployDttm);
		builder.append(", deploySysCd=");
		builder.append(deploySysCd);
		builder.append(", deployResultCd=");
		builder.append(deployResultCd);
		builder.append(", rawData=");
		builder.append(rawData);
		builder.append("]");
		return builder.toString();
	}

}