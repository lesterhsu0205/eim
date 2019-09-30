package eims.web.dto.table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_ABSENT, content = Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MetaInterfaceDto {

	int ifDataSeq; 
	String ifDataCrtTmstmp; 
	String ifDataAplyTpCd; 
	String ifDataAplyStsCd;
	String ifDataAplyTmstmp;
	String ifDataAplyErrCd;
	String ifDataAplyErrMsg;
	String stdTrmTpCd;
	String stdTrmLgclNm;
	String stdTrmPsclNm;
	String stdTrmDataTpNm;
	int stdTrmDataLen;
	int stdTrmDataScl;
	String stdTrmDmnEncYn;
	
	public int getIfDataSeq() {
		return ifDataSeq;
	}
	public void setIfDataSeq(int ifDataSeq) {
		this.ifDataSeq = ifDataSeq;
	}
	public String getIfDataCrtTmstmp() {
		return ifDataCrtTmstmp;
	}
	public void setIfDataCrtTmstmp(String ifDataCrtTmstmp) {
		this.ifDataCrtTmstmp = ifDataCrtTmstmp;
	}
	public String getIfDataAplyTpCd() {
		return ifDataAplyTpCd;
	}
	public void setIfDataAplyTpCd(String ifDataAplyTpCd) {
		this.ifDataAplyTpCd = ifDataAplyTpCd;
	}
	public String getIfDataAplyStsCd() {
		return ifDataAplyStsCd;
	}
	public void setIfDataAplyStsCd(String ifDataAplyStsCd) {
		this.ifDataAplyStsCd = ifDataAplyStsCd;
	}
	public String getIfDataAplyTmstmp() {
		return ifDataAplyTmstmp;
	}
	public void setIfDataAplyTmstmp(String ifDataAplyTmstmp) {
		this.ifDataAplyTmstmp = ifDataAplyTmstmp;
	}
	public String getIfDataAplyErrCd() {
		return ifDataAplyErrCd;
	}
	public void setIfDataAplyErrCd(String ifDataAplyErrCd) {
		this.ifDataAplyErrCd = ifDataAplyErrCd;
	}
	public String getIfDataAplyErrMsg() {
		return ifDataAplyErrMsg;
	}
	public void setIfDataAplyErrMsg(String ifDataAplyErrMsg) {
		this.ifDataAplyErrMsg = ifDataAplyErrMsg;
	}
	public String getStdTrmTpCd() {
		return stdTrmTpCd;
	}
	public void setStdTrmTpCd(String stdTrmTpCd) {
		this.stdTrmTpCd = stdTrmTpCd;
	}
	public String getStdTrmLgclNm() {
		return stdTrmLgclNm;
	}
	public void setStdTrmLgclNm(String stdTrmLgclNm) {
		this.stdTrmLgclNm = stdTrmLgclNm;
	}
	public String getStdTrmPsclNm() {
		return stdTrmPsclNm;
	}
	public void setStdTrmPsclNm(String stdTrmPsclNm) {
		this.stdTrmPsclNm = stdTrmPsclNm;
	}
	public String getStdTrmDataTpNm() {
		return stdTrmDataTpNm;
	}
	public void setStdTrmDataTpNm(String stdTrmDataTpNm) {
		this.stdTrmDataTpNm = stdTrmDataTpNm;
	}
	public int getStdTrmDataLen() {
		return stdTrmDataLen;
	}
	public void setStdTrmDataLen(int stdTrmDataLen) {
		this.stdTrmDataLen = stdTrmDataLen;
	}
	public int getStdTrmDataScl() {
		return stdTrmDataScl;
	}
	public void setStdTrmDataScl(int stdTrmDataScl) {
		this.stdTrmDataScl = stdTrmDataScl;
	}
	public String getStdTrmDmnEncYn() {
		return stdTrmDmnEncYn;
	}
	public void setStdTrmDmnEncYn(String stdTrmDmnEncYn) {
		this.stdTrmDmnEncYn = stdTrmDmnEncYn;
	}
	
	
	
}