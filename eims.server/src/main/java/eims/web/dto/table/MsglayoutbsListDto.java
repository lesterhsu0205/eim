package eims.web.dto.table;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_ABSENT, content = Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MsglayoutbsListDto {
	

	List<MsglayoutbsDto> msglayoutbsDtoList; // 전문레이아웃 필드 상세 정보

	public List<MsglayoutbsDto> getMsglayoutbsDtoList() {
		return msglayoutbsDtoList;
	}

	public void setMsglayoutbsDtoList(List<MsglayoutbsDto> msglayoutbsDtoList) {
		this.msglayoutbsDtoList = msglayoutbsDtoList;
	}


}