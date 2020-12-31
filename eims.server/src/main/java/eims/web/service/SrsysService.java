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
import eims.web.dao.MaskcdDao;
import eims.web.dao.SrsysbsDao;
import eims.web.dto.table.CodeInterfaceDto;
import eims.web.dto.table.MaskcdDto;
import eims.web.dto.table.SrsysbsDto;
import eims.web.dto.ui.UiSrsysbsOut;
import eims.web.exception.ServiceException;

@Service
@Transactional(transactionManager="transactionManager")
public class SrsysService {

	final Logger logger = LoggerFactory.getLogger(getClass());
	final String stdAreaId = "6C5D465E52366C8BE0536541E60AEE45";
	final String cdId = "72BC4CDB6677A7C3E0536541E60A16FF";

	@Autowired
	private SrsysbsDao srsysbsDao;
	@Autowired
	private MaskcdDao maskcdDao;

	public UiSrsysbsOut getList(String sysCd, String sysNm, String sysCdDesc, String crgManNm, String noncoreYn, int pageSize, int pageNumber) {
		UiSrsysbsOut out = new UiSrsysbsOut();
		
		if(sysCd != null && !sysCd.equals("")) {
			sysCd = sysCd.toUpperCase() ;
		}

		int totalCount = srsysbsDao.selectAllCnt(sysCd, sysNm, sysCdDesc, crgManNm, noncoreYn);

		List<SrsysbsDto> srsysbsList = srsysbsDao.selectAll(sysCd, sysNm, sysCdDesc, crgManNm, noncoreYn, pageSize, pageNumber);

		if (srsysbsList == null) {
			srsysbsList = new ArrayList<SrsysbsDto>();
		}

		if (totalCount > 0) {
			out.setTotalCnt(totalCount);
			out.setSrsysbsOutList(srsysbsList);
		}

		return out;
	}

	public SrsysbsDto get(String sysCd) {
		SrsysbsDto out = srsysbsDao.selectSrsysbs(sysCd);

		if (out == null) {
			throw new ServiceException(BxMessages.Error.NOT_FOUNDED, sysCd);
		}
		return out;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public int add(SrsysbsDto in) {
		SrsysbsDto curSrsysbsInfo = srsysbsDao.selectSrsysbs(in.getSysCd());

		if (curSrsysbsInfo != null) {
			throw new ServiceException(BxMessages.Error.DUPLICATE_KEY, in.getSysCd());
		}

		return srsysbsDao.insertSrsysbs(in);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public int update(SrsysbsDto in) {
		int out;

		out = srsysbsDao.updateSrsysbs(in);
		if (out == 0) {
			throw new ServiceException(BxMessages.Error.NOT_FOUNDED, in.getSysCd());
		}

		return out;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public int delete(String sysCd) {
		int result;

		result = srsysbsDao.deleteSrsysbs(sysCd);
		if (result == 0) {
			throw new ServiceException(BxMessages.Error.NOT_FOUNDED, sysCd);
		}
		return result;

	}

	@Transactional(transactionManager="transactionManager", propagation=Propagation.REQUIRES_NEW)
	@Scheduled(cron = "0 5,15,25,35,45,55 * * * *")
	public void getMetaSysCode() {
		logger.debug("----- getMetaSysCode START -----");
		
		if(BxConstants.Default.IS_SERVER) {			
			List<CodeInterfaceDto> sysCodeList = srsysbsDao.selectSysCodeInterfaceList();
			for (CodeInterfaceDto tobe : sysCodeList) { 
				SrsysbsDto sysDto = new SrsysbsDto();
				sysDto.setSysCd(tobe.getDmnCdVal());
				sysDto.setSysNm(tobe.getDmnCdValGlbl());
				
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
						logger.error("SysCode Insert");
						srsysbsDao.insertSrsysbs(sysDto);
					} catch (DuplicateKeyException e) {
						srsysbsDao.updateSrsysbs(sysDto);
					} finally {
						srsysbsDao.updateSysCodeInterface(updateAppCodeInterfaceDto);
					}
					
					
				} else if(tobe.getIfDataAplyTpCd().equals("U")) {
					try {
						logger.error("SysCode Update");
						srsysbsDao.updateSrsysbs(sysDto);
					} catch (DuplicateKeyException e) {
						
					} finally {
						srsysbsDao.updateSysCodeInterface(updateAppCodeInterfaceDto);
					}
					srsysbsDao.updateSysCodeInterface(updateAppCodeInterfaceDto);
				} else if(tobe.getIfDataAplyTpCd().equals("D") ) {
					try {
						logger.error("SysCode Delete");							
						srsysbsDao.deleteSrsysbs(tobe.getDmnCdVal());
					} catch (DuplicateKeyException e) {
						
					} finally {
						srsysbsDao.updateSysCodeInterface(updateAppCodeInterfaceDto);
					}
				} else {
					logger.error("AppCode Nothing");	
				}
			} 		
		}	

		logger.debug("----- getMetaSysCode END -----");
		
	}


}
