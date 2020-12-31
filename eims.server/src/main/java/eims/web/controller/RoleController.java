package eims.web.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import eims.web.dto.table.MenuRoleRelDto;
import eims.web.dto.table.PermDto;
import eims.web.dto.table.RoleDto;
import eims.web.dto.ui.UiMenuTreeInfo;
import eims.web.dto.ui.UiRoleOut;
import eims.web.service.RoleService;

@Controller
public class RoleController {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private RoleService roleService;

	@RequestMapping(value = "/roles", method = RequestMethod.GET)
	public ResponseEntity<UiRoleOut> getRoles(@RequestParam(value = "roleId", required = false) String roleId,
			@RequestParam(value = "roleNm", required = false) String roleNm,
			@RequestParam(value = "roleDesc", required = false) String roleDesc,
			@RequestParam(value = "basicMenuId", required = false) String basicMenuId,
			@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
			@RequestParam(value = "pageSize", required = false, defaultValue = "1000") int pageSize) {
		logger.debug("INPUT   roleId : [{}],  roleNm : [{}],  roleDesc : [{}],  basicMenuId : [{}]", roleId, roleNm,
				roleDesc, basicMenuId);

		UiRoleOut out = roleService.getList(roleId, roleNm, roleDesc, basicMenuId, pageSize, pageNumber);

		logger.debug(" OUTPUT : {}", out.getTotalCnt());

		return new ResponseEntity<UiRoleOut>(out, HttpStatus.OK);
	}

	@RequestMapping(value = "/roles/{roleId}", method = RequestMethod.GET)
	public ResponseEntity<RoleDto> getRole(@PathVariable(value = "roleId", required = true) String roleId,
			HttpSession session) {

		logger.debug(" INPUT : roleId : [{}]", roleId);

		RoleDto out = roleService.get(roleId);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<RoleDto>(out, HttpStatus.OK);
	}

