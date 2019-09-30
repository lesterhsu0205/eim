package eims.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import eims.web.dto.table.PermDto;

@Mapper
public interface PermDao {

	PermDto selectPerm(@Param("permId") String permId);


	int insertPerm(PermDto perm);


	int selectAllCnt(@Param("permId") String permId, @Param("permNm") String permNm, @Param("permDesc") String permDesc,
			@Param("permTypeCd") String permTypeCd);


	List<PermDto> selectAll(@Param("permId") String permId, @Param("permNm") String permNm,
			@Param("permDesc") String permDesc, @Param("permTypeCd") String permTypeCd, @Param("pageSize") int pageSize,
			@Param("pageNumber") int pageNumber);


	int updatePerm(PermDto perm);


	int deletePerm(@Param("permId") String permId);


	List<PermDto> selectPermListByRole(@Param("roleId") String roleId);
}
