package eims.web.dto.table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_ABSENT, content = Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PermDto {

	String permId; // 권한ID
	String permNm; // 권한명
	String permDesc; // 권한설명
	String permTypeCd; // 권한유형코드
	private boolean check;

	public boolean isCheck() {
		return check;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}

	public String getPermId() {
		return permId;
	}

	public void setPermId(String permId) {
		this.permId = permId;
	}

	public String getPermNm() {
		return permNm;
	}

	public void setPermNm(String permNm) {
		this.permNm = permNm;
	}

	public String getPermDesc() {
		return permDesc;
	}

	public void setPermDesc(String permDesc) {
		this.permDesc = permDesc;
	}

	public String getPermTypeCd() {
		return permTypeCd;
	}

	public void setPermTypeCd(String permTypeCd) {
		this.permTypeCd = permTypeCd;
	}

}
