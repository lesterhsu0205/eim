package eims.web.dto.ui;

import java.util.List;

import eims.web.dto.table.EnccdDto;

public class UiEncOut {

	private int totalCnt;
	private List<EnccdDto> encOutList;
	
	public int getTotalCnt() {
		return totalCnt;
	}
	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
	}
	public List<EnccdDto> getEncOutList() {
		return encOutList;
	}
	public void setEncOutList(List<EnccdDto> encOutList) {
		this.encOutList = encOutList;
	}
}