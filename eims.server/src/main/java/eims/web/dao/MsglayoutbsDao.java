package eims.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import eims.web.dto.table.IntrfcdeployhisthsDto;
import eims.web.dto.table.MsgIdCreateDto;
import eims.web.dto.table.MsgLayoutEffectDto;
import eims.web.dto.table.MsglayoutbsDto;

@Mapper
public interface MsglayoutbsDao {

	List<IntrfcdeployhisthsDto> selectDeployList();
	
	MsglayoutbsDto selectMsglayoutbs(@Param("msgLayoutId") String msgLayoutId);

	List<MsgLayoutEffectDto> selectMsglayoutEffect(@Param("msgLayoutId") String msgLayoutId, @Param("intrfcId") String intrfcId, @Param("intrfcNm") String intrfcNm);

	int insertMsglayoutbs(MsglayoutbsDto msglayoutbs);

	Integer selectIdSeq(MsgIdCreateDto in);

	Integer selectIdSeqFile(MsgIdCreateDto in);
	
	int checkLayoutId(@Param("msgLayoutId") String msgLayoutId);

	int selectAllCnt(@Param("msgNm") String msgNm,
			@Param("msgNmSub") String msgNmSub,
			@Param("msgVersion") int msgVersion,
			@Param("msgDscd") String msgDscd,
			@Param("regManId") String regManId,
			@Param("regDttm") String regDttm,
			@Param("msgDataVal") String msgDataVal,
			@Param("msgDesc") String msgDesc,
			@Param("extrnlBizNm") String extrnlBizNm,
			@Param("dtoNm") String dtoNm,
			@Param("msgLayoutId") String msgLayoutId,
			@Param("appTypeCd") String appTypeCd,
			@Param("trxCd") String trxCd,
			@Param("appNm") String appNm,
			@Param("lv1Cd") String lv1Cd,
			@Param("lv2Cd") String lv2Cd,
			@Param("lv3Cd") String lv3Cd,
			@Param("lv4Cd") String lv4Cd,
			@Param("lv5Cd") String lv5Cd,
			@Param("trxDscd") String trxDscd,
			@Param("bitMapCrtnYn") String bitMapCrtnYn,
			@Param("jobId") String jobId,
			@Param("chlDscd") String chlDscd,
			@Param("iso8583DataTypeCd") String iso8583DataTypeCd,
			@Param("crtnSeq") Integer crtnSeq,
			@Param("rsrvFldVal1") String rsrvFldVal1,
			@Param("rsrvFldVal2") String rsrvFldVal2,
			@Param("rsrvFldVal3") String rsrvFldVal3,
			@Param("bitMapTypeCd") String bitMapTypeCd,
			@Param("cfgDesc") String cfgDesc,
			@Param("indsYn") String indsYn,
			@Param("workStatusCd") String workStatusCd,
			@Param("custApiYn") String custApiYn,
			@Param("msgRvsNo") int msgRvsNo
			);

	List<MsglayoutbsDto> selectAll(@Param("msgNm") String msgNm,
			@Param("msgNmSub") String msgNmSub,
			@Param("msgVersion") int msgVersion,
			@Param("msgDscd") String msgDscd,
			@Param("regManId") String regManId,
			@Param("regDttm") String regDttm,
			@Param("msgDataVal") String msgDataVal,
			@Param("msgDesc") String msgDesc,
			@Param("extrnlBizNm") String extrnlBizNm,
			@Param("dtoNm") String dtoNm,
			@Param("msgLayoutId") String msgLayoutId,
			@Param("appTypeCd") String appTypeCd,
			@Param("trxCd") String trxCd,
			@Param("appNm") String appNm,
			@Param("lv1Cd") String lv1Cd,
			@Param("lv2Cd") String lv2Cd,
			@Param("lv3Cd") String lv3Cd,
			@Param("lv4Cd") String lv4Cd,
			@Param("lv5Cd") String lv5Cd,
			@Param("trxDscd") String trxDscd,
			@Param("bitMapCrtnYn") String bitMapCrtnYn,
			@Param("jobId") String jobId,
			@Param("chlDscd") String chlDscd,
			@Param("iso8583DataTypeCd") String iso8583DataTypeCd,
			@Param("crtnSeq") Integer crtnSeq,
			@Param("rsrvFldVal1") String rsrvFldVal1,
			@Param("rsrvFldVal2") String rsrvFldVal2,
			@Param("rsrvFldVal3") String rsrvFldVal3,
			@Param("bitMapTypeCd") String bitMapTypeCd,
			@Param("cfgDesc") String cfgDesc,
			@Param("indsYn") String indsYn,
			@Param("workStatusCd") String workStatusCd,
			@Param("custApiYn") String custApiYn,
			@Param("msgRvsNo") int msgRvsNo,
			@Param("pageSize") int pageSize,
			@Param("pageNumber") int pageNumber);

