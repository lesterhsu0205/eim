package eims.web.dto.table;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_ABSENT, content = Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MsglayoutbsDto {

	String msgLayoutId; // 전문레이아웃ID
	String msgNm; // 전문명
	String msgNmSub; // 전문명
	int msgVersion; // 전문버전
	String chlDscd; // 채널구분코드
	String trxDscd; //거래구분코드
	String msgDscd; // 전문구분코드
	String regManId; // 등록자ID
	String regDttm; // 등록일시
	String msgDesc; // 전문설명
	String dtoNm; // DTO명
	String lv1Cd; // 레벨1코드
	String lv2Cd; // 레벨2코드
	String lv3Cd; // 레벨3코드
	String lv4Cd; // 레벨4코드
	String lv5Cd; // 레벨5코드
	String msgDataVal; // 전문번호/DTO명
	String jobId; // 파일ID
	String extrnlBizNm; // 대외업무명
	String bitMapCrtnYn; // 비트맵생성여부
	String iso8583DataTypeCd; // ISO8583데이터타입코드
	String bitMapTypeCd; // 비트맵타입코드
	String rsrvFldVal1; // 송신시스템코드  - 대내, 온라인, 개별부인 경우만 존재
	String rsrvFldVal2; // 수신시스템코드 - 대내, 온라인, 개별부인 경우만 존재
	String appTypeCd; // 어플리케이션타입코드 ***미사용***
	String trxCd; // 거래코드 ***미사용***
	String appNm; // 어플리케이션명 ***미사용***
	Integer crtnSeq; // 시퀀스 - 전문아이디 채번을 위함 ***EIMS에만사용***
	String rsrvFldVal3; // 전문 총 길이 .
		

	
	public String getMsgNmSub() {
		return msgNmSub;
	}

	public void setMsgNmSub(String msgNmSub) {
		this.msgNmSub = msgNmSub;
	}

	/**
	 * 2018.10.16 추가
	 * */
	String sharedYn; //shared여부 - F/W 사용
	/**
	 * 2018.11.13 추가
	 * */
	String workStatusCd; // 작업상태코드
	/**
	 * 2019.01.09 추가
	 * */
	String custApiYn; //고객사API여부
	Integer msgRvsNo ; //메시지개정번호Revision

	List<MsglayoutdtDto> msglayoutdtDto; // 전문레이아웃 필드 상세 정보

	public Integer getMsgRvsNo() {
		return msgRvsNo;
	}

	public void setMsgRvsNo(Integer msgRvsNo) {
		this.msgRvsNo = msgRvsNo;
	}

	public String getCustApiYn() {
		return custApiYn;
	}

	public void setCustApiYn(String custApiYn) {
		this.custApiYn = custApiYn;
	}

	public String getWorkStatusCd() {
		return workStatusCd;
	}

	public void setWorkStatusCd(String workStatusCd) {
		this.workStatusCd = workStatusCd;
	}

	public String getSharedYn() {
		return sharedYn;
	}

	public void setSharedYn(String sharedYn) {
		this.sharedYn = sharedYn;
	}

	public String getBitMapTypeCd() {
		return bitMapTypeCd;
	}

	public void setBitMapTypeCd(String bitMapTypeCd) {
		this.bitMapTypeCd = bitMapTypeCd;
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

	public String getIso8583DataTypeCd() {
		return iso8583DataTypeCd;
	}

	public void setIso8583DataTypeCd(String iso8583DataTypeCd) {
		this.iso8583DataTypeCd = iso8583DataTypeCd;
	}

	public String getChlDscd() {
		return chlDscd;
	}

	public void setChlDscd(String chlDscd) {
		this.chlDscd = chlDscd;
	}

	public String getBitMapCrtnYn() {
		return bitMapCrtnYn;
	}

	public void setBitMapCrtnYn(String bitMapCrtnYn) {
		this.bitMapCrtnYn = bitMapCrtnYn;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getTrxDscd() {
		return trxDscd;
	}

	public void setTrxDscd(String trxDscd) {
		this.trxDscd = trxDscd;
	}

	public String getMsgNm() {
		return msgNm;
	}

	public void setMsgNm(String msgNm) {
		this.msgNm = msgNm;
	}

	public int getMsgVersion() {
		return msgVersion;
	}

	public void setMsgVersion(int msgVersion) {
		this.msgVersion = msgVersion;
	}

	public String getMsgDscd() {
		return msgDscd;
	}

	public void setMsgDscd(String msgDscd) {
		this.msgDscd = msgDscd;
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

	public String getMsgDataVal() {
		return msgDataVal;
	}

	public void setMsgDataVal(String msgDataVal) {
		this.msgDataVal = msgDataVal;
	}

	public String getMsgDesc() {
		return msgDesc;
	}

	public void setMsgDesc(String msgDesc) {
		this.msgDesc = msgDesc;
	}

	public String getExtrnlBizNm() {
		return extrnlBizNm;
	}

	public void setExtrnlBizNm(String extrnlBizNm) {
		this.extrnlBizNm = extrnlBizNm;
	}

	public String getDtoNm() {
		return dtoNm;
	}

	public void setDtoNm(String dtoNm) {
		this.dtoNm = dtoNm;
	}

	public String getMsgLayoutId() {
		return msgLayoutId;
	}

	public void setMsgLayoutId(String msgLayoutId) {
		this.msgLayoutId = msgLayoutId;
	}

	public String getAppTypeCd() {
		return appTypeCd;
	}

	public void setAppTypeCd(String appTypeCd) {
		this.appTypeCd = appTypeCd;
	}

	public String getTrxCd() {
		return trxCd;
	}

	public void setTrxCd(String trxCd) {
		this.trxCd = trxCd;
	}

	public String getAppNm() {
		return appNm;
	}

	public void setAppNm(String appNm) {
		this.appNm = appNm;
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

	public List<MsglayoutdtDto> getMsglayoutdtDto() {
		return msglayoutdtDto;
	}

	public void setMsglayoutdtDto(List<MsglayoutdtDto> msglayoutdtDto) {
		this.msglayoutdtDto = msglayoutdtDto;
	}

	@Override
	public String toString() {
		return "MsglayoutbsDto [msgLayoutId=" + msgLayoutId + ", msgNm=" + msgNm + ", msgVersion=" + msgVersion
				+ ", chlDscd=" + chlDscd + ", trxDscd=" + trxDscd + ", msgDscd=" + msgDscd + ", regManId=" + regManId
				+ ", regDttm=" + regDttm + ", msgDesc=" + msgDesc + ", dtoNm=" + dtoNm + ", lv1Cd=" + lv1Cd + ", lv2Cd="
				+ lv2Cd + ", lv3Cd=" + lv3Cd + ", lv4Cd=" + lv4Cd + ", lv5Cd=" + lv5Cd + ", msgDataVal=" + msgDataVal
				+ ", jobId=" + jobId + ", extrnlBizNm=" + extrnlBizNm + ", bitMapCrtnYn=" + bitMapCrtnYn
				+ ", iso8583DataTypeCd=" + iso8583DataTypeCd + ", bitMapTypeCd=" + bitMapTypeCd + ", rsrvFldVal1="
				+ rsrvFldVal1 + ", rsrvFldVal2=" + rsrvFldVal2 + ", appTypeCd=" + appTypeCd + ", trxCd=" + trxCd
				+ ", appNm=" + appNm + ", crtnSeq=" + crtnSeq + ", rsrvFldVal3=" + rsrvFldVal3 + ", msglayoutdtDto="
				+ msglayoutdtDto + "]";
	}

}