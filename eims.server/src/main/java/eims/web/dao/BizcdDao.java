package eims.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import eims.web.dto.table.BizcdDto;

@Mapper
public interface BizcdDao {

	BizcdDto selectBizcd(@Param("bizCd") String bizCd);


	int insertBizcd(BizcdDto bizcd);


	int selectAllCnt(@Param("bizCd") String bizCd, @Param("bizCdNm") String bizCdNm,
			@Param("bizCdDesc") String bizCdDesc);


	List<BizcdDto> selectAll(@Param("bizCd") String bizCd, @Param("bizCdNm") String bizCdNm,
			@Param("bizCdDesc") String bizCdDesc, @Param("pageSize") int pageSize, @Param("pageNumber") int pageNumber);


	int updateBizcd(BizcdDto bizcd);


	int deleteBizcd(@Param("bizCd") String bizCd);
	int deleteBizcdAll();
	
}