 package eims.web.constants;

public interface BxConstants {

	interface Session {
		String LOCALE = "locale";
		String USER_ID = "userId";
		String ROLE_ID = "roleId";
		String USER_INFO = "userInfo";
		String IS_USER_LOGIN = "isUserLogin";
	}

	interface Default {
		String PROPERTIES_FILE = "system.properties";
		BxCode.Locale LOCALE = BxCode.Locale.en;
//		諻堅��� 鴥潰�� true , 諢賑穈��� false
		boolean IS_SERVER = true;
//		boolean IS_SERVER = false; 
	}

	/** Properties Key */
	interface Keys {
		String MESSAGE_LOCALE = "message.locale";
		String DEFAULT_ROLE_ID = "default.roleId";
		String ADMIN_ROLE_ID = "admin.roleId";
		String CRYPTO_KEY = "crypto.key";
		String DEPLOY_READ_TIMEOUT = "deploy.readtimeout" ;
		String DEPLOY_CONN_TIMEOUT = "deploy.conntimeout" ;
	} 

	/** Exception 諻���  �� ��爰��� �� */
	interface Status {
		String ERROR = "ERROR";
		String WARN = "WARN";
	}

	/** 麇�� Prefix */
	interface Guid {
		String MESSAGE = "MESSAGE";
	}
}
