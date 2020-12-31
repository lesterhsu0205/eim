package eims.web.dto.table;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DomainCORSDto {

	String sysCd; // SYSTEM CODE
	String domainId; // DOMAIN ID
	String originDomainNm; // ORIGIN VALUE
	String originHeaderContent; // ORIGIN HEADER
	String originMethodId; // ORIGIN METHOD ID
	String createUserId; // CREATE USER ID
	String createAppId; // CREATE APPLICATION ID
	Date createDt; // CREATE DATE
	String updateUserId; // UPDATE USER ID
	String updateAppId; // UPDATE APPLICATION ID
	Date updateDt; // UPDATE DATE

	// Remove me
	String lastChngDtm; // 최종변경일시
	String lastChngGuid; // 최종변경GUID

	/**
	 * @param lastChngDtm the lastChngDtm to set
	 */
	public void setLastChngDtm(String lastChngDtm) {
		this.lastChngDtm = lastChngDtm;
	}

	/**
	 * @param lastChngGuid the lastChngGuid to set
	 */
	public void setLastChngGuid(String lastChngGuid) {
		this.lastChngGuid = lastChngGuid;
	}

	public String getSysCd() {
		return this.sysCd;
	}

	@JsonProperty("tenantId")
	public void setSysCd(String sysCd) {
		this.sysCd = sysCd;
	}

	public String getDomainId() {
		return this.domainId;
	}

	@JsonProperty("domainId")
	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}

	public String getOriginDomainNm() {
		return this.originDomainNm;
	}

	@JsonProperty("originValue")
	public void setOriginDomainNm(String originDomainNm) {
		this.originDomainNm = originDomainNm;
	}

	public String getOriginHeaderContent() {
		return this.originHeaderContent;
	}

	@JsonProperty("originHeader")
	public void setOriginHeaderContent(String originHeaderContent) {
		this.originHeaderContent = originHeaderContent;
	}

	public String getOriginMethodId() {
		return this.originMethodId;
	}

	@JsonProperty("originMethod")
	public void setOriginMethodId(String originMethodId) {
		this.originMethodId = originMethodId;
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
		builder.append("DomainCORSDto [");
		builder.append("sysCd=");
		builder.append(sysCd);
		builder.append(", domainId=");
		builder.append(domainId);
		builder.append(", originDomainNm=");
		builder.append(originDomainNm);
		builder.append(", originHeaderContent=");
		builder.append(originHeaderContent);
		builder.append(", originMethodId=");
		builder.append(originMethodId);
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
