package eims.web.dto.table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_ABSENT, content = Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SrsysbsDto {

	String sysCd; // $col.colComment
	String sysNm; // $col.colComment
	String sysCdDesc; // 시스템코드설명
	String crgManNm; // $col.colComment
	String noncoreYn;
	
	
	public String getNoncoreYn() {
		return noncoreYn;
	}

	public void setNoncoreYn(String noncoreYn) {
		this.noncoreYn = noncoreYn;
	}

	public String getCrgManNm() {
		return crgManNm;
	}

	public void setCrgManNm(String crgManNm) {
		this.crgManNm = crgManNm;
	}

	public String getSysCd() {
		return this.sysCd;
	}

	public void setSysCd(String sysCd) {
		this.sysCd = sysCd;
	}

	public String getSysNm() {
		return this.sysNm;
	}

	public void setSysNm(String sysNm) {
		this.sysNm = sysNm;
	}

	public String getSysCdDesc() {
		return this.sysCdDesc;
	}

	public void setSysCdDesc(String sysCdDesc) {
		this.sysCdDesc = sysCdDesc;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SrsysbsDto [");
		builder.append("sysCd=");
		builder.append(sysCd);
		builder.append(", sysNm=");
		builder.append(sysNm);
		builder.append(", sysCdDesc=");
		builder.append(sysCdDesc);
		builder.append("]");
		return builder.toString();
	}

}