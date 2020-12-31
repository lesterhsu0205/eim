package eims.web.dto.ui;

import java.util.List;
import eims.web.dto.table.TrxcdDto;

public class UiTrxcdOut {

	private int totalCnt;
	private List<TrxcdDto> trxcdOutList;

	public int getTotalCnt() {
		return totalCnt;
	}

	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
	}
	
	public List<TrxcdDto> getTrxcdOutList() {
		return trxcdOutList;
	}

	public void setTrxcdOutList(List<TrxcdDto> trxcdOutList) {
		this.trxcdOutList = trxcdOutList;
	}

}