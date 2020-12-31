package eims.web.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eims.web.constants.BxMessages;
import eims.web.dao.ExtrnlinstcdDao;
import eims.web.dto.table.BcMetaSysCdDto;
import eims.web.dto.table.ExtrnlinstcdDto;
import eims.web.dto.ui.UiExtrnlinstcdOut;
import eims.web.exception.ServiceException;
import eims.web.meta.dao.BcMetaDao;

@Service
@Transactional(transactionManager="transactionManager")
public class ExtrnlinstService {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ExtrnlinstcdDao extrnlinstcdDao;
	@Autowired
	private BcMetaDao bcMetaDao;


	public UiExtrnlinstcdOut getList(String instCd, String instCdNm, String instDstnctnVal, String instCdDesc,
			int pageSize, int pageNumber) {
		UiExtrnlinstcdOut out = new UiExtrnlinstcdOut();

		int totalCount = extrnlinstcdDao.selectAllCnt(instCd, instCdNm, instDstnctnVal, instCdDesc);

		List<ExtrnlinstcdDto> extrnlinstcdList = extrnlinstcdDao.selectAll(instCd, instCdNm, instDstnctnVal, instCdDesc,
				pageSize, pageNumber);

		if (extrnlinstcdList == null) {
			extrnlinstcdList = new ArrayList<ExtrnlinstcdDto>();
		}

		if (totalCount > 0) {
			out.setTotalCnt(totalCount);
			out.setExtrnlinstcdOutList(extrnlinstcdList);
		}

		return out;
	}


	public ExtrnlinstcdDto get(String instCd, String instDstnctnVal) {
		ExtrnlinstcdDto out = extrnlinstcdDao.selectExtrnlinstcd(instCd, instDstnctnVal);

		if (out == null) {
			throw new ServiceException(BxMessages.Error.NOT_FOUNDED, instCd, instDstnctnVal);
		}
		return out;
	}


	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public int add(ExtrnlinstcdDto in) {
		ExtrnlinstcdDto curExtrnlinstcdInfo = extrnlinstcdDao.selectExtrnlinstcd(in.getInstCd(),
				in.getInstDstnctnVal());

		if (curExtrnlinstcdInfo != null) {
			throw new ServiceException(BxMessages.Error.DUPLICATE_KEY, in.getInstCd(), in.getInstDstnctnVal());
		}

		return extrnlinstcdDao.insertExtrnlinstcd(in);
	}


	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public int update(ExtrnlinstcdDto in) {
		int out;

		out = extrnlinstcdDao.updateExtrnlinstcd(in);
		if (out == 0) {
			throw new ServiceException(BxMessages.Error.NOT_FOUNDED, in.getInstCd(), in.getInstDstnctnVal());
		}

		return out;
	}


	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public int delete(String instCd, String instDstnctnVal) {
		int result;

		result = extrnlinstcdDao.deleteExtrnlinstcd(instCd, instDstnctnVal);
		if (result == 0) {
			throw new ServiceException(BxMessages.Error.NOT_FOUNDED, instCd, instDstnctnVal);
		}
		return result;

	}
	
	@Transactional(transactionManager="transactionManager", propagation=Propagation.REQUIRES_NEW)
	@Scheduled(cron = "0 0 8 * * *")
//	@Scheduled(cron = "0 0,10,20,30,35,40,50 * * * *")
	public void getMetaExtCode() {
		
		String scheduledFlag = System.getProperty("eims.scheduled.flag") ;

		if(scheduledFlag != null && scheduledFlag.equals("true")) {
			logger.debug("------EXT Code Sync Start-----");
			
			String tobeCode = "6C5D465E52366C8BE0536541E60AEE45" ;
			String extCodeId = "730D182019CF2989E0536541E60A4533" ;
			
			int delCnt = extrnlinstcdDao.deleteExtrnlinstcdAll() ;
			logger.debug("삭제건수: " + delCnt);
			
			List<BcMetaSysCdDto> metaResultList = bcMetaDao.selectSrSysCode(tobeCode, extCodeId) ;
			
			for(BcMetaSysCdDto dto : metaResultList) {
				ExtrnlinstcdDto extCdDto = new ExtrnlinstcdDto() ;
				
				extCdDto.setInstCd(dto.getCdVal());
				extCdDto.setInstCdDesc(dto.getCdValNm());
				extCdDto.setInstCdNm(dto.getCdValNm());
				extCdDto.setInstDstnctnVal("INST");
				
				extrnlinstcdDao.insertExtrnlinstcd(extCdDto) ;
			}
			
			logger.debug("------EXT Code Sync End-----");
		}
		
	}

}
