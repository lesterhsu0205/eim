package eims.web.dto.table;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_ABSENT, content = Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class IntrfcMsgFieldEncodingDto {

	String intrfcId; // 인터페이스ID
	String msgLayoutId; // 메시지레이아웃ID
	String srCd ; //송수신구분
	String srSysCd ; //송수신시스템코드
	List<FieldEncodingDto> fieldEncodingDto; //필드인코딩상세정보

	public String getSrCd() {
		return srCd;
	}

	public void setSrCd(String srCd) {
		this.srCd = srCd;
	}

	public String getSrSysCd() {
		return srSysCd;
	}

	public void setSrSysCd(String srSysCd) {
		this.srSysCd = srSysCd;
	}

	public String getIntrfcId() {
		return intrfcId;
	}

	public void setIntrfcId(String intrfcId) {
		this.intrfcId = intrfcId;
	}

	public String getMsgLayoutId() {
		return msgLayoutId;
	}

	public void setMsgLayoutId(String msgLayoutId) {
		this.msgLayoutId = msgLayoutId;
	}

	public List<FieldEncodingDto> getFieldEncodingDto() {
		return fieldEncodingDto;
	}

	public void setFieldEncodingDto(List<FieldEncodingDto> fieldEncodingDto) {
		this.fieldEncodingDto = fieldEncodingDto;
	}

}