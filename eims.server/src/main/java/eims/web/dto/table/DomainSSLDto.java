package eims.web.dto.table;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DomainSSLDto {

	String sysCd; // SYSTEM CODE
	String domainId; // DOMAIN ID
	String resourcePathUrlAdr; // RESOURCE PATH
	String sslPassword; // SSL PASSWORD
	byte[] sslContent;
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

	public byte[] getSslContent() {
		return sslContent;
	}

	@JsonProperty("sslContent")
	public void setSslContent(byte[] sslContent) {
		this.sslContent = sslContent;
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

	public String getResourcePathUrlAdr() {
		return this.resourcePathUrlAdr;
	}

	@JsonProperty("resourcePath")
	public void setResourcePathUrlAdr(String resourcePathUrlAdr) {
		this.resourcePathUrlAdr = resourcePathUrlAdr;
	}

	public String getSslPassword() {
		return this.sslPassword;
	}

	@JsonProperty("pwd")
	public void setSslPassword(String sslPassword) {
		this.sslPassword = sslPassword;
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
		builder.append("DomainDto [");
		builder.append("sysCd=");
		builder.append(sysCd);
		builder.append(", domainId=");
		builder.append(domainId);
		builder.append(", resourcePathUrlAdr=");
		builder.append(resourcePathUrlAdr);
		builder.append(", sslPassword=");
		builder.append(sslPassword);
		builder.append(", sslContent=");
		builder.append(sslContent);
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
