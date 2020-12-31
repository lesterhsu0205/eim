package eims.web.dto.table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_ABSENT, content = Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MaskcdDto {

	String maskCd; 
	String maskNm;
	
	public String getMaskCd() {
		return maskCd;
	}
	public void setMaskCd(String maskCd) {
		this.maskCd = maskCd;
	}
	public String getMaskNm() {
		return maskNm;
	}
	public void setMaskNm(String maskNm) {
		this.maskNm = maskNm;
	} 
	
	

}