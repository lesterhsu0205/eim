package eims.web.excel.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
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
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import eims.web.constants.BxCode;
import eims.web.constants.BxMessages;
import eims.web.dao.CommCodeDao;
import eims.web.dto.table.CommCodeDto;
import eims.web.dto.table.IntrfccombsDto;
import eims.web.dto.table.IntrfccombsMappingDto;
import eims.web.dto.table.IntrfcmsglayoutdtDto;
import eims.web.dto.table.IntrfcroutinfodtDto;
import eims.web.dto.table.IntrfcsrsysdtDto;
import eims.web.dto.table.MsglayoutbsDto;
import eims.web.dto.table.MsglayoutdtDto;
import eims.web.excel.drm.DrmUtil;
import eims.web.excel.drm.exception.FasooDrmException;
import eims.web.excel.drm.exception.FileTypeException;
import eims.web.exception.ServiceException;
import eims.web.utils.ExcelUtils;

public class InterfaceExcelView extends AbstractXlsxView {

	final Logger logger = LoggerFactory.getLogger(getClass());

	private CommCodeDao codeDao;
	private Map<String, String> aaMap = new HashMap<String, String>();

	public InterfaceExcelView(CommCodeDao codeDao) {
		this.codeDao = codeDao;
		aaMap.put("AA", "승인");
		aaMap.put("AC", "국외매입정산");
		aaMap.put("AS", "국내매입정산");
		aaMap.put("AV", "단기카드대출정산");
		aaMap.put("AM", "가맹점");
		aaMap.put("PC", "카드상품");
		aaMap.put("PO", "카드상품오퍼");
		aaMap.put("PP", "선불");
		aaMap.put("PD", "현장할인");
		aaMap.put("PR", "구매카드");
		aaMap.put("PB", "포인트");
		aaMap.put("PM", "통합마케팅");
		aaMap.put("SH", "회원");
		aaMap.put("SP", "카드발급");
		aaMap.put("SA", "연회비");
		aaMap.put("SB", "회원청구");
		aaMap.put("SC", "SCMS");
		aaMap.put("SD", "디지털인증결제WEB");
		aaMap.put("SE", "디지털인증결제");
		aaMap.put("ST", "소득공제");
		aaMap.put("SN", "분할청구");
		aaMap.put("RL", "회원한도");
		aaMap.put("RA", "회원사고");
		aaMap.put("RS", "회원평점");
		aaMap.put("RF", "FDS");
		aaMap.put("RU", "부정사용");
		aaMap.put("RC", "채권관리");
		aaMap.put("BC", "고객");
		aaMap.put("BT", "고객사관리");
		aaMap.put("BY", "제휴정산");
		aaMap.put("BF", "수수료정산");
		aaMap.put("BE", "에코머니");
		aaMap.put("BP", "오포인트");
		aaMap.put("BL", "여신");
		aaMap.put("BN", "네트워크서비스");
		aaMap.put("BM", "커머스");
		aaMap.put("BZ", "업무공통");

	}

	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		try {
			String l3cd = (String) model.get("l3cd");
			String l3cdFileName = l3cd + "0";
			String l3cdNm = aaMap.get(l3cd);
			String userNm = (String) model.get("userNm");
			if (l3cdNm != null && !l3cdNm.equals("")) {
				l3cdNm = new String(l3cdNm.getBytes("UTF-8"));
			}
			String intrfcString = new String("인터페이스설계서".getBytes("UTF-8"));

			String attachment = "attachment; filename=\"BCC-" + l3cdFileName + "-DS-10(" + intrfcString + "-" + l3cdNm
					+ "-" + model.get("intrfcId") + ")-V.0.9.xlsx\"";
			attachment = URLEncoder.encode(attachment, "UTF-8");
			// String attachment = "attachment; filename=\"" + model.get("intrfcId") + "_" +
			// model.get("intrfcType") + "_"
			// + LocalDateTime.now() + ".xlsx\"";
			response.setHeader("Content-Type", "application/octet-stream");
			response.setHeader("Content-Transper-Encoding", "binary");
			response.setHeader("Content-Disposition", attachment);
			response.setHeader("Content-Language", "UTF-8");
			// response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet; charset=UTF-8");
			final int startGridRowNum = 15;
			final int endGridRowNum = 615;

			String intrfcType = (String) model.get("intrfcType");
			String trxDscd = (String) model.get("trxDscd");
			IntrfccombsDto intrfcDto = (IntrfccombsDto) model.get("intrfcDto");

			if (intrfcDto.getMsgTrnsfrmYn() == null || intrfcDto.getMsgTrnsfrmYn().equals("")
					|| intrfcDto.getMsgTrnsfrmYn().equals("N")) {
				intrfcDto.setMsgTrnsfrmYn("N");
				int reqMappingSeq = workbook.getSheetIndex("reqMapping");
				workbook.removeSheetAt(reqMappingSeq);
				int resMappingSeq = workbook.getSheetIndex("resMapping");
				workbook.removeSheetAt(resMappingSeq);
			}

			Sheet coverSheet = workbook.getSheet("표지");
			Sheet historySheet = workbook.getSheet("개정이력");
			Date today = new Date();
			SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
			String dateString = date.format(today);

			ExcelUtils.writeValue(coverSheet, 8, "H", l3cdNm);
			ExcelUtils.writeValue(coverSheet, 10, "H", "BCC-" + l3cdFileName + "-DS-10");
			ExcelUtils.writeValue(coverSheet, 11, "H", "V.0.9");

			ExcelUtils.writeValue(historySheet, 4, "A", "V.0.9");
			ExcelUtils.writeValue(historySheet, 4, "C", dateString);
			if (userNm == null || userNm.equals("")) {
				userNm = "홍길동";
			}
			ExcelUtils.writeValue(historySheet, 4, "D", userNm);

			Map<String, String> layoutMap = new HashMap<String, String>();

			if (intrfcType.equals("MCI")) {
				List<IntrfcmsglayoutdtDto> intrfcmsglayoutdtDto = intrfcDto.getIntrfcmsglayoutdtDto();

				addMCIInterface(workbook.getSheet("대내온라인"), intrfcDto);

				for (IntrfcmsglayoutdtDto dto : intrfcmsglayoutdtDto) {
					String srType = dto.getSrTypeCd();
					String reqResType = dto.getRqstRspsTypeCd();

					if (srType.equals("SEND") && reqResType.equals("REQUEST")) {
						if (workbook.getSheet(dto.getMsgLayoutId()) == null) {
							String sheetName = getSheetCopyName(workbook, dto, "SEND", "REQ");
							addDataIndividual(workbook.getSheet(sheetName), dto.getMsglayoutbsDto());
							addDataGrid(workbook.getSheet(sheetName), dto.getMsglayoutbsDto(), startGridRowNum,
									endGridRowNum);
							layoutMap.put(sheetName, "create");
						}
					} else if (srType.equals("SEND") && reqResType.equals("RESPONSE")) {
						if (workbook.getSheet(dto.getMsgLayoutId()) == null) {
							String sheetName = getSheetCopyName(workbook, dto, "SEND", "RES");
							addDataIndividual(workbook.getSheet(sheetName), dto.getMsglayoutbsDto());
							addDataGrid(workbook.getSheet(sheetName), dto.getMsglayoutbsDto(), startGridRowNum,
									endGridRowNum);
							layoutMap.put(sheetName, "create");
						}
					} else if (srType.equals("RECEIVE") && reqResType.equals("REQUEST")) {
						if (workbook.getSheet(dto.getMsgLayoutId()) == null) {
							String sheetName = getSheetCopyName(workbook, dto, "RECV", "REQ");
							addDataIndividual(workbook.getSheet(sheetName), dto.getMsglayoutbsDto());
							addDataGrid(workbook.getSheet(sheetName), dto.getMsglayoutbsDto(), startGridRowNum,
									endGridRowNum);
							layoutMap.put(sheetName, "create");
						}
					} else {
						if (workbook.getSheet(dto.getMsgLayoutId()) == null) {
							String sheetName = getSheetCopyName(workbook, dto, "RECV", "RES");
							addDataIndividual(workbook.getSheet(sheetName), dto.getMsglayoutbsDto());
							addDataGrid(workbook.getSheet(sheetName), dto.getMsglayoutbsDto(), startGridRowNum,
									endGridRowNum);
							layoutMap.put(sheetName, "create");
						}
					}
				}

				int reqMappingSeq = workbook.getSheetIndex("reqMapping");
				int resMappingSeq = workbook.getSheetIndex("resMapping");

				if (intrfcDto.getMsgTrnsfrmYn().equals("Y")) {
					if (intrfcDto.getIntrfccombsMappingReqDto() != null
							&& intrfcDto.getIntrfccombsMappingResDto() != null) {
						logger.debug("111111111111111111111111111111111111111111111111111111111");
						Sheet sReq = workbook.cloneSheet(reqMappingSeq);
						int sheetindex = workbook.getSheetIndex(sReq.getSheetName());
						String sheetNameReq = "REQ_Mapping";
						workbook.setSheetName(sheetindex, sheetNameReq);

						Sheet sRes = workbook.cloneSheet(resMappingSeq);
						int sheetindex2 = workbook.getSheetIndex(sRes.getSheetName());
						String sheetNameRes = "RES_Mapping";
						workbook.setSheetName(sheetindex2, sheetNameRes);

						mappingDataReqSet(workbook.getSheet(sheetNameReq), intrfcDto);
						mappingDataResSet(workbook.getSheet(sheetNameRes), intrfcDto);

					} else if (intrfcDto.getIntrfccombsMappingReqDto() != null
							&& intrfcDto.getIntrfccombsMappingResDto() == null) {
						logger.debug("222222222222222222222222222222222222222222222222222222222");
						Sheet sReq = workbook.cloneSheet(reqMappingSeq);
						int sheetindex = workbook.getSheetIndex(sReq.getSheetName());
						String sheetNameReq = "REQ_Mapping";
						workbook.setSheetName(sheetindex, sheetNameReq);
						mappingDataReqSet(workbook.getSheet(sheetNameReq), intrfcDto);
					} else {
						logger.debug("3333333333333333333333333333333333333333333333333333333333");
						Sheet sRes = workbook.cloneSheet(resMappingSeq);
						int sheetindex2 = workbook.getSheetIndex(sRes.getSheetName());
						String sheetNameRes = "RES_Mapping";
						workbook.setSheetName(sheetindex2, sheetNameRes);
						// mappingDataResSet(workbook.getSheet(sheetNameRes), intrfcDto);
					}

					workbook.removeSheetAt(workbook.getSheetIndex("reqMapping"));
					workbook.removeSheetAt(workbook.getSheetIndex("resMapping"));
				} else {
					// workbook.removeSheetAt(workbook.getSheetIndex("reqMapping"));
					// workbook.removeSheetAt(workbook.getSheetIndex("resMapping"));
				}
			} else if (intrfcType.equals("CC")) {
				List<IntrfcmsglayoutdtDto> intrfcmsglayoutdtDto = intrfcDto.getIntrfcmsglayoutdtDto();

				addCCInterface(workbook.getSheet("대내설계서"), intrfcDto);

				for (IntrfcmsglayoutdtDto dto : intrfcmsglayoutdtDto) {
					String srType = dto.getSrTypeCd();
					String reqResType = dto.getRqstRspsTypeCd();

					if (srType.equals("SEND") && reqResType.equals("REQUEST")) {
						if (workbook.getSheet(dto.getMsgLayoutId()) == null) {
							String sheetName = getSheetCopyName(workbook, dto, "SEND", "REQ");
							addDataIndividual(workbook.getSheet(sheetName), dto.getMsglayoutbsDto());
							addDataGrid(workbook.getSheet(sheetName), dto.getMsglayoutbsDto(), startGridRowNum,
									endGridRowNum);
							layoutMap.put(sheetName, "create");
						}
					} else if (srType.equals("SEND") && reqResType.equals("RESPONSE")) {
						if (workbook.getSheet(dto.getMsgLayoutId()) == null) {
							String sheetName = getSheetCopyName(workbook, dto, "SEND", "RES");
							addDataIndividual(workbook.getSheet(sheetName), dto.getMsglayoutbsDto());
							addDataGrid(workbook.getSheet(sheetName), dto.getMsglayoutbsDto(), startGridRowNum,
									endGridRowNum);
							layoutMap.put(sheetName, "create");
						}
					}
				}

			} else if (intrfcType.equals("EAI_I") || intrfcType.equals("EAI_E")) {
				logger.debug(
						"EAIEAIEAIEAIEAIEAIEAIEAIEAIEAIEAIEAIEAIEAIEAIEAIEAIEAIEAIEAIEAIEAIEAEIAEIAEAIEAIEAEAIEAEIAEAIEAEIAEAIEAIEAEIAEAIEAIEAIEAEIAEIAEAIEAIEAIEAEIAEIAEIAEAIEAIEAIEAIEAEIAEAIEAIEAIEAIEAEIAEIA");
				List<IntrfcmsglayoutdtDto> intrfcmsglayoutdtDto = intrfcDto.getIntrfcmsglayoutdtDto();

				if (intrfcDto.getTrxDscd().equals("ONLINE")) {
					addEAIInterface(workbook.getSheet("대내온라인_" + intrfcDto.getIntrfcWayCd()), intrfcDto,
							intrfcDto.getIntrfcWayCd());
					if (intrfcDto.getIntrfcWayCd().equals("APTOAP")) {
						workbook.removeSheetAt(workbook.getSheetIndex("대내배치_FILETOFILE"));
						workbook.removeSheetAt(workbook.getSheetIndex("대내온라인_APTODB"));
						workbook.removeSheetAt(workbook.getSheetIndex("대내배치_DBTODB"));
						workbook.removeSheetAt(workbook.getSheetIndex("대외파일전송"));
					} else {
						workbook.removeSheetAt(workbook.getSheetIndex("대내배치_FILETOFILE"));
						workbook.removeSheetAt(workbook.getSheetIndex("대내온라인_APTOAP"));
						workbook.removeSheetAt(workbook.getSheetIndex("대내배치_DBTODB"));
						workbook.removeSheetAt(workbook.getSheetIndex("대외파일전송"));
					}
				} else {
					if (intrfcDto.getIntrfcWayCd().equals("FILETOFILE")) {
						if (intrfcDto.getIntrfcTypeCd().equals("EAI_I")) {
							addEAIInterface(workbook.getSheet("대내배치_" + intrfcDto.getIntrfcWayCd()), intrfcDto,
									intrfcDto.getIntrfcWayCd());
							workbook.removeSheetAt(workbook.getSheetIndex("대내온라인_APTOAP"));
							workbook.removeSheetAt(workbook.getSheetIndex("대내온라인_APTODB"));
							workbook.removeSheetAt(workbook.getSheetIndex("대내배치_DBTODB"));
							workbook.removeSheetAt(workbook.getSheetIndex("대외파일전송"));
						} else {
							addEAIInterface(workbook.getSheet("대외파일전송"), intrfcDto, intrfcDto.getIntrfcWayCd());
							workbook.removeSheetAt(workbook.getSheetIndex("대내온라인_APTOAP"));
							workbook.removeSheetAt(workbook.getSheetIndex("대내온라인_APTODB"));
							workbook.removeSheetAt(workbook.getSheetIndex("대내배치_DBTODB"));
							workbook.removeSheetAt(workbook.getSheetIndex("대내배치_FILETOFILE"));
						}
					} else {
						addEAIInterface(workbook.getSheet("대내배치_" + intrfcDto.getIntrfcWayCd()), intrfcDto,
								intrfcDto.getIntrfcWayCd());
						workbook.removeSheetAt(workbook.getSheetIndex("대내배치_FILETOFILE"));
						workbook.removeSheetAt(workbook.getSheetIndex("대내온라인_APTOAP"));
						workbook.removeSheetAt(workbook.getSheetIndex("대내온라인_APTODB"));
						workbook.removeSheetAt(workbook.getSheetIndex("대외파일전송"));
					}
				}

				for (IntrfcmsglayoutdtDto dto : intrfcmsglayoutdtDto) {
					String srType = dto.getSrTypeCd();
					String reqResType = dto.getRqstRspsTypeCd();

					if (srType.equals("SEND") && reqResType.equals("REQUEST")) {
						if (workbook.getSheet(dto.getMsgLayoutId()) == null) {
							String sheetName = getSheetCopyName(workbook, dto, "SEND", "REQ");
							addDataIndividual(workbook.getSheet(sheetName), dto.getMsglayoutbsDto());
							addDataGrid(workbook.getSheet(sheetName), dto.getMsglayoutbsDto(), startGridRowNum,
									endGridRowNum);
						}
					} else if (srType.equals("SEND") && reqResType.equals("RESPONSE")) {
						if (workbook.getSheet(dto.getMsgLayoutId()) == null) {
							String sheetName = getSheetCopyName(workbook, dto, "SEND", "RES");
							addDataIndividual(workbook.getSheet(sheetName), dto.getMsglayoutbsDto());
							addDataGrid(workbook.getSheet(sheetName), dto.getMsglayoutbsDto(), startGridRowNum,
									endGridRowNum);
						}
					} else if (srType.equals("RECEIVE") && reqResType.equals("REQUEST")) {
						if (workbook.getSheet(dto.getMsgLayoutId()) == null) {
							String sheetName = getSheetCopyName(workbook, dto, "RECV", "REQ");
							addDataIndividual(workbook.getSheet(sheetName), dto.getMsglayoutbsDto());
							addDataGrid(workbook.getSheet(sheetName), dto.getMsglayoutbsDto(), startGridRowNum,
									endGridRowNum);
						}
					} else {
						if (workbook.getSheet(dto.getMsgLayoutId()) == null) {
							String sheetName = getSheetCopyName(workbook, dto, "RECV", "RES");
							addDataIndividual(workbook.getSheet(sheetName), dto.getMsglayoutbsDto());
							addDataGrid(workbook.getSheet(sheetName), dto.getMsglayoutbsDto(), startGridRowNum,
									endGridRowNum);
						}
					}
				}

				int reqMappingSeq = workbook.getSheetIndex("reqMapping");
				int resMappingSeq = workbook.getSheetIndex("resMapping");

				if (intrfcDto.getMsgTrnsfrmYn().equals("Y")) {
					if (intrfcDto.getIntrfccombsMappingReqDto() != null
							&& intrfcDto.getIntrfccombsMappingResDto() != null) {
						logger.debug("111111111111111111111111111111111111111111111111111111111");
						Sheet sReq = workbook.cloneSheet(reqMappingSeq);
						int sheetindex = workbook.getSheetIndex(sReq.getSheetName());
						String sheetNameReq = "REQ_Mapping";
						workbook.setSheetName(sheetindex, sheetNameReq);

						Sheet sRes = workbook.cloneSheet(resMappingSeq);
						int sheetindex2 = workbook.getSheetIndex(sRes.getSheetName());
						String sheetNameRes = "RES_Mapping";
						workbook.setSheetName(sheetindex2, sheetNameRes);

						mappingDataReqSet(workbook.getSheet(sheetNameReq), intrfcDto);
						mappingDataResSet(workbook.getSheet(sheetNameRes), intrfcDto);

					} else if (intrfcDto.getIntrfccombsMappingReqDto() != null
							&& intrfcDto.getIntrfccombsMappingResDto() == null) {
						logger.debug("222222222222222222222222222222222222222222222222222222222");
						Sheet sReq = workbook.cloneSheet(reqMappingSeq);
						int sheetindex = workbook.getSheetIndex(sReq.getSheetName());
						String sheetNameReq = "REQ_Mapping";
						workbook.setSheetName(sheetindex, sheetNameReq);
						mappingDataReqSet(workbook.getSheet(sheetNameReq), intrfcDto);
					} else if (intrfcDto.getIntrfccombsMappingReqDto() == null
							&& intrfcDto.getIntrfccombsMappingResDto() != null) {
						logger.debug("3333333333333333333333333333333333333333333333333333333333");
						Sheet sRes = workbook.cloneSheet(resMappingSeq);
						int sheetindex2 = workbook.getSheetIndex(sRes.getSheetName());
						String sheetNameRes = "RES_Mapping";
						workbook.setSheetName(sheetindex2, sheetNameRes);
						// mappingDataResSet(workbook.getSheet(sheetNameRes), intrfcDto);
					}
					workbook.removeSheetAt(workbook.getSheetIndex("reqMapping"));
					workbook.removeSheetAt(workbook.getSheetIndex("resMapping"));
				} else {
					// logger.debug("workbook.getSheetIndex(\"reqMapping\") : {}",
					// workbook.getSheetIndex("reqMapping"));
					// logger.debug("workbook.getSheetIndex(\"resMapping\") : {}",
					// workbook.getSheetIndex("resMapping"));
					//
					// workbook.removeSheetAt(workbook.getSheetIndex("reqMapping"));
					// workbook.removeSheetAt(workbook.getSheetIndex("resMapping"));
				}

			} else {
				logger.debug(
						"FEPFEPFEPFEPFEPFEPFEPFEPFEPFEPFEPFEPFEPFEPFEPFEPFEPFEPFEPFEPFEPFEPFEPFEPFEPFEPFEPFEPFEPFEPFEPFEPFEPFEPFEPFEPFEPFEPFEPFEPFEPFEPFEPFEPFEPFEPFEPFEPFEPFEPFEPFEPFEPFEPFEPFEPFEPFEPFEPFEPFEPFEPFEPFEP");
				List<IntrfcmsglayoutdtDto> intrfcmsglayoutdtDto = intrfcDto.getIntrfcmsglayoutdtDto();

				if (intrfcDto.getTrxDscd().equals("ONLINE")) {
					addFEPInterface(workbook.getSheet("대외온라인"), intrfcDto);
					workbook.removeSheetAt(workbook.getSheetIndex("대외배치"));
				} else {
					addFEPInterface(workbook.getSheet("대외배치"), intrfcDto);
					workbook.removeSheetAt(workbook.getSheetIndex("대외온라인"));
				}

				for (IntrfcmsglayoutdtDto dto : intrfcmsglayoutdtDto) {
					String srType = dto.getSrTypeCd();
					String reqResType = dto.getRqstRspsTypeCd();

					if (srType.equals("SEND") && reqResType.equals("REQUEST")) {
						if (workbook.getSheet(dto.getMsgLayoutId()) == null) {
							String sheetName = getSheetCopyName(workbook, dto, "SEND", "REQ");
							addDataIndividual(workbook.getSheet(sheetName), dto.getMsglayoutbsDto());
							addDataGrid(workbook.getSheet(sheetName), dto.getMsglayoutbsDto(), startGridRowNum,
									endGridRowNum);
						}
					} else if (srType.equals("SEND") && reqResType.equals("RESPONSE")) {
						if (workbook.getSheet(dto.getMsgLayoutId()) == null) {
							String sheetName = getSheetCopyName(workbook, dto, "SEND", "RES");
							addDataIndividual(workbook.getSheet(sheetName), dto.getMsglayoutbsDto());
							addDataGrid(workbook.getSheet(sheetName), dto.getMsglayoutbsDto(), startGridRowNum,
									endGridRowNum);
						}
					} else if (srType.equals("RECEIVE") && reqResType.equals("REQUEST")) {
						if (workbook.getSheet(dto.getMsgLayoutId()) == null) {
							String sheetName = getSheetCopyName(workbook, dto, "RECV", "REQ");
							addDataIndividual(workbook.getSheet(sheetName), dto.getMsglayoutbsDto());
							addDataGrid(workbook.getSheet(sheetName), dto.getMsglayoutbsDto(), startGridRowNum,
									endGridRowNum);
						}
					} else {
						if (workbook.getSheet(dto.getMsgLayoutId()) == null) {
							String sheetName = getSheetCopyName(workbook, dto, "RECV", "RES");
							addDataIndividual(workbook.getSheet(sheetName), dto.getMsglayoutbsDto());
							addDataGrid(workbook.getSheet(sheetName), dto.getMsglayoutbsDto(), startGridRowNum,
									endGridRowNum);
						}
					}
				}

				int reqMappingSeq = workbook.getSheetIndex("reqMapping");
				int resMappingSeq = workbook.getSheetIndex("resMapping");

				if (intrfcDto.getMsgTrnsfrmYn().equals("Y")) {
					if (intrfcDto.getIntrfccombsMappingReqDto() != null
							&& intrfcDto.getIntrfccombsMappingResDto() != null) {
						logger.debug("111111111111111111111111111111111111111111111111111111111");
						Sheet sReq = workbook.cloneSheet(reqMappingSeq);
						int sheetindex = workbook.getSheetIndex(sReq.getSheetName());
						String sheetNameReq = "REQ_Mapping";
						workbook.setSheetName(sheetindex, sheetNameReq);

						Sheet sRes = workbook.cloneSheet(resMappingSeq);
						int sheetindex2 = workbook.getSheetIndex(sRes.getSheetName());
						String sheetNameRes = "RES_Mapping";
						workbook.setSheetName(sheetindex2, sheetNameRes);

						mappingDataReqSet(workbook.getSheet(sheetNameReq), intrfcDto);
						mappingDataResSet(workbook.getSheet(sheetNameRes), intrfcDto);

					} else if (intrfcDto.getIntrfccombsMappingReqDto() != null
							&& intrfcDto.getIntrfccombsMappingResDto() == null) {
						logger.debug("222222222222222222222222222222222222222222222222222222222");
						Sheet sReq = workbook.cloneSheet(reqMappingSeq);
						int sheetindex = workbook.getSheetIndex(sReq.getSheetName());
						String sheetNameReq = "REQ_Mapping";
						workbook.setSheetName(sheetindex, sheetNameReq);
						mappingDataReqSet(workbook.getSheet(sheetNameReq), intrfcDto);
					} else {
						logger.debug("3333333333333333333333333333333333333333333333333333333333");
						Sheet sRes = workbook.cloneSheet(resMappingSeq);
						int sheetindex2 = workbook.getSheetIndex(sRes.getSheetName());
						String sheetNameRes = "RES_Mapping";
						workbook.setSheetName(sheetindex2, sheetNameRes);
						// mappingDataResSet(workbook.getSheet(sheetNameRes), intrfcDto);
					}

					workbook.removeSheetAt(workbook.getSheetIndex("reqMapping"));
					workbook.removeSheetAt(workbook.getSheetIndex("resMapping"));
				} else {
					// workbook.removeSheetAt(workbook.getSheetIndex("reqMapping"));
					// workbook.removeSheetAt(workbook.getSheetIndex("resMapping"));
				}

			}
			workbook.removeSheetAt(workbook.getSheetIndex("sendReq1"));
		} catch (Exception e) {
			logger.error("{}", e);
//			throw new Exception(e);
			throw new ServiceException(BxMessages.Error.SYSTEM_EXCEPTION);
		}
		return;
	}

	private void addEAIInterface(Sheet sheet, IntrfccombsDto intrfcDto, String intrfcWayCd) {

		String trxDscd = intrfcDto.getTrxDscd();

		if (trxDscd.equals("ONLINE") && intrfcWayCd.equals("APTOAP")) {
			ExcelUtils.writeValue(sheet, 3, "I", intrfcDto.getRegDttm()); // date
			ExcelUtils.writeValue(sheet, 3, "M", intrfcDto.getRegManId());// 작성자
			ExcelUtils.writeValue(sheet, 5, "E", intrfcDto.getIntrfcId());// 인터페이스아이디
			ExcelUtils.writeValue(sheet, 5, "M", intrfcDto.getIntrfcNm());// 인터페이스명
			ExcelUtils.writeValue(sheet, 6, "E", getCodeValue("SENC_RECV_DSCD", intrfcDto.getSrTypeCd(), "ko"));// 송수신구분
			ExcelUtils.writeValue(sheet, 6, "M", getCodeValue("SYNC_DSCD", intrfcDto.getSyncAsyncDscd(), "ko"));// 동기구분
			ExcelUtils.writeValue(sheet, 7, "E", getCodeValue("TRAN_DSCD", intrfcDto.getTrxDscd(), "ko"));// 거래유형
			ExcelUtils.writeValue(sheet, 7, "M", getCodeValue("INTRFC_WAY_CD", intrfcDto.getIntrfcWayCd(), "ko"));// 인터페이스방식
			ExcelUtils.writeValue(sheet, 11, "E", intrfcDto.getRspsYn());// 응답여부
			for (IntrfcsrsysdtDto dto : intrfcDto.getIntrfcsrsysdtDto()) {
				if (dto.getSrTypeCd().equals("SEND")) {
					ExcelUtils.writeValue(sheet, 8, "E", dto.getSysCd());// 송신시스템코드
					ExcelUtils.writeValue(sheet, 9, "E", dto.getSysNm());// 송신시스템명
					ExcelUtils.writeValue(sheet, 10, "E", dto.getCrgManNm());// 송신시스템명
				} else if (dto.getSrTypeCd().equals("RECEIVE")) {
					ExcelUtils.writeValue(sheet, 8, "M", dto.getSysCd());// 수신시스템코드
					ExcelUtils.writeValue(sheet, 9, "M", dto.getSysNm());// 수신시스템명
					ExcelUtils.writeValue(sheet, 10, "M", dto.getCrgManNm());// 수
					ExcelUtils.writeValue(sheet, 11, "M", dto.getTrxCd());// 수신시스템거래코드
				}
			}
			ExcelUtils.writeValue(sheet, 12, "E", intrfcDto.getMsgTrnsfrmYn());// 전문변환여부

			// 어플리케이션코드 L1~L4
			ExcelUtils.writeValue(sheet, 14, "E", intrfcDto.getLv1Cd());
			ExcelUtils.writeValue(sheet, 14, "G", intrfcDto.getLv2Cd());
			ExcelUtils.writeValue(sheet, 14, "H", intrfcDto.getLv3Cd());

//			ExcelUtils.writeValue(sheet, 15, "E", intrfcDto.getEaiDto().getIntrfcUse());// 인터페이스용도
			ExcelUtils.writeValue(sheet, 16, "E",
					intrfcDto.getEaiDto().getIntrfDesc() == null ? "" : intrfcDto.getEaiDto().getIntrfDesc());// 비고

			// 상세정보
			ExcelUtils.writeValue(sheet, 19, "E", intrfcDto.getEaiDto().getTimeOut()); // 타임아웃
			ExcelUtils.writeValue(sheet, 19, "I",
					getCodeValue("GEN_CYCLE_CD", intrfcDto.getEaiDto().getOccurCycle(), "ko"));// 발생주기
			ExcelUtils.writeValue(sheet, 19, "M", intrfcDto.getEaiDto().getDayOccurCnt());// 일발생건수
			ExcelUtils.writeValue(sheet, 20, "E", intrfcDto.getEaiDto().getPrivacyInclYn());// 개인정보포함여부
			ExcelUtils.writeValue(sheet, 20, "I", intrfcDto.getEaiDto().getCurrIntrfcIdentifier());// 현행인터페이스식별자
			ExcelUtils.writeValue(sheet, 20, "M", getCodeValue("BACKUP_CD", intrfcDto.getEaiDto().getBackupAprvStat(), "ko"));// 백업승인상태확인
			ExcelUtils.writeValue(sheet, 21, "E", intrfcDto.getEaiDto().getEncTargetYn());// 암호화대상여부

			// 송수신시스템전문정보
			for (IntrfcmsglayoutdtDto dto : intrfcDto.getIntrfcmsglayoutdtDto()) {
				String srType = dto.getSrTypeCd();
				String reqResType = dto.getRqstRspsTypeCd();

				if (srType.equals("SEND") && reqResType.equals("REQUEST")) {
					if (dto.getRqstRspsSeq() == 1) {
						ExcelUtils.writeValue(sheet, 27, "C", dto.getMsgLayoutId());
					} else if (dto.getRqstRspsSeq() == 2) {
						ExcelUtils.writeValue(sheet, 28, "C", dto.getMsgLayoutId());
					} else {
						ExcelUtils.writeValue(sheet, 29, "C", dto.getMsgLayoutId());
					}
				} else if (srType.equals("SEND") && reqResType.equals("RESPONSE")) {
					if (dto.getRqstRspsSeq() == 1) {
						ExcelUtils.writeValue(sheet, 27, "G", dto.getMsgLayoutId());
					} else if (dto.getRqstRspsSeq() == 2) {
						ExcelUtils.writeValue(sheet, 28, "G", dto.getMsgLayoutId());
					} else {
						ExcelUtils.writeValue(sheet, 29, "G", dto.getMsgLayoutId());
					}
				} else if (srType.equals("RECEIVE") && reqResType.equals("REQUEST")) {
					if (dto.getRqstRspsSeq() == 1) {
						ExcelUtils.writeValue(sheet, 27, "J", dto.getMsgLayoutId());
					} else if (dto.getRqstRspsSeq() == 2) {
						ExcelUtils.writeValue(sheet, 28, "J", dto.getMsgLayoutId());
					} else {
						ExcelUtils.writeValue(sheet, 29, "J", dto.getMsgLayoutId());
					}
				} else {
					if (dto.getRqstRspsSeq() == 1) {
						ExcelUtils.writeValue(sheet, 27, "N", dto.getMsgLayoutId());
					} else if (dto.getRqstRspsSeq() == 2) {
						ExcelUtils.writeValue(sheet, 28, "N", dto.getMsgLayoutId());
					} else {
						ExcelUtils.writeValue(sheet, 29, "N", dto.getMsgLayoutId());
					}
				}
			}

			// 매핑정보
			if (intrfcDto.getMsgTrnsfrmYn().equals("Y")) {
				ExcelUtils.writeValue(sheet, 34, "C", "REQ_Mapping");
				ExcelUtils.writeValue(sheet, 34, "J", "RES_Mapping");
			}

		} else if (trxDscd.equals("ONLINE") && intrfcWayCd.equals("APTODB")) {
			ExcelUtils.writeValue(sheet, 3, "I", intrfcDto.getRegDttm()); // date
			ExcelUtils.writeValue(sheet, 3, "M", intrfcDto.getRegManId());// 작성자
			ExcelUtils.writeValue(sheet, 5, "E", intrfcDto.getIntrfcId());// 인터페이스아이디
			ExcelUtils.writeValue(sheet, 5, "M", intrfcDto.getIntrfcNm());// 인터페이스명
			ExcelUtils.writeValue(sheet, 6, "E", getCodeValue("SENC_RECV_DSCD", intrfcDto.getSrTypeCd(), "ko"));// 송수신구분
			ExcelUtils.writeValue(sheet, 6, "M", getCodeValue("SYNC_DSCD", intrfcDto.getSyncAsyncDscd(), "ko"));// 동기구분
			ExcelUtils.writeValue(sheet, 7, "E", getCodeValue("TRAN_DSCD", intrfcDto.getTrxDscd(), "ko"));// 거래유형
			ExcelUtils.writeValue(sheet, 7, "M", getCodeValue("INTRFC_WAY_CD", intrfcDto.getIntrfcWayCd(), "ko"));// 인터페이스방식
			for (IntrfcsrsysdtDto dto : intrfcDto.getIntrfcsrsysdtDto()) {
				if (dto.getSrTypeCd().equals("SEND")) {
					ExcelUtils.writeValue(sheet, 8, "E", dto.getSysCd());// 송신시스템코드
					ExcelUtils.writeValue(sheet, 9, "E", dto.getSysNm());//  송신시스템명
					ExcelUtils.writeValue(sheet, 10, "E", dto.getCrgManNm());//  송신시스템명
				} else if (dto.getSrTypeCd().equals("RECEIVE")) {
					ExcelUtils.writeValue(sheet, 8, "M", dto.getSysCd());// 수신시스템코드
					ExcelUtils.writeValue(sheet, 9, "M", dto.getSysNm());//  수신시스템명
					ExcelUtils.writeValue(sheet, 10, "M", dto.getCrgManNm());//  수신시스템명
				}
			}
			ExcelUtils.writeValue(sheet, 11, "E", intrfcDto.getRspsYn()); // 응답여부
			// ExcelUtils.writeValue(sheet, 11, "E", intrfcDto.getMsgTrnsfrmYn());//전문변환여부

			// 어플리케이션코드 L1~L4
			ExcelUtils.writeValue(sheet, 13, "E", intrfcDto.getLv1Cd());
			ExcelUtils.writeValue(sheet, 13, "G", intrfcDto.getLv2Cd());
			ExcelUtils.writeValue(sheet, 13, "H", intrfcDto.getLv3Cd());

//			ExcelUtils.writeValue(sheet, 14, "E", intrfcDto.getEaiDto().getIntrfcUse());// 인터페이스용도
			ExcelUtils.writeValue(sheet, 15, "E",
					intrfcDto.getEaiDto().getIntrfDesc() == null ? "" : intrfcDto.getEaiDto().getIntrfDesc());// 비고

			// db속성
			ExcelUtils.writeValue(sheet, 18, "E",
					getCodeValue("RECV_DB_ACT_TYPE", intrfcDto.getEaiDto().getRecvDbActionType(), "ko"));// 수신db동작유형
			ExcelUtils.writeValue(sheet, 18, "I", intrfcDto.getEaiDto().getLobColNm());// 랍컬럼명
			ExcelUtils.writeValue(sheet, 18, "M", intrfcDto.getEaiDto().getSearchProcCnt());// 조회처리건수
			ExcelUtils.writeValue(sheet, 19, "E", intrfcDto.getEaiDto().getErrSkipYn());// 에러skip여부
			ExcelUtils.writeValue(sheet, 19, "I", intrfcDto.getEaiDto().getRecvDbQuery());// 수신DBTO 쿼리문

			// 상세정보
			ExcelUtils.writeValue(sheet, 23, "E", intrfcDto.getEaiDto().getTimeOut()); // 타임아웃
			ExcelUtils.writeValue(sheet, 23, "I",
					getCodeValue("GEN_CYCLE_CD", intrfcDto.getEaiDto().getOccurCycle(), "ko"));// 발생주기
			ExcelUtils.writeValue(sheet, 23, "M", intrfcDto.getEaiDto().getDayOccurCnt());// 일발생건수
			ExcelUtils.writeValue(sheet, 24, "E", intrfcDto.getEaiDto().getPrivacyInclYn());// 개인정보포함여부
			ExcelUtils.writeValue(sheet, 24, "I", intrfcDto.getEaiDto().getCurrIntrfcIdentifier());// 현행인터페이스식별자
			ExcelUtils.writeValue(sheet, 24, "M", getCodeValue("BACKUP_CD", intrfcDto.getEaiDto().getBackupAprvStat(), "ko"));// 백업승인상태확인
			ExcelUtils.writeValue(sheet, 25, "E", intrfcDto.getEaiDto().getEncTargetYn());// 암호화대상여부

			// 송수신시스템전문정보
			for (IntrfcmsglayoutdtDto dto : intrfcDto.getIntrfcmsglayoutdtDto()) {
				String srType = dto.getSrTypeCd();
				String reqResType = dto.getRqstRspsTypeCd();

				if (srType.equals("SEND") && reqResType.equals("REQUEST")) {
					if (dto.getRqstRspsSeq() == 1) {
						ExcelUtils.writeValue(sheet, 31, "C", dto.getMsgLayoutId());
					} else if (dto.getRqstRspsSeq() == 2) {
						ExcelUtils.writeValue(sheet, 32, "C", dto.getMsgLayoutId());
					} else {
						ExcelUtils.writeValue(sheet, 33, "C", dto.getMsgLayoutId());
					}
				} else if (srType.equals("SEND") && reqResType.equals("RESPONSE")) {
					if (dto.getRqstRspsSeq() == 1) {
						ExcelUtils.writeValue(sheet, 31, "J", dto.getMsgLayoutId());
					} else if (dto.getRqstRspsSeq() == 2) {
						ExcelUtils.writeValue(sheet, 32, "J", dto.getMsgLayoutId());
					} else {
						ExcelUtils.writeValue(sheet, 33, "J", dto.getMsgLayoutId());
					}
				}
			}

		} else if (trxDscd.equals("BATCH") && intrfcWayCd.equals("DBTODB")) {// 대내배치. DBTODB
			ExcelUtils.writeValue(sheet, 3, "I", intrfcDto.getRegDttm()); // date
			ExcelUtils.writeValue(sheet, 3, "M", intrfcDto.getRegManId());// 작성자
			ExcelUtils.writeValue(sheet, 5, "E", intrfcDto.getIntrfcId());// 인터페이스아이디
			ExcelUtils.writeValue(sheet, 5, "M", intrfcDto.getIntrfcNm());// 인터페이스명
			ExcelUtils.writeValue(sheet, 6, "E", getCodeValue("SENC_RECV_DSCD", intrfcDto.getSrTypeCd(), "ko"));// 송수신구분
			ExcelUtils.writeValue(sheet, 6, "M", getCodeValue("TRAN_DSCD", intrfcDto.getTrxDscd(), "ko"));// 거래유형
			ExcelUtils.writeValue(sheet, 7, "E", getCodeValue("INTRFC_WAY_CD", intrfcDto.getIntrfcWayCd(), "ko"));// 인터페이스방식
			for (IntrfcsrsysdtDto dto : intrfcDto.getIntrfcsrsysdtDto()) {
				if (dto.getSrTypeCd().equals("SEND")) {
					ExcelUtils.writeValue(sheet, 8, "E", dto.getSysCd());// 송신시스템코드
					ExcelUtils.writeValue(sheet, 9, "E", dto.getSysNm());//  송신시스템명
					ExcelUtils.writeValue(sheet, 10, "E", dto.getCrgManNm());//  송신시스템명
				} else if (dto.getSrTypeCd().equals("RECEIVE")) {
					ExcelUtils.writeValue(sheet, 8, "M", dto.getSysCd());// 수신시스템코드
					ExcelUtils.writeValue(sheet, 9, "M", dto.getSysNm());//  수신시스템명
					ExcelUtils.writeValue(sheet, 10, "M", dto.getCrgManNm());//  수신시스템명
				}
			}
			// 어플리케이션코드 L1~L4
			ExcelUtils.writeValue(sheet, 12, "E", intrfcDto.getLv1Cd());
			ExcelUtils.writeValue(sheet, 12, "G", intrfcDto.getLv2Cd());
			ExcelUtils.writeValue(sheet, 12, "H", intrfcDto.getLv3Cd());

//			ExcelUtils.writeValue(sheet, 13, "E", intrfcDto.getEaiDto().getIntrfcUse());// 인터페이스용도
			ExcelUtils.writeValue(sheet, 14, "E",
					intrfcDto.getEaiDto().getIntrfDesc() == null ? "" : intrfcDto.getEaiDto().getIntrfDesc());// 비고

			// DB속성
			ExcelUtils.writeValue(sheet, 17, "E",
					getCodeValue("RECV_DB_ACT_TYPE", intrfcDto.getEaiDto().getRecvDbActionType(), "ko"));// 수신db동작유형
			ExcelUtils.writeValue(sheet, 17, "I", intrfcDto.getEaiDto().getLobColNm());// 랍컬럼명
			ExcelUtils.writeValue(sheet, 17, "M", intrfcDto.getEaiDto().getSearchProcCnt());// 조회처리건수
			ExcelUtils.writeValue(sheet, 18, "E", intrfcDto.getEaiDto().getErrSkipYn());// 에러skip여부
			ExcelUtils.writeValue(sheet, 19, "E", intrfcDto.getEaiDto().getSendDbQuery());// 송신DB쿼리
			ExcelUtils.writeValue(sheet, 19, "I", intrfcDto.getEaiDto().getRecvDbQuery());// 수신DBTO 쿼리문

			// 상세정보
			ExcelUtils.writeValue(sheet, 23, "E", intrfcDto.getEaiDto().getPrivacyInclYn());// 개인정보포함여부
			ExcelUtils.writeValue(sheet, 23, "I", intrfcDto.getEaiDto().getCurrIntrfcIdentifier());// 현행인터페이스식별자
			ExcelUtils.writeValue(sheet, 23, "M", getCodeValue("BACKUP_CD", intrfcDto.getEaiDto().getBackupAprvStat(), "ko"));// 백업승인상태확인
			ExcelUtils.writeValue(sheet, 24, "E",
					getCodeValue("GEN_CYCLE_CD", intrfcDto.getEaiDto().getOccurCycle(), "ko"));// 발생주기
			ExcelUtils.writeValue(sheet, 24, "I", intrfcDto.getEaiDto().getDayOccurCnt());// 일발생건수
			ExcelUtils.writeValue(sheet, 24, "M", intrfcDto.getEaiDto().getEncTargetYn());// 암호화대상여부

		} else if (trxDscd.equals("BATCH") && intrfcWayCd.equals("FILETOFILE")) {// 대내배치. FILETOFILE

			ExcelUtils.writeValue(sheet, 3, "H", intrfcDto.getRegDttm()); // date
			ExcelUtils.writeValue(sheet, 3, "L", intrfcDto.getRegManId());// 작성자
			ExcelUtils.writeValue(sheet, 5, "E", intrfcDto.getIntrfcId());// 인터페이스아이디
			ExcelUtils.writeValue(sheet, 5, "L", intrfcDto.getIntrfcNm());// 인터페이스명
			ExcelUtils.writeValue(sheet, 6, "E", getCodeValue("SENC_RECV_DSCD", intrfcDto.getSrTypeCd(), "ko"));// 송수신구분
			ExcelUtils.writeValue(sheet, 6, "L", getCodeValue("TRAN_DSCD", intrfcDto.getTrxDscd(), "ko"));// 거래유형
			ExcelUtils.writeValue(sheet, 7, "E", getCodeValue("INTRFC_WAY_CD", intrfcDto.getIntrfcWayCd(), "ko"));// 인터페이스방식
			for (IntrfcsrsysdtDto dto : intrfcDto.getIntrfcsrsysdtDto()) {
				if (dto.getSrTypeCd().equals("SEND")) {
					ExcelUtils.writeValue(sheet, 8, "E", dto.getSysCd());// 송신시스템코드
					ExcelUtils.writeValue(sheet, 9, "E", dto.getSysNm());// 송신시스템명
					ExcelUtils.writeValue(sheet, 10, "E", dto.getCrgManNm());// 송신시스템명
				} else if (dto.getSrTypeCd().equals("RECEIVE")) {
					ExcelUtils.writeValue(sheet, 8, "L", dto.getSysCd());// 수신시스템코드
					ExcelUtils.writeValue(sheet, 9, "L", dto.getSysNm());// 수신시스템명
					ExcelUtils.writeValue(sheet, 10, "L", dto.getCrgManNm());// 수신시스템명
				}
			}
			// 어플리케이션코드 L1~L4
			ExcelUtils.writeValue(sheet, 12, "E", intrfcDto.getLv1Cd());
			ExcelUtils.writeValue(sheet, 12, "F", intrfcDto.getLv2Cd());
			ExcelUtils.writeValue(sheet, 12, "G", intrfcDto.getLv3Cd());

//			ExcelUtils.writeValue(sheet, 13, "E", intrfcDto.getEaiDto().getIntrfcUse());// 인터페이스용도
			ExcelUtils.writeValue(sheet, 14, "E",
					intrfcDto.getEaiDto().getIntrfDesc() == null ? "" : intrfcDto.getEaiDto().getIntrfDesc());// 비고

			String sheetName = sheet.getSheetName();

			if (sheetName.equals("대외파일전송")) {

				// 대외상세정보
				ExcelUtils.writeValue(sheet, 17, "E", intrfcDto.getInstCd());// 기관코드
				ExcelUtils.writeValue(sheet, 17, "H", intrfcDto.getEaiDto().getFepTranIntrfcId());// 일괄전송인터페이스ID

				// 송신파일속성
				ExcelUtils.writeValue(sheet, 20, "E", intrfcDto.getEaiDto().getSendFilePath());// 파일경로
				ExcelUtils.writeValue(sheet, 20, "H", intrfcDto.getEaiDto().getSendFileNm());// 파일명
				ExcelUtils.writeValue(sheet, 20, "L",
						getCodeValue("TRAN_POST_PROC", intrfcDto.getEaiDto().getSendTranPostProc(), "ko"));// 전송후처리유형
				ExcelUtils.writeValue(sheet, 21, "E", intrfcDto.getEaiDto().getSendTranPostBackPath());// 전송후백업경로
				ExcelUtils.writeValue(sheet, 21, "H", intrfcDto.getEaiDto().getSendTranBeforeScript());//전송전선행스크립트
				ExcelUtils.writeValue(sheet, 21, "L", getCodeValue("TRAN_BEFORE_SCRIPT_TYPE", intrfcDto.getEaiDto().getSendTranBeforeScriptType(), "ko"));//전송전선행스크립트유형

				// 수신파일속성
				ExcelUtils.writeValue(sheet, 24, "E", intrfcDto.getEaiDto().getRecvFilePath());// 파일경로
				ExcelUtils.writeValue(sheet, 24, "H", intrfcDto.getEaiDto().getRecvFileNm());// 파일명
				ExcelUtils.writeValue(sheet, 24, "L", intrfcDto.getEaiDto().getRecvTranPostFinCreateYn());// 전송후fin파일생성여부
				ExcelUtils.writeValue(sheet, 25, "E", intrfcDto.getEaiDto().getRecvTranPostScript());// 전송후후행스크립트
				ExcelUtils.writeValue(sheet, 25, "H", getCodeValue("TRAN_POST_SCRIPT_TYPE", intrfcDto.getEaiDto().getRecvTranPostScriptType(), "ko"));//전송후후행스크립트유형

				// 상세정보
				ExcelUtils.writeValue(sheet, 28, "E",
						getCodeValue("GEN_CYCLE_CD", intrfcDto.getEaiDto().getOccurCycle(), "ko"));// 발생주기
				ExcelUtils.writeValue(sheet, 28, "H", intrfcDto.getEaiDto().getPrivacyInclYn());// 개인정보포함여부
				ExcelUtils.writeValue(sheet, 28, "L", getCodeValue("BACKUP_CD", intrfcDto.getEaiDto().getBackupAprvStat(), "ko"));// 백업승인상태확인
				ExcelUtils.writeValue(sheet, 29, "E", intrfcDto.getEaiDto().getEncTargetYn());// 암호화대상여부
				ExcelUtils.writeValue(sheet, 29, "H", intrfcDto.getEaiDto().getDayOccurCnt());// 일발생건수
				ExcelUtils.writeValue(sheet, 29, "L", intrfcDto.getEaiDto().getCurrIntrfcIdentifier());// 현행인터페이스식별자
				ExcelUtils.writeValue(sheet, 30, "E", intrfcDto.getEaiDto().getReqWrapperDtoNm());// 현행인터페이스식별자
				ExcelUtils.writeValue(sheet, 30, "H", intrfcDto.getEaiDto().getResWrapperDtoNm());// 현행인터페이스식별자
				ExcelUtils.writeValue(sheet, 30, "L", getCodeValue("RECORD_SEPARATOR_CD", intrfcDto.getEaiDto().getRecordSeparator(), "ko"));// 레코드구분자
				ExcelUtils.writeValue(sheet, 31, "E", intrfcDto.getEaiDto().getRecordSize());// 레코드사이즈

				// 송수신시스템전문정보
				for (IntrfcmsglayoutdtDto dto : intrfcDto.getIntrfcmsglayoutdtDto()) {
					String srType = dto.getSrTypeCd();
					String reqResType = dto.getRqstRspsTypeCd();

					if (srType.equals("SEND") && reqResType.equals("REQUEST")) {
						if (dto.getRqstRspsSeq() == 1) {
							ExcelUtils.writeValue(sheet, 37, "C", dto.getMsgLayoutId());
						} else if (dto.getRqstRspsSeq() == 2) {
							ExcelUtils.writeValue(sheet, 38, "C", dto.getMsgLayoutId());
						} else if (dto.getRqstRspsSeq() == 3) {
							ExcelUtils.writeValue(sheet, 39, "C", dto.getMsgLayoutId());
						} else if (dto.getRqstRspsSeq() == 4) {
							ExcelUtils.writeValue(sheet, 40, "C", dto.getMsgLayoutId());
						} else {
							ExcelUtils.writeValue(sheet, 41, "C", dto.getMsgLayoutId());
						}
					} else if (srType.equals("SEND") && reqResType.equals("RESPONSE")) {
						if (dto.getRqstRspsSeq() == 1) {
							ExcelUtils.writeValue(sheet, 37, "G", dto.getMsgLayoutId());
						} else if (dto.getRqstRspsSeq() == 2) {
							ExcelUtils.writeValue(sheet, 38, "G", dto.getMsgLayoutId());
						} else if (dto.getRqstRspsSeq() == 3) {
							ExcelUtils.writeValue(sheet, 39, "G", dto.getMsgLayoutId());
						} else if (dto.getRqstRspsSeq() == 4) {
							ExcelUtils.writeValue(sheet, 40, "G", dto.getMsgLayoutId());
						} else {
							ExcelUtils.writeValue(sheet, 41, "G", dto.getMsgLayoutId());
						}
					} else if (srType.equals("RECEIVE") && reqResType.equals("REQUEST")) {
						if (dto.getRqstRspsSeq() == 1) {
							ExcelUtils.writeValue(sheet, 37, "I", dto.getMsgLayoutId());
						} else if (dto.getRqstRspsSeq() == 2) {
							ExcelUtils.writeValue(sheet, 38, "I", dto.getMsgLayoutId());
						} else if (dto.getRqstRspsSeq() == 3) {
							ExcelUtils.writeValue(sheet, 39, "I", dto.getMsgLayoutId());
						} else if (dto.getRqstRspsSeq() == 4) {
							ExcelUtils.writeValue(sheet, 40, "I", dto.getMsgLayoutId());
						} else {
							ExcelUtils.writeValue(sheet, 41, "I", dto.getMsgLayoutId());
						}
					} else {
						if (dto.getRqstRspsSeq() == 1) {
							ExcelUtils.writeValue(sheet, 37, "M", dto.getMsgLayoutId());
						} else if (dto.getRqstRspsSeq() == 2) {
							ExcelUtils.writeValue(sheet, 38, "M", dto.getMsgLayoutId());
						} else if (dto.getRqstRspsSeq() == 3) {
							ExcelUtils.writeValue(sheet, 39, "M", dto.getMsgLayoutId());
						} else if (dto.getRqstRspsSeq() == 4) {
							ExcelUtils.writeValue(sheet, 40, "M", dto.getMsgLayoutId());
						} else {
							ExcelUtils.writeValue(sheet, 41, "M", dto.getMsgLayoutId());
						}
					}
				}

			} else {
				// 송신파일속성
				ExcelUtils.writeValue(sheet, 17, "E", intrfcDto.getEaiDto().getSendFilePath());// 파일경로
				ExcelUtils.writeValue(sheet, 17, "H", intrfcDto.getEaiDto().getSendFileNm());// 파일명
				ExcelUtils.writeValue(sheet, 17, "L",
						getCodeValue("TRAN_POST_PROC", intrfcDto.getEaiDto().getSendTranPostProc(), "ko"));// 전송후처리유형
				ExcelUtils.writeValue(sheet, 18, "E", intrfcDto.getEaiDto().getSendTranPostBackPath());// 전송후백업경로
				ExcelUtils.writeValue(sheet, 18, "H", intrfcDto.getEaiDto().getSendTranBeforeScript());//전송전선행스크립트
				ExcelUtils.writeValue(sheet, 18, "L", getCodeValue("TRAN_BEFORE_SCRIPT_TYPE", intrfcDto.getEaiDto().getSendTranBeforeScriptType(), "ko"));//전송전선행스크립트유형

				// 수신파일속성
				ExcelUtils.writeValue(sheet, 21, "E", intrfcDto.getEaiDto().getRecvFilePath());// 파일경로
				ExcelUtils.writeValue(sheet, 21, "H", intrfcDto.getEaiDto().getRecvFileNm());// 파일명
				ExcelUtils.writeValue(sheet, 21, "L", intrfcDto.getEaiDto().getRecvTranPostFinCreateYn());// 전송후fin파일생성여부
				ExcelUtils.writeValue(sheet, 22, "E", intrfcDto.getEaiDto().getRecvTranPostScript());// 전송후후행스크립트
				ExcelUtils.writeValue(sheet, 22, "H", getCodeValue("TRAN_POST_SCRIPT_TYPE", intrfcDto.getEaiDto().getRecvTranPostScriptType(), "ko"));//전송후후행스크립트유형

				// 상세정보
				ExcelUtils.writeValue(sheet, 25, "E",
						getCodeValue("GEN_CYCLE_CD", intrfcDto.getEaiDto().getOccurCycle(), "ko"));// 발생주기
				ExcelUtils.writeValue(sheet, 25, "H", intrfcDto.getEaiDto().getPrivacyInclYn());// 개인정보포함여부
				ExcelUtils.writeValue(sheet, 25, "L", getCodeValue("BACKUP_CD", intrfcDto.getEaiDto().getBackupAprvStat(), "ko"));// 백업승인상태확인
				ExcelUtils.writeValue(sheet, 26, "E", intrfcDto.getEaiDto().getEncTargetYn());// 암호화대상여부
				ExcelUtils.writeValue(sheet, 26, "H", intrfcDto.getEaiDto().getDayOccurCnt());// 일발생건수
				ExcelUtils.writeValue(sheet, 26, "L", intrfcDto.getEaiDto().getCurrIntrfcIdentifier());// 현행인터페이스식별자
				ExcelUtils.writeValue(sheet, 27, "E", intrfcDto.getEaiDto().getReqWrapperDtoNm());// 현행인터페이스식별자
				ExcelUtils.writeValue(sheet, 27, "H", intrfcDto.getEaiDto().getResWrapperDtoNm());// 현행인터페이스식별자
				ExcelUtils.writeValue(sheet, 27, "L", getCodeValue("RECORD_SEPARATOR_CD", intrfcDto.getEaiDto().getRecordSeparator(), "ko"));// 레코드구분자
				ExcelUtils.writeValue(sheet, 28, "E", intrfcDto.getEaiDto().getRecordSize());// 레코드사이즈

				// 송수신시스템전문정보
				for (IntrfcmsglayoutdtDto dto : intrfcDto.getIntrfcmsglayoutdtDto()) {
					String srType = dto.getSrTypeCd();
					String reqResType = dto.getRqstRspsTypeCd();

					if (srType.equals("SEND") && reqResType.equals("REQUEST")) {
						if (dto.getRqstRspsSeq() == 1) {
							ExcelUtils.writeValue(sheet, 34, "C", dto.getMsgLayoutId());
						} else if (dto.getRqstRspsSeq() == 2) {
							ExcelUtils.writeValue(sheet, 35, "C", dto.getMsgLayoutId());
						} else if (dto.getRqstRspsSeq() == 3) {
							ExcelUtils.writeValue(sheet, 36, "C", dto.getMsgLayoutId());
						} else if (dto.getRqstRspsSeq() == 4) {
							ExcelUtils.writeValue(sheet, 37, "C", dto.getMsgLayoutId());
						} else {
							ExcelUtils.writeValue(sheet, 38, "C", dto.getMsgLayoutId());
						}
					} else if (srType.equals("SEND") && reqResType.equals("RESPONSE")) {
						if (dto.getRqstRspsSeq() == 1) {
							ExcelUtils.writeValue(sheet, 34, "G", dto.getMsgLayoutId());
						} else if (dto.getRqstRspsSeq() == 2) {
							ExcelUtils.writeValue(sheet, 35, "G", dto.getMsgLayoutId());
						} else if (dto.getRqstRspsSeq() == 3) {
							ExcelUtils.writeValue(sheet, 36, "G", dto.getMsgLayoutId());
						} else if (dto.getRqstRspsSeq() == 4) {
							ExcelUtils.writeValue(sheet, 37, "G", dto.getMsgLayoutId());
						} else {
							ExcelUtils.writeValue(sheet, 38, "G", dto.getMsgLayoutId());
						}
					} else if (srType.equals("RECEIVE") && reqResType.equals("REQUEST")) {
						if (dto.getRqstRspsSeq() == 1) {
							ExcelUtils.writeValue(sheet, 34, "I", dto.getMsgLayoutId());
						} else if (dto.getRqstRspsSeq() == 2) {
							ExcelUtils.writeValue(sheet, 35, "I", dto.getMsgLayoutId());
						} else if (dto.getRqstRspsSeq() == 3) {
							ExcelUtils.writeValue(sheet, 36, "I", dto.getMsgLayoutId());
						} else if (dto.getRqstRspsSeq() == 4) {
							ExcelUtils.writeValue(sheet, 37, "I", dto.getMsgLayoutId());
						} else {
							ExcelUtils.writeValue(sheet, 38, "I", dto.getMsgLayoutId());
						}
					} else {
						if (dto.getRqstRspsSeq() == 1) {
							ExcelUtils.writeValue(sheet, 34, "M", dto.getMsgLayoutId());
						} else if (dto.getRqstRspsSeq() == 2) {
							ExcelUtils.writeValue(sheet, 35, "M", dto.getMsgLayoutId());
						} else if (dto.getRqstRspsSeq() == 3) {
							ExcelUtils.writeValue(sheet, 36, "M", dto.getMsgLayoutId());
						} else if (dto.getRqstRspsSeq() == 4) {
							ExcelUtils.writeValue(sheet, 37, "M", dto.getMsgLayoutId());
						} else {
							ExcelUtils.writeValue(sheet, 38, "M", dto.getMsgLayoutId());
						}
					}
				}
			}

		}

	}

	private void addFEPInterface(Sheet sheet, IntrfccombsDto intrfcDto) {

		String trxDscd = intrfcDto.getTrxDscd();

		if (trxDscd.equals("ONLINE")) {
			ExcelUtils.writeValue(sheet, 3, "H", intrfcDto.getRegDttm()); // date
			ExcelUtils.writeValue(sheet, 3, "L", intrfcDto.getRegManId());// 작성자
			ExcelUtils.writeValue(sheet, 5, "E", intrfcDto.getIntrfcId());// 인터페이스아이디
			ExcelUtils.writeValue(sheet, 5, "L", intrfcDto.getIntrfcNm());// 인터페이스명
			ExcelUtils.writeValue(sheet, 6, "E", getCodeValue("SENC_RECV_DSCD", intrfcDto.getSrTypeCd(), "ko"));// 송수신구분
			ExcelUtils.writeValue(sheet, 6, "L", getCodeValue("SYNC_DSCD_FEP", intrfcDto.getSyncAsyncDscd(), "ko"));// 동기구분
			ExcelUtils.writeValue(sheet, 7, "E", getCodeValue("TRAN_DSCD", intrfcDto.getTrxDscd(), "ko"));// 거래유형
			ExcelUtils.writeValue(sheet, 7, "L", intrfcDto.getMsgTrnsfrmYn());// 전문변환여부
			for (IntrfcsrsysdtDto dto : intrfcDto.getIntrfcsrsysdtDto()) {
				if (dto.getSrTypeCd().equals("SEND")) {
					ExcelUtils.writeValue(sheet, 8, "E", dto.getSysCd());// 송신시스템코드
					ExcelUtils.writeValue(sheet, 9, "E", dto.getSysNm());// 송신시스템명
					ExcelUtils.writeValue(sheet, 10, "E", dto.getCrgManNm());// 송신시스템명
				} else if (dto.getSrTypeCd().equals("RECEIVE")) {
					ExcelUtils.writeValue(sheet, 8, "L", dto.getSysCd());// 수신시스템코드
					ExcelUtils.writeValue(sheet, 9, "L", dto.getSysNm());// 수신시스템명
					ExcelUtils.writeValue(sheet, 10, "L", dto.getCrgManNm());// 수신시스템명
					ExcelUtils.writeValue(sheet, 11, "E", dto.getTrxCd());// 수신시스템거래코드
				}
			}
			// 어플리케이션코드 L1~L4
			ExcelUtils.writeValue(sheet, 13, "E", intrfcDto.getLv1Cd());
			ExcelUtils.writeValue(sheet, 13, "F", intrfcDto.getLv2Cd());
			ExcelUtils.writeValue(sheet, 13, "G", intrfcDto.getLv3Cd());

			ExcelUtils.writeValue(sheet, 14, "E", intrfcDto.getFepDto().getIntrfcUse());
			ExcelUtils.writeValue(sheet, 15, "E",
					intrfcDto.getFepDto().getIntrfDesc() == null ? "" : intrfcDto.getFepDto().getIntrfDesc());// 비고

			// 라우팅정보
			int i = 0;
			for (IntrfcroutinfodtDto dto : intrfcDto.getIntrfcroutinfodtDto()) {
				ExcelUtils.writeValue(sheet, 19 + i, "C", dto.getLenFldOffsetLen());
				ExcelUtils.writeValue(sheet, 19 + i, "F", dto.getFldDataLen());
				ExcelUtils.writeValue(sheet, 19 + i, "G", getCodeValue("CONDITION_CD", dto.getCndtCd(), "ko"));// condition
																												// G
				ExcelUtils.writeValue(sheet, 19 + i, "H", dto.getFldCfgVal());
				ExcelUtils.writeValue(sheet, 19 + i, "K", dto.getRutngDesc());// 설명 K
				i++;
			}

			// 상세정보
			ExcelUtils.writeValue(sheet, 24, "E", intrfcDto.getInstCd()); // 기관코드
			ExcelUtils.writeValue(sheet, 24, "H", intrfcDto.getRqstExtrnlMsgNo());
			ExcelUtils.writeValue(sheet, 24, "L", intrfcDto.getRspsExtrnlMsgNo());
			ExcelUtils.writeValue(sheet, 25, "E", intrfcDto.getBizCd());
			ExcelUtils.writeValue(sheet, 25, "H", intrfcDto.getFepDto().getTimeOut());
			ExcelUtils.writeValue(sheet, 25, "L",
					getCodeValue("TIMEOUT_PROC_CD", intrfcDto.getFepDto().getTimeOutProcMode(), "ko"));
			ExcelUtils.writeValue(sheet, 26, "E", intrfcDto.getFepDto().getDelayRspsYn());
			ExcelUtils.writeValue(sheet, 26, "H", intrfcDto.getFepDto().getCurrIntrfcIdentifier());
			ExcelUtils.writeValue(sheet, 26, "L", intrfcDto.getFepDto().getPrivacyInclYn());
			ExcelUtils.writeValue(sheet, 27, "E", intrfcDto.getFepDto().getEncTargetYn());
			ExcelUtils.writeValue(sheet, 27, "H", intrfcDto.getFepDto().getTrnsmsnErrResYn());
			ExcelUtils.writeValue(sheet, 27, "L", getCodeValue("BACKUP_CD", intrfcDto.getFepDto().getBackupAprvStat(), "ko"));
			ExcelUtils.writeValue(sheet, 28, "E", intrfcDto.getFepDto().getReqWrapperDtoNm());
			ExcelUtils.writeValue(sheet, 28, "H", intrfcDto.getFepDto().getResWrapperDtoNm());
			ExcelUtils.writeValue(sheet, 28, "L", intrfcDto.getFepDto().getRelationTrxCd());
			ExcelUtils.writeValue(sheet, 29, "E", intrfcDto.getFepDto().getCommNetworkIntrfcYn());//통신망인터페이스여부

			// 송수신시스템전문정보
			for (IntrfcmsglayoutdtDto dto : intrfcDto.getIntrfcmsglayoutdtDto()) {
				String srType = dto.getSrTypeCd();
				String reqResType = dto.getRqstRspsTypeCd();

				if (srType.equals("SEND") && reqResType.equals("REQUEST")) {
					if (dto.getRqstRspsSeq() == 1) {
						ExcelUtils.writeValue(sheet, 35, "C", dto.getMsgLayoutId());
					} else if (dto.getRqstRspsSeq() == 2) {
						ExcelUtils.writeValue(sheet, 36, "C", dto.getMsgLayoutId());
					} else {
						ExcelUtils.writeValue(sheet, 37, "C", dto.getMsgLayoutId());
					}
				} else if (srType.equals("SEND") && reqResType.equals("RESPONSE")) {
					if (dto.getRqstRspsSeq() == 1) {
						ExcelUtils.writeValue(sheet, 35, "G", dto.getMsgLayoutId());
					} else if (dto.getRqstRspsSeq() == 2) {
						ExcelUtils.writeValue(sheet, 36, "G", dto.getMsgLayoutId());
					} else {
						ExcelUtils.writeValue(sheet, 37, "G", dto.getMsgLayoutId());
					}
				} else if (srType.equals("RECEIVE") && reqResType.equals("REQUEST")) {
					if (dto.getRqstRspsSeq() == 1) {
						ExcelUtils.writeValue(sheet, 35, "I", dto.getMsgLayoutId());
					} else if (dto.getRqstRspsSeq() == 2) {
						ExcelUtils.writeValue(sheet, 36, "I", dto.getMsgLayoutId());
					} else {
						ExcelUtils.writeValue(sheet, 37, "I", dto.getMsgLayoutId());
					}
				} else {
					if (dto.getRqstRspsSeq() == 1) {
						ExcelUtils.writeValue(sheet, 35, "M", dto.getMsgLayoutId());
					} else if (dto.getRqstRspsSeq() == 2) {
						ExcelUtils.writeValue(sheet, 36, "M", dto.getMsgLayoutId());
					} else {
						ExcelUtils.writeValue(sheet, 37, "M", dto.getMsgLayoutId());
					}
				}
			}

			// 매핑정보
			if (intrfcDto.getMsgTrnsfrmYn().equals("Y")) {
				ExcelUtils.writeValue(sheet, 42, "C", "REQ_Mapping");
				ExcelUtils.writeValue(sheet, 42, "I", "RES_Mapping");
			}

		} else { // 대외배치
			ExcelUtils.writeValue(sheet, 3, "H", intrfcDto.getRegDttm()); // date
			ExcelUtils.writeValue(sheet, 3, "L", intrfcDto.getRegManId());// 작성자
			ExcelUtils.writeValue(sheet, 5, "E", intrfcDto.getIntrfcId());// 인터페이스아이디
			ExcelUtils.writeValue(sheet, 5, "L", intrfcDto.getIntrfcNm());// 인터페이스명
			ExcelUtils.writeValue(sheet, 6, "E", getCodeValue("SENC_RECV_DSCD", intrfcDto.getSrTypeCd(), "ko"));// 송수신구분
			ExcelUtils.writeValue(sheet, 6, "L", getCodeValue("TRAN_DSCD", intrfcDto.getTrxDscd(), "ko"));// 거래유형
			for (IntrfcsrsysdtDto dto : intrfcDto.getIntrfcsrsysdtDto()) {
				if (dto.getSrTypeCd().equals("SEND")) {
					ExcelUtils.writeValue(sheet, 7, "E", dto.getSysCd());// 송신시스템코드
					ExcelUtils.writeValue(sheet, 8, "E", dto.getSysNm());// 송신시스템명
					ExcelUtils.writeValue(sheet, 9, "E", dto.getFilePath());// 송신시스템거래코드
					ExcelUtils.writeValue(sheet, 10, "E", dto.getCrgManNm());// 송신시스템거래코드
				} else if (dto.getSrTypeCd().equals("RECEIVE")) {
					ExcelUtils.writeValue(sheet, 7, "L", dto.getSysCd());// 수신시스템코드
					ExcelUtils.writeValue(sheet, 8, "L", dto.getSysNm());// 수신시스템명
					ExcelUtils.writeValue(sheet, 9, "L", dto.getFilePath());// 수신시스템거래코드
					ExcelUtils.writeValue(sheet, 10, "L", dto.getCrgManNm());// 수신시스템거래코드
				}
			}
			// 어플리케이션코드 L1~L4
			ExcelUtils.writeValue(sheet, 12, "E", intrfcDto.getLv1Cd());
			ExcelUtils.writeValue(sheet, 12, "F", intrfcDto.getLv2Cd());
			ExcelUtils.writeValue(sheet, 12, "G", intrfcDto.getLv3Cd());

			ExcelUtils.writeValue(sheet, 13, "E", intrfcDto.getFepDto().getIntrfcUse());// 인터페이스용도
			ExcelUtils.writeValue(sheet, 14, "E",
					intrfcDto.getFepDto().getIntrfDesc() == null ? "" : intrfcDto.getFepDto().getIntrfDesc());// 비고

			// 전송정보
			ExcelUtils.writeValue(sheet, 17, "E", intrfcDto.getFepDto().getFileId()); // 파일ID
			ExcelUtils.writeValue(sheet, 17, "H", intrfcDto.getFepDto().getDupTrnsmsnAllwYn());// 중복전송허용여부
			ExcelUtils.writeValue(sheet, 17, "L", intrfcDto.getFepDto().getRecordSize());// 래코드사이즈
			ExcelUtils.writeValue(sheet, 18, "E",
					getCodeValue("RECORD_SEPARATOR_CD", intrfcDto.getFepDto().getRecordSeparator(), "ko"));// 레코드구분자
			ExcelUtils.writeValue(sheet, 18, "H", intrfcDto.getFepDto().getFileNm());// 파일명

			// 상세정보
			ExcelUtils.writeValue(sheet, 21, "E", intrfcDto.getInstCd());
			ExcelUtils.writeValue(sheet, 21, "H", intrfcDto.getBizCd());
			ExcelUtils.writeValue(sheet, 21, "L", intrfcDto.getFepDto().getCurrIntrfcIdentifier());
			ExcelUtils.writeValue(sheet, 22, "E", intrfcDto.getFepDto().getPrivacyInclYn());
//			ExcelUtils.writeValue(sheet, 22, "H",
//					getCodeValue("DUP_FILE_PROC_CD", intrfcDto.getFepDto().getDupFileProcMode(), "ko"));  //중복파일처리방법삭제
			ExcelUtils.writeValue(sheet, 22, "H", intrfcDto.getFepDto().getFileTranIntrfcId());
			ExcelUtils.writeValue(sheet, 22, "L",
					getCodeValue("GEN_CYCLE_CD", intrfcDto.getFepDto().getOccurCycle(), "ko"));
			ExcelUtils.writeValue(sheet, 23, "E", intrfcDto.getFepDto().getEncTargetYn());

			// 송수신시스템전문정보
			for (IntrfcmsglayoutdtDto dto : intrfcDto.getIntrfcmsglayoutdtDto()) {
				String srType = dto.getSrTypeCd();
				String reqResType = dto.getRqstRspsTypeCd();

				if (srType.equals("SEND") && reqResType.equals("REQUEST")) {
					if (dto.getRqstRspsSeq() == 1) {
						ExcelUtils.writeValue(sheet, 29, "C", dto.getMsgLayoutId());
					} else if (dto.getRqstRspsSeq() == 2) {
						ExcelUtils.writeValue(sheet, 30, "C", dto.getMsgLayoutId());
					} else if (dto.getRqstRspsSeq() == 3) {
						ExcelUtils.writeValue(sheet, 31, "C", dto.getMsgLayoutId());
					} else if (dto.getRqstRspsSeq() == 4) {
						ExcelUtils.writeValue(sheet, 32, "C", dto.getMsgLayoutId());
					} else {
						ExcelUtils.writeValue(sheet, 33, "C", dto.getMsgLayoutId());
					}
				} else if (srType.equals("SEND") && reqResType.equals("RESPONSE")) {
					if (dto.getRqstRspsSeq() == 1) {
						ExcelUtils.writeValue(sheet, 29, "G", dto.getMsgLayoutId());
					} else if (dto.getRqstRspsSeq() == 2) {
						ExcelUtils.writeValue(sheet, 30, "G", dto.getMsgLayoutId());
					} else if (dto.getRqstRspsSeq() == 3) {
						ExcelUtils.writeValue(sheet, 31, "G", dto.getMsgLayoutId());
					} else if (dto.getRqstRspsSeq() == 4) {
						ExcelUtils.writeValue(sheet, 32, "G", dto.getMsgLayoutId());
					} else {
						ExcelUtils.writeValue(sheet, 33, "G", dto.getMsgLayoutId());
					}
				} else if (srType.equals("RECEIVE") && reqResType.equals("REQUEST")) {
					if (dto.getRqstRspsSeq() == 1) {
						ExcelUtils.writeValue(sheet, 29, "I", dto.getMsgLayoutId());
					} else if (dto.getRqstRspsSeq() == 2) {
						ExcelUtils.writeValue(sheet, 30, "I", dto.getMsgLayoutId());
					} else if (dto.getRqstRspsSeq() == 3) {
						ExcelUtils.writeValue(sheet, 31, "I", dto.getMsgLayoutId());
					} else if (dto.getRqstRspsSeq() == 4) {
						ExcelUtils.writeValue(sheet, 32, "I", dto.getMsgLayoutId());
					} else {
						ExcelUtils.writeValue(sheet, 33, "I", dto.getMsgLayoutId());
					}
				} else {
					if (dto.getRqstRspsSeq() == 1) {
						ExcelUtils.writeValue(sheet, 29, "M", dto.getMsgLayoutId());
					} else if (dto.getRqstRspsSeq() == 2) {
						ExcelUtils.writeValue(sheet, 30, "M", dto.getMsgLayoutId());
					} else if (dto.getRqstRspsSeq() == 3) {
						ExcelUtils.writeValue(sheet, 31, "M", dto.getMsgLayoutId());
					} else if (dto.getRqstRspsSeq() == 4) {
						ExcelUtils.writeValue(sheet, 32, "M", dto.getMsgLayoutId());
					} else {
						ExcelUtils.writeValue(sheet, 33, "M", dto.getMsgLayoutId());
					}
				}
			}

		}

	}

	private void mappingDataResSet(Sheet sheet, IntrfccombsDto intrfcDto) {

		int startIndex = 5;
		int endIndext = 1784;
		int lastIndex = 0;

		Object[] array = new Object[6];
		boolean listSizeCheck = true;
		Map<String, Integer> msgSeqMap = new HashMap<String, Integer>();

		for (IntrfcmsglayoutdtDto dto : intrfcDto.getIntrfcmsglayoutdtDto()) {
			String srType = dto.getSrTypeCd();
			String reqResType = dto.getRqstRspsTypeCd();

			if (srType.equals("SEND") && reqResType.equals("RESPONSE")) {
				if (dto.getRqstRspsSeq() == 1) {
					array[0] = dto.getMsglayoutbsDto();
					listSizeCheck = false;
					// msgListReq.add(0, dto.getMsglayoutbsDto());
					// addDataGridMapping(sheet, dto.getMsglayoutbsDto(), startIndex, endIndext);
				} else if (dto.getRqstRspsSeq() == 2) {
					array[2] = dto.getMsglayoutbsDto();

					// msgListReq.add(2, dto.getMsglayoutbsDto());
				} else {

					array[4] = dto.getMsglayoutbsDto();
					// msgListReq.add(4, dto.getMsglayoutbsDto());
				}
			} else if (srType.equals("RECEIVE") && reqResType.equals("RESPONSE")) {
				if (dto.getRqstRspsSeq() == 1) {
					array[1] = dto.getMsglayoutbsDto();
					msgSeqMap.put(dto.getMsgLayoutId(), 1);
					listSizeCheck = false;
					// msgListReq.add(1, dto.getMsglayoutbsDto());
					// addDataGridMappingRes(sheet, dto.getMsglayoutbsDto(), startIndex, endIndext);
				} else if (dto.getRqstRspsSeq() == 2) {
					array[3] = dto.getMsglayoutbsDto();
					msgSeqMap.put(dto.getMsgLayoutId(), 2);
					// msgListReq.add(3, dto.getMsglayoutbsDto());
				} else {
					array[5] = dto.getMsglayoutbsDto();
					msgSeqMap.put(dto.getMsgLayoutId(), 3);
					// msgListReq.add(5, dto.getMsglayoutbsDto());
				}
			}
		}

		List<Object> msgListRes = Arrays.asList(array);

		if (listSizeCheck) {
			// Remove Rows
			logger.debug("-----------------------------lastIndex------------------------------------");
			logger.debug("{}", lastIndex);
			logger.debug("-----------------------------lastIndex------------------------------------");
			for (int i = startIndex - 1; i < 1784 - 1; i++) {
				sheet.removeRow(sheet.getRow(i));
			}
			// Shift Rows
			sheet.shiftRows(1784 - 1, 1784 - 1, -(1784 - startIndex), true, false);
		} else {

			if (((MsglayoutbsDto) msgListRes.get(0)).getMsglayoutdtDto().size() >= ((MsglayoutbsDto) msgListRes.get(1))
					.getMsglayoutdtDto().size()) {
				endIndext = ((MsglayoutbsDto) msgListRes.get(0)).getMsglayoutdtDto().size();
			} else {
				endIndext = ((MsglayoutbsDto) msgListRes.get(1)).getMsglayoutdtDto().size();
			}
			logger.debug("ENDINDEX : {}", endIndext);

			addDataGridMappingRes(sheet, (MsglayoutbsDto) msgListRes.get(1), startIndex, endIndext, "RECV", 1,
					intrfcDto, msgSeqMap);
			addDataGridMappingRes(sheet, (MsglayoutbsDto) msgListRes.get(0), startIndex, endIndext, "SEND", 1,
					intrfcDto, msgSeqMap);

			// 
			logger.debug("ENDINDEX-------------{}, {}, {}", endIndext, startIndex - 1, endIndext + 5 - 2);
			if (startIndex - 1 < endIndext + 5 - 2) {
				sheet.addMergedRegion(new CellRangeAddress(startIndex - 1, endIndext + 5 - 2, 1, 1));
				sheet.addMergedRegion(new CellRangeAddress(startIndex - 1, endIndext + 5 - 2, 11, 11));
				if (intrfcDto.getIntrfcTypeCd().equals("FEP") && intrfcDto.getTrxDscd().equals("ONLINE")) {
					sheet.addMergedRegion(new CellRangeAddress(startIndex - 1, endIndext + 5 - 2, 24, 24));
				} else {
					sheet.addMergedRegion(new CellRangeAddress(startIndex - 1, endIndext + 5 - 2, 21, 21));
				}
			}
			// sheet.addMergedRegion(new CellRangeAddress(startIndex - 1, endIndext + 5, 8,
			// 8));
			// sheet.addMergedRegion(new CellRangeAddress(startIndex - 1, endIndext + 5, 16,
			// 16));

			startIndex = endIndext + 5;
			lastIndex = endIndext + 5;

			logger.debug("startIndex : {}", startIndex);

			if (msgListRes.get(2) != null && msgListRes.get(3) != null) {
				if (((MsglayoutbsDto) msgListRes.get(2)).getMsglayoutdtDto()
						.size() >= ((MsglayoutbsDto) msgListRes.get(3)).getMsglayoutdtDto().size()) {
					endIndext = ((MsglayoutbsDto) msgListRes.get(2)).getMsglayoutdtDto().size();
				} else {
					endIndext = ((MsglayoutbsDto) msgListRes.get(3)).getMsglayoutdtDto().size();
				}
				addDataGridMappingRes(sheet, (MsglayoutbsDto) msgListRes.get(3), startIndex, endIndext, "RECV", 2,
						intrfcDto, msgSeqMap);
				addDataGridMappingRes(sheet, (MsglayoutbsDto) msgListRes.get(2), startIndex, endIndext, "SEND", 2,
						intrfcDto, msgSeqMap);
				logger.debug("ENDINDEX-------------{}, {}, {}", endIndext, startIndex - 1, endIndext + 5 - 2);
				if (startIndex - 1 < endIndext + startIndex - 2) {
					sheet.addMergedRegion(new CellRangeAddress(startIndex - 1, endIndext + startIndex - 2, 1, 1));
					sheet.addMergedRegion(new CellRangeAddress(startIndex - 1, endIndext + startIndex - 2, 11, 11));
					if (intrfcDto.getIntrfcTypeCd().equals("FEP") && intrfcDto.getTrxDscd().equals("ONLINE")) {
						sheet.addMergedRegion(new CellRangeAddress(startIndex - 1, endIndext + startIndex - 2, 24, 24));
					} else {
						sheet.addMergedRegion(new CellRangeAddress(startIndex - 1, endIndext + startIndex - 2, 21, 21));
					}
				}
				startIndex = startIndex + endIndext;
				lastIndex = lastIndex + endIndext;
			} else if (msgListRes.get(2) != null && msgListRes.get(3) == null) {
				endIndext = ((MsglayoutbsDto) msgListRes.get(2)).getMsglayoutdtDto().size();
				addDataGridMappingRes(sheet, (MsglayoutbsDto) msgListRes.get(2), startIndex, endIndext, "SEND", 2,
						intrfcDto, msgSeqMap);
				if (startIndex - 1 < endIndext + startIndex - 2) {
					sheet.addMergedRegion(new CellRangeAddress(startIndex - 1, endIndext + startIndex - 2, 1, 1));
					sheet.addMergedRegion(new CellRangeAddress(startIndex - 1, endIndext + startIndex - 2, 11, 11));
					if (intrfcDto.getIntrfcTypeCd().equals("FEP") && intrfcDto.getTrxDscd().equals("ONLINE")) {
						sheet.addMergedRegion(new CellRangeAddress(startIndex - 1, endIndext + startIndex - 2, 24, 24));
					} else {
						sheet.addMergedRegion(new CellRangeAddress(startIndex - 1, endIndext + startIndex - 2, 21, 21));
					}
				}
				startIndex = startIndex + endIndext;
				lastIndex = lastIndex + endIndext;
			} else if (msgListRes.get(2) == null && msgListRes.get(3) != null) {
				endIndext = ((MsglayoutbsDto) msgListRes.get(3)).getMsglayoutdtDto().size();
				addDataGridMappingRes(sheet, (MsglayoutbsDto) msgListRes.get(3), startIndex, endIndext, "RECV", 2,
						intrfcDto, msgSeqMap);
				if (startIndex - 1 < endIndext + startIndex - 2) {
					sheet.addMergedRegion(new CellRangeAddress(startIndex - 1, endIndext + startIndex - 2, 1, 1));
					sheet.addMergedRegion(new CellRangeAddress(startIndex - 1, endIndext + startIndex - 2, 11, 11));
					if (intrfcDto.getIntrfcTypeCd().equals("FEP") && intrfcDto.getTrxDscd().equals("ONLINE")) {
						sheet.addMergedRegion(new CellRangeAddress(startIndex - 1, endIndext + startIndex - 2, 24, 24));
					} else {
						sheet.addMergedRegion(new CellRangeAddress(startIndex - 1, endIndext + startIndex - 2, 21, 21));
					}
				}
				startIndex = startIndex + endIndext;
				lastIndex = lastIndex + endIndext;
			} else {
				// Remove Rows
				logger.debug("-----------------------------lastIndex------------------------------------");
				logger.debug("{}", lastIndex);
				logger.debug("-----------------------------lastIndex------------------------------------");
				for (int i = lastIndex - 1; i < 1784 - 1; i++) {
					sheet.removeRow(sheet.getRow(i));
				}
				// Shift Rows
				sheet.shiftRows(1784 - 1, 1784 - 1, -(1784 - lastIndex), true, false);
				return;
			}

			if (msgListRes.get(4) != null && msgListRes.get(5) != null) {
				if (((MsglayoutbsDto) msgListRes.get(4)).getMsglayoutdtDto()
						.size() >= ((MsglayoutbsDto) msgListRes.get(5)).getMsglayoutdtDto().size()) {
					endIndext = ((MsglayoutbsDto) msgListRes.get(4)).getMsglayoutdtDto().size();
				} else {
					endIndext = ((MsglayoutbsDto) msgListRes.get(5)).getMsglayoutdtDto().size();
				}
				addDataGridMappingRes(sheet, (MsglayoutbsDto) msgListRes.get(4), startIndex, endIndext, "SEND", 3,
						intrfcDto, msgSeqMap);
				addDataGridMappingRes(sheet, (MsglayoutbsDto) msgListRes.get(5), startIndex, endIndext, "RECV", 3,
						intrfcDto, msgSeqMap);
				if (startIndex - 1 < endIndext + startIndex - 2) {
					sheet.addMergedRegion(new CellRangeAddress(startIndex - 1, endIndext + startIndex - 2, 1, 1));
					sheet.addMergedRegion(new CellRangeAddress(startIndex - 1, endIndext + startIndex - 2, 11, 11));
					if (intrfcDto.getIntrfcTypeCd().equals("FEP") && intrfcDto.getTrxDscd().equals("ONLINE")) {
						sheet.addMergedRegion(new CellRangeAddress(startIndex - 1, endIndext + startIndex - 2, 24, 24));
					} else {
						sheet.addMergedRegion(new CellRangeAddress(startIndex - 1, endIndext + startIndex - 2, 21, 21));
					}
				}
				startIndex = startIndex + endIndext;
				lastIndex = lastIndex + endIndext;
			} else if (msgListRes.get(4) != null && msgListRes.get(5) == null) {
				endIndext = ((MsglayoutbsDto) msgListRes.get(4)).getMsglayoutdtDto().size();
				addDataGridMappingRes(sheet, (MsglayoutbsDto) msgListRes.get(4), startIndex, endIndext, "SEND", 2,
						intrfcDto, msgSeqMap);
				if (startIndex - 1 < endIndext + startIndex - 2) {
					sheet.addMergedRegion(new CellRangeAddress(startIndex - 1, endIndext + startIndex - 2, 1, 1));
					sheet.addMergedRegion(new CellRangeAddress(startIndex - 1, endIndext + startIndex - 2, 11, 11));
					if (intrfcDto.getIntrfcTypeCd().equals("FEP") && intrfcDto.getTrxDscd().equals("ONLINE")) {
						sheet.addMergedRegion(new CellRangeAddress(startIndex - 1, endIndext + startIndex - 2, 24, 24));
					} else {
						sheet.addMergedRegion(new CellRangeAddress(startIndex - 1, endIndext + startIndex - 2, 21, 21));
					}
				}
				startIndex = startIndex + endIndext;
				lastIndex = lastIndex + endIndext;
			} else if (msgListRes.get(4) == null && msgListRes.get(5) != null) {
				endIndext = ((MsglayoutbsDto) msgListRes.get(5)).getMsglayoutdtDto().size();
				addDataGridMappingRes(sheet, (MsglayoutbsDto) msgListRes.get(5), startIndex, endIndext, "RECV", 2,
						intrfcDto, msgSeqMap);
				if (startIndex - 1 < endIndext + startIndex - 2) {
					sheet.addMergedRegion(new CellRangeAddress(startIndex - 1, endIndext + startIndex - 2, 1, 1));
					sheet.addMergedRegion(new CellRangeAddress(startIndex - 1, endIndext + startIndex - 2, 11, 11));
					if (intrfcDto.getIntrfcTypeCd().equals("FEP") && intrfcDto.getTrxDscd().equals("ONLINE")) {
						sheet.addMergedRegion(new CellRangeAddress(startIndex - 1, endIndext + startIndex - 2, 24, 24));
					} else {
						sheet.addMergedRegion(new CellRangeAddress(startIndex - 1, endIndext + startIndex - 2, 21, 21));
					}
				}
				startIndex = startIndex + endIndext;
				lastIndex = lastIndex + endIndext;
			}
			// Remove Rows
			logger.debug("-----------------------------lastIndex------------------------------------");
			logger.debug("{}", lastIndex);
			logger.debug("-----------------------------lastIndex------------------------------------");
			for (int i = lastIndex - 1; i < 1784 - 1; i++) {
				sheet.removeRow(sheet.getRow(i));
			}
			// Shift Rows
			sheet.shiftRows(1784 - 1, 1784 - 1, -(1784 - lastIndex), true, false);
		}
	}

	private void mappingDataReqSet(Sheet sheet, IntrfccombsDto intrfcDto) {

		int startIndex = 5;
		int endIndext = 1784;
		int lastIndex = 0;

		Object[] array = new Object[6];
		boolean listSizeCheck = true;
		Map<String, Integer> msgSeqMap = new HashMap<String, Integer>();

		for (IntrfcmsglayoutdtDto dto : intrfcDto.getIntrfcmsglayoutdtDto()) {
			String srType = dto.getSrTypeCd();
			String reqResType = dto.getRqstRspsTypeCd();

			if (srType.equals("SEND") && reqResType.equals("REQUEST")) {
				if (dto.getRqstRspsSeq() == 1) {
					array[0] = dto.getMsglayoutbsDto();
					msgSeqMap.put(dto.getMsgLayoutId(), 1);
					listSizeCheck = false;
					// msgListReq.add(0, dto.getMsglayoutbsDto());
					// addDataGridMapping(sheet, dto.getMsglayoutbsDto(), startIndex, endIndext);
				} else if (dto.getRqstRspsSeq() == 2) {
					array[2] = dto.getMsglayoutbsDto();
					msgSeqMap.put(dto.getMsgLayoutId(), 2);
					// msgListReq.add(2, dto.getMsglayoutbsDto());
				} else {
					msgSeqMap.put(dto.getMsgLayoutId(), 3);
					array[4] = dto.getMsglayoutbsDto();
					// msgListReq.add(4, dto.getMsglayoutbsDto());
				}
			} else if (srType.equals("RECEIVE") && reqResType.equals("REQUEST")) {
				if (dto.getRqstRspsSeq() == 1) {
					array[1] = dto.getMsglayoutbsDto();
					listSizeCheck = false;
					// msgListReq.add(1, dto.getMsglayoutbsDto());
					// addDataGridMappingRes(sheet, dto.getMsglayoutbsDto(), startIndex, endIndext);
				} else if (dto.getRqstRspsSeq() == 2) {
					array[3] = dto.getMsglayoutbsDto();
					// msgListReq.add(3, dto.getMsglayoutbsDto());
				} else {
					array[5] = dto.getMsglayoutbsDto();
					// msgListReq.add(5, dto.getMsglayoutbsDto());
				}
			}
		}

		List<Object> msgListReq = Arrays.asList(array);

		if (listSizeCheck) {
			// Remove Rows
			logger.debug("-----------------------------lastIndex------------------------------------");
			logger.debug("{}", lastIndex);
			logger.debug("-----------------------------lastIndex------------------------------------");
			for (int i = startIndex - 1; i < 1784 - 1; i++) {
				sheet.removeRow(sheet.getRow(i));
			}
			// Shift Rows
			sheet.shiftRows(1784 - 1, 1784 - 1, -(1784 - startIndex), true, false);
		} else {

			if (((MsglayoutbsDto) msgListReq.get(0)).getMsglayoutdtDto().size() >= ((MsglayoutbsDto) msgListReq.get(1))
					.getMsglayoutdtDto().size()) {
				endIndext = ((MsglayoutbsDto) msgListReq.get(0)).getMsglayoutdtDto().size();
			} else {
				endIndext = ((MsglayoutbsDto) msgListReq.get(1)).getMsglayoutdtDto().size();
			}
			logger.debug("ENDINDEX : {}", endIndext);

			logger.debug("11111111111111111111111111111111111111111111111111111111111111111111111");
			logger.debug("{}", startIndex);
			logger.debug("{}", endIndext);
			logger.debug("11111111111111111111111111111111111111111111111111111111111111111111111");

			addDataGridMapping(sheet, (MsglayoutbsDto) msgListReq.get(0), startIndex, endIndext, "SEND", 1, intrfcDto,
					msgSeqMap);
			addDataGridMapping(sheet, (MsglayoutbsDto) msgListReq.get(1), startIndex, endIndext, "RECV", 1, intrfcDto,
					msgSeqMap);

			// 
			logger.debug("ENDINDEX-------------{}, {}, {}", endIndext, startIndex - 1, endIndext + 5 - 2);
			if (startIndex - 1 < endIndext + 5 - 2) {
				sheet.addMergedRegion(new CellRangeAddress(startIndex - 1, endIndext + 5 - 2, 1, 1));
				sheet.addMergedRegion(new CellRangeAddress(startIndex - 1, endIndext + 5 - 2, 11, 11));
				if (intrfcDto.getIntrfcTypeCd().equals("FEP") && intrfcDto.getTrxDscd().equals("ONLINE")) {
					sheet.addMergedRegion(new CellRangeAddress(startIndex - 1, endIndext + 5 - 2, 24, 24));
				} else {
					sheet.addMergedRegion(new CellRangeAddress(startIndex - 1, endIndext + 5 - 2, 21, 21));
				}
			}
			// sheet.addMergedRegion(new CellRangeAddress(startIndex - 1, endIndext + 5, 8,
			// 8));
			// sheet.addMergedRegion(new CellRangeAddress(startIndex - 1, endIndext + 5, 16,
			// 16));

			startIndex = endIndext + 5;
			lastIndex = endIndext + 5;

			logger.debug("startIndex : {}", startIndex);

			if (msgListReq.get(2) != null && msgListReq.get(3) != null) {
				if (((MsglayoutbsDto) msgListReq.get(2)).getMsglayoutdtDto()
						.size() >= ((MsglayoutbsDto) msgListReq.get(3)).getMsglayoutdtDto().size()) {
					endIndext = ((MsglayoutbsDto) msgListReq.get(2)).getMsglayoutdtDto().size();
				} else {
					endIndext = ((MsglayoutbsDto) msgListReq.get(3)).getMsglayoutdtDto().size();
				}
				logger.debug("22222222222222222222222222222222222222222222222222222222222222222222222");
				logger.debug("{}", startIndex);
				logger.debug("{}", endIndext);
				logger.debug("222222222222222222222222222222222222222222222222222222222222222222222222");
				addDataGridMapping(sheet, (MsglayoutbsDto) msgListReq.get(2), startIndex, endIndext, "SEND", 2,
						intrfcDto, msgSeqMap);
				addDataGridMapping(sheet, (MsglayoutbsDto) msgListReq.get(3), startIndex, endIndext, "RECV", 2,
						intrfcDto, msgSeqMap);
				logger.debug("ENDINDEX-------------{}, {}, {}", endIndext, startIndex - 1, endIndext + 5 - 2);
				if (startIndex - 1 < endIndext + startIndex - 2) {
					sheet.addMergedRegion(new CellRangeAddress(startIndex - 1, endIndext + startIndex - 2, 1, 1));
					sheet.addMergedRegion(new CellRangeAddress(startIndex - 1, endIndext + startIndex - 2, 11, 11));
					if (intrfcDto.getIntrfcTypeCd().equals("FEP") && intrfcDto.getTrxDscd().equals("ONLINE")) {
						sheet.addMergedRegion(new CellRangeAddress(startIndex - 1, endIndext + startIndex - 2, 24, 24));
					} else {
						sheet.addMergedRegion(new CellRangeAddress(startIndex - 1, endIndext + startIndex - 2, 21, 21));
					}
				}
				startIndex = startIndex + endIndext;
				lastIndex = lastIndex + endIndext;
			} else if (msgListReq.get(2) != null && msgListReq.get(3) == null) {
				endIndext = ((MsglayoutbsDto) msgListReq.get(2)).getMsglayoutdtDto().size();
				addDataGridMapping(sheet, (MsglayoutbsDto) msgListReq.get(2), startIndex, endIndext, "SEND", 2,
						intrfcDto, msgSeqMap);
				if (startIndex - 1 < endIndext + startIndex - 2) {
					sheet.addMergedRegion(new CellRangeAddress(startIndex - 1, endIndext + startIndex - 2, 1, 1));
					sheet.addMergedRegion(new CellRangeAddress(startIndex - 1, endIndext + startIndex - 2, 11, 11));
					if (intrfcDto.getIntrfcTypeCd().equals("FEP") && intrfcDto.getTrxDscd().equals("ONLINE")) {
						sheet.addMergedRegion(new CellRangeAddress(startIndex - 1, endIndext + startIndex - 2, 24, 24));
					} else {
						sheet.addMergedRegion(new CellRangeAddress(startIndex - 1, endIndext + startIndex - 2, 21, 21));
					}
				}
				startIndex = startIndex + endIndext;
				lastIndex = lastIndex + endIndext;
			} else if (msgListReq.get(2) == null && msgListReq.get(3) != null) {
				endIndext = ((MsglayoutbsDto) msgListReq.get(3)).getMsglayoutdtDto().size();
				addDataGridMapping(sheet, (MsglayoutbsDto) msgListReq.get(3), startIndex, endIndext, "RECV", 2,
						intrfcDto, msgSeqMap);
				if (startIndex - 1 < endIndext + startIndex - 2) {
					sheet.addMergedRegion(new CellRangeAddress(startIndex - 1, endIndext + startIndex - 2, 1, 1));
					sheet.addMergedRegion(new CellRangeAddress(startIndex - 1, endIndext + startIndex - 2, 11, 11));
					if (intrfcDto.getIntrfcTypeCd().equals("FEP") && intrfcDto.getTrxDscd().equals("ONLINE")) {
						sheet.addMergedRegion(new CellRangeAddress(startIndex - 1, endIndext + startIndex - 2, 24, 24));
					} else {
						sheet.addMergedRegion(new CellRangeAddress(startIndex - 1, endIndext + startIndex - 2, 21, 21));
					}
				}
				startIndex = startIndex + endIndext;
				lastIndex = lastIndex + endIndext;
			} else {
				// Remove Rows
				logger.debug("-----------------------------lastIndex------------------------------------");
				logger.debug("{}", lastIndex);
				logger.debug("-----------------------------lastIndex------------------------------------");
				for (int i = lastIndex - 1; i < 1784 - 1; i++) {
					sheet.removeRow(sheet.getRow(i));
				}
				// Shift Rows
				sheet.shiftRows(1784 - 1, 1784 - 1, -(1784 - lastIndex), true, false);
				return;
			}

			if (msgListReq.get(4) != null && msgListReq.get(5) != null) {
				if (((MsglayoutbsDto) msgListReq.get(4)).getMsglayoutdtDto()
						.size() >= ((MsglayoutbsDto) msgListReq.get(5)).getMsglayoutdtDto().size()) {
					endIndext = ((MsglayoutbsDto) msgListReq.get(4)).getMsglayoutdtDto().size();
				} else {
					endIndext = ((MsglayoutbsDto) msgListReq.get(5)).getMsglayoutdtDto().size();
				}
				logger.debug("444444444444444444444444444444444444444444444444444444444444444444444444");
				logger.debug("{}", startIndex);
				logger.debug("{}", endIndext);
				logger.debug("444444444444444444444444444444444444444444444444444444444444444444444444");

				addDataGridMapping(sheet, (MsglayoutbsDto) msgListReq.get(4), startIndex, endIndext, "SEND", 3,
						intrfcDto, msgSeqMap);
				addDataGridMapping(sheet, (MsglayoutbsDto) msgListReq.get(5), startIndex, endIndext, "RECV", 3,
						intrfcDto, msgSeqMap);
				if (startIndex - 1 < endIndext + startIndex - 2) {
					sheet.addMergedRegion(new CellRangeAddress(startIndex - 1, endIndext + startIndex - 2, 1, 1));
					sheet.addMergedRegion(new CellRangeAddress(startIndex - 1, endIndext + startIndex - 2, 11, 11));
					if (intrfcDto.getIntrfcTypeCd().equals("FEP") && intrfcDto.getTrxDscd().equals("ONLINE")) {
						sheet.addMergedRegion(new CellRangeAddress(startIndex - 1, endIndext + startIndex - 2, 24, 24));
					} else {
						sheet.addMergedRegion(new CellRangeAddress(startIndex - 1, endIndext + startIndex - 2, 21, 21));
					}
				}
				startIndex = startIndex + endIndext;
				lastIndex = lastIndex + endIndext;
			} else if (msgListReq.get(4) != null && msgListReq.get(5) == null) {
				endIndext = ((MsglayoutbsDto) msgListReq.get(4)).getMsglayoutdtDto().size();
				addDataGridMapping(sheet, (MsglayoutbsDto) msgListReq.get(4), startIndex, endIndext, "SEND", 2,
						intrfcDto, msgSeqMap);
				if (startIndex - 1 < endIndext + startIndex - 2) {
					sheet.addMergedRegion(new CellRangeAddress(startIndex - 1, endIndext + startIndex - 2, 1, 1));
					sheet.addMergedRegion(new CellRangeAddress(startIndex - 1, endIndext + startIndex - 2, 11, 11));
					if (intrfcDto.getIntrfcTypeCd().equals("FEP") && intrfcDto.getTrxDscd().equals("ONLINE")) {
						sheet.addMergedRegion(new CellRangeAddress(startIndex - 1, endIndext + startIndex - 2, 24, 24));
					} else {
						sheet.addMergedRegion(new CellRangeAddress(startIndex - 1, endIndext + startIndex - 2, 21, 21));
					}
				}
				startIndex = startIndex + endIndext;
				lastIndex = lastIndex + endIndext;
			} else if (msgListReq.get(4) == null && msgListReq.get(5) != null) {
				endIndext = ((MsglayoutbsDto) msgListReq.get(5)).getMsglayoutdtDto().size();
				addDataGridMapping(sheet, (MsglayoutbsDto) msgListReq.get(5), startIndex, endIndext, "RECV", 2,
						intrfcDto, msgSeqMap);
				if (startIndex - 1 < endIndext + startIndex - 2) {
					sheet.addMergedRegion(new CellRangeAddress(startIndex - 1, endIndext + startIndex - 2, 1, 1));
					sheet.addMergedRegion(new CellRangeAddress(startIndex - 1, endIndext + startIndex - 2, 11, 11));
					if (intrfcDto.getIntrfcTypeCd().equals("FEP") && intrfcDto.getTrxDscd().equals("ONLINE")) {
						sheet.addMergedRegion(new CellRangeAddress(startIndex - 1, endIndext + startIndex - 2, 24, 24));
					} else {
						sheet.addMergedRegion(new CellRangeAddress(startIndex - 1, endIndext + startIndex - 2, 21, 21));
					}
				}
				startIndex = startIndex + endIndext;
				lastIndex = lastIndex + endIndext;
			}
			// Remove Rows
			logger.debug("-----------------------------lastIndex------------------------------------");
			logger.debug("{}", lastIndex);
			logger.debug("-----------------------------lastIndex------------------------------------");
			for (int i = lastIndex - 1; i < 1784 - 1; i++) {
				sheet.removeRow(sheet.getRow(i));
			}
			// Shift Rows
			sheet.shiftRows(1784 - 1, 1784 - 1, -(1784 - lastIndex), true, false);
		}
	}

	private void addDataGridMapping(Sheet sheet, MsglayoutbsDto data, int startIndex, int endIndext, String srType,
			int seq, IntrfccombsDto intrfcDto, Map<String, Integer> msgSeqMap) {
		List<MsglayoutdtDto> layoutList = data.getMsglayoutdtDto();
		String intrfcType = intrfcDto.getIntrfcTypeCd();
		String trxDscd = intrfcDto.getTrxDscd();

		int index = startIndex;
		if (srType.equals("SEND")) {
			ExcelUtils.writeValue(sheet, index, "B", seq); // 전문순번
			for (MsglayoutdtDto layout : layoutList) {
				// ExcelUtils.writeValue(sheet, index, "B", 1); //전문순번
				ExcelUtils.writeValue(sheet, index, "C", layout.getMsgSeq());
				ExcelUtils.writeValue(sheet, index, "D", layout.getFldEngNm()); // 필드영문명
				ExcelUtils.writeValue(sheet, index, "E", layout.getFldKorNm()); // 한글명
				ExcelUtils.writeValue(sheet, index, "G", layout.getDataTypeNm()); // 데이터타입
				ExcelUtils.writeValue(sheet, index, "I", layout.getMsgLen()); // 길이
				ExcelUtils.writeValue(sheet, index, "J", layout.getDecimalLen()); // 소수점길이
				ExcelUtils.writeValue(sheet, index, "K", layout.getFldLvNo() == null ? 0 : layout.getFldLvNo()); // 뎁스
				index++;
			}
		} else if (srType.equals("RECV")) {
			int startMapIndex = 0;
			startMapIndex = index;
			ExcelUtils.writeValue(sheet, index, "L", seq); // 전문순번
			boolean oneWrite = false;
			boolean oneWrite2 = true;
			for (MsglayoutdtDto layout : layoutList) {
				// ExcelUtils.writeValue(sheet, index, "B", 1); //전문순번
				ExcelUtils.writeValue(sheet, index, "M", layout.getMsgSeq());
				ExcelUtils.writeValue(sheet, index, "N", layout.getFldEngNm()); // 필드영문명
				ExcelUtils.writeValue(sheet, index, "O", layout.getFldKorNm()); // 한글명
				ExcelUtils.writeValue(sheet, index, "Q", layout.getDataTypeNm()); // 데이터타입
				ExcelUtils.writeValue(sheet, index, "S", layout.getMsgLen()); // 길이
				ExcelUtils.writeValue(sheet, index, "T", layout.getDecimalLen()); // 소수점길이
				ExcelUtils.writeValue(sheet, index, "U", layout.getFldLvNo() == null ? 0 : layout.getFldLvNo()); // 뎁스

				for (IntrfccombsMappingDto mapping : intrfcDto.getIntrfccombsMappingReqDto()) {

					if (intrfcType.equals("FEP") && trxDscd.equals("ONLINE")) {
						if (layout.getFldUnqId().equals(mapping.getTargetData())) {
							ExcelUtils.writeValue(sheet, index, "Z",
									getCodeValue("MAPPING_DSCD", mapping.getMappingTypeCd(), "ko"));
							if (mapping.getMappingTypeCd().equals("PROPT")) {
								ExcelUtils.writeValue(sheet, index, "AA",
										mapping.getSrcData().substring(mapping.getSrcData().indexOf(".") + 1));
							}
							int seqN = 0;
							if (mapping.getMappingTypeCd().equals("PROPT") && oneWrite2) {
								logger.debug(
										"+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
								logger.debug("{}",
										mapping.getSrcData().substring(0, mapping.getSrcData().lastIndexOf(".")));
								logger.debug(
										"+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
								seqN = msgSeqMap
										.get(mapping.getSrcData().substring(0, mapping.getSrcData().lastIndexOf(".")));
								oneWrite = true;
								oneWrite2 = false;
							}
							String wideHalfDscd = mapping.getWideHalfCharCngCd();
							String encCode = mapping.getEncCd();
							String fldEncoding = mapping.getFldEncoding();
							if (wideHalfDscd != null && !wideHalfDscd.equals("")) {
								ExcelUtils.writeValue(sheet, index, "V", getCodeValue("WIDE_HALF_CHAR_CD", wideHalfDscd, "ko"));
							}
							if (encCode != null && !encCode.equals("")) {
								ExcelUtils.writeValue(sheet, index, "W", getCodeValue("ENC_CD_MAPPING", encCode, "ko"));
							}
							if (fldEncoding != null && !fldEncoding.equals("")) {
								ExcelUtils.writeValue(sheet, index, "X", getCodeValue("FIELD_ENCODING_ONL", fldEncoding, "ko"));
							}

							if (oneWrite) {
								ExcelUtils.writeValue(sheet, startMapIndex, "Y", seqN);
								oneWrite = false;
							}

						}

					} else {
						if (layout.getFldUnqId().equals(mapping.getTargetData())) {
							ExcelUtils.writeValue(sheet, index, "W",
									getCodeValue("MAPPING_DSCD", mapping.getMappingTypeCd(), "ko"));
							if (mapping.getMappingTypeCd().equals("PROPT")) {
								ExcelUtils.writeValue(sheet, index, "X",
										mapping.getSrcData().substring(mapping.getSrcData().indexOf(".") + 1));
							}
							int seqN = 0;
							if (mapping.getMappingTypeCd().equals("PROPT") && oneWrite2) {
								logger.debug(
										"+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
								logger.debug("{}",
										mapping.getSrcData().substring(0, mapping.getSrcData().lastIndexOf(".")));
								logger.debug(
										"+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
								seqN = msgSeqMap
										.get(mapping.getSrcData().substring(0, mapping.getSrcData().lastIndexOf(".")));
								oneWrite = true;
								oneWrite2 = false;
							}
							if (oneWrite) {
								ExcelUtils.writeValue(sheet, startMapIndex, "V", seqN);
								oneWrite = false;
							}
						}

					}

				}

				index++;
			}
		}

		boolean endFlag = false;

		if (endFlag) {

			// Remove Rows
			for (int i = index - 1; i < endIndext - 1; i++) {
				logger.debug("{}", i);
				sheet.removeRow(sheet.getRow(i));
			}

			// Shift Rows
			logger.debug("---------------------------------------------------------------------------------");
			logger.debug("{}", endIndext - 1);
			logger.debug("{}", -(endIndext - index));
			logger.debug("---------------------------------------------------------------------------------");
			sheet.shiftRows(endIndext - 1, endIndext - 1, -(endIndext - index), true, false);

		}
	}

	private void addDataGridMappingRes(Sheet sheet, MsglayoutbsDto data, int startIndex, int endIndext, String srType,
			int seq, IntrfccombsDto intrfcDto, Map<String, Integer> msgSeqMap) {
		List<MsglayoutdtDto> layoutList = data.getMsglayoutdtDto();
		String intrfcType = intrfcDto.getIntrfcTypeCd();
		String trxDscd = intrfcDto.getTrxDscd();

		int index = startIndex;
		if (srType.equals("RECV")) {
			ExcelUtils.writeValue(sheet, index, "B", seq); // 전문순번
			for (MsglayoutdtDto layout : layoutList) {
				// ExcelUtils.writeValue(sheet, index, "B", 1); //전문순번
				ExcelUtils.writeValue(sheet, index, "C", layout.getMsgSeq());
				ExcelUtils.writeValue(sheet, index, "D", layout.getFldEngNm()); // 필드영문명
				ExcelUtils.writeValue(sheet, index, "E", layout.getFldKorNm()); // 한글명
				ExcelUtils.writeValue(sheet, index, "G", layout.getDataTypeNm()); // 데이터타입
				ExcelUtils.writeValue(sheet, index, "I", layout.getMsgLen()); // 길이
				ExcelUtils.writeValue(sheet, index, "J", layout.getDecimalLen()); // 소수점길이
				ExcelUtils.writeValue(sheet, index, "K", layout.getFldLvNo() == null ? 0 : layout.getFldLvNo()); // 뎁스
				index++;
			}
		} else if (srType.equals("SEND")) {
			int startMapIndex = 0;
			startMapIndex = index;
			ExcelUtils.writeValue(sheet, index, "L", seq); // 전문순번
			boolean oneWrite = true;
			boolean oneWrite2 = true;
			for (MsglayoutdtDto layout : layoutList) {
				// ExcelUtils.writeValue(sheet, index, "B", 1); //전문순번
				ExcelUtils.writeValue(sheet, index, "M", layout.getMsgSeq());
				ExcelUtils.writeValue(sheet, index, "N", layout.getFldEngNm()); // 필드영문명
				ExcelUtils.writeValue(sheet, index, "O", layout.getFldKorNm()); // 한글명
				ExcelUtils.writeValue(sheet, index, "Q", layout.getDataTypeNm()); // 데이터타입
				ExcelUtils.writeValue(sheet, index, "S", layout.getMsgLen()); // 길이
				ExcelUtils.writeValue(sheet, index, "T", layout.getDecimalLen()); // 소수점길이
				ExcelUtils.writeValue(sheet, index, "U", layout.getFldLvNo() == null ? 0 : layout.getFldLvNo()); // 뎁스

				for (IntrfccombsMappingDto mapping : intrfcDto.getIntrfccombsMappingResDto()) {

					if (intrfcType.equals("FEP") && trxDscd.equals("ONLINE")) {
						if (layout.getFldUnqId().equals(mapping.getTargetData())) {
							ExcelUtils.writeValue(sheet, index, "Z",
									getCodeValue("MAPPING_DSCD", mapping.getMappingTypeCd(), "ko"));
							if (mapping.getMappingTypeCd().equals("PROPT")) {
								ExcelUtils.writeValue(sheet, index, "AA",
										mapping.getSrcData().substring(mapping.getSrcData().indexOf(".") + 1));
							}
							int seqN = 0;
							if (mapping.getMappingTypeCd().equals("PROPT") && oneWrite2) {
								logger.debug(
										"+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
								logger.debug("{}",
										mapping.getSrcData().substring(0, mapping.getSrcData().lastIndexOf(".")));
								logger.debug(
										"+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
								seqN = msgSeqMap
										.get(mapping.getSrcData().substring(0, mapping.getSrcData().lastIndexOf(".")));
								oneWrite2 = false;
							}
							if (oneWrite) {
								ExcelUtils.writeValue(sheet, startMapIndex, "Y", seqN);
								oneWrite = false;
							}

							String wideHalfDscd = mapping.getWideHalfCharCngCd();
							String encCode = mapping.getEncCd();
							String fldEncoding = mapping.getFldEncoding();
							if (wideHalfDscd != null && !wideHalfDscd.equals("")) {
								ExcelUtils.writeValue(sheet, index, "V", getCodeValue("WIDE_HALF_CHAR_CD", wideHalfDscd, "ko"));
							}
							if (encCode != null && !encCode.equals("")) {
								ExcelUtils.writeValue(sheet, index, "W", getCodeValue("ENC_CD_MAPPING", encCode, "ko"));
							}
							if (fldEncoding != null && !fldEncoding.equals("")) {
								ExcelUtils.writeValue(sheet, index, "X", getCodeValue("FIELD_ENCODING_ONL", fldEncoding, "ko"));
							}
						}
					} else {
						if (layout.getFldUnqId().equals(mapping.getTargetData())) {
							ExcelUtils.writeValue(sheet, index, "W",
									getCodeValue("MAPPING_DSCD", mapping.getMappingTypeCd(), "ko"));
							if (mapping.getMappingTypeCd().equals("PROPT")) {
								ExcelUtils.writeValue(sheet, index, "X",
										mapping.getSrcData().substring(mapping.getSrcData().indexOf(".") + 1));
							}
							int seqN = 0;
							if (mapping.getMappingTypeCd().equals("PROPT") && oneWrite2) {
								logger.debug(
										"+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
								logger.debug("{}",
										mapping.getSrcData().substring(0, mapping.getSrcData().lastIndexOf(".")));
								logger.debug(
										"+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
								seqN = msgSeqMap
										.get(mapping.getSrcData().substring(0, mapping.getSrcData().lastIndexOf(".")));
								oneWrite2 = false;
							}
							if (oneWrite) {
								ExcelUtils.writeValue(sheet, startMapIndex, "V", seqN);
								oneWrite = false;
							}
						}
					}
				}

				index++;
			}
		}

		boolean endFlag = false;

		if (endFlag) {

			// Remove Rows
			for (int i = index - 1; i < endIndext - 1; i++) {
				logger.debug("{}", i);
				sheet.removeRow(sheet.getRow(i));
			}

			// Shift Rows
			logger.debug("---------------------------------------------------------------------------------");
			logger.debug("{}", endIndext - 1);
			logger.debug("{}", -(endIndext - index));
			logger.debug("---------------------------------------------------------------------------------");
			sheet.shiftRows(endIndext - 1, endIndext - 1, -(endIndext - index), true, false);

		}
	}

	private void addCCInterface(Sheet sheet, IntrfccombsDto intrfcDto) {
		ExcelUtils.writeValue(sheet, 3, "I", intrfcDto.getRegDttm()); // date
		ExcelUtils.writeValue(sheet, 3, "M", intrfcDto.getRegManId());// 작성자
		ExcelUtils.writeValue(sheet, 5, "E", intrfcDto.getIntrfcId());// 인터페이스아이디
		ExcelUtils.writeValue(sheet, 5, "M", intrfcDto.getIntrfcNm());// 인터페이스명
		// ExcelUtils.writeValue(sheet, 6, "E", getCodeValue("SENC_RECV_DSCD",
		// intrfcDto.getSrTypeCd(), "ko"));//송수신구분
		ExcelUtils.writeValue(sheet, 6, "E", getCodeValue("TRAN_DSCD", intrfcDto.getTrxDscd(), "ko"));// 발생유형
		ExcelUtils.writeValue(sheet, 6, "M", getCodeValue("TRX_TYPE_DSCD_CC", intrfcDto.getTrxTypeDscd(), "ko"));// 거래유형
		for (IntrfcsrsysdtDto dto : intrfcDto.getIntrfcsrsysdtDto()) {
			if (dto.getSrTypeCd().equals("SEND")) {
				ExcelUtils.writeValue(sheet, 7, "E", dto.getSysCd());// 송신시스템코드
				ExcelUtils.writeValue(sheet, 8, "E", dto.getSysNm());//  송신시스템명
				ExcelUtils.writeValue(sheet, 9, "E", dto.getCrgManNm());
				// ExcelUtils.writeValue(sheet, 10, "E", dto.getTrxCd());//송신시스템거래코드
			} else if (dto.getSrTypeCd().equals("RECEIVE")) {
				ExcelUtils.writeValue(sheet, 7, "M", dto.getSysCd());// 수신시스템코드
				ExcelUtils.writeValue(sheet, 8, "M", dto.getSysNm());//  수신시스템명
				ExcelUtils.writeValue(sheet, 9, "M", dto.getCrgManNm());//  수신시스템명
				ExcelUtils.writeValue(sheet, 10, "M", dto.getTrxCd());// 수신시스템거래코드
			}
		}
		ExcelUtils.writeValue(sheet, 11, "E", intrfcDto.getLv1Cd());
		ExcelUtils.writeValue(sheet, 11, "G", intrfcDto.getLv2Cd());
		ExcelUtils.writeValue(sheet, 11, "H", intrfcDto.getLv3Cd());

		ExcelUtils.writeValue(sheet, 13, "E",
				intrfcDto.getCcDto().getIntrfDesc() == null ? "" : intrfcDto.getCcDto().getIntrfDesc());// 비고

		// 송수신시스템전문정보
		for (IntrfcmsglayoutdtDto dto : intrfcDto.getIntrfcmsglayoutdtDto()) {
			String srType = dto.getSrTypeCd();
			String reqResType = dto.getRqstRspsTypeCd();

			if (srType.equals("SEND") && reqResType.equals("REQUEST")) {
				if (dto.getRqstRspsSeq() == 1) {
					ExcelUtils.writeValue(sheet, 18, "C", dto.getMsgLayoutId());
				} else if (dto.getRqstRspsSeq() == 2) {
					ExcelUtils.writeValue(sheet, 19, "C", dto.getMsgLayoutId());
				} else {
					ExcelUtils.writeValue(sheet, 20, "C", dto.getMsgLayoutId());
				}
			} else if (srType.equals("SEND") && reqResType.equals("RESPONSE")) {
				if (dto.getRqstRspsSeq() == 1) {
					ExcelUtils.writeValue(sheet, 18, "J", dto.getMsgLayoutId());
				} else if (dto.getRqstRspsSeq() == 2) {
					ExcelUtils.writeValue(sheet, 19, "J", dto.getMsgLayoutId());
				} else {
					ExcelUtils.writeValue(sheet, 20, "J", dto.getMsgLayoutId());
				}
			}
		}

	}

	private void addMCIInterface(Sheet sheet, IntrfccombsDto intrfcDto) {
		ExcelUtils.writeValue(sheet, 3, "I", intrfcDto.getRegDttm()); // date
		ExcelUtils.writeValue(sheet, 3, "M", intrfcDto.getRegManId());// 작성자
		ExcelUtils.writeValue(sheet, 5, "E", intrfcDto.getIntrfcId());// 인터페이스아이디
		ExcelUtils.writeValue(sheet, 5, "M", intrfcDto.getIntrfcNm());// 인터페이스명
		// ExcelUtils.writeValue(sheet, 6, "E", getCodeValue("SENC_RECV_DSCD",
		// intrfcDto.getSrTypeCd(), "ko"));//송수신구분
		ExcelUtils.writeValue(sheet, 6, "M", getCodeValue("SYNC_DSCD", intrfcDto.getSyncAsyncDscd(), "ko"));// 동기구분
		ExcelUtils.writeValue(sheet, 7, "E", getCodeValue("TRAN_DSCD", intrfcDto.getTrxDscd(), "ko"));// 발생유형
		ExcelUtils.writeValue(sheet, 7, "M", getCodeValue("TRX_TYPE_DSCD", intrfcDto.getTrxTypeDscd(), "ko"));// 거래유형
		ExcelUtils.writeValue(sheet, 6, "E", intrfcDto.getMsgTrnsfrmYn());// 전문변환여부
		for (IntrfcsrsysdtDto dto : intrfcDto.getIntrfcsrsysdtDto()) {
			if (dto.getSrTypeCd().equals("SEND")) {
				ExcelUtils.writeValue(sheet, 8, "E", dto.getSysCd());// 송신시스템코드
				ExcelUtils.writeValue(sheet, 9, "E", dto.getSysNm());//  송신시스템명
				ExcelUtils.writeValue(sheet, 10, "E", dto.getCrgManNm());
				// ExcelUtils.writeValue(sheet, 10, "E", dto.getTrxCd());//송신시스템거래코드
			} else if (dto.getSrTypeCd().equals("RECEIVE")) {
				ExcelUtils.writeValue(sheet, 8, "M", dto.getSysCd());// 수신시스템코드
				ExcelUtils.writeValue(sheet, 9, "M", dto.getSysNm());//  수신시스템명
				ExcelUtils.writeValue(sheet, 10, "M", dto.getCrgManNm());//  수신시스템명
				ExcelUtils.writeValue(sheet, 11, "M", dto.getTrxCd());// 수신시스템거래코드
			}
		}
		ExcelUtils.writeValue(sheet, 11, "E", intrfcDto.getRspsYn());// 응답여부
		// 어플리케이션코드 L1~L4
		ExcelUtils.writeValue(sheet, 13, "E", intrfcDto.getLv1Cd());
		ExcelUtils.writeValue(sheet, 13, "G", intrfcDto.getLv2Cd());
		ExcelUtils.writeValue(sheet, 13, "H", intrfcDto.getLv3Cd());
		// ExcelUtils.writeValue(sheet, 12, "L", intrfcDto.getLv4Cd());

//		ExcelUtils.writeValue(sheet, 14, "E",
//				intrfcDto.getMciDto().getIntrfcUse() == null ? "" : intrfcDto.getMciDto().getIntrfcUse());// 인터페이스용도
		ExcelUtils.writeValue(sheet, 15, "E",
				intrfcDto.getMciDto().getIntrfDesc() == null ? "" : intrfcDto.getMciDto().getIntrfDesc());// 비고
		ExcelUtils.writeValue(sheet, 18, "E", intrfcDto.getMciDto().getTimeOut());// 타임아웃
		ExcelUtils.writeValue(sheet, 18, "I",
				getCodeValue("GEN_CYCLE_CD", intrfcDto.getMciDto().getOccurCycle(), "ko"));// 발생주기
		ExcelUtils.writeValue(sheet, 18, "M", intrfcDto.getMciDto().getDayOccurCnt());// 일발생건수
		//ExcelUtils.writeValue(sheet, 19, "E", intrfcDto.getMciDto().getPrivacyInclYn());// 개인정보포함여부
		//ExcelUtils.writeValue(sheet, 19, "Iㅗ", intrfcDto.getMciDto().getCurrIntrfcIdentifier());// 현행인터페이스식별자
		//ExcelUtils.writeValue(sheet, 19, "M", getCodeValue("BACKUP_CD", intrfcDto.getMciDto().getBackupAprvStat(), "ko"));// 백업승인상태확인
		//ExcelUtils.writeValue(sheet, 20, "E", intrfcDto.getMciDto().getEncTargetYn());// 암호화대상여부

		// 송수신시스템전문정보
		for (IntrfcmsglayoutdtDto dto : intrfcDto.getIntrfcmsglayoutdtDto()) {
			String srType = dto.getSrTypeCd();
			String reqResType = dto.getRqstRspsTypeCd();

			if (srType.equals("SEND") && reqResType.equals("REQUEST")) {
				if (dto.getRqstRspsSeq() == 1) {
					ExcelUtils.writeValue(sheet, 26, "C", dto.getMsgLayoutId());
				} else if (dto.getRqstRspsSeq() == 2) {
					ExcelUtils.writeValue(sheet, 27, "C", dto.getMsgLayoutId());
				} else {
					ExcelUtils.writeValue(sheet, 28, "C", dto.getMsgLayoutId());
				}
			} else if (srType.equals("SEND") && reqResType.equals("RESPONSE")) {
				if (dto.getRqstRspsSeq() == 1) {
					ExcelUtils.writeValue(sheet, 26, "G", dto.getMsgLayoutId());
				} else if (dto.getRqstRspsSeq() == 2) {
					ExcelUtils.writeValue(sheet, 27, "G", dto.getMsgLayoutId());
				} else {
					ExcelUtils.writeValue(sheet, 28, "G", dto.getMsgLayoutId());
				}
			} else if (srType.equals("RECEIVE") && reqResType.equals("REQUEST")) {
				if (dto.getRqstRspsSeq() == 1) {
					ExcelUtils.writeValue(sheet, 26, "J", dto.getMsgLayoutId());
				} else if (dto.getRqstRspsSeq() == 2) {
					ExcelUtils.writeValue(sheet, 27, "J", dto.getMsgLayoutId());
				} else {
					ExcelUtils.writeValue(sheet, 28, "J", dto.getMsgLayoutId());
				}
			} else {
				if (dto.getRqstRspsSeq() == 1) {
					ExcelUtils.writeValue(sheet, 26, "N", dto.getMsgLayoutId());
				} else if (dto.getRqstRspsSeq() == 2) {
					ExcelUtils.writeValue(sheet, 27, "N", dto.getMsgLayoutId());
				} else {
					ExcelUtils.writeValue(sheet, 28, "N", dto.getMsgLayoutId());
				}
			}
		}

		// 매핑정보
		if (intrfcDto.getMsgTrnsfrmYn().equals("Y")) {
			ExcelUtils.writeValue(sheet, 33, "C", "REQ_Mapping");
			ExcelUtils.writeValue(sheet, 33, "J", "RES_Mapping");
		}
	}

	public String getSheetCopyName(Workbook workbook, IntrfcmsglayoutdtDto dto, String sendRecvType,
			String reqResType) {

		Sheet s = workbook.cloneSheet(3);
		int sheetindex = workbook.getSheetIndex(s.getSheetName());
		// String sheetName = sendRecvType + "_" + reqResType + "_" +
		// dto.getMsgLayoutId();
		String sheetName = dto.getMsgLayoutId();
		logger.debug("==============================================================================================");
		logger.debug(sheetName);
		logger.debug("==============================================================================================");
		workbook.setSheetName(sheetindex, sheetName);

		return sheetName;

	}

	private void addDataIndividual(Sheet sheet, MsglayoutbsDto data) {
		ExcelUtils.writeValue(sheet, 3, "Q", data.getRegDttm());
		ExcelUtils.writeValue(sheet, 3, "T", data.getRegManId());

		ExcelUtils.writeValue(sheet, 5, "D", data.getMsgLayoutId());
		ExcelUtils.writeValue(sheet, 5, "J", data.getMsgNm());
		ExcelUtils.writeValue(sheet, 5, "N", getCodeValue("CHL_DSCD", data.getChlDscd(), "ko"));
		ExcelUtils.writeValue(sheet, 5, "T", getCodeValue("TRAN_DSCD", data.getTrxDscd(), "ko"));

		ExcelUtils.writeValue(sheet, 7, "J", data.getMsgDataVal());
		ExcelUtils.writeValue(sheet, 7, "N", getCodeValue("MSG_TYPE", data.getMsgDscd(), "ko"));
		ExcelUtils.writeValue(sheet, 7, "T", data.getJobId());

		ExcelUtils.writeValue(sheet, 8, "D", data.getLv1Cd());
		ExcelUtils.writeValue(sheet, 8, "E", data.getLv2Cd());
		ExcelUtils.writeValue(sheet, 8, "F", data.getLv3Cd());

		ExcelUtils.writeValue(sheet, 10, "D", data.getRsrvFldVal1());
		ExcelUtils.writeValue(sheet, 10, "J", data.getRsrvFldVal2());
		ExcelUtils.writeValue(sheet, 11, "D", data.getMsgDesc());

		ExcelUtils.writeValue(sheet, 11, "X", data.getExtrnlBizNm());
		ExcelUtils.writeValue(sheet, 11, "Y", String.valueOf(data.getMsgVersion()));
		ExcelUtils.writeValue(sheet, 11, "Z", data.getDtoNm());
		ExcelUtils.writeValue(sheet, 11, "AB", data.getBitMapCrtnYn());
		ExcelUtils.writeValue(sheet, 11, "AD", getCodeValue("ISO_DATA_TYPE", data.getIso8583DataTypeCd(), "ko"));
		ExcelUtils.writeValue(sheet, 11, "AH", getCodeValue("ISO_BITMAP_TYPE_CD", data.getBitMapTypeCd(), "ko"));
		ExcelUtils.writeValue(sheet, 10, "N", data.getSharedYn());
		ExcelUtils.writeValue(sheet, 10, "T", data.getCustApiYn());
		ExcelUtils.writeValue(sheet, 3, "V", data.getMsgRvsNo());

		return;
	}

	private int addDataGrid(Sheet sheet, MsglayoutbsDto data, int startIndex, int endIndex) {
		List<MsglayoutdtDto> layoutList = data.getMsglayoutdtDto();

		Map<String, MsglayoutdtDto> layoutTypeMap = new HashMap<String, MsglayoutdtDto>();

		int index = startIndex;
		for (MsglayoutdtDto layout : layoutList) {
			ExcelUtils.writeValue(sheet, index, "C", layout.getFldEngNm()); // 필드영문명
			ExcelUtils.writeValue(sheet, index, "D", layout.getFldKorNm()); // 한글명
			ExcelUtils.writeValue(sheet, index, "E", layout.getCfgDesc()); // 설명
			ExcelUtils.writeValue(sheet, index, "F", getCodeValue("DATA_TYPE", layout.getDataTypeNm(), "ko")); // 데이터타입
			ExcelUtils.writeValue(sheet, index, "G", layout.getMsgLen()); // 길이
			ExcelUtils.writeValue(sheet, index, "H", layout.getDecimalLen()); // 소수점길이
			ExcelUtils.writeValue(sheet, index, "I", layout.getFldLvNo() == null ? 0 : layout.getFldLvNo()); // 뎁스

			ExcelUtils.writeValue(sheet, index, "J", layout.getIndsYn());//필수여부J
			ExcelUtils.writeValue(sheet, index, "K", layout.getFldRmk()); //비고K
//			ExcelUtils.writeValue(sheet, index, "L", layout.getCdAttrNm());//코드속성L
			ExcelUtils.writeValue(sheet, index, "L", layout.getArraySizeRefVal());//배열참조M->L
//			ExcelUtils.writeValue(sheet, index, "N", getCodeValue("PRIVACY_CD", layout.getPrivacyDscd(), "ko"));//개인정보N
//			if (data.getChlDscd().equals("EXTERNAL")) {
//				ExcelUtils.writeValue(sheet, index, "O", getCodeValue("REPLACEKEY_EXT", layout.getReplKey(), "ko")); // 대체키
//			} else {
//				ExcelUtils.writeValue(sheet, index, "O", getCodeValue("REPLACEKEY_INT", layout.getReplKey(), "ko")); // 대체키
//			} //개인정보식별자O
//			ExcelUtils.writeValue(sheet, index, "P", layout.getEncYn());//암호화여부P
			ExcelUtils.writeValue(sheet, index, "M", getCodeValue("ALIGN_CD", layout.getAlignNm(), "ko"));//정렬Q->M
			ExcelUtils.writeValue(sheet, index, "N", layout.getBasicVal());//기본값R->N
//			ExcelUtils.writeValue(sheet, index, "S", layout.getKorInclYn());//한글포함여부S
			ExcelUtils.writeValue(sheet, index, "O", getCodeValue("IO_KEY", layout.getIoKey(), "ko"));//IO키T->O
			ExcelUtils.writeValue(sheet, index, "P", layout.getChildDtoNm());//하위IO명U->P
			ExcelUtils.writeValue(sheet, index, "Q", getCodeValue("FILLER_CD", layout.getFillerVal(), "ko"));//Filler->Q

			if (data.getTrxDscd().equals("ISOH")) {
//				ExcelUtils.writeValue(sheet, index, "AB", layout.getExtrnlMsgNoYn()); // 전문번호식별
			} else {
				ExcelUtils.writeValue(sheet, index, "X", layout.getExtrnlMsgNoYn()); // 전문번호식별
			}
			ExcelUtils.writeValue(sheet, index, "Y", layout.getExtrnlSrchKeyYn()); // 매칭키식별
			// ExcelUtils.writeValue(sheet, index, "X", layout.getExtrnlOffsetYn()); //오프셋식별
//			ExcelUtils.writeValue(sheet, index, "AC", layout.getIso8583SpecNo()); // Specification번호
//			ExcelUtils.writeValue(sheet, index, "AD",
//					getCodeValue("ISO_FLD_CHARSET_CD", layout.getIso8583FldCharSetCd(), "ko")); // ISO 필드 타입
//			ExcelUtils.writeValue(sheet, index, "AE", layout.getIso8583VariableYn()); // 가변여부
//			ExcelUtils.writeValue(sheet, index, "AF", getCodeValue("ISO_FLD_LEN_TYPE_CD", layout.getIso8583FldLenTypeCd(), "ko")); // 길이타입
//			ExcelUtils.writeValue(sheet, index, "AG", layout.getIso8583LenFldLen()); // 길이부길이
//			ExcelUtils.writeValue(sheet, index, "AH", layout.getIso8583LenFldInclYn()); // 길이포함여부
//			ExcelUtils.writeValue(sheet, index, "AI", layout.getIso8583LenFldCopyYn()); // 길이복사여부
//			ExcelUtils.writeValue(sheet, index, "AJ", layout.getIso8583FldMaxLen()); // 필드최대길이
//			ExcelUtils.writeValue(sheet, index, "AK", layout.getIso8583TrnsmsnDataLen()); // 전송데이터길이

			if (layout.getDataTypeNm().equals(BxCode.DataType.LAYOUT.toString())) {
				layoutTypeMap.put(layout.getFldUnqId(), layout);
			}

			index++;
		}

		try {

			// if (data.getRsrvFldVal3() == null || data.getRsrvFldVal3().equals("")
			// || data.getRsrvFldVal3().equals("0")) {
			int msgTotLength = 0;
			int result = 0;
			for (MsglayoutdtDto dto : layoutList) {
				// logger.debug("****msgLenTotal***** : {}", msgTotLength);
				if (!dto.getDataTypeNm().equals("LAYOUT")) {
					msgTotLength = getMsgTotalLen(msgTotLength, dto.getMsgLen(), dto, layoutTypeMap, result);
				}

			}

			data.setRsrvFldVal3(Integer.toString(msgTotLength));
			// }
		} catch (Exception e) {
			logger.error("{}", e);
			data.setRsrvFldVal3("0");
		}

		// Remove Rows
		for (int i = index - 1; i < endIndex - 1; i++) {
			sheet.removeRow(sheet.getRow(i));
		}

		// Shift Rows
		sheet.shiftRows(endIndex - 1, endIndex - 1, -(endIndex - index), true, false);

		// Formula
		String sum = "SUM(G" + (startIndex) + ":G" + (index - 1) + ")";
		ExcelUtils.writeFormula(sheet, index, "G", data.getRsrvFldVal3());

		return index;
	}

	public int getMsgTotalLen(int msgTotLength, int msgLen, MsglayoutdtDto dto,
			Map<String, MsglayoutdtDto> layoutTypeMap, int result) {

		int fldDepth = dto.getFldLvNo();
		String parentDtoNm = dto.getParentFldNm();
		int multiply = 0;
		int msgLenData = msgLen;

		if (fldDepth > 0) {
			for (int i = 0; i < fldDepth; i++) {
				MsglayoutdtDto msgDto = layoutTypeMap.get(parentDtoNm);
				if (isInteger(msgDto.getArraySizeRefVal())) {
					multiply = Integer.parseInt(msgDto.getArraySizeRefVal());
				} else {
					multiply = 1;
				}
				msgLenData = msgLenData * multiply;

				if (msgDto.getParentFldNm() != null && !msgDto.getParentFldNm().equals("")) {
					parentDtoNm = msgDto.getParentFldNm();
				}
			}
			msgTotLength = msgTotLength + msgLenData;
			return msgTotLength;
		} else {
			msgTotLength = msgTotLength + msgLen;
			return msgTotLength;
		}

		// if (dto.getFldLvNo() > 0 || dto.getDataTypeNm().equals("LAYOUT")) {
		// int multipl = 0;
		// MsglayoutdtDto msgDto = layoutTypeMap.get(dto.getParentFldNm());
		// String arraySize = msgDto.getArraySizeRefVal();
		// boolean isInt = isInteger(arraySize);
		//
		// if (isInt) {
		// multipl = Integer.parseInt(arraySize);
		// } else {
		// multipl = 1;
		// }
		// msgLen = msgLen * multipl;
		//// msgTotLength = msgTotLength + msgLen;
		//
		// if (dto.getFldLvNo() > 1) {
		// msgLen = getMsgTotalLen(msgTotLength, msgLen,
		// layoutTypeMap.get(dto.getParentFldNm()), layoutTypeMap, result);
		// }
		//
		// return msgLen ;
		//
		// } else {
		// msgTotLength = msgTotLength + msgLen;
		// return msgTotLength;
		// }
		//// logger.debug("dtoname : {}", dto.getFldEngNm());
		//// logger.debug("****msgLenTotal : {}", msgTotLength);

	}

	public boolean isInteger(String data) {

		try {
			Integer.parseInt(data);
		} catch (NumberFormatException e) {
			return false;
		} catch (NullPointerException e) {
			return false;
		}

		return true;

	}

	public String getCodeValue(String codeId, String codeValue, String locale) {

		CommCodeDto codeValueNmDto = codeDao.selectCommCode(codeId, codeValue, locale);
		String codeValueNm = "";
		if (codeValueNmDto != null) {
			codeValueNm = codeValueNmDto.getCdValNm();
		}

		return codeValueNm;

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
		String trxDscd = (String) model.get("trxDscd");

		String rootPath = request.getSession().getServletContext().getRealPath("/");
		logger.debug("----root Path----- : {}", rootPath);

		if (intrfcType.equals("EAI_I") || intrfcType.equals("EAI_E")) {
			intrfcType = "EAI";
		}
		String uuid = null;
		for (int i = 0; i < 10; i++) {
			uuid = UUID.randomUUID().toString();
			uuid = uuid.replace("-", "");
		}

		String templateFile = "/WEB-INF/classes/templates/EIMS_InterfaceTemplate_" + intrfcType + "_V0.2.xlsx";
		String decryptedFile = "/WEB-INF/classes/templates/temp/EIMS_InterfaceTemplate_" + uuid + "_Decrypted_V0.2.xlsx";

		Workbook workbook = null;

		try {

			logger.debug(rootPath + templateFile);
			logger.debug(rootPath + decryptedFile);
			
			File target = new File(rootPath + decryptedFile) ;
			copyFile(new File(rootPath + templateFile), target) ;

//			DrmUtil.decrypt(rootPath + templateFile, rootPath + decryptedFile);
			File decryptedInputFile = Optional.ofNullable(target).filter(File::exists)
					.filter(File::canRead).orElseThrow(() -> new IOException(rootPath + decryptedFile));

			logger.debug("Template file path : {}", decryptedFile);
			workbook = new XSSFWorkbook(decryptedInputFile);
			// workbook = WorkbookFactory.create(decryptedInputFile);
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
}
