package eims.web.excel.workbook;

import java.util.Map;

public class WorkbookLabelsUtil {

	@SuppressWarnings("unchecked")
	public static boolean validateLabels(Map<String, Object> standardLabelTree, Map<String, Object> record) {

		for (Map.Entry<String, Object> labelEntry : standardLabelTree.entrySet()) {
			String key = labelEntry.getKey();

			if (record.containsKey(key)) {
				Object standardSubtree = labelEntry.getValue();
				Object recordSubtree = record.get(key);

				if (standardSubtree instanceof String && recordSubtree instanceof String) {
					return true;
				} else if (standardSubtree instanceof Map<?, ?> && recordSubtree instanceof Map<?, ?>) {
					if (validateLabels((Map<String, Object>) standardSubtree, (Map<String, Object>) recordSubtree)) {
						continue;
					} else {
						return false;
					}
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
		return true;
	}
}
