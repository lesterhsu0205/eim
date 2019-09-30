package eims.web.dto.ui;

import java.util.List;
import eims.web.dto.table.SrsysbsDto;

public class UiSrsysbsOut {

	private int totalCnt;
	private List<SrsysbsDto> srsysbsOutList;

	public int getTotalCnt() {
		return totalCnt;
	}

	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
	}
	
	public List<SrsysbsDto> getSrsysbsOutList() {
		return srsysbsOutList;
	}

	public void setSrsysbsOutList(List<SrsysbsDto> srsysbsOutList) {
		this.srsysbsOutList = srsysbsOutList;
	}

}