package eims.web.dto.table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_ABSENT, content = Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class IntrfccombsMappingDto {

	String mappingTypeCd; //매핑타입코드
	String reqResTypeCd; //요청응답타입코드
	int mappingSeq; //매핑순번
	String srcData; //소스필드데이터
	String targetData; //타겟필드데이터
	int targetDataSeq; //타겟데이터순번

	/**
	 * 20181113 추가
	 */
	String fldEncoding;//필드인코딩
	String wideHalfCharCngCd; //전박반각변환코드
	String encCd; //암호화코드

	public String getFldEncoding() {
		return fldEncoding;
	}

	public void setFldEncoding(String fldEncoding) {
		this.fldEncoding = fldEncoding;
	}

	public String getWideHalfCharCngCd() {
		return wideHalfCharCngCd;
	}

	public void setWideHalfCharCngCd(String wideHalfCharCngCd) {
		this.wideHalfCharCngCd = wideHalfCharCngCd;
	}

	public String getEncCd() {
		return encCd;
	}

	public void setEncCd(String encCd) {
		this.encCd = encCd;
	}

	public String getReqResTypeCd() {
		return reqResTypeCd;
	}

	public void setReqResTypeCd(String reqResTypeCd) {
		this.reqResTypeCd = reqResTypeCd;
	}

	public String getSrcData() {
		return srcData;
	}

	public void setSrcData(String srcData) {
		this.srcData = srcData;
	}

	public String getMappingTypeCd() {
		return mappingTypeCd;
	}

	public void setMappingTypeCd(String mappingTypeCd) {
		this.mappingTypeCd = mappingTypeCd;
	}

	public int getMappingSeq() {
		return mappingSeq;
	}

	public void setMappingSeq(int mappingSeq) {
		this.mappingSeq = mappingSeq;
	}

	public String getTargetData() {
		return targetData;
	}

	public void setTargetData(String targetData) {
		this.targetData = targetData;
	}

	public int getTargetDataSeq() {
		return targetDataSeq;
	}

	public void setTargetDataSeq(int targetDataSeq) {
		this.targetDataSeq = targetDataSeq;
	}

}