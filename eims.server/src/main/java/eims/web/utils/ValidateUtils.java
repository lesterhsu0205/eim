package eims.web.utils;

public class ValidateUtils {

	public static String validateUrl(String url) {

		if (!StringUtils.isEmpty(url) && !url.startsWith("/")) {
			StringBuffer strBuf = new StringBuffer();
			strBuf.append("/").append(url);
			return strBuf.toString();
		}

		return url;
	}
}
