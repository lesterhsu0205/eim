package eims.web.dto.table;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_ABSENT, content = Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class IntrfccombsDto {
	String intrfcId; // $col.colComment
	String intrfcNm; // $col.colComment
	String intrfcNmSub; // $col.colComment
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
	String protocolDscd;
	String httpMethod;
	String contextUrl;
	String requestMediaType;
	String responseMediaType;
	String execEnvDscd;
	List<IntrfcmsglayoutdtDto> intrfcmsglayoutdtDto;
	List<IntrfcsrsysdtDto> intrfcsrsysdtDto;
	List<IntrfcroutinfodtDto> intrfcroutinfodtDto;
	List<IntrfcdeployhisthsDto> intrfcdeployhisthsDto;
	List<IntrfcdeploysysdtDto> intrfcdeploysysdtDto;
	List<IntrfccombsMappingDto> intrfccombsMappingReqDto;
	List<IntrfccombsMappingDto> intrfccombsMappingResDto;
	IntrfccombsDetailEAIDto eaiDto = null;
	IntrfccombsDetailMCIDto mciDto = null;
	IntrfccombsDetailFEPDto fepDto = null;
	IntrfccombsDetailCCDto ccDto = null;
	String msgLayoutTranYn ;
	String compulsionDeployYn ;
	List<String> msgLayoutTranList ;
	List<IntrfcMsgFieldEncodingDto> intrfcMsgFieldEncodingDto; //필드인코딩설정
	

	
	public IntrfccombsDetailCCDto getCcDto() {
		return ccDto;
	}
	public void setCcDto(IntrfccombsDetailCCDto ccDto) {
		this.ccDto = ccDto;
	}
	public List<IntrfcMsgFieldEncodingDto> getIntrfcMsgFieldEncodingDto() {
		return intrfcMsgFieldEncodingDto;
	}
	public void setIntrfcMsgFieldEncodingDto(List<IntrfcMsgFieldEncodingDto> intrfcMsgFieldEncodingDto) {
		this.intrfcMsgFieldEncodingDto = intrfcMsgFieldEncodingDto;
	}
	public List<String> getMsgLayoutTranList() {
		return msgLayoutTranList;
	}
	public void setMsgLayoutTranList(List<String> msgLayoutTranList) {
		this.msgLayoutTranList = msgLayoutTranList;
	}
	public String getCompulsionDeployYn() {
		return compulsionDeployYn;
	}
	public void setCompulsionDeployYn(String compulsionDeployYn) {
		this.compulsionDeployYn = compulsionDeployYn;
	}
	public String getMsgLayoutTranYn() {
		return msgLayoutTranYn;
	}
	public void setMsgLayoutTranYn(String msgLayoutTranYn) {
		this.msgLayoutTranYn = msgLayoutTranYn;
	}
	@Override
	public String toString() {
		String str = "===== IntrfccombsDto =====" +				      
				"\nintrfcId : " + getIntrfcId() +
				"\nintrfcNm : " + getIntrfcNm() +
				"\nintrfcNmSub : " + getIntrfcNmSub() +
				"\nintrfcWayCd : " + getIntrfcWayCd() +
				"\nworkStatusCd : " + getWorkStatusCd() +
				"\nregManId : " + getRegManId() +
				"\nregDttm : " + getRegDttm() +
				"\nmsgTrnsfrmYn : " + getMsgTrnsfrmYn() +
				"\ntrxCd : " + getTrxCd() +
				"\nbizCd : " + getBizCd() +
				"\ninstCd : " + getInstCd() +
				"\ntrxDscd : " + getTrxDscd() +
				"\nintrfcTypeCd : " + getIntrfcTypeCd() +
				"\nlv1Cd : " + getLv1Cd() +
				"\nlv2Cd : " + getLv2Cd() +
				"\nlv3Cd : " + getLv3Cd() +
				"\nlv4Cd : " + getLv4Cd() +
				"\nlv5Cd : " + getLv5Cd() +
				"\nsyncAsyncDscd : " + getSyncAsyncDscd() +
				"\nsrTypeCd : " + getSrTypeCd() +
				"\nrqstExtrnlMsgNo : " + getRqstExtrnlMsgNo() +
				"\nrspsExtrnlMsgNo : " + getRspsExtrnlMsgNo() +				
				"\nrsrvFldVal1 : " + getRsrvFldVal1() +
				"\nrsrvFldVal2 : " + getRsrvFldVal2() +
				"\nrsrvFldVal3 : " + getRsrvFldVal3() +
				"\nrspsYn : " + getRspsYn() +
				"\ntrxTypeDscd : " + getTrxDscd() +
				"\nrawData : " + getRawData() +
				"\nbizCdNm : " + getBizCdNm() +
				"\ninstCdNm : " + getInstCdNm() +
				"\n==========================";
		return str;
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
	

	public String getIntrfcNmSub() {
		return intrfcNmSub;
	}
	
	public void setIntrfcNmSub(String intrfcNmSub) {
		this.intrfcNmSub = intrfcNmSub;
	}
	
	public String getTrxTypeDscd() {
		return trxTypeDscd;
	}

	public void setTrxTypeDscd(String trxTypeDscd) {
		this.trxTypeDscd = trxTypeDscd;
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

	public List<IntrfcroutinfodtDto> getIntrfcroutinfodtDto() {
		return intrfcroutinfodtDto;
	}

	public void setIntrfcroutinfodtDto(List<IntrfcroutinfodtDto> intrfcroutinfodtDto) {
		this.intrfcroutinfodtDto = intrfcroutinfodtDto;
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

	public String getRawData() {
		return rawData;
	}

	public void setRawData(String rawData) {
		this.rawData = rawData;
	}
	
	public String getViewId() {
		return viewId;
	}
	public void setViewId(String viewId) {
		this.viewId = viewId;
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
	public String getExecEnvDscd() {
		return execEnvDscd;
	}
	public void setExecEnvDscd(String execEnvDscd) {
		this.execEnvDscd = execEnvDscd;
	}
	public List<IntrfcmsglayoutdtDto> getIntrfcmsglayoutdtDto() {
		return intrfcmsglayoutdtDto;
	}

	public void setIntrfcmsglayoutdtDto(List<IntrfcmsglayoutdtDto> intrfcmsglayoutdtDto) {
		this.intrfcmsglayoutdtDto = intrfcmsglayoutdtDto;
	}

	public List<IntrfcsrsysdtDto> getIntrfcsrsysdtDto() {
		return intrfcsrsysdtDto;
	}

	public void setIntrfcsrsysdtDto(List<IntrfcsrsysdtDto> intrfcsrsysdtDto) {
		this.intrfcsrsysdtDto = intrfcsrsysdtDto;
	}

	public List<IntrfcdeployhisthsDto> getIntrfcdeployhisthsDto() {
		return intrfcdeployhisthsDto;
	}

	public void setIntrfcdeployhisthsDto(List<IntrfcdeployhisthsDto> intrfcdeployhisthsDto) {
		this.intrfcdeployhisthsDto = intrfcdeployhisthsDto;
	}

	public List<IntrfcdeploysysdtDto> getIntrfcdeploysysdtDto() {
		return intrfcdeploysysdtDto;
	}

	public void setIntrfcdeploysysdtDto(List<IntrfcdeploysysdtDto> intrfcdeploysysdtDto) {
		this.intrfcdeploysysdtDto = intrfcdeploysysdtDto;
	}

	public List<IntrfccombsMappingDto> getIntrfccombsMappingReqDto() {
		return intrfccombsMappingReqDto;
	}

	public void setIntrfccombsMappingReqDto(List<IntrfccombsMappingDto> intrfccombsMappingReqDto) {
		this.intrfccombsMappingReqDto = intrfccombsMappingReqDto;
	}

	public List<IntrfccombsMappingDto> getIntrfccombsMappingResDto() {
		return intrfccombsMappingResDto;
	}

	public void setIntrfccombsMappingResDto(List<IntrfccombsMappingDto> intrfccombsMappingResDto) {
		this.intrfccombsMappingResDto = intrfccombsMappingResDto;
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

}