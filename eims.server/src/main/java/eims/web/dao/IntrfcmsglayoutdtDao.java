package eims.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import eims.web.dto.table.IntrfcmsglayoutdtDto;

@Mapper
public interface IntrfcmsglayoutdtDao {

	List<IntrfcmsglayoutdtDto> selectIntrfcmsglayoutdt(@Param("intrfcId") String intrfcId);

	int insertIntrfcmsglayoutdt(IntrfcmsglayoutdtDto intrfcmsglayoutdt);

	int selectAllCnt(@Param("intrfcId") String intrfcId,
			@Param("srTypeCd") String srTypeCd,
			@Param("srSeq") int srSeq,
			@Param("rqstRspsTypeCd") String rqstRspsTypeCd,
			@Param("rqstRspsSeq") int rqstRspsSeq,
			@Param("sysCd") String sysCd,
			@Param("msgLayoutId") String msgLayoutId);

	List<IntrfcmsglayoutdtDto> selectAll(@Param("intrfcId") String intrfcId,
			@Param("srTypeCd") String srTypeCd,
			@Param("srSeq") int srSeq,
			@Param("rqstRspsTypeCd") String rqstRspsTypeCd,
			@Param("rqstRspsSeq") int rqstRspsSeq,
			@Param("sysCd") String sysCd,
			@Param("msgLayoutId") String msgLayoutId,
			@Param("pageSize") int pageSize,
			@Param("pageNumber") int pageNumber);

	int updateIntrfcmsglayoutdt(IntrfcmsglayoutdtDto intrfcmsglayoutdt);

	int deleteIntrfcmsglayoutdt(@Param("intrfcId") String intrfcId);
}