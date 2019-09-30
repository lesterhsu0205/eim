package eims.web.constants;

import eims.ServiceContext;

public interface BxMessages {
	String getType();

	String getMessage();

	public enum Error implements BxMessages {

		/**전문 배열참조 validation 체크 오류*/
		ARRAY_REF_VALIDATION("ERROR", "전문 배열참조 validation 체크중 오류가 발생하였습니다.", "English"),
		
		/** 작업중상태 시 배포 불가 */
		DEPLOY_STATUS_ERR("ERROR", "작업중 상태 시 배포가 불가능합니다. 작업완료 후 배포하여주세요.", "English"),
		/** 전문 밸리데이션체크 익셉션 */
		MSG_VALID_CHECK("ERROR", "전문 유효성검사 중 오류가 발생하였습니다.", "English"),
		/** 인터페이스 밸리데이션체크 익셉션 */
		INTRFC_VALID_CHECK("ERROR", "인터페이스 유효성검사 중 오류가 발생하였습니다.", "English"),
		/** 입력하신 로그인 ID가 존재하지 않습니다.*/
		INVALID_ID("ERROR", "입력하신 로그인 ID가 존재하지 않습니다.", "English"),
		
		WRAPPER_IO_DUP("ERROR", "WrapperIo명이 이미 전문에서 사용중입니다.", "English"),

		FAIL_LOGIN("ERROR", "로그인에 실패하였습니다. 운영자에게 문의하여주세요.", "English"),

		/** 비밀번호가 일치하지 않습니다.*/
		INVALID_PASSWORD("ERROR", "비밀번호가 일치하지 않습니다.", "English"),

		/** 세션이 존재하지 않습니다.*/
		INVALID_SESSION("ERROR", "세션이 존재하지 않습니다.", "English"),

		/** 해당 리소스가 존재하지 않습니다. */
		NOT_FOUNDED_TER("ERROR", "단말 송수신시스템이 존재하지 않습니다.", "English"),

		/** 파일아이디 길이 초과 */
		FILE_SIZE_ERR("ERROR", "파일아이디 길이 초과", "English"),

		/** 해당 리소스가 존재하지 않습니다. */
		NOT_FOUNDED("ERROR", "해당 리소스가 존재하지 않습니다.", "English"),

		/** 메시지레이아웃이 인터페이스에서 사용중 이므로 삭제할 수 없습니다. */
		ALREADY_USE_MSG("ERROR", "메시지레이아웃이 인터페이스에서 사용중 이므로 삭제할 수 없습니다. 영향도 분석을 통해 확인하여 주세요.", "English"),

		/** 고객사API여부가 Y인 전문은 송수신시스템코드가 COO, OAG인 MCI인터페이스에서만 사용할 수 있습니다.  */
		CUS_API_YN_CHECK_ERR("ERROR", "고객사API여부가 Y인 전문은 송수신시스템코드가 COO, OAG인 MCI인터페이스에서만 사용할 수 있습니다.", "English"),
		
		/** 해당 리소스가 존재하지 않습니다. */
		NOT_FOUNDED_DEPLOYHS("ERROR", "해당 배포이력이 존재하지 않습니다.", "English"),

		/** 동일한 리소스가 이미 존재합니다. */
		MSG_DEFAULT_VALUE_SET_ERR("ERROR", "전문 기본값 세팅 시 오류가 발생하였습니다.", "English"),
		
		IO_NM_DUP_ERR("ERROR", "전문IO명 중복 오류가 발생하였습니다.", "English"),
		
		/** 동일한 리소스가 이미 존재합니다. */
		DUPLICATE_KEY("ERROR", "동일한 리소스가 이미 존재합니다.", "English"),

		/** 동일한 리소스가 이미 존재합니다. */
		DUPLICATE_KEY_EXTERNAL("ERROR", "동일한 리소스가 이미 존재합니다. - 대외개별부, ISO8583Body 전문", "English"),

