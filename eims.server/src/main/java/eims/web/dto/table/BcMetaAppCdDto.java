package eims.web.dto.table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_ABSENT, content = Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BcMetaAppCdDto {

	private String cdVal;
	private String cdValNm;
	private String pCdId;
	private String pCdValId;
	private String cdValId;

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

	public String getpCdId() {
		return pCdId;
	}

	public void setpCdId(String pCdId) {
		this.pCdId = pCdId;
	}

	public String getpCdValId() {
		return pCdValId;
	}

	public void setpCdValId(String pCdValId) {
		this.pCdValId = pCdValId;
	}

	public String getCdValId() {
		return cdValId;
	}

	public void setCdValId(String cdValId) {
		this.cdValId = cdValId;
	}

}