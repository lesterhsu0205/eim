package eims.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import eims.web.dto.table.CommCodeDto;

@Mapper
public interface CommCodeDao {

	CommCodeDto selectCommCode(@Param("cdId") String cdId,
			@Param("cdVal") String cdVal,
			@Param("langCd") String langCd);

	CommCodeDto selectCommCodeValue(@Param("cdId") String cdId,
			@Param("cdValNm") String cdValNm,
			@Param("langCd") String langCd);

	int insertCommCode(CommCodeDto commCode);

	int selectAllCnt(@Param("cdId") String cdId,
			@Param("cdNm") String cdNm);

	List<CommCodeDto> selectAll(@Param("cdId") String cdId,
			@Param("cdNm") String cdNm,
			@Param("pageSize") int pageSize,
			@Param("pageNumber") int pageNumber);

	List<CommCodeDto> selectCommonCodeList(@Param("cdId") String cdId);

	int updateCommCode(CommCodeDto commCode);

	int deleteCommCode(@Param("cdId") String cdId,
			@Param("cdVal") String cdVal,
			@Param("langCd") String langCd);
}
