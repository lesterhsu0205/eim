package eims.web.dto.ui;

import java.util.List;

import eims.web.dto.table.MenuDto;

public class UiMenuOut {

	private int totalCnt;
	private List<MenuDto> menuOutList;


	public int getTotalCnt() {
		return totalCnt;
	}


	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
	}


	public List<MenuDto> getMenuOutList() {
		return menuOutList;
	}


	public void setMenuOutList(List<MenuDto> menuOutList) {
		this.menuOutList = menuOutList;
	}

}