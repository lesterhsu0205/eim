package eims.web.dto.table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_ABSENT, content = Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MsgInsertDto {
	
	MsglayoutbsDto msglayoutbsDto ;
	boolean insertYn ;
	public MsglayoutbsDto getMsglayoutbsDto() {
		return msglayoutbsDto;
	}
	public void setMsglayoutbsDto(MsglayoutbsDto msglayoutbsDto) {
		this.msglayoutbsDto = msglayoutbsDto;
	}
	public boolean isInsertYn() {
		return insertYn;
	}
	public void setInsertYn(boolean insertYn) {
		this.insertYn = insertYn;
	}
	
	
	
}