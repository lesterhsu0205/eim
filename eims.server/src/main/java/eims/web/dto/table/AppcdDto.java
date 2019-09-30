package eims.web.dto.table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_ABSENT, content = Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AppcdDto {

	String appCd; // $col.colComment
	String appCdNm; // $col.colComment
	String parentAppCd; // $col.colComment
	String lvCd; // $col.colComment
	int alignOrderNo; // $col.colComment
	String appCdDesc; // $col.colComment
	String appUnqKey;

	public String getAppUnqKey() {
		return appUnqKey;
	}

	public void setAppUnqKey(String appUnqKey) {
		this.appUnqKey = appUnqKey;
	}

	public String getAppCd() {
		return this.appCd;
	}

	public void setAppCd(String appCd) {
		this.appCd = appCd;
	}

	public String getAppCdNm() {
		return this.appCdNm;
	}

	public void setAppCdNm(String appCdNm) {
		this.appCdNm = appCdNm;
	}

	public String getParentAppCd() {
		return this.parentAppCd;
	}

	public void setParentAppCd(String parentAppCd) {
		this.parentAppCd = parentAppCd;
	}

	public String getLvCd() {
		return this.lvCd;
	}

	public void setLvCd(String lvCd) {
		this.lvCd = lvCd;
	}

	public int getAlignOrderNo() {
		return this.alignOrderNo;
	}

	public void setAlignOrderNo(int alignOrderNo) {
		this.alignOrderNo = alignOrderNo;
	}

	public String getAppCdDesc() {
		return this.appCdDesc;
	}

	public void setAppCdDesc(String appCdDesc) {
		this.appCdDesc = appCdDesc;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AppcdDto [");
		builder.append("appCd=");
		builder.append(appCd);
		builder.append(", appCdNm=");
		builder.append(appCdNm);
		builder.append(", parentAppCd=");
		builder.append(parentAppCd);
		builder.append(", lvCd=");
		builder.append(lvCd);
		builder.append(", alignOrderNo=");
		builder.append(alignOrderNo);
		builder.append(", appCdDesc=");
		builder.append(appCdDesc);
		builder.append("]");
		return builder.toString();
	}

}