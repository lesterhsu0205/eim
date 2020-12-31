package eims.web.utils;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonUtils {

	public static <T> T jsonToObect(String jsonString, Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectmapper = new ObjectMapper();

		return objectmapper.readValue(jsonString, clazz);
	}

	public static <T> String objectToJson(T object) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectmapper = new ObjectMapper();

		objectmapper.configure(SerializationFeature.INDENT_OUTPUT, true);

		return objectmapper.writeValueAsString(object);
	}
}
