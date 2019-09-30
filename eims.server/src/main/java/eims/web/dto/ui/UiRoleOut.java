package eims.web.dto.ui;

import java.util.List;

import eims.web.dto.table.RoleDto;

public class UiRoleOut {

	private int totalCnt;
	private List<RoleDto> roleDtoList;

	public int getTotalCnt() {
		return totalCnt;
	}

	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
	}

	public List<RoleDto> getRoleDtoList() {
		return roleDtoList;
	}

	public void setRoleDtoList(List<RoleDto> roleDtoList) {
		this.roleDtoList = roleDtoList;
	}

}
