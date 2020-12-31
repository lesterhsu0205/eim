package eims.web.dto.table;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_ABSENT, content = Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class IntrfccombsRawDataDto {

	List<IntrfccombsMappingDto> intrfccombsMappingReqDto;
	List<IntrfccombsMappingDto> intrfccombsMappingResDto;
	IntrfccombsDetailEAIDto eaiDto;
	IntrfccombsDetailMCIDto mciDto;
	IntrfccombsDetailFEPDto fepDto;
	IntrfccombsDetailCCDto ccDto ;
	List<IntrfcMsgFieldEncodingDto> intrfcMsgFieldEncodingDto;

	
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