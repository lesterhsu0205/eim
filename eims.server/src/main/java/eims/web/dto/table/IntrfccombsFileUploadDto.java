package eims.web.dto.table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_ABSENT, content = Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class IntrfccombsFileUploadDto {

	boolean isAdd;
	IntrfccombsDto intrfccombsDto;

	public boolean isAdd() {
		return isAdd;
	}

	public void setAdd(boolean isAdd) {
		this.isAdd = isAdd;
	}

	public IntrfccombsDto getIntrfccombsDto() {
		return intrfccombsDto;
	}

	public void setIntrfccombsDto(IntrfccombsDto intrfccombsDto) {
		this.intrfccombsDto = intrfccombsDto;
	}

}