package eims.web.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AdminLayoutInfo {

	private String dtoClass;
	private List<GenLayoutDetailDto> genLayoutDetailList;

	public String getDtoClass() {
		return dtoClass;
	}

	public void setDtoClass(String dtoClass) {
		this.dtoClass = dtoClass;
	}

	public List<GenLayoutDetailDto> getGenLayoutDetailList() {
		return genLayoutDetailList;
	}

	@JsonProperty("layoutDetailList")
	public void setGenLayoutDetailList(List<GenLayoutDetailDto> genLayoutDetailList) {
		this.genLayoutDetailList = genLayoutDetailList;
	}

}
