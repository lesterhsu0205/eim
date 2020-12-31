package eims.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import eims.web.dto.table.IntrfcdeployhisthsDto;

@Mapper
public interface IntrfcdeployhisthsDao {

	List<IntrfcdeployhisthsDto> selectIntrfcdeployhisths(@Param("intrfcId") String intrfcId);

	IntrfcdeployhisthsDto selectIntrfcdeployhisthsOneRow(@Param("intrfcId") String intrfcId);

	IntrfcdeployhisthsDto selectIntrfcdeployhisthsRaw(@Param("intrfcId") String intrfcId, @Param("deployVersion") int deployVersion);
	
	String selectIntrfcdeployhisthsResultData(@Param("intrfcId") String intrfcId, @Param("deployVersion") Integer deployVersion, @Param("deployDttm") String deployDttm, @Param("deployResultCd") String deployResultCd) ;

	int insertIntrfcdeployhisths(IntrfcdeployhisthsDto intrfcdeployhisths);

	int selectAllCnt(@Param("intrfcId") String intrfcId, @Param("deployResultCd") String deployResultCd);

	List<IntrfcdeployhisthsDto> selectAll(@Param("intrfcId") String intrfcId, @Param("deployResultCd") String deployResultCd,
			@Param("pageSize") int pageSize,
			@Param("pageNumber") int pageNumber);

	int updateIntrfcdeployhisths(IntrfcdeployhisthsDto intrfcdeployhisths);

	int deleteIntrfcdeployhisths(@Param("intrfcId") String intrfcId);
}