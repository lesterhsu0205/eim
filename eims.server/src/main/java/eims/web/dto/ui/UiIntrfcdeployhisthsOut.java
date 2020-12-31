package eims.web.dto.ui;

import java.util.List;
import eims.web.dto.table.IntrfcdeployhisthsDto;

public class UiIntrfcdeployhisthsOut {

	private int totalCnt;
	private List<IntrfcdeployhisthsDto> intrfcdeployhisthsOutList;

	public int getTotalCnt() {
		return totalCnt;
	}

	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
	}
	
	public List<IntrfcdeployhisthsDto> getIntrfcdeployhisthsOutList() {
		return intrfcdeployhisthsOutList;
	}

	public void setIntrfcdeployhisthsOutList(List<IntrfcdeployhisthsDto> intrfcdeployhisthsOutList) {
		this.intrfcdeployhisthsOutList = intrfcdeployhisthsOutList;
	}

}