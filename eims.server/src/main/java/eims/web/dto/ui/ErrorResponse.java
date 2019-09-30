package eims.web.dto.ui;

public class ErrorResponse {

	private String responseStatus;
	private String message;
	private String[] parameters;
	private String stackTrace;


	public String getResponseStatus() {
		return responseStatus;
	}


	public void setResponseStatus(String responseStatus) {
		this.responseStatus = responseStatus;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public String[] getParameters() {
		return parameters;
	}


	public void setParameters(String[] parameters) {
		this.parameters = parameters;
	}


	public String getStackTrace() {
		return stackTrace;
	}


	public void setStackTrace(String stackTrace) {
		this.stackTrace = stackTrace;
	}

}
