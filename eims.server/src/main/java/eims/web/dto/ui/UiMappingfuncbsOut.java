package eims.web.dto.ui;

import java.util.List;
import eims.web.dto.table.MappingfuncbsDto;

public class UiMappingfuncbsOut {

	private int totalCnt;
	private List<MappingfuncbsDto> mappingfuncbsOutList;

	public int getTotalCnt() {
		return totalCnt;
	}

	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
	}
	
	public List<MappingfuncbsDto> getMappingfuncbsOutList() {
		return mappingfuncbsOutList;
	}

	public void setMappingfuncbsOutList(List<MappingfuncbsDto> mappingfuncbsOutList) {
		this.mappingfuncbsOutList = mappingfuncbsOutList;
	}

}