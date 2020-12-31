package eims.web.dto.ui;

import java.util.List;
import eims.web.dto.table.DepolysysbsDto;

public class UiDepolysysbsOut {

	private int totalCnt;
	private List<DepolysysbsDto> depolysysbsOutList;

	public int getTotalCnt() {
		return totalCnt;
	}

	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
	}
	
	public List<DepolysysbsDto> getDepolysysbsOutList() {
		return depolysysbsOutList;
	}

	public void setDepolysysbsOutList(List<DepolysysbsDto> depolysysbsOutList) {
		this.depolysysbsOutList = depolysysbsOutList;
	}

}