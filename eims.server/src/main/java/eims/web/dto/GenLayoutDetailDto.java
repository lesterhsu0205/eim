package eims.web.dto;

import java.util.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GenLayoutDetailDto {

	String sysCd; // SYSTEM CODE
	String voClassId; // VO CLASS ID
	@NotNull
	@Max(999)
	int layoutDatailSeq; // LAYOUT SEQUENCE
	@Size(max = 100)
	String elementNm; // ELEMENT NAME
	@NotNull
	@Size(max = 30)
	String fieldNm; // FIELD NAME
	@NotNull
	@Size(max = 100)
	String fieldDesc; // FIELD DESCRIPTION
	@NotNull
	@Size(max = 128)
	String fieldDataTypeCd; // FIELD DATA TYPE CODE
	@NotNull
	@Max(9999)
	int fieldLengthVal; // FIELD LENGTH
	@Max(9999)
	int decimalLengthVal; // DECIMAL LENGTH
	@Size(max = 30)
	String formatCd; // FORMAT CODE
	@Size(max = 30)
	String arrayRefFieldId; // ARRAY REFERENCE FIELD NAME
	@NotNull
	@Size(min = 1, max = 1)
	String encryptDecryptDivisionCd; // ENCRYPT DECRYPT DEVISION CODE
	@Size(max = 50)
	String defaultVal; // DEFAULT VALUE
	@Size(max = 10)
	String alignCd; // ALIGN CODE
	String createUserId; // CREATE USER ID
	String createAppId; // CREATE APPLICATION ID
	Date createDt; // CREATE DATE
	String updateUserId; // UPDATE USER ID
	String updateAppId; // UPDATE APPLICATION ID
	Date updateDt; // UPDATE DATE

	public String getSysCd() {
		return this.sysCd;
	}

	@JsonProperty("sysCd")
	public void setSysCd(String sysCd) {
		this.sysCd = sysCd;
	}

	public String getVoClassId() {
		return this.voClassId;
	}

	@JsonProperty("voClassId")
	public void setVoClassId(String voClassId) {
		this.voClassId = voClassId;
	}

	public int getLayoutDetailSeq() {
		return this.layoutDatailSeq;
	}

	@JsonProperty("layoutDetailSeq")
	public void setLayoutDetailSeq(int layoutDatailSeq) {
		this.layoutDatailSeq = layoutDatailSeq;
	}

	public String getElementNm() {
		return this.elementNm;
	}

	@JsonProperty("elementNm")
	public void setElementNm(String elementNm) {
		this.elementNm = elementNm;
	}

	public String getFieldNm() {
		return this.fieldNm;
	}

	@JsonProperty("fieldNm")
	public void setFieldNm(String fieldNm) {
		this.fieldNm = fieldNm;
	}

	public String getFieldDesc() {
		return this.fieldDesc;
	}

	@JsonProperty("fieldDesc")
	public void setFieldDesc(String fieldDesc) {
		this.fieldDesc = fieldDesc;
	}

	public String getFieldDataTypeCd() {
		return this.fieldDataTypeCd;
	}

	@JsonProperty("fieldDataTypeCd")
	public void setFieldDataTypeCd(String fieldDataTypeCd) {
		this.fieldDataTypeCd = fieldDataTypeCd;
	}

	public int getFieldLengthVal() {
		return this.fieldLengthVal;
	}

	@JsonProperty("fieldLengthVal")
	public void setFieldLengthVal(int fieldLengthVal) {
		this.fieldLengthVal = fieldLengthVal;
	}

	public int getDecimalLengthVal() {
		return this.decimalLengthVal;
	}

	@JsonProperty("decimalLengthVal")
	public void setDecimalLengthVal(int decimalLengthVal) {
		this.decimalLengthVal = decimalLengthVal;
	}

	public String getFormatCd() {
		return this.formatCd;
	}

	@JsonProperty("formatCd")
	public void setFormatCd(String formatCd) {
		this.formatCd = formatCd;
	}

	public String getArrayRefFieldId() {
		return this.arrayRefFieldId;
	}

	@JsonProperty("arrayRefFieldId")
	public void setArrayRefFieldId(String arrayRefFieldId) {
		this.arrayRefFieldId = arrayRefFieldId;
	}

	public String getEncryptDecryptDivisionCd() {
		return this.encryptDecryptDivisionCd;
	}

	@JsonProperty("encryptDecryptDivisionCd")
	public void setEncryptDecryptDivisionCd(String encryptDecryptDivisionCd) {
		this.encryptDecryptDivisionCd = encryptDecryptDivisionCd;
	}

	public String getDefaultVal() {
		return this.defaultVal;
	}

	@JsonProperty("defaultVal")
	public void setDefaultVal(String defaultVal) {
		this.defaultVal = defaultVal;
	}

	public String getAlignCd() {
		return this.alignCd;
	}

	@JsonProperty("alignCd")
	public void setAlignCd(String alignCd) {
		this.alignCd = alignCd;
	}

	public String getCreateUserId() {
		return this.createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public String getCreateAppId() {
		return this.createAppId;
	}

	public void setCreateAppId(String createAppId) {
		this.createAppId = createAppId;
	}

	public Date getCreateDt() {
		return this.createDt;
	}

	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}

	public String getUpdateUserId() {
		return this.updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}

	public String getUpdateAppId() {
		return this.updateAppId;
	}

	public void setUpdateAppId(String updateAppId) {
		this.updateAppId = updateAppId;
	}

	public Date getUpdateDt() {
		return this.updateDt;
	}

	public void setUpdateDt(Date updateDt) {
		this.updateDt = updateDt;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LayoutDetailDto [");
		builder.append("sysCd=");
		builder.append(sysCd);
		builder.append(", voClassId=");
		builder.append(voClassId);
		builder.append(", layoutDetailSeq=");
		builder.append(layoutDatailSeq);
		builder.append(", elementNm=");
		builder.append(elementNm);
		builder.append(", fieldNm=");
		builder.append(fieldNm);
		builder.append(", fieldDesc=");
		builder.append(fieldDesc);
		builder.append(", fieldDataTypeCd=");
		builder.append(fieldDataTypeCd);
		builder.append(", fieldLengthVal=");
		builder.append(fieldLengthVal);
		builder.append(", decimalLengthVal=");
		builder.append(decimalLengthVal);
		builder.append(", formatCd=");
		builder.append(formatCd);
		builder.append(", arrayRefFieldId=");
		builder.append(arrayRefFieldId);
		builder.append(", encryptDecryptDivisionCd=");
		builder.append(encryptDecryptDivisionCd);
		builder.append(", defaultVal=");
		builder.append(defaultVal);
		builder.append(", alignCd=");
		builder.append(alignCd);
		builder.append(", createUserId=");
		builder.append(createUserId);
		builder.append(", createAppId=");
		builder.append(createAppId);
		builder.append(", createDt=");
		builder.append(createDt);
		builder.append(", updateUserId=");
		builder.append(updateUserId);
		builder.append(", updateAppId=");
		builder.append(updateAppId);
		builder.append(", updateDt=");
		builder.append(updateDt);
		builder.append("]");
		return builder.toString();
	}

}
