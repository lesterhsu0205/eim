package eims.web.dto.ui;

import java.util.List;
import eims.web.dto.table.ExtrnlinstcdDto;

public class UiExtrnlinstcdOut {

	private int totalCnt;
	private List<ExtrnlinstcdDto> extrnlinstcdOutList;

	public int getTotalCnt() {
		return totalCnt;
	}

	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
	}
	
	public List<ExtrnlinstcdDto> getExtrnlinstcdOutList() {
		return extrnlinstcdOutList;
	}

	public void setExtrnlinstcdOutList(List<ExtrnlinstcdDto> extrnlinstcdOutList) {
		this.extrnlinstcdOutList = extrnlinstcdOutList;
	}

}