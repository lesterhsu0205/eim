package eims.web.dto.ui;

import java.util.List;
import eims.web.dto.table.BizcdDto;

public class UiBizcdOut {

	private int totalCnt;
	private List<BizcdDto> bizcdOutList;

	public int getTotalCnt() {
		return totalCnt;
	}

	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
	}
	
	public List<BizcdDto> getBizcdOutList() {
		return bizcdOutList;
	}

	public void setBizcdOutList(List<BizcdDto> bizcdOutList) {
		this.bizcdOutList = bizcdOutList;
	}

}