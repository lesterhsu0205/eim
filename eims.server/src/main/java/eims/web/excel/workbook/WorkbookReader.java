package eims.web.excel.workbook;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;

import eims.web.excel.workbook.exception.ReadLabelMismatchException;
import eims.web.excel.workbook.exception.SheetNotFoundException;

public class WorkbookReader {

	private Workbook workbook;
	private String inputFilePath;


	public WorkbookReader(File inputFile) throws EncryptedDocumentException, InvalidFormatException, IOException {

		workbook = WorkbookFactory.create(inputFile);
		inputFilePath = inputFile.getCanonicalPath();
	}


	public void close() throws IOException {

		workbook.close();
	}


	public int getNumberOfSheets() {

		return workbook.getNumberOfSheets();
	}


	public Map<String, Object> readSparsePairs(String sheetName, int firstRowIndex, int lastRowIndex,
			int[] columnIndices, Map<String, Object> standardLabels)
			throws SheetNotFoundException, ReadLabelMismatchException {

		Sheet sheet = Optional.ofNullable(workbook.getSheet(sheetName))
				.orElseThrow(() -> new SheetNotFoundException(inputFilePath, sheetName));

		return readSparsePairs(sheet, firstRowIndex, lastRowIndex, columnIndices, standardLabels);
	}


	public Map<String, Object> readSparsePairs(int sheetIndex, int firstRowIndex, int lastRowIndex, int[] columnIndices,
			Map<String, Object> standardLabels) throws SheetNotFoundException, ReadLabelMismatchException {

		Sheet sheet = Optional.ofNullable(workbook.getSheetAt(sheetIndex))
				.orElseThrow(() -> new SheetNotFoundException(inputFilePath, sheetIndex));

		return readSparsePairs(sheet, firstRowIndex, lastRowIndex, columnIndices, standardLabels);
	}


	public List<Map<String, Object>> readTable(String sheetName, int firstRowIndex, int lastRowIndex, int labelRowCount,
			int firstColumnIndex, int lastColumnIndex, Map<String, Object> standardLabels)
			throws SheetNotFoundException, ReadLabelMismatchException {

		Sheet sheet = Optional.ofNullable(workbook.getSheet(sheetName))
				.orElseThrow(() -> new SheetNotFoundException(inputFilePath, sheetName));

		return readTable(sheet, firstRowIndex, lastRowIndex, labelRowCount, firstColumnIndex, lastColumnIndex,
				standardLabels);
	}


	public List<Map<String, Object>> readTable(int sheetIndex, int firstRowIndex, int lastRowIndex, int labelRowCount,
			int firstColumnIndex, int lastColumnIndex, Map<String, Object> standardLabels)
			throws SheetNotFoundException, ReadLabelMismatchException {

		Sheet sheet = Optional.ofNullable(workbook.getSheetAt(sheetIndex))
				.orElseThrow(() -> new SheetNotFoundException(inputFilePath, sheetIndex));

		return readTable(sheet, firstRowIndex, lastRowIndex, labelRowCount, firstColumnIndex, lastColumnIndex,
				standardLabels);
	}


