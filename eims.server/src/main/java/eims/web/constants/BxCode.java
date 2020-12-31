package eims.web.constants;

public interface BxCode {

	public enum Locale implements BxCode {
		ko, en;
	}

	public enum YesNo implements BxCode {
		YES, NO; 
	}

	public enum HttpMethod implements BxCode {
		GET, POST;
	}

	public enum JavaType implements BxCode {
		BOOLEAN_PRIMITIVE, BOOLEAN, BYTE_PRIMITIVE, BYTE, CHAR, CHARACTER, SHORT_PRIMITIVE, SHORT, INT, INTEGER, LONG_PRIMITIVE, LONG, FLOAT_PRIMITIVE, FLOAT, DOUBLE_PRIMITIVE, DOUBLE, BIGDECIMAL, BIGINTEGER, STRING, CALENDAR, DTO, OBJECT, DATE, BLOB, CLOB;
	}

	public enum syncAsync implements BxCode {
		SYNC, ASYNC;
	}

	public enum interfaceWay implements BxCode {
		ONLINE, BATCH;
	}

	public enum DataType implements BxCode {
		STRING, LONG, BIGDECIMAL, BYTEARRAY, LAYOUT;
	}

	public enum DataBaseType implements BxCode {
		VARCHAR, NUMERIC, CLOB, BLOB, TIMESTAMP, DATE;
	}

	public enum WorkStatusCd implements BxCode {
		WORKING, WORK_COMP, DEPLOY_COMP;
	}

	public enum ChlDscd implements BxCode {
		INTERNAL, EXTERNAL;
	}

	public enum SystemCode implements BxCode {
		TER;
	}

}
