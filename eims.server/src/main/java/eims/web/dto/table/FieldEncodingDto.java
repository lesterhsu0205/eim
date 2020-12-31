package eims.web.dto.table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_ABSENT, content = Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FieldEncodingDto {
	
	Integer seq ; //필드 시퀀스
	String fldEngNm ; //필드영문명
	String fldUnqId ; //필드유니크아이디
	String fldEncoding ;//필드인코딩
	String wideHalfCharCngCd ; //전박반각변환코드
	
	public Integer getSeq() {
		return seq;
	}
	public void setSeq(Integer seq) {
		this.seq = seq;
	}
	public String getFldEngNm() {
		return fldEngNm;
	}
	public void setFldEngNm(String fldEngNm) {
		this.fldEngNm = fldEngNm;
	}
	public String getFldUnqId() {
		return fldUnqId;
	}
	public void setFldUnqId(String fldUnqId) {
		this.fldUnqId = fldUnqId;
	}
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
	
	
}