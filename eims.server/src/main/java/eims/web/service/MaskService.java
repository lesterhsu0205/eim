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
import eims.web.dao.MaskcdDao;
import eims.web.dto.table.CodeInterfaceDto;
import eims.web.dto.table.MaskcdDto;
import eims.web.dto.ui.UiMaskOut;
import eims.web.dto.ui.UiSrsysbsOut;

@Service
@Transactional(transactionManager="transactionManager")
public class MaskService {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private MaskcdDao maskcdDao;

	public UiMaskOut getList() {
		UiMaskOut out = new UiMaskOut();
		

		int totalCount = maskcdDao.selectAllCnt();

		List<MaskcdDto> maskList = maskcdDao.selectAll();

		if (maskList == null) {
			maskList = new ArrayList<MaskcdDto>();
		}

		if (totalCount > 0) {
			out.setTotalCnt(totalCount);
			out.setMaskOutList(maskList);;
		}

		return out;
	}


	@Transactional(transactionManager="transactionManager", propagation=Propagation.REQUIRES_NEW)
	@Scheduled(cron = "0 6,16,26,36,46,56 * * * *")
	public void getMetaMaskCode() {
		logger.debug("----- getMetaMaskCode START -----");
		
		if(BxConstants.Default.IS_SERVER) {		
			List<CodeInterfaceDto> maskCodeList = maskcdDao.selectMaskCodeInterfaceList();
			for (CodeInterfaceDto tobe : maskCodeList) { 
				MaskcdDto maskDto = new MaskcdDto();
				maskDto.setMaskCd(tobe.getDmnCdVal());
				maskDto.setMaskNm(tobe.getDmnCdValGlbl());
				
				Calendar calender = Calendar.getInstance();
				Date date = calender.getTime();
				String nowTime = new SimpleDateFormat("yyyyMMddHHmmss").format(date);
				CodeInterfaceDto updateMaskCodeInterfaceDto = new CodeInterfaceDto();
				updateMaskCodeInterfaceDto.setIfDataSeq(tobe.getIfDataSeq());
				updateMaskCodeInterfaceDto.setIfDataAplyStsCd("Y");					
				updateMaskCodeInterfaceDto.setIfDataAplyTmstmp(nowTime);
				updateMaskCodeInterfaceDto.setIfDataAplyErrCd("");
				updateMaskCodeInterfaceDto.setIfDataAplyErrMsg("");
				
				if(tobe.getIfDataAplyTpCd().equals("I")) {
					try {
						logger.error("MaskCode Insert");
						maskcdDao.insertMaskcd(maskDto);
					} catch (DuplicateKeyException e) {
						maskcdDao.updateMaskcd(maskDto);
					} finally {
						maskcdDao.updateMaskCodeInterface(updateMaskCodeInterfaceDto);
					}
					
					
				} else if(tobe.getIfDataAplyTpCd().equals("U")) {
					try {
						logger.error("MaskCode Update");
						maskcdDao.updateMaskcd(maskDto);
					} catch (DuplicateKeyException e) {
						
					} finally {
						maskcdDao.updateMaskCodeInterface(updateMaskCodeInterfaceDto);
					}
					maskcdDao.updateMaskCodeInterface(updateMaskCodeInterfaceDto);
				} else if(tobe.getIfDataAplyTpCd().equals("D") ) {
					try {
						logger.error("MaskCode Delete");							
						maskcdDao.deleteMaskcd(tobe.getDmnCdVal());
					} catch (DuplicateKeyException e) {
						
					} finally {
						maskcdDao.updateMaskCodeInterface(updateMaskCodeInterfaceDto);
					}
				} else {
					logger.error("MaskCode Nothing");	
				}
			} 		
		}	

		logger.debug("----- getMetaMaskCode END -----");
		
	}


}
