package eims.web.excel.drm.exception;

import static eims.web.excel.workbook.Constant.lineBreak;

public class FasooDrmException extends Exception {

	private static final long serialVersionUID = -3340861479768942419L;
	private int errorCode;
	private String errorMessage;


	@SuppressWarnings("unused")
	private FasooDrmException() {
	}


	public FasooDrmException(int errorCode, String errorMessage) {

		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}


	@Override
	public String getMessage() {

		return lineBreak + "Fasoo Error Message: " + errorCode + " " + errorMessage;
	}
}
