package eims.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import eims.web.dto.table.MappingfuncbsDto;

@Mapper
public interface MappingfuncbsDao {

	MappingfuncbsDto selectMappingfuncbs(@Param("mappingFuncNm") String mappingFuncNm);


	int insertMappingfuncbs(MappingfuncbsDto mappingfuncbs);


	int selectAllCnt(@Param("mappingFuncNm") String mappingFuncNm, @Param("argsCnt") int argsCnt,
			@Param("guideDesc") String guideDesc);


	List<MappingfuncbsDto> selectAll(@Param("mappingFuncNm") String mappingFuncNm, @Param("argsCnt") int argsCnt,
			@Param("guideDesc") String guideDesc, @Param("pageSize") int pageSize, @Param("pageNumber") int pageNumber);


	int updateMappingfuncbs(MappingfuncbsDto mappingfuncbs);


	int deleteMappingfuncbs(@Param("mappingFuncNm") String mappingFuncNm);
}