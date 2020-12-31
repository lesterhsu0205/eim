package eims.web.dto.ui;

import java.util.List;
import eims.web.dto.table.MsglayoutbsDto;

public class UiMsglayoutbsOut {

	private int totalCnt;
	private List<MsglayoutbsDto> msglayoutbsOutList;

	public int getTotalCnt() {
		return totalCnt;
	}

	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
	}
	
	public List<MsglayoutbsDto> getMsglayoutbsOutList() {
		return msglayoutbsOutList;
	}

	public void setMsglayoutbsOutList(List<MsglayoutbsDto> msglayoutbsOutList) {
		this.msglayoutbsOutList = msglayoutbsOutList;
	}

}