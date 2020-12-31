package eims.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import eims.web.dto.table.RoleDto;

@Mapper
public interface RoleDao {

	RoleDto selectRole(@Param("roleId") String roleId);

	int insertRole(RoleDto role);

	int selectAllCnt(@Param("roleId") String roleId, @Param("roleNm") String roleNm, @Param("roleDesc") String roleDesc,
			@Param("basicMenuId") String basicMenuId);

	List<RoleDto> selectAll(@Param("roleId") String roleId, @Param("roleNm") String roleNm,
			@Param("roleDesc") String roleDesc, @Param("basicMenuId") String basicMenuId,
			@Param("pageSize") int pageSize, @Param("pageNumber") int pageNumber);

	int updateRole(RoleDto role);

	int deleteRole(@Param("roleId") String roleId);
}
