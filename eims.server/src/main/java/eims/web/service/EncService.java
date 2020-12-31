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
import eims.web.dao.EnccdDao;
import eims.web.dto.table.CodeInterfaceDto;
import eims.web.dto.table.EnccdDto;
import eims.web.dto.table.MaskcdDto;
import eims.web.dto.ui.UiEncOut;
import eims.web.dto.ui.UiMaskOut;

@Service
@Transactional(transactionManager="transactionManager")
public class EncService {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private EnccdDao enccdDao;

	public UiEncOut getList() {
		UiEncOut out = new UiEncOut();		

		int totalCount = enccdDao.selectAllCnt();

		List<EnccdDto> encList = enccdDao.selectAll();

		if (encList == null) {
			encList = new ArrayList<EnccdDto>();
		}

		if (totalCount > 0) {
			out.setTotalCnt(totalCount);
			out.setEncOutList(encList);
		}

		return out;
	}


	@Transactional(transactionManager="transactionManager", propagation=Propagation.REQUIRES_NEW)
	@Scheduled(cron = "0 7,17,27,37,47,57 * * * *")
	public void getMetaEncCode() {
		logger.debug("----- getMetaEncCode START -----");
		
		if(BxConstants.Default.IS_SERVER) {
			List<CodeInterfaceDto> encCodeList = enccdDao.selectEncCodeInterfaceList();
			for (CodeInterfaceDto tobe : encCodeList) { 
				EnccdDto encDto = new EnccdDto();
				encDto.setEncCd(tobe.getDmnCdVal());
				encDto.setEncNm(tobe.getDmnCdValGlbl());
				
				Calendar calender = Calendar.getInstance();
				Date date = calender.getTime();
				String nowTime = new SimpleDateFormat("yyyyMMddHHmmss").format(date);
				CodeInterfaceDto updateEncCodeInterfaceDto = new CodeInterfaceDto();
				updateEncCodeInterfaceDto.setIfDataSeq(tobe.getIfDataSeq());
				updateEncCodeInterfaceDto.setIfDataAplyStsCd("Y");					
				updateEncCodeInterfaceDto.setIfDataAplyTmstmp(nowTime);
				updateEncCodeInterfaceDto.setIfDataAplyErrCd("");
				updateEncCodeInterfaceDto.setIfDataAplyErrMsg("");
				
				if(tobe.getIfDataAplyTpCd().equals("I")) {
					try {
						logger.error("EncCode Insert");
						enccdDao.insertEnccd(encDto);
					} catch (DuplicateKeyException e) {
						enccdDao.updateEnccd(encDto);
					} finally {
						enccdDao.updateEncCodeInterface(updateEncCodeInterfaceDto);
					}
					
					
				} else if(tobe.getIfDataAplyTpCd().equals("U")) {
					try {
						logger.error("EncCode Update");
						enccdDao.updateEnccd(encDto);
					} catch (DuplicateKeyException e) {
						
					} finally {
						enccdDao.updateEncCodeInterface(updateEncCodeInterfaceDto);
					}
					enccdDao.updateEncCodeInterface(updateEncCodeInterfaceDto);
				} else if(tobe.getIfDataAplyTpCd().equals("D") ) {
					try {
						logger.error("EncCode Delete");							
						enccdDao.deleteEnccd(tobe.getDmnCdVal());
					} catch (DuplicateKeyException e) {
						
					} finally {
						enccdDao.updateEncCodeInterface(updateEncCodeInterfaceDto);
					}
				} else {
					logger.error("EncCode Nothing");	
				}
			} 		
		}	

		logger.debug("----- getMetaMaskCode END -----");
		
	}


}
