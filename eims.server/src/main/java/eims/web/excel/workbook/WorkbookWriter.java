package eims.web.excel.workbook;

import java.awt.Color;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import eims.web.excel.workbook.exception.WriteLabelMismatchException;

public class WorkbookWriter {

	//  셀 배경색, 글자색
	private final static Color DEFAULT_LABEL_FILL_COLOR = Color.YELLOW;
	private final static Color DEFAULT_LABEL_FONT_COLOR = Color.BLACK;
	private final static Color DEFAULT_RECORD_FILL_COLOR = Color.WHITE;
	private final static Color DEFAULT_RECORD_FONT_COLOR = Color.BLACK;

	// 시트 잠금 비밀번호
	private final static String SHEET_PROTECTION_KEY = "";

	private Workbook workbook;
	private File outputFile;
	private Color labelFillColor;
	private Color labelFontColor;
	private Color normalFillColor;
	private Color normalFontColor;


	// 라벨의 배경색, 글자색 get, set
	public Color getLabelFillColor() {
		return labelFillColor;
	}


	public void setLabelFillColor(Color labelFillColor) {
		this.labelFillColor = labelFillColor;
	}


	public Color getLabelFontColor() {
		return labelFontColor;
	}


	public void setLabelFontColor(Color labelFontColor) {
		this.labelFontColor = labelFontColor;
	}


	// 라벨이 아닌 셀의 배경색, 글자색 get, set
	public Color getNormalFillColor() {
		return normalFillColor;
	}


	public void setNormalFillColor(Color normalFillColor) {
		this.normalFillColor = normalFillColor;
	}


	public Color getNormalFontColor() {
		return normalFontColor;
	}


	public void setNormalFontColor(Color normalFontColor) {
		this.normalFontColor = normalFontColor;
	}


	public WorkbookWriter(File outputFile) {

		workbook = new XSSFWorkbook();
		this.outputFile = outputFile;
		labelFillColor = DEFAULT_LABEL_FILL_COLOR;
		labelFontColor = DEFAULT_LABEL_FONT_COLOR;
		normalFillColor = DEFAULT_RECORD_FILL_COLOR;
		normalFontColor = DEFAULT_RECORD_FONT_COLOR;
	}


	public void close() throws IOException {

		workbook.close();
	}


	public void writeSparsePairs(String sheetName, Map<String, Object> sparsePairs, int firstRowIndex,
			int[] columnIndices, Map<String, Object> standardLabels) throws IOException, WriteLabelMismatchException {

		writeSparsePairs(createSheetIfAbsent(sheetName), sparsePairs, firstRowIndex, columnIndices, standardLabels);
	}


	public void writeTitle(String sheetName, String title, int rowIndex, int columnIndex, int mergeWidth,
			int mergeHeight) throws IOException {

		writeTitle(createSheetIfAbsent(sheetName), title, rowIndex, columnIndex, mergeWidth, mergeHeight);
	}


	public void writeTable(String sheetName, List<Map<String, Object>> records, int firstRowIndex, int labelRowCount,
			int firstColumnIndex, Map<String, Object> standardLabels) throws IOException, WriteLabelMismatchException {

		writeTable(createSheetIfAbsent(sheetName), records, firstRowIndex, labelRowCount, firstColumnIndex,
				standardLabels);
	}


	// 지정된 위치에 테이블 쓰기
	// sheet: 대상 시트
	// records: 쓸 레코드의 리스트. 맵 하나당 레코드 행 하나
	// firstRowIndex: 테이블의 첫 행 인덱스(0-based)
	// labelRowCount: 라벨 부분의 행 수
	// firstColumnIndex: 테이블의 첫 열 인덱스(0-based)
	// standardLabels: 표준 라벨. null이면 표준 라벨 체크 안 함
	private boolean writeTable(Sheet sheet, List<Map<String, Object>> records, int firstRowIndex, int labelRowCount,
			int firstColumnIndex, Map<String, Object> standardLabels) throws IOException, WriteLabelMismatchException {

		writeLabels(sheet, standardLabels, firstRowIndex, firstColumnIndex, labelRowCount - 1);

		int rowIndex = firstRowIndex + labelRowCount;
		int columnIndex = firstColumnIndex;

		for (Map<String, Object> record : records) {
			if (Optional.ofNullable(standardLabels).isPresent()) {
				if (!WorkbookLabelsUtil.validateLabels(standardLabels, record)) {
					throw new WriteLabelMismatchException(standardLabels, record);
				}
			}
			writeRecord(sheet, record, rowIndex++, columnIndex, standardLabels);
		}

		sheet.protectSheet(SHEET_PROTECTION_KEY);
		((XSSFSheet) sheet).lockSelectLockedCells(true);

		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(outputFile));
		workbook.write(bufferedOutputStream);
		bufferedOutputStream.close();