	List<MsglayoutbsDto> selectAllNotLike(@Param("msgNm") String msgNm,
			@Param("msgVersion") int msgVersion,
			@Param("msgDscd") String msgDscd,
			@Param("regManId") String regManId,
			@Param("regDttm") String regDttm,
			@Param("msgDataVal") String msgDataVal,
			@Param("msgDesc") String msgDesc,
			@Param("extrnlBizNm") String extrnlBizNm,
			@Param("dtoNm") String dtoNm,
			@Param("msgLayoutId") String msgLayoutId,
			@Param("appTypeCd") String appTypeCd,
			@Param("trxCd") String trxCd,
			@Param("appNm") String appNm,
			@Param("lv1Cd") String lv1Cd,
			@Param("lv2Cd") String lv2Cd,
			@Param("lv3Cd") String lv3Cd,
			@Param("lv4Cd") String lv4Cd,
			@Param("lv5Cd") String lv5Cd,
			@Param("trxDscd") String trxDscd,
			@Param("bitMapCrtnYn") String bitMapCrtnYn,
			@Param("jobId") String jobId,
			@Param("chlDscd") String chlDscd,
			@Param("iso8583DataTypeCd") String iso8583DataTypeCd,
			@Param("crtnSeq") Integer crtnSeq,
			@Param("rsrvFldVal1") String rsrvFldVal1,
			@Param("rsrvFldVal2") String rsrvFldVal2,
			@Param("rsrvFldVal3") String rsrvFldVal3,
			@Param("bitMapTypeCd") String bitMapTypeCd,
			@Param("cfgDesc") String cfgDesc,
			@Param("indsYn") String indsYn,
			@Param("workStatusCd") String workStatusCd,
			@Param("custApiYn") String custApiYn,
			@Param("msgRvsNo") int msgRvsNo,
			@Param("pageSize") int pageSize,
			@Param("pageNumber") int pageNumber);

	int updateMsglayoutbs(MsglayoutbsDto msglayoutbs);

	int deleteMsglayoutbs(@Param("msgLayoutId") String msgLayoutId);

	List<MsglayoutbsDto> selectExtrnlMsgList(
			@Param("regManId") String regManId,
			@Param("msgDataVal") String msgDataVal,
			@Param("extrnlBizNm") String extrnlBizNm,
			@Param("msgLayoutId") String msgLayoutId,
			@Param("trxDscd") String trxDscd,
			@Param("pageSize") int pageSize,
			@Param("pageNumber") int pageNumber);

	int selectExtrnlMsgListCnt(
			@Param("regManId") String regManId,
			@Param("msgDataVal") String msgDataVal,
			@Param("extrnlBizNm") String extrnlBizNm,
			@Param("msgLayoutId") String msgLayoutId,
			@Param("trxDscd") String trxDscd);
	
	List<String> selectSameIONmInternal(@Param("msgDataVal") String msgDataVal, 
			@Param("msgLayoutId") String msgLayoutId) ;
	List<String> selectSameIONmInternalWrapper(@Param("msgDataVal") String msgDataVal, 
			@Param("msgLayoutId") String msgLayoutId) ;
	
	List<String> selectSameIONmExternal(@Param("dtoNm") String dtoNm, 
			@Param("msgLayoutId") String msgLayoutId) ;
}