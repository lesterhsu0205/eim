package eims.web.exception;

import org.springframework.http.HttpStatus;

import eims.web.constants.BxMessages;

public class ServiceException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private BxMessages message;
	private String[] parameter;

	public HttpStatus getHttpStatus() {
		return HttpStatus.BAD_REQUEST;
	}

	public String getStatus() {
		return message.getType();
	}

	public String[] getParameter() {
		return parameter;
	}

	public ServiceException(BxMessages message) {
		super(message.getMessage());

		this.message = message;
	}

	public ServiceException(BxMessages message, String... parameter) {
		super(message.getMessage());

		this.message = message;
		this.parameter = parameter;
	}

}