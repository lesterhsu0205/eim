package eims.web.dto.table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_ABSENT, content = Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BcMetaDto {

	String stdAreaNm;
	String dicPhyNm;
	String dicLogNm;
	String dataTypeNm;
	String dataTypeCd;
	Integer dataLen;
	Integer dataScale;
	String dicDesc;

	public String getStdAreaNm() {
		return stdAreaNm;
	}

	public void setStdAreaNm(String stdAreaNm) {
		this.stdAreaNm = stdAreaNm;
	}

	public String getDicPhyNm() {
		return dicPhyNm;
	}

	public void setDicPhyNm(String dicPhyNm) {
		this.dicPhyNm = dicPhyNm;
	}

	public String getDicLogNm() {
		return dicLogNm;
	}

	public void setDicLogNm(String dicLogNm) {
		this.dicLogNm = dicLogNm;
	}

	public String getDataTypeNm() {
		return dataTypeNm;
	}

	public void setDataTypeNm(String dataTypeNm) {
		this.dataTypeNm = dataTypeNm;
	}

	public String getDataTypeCd() {
		return dataTypeCd;
	}

	public void setDataTypeCd(String dataTypeCd) {
		this.dataTypeCd = dataTypeCd;
	}

	public Integer getDataLen() {
		return dataLen;
	}

	public void setDataLen(Integer dataLen) {
		this.dataLen = dataLen;
	}

	public Integer getDataScale() {
		return dataScale;
	}

	public void setDataScale(Integer dataScale) {
		this.dataScale = dataScale;
	}

	public String getDicDesc() {
		return dicDesc;
	}

	public void setDicDesc(String dicDesc) {
		this.dicDesc = dicDesc;
	}

}