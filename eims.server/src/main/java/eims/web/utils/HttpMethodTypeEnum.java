package eims.web.utils;

/**
 * HTTP method type
 *
 * @author
 * @history
 *          2016.05.27 initial
 */
public enum HttpMethodTypeEnum {
	GET("GET", "get"),
	POST("POST", "post"),
	PUT("PUT", "put"),
	PATCH("PATCH", "patch"),
	DELETE("DELETE", "delete"),
	HEAD("HEAD", "head"),
	TRACE("TRACE", "trace"),
	CONNECT("CONNECT", "connect"),
	OPTIONS("OPTIONS", "options"),
	UNKNOWN("Unknown", "Unknown");

	/**
	 * Code
	 */
	private String code;

	/**
	 * Description
	 */
	private String description;

	/**
	 * Constructor
	 *
	 * @param code Enum Code
	 * @param description Enum description
	 */
	HttpMethodTypeEnum(String code, String description) {
		this.code = code;
		this.description = description;
	}

	/**
	 * Getter method for property <tt>code</tt>.
	 *
	 * @return property code of code
	 */
	public String getCode() {
		return this.code;
	}

	/**
	 * Getter method for property <tt>description</tt>.
	 *
	 * @return property description of description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Search Enum code
	 *
	 * @param code
	 * @return LogLevelEnum
	 */
	public static HttpMethodTypeEnum getByCode(String code) {
		for (HttpMethodTypeEnum enumObject : values()) {
			if (enumObject.getCode().equals(code)) {
				return enumObject;
			}
		}
		return UNKNOWN;
	}

	/**
	 * Check code
	 *
	 * @param code
	 * @return
	 */
	public static boolean isValid(String code) {
		for (HttpMethodTypeEnum enumObject : values()) {
			if (enumObject.getCode().equals(code)) {
				return true;
			}
		}
		return false;
	}
}
