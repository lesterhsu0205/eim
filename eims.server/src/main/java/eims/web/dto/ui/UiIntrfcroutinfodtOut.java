package eims.web.dto.ui;

import java.util.List;
import eims.web.dto.table.IntrfcroutinfodtDto;

public class UiIntrfcroutinfodtOut {

	private int totalCnt;
	private List<IntrfcroutinfodtDto> intrfcroutinfodtOutList;

	public int getTotalCnt() {
		return totalCnt;
	}

	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
	}
	
	public List<IntrfcroutinfodtDto> getIntrfcroutinfodtOutList() {
		return intrfcroutinfodtOutList;
	}

	public void setIntrfcroutinfodtOutList(List<IntrfcroutinfodtDto> intrfcroutinfodtOutList) {
		this.intrfcroutinfodtOutList = intrfcroutinfodtOutList;
	}

}