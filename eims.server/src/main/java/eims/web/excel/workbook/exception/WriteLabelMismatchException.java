package eims.web.excel.workbook.exception;

import static eims.web.excel.workbook.Constant.lineBreak;

import java.util.Map;

public class WriteLabelMismatchException extends Exception {

	private static final long serialVersionUID = 5952310309299090962L;
	private Map<String, Object> errorRecord;
	private Map<String, Object> standardLabel;


	@SuppressWarnings("unused")
	private WriteLabelMismatchException() {
	}


	public WriteLabelMismatchException(Map<String, Object> standardLabel, Map<String, Object> errorRecord) {

		this.errorRecord = errorRecord;
		this.standardLabel = standardLabel;
	}


	@Override
	public String getMessage() {

		return lineBreak + "Expected Label Form: " + standardLabel + lineBreak + "Error: " + errorRecord;
	}
}