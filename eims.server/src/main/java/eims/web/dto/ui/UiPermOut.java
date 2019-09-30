package eims.web.dto.ui;

import java.util.List;

import eims.web.dto.table.PermDto;

public class UiPermOut {

	private int totalCnt;
	private List<PermDto> permDtoList;

	public int getTotalCnt() {
		return totalCnt;
	}

	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
	}

	public List<PermDto> getPermDtoList() {
		return permDtoList;
	}

	public void setPermDtoList(List<PermDto> permDtoList) {
		this.permDtoList = permDtoList;
	}

}
