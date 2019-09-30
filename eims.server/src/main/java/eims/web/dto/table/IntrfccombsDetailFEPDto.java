package eims.web.dto.table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_ABSENT, content = Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class IntrfccombsDetailFEPDto {
	//온라인
	private String timeOut; //타임아웃
	private String timeOutProcMode; //탕미아웃처리방법
	private String trnsmsnErrResYn; //전송에러응답여부
	private String delayRspsYn; //지연응답여부
	private String reqWrapperDtoNm; //요청wrapperDto명
	private String resWrapperDtoNm; //응답wrapperDto명
	private String relationTrxCd; //연관거래코드
	private String commNetworkIntrfcYn ;//통신망인터페이스여부
	//배치
	private String fileId;// 파일ID
//	private String dataTrnsSize; //데이터전송크기
	private String recordSize; //레코드사이즈
	private String recordSeparator; //레코드구분자
	private String fileNm; //파일명
	private String dupTrnsmsnAllwYn; //중복전송허용여부
	private String dupFileProcMode; //중복파일처리방법 
	private String dupFileProc; //중복처리방법 (라인추가)
//	private String rqstEventDstnctn;//요청이벤트구분
	private String occurCycle; //발생주기
	private String fileTranIntrfcId; //파일전송인터페이스ID

	//공통
	private String privacyInclYn; //개인정보포함여부
	private String encTargetYn; //암호화대상여부
	private String currIntrfcIdentifier; //현행인터페이스식별자
	private String intrfDesc;
	private String backupAprvStat; //백업승인상태확인
	private String intrfcUse; //인터페이스용도
	
	private String sendFileNm; //파일명
	private String sendFilePath; // 파일 경로
	private String recvFilePath; //파일경로			

	public String getDupFileProc() {
		return dupFileProc;
	}

	public void setDupFileProc(String dupFileProc) {
		this.dupFileProc = dupFileProc;
	}

	public String getRecvFilePath() {
		return recvFilePath;
	}

	public void setRecvFilePath(String recvFilePath) {
		this.recvFilePath = recvFilePath;
	}
	
	public String getSendFileNm() {
		return sendFileNm;
	}

	public void setSendFileNm(String sendFileNm) {
		this.sendFileNm = sendFileNm;
	}


	public String getSendFilePath() {
		return sendFilePath;
	}

	public void setSendFilePath(String sendFilePath) {
		this.sendFilePath = sendFilePath;
	}

	public String getCommNetworkIntrfcYn() {
		return commNetworkIntrfcYn;
	}

	public void setCommNetworkIntrfcYn(String commNetworkIntrfcYn) {
		this.commNetworkIntrfcYn = commNetworkIntrfcYn;
	}

	public String getRelationTrxCd() {
		return relationTrxCd;
	}

	public void setRelationTrxCd(String relationTrxCd) {
		this.relationTrxCd = relationTrxCd;
	}

	public String getFileTranIntrfcId() {
		return fileTranIntrfcId;
	}

	public void setFileTranIntrfcId(String fileTranIntrfcId) {
		this.fileTranIntrfcId = fileTranIntrfcId;
	}

	public String getIntrfcUse() {
		return intrfcUse;
	}

	public void setIntrfcUse(String intrfcUse) {
		this.intrfcUse = intrfcUse;
	}

	public String getBackupAprvStat() {
		return backupAprvStat;
	}

	public void setBackupAprvStat(String backupAprvStat) {
		this.backupAprvStat = backupAprvStat;
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

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

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

	public String getIntrfDesc() {
		return intrfDesc;
	}

	public void setIntrfDesc(String intrfDesc) {
		this.intrfDesc = intrfDesc;
	}

	public String getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(String timeOut) {
		this.timeOut = timeOut;
	}

	public String getTimeOutProcMode() {
		return timeOutProcMode;
	}

	public void setTimeOutProcMode(String timeOutProcMode) {
		this.timeOutProcMode = timeOutProcMode;
	}

	public String getDelayRspsYn() {
		return delayRspsYn;
	}

	public void setDelayRspsYn(String delayRspsYn) {
		this.delayRspsYn = delayRspsYn;
	}

	public String getTrnsmsnErrResYn() {
		return trnsmsnErrResYn;
	}

	public void setTrnsmsnErrResYn(String trnsmsnErrResYn) {
		this.trnsmsnErrResYn = trnsmsnErrResYn;
	}

	public String getFileNm() {
		return fileNm;
	}

	public void setFileNm(String fileNm) {
		this.fileNm = fileNm;
	}

	public String getDupTrnsmsnAllwYn() {
		return dupTrnsmsnAllwYn;
	}

	public void setDupTrnsmsnAllwYn(String dupTrnsmsnAllwYn) {
		this.dupTrnsmsnAllwYn = dupTrnsmsnAllwYn;
	}

	public String getDupFileProcMode() {
		return dupFileProcMode;
	}

	public void setDupFileProcMode(String dupFileProcMode) {
		this.dupFileProcMode = dupFileProcMode;
	}

	public String getOccurCycle() {
		return occurCycle;
	}

	public void setOccurCycle(String occurCycle) {
		this.occurCycle = occurCycle;
	}

	public String getPrivacyInclYn() {
		return privacyInclYn;
	}

	public void setPrivacyInclYn(String privacyInclYn) {
		this.privacyInclYn = privacyInclYn;
	}

	public String getEncTargetYn() {
		return encTargetYn;
	}

	public void setEncTargetYn(String encTargetYn) {
		this.encTargetYn = encTargetYn;
	}

	public String getCurrIntrfcIdentifier() {
		return currIntrfcIdentifier;
	}

	public void setCurrIntrfcIdentifier(String currIntrfcIdentifier) {
		this.currIntrfcIdentifier = currIntrfcIdentifier;
	}

}