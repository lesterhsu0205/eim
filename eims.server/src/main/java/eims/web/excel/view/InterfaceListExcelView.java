/**
 * 
 */
package eims.web.excel.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import com.google.common.io.Files;

import eims.ServiceContext;
import eims.web.constants.BxMessages;
import eims.web.dao.AppcdDao;
import eims.web.dao.CommCodeDao;
import eims.web.dto.IntrfcInfoExportDto;
import eims.web.dto.table.AppcdDto;
import eims.web.exception.ServiceException;
import eims.web.utils.ExcelUtils;

/**
 * @author 20180032
 *
 */
public class InterfaceListExcelView extends AbstractXlsxView {

	final Logger logger = LoggerFactory.getLogger(getClass());

	private CommCodeDao codeDao;
	@Autowired
	private AppcdDao appCdDao;
	private final int startLine = 5;
	private static final String STR_TEMPLATE_FILE_PATH = "/WEB-INF/classes/templates/";
	private static final String STR_TEMPLATE_FILENAME_PREFIX = "LBTW_BW_QAS_5.0.Interface List_v1.0_";
//	private static final String STR_TEMPLATE_FILE_PATH = "C:\\Users\\user\\Desktop\\temp\\template\\";
//	private static final String STR_TEMPLATE_FILENAME_PREFIX = "LBTW_BW_QAS_5.0.Interface List_v1.0_";

	public InterfaceListExcelView(CommCodeDao codeDao) {
		this.codeDao = codeDao;
	}

	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 응답헤더 세팅
		String intrfcTypeFileName = (String) model.get("intrfcType");
		if(intrfcTypeFileName.equals("MCI")) intrfcTypeFileName = "MCA";
		if(intrfcTypeFileName.equals("EAI_I")) intrfcTypeFileName = "EAI";
		String attachment = "attachment; filename=\"" + intrfcTypeFileName + "_"
				+ LocalDateTime.now() + ".xlsx\"";
		response.setHeader("Content-Disposition", attachment);

		String intrfcType = (String) model.get("intrfcType");
		List<IntrfcInfoExportDto> intrfcInfos = (List<IntrfcInfoExportDto>) model.get("intrfcInfos");
		int startIndex = 5;
		int endIndex = 10000;

		Sheet sheet = null;
		int writeLine = startLine;
		if (intrfcType.equals("MCI")) {
			for (IntrfcInfoExportDto intrfcInfo : intrfcInfos) {
				logger.debug("{}", intrfcInfo);
				if (intrfcInfo.getMciDto() == null) {
					logger.error("MCI DTO in rawdata is missing. MCIDTO : {}", intrfcInfo.getMciDto());
				} else {
					sheet = workbook.getSheet("MCA");
					writeInterfaceInfoMCI(sheet, intrfcInfo, writeLine++);
					startIndex++;
				}
			}
		} else if (intrfcType.equals("CC")) {
			for (IntrfcInfoExportDto intrfcInfo : intrfcInfos) {
				logger.debug("{}", intrfcInfo);
				sheet = workbook.getSheet("CC");
				writeInterfaceInfoCC(sheet, intrfcInfo, writeLine++);
				startIndex++;

			}
		} else if (intrfcType.equals("EAI_I")) {
			for (IntrfcInfoExportDto intrfcInfo : intrfcInfos) {
				logger.debug("{}", intrfcInfo);
				if (intrfcInfo.getEaiDto() == null) {
					logger.error("EAI_I DTO in rawdata is missing. EAI_I DTO : {}", intrfcInfo.getEaiDto());
				} else {
					sheet = workbook.getSheet("EAI");
					writeInterfaceInfoEAI_I(sheet, intrfcInfo, writeLine++);
					startIndex++;
				}
			}
		} else if (intrfcType.equals("EAI_E")) {
			for (IntrfcInfoExportDto intrfcInfo : intrfcInfos) {
				logger.debug("{}", intrfcInfo);
				if (intrfcInfo.getEaiDto() == null) {
					logger.error("EAI_E DTO in rawdata is missing. EAI_E DTO : {}", intrfcInfo.getEaiDto());
				} else {
					sheet = workbook.getSheet("EAI_E");
					writeInterfaceInfoEAI_E(sheet, intrfcInfo, writeLine++);
					startIndex++;
				}
			}
		} else if (intrfcType.equals("FEP")) {
			for (IntrfcInfoExportDto intrfcInfo : intrfcInfos) {
				logger.debug("{}", intrfcInfo);
				if (intrfcInfo.getFepDto() == null) {
					logger.error("FEP DTO in rawdata is missing. FEP DTO : {}", intrfcInfo.getFepDto());
				} else {
					sheet = workbook.getSheet("FEP");
					writeInterfaceInfoFEP(sheet, intrfcInfo, writeLine++);
					startIndex++;
				}
			}
		} else {
			throw new Exception("Invalid IntrfcType : " + intrfcType);
		}