	// 엑셀상의 표에서 record 들을 읽어서 리스트로(레코드당 맵 하나) 리턴
	// sheet: 대상 시트
	// firstRowIndex - lastrowIndex: 해당 표의 최대 범위
	// labelRowCount: 범위 안에서 라벨 부분에 해당하는 행 갯수
	// 	라벨 외의 레코드는 범위 안에 있어도 아무것도 없는 행은 무시함(즉, 실제 데이터가 3행이어도 범위를 더 크게 잡아서 호출해도 됨)
	// 	단, 라벨이 항상 맨 위에 있어야 하며 라벨 위에 빈 행이 있으면 안 됨
	// standardLabels: 표준 라벨 (null이면 라벨 검사 안 함)
	// 	표준 라벨에 있는 모든 key가 record안에 존재할 때만 정상
	//  record에 있는데 표준 라벨에 없는 키는 읽지 않음
	private List<Map<String, Object>> readTable(Sheet sheet, int firstRowIndex, int lastRowIndex, int labelRowCount,
			int firstColumnIndex, int lastColumnIndex, Map<String, Object> standardLabels)
			throws ReadLabelMismatchException {

		int firstRecordRowIndex = firstRowIndex + labelRowCount;

		List<Map<String, Object>> joinedRecords = new ArrayList<>();

		for (Row row : sheet) {
			if (row.getRowNum() >= firstRecordRowIndex) {
				String[] record = readRecord(row, firstColumnIndex, lastColumnIndex);

				if (!Stream.of(record).allMatch(String::isEmpty)) {
					Map<String, Object> joinedRecord = joinRecordWithLabels(sheet, sheet.getRow(firstRowIndex),
							firstColumnIndex, lastColumnIndex, labelRowCount - 1, record);

					if (Optional.ofNullable(standardLabels).isPresent()) {
						if (!WorkbookLabelsUtil.validateLabels(standardLabels, joinedRecord)) {
							throw new ReadLabelMismatchException(inputFilePath, sheet.getSheetName(), standardLabels,
									joinedRecord);
						}
					}

					joinedRecords.add(joinedRecord);
				}
			}
		}

		return joinedRecords;
	}


	// sparsePairs 읽기
	// 모든 쌍을 '라벨: 값' 형태로 담은 맵을 리턴
	//
	// sheet: 대상 시트
	// firstRowIndex: 읽기 시작할 첫 행 번호 (0-based)
	// lastRowIndex: 읽을 마지막 행 번호 (0-based)
	// labelColumnIndices : 읽을 sparsePairs의 라벨이 있는 열 번호 (0-based)
	// standardLabels: 읽을 표준 라벨의 맵 (예: "InterfaceName" : 더미값 ), 순서는 상관없음
	private Map<String, Object> readSparsePairs(Sheet sheet, int firstRowIndex, int lastRowIndex,
			int[] labelColumnIndices, Map<String, Object> standardLabels) throws ReadLabelMismatchException {

		Map<String, Object> sparseLabelValuePairs = new LinkedHashMap<>();

		for (int rowIndex = firstRowIndex; rowIndex <= lastRowIndex; rowIndex++) {
			Optional.ofNullable(sheet.getRow(rowIndex)).ifPresent((row) -> {
				for (int labelColumnIndex : labelColumnIndices) {
					String[] labelValuePair = readSparseCellPair(row, labelColumnIndex);

					if (!labelValuePair[0].isEmpty()) {
						sparseLabelValuePairs.put(labelValuePair[0], labelValuePair[1]);
					}
				}
			});
		}

		if (Optional.ofNullable(standardLabels).isPresent()) {
			if (!WorkbookLabelsUtil.validateLabels(standardLabels, sparseLabelValuePairs)) {
				throw new ReadLabelMismatchException(inputFilePath, sheet.getSheetName(), standardLabels,
						sparseLabelValuePairs);
			}
		}

		return sparseLabelValuePairs;
	}


