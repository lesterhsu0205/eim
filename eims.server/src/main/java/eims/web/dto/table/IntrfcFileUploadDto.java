package eims.web.dto.table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_ABSENT, content = Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class IntrfcFileUploadDto {
	String intrfcTypeCd; // 인터페이스타입코드

	public String getIntrfcTypeCd() {
		return intrfcTypeCd;
	}

	public void setIntrfcTypeCd(String intrfcTypeCd) {
		this.intrfcTypeCd = intrfcTypeCd;
	}

}