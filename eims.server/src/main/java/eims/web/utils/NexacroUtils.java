//package eims.web.utils;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.Iterator;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.commons.io.FileUtils;
//import org.hibernate.validator.internal.util.privilegedactions.GetClassLoader;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.nexacro17.xapi.data.DataSet;
//import com.nexacro17.xapi.data.DataTypes;
//import com.nexacro17.xapi.data.PlatformData;
//import com.nexacro17.xapi.tx.HttpPlatformResponse;
//import com.nexacro17.xapi.tx.PlatformException;
//
//public class NexacroUtils {
//	private static final Logger logger = LoggerFactory.getLogger(NexacroUtils.class);
//
//	private static final String DS_PREFIX = "ds_"; //데이터셋 구부 prefix
//	private static final String STR_UNDERSCORE = "_"; //데이터셋 구분자.
//	private static final String STR_ROOT = "root";//root 접미사
//	private static final String SEPERATOR = "^";
//
//	/*
//	 * 전체 인터페이스ID 목록 조회
//	 * nexacro studio의 bs에서 "Refresh" 메뉴 클릭 시 호출되는 serviceList 조회 Method
//	 * @param jsonData
//	 * @return
//	 * @throws IOException
//	 */
//	public static PlatformData getDataSetLayoutList(String jsonData) throws IOException {
//
//		ObjectMapper objectMapper = new ObjectMapper();
//		JsonNode rootNode = objectMapper.readTree(jsonData);
//
//		return getLayoutListInfo(rootNode);
//	}
//
//	/*
//	 * 개별 인터페이스ID input/output 조회
//	 * nexacro studio의 bs에서 인터페이스ID를 "Refresh" 메뉴 클릭 시 호출되는 데이터셋 컬럼정보 조회 Method
//	 * @param jsonData
//	 * @return
//	 * @throws IOException
//	 */
//	public static PlatformData getDataSetLayoutDetail(String jsonData) throws IOException {
//
//		ObjectMapper objectMapper = new ObjectMapper();
//		JsonNode rootNode = objectMapper.readTree(jsonData);
//
//		return getLayoutDetailInfo(rootNode.findPath("intrfcmsglayoutdtDto"));
//	}
//
//	/**
//	 * 인터페이스ID별 Layouts  정보(테이블데이터) 조회
//	 * depth 0 -> ROOT 데이터셋
//	 *       1 -> Sub 데이터셋
//	 * @param jsonData
//	 * @return
//	 * @throws IOException
//	 */
//	public static PlatformData toNexacroDataSet(String jsonData) throws IOException {
//
//		ObjectMapper objectMapper = new ObjectMapper();
//		JsonNode rootNode = objectMapper.readTree(jsonData);
//
//		JsonNode targetNode = rootNode.findPath("intrfcmsglayoutdtDto");
//
//		//데이터셋 컬럼정보 생성.
//		PlatformData responsePlatformData = createLayoutColumnHeader(targetNode);
//
//		//데이터 생성
//		createDataSetRowInfo(targetNode, responsePlatformData);
//
//		return responsePlatformData;
//	}
//
//	/**
//	 * 데이터셋 정보 화면으로 전송.
//	 * @param response
//	 * @param responsePlatformData
//	 */
//	public static void sendResponseData(HttpServletResponse response, PlatformData responsePlatformData) {
//
//		try {
//			HttpPlatformResponse platformResponse = new HttpPlatformResponse(response);
//			platformResponse.setData(responsePlatformData);
//			platformResponse.sendData();
//		} catch (PlatformException e) {
//			logger.error(e.getMessage());
//		}
//	}
//
//	/**
//	 * 인터페이스ID별 서비스목록 생성
//	 * @param rootNode
//	 * @return
//	 */
//	private static PlatformData getLayoutListInfo(JsonNode rootNode) {
//		PlatformData platformData = new PlatformData();
//
//		DataSet layoutInfo = new DataSet("layoutlist");
//		layoutInfo.addColumn("domain", DataTypes.STRING);
//		layoutInfo.addColumn("model", DataTypes.STRING);
//		layoutInfo.addColumn("description", DataTypes.STRING);
//
//		String domain = "eims";
//		//String intrfcId = rootNode.get("intrfcId").textValue();
//		String intrfcId = rootNode.get("intrfcId").textValue() + SEPERATOR + rootNode.get("deployVersion").textValue();
//		String intrfcNm = rootNode.get("intrfcId").textValue();
//		//-----------------------------------------------------------------------
//
//		int rowPos = layoutInfo.newRow();
//		layoutInfo.set(rowPos, "domain", domain);
//		layoutInfo.set(rowPos, "model", intrfcId);
//		layoutInfo.set(rowPos, "description", intrfcNm);
//
//		platformData.addDataSet(layoutInfo);
//
//		return platformData;
//	}
//
//	/**
//	 * 데이터셋의 컬럼정보 조회 - nexacro studio의 인터페이스ID Refresh 선택 시 호출.
//	 * @param parameterJsonNode
//	 * @return
//	 */
//	private static PlatformData getLayoutDetailInfo(JsonNode parameterJsonNode) {
//		PlatformData platformData = new PlatformData();
//
//		Iterator<JsonNode> elements = parameterJsonNode.elements();
//		while (elements.hasNext()) {
//
//			JsonNode childNode = elements.next();
//
//			String intrfcId = childNode.get("intrfcId").asText(); //인터페이스ID
//			String rqstRspsTypeCd = childNode.get("rqstRspsTypeCd").asText(); //처리유형
//			String rootDataSetName = DS_PREFIX + intrfcId + STR_UNDERSCORE + rqstRspsTypeCd.substring(0, 3) + STR_UNDERSCORE + STR_ROOT;
//			//-----------------------------------------------------------------------
//
//			/**
//			 * REQUEST인 경우 input 데이터셋에 정보를 설정한다.
//			 */
//			if ("REQUEST".equals(rqstRspsTypeCd)) {
//
//				DataSet layoutInfo = new DataSet();
//				layoutInfo.setName("input");
//				layoutInfo.addColumn("name", DataTypes.STRING);
//				layoutInfo.addColumn("type", DataTypes.STRING);
//
//				platformData.addDataSet(layoutInfo);
//
//				int rowPos = layoutInfo.newRow();
//				layoutInfo.set(rowPos, "name", rootDataSetName);
//				layoutInfo.set(rowPos, "type", "dataset");
//
//				JsonNode msglayoutdtDtoNode = childNode.get("msglayoutbsDto").get("msglayoutdtDto");
//
//				Iterator<JsonNode> iterators = msglayoutdtDtoNode.iterator();
//				while (iterators.hasNext()) {
//					JsonNode node = iterators.next();
//
//					Iterator<Map.Entry<String, JsonNode>> nodeFields = node.fields();
//					while (nodeFields.hasNext()) {
//
//						Map.Entry<String, JsonNode> fields = nodeFields.next();
//
//						if ("LAYOUT".equals(fields.getValue().asText())) {
//
//							String subDataSetName = DS_PREFIX + intrfcId + STR_UNDERSCORE + rqstRspsTypeCd.substring(0, 3) + STR_UNDERSCORE + node.get("fldEngNm").textValue();
//
//							rowPos = layoutInfo.newRow();
//							layoutInfo.set(rowPos, "name", subDataSetName);
//							layoutInfo.set(rowPos, "type", "dataset");
//
//						}
//					}
//				}
//			}
//			/**
//			 * RESPONSE 인경우 oupput에 정보를 설정한다.
//			 */
//			else if ("RESPONSE".equals(rqstRspsTypeCd)) {
//				DataSet layoutInfo = new DataSet();
//				layoutInfo.setName("output");
//
//				layoutInfo.addColumn("name", DataTypes.STRING);
//				layoutInfo.addColumn("type", DataTypes.STRING);
//
//				int rowPos = layoutInfo.newRow();
//				layoutInfo.set(rowPos, "name", rootDataSetName);
//				layoutInfo.set(rowPos, "type", "dataset");
//				platformData.addDataSet(layoutInfo);
//
//				JsonNode msglayoutdtDtoNode = childNode.get("msglayoutbsDto").get("msglayoutdtDto");
//
//				Iterator<JsonNode> iterators = msglayoutdtDtoNode.iterator();
//				while (iterators.hasNext()) {
//					JsonNode node = iterators.next();
//
//					Iterator<Map.Entry<String, JsonNode>> nodeFields = node.fields();
//					while (nodeFields.hasNext()) {
//
//						Map.Entry<String, JsonNode> fields = nodeFields.next();
//
//						if ("LAYOUT".equals(fields.getValue().asText())) {
//
//							String subDataSetName = DS_PREFIX + intrfcId + STR_UNDERSCORE + rqstRspsTypeCd.substring(0, 3) + STR_UNDERSCORE + node.get("fldEngNm").textValue();
//
//							rowPos = layoutInfo.newRow();
//							layoutInfo.set(rowPos, "name", subDataSetName);
//							layoutInfo.set(rowPos, "type", "dataset");
//						}
//					}
//				}
//			}
//		}
//
//		//데이터셋 컬럼정보 생성.
//		createLayoutColumnHeader(parameterJsonNode, platformData);
//
//		return platformData;
//	}
//
//	/**
//	 * 데이터셋 컬럼정보 생성 - BS에서 사용
//	 * @param parameterJsonNode
//	 * @return
//	 */
//	private static PlatformData createLayoutColumnHeader(JsonNode parameterJsonNode) {
//		PlatformData platformData = new PlatformData();
//
//		Iterator<JsonNode> elements = parameterJsonNode.elements();
//		String subDataSetName = "";
//
//		while (elements.hasNext()) {
//
//			JsonNode childNode = elements.next();
//
//			String intrfcId = childNode.get("intrfcId").asText(); //인터페이스ID
//			String rqstRspsTypeCd = childNode.get("rqstRspsTypeCd").asText(); //처리유형
//			String rootDataSetName = DS_PREFIX + intrfcId + STR_UNDERSCORE + rqstRspsTypeCd.substring(0, 3) + STR_UNDERSCORE + STR_ROOT;
//			//-----------------------------------------------------------------------
//
//			JsonNode msglayoutdtDtoNode = childNode.get("msglayoutbsDto").get("msglayoutdtDto");
//
//			DataSet rootDataSet = new DataSet();
//			rootDataSet.setName(rootDataSetName);
//			platformData.addDataSet(rootDataSet);
//
//			logger.info("-------> ROOT COLUMNHEADER 생성 [" + rootDataSetName + "]");
//
//			boolean isSubData = false;
//			DataSet subDataSet = new DataSet();
//
//			Iterator<JsonNode> rowIterators = msglayoutdtDtoNode.iterator();
//			while (rowIterators.hasNext()) {
//				JsonNode rowData = rowIterators.next();
//				int fldLvNo = rowData.get("fldLvNo").intValue();
//
//				//SUB DATASET명 생성
//				if ("LAYOUT".equals(rowData.get("dataTypeNm").textValue())) {
//					isSubData = true;
//					subDataSetName = DS_PREFIX + intrfcId + STR_UNDERSCORE + rqstRspsTypeCd.substring(0, 3) + STR_UNDERSCORE + rowData.get("fldEngNm").textValue();
//
//					subDataSet = new DataSet();
//					subDataSet.setName(subDataSetName);
//					platformData.addDataSet(subDataSet);
//
//					logger.info("-------> SUB COLUMNHEADER 생성 [" + subDataSetName + "]");
//				}
//
//				Iterator<Map.Entry<String, JsonNode>> columnFields = rowData.fields();
//				while (columnFields.hasNext()) {
//
//					Map.Entry<String, JsonNode> fields = columnFields.next();
//
//					//ROOT 데이터셋 컬럼정보 생성
//					if (fldLvNo == 0) {
//						if (fields.getValue().isTextual()) {
//							if (rootDataSet.containsColumn(fields.getKey()) == false) {
//								rootDataSet.addColumn(fields.getKey(), DataTypes.STRING);
//							}
//						} else if (fields.getValue().isInt()) {
//							if (rootDataSet.containsColumn(fields.getKey()) == false) {
//								rootDataSet.addColumn(fields.getKey(), DataTypes.INT);
//							}
//						} else if (fields.getValue().isLong() || fields.getValue().isBigDecimal()) {
//							if (rootDataSet.containsColumn(fields.getKey()) == false) {
//								rootDataSet.addColumn(fields.getKey(), DataTypes.BIG_DECIMAL);
//							}
//						}
//					}
//					//SUB 데이터셋 컬럼정보 생성
//					else if (isSubData && fldLvNo > 0) {
//
//						if (fields.getValue().isTextual()) {
//							if (subDataSet.containsColumn(fields.getKey()) == false) {
//								subDataSet.addColumn(fields.getKey(), DataTypes.STRING);
//							}
//						} else if (fields.getValue().isInt()) {
//							if (subDataSet.containsColumn(fields.getKey()) == false) {
//								subDataSet.addColumn(fields.getKey(), DataTypes.INT);
//							}
//						} else if (fields.getValue().isLong() || fields.getValue().isBigDecimal()) {
//							if (subDataSet.containsColumn(fields.getKey()) == false) {
//								subDataSet.addColumn(fields.getKey(), DataTypes.BIG_DECIMAL);
//							}
//						}
//					}
//				}
//			}
//		}
//
//		return platformData;
//	}
//
//	/**
//	 * 데이터셋 컬럼정보 생성 - 데이터 조회 시 사용
//	 * @param parameterJsonNode
//	 * @param platformData
//	 */
//	private static void createLayoutColumnHeader(JsonNode parameterJsonNode, PlatformData platformData) {
//
//		Iterator<JsonNode> elements = parameterJsonNode.elements();
//		String subDataSetName = "";
//
//		while (elements.hasNext()) {
//
//			JsonNode childNode = elements.next();
//
//			String intrfcId = childNode.get("intrfcId").asText();
//			String rqstRspsTypeCd = childNode.get("rqstRspsTypeCd").asText();
//			String rootDataSetName = DS_PREFIX + intrfcId + STR_UNDERSCORE + rqstRspsTypeCd.substring(0, 3) + STR_UNDERSCORE + STR_ROOT;
//			//-----------------------------------------------------------------------
//
//			JsonNode msglayoutdtDtoNode = childNode.get("msglayoutbsDto").get("msglayoutdtDto");
//
//			DataSet rootDataSet = new DataSet();
//			rootDataSet.setName(rootDataSetName);
//			platformData.addDataSet(rootDataSet);
//
//			logger.info("-------> ROOT COLUMNHEADER 생성 [" + rootDataSetName + "]");
//
//			boolean isSubData = false;
//			DataSet subDataSet = new DataSet();
//
//			Iterator<JsonNode> rowIterators = msglayoutdtDtoNode.iterator();
//			while (rowIterators.hasNext()) {
//
//				JsonNode rowData = rowIterators.next();
//
//				int fldLvNo = rowData.get("fldLvNo").intValue();
//				String sColId = rowData.get("fldEngNm").textValue();
//				String sDataType = rowData.get("dataTypeNm").textValue();
//				int nColLength = rowData.get("msgLen").intValue();;
//
//				if (fldLvNo == 0) {
//					if ("LAYOUT".equals(sDataType)) {
//						isSubData = true;
//						subDataSetName = DS_PREFIX + intrfcId + STR_UNDERSCORE + rqstRspsTypeCd.substring(0, 3) + STR_UNDERSCORE + rowData.get("fldEngNm").textValue();
//
//						subDataSet = new DataSet();
//						subDataSet.setName(subDataSetName);
//						platformData.addDataSet(subDataSet);
//
//						logger.info("-------> SUB 데이터셋 COLUMNHEADER 생성 [" + subDataSetName + "]");
//					} else {
//						if ("BIGDECIMAL".equals(sDataType) || "LONG".equals(sDataType)) {
//							rootDataSet.addColumn(sColId, DataTypes.BIG_DECIMAL, nColLength);
//						} else {
//							rootDataSet.addColumn(sColId, DataTypes.STRING, nColLength);
//						}
//					}
//				} else if (fldLvNo == 1) {
//					if ("BIGDECIMAL".equals(sDataType) || "LONG".equals(sDataType)) {
//						subDataSet.addColumn(sColId, DataTypes.BIG_DECIMAL, nColLength);
//					} else {
//						subDataSet.addColumn(sColId, DataTypes.STRING, nColLength);
//					}
//				} 
//			}
//		}
//	}
//
//	/**
//	 * Layouts 데이터 조회 시 Row 데이터 생성.
//	 * @param parameterJsonNode
//	 * @param platformData
//	 */
//	private static void createDataSetRowInfo(JsonNode parameterJsonNode, PlatformData platformData) {
//
//		Iterator<JsonNode> elements = parameterJsonNode.elements();
//
//		while (elements.hasNext()) {
//
//			JsonNode childNode = elements.next();
//
//			String intrfcId = childNode.get("intrfcId").asText(); //인터페이스ID
//			String rqstRspsTypeCd = childNode.get("rqstRspsTypeCd").asText(); //처리유형
//			String rootDataSetName = DS_PREFIX + intrfcId + STR_UNDERSCORE + rqstRspsTypeCd.substring(0, 3) + STR_UNDERSCORE + STR_ROOT;
//			//-----------------------------------------------------------------------
//			logger.info("---------------> ROOT ROW 생성 [" + rootDataSetName + "]");
//
//			DataSet rootDataSet = platformData.getDataSet(rootDataSetName);
//
//			JsonNode msglayoutdtDtoNode = childNode.get("msglayoutbsDto").get("msglayoutdtDto");
//			Iterator<JsonNode> rowIterators = msglayoutdtDtoNode.iterator();
//
//			boolean isSubData = false;
//			String subDataSetName = "";
//
//			while (rowIterators.hasNext()) {
//				JsonNode rowData = rowIterators.next();
//
//				//logger.info(rowData.toString());
//				//SUB 데이터 명 생성
//				if ("LAYOUT".equals(rowData.get("dataTypeNm").textValue())) {
//					isSubData = true;
//					subDataSetName = DS_PREFIX + intrfcId + STR_UNDERSCORE + rqstRspsTypeCd.substring(0, 3) + STR_UNDERSCORE + rowData.get("fldEngNm").textValue();
//					logger.info("---------------> SUB ROW 생성 [" + subDataSetName + "]");
//				} else {
//					//ROOT 데이터셋 생성
//					if (rowData.get("fldLvNo").intValue() == 0) {
//						int rootRowPos = rootDataSet.newRow();
//						Iterator<Map.Entry<String, JsonNode>> columnFields = rowData.fields();
//						while (columnFields.hasNext()) {
//							Map.Entry<String, JsonNode> fields = columnFields.next();
//							if (fields.getValue().isTextual()) {
//								rootDataSet.set(rootRowPos, fields.getKey(), fields.getValue().textValue());
//							} else if (fields.getValue().isInt()) {
//								rootDataSet.set(rootRowPos, fields.getKey(), fields.getValue().intValue());
//							} else if (fields.getValue().isLong() || fields.getValue().isBigDecimal()) {
//								rootDataSet.set(rootRowPos, fields.getKey(), fields.getValue().decimalValue());
//							}
//						}
//					}
//					//SUB 데이터셋 생성
//					else if (isSubData && rowData.get("fldLvNo").intValue() > 0) {
//						DataSet subDataSet = platformData.getDataSet(subDataSetName);
//						int subRowPos = subDataSet.newRow();
//
//						Iterator<Map.Entry<String, JsonNode>> columnFields = rowData.fields();
//						while (columnFields.hasNext()) {
//
//							Map.Entry<String, JsonNode> fields = columnFields.next();
//
//							if (fields.getValue().isTextual()) {
//								subDataSet.set(subRowPos, fields.getKey(), fields.getValue().textValue());
//							} else if (fields.getValue().isInt()) {
//								subDataSet.set(subRowPos, fields.getKey(), fields.getValue().intValue());
//							} else if (fields.getValue().isLong() || fields.getValue().isBigDecimal()) {
//								subDataSet.set(subRowPos, fields.getKey(), fields.getValue().decimalValue());
//							}
//						}
//					}
//				}
//			}
//		}
//	}
//
//	//테스트 json 데이터 -> bccard.jsp 파일 호출
//	public static String getTestJsonStringData() {
//
//		try {
//			File file = new File(GetClassLoader.class.getResource("/bccard1.json").getFile());
//			String data = FileUtils.readFileToString(file);
//
//			//logger.info(data);
//			return data;
//		} catch (IOException e) {
//			logger.error("{}", e);
//		}
//		return "";
//	}
//
//	//테스트 json 데이터 -> bccard.jsp 파일 호출
//	public static String getTestJsonStringData(String target) {
//
//		try {
//			File file = new File(GetClassLoader.class.getResource("/bccard1.json").getFile());
//			String data = FileUtils.readFileToString(file);
//
//			//logger.info(data);
//			return data;
//		} catch (IOException e) {
//			logger.error("{}", e);
//		}
//		return "";
//	}
//}
