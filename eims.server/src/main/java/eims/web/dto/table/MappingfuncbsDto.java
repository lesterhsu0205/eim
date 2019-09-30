package eims.web.dto.table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_ABSENT, content = Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MappingfuncbsDto {

	String mappingFuncNm; // $col.colComment
	int argsCnt; // $col.colComment
	String guideDesc; // $col.colComment

	public String getMappingFuncNm() {
		return this.mappingFuncNm;
	}

	public void setMappingFuncNm(String mappingFuncNm) {
		this.mappingFuncNm = mappingFuncNm;
	}

	public int getArgsCnt() {
		return this.argsCnt;
	}

	public void setArgsCnt(int argsCnt) {
		this.argsCnt = argsCnt;
	}

	public String getGuideDesc() {
		return this.guideDesc;
	}

	public void setGuideDesc(String guideDesc) {
		this.guideDesc = guideDesc;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MappingfuncbsDto [");
		builder.append("mappingFuncNm=");
		builder.append(mappingFuncNm);
		builder.append(", argsCnt=");
		builder.append(argsCnt);
		builder.append(", guideDesc=");
		builder.append(guideDesc);
		builder.append("]");
		return builder.toString();
	}

}