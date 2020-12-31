package eims.web.dao;

import java.util.List;
import eims.web.dto.table.IntrfccomdtDto;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IntrfccomdtDao{
	
	IntrfccomdtDto selectIntrfccomdt(@Param("intrfcId") String intrfcId, 
				@Param("trxCd") String trxCd, 
				@Param("bizCd") String bizCd, 
				@Param("instCd") String instCd, 
				@Param("trxTypeCd") String trxTypeCd);

	int insertIntrfccomdt(IntrfccomdtDto intrfccomdt);

	int selectAllCnt(@Param("intrfcId") String intrfcId, 
				@Param("trxCd") String trxCd, 
				@Param("bizCd") String bizCd, 
				@Param("instCd") String instCd, 
				@Param("trxTypeCd") String trxTypeCd);

	List<IntrfccomdtDto> selectAll(@Param("intrfcId") String intrfcId, 
				@Param("trxCd") String trxCd, 
				@Param("bizCd") String bizCd, 
				@Param("instCd") String instCd, 
				@Param("trxTypeCd") String trxTypeCd,
	        	@Param("pageSize") int pageSize,
	        	@Param("pageNumber") int pageNumber);

	int updateIntrfccomdt(IntrfccomdtDto intrfccomdt);

	int deleteIntrfccomdt(@Param("intrfcId") String intrfcId, 
				@Param("trxCd") String trxCd, 
				@Param("bizCd") String bizCd, 
				@Param("instCd") String instCd, 
				@Param("trxTypeCd") String trxTypeCd);
}