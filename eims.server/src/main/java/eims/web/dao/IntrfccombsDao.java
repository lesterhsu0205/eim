package eims.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import eims.web.dto.IntrfcInfoExportDto;
import eims.web.dto.table.IntrfccombsDto;
import eims.web.dto.table.IntrfccombsListDto;

@Mapper
public interface IntrfccombsDao {

	IntrfccombsDto selectIntrfccombs(@Param("intrfcId") String intrfcId);

//	String selectIntrfIdList(@Param("intrfcId") String intrfcId);

	int insertIntrfccombs(IntrfccombsDto intrfccombs);

	int checkIntrfcid(@Param("intrfcId") String intrfcId);
	int selectAllCnt(
			@Param("intrfcId") String intrfcId,
			@Param("intrfcNm") String intrfcNm,
			@Param("intrfcNmSub") String intrfcNmSub,
			@Param("intrfcWayCd") String intrfcWayCd,
			@Param("workStatusCd") String workStatusCd,
			@Param("regManId") String regManId,
			@Param("regDttm") String regDttm,
			@Param("msgTrnsfrmYn") String msgTrnsfrmYn,
			@Param("trxCd") String trxCd,
			@Param("bizCd") String bizCd,
			@Param("instCd") String instCd,
			@Param("trxDscd") String trxDscd,
			@Param("intrfcTypeCd") String intrfcTypeCd,
			@Param("lv1Cd") String lv1Cd,
			@Param("lv2Cd") String lv2Cd,
			@Param("lv3Cd") String lv3Cd,
			@Param("lv4Cd") String lv4Cd,
			@Param("lv5Cd") String lv5Cd,
			@Param("syncAsyncDscd") String syncAsyncDscd,
			@Param("srTypeCd") String srTypeCd,
			@Param("rqstExtrnlMsgNo") String rqstExtrnlMsgNo,
			@Param("rspsExtrnlMsgNo") String rspsExtrnlMsgNo,
			@Param("sysCdS") String sysCdS,
			@Param("sysCdR") String sysCdR,
			@Param("msgLayoutId") String msgLayoutId,
			@Param("trxTypeDscd") String trxTypeDscd,
			@Param("viewId") String viewId,
			@Param("execEnvDscd") String execEnvDscd);

	List<IntrfccombsListDto> selectAll(
			@Param("intrfcId") String intrfcId,
			@Param("intrfcNm") String intrfcNm,
			@Param("intrfcNmSub") String intrfcNmSub,
			@Param("intrfcWayCd") String intrfcWayCd,
			@Param("workStatusCd") String workStatusCd,
			@Param("regManId") String regManId,
			@Param("regDttm") String regDttm,
			@Param("msgTrnsfrmYn") String msgTrnsfrmYn,
			@Param("trxCd") String trxCd,
			@Param("bizCd") String bizCd,
			@Param("instCd") String instCd,
			@Param("trxDscd") String trxDscd,
			@Param("intrfcTypeCd") String intrfcTypeCd,
			@Param("lv1Cd") String lv1Cd,
			@Param("lv2Cd") String lv2Cd,
			@Param("lv3Cd") String lv3Cd,
			@Param("lv4Cd") String lv4Cd,
			@Param("lv5Cd") String lv5Cd,
			@Param("syncAsyncDscd") String syncAsyncDscd,
			@Param("srTypeCd") String srTypeCd,
			@Param("rqstExtrnlMsgNo") String rqstExtrnlMsgNo,
			@Param("rspsExtrnlMsgNo") String rspsExtrnlMsgNo,
			@Param("sysCdS") String sysCdS,
			@Param("sysCdR") String sysCdR,
			@Param("msgLayoutId") String msgLayoutId,
			@Param("trxTypeDscd") String trxTypeDscd,
			@Param("viewId") String viewId,
			@Param("execEnvDscd") String execEnvDscd,
			@Param("pageSize") int pageSize,
			@Param("pageNumber") int pageNumber);

