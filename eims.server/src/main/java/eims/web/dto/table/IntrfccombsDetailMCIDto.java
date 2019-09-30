package eims.web.dto.table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import eims.web.dto.IntrfccombsDetail;

@JsonInclude(value = Include.NON_ABSENT, content = Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class IntrfccombsDetailMCIDto {

	private String timeOut; //타입아웃
//	private String timeOutProcMode; //타임아웃처리방법
//	private String trnsmsnErrResYn; //전송에러응답여부
	private String privacyInclYn; //개인정보여부
	private String occurCycle; //발생주기
	private String dayOccurCnt; //일발생건수
	private String currIntrfcIdentifier; //현행인터페이스식별자
	private String backupAprvStat; //백업승인상태확인
	private String encTargetYn; //암호화대상여부
	private String intrfDesc;
	private String intrfcUse; //인터페이스용도

	public String getIntrfcUse() {
		return intrfcUse;
	}

	public void setIntrfcUse(String intrfcUse) {
		this.intrfcUse = intrfcUse;
	}

	public String getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(String timeOut) {
		this.timeOut = timeOut;
	}

	public String getPrivacyInclYn() {
		return privacyInclYn;
	}

	public void setPrivacyInclYn(String privacyInclYn) {
		this.privacyInclYn = privacyInclYn;
	}

	public String getOccurCycle() {
		return occurCycle;
	}

	public void setOccurCycle(String occurCycle) {
		this.occurCycle = occurCycle;
	}

	public String getDayOccurCnt() {
		return dayOccurCnt;
	}

	public void setDayOccurCnt(String dayOccurCnt) {
		this.dayOccurCnt = dayOccurCnt;
	}

	public String getCurrIntrfcIdentifier() {
		return currIntrfcIdentifier;
	}

	public void setCurrIntrfcIdentifier(String currIntrfcIdentifier) {
		this.currIntrfcIdentifier = currIntrfcIdentifier;
	}

	public String getBackupAprvStat() {
		return backupAprvStat;
	}

	public void setBackupAprvStat(String backupAprvStat) {
		this.backupAprvStat = backupAprvStat;
	}

	public String getEncTargetYn() {
		return encTargetYn;
	}

	public void setEncTargetYn(String encTargetYn) {
		this.encTargetYn = encTargetYn;
	}

	public String getIntrfDesc() {
		return intrfDesc;
	}

	public void setIntrfDesc(String intrfDesc) {
		this.intrfDesc = intrfDesc;
	}

}