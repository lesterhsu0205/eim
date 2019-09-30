package eims.web.exception;

public class BXRuntimeSystemException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String emsgCode;

	public BXRuntimeSystemException() {
		super();
	}

	public BXRuntimeSystemException(String message, Throwable cause) {
		super(message, cause);
	}

	public BXRuntimeSystemException(String message) {
		super(message);
	}

	public BXRuntimeSystemException(Throwable cause) {
		super(cause);
	}

	public String getEmsgCode() {
		return emsgCode;
	}

	public BXRuntimeSystemException setEmsgCode(String emsgCode) {
		this.emsgCode = emsgCode;
		return this;
	}

}
