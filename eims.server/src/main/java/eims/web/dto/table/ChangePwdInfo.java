package eims.web.dto.table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(value = Include.NON_ABSENT, content = Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChangePwdInfo {
	private String sysCd;
	private String userId;
	private String userPwd;
	private String changePwd;
	private String updateUserId;
	private String updateAppId;

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
		return sysCd;
	}

	@JsonProperty("tenantId")
	public void setSysCd(String sysCd) {
		this.sysCd = sysCd;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	public String getChangePwd() {
		return changePwd;
	}

	public void setChangePwd(String changePwd) {
		this.changePwd = changePwd;
	}

	public String getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}

	public String getUpdateAppId() {
		return updateAppId;
	}

	public void setUpdateAppId(String updateAppId) {
		this.updateAppId = updateAppId;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ChangePwdInfo [sysCd=");
		builder.append(sysCd);
		builder.append(", userId=");
		builder.append(userId);
		builder.append(", userPwd=");
		builder.append(userPwd);
		builder.append(", changePwd=");
		builder.append(changePwd);
		builder.append(", updateUserId=");
		builder.append(updateUserId);
		builder.append(", updateAppId=");
		builder.append(updateAppId);
		builder.append("]");
		return builder.toString();
	}

}
