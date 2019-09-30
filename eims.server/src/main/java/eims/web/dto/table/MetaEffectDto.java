package eims.web.dto.table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_ABSENT, content = Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MetaEffectDto {
	String metaEngNm;
	String intrfcId;
	String intrfcNm;
	String intrfcType;
	String msgLayoutId;

	public String getMetaEngNm() {
		return metaEngNm;
	}

	public void setMetaEngNm(String metaEngNm) {
		this.metaEngNm = metaEngNm;
	}

	public String getIntrfcId() {
		return intrfcId;
	}

	public void setIntrfcId(String intrfcId) {
		this.intrfcId = intrfcId;
	}

	public String getIntrfcNm() {
		return intrfcNm;
	}

	public void setIntrfcNm(String intrfcNm) {
		this.intrfcNm = intrfcNm;
	}

	public String getIntrfcType() {
		return intrfcType;
	}

	public void setIntrfcType(String intrfcType) {
		this.intrfcType = intrfcType;
	}

	public String getMsgLayoutId() {
		return msgLayoutId;
	}

	public void setMsgLayoutId(String msgLayoutId) {
		this.msgLayoutId = msgLayoutId;
	}

}