		return true;
	}


	// 편의성 메서드. 셀에 아래, 위, 왼쪽, 오른쪽 테두리를 한꺼번에 설정
	private void setBorderAll(CellStyle cellStyle, BorderStyle borderStyle) {

		cellStyle.setBorderBottom(borderStyle);
		cellStyle.setBorderTop(borderStyle);
		cellStyle.setBorderLeft(borderStyle);
		cellStyle.setBorderRight(borderStyle);
	}


	// 편의성 메서드. 해당 범위(병합된 셀)의 아래, 위 , 왼쪽, 오른쪽 테두리를 한꺼번에 설정
	private void setRegionBorderAll(Sheet sheet, CellRangeAddress region, BorderStyle borderStyle) {

		RegionUtil.setBorderBottom(borderStyle, region, sheet);
		RegionUtil.setBorderTop(borderStyle, region, sheet);
		RegionUtil.setBorderLeft(borderStyle, region, sheet);
		RegionUtil.setBorderRight(borderStyle, region, sheet);
	}


	// 셀 스타일 설정
	// sheet: 대상 시트
	// cell: 스타일 설정 대상 셀
	// isLabel: 라벨 여부
	private void setCellStyle(Sheet sheet, XSSFCell cell, boolean isLabel) {

		if (isLabel) {
			XSSFFont font = (XSSFFont) workbook.createFont();
			font.setBold(true);
			font.setColor(new XSSFColor(labelFontColor));

			XSSFCellStyle cellStyle = (XSSFCellStyle) workbook.createCellStyle();
			cellStyle.setFont(font);
			cellStyle.setFillForegroundColor(new XSSFColor(labelFillColor));
			cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			cellStyle.setAlignment(HorizontalAlignment.CENTER);
			cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			setBorderAll(cellStyle, BorderStyle.THIN);
			cellStyle.setLocked(true); //라벨 셀 값을 변경하지 못하도록 잠금

			cell.setCellStyle(cellStyle);

		} else {
			XSSFFont font = (XSSFFont) workbook.createFont();
			font.setBold(false);
			font.setColor(new XSSFColor(normalFontColor));

			XSSFCellStyle cellStyle = (XSSFCellStyle) workbook.createCellStyle();
			cellStyle.setFont(font);
			cellStyle.setFillForegroundColor(new XSSFColor(normalFillColor));
			cellStyle.setFillPattern(FillPatternType.NO_FILL);
			cellStyle.setAlignment(HorizontalAlignment.LEFT);
			cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			setBorderAll(cellStyle, BorderStyle.THIN);
			cellStyle.setLocked(false);

			cell.setCellStyle(cellStyle);
		}
	}


	// sparsePairs를 시트의 지정된 위치에 쓰기
	// sheet: 대상 시트
	// sparsePairs: 쓸 sparsePair
	// firstRowIndex: sparsePairs를 쓸 시트의 첫 번째 행 번호
	// columnIndices: 라벨의 열 번호. 예를 들어 {0, 3, 6}이면 시트의 A, D, G열에 라벨이 들어가고 B, E, H열에 값이 들어감
	// standardLabels: 표준 라벨. null이면 표준 라벨 체크 안 함
	private void writeSparsePairs(Sheet sheet, Map<String, Object> sparsePairs, int firstRowIndex, int[] columnIndices,
			Map<String, Object> standardLabels) throws IOException, WriteLabelMismatchException {

		if (Optional.ofNullable(standardLabels).isPresent()) {
			if (!WorkbookLabelsUtil.validateLabels(standardLabels, sparsePairs)) {
				throw new WriteLabelMismatchException(standardLabels, sparsePairs);
			}
		}

		int pairIndex = 0;
		for (Map.Entry<String, Object> labelEntry : standardLabels.entrySet()) {
			writeSparsePair(labelEntry.getKey(), (String) sparsePairs.get(labelEntry.getKey()), sheet,
					firstRowIndex + (2 * (int) Math.floor((double) pairIndex / columnIndices.length)),
					columnIndices[pairIndex % columnIndices.length]);
			pairIndex++;
		}

		sheet.protectSheet(SHEET_PROTECTION_KEY);

		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(outputFile));
		workbook.write(bufferedOutputStream);
		bufferedOutputStream.close();
	}


	// 대상 행에 레코드 하나 쓰기
	// sheet: 대상 시트
	// record: 쓸 레코드
	// firstColumnIndex: 첫 번째 열 번호
	// standardLabels: 표준 라벨. null이면 표준 라벨 체크 안 함
	private void writeRecord(Sheet sheet, Map<String, Object> record, int rowIndex, int firstColumnIndex,
			Map<String, Object> standardLabels) {

		Row row = createRowIfAbsent(sheet, rowIndex);

		int columnIndex = firstColumnIndex;

		List<String> leafValues = getLeafValues(record, standardLabels);
		for (String leafValue : leafValues) {
			Cell cell = createCellIfAbsent(row, columnIndex++);
			cell.setCellValue(leafValue);
			setCellStyle(sheet, (XSSFCell) cell, false);
		}
	}


	// 레코드에서 리프 라벨(자식 라벨이 없는)을 골라내서 이름만 리스트에 담아서 리턴
	// 예) {"A" : { "B": "bbb", "C": "ccc" } }  -> {"B", "C"}
	// tree: 대상 레코드 맵
	// standardLabels: 표준 라벨. null이면 표준 라벨 체크 안 함
	@SuppressWarnings("unchecked")
	private List<String> getLeafValues(Map<String, Object> tree, Map<String, Object> standardLabels) {

		List<String> leafValues = new ArrayList<>();

		standardLabels.entrySet().forEach((entry) -> {
			String key = entry.getKey();
			Object subtreeOrValue = tree.get(key);

			if (subtreeOrValue instanceof String) {
				leafValues.add((String) subtreeOrValue);
			} else if (subtreeOrValue instanceof Map<?, ?>) {
				leafValues.addAll(
						getLeafValues((Map<String, Object>) subtreeOrValue, (Map<String, Object>) entry.getValue()));
			}
		});

		return leafValues;
	}


	// 테이블 라벨을 시트에 쓰기. 
	// sheet: 대상 시트
	// rootLabel: 쓰려는 라벨
	// firstRowIndex: 라벨의 첫 행 번호(테이블의 첫 행 번호와 같음)
	// labelDepth: 남아있는 라벨 트리의 depth.
	@SuppressWarnings("unchecked")
	private void writeLabels(Sheet sheet, Map<String, Object> rootLabel, int firstRowIndex, int firstColumnIndex,
			int labelDepth) {

		int columnIndex = firstColumnIndex;
		int rowIndex = firstRowIndex;

		for (Map.Entry<String, Object> labelEntry : rootLabel.entrySet()) {
			Cell mergedCell = createCellIfAbsent(createRowIfAbsent(sheet, rowIndex), columnIndex);
			mergedCell.setCellValue(labelEntry.getKey());
			setCellStyle(sheet, (XSSFCell) mergedCell, true);

			int mergeHeight = 1;
			int mergeWidth = 1;

			Object labelEntryValue = labelEntry.getValue();

			if (labelEntryValue instanceof String) {
				mergeHeight = labelDepth + 1;
			} else if (labelEntryValue instanceof Map<?, ?>) {
				mergeWidth = getHorizontalMergeSize((Map<String, Object>) labelEntryValue);
				writeLabels(sheet, (Map<String, Object>) labelEntryValue, rowIndex + mergeHeight, columnIndex,
						labelDepth - 1);
			}

			if (mergeHeight > 1 || mergeWidth > 1) {
				CellRangeAddress mergedRegion = new CellRangeAddress(rowIndex, rowIndex + mergeHeight - 1, columnIndex,
						columnIndex + mergeWidth - 1);
				sheet.addMergedRegion(mergedRegion);
				setRegionBorderAll(sheet, mergedRegion, BorderStyle.THIN);
			}

			columnIndex += mergeWidth;
		}
	}


	// 셀 하나에 값을 쓰고 원하는 크기까지 병합
	// sheet: 대상 시트
	// title: 해당 셀에 쓸 값
	// rowIndex: 셀 위치의 행 번호
	// columnIndex: 셀 위치의 열 번호
	// mergeWidth: 병합 가로 폭
	// mergeHeight: 병합 세로 폭
	private void writeTitle(Sheet sheet, String title, int rowIndex, int columnIndex, int mergeWidth, int mergeHeight)
			throws IOException {

		Cell titleCell = createCellIfAbsent(createRowIfAbsent(sheet, rowIndex), columnIndex);
		titleCell.setCellValue(title);
		setCellStyle(sheet, (XSSFCell) titleCell, true);

		if (mergeHeight > 1 || mergeWidth > 1) {
			CellRangeAddress mergedRegion = new CellRangeAddress(rowIndex, rowIndex + mergeHeight - 1, columnIndex,
					columnIndex + mergeWidth - 1);
			sheet.addMergedRegion(mergedRegion);
			setRegionBorderAll(sheet, mergedRegion, BorderStyle.THIN);
		}

		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(outputFile));
		workbook.write(bufferedOutputStream);
		bufferedOutputStream.close();
	}


	// 대상 라벨의 가로 병합 폭 계산. 
	// 하위 라벨의 가로 병합 폭을 전부 합해서 구함
	@SuppressWarnings("unchecked")
	private int getHorizontalMergeSize(Map<String, Object> labels) {

		int mergeSize = 0;

		for (Map.Entry<String, Object> label : labels.entrySet()) {
			if (label.getValue() instanceof String) {
				mergeSize += 1;
			} else {
				mergeSize += getHorizontalMergeSize((Map<String, Object>) label.getValue());
			}
		}

		return mergeSize;
	}


	// 시트상의 대상 위치에 하나의 키:값 쌍을 씀
	// label: 라벨(키) 값
	// value: 값
	// sheet: 대상 시트
	// labelRowIndex: 시트상의 대상 행 번호
	// labelColumnIndex: 시트상의 라벨 열 번호
	private boolean writeSparsePair(String label, String value, Sheet sheet, int labelRowIndex, int labelColumnIndex) {

		Cell labelCell = createCellIfAbsent(createRowIfAbsent(sheet, labelRowIndex), labelColumnIndex);
		labelCell.setCellValue(label);
		setCellStyle(sheet, (XSSFCell) labelCell, true);

		Cell valueCell = createCellIfAbsent(createRowIfAbsent(sheet, labelRowIndex), labelColumnIndex + 1);
		valueCell.setCellValue(value);
		setCellStyle(sheet, (XSSFCell) valueCell, false);

		return true;
	}


	// 해당 행 번호에 행이 존재하면 가져오고, 그렇지 않으면 생성
	private Row createRowIfAbsent(Sheet sheet, int rowIndex) {

		return Optional.ofNullable(sheet.getRow(rowIndex)).orElseGet(() -> sheet.createRow(rowIndex));
	}


	// 해당 위치에 셀이 존재하면 가져오고, 그렇지 않으면 생성
	private Cell createCellIfAbsent(Row row, int columnIndex) {

		return Optional.ofNullable(row.getCell(columnIndex)).orElseGet(() -> row.createCell(columnIndex));
	}


	// 해당 이름의 시트가 존재하면 가져오고, 그렇지 않으면 생성
	private Sheet createSheetIfAbsent(String sheetName) {

		return Optional.ofNullable(workbook.getSheet(sheetName)).orElseGet(() -> workbook.createSheet(sheetName));
	}
}