	// 레코드 하나를 읽은 문자열 배열을 라벨과 합쳐서 맵으로 생성
	// sheet: 대상 시트
	// row: 대상 행
	// firstColumnIndex: 라벨과 합칠 엑셀 표의 첫 열 번호(0-based)
	// lastColumnIndex: 라벨과 합칠 엑셀 표의 마지막 열 번호(0-based)
	// depthLeft: 남은 라벨의 depth(행 숫자)
	// record: 레코드를 읽은 문자열 배열
	private Map<String, Object> joinRecordWithLabels(Sheet sheet, Row row, int firstColumnIndex, int lastColumnIndex,
			int depthLeft, String[] record) {

		Map<String, Object> labelsSubtree = new LinkedHashMap<>();

		for (int columnIndex = firstColumnIndex; columnIndex <= lastColumnIndex; columnIndex++) {

			Optional.ofNullable(row.getCell(columnIndex)).ifPresent((cell) -> {
				CellRangeAddress mergedRegion = getMergedRegion(sheet, cell);

				Row childRow;
				int depthLeftForChild;

				if (cell.getColumnIndex() == mergedRegion.getFirstColumn()) {
					if (cell.getRowIndex() == mergedRegion.getLastRow()) {
						childRow = sheet.getRow(row.getRowNum() + 1);
						depthLeftForChild = depthLeft - 1;
					} else {
						childRow = sheet.getRow(mergedRegion.getLastRow() + 1);
						depthLeftForChild = depthLeft - (mergedRegion.getLastRow() - cell.getRowIndex());
					}

					if (depthLeftForChild > 0) {
						Map<String, Object> joinedRecord = joinRecordWithLabels(sheet, childRow,
								mergedRegion.getFirstColumn(), mergedRegion.getLastColumn(), depthLeftForChild, record);

						labelsSubtree.put(readCellValue(cell), joinedRecord);
					} else {
						labelsSubtree.put(readCellValue(cell), record[cell.getColumnIndex() - firstColumnIndex]);
					}
				}
			});
		}

		return labelsSubtree;
	}


	// 대상 셀이 병합되었는지 확인하고, 병합되었으면 병합된 범위를 리턴
	// sheet: 대상 시트
	// cell: 대상 셀
	//
	// return
	// 병합된 셀의 일부이면 병합된 범위를 리턴, 병합된 셀이 아니면 해당 셀의 범위를 리턴
	private CellRangeAddress getMergedRegion(Sheet sheet, Cell cell) {

		return sheet.getMergedRegions().stream().filter((region) -> region.isInRange(cell)).findFirst()
				.orElse(new CellRangeAddress(cell.getRowIndex(), cell.getRowIndex(), cell.getColumnIndex(),
						cell.getColumnIndex()));
	}


	// 해당 행에서 레코드를 읽어서, 표기 순서 그대로 배열로 리턴
	// row: 대상 행
	// firstColumnIndex: 읽을 범위의 첫 열 번호
	// lastColumnIndex: 읽을 범위의 마지막 열 번호
	private String[] readRecord(Row row, int firstColumnIndex, int lastColumnIndex) {

		String[] record = new String[lastColumnIndex - firstColumnIndex + 1];

		for (int columnIndex = firstColumnIndex; columnIndex <= lastColumnIndex; columnIndex++) {
			String value = Optional.ofNullable(row.getCell(columnIndex)).map(this::readCellValue).orElse("");

			record[columnIndex - firstColumnIndex] = value;
		}

		return record;
	}


	// sparsePair 하나를 읽어서 배열로 리턴
	// row: 대상 행
	// labelColumnIndex: 라벨의 열 번호
	//
	// return 
	// [0]: 라벨
	// [1]: 값
	private String[] readSparseCellPair(Row row, int labelColumnIndex) {

		String label = Optional.ofNullable(row.getCell(labelColumnIndex)).map(this::readCellValue).orElse("");

		String value = Optional.ofNullable(row.getCell(labelColumnIndex + 1)).map(this::readCellValue).orElse("");

		return new String[] { label, value };
	}


	// 해당 셀에서 값을 읽어서 문자열로 리턴
	// 빈 셀이면 "" 리턴
	// 수식이면 수식을 계산해서 결과를 문자열로 리턴
	private String readCellValue(Cell cell) {

		switch (cell.getCellTypeEnum()) {
		case STRING:
			return cell.getStringCellValue();
		case BOOLEAN:
			return Boolean.toString(cell.getBooleanCellValue());
		case ERROR:
			return Byte.toString(cell.getErrorCellValue());
		case NUMERIC:
			return Double.toString(cell.getNumericCellValue());
		case FORMULA:
			FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
			CellValue cellValue = formulaEvaluator.evaluate(cell);

			return Optional.ofNullable(cellValue).map(this::readCellValue).orElse("");
		case BLANK:
		case _NONE:
			return "";
		default:
			return null;
		}
	}


