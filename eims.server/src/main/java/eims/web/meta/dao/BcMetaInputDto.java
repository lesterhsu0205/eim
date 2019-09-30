package eims.web.meta.dao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_ABSENT, content = Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BcMetaInputDto {

	String tobeAreaId; //투비차세대
	String applicationAreaId;

	public String getTobeAreaId() {
		return tobeAreaId;
	}

	public void setTobeAreaId(String tobeAreaId) {
		this.tobeAreaId = tobeAreaId;
	}

	public String getApplicationAreaId() {
		return applicationAreaId;
	}

	public void setApplicationAreaId(String applicationAreaId) {
		this.applicationAreaId = applicationAreaId;
	}

}