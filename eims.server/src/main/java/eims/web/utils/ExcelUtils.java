package eims.web.utils;

import java.util.Optional;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class ExcelUtils {

	static public String readValue(Sheet sheet, int rowIdx, String colStr) {
		int colIdx = (colStr.length() == 2) ? colStr.charAt(1) - 65 + 26 : colStr.charAt(0) - 65;
		Cell cell = createCellIfAbsent(createRowIfAbsent(sheet, rowIdx - 1), colIdx);

		switch (cell.getCellTypeEnum()) {
		case STRING:
			System.err.println("String");
			return cell.getStringCellValue();
		case BOOLEAN:
			System.err.println("Boolean");
			return Boolean.toString(cell.getBooleanCellValue());
		case ERROR:
			System.err.println("Error");
			return Byte.toString(cell.getErrorCellValue());
		case NUMERIC:
			System.err.println("Numeric");
			return Long.valueOf((long) cell.getNumericCellValue()).toString();
		case BLANK:
			System.err.println("BLANK");
		case _NONE:
			System.err.println("_NONE");			
			return "";
		case FORMULA:
			System.err.println("FORMULA");
			return cell.getCellFormula();
		
		default:
			System.err.println("default");
			return null;
		}
	}


	static public boolean writeValue(Sheet sheet, int rowIdx, String colStr, Object value) {
		int colIdx = (colStr.length() == 2) ? colStr.charAt(1) - 65 + 26 : colStr.charAt(0) - 65;
		
		Cell cell = createCellIfAbsent(createRowIfAbsent(sheet, rowIdx - 1), colIdx);

		if (value instanceof Integer) {
			cell.setCellValue((int) value);
		} else {
			cell.setCellValue((String) value);
		}
		return true;
	}
	
	static public boolean writeValue(Sheet sheet, int rowIdx, int colIdx, Object value) {
		Cell cell = createCellIfAbsent(createRowIfAbsent(sheet, rowIdx - 1), colIdx);

		if (value instanceof Integer) {
			cell.setCellValue((int) value);
		} else {
			cell.setCellValue((String) value);
		}
		return true;
	}


	static public boolean writeFormula(Sheet sheet, int rowIdx, String colStr, String value) {
		int colIdx = (colStr.length() == 2) ? colStr.charAt(1) - 65 + 26 : colStr.charAt(0) - 65;
		Cell cell = createCellIfAbsent(createRowIfAbsent(sheet, rowIdx - 1), colIdx);
		cell.setCellFormula(value);

		return true;
	}


	static public Row createRowIfAbsent(Sheet sheet, int rowIndex) {
		return Optional.ofNullable(sheet.getRow(rowIndex)).orElseGet(() -> sheet.createRow(rowIndex));
	}


	static public Cell createCellIfAbsent(Row row, int columnIndex) {
		return Optional.ofNullable(row.getCell(columnIndex)).orElseGet(() -> row.createCell(columnIndex));
	}


	static public Sheet createSheetIfAbsent(Workbook workbook, String sheetName) {
		return Optional.ofNullable(workbook.getSheet(sheetName)).orElseGet(() -> workbook.createSheet(sheetName));
	}

}
