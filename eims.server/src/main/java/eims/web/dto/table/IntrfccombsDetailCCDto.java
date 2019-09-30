package eims.web.dto.table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_ABSENT, content = Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class IntrfccombsDetailCCDto {
	private String intrfDesc;

	public String getIntrfDesc() {
		return intrfDesc;
	}

	public void setIntrfDesc(String intrfDesc) {
		this.intrfDesc = intrfDesc;
	}
	
	
}