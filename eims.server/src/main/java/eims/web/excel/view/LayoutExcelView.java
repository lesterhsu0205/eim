package eims.web.excel.view;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
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
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import com.google.common.io.Files;

import eims.web.constants.BxCode;
import eims.web.dao.CommCodeDao;
import eims.web.dto.table.CommCodeDto;
import eims.web.dto.table.MsglayoutbsDto;
import eims.web.dto.table.MsglayoutdtDto;
import eims.web.excel.drm.DrmUtil;
import eims.web.excel.drm.exception.FasooDrmException;
import eims.web.excel.drm.exception.FileTypeException;
import eims.web.utils.ExcelUtils;

public class LayoutExcelView extends AbstractXlsxView {

	final Logger logger = LoggerFactory.getLogger(getClass());

	private CommCodeDao codeDao;

	public LayoutExcelView(CommCodeDao codeDao) {
		this.codeDao = codeDao;
	}
	
	public String convertMsgKorToEng(String code) {
		// 일괄 엑셀 등록 템플릿 변경으로 인해 임시 유틸성 메서드			
		switch (code) {
			case "대내":
				return "Internal";
			case "대외":
				return "External";
			case "온라인":
				return "Online";
			case "배치":
				return "Batch";
			case "개별부":
				return "Non-Common Body";
			case "공통헤더":
				return "Common Header";
			case "배치Header":
				return "Batch Header";
			case "배치Body":
				return "Batch Body";
			case "배치Trailer":
				return "Batch Trailer";
			default:
				return null;
		}
	}

	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String attachment = "attachment; filename=\"" + model.get("msgLayoutId") + "_" + LocalDateTime.now()
				+ ".xlsx\"";
		response.setHeader("Content-Disposition", attachment);

		//final String layoutSheetName = "MessageLayo1ut";
		final String layoutSheetName = "Interface Design(D)";
		final int startGridRowNum = 12;
		final int endGridRowNum = 332;

		MsglayoutbsDto data = (MsglayoutbsDto) model.get("data");

		logger.debug("### data : {}", data);
		addDataIndividual(workbook.getSheet(layoutSheetName), data);
		addDataGrid(workbook.getSheet(layoutSheetName), data, startGridRowNum, endGridRowNum);

