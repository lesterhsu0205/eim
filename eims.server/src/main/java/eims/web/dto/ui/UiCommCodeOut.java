package eims.web.dto.ui;

import java.util.List;

import eims.web.dto.table.CommCodeDto;

public class UiCommCodeOut {

	private int totalCnt;
	private List<CommCodeDto> commCodeDtoList;

	public int getTotalCnt() {
		return totalCnt;
	}

	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
	}

	public List<CommCodeDto> getCommCodeDtoList() {
		return commCodeDtoList;
	}

	public void setCommCodeDtoList(List<CommCodeDto> commCodeDtoList) {
		this.commCodeDtoList = commCodeDtoList;
	}

}
