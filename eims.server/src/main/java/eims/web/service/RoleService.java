package eims.web.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eims.web.constants.BxMessages;
import eims.web.dao.MenuDao;
import eims.web.dao.MenuRoleRelDao;
import eims.web.dao.PermDao;
import eims.web.dao.RoleDao;
import eims.web.dao.RolePermRelDao;
import eims.web.dto.table.MenuDto;
import eims.web.dto.table.MenuRoleRelDto;
import eims.web.dto.table.PermDto;
import eims.web.dto.table.RoleDto;
import eims.web.dto.table.RolePermRelDto;
import eims.web.dto.ui.UiMenuTreeInfo;
import eims.web.dto.ui.UiRoleOut;
import eims.web.exception.ServiceException;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class RoleService {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private RoleDao roleDao;

	@Autowired
	private MenuDao menuDao;

	@Autowired
	private MenuRoleRelDao menuRoleRelDao;

	@Autowired
	private PermDao permDao;

	@Autowired
	private RolePermRelDao rolePermRelDao;

	public UiRoleOut getList(String roleId, String roleNm, String roleDesc, String basicMenuId, int pageSize,
			int pageNumber) {
		UiRoleOut out = new UiRoleOut();

		int totalCount = roleDao.selectAllCnt(roleId, roleNm, roleDesc, basicMenuId);

		List<RoleDto> roleList = roleDao.selectAll(roleId, roleNm, roleDesc, basicMenuId, pageSize, pageNumber);

		if (roleList == null) {
			roleList = new ArrayList<RoleDto>();
		}

		if (totalCount > 0) {
			out.setTotalCnt(totalCount);
			out.setRoleDtoList(roleList);
		}

		return out;
	}

	public RoleDto get(String roleId) {
		RoleDto roleDto = roleDao.selectRole(roleId);

		if (roleDto == null) {
			throw new ServiceException(BxMessages.Error.NOT_FOUNDED, roleId);
		}

		return roleDto;
	}

	public List<UiMenuTreeInfo> getMenuList(String roleId) {
		List<UiMenuTreeInfo> menuAllList = menuDao.selectAll();
		Map<String, UiMenuTreeInfo> menuMap = new HashMap<String, UiMenuTreeInfo>();

		for (UiMenuTreeInfo menu : menuAllList) {
			menuMap.put(menu.getId(), menu);
		}

		List<UiMenuTreeInfo> menuList = menuDao.selectMenuListByRole(roleId);
		List<UiMenuTreeInfo> menuListResult = new ArrayList<UiMenuTreeInfo>();

		for (UiMenuTreeInfo menuList2 : menuList) {

			logger.debug("-----------------parents---------------------");
			logger.debug(menuList2.getParentId());
			logger.debug("-----------------parents---------------------");

			if (menuList2.getParentId() == null || "".equals(menuList2.getParentId())) {
				menuListResult.add(menuList2);
			}
		}

		for (UiMenuTreeInfo menuList2 : menuList) {
			if (menuList2.getParentId() != null && !"".equals(menuList2.getParentId())) {
				menuListResult.add(menuList2);
			}
		}

		return menuListResult;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public int updateMenuList(String roleId, List<UiMenuTreeInfo> treeInfoList) {
		int out = 0;
		MenuRoleRelDto menuRoleRelDto = new MenuRoleRelDto();
		menuRoleRelDto.setRoleId(roleId);

//		menuRoleRelDao.deleteMenuRoleRel(roleId);

		for (UiMenuTreeInfo treeInfo : treeInfoList) {
			if (treeInfo.isCheck()) {
				menuRoleRelDto.setMenuId(treeInfo.getId());
				if (menuRoleRelDao.selectRoleMenuRel(menuRoleRelDto) == 0) {
					out += menuRoleRelDao.insertMenuRoleRel(menuRoleRelDto);
				}
			}
		}

		return out;
	}

	public List<PermDto> getPermList(String roleId) {
		List<PermDto> permAllList = permDao.selectAll(null, null, null, null, permDao.selectAllCnt(null, null, null, null), 1);
		Map<String, PermDto> permMap = new HashMap<String, PermDto>();

		for (PermDto perm : permAllList) {
			permMap.put(perm.getPermId(), perm);
		}

		List<PermDto> permList = permDao.selectPermListByRole(roleId);
//		for (PermDto perm : permList) {
//			if (permMap.get(perm.getPermId()) != null) {
//				permMap.get(perm.getPermId()).setCheck(true);
//			}
//		}
//		List<PermDto> out = new ArrayList<PermDto>(permMap.values());

		return permList;
	}
	
	public List<PermDto> getMenuPermList(String roleId, String permId) {
		List<PermDto> permAllList = permDao.selectAll(null, null, null, null, permDao.selectAllCnt(null, null, null, null), 1);
		Map<String, PermDto> permMap = new HashMap<String, PermDto>();

		for (PermDto perm : permAllList) {
			permMap.put(perm.getPermId(), perm);
		}

		String[] permSplit = permId.split(",");
		String permStr = "";
		List<PermDto> permList = new ArrayList<>();
		for (String perm : permSplit) {
			PermDto permInfo = permDao.selectMenuPermListByRole(roleId, perm);
			permList.add(permInfo);
		}
		
		
		//List<PermDto> permList = permDao.selectMenuPermListByRole(roleId, permStr);
//		for (PermDto perm : permList) {
//			if (permMap.get(perm.getPermId()) != null) {
//				permMap.get(perm.getPermId()).setCheck(true);
//			}
//		}
//		List<PermDto> out = new ArrayList<PermDto>(permMap.values());

		
		return permList;
	}

	public MenuRoleRelDto getMenuRoleList(String roleId, String menuId) {
		List<PermDto> permAllList = permDao.selectAll(null, null, null, null, permDao.selectAllCnt(null, null, null, null), 1);
		Map<String, PermDto> permMap = new HashMap<String, PermDto>();

		for (PermDto perm : permAllList) {
			permMap.put(perm.getPermId(), perm);
		}

		MenuRoleRelDto menuRole = permDao.selectMenuByRole(roleId,menuId);
//		for (PermDto perm : permList) {
//			if (permMap.get(perm.getPermId()) != null) {
//				permMap.get(perm.getPermId()).setCheck(true);
//			}
//		}
//		List<PermDto> out = new ArrayList<PermDto>(permMap.values());

		return menuRole;
	}
	/*
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public int updatePermList(String roleId, List<PermDto> permList) {
		int out = 0;
		RolePermRelDto rolePermRelDto = new RolePermRelDto();
		rolePermRelDto.setRoleId(roleId);

//		rolePermRelDao.deleteRolePermRel(roleId);

		for (PermDto permInfo : permList) {
			if (permInfo.isCheck()) {
				rolePermRelDto.setPermId(permInfo.getPermId());
				if (rolePermRelDao.selectRolePermRel(rolePermRelDto) == 0) {
					out += rolePermRelDao.insertRolePermRel(rolePermRelDto);
				}
			}
		}

		return out;
	}
	*/
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public int updatePermList(String roleId, String menuId, List<PermDto> permList) {
		int out = 0;
		
		MenuRoleRelDto menuRoleRelDto = permDao.selectMenuByRole(roleId, menuId);
		
		menuRoleRelDto.setRoleId(roleId);
		
		String permStr = "";
		logger.debug(" menuRoleRelDto.getPermId()  : [{}]", menuRoleRelDto.getPermId());
		
		if( menuRoleRelDto.getPermId() != null && menuRoleRelDto.getPermId().length() > 0) {
			permStr = menuRoleRelDto.getPermId() + ",";
		}
			
		for (PermDto permInfo : permList) {
			if (permInfo.isCheck()) { 
			permStr += permInfo.getPermId() + ",";
			}
		}
		
		permStr = permStr.substring(0, permStr.length()-1);
		menuRoleRelDto.setPermId(permStr);
		menuRoleRelDto.setMenuId(menuId);
		
		out = permDao.updateMenuByRole(menuRoleRelDto);
		return out;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public int add(RoleDto in) {
		RoleDto curRoleInfo = roleDao.selectRole(in.getRoleId());

		if (curRoleInfo != null) {
			throw new ServiceException(BxMessages.Error.DUPLICATE_KEY, in.getRoleId());
		}

		return roleDao.insertRole(in);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public int update(RoleDto in) {
		int out = roleDao.updateRole(in);

		if (out == 0) {
			throw new ServiceException(BxMessages.Error.NOT_FOUNDED, in.getRoleId());
		}

		return out;

	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public int delete(String roleId) {
		int out = roleDao.deleteRole(roleId);

		if (out == 0) {
			throw new ServiceException(BxMessages.Error.NOT_FOUNDED, roleId);
		}

		menuRoleRelDao.deleteMenuRoleRel(roleId);
		rolePermRelDao.deleteRolePermRel(roleId);

		return out;
	}

	public int deleteMenu(String roleId, String menuId) {

		MenuDto menuDto = menuDao.selectMenu(menuId);
		String parentId = menuDto.getParentMenuId();

		List<MenuDto> parentMenuList = null;
		int result = 0;
		if (parentId == null || parentId.equals("")) {
			parentMenuList = menuDao.selectMenuChild(menuId);

			menuRoleRelDao.deleteMenuRoleRel2(roleId, menuId);
			for (MenuDto listMenu : parentMenuList) {
				result = menuRoleRelDao.deleteMenuRoleRel2(roleId, listMenu.getMenuId());
			}
		} else {
			result = menuRoleRelDao.deleteMenuRoleRel2(roleId, menuId);
		}

		return result;
	}

	public int deletePerm(String roleId, String permId, String menuId) {

		//MenuRoleRelDto dto = new MenuRoleRelDto();

		MenuRoleRelDto dto = permDao.selectMenuByRole(roleId, menuId);
		
		String[] permSplit = dto.getPermId().split(",");

		String permStr = "";
		for(String str : permSplit) {
			if(!str.equals(permId)) {
				permStr += str + ",";
			}
		}
		
		System.out.println("permStr : "+ permStr);
		if(permStr.length() != 0) {
			permStr = permStr.substring(0, permStr.length() - 1);
		}
		dto.setPermId(permStr);

		int result = permDao.updateMenuByRole(dto);

		return result;
	}
	/*
	public int deletePerm(String roleId, String permId) {

		MenuRoleRelDto dto = new MenuRoleRelDto();

		dto.setRoleId(roleId);
		dto.setMenuId(menuId);		
		int result = permDao.deleteMenuByRole(dto);
		return result;
	}
	*/

	public List<PermDto> getPermListPopup(String roleId, String permId) {
		List<PermDto> permAllList = permDao.selectAll(null, null, null, null, permDao.selectAllCnt(null, null, null, null), 1);
		Map<String, PermDto> permMap = new HashMap<String, PermDto>();
	//	List<PermDto> permList = permDao.selectPermListByRole(roleId);
		
		if (permId == null || permId.equals("")) {
			return permAllList;
		}
		
		String[] permSplit = permId.split(",");
		String permStr = "";
		List<PermDto> permList = new ArrayList<>();
		for (String perm : permSplit) {
			PermDto permInfo = permDao.selectMenuPermListByRole(roleId, perm);
			permList.add(permInfo);
		}



		List<PermDto> permReturn = new ArrayList<PermDto>();
		boolean permBool = true;

		for (PermDto perm : permAllList) {
			permBool = true;
			for (PermDto perm2 : permList) {
				if (perm.getPermId().equals(perm2.getPermId()))
					permBool = false;
			}
			if (permBool) {
				permReturn.add(perm);
			}
		}

//		List<PermDto> permList = permDao.selectPermListByRole(roleId);
//		for (PermDto perm : permList) {
//			if (!permMap.containsKey(perm.getPermId()))
//				permReturn.add(perm);
//		}
//		List<PermDto> out = new ArrayList<PermDto>(permMap.values());

		return permReturn;
	}
	
	/*
	public List<PermDto> getPermListPopup(String roleId) {
		List<PermDto> permAllList = permDao.selectAll(null, null, null, null, permDao.selectAllCnt(null, null, null, null), 1);
		Map<String, PermDto> permMap = new HashMap<String, PermDto>();
		List<PermDto> permList = permDao.selectPermListByRole(roleId);

		if (permList == null) {

			return permAllList;
		}

		List<PermDto> permReturn = new ArrayList<PermDto>();
		boolean permBool = true;

		for (PermDto perm : permAllList) {
			permBool = true;
			for (PermDto perm2 : permList) {
				if (perm.getPermId().equals(perm2.getPermId()))
					permBool = false;
			}
			if (permBool) {
				permReturn.add(perm);
			}
		}

//		List<PermDto> permList = permDao.selectPermListByRole(roleId);
//		for (PermDto perm : permList) {
//			if (!permMap.containsKey(perm.getPermId()))
//				permReturn.add(perm);
//		}
//		List<PermDto> out = new ArrayList<PermDto>(permMap.values());

		return permReturn;
	}
*/
	public List<UiMenuTreeInfo> getMenuListPopup(String roleId) {
		List<UiMenuTreeInfo> menuAllList = menuDao.selectAll();
		List<UiMenuTreeInfo> menuList = menuDao.selectMenuListByRole(roleId);

		if (menuList == null) {

			return menuAllList;
		}

		List<UiMenuTreeInfo> menuListReturn = new ArrayList<UiMenuTreeInfo>();
		boolean menuBool = true;

		for (UiMenuTreeInfo menu : menuAllList) {
			menuBool = true;
			for (UiMenuTreeInfo menu2 : menuList) {
				if (menu.getId().equals(menu2.getId())) {
					if (menu.getParentId() != null && !menu.getParentId().equals(""))
						menuBool = false;
				}
			}
			if (menuBool) {
				menuListReturn.add(menu);
			}
		}

		List<UiMenuTreeInfo> menuListResult = new ArrayList<UiMenuTreeInfo>();

		for (UiMenuTreeInfo menuList2 : menuListReturn) {

			logger.debug("-----------------parents---------------------");
			logger.debug(menuList2.getParentId());
			logger.debug("-----------------parents---------------------");

			if (menuList2.getParentId() == null || "".equals(menuList2.getParentId())) {
				menuListResult.add(menuList2);
			}
		}

		for (UiMenuTreeInfo menuList2 : menuListReturn) {
			if (menuList2.getParentId() != null && !"".equals(menuList2.getParentId())) {
				menuListResult.add(menuList2);
			}
		}

//		for (UiMenuTreeInfo menu : menuList) {
//			if (menuMap.get(menu.getId()) != null) {
//				menuMap.get(menu.getId()).setCheck(true);
//			}
//		}
//		List<UiMenuTreeInfo> out = new ArrayList<UiMenuTreeInfo>(menuMap.values());

		return menuListResult;
	}
}
