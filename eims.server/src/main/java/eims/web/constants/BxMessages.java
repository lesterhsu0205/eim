package eims.web.constants;

import eims.ServiceContext;

public interface BxMessages {
	String getType();

	String getMessage();

	public enum Error implements BxMessages {

		/**전문 배열참조 validation 체크 오류*/
		ARRAY_REF_VALIDATION("ERROR", "Error checking telegram array reference validation.", "Error checking telegram array reference validation."),
		
		/** 작업중상태 시 배포 불가 */
		DEPLOY_STATUS_ERR("ERROR", "Deployment is not possible during operation. Please distribute the work after completion.", "Deployment is not possible during operation. Please distribute the work after completion."),
		/** 전문 밸리데이션체크 익셉션 */
		MSG_VALID_CHECK("ERROR", "Error occurred during telegram validation", "Error occurred during telegram validation"),
		/** 인터페이스 밸리데이션체크 익셉션 */
		INTRFC_VALID_CHECK("ERROR", "Error occurred during interface validation.", "Error occurred during interface validation."),
		/** 입력하신 로그인 ID가 존재하지 않습니다.*/
		INVALID_ID("ERROR", "The login ID you entered does not exist.", "The login ID you entered does not exist."),
		
		WRAPPER_IO_DUP("ERROR", "The Io name is already in telegram use.", "The Io name is already in telegram use."),

		FAIL_LOGIN("ERROR", "Login failed. Please contact the operator.", "Login failed. Please contact the operator."),

		/** 비밀번호가 일치하지 않습니다.*/
		INVALID_PASSWORD("ERROR", "Passwords do not match.", "Passwords do not match."),

		/** 세션이 존재하지 않습니다.*/
		INVALID_SESSION("ERROR", "Session does not exist", "Session does not exist"),

		/** 해당 리소스가 존재하지 않습니다. */
		NOT_FOUNDED_TER("ERROR", "The transmission and receiving system does not exist.", "The transmission and receiving system does not exist."),

		/** 파일아이디 길이 초과 */
		FILE_SIZE_ERR("ERROR", "File ID length exceeded", "File ID length exceeded"),

		/** 해당 리소스가 존재하지 않습니다. */
		NOT_FOUNDED("ERROR", "The resource does not exist.", "The resource does not exist."),

		/** 메시지레이아웃이 인터페이스에서 사용중 이므로 삭제할 수 없습니다. */
		ALREADY_USE_MSG("ERROR", "MessageLayout is in use by the interface and cannot be deleted. Please check the impact level through analysis.", "MessageLayout is in use by the interface and cannot be deleted. Please check the impact level through analysis."),

		/** 고객사API여부가 Y인 전문은 송수신시스템코드가 COO, OAG인 MCI인터페이스에서만 사용할 수 있습니다.  */
		CUS_API_YN_CHECK_ERR("ERROR", "Please contact the operator.", "Please contact the operator."),
		
		/** 해당 리소스가 존재하지 않습니다. */
		NOT_FOUNDED_DEPLOYHS("ERROR", "The deployment history does not exist.", "The deployment history does not exist."),

		/** 동일한 리소스가 이미 존재합니다. */
		MSG_DEFAULT_VALUE_SET_ERR("ERROR", "An error occurred when setting the telegram default.", "An error occurred when setting the telegram default."),
		
		IO_NM_DUP_ERR("ERROR", "Duplicate error occurred for telegram IO name.", "Duplicate error occurred for telegram IO name."),
		
		/** 동일한 리소스가 이미 존재합니다. */
		DUPLICATE_KEY("ERROR", "The same resource already exists.", "The same resource already exists."),

		/** 동일한 리소스가 이미 존재합니다. */
		DUPLICATE_KEY_EXTERNAL("ERROR", "The same resource already exists - Foreign Department, Telegram", "The same resource already exists - Foreign Department, Telegram"),

		/** 파일이 존재하지 않습니다. */
		FILE_NOT_EXISTED("ERROR", "File does not exist", "File does not exist"),

		FILE_UPLOAD_INTERFACE_TYPE("ERROR", "The type of file you want to upload is different from the menu you want to upload.", "The type of file you want to upload is different from the menu you want to upload."),

