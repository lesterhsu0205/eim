package eims.web.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import eims.web.dto.table.RolePermRelDto;

@Mapper
public interface RolePermRelDao {

	int selectRolePermRel(RolePermRelDto role);

	int insertRolePermRel(RolePermRelDto role);

	int deleteRolePermRel(@Param("roleId") String roleId);

	int deleteRolePermRel2(RolePermRelDto role);
}