		if (intrfcType.equals("FEP")) {
			endIndex = 10000;
		}

		for (int i = startIndex - 1; i < endIndex; i++) {
			sheet.removeRow(sheet.getRow(i));
		}

		// Shift Rows
		sheet.shiftRows(endIndex, endIndex, -(endIndex - startIndex), true, false);

	}

	private void writeInterfaceInfoFEP(Sheet sheet, IntrfcInfoExportDto intrfcDto, int writeLine) throws ParseException {
		logger.debug("Interface ID to write in excelfile: {}, FEP DTO : {}", intrfcDto.getIntrfcId(), intrfcDto.getFepDto());
		ExcelUtils.writeValue(sheet, writeLine, "B", intrfcDto.getLv1Cd());// 어플리케이션
		ExcelUtils.writeValue(sheet, writeLine, "C", intrfcDto.getAppCdNm());// 어플리케이션명
		ExcelUtils.writeValue(sheet, writeLine, "D", intrfcDto.getIntrfcId());// 인터페이스아이디
		ExcelUtils.writeValue(sheet, writeLine, "E", intrfcDto.getIntrfcNm());// 인터페이스명
		
		ExcelUtils.writeValue(sheet, writeLine, "F", getCodeValue("INTRFC_WAY_CD", intrfcDto.getIntrfcWayCd(), "ko"));// 발생유형
		ExcelUtils.writeValue(sheet, writeLine, "G", getCodeValue("TRAN_DSCD", intrfcDto.getTrxDscd(), "ko"));// 거래유형	
		
		ExcelUtils.writeValue(sheet, writeLine, "H", intrfcDto.getRqstExtrnlMsgNo());// 요청전문번호
		ExcelUtils.writeValue(sheet, writeLine, "I", intrfcDto.getInstCd());// 기관코드
		ExcelUtils.writeValue(sheet, writeLine, "J", intrfcDto.getInstCdNm());// 기관코드
		ExcelUtils.writeValue(sheet, writeLine, "K", intrfcDto.getProtocolDscd());// 프로토콜 종류
		if(intrfcDto.getProtocolDscd() != null) {
			if(intrfcDto.getProtocolDscd().equals("REST")) {
			ExcelUtils.writeValue(sheet, writeLine, "L", intrfcDto.getHttpMethod()); // HTTP METHOD
			ExcelUtils.writeValue(sheet, writeLine, "M", intrfcDto.getContextUrl()); // ContextUrl
			ExcelUtils.writeValue(sheet, writeLine, "N", intrfcDto.getRequestMediaType()); // RequestMediaType
			ExcelUtils.writeValue(sheet, writeLine, "O", intrfcDto.getResponseMediaType()); // ResponseMediaType
			}
		}
		ExcelUtils.writeValue(sheet, writeLine, "R", intrfcDto.getSysCdSend());// 송신시스템코드
		ExcelUtils.writeValue(sheet, writeLine, "S", intrfcDto.getSysNmSend());//  송신시스템명
		ExcelUtils.writeValue(sheet, writeLine, "T", intrfcDto.getCrgManNmSend());// 송신시스템관리자명
		ExcelUtils.writeValue(sheet, writeLine, "U", intrfcDto.getSysCdReceive());// 수신시스템코드
		ExcelUtils.writeValue(sheet, writeLine, "V", intrfcDto.getSysNmReceive());//  수신시스템명
		ExcelUtils.writeValue(sheet, writeLine, "W", intrfcDto.getCrgManNmReceive());// 수신시스템관리자명		
		ExcelUtils.writeValue(sheet, writeLine, "X", intrfcDto.getTrxCdReceive());// 수신시스템거래코드

		ExcelUtils.writeValue(sheet, writeLine, "Y", getCodeValue("SYNC_DSCD_FEP", intrfcDto.getSyncAsyncDscd(), "ko"));// 동기구분
		if(intrfcDto.getFepDto().getTimeOut() == null) {
			intrfcDto.getFepDto().setTimeOut("10"); 
		}
		ExcelUtils.writeValue(sheet, writeLine, "AA", intrfcDto.getFepDto().getTimeOut());

		ExcelUtils.writeValue(sheet, writeLine, "Z", intrfcDto.getRspsYn());// 응답여부		
		ExcelUtils.writeValue(sheet, writeLine, "AB", intrfcDto.getMsgTrnsfrmYn());// 전문변환여부
		if (intrfcDto.getIntrfcWayCd() != null) {
			if(intrfcDto.getIntrfcWayCd().equals("FILETOFILE"))  {
				ExcelUtils.writeValue(sheet, writeLine, "AF", intrfcDto.getFepDto().getDupFileProc());// 파일 중복 처리 정책
			}
		}
		ExcelUtils.writeValue(sheet, writeLine, "AG", getCodeValue("GEN_CYCLE_CD", intrfcDto.getFepDto().getOccurCycle(), "ko"));
		//ExcelUtils.writeValue(sheet, writeLine, "AK", intrfcDto.getInstCd());
		//ExcelUtils.writeValue(sheet, writeLine, "AL", intrfcDto.getBizCd());
		//ExcelUtils.writeValue(sheet, writeLine, "AM", intrfcDto.getRqstExtrnlMsgNo());
		//ExcelUtils.writeValue(sheet, writeLine, "AN", intrfcDto.getRspsExtrnlMsgNo());
		ExcelUtils.writeValue(sheet, writeLine, "AC", intrfcDto.getFepDto().getSendFileNm());// 파일명
		ExcelUtils.writeValue(sheet, writeLine, "AD", intrfcDto.getFepDto().getSendFilePath());// 송신 경로
		ExcelUtils.writeValue(sheet, writeLine, "AE", intrfcDto.getFepDto().getRecvFilePath());// 수신 경로
		ExcelUtils.writeValue(sheet, writeLine, "AJ", intrfcDto.getRegManId());// 등록자ID
		ExcelUtils.writeValue(sheet, writeLine, "AK", intrfcDto.getRegMamNm());// 등록자이름
		ExcelUtils.writeValue(sheet, writeLine, "AL", intrfcDto.getFepDto().getIntrfDesc());// 비고		
	}

	private void writeInterfaceInfoEAI_E(Sheet sheet, IntrfcInfoExportDto intrfcDto, int writeLine) throws ParseException {
		logger.debug("Interface ID to write in excelfile: {}, EAI_E DTO : {}", intrfcDto.getIntrfcId(), intrfcDto.getEaiDto());
		ExcelUtils.writeValue(sheet, writeLine, "C", intrfcDto.getLv3Cd());// 어플리케이션
		ExcelUtils.writeValue(sheet, writeLine, "D", intrfcDto.getIntrfcId());// 인터페이스아이디
		ExcelUtils.writeValue(sheet, writeLine, "E", intrfcDto.getIntrfcNm());// 인터페이스명
		ExcelUtils.writeValue(sheet, writeLine, "F", intrfcDto.getSysCdSend());// 송신시스템코드
		ExcelUtils.writeValue(sheet, writeLine, "G", intrfcDto.getSysNmSend());//  송신시스템명
		ExcelUtils.writeValue(sheet, writeLine, "H", intrfcDto.getCrgManNmSend());// 송신시스템관리자명
		ExcelUtils.writeValue(sheet, writeLine, "I", intrfcDto.getSysCdReceive());// 수신시스템코드
		ExcelUtils.writeValue(sheet, writeLine, "J", intrfcDto.getSysNmReceive());//  수신시스템명
		ExcelUtils.writeValue(sheet, writeLine, "K", intrfcDto.getCrgManNmReceive());// 수신시스템관리자명				

		// 송신파일속성
		ExcelUtils.writeValue(sheet, writeLine, "L", intrfcDto.getEaiDto().getSendFilePath());// 파일경로
		ExcelUtils.writeValue(sheet, writeLine, "M", intrfcDto.getEaiDto().getSendFileNm());// 파일명
		ExcelUtils.writeValue(sheet, writeLine, "N",
				getCodeValue("TRAN_POST_PROC", intrfcDto.getEaiDto().getSendTranPostProc(), "ko"));// 전송후처리유형
		ExcelUtils.writeValue(sheet, writeLine, "O", intrfcDto.getEaiDto().getSendTranPostBackPath());// 전송후백업경로
		ExcelUtils.writeValue(sheet, writeLine, "P", intrfcDto.getEaiDto().getSendTranBeforeScript());//전송전선행스크립트
		ExcelUtils.writeValue(sheet, writeLine, "Q", getCodeValue("TRAN_BEFORE_SCRIPT_TYPE", intrfcDto.getEaiDto().getSendTranBeforeScriptType(), "ko"));//전송전선행스크립트유형

		// 수신파일속성
		ExcelUtils.writeValue(sheet, writeLine, "R", intrfcDto.getEaiDto().getRecvFilePath());// 파일경로
		ExcelUtils.writeValue(sheet, writeLine, "S", intrfcDto.getEaiDto().getRecvFileNm());// 파일명
		ExcelUtils.writeValue(sheet, writeLine, "T", intrfcDto.getEaiDto().getRecvTranPostFinCreateYn());// 전송후fin파일생성여부
		ExcelUtils.writeValue(sheet, writeLine, "U", intrfcDto.getEaiDto().getRecvTranPostScript());// 전송후후행스크립트
		ExcelUtils.writeValue(sheet, writeLine, "V", getCodeValue("TRAN_POST_SCRIPT_TYPE", intrfcDto.getEaiDto().getRecvTranPostScriptType(), "ko"));//전송후후행스크립트유형

		ExcelUtils.writeValue(sheet, writeLine, "W", intrfcDto.getEaiDto().getPrivacyInclYn());// 개인정보포함여부
		ExcelUtils.writeValue(sheet, writeLine, "X", getCodeValue("BACKUP_CD", intrfcDto.getEaiDto().getBackupAprvStat(), "ko"));// 백업승인상태확인
		ExcelUtils.writeValue(sheet, writeLine, "Y", intrfcDto.getEaiDto().getEncTargetYn());// 암호화대상여부
		ExcelUtils.writeValue(sheet, writeLine, "Z",
				getCodeValue("GEN_CYCLE_CD", intrfcDto.getEaiDto().getOccurCycle(), "ko"));// 발생주기
		ExcelUtils.writeValue(sheet, writeLine, "AA", intrfcDto.getEaiDto().getDayOccurCnt());// 일발생건수		
		ExcelUtils.writeValue(sheet, writeLine, "AB", intrfcDto.getInstCd());// 기관코드
		ExcelUtils.writeValue(sheet, writeLine, "AC", intrfcDto.getEaiDto().getFepTranIntrfcId());// 일괄전송인터페이스ID
		ExcelUtils.writeValue(sheet, writeLine, "AD", intrfcDto.getEaiDto().getRecordSize());//레코드사이즈
		ExcelUtils.writeValue(sheet, writeLine, "AE", getCodeValue("RECORD_SEPARATOR_CD", intrfcDto.getEaiDto().getRecordSeparator(), "ko"));//레코드구분자
		ExcelUtils.writeValue(sheet, writeLine, "AF", intrfcDto.getEaiDto().getReqWrapperDtoNm());// 송신 Wrapper IO명
		ExcelUtils.writeValue(sheet, writeLine, "AG", intrfcDto.getEaiDto().getResWrapperDtoNm());// 수신 Wrapper IO명		

		ExcelUtils.writeValue(sheet, writeLine, "AH", intrfcDto.getRegManId());// 등록자ID
		ExcelUtils.writeValue(sheet, writeLine, "AI", getFormDate(intrfcDto.getRegDttm()));// 등록일자
		ExcelUtils.writeValue(sheet, writeLine, "AJ", getCodeValue("WORK_STATUS_CD", intrfcDto.getWorkStatusCd(), "ko"));// 상태			
		ExcelUtils.writeValue(sheet, writeLine, "AK", intrfcDto.getEaiDto().getCurrIntrfcIdentifier());// 현행인터페이스식별자
		ExcelUtils.writeValue(sheet, writeLine, "AL", intrfcDto.getEaiDto().getIntrfcUse());// 인터페이스용도
		ExcelUtils.writeValue(sheet, writeLine, "AM", intrfcDto.getEaiDto().getIntrfDesc());// 비고

	}

	private void writeInterfaceInfoEAI_I(Sheet sheet, IntrfcInfoExportDto intrfcDto, int writeLine) throws ParseException {
		logger.debug("Interface ID to write in excelfile: {}, EAI_I DTO : {}", intrfcDto.getIntrfcId(), intrfcDto.getEaiDto());
		ExcelUtils.writeValue(sheet, writeLine, "B", intrfcDto.getLv1Cd());// 어플리케이션
		ExcelUtils.writeValue(sheet, writeLine, "C", intrfcDto.getAppCdNm());// 어플리케이션 명
		ExcelUtils.writeValue(sheet, writeLine, "D", intrfcDto.getIntrfcId());// 인터페이스아이디
		ExcelUtils.writeValue(sheet, writeLine, "E", intrfcDto.getIntrfcNm());// 인터페이스명
		ExcelUtils.writeValue(sheet, writeLine, "F", getCodeValue("INTRFC_WAY_CD", intrfcDto.getIntrfcWayCd(), "ko"));// 발생유형
		ExcelUtils.writeValue(sheet, writeLine, "G", getCodeValue("TRAN_DSCD", intrfcDto.getTrxDscd(), "ko"));// 거래유형		
		ExcelUtils.writeValue(sheet, writeLine, "H", intrfcDto.getSysCdSend());// 송신시스템코드
		ExcelUtils.writeValue(sheet, writeLine, "I", intrfcDto.getSysNmSend());//  송신시스템명
		ExcelUtils.writeValue(sheet, writeLine, "J", intrfcDto.getCrgManNmSend());// 송신시스템관리자명
		ExcelUtils.writeValue(sheet, writeLine, "K", intrfcDto.getSysCdReceive());// 수신시스템코드
		ExcelUtils.writeValue(sheet, writeLine, "L", intrfcDto.getSysNmReceive());//  수신시스템명
		ExcelUtils.writeValue(sheet, writeLine, "M", intrfcDto.getCrgManNmReceive());// 수신시스템관리자명
		ExcelUtils.writeValue(sheet, writeLine, "N", intrfcDto.getTrxCdReceive());// 수신시스템거래코드

		// 송신파일속성

		ExcelUtils.writeValue(sheet, writeLine, "T", intrfcDto.getEaiDto().getSendFilePath());// 파일경로
		ExcelUtils.writeValue(sheet, writeLine, "S", intrfcDto.getEaiDto().getSendFileNm());// 파일명
		// 수신파일속성
		ExcelUtils.writeValue(sheet, writeLine, "U", intrfcDto.getEaiDto().getRecvFilePath());// 파일경로
		
		// db속성
		ExcelUtils.writeValue(sheet, writeLine, "Y", intrfcDto.getEaiDto().getSearchProcCnt());// 조회처리건수
		ExcelUtils.writeValue(sheet, writeLine, "Z", intrfcDto.getEaiDto().getErrSkipYn());// 에러skip여부
		ExcelUtils.writeValue(sheet, writeLine, "W", intrfcDto.getEaiDto().getSendDbQuery());// 송신DB쿼리
		ExcelUtils.writeValue(sheet, writeLine, "X", intrfcDto.getEaiDto().getRecvDbQuery());// 수신DBTO 쿼리문

		// 상세정보
		ExcelUtils.writeValue(sheet, writeLine, "O", getCodeValue("SYNC_DSCD", intrfcDto.getSyncAsyncDscd(), "ko"));// 동기구분		
		ExcelUtils.writeValue(sheet, writeLine, "Q", intrfcDto.getEaiDto().getTimeOut());// 타임아웃
		ExcelUtils.writeValue(sheet, writeLine, "P", intrfcDto.getRspsYn());// 응답여부		
		ExcelUtils.writeValue(sheet, writeLine, "R", intrfcDto.getMsgTrnsfrmYn());// 전문변환여부
		ExcelUtils.writeValue(sheet, writeLine, "V",
				getCodeValue("GEN_CYCLE_CD", intrfcDto.getEaiDto().getOccurCycle(), "ko"));// 발생주기

		ExcelUtils.writeValue(sheet, writeLine, "AC", intrfcDto.getRegManId());// 등록자ID	
		ExcelUtils.writeValue(sheet, writeLine, "AD", intrfcDto.getEaiDto().getIntrfDesc());// 비고
	}

	private void writeInterfaceInfoMCI(Sheet sheet, IntrfcInfoExportDto intrfcDto, int writeLine) throws ParseException {
		logger.debug("Interface ID to write in excelfile: {}, MCIDTO : {}", intrfcDto.getIntrfcId(), intrfcDto.getMciDto());
		ExcelUtils.writeValue(sheet, writeLine, "B", intrfcDto.getLv1Cd());// 어플리케이션
		ExcelUtils.writeValue(sheet, writeLine, "C", intrfcDto.getAppCdNm());// 어플리케이션명
		ExcelUtils.writeValue(sheet, writeLine, "D", intrfcDto.getIntrfcId());// 인터페이스아이디
		ExcelUtils.writeValue(sheet, writeLine, "E", intrfcDto.getIntrfcNm());// 인터페이스명
		ExcelUtils.writeValue(sheet, writeLine, "F", intrfcDto.getSysCdSend());// 송신시스템코드
		ExcelUtils.writeValue(sheet, writeLine, "G", intrfcDto.getSysNmSend());//  송신시스템명
		ExcelUtils.writeValue(sheet, writeLine, "H", intrfcDto.getCrgManNmSend());// 송신시스템관리자명
		ExcelUtils.writeValue(sheet, writeLine, "I", intrfcDto.getSysCdReceive());// 수신시스템코드
		ExcelUtils.writeValue(sheet, writeLine, "J", intrfcDto.getSysNmReceive());//  수신시스템명
		ExcelUtils.writeValue(sheet, writeLine, "K", intrfcDto.getCrgManNmReceive());// 수신시스템관리자명
		ExcelUtils.writeValue(sheet, writeLine, "L", intrfcDto.getTrxCdReceive());// 수신시스템거래코드

		ExcelUtils.writeValue(sheet, writeLine, "M", getCodeValue("SYNC_DSCD", intrfcDto.getSyncAsyncDscd(), "ko"));// 동기구분
		// ExcelUtils.writeValue(sheet, writeLine, "N", getCodeValue("TRX_TYPE_DSCD", intrfcDto.getTrxTypeDscd(), "ko"));// 거래유형
		if(intrfcDto.getMciDto().getTimeOut() == null) {
			intrfcDto.getMciDto().setTimeOut("10"); 
		}
		ExcelUtils.writeValue(sheet, writeLine, "O", intrfcDto.getMciDto().getTimeOut());// 타임아웃
		ExcelUtils.writeValue(sheet, writeLine, "N", intrfcDto.getRspsYn());// 응답여부		
		ExcelUtils.writeValue(sheet, writeLine, "P", intrfcDto.getMsgTrnsfrmYn());// 전문변환여부
		ExcelUtils.writeValue(sheet, writeLine, "Q", intrfcDto.getViewId());// 전문변환여부
		ExcelUtils.writeValue(sheet, writeLine, "U", intrfcDto.getRegManId());// 등록자ID
		ExcelUtils.writeValue(sheet, writeLine, "V", intrfcDto.getRegMamNm());// 등록자NAME
//		ExcelUtils.writeValue(sheet, writeLine, "Y", getCodeValue("WORK_STATUS_CD", intrfcDto.getWorkStatusCd(), "ko"));// 상태			
		ExcelUtils.writeValue(sheet, writeLine, "W",
				intrfcDto.getMciDto().getIntrfDesc() == null ? "" : intrfcDto.getMciDto().getIntrfDesc());// 비고	
	}

	private void writeInterfaceInfoCC(Sheet sheet, IntrfcInfoExportDto intrfcDto, int writeLine) throws ParseException {
		logger.debug("Interface ID to write in excelfile: {}, CCDTO : {}", intrfcDto.getIntrfcId(), intrfcDto.getCcDto());
		ExcelUtils.writeValue(sheet, writeLine, "C", intrfcDto.getLv3Cd());// 어플리케이션
		ExcelUtils.writeValue(sheet, writeLine, "D", intrfcDto.getIntrfcId());// 인터페이스아이디
		ExcelUtils.writeValue(sheet, writeLine, "E", intrfcDto.getIntrfcNm());// 인터페이스명
		ExcelUtils.writeValue(sheet, writeLine, "F", intrfcDto.getSysCdSend());// 송신시스템코드
		ExcelUtils.writeValue(sheet, writeLine, "G", intrfcDto.getSysNmSend());//  송신시스템명
		ExcelUtils.writeValue(sheet, writeLine, "H", intrfcDto.getCrgManNmSend());// 송신시스템관리자명
		ExcelUtils.writeValue(sheet, writeLine, "I", intrfcDto.getSysCdReceive());// 수신시스템코드
		ExcelUtils.writeValue(sheet, writeLine, "J", intrfcDto.getSysNmReceive());//  수신시스템명
		ExcelUtils.writeValue(sheet, writeLine, "K", intrfcDto.getCrgManNmReceive());// 수신시스템관리자명
		ExcelUtils.writeValue(sheet, writeLine, "L", intrfcDto.getTrxCdReceive());// 수신시스템거래코드

		ExcelUtils.writeValue(sheet, writeLine, "M", getCodeValue("TRAN_DSCD", intrfcDto.getTrxDscd(), "ko"));// 발생유형
		ExcelUtils.writeValue(sheet, writeLine, "N", getCodeValue("TRX_TYPE_DSCD_CC", intrfcDto.getTrxTypeDscd(), "ko"));// 거래유형
		ExcelUtils.writeValue(sheet, writeLine, "O", intrfcDto.getRegManId());// 등록자ID
		ExcelUtils.writeValue(sheet, writeLine, "P", getFormDate(intrfcDto.getRegDttm()));// 등록일자
		ExcelUtils.writeValue(sheet, writeLine, "Q", getCodeValue("WORK_STATUS_CD", intrfcDto.getWorkStatusCd(), "ko"));// 상태	
		if (intrfcDto.getCcDto() != null) {
			ExcelUtils.writeValue(sheet, writeLine, "S",
					intrfcDto.getCcDto().getIntrfDesc() == null ? "" : intrfcDto.getCcDto().getIntrfDesc());// 비고	
		}
	}
	
	private void copyFile(File src, File target) {
		InputStream in = null ;
		OutputStream out = null ;
		
		try {
			in = new FileInputStream(src) ;
			out = new FileOutputStream(target) ;
			
			byte[] byteFile = new byte[1024] ;
			int length ;
			
			while((length = in.read(byteFile)) > 0) {
				out.write(byteFile, 0, length);
			}
		} catch (FileNotFoundException e) {
			logger.error("{}", e);
			throw new ServiceException(BxMessages.Error.DRM_EXCEPTION);
		} catch (IOException e) {
			logger.error("{}", e);
			throw new ServiceException(BxMessages.Error.DRM_EXCEPTION);
		}
		
	}

	@Override
	protected Workbook createWorkbook(Map<String, Object> model, HttpServletRequest request) {
		String intrfcType = (String) model.get("intrfcType");

		String rootPath = request.getSession().getServletContext().getRealPath("/");
		logger.debug("----root Path----- : {}", rootPath);

		String uuid = null;
		for (int i = 0; i < 10; i++) {
			uuid = UUID.randomUUID().toString();
			uuid = uuid.replace("-", "");
		}

		String templateFile = rootPath + STR_TEMPLATE_FILE_PATH +
				STR_TEMPLATE_FILENAME_PREFIX + intrfcType + ".xlsx";
		String filenameToWrite = rootPath + STR_TEMPLATE_FILE_PATH + "temp/" +
				STR_TEMPLATE_FILENAME_PREFIX + intrfcType + "_" + uuid + ".xlsx";
		
//		String templateFile =  STR_TEMPLATE_FILE_PATH +
//				STR_TEMPLATE_FILENAME_PREFIX + intrfcType + ".xlsx";
//		String filenameToWrite =  STR_TEMPLATE_FILE_PATH + "temp/" +
//				STR_TEMPLATE_FILENAME_PREFIX + intrfcType + "_" + uuid + ".xlsx";

		Workbook workbook = null;
		logger.debug("Org File : {}", templateFile);

		try {
			File decrypted = new File(templateFile);

			if (!decrypted.exists()) {
				decrypted = new File(templateFile);
			}
			logger.debug("decrypted file : {}", decrypted.getName());
			File toWrite = new File(filenameToWrite);
			Files.copy(decrypted, toWrite);

			File decryptedInputFile = Optional.ofNullable(toWrite).filter(File::exists)
					.filter(File::canRead).orElseThrow(() -> new IOException(templateFile));

			logger.debug("Template file path : {}", templateFile);
			workbook = new XSSFWorkbook(decryptedInputFile);
		} catch (EncryptedDocumentException e) {
			logger.error("{}", e);
			throw new ServiceException(BxMessages.Error.DRM_EXCEPTION);
		} catch (InvalidFormatException e) {
			logger.error("{}", e);
			throw new ServiceException(BxMessages.Error.DRM_EXCEPTION);
		} catch (IOException e) {
			logger.error("{}", e);
			throw new ServiceException(BxMessages.Error.DRM_EXCEPTION);
		} 
		return workbook;
	}

	private String getCodeValue(String codeId, String codeValue, String locale) {

		String codeValueNm = ServiceContext.getCodeValue(codeId, codeValue, locale);
		if (codeValueNm == null) {
			return "";
		}

		return codeValueNm;
	}

	private String getFormDate(String flatDate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
		String formDate = new SimpleDateFormat("yyyy-MM-dd").format(sdf.parse(flatDate));
		return formDate;
	}
}
