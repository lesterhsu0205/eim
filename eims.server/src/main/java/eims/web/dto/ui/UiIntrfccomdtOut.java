package eims.web.dto.ui;

import java.util.List;
import eims.web.dto.table.IntrfccomdtDto;

public class UiIntrfccomdtOut {

	private int totalCnt;
	private List<IntrfccomdtDto> intrfccomdtOutList;

	public int getTotalCnt() {
		return totalCnt;
	}

	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
	}
	
	public List<IntrfccomdtDto> getIntrfccomdtOutList() {
		return intrfccomdtOutList;
	}

	public void setIntrfccomdtOutList(List<IntrfccomdtDto> intrfccomdtOutList) {
		this.intrfccomdtOutList = intrfccomdtOutList;
	}

}