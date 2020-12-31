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
import eims.web.dao.BizcdDao;
import eims.web.dto.table.BcMetaSysCdDto;
import eims.web.dto.table.BizcdDto;
import eims.web.dto.table.ExtrnlinstcdDto;
import eims.web.dto.ui.UiBizcdOut;
import eims.web.exception.ServiceException;
import eims.web.meta.dao.BcMetaDao;

@Service
@Transactional(transactionManager="transactionManager")
public class BizService {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private BizcdDao bizcdDao;
	@Autowired
	private BcMetaDao bcMetaDao;

	public UiBizcdOut getList(String bizCd, String bizCdNm, String bizCdDesc, int pageSize, int pageNumber) {
		UiBizcdOut out = new UiBizcdOut();
		
		if(bizCd != null && !bizCd.equals("")) {
			bizCd = bizCd.toUpperCase() ;
		}

		int totalCount = bizcdDao.selectAllCnt(bizCd, bizCdNm, bizCdDesc);

		List<BizcdDto> bizcdList = bizcdDao.selectAll(bizCd, bizCdNm, bizCdDesc, pageSize, pageNumber);

		if (bizcdList == null) {
			bizcdList = new ArrayList<BizcdDto>();
		}

		if (totalCount > 0) {
			out.setTotalCnt(totalCount);
			out.setBizcdOutList(bizcdList);
		}

		return out;
	}

	public BizcdDto get(String bizCd) {
		BizcdDto out = bizcdDao.selectBizcd(bizCd);

		if (out == null) {
			throw new ServiceException(BxMessages.Error.NOT_FOUNDED, bizCd);
		}
		return out;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public int add(BizcdDto in) {
		BizcdDto curBizcdInfo = bizcdDao.selectBizcd(in.getBizCd());

		if (curBizcdInfo != null) {
			throw new ServiceException(BxMessages.Error.DUPLICATE_KEY, in.getBizCd());
		}

		return bizcdDao.insertBizcd(in);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public int update(BizcdDto in) {
		int out;

		out = bizcdDao.updateBizcd(in);
		if (out == 0) {
			throw new ServiceException(BxMessages.Error.NOT_FOUNDED, in.getBizCd());
		}

		return out;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public int delete(String bizCd) {
		int result;

		result = bizcdDao.deleteBizcd(bizCd);
		if (result == 0) {
			throw new ServiceException(BxMessages.Error.NOT_FOUNDED, bizCd);
		}
		return result;

	}
	
	@Transactional(transactionManager="transactionManager", propagation=Propagation.REQUIRES_NEW)
	@Scheduled(cron = "0 0 8 * * *")
//	@Scheduled(cron = "0 0,10,20,30,35,40,50 * * * *")
	public void getMetaBizCode() {
		
		String scheduledFlag = System.getProperty("eims.scheduled.flag") ;

		if(scheduledFlag != null && scheduledFlag.equals("true")) {
			logger.debug("------EXT Code Sync Start-----");
			
			String tobeCode = "6C5D465E52366C8BE0536541E60AEE45" ;
			String bizCodeId = "730D182019D02989E0536541E60A4533" ;
			
			
			int delCnt = bizcdDao.deleteBizcdAll() ;
			logger.debug("삭제건수: " + delCnt);
			
			List<BcMetaSysCdDto> metaResultList = bcMetaDao.selectSrSysCode(tobeCode, bizCodeId) ;
			
			for(BcMetaSysCdDto dto : metaResultList) {
				BizcdDto bizcd = new BizcdDto() ;
				
				bizcd.setBizCd(dto.getCdVal());
				bizcd.setBizCdNm(dto.getCdValNm());
				bizcd.setBizCdDesc(dto.getCdValNm());
				
				bizcdDao.insertBizcd(bizcd) ;
			}
			
			logger.debug("------EXT Code Sync End-----");
		}
		
	}

}
