package eims.web.excel.drm.exception;

import static eims.web.excel.workbook.Constant.lineBreak;

public class FileNotSupportedException extends Exception {

	private static final long serialVersionUID = -8428656258900703910L;


	public FileNotSupportedException() {
	}


	@Override
	public String getMessage() {
		return lineBreak + "File Not Supported by Fasoo DRM";
	}
}
