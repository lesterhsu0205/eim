package eims.web.dto.ui;

import java.util.List;
import eims.web.dto.table.IntrfcsrsysdtDto;

public class UiIntrfcsrsysdtOut {

	private int totalCnt;
	private List<IntrfcsrsysdtDto> intrfcsrsysdtOutList;

	public int getTotalCnt() {
		return totalCnt;
	}

	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
	}
	
	public List<IntrfcsrsysdtDto> getIntrfcsrsysdtOutList() {
		return intrfcsrsysdtOutList;
	}

	public void setIntrfcsrsysdtOutList(List<IntrfcsrsysdtDto> intrfcsrsysdtOutList) {
		this.intrfcsrsysdtOutList = intrfcsrsysdtOutList;
	}

}