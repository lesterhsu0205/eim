package eims.web.dto.table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_ABSENT, content = Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MsglayoutdtDto {

	String msgLayoutId; // 메시지레이아웃ID
	int msgVersion; // 전문버전
	String fldEngNm; // 필드영문명
	String fldKorNm; // 필드한글명
	String dataTypeNm; // 데이터타입
	String arraySizeRefVal; // ARRAY사이즈참조값
	String basicVal; // 기본값
	String encYn; // 암호화여부
	String parentFldNm; // 상위필드명
	String ioKey; // IO키
	String childDtoNm; // 하위DTO명
	String fldUnqId; // 필드고유ID
	int msgSeq; // 메시지일련번호
	int msgLen; // 메시지길이
	int decimalLen; // DECIMAL길이
	String alignNm; // 정렬명
	String privacyDscd; // 개인정보구분코드
	String cdAttrNm; // 코드속성명
	String fillerVal; // FILLER값
	Integer fldLvNo; // 필드레벨번호
	String extrnlMsgBizCdYn; // 대외전문업무코드여부
	String extrnlSrchKeyYn; // 대외검색키여부
	String extrnlMsgNoYn; // 대외전문번호여부
	String extrnlProcessorCdYn; // 대외프로세서코드여부
	String extrnlOffsetYn; // 대외오프셋여부
	String iso8583LenFldCopyYn; // ISO8583길이필드복사여부
	Integer iso8583FldDataLen; // ISO8583필드데이터길이
	Integer iso8583FldMaxLen; // ISO8583필드최대길이
	String iso8583SpecNo; // ISO8583명세번호
	String iso8583FldCharSetCd; // ISO8583필드캐릭터셋코드
	String iso8583VariableYn; // ISO8583가변여부
	String iso8583FldLenTypeCd; // ISO8583필드길이타입코드
	String iso8583LenFldInclYn; // ISO8583길이필드포함여부
	Integer iso8583TrnsmsnDataLen; // ISO8583전송데이터길이
	Integer iso8583TrnsmsnLen; // ISO8583전송길이

	String fldRmk; // 비고
	String cfgDesc; // 설명
	String indsYn; // 필수여부
	Integer iso8583LenFldLen; // ISO8583필드길이
	String fldEncodingCd; // 필드인코딩코드
	String fldTrnsfrmCd; // 필드변환코드

	String maskingWayCd; // 마스킹방식코드 ***미사용***
	String encWayCd; // 암호화방식코드 ***미사용***
	String privacyYn; // 개인정보여부 ***미사용***
	String rsrvFldVal1; // 예비1 ***미사용***
	String rsrvFldVal2; // 예비2 ***미사용***
	String rsrvFldVal3; // 예비3 ***미사용***
	String metaValidVal; // 메타검증값 ***미사용***
	String paramType; // 파라미터 타입

	/** 
	 * 2018.10.11 추가
	 * */
	String replKey; //개인정보식별자
	/** 
	 * 2018.11.14 추가
	 * */
	String korInclYn ; //한글포함여부

	public String getKorInclYn() {
		return korInclYn;
	}

	public void setKorInclYn(String korInclYn) {
		this.korInclYn = korInclYn;
	}

	public String getReplKey() {
		return replKey;
	}

	public void setReplKey(String replKey) {
		this.replKey = replKey;
	}

	public Integer getIso8583LenFldLen() {
		return iso8583LenFldLen;
	}

	public void setIso8583LenFldLen(Integer iso8583LenFldLen) {
		this.iso8583LenFldLen = iso8583LenFldLen;
	}

	public String getFldEncodingCd() {
		return fldEncodingCd;
	}

	public void setFldEncodingCd(String fldEncodingCd) {
		this.fldEncodingCd = fldEncodingCd;
	}

	public String getFldTrnsfrmCd() {
		return fldTrnsfrmCd;
	}

	public void setFldTrnsfrmCd(String fldTrnsfrmCd) {
		this.fldTrnsfrmCd = fldTrnsfrmCd;
	}

	public String getCfgDesc() {
		return cfgDesc;
	}

	public void setCfgDesc(String cfgDesc) {
		this.cfgDesc = cfgDesc;
	}

	public String getIndsYn() {
		return indsYn;
	}

	public void setIndsYn(String indsYn) {
		this.indsYn = indsYn;
	}

	public String getFldRmk() {
		return fldRmk;
	}

	public void setFldRmk(String fldRmk) {
		this.fldRmk = fldRmk;
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

	public String getMetaValidVal() {
		return metaValidVal;
	}

	public void setMetaValidVal(String metaValidVal) {
		this.metaValidVal = metaValidVal;
	}

	public String getPrivacyDscd() {
		return privacyDscd;
	}

	public void setPrivacyDscd(String privacyDscd) {
		this.privacyDscd = privacyDscd;
	}

	public String getExtrnlOffsetYn() {
		return extrnlOffsetYn;
	}

	public void setExtrnlOffsetYn(String extrnlOffsetYn) {
		this.extrnlOffsetYn = extrnlOffsetYn;
	}

	public int getMsgVersion() {
		return msgVersion;
	}

	public void setMsgVersion(int msgVersion) {
		this.msgVersion = msgVersion;
	}

	public String getFldEngNm() {
		return fldEngNm;
	}

	public void setFldEngNm(String fldEngNm) {
		this.fldEngNm = fldEngNm;
	}

	public String getFldKorNm() {
		return fldKorNm;
	}

	public void setFldKorNm(String fldKorNm) {
		this.fldKorNm = fldKorNm;
	}

	public String getDataTypeNm() {
		return dataTypeNm;
	}

	public void setDataTypeNm(String dataTypeNm) {
		this.dataTypeNm = dataTypeNm;
	}

	public String getArraySizeRefVal() {
		return arraySizeRefVal;
	}

	public void setArraySizeRefVal(String arraySizeRefVal) {
		this.arraySizeRefVal = arraySizeRefVal;
	}

	public String getBasicVal() {
		return basicVal;
	}

	public void setBasicVal(String basicVal) {
		this.basicVal = basicVal;
	}

	public String getEncYn() {
		return encYn;
	}

	public void setEncYn(String encYn) {
		this.encYn = encYn;
	}

	public String getParentFldNm() {
		return parentFldNm;
	}

	public void setParentFldNm(String parentFldNm) {
		this.parentFldNm = parentFldNm;
	}

	public String getIoKey() {
		return ioKey;
	}

	public void setIoKey(String ioKey) {
		this.ioKey = ioKey;
	}

	public String getExtrnlMsgBizCdYn() {
		return extrnlMsgBizCdYn;
	}

	public void setExtrnlMsgBizCdYn(String extrnlMsgBizCdYn) {
		this.extrnlMsgBizCdYn = extrnlMsgBizCdYn;
	}

	public String getExtrnlSrchKeyYn() {
		return extrnlSrchKeyYn;
	}

	public void setExtrnlSrchKeyYn(String extrnlSrchKeyYn) {
		this.extrnlSrchKeyYn = extrnlSrchKeyYn;
	}

	public String getChildDtoNm() {
		return childDtoNm;
	}

	public void setChildDtoNm(String childDtoNm) {
		this.childDtoNm = childDtoNm;
	}

	public String getFldUnqId() {
		return fldUnqId;
	}

	public void setFldUnqId(String fldUnqId) {
		this.fldUnqId = fldUnqId;
	}

	public String getExtrnlMsgNoYn() {
		return extrnlMsgNoYn;
	}

	public void setExtrnlMsgNoYn(String extrnlMsgNoYn) {
		this.extrnlMsgNoYn = extrnlMsgNoYn;
	}

	public String getMsgLayoutId() {
		return msgLayoutId;
	}

	public void setMsgLayoutId(String msgLayoutId) {
		this.msgLayoutId = msgLayoutId;
	}

	public int getMsgSeq() {
		return msgSeq;
	}

	public void setMsgSeq(int msgSeq) {
		this.msgSeq = msgSeq;
	}

	public int getMsgLen() {
		return msgLen;
	}

	public void setMsgLen(int msgLen) {
		this.msgLen = msgLen;
	}

	public int getDecimalLen() {
		return decimalLen;
	}

	public void setDecimalLen(int decimalLen) {
		this.decimalLen = decimalLen;
	}

	public String getMaskingWayCd() {
		return maskingWayCd;
	}

	public void setMaskingWayCd(String maskingWayCd) {
		this.maskingWayCd = maskingWayCd;
	}

	public String getEncWayCd() {
		return encWayCd;
	}

	public void setEncWayCd(String encWayCd) {
		this.encWayCd = encWayCd;
	}

	public String getAlignNm() {
		return alignNm;
	}

	public void setAlignNm(String alignNm) {
		this.alignNm = alignNm;
	}

	public String getPrivacyYn() {
		return privacyYn;
	}

	public void setPrivacyYn(String privacyYn) {
		this.privacyYn = privacyYn;
	}

	public String getCdAttrNm() {
		return cdAttrNm;
	}

	public void setCdAttrNm(String cdAttrNm) {
		this.cdAttrNm = cdAttrNm;
	}

	public String getFillerVal() {
		return fillerVal;
	}

	public void setFillerVal(String fillerVal) {
		this.fillerVal = fillerVal;
	}

	public String getIso8583LenFldCopyYn() {
		return iso8583LenFldCopyYn;
	}

	public void setIso8583LenFldCopyYn(String iso8583LenFldCopyYn) {
		this.iso8583LenFldCopyYn = iso8583LenFldCopyYn;
	}

	public Integer getIso8583FldDataLen() {
		return iso8583FldDataLen;
	}

	public void setIso8583FldDataLen(Integer iso8583FldDataLen) {
		this.iso8583FldDataLen = iso8583FldDataLen;
	}

	public Integer getIso8583FldMaxLen() {
		return iso8583FldMaxLen;
	}

	public void setIso8583FldMaxLen(Integer iso8583FldMaxLen) {
		this.iso8583FldMaxLen = iso8583FldMaxLen;
	}

	public Integer getFldLvNo() {
		return fldLvNo;
	}

	public void setFldLvNo(Integer fldLvNo) {
		this.fldLvNo = fldLvNo;
	}

	public String getExtrnlProcessorCdYn() {
		return extrnlProcessorCdYn;
	}

	public void setExtrnlProcessorCdYn(String extrnlProcessorCdYn) {
		this.extrnlProcessorCdYn = extrnlProcessorCdYn;
	}

	public String getIso8583SpecNo() {
		return iso8583SpecNo;
	}

	public void setIso8583SpecNo(String iso8583SpecNo) {
		this.iso8583SpecNo = iso8583SpecNo;
	}

	public String getIso8583FldCharSetCd() {
		return iso8583FldCharSetCd;
	}

	public void setIso8583FldCharSetCd(String iso8583FldCharSetCd) {
		this.iso8583FldCharSetCd = iso8583FldCharSetCd;
	}

	public String getIso8583VariableYn() {
		return iso8583VariableYn;
	}

	public void setIso8583VariableYn(String iso8583VariableYn) {
		this.iso8583VariableYn = iso8583VariableYn;
	}

	public String getIso8583FldLenTypeCd() {
		return iso8583FldLenTypeCd;
	}

	public void setIso8583FldLenTypeCd(String iso8583FldLenTypeCd) {
		this.iso8583FldLenTypeCd = iso8583FldLenTypeCd;
	}

	public String getIso8583LenFldInclYn() {
		return iso8583LenFldInclYn;
	}

	public void setIso8583LenFldInclYn(String iso8583LenFldInclYn) {
		this.iso8583LenFldInclYn = iso8583LenFldInclYn;
	}

	public Integer getIso8583TrnsmsnDataLen() {
		return iso8583TrnsmsnDataLen;
	}

	public void setIso8583TrnsmsnDataLen(Integer iso8583TrnsmsnDataLen) {
		this.iso8583TrnsmsnDataLen = iso8583TrnsmsnDataLen;
	}

	public Integer getIso8583TrnsmsnLen() {
		return iso8583TrnsmsnLen;
	}

	public void setIso8583TrnsmsnLen(Integer iso8583TrnsmsnLen) {
		this.iso8583TrnsmsnLen = iso8583TrnsmsnLen;
	}

	public String getParamType() {
		return paramType;
	}

	public void setParamType(String paramType) {
		this.paramType = paramType;
	}
	
	

}