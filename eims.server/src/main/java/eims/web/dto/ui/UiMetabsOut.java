package eims.web.dto.ui;

import java.util.List;
import eims.web.dto.table.MetabsDto;

public class UiMetabsOut {

	private int totalCnt;
	private List<MetabsDto> metabsOutList;

	public int getTotalCnt() {
		return totalCnt;
	}

	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
	}
	
	public List<MetabsDto> getMetabsOutList() {
		return metabsOutList;
	}

	public void setMetabsOutList(List<MetabsDto> metabsOutList) {
		this.metabsOutList = metabsOutList;
	}

}