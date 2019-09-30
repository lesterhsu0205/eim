package eims.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import eims.web.dto.table.DepolysysbsDto;

@Mapper
public interface DepolysysbsDao {

	DepolysysbsDto selectDepolysysbs(@Param("deploySysCd") String deploySysCd);


	int insertDepolysysbs(DepolysysbsDto depolysysbs);


	int selectAllCnt(@Param("deploySysCd") String deploySysCd, @Param("deploySysNm") String deploySysNm,
			@Param("deploySysUrl") String deploySysUrl, @Param("deploySysDesc") String deploySysDesc,
			@Param("deploySysGrpCd") String deploySysGrpCd);


	List<DepolysysbsDto> selectAll(@Param("deploySysCd") String deploySysCd, @Param("deploySysNm") String deploySysNm,
			@Param("deploySysUrl") String deploySysUrl, @Param("deploySysDesc") String deploySysDesc,
			@Param("deploySysGrpCd") String deploySysGrpCd, @Param("pageSize") int pageSize,
			@Param("pageNumber") int pageNumber);


	int updateDepolysysbs(DepolysysbsDto depolysysbs);


	int deleteDepolysysbs(@Param("deploySysCd") String deploySysCd);
}