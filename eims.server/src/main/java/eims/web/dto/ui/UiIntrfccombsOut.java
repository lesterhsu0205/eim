package eims.web.dto.ui;

import java.util.List;
import eims.web.dto.table.IntrfccombsListDto;

public class UiIntrfccombsOut {

	private int totalCnt;
	private List<IntrfccombsListDto> intrfccombsOutList;

	public int getTotalCnt() {
		return totalCnt;
	}

	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
	}
	
	public List<IntrfccombsListDto> getIntrfccombsOutList() {
		return intrfccombsOutList;
	}

	public void setIntrfccombsOutList(List<IntrfccombsListDto> intrfccombsOutList) {
		this.intrfccombsOutList = intrfccombsOutList;
	}

}