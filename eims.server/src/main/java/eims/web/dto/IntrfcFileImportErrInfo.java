/**
 * 
 */
package eims.web.dto;

import eims.web.constants.BxMessages;

/**
 * @author 20180032
 *
 * 인터페이스 정보 Multi Import 시 오류 내역 정보 DTO 
 */
public class IntrfcFileImportErrInfo {

	private String fileName;
	private String message;
	private String[] parameter;
	private int status;

//	public String getStatus() {
//		return message.getType();
//	}

	public String[] getParameter() {
		return parameter;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setParameter(String[] parameter) {
		this.parameter = parameter;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	
}
