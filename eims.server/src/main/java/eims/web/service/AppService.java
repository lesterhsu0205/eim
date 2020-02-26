package eims.web.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eims.web.constants.BxConstants;
import eims.web.constants.BxMessages;
import eims.web.dao.AppcdDao;
import eims.web.dto.table.AppcdDto;
import eims.web.dto.table.CodeInterfaceDto;
import eims.web.dto.ui.UiAppcdOut;
import eims.web.exception.ServiceException;
import eims.web.meta.dao.BcMetaDao;

@Service
@Transactional(transactionManager="transactionManager")
public class AppService {

	final Logger logger = LoggerFactory.getLogger(getClass());
	final String stdAreaId = "6C5D465E52366C8BE0536541E60AEE45";

	@Autowired
	private AppcdDao appcdDao;
	@Autowired
	private BcMetaDao bcMetaDao;

	public UiAppcdOut getList(String appCd, String appCdNm, String parentAppCd, String lvCd, int alignOrderNo,
			String appCdDesc, int pageSize, int pageNumber) {
		UiAppcdOut out = new UiAppcdOut();
		
		if(appCd != null && !appCd.equals("")) {
			appCd = appCd.toUpperCase() ;
		}
		

		int totalCount = appcdDao.selectAllCnt(appCd, appCdNm, parentAppCd, lvCd, alignOrderNo, appCdDesc);

		List<AppcdDto> appcdList = appcdDao.selectAll(appCd, appCdNm, parentAppCd, lvCd, alignOrderNo, appCdDesc,
				pageSize, pageNumber);

		if (appcdList == null) {
			appcdList = new ArrayList<AppcdDto>();
		}

		for (int i = 0; i < appcdList.size(); i++) {
			String appCdTemp = appcdList.get(i).getAppCd();
			String appLvCdTemp = appcdList.get(i).getLvCd();
			String parentAppCdTemp = appcdList.get(i).getParentAppCd();

			StringBuilder appUnqKey = new StringBuilder();
			appUnqKey.append(appCdTemp);
			appUnqKey.append("_");
			appUnqKey.append(appLvCdTemp);

			if (parentAppCdTemp != null && !"".equals(parentAppCdTemp)) {
				appUnqKey.append("_");
				appUnqKey.append(parentAppCdTemp);
			}
			appcdList.get(i).setAppUnqKey(appUnqKey.toString());
		}

		if (totalCount > 0) {
			out.setTotalCnt(totalCount);
			out.setAppcdOutList(appcdList);
		}

		return out;
	}

	public AppcdDto get(String appCd, String parentAppCd, String lvCd) {
		AppcdDto out = appcdDao.selectAppcd(appCd, parentAppCd, lvCd);

		if (out == null) {
			throw new ServiceException(BxMessages.Error.NOT_FOUNDED, appCd, parentAppCd, lvCd);
		}
		return out;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public int add(AppcdDto in) {
		AppcdDto curAppcdInfo = appcdDao.selectAppcd(in.getAppCd(), in.getParentAppCd(), in.getLvCd());

		if (curAppcdInfo != null) {
			throw new ServiceException(BxMessages.Error.DUPLICATE_KEY, in.getAppCd(), in.getParentAppCd(),
					in.getLvCd());
		}

		return appcdDao.insertAppcd(in);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public int update(AppcdDto in) {
		int out;

		out = appcdDao.updateAppcd(in);
		if (out == 0) {
			throw new ServiceException(BxMessages.Error.NOT_FOUNDED, in.getAppCd(), in.getParentAppCd(), in.getLvCd());
		}

		return out;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public int delete(String appCd, String parentAppCd, String lvCd) {
		int result;

		result = appcdDao.deleteAppcd(appCd, parentAppCd, lvCd);
		if (result == 0) {
			throw new ServiceException(BxMessages.Error.NOT_FOUNDED, appCd, parentAppCd, lvCd);
		}
		return result;

	}

	@Transactional(transactionManager="transactionManager", propagation=Propagation.REQUIRES_NEW)
	@Scheduled(cron = "0 2,12,22,32,42,52 * * * *")
	public void getMetaAppCode() {
		logger.debug("----- getMetaAppCode START -----");
		
		if(BxConstants.Default.IS_SERVER) {
			AppcdDto appDto = new AppcdDto();

			List<CodeInterfaceDto> appCodeList = appcdDao.selectAppCodeInterfaceList();
			for (CodeInterfaceDto tobe : appCodeList) { 
				appDto = new AppcdDto();
				appDto.setAppCd(tobe.getDmnCdVal());
				appDto.setAppCdNm(tobe.getDmnCdValGlbl());
				appDto.setLvCd("1");
				appDto.setAlignOrderNo(0);
				
				Calendar calender = Calendar.getInstance();
				Date date = calender.getTime();
				String nowTime = new SimpleDateFormat("yyyyMMddHHmmss").format(date);
				CodeInterfaceDto updateAppCodeInterfaceDto = new CodeInterfaceDto();
				updateAppCodeInterfaceDto.setIfDataSeq(tobe.getIfDataSeq());
				updateAppCodeInterfaceDto.setIfDataAplyStsCd("Y");					
				updateAppCodeInterfaceDto.setIfDataAplyTmstmp(nowTime);
				updateAppCodeInterfaceDto.setIfDataAplyErrCd("");
				updateAppCodeInterfaceDto.setIfDataAplyErrMsg("");
				
				if(tobe.getIfDataAplyTpCd().equals("I")) {
					try {
						logger.error("AppCode Insert");
						appcdDao.insertAppcd(appDto);
					} catch (DuplicateKeyException e) {
						appcdDao.updateAppcd(appDto);
					} finally {
						appcdDao.updateAppCodeInterface(updateAppCodeInterfaceDto);
					}
					
					
				} else if(tobe.getIfDataAplyTpCd().equals("U")) {
					try {
						logger.error("AppCode Update");
						appcdDao.updateAppcd(appDto);
					} catch (DuplicateKeyException e) {
						
					} finally {
						appcdDao.updateAppCodeInterface(updateAppCodeInterfaceDto);
					}
					appcdDao.updateAppCodeInterface(updateAppCodeInterfaceDto);
				} else if(tobe.getIfDataAplyTpCd().equals("D") ) {
					try {
						logger.error("AppCode Delete");							
						appcdDao.deleteAppcd(tobe.getDmnCdVal(), null, "1");
					} catch (DuplicateKeyException e) {
						
					} finally {
						appcdDao.updateAppCodeInterface(updateAppCodeInterfaceDto);
					}
				} else {
					logger.error("AppCode Nothing");	
				}
			}			
		}
		logger.debug("----- getMetaAppCode END -----");
		
	}


}