	int selectFebAllCnt(
			@Param("intrfcId") String intrfcId,
			@Param("intrfcNm") String intrfcNm,
			@Param("intrfcNmSub") String intrfcNmSub,
			@Param("intrfcWayCd") String intrfcWayCd,
			@Param("workStatusCd") String workStatusCd,
			@Param("regManId") String regManId,
			@Param("regDttm") String regDttm,
			@Param("msgTrnsfrmYn") String msgTrnsfrmYn,
			@Param("trxCd") String trxCd,
			@Param("bizCd") String bizCd,
			@Param("instCd") String instCd,
			@Param("trxDscd") String trxDscd,
			@Param("intrfcTypeCd") String intrfcTypeCd,
			@Param("lv1Cd") String lv1Cd,
			@Param("lv2Cd") String lv2Cd,
			@Param("lv3Cd") String lv3Cd,
			@Param("lv4Cd") String lv4Cd,
			@Param("lv5Cd") String lv5Cd,
			@Param("syncAsyncDscd") String syncAsyncDscd,
			@Param("srTypeCd") String srTypeCd,
			@Param("rqstExtrnlMsgNo") String rqstExtrnlMsgNo,
			@Param("rspsExtrnlMsgNo") String rspsExtrnlMsgNo,
			@Param("sysCdS") String sysCdS,
			@Param("sysCdR") String sysCdR,
			@Param("msgLayoutId") String msgLayoutId,
			@Param("trxTypeDscd") String trxTypeDscd,
			@Param("viewId") String viewId);

	List<IntrfccombsListDto> selectFebAll(
			@Param("intrfcId") String intrfcId,
			@Param("intrfcNm") String intrfcNm,
			@Param("intrfcNmSub") String intrfcNmSub,
			@Param("intrfcWayCd") String intrfcWayCd,
			@Param("workStatusCd") String workStatusCd,
			@Param("regManId") String regManId,
			@Param("regDttm") String regDttm,
			@Param("msgTrnsfrmYn") String msgTrnsfrmYn,
			@Param("trxCd") String trxCd,
			@Param("bizCd") String bizCd,
			@Param("instCd") String instCd,
			@Param("trxDscd") String trxDscd,
			@Param("intrfcTypeCd") String intrfcTypeCd,
			@Param("lv1Cd") String lv1Cd,
			@Param("lv2Cd") String lv2Cd,
			@Param("lv3Cd") String lv3Cd,
			@Param("lv4Cd") String lv4Cd,
			@Param("lv5Cd") String lv5Cd,
			@Param("syncAsyncDscd") String syncAsyncDscd,
			@Param("srTypeCd") String srTypeCd,
			@Param("rqstExtrnlMsgNo") String rqstExtrnlMsgNo,
			@Param("rspsExtrnlMsgNo") String rspsExtrnlMsgNo,
			@Param("sysCdS") String sysCdS,
			@Param("sysCdR") String sysCdR,
			@Param("msgLayoutId") String msgLayoutId,
			@Param("trxTypeDscd") String trxTypeDscd,
			@Param("viewId") String viewId,
			@Param("pageSize") int pageSize,
			@Param("pageNumber") int pageNumber);
	
	int updateIntrfccombs(IntrfccombsDto intrfccombs);
	
	int updateIntrfccombsStatus(@Param("intrfcId") String intrfcId, @Param("workStatusCd") String workStatusCd);

	int deleteIntrfccombs(@Param("intrfcId") String intrfcId);

	Integer selectIdSeq(@Param("intrfcIdPre") String intrfcIdPre);	
	
	List<IntrfcInfoExportDto> selectAllIntrfcInfoToExport(@Param("intrfcId") String intrfcId,
							@Param("intrfcNm") String intrfcNm,
							@Param("intrfcWayCd") String intrfcWayCd,
							@Param("workStatusCd") String workStatusCd,
							@Param("regManId") String regManId,
							@Param("regDttm") String regDttm,
							@Param("msgTrnsfrmYn") String msgTrnsfrmYn,
							@Param("trxCd") String trxCd,
							@Param("bizCd") String bizCd,
							@Param("instCd") String instCd,
							@Param("trxDscd") String trxDscd,
							@Param("intrfcTypeCd") String intrfcTypeCd,
							@Param("lv1Cd") String lv1Cd,
							@Param("lv2Cd") String lv2Cd,
							@Param("lv3Cd") String lv3Cd,
							@Param("lv4Cd") String lv4Cd,
							@Param("lv5Cd") String lv5Cd,
							@Param("syncAsyncDscd") String syncAsyncDscd,
							@Param("srTypeCd") String srTypeCd,
							@Param("rqstExtrnlMsgNo") String rqstExtrnlMsgNo,
							@Param("rspsExtrnlMsgNo") String rspsExtrnlMsgNo,
							@Param("sysCdS") String sysCdS,
							@Param("sysCdR") String sysCdR,
							@Param("msgLayoutId") String msgLayoutId,
							@Param("trxTypeDscd") String trxTypeDscd);	
	
	List<String> selectDeployAllInterface(@Param("intrfType") String intrfType);
}