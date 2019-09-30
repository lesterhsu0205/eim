package eims;

import java.util.List;

import org.apache.commons.collections4.map.MultiKeyMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eims.web.dao.CommCodeDao;
import eims.web.dto.UserInfo;
import eims.web.dto.table.CommCodeDto;

//@Component
//@Scope("prototype")
public class ServiceContext {
	private static final ThreadLocal<UserInfo> userInfo = new ThreadLocal<>();
	private static final MultiKeyMap<String, String> codeMap = new MultiKeyMap<>();

	private static final Logger logger = LoggerFactory.getLogger(ServiceContext.class);

	public static UserInfo getUserInfo() {
		return ServiceContext.userInfo.get();
	}


	public static void setUserInfo(UserInfo userInfo) {
		ServiceContext.userInfo.set(userInfo);
	}

	public static String getCodeValue(String codeId, String codeValue, String locale) {
		return codeMap.get(codeId, codeValue, locale);
	}
	
	public static void setCommCode(CommCodeDao codeDao) {
		if(codeMap.size() > 0) {
			logger.debug("CommCodeMap already set");
			return;
		}
		List<CommCodeDto> codeDtoList = codeDao.selectAll(null, null, 999999, 1);
		for (CommCodeDto commCodeDto : codeDtoList) {
			codeMap.put(commCodeDto.getCdId(), commCodeDto.getCdVal(), commCodeDto.getLangCd(), commCodeDto.getCdValNm());
			logger.debug("CD_ID [{}] CD_VAL [{}] LANG_CD [{}] CD_VAL_NM [{}]", commCodeDto.getCdId(), commCodeDto.getCdVal(), commCodeDto.getLangCd(), commCodeDto.getCdValNm());
		}
		logger.debug("{} CommCodes loaded", codeMap.size());
	}

	public static void clear() {
		ServiceContext.userInfo.remove();
	}
}
