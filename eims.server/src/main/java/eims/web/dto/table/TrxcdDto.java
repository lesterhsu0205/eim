package eims.web.dto.table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_ABSENT, content = Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrxcdDto {

	String trxCd; // 거래코드
	String trxCdNm; // 거래코드명
	String mngSysCd; // 관리시스템코드 -> COO, COB 등
	String trxCdDesc; // 거래코드설명

	public String getTrxCd() {
		return this.trxCd;
	}

	public void setTrxCd(String trxCd) {
		this.trxCd = trxCd;
	}

	public String getTrxCdNm() {
		return this.trxCdNm;
	}

	public void setTrxCdNm(String trxCdNm) {
		this.trxCdNm = trxCdNm;
	}

	public String getMngSysCd() {
		return this.mngSysCd;
	}

	public void setMngSysCd(String mngSysCd) {
		this.mngSysCd = mngSysCd;
	}

	public String getTrxCdDesc() {
		return this.trxCdDesc;
	}

	public void setTrxCdDesc(String trxCdDesc) {
		this.trxCdDesc = trxCdDesc;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TrxcdDto [");
		builder.append("trxCd=");
		builder.append(trxCd);
		builder.append(", trxCdNm=");
		builder.append(trxCdNm);
		builder.append(", mngSysCd=");
		builder.append(mngSysCd);
		builder.append(", trxCdDesc=");
		builder.append(trxCdDesc);
		builder.append("]");
		return builder.toString();
	}

}