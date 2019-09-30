package eims.web.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import eims.web.dto.table.MenuRoleRelDto;

@Mapper
public interface MenuRoleRelDao {

	int insertMenuRoleRel(MenuRoleRelDto menu);

	int deleteMenuRoleRel(@Param("roleId") String roleId);

	int deleteMenuRoleRel2(@Param("roleId") String roleId, @Param("menuId") String menuId);

	int selectRoleMenuRel(MenuRoleRelDto menuRoleRelDto);
}
