package eims.web.dto.table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_ABSENT, content = Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EnccdDto {

	String encCd; 
	String encNm;
	
	public String getEncCd() {
		return encCd;
	}
	public void setEncCd(String encCd) {
		this.encCd = encCd;
	}
	public String getEncNm() {
		return encNm;
	}
	public void setEncNm(String encNm) {
		this.encNm = encNm;
	}
}