		/** 파일이 존재하지 않습니다. */
		FILE_NOT_EXISTED("ERROR", "파일이 존재하지 않습니다.", "English"),

		FILE_UPLOAD_INTERFACE_TYPE("ERROR", "해당 파일의 타입과 업로드 하려는 메뉴가 다릅니다.", "English"),

		/** DRM Exception. */
		DRM_EXCEPTION("ERROR", "복호화 중 오류가 발생하였습니다.", "English"),

		/** 시스템 오류가 발생했습니다. 운영자에게 연락주세요. */
		REDEPLOY_EXCEPTION("ERROR", "재배포에 실패하였습니다. 운영자에게 연락주세요.", "English"),

		NEXACRO_API_EXCEPTION("ERROR", "NEXACRO_API_EXCEPTION. 운영자에게 연락주세요.", "English"),

		/** 시스템 오류가 발생했습니다. 운영자에게 연락주세요. */
		SYSTEM_EXCEPTION("ERROR", "시스템 오류가 발생했습니다. 운영자에게 연락주세요.", "English"),
		
		/** 시스템 오류가 발생했습니다. 운영자에게 연락주세요. */
		IO_KEY_DEPTH_EXCEPTION("ERROR", "전문IO KEY는 MainIO(0뎁스)에만 설정 가능합니다.", "English"),
		
		EXT_IONM_NULL_EXCEPTION("ERROR", "전문 IO명 필수 값 체크 오류", "English"),
		
		MSG_TRANS_MAPPING_EXCEPTION("ERROR", "전문이 변경되었습니다. 매핑정보를 확인 후 저장해 주세요. 강제배포를 원할 시 강제배포 선택 후 배포하여 주세요.", "English"),
		
		EXT_CHILD_IONM_NULL_EXCEPTION("ERROR", "하위IO명이 비어있습니다.", "English"),
		
		/** 시스템 오류가 발생했습니다. 운영자에게 연락주세요. */
		IO_KEY_DUP_EXCEPTION("ERROR", "전문IO KEY는 중복될 수 없습니다.", "English"),
		
		REPL_KEY_DUP_EXCEPTION("ERROR", "개인정보식별자는 인터페이스 송수신, 요청응답으로 구분된 전문에 따라 한개만 설정 가능합니다.", "English"),
		
//		REPL_KEY_PRIVACY_EXCEPTION("ERROR", "전문에 개인정보 코드가 설정되어 있는 경우, 개인정보식별자가 선택되어야합니다.", "English"),
		REPL_KEY_PRIVACY_EXCEPTION2("ERROR", "대외전문의 개인정보식별자는 개인정보가 선택된 필드에만 존재할 수 있습니다.", "English"),
		REPL_KEY_PRIVACY_EXCEPTION3("ERROR", "전문에 개인정보식별자가 존재 할 경우 개인정보제공사유는 필수입력되어야 합니다.", "English"),
		//전문 내 레이아웃 항목들 중에 개인정보가 한 개 이상 설정
		REPL_KEY_PRIVACY_EXCEPTION4("ERROR", "대내전문 개인정보식별자 선택 시 레이아웃 항목들 중에 개인정보가 한 개 이상 설정되어야 합니다.", "English"),
		
		/** 시스템 오류가 발생했습니다. 운영자에게 연락주세요. */
		IO_KEY_LAYOUT_EXCEPTION("ERROR", "전문IO KEY는 LAYOUT 데이터타입에 설정할 수 없습니다.", "English"),
		
		/** 시스템 오류가 발생했습니다. 운영자에게 연락주세요. */
		REPL_KEY_LAYOUT_EXCEPTION("ERROR", "개인정보식별자는 LAYOUT 데이터타입에 설정할 수 없습니다.", "English"),

		/** 시스템 오류가 발생했습니다. 운영자에게 연락주세요. */
		FILE_UPLOAD_EXCEPTION("ERROR", "파일업로드 중 오류가 발생하였습니다. 운영자에게 연락주세요.", "English");

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
