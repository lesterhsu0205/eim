package eims.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import eims.web.dto.table.ActionhisthsDto;

@Mapper
public interface ActionhisthsDao {

	ActionhisthsDto selectActionhisths();

	int insertActionhisths(ActionhisthsDto actionhisths);

	int selectAllCnt(@Param("hstDscd") String hstDscd, @Param("itemId") String itemId,
			@Param("workCttCd") String workCttCd, @Param("workDttmFrom") String workDttmFrom,
			@Param("workDttmTo") String workDttmTo,
			@Param("userId") String userId,
			@Param("itemDesc") String itemDesc);

	List<ActionhisthsDto> selectAll(@Param("hstDscd") String hstDscd, @Param("itemId") String itemId,
			@Param("workCttCd") String workCttCd, @Param("workDttmFrom") String workDttmFrom,
			@Param("workDttmTo") String workDttmTo, @Param("userId") String userId,
			@Param("itemDesc") String itemDesc, @Param("pageSize") int pageSize, @Param("pageNumber") int pageNumber);

	int updateActionhisths(ActionhisthsDto actionhisths);

	int deleteActionhisths();
}