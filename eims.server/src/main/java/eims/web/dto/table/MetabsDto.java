package eims.web.dto.table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_ABSENT, content = Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MetabsDto {

	String metaEngNm; // $col.colComment
	String metaKorNm; // $col.colComment
	int metaLen; // $col.colComment
	int decimalLen; // $col.colComment
	String dataTypeNm; // $col.colComment
	String metaEncYn;
	String metaTermType;
	
	
	public String getMetaTermType() {
		return metaTermType;
	}

	public void setMetaTermType(String metaTermType) {
		this.metaTermType = metaTermType;
	}

	public int getMetaLen() {
		return metaLen;
	}

	public void setMetaLen(int metaLen) {
		this.metaLen = metaLen;
	}

	public int getDecimalLen() {
		return decimalLen;
	}

	public void setDecimalLen(int decimalLen) {
		this.decimalLen = decimalLen;
	}

	public String getMetaEngNm() {
		return this.metaEngNm; 
	}

	public void setMetaEngNm(String metaEngNm) {
		this.metaEngNm = metaEngNm;
	}

	public String getMetaKorNm() {
		return this.metaKorNm;
	}

	public void setMetaKorNm(String metaKorNm) {
		this.metaKorNm = metaKorNm;
	}

	public String getDataTypeNm() {
		return this.dataTypeNm;
	}

	public void setDataTypeNm(String dataTypeNm) {
		this.dataTypeNm = dataTypeNm;
	}

	public String getMetaEncYn() {
		return metaEncYn;
	}

	public void setMetaEncYn(String metaEncYn) {
		this.metaEncYn = metaEncYn;
	}

	
}