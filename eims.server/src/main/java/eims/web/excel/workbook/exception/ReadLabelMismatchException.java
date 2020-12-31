package eims.web.excel.workbook.exception;

import static eims.web.excel.workbook.Constant.lineBreak;

import java.util.Map;
import java.util.Optional;

import org.apache.commons.io.FilenameUtils;

public class ReadLabelMismatchException extends Exception {

	private static final long serialVersionUID = 5952310309299090962L;
	private String fileName;
	private int sheetIndex;
	private String sheetName;
	Map<String, Object> errorRecord;
	Map<String, Object> standardLabel;


	@SuppressWarnings("unused")
	private ReadLabelMismatchException() {
	}


	public ReadLabelMismatchException(String filePath, int sheetIndex, Map<String, Object> errorRecord,
			Map<String, Object> standardLabel) {

		fileName = FilenameUtils.getName(filePath);
		this.sheetIndex = sheetIndex;
		this.sheetName = null;
		this.errorRecord = errorRecord;
		this.standardLabel = standardLabel;
	}


	public ReadLabelMismatchException(String filePath, String sheetName, Map<String, Object> standardLabel,
			Map<String, Object> errorRecord) {

		fileName = FilenameUtils.getName(filePath);
		this.sheetIndex = -1;
		this.sheetName = sheetName;
		this.errorRecord = errorRecord;
		this.standardLabel = standardLabel;
	}


	@Override
	public String getMessage() {

		String sheetNameOrIndex = Optional.ofNullable((Object) sheetName)
				.map((maybeSheetName) -> "Sheet Name: " + maybeSheetName).orElse("Sheet Index: " + sheetIndex);

		return lineBreak + "File Name: " + fileName + lineBreak + sheetNameOrIndex + lineBreak + "Expected Label Form: "
				+ standardLabel + lineBreak + "Error: " + errorRecord;
	}
}