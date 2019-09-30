package eims.web.dto.ui;

import java.util.List;

import eims.web.dto.table.UserDto;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringCodegen",
        date = "2016-11-14T13:48:25.969+09:00")

public class UiUserOut {

	private int totalCnt;
	private List<UserDto> userOutList;

	public int getTotalCnt() {
		return totalCnt;
	}

	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
	}

	public List<UserDto> getUserOutList() {
		return userOutList;
	}

	public void setUserOutList(List<UserDto> userOutList) {
		this.userOutList = userOutList;
	}

}
