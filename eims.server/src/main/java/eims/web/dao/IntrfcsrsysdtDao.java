package eims.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import eims.web.dto.table.IntrfcsrsysdtDto;

@Mapper
public interface IntrfcsrsysdtDao {

	List<IntrfcsrsysdtDto> selectIntrfcsrsysdt(@Param("intrfcId") String intrfcId);

	int insertIntrfcsrsysdt(IntrfcsrsysdtDto intrfcsrsysdt);

	int selectAllCnt(@Param("intrfcId") String intrfcId,
			@Param("srTypeCd") String srTypeCd,
			@Param("srSeq") int srSeq,
			@Param("sysCd") String sysCd,
			@Param("trxCd") String trxCd,
			@Param("filePath") String filePath);

	List<IntrfcsrsysdtDto> selectAll(@Param("intrfcId") String intrfcId,
			@Param("srTypeCd") String srTypeCd,
			@Param("srSeq") int srSeq,
			@Param("sysCd") String sysCd,
			@Param("trxCd") String trxCd,
			@Param("filePath") String filePath,
			@Param("pageSize") int pageSize,
			@Param("pageNumber") int pageNumber);

	int updateIntrfcsrsysdt(IntrfcsrsysdtDto intrfcsrsysdt);

	int deleteIntrfcsrsysdt(@Param("intrfcId") String intrfcId);
}