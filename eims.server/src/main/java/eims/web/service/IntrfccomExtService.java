/**
 * 
 */
package eims.web.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import eims.web.constants.BxMessages;
import eims.web.constants.ExcelLayout;
import eims.web.dao.BizcdDao;
import eims.web.dao.ExtrnlinstcdDao;
import eims.web.dao.IntrfccombsDao;
import eims.web.dto.IntrfcFileImportErrInfo;
import eims.web.dto.IntrfcInfoExportDto;
import eims.web.dto.table.ExtrnlinstcdDto;
import eims.web.dto.table.IntrfccombsDto;
import eims.web.dto.table.IntrfccombsRawDataDto;
import eims.web.excel.drm.DrmUtil;
import eims.web.excel.drm.exception.FasooDrmException;
import eims.web.excel.drm.exception.FileTypeException;
import eims.web.exception.ServiceException;
import eims.web.utils.ExcelUtils;
import eims.web.utils.JsonUtils;
import eims.web.utils.UidUtils;

/**
 * @author 20180032
 * 
 */
@Service
public class IntrfccomExtService {

	private static final String ONL_BAT_DSCD_ONLINE = "온라인";
	private static final String ONL_BAT_DSCD_BATCH = "배치";
	private static final String SEND_TRAN_POST_PROC_BACKUP = "Backup";
	private static final String SYS_CD_BC_GATOR = "CFB";
	private static final String SYS_CD_CARD_CORE_ONL = "COO";
	private static final String SYS_CD_CARD_CORE_BAT = "COB";
	private static final String SYS_CD_CARD_AUTH_BAK = "CAB";
	private static final String SYS_CD_FDS = "FDS";
	private static final String SYS_CD_INTERNET_CORE_BAT = "IOB";
	private static final String SYS_CD_FEP = "FEP";
	private static final String SYS_CD_OPENAPI_GATEWAY = "OAG";
	private static final String SYNC_DSCD_FEP_SYNC = "SYNC";
	private static final String SENC_RECV_DSCD_SEND = "SEND";
	private static final String GEN_CYCLE_CD_FREQ = "FREQUENT";
	private static final String SYS_CD_EXT = "EXT";

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Resource(name="transactionManager")
	protected DataSourceTransactionManager txManager ;
	@Autowired
	private IntrfccomService intrfccomService;
	@Autowired
	private ExtrnlinstcdDao extrnlinstcdDao;
	@Autowired
	private BizcdDao bizCdDao;
	@Autowired
	private IntrfccombsDao intrfccombsDao;

	/* List Interface Definition Info */
	public List<IntrfcInfoExportDto> getIntrfcInfos(String intrfcId, String intrfcNm, String intrfcWayCd, String workStatusCd,
			String regManId, String regDttm, String msgTrnsfrmYn, String trxCd, String bizCd, String instCd,
			String trxDscd, String intrfcTypeCd, String lv1Cd, String lv2Cd, String lv3Cd, String lv4Cd, String lv5Cd,
			String syncAsyncDscd, String srTypeCd, String rqstExtrnlMsgNo, String rspsExtrnlMsgNo, String sysCdS,
			String sysCdR, String msgLayoutId, String trxTypeDscd) {
		List<IntrfcInfoExportDto> out = intrfccombsDao.selectAllIntrfcInfoToExport(intrfcId, intrfcNm, intrfcWayCd, workStatusCd, regManId, regDttm, msgTrnsfrmYn, trxCd, bizCd, instCd, trxDscd, intrfcTypeCd, lv1Cd, lv2Cd, lv3Cd, lv4Cd, lv5Cd, syncAsyncDscd, srTypeCd, rqstExtrnlMsgNo,
				rspsExtrnlMsgNo, sysCdS, sysCdR, msgLayoutId, trxTypeDscd);

		for (IntrfcInfoExportDto intrfcInfoExportDto : out) {
			IntrfccombsRawDataDto rawDataDto = null;
			String jsonRawData = intrfcInfoExportDto.getRawData();
			if (jsonRawData != null && !jsonRawData.equals("")) {
				try {
					rawDataDto = JsonUtils.jsonToObect(jsonRawData, IntrfccombsRawDataDto.class);
				} catch (JsonParseException e) {
					logger.error("{}", e);
					throw new ServiceException(BxMessages.Error.SYSTEM_EXCEPTION);
				} catch (JsonMappingException e) {
					logger.error("{}", e);
					throw new ServiceException(BxMessages.Error.SYSTEM_EXCEPTION);
				} catch (IOException e) {
					logger.error("{}", e);
					throw new ServiceException(BxMessages.Error.SYSTEM_EXCEPTION);
				}
			}

			if (rawDataDto != null) {
				if (intrfcInfoExportDto.getIntrfcTypeCd().equals("MCI")) {
					intrfcInfoExportDto.setMciDto(rawDataDto.getMciDto());
				} else if (intrfcInfoExportDto.getIntrfcTypeCd().equals("CC")) {
					intrfcInfoExportDto.setCcDto(rawDataDto.getCcDto());
				} else if (intrfcInfoExportDto.getIntrfcTypeCd().equals("EAI_I") || intrfcInfoExportDto.getIntrfcTypeCd().equals("EAI_E")) {
					intrfcInfoExportDto.setEaiDto(rawDataDto.getEaiDto());
				} else {
					intrfcInfoExportDto.setFepDto(rawDataDto.getFepDto());
				}
			}
			intrfcInfoExportDto.setRawData(null);
		}
		return out;
	}

	/* Multi File Excel Import */
	public List<IntrfcFileImportErrInfo> importIntrfcFiles(List<MultipartFile> intrfcFiles, String intrfcTypeCd) {
		
		DefaultTransactionDefinition def = new DefaultTransactionDefinition() ;
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus txStatus = txManager.getTransaction(def) ;
		
		if (intrfcFiles == null || intrfcFiles.size() < 1) {
			throw new ServiceException(BxMessages.Error.FILE_NOT_EXISTED);
		}

		List<IntrfcFileImportErrInfo> errInfos = new ArrayList<IntrfcFileImportErrInfo>();
		IntrfcFileImportErrInfo errInfo = null;

		for (int i = 0; i < intrfcFiles.size(); i++) {
			try {
				logger.debug("File to Import : {}", intrfcFiles.get(i).getOriginalFilename());
				errInfo = new IntrfcFileImportErrInfo();
				errInfo.setFileName(intrfcFiles.get(i).getOriginalFilename());
				importIntrfcFile(intrfcFiles.get(i), intrfcTypeCd);
				errInfo.setStatus(0);
				txManager.commit(txStatus);
			} catch (ServiceException e) {
				errInfo.setMessage(e.getMessage());
				errInfo.setParameter(e.getParameter());
				errInfo.setStatus(-1);
				logger.error(e.getMessage());
				txManager.rollback(txStatus);
			} catch (Exception e) {
				errInfo.setMessage(ExceptionUtils.getFullStackTrace(e));
				errInfo.setStatus(-1);
				logger.error(e.getMessage(), e);
				txManager.rollback(txStatus);
			}
			errInfos.add(errInfo);
		}
		return errInfos;
	}
	
	public List<IntrfcFileImportErrInfo> importDefinition(List<MultipartFile> intrfcFiles,String intrfcTypeCd) {
				
		if (intrfcFiles == null || intrfcFiles.size() < 1) {
			throw new ServiceException(BxMessages.Error.FILE_NOT_EXISTED);
		}

		List<IntrfcFileImportErrInfo> errInfos = new ArrayList<IntrfcFileImportErrInfo>();
		IntrfcFileImportErrInfo errInfo = null;

		for (int i = 0; i < intrfcFiles.size(); i++) {
			try {
				logger.debug("File to Import : {}", intrfcFiles.get(i).getOriginalFilename());
				errInfo = new IntrfcFileImportErrInfo();
				errInfo.setFileName(intrfcFiles.get(i).getOriginalFilename());
				importDefinitionFile(intrfcFiles.get(i), intrfcTypeCd);
				errInfo.setStatus(0);
			} catch (ServiceException e) {
				errInfo.setMessage(e.getMessage());
				errInfo.setParameter(e.getParameter());
				errInfo.setStatus(-1);
				logger.error(e.getMessage());
			} catch (Exception e) {
				errInfo.setMessage(ExceptionUtils.getFullStackTrace(e));
				errInfo.setStatus(-1);
				logger.error(e.getMessage(), e);
			}
			errInfos.add(errInfo);
		}
		return errInfos;
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	private int importIntrfcFile(MultipartFile intrfcFile, String intrfcTypeCd) {

		logger.debug("intrfcTypeCd: {}", intrfcTypeCd);

		String uid = UidUtils.genUUID();

		String inFile = "/tmp/" + uid + ".xlsx";
		IntrfccombsDto intrfccombsDto = new IntrfccombsDto();

		Workbook workbook = null;
		try (FileOutputStream out = new FileOutputStream(inFile)) {
			out.write(intrfcFile.getBytes());
			out.close();

			File decryptedInputFile = Optional.ofNullable(new File(inFile)).filter(File::exists).filter(File::canRead)
					.orElseThrow(() -> new IOException(inFile));
			workbook = WorkbookFactory.create(decryptedInputFile);
			Sheet sheet = workbook.getSheetAt(2);
			String title = ExcelUtils.readValue(sheet, 2, "B");
			int titleYn = 0;
			if (intrfcTypeCd.equals("MCI")) {
				titleYn = title.indexOf("MCI");
				if (titleYn < 0) {
					throw new ServiceException(BxMessages.Error.FILE_UPLOAD_INTERFACE_TYPE);
				}
				validateInputMCI(sheet);

				intrfccomService.readMciData(sheet, intrfccombsDto, workbook);

				workbook.close();

			} else if (intrfcTypeCd.equals("CC")) {
				titleYn = title.indexOf("센터컷");
				if (titleYn < 0) {
					throw new ServiceException(BxMessages.Error.FILE_UPLOAD_INTERFACE_TYPE);
				}
				validateInputCC(sheet);

				intrfccomService.readCcData(sheet, intrfccombsDto, workbook);

				workbook.close();
			} else if (intrfcTypeCd.equals("EAI_I") || intrfcTypeCd.equals("EAI_E")) {
				if (intrfcTypeCd.equals("EAI_I")) {
					titleYn = title.indexOf("EAI");
					if (titleYn < 0) {
						throw new ServiceException(BxMessages.Error.FILE_UPLOAD_INTERFACE_TYPE);
					}
					validateInputEAI_I(sheet);
				} else {
					titleYn = title.indexOf("대외파일전송");
					if (titleYn < 0) {
						throw new ServiceException(BxMessages.Error.FILE_UPLOAD_INTERFACE_TYPE);
					}
					validateInputEAI_E(sheet);
				}

				intrfccomService.readEaiData(intrfccombsDto, workbook, intrfcTypeCd);

				workbook.close();
			} else {
				titleYn = title.indexOf("FEP");
				if (titleYn < 0) {
					throw new ServiceException(BxMessages.Error.FILE_UPLOAD_INTERFACE_TYPE);
				}

				StringBuilder errMsg = new StringBuilder();
				boolean isValid = true;

				if (sheet.getSheetName().equals("대외온라인")) {
					isValid = validateInputFEPOnline(sheet, errMsg);
				} else { // "대외배치"
					isValid = validateInputFEPBatch(sheet, errMsg);
				}
				if (!isValid) {
					throw new ServiceException(BxMessages.Error.MSG_VALID_CHECK, errMsg.toString());
				}

				intrfccomService.readFepData(intrfccombsDto, workbook);
				workbook.close();
			}

		} catch (FileNotFoundException e) {
			logger.error("{}", e);
			throw new ServiceException(BxMessages.Error.FILE_NOT_EXISTED);
		} catch (IOException e) {
			logger.error("{}", e);
			throw new ServiceException(BxMessages.Error.SYSTEM_EXCEPTION);
		} catch (EncryptedDocumentException e) {
			logger.error("{}", e);
			throw new ServiceException(BxMessages.Error.SYSTEM_EXCEPTION);
		} catch (InvalidFormatException e) {
			logger.error("{}", e);
			throw new ServiceException(BxMessages.Error.SYSTEM_EXCEPTION);
		} finally {
			new File(inFile).delete();
			if (workbook != null) {
				try {
					workbook.close();
				} catch (IOException e) {
					logger.error("{}", e);
				}
			}
		}
		return 0;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	private int importDefinitionFile(MultipartFile intrfcFile, String intrfcTypeCd) {

		logger.debug("intrfcTypeCd: {}", intrfcTypeCd);

		String uid = UidUtils.genUUID();

//		String inFile = "/tmp/" + uid + ".xlsx";
		String inFile = "C:\\Users\\user\\Desktop\\temp\\mci.xlsx";
		IntrfccombsDto intrfccombsDto = new IntrfccombsDto();

		Workbook workbook = null;
		
		try (FileOutputStream out = new FileOutputStream(inFile)) {
			out.write(intrfcFile.getBytes());
			out.close();

			File decryptedInputFile = Optional.ofNullable(new File(inFile)).filter(File::exists).filter(File::canRead)
					.orElseThrow(() -> new IOException(inFile));
			workbook = WorkbookFactory.create(decryptedInputFile);
			Sheet sheet = null;
		
			if (intrfcTypeCd.equals("MCI")) {
				sheet = workbook.getSheet("Interface List_MCA(D)");
				intrfccomService.readMciDataDummy(sheet, intrfccombsDto, workbook);
			} else if (intrfcTypeCd.equals("EAI_I")) {
				intrfccomService.readEaiDataDummy(intrfccombsDto, workbook, intrfcTypeCd);
			} else if (intrfcTypeCd.equals("FEP")) {
				intrfccomService.readFepDataDummy(intrfccombsDto, workbook,intrfcTypeCd);
			} else {
				
			}
			workbook.close();
		} catch (FileNotFoundException e) {
			logger.error("{}", e);
			throw new ServiceException(BxMessages.Error.FILE_NOT_EXISTED);
		} catch (IOException e) {
			logger.error("{}", e);
			throw new ServiceException(BxMessages.Error.SYSTEM_EXCEPTION);
		} catch (EncryptedDocumentException e) {
			logger.error("{}", e);
			throw new ServiceException(BxMessages.Error.SYSTEM_EXCEPTION);
		} catch (InvalidFormatException e) {
			logger.error("{}", e);
			throw new ServiceException(BxMessages.Error.SYSTEM_EXCEPTION);
		} finally {
			new File(inFile).delete();
			if (workbook != null) {
				try {
					workbook.close();
				} catch (IOException e) {
					logger.error("{}", e);
				}
			}
		}
		return 0;
	}
	
	private boolean validateInputFEPBatch(Sheet sheet, StringBuilder errMsg) {
		boolean isValid = true;

		/* 어플리케이션 Level3 */
		String appCdL3 = ExcelUtils.readValue(sheet, ExcelLayout.FEP_BATCH_L3_ROW, ExcelLayout.FEP_BATCH_L3_COL);
		/* 인터페이스명 */
		String intrfcNm = ExcelUtils.readValue(sheet, ExcelLayout.FEP_BATCH_IFNM_ROW, ExcelLayout.FEP_BATCH_IFNM_COL);
		/* 송수신구분 */
		String srDscd = intrfccomService.getCodeValueNm("SENC_RECV_DSCD", ExcelUtils.readValue(sheet, ExcelLayout.FEP_BATCH_SR_DSCD_ROW, ExcelLayout.FEP_BATCH_SR_DSCD_COL), "ko");
		/* 발생유형 */
		String onlBatDscd = ExcelUtils.readValue(sheet, ExcelLayout.FEP_BATCH_ONL_BAT_DSCD_ROW, ExcelLayout.FEP_BATCH_ONL_BAT_DSCD_COL);
		/* 기관코드 */
		String instCd = ExcelUtils.readValue(sheet, ExcelLayout.FEP_BATCH_INST_CD_ROW, ExcelLayout.FEP_BATCH_INST_CD_COL);
		isValid = checkInstCd(errMsg, instCd);
		/* 기관업무구분코드 */
		String bizCd = ExcelUtils.readValue(sheet, ExcelLayout.FEP_BATCH_BIZ_CD_ROW, ExcelLayout.FEP_BATCH_BIZ_CD_COL);
		isValid = checkBizCd(errMsg, bizCd);
		/* 개인정보포함여부 */
		String privacyInclYn = ExcelUtils.readValue(sheet, ExcelLayout.FEP_BATCH_PRIVACY_YN_ROW, ExcelLayout.FEP_BATCH_PRIVACY_YN_COL);
		/* 암호화대상여부 */
		String encTargetYn = ExcelUtils.readValue(sheet, ExcelLayout.FEP_BATCH_ENC_YN_ROW, ExcelLayout.FEP_BATCH_ENC_YN_COL);
		/* 현행인터페이스식별자 */
		String currIntrfcIdentifier = ExcelUtils.readValue(sheet, ExcelLayout.FEP_BATCH_CURR_INF_ID_ROW, ExcelLayout.FEP_BATCH_CURR_INF_ID_COL);
		/* 인터페이스 용도 */
		String intrfcUse = ExcelUtils.readValue(sheet, ExcelLayout.FEP_BATCH_INF_USE_ROW, ExcelLayout.FEP_BATCH_INF_USE_COL);
		/* (송신시스템)시스템코드 */
		String sendSysCd = ExcelUtils.readValue(sheet, ExcelLayout.FEP_BATCH_SEND_SYS_CD_ROW, ExcelLayout.FEP_BATCH_SEND_SYS_CD_COL);
		/* (송신시스템)담당자 성명 */
		String sendCrgManNm = ExcelUtils.readValue(sheet, ExcelLayout.FEP_BATCH_SEND_CRG_MAN_NM_ROW, ExcelLayout.FEP_BATCH_SEND_CRG_MAN_NM_COL);
		/* (수신시스템)시스템코드 */
		String recvSysCd = ExcelUtils.readValue(sheet, ExcelLayout.FEP_BATCH_RECV_SYS_CD_ROW, ExcelLayout.FEP_BATCH_RECV_SYS_CD_COL);
		/* (수신시스템)담당자 성명 */
		String recvCrgManNm = ExcelUtils.readValue(sheet, ExcelLayout.FEP_BATCH_RECV_CRG_MAN_NM_ROW, ExcelLayout.FEP_BATCH_RECV_CRG_MAN_NM_COL);

		/* FILE ID */
		String fileId = ExcelUtils.readValue(sheet, ExcelLayout.FEP_BATCH_FILE_ID_ROW, ExcelLayout.FEP_BATCH_FILE_ID_COL);
		/* 레코드사이즈 */
		String recordSize = ExcelUtils.readValue(sheet, ExcelLayout.FEP_BATCH_REC_SIZE_ROW, ExcelLayout.FEP_BATCH_REC_SIZE_COL);
		/* 레코드구분자 */
		String recordSeparator = intrfccomService.getCodeValueNm("RECORD_SEPARATOR_CD", ExcelUtils.readValue(sheet, ExcelLayout.FEP_BATCH_REC_SEP_ROW, ExcelLayout.FEP_BATCH_REC_SEP_COL), "ko");
		/* 중복전송허용여부 */
		String dupTrnsmsnAllwYn = ExcelUtils.readValue(sheet, ExcelLayout.FEP_BATCH_DUP_YN_ROW, ExcelLayout.FEP_BATCH_DUP_YN_COL);
		/* 발생주기 */
		String occurCycle = intrfccomService.getCodeValueNm("GEN_CYCLE_CD", ExcelUtils.readValue(sheet, ExcelLayout.FEP_BATCH_OCCUR_CYCLE_ROW, ExcelLayout.FEP_BATCH_OCCUR_CYCLE_COL), "ko");
		/* 송신 파일경로 */
		String sendFilePath = ExcelUtils.readValue(sheet, ExcelLayout.FEP_BATCH_SEND_FILEPATH_ROW, ExcelLayout.FEP_BATCH_SEND_FILEPATH_COL);
		/* 수신 파일경로 */
		String recvFilePath = ExcelUtils.readValue(sheet, ExcelLayout.FEP_BATCH_RECV_FILEPATH_ROW, ExcelLayout.FEP_BATCH_RECV_FILEPATH_COL);
		/* 파일명 */
		String fileNm = ExcelUtils.readValue(sheet, ExcelLayout.FEP_BATCH_FILE_NAME_ROW, ExcelLayout.FEP_BATCH_FILE_NAME_COL);
		/* 중복파일처리방법 */
		String dupFileProcMode = intrfccomService.getCodeValueNm("DUP_FILE_PROC_CD", ExcelUtils.readValue(sheet, ExcelLayout.FEP_BATCH_DUP_FILE_PROC_MODE_ROW, ExcelLayout.FEP_BATCH_DUP_FILE_PROC_MODE_COL), "ko");

		isValid = intrfccomService.nullCheck(errMsg, appCdL3, "어플리케이션 Level3", isValid);
		isValid = intrfccomService.nullCheck(errMsg, intrfcNm, "인터페이스명", isValid);
		isValid = intrfccomService.nullCheck(errMsg, srDscd, "송수신구분", isValid);
		isValid = intrfccomService.nullCheck(errMsg, onlBatDscd, "발생유형", isValid);
		isValid = intrfccomService.nullCheck(errMsg, instCd, "기관코드", isValid);
		isValid = intrfccomService.nullCheck(errMsg, bizCd, "기관업무구분코드", isValid);
		isValid = intrfccomService.nullCheck(errMsg, privacyInclYn, "개인정보포함여부", isValid);
		isValid = intrfccomService.nullCheck(errMsg, encTargetYn, "암호화대상여부", isValid);
		isValid = intrfccomService.nullCheck(errMsg, sendSysCd, "송신시스템 시스템코드", isValid);
		if (!sendSysCd.equals(SYS_CD_EXT)) {
			isValid = intrfccomService.nullCheck(errMsg, sendCrgManNm, "송신시스템 담당자 성명", isValid);
		}
		isValid = intrfccomService.nullCheck(errMsg, recvSysCd, "수신시스템 시스템코드", isValid);
		if (!recvSysCd.equals(SYS_CD_EXT)) {
			isValid = intrfccomService.nullCheck(errMsg, recvCrgManNm, "수신시스템 담당자 성명", isValid);
		}
		isValid = intrfccomService.nullCheck(errMsg, currIntrfcIdentifier, "현행인터페이스식별자", isValid);
//		isValid = intrfccomService.nullCheck(errMsg, intrfcUse, "인터페이스 용도", isValid);

		isValid = intrfccomService.nullCheck(errMsg, fileId, "FILE ID", isValid);
		isValid = intrfccomService.nullCheck(errMsg, recordSize, "레코드사이즈", isValid);
		isValid = intrfccomService.nullCheck(errMsg, recordSeparator, "레코드구분자", isValid);
		isValid = intrfccomService.nullCheck(errMsg, dupTrnsmsnAllwYn, "중복전송허용여부", isValid);
		isValid = intrfccomService.nullCheck(errMsg, occurCycle, "발생주기", isValid);
//		isValid = intrfccomService.nullCheck(errMsg, sendFilePath, "송신 파일경로", isValid);
//		isValid = intrfccomService.nullCheck(errMsg, recvFilePath, "수신 파일경로", isValid);
		isValid = intrfccomService.nullCheck(errMsg, fileNm, "파일명", isValid);
//		isValid = intrfccomService.nullCheck(errMsg, dupFileProcMode, "중복파일처리방법", isValid);
		return isValid;
	}

	private boolean checkBizCd(StringBuilder errMsg, String bizCd) {
		boolean isValid = true;

		if (bizCd != null && !bizCd.equals("")) {
			if (bizCdDao.selectBizcd(bizCd) == null) {
				errMsg.append("\n존재하지않는 기관업무코드입니다. ");
				isValid = false;
			}
		}
		return isValid;
	}

	private boolean checkInstCd(StringBuilder errMsg, String instCd) {
		boolean isValid = true;
		ExtrnlinstcdDto extrnlinstcdDto = extrnlinstcdDao.selectExtrnlinstcd(instCd, "INST");

		if (instCd != null && !instCd.equals("")) {
			if (extrnlinstcdDto == null) {
				errMsg.append("\n존재하지않는 기관코드입니다. ");
				isValid = false;
			}
		}
		return isValid;
	}

	private boolean validateInputFEPOnline(Sheet sheet, StringBuilder errMsg) {
		boolean isValid = true;

		/* 어플리케이션 Level3 */
		String appCdL3 = ExcelUtils.readValue(sheet, ExcelLayout.FEP_ONLINE_L3_ROW, ExcelLayout.FEP_ONLINE_L3_COL);
		/* 인터페이스명 */
		String intrfcNm = ExcelUtils.readValue(sheet, ExcelLayout.FEP_ONLINE_IFNM_ROW, ExcelLayout.FEP_ONLINE_IFNM_COL);
		/* 송수신구분 */
		String srDscd = intrfccomService.getCodeValueNm("SENC_RECV_DSCD", ExcelUtils.readValue(sheet, ExcelLayout.FEP_ONLINE_SR_DSCD_ROW, ExcelLayout.FEP_ONLINE_SR_DSCD_COL), "ko");
		/* 발생유형 */
		String onlBatDscd = ExcelUtils.readValue(sheet, ExcelLayout.FEP_ONLINE_ONL_BAT_DSCD_ROW, ExcelLayout.FEP_ONLINE_ONL_BAT_DSCD_COL);
		/* 기관코드 */
		String instCd = ExcelUtils.readValue(sheet, ExcelLayout.FEP_ONLINE_INST_CD_ROW, ExcelLayout.FEP_ONLINE_INST_CD_COL);
		isValid = checkInstCd(errMsg, instCd);
		/* 기관업무구분코드 */
		String bizCd = ExcelUtils.readValue(sheet, ExcelLayout.FEP_ONLINE_BIZ_CD_ROW, ExcelLayout.FEP_ONLINE_BIZ_CD_COL);
		isValid = checkBizCd(errMsg, bizCd);
		/* 개인정보포함여부 */
		String privacyInclYn = ExcelUtils.readValue(sheet, ExcelLayout.FEP_ONLINE_PRIVACY_YN_ROW, ExcelLayout.FEP_ONLINE_PRIVACY_YN_COL);
		/* 암호화대상여부 */
		String encTargetYn = ExcelUtils.readValue(sheet, ExcelLayout.FEP_ONLINE_ENC_YN_ROW, ExcelLayout.FEP_ONLINE_ENC_YN_COL);
		/* 현행인터페이스식별자 */
		String currIntrfcIdentifier = ExcelUtils.readValue(sheet, ExcelLayout.FEP_ONLINE_CURR_INF_ID_ROW, ExcelLayout.FEP_ONLINE_CURR_INF_ID_COL);
		/* 인터페이스 용도 */
		String intrfcUse = ExcelUtils.readValue(sheet, ExcelLayout.FEP_ONLINE_INF_USE_ROW, ExcelLayout.FEP_ONLINE_INF_USE_COL);
		/* (송신시스템)시스템코드 */
		String sendSysCd = ExcelUtils.readValue(sheet, ExcelLayout.FEP_ONLINE_SEND_SYS_CD_ROW, ExcelLayout.FEP_ONLINE_SEND_SYS_CD_COL);
		/* (송신시스템)담당자 성명 */
		String sendCrgManNm = ExcelUtils.readValue(sheet, ExcelLayout.FEP_ONLINE_SEND_CRG_MAN_NM_ROW, ExcelLayout.FEP_ONLINE_SEND_CRG_MAN_NM_COL);
		/* (수신시스템)시스템코드 */
		String recvSysCd = ExcelUtils.readValue(sheet, ExcelLayout.FEP_ONLINE_RECV_SYS_CD_ROW, ExcelLayout.FEP_ONLINE_RECV_SYS_CD_COL);
		/* (수신시스템)담당자 성명 */
		String recvCrgManNm = ExcelUtils.readValue(sheet, ExcelLayout.FEP_ONLINE_RECV_CRG_MAN_NM_ROW, ExcelLayout.FEP_ONLINE_RECV_CRG_MAN_NM_COL);

		/* 동기구분 */
		String syncAsyncDscd = intrfccomService.getCodeValueNm("SYNC_DSCD_FEP", ExcelUtils.readValue(sheet, ExcelLayout.FEP_ONLINE_SYNC_ASYNC_DSCD_ROW, ExcelLayout.FEP_ONLINE_SYNC_ASYNC_DSCD_COL), "ko");
		/* Timeout 시간 */
		String timeoutSec = ExcelUtils.readValue(sheet, ExcelLayout.FEP_ONLINE_TIMEOUT_ROW, ExcelLayout.FEP_ONLINE_TIMEOUT_COL);
		/* Timeout 처리방법 */
		String timeoutHdl = intrfccomService.getCodeValueNm("TIMEOUT_PROC_CD", ExcelUtils.readValue(sheet, ExcelLayout.FEP_ONLINE_TIMEOUT_PROC_ROW, ExcelLayout.FEP_ONLINE_TIMEOUT_PROC_COL), "ko");
		/* 지연응답여부 */
		String delayRspsYn = ExcelUtils.readValue(sheet, ExcelLayout.FEP_ONLINE_DELAY_RSPS_YN_ROW, ExcelLayout.FEP_ONLINE_DELAY_RSPS_YN_COL);
		/* 전송에러응답여부 */
		String trnsmsnErrResYn = ExcelUtils.readValue(sheet, ExcelLayout.FEP_ONLINE_ERR_RES_YN_ROW, ExcelLayout.FEP_ONLINE_ERR_RES_YN_COL);
		/* 요청전문번호 */
		String rqstExtrnlMsgNo = ExcelUtils.readValue(sheet, ExcelLayout.FEP_ONLINE_RQST_MSG_NO_ROW, ExcelLayout.FEP_ONLINE_RQST_MSG_NO_COL);
		/* 응답전문번호 */
		String rspsExtrnlMsgNo = ExcelUtils.readValue(sheet, ExcelLayout.FEP_ONLINE_RSPS_MSG_NO_ROW, ExcelLayout.FEP_ONLINE_RSPS_MSG_NO_COL);
		/* 거래코드 */
		String trxCd = ExcelUtils.readValue(sheet, ExcelLayout.FEP_ONLINE_TRX_CD_ROW, ExcelLayout.FEP_ONLINE_TRX_CD_COL);
		/* FEP온라인래퍼IO */
		String reqWrapperIo = ExcelUtils.readValue(sheet, ExcelLayout.FEP_ONLINE_REQ_WRAPPER_IO_ROW, ExcelLayout.FEP_ONLINE_REQ_WRAPPER_IO_COL);
		String resWrapperIo = ExcelUtils.readValue(sheet, ExcelLayout.FEP_ONLINE_RES_WRAPPER_IO_ROW, ExcelLayout.FEP_ONLINE_RES_WRAPPER_IO_COL);

		isValid = intrfccomService.nullCheck(errMsg, appCdL3, "어플리케이션 Level3", isValid);
		isValid = intrfccomService.nullCheck(errMsg, intrfcNm, "인터페이스명", isValid);
		isValid = intrfccomService.nullCheck(errMsg, srDscd, "송수신구분", isValid);
		isValid = intrfccomService.nullCheck(errMsg, onlBatDscd, "발생유형", isValid);
		isValid = intrfccomService.nullCheck(errMsg, instCd, "기관코드", isValid);
		isValid = intrfccomService.nullCheck(errMsg, bizCd, "기관업무구분코드", isValid);
		isValid = intrfccomService.nullCheck(errMsg, privacyInclYn, "개인정보포함여부", isValid);
		isValid = intrfccomService.nullCheck(errMsg, encTargetYn, "암호화대상여부", isValid);
		isValid = intrfccomService.nullCheck(errMsg, sendSysCd, "송신시스템 시스템코드", isValid);
		if (!sendSysCd.equals(SYS_CD_EXT)) {
			isValid = intrfccomService.nullCheck(errMsg, sendCrgManNm, "송신시스템 담당자 성명", isValid);
		}
		isValid = intrfccomService.nullCheck(errMsg, recvSysCd, "수신시스템 시스템코드", isValid);
		if (!recvSysCd.equals(SYS_CD_EXT)) {
			isValid = intrfccomService.nullCheck(errMsg, recvCrgManNm, "수신시스템 담당자 성명", isValid);
		}
		isValid = intrfccomService.nullCheck(errMsg, currIntrfcIdentifier, "현행인터페이스식별자", isValid);
//		isValid = intrfccomService.nullCheck(errMsg, intrfcUse, "인터페이스 용도", isValid);
		isValid = intrfccomService.nullCheck(errMsg, syncAsyncDscd, "동기구분", isValid);
		if (srDscd.equals(SENC_RECV_DSCD_SEND)) {
			isValid = intrfccomService.nullCheck(errMsg, trnsmsnErrResYn, "전송에러응답여부", isValid);

			if (syncAsyncDscd.equals(SYNC_DSCD_FEP_SYNC)) {
				isValid = intrfccomService.nullCheck(errMsg, timeoutSec, "Timeout 시간", isValid);
				isValid = intrfccomService.nullCheck(errMsg, timeoutHdl, "Timeout 처리방법", isValid);
				isValid = intrfccomService.nullCheck(errMsg, delayRspsYn, "지연응답여부", isValid);
			}
		}
		isValid = intrfccomService.nullCheck(errMsg, rqstExtrnlMsgNo, "요청전문번호", isValid);
		isValid = intrfccomService.nullCheck(errMsg, reqWrapperIo, "요청WrapperIo", isValid);
		if (rspsExtrnlMsgNo != null && !rspsExtrnlMsgNo.equals("")) {
			isValid = intrfccomService.nullCheck(errMsg, resWrapperIo, "응답WrapperIo", isValid);
		}
//		isValid = intrfccomService.nullCheck(errMsg, rspsExtrnlMsgNo, "응답전문번호", isValid);
		if (!recvSysCd.equals(SYS_CD_EXT)) {
			isValid = intrfccomService.nullCheck(errMsg, trxCd, "거래코드", isValid);
		}

		return isValid;
	}

	private void validateInputEAI_E(Sheet sheet) {
		boolean isValid = true;
		StringBuilder errMsg = new StringBuilder();

		/* 어플리케이션 Level3 */
		String appCdL3 = ExcelUtils.readValue(sheet, ExcelLayout.EAI_EXTFILE_L3_ROW, ExcelLayout.EAI_EXTFILE_L3_COL);
		/* 인터페이스명 */
		String intrfcNm = ExcelUtils.readValue(sheet, ExcelLayout.EAI_EXTFILE_IFNM_ROW, ExcelLayout.EAI_EXTFILE_IFNM_COL);
		/* 기관코드 */
		String instCd = ExcelUtils.readValue(sheet, ExcelLayout.EAI_EXTFILE_INSTCD_ROW, ExcelLayout.EAI_EXTFILE_INSTCD_COL);
		isValid = checkInstCd(errMsg, instCd);
		/* 발생주기 */
		String occurCycle = intrfccomService.getCodeValueNm("GEN_CYCLE_CD", ExcelUtils.readValue(sheet, ExcelLayout.EAI_EXTFILE_OCCUR_CYCLE_ROW, ExcelLayout.EAI_EXTFILE_OCCUR_CYCLE_COL), "ko");
		/* 일 발생건수 */
		String dayOccurCnt = ExcelUtils.readValue(sheet, ExcelLayout.EAI_EXTFILE_DAY_OCCUR_CNT_ROW, ExcelLayout.EAI_EXTFILE_DAY_OCCUR_CNT_COL);
		/* (송신시스템)시스템코드 */
		String sendSysCd = ExcelUtils.readValue(sheet, ExcelLayout.EAI_EXTFILE_SEND_SYS_CD_ROW, ExcelLayout.EAI_EXTFILE_SEND_SYS_CD_COL);
		/* (송신시스템)담당자 성명 */
		String sendCrgManNm = ExcelUtils.readValue(sheet, ExcelLayout.EAI_EXTFILE_SEND_CRG_MAN_NM_ROW, ExcelLayout.EAI_EXTFILE_SEND_CRG_MAN_NM_COL);
		/* (수신시스템)시스템코드 */
		String recvSysCd = ExcelUtils.readValue(sheet, ExcelLayout.EAI_EXTFILE_RECV_SYS_CD_ROW, ExcelLayout.EAI_EXTFILE_RECV_SYS_CD_COL);
		/* (수신시스템)담당자 성명 */
		String recvCrgManNm = ExcelUtils.readValue(sheet, ExcelLayout.EAI_EXTFILE_RECV_CRG_MAN_NM_ROW, ExcelLayout.EAI_EXTFILE_RECV_CRG_MAN_NM_COL);
		/* 개인정보포함여부 */
		String privacyInclYn = ExcelUtils.readValue(sheet, ExcelLayout.EAI_EXTFILE_PRIVACY_YN_ROW, ExcelLayout.EAI_EXTFILE_PRIVACY_YN_COL);
		/* 암호화대상여부 */
		String encTargetYn = ExcelUtils.readValue(sheet, ExcelLayout.EAI_EXTFILE_ENC_YN_ROW, ExcelLayout.EAI_EXTFILE_ENC_YN_COL);
		/* 현행인터페이스식별자 */
		String currIntrfcIdentifier = ExcelUtils.readValue(sheet, ExcelLayout.EAI_EXTFILE_CURR_INF_ID_ROW, ExcelLayout.EAI_EXTFILE_CURR_INF_ID_COL);
		/* 인터페이스 용도 */
		String intrfcUse = ExcelUtils.readValue(sheet, ExcelLayout.EAI_EXTFILE_INF_USE_ROW, ExcelLayout.EAI_EXTFILE_INF_USE_COL);

		/* 송신 파일경로 */
		String sendFilePath = ExcelUtils.readValue(sheet, ExcelLayout.EAI_EXTFILE_SEND_FILEPATH_ROW, ExcelLayout.EAI_EXTFILE_SEND_FILEPATH_COL);
		/* 송신 파일명 */
		String sendFileNm = ExcelUtils.readValue(sheet, ExcelLayout.EAI_EXTFILE_SEND_FILENM_ROW, ExcelLayout.EAI_EXTFILE_SEND_FILENM_COL);
		/* 송신 파일 전송후처리유형 */
		String sendTranPostProc = intrfccomService.getCodeValueNm("TRAN_POST_PROC", ExcelUtils.readValue(sheet, ExcelLayout.EAI_EXTFILE_SEND_TRAN_POST_PROC_ROW, ExcelLayout.EAI_EXTFILE_SEND_TRAN_POST_PROC_COL), "ko");
		/* 송신 파일 전송후백업경로 */
		String sendTranPostBackPath = ExcelUtils.readValue(sheet, ExcelLayout.EAI_EXTFILE_SEND_TRAN_POST_BACK_PATH_ROW, ExcelLayout.EAI_EXTFILE_SEND_TRAN_POST_BACK_PATH_COL);

		/* 수신 파일경로 */
		String recvFilePath = ExcelUtils.readValue(sheet, ExcelLayout.EAI_EXTFILE_RECV_FILEPATH_ROW, ExcelLayout.EAI_EXTFILE_RECV_FILEPATH_COL);
		/* 수신 파일명 */
		String recvFileNm = ExcelUtils.readValue(sheet, ExcelLayout.EAI_EXTFILE_RECV_FILENAME_ROW, ExcelLayout.EAI_EXTFILE_RECV_FILENAME_COL);
		/* 수신 파일 전송후핀파일생성여부 */
		String recvTranPostFinCreateYn = ExcelUtils.readValue(sheet, ExcelLayout.EAI_EXTFILE_RECV_TRAN_POST_FIN_YN_ROW, ExcelLayout.EAI_EXTFILE_RECV_TRAN_POST_FIN_YN_COL);

		isValid = intrfccomService.nullCheck(errMsg, appCdL3, "어플리케이션 Level3", isValid);
		isValid = intrfccomService.nullCheck(errMsg, intrfcNm, "인터페이스명", isValid);
		isValid = intrfccomService.nullCheck(errMsg, instCd, "기관코드", isValid);
		isValid = intrfccomService.nullCheck(errMsg, occurCycle, "발생주기", isValid);
		/* 발생주기 수시인 경우 필수 */
		if (occurCycle.equals(GEN_CYCLE_CD_FREQ)) {
			isValid = intrfccomService.nullCheck(errMsg, dayOccurCnt, "일 발생건수", isValid);
		}
		isValid = intrfccomService.nullCheck(errMsg, sendSysCd, "송신시스템 시스템코드", isValid);
		isValid = intrfccomService.nullCheck(errMsg, sendCrgManNm, "송신시스템 담당자 성명", isValid);
		isValid = intrfccomService.nullCheck(errMsg, recvSysCd, "수신시스템 시스템코드", isValid);
		isValid = intrfccomService.nullCheck(errMsg, recvCrgManNm, "수신시스템 담당자 성명", isValid);
		isValid = intrfccomService.nullCheck(errMsg, privacyInclYn, "개인정보포함여부", isValid);
		isValid = intrfccomService.nullCheck(errMsg, encTargetYn, "암호화대상여부", isValid);
		isValid = intrfccomService.nullCheck(errMsg, currIntrfcIdentifier, "현행인터페이스식별자", isValid);
//		isValid = intrfccomService.nullCheck(errMsg, intrfcUse, "인터페이스 용도", isValid);

		if (isNextGenSystem(sendSysCd)) {
			if (!sendSysCd.equals(SYS_CD_BC_GATOR)) {
				isValid = intrfccomService.nullCheck(errMsg, sendFilePath, "송신 파일경로", isValid);
			}
			isValid = intrfccomService.nullCheck(errMsg, sendFileNm, "송신 파일명", isValid);
			if (sendSysCd.equals(SYS_CD_CARD_CORE_BAT)) {
				isValid = intrfccomService.nullCheck(errMsg, sendTranPostProc, "송신 파일 전송후처리유형", isValid);
			}
			if (sendTranPostProc.equals(SEND_TRAN_POST_PROC_BACKUP)) {
				isValid = intrfccomService.nullCheck(errMsg, sendTranPostBackPath, "송신 파일 전송후백업경로", isValid);
			}
		}
		if (isNextGenSystem(recvSysCd)) {
			if (recvSysCd.equals(SYS_CD_BC_GATOR)) {
				isValid = intrfccomService.nullCheck(errMsg, recvFilePath, "수신 파일경로", isValid);
			}
			isValid = intrfccomService.nullCheck(errMsg, recvFileNm, "수신 파일명", isValid);
			if (recvSysCd.equals(SYS_CD_CARD_CORE_BAT)) {
				isValid = intrfccomService.nullCheck(errMsg, recvTranPostFinCreateYn, "수신 파일 전송후핀파일생성여부", isValid);
			}
		}

		if (!isValid) {
			throw new ServiceException(BxMessages.Error.MSG_VALID_CHECK, errMsg.toString());
		}
	}

	private void validateInputEAI_I(Sheet sheet) {
		boolean isValid = true;
		StringBuilder errMsg = new StringBuilder();

		if (sheet.getSheetName().equals("대내온라인_APTOAP")) {
			/* 어플리케이션 Level3 */
			String appCdL3 = ExcelUtils.readValue(sheet, ExcelLayout.EAI_APTOAP_L3_ROW, ExcelLayout.EAI_APTOAP_L3_COL);
			/* 인터페이스명 */
			String intrfcNm = ExcelUtils.readValue(sheet, ExcelLayout.EAI_APTOAP_IFNM_ROW, ExcelLayout.EAI_APTOAP_IFNM_COL);
			/* 발생유형 */
			String onlBatDscd = ExcelUtils.readValue(sheet, ExcelLayout.EAI_APTOAP_ONL_BAT_DSCD_ROW, ExcelLayout.EAI_APTOAP_ONL_BAT_DSCD_COL);
			/* 인터페이스 방식(거래유형) */
			String intrfcWayCd = intrfccomService.getCodeValueNm("INTRFC_WAY_CD", ExcelUtils.readValue(sheet, ExcelLayout.EAI_APTOAP_INF_TYPE_ROW, ExcelLayout.EAI_APTOAP_INF_TYPE_COL), "ko");
			/* 동기구분 */
			String syncAsyncDscd = intrfccomService.getCodeValueNm("SYNC_DSCD", ExcelUtils.readValue(sheet, ExcelLayout.EAI_APTOAP_SYNC_DSCD_ROW, ExcelLayout.EAI_APTOAP_SYNC_DSCD_COL), "ko");
			/* 응답여부 */
			String rspsYn = ExcelUtils.readValue(sheet, ExcelLayout.EAI_APTOAP_RESP_YN_ROW, ExcelLayout.EAI_APTOAP_RESP_YN_COL);
			/* 발생주기 */
			String occurCycle = intrfccomService.getCodeValueNm("GEN_CYCLE_CD", ExcelUtils.readValue(sheet, ExcelLayout.EAI_APTOAP_GEN_CYCLE_CD_ROW, ExcelLayout.EAI_APTOAP_GEN_CYCLE_CD_COL), "ko");
			/* 일 발생건수 */
			String dayOccurCnt = ExcelUtils.readValue(sheet, ExcelLayout.EAI_APTOAP_OCCUR_CNT_ROW, ExcelLayout.EAI_APTOAP_OCCUR_CNT_COL);
			/* (송신시스템)시스템코드 */
			String sendSyscd = ExcelUtils.readValue(sheet, ExcelLayout.EAI_APTOAP_SEND_SYS_CD_ROW, ExcelLayout.EAI_APTOAP_SEND_SYS_CD_COL);
			/* (송신시스템)담당자 성명 */
			String sendCrgManNm = ExcelUtils.readValue(sheet, ExcelLayout.EAI_APTOAP_SEND_CRG_MAN_NM_ROW, ExcelLayout.EAI_APTOAP_SEND_CRG_MAN_NM_COL);
			/* (수신시스템)시스템코드 */
			String recvSysCd = ExcelUtils.readValue(sheet, ExcelLayout.EAI_APTOAP_RECV_SYS_CD_ROW, ExcelLayout.EAI_APTOAP_RECV_SYS_CD_COL);
			/* (수신시스템)담당자 성명 */
			String recvCrgManNm = ExcelUtils.readValue(sheet, ExcelLayout.EAI_APTOAP_RECV_CRG_MAN_NM_ROW, ExcelLayout.EAI_APTOAP_RECV_CRG_MAN_NM_COL);
			/* 개인정보포함여부 */
			String privacyInclYn = ExcelUtils.readValue(sheet, ExcelLayout.EAI_APTOAP_PRIVACY_YN_ROW, ExcelLayout.EAI_APTOAP_PRIVACY_YN_COL);
			/* 암호화대상여부 */
			String encTargetYn = ExcelUtils.readValue(sheet, ExcelLayout.EAI_APTOAP_ENC_YN_ROW, ExcelLayout.EAI_APTOAP_ENC_YN_COL);
			/* 현행인터페이스식별자 */
			String currIntrfcIdentifier = ExcelUtils.readValue(sheet, ExcelLayout.EAI_APTOAP_CURR_INF_ID_ROW, ExcelLayout.EAI_APTOAP_CURR_INF_ID_COL);
			/* 인터페이스 용도 */
			String intrfcUse = ExcelUtils.readValue(sheet, ExcelLayout.EAI_APTOAP_INF_USE_ROW, ExcelLayout.EAI_APTOAP_INF_USE_COL);
			/* 거래코드 */
			String trxCd = ExcelUtils.readValue(sheet, ExcelLayout.EAI_APTOAP_TRX_CD_ROW, ExcelLayout.EAI_APTOAP_TRX_CD_COL);

			isValid = validateInputEAICommon(isValid, errMsg, appCdL3, intrfcNm, onlBatDscd, intrfcWayCd, syncAsyncDscd,
					rspsYn, occurCycle, dayOccurCnt, sendSyscd, sendCrgManNm, recvSysCd, recvCrgManNm, privacyInclYn,
					encTargetYn, currIntrfcIdentifier, intrfcUse);
			if (recvSysCd.equals(SYS_CD_CARD_CORE_ONL)) {
				isValid = intrfccomService.nullCheck(errMsg, trxCd, "거래코드", isValid);
			}
		} else if (sheet.getSheetName().equals("대내온라인_APTODB")) {
			/* 어플리케이션 Level3 */
			String appCdL3 = ExcelUtils.readValue(sheet, ExcelLayout.EAI_APTODB_L3_ROW, ExcelLayout.EAI_APTODB_L3_COL);
			/* 인터페이스명 */
			String intrfcNm = ExcelUtils.readValue(sheet, ExcelLayout.EAI_APTODB_IFNM_ROW, ExcelLayout.EAI_APTODB_IFNM_COL);
			/* 발생유형 */
			String onlBatDscd = ExcelUtils.readValue(sheet, ExcelLayout.EAI_APTODB_ONL_BAT_DSCD_ROW, ExcelLayout.EAI_APTODB_ONL_BAT_DSCD_COL);
			/* 인터페이스 방식(거래유형) */
			String intrfcWayCd = intrfccomService.getCodeValueNm("INTRFC_WAY_CD", ExcelUtils.readValue(sheet, ExcelLayout.EAI_APTODB_INF_TYPE_ROW, ExcelLayout.EAI_APTODB_INF_TYPE_COL), "ko");
			/* 동기구분 */
			String syncAsyncDscd = intrfccomService.getCodeValueNm("SYNC_DSCD", ExcelUtils.readValue(sheet, ExcelLayout.EAI_APTODB_SYNC_DSCD_ROW, ExcelLayout.EAI_APTODB_SYNC_DSCD_COL), "ko");
			/* 응답여부 */
			String rspsYn = ExcelUtils.readValue(sheet, ExcelLayout.EAI_APTODB_RESP_YN_ROW, ExcelLayout.EAI_APTODB_RESP_YN_COL);
			/* 발생주기 */
			String occurCycle = intrfccomService.getCodeValueNm("GEN_CYCLE_CD", ExcelUtils.readValue(sheet, ExcelLayout.EAI_APTODB_GEN_CYCLE_CD_ROW, ExcelLayout.EAI_APTODB_GEN_CYCLE_CD_COL), "ko");
			/* 일 발생건수 */
			String dayOccurCnt = ExcelUtils.readValue(sheet, ExcelLayout.EAI_APTODB_OCCUR_CNT_ROW, ExcelLayout.EAI_APTODB_OCCUR_CNT_COL);
			/* (송신시스템)시스템코드 */
			String sendSyscd = ExcelUtils.readValue(sheet, ExcelLayout.EAI_APTODB_SEND_SYS_CD_ROW, ExcelLayout.EAI_APTODB_SEND_SYS_CD_COL);
			/* (송신시스템)담당자 성명 */
			String sendCrgManNm = ExcelUtils.readValue(sheet, ExcelLayout.EAI_APTODB_SEND_CRG_MAN_NM_ROW, ExcelLayout.EAI_APTODB_SEND_CRG_MAN_NM_COL);
			/* (수신시스템)시스템코드 */
			String recvSysCd = ExcelUtils.readValue(sheet, ExcelLayout.EAI_APTODB_RECV_SYS_CD_ROW, ExcelLayout.EAI_APTODB_RECV_SYS_CD_COL);
			/* (수신시스템)담당자 성명 */
			String recvCrgManNm = ExcelUtils.readValue(sheet, ExcelLayout.EAI_APTODB_RECV_CRG_MAN_NM_ROW, ExcelLayout.EAI_APTODB_RECV_CRG_MAN_NM_COL);
			/* 개인정보포함여부 */
			String privacyInclYn = ExcelUtils.readValue(sheet, ExcelLayout.EAI_APTODB_PRIVACY_YN_ROW, ExcelLayout.EAI_APTODB_PRIVACY_YN_COL);
			/* 암호화대상여부 */
			String encTargetYn = ExcelUtils.readValue(sheet, ExcelLayout.EAI_APTODB_ENC_YN_ROW, ExcelLayout.EAI_APTODB_ENC_YN_COL);
			/* 현행인터페이스식별자 */
			String currIntrfcIdentifier = ExcelUtils.readValue(sheet, ExcelLayout.EAI_APTODB_CURR_INF_ID_ROW, ExcelLayout.EAI_APTODB_CURR_INF_ID_COL);
			/* 인터페이스 용도 */
			String intrfcUse = ExcelUtils.readValue(sheet, ExcelLayout.EAI_APTODB_INF_USE_ROW, ExcelLayout.EAI_APTODB_INF_USE_COL);

			/* 수신DB동작유형 */
			String recvDbActionType = intrfccomService.getCodeValueNm("RECV_DB_ACT_TYPE", ExcelUtils.readValue(sheet, ExcelLayout.EAI_APTODB_RECV_DB_TYPE_ROW, ExcelLayout.EAI_APTODB_RECV_DB_TYPE_COL), "ko");
			/* 수신DB쿼리문 */
			String recvDbQuery = ExcelUtils.readValue(sheet, ExcelLayout.EAI_APTODB_RECV_QUERY_ROW, ExcelLayout.EAI_APTODB_RECV_QUERY_COL);

			isValid = validateInputEAICommon(isValid, errMsg, appCdL3, intrfcNm, onlBatDscd, intrfcWayCd, syncAsyncDscd,
					rspsYn, occurCycle, dayOccurCnt, sendSyscd, sendCrgManNm, recvSysCd, recvCrgManNm, privacyInclYn,
					encTargetYn, currIntrfcIdentifier, intrfcUse);

			if (isNextGenSystem(recvSysCd)) {
				isValid = intrfccomService.nullCheck(errMsg, recvDbActionType, "수신DB동작유형", isValid);
				isValid = intrfccomService.nullCheck(errMsg, recvDbQuery, "수신DB쿼리문", isValid);
			}

		} else if (sheet.getSheetName().equals("대내배치_DBTODB")) {
			/* 어플리케이션 Level3 */
			String appCdL3 = ExcelUtils.readValue(sheet, ExcelLayout.EAI_DBTODB_L3_ROW, ExcelLayout.EAI_DBTODB_L3_COL);
			/* 인터페이스명 */
			String intrfcNm = ExcelUtils.readValue(sheet, ExcelLayout.EAI_DBTODB_IFNM_ROW, ExcelLayout.EAI_DBTODB_IFNM_COL);
			/* 발생유형 */
			String onlBatDscd = ExcelUtils.readValue(sheet, ExcelLayout.EAI_DBTODB_ONL_BAT_DSCD_ROW, ExcelLayout.EAI_DBTODB_ONL_BAT_DSCD_COL);
			/* 인터페이스 방식(거래유형) */
			String intrfcWayCd = intrfccomService.getCodeValueNm("INTRFC_WAY_CD", ExcelUtils.readValue(sheet, ExcelLayout.EAI_DBTODB_INF_TYPE_ROW, ExcelLayout.EAI_DBTODB_INF_TYPE_COL), "ko");
			/* 발생주기 */
			String occurCycle = intrfccomService.getCodeValueNm("GEN_CYCLE_CD", ExcelUtils.readValue(sheet, ExcelLayout.EAI_DBTODB_GEN_CYCLE_CD_ROW, ExcelLayout.EAI_DBTODB_GEN_CYCLE_CD_COL), "ko");
			/* 일 발생건수 */
			String dayOccurCnt = ExcelUtils.readValue(sheet, ExcelLayout.EAI_DBTODB_OCCUR_CNT_ROW, ExcelLayout.EAI_DBTODB_OCCUR_CNT_COL);
			/* (송신시스템)시스템코드 */
			String sendSyscd = ExcelUtils.readValue(sheet, ExcelLayout.EAI_DBTODB_SEND_SYS_CD_ROW, ExcelLayout.EAI_DBTODB_SEND_SYS_CD_COL);
			/* (송신시스템)담당자 성명 */
			String sendCrgManNm = ExcelUtils.readValue(sheet, ExcelLayout.EAI_DBTODB_SEND_CRG_MAN_NM_ROW, ExcelLayout.EAI_DBTODB_SEND_CRG_MAN_NM_COL);
			/* (수신시스템)시스템코드 */
			String recvSysCd = ExcelUtils.readValue(sheet, ExcelLayout.EAI_DBTODB_RECV_SYS_CD_ROW, ExcelLayout.EAI_DBTODB_RECV_SYS_CD_COL);
			/* (수신시스템)담당자 성명 */
			String recvCrgManNm = ExcelUtils.readValue(sheet, ExcelLayout.EAI_DBTODB_RECV_CRG_MAN_NM_ROW, ExcelLayout.EAI_DBTODB_RECV_CRG_MAN_NM_COL);
			/* 개인정보포함여부 */
			String privacyInclYn = ExcelUtils.readValue(sheet, ExcelLayout.EAI_DBTODB_PRIVACY_YN_ROW, ExcelLayout.EAI_DBTODB_PRIVACY_YN_COL);
			/* 암호화대상여부 */
			String encTargetYn = ExcelUtils.readValue(sheet, ExcelLayout.EAI_DBTODB_ENC_YN_ROW, ExcelLayout.EAI_DBTODB_ENC_YN_COL);
			/* 현행인터페이스식별자 */
			String currIntrfcIdentifier = ExcelUtils.readValue(sheet, ExcelLayout.EAI_DBTODB_CURR_INF_ID_ROW, ExcelLayout.EAI_DBTODB_CURR_INF_ID_COL);
			/* 인터페이스 용도 */
			String intrfcUse = ExcelUtils.readValue(sheet, ExcelLayout.EAI_DBTODB_INF_USE_ROW, ExcelLayout.EAI_DBTODB_INF_USE_COL);

			/* 송신DB쿼리문 */
			String sendDbQuery = ExcelUtils.readValue(sheet, ExcelLayout.EAI_DBTODB_SEND_QUERY_ROW, ExcelLayout.EAI_DBTODB_SEND_QUERY_COL);
			/* 수신DB쿼리문 */
			String recvDbQuery = ExcelUtils.readValue(sheet, ExcelLayout.EAI_DBTODB_RECV_QUERY_ROW, ExcelLayout.EAI_DBTODB_RECV_QUERY_COL);
			/* 에러스킵여부 */
			String errSkipYn = ExcelUtils.readValue(sheet, ExcelLayout.EAI_DBTODB_ERR_SKIP_YN_ROW, ExcelLayout.EAI_DBTODB_ERR_SKIP_YN_COL);

			isValid = validateInputEAICommon(isValid, errMsg, appCdL3, intrfcNm, onlBatDscd, intrfcWayCd, null,
					null, occurCycle, dayOccurCnt, sendSyscd, sendCrgManNm, recvSysCd, recvCrgManNm, privacyInclYn,
					encTargetYn, currIntrfcIdentifier, intrfcUse);

			if (isNextGenSystem(sendSyscd)) {
				isValid = intrfccomService.nullCheck(errMsg, sendDbQuery, "송신DB쿼리문", isValid);
			}
			if (isNextGenSystem(recvSysCd)) {
				isValid = intrfccomService.nullCheck(errMsg, recvDbQuery, "수신DB쿼리문", isValid);
				isValid = intrfccomService.nullCheck(errMsg, errSkipYn, "에러스킵여부", isValid);
			}

		} else if (sheet.getSheetName().equals("대내배치_FILETOFILE")) {
			/* 어플리케이션 Level3 */
			String appCdL3 = ExcelUtils.readValue(sheet, ExcelLayout.EAI_FILETOFILE_L3_ROW, ExcelLayout.EAI_FILETOFILE_L3_COL);
			/* 인터페이스명 */
			String intrfcNm = ExcelUtils.readValue(sheet, ExcelLayout.EAI_FILETOFILE_IFNM_ROW, ExcelLayout.EAI_FILETOFILE_IFNM_COL);
			/* 발생유형 */
			String onlBatDscd = ExcelUtils.readValue(sheet, ExcelLayout.EAI_FILETOFILE_ONL_BAT_DSCD_ROW, ExcelLayout.EAI_FILETOFILE_ONL_BAT_DSCD_COL);
			/* 인터페이스 방식(거래유형) */
			String intrfcWayCd = intrfccomService.getCodeValueNm("INTRFC_WAY_CD", ExcelUtils.readValue(sheet, ExcelLayout.EAI_FILETOFILE_INF_TYPE_ROW, ExcelLayout.EAI_FILETOFILE_INF_TYPE_COL), "ko");
			/* 발생주기 */
			String occurCycle = intrfccomService.getCodeValueNm("GEN_CYCLE_CD", ExcelUtils.readValue(sheet, ExcelLayout.EAI_FILETOFILE_GEN_CYCLE_CD_ROW, ExcelLayout.EAI_FILETOFILE_GEN_CYCLE_CD_COL), "ko");
			/* 일 발생건수 */
			String dayOccurCnt = ExcelUtils.readValue(sheet, ExcelLayout.EAI_FILETOFILE_OCCUR_CNT_ROW, ExcelLayout.EAI_FILETOFILE_OCCUR_CNT_COL);
			/* (송신시스템)시스템코드 */
			String sendSyscd = ExcelUtils.readValue(sheet, ExcelLayout.EAI_FILETOFILE_SEND_SYS_CD_ROW, ExcelLayout.EAI_FILETOFILE_SEND_SYS_CD_COL);
			/* (송신시스템)담당자 성명 */
			String sendCrgManNm = ExcelUtils.readValue(sheet, ExcelLayout.EAI_FILETOFILE_SEND_CRG_MAN_NM_ROW, ExcelLayout.EAI_FILETOFILE_SEND_CRG_MAN_NM_COL);
			/* (수신시스템)시스템코드 */
			String recvSysCd = ExcelUtils.readValue(sheet, ExcelLayout.EAI_FILETOFILE_RECV_SYS_CD_ROW, ExcelLayout.EAI_FILETOFILE_RECV_SYS_CD_COL);
			/* (수신시스템)담당자 성명 */
			String recvCrgManNm = ExcelUtils.readValue(sheet, ExcelLayout.EAI_FILETOFILE_RECV_CRG_MAN_NM_ROW, ExcelLayout.EAI_FILETOFILE_RECV_CRG_MAN_NM_COL);
			/* 개인정보포함여부 */
			String privacyInclYn = ExcelUtils.readValue(sheet, ExcelLayout.EAI_FILETOFILE_PRIVACY_YN_ROW, ExcelLayout.EAI_FILETOFILE_PRIVACY_YN_COL);
			/* 암호화대상여부 */
			String encTargetYn = ExcelUtils.readValue(sheet, ExcelLayout.EAI_FILETOFILE_ENC_YN_ROW, ExcelLayout.EAI_FILETOFILE_ENC_YN_COL);
			/* 현행인터페이스식별자 */
			String currIntrfcIdentifier = ExcelUtils.readValue(sheet, ExcelLayout.EAI_FILETOFILE_CURR_INF_ID_ROW, ExcelLayout.EAI_FILETOFILE_CURR_INF_ID_COL);
			/* 인터페이스 용도 */
			String intrfcUse = ExcelUtils.readValue(sheet, ExcelLayout.EAI_FILETOFILE_INF_USE_ROW, ExcelLayout.EAI_FILETOFILE_INF_USE_COL);

			/* 송신 파일경로 */
			String sendFilePath = ExcelUtils.readValue(sheet, ExcelLayout.EAI_FILETOFILE_SEND_FILEPATH_ROW, ExcelLayout.EAI_FILETOFILE_SEND_FILEPATH_COL);
			/* 송신 파일명 */
			String sendFileNm = ExcelUtils.readValue(sheet, ExcelLayout.EAI_FILETOFILE_SEND_FILENAME_ROW, ExcelLayout.EAI_FILETOFILE_SEND_FILENAME_COL);
			/* 송신 파일 전송후처리유형 */
			String sendTranPostProc = intrfccomService.getCodeValueNm("TRAN_POST_PROC", ExcelUtils.readValue(sheet, ExcelLayout.EAI_FILETOFILE_SEND_TRAN_POST_PROC_ROW, ExcelLayout.EAI_FILETOFILE_SEND_TRAN_POST_PROC_COL), "ko");
			/* 송신 파일 전송후백업경로 */
			String sendTranPostBackPath = ExcelUtils.readValue(sheet, ExcelLayout.EAI_FILETOFILE_SEND_TRAN_POST_BACK_PATH_ROW, ExcelLayout.EAI_FILETOFILE_SEND_TRAN_POST_BACK_PATH_COL);

			/* 수신 파일경로 */
			String recvFilePath = ExcelUtils.readValue(sheet, ExcelLayout.EAI_FILETOFILE_RECV_FILEPATH_ROW, ExcelLayout.EAI_FILETOFILE_RECV_FILEPATH_COL);
			/* 수신 파일명 */
			String recvFileNm = ExcelUtils.readValue(sheet, ExcelLayout.EAI_FILETOFILE_RECV_FILENAME_ROW, ExcelLayout.EAI_FILETOFILE_RECV_FILENAME_COL);
			/* 수신 파일 전송후핀파일생성여부 */
			String recvTranPostFinCreateYn = ExcelUtils.readValue(sheet, ExcelLayout.EAI_FILETOFILE_RECV_TRAN_POST_FIN_YN_ROW, ExcelLayout.EAI_FILETOFILE_RECV_TRAN_POST_FIN_YN_COL);

			isValid = validateInputEAICommon(isValid, errMsg, appCdL3, intrfcNm, onlBatDscd, intrfcWayCd, null,
					null, occurCycle, dayOccurCnt, sendSyscd, sendCrgManNm, recvSysCd, recvCrgManNm, privacyInclYn,
					encTargetYn, currIntrfcIdentifier, intrfcUse);

			if (isNextGenSystem(sendSyscd)) {
				isValid = intrfccomService.nullCheck(errMsg, sendFilePath, "송신 파일경로", isValid);
				isValid = intrfccomService.nullCheck(errMsg, sendFileNm, "송신 파일명", isValid);
				isValid = intrfccomService.nullCheck(errMsg, sendTranPostProc, "송신 파일 전송후처리유형", isValid);
				if (sendTranPostProc.equals(SEND_TRAN_POST_PROC_BACKUP)) {
					isValid = intrfccomService.nullCheck(errMsg, sendTranPostBackPath, "송신 파일 전송후백업경로 ", isValid);
				}
			}
			if (isNextGenSystem(recvSysCd)) {
				isValid = intrfccomService.nullCheck(errMsg, recvFilePath, "수신 파일경로", isValid);
				isValid = intrfccomService.nullCheck(errMsg, recvFileNm, "수신 파일명", isValid);
				isValid = intrfccomService.nullCheck(errMsg, recvTranPostFinCreateYn, "수신 파일 전송후핀파일생성여부", isValid);
			}
		} else {
			throw new ServiceException(BxMessages.Error.FILE_UPLOAD_EXCEPTION, "Sheet 명 오류");
		}

		if (!isValid) {
			throw new ServiceException(BxMessages.Error.MSG_VALID_CHECK, errMsg.toString());
		}
	}

	private boolean validateInputEAICommon(boolean isValid, StringBuilder errMsg, String appCdL3, String intrfcNm,
			String onlBatDscd, String intrfcWayCd, String syncAsyncDscd, String rspsYn, String occurCycle,
			String dayOccurCnt, String sendSyscd, String sendCrgManNm, String recvSysCd, String recvCrgManNm,
			String privacyInclYn, String encTargetYn, String currIntrfcIdentifier, String intrfcUse) {

		isValid = intrfccomService.nullCheck(errMsg, appCdL3, "어플리케이션 Level3", isValid);
		isValid = intrfccomService.nullCheck(errMsg, intrfcNm, "인터페이스명", isValid);
		isValid = intrfccomService.nullCheck(errMsg, onlBatDscd, "발생유형", isValid);
		isValid = intrfccomService.nullCheck(errMsg, intrfcWayCd, "거래유형", isValid);
		if (onlBatDscd.equals(ONL_BAT_DSCD_ONLINE)) {
			isValid = intrfccomService.nullCheck(errMsg, syncAsyncDscd, "동기구분", isValid);
			isValid = intrfccomService.nullCheck(errMsg, rspsYn, "응답여부", isValid);
			isValid = intrfccomService.nullCheck(errMsg, privacyInclYn, "개인정보포함여부", isValid);
			isValid = intrfccomService.nullCheck(errMsg, encTargetYn, "암호화대상여부", isValid);
		}
		isValid = intrfccomService.nullCheck(errMsg, occurCycle, "발생주기", isValid);
		/* 발생주기 수시인 경우 필수 */
		if (occurCycle.equals(GEN_CYCLE_CD_FREQ)) {
			isValid = intrfccomService.nullCheck(errMsg, dayOccurCnt, "일 발생건수", isValid);
		}
		isValid = intrfccomService.nullCheck(errMsg, sendSyscd, "송신시스템 시스템코드", isValid);
		isValid = intrfccomService.nullCheck(errMsg, sendCrgManNm, "송신시스템 담당자 성명", isValid);
		isValid = intrfccomService.nullCheck(errMsg, recvSysCd, "수신시스템 시스템코드", isValid);
		isValid = intrfccomService.nullCheck(errMsg, recvCrgManNm, "수신시스템 담당자 성명", isValid);
		isValid = intrfccomService.nullCheck(errMsg, currIntrfcIdentifier, "현행인터페이스식별자", isValid);
//		isValid = intrfccomService.nullCheck(errMsg, intrfcUse, "인터페이스 용도", isValid);
		return isValid;
	}

	private void validateInputCC(Sheet sheet) {
		boolean isValid = true;
		StringBuilder errMsg = new StringBuilder();

		/* 어플리케이션 Level3 */
		String appCdL3 = ExcelUtils.readValue(sheet, 11, "H");
		/* 인터페이스명 */
		String intrfcNm = ExcelUtils.readValue(sheet, 5, "M");
		/* 거래유형 */
		String trxTypeDscd = intrfccomService.getCodeValueNm("TRX_TYPE_DSCD_CC", ExcelUtils.readValue(sheet, 6, "M"), "ko");
		/* 발생유형 */
		String trxDscd = ExcelUtils.readValue(sheet, 6, "E");
		/* (송신시스템)시스템코드 */
		String sendSyscd = ExcelUtils.readValue(sheet, 7, "E");
		/* (송신시스템)담당자 성명 */
		String sendCrgManNm = ExcelUtils.readValue(sheet, 9,"E");
		/* (수신시스템)시스템코드 */
		String recvSysCd = ExcelUtils.readValue(sheet, 7, "M");
		/* (수신시스템)담당자 성명 */
		String recvCrgManNm = ExcelUtils.readValue(sheet, 9, "M");

		isValid = intrfccomService.nullCheck(errMsg, appCdL3, "어플리케이션 Level3", isValid);
		isValid = intrfccomService.nullCheck(errMsg, intrfcNm, "인터페이스명", isValid);
		isValid = intrfccomService.nullCheck(errMsg, trxTypeDscd, "거래유형", isValid);
		isValid = intrfccomService.nullCheck(errMsg, trxDscd, "발생유형", isValid);
		isValid = intrfccomService.nullCheck(errMsg, sendSyscd, "송신시스템 시스템코드", isValid);
//		isValid = intrfccomService.nullCheck(errMsg, sendCrgManNm, "송신시스템 담당자 성명", isValid);
		isValid = intrfccomService.nullCheck(errMsg, recvSysCd, "수신시스템 시스템코드", isValid);
//		isValid = intrfccomService.nullCheck(errMsg, recvCrgManNm, "수신시스템 담당자 성명", isValid);
//		isValid = intrfccomService.nullCheck(errMsg, intrfcUse, "인터페이스 용도", isValid);

		if (!isValid) {
			throw new ServiceException(BxMessages.Error.MSG_VALID_CHECK, errMsg.toString());
		}
	}

	private void validateInputMCI(Sheet sheet) {
		boolean isValid = true;
		StringBuilder errMsg = new StringBuilder();

		/* 어플리케이션 Level3 */
		String appCdL3 = ExcelUtils.readValue(sheet, ExcelLayout.MCI_L3_ROW, ExcelLayout.MCI_L3_COL);
		/* 인터페이스명 */
		String intrfcNm = ExcelUtils.readValue(sheet, ExcelLayout.MCI_IFNM_ROW, ExcelLayout.MCI_IFNM_COL);
		/* 거래유형 */
		String trxTypeDscd = intrfccomService.getCodeValueNm("TRX_TYPE_DSCD", ExcelUtils.readValue(sheet, ExcelLayout.MCI_TRX_TYPE_DSCD_ROW, ExcelLayout.MCI_TRX_TYPE_DSCD_COL), "ko");
		/* 동기구분 */
		String syncAsyncDscd = intrfccomService.getCodeValueNm("SYNC_DSCD", ExcelUtils.readValue(sheet, ExcelLayout.MCI_SYNC_DSCD_ROW, ExcelLayout.MCI_SYNC_DSCD_COL), "ko");
		/* 응답여부 */
		String rspsYn = ExcelUtils.readValue(sheet, ExcelLayout.MCI_RESP_YN_ROW, ExcelLayout.MCI_RESP_YN_COL);
		/* 발생주기 */
		String occurCycle = intrfccomService.getCodeValueNm("GEN_CYCLE_CD", ExcelUtils.readValue(sheet, ExcelLayout.MCI_OCCUR_CYCLE_ROW, ExcelLayout.MCI_OCCUR_CYCLE_COL), "ko");
		/* 일 발생건수 */
		String dayOccurCnt = ExcelUtils.readValue(sheet, ExcelLayout.MCI_DAY_OCCUR_CNT_ROW, ExcelLayout.MCI_DAY_OCCUR_CNT_COL);
		/* (송신시스템)시스템코드 */
		String sendSyscd = ExcelUtils.readValue(sheet, ExcelLayout.MCI_SEND_SYS_CD_ROW, ExcelLayout.MCI_SEND_SYS_CD_COL);
		/* (송신시스템)담당자 성명 */
		String sendCrgManNm = ExcelUtils.readValue(sheet, ExcelLayout.MCI_SEND_CRG_MAN_NM_ROW, ExcelLayout.MCI_SEND_CRG_MAN_NM_COL);
		/* (수신시스템)시스템코드 */
		String recvSysCd = ExcelUtils.readValue(sheet, ExcelLayout.MCI_RECV_SYS_CD_ROW, ExcelLayout.MCI_RECV_SYS_CD_COL);
		/* (수신시스템)담당자 성명 */
		String recvCrgManNm = ExcelUtils.readValue(sheet, ExcelLayout.MCI_RECV_CRG_MAN_NM_ROW, ExcelLayout.MCI_RECV_CRG_MAN_NM_COL);
		/* 개인정보포함여부 */
		String privacyInclYn = ExcelUtils.readValue(sheet, ExcelLayout.MCI_PRIVACY_YN_ROW, ExcelLayout.MCI_PRIVACY_YN_COL);
		/* 암호화대상여부 */
		String encTargetYn = ExcelUtils.readValue(sheet, ExcelLayout.MCI_ENC_YN_ROW, ExcelLayout.MCI_ENC_YN_COL);
		/* 현행인터페이스식별자 */
		String currIntrfcIdentifier = ExcelUtils.readValue(sheet, ExcelLayout.MCI_CURR_INF_ID_ROW, ExcelLayout.MCI_CURR_INF_ID_COL);
		/* 인터페이스 용도 */
		String intrfcUse = ExcelUtils.readValue(sheet, ExcelLayout.MCI_INF_USE_ROW, ExcelLayout.MCI_INF_USE_COL);
		/* 거래코드 */
		String trxCd = ExcelUtils.readValue(sheet, ExcelLayout.MCI_TRX_CD_ROW, ExcelLayout.MCI_TRX_CD_COL);

		isValid = intrfccomService.nullCheck(errMsg, appCdL3, "어플리케이션 Level3", isValid);
		isValid = intrfccomService.nullCheck(errMsg, intrfcNm, "인터페이스명", isValid);
		isValid = intrfccomService.nullCheck(errMsg, trxTypeDscd, "거래유형", isValid);
		isValid = intrfccomService.nullCheck(errMsg, syncAsyncDscd, "동기구분", isValid);
		isValid = intrfccomService.nullCheck(errMsg, rspsYn, "응답여부", isValid);
		isValid = intrfccomService.nullCheck(errMsg, occurCycle, "발생주기", isValid);
		/* 발생주기 수시인 경우 필수 */
		if (occurCycle.equals(GEN_CYCLE_CD_FREQ)) {
			isValid = intrfccomService.nullCheck(errMsg, dayOccurCnt, "일 발생건수", isValid);
		}
		isValid = intrfccomService.nullCheck(errMsg, sendSyscd, "송신시스템 시스템코드", isValid);
		isValid = intrfccomService.nullCheck(errMsg, sendCrgManNm, "송신시스템 담당자 성명", isValid);
		isValid = intrfccomService.nullCheck(errMsg, recvSysCd, "수신시스템 시스템코드", isValid);
		isValid = intrfccomService.nullCheck(errMsg, recvCrgManNm, "수신시스템 담당자 성명", isValid);
		isValid = intrfccomService.nullCheck(errMsg, privacyInclYn, "개인정보포함여부", isValid);
		isValid = intrfccomService.nullCheck(errMsg, encTargetYn, "암호화대상여부", isValid);
		isValid = intrfccomService.nullCheck(errMsg, currIntrfcIdentifier, "현행인터페이스식별자", isValid);
//		isValid = intrfccomService.nullCheck(errMsg, intrfcUse, "인터페이스 용도", isValid);
		/* 거래유형이 '일반거래' 또는 '책임자승인거래'인 경우 필수 */
		if (trxTypeDscd.equals("STANDARD") || trxTypeDscd.equals("DIRECTORAPPR")) {
			isValid = intrfccomService.nullCheck(errMsg, trxCd, "거래코드", isValid);
		}

		if (!isValid) {
			throw new ServiceException(BxMessages.Error.MSG_VALID_CHECK, errMsg.toString());
		}
	}

	public boolean isNextGenSystem(String sysCd) {
		if (sysCd.equals(SYS_CD_CARD_CORE_ONL)
				|| sysCd.equals(SYS_CD_CARD_CORE_BAT)
				|| sysCd.equals(SYS_CD_CARD_AUTH_BAK)
				|| sysCd.equals(SYS_CD_FDS)
				|| sysCd.equals(SYS_CD_INTERNET_CORE_BAT)
				|| sysCd.equals(SYS_CD_FEP)
				|| sysCd.equals(SYS_CD_OPENAPI_GATEWAY)) {
			return true;
		} else {
			return false;
		}
	}
}
