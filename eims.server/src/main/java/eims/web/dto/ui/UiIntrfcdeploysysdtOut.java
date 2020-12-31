package eims.web.dto.ui;

import java.util.List;
import eims.web.dto.table.IntrfcdeploysysdtDto;

public class UiIntrfcdeploysysdtOut {

	private int totalCnt;
	private List<IntrfcdeploysysdtDto> intrfcdeploysysdtOutList;

	public int getTotalCnt() {
		return totalCnt;
	}

	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
	}
	
	public List<IntrfcdeploysysdtDto> getIntrfcdeploysysdtOutList() {
		return intrfcdeploysysdtOutList;
	}

	public void setIntrfcdeploysysdtOutList(List<IntrfcdeploysysdtDto> intrfcdeploysysdtOutList) {
		this.intrfcdeploysysdtOutList = intrfcdeploysysdtOutList;
	}

}