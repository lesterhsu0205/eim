package eims.web.excel.drm.exception;

import static eims.web.excel.workbook.Constant.lineBreak;

import org.apache.commons.io.FilenameUtils;

import eims.web.excel.drm.FileType;

public class FileTypeException extends Exception {

	private static final long serialVersionUID = -8428656258900703910L;
	private String fileName;
	private FileType fileType;


	@SuppressWarnings("unused")
	private FileTypeException() {
	}


	public FileTypeException(String filePath, FileType fileType) {

		fileName = FilenameUtils.getName(filePath);
		this.fileType = fileType;
	}


	@Override
	public String getMessage() {

		return lineBreak + "File Name: " + fileName + lineBreak + "File Type: " + fileType.getCode() + " "
				+ fileType.getDescription();
	}
}
