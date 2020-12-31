package eims.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;

public class CustomObjectMapper extends ObjectMapper {
	private static final long serialVersionUID = 1L;

	public CustomObjectMapper() {
		DefaultSerializerProvider sp = new CustomNullStringSerializerProvider();
		this.setSerializerProvider(sp);
	}
}