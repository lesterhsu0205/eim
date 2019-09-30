package eims.web.utils;

public enum MediaTypeEnum {
	APPLICATION_JSON("application/json", "application/json"),
	APPLICATION_XML("application/xml", "application/xml"),
	UNKNOWN("Unknown", "Unknown");

	private String code;
	private String description;

	MediaTypeEnum(String code, String description) {
		this.code = code;
		this.description = description;
	}

	public String getCode() {
		return this.code;
	}

	public String getDescription() {
		return description;
	}

	public static MediaTypeEnum getByCode(String code) {
		for (MediaTypeEnum enumObject : values()) {
			if (enumObject.getCode().equals(code)) {
				return enumObject;
			}
		}
		return UNKNOWN;
	}

	public static boolean isValid(String code) {
		for (MediaTypeEnum enumObject : values()) {
			if (enumObject.getCode().equals(code)) {
				return true;
			}
		}
		return false;
	}
}
