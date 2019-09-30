package eims.web.dto.ui;

import java.util.List;
import eims.web.dto.table.AppcdDto;

public class UiAppcdOut {

	private int totalCnt;
	private List<AppcdDto> appcdOutList;

	public int getTotalCnt() {
		return totalCnt;
	}

	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
	}
	
	public List<AppcdDto> getAppcdOutList() {
		return appcdOutList;
	}

	public void setAppcdOutList(List<AppcdDto> appcdOutList) {
		this.appcdOutList = appcdOutList;
	}

}