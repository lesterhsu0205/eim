package eims.web.excel.workbook.exception;

import static eims.web.excel.workbook.Constant.lineBreak;

import java.util.Optional;

import org.apache.commons.io.FilenameUtils;

public class SheetNotFoundException extends Exception {

	private static final long serialVersionUID = 2716721476122825054L;
	private String fileName;
	private int sheetIndex;
	private String sheetName;


	@SuppressWarnings("unused")
	private SheetNotFoundException() {
	}


	public SheetNotFoundException(String filePath, int sheetIndex) {

		fileName = FilenameUtils.getName(filePath);
		this.sheetIndex = sheetIndex;
		this.sheetName = null;
	}


	public SheetNotFoundException(String filePath, String sheetName) {

		fileName = FilenameUtils.getName(filePath);
		this.sheetIndex = -1;
		this.sheetName = sheetName;
	}


	@Override
	public String getMessage() {

		String sheetNameOrIndex = Optional.ofNullable((Object) sheetName)
				.map((maybeSheetName) -> "Sheet Name: " + maybeSheetName).orElse("Sheet Index: " + sheetIndex);

		return lineBreak + "File Name: " + fileName + lineBreak + sheetNameOrIndex;
	}
}
