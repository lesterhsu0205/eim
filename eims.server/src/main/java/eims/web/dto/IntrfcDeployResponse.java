package eims.web.dto;

public class IntrfcDeployResponse {

	String systemCd; //시스템코드
	String deployStatus; //성공(SUCCESS), 실패(FAIL)
	String message; // 에러 또는 성공 메시지

	public String getSystemCd() {
		return systemCd;
	}

	public void setSystemCd(String systemCd) {
		this.systemCd = systemCd;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDeployStatus() {
		return deployStatus;
	}

	public void setDeployStatus(String deployStatus) {
		this.deployStatus = deployStatus;
	}

}