package eims.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_ABSENT, content = Include.NON_EMPTY)
public class IntrfcDeploy {

	String deployStatus; //배포(DEPLOY), 롤백(ROLLBACK), 삭제(DELETE)
	String intrfcId; //인터페이스ID
	String deployTime; //배포시간
	String deployer; //배포자
	String deploySysCd; //배포시스템코드
	String deployVersion; //배포버전
	IntrfcInfo intrfccombsDto; //인터페이스 정보

	public String getDeployVersion() {
		return deployVersion;
	}

	public void setDeployVersion(String deployVersion) {
		this.deployVersion = deployVersion;
	}

	public String getDeploySysCd() {
		return deploySysCd;
	}

	public void setDeploySysCd(String deploySysCd) {
		this.deploySysCd = deploySysCd;
	}

	public IntrfcInfo getIntrfccombsDto() {
		return intrfccombsDto;
	}

	public void setIntrfccombsDto(IntrfcInfo intrfccombsDto) {
		this.intrfccombsDto = intrfccombsDto;
	}

	public String getDeployStatus() {
		return deployStatus;
	}

	public void setDeployStatus(String deployStatus) {
		this.deployStatus = deployStatus;
	}

	public String getIntrfcId() {
		return intrfcId;
	}

	public void setIntrfcId(String intrfcId) {
		this.intrfcId = intrfcId;
	}

	public String getDeployTime() {
		return deployTime;
	}

	public void setDeployTime(String deployTime) {
		this.deployTime = deployTime;
	}

	public String getDeployer() {
		return deployer;
	}

	public void setDeployer(String deployer) {
		this.deployer = deployer;
	}

}