package eims.config;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;

import eims.web.dto.table.DomainCORSDto;
import eims.web.dto.table.DomainSSLDto;

public class CustomNullStringSerializerProvider extends DefaultSerializerProvider {

	private static final long serialVersionUID = 1L;

	public CustomNullStringSerializerProvider() {
		super();
	}

	public CustomNullStringSerializerProvider(CustomNullStringSerializerProvider provider, SerializationConfig config,
			SerializerFactory jsf) {
		super(provider, config, jsf);
	}

	@Override
	public CustomNullStringSerializerProvider createInstance(SerializationConfig config, SerializerFactory jsf) {
		return new CustomNullStringSerializerProvider(this, config, jsf);
	}

	@Override
	public JsonSerializer<Object> findNullValueSerializer(BeanProperty property) throws JsonMappingException {
		if (property.getType().getRawClass().equals(DomainSSLDto.class)
				|| property.getType().getRawClass().equals(DomainCORSDto.class)) {
			return super.findNullValueSerializer(property);
		} else {
			return NullSerializer.INSTANCE;
		}
	}
}
