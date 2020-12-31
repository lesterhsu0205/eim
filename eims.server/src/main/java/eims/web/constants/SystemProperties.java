package eims.web.constants;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SystemProperties {

	private static Properties properites = new Properties();

	static {
		InputStream inputStream;
		try {
			inputStream = SystemProperties.class.getClassLoader().getResourceAsStream(BxConstants.Default.PROPERTIES_FILE);
			properites.load(inputStream);
			inputStream.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public static String get(String key) {
		return properites.getProperty(key);
	}
}
