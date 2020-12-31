package eims.web.dto;

public class IntrfcdeployInfoDto {

	String deploySysCd; // $col.colComment
	String deployUrl;
	String jsonData; // $col.colComment

	public String getDeploySysCd() {
		return deploySysCd;
	}

	public void setDeploySysCd(String deploySysCd) {
		this.deploySysCd = deploySysCd;
	}

	public String getDeployUrl() {
		return deployUrl;
	}

	public void setDeployUrl(String deployUrl) {
		this.deployUrl = deployUrl;
	}

	public String getJsonData() {
		return jsonData;
	}

	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}

}