		/** DRM Exception. */
		DRM_EXCEPTION("ERROR", "DRM ERROR", "English"),

		/** 시스템 오류가 발생했습니다. 운영자에게 연락주세요. */
		REDEPLOY_EXCEPTION("ERROR", "Failed to redeploy. Please contact the operator.", "Failed to redeploy. Please contact the operator."),

		NEXACRO_API_EXCEPTION("ERROR", "NEXACRO_API_EXCEPTION. Please contact the operator.", "Please contact the operator."),

		/** 시스템 오류가 발생했습니다. 운영자에게 연락주세요. */
		SYSTEM_EXCEPTION("ERROR", "A system error occurred. Please contact the operator.", "A system error occurred. Please contact the operator."),
		
		/** 시스템 오류가 발생했습니다. 운영자에게 연락주세요. */
		IO_KEY_DEPTH_EXCEPTION("ERROR", "IO KEY can only be set to MainIO (0 Depps).", "IO KEY can only be set to MainIO (0 Depps)."),
		
		EXT_IONM_NULL_EXCEPTION("ERROR", "Professional IO required value check error", "Professional IO required value check error"),
		
		MSG_TRANS_MAPPING_EXCEPTION("ERROR", "MSG_TRANS_MAPPING_EXCEPTION", "MSG_TRANS_MAPPING_EXCEPTION"),
		
		EXT_CHILD_IONM_NULL_EXCEPTION("ERROR", "Lower IO name is empty.", "Lower IO name is empty."),
		
		/** 시스템 오류가 발생했습니다. 운영자에게 연락주세요. */
		IO_KEY_DUP_EXCEPTION("ERROR", "Telegram IO KEY cannot be duplicated.", "Telegram IO KEY cannot be duplicated."),
		
		REPL_KEY_DUP_EXCEPTION("ERROR", "REPL_KEY_DUP_EXCEPTION Please contact the operator.", "REPL_KEY_DUP_EXCEPTION Please contact the operator."),
		
		REPL_KEY_PRIVACY_EXCEPTION2("ERROR", "REPL_KEY_PRIVACY_EXCEPTION2 Please contact the operator.", "REPL_KEY_PRIVACY_EXCEPTION2 Please contact the operator."),
		REPL_KEY_PRIVACY_EXCEPTION3("ERROR", "REPL_KEY_PRIVACY_EXCEPTION3 Please contact the operator", "REPL_KEY_PRIVACY_EXCEPTION2 Please contact the "),
		//전문 내 레이아웃 항목들 중에 개인정보가 한 개 이상 설정
		REPL_KEY_PRIVACY_EXCEPTION4("ERROR", "REPL_KEY_PRIVACY_EXCEPTION4 Please contact the operator", "REPL_KEY_PRIVACY_EXCEPTION4 Please contact the "),
		
		/** 시스템 오류가 발생했습니다. 운영자에게 연락주세요. */
		IO_KEY_LAYOUT_EXCEPTION("ERROR", "Telegram IO KEY cannot be set in LAYOUT data type.", "Telegram IO KEY cannot be set in LAYOUT data type."),
		
		/** 시스템 오류가 발생했습니다. 운영자에게 연락주세요. */
		REPL_KEY_LAYOUT_EXCEPTION("ERROR", "REPL_KEY_LAYOUT_EXCEPTION Please contact the operator", "REPL_KEY_LAYOUT_EXCEPTION Please contact the operator"),

		/** 시스템 오류가 발생했습니다. 운영자에게 연락주세요. */
		FILE_UPLOAD_EXCEPTION("ERROR", "FILE_UPLOAD_EXCEPTION Please contact the operator", "FILE_UPLOAD_EXCEPTION Please contact the operator");

		String type;
		String korean;
		String english;

		private Error(String type, String korean, String english) {
			this.type = type;
			this.korean = korean;
			this.english = english;
		}

		@Override
		public String getType() {
			return type;
		}

		@Override
		public String getMessage() {

			if (ServiceContext.getUserInfo() == null) {
				return korean;
			} else {
				if (ServiceContext.getUserInfo().getLocale().equals(BxCode.Locale.ko.name())) {
					return korean;
				} else {
					return english;
				}
			}
		}
	}
}
