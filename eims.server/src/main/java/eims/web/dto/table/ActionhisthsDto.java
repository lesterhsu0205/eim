package eims.web.dto.table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_ABSENT, content = Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ActionhisthsDto {

	String hstDscd; // $col.colComment
	String itemId; // $col.colComment
	String workCttCd; // $col.colComment
	String workDttm; // $col.colComment
	String userId; // $col.colComment
	String itemDesc; // $col.colComment
	String intrfcType; // 인터페이스타입

	public String getIntrfcType() {
		return intrfcType;
	}

	public void setIntrfcType(String intrfcType) {
		this.intrfcType = intrfcType;
	}

	public String getHstDscd() {
		return this.hstDscd;
	}

	public void setHstDscd(String hstDscd) {
		this.hstDscd = hstDscd;
	}

	public String getItemId() {
		return this.itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getWorkCttCd() {
		return this.workCttCd;
	}

	public void setWorkCttCd(String workCttCd) {
		this.workCttCd = workCttCd;
	}

	public String getWorkDttm() {
		return this.workDttm;
	}

	public void setWorkDttm(String workDttm) {
		this.workDttm = workDttm;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getItemDesc() {
		return this.itemDesc;
	}

	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ActionhisthsDto [");
		builder.append("hstDscd=");
		builder.append(hstDscd);
		builder.append(", itemId=");
		builder.append(itemId);
		builder.append(", workCttCd=");
		builder.append(workCttCd);
		builder.append(", workDttm=");
		builder.append(workDttm);
		builder.append(", userId=");
		builder.append(userId);
		builder.append(", itemDesc=");
		builder.append(itemDesc);
		builder.append("]");
		return builder.toString();
	}

}