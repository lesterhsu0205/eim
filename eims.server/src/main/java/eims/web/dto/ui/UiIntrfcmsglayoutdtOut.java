package eims.web.dto.ui;

import java.util.List;
import eims.web.dto.table.IntrfcmsglayoutdtDto;

public class UiIntrfcmsglayoutdtOut {

	private int totalCnt;
	private List<IntrfcmsglayoutdtDto> intrfcmsglayoutdtOutList;

	public int getTotalCnt() {
		return totalCnt;
	}

	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
	}
	
	public List<IntrfcmsglayoutdtDto> getIntrfcmsglayoutdtOutList() {
		return intrfcmsglayoutdtOutList;
	}

	public void setIntrfcmsglayoutdtOutList(List<IntrfcmsglayoutdtDto> intrfcmsglayoutdtOutList) {
		this.intrfcmsglayoutdtOutList = intrfcmsglayoutdtOutList;
	}

}