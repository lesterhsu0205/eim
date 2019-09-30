package eims.web.dto.table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import eims.web.dto.IntrfccombsDetail;

@JsonInclude(value = Include.NON_ABSENT, content = Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class IntrfccombsDetailEAIDto {

	//공통
	private String timeOut; //타입아웃
	private String privacyInclYn; //개인정보포함여부
	private String currIntrfcIdentifier; //현행인터페이스식별자
	private String backupAprvStat; //백업승인상태확인
	private String occurCycle; //발생주기
	private String dupFileProc; //중복처리방법
	private String scheduleProcInterval; // 스케줄 처리 간격(초)
	private String dayOccurCnt; //일발생건수
	private String encTargetYn; //암호화대상여부
	private String intrfDesc; //비고
	private String intrfcUse; //인터페이스용도
	//송신파일속성
	private String sendFilePath; //파일경로
	private String sendFileNm; //파일명
	private String sendTranPostProc; //전송후처리유형
	private String sendTranPostBackPath; //전송후백업경로
	/** 
	 * 20181018 추가
	 * */
	private String sendTranBeforeScript ;//전송전선행스크립트
	private String sendTranBeforeScriptType ;//전송전선행스크립트 유형
	
	//수신파일속성rh
	private String recvFilePath; //파일경로
	private String recvFileNm;//파일명
	private String recvTranPostFinCreateYn;//전송후fin파일생성여부
	private String recvTranPostScript; //전송후후행스크립트
	private String recordSize; //레코드사이즈
	private String recordSeparator; //레코드구분자
	/** 
	 * 20181018 추가
	 * */
	private String recvTranPostScriptType; //전송후후행스크립트유형

	//DBTODB / APTODB 속성
	private String recvDbActionType; //수신DB동작유형
	private String lobColNm;//Lob 컬럼명
	private Integer searchProcCnt;//조회처리건수
	private String errSkipYn;//에러Skip여부
	private String sendDbQuery;//송신DB쿼리문
	private String recvDbQuery;//수신DB쿼리문
	
	//대외파일전송인터페이스/FILETOFILE
	private String reqWrapperDtoNm; //요청wrapperDto명
	private String resWrapperDtoNm; //응답wrapperDto명

	
	public String getScheduleProcInterval() {
		return scheduleProcInterval;
	}

	public void setScheduleProcInterval(String scheduleProcInterval) {
		this.scheduleProcInterval = scheduleProcInterval;
	}
	
	public String getDupFileProc() {
		return dupFileProc;
	}

	public void setDupFileProc(String dupFileProc) {
		this.dupFileProc = dupFileProc;
	}

	public String getSendTranBeforeScript() {
		return sendTranBeforeScript;
	}

	public void setSendTranBeforeScript(String sendTranBeforeScript) {
		this.sendTranBeforeScript = sendTranBeforeScript;
	}

	public String getSendTranBeforeScriptType() {
		return sendTranBeforeScriptType;
	}

	public void setSendTranBeforeScriptType(String sendTranBeforeScriptType) {
		this.sendTranBeforeScriptType = sendTranBeforeScriptType;
	}

	public String getRecvTranPostScriptType() {
		return recvTranPostScriptType;
	}

	public void setRecvTranPostScriptType(String recvTranPostScriptType) {
		this.recvTranPostScriptType = recvTranPostScriptType;
	}

	private String fepTranIntrfcId; //fep 일괄전송인터페이스ID

	public String getRecordSize() {
		return recordSize;
	}

	public void setRecordSize(String recordSize) {
		this.recordSize = recordSize;
	}

	public String getRecordSeparator() {
		return recordSeparator;
	}

	public void setRecordSeparator(String recordSeparator) {
		this.recordSeparator = recordSeparator;
	}

	public String getReqWrapperDtoNm() {
		return reqWrapperDtoNm;
	}

	public void setReqWrapperDtoNm(String reqWrapperDtoNm) {
		this.reqWrapperDtoNm = reqWrapperDtoNm;
	}

	public String getResWrapperDtoNm() {
		return resWrapperDtoNm;
	}

	public void setResWrapperDtoNm(String resWrapperDtoNm) {
		this.resWrapperDtoNm = resWrapperDtoNm;
	}

	public String getFepTranIntrfcId() {
		return fepTranIntrfcId;
	}

	public void setFepTranIntrfcId(String fepTranIntrfcId) {
		this.fepTranIntrfcId = fepTranIntrfcId;
	}

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

	public String getSendFilePath() {
		return sendFilePath;
	}

	public void setSendFilePath(String sendFilePath) {
		this.sendFilePath = sendFilePath;
	}

	public String getSendFileNm() {
		return sendFileNm;
	}

	public void setSendFileNm(String sendFileNm) {
		this.sendFileNm = sendFileNm;
	}

	public String getSendTranPostProc() {
		return sendTranPostProc;
	}

	public void setSendTranPostProc(String sendTranPostProc) {
		this.sendTranPostProc = sendTranPostProc;
	}

	public String getSendTranPostBackPath() {
		return sendTranPostBackPath;
	}

	public void setSendTranPostBackPath(String sendTranPostBackPath) {
		this.sendTranPostBackPath = sendTranPostBackPath;
	}

	public String getRecvFilePath() {
		return recvFilePath;
	}

	public void setRecvFilePath(String recvFilePath) {
		this.recvFilePath = recvFilePath;
	}

	public String getRecvFileNm() {
		return recvFileNm;
	}

	public void setRecvFileNm(String recvFileNm) {
		this.recvFileNm = recvFileNm;
	}

	public String getRecvTranPostFinCreateYn() {
		return recvTranPostFinCreateYn;
	}

	public void setRecvTranPostFinCreateYn(String recvTranPostFinCreateYn) {
		this.recvTranPostFinCreateYn = recvTranPostFinCreateYn;
	}

	public String getRecvTranPostScript() {
		return recvTranPostScript;
	}

	public void setRecvTranPostScript(String recvTranPostScript) {
		this.recvTranPostScript = recvTranPostScript;
	}

	public String getRecvDbActionType() {
		return recvDbActionType;
	}

	public void setRecvDbActionType(String recvDbActionType) {
		this.recvDbActionType = recvDbActionType;
	}

	public String getLobColNm() {
		return lobColNm;
	}

	public void setLobColNm(String lobColNm) {
		this.lobColNm = lobColNm;
	}

	public Integer getSearchProcCnt() {
		return searchProcCnt;
	}

	public void setSearchProcCnt(Integer searchProcCnt) {
		this.searchProcCnt = searchProcCnt;
	}

	public String getErrSkipYn() {
		return errSkipYn;
	}

	public void setErrSkipYn(String errSkipYn) {
		this.errSkipYn = errSkipYn;
	}

	public String getSendDbQuery() {
		return sendDbQuery;
	}

	public void setSendDbQuery(String sendDbQuery) {
		this.sendDbQuery = sendDbQuery;
	}

	public String getRecvDbQuery() {
		return recvDbQuery;
	}

	public void setRecvDbQuery(String recvDbQuery) {
		this.recvDbQuery = recvDbQuery;
	}

}