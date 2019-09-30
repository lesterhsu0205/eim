package eims.web.dto.table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_ABSENT, content = Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class IntrfcroutinfodtDto {

	String intrfcId; // $col.colComment
	int seq; // $col.colComment
	int lenFldOffsetLen; // $col.colComment
	int fldDataLen; // $col.colComment
	String fldCfgVal; // $col.colComment
	String rutngDesc; // $col.colComment
	String cndtCd; // $col.colComment

	public String getRutngDesc() {
		return rutngDesc;
	}

	public void setRutngDesc(String rutngDesc) {
		this.rutngDesc = rutngDesc;
	}

	public String getCndtCd() {
		return cndtCd;
	}

	public void setCndtCd(String cndtCd) {
		this.cndtCd = cndtCd;
	}

	public String getIntrfcId() {
		return this.intrfcId;
	}

	public void setIntrfcId(String intrfcId) {
		this.intrfcId = intrfcId;
	}

	public int getSeq() {
		return this.seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public int getLenFldOffsetLen() {
		return this.lenFldOffsetLen;
	}

	public void setLenFldOffsetLen(int lenFldOffsetLen) {
		this.lenFldOffsetLen = lenFldOffsetLen;
	}

	public int getFldDataLen() {
		return this.fldDataLen;
	}

	public void setFldDataLen(int fldDataLen) {
		this.fldDataLen = fldDataLen;
	}

	public String getFldCfgVal() {
		return this.fldCfgVal;
	}

	public void setFldCfgVal(String fldCfgVal) {
		this.fldCfgVal = fldCfgVal;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("IntrfcroutinfodtDto [");
		builder.append("intrfcId=");
		builder.append(intrfcId);
		builder.append(", seq=");
		builder.append(seq);
		builder.append(", lenFldOffsetLen=");
		builder.append(lenFldOffsetLen);
		builder.append(", fldDataLen=");
		builder.append(fldDataLen);
		builder.append(", fldCfgVal=");
		builder.append(fldCfgVal);
		builder.append("]");
		return builder.toString();
	}

}