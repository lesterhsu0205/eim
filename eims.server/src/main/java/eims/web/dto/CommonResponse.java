package eims.web.dto;

public class CommonResponse {

	private boolean hasError = true;
	private String errorMsg;

	public boolean isHasError() {
		return hasError;
	}

	public void setHasError(boolean hasError) {
		this.hasError = hasError;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CommonResponse [hasError=");
		builder.append(hasError);
		builder.append(", errorMsg=");
		builder.append(errorMsg);
		builder.append(", isHasError()=");
		builder.append(isHasError());
		builder.append(", getErrorMsg()=");
		builder.append(getErrorMsg());
		builder.append(", getClass()=");
		builder.append(getClass());
		builder.append(", hashCode()=");
		builder.append(hashCode());
		builder.append(", toString()=");
		builder.append(super.toString());
		builder.append("]");
		return builder.toString();
	}

}