	@RequestMapping(value = "/roles", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<Integer> addRole(@RequestBody RoleDto roleDto, HttpSession session) {

		logger.debug(" INPUT : RoleDto [{}]", roleDto);

		int out = roleService.add(roleDto);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<Integer>(out, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/roles", method = RequestMethod.PUT)
	public ResponseEntity<Integer> updateRole(@RequestBody RoleDto roleDto, HttpSession session) {

		logger.debug(" INPUT : RoleDto [{}]", roleDto);

		int out = roleService.update(roleDto);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<Integer>(out, HttpStatus.OK);
	}

	@RequestMapping(value = "/roles/{roleId}", method = RequestMethod.DELETE)
	public ResponseEntity<Integer> deleteRole(@PathVariable(value = "roleId", required = true) String roleId,
			HttpSession session) {

		logger.debug(" INPUT : roleId : [{}]", roleId);

		int out = roleService.delete(roleId);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<Integer>(out, HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/roles/{roleId}/menus", method = RequestMethod.GET)
	public ResponseEntity<List<UiMenuTreeInfo>> getRoleMenuReleation(
			@PathVariable(value = "roleId", required = true) String roleId, HttpSession session) {

		logger.debug(" INPUT : roleId [{}]", roleId);

		List<UiMenuTreeInfo> outList = roleService.getMenuList(roleId);

		logger.debug(" OUTPUT : {}", outList.size());

		return new ResponseEntity<List<UiMenuTreeInfo>>(outList, HttpStatus.OK);
	}

	@RequestMapping(value = "/roles/{roleId}/menupopups", method = RequestMethod.GET)
	public ResponseEntity<List<UiMenuTreeInfo>> getRoleMenuReleationPopup(
			@PathVariable(value = "roleId", required = true) String roleId, HttpSession session) {

		logger.debug(" INPUT : roleId [{}]", roleId);

		List<UiMenuTreeInfo> outList = roleService.getMenuListPopup(roleId);

		logger.debug(" OUTPUT : {}", outList.size());

		return new ResponseEntity<List<UiMenuTreeInfo>>(outList, HttpStatus.OK);
	}

	@RequestMapping(value = "/roles/{roleId}/menus", method = RequestMethod.PUT)
	public ResponseEntity<Integer> updateRoleMenuReleation(
			@PathVariable(value = "roleId", required = true) String roleId,
			@RequestBody(required = false) List<UiMenuTreeInfo> treeInfoList, HttpSession session) {

		logger.debug(" INPUT : roleId : [{}], treeInfoList : [{}]", roleId, treeInfoList);

		int out = roleService.updateMenuList(roleId, treeInfoList);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<Integer>(out, HttpStatus.OK);
	}

	@RequestMapping(value = "/roles/{roleId}/menus/{menuId}", method = RequestMethod.DELETE)
	public ResponseEntity<Integer> deleteMenu(@PathVariable(value = "roleId", required = true) String roleId, @PathVariable(value = "menuId", required = true) String menuId,
			HttpSession session) {

		logger.debug(" INPUT : roleId : [{}]", roleId);

		int out = roleService.deleteMenu(roleId, menuId);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<Integer>(out, HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/roles/{roleId}/perms", method = RequestMethod.GET)
	public ResponseEntity<List<PermDto>> getRolePermReleation(
			@PathVariable(value = "roleId", required = true) String roleId,
			@RequestParam(value = "menuId", required = false) String menuId,
			HttpSession session) {

		logger.debug(" INPUT : roleId [{}]", roleId);
		logger.debug(" INPUT : menuId [{}]", menuId);
		
		MenuRoleRelDto menuRole = roleService.getMenuRoleList(roleId, menuId);
				
		
		List<PermDto> outList = roleService.getMenuPermList(roleId, menuRole.getPermId());
		
		//List<PermDto> outList = roleService.getPermList(roleId);

		logger.debug(" OUTPUT : {}", outList.size());

		return new ResponseEntity<List<PermDto>>(outList, HttpStatus.OK);
	}

	@RequestMapping(value = "/roles/{roleId}/permpopups", method = RequestMethod.GET)
	public ResponseEntity<List<PermDto>> getRolePermReleationPopup(
			@PathVariable(value = "roleId", required = true) String roleId, 
			@RequestParam(value = "menuId", required = false) String menuId,
			HttpSession session) {

		logger.debug(" INPUT : roleId [{}]", roleId);
		logger.debug(" INPUT : menuId [{}]", menuId);
		
		MenuRoleRelDto menuRole = roleService.getMenuRoleList(roleId, menuId);
		
		List<PermDto> outList = roleService.getPermListPopup(roleId, menuRole.getPermId());

		logger.debug(" OUTPUT : {}", outList.size());

		return new ResponseEntity<List<PermDto>>(outList, HttpStatus.OK);
	}

	@RequestMapping(value = "/roles/{roleId}/perms", method = RequestMethod.PUT)
	public ResponseEntity<Integer> updateRolePermReleation(
			@PathVariable(value = "roleId", required = true) String roleId,
			@RequestParam(required = false) String menuId,
			@RequestBody(required = false) List<PermDto> permList, HttpSession session) {

		logger.debug(" INPUT : roleId : [{}], menuId : [{}], permList : [{}]", roleId, menuId, permList);

		for(int i = 0; i < permList.size();  i++) {
			System.out.println(permList.get(i).getPermId() +","+ permList.get(i).isCheck());
		}
		int out = roleService.updatePermList(roleId, menuId, permList);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<Integer>(out, HttpStatus.OK);
	}

	@RequestMapping(value = "/roles/{roleId}/perms/{permId}", method = RequestMethod.DELETE)
	public ResponseEntity<Integer> deletePerm(@PathVariable(value = "roleId", required = true) String roleId, @PathVariable(value = "permId", required = true) String permId,
			@RequestParam(required = false) String menuId,
			HttpSession session) {

		logger.debug(" INPUT : roleId : [{}], menuId : [{}]", roleId, menuId);

		int out = roleService.deletePerm(roleId, permId, menuId);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<Integer>(out, HttpStatus.NO_CONTENT);
	}
}