	// 수식 계산 결과값을 문자열로 읽어서 리턴
	private String readCellValue(CellValue cellValue) {

		switch (cellValue.getCellTypeEnum()) {
		case STRING:
			return cellValue.getStringValue();
		case BOOLEAN:
			return Boolean.toString(cellValue.getBooleanValue());
		case ERROR:
			return Byte.toString(cellValue.getErrorValue());
		case NUMERIC:
			return Double.toString(cellValue.getNumberValue());
		case BLANK:
		case _NONE:
		default:
			return "";
		}
	}


	public Map<String, List<?>> readTablesWithVerticalLabel(int sheetIndex, int labelRowCount, int firstRowIndex,
			int lastRowIndex, int verticalLabelColumnIndex, int lastColumnIndex, Map<String, Object> standardLabels)
			throws ReadLabelMismatchException, SheetNotFoundException {

		Sheet sheet = Optional.ofNullable(workbook.getSheetAt(sheetIndex))
				.orElseThrow(() -> new SheetNotFoundException(inputFilePath, sheetIndex));

		return readTablesWithVerticalLabel(sheet, labelRowCount, firstRowIndex, lastRowIndex, verticalLabelColumnIndex,
				lastColumnIndex, standardLabels);
	}


	public Map<String, List<?>> readTablesWithVerticalLabel(String sheetName, int labelRowCount, int firstRowIndex,
			int lastRowIndex, int verticalLabelColumnIndex, int lastColumnIndex, Map<String, Object> standardLabels)
			throws ReadLabelMismatchException, SheetNotFoundException {

		Sheet sheet = Optional.ofNullable(workbook.getSheet(sheetName))
				.orElseThrow(() -> new SheetNotFoundException(inputFilePath, sheetName));

		return readTablesWithVerticalLabel(sheet, labelRowCount, firstRowIndex, lastRowIndex, verticalLabelColumnIndex,
				lastColumnIndex, standardLabels);
	}


	// 세로로 긴 라벨이 첫 열에 있는 테이블들 읽기
	// sheet: 대상 시트
	// labelRowCount: 각 테이블의 라벨 행 갯수
	// firstRowIndex: 처음으로 읽을 행 번호
	// lastRowIndex: 마지막으로 읽을 행 번호 최댓값
	// verticalLabelColumnIdex: 첫 번째 열 번호(세로 라벨이 있는 열)
	// lastColumnIndex: 테이블 마지막 열 번호(각 테이블이 모두 같아야 함)
	// standardLabels: 표준 라벨
	private Map<String, List<?>> readTablesWithVerticalLabel(Sheet sheet, int labelRowCount, int firstRowIndex,
			int lastRowIndex, int verticalLabelColumnIndex, int lastColumnIndex, Map<String, Object> standardLabels)
			throws ReadLabelMismatchException {

		//readTablesWithVerticalLabel("Sheet1", 1, 0, 10000, 0, 3, standardLabels)

		int rowIndex = firstRowIndex;

		Map<String, List<?>> tablesWithVerticalLabel = new LinkedHashMap<>();

		while (rowIndex <= lastRowIndex) {
			Optional<Cell> maybeLabelCell = Optional.ofNullable(sheet.getRow(rowIndex))
					.map((row) -> row.getCell(verticalLabelColumnIndex));

			if (maybeLabelCell.isPresent()) {
				Cell labelCell = maybeLabelCell.get();

				CellRangeAddress mergedRegion = getMergedRegion(sheet, labelCell);
				Cell mergedRegionFirstCell = sheet.getRow(mergedRegion.getFirstRow())
						.getCell(mergedRegion.getFirstColumn());

				String label = readCellValue(mergedRegionFirstCell);

				List<Map<String, Object>> partialTable = readTable(sheet, mergedRegion.getFirstRow(),
						mergedRegion.getLastRow(), labelRowCount, verticalLabelColumnIndex + 1, lastColumnIndex,
						standardLabels);

				tablesWithVerticalLabel.put(label, partialTable);
			}
		}

		return tablesWithVerticalLabel;
	}
}