package eims.web.dto.table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_ABSENT, content = Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BcMetaSysCdDto {

	private String cdVal;
	private String cdValNm;
	private String cdValDesc;

	public String getCdVal() {
		return cdVal;
	}

	public void setCdVal(String cdVal) {
		this.cdVal = cdVal;
	}

	public String getCdValNm() {
		return cdValNm;
	}

	public void setCdValNm(String cdValNm) {
		this.cdValNm = cdValNm;
	}

	public String getCdValDesc() {
		return cdValDesc;
	}

	public void setCdValDesc(String cdValDesc) {
		this.cdValDesc = cdValDesc;
	}

}