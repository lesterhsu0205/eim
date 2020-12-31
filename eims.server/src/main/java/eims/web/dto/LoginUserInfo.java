package eims.web.dto;

import java.util.List;

import eims.web.dto.table.PermDto;
import eims.web.dto.table.UserDto;
import eims.web.dto.ui.UiMenuTreeInfo;

public class LoginUserInfo {

	private SessionInfo sessionInfo;
	private UserDto userDto;
	private List<PermDto> permList;
	private List<UiMenuTreeInfo> menuList;


	public SessionInfo getSessionInfo() {
		return sessionInfo;
	}


	public List<PermDto> getPermList() {
		return permList;
	}


	public void setPermList(List<PermDto> permList) {
		this.permList = permList;
	}


	public void setSessionInfo(SessionInfo sessionInfo) {
		this.sessionInfo = sessionInfo;
	}


	public UserDto getUserDto() {
		return userDto;
	}


	public void setUserDto(UserDto userDto) {
		this.userDto = userDto;
	}


	public List<UiMenuTreeInfo> getMenuList() {
		return menuList;
	}


	public void setMenuList(List<UiMenuTreeInfo> menuList) {
		this.menuList = menuList;
	}

}
