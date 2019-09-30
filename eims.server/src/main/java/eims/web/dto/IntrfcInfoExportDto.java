/**
 * 
 */
package eims.web.dto;

import eims.web.dto.table.IntrfccombsDetailCCDto;
import eims.web.dto.table.IntrfccombsDetailEAIDto;
import eims.web.dto.table.IntrfccombsDetailFEPDto;
import eims.web.dto.table.IntrfccombsDetailMCIDto;

/**
 * @author 20180032
 *
 */
public class IntrfcInfoExportDto {
	String intrfcId; // $col.colComment
	String intrfcNm; // $col.colComment
	String intrfcWayCd; // 인터페이스방식코드
	String workStatusCd; // $col.colComment
	String regManId; // 등록자ID
	String regDttm; // $col.colComment
	String msgTrnsfrmYn; // $col.colComment
	String trxCd; // $col.colComment
	String bizCd; // $col.colComment
	String instCd; // $col.colComment
	String trxDscd; // 거래타입코드
	String intrfcTypeCd; // 인터페이스타입코드
	String lv1Cd; // 레벨1코드
	String lv2Cd; // 레벨2코드
	String lv3Cd; // 레벨3코드
	String lv4Cd; // 레벨4코드
	String lv5Cd; // 레벨5코드
	String syncAsyncDscd; // $col.colComment
	String srTypeCd; // $col.colComment
	String rqstExtrnlMsgNo; // $col.colComment
	String rspsExtrnlMsgNo; // $col.colComment
	Integer crtnSeq; // $col.colComment
	String rsrvFldVal1; // $col.colComment
	String rsrvFldVal2; // $col.colComment
	String rsrvFldVal3; // $col.colComment
	String rspsYn; // $col.colComment
	String trxTypeDscd; // $col.colComment
	String rawData; //intrfccombsMappingDto, eaiDto, mciDto, fepDto의 값이 들어가야함
	String bizCdNm; // $col.colComment
	String instCdNm; // $col.colComment
	String viewId;
	String regMamNm; // 등록자name
	String appCdNm;	
	
	
	String srTypeCdSend; // 송신타입코드	
	String sysCdSend; // 송신시스템코드
	String sysNmSend; // 송신시스템명
	String trxCdSend; // 거래코드
	String filePathSend; // 파일경로
	String crgManNmSend; // $col.colComment
	
	String srTypeCdReceive; // 수신타입코드	
	String sysCdReceive; // 수신시스템코드
	String sysNmReceive; // 수신시스템명
	String trxCdReceive; // 거래코드
	String filePathReceive; // 파일경로
	String crgManNmReceive; // $col.colComment
	
	int seq1; // $col.colComment
	int lenFldOffsetLen1; // $col.colComment
	int fldDataLen1; // $col.colComment
	String fldCfgVal1; // $col.colComment
	String rutngDesc1; // $col.colComment
	String cndtCd1; // $col.colComment
	
	int seq2; // $col.colComment
	int lenFldOffsetLen2; // $col.colComment
	int fldDataLen2; // $col.colComment
	String fldCfgVal2; // $col.colComment
	String rutngDesc2; // $col.colComment
	String cndtCd2; // $col.colComment
	
	
	String protocolDscd;
	String httpMethod;
	String contextUrl;
	String requestMediaType;
	String responseMediaType;

	String execEnvDscd;
	
	IntrfccombsDetailEAIDto eaiDto = null;
	IntrfccombsDetailMCIDto mciDto = null;
	IntrfccombsDetailFEPDto fepDto = null;
	IntrfccombsDetailCCDto ccDto = null;

	
	@Override
	public String toString() {
		String str = "===== IntrfcInfoExportDto =====" +				      
				
				"\n==========================";
		return str;
	}

	
	
	
	public String getProtocolDscd() {
		return protocolDscd;
	}




	public void setProtocolDscd(String protocolDscd) {
		this.protocolDscd = protocolDscd;
	}




	public String getHttpMethod() {
		return httpMethod;
	}




	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}




	public String getContextUrl() {
		return contextUrl;
	}




	public void setContextUrl(String contextUrl) {
		this.contextUrl = contextUrl;
	}




	public String getRequestMediaType() {
		return requestMediaType;
	}




	public void setRequestMediaType(String requestMediaType) {
		this.requestMediaType = requestMediaType;
	}




	public String getResponseMediaType() {
		return responseMediaType;
	}




	public void setResponseMediaType(String responseMediaType) {
		this.responseMediaType = responseMediaType;
	}




	public String getRegMamNm() {
		return regMamNm;
	}



	public void setRegMamNm(String regMamNm) {
		this.regMamNm = regMamNm;
	}



	public String getAppCdNm() {
		return appCdNm;
	}



	public void setAppCdNm(String appCdNm) {
		this.appCdNm = appCdNm;
	}



	public IntrfccombsDetailCCDto getCcDto() {
		return ccDto;
	}

	public void setCcDto(IntrfccombsDetailCCDto ccDto) {
		this.ccDto = ccDto;
	}

	public String getIntrfcId() {
		return intrfcId;
	}

	public void setIntrfcId(String intrfcId) {
		this.intrfcId = intrfcId;
	}

	public String getIntrfcNm() {
		return intrfcNm;
	}

	public void setIntrfcNm(String intrfcNm) {
		this.intrfcNm = intrfcNm;
	}

	public String getIntrfcWayCd() {
		return intrfcWayCd;
	}

	public void setIntrfcWayCd(String intrfcWayCd) {
		this.intrfcWayCd = intrfcWayCd;
	}

	public String getWorkStatusCd() {
		return workStatusCd;
	}

	public void setWorkStatusCd(String workStatusCd) {
		this.workStatusCd = workStatusCd;
	}

	public String getRegManId() {
		return regManId;
	}

	public void setRegManId(String regManId) {
		this.regManId = regManId;
	}

	public String getRegDttm() {
		return regDttm;
	}

	public void setRegDttm(String regDttm) {
		this.regDttm = regDttm;
	}

	public String getMsgTrnsfrmYn() {
		return msgTrnsfrmYn;
	}

	public void setMsgTrnsfrmYn(String msgTrnsfrmYn) {
		this.msgTrnsfrmYn = msgTrnsfrmYn;
	}

	public String getTrxCd() {
		return trxCd;
	}

	public void setTrxCd(String trxCd) {
		this.trxCd = trxCd;
	}

	public String getBizCd() {
		return bizCd;
	}

	public void setBizCd(String bizCd) {
		this.bizCd = bizCd;
	}

	public String getInstCd() {
		return instCd;
	}

	public void setInstCd(String instCd) {
		this.instCd = instCd;
	}

	public String getTrxDscd() {
		return trxDscd;
	}

	public void setTrxDscd(String trxDscd) {
		this.trxDscd = trxDscd;
	}

	public String getIntrfcTypeCd() {
		return intrfcTypeCd;
	}

	public void setIntrfcTypeCd(String intrfcTypeCd) {
		this.intrfcTypeCd = intrfcTypeCd;
	}

	public String getLv1Cd() {
		return lv1Cd;
	}

	public void setLv1Cd(String lv1Cd) {
		this.lv1Cd = lv1Cd;
	}

	public String getLv2Cd() {
		return lv2Cd;
	}

	public void setLv2Cd(String lv2Cd) {
		this.lv2Cd = lv2Cd;
	}

	public String getLv3Cd() {
		return lv3Cd;
	}

	public void setLv3Cd(String lv3Cd) {
		this.lv3Cd = lv3Cd;
	}

	public String getLv4Cd() {
		return lv4Cd;
	}

	public void setLv4Cd(String lv4Cd) {
		this.lv4Cd = lv4Cd;
	}

	public String getLv5Cd() {
		return lv5Cd;
	}

	public void setLv5Cd(String lv5Cd) {
		this.lv5Cd = lv5Cd;
	}

	public String getSyncAsyncDscd() {
		return syncAsyncDscd;
	}

	public void setSyncAsyncDscd(String syncAsyncDscd) {
		this.syncAsyncDscd = syncAsyncDscd;
	}

	public String getSrTypeCd() {
		return srTypeCd;
	}

	public void setSrTypeCd(String srTypeCd) {
		this.srTypeCd = srTypeCd;
	}

	public String getRqstExtrnlMsgNo() {
		return rqstExtrnlMsgNo;
	}

	public void setRqstExtrnlMsgNo(String rqstExtrnlMsgNo) {
		this.rqstExtrnlMsgNo = rqstExtrnlMsgNo;
	}

	public String getRspsExtrnlMsgNo() {
		return rspsExtrnlMsgNo;
	}

	public void setRspsExtrnlMsgNo(String rspsExtrnlMsgNo) {
		this.rspsExtrnlMsgNo = rspsExtrnlMsgNo;
	}

	public Integer getCrtnSeq() {
		return crtnSeq;
	}

	public void setCrtnSeq(Integer crtnSeq) {
		this.crtnSeq = crtnSeq;
	}

	public String getRsrvFldVal1() {
		return rsrvFldVal1;
	}

	public void setRsrvFldVal1(String rsrvFldVal1) {
		this.rsrvFldVal1 = rsrvFldVal1;
	}

	public String getRsrvFldVal2() {
		return rsrvFldVal2;
	}

	public void setRsrvFldVal2(String rsrvFldVal2) {
		this.rsrvFldVal2 = rsrvFldVal2;
	}

	public String getRsrvFldVal3() {
		return rsrvFldVal3;
	}

	public void setRsrvFldVal3(String rsrvFldVal3) {
		this.rsrvFldVal3 = rsrvFldVal3;
	}

	public String getRspsYn() {
		return rspsYn;
	}

	public void setRspsYn(String rspsYn) {
		this.rspsYn = rspsYn;
	}

	public String getTrxTypeDscd() {
		return trxTypeDscd;
	}

	public void setTrxTypeDscd(String trxTypeDscd) {
		this.trxTypeDscd = trxTypeDscd;
	}

	public String getRawData() {
		return rawData;
	}

	public void setRawData(String rawData) {
		this.rawData = rawData;
	}

	public String getBizCdNm() {
		return bizCdNm;
	}

	public void setBizCdNm(String bizCdNm) {
		this.bizCdNm = bizCdNm;
	}

	public String getInstCdNm() {
		return instCdNm;
	}

	public void setInstCdNm(String instCdNm) {
		this.instCdNm = instCdNm;
	}

	public String getSrTypeCdSend() {
		return srTypeCdSend;
	}

	public void setSrTypeCdSend(String srTypeCdSend) {
		this.srTypeCdSend = srTypeCdSend;
	}

	public String getSysCdSend() {
		return sysCdSend;
	}

	public void setSysCdSend(String sysCdSend) {
		this.sysCdSend = sysCdSend;
	}

	public String getSysNmSend() {
		return sysNmSend;
	}

	public void setSysNmSend(String sysNmSend) {
		this.sysNmSend = sysNmSend;
	}

	public String getTrxCdSend() {
		return trxCdSend;
	}

	public void setTrxCdSend(String trxCdSend) {
		this.trxCdSend = trxCdSend;
	}

	public String getFilePathSend() {
		return filePathSend;
	}

	public void setFilePathSend(String filePathSend) {
		this.filePathSend = filePathSend;
	}

	public String getCrgManNmSend() {
		return crgManNmSend;
	}

	public void setCrgManNmSend(String crgManNmSend) {
		this.crgManNmSend = crgManNmSend;
	}

	public String getSrTypeCdReceive() {
		return srTypeCdReceive;
	}

	public void setSrTypeCdReceive(String srTypeCdReceive) {
		this.srTypeCdReceive = srTypeCdReceive;
	}

	public String getSysCdReceive() {
		return sysCdReceive;
	}

	public void setSysCdReceive(String sysCdReceive) {
		this.sysCdReceive = sysCdReceive;
	}

	public String getSysNmReceive() {
		return sysNmReceive;
	}

	public void setSysNmReceive(String sysNmReceive) {
		this.sysNmReceive = sysNmReceive;
	}

	public String getTrxCdReceive() {
		return trxCdReceive;
	}

	public void setTrxCdReceive(String trxCdReceive) {
		this.trxCdReceive = trxCdReceive;
	}

	public String getFilePathReceive() {
		return filePathReceive;
	}

	public void setFilePathReceive(String filePathReceive) {
		this.filePathReceive = filePathReceive;
	}

	public String getCrgManNmReceive() {
		return crgManNmReceive;
	}

	public void setCrgManNmReceive(String crgManNmReceive) {
		this.crgManNmReceive = crgManNmReceive;
	}

	public int getSeq1() {
		return seq1;
	}

	public void setSeq1(int seq1) {
		this.seq1 = seq1;
	}

	public int getLenFldOffsetLen1() {
		return lenFldOffsetLen1;
	}

	public void setLenFldOffsetLen1(int lenFldOffsetLen1) {
		this.lenFldOffsetLen1 = lenFldOffsetLen1;
	}

	public int getFldDataLen1() {
		return fldDataLen1;
	}

	public void setFldDataLen1(int fldDataLen1) {
		this.fldDataLen1 = fldDataLen1;
	}

	public String getFldCfgVal1() {
		return fldCfgVal1;
	}

	public void setFldCfgVal1(String fldCfgVal1) {
		this.fldCfgVal1 = fldCfgVal1;
	}

	public String getRutngDesc1() {
		return rutngDesc1;
	}

	public void setRutngDesc1(String rutngDesc1) {
		this.rutngDesc1 = rutngDesc1;
	}

	public String getCndtCd1() {
		return cndtCd1;
	}

	public void setCndtCd1(String cndtCd1) {
		this.cndtCd1 = cndtCd1;
	}

	public int getSeq2() {
		return seq2;
	}

	public void setSeq2(int seq2) {
		this.seq2 = seq2;
	}

	public int getLenFldOffsetLen2() {
		return lenFldOffsetLen2;
	}

	public void setLenFldOffsetLen2(int lenFldOffsetLen2) {
		this.lenFldOffsetLen2 = lenFldOffsetLen2;
	}

	public int getFldDataLen2() {
		return fldDataLen2;
	}

	public void setFldDataLen2(int fldDataLen2) {
		this.fldDataLen2 = fldDataLen2;
	}

	public String getFldCfgVal2() {
		return fldCfgVal2;
	}

	public void setFldCfgVal2(String fldCfgVal2) {
		this.fldCfgVal2 = fldCfgVal2;
	}

	public String getRutngDesc2() {
		return rutngDesc2;
	}

	public void setRutngDesc2(String rutngDesc2) {
		this.rutngDesc2 = rutngDesc2;
	}

	public String getCndtCd2() {
		return cndtCd2;
	}

	public void setCndtCd2(String cndtCd2) {
		this.cndtCd2 = cndtCd2;
	}

	public IntrfccombsDetailEAIDto getEaiDto() {
		return eaiDto;
	}

	public void setEaiDto(IntrfccombsDetailEAIDto eaiDto) {
		this.eaiDto = eaiDto;
	}

	public IntrfccombsDetailMCIDto getMciDto() {
		return mciDto;
	}

	public void setMciDto(IntrfccombsDetailMCIDto mciDto) {
		this.mciDto = mciDto;
	}

	public IntrfccombsDetailFEPDto getFepDto() {
		return fepDto;
	}

	public void setFepDto(IntrfccombsDetailFEPDto fepDto) {
		this.fepDto = fepDto;
	}


	public String getViewId() {
		return viewId;
	}


	public void setViewId(String viewId) {
		this.viewId = viewId;
	}

	public String getExecEnvDscd() {
		return execEnvDscd;
	}

	public void setExecEnvDscd(String execEnvDscd) {
		this.execEnvDscd = execEnvDscd;
	}
	
	
	
}
