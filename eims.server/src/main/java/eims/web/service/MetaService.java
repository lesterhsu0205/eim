package eims.web.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

import eims.ServiceContext;
import eims.web.constants.BxCode;
import eims.web.constants.BxConstants;
import eims.web.constants.BxMessages;
import eims.web.dao.MetabsDao;
import eims.web.dto.table.MetaEffectDto;
import eims.web.dto.table.MetaInterfaceDto;
import eims.web.dto.table.MetabsDto;
import eims.web.dto.ui.UiMetabsOut;
import eims.web.exception.ServiceException;
import eims.web.meta.dao.BcMetaDao;
import eims.web.utils.StringUtils;

@Service
@Transactional(transactionManager="transactionManager")
public class MetaService {

	final Logger logger = LoggerFactory.getLogger(getClass());

	private String tobeAreaId = "6C5D465E52366C8BE0536541E60AEE45";
	private String appAreaId = "6C5CA247225D6C57E0536541E60AE15F";

	@Autowired
	private MetabsDao metabsDao;
	@Autowired
	private BcMetaDao bcMetaDao;

	public UiMetabsOut getList(String metaEngNm, String metaKorNm, String dataTypeNm, int metaLen, int decimalLen, int pageSize, int pageNumber) {
		UiMetabsOut out = new UiMetabsOut();

		int totalCount = metabsDao.selectAllCnt(metaEngNm, metaKorNm, dataTypeNm, metaLen, decimalLen);

		List<MetabsDto> metabsList = metabsDao.selectAll(metaEngNm, metaKorNm, dataTypeNm, metaLen, decimalLen, pageSize, pageNumber);

		if (metabsList == null) {
			metabsList = new ArrayList<MetabsDto>();
		}

		if (totalCount > 0) {
			out.setTotalCnt(totalCount);
			out.setMetabsOutList(metabsList);
		}

		return out;
	}

