package eims.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import eims.web.dto.table.ExtrnlinstcdDto;

@Mapper
public interface ExtrnlinstcdDao {

	ExtrnlinstcdDto selectExtrnlinstcd(@Param("instCd") String instCd, @Param("instDstnctnVal") String instDstnctnVal);


	int insertExtrnlinstcd(ExtrnlinstcdDto extrnlinstcd);


	int selectAllCnt(@Param("instCd") String instCd, @Param("instCdNm") String instCdNm,
			@Param("instDstnctnVal") String instDstnctnVal, @Param("instCdDesc") String instCdDesc);


	List<ExtrnlinstcdDto> selectAll(@Param("instCd") String instCd, @Param("instCdNm") String instCdNm,
			@Param("instDstnctnVal") String instDstnctnVal, @Param("instCdDesc") String instCdDesc,
			@Param("pageSize") int pageSize, @Param("pageNumber") int pageNumber);


	int updateExtrnlinstcd(ExtrnlinstcdDto extrnlinstcd);


	int deleteExtrnlinstcd(@Param("instCd") String instCd, @Param("instDstnctnVal") String instDstnctnVal);
	int deleteExtrnlinstcdAll();
	
	
}