		return;
	}
	
	private void addDataIndividual(Sheet sheet, MsglayoutbsDto data) {
		ExcelUtils.writeValue(sheet, 3, "J", data.getRegDttm());
		ExcelUtils.writeValue(sheet, 3, "K", data.getRegManId());
		ExcelUtils.writeValue(sheet, 3, "L", data.getRegManId());

		ExcelUtils.writeValue(sheet, 5, "C", data.getMsgLayoutId());
		ExcelUtils.writeValue(sheet, 5, "F", data.getMsgNm());
		ExcelUtils.writeValue(sheet, 5, "I", convertMsgKorToEng(getCodeValue("CHL_DSCD", data.getChlDscd(), "ko")));
		ExcelUtils.writeValue(sheet, 5, "L", convertMsgKorToEng(getCodeValue("TRAN_DSCD", data.getTrxDscd(), "ko")));

		ExcelUtils.writeValue(sheet, 6, "F", data.getMsgDataVal());
		ExcelUtils.writeValue(sheet, 6, "I", convertMsgKorToEng(getCodeValue("MSG_TYPE", data.getMsgDscd(), "ko")));
		ExcelUtils.writeValue(sheet, 6, "L", data.getJobId());

		ExcelUtils.writeValue(sheet, 6, "C", data.getLv1Cd());

		ExcelUtils.writeValue(sheet, 7, "C", data.getRsrvFldVal1());
		ExcelUtils.writeValue(sheet, 7, "F", data.getRsrvFldVal2());
		ExcelUtils.writeValue(sheet, 8, "C", data.getMsgDesc());

		ExcelUtils.writeValue(sheet, 7, "I", data.getCustApiYn());
		return;
	}

	private int addDataGrid(Sheet sheet, MsglayoutbsDto data, int startIndex, int endIndex) {
		List<MsglayoutdtDto> layoutList = data.getMsglayoutdtDto();

		Map<String, MsglayoutdtDto> layoutTypeMap = new HashMap<String, MsglayoutdtDto>();
		int index = startIndex;
		logger.debug("### layoutList : {}", layoutList);
		for (MsglayoutdtDto layout : layoutList) {
			ExcelUtils.writeValue(sheet, index, "B", layout.getFldEngNm()); // 필드영문명
			ExcelUtils.writeValue(sheet, index, "C", layout.getFldKorNm()); // 한글명
			ExcelUtils.writeValue(sheet, index, "D", layout.getDataTypeNm()); // 데이터타입
			ExcelUtils.writeValue(sheet, index, "E", layout.getMsgLen()); // 길이
			ExcelUtils.writeValue(sheet, index, "F", layout.getDecimalLen()); // 소수점길이
			ExcelUtils.writeValue(sheet, index, "G", layout.getFldLvNo()); // 뎁스
			ExcelUtils.writeValue(sheet, index, "L", layout.getFldRmk()); //비고
			ExcelUtils.writeValue(sheet, index, "H", layout.getArraySizeRefVal());//배열참조
			ExcelUtils.writeValue(sheet, index, "J", layout.getAlignNm());//정렬Q->M
			ExcelUtils.writeValue(sheet, index, "I", layout.getChildDtoNm());//하위IO명
			ExcelUtils.writeValue(sheet, index, "K", layout.getFillerVal());//Padding

			if (layout.getDataTypeNm().equals(BxCode.DataType.LAYOUT.toString())) {
				layoutTypeMap.put(layout.getFldUnqId(), layout);
			}

			index++;
		}

		try {

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
		ExcelUtils.writeFormula(sheet, index, "E", data.getRsrvFldVal3());
		
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

	@Override
	protected Workbook createWorkbook(Map<String, Object> model, HttpServletRequest request) {
		String rootPath = request.getSession().getServletContext().getRealPath("/");

		String uuid = null;
		for (int i = 0; i < 10; i++) {
			uuid = UUID.randomUUID().toString();
			uuid = uuid.replace("-", "");
		}
		// 라이브 용
		String templateFile = "/WEB-INF/classes/templates/LBTW_BW_QAS_5.1.Interface Design_v1.0_20190325.xlsx";
		String copyFile = "/WEB-INF/classes/templates/temp/LBTW_BW_QAS_5.1.Interface Design_v1.0_" + uuid + ".xlsx";
		File decrypted = new File(rootPath + templateFile);
		File toWrite = new File(rootPath + copyFile);
	
		//local test용
//		String templateFile = "C:\\Users\\user\\Desktop\\temp\\template\\LBTW_BW_QAS_5.1.Interface Design_v1.0_20190325.xlsx";
//		String copyFile = "C:\\Users\\user\\Desktop\\temp\\template\\LBTW_BW_QAS_5.1.Interface Design_v1.0_" + uuid + ".xlsx";
//		File decrypted = new File(templateFile);
//		File toWrite = new File(copyFile);

		if (!decrypted.exists()) {
			decrypted = new File(templateFile);
		}
		logger.debug("decrypted file : {}", decrypted.getName());
		
		try {
			Files.copy(decrypted, toWrite);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Workbook workbook = null;

		try {
			File decryptedInputFile = Optional.ofNullable(toWrite).filter(File::exists)
					.filter(File::canRead).orElseThrow(() -> new IOException(rootPath + copyFile));

			logger.debug("Template file path : {}", templateFile);
			workbook = new XSSFWorkbook(decryptedInputFile);
			// workbook = WorkbookFactory.create(decryptedInputFile);
		} catch (EncryptedDocumentException e) {
			logger.error("{}", e);
		} catch (InvalidFormatException e) {
			logger.error("{}", e);
		} catch (IOException e) {
			logger.error("{}", e);
		} 
		return workbook;
	}
}