	public MetabsDto get(String metaEngNm) {
		MetabsDto out = metabsDao.selectMetabs(metaEngNm);

		if (out == null) {
			throw new ServiceException(BxMessages.Error.NOT_FOUNDED, metaEngNm);
		}

		return out;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public int add(MetabsDto in) {
		MetabsDto curMetabsInfo = metabsDao.selectMetabs(in.getMetaEngNm());

		if (curMetabsInfo != null) {
			throw new ServiceException(BxMessages.Error.DUPLICATE_KEY, in.getMetaEngNm());
		}

		return metabsDao.insertMetabs(in);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public int update(MetabsDto in) {
		int out = metabsDao.updateMetabs(in);

		if (out == 0) {
			throw new ServiceException(BxMessages.Error.NOT_FOUNDED, in.getMetaEngNm());
		}

		return out;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public int delete(String metaEngNm) {
		int out = metabsDao.deleteMetabs(metaEngNm);

		if (out == 0) {
			throw new ServiceException(BxMessages.Error.NOT_FOUNDED, metaEngNm);
		}

		return out;
	}

	public List<MetaEffectDto> getEffectList(String metaEngNm, String metaKorNm, int len, String scaleVal, String dataTypeNm, String intrfcId, String intrfcNm) {

		MetaEffectDto metaEffectDto = new MetaEffectDto();

		metaEffectDto.setMetaEngNm(metaEngNm);
		metaEffectDto.setIntrfcId(intrfcId);
		metaEffectDto.setIntrfcNm(intrfcNm);

		List<MetaEffectDto> metaEffectResult = metabsDao.selectMetaEffect(metaEffectDto);

		return metaEffectResult;
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public int syncMeta(boolean roleCheck) {
		try {
			logger.error("###syncMeta start####");	
			
			if (roleCheck) {
				String roleId = ServiceContext.getUserInfo().getRoleId();
				if (roleId == null || !roleId.equals("Administrator")) {
					return 0;
				}
			}
						
			List<MetaInterfaceDto> metaListTobe = metabsDao.selectMetaInterfaceList();
			 
			if(!metaListTobe.isEmpty() || metaListTobe != null) {
				for (MetaInterfaceDto tobe : metaListTobe) {
					MetabsDto meta = new MetabsDto();
					meta.setMetaKorNm(tobe.getStdTrmLgclNm());
					
					if(tobe.getStdTrmTpCd().equals("N")) {
						//논코어 용어 일경우 카멜형식 변환 없이 그대로 저장
						meta.setMetaEngNm(tobe.getStdTrmPsclNm());
					} else {
						meta.setMetaEngNm(StringUtils.converStringToCamel(tobe.getStdTrmPsclNm()));
					}					
					
					String dataType = "";
					meta.setMetaLen(tobe.getStdTrmDataLen());
					meta.setMetaTermType(tobe.getStdTrmTpCd());
					
					
					if (tobe.getStdTrmDataTpNm().equals("VARCHAR")) {
						dataType = "STRING";
						meta.setDataTypeNm(dataType);
					} else if (tobe.getStdTrmDataTpNm().equals("NUMBER")) {
						if (tobe.getStdTrmDataScl() > 0 || tobe.getStdTrmDataLen() >= 10) {
							dataType = "BIGDECIMAL";
						} else {
							dataType = "INTEGER";
						}	
						meta.setDataTypeNm(dataType);
					} else {
						meta.setDataTypeNm(tobe.getStdTrmDataTpNm());
					}
					
					logger.error("metaListTobe : [{}]", meta.getMetaKorNm());
					
					Calendar calender = Calendar.getInstance();
					Date date = calender.getTime();
					String nowTime = new SimpleDateFormat("yyyyMMddHHmmss").format(date);
					MetaInterfaceDto updateMetaInterfaceDto = new MetaInterfaceDto();
					updateMetaInterfaceDto.setIfDataSeq(tobe.getIfDataSeq());
					updateMetaInterfaceDto.setIfDataAplyStsCd("Y");					
					updateMetaInterfaceDto.setIfDataAplyTmstmp(nowTime);
					updateMetaInterfaceDto.setIfDataAplyErrCd("");
					updateMetaInterfaceDto.setIfDataAplyErrMsg("");
					
					
					if(tobe.getIfDataAplyTpCd().equals("I")) {
						try {
							logger.error("meta insert");
							metabsDao.insertMetabs(meta);
						} catch (DuplicateKeyException e) {
							metabsDao.updateMetabs(meta);
						} finally {
							metabsDao.updateInterface(updateMetaInterfaceDto);
						}
						
						
					} else if(tobe.getIfDataAplyTpCd().equals("U")) {
						try {
							logger.error("meta update");
							metabsDao.updateMetabs(meta);
						} catch (DuplicateKeyException e) {
							
						} finally {
							metabsDao.updateInterface(updateMetaInterfaceDto);
						}
						metabsDao.updateInterface(updateMetaInterfaceDto);
					} else if(tobe.getIfDataAplyTpCd().equals("D") ) {
						try {
							logger.error("meta delete");							
							metabsDao.deleteMetabs(meta.getMetaEngNm());
						} catch (DuplicateKeyException e) {
							
						} finally {
							metabsDao.updateInterface(updateMetaInterfaceDto);
						}
					} else {
						logger.error("meta nothing");	
					}
					
				}
			}
			
		} catch (Exception e) {
			logger.error("{}", e);
			return 0;
		}
		return 0;
	}
/*
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public int syncMeta(boolean roleCheck) {
		
		try {

			if (roleCheck) {
				String roleId = ServiceContext.getUserInfo().getRoleId();
				if (roleId == null || !roleId.equals("Administrator")) {
					return 0;
				}
			}

//		int cnt = bcMetaDao.selectAllCnt();
			List<BcMetaDto> selectMetaListTobe = bcMetaDao.selectTobe(tobeAreaId);
			List<BcMetaDto> selectMetaListApp = bcMetaDao.selectApp(appAreaId);
			MetabsDto eimsMetaDto;
//		List<MetabsDto> metaDtoList = new ArrayList<MetabsDto>();
			int cnt = 0;

//		int deleteCnt = metabsDao.deleteMetaAll();

			List<MetabsDto> tobeArrayList = new ArrayList<MetabsDto>();
			List<MetabsDto> appArrayList = new ArrayList<MetabsDto>();

			for (BcMetaDto bcMeta : selectMetaListTobe) {
				eimsMetaDto = new MetabsDto();
				String camelCase = StringUtils.converStringToCamel(bcMeta.getDicPhyNm());

				eimsMetaDto.setMetaEngNm(camelCase);
				eimsMetaDto.setMetaKorNm(bcMeta.getDicLogNm());
				eimsMetaDto.setDataTypeNm(typeCheck(bcMeta.getDataTypeNm(), bcMeta.getDataScale()));
				if (bcMeta.getDataTypeNm().equals(BxCode.DataBaseType.TIMESTAMP.toString())) {
					eimsMetaDto.setMetaLen(23);
				} else if (bcMeta.getDataTypeNm().equals(BxCode.DataBaseType.DATE.toString())) {
					eimsMetaDto.setMetaLen(23);
				} else {
					eimsMetaDto.setMetaLen(bcMeta.getDataLen());
				}
				eimsMetaDto.setDecimalLen(bcMeta.getDataScale());
				tobeArrayList.add(eimsMetaDto); //투비차세대
			}

			for (BcMetaDto bcMeta : selectMetaListApp) {
				eimsMetaDto = new MetabsDto();
				String camelCase = StringUtils.converStringToCamel(bcMeta.getDicPhyNm());

				eimsMetaDto.setMetaEngNm(camelCase);
				eimsMetaDto.setMetaKorNm(bcMeta.getDicLogNm());
				eimsMetaDto.setDataTypeNm(typeCheck(bcMeta.getDataTypeNm(), bcMeta.getDataScale()));
				if (bcMeta.getDataTypeNm().equals(BxCode.DataBaseType.TIMESTAMP.toString())) {
					eimsMetaDto.setMetaLen(23);
				} else if (bcMeta.getDataTypeNm().equals(BxCode.DataBaseType.DATE.toString())) {
					eimsMetaDto.setMetaLen(23);
				} else {
					eimsMetaDto.setMetaLen(bcMeta.getDataLen());
				}
				eimsMetaDto.setDecimalLen(bcMeta.getDataScale());
				appArrayList.add(eimsMetaDto); //응용표준
			}

			logger.debug("appList size: {}", tobeArrayList.size());
			logger.debug("tobeList size: {}", appArrayList.size());

			for (MetabsDto app : appArrayList) {
				logger.debug("tobetobetobetobetobetobe");
//			MetabsDto metabsDto = metabsDao.selectMetabs(app.getMetaKorNm());

				try {
					metabsDao.insertMetabs(app);
				} catch (DuplicateKeyException e) {
					metabsDao.updateMetabs(app);
				}

//			try {
//				if (metabsDto == null) {
//					metabsDao.insertMetabs(app);
//				} else {
//					metabsDao.updateMetabs(app);
//				}
//			} catch (Exception e) {
//				logger.error("{}", e);
//			}

//			int updateCnt = metabsDao.updateMetabs(tobe);
//			if (updateCnt == 0) {
//				try {
//					metabsDao.insertMetabs(tobe);
//				} catch (DuplicateKeyException e) {
//				}
//			}
			}

			for (MetabsDto tobe : tobeArrayList) {
				logger.debug("appappappappappappapp");
//			MetabsDto metabsDto = metabsDao.selectMetabs(tobe.getMetaKorNm());

				try {
					metabsDao.insertMetabs(tobe);
				} catch (DuplicateKeyException e) {
					metabsDao.updateMetabs(tobe);
				}

//			if (metabsDto == null) {
//				metabsDao.insertMetabs(tobe);
//			} else {
//				metabsDao.updateMetabs(tobe);
//			}

//			int updateCnt = metabsDao.updateMetabs(app);
//			if (updateCnt == 0) {
//				try {
//					metabsDao.insertMetabs(app);
//				} catch (DuplicateKeyException e) {
//				}
//			}
			}
		} catch (Exception e) {
			logger.error("{}", e);
			return 0;
		}
		return 0;
	}
*/
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@Scheduled(cron = "0 */1 * * * *")
	public void syncMetaSchedule() {	    

		if(BxConstants.Default.IS_SERVER) { 
		//if(true) {		
			logger.debug("----- syncMetaSchedule START -----");
			System.out.println("11 syncMetaSchedule !!");
			List<MetaInterfaceDto> metaListTobe = metabsDao.selectMetaInterfaceList();
			
 			if(!metaListTobe.isEmpty() || metaListTobe != null) {
				for (MetaInterfaceDto tobe : metaListTobe) {
					MetabsDto meta = new MetabsDto();
					meta.setMetaKorNm(tobe.getStdTrmLgclNm());
					
					if(tobe.getStdTrmTpCd().equals("N")) {
						//논코어 용어 일 경우 카멜형식 변환 없이 그대로 저장
						meta.setMetaEngNm(tobe.getStdTrmPsclNm());
					} else {
						meta.setMetaEngNm(StringUtils.converStringToCamel(tobe.getStdTrmPsclNm()));
					}					
					
					String dataType = "";
					meta.setMetaLen(tobe.getStdTrmDataLen());
					meta.setMetaTermType(tobe.getStdTrmTpCd());
					
					if (tobe.getStdTrmDataTpNm().equals("VARCHAR")) {
						dataType = "STRING";
						meta.setDataTypeNm(dataType);
					} else if (tobe.getStdTrmDataTpNm().equals("NUMBER")) {
						if (tobe.getStdTrmDataScl() > 0 || tobe.getStdTrmDataLen() >= 10) {
							dataType = "BIGDECIMAL";
						} else {
							dataType = "INTEGER";
						}	
						meta.setDataTypeNm(dataType);
					} else {
						meta.setDataTypeNm(tobe.getStdTrmDataTpNm());
					}
					
					meta.setDecimalLen(tobe.getStdTrmDataScl());					
					meta.setMetaEncYn(tobe.getStdTrmDmnEncYn());
					logger.error("metaListTobe : [{}]", meta.getMetaKorNm());
					 
					Calendar calender = Calendar.getInstance();
					Date date = calender.getTime();
					String nowTime = new SimpleDateFormat("yyyyMMddHHmmss").format(date);
					MetaInterfaceDto updateMetaInterfaceDto = new MetaInterfaceDto();
					updateMetaInterfaceDto.setIfDataSeq(tobe.getIfDataSeq());
					updateMetaInterfaceDto.setIfDataAplyStsCd("Y");					
					updateMetaInterfaceDto.setIfDataAplyTmstmp(nowTime);
					updateMetaInterfaceDto.setIfDataAplyErrCd("");
					updateMetaInterfaceDto.setIfDataAplyErrMsg("");
															
					if(tobe.getIfDataAplyTpCd().equals("I")) {
						try {
							logger.error("meta insert");
							metabsDao.insertMetabs(meta);
						} catch (DuplicateKeyException e) {
							metabsDao.updateMetabs(meta);
						}
						metabsDao.updateInterface(updateMetaInterfaceDto);
						
					} else if(tobe.getIfDataAplyTpCd().equals("U")) {
						try {
							logger.error("meta update");
							metabsDao.updateMetabs(meta);
						} catch (DuplicateKeyException e) {
							
						}
						metabsDao.updateInterface(updateMetaInterfaceDto);
					} else if(tobe.getIfDataAplyTpCd().equals("D") ) {
						try {
							logger.error("meta delete");							
							metabsDao.deleteMetabs(meta.getMetaEngNm());
						} catch (DuplicateKeyException e) {
							
						}
						metabsDao.updateInterface(updateMetaInterfaceDto);
					} else {
						logger.error("meta nothing");	
					}
					
				}
			}				
		}		
		logger.debug("----- syncMetaSchedule END -----");
	}
		
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@Scheduled(cron = "0 7,17,27,37,47,57 * * * *")
	public void uploadGitSchedule() throws IOException {	    

		if(BxConstants.Default.IS_SERVER) { 		
			logger.error("----- uploadGitSchedule START -----");
			Runtime runtime = Runtime.getRuntime();
			Process process = runtime.exec("/logs/jboss/opseim01_18080/eims_logs/deploy/lbtw_deploy_interface/git_upload.sh");
			InputStream is = process.getInputStream();
			InputStreamReader isr = new  InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line;
			while((line  = br.readLine()) != null) {
				logger.error(line);	
			}
			logger.error("----- uploadGitSchedule END -----");
		}		
		
	}

	private String typeCheck(String dataType, int scaleLen) {

		if (dataType.equals(BxCode.DataBaseType.VARCHAR.toString())) {
			return BxCode.DataType.STRING.toString();
		} else if (dataType.equals(BxCode.DataBaseType.NUMERIC.toString()) && scaleLen == 0) {
			return BxCode.DataType.LONG.toString();
		} else if (dataType.equals(BxCode.DataBaseType.NUMERIC.toString()) && scaleLen != 0) {
			return BxCode.DataType.BIGDECIMAL.toString();
		} else if (dataType.equals(BxCode.DataBaseType.CLOB.toString())) {
			return BxCode.DataType.STRING.toString();
		} else if (dataType.equals(BxCode.DataBaseType.BLOB.toString())) {
			return BxCode.DataType.BYTEARRAY.toString();
		} else if (dataType.equals(BxCode.DataBaseType.TIMESTAMP.toString())) {
			return BxCode.DataType.STRING.toString();
		} else if (dataType.equals(BxCode.DataBaseType.DATE.toString())) {
			return BxCode.DataType.STRING.toString();
		} else {
			return BxCode.DataType.STRING.toString();
		}
	}
}
