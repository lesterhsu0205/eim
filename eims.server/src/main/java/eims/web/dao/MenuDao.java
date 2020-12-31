package eims.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import eims.web.dto.table.MenuDto;
import eims.web.dto.ui.UiMenuTreeInfo;

@Mapper
public interface MenuDao {

	MenuDto selectMenu(@Param("menuId") String menuId);

	List<MenuDto> selectMenuChild(@Param("menuId") String menuId);

	int insertMenu(MenuDto menu);

	List<UiMenuTreeInfo> selectAll();

	int updateMenu(MenuDto menu);

	int deleteMenu(@Param("menuId") String menuId);

	List<UiMenuTreeInfo> selectMenuListByRole(@Param("roleId") String roleId);
}
