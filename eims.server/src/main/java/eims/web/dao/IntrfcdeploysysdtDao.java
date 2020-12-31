package eims.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import eims.web.dto.table.IntrfcdeploysysdtDto;

@Mapper
public interface IntrfcdeploysysdtDao {

	List<IntrfcdeploysysdtDto> selectIntrfcdeploysysdt(@Param("intrfcId") String intrfcId);

	String selectDeploySystemUrl(@Param("intrfcId") String intrfcId, @Param("deploySystem") String deploySystem);

	int insertIntrfcdeploysysdt(IntrfcdeploysysdtDto intrfcdeploysysdt);

	int selectAllCnt(@Param("intrfcId") String intrfcId);

	List<IntrfcdeploysysdtDto> selectAll(@Param("intrfcId") String intrfcId,
			@Param("pageSize") int pageSize,
			@Param("pageNumber") int pageNumber);

	int updateIntrfcdeploysysdt(IntrfcdeploysysdtDto intrfcdeploysysdt);

	int deleteIntrfcdeploysysdt(@Param("intrfcId") String intrfcId);
}