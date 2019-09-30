package eims.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import eims.web.dto.table.IntrfcroutinfodtDto;

@Mapper
public interface IntrfcroutinfodtDao {

	List<IntrfcroutinfodtDto> selectIntrfcroutinfodt(@Param("intrfcId") String intrfcId);

	int insertIntrfcroutinfodt(IntrfcroutinfodtDto intrfcroutinfodt);

	int selectAllCnt(@Param("intrfcId") String intrfcId,
			@Param("seq") int seq,
			@Param("lenFldOffsetLen") int lenFldOffsetLen,
			@Param("fldDataLen") int fldDataLen,
			@Param("fldCfgVal") String fldCfgVal);

	List<IntrfcroutinfodtDto> selectAll(@Param("intrfcId") String intrfcId,
			@Param("seq") int seq,
			@Param("lenFldOffsetLen") int lenFldOffsetLen,
			@Param("fldDataLen") int fldDataLen,
			@Param("fldCfgVal") String fldCfgVal,
			@Param("pageSize") int pageSize,
			@Param("pageNumber") int pageNumber);

	int updateIntrfcroutinfodt(IntrfcroutinfodtDto intrfcroutinfodt);

	int deleteIntrfcroutinfodt(@Param("intrfcId") String intrfcId);
}