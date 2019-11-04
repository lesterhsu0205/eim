package eims.web.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import eims.ServiceContext;
import eims.web.constants.BxCode;
import eims.web.constants.BxMessages;
import eims.web.dao.AppcdDao;
import eims.web.dao.BizcdDao;
import eims.web.dao.CommCodeDao;
import eims.web.dao.MsglayoutbsDao;
import eims.web.dao.MsglayoutdtDao;
import eims.web.dao.SrsysbsDao;
import eims.web.dto.table.ActionhisthsDto;
import eims.web.dto.table.CommCodeDto;
import eims.web.dto.table.IntrfcdeployhisthsDto;
import eims.web.dto.table.MsgIdCreateDto;
import eims.web.dto.table.MsgLayoutEffectDto;
import eims.web.dto.table.MsglayoutbsDto;
import eims.web.dto.table.MsglayoutbsFileUploadDto;
import eims.web.dto.table.MsglayoutbsListDto;
import eims.web.dto.table.MsglayoutdtDto;
import eims.web.dto.ui.UiMsglayoutbsOut;
import eims.web.exception.ServiceException;
import eims.web.utils.DateUtils;
import eims.web.utils.ExcelUtils;
import eims.web.utils.JsonUtils;
import eims.web.utils.UidUtils;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class MsglayoutService {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private MsglayoutbsDao msglayoutbsDao;
	@Autowired
	private MsglayoutdtDao msglayoutdtDao;	
	@Autowired
	private AppcdDao appcdDao;
	@Autowired
	private SrsysbsDao srsysbsDao;
	@Autowired
	private BizcdDao bizCdDao;
	@Autowired
	private SequenceService seqService;
	@Autowired
	private ActionhistService actionhistService;
	@Autowired
	private CommCodeDao codeDao;

	public UiMsglayoutbsOut getList(String msgNm, String msgNmSub, int msgVersion, String msgDscd, String regManId, String regDttm,
			String msgDataVal, String msgDesc, String extrnlBizNm, String dtoNm, String msgLayoutId, String appTypeCd,
			String trxCd, String appNm, String lv1Cd, String lv2Cd, String lv3Cd, String lv4Cd, String lv5Cd,
			String trxDscd, String bitMapCrtnYn, String jobId, String chlDscd, String iso8583DataTypeCd, int crtnSeq,
			String rsrvFldVal1, String rsrvFldVal2, String rsrvFldVal3, String bitMapTypeCd, String cfgDesc,
			String indsYn, String workStatusCd, String custApiYn, Integer msgRvsNo, int pageSize, int pageNumber) {
		UiMsglayoutbsOut out = new UiMsglayoutbsOut();

		int totalCount = msglayoutbsDao.selectAllCnt(msgNm, msgNmSub, msgVersion, msgDscd, regManId, regDttm, msgDataVal, msgDesc,
				extrnlBizNm, dtoNm, msgLayoutId, appTypeCd, trxCd, appNm, lv1Cd, lv2Cd, lv3Cd, lv4Cd, lv5Cd, trxDscd,
				bitMapCrtnYn, jobId, chlDscd, iso8583DataTypeCd, crtnSeq, rsrvFldVal1, rsrvFldVal2, rsrvFldVal3,
				bitMapTypeCd, cfgDesc, indsYn, workStatusCd, custApiYn, msgRvsNo);

		List<MsglayoutbsDto> msglayoutbsList = msglayoutbsDao.selectAll(msgNm, msgNmSub, msgVersion, msgDscd, regManId, regDttm,
				msgDataVal, msgDesc, extrnlBizNm, dtoNm, msgLayoutId, appTypeCd, trxCd, appNm, lv1Cd, lv2Cd, lv3Cd,
				lv4Cd, lv5Cd, trxDscd, bitMapCrtnYn, jobId, chlDscd, iso8583DataTypeCd, crtnSeq, rsrvFldVal1,
				rsrvFldVal2, rsrvFldVal3, bitMapTypeCd, cfgDesc, indsYn, workStatusCd, custApiYn, msgRvsNo, pageSize,
				pageNumber);

		if (msglayoutbsList == null) {
			msglayoutbsList = new ArrayList<MsglayoutbsDto>();
		}

		if (totalCount > 0) {
			out.setTotalCnt(totalCount);
			out.setMsglayoutbsOutList(msglayoutbsList);
		}

		return out;
	}

	public MsglayoutbsDto get(String msgLayoutId) {
		MsglayoutbsDto out = msglayoutbsDao.selectMsglayoutbs(msgLayoutId);

		if (out == null) {
			throw new ServiceException(BxMessages.Error.NOT_FOUNDED, msgLayoutId);
		}
		List<MsglayoutdtDto> msglayoutdtList = msglayoutdtDao.selectMsglayoutdt(msgLayoutId);
		
		out.setMsglayoutdtDto(msglayoutdtList);

		return out;
	}
	
	public void makeDeployFile(MultipartFile messageFile) {
		System.out.println("@@@@@makeDeployFile");
		List<IntrfcdeployhisthsDto> list = new ArrayList<IntrfcdeployhisthsDto>();
		
		list = msglayoutbsDao.selectDeployList();
		
		for(IntrfcdeployhisthsDto item : list) {
			String id = item.getIntrfcId();
			String deployInfo = item.getRawData();
			System.out.println("interface@@@@:"+id);
			System.out.println(deployInfo);
			String deployPath = "C:\\linebank\\temp\\deploy\\";
			String deployFileName = deployPath + id + ".json";
			File deployFile = new File(deployFileName);
			FileWriter writer = null;
			try {
				writer = new FileWriter(deployFile, false);
				writer.write(deployInfo);
				writer.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.error("{}", e);
			}
		} 
	}
	
	public MsglayoutbsListDto getList(List<String> msgLayoutList) {

		List<MsglayoutbsDto> list = new ArrayList<MsglayoutbsDto>();
		MsglayoutbsListDto msglayoutbsListDto = new MsglayoutbsListDto();

		for (String msgId : msgLayoutList) {
			MsglayoutbsDto out = msglayoutbsDao.selectMsglayoutbs(msgId);

			if (out == null) {
				throw new ServiceException(BxMessages.Error.NOT_FOUNDED, msgId);
			}
			List<MsglayoutdtDto> msglayoutdtList = msglayoutdtDao.selectMsglayoutdt(msgId);

			out.setMsglayoutdtDto(msglayoutdtList);

			list.add(out);
		}

		msglayoutbsListDto.setMsglayoutbsDtoList(list);

		return msglayoutbsListDto;
	}

	// public void listAdd(List<MsglayoutdtDto> dtoList) {
	// for (MsglayoutdtDto dto : dtoList) {
	//
	// if (dto.getDataTypeNm().equals("Layout")) {
	// List<MsglayoutdtDto> dtoList2 =
	// msglayoutdtDao.selectMsglayoutdt(dto.getFldEngNm());
	// dto.setMsglayoutdtDto(dtoList2);
	// listAdd(dtoList2);
	// }
	// }
	// }
	public boolean validationIoNm(MsglayoutbsDto msgLayoutbsDto, StringBuilder str) {

		boolean isValid = true;

		String chlDscd = msgLayoutbsDto.getChlDscd();
		String ioNm = null;
		String layoutId = msgLayoutbsDto.getMsgLayoutId();
		List<MsglayoutdtDto> msgDtList = msgLayoutbsDto.getMsglayoutdtDto();

		if (chlDscd.equals("INTERNAL")) {
			ioNm = msgLayoutbsDto.getMsgDataVal();
		} else {
			ioNm = msgLayoutbsDto.getDtoNm();
		}

		if (ioNm != null && !ioNm.equals("")) {
			List<String> msgDupList = msglayoutbsDao.selectSameIONmInternal(ioNm, layoutId);
			List<String> msgDupListExt = msglayoutbsDao.selectSameIONmExternal(ioNm, layoutId);
			List<String> childDupList = msglayoutdtDao.selectSameIONmChildNm(ioNm, layoutId);
			if (msgDupList != null && msgDupList.size() > 0) {
				str.append("\n세팅한 전문IO명이 이미 존재합니다. 전문ID [" + layoutId + "], 전문IO명 [" + ioNm + "], 사용중인 전문ID ["
						+ msgDupList + "]");
				isValid = false;
			}
			if (msgDupListExt != null && msgDupListExt.size() > 0) {
				str.append("\n세팅한 전문IO명이 이미 존재합니다. 전문ID [" + layoutId + "], 전문IO명 [" + ioNm + "], 사용중인 전문ID ["
						+ msgDupListExt + "]");
				isValid = false;
			}
			if (childDupList != null && childDupList.size() > 0) {
				str.append("\n세팅한 전문IO명이 하위IO명에 이미 존재합니다. 전문ID [" + layoutId + "], 전문IO명 [" + ioNm + "], 사용중인 전문ID ["
						+ childDupList + "]");
				isValid = false;
			}
		}

		/*
		if (isValid) {
			for (MsglayoutdtDto dto : msgDtList) {
				if (dto.getDataTypeNm().equals("LAYOUT")) {
					String childIoNm = dto.getChildDtoNm();

					List<String> msgDupList = msglayoutbsDao.selectSameIONmInternal(childIoNm, layoutId);
					List<String> msgDupListExt = msglayoutbsDao.selectSameIONmExternal(childIoNm, layoutId);
					List<String> childDupList = msglayoutdtDao.selectSameIONmChildNm(childIoNm, layoutId);
					
					if (msgDupList != null && msgDupList.size() > 0) {
						str.append("\n세팅한 하위IO명이 이미 존재합니다. 전문ID [" + layoutId + "], 하위IO명 [" + childIoNm
								+ "], 사용중인 전문ID [" + msgDupList + "]");
						isValid = false;
					}
					if (msgDupListExt != null && msgDupListExt.size() > 0) {
						str.append("\n세팅한 하위IO명이 이미 존재합니다. 전문ID [" + layoutId + "], 하위IO명 [" + childIoNm
								+ "], 사용중인 전문ID [" + msgDupListExt + "]");
						isValid = false;
					}
					if (childDupList != null && childDupList.size() > 0) {
						str.append("\n세팅한 하위IO명이 다른 전문의 하위IO명에 이미 존재합니다. 전문ID [" + layoutId + "], 하위IO명 [" + childIoNm
								+ "], 사용중인 전문ID [" + childDupList + "]");
						isValid = false;
					}
					
				}
			}
		}
		*/
		return isValid;

	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String addTemp(MsglayoutbsDto in, String flag) {

		int cnt = 0;
		List<MsglayoutbsDto> msgDto = null;
		// 채널구분에 따른 중복체크
		if (in.getChlDscd().equals(BxCode.ChlDscd.EXTERNAL.toString()) && in.getTrxDscd().equals("ONLINE")) {
			if (in.getMsgDscd().equals("IV") || in.getMsgDscd().equals("ISOB")) {
				msgDto = msglayoutbsDao.selectAllNotLike(null, in.getMsgVersion(), in.getMsgDscd(), null, null,
						in.getMsgDataVal(), null, in.getExtrnlBizNm(), null, null, null, null, null, in.getLv1Cd(),
						in.getLv2Cd(), in.getLv3Cd(), in.getLv4Cd(), in.getLv5Cd(), in.getTrxDscd(), null, null,
						in.getChlDscd(), null, null, null, null, null, null, null, null, null, null, 0, 9999, 1);
			}
		}

		if (msgDto != null && msgDto.size() != 0) {
			throw new ServiceException(BxMessages.Error.DUPLICATE_KEY_EXTERNAL, msgDto.get(0).getMsgLayoutId());
		}

		ActionhisthsDto actionhisthsDtoMsg = new ActionhisthsDto();
		String layoutId = in.getMsgLayoutId();
		in.setRegManId(ServiceContext.getUserInfo().getUserId());
		in.setRegDttm(DateUtils.getCurrentDate(9));
		if (in.getMsgLayoutId() == null || in.getMsgLayoutId().equals("") || flag.equals("SCREEN")) {

			MsgIdCreateDto msgId = new MsgIdCreateDto();

			msgId.setChlDscd(in.getChlDscd());
			msgId.setExtrnlBizNm(in.getExtrnlBizNm());
			msgId.setFileId(in.getJobId());
			msgId.setLv3Cd(in.getLv3Cd());
			msgId.setMsgDscd(in.getMsgDscd());
			msgId.setMsgNumber(in.getMsgDataVal());
			msgId.setMsgVersion(in.getMsgVersion());
			msgId.setReceiveSysCd(in.getRsrvFldVal2());
			msgId.setSendSysCd(in.getRsrvFldVal1());
			msgId.setTrxDscd(in.getTrxDscd());

			MsgIdCreateDto msgIdResult = this.getMsgLayoutId(msgId);
			//layoutId = msgIdResult.getMsgLayoutId();
			//in.setMsgLayoutId(layoutId);
			if (msgIdResult.getSeq() == null) {
				in.setCrtnSeq(null);
			} else {
				in.setCrtnSeq(Integer.parseInt(msgIdResult.getSeq()));
			}
		}

		in.setWorkStatusCd("WORKING");

		int i = msglayoutbsDao.insertMsglayoutbs(in);

		if (in.getMsglayoutdtDto().size() == 0) {
			// throw new ServiceException(BxMessages.Error.DUPLICATE_KEY,
			// in.getMsgLayoutId());
			logger.debug("dt 데이터 미존재");
		} else {
			for (MsglayoutdtDto dto : in.getMsglayoutdtDto()) {
				String preId = dto.getMsgLayoutId();
				dto.setMsgLayoutId(in.getMsgLayoutId());

				if (dto.getFldUnqId() != null && !dto.getFldUnqId().equals("")) {
					dto.setFldUnqId(StringUtils.replace(dto.getFldUnqId(), preId, dto.getMsgLayoutId()));
				}
				if (dto.getParentFldNm() != null && !dto.getParentFldNm().equals("")) {
					dto.setParentFldNm(StringUtils.replace(dto.getParentFldNm(), preId, dto.getMsgLayoutId()));
				}

				msglayoutdtDao.insertMsglayoutdt(dto);
			}
		}

		actionhisthsDtoMsg.setHstDscd("MESSAGE");
		actionhisthsDtoMsg.setItemDesc(in.getMsgDesc());
		actionhisthsDtoMsg.setItemId(in.getMsgLayoutId());
		actionhisthsDtoMsg.setUserId(ServiceContext.getUserInfo().getUserId());
		actionhisthsDtoMsg.setWorkCttCd("CREATE");
		actionhisthsDtoMsg.setWorkDttm(DateUtils.getCurrentDate(10));

		actionhistService.add(actionhisthsDtoMsg);

		return in.getMsgLayoutId();
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public int updateTemp(MsglayoutbsDto in) {
		int out;
		ActionhisthsDto actionhisthsDtoMsg = new ActionhisthsDto();

		MsglayoutbsDto dtoMsg = msglayoutbsDao.selectMsglayoutbs(in.getMsgLayoutId());
		if (dtoMsg == null) {
			throw new ServiceException(BxMessages.Error.NOT_FOUNDED, in.getMsgLayoutId());
		}

		in.setRegManId(ServiceContext.getUserInfo().getUserId());
		in.setRegDttm(DateUtils.getCurrentDate(9));

		in.setCrtnSeq(dtoMsg.getCrtnSeq());

		in.setWorkStatusCd("WORKING");

		out = msglayoutbsDao.updateMsglayoutbs(in);

		int result;
		result = msglayoutdtDao.deleteMsglayoutdt(in.getMsgLayoutId());

		if (in.getMsglayoutdtDto().size() == 0) {
			// throw new ServiceException(BxMessages.Error.DUPLICATE_KEY,
			// in.getMsgLayoutId());
			logger.debug("DT is Null");
		} else {
			for (MsglayoutdtDto dto : in.getMsglayoutdtDto()) {
				msglayoutdtDao.insertMsglayoutdt(dto);
			}
		}

		if (out == 0) {
			throw new ServiceException(BxMessages.Error.NOT_FOUNDED, in.getMsgLayoutId());
		}

		actionhisthsDtoMsg.setHstDscd("MESSAGE");
		actionhisthsDtoMsg.setItemDesc(in.getMsgDesc());
		actionhisthsDtoMsg.setItemId(in.getMsgLayoutId());
		actionhisthsDtoMsg.setUserId(ServiceContext.getUserInfo().getUserId());
		actionhisthsDtoMsg.setWorkCttCd("UPDATE");
		actionhisthsDtoMsg.setWorkDttm(DateUtils.getCurrentDate(10));

		actionhistService.add(actionhisthsDtoMsg);

		return out;
	}
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String addFileUpload(MsglayoutbsDto in, String flag) {
		// MsglayoutbsDto curMsglayoutbsInfo =
		// msglayoutbsDao.selectMsglayoutbs(in.getMsgLayoutId());
		//
		// if (curMsglayoutbsInfo != null) {
		// throw new ServiceException(BxMessages.Error.DUPLICATE_KEY,
		// in.getMsgLayoutId());
		// }

		int cnt = 0;
		List<MsglayoutbsDto> msgDto = null;
		// 채널구분에 따른 중복체크
		if (in.getChlDscd().equals(BxCode.ChlDscd.EXTERNAL.toString()) && in.getTrxDscd().equals("ONLINE")) {
			if (in.getMsgDscd().equals("IV") || in.getMsgDscd().equals("ISOB")) {
				msgDto = msglayoutbsDao.selectAllNotLike(null, in.getMsgVersion(), in.getMsgDscd(), null, null,
						in.getMsgDataVal(), null, in.getExtrnlBizNm(), null, null, null, null, null, in.getLv1Cd(),
						in.getLv2Cd(), in.getLv3Cd(), in.getLv4Cd(), in.getLv5Cd(), in.getTrxDscd(), null, null,
						in.getChlDscd(), null, null, null, null, null, null, null, null, null, null, 0, 9999, 1);
			}
		}

		// if (flag.equals("SCREEN")) {
		if (msgDto != null && msgDto.size() != 0) {
			throw new ServiceException(BxMessages.Error.DUPLICATE_KEY_EXTERNAL, msgDto.get(0).getMsgLayoutId());
		}
		// } else {
		// if (msgDto != null && msgDto.size() != 0) {
		// String preMsgId = in.getMsgLayoutId();
		// in.setMsgLayoutId(msgDto.get(0).getMsgLayoutId());
		// for (MsglayoutdtDto dto : in.getMsglayoutdtDto()) {
		// dto.setMsgLayoutId(msgDto.get(0).getMsgLayoutId());
		// if (dto.getParentFldNm() != null && !dto.getParentFldNm().equals("")) {
		// dto.setParentFldNm(dto.getParentFldNm().replace(preMsgId,
		// in.getMsgLayoutId()));
		// }
		//
		// if (dto.getFldUnqId() != null && !dto.getFldUnqId().equals("")) {
		// dto.setFldUnqId(dto.getFldUnqId().replace(preMsgId, in.getMsgLayoutId()));
		// }
		// }
		// int i = this.update(in);
		// return i;
		// }
		// }

		ActionhisthsDto actionhisthsDtoMsg = new ActionhisthsDto();
		// String layoutId = in.getMsgLayoutId();
		//live시
//		in.setRegManId(ServiceContext.getUserInfo().getUserId());
		in.setRegDttm(DateUtils.getCurrentDate(9));
		if (in.getMsgLayoutId() == null || in.getMsgLayoutId().equals("") || flag.equals("SCREEN")) {

			MsgIdCreateDto msgId = new MsgIdCreateDto();

			msgId.setChlDscd(in.getChlDscd());
			msgId.setExtrnlBizNm(in.getExtrnlBizNm());
			msgId.setFileId(in.getJobId());
			msgId.setLv3Cd(in.getLv3Cd());
			msgId.setMsgDscd(in.getMsgDscd());
			msgId.setMsgNumber(in.getMsgDataVal());
			msgId.setMsgVersion(in.getMsgVersion());
			msgId.setReceiveSysCd(in.getRsrvFldVal2());
			msgId.setSendSysCd(in.getRsrvFldVal1());
			msgId.setTrxDscd(in.getTrxDscd());

			MsgIdCreateDto msgIdResult = this.getMsgLayoutId(msgId);
			msgIdResult.setMsgLayoutId(in.getMsgLayoutId());
			String layoutId = msgIdResult.getMsgLayoutId();
			in.setMsgLayoutId(layoutId);
			if (msgIdResult.getSeq() == null) {
				in.setCrtnSeq(null);
			} else {
				in.setCrtnSeq(Integer.parseInt(msgIdResult.getSeq()));
			}
		}

		boolean isValidMsg = true;
		StringBuilder str = new StringBuilder();
		isValidMsg = msgDefaultValid(in, isValidMsg, str);
		if (!isValidMsg) {
			throw new ServiceException(BxMessages.Error.MSG_DEFAULT_VALUE_SET_ERR, str.toString());
		}
/*
		boolean isIoNmValid = validationIoNm(in, str);
		if (!isIoNmValid) {
			throw new ServiceException(BxMessages.Error.IO_NM_DUP_ERR, str.toString());
		}
*/
		in.setWorkStatusCd("WORKING");
		in.setMsgRvsNo(1);

		int i = msglayoutbsDao.insertMsglayoutbs(in);
/*
		if (in.getMsglayoutdtDto().size() == 0) {
			throw new ServiceException(BxMessages.Error.DUPLICATE_KEY, in.getMsgLayoutId());
		}
*/
		for (MsglayoutdtDto dto : in.getMsglayoutdtDto()) {
			String preId = dto.getMsgLayoutId();
			dto.setMsgLayoutId(in.getMsgLayoutId());

			if (dto.getFldUnqId() != null && !dto.getFldUnqId().equals("")) {
				dto.setFldUnqId(StringUtils.replace(dto.getFldUnqId(), preId, dto.getMsgLayoutId()));
			}
			if (dto.getParentFldNm() != null && !dto.getParentFldNm().equals("")) {
				dto.setParentFldNm(StringUtils.replace(dto.getParentFldNm(), preId, dto.getMsgLayoutId()));
			}

			msglayoutdtDao.insertMsglayoutdt(dto);
		}

		actionhisthsDtoMsg.setHstDscd("MESSAGE");
		actionhisthsDtoMsg.setItemDesc(in.getMsgDesc());
		actionhisthsDtoMsg.setItemId(in.getMsgLayoutId());
		actionhisthsDtoMsg.setUserId(ServiceContext.getUserInfo().getUserId());
		actionhisthsDtoMsg.setWorkCttCd("CREATE");
		actionhisthsDtoMsg.setWorkDttm(DateUtils.getCurrentDate(10));

		actionhistService.add(actionhisthsDtoMsg);

		return in.getMsgLayoutId();
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public int updateFileUpload(MsglayoutbsDto in) {
		int out;
		ActionhisthsDto actionhisthsDtoMsg = new ActionhisthsDto();

		MsglayoutbsDto dtoMsg = msglayoutbsDao.selectMsglayoutbs(in.getMsgLayoutId());
		if (dtoMsg == null) {
			throw new ServiceException(BxMessages.Error.NOT_FOUNDED, in.getMsgLayoutId());
		}

		in.setRegManId(ServiceContext.getUserInfo().getUserId());
		in.setRegDttm(DateUtils.getCurrentDate(9));

		in.setCrtnSeq(dtoMsg.getCrtnSeq());

		boolean isValidMsg = true;
		StringBuilder str = new StringBuilder();
		isValidMsg = msgDefaultValid(in, isValidMsg, str);
		if (!isValidMsg) {
			throw new ServiceException(BxMessages.Error.MSG_DEFAULT_VALUE_SET_ERR, str.toString());
		}

		boolean isIoNmValid = validationIoNm(in, str);
		if (!isIoNmValid) {
			throw new ServiceException(BxMessages.Error.IO_NM_DUP_ERR, str.toString());
		}

		in.setWorkStatusCd("WORKING");
		Integer msgRvsNo = dtoMsg.getMsgRvsNo();
		if (msgRvsNo == null) {
			msgRvsNo = 1;
		} else {
			msgRvsNo = msgRvsNo + 1;
		}
		logger.debug("msgRvsNo : " + msgRvsNo);
		in.setMsgRvsNo(msgRvsNo);

		out = msglayoutbsDao.updateMsglayoutbs(in);

		int result;
		result = msglayoutdtDao.deleteMsglayoutdt(in.getMsgLayoutId());

		if (in.getMsglayoutdtDto().size() == 0) {
			throw new ServiceException(BxMessages.Error.DUPLICATE_KEY, in.getMsgLayoutId());
		}
		for (MsglayoutdtDto dto : in.getMsglayoutdtDto()) {

			// if (flag.equals("FILE")) {
			// if (dto.getFldUnqId() == null || "".equals(dto.getFldUnqId())) {
			// if (dto.getParentFldNm() == null || "".equals(dto.getParentFldNm())) {
			// dto.setFldUnqId(dto.getMsgLayoutId() + "." + dto.getFldEngNm());
			// } else {
			// dto.setFldUnqId(dto.getMsgLayoutId() + dto.getParentFldNm() + "." +
			// dto.getFldEngNm());
			// dto.setParentFldNm(dto.getMsgLayoutId() + dto.getParentFldNm());
			// }
			// }
			// }

			msglayoutdtDao.insertMsglayoutdt(dto);
		}

		if (out == 0) {
			throw new ServiceException(BxMessages.Error.NOT_FOUNDED, in.getMsgLayoutId());
		}

		actionhisthsDtoMsg.setHstDscd("MESSAGE");
		actionhisthsDtoMsg.setItemDesc(in.getMsgDesc());
		actionhisthsDtoMsg.setItemId(in.getMsgLayoutId());
		actionhisthsDtoMsg.setUserId(ServiceContext.getUserInfo().getUserId());
		actionhisthsDtoMsg.setWorkCttCd("UPDATE");
		actionhisthsDtoMsg.setWorkDttm(DateUtils.getCurrentDate(10));

		actionhistService.add(actionhisthsDtoMsg);

		return out;
	}
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String add(MsglayoutbsDto in, String flag) {
		// MsglayoutbsDto curMsglayoutbsInfo =
		// msglayoutbsDao.selectMsglayoutbs(in.getMsgLayoutId());
		//
		// if (curMsglayoutbsInfo != null) {
		// throw new ServiceException(BxMessages.Error.DUPLICATE_KEY,
		// in.getMsgLayoutId());
		// }

		int cnt = 0;
		List<MsglayoutbsDto> msgDto = null;
		// 채널구분에 따른 중복체크
		if (in.getChlDscd().equals(BxCode.ChlDscd.EXTERNAL.toString()) && in.getTrxDscd().equals("ONLINE")) {
			if (in.getMsgDscd().equals("IV") || in.getMsgDscd().equals("ISOB")) {
				msgDto = msglayoutbsDao.selectAllNotLike(null, in.getMsgVersion(), in.getMsgDscd(), null, null,
						in.getMsgDataVal(), null, in.getExtrnlBizNm(), null, null, null, null, null, in.getLv1Cd(),
						in.getLv2Cd(), in.getLv3Cd(), in.getLv4Cd(), in.getLv5Cd(), in.getTrxDscd(), null, null,
						in.getChlDscd(), null, null, null, null, null, null, null, null, null, null, 0, 9999, 1);
			}
		}

		// if (flag.equals("SCREEN")) {
		//if (msgDto != null && msgDto.size() != 0) {
		//	throw new ServiceException(BxMessages.Error.DUPLICATE_KEY_EXTERNAL, msgDto.get(0).getMsgLayoutId());
		//}
		// } else {
		// if (msgDto != null && msgDto.size() != 0) {
		// String preMsgId = in.getMsgLayoutId();
		// in.setMsgLayoutId(msgDto.get(0).getMsgLayoutId());
		// for (MsglayoutdtDto dto : in.getMsglayoutdtDto()) {
		// dto.setMsgLayoutId(msgDto.get(0).getMsgLayoutId());
		// if (dto.getParentFldNm() != null && !dto.getParentFldNm().equals("")) {
		// dto.setParentFldNm(dto.getParentFldNm().replace(preMsgId,
		// in.getMsgLayoutId()));
		// }
		//
		// if (dto.getFldUnqId() != null && !dto.getFldUnqId().equals("")) {
		// dto.setFldUnqId(dto.getFldUnqId().replace(preMsgId, in.getMsgLayoutId()));
		// }
		// }
		// int i = this.update(in);
		// return i;
		// }
		// }

		ActionhisthsDto actionhisthsDtoMsg = new ActionhisthsDto();
		// String layoutId = in.getMsgLayoutId();
		//live시
//		in.setRegManId(ServiceContext.getUserInfo().getUserId());
		in.setRegDttm(DateUtils.getCurrentDate(9));
		if (in.getMsgLayoutId() == null || in.getMsgLayoutId().equals("") || flag.equals("SCREEN")) {

			MsgIdCreateDto msgId = new MsgIdCreateDto();

			msgId.setChlDscd(in.getChlDscd());
			msgId.setExtrnlBizNm(in.getExtrnlBizNm());
			msgId.setFileId(in.getJobId());
			msgId.setLv3Cd(in.getLv3Cd());
			msgId.setMsgDscd(in.getMsgDscd());
			msgId.setMsgNumber(in.getMsgDataVal());
			msgId.setMsgVersion(in.getMsgVersion());
			msgId.setReceiveSysCd(in.getRsrvFldVal2());
			msgId.setSendSysCd(in.getRsrvFldVal1());
			msgId.setTrxDscd(in.getTrxDscd());

			MsgIdCreateDto msgIdResult = this.getMsgLayoutId(msgId);
			msgIdResult.setMsgLayoutId(in.getMsgLayoutId());
			String layoutId = msgIdResult.getMsgLayoutId();
			in.setMsgLayoutId(layoutId);
			if (msgIdResult.getSeq() == null) {
				in.setCrtnSeq(null);
			} else {
				in.setCrtnSeq(Integer.parseInt(msgIdResult.getSeq()));
			}
		}

		boolean isValidMsg = true;
		StringBuilder str = new StringBuilder();
		isValidMsg = msgDefaultValid(in, isValidMsg, str);
		if (!isValidMsg) {
			throw new ServiceException(BxMessages.Error.MSG_DEFAULT_VALUE_SET_ERR, str.toString());
		}

		boolean isIoNmValid = validationIoNm(in, str);
		if (!isIoNmValid) {
			throw new ServiceException(BxMessages.Error.IO_NM_DUP_ERR, str.toString());
		}

		in.setWorkStatusCd("WORK_COMP");
		in.setMsgRvsNo(1);

		int i = msglayoutbsDao.insertMsglayoutbs(in);

		if (in.getMsglayoutdtDto().size() == 0) {
			throw new ServiceException(BxMessages.Error.DUPLICATE_KEY, in.getMsgLayoutId());
		}

		for (MsglayoutdtDto dto : in.getMsglayoutdtDto()) {
			String preId = dto.getMsgLayoutId();
			dto.setMsgLayoutId(in.getMsgLayoutId());

			if (dto.getFldUnqId() != null && !dto.getFldUnqId().equals("")) {
				dto.setFldUnqId(StringUtils.replace(dto.getFldUnqId(), preId, dto.getMsgLayoutId()));
			}
			if (dto.getParentFldNm() != null && !dto.getParentFldNm().equals("")) {
				dto.setParentFldNm(StringUtils.replace(dto.getParentFldNm(), preId, dto.getMsgLayoutId()));
			}

			msglayoutdtDao.insertMsglayoutdt(dto);
		}

		actionhisthsDtoMsg.setHstDscd("MESSAGE");
		actionhisthsDtoMsg.setItemDesc(in.getMsgDesc());
		actionhisthsDtoMsg.setItemId(in.getMsgLayoutId());
		actionhisthsDtoMsg.setUserId(ServiceContext.getUserInfo().getUserId());
		actionhisthsDtoMsg.setWorkCttCd("CREATE");
		actionhisthsDtoMsg.setWorkDttm(DateUtils.getCurrentDate(10));

		actionhistService.add(actionhisthsDtoMsg);

		return in.getMsgLayoutId();
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public int update(MsglayoutbsDto in) {
		int out;
		ActionhisthsDto actionhisthsDtoMsg = new ActionhisthsDto();

		MsglayoutbsDto dtoMsg = msglayoutbsDao.selectMsglayoutbs(in.getMsgLayoutId());
		if (dtoMsg == null) {
			throw new ServiceException(BxMessages.Error.NOT_FOUNDED, in.getMsgLayoutId());
		}

		in.setRegManId(ServiceContext.getUserInfo().getUserId());
		in.setRegDttm(DateUtils.getCurrentDate(9));

		in.setCrtnSeq(dtoMsg.getCrtnSeq());

		boolean isValidMsg = true;
		StringBuilder str = new StringBuilder();
		isValidMsg = msgDefaultValid(in, isValidMsg, str);
		if (!isValidMsg) {
			throw new ServiceException(BxMessages.Error.MSG_DEFAULT_VALUE_SET_ERR, str.toString());
		}

		boolean isIoNmValid = validationIoNm(in, str);
		if (!isIoNmValid) {
			throw new ServiceException(BxMessages.Error.IO_NM_DUP_ERR, str.toString());
		}

		in.setWorkStatusCd("WORK_COMP");
		Integer msgRvsNo = dtoMsg.getMsgRvsNo();
		if (msgRvsNo == null) {
			msgRvsNo = 1;
		} else {
			msgRvsNo = msgRvsNo + 1;
		}
		logger.debug("msgRvsNo : " + msgRvsNo);
		in.setMsgRvsNo(msgRvsNo);

		out = msglayoutbsDao.updateMsglayoutbs(in);

		int result;
		result = msglayoutdtDao.deleteMsglayoutdt(in.getMsgLayoutId());

		if (in.getMsglayoutdtDto().size() == 0) {
			throw new ServiceException(BxMessages.Error.DUPLICATE_KEY, in.getMsgLayoutId());
		}
		for (MsglayoutdtDto dto : in.getMsglayoutdtDto()) {

			// if (flag.equals("FILE")) {
			// if (dto.getFldUnqId() == null || "".equals(dto.getFldUnqId())) {
			// if (dto.getParentFldNm() == null || "".equals(dto.getParentFldNm())) {
			// dto.setFldUnqId(dto.getMsgLayoutId() + "." + dto.getFldEngNm());
			// } else {
			// dto.setFldUnqId(dto.getMsgLayoutId() + dto.getParentFldNm() + "." +
			// dto.getFldEngNm());
			// dto.setParentFldNm(dto.getMsgLayoutId() + dto.getParentFldNm());
			// }
			// }
			// }

			msglayoutdtDao.insertMsglayoutdt(dto);
		}

		if (out == 0) {
			throw new ServiceException(BxMessages.Error.NOT_FOUNDED, in.getMsgLayoutId());
		}

		actionhisthsDtoMsg.setHstDscd("MESSAGE");
		actionhisthsDtoMsg.setItemDesc(in.getMsgDesc());
		actionhisthsDtoMsg.setItemId(in.getMsgLayoutId());
		actionhisthsDtoMsg.setUserId(ServiceContext.getUserInfo().getUserId());
		actionhisthsDtoMsg.setWorkCttCd("UPDATE");
		actionhisthsDtoMsg.setWorkDttm(DateUtils.getCurrentDate(10));

		actionhistService.add(actionhisthsDtoMsg);

		return out;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public int delete(String msgLayoutId) {
		int result;
		ActionhisthsDto actionhisthsDtoMsg = new ActionhisthsDto();

		List<MsgLayoutEffectDto> resultList = msglayoutbsDao.selectMsglayoutEffect(msgLayoutId, null, null);

		if (resultList.size() > 0) {
			throw new ServiceException(BxMessages.Error.ALREADY_USE_MSG, msgLayoutId);
		}

		MsglayoutbsDto out = msglayoutbsDao.selectMsglayoutbs(msgLayoutId);
		result = msglayoutbsDao.deleteMsglayoutbs(msgLayoutId);
		msglayoutdtDao.deleteMsglayoutdt(msgLayoutId);
		if (result == 0) {
			throw new ServiceException(BxMessages.Error.NOT_FOUNDED, msgLayoutId);
		}

		actionhisthsDtoMsg.setHstDscd("MESSAGE");
		actionhisthsDtoMsg.setItemDesc(out.getMsgDesc());
		actionhisthsDtoMsg.setItemId(msgLayoutId);
		actionhisthsDtoMsg.setUserId(ServiceContext.getUserInfo().getUserId());
		actionhisthsDtoMsg.setWorkCttCd("DELETE");
		actionhisthsDtoMsg.setWorkDttm(DateUtils.getCurrentDate(10));

		actionhistService.add(actionhisthsDtoMsg);

		return result;

	}

	public List<MsgLayoutEffectDto> getEffects(String msgLayoutId, String intrfcId, String intrfcNm) {

		List<MsgLayoutEffectDto> resultList = msglayoutbsDao.selectMsglayoutEffect(msgLayoutId, intrfcId, intrfcNm);

		return resultList;
	}

	public UiMsglayoutbsOut getListExtrnlMsg(String regManId, String msgDataVal, String extrnlBizNm, String msgLayoutId,
			String trxDscd, int pageSize, int pageNumber) {

		UiMsglayoutbsOut result = new UiMsglayoutbsOut();

		List<MsglayoutbsDto> resultList = msglayoutbsDao.selectExtrnlMsgList(regManId, msgDataVal, extrnlBizNm,
				msgLayoutId, trxDscd, pageSize, pageNumber);
		int totalCnt = msglayoutbsDao.selectExtrnlMsgListCnt(regManId, msgDataVal, extrnlBizNm, msgLayoutId, trxDscd);

		result.setTotalCnt(totalCnt);
		result.setMsglayoutbsOutList(resultList);

		return result;
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public MsglayoutbsFileUploadDto fileUpload(MultipartFile messageFile) {
		logger.debug("fileName: {}", messageFile.getOriginalFilename());
		logger.debug("fileSize: {}", messageFile.getSize());      
		
		String uid = UidUtils.genUUID();
		//String inFile = "/tmp/" + uid + ".xlsx";
		String inFile = "C:\\linebank\\1.product\\01.EIMS\\01.data\\workspace\\eims.server\\src\\main\\resources\\templates\\temp\\"+uid+".xlsx";

		
		StringBuilder errMsg = new StringBuilder();
		boolean isValid = true;

		MsglayoutbsDto data = new MsglayoutbsDto();
		MsglayoutbsFileUploadDto fileResult = null;

		Workbook workbook = null;
		try (FileOutputStream out = new FileOutputStream(inFile)) {
			out.write(messageFile.getBytes());
			out.close();

			File decryptedInputFile = Optional.ofNullable(new File(inFile)).filter(File::exists).filter(File::canRead)
					.orElseThrow(() -> new IOException(inFile));
			workbook = WorkbookFactory.create(decryptedInputFile);
			Sheet sheet = workbook.getSheet("Layout(D)");
			
			String chlDscd = getCodeValueNm("CHL_DSCD", ExcelUtils.readValue(sheet, 5, "J"), "ko");
			String trxDscd =  getCodeValueNm("TRAN_DSCD", ExcelUtils.readValue(sheet, 5, "M"), "ko");
			String msgType = getCodeValueNm("MSG_TYPE", ExcelUtils.readValue(sheet, 6, "J"), "ko");
			


			if (chlDscd.equals("INTERNAL")) {
				if (trxDscd.equals("ONLINE")) {
					if (!msgType.equals("IV")) {	
						errMsg.append("\n대내 온라인 타입에 올 수 없는 전문 타입입니다.");
						isValid = false;

					}
					String sendSyscd = ExcelUtils.readValue(sheet, 7, "D");
					String recvSysCd = ExcelUtils.readValue(sheet, 7, "G");
					isValid = nullCheck(errMsg, sendSyscd, "\n송신시스템코드가", isValid);
					isValid = nullCheck(errMsg, recvSysCd, "\n수신시스템코드가", isValid);

					int send = srsysbsDao.selectAllCnt(sendSyscd, null, null, null, null);
					int recv = srsysbsDao.selectAllCnt(recvSysCd, null, null, null, null);

					if (send == 0) {
						errMsg.append("\n송신시스템코드가 존재하지 않습니다.");
						isValid = false;
					}
					if (recv == 0) {
						errMsg.append("\n수신시스템코드가 존재하지 않습니다.");
						isValid = false;
					}

				} else {

				}
			} else {
				if (trxDscd.equals("ONLINE")) {
					if (!msgType.equals("IV") && !msgType.equals("CH") && !msgType.equals("ISOH")
							&& !msgType.equals("ISOB")) {
						errMsg.append("\n대외 온라인 타입에 올 수 없는 전문 타입입니다.");
						isValid = false;
					}



				} else {
					if (!msgType.equals("BATH") && !msgType.equals("BATB") && !msgType.equals("BATT")) {
						errMsg.append("\n대내 배치 타입에 올 수 없는 전문 타입입니다.");
						isValid = false;
					}
			
				}
			}

			int startIndex = 12;

			MsgIdCreateDto msgId = new MsgIdCreateDto();

			msgId.setChlDscd(getCodeValueNm("CHL_DSCD", ExcelUtils.readValue(sheet, 5, "J"), "ko"));
			
			msgId.setMsgVersion(1);
			msgId.setFileId(ExcelUtils.readValue(sheet, 6, "M"));
			msgId.setLv1Cd(ExcelUtils.readValue(sheet, 6, "D"));
			msgId.setMsgDscd(getCodeValueNm("MSG_TYPE", ExcelUtils.readValue(sheet, 6, "J"), "ko"));
			msgId.setMsgNumber(ExcelUtils.readValue(sheet, 6, "J"));
			msgId.setTrxDscd(getCodeValueNm("TRAN_DSCD", ExcelUtils.readValue(sheet, 5, "M"), "ko"));
			msgId.setReceiveSysCd(ExcelUtils.readValue(sheet, 7, "G"));
			msgId.setSendSysCd(ExcelUtils.readValue(sheet, 7, "D"));
			

			MsgIdCreateDto msgIdResult = null;
			
			//String layoutId = msgIdResult.getMsgLayoutId();
			
			String layoutId  =ExcelUtils.readValue(sheet, 5, "D");

			data.setCrtnSeq(null);
			/*
			if (msgIdResult.getSeq() == null) {
				data.setCrtnSeq(null);
			} else {
				data.setCrtnSeq(Integer.parseInt(msgIdResult.getSeq()));
			}
			*/

			boolean insertYn = true;

			readDataIndividual(sheet, data, layoutId);

			readDataGrid(sheet, data, startIndex, layoutId, isValid, errMsg);

			/*
			if (!isValid) {
				throw new ServiceException(BxMessages.Error.MSG_VALID_CHECK, errMsg.toString());
			}
			 */
			workbook.close();

			boolean isAdd = true;
			
			int checkYn = msglayoutbsDao.checkLayoutId(layoutId);
			
			if (checkYn == 0) {
				isAdd = true;
				 this.add(data, "FILE");
			} else {
				isAdd = false;
				this.update(data);
			}
			fileResult = new MsglayoutbsFileUploadDto();
			fileResult.setAdd(isAdd);
			fileResult.setMsglayoutbsDto(data);

			} catch (FileNotFoundException e) {
				logger.error("{}", e);
			} catch (IOException e) {
				logger.error("{}", e);
			} catch (EncryptedDocumentException e) {
				logger.error("{}", e);
			} catch (InvalidFormatException e) {
				logger.error("{}", e);
			} finally {
				new File(inFile).delete();
				if (workbook != null) {
					try {
						workbook.close();
					} catch (IOException e) {
						logger.error("{}", e);
					}
				}
			}

		if (fileResult == null) {
			throw new ServiceException(BxMessages.Error.FILE_UPLOAD_EXCEPTION);
		}
		
		return fileResult;
	}	
	

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void AllFileUpload(MultipartFile messageFile) {
		logger.debug("fileName: {}", messageFile.getOriginalFilename());
		logger.debug("fileSize: {}", messageFile.getSize());      
		
		File folder = new File("C:\\Users\\user\\Desktop\\temp\\layout");
		
		
		logger.debug("###########start!!######"); 
		for (File fileEntry : folder.listFiles()){
			if(fileEntry.isDirectory()) {
				
			} else {
				logger.debug("filePath2: {}",fileEntry.getName()); 
				String uid = UidUtils.genUUID();

				//String inFile = "/tmp/" + uid + ".xlsx";
				//String inFile = "C:\\linebank\\1.product\\01.EIMS\\01.data\\workspace\\eims.server\\src\\main\\resources\\templates\\temp\\"+uid+".xlsx";
				
				String inFile = fileEntry.getName();
			
				byte[] fileContent = null;
				
				try {
					fileContent = Files.readAllBytes(fileEntry.toPath());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				StringBuilder errMsg = new StringBuilder();
				boolean isValid = true;

				MsglayoutbsDto data = new MsglayoutbsDto();
				MsglayoutbsFileUploadDto fileResult = null;

				Workbook workbook = null;
				try (FileOutputStream out = new FileOutputStream(inFile)) {
					out.write(fileContent);
					out.close();

					File decryptedInputFile = Optional.ofNullable(new File(inFile)).filter(File::exists).filter(File::canRead)
							.orElseThrow(() -> new IOException(inFile));
					workbook = WorkbookFactory.create(decryptedInputFile);
					Sheet sheet = workbook.getSheet("interface Design(D)");
					
					String chlDscd = getCodeValueNm("CHL_DSCD", convertMsgEngToKor(ExcelUtils.readValue(sheet, 5, "I")), "ko");
					String trxDscd =  getCodeValueNm("TRAN_DSCD", convertMsgEngToKor(ExcelUtils.readValue(sheet, 5, "L")), "ko");
					String msgType = getCodeValueNm("MSG_TYPE", convertMsgEngToKor(ExcelUtils.readValue(sheet, 6, "I")), "ko");
					


					if (chlDscd.equals("INTERNAL")) {
						if (trxDscd.equals("ONLINE")) {
							if (!msgType.equals("IV")) {	
								errMsg.append("\n대내 온라인 타입에 올 수 없는 전문 타입입니다.");
								isValid = false;

							}
							String sendSyscd = ExcelUtils.readValue(sheet, 7, "C");
							String recvSysCd = ExcelUtils.readValue(sheet, 7, "F");
							isValid = nullCheck(errMsg, sendSyscd, "\n송신시스템코드가", isValid);
							isValid = nullCheck(errMsg, recvSysCd, "\n수신시스템코드가", isValid);

							int send = srsysbsDao.selectAllCnt(sendSyscd, null, null, null, null);
							int recv = srsysbsDao.selectAllCnt(recvSysCd, null, null, null, null);

							if (send == 0) {
								errMsg.append("\n송신시스템코드가 존재하지 않습니다.");
								isValid = false;
							}
							if (recv == 0) {
								errMsg.append("\n수신시스템코드가 존재하지 않습니다.");
								isValid = false;
							}

						} else {

						}
					} else {
						if (trxDscd.equals("ONLINE")) {
							if (!msgType.equals("IV") && !msgType.equals("CH") && !msgType.equals("ISOH")
									&& !msgType.equals("ISOB")) {
								errMsg.append("\n대외 온라인 타입에 올 수 없는 전문 타입입니다.");
								isValid = false;
							}



						} else {
							if (!msgType.equals("BATH") && !msgType.equals("BATB") && !msgType.equals("BATT")) {
								errMsg.append("\n대내 배치 타입에 올 수 없는 전문 타입입니다.");
								isValid = false;
							}
					
						}
					}

					int startIndex = 12;

					MsgIdCreateDto msgId = new MsgIdCreateDto();

					msgId.setChlDscd(getCodeValueNm("CHL_DSCD", convertMsgEngToKor(ExcelUtils.readValue(sheet, 5, "I")), "ko"));
					
					msgId.setMsgVersion(1);
					msgId.setFileId(ExcelUtils.readValue(sheet, 6, "L"));
					msgId.setLv1Cd(ExcelUtils.readValue(sheet, 6, "C"));
					msgId.setMsgDscd(getCodeValueNm("MSG_TYPE", convertMsgEngToKor(ExcelUtils.readValue(sheet, 6, "I")), "ko"));
					msgId.setMsgNumber(ExcelUtils.readValue(sheet, 6, "I"));
					msgId.setTrxDscd(getCodeValueNm("TRAN_DSCD", convertMsgEngToKor(ExcelUtils.readValue(sheet, 5, "L")), "ko"));
					msgId.setReceiveSysCd(ExcelUtils.readValue(sheet, 7, "F"));
					msgId.setSendSysCd(ExcelUtils.readValue(sheet, 7, "C"));
					

					MsgIdCreateDto msgIdResult = null;
					
					//String layoutId = msgIdResult.getMsgLayoutId();
					
					String layoutId  =ExcelUtils.readValue(sheet, 5, "C");

					data.setCrtnSeq(null);
					/*
					if (msgIdResult.getSeq() == null) {
						data.setCrtnSeq(null);
					} else {
						data.setCrtnSeq(Integer.parseInt(msgIdResult.getSeq()));
					}
					*/

					boolean insertYn = true;

					readDataIndividual(sheet, data, layoutId);

					readDataGrid(sheet, data, startIndex, layoutId, isValid, errMsg);

					/*
					if (!isValid) {
						throw new ServiceException(BxMessages.Error.MSG_VALID_CHECK, errMsg.toString());
					}
					 */
					workbook.close();

					boolean isAdd = true;
					
					int checkYn = msglayoutbsDao.checkLayoutId(layoutId);
					
					if (checkYn == 0) {
						isAdd = true;
					//	 this.add(data, "FILE");
						 this.addFileUpload(data, "FILE");
					} else {
						isAdd = false;
						this.updateFileUpload(data);
					}
					fileResult = new MsglayoutbsFileUploadDto();
					fileResult.setAdd(isAdd);
					fileResult.setMsglayoutbsDto(data);

				} catch (FileNotFoundException e) {
					logger.error("{}", e);
				} catch (IOException e) {
					logger.error("{}", e);
				} catch (EncryptedDocumentException e) {
					logger.error("{}", e);
				} catch (InvalidFormatException e) {
					logger.error("{}", e);
				} finally {
					new File(inFile).delete();
					if (workbook != null) {
						try {
							workbook.close();
						} catch (IOException e) {
							logger.error("{}", e);
						}
					}
				}
/*
				if (fileResult == null) {
					throw new ServiceException(BxMessages.Error.FILE_UPLOAD_EXCEPTION);
				}
*/
			}
		}
		logger.debug("###########end!!######");

	

		
	//	return fileResult;
	}	
	
/*
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public MsglayoutbsFileUploadDto fileUpload(MultipartFile messageFile) {
		logger.debug("fileName: {}", messageFile.getOriginalFilename());
		logger.debug("fileSize: {}", messageFile.getSize());

		String uid = UidUtils.genUUID();

		String inFile = "/tmp/" + uid + ".xlsx";
		//String inFile = "C:\\linebank\\1.product\\01.EIMS\\01.data\\workspace\\eims.server\\src\\main\\resources\\templates\\temp\\"+uid+".xlsx";
		StringBuilder errMsg = new StringBuilder();
		boolean isValid = true;

		MsglayoutbsDto data = new MsglayoutbsDto();
		MsglayoutbsFileUploadDto fileResult = null;

		Workbook workbook = null;
		try (FileOutputStream out = new FileOutputStream(inFile)) {
			out.write(messageFile.getBytes());
			out.close();

			File decryptedInputFile = Optional.ofNullable(new File(inFile)).filter(File::exists).filter(File::canRead)
					.orElseThrow(() -> new IOException(inFile));
			workbook = WorkbookFactory.create(decryptedInputFile);
			Sheet sheet = workbook.getSheet("MessageLayout");

			isValid = validationMsgInfo(sheet, errMsg);
			if (!isValid) {
				throw new ServiceException(BxMessages.Error.MSG_VALID_CHECK, errMsg.toString());
			}

			String layoutVersion = ExcelUtils.readValue(sheet, 5, "W");

			String chlDscd = getCodeValueNm("CHL_DSCD", ExcelUtils.readValue(sheet, 5, "N"), "ko");
			String trxDscd = layoutVersion.equals("v20181011") || layoutVersion.equals("v20181016")
					|| layoutVersion.equals("v20181112") || layoutVersion.equals("v20181114")
					|| layoutVersion.equals("v20181130") || layoutVersion.equals("v20181211")
					|| layoutVersion.equals("v20181226") || layoutVersion.equals("v20190109")
							? getCodeValueNm("TRAN_DSCD", ExcelUtils.readValue(sheet, 5, "T"), "ko")
							: getCodeValueNm("TRAN_DSCD", ExcelUtils.readValue(sheet, 5, "S"), "ko");
			String msgType = getCodeValueNm("MSG_TYPE", ExcelUtils.readValue(sheet, 7, "N"), "ko");

			if (chlDscd.equals("INTERNAL")) {
				if (trxDscd.equals("ONLINE")) {
					if (!msgType.equals("IV")) {
						errMsg.append("\n대내 온라인 타입에 올 수 없는 전문 타입입니다.");
						isValid = false;

					}
					String sendSyscd = ExcelUtils.readValue(sheet, 10, "D");
					String recvSysCd = ExcelUtils.readValue(sheet, 10, "J");
					isValid = nullCheck(errMsg, sendSyscd, "\n송신시스템코드가", isValid);
					isValid = nullCheck(errMsg, recvSysCd, "\n수신시스템코드가", isValid);

					int send = srsysbsDao.selectAllCnt(sendSyscd, null, null, null, null);
					int recv = srsysbsDao.selectAllCnt(recvSysCd, null, null, null, null);

					if (send == 0) {
						errMsg.append("\n송신시스템코드가 존재하지 않습니다.");
						isValid = false;
					}
					if (recv == 0) {
						errMsg.append("\n수신시스템코드가 존재하지 않습니다.");
						isValid = false;
					}

				} else {
					if (!msgType.equals("BATH") && !msgType.equals("BATB") && !msgType.equals("BATT")) {
						errMsg.append("\n대내 배치 타입에 올 수 없는 전문 타입입니다.");
						isValid = false;
					}
					String fileId = layoutVersion.equals("v20181011") || layoutVersion.equals("v20181016")
							|| layoutVersion.equals("v20181112") || layoutVersion.equals("v20181114")
							|| layoutVersion.equals("v20181130") || layoutVersion.equals("v20181211")
							|| layoutVersion.equals("v20181226") || layoutVersion.equals("v20190109")
									? ExcelUtils.readValue(sheet, 7, "T")
									: ExcelUtils.readValue(sheet, 7, "S");
					isValid = nullCheck(errMsg, fileId, "\n파일아이디가", isValid);
					if (fileId.length() > 8) {
						// errMsg.append("\n파일ID 사이즈가 8 이상입니다.");
						// isValid = false;
					}
				}
			} else {
				if (trxDscd.equals("ONLINE")) {
					if (!msgType.equals("IV") && !msgType.equals("CH") && !msgType.equals("ISOH")
							&& !msgType.equals("ISOB")) {
						errMsg.append("\n대외 온라인 타입에 올 수 없는 전문 타입입니다.");
						isValid = false;
					}

					if (msgType.equals("IV") || msgType.equals("ISOB")) {
						String extBizCd = "";
						if (layoutVersion.equals("v20181011") || layoutVersion.equals("v20181016")
								|| layoutVersion.equals("v20181112") || layoutVersion.equals("v20181114")
								|| layoutVersion.equals("v20181130") || layoutVersion.equals("v20181211")) {
							extBizCd = ExcelUtils.readValue(sheet, 11, "W");
						} else if (layoutVersion.equals("v20181226") || layoutVersion.equals("v20190109")) {
							extBizCd = ExcelUtils.readValue(sheet, 11, "X");
						} else {
							extBizCd = ExcelUtils.readValue(sheet, 11, "V");
						}
						isValid = nullCheck(errMsg, extBizCd, "\n기관업무코드가", isValid);
						if (!isValid) {
							if (bizCdDao.selectBizcd(extBizCd) == null) {
								errMsg.append("\n기관업무코드가 존재하지 않습니다. ");
								isValid = false;
							}
						}
						String msgNmAndDto = ExcelUtils.readValue(sheet, 7, "J");
						isValid = nullCheck(errMsg, msgNmAndDto, "\n전문번호가", isValid);
						String msgVer = "";
						if (layoutVersion.equals("v20181011") || layoutVersion.equals("v20181016")
								|| layoutVersion.equals("v20181112") || layoutVersion.equals("v20181114")
								|| layoutVersion.equals("v20181130") || layoutVersion.equals("v20181211")) {
							msgVer = ExcelUtils.readValue(sheet, 11, "X");
						} else if (layoutVersion.equals("v20181226") || layoutVersion.equals("v20190109")) {
							msgVer = ExcelUtils.readValue(sheet, 11, "Y");
						} else {
							msgVer = ExcelUtils.readValue(sheet, 11, "W");
						}
						isValid = nullCheck(errMsg, msgVer, "\n전문버전이", isValid);
					} else {
						String extBizCd = "";
						if (layoutVersion.equals("v20181011") || layoutVersion.equals("v20181016")
								|| layoutVersion.equals("v20181112") || layoutVersion.equals("v20181114")
								|| layoutVersion.equals("v20181130") || layoutVersion.equals("v20181211")) {
							extBizCd = ExcelUtils.readValue(sheet, 11, "W");
						} else if (layoutVersion.equals("v20181226") || layoutVersion.equals("v20190109")) {
							extBizCd = ExcelUtils.readValue(sheet, 11, "X");
						} else {
							extBizCd = ExcelUtils.readValue(sheet, 11, "V");
						}
						isValid = nullCheck(errMsg, extBizCd, "\n기관업무코드가", isValid);
						if (!isValid) {
							if (bizCdDao.selectBizcd(extBizCd) == null) {
								errMsg.append("\n기관업무코드가 존재하지 않습니다. ");
								isValid = false;
							}
						}
					}

				} else {
					if (!msgType.equals("BATH") && !msgType.equals("BATB") && !msgType.equals("BATT")) {
						errMsg.append("\n대내 배치 타입에 올 수 없는 전문 타입입니다.");
						isValid = false;
					}
					String msgVer = "";
					if (layoutVersion.equals("v20181011") || layoutVersion.equals("v20181016")
							|| layoutVersion.equals("v20181112") || layoutVersion.equals("v20181114")
							|| layoutVersion.equals("v20181130") || layoutVersion.equals("v20181211")) {
						msgVer = ExcelUtils.readValue(sheet, 11, "X");
					} else if (layoutVersion.equals("v20181226") || layoutVersion.equals("v20190109")) {
						msgVer = ExcelUtils.readValue(sheet, 11, "Y");
					} else {
						msgVer = ExcelUtils.readValue(sheet, 11, "W");
					}
					isValid = nullCheck(errMsg, msgVer, "\n전문버전이", isValid);
					String fileId = layoutVersion.equals("v20181011") || layoutVersion.equals("v20181016")
							|| layoutVersion.equals("v20181112") || layoutVersion.equals("v20181114")
							|| layoutVersion.equals("v20181130") || layoutVersion.equals("v20181211")
							|| layoutVersion.equals("v20181226") || layoutVersion.equals("v20190109")
									? ExcelUtils.readValue(sheet, 7, "T")
									: ExcelUtils.readValue(sheet, 7, "S");
					isValid = nullCheck(errMsg, fileId, "\n파일아이디가", isValid);
					if (fileId.length() > 8) {
						// errMsg.append("\n파일ID 사이즈가 8 이상입니다.");
						// isValid = false;
					}
				}
			}

			if (!isValid) {
				throw new ServiceException(BxMessages.Error.MSG_VALID_CHECK, errMsg.toString());
			}

			int startIndex = 15;

			MsgIdCreateDto msgId = new MsgIdCreateDto();

			msgId.setChlDscd(getCodeValueNm("CHL_DSCD", ExcelUtils.readValue(sheet, 5, "N"), "ko"));
			msgId.setExtrnlBizNm(ExcelUtils.readValue(sheet, 11, "V"));
			msgId.setFileId(layoutVersion.equals("v20181011") || layoutVersion.equals("v20181016")
					|| layoutVersion.equals("v20181112") || layoutVersion.equals("v20181114")
					|| layoutVersion.equals("v20181130") || layoutVersion.equals("v20181211")
					|| layoutVersion.equals("v20181226") || layoutVersion.equals("v20190109")
							? ExcelUtils.readValue(sheet, 7, "T")
							: ExcelUtils.readValue(sheet, 7, "S"));
			msgId.setLv3Cd(ExcelUtils.readValue(sheet, 8, "F"));
			msgId.setMsgDscd(getCodeValueNm("MSG_TYPE", ExcelUtils.readValue(sheet, 7, "N"), "ko"));
			msgId.setMsgNumber(ExcelUtils.readValue(sheet, 7, "J"));
			if (layoutVersion.equals("v20181011") || layoutVersion.equals("v20181016")
					|| layoutVersion.equals("v20181112") || layoutVersion.equals("v20181114")
					|| layoutVersion.equals("v20181130") || layoutVersion.equals("v20181211")) {
				if (ExcelUtils.readValue(sheet, 11, "X") == null || ExcelUtils.readValue(sheet, 11, "X").equals("")
						|| ExcelUtils.readValue(sheet, 11, "X").equals("0")) {
					msgId.setMsgVersion(1);
				} else {
					msgId.setMsgVersion(Integer.parseInt(ExcelUtils.readValue(sheet, 11, "X")));
				}

				msgId.setTrxDscd(getCodeValueNm("TRAN_DSCD", ExcelUtils.readValue(sheet, 5, "T"), "ko"));

			} else if (layoutVersion.equals("v20181226") || layoutVersion.equals("v20190109")) {
				if (ExcelUtils.readValue(sheet, 11, "Y") == null || ExcelUtils.readValue(sheet, 11, "Y").equals("")
						|| ExcelUtils.readValue(sheet, 11, "Y").equals("0")) {
					msgId.setMsgVersion(1);
				} else {
					msgId.setMsgVersion(Integer.parseInt(ExcelUtils.readValue(sheet, 11, "Y")));
				}

				msgId.setTrxDscd(getCodeValueNm("TRAN_DSCD", ExcelUtils.readValue(sheet, 5, "T"), "ko"));

			} else {
				if (ExcelUtils.readValue(sheet, 11, "W") == null || ExcelUtils.readValue(sheet, 11, "W").equals("")
						|| ExcelUtils.readValue(sheet, 11, "W").equals("0")) {
					msgId.setMsgVersion(1);
				} else {
					msgId.setMsgVersion(Integer.parseInt(ExcelUtils.readValue(sheet, 11, "W")));
				}

				msgId.setTrxDscd(getCodeValueNm("TRAN_DSCD", ExcelUtils.readValue(sheet, 5, "S"), "ko"));

			}
			msgId.setReceiveSysCd(ExcelUtils.readValue(sheet, 10, "J"));
			msgId.setSendSysCd(ExcelUtils.readValue(sheet, 10, "D"));

			MsgIdCreateDto msgIdResult = this.getMsgLayoutId(msgId, isValid, errMsg);

			String layoutId = msgIdResult.getMsgLayoutId();

			if (layoutId.length() != 19) {
				errMsg.append("\n레이아웃 아이디 사이즈가 맞지 않습니다. [" + layoutId + "]");
				throw new ServiceException(BxMessages.Error.MSG_VALID_CHECK, errMsg.toString());
			}

			if (msgIdResult.getSeq() == null) {
				data.setCrtnSeq(null);
			} else {
				data.setCrtnSeq(Integer.parseInt(msgIdResult.getSeq()));
			}

			String layOutIdDoc = ExcelUtils.readValue(sheet, 5, "D");
			boolean insertYn = true;

			if (layOutIdDoc != null && !layOutIdDoc.equals("")) {
				layoutId = layOutIdDoc;
				insertYn = false;
			}

			readDataIndividual(sheet, data, layoutId);

			readDataGrid(sheet, data, startIndex, layoutId, isValid, errMsg);

			if (!isValid) {
				throw new ServiceException(BxMessages.Error.MSG_VALID_CHECK, errMsg.toString());
			}

			workbook.close();

			boolean isAdd = true;

			if (insertYn) {
				isAdd = true;
				// this.add(data, "FILE");
			} else {
				isAdd = false;
				// this.update(data);
			}
			fileResult = new MsglayoutbsFileUploadDto();
			fileResult.setAdd(isAdd);
			fileResult.setMsglayoutbsDto(data);

		} catch (FileNotFoundException e) {
			logger.error("{}", e);
		} catch (IOException e) {
			logger.error("{}", e);
		} catch (EncryptedDocumentException e) {
			logger.error("{}", e);
		} catch (InvalidFormatException e) {
			logger.error("{}", e);
		} finally {
			new File(inFile).delete();
			if (workbook != null) {
				try {
					workbook.close();
				} catch (IOException e) {
					logger.error("{}", e);
				}
			}
		}

		if (fileResult == null) {
			throw new ServiceException(BxMessages.Error.FILE_UPLOAD_EXCEPTION);
		}
		return fileResult;
	}
*/
	public MsgIdCreateDto getMsgLayoutId(MsgIdCreateDto msgIdCreateDto, boolean validation, StringBuilder errMsg) {

		// MSG_TYPE 전문타입 IV 개별부
		// MSG_TYPE 전문타입 CH 공통헤더
		// MSG_TYPE 전문타입 STH 표준헤더
		// MSG_TYPE 전문타입 ISOH ISO8583헤더
		// MSG_TYPE 전문타입 ISOB ISO8583바디
		// MSG_TYPE 전문타입 BATH 배치Header
		// MSG_TYPE 전문타입 BATB 배치Body
		// MSG_TYPE 전문타입 BATT 배치Trailer

		MsgIdCreateDto returnDto = new MsgIdCreateDto();

		String msgType = null;


		errMsg.append(msgIdCreateDto.getChlDscd());
		errMsg.append(msgIdCreateDto.getTrxDscd());
		errMsg.append(msgIdCreateDto.getMsgDscd());
		if (msgIdCreateDto.getChlDscd().equals("INTERNAL")) {
			if (msgIdCreateDto.getTrxDscd().equals("ONLINE")) {
				if (msgIdCreateDto.getMsgDscd().equals("IV")) {
					msgType = "IB";
				} else if (msgIdCreateDto.getMsgDscd().equals("STH")) {
					msgType = "IH";
				}
			} else {
				if (msgIdCreateDto.getMsgDscd().equals("BATH")) {
					msgType = "BH";
				} else if (msgIdCreateDto.getMsgDscd().equals("BATB")) {
					msgType = "BD";
				} else {
					msgType = "BT";
				}
			}
		} else {
			if (msgIdCreateDto.getTrxDscd().equals("ONLINE")) {
				if (msgIdCreateDto.getMsgDscd().equals("IV")) {
					msgType = "EB";
				} else if (msgIdCreateDto.getMsgDscd().equals("CH")) {
					msgType = "EH";
				} else if (msgIdCreateDto.getMsgDscd().equals("ISOH")) {
					msgType = "SH";
				} else if (msgIdCreateDto.getMsgDscd().equals("ISOB")) {
					msgType = "SB";
				}
			} else {
				if (msgIdCreateDto.getMsgDscd().equals("BATH")) {
					msgType = "BH";
				} else if (msgIdCreateDto.getMsgDscd().equals("BATB")) {
					msgType = "BD";
				} else {
					msgType = "BT";
				}
			}
		}

		// 대외공통부: EH, 대외개별부: EB, ISO8583공통부: SH, ISO8583개별부: SB, 대내개별부: IB, 배치헤더: BH,
		// 배치데이터: BD, 배치트레일러: BT
		// ※ 전문ID 채번 규칙
		// - 대외공통부, ISO8583공통부: 전문타입2자리 + 기관업무구분8자리 + 어플리케이션코드2자리 + 일련번호4자리+ V + 전문버전2자리
		// - 대외개별부, ISO8583개별부: 전문타입2자리 + 기관업무구분8자리 + 어플리케이션코드2자리 + 전문번호4자리+ V + 전문버전2자리
		// - 대내개별부: 전문타입2자리 + 송수신시스템코드6자리 + 어플리케이션코드2자리 + 일련번호6자리+ V + 전문버전2자리
		// - 배치 헤더/데이터/트레일러: 전문타입2자리 + 어플리케이션코드2자리 + 파일ID8자리 + 일련번호4자리+ V + 전문버전2자리
		String msgLayoutId = null;
		String seqResult = null;
		if (msgType == null || "".equals(msgType)) {
			errMsg.append("\n전문타입이 유효하지 않습니다. 채널구분, 거래구분, 전문타입을 확인하여 주세요.");
			throw new ServiceException(BxMessages.Error.MSG_VALID_CHECK, errMsg.toString());
		}

		if (msgType.equals("EH") || msgType.equals("SH")) {

			String extBizCd = msgIdCreateDto.getExtrnlBizNm();

			if (extBizCd.length() > 8) {
				errMsg.append("\n기관업무구분코드 자릿수가 8자리이상입니다.");
				validation = false;
			}

			extBizCd = StringUtils.rightPad(extBizCd, 8, "0");
			String l3Cd = msgIdCreateDto.getLv3Cd();
			if (l3Cd.length() > 2) {
				errMsg.append("\n어플리케이션코드 자릿수가 2자리이상입니다.");
				validation = false;
			}
			String version = Integer.toString(msgIdCreateDto.getMsgVersion());
			version = StringUtils.leftPad(version, 2, "0");
			msgIdCreateDto.setFileId("");
			msgIdCreateDto.setMsgNumber("");
			msgIdCreateDto.setReceiveSysCd("");
			msgIdCreateDto.setSendSysCd("");
			Integer seq = msglayoutbsDao.selectIdSeq(msgIdCreateDto);

			if (seq == null) {
				seq = 0;
			}
			seq = seq + 1;
			seqResult = StringUtils.leftPad(Integer.toString(seq), 4, "0");
			msgLayoutId = msgType + extBizCd + l3Cd + seqResult + "V" + version;
		} else if (msgType.equals("EB") || msgType.equals("SB")) {

			String extBizCd = msgIdCreateDto.getExtrnlBizNm();
			extBizCd = StringUtils.rightPad(extBizCd, 8, "0");
			if (extBizCd.length() > 8) {
				errMsg.append("\n기관업무구분코드 자릿수가 8자리이상입니다.");
				validation = false;
			}
			String l3Cd = msgIdCreateDto.getLv3Cd();
			if (l3Cd.length() > 2) {
				errMsg.append("\n어플리케이션코드 자릿수가 2자리이상입니다.");
				validation = false;
			}
			String version = Integer.toString(msgIdCreateDto.getMsgVersion());
			version = StringUtils.leftPad(version, 2, "0");
			String msgNumber = msgIdCreateDto.getMsgNumber();
			if (msgNumber.length() > 4) {
				errMsg.append("\n전문번호 자릿수가 4자리이상입니다.");
				validation = false;
			}
			msgNumber = StringUtils.leftPad(msgNumber, 4, "0");
			msgLayoutId = msgType + extBizCd + l3Cd + msgNumber + "V" + version;
		} else if (msgType.equals("IB")) {

			String sendSystem = msgIdCreateDto.getSendSysCd();
			if (sendSystem.length() > 3) {
				errMsg.append("\n송신시스템코드가자릿수가 3자리이상입니다.");
				validation = false;
			}
			String recvSystem = msgIdCreateDto.getReceiveSysCd();
			if (recvSystem.length() > 3) {
				errMsg.append("\n수신시스템코드가자릿수가 3자리이상입니다.");
				validation = false;
			}
			String l3Cd = msgIdCreateDto.getLv3Cd();
			if (l3Cd.length() > 2) {
				errMsg.append("\n어플리케이션코드 자릿수가 2자리이상입니다.");
				validation = false;
			}
			String version = Integer.toString(msgIdCreateDto.getMsgVersion());
			version = StringUtils.leftPad(version, 2, "0");
			msgIdCreateDto.setMsgNumber("");
			Integer seq = msglayoutbsDao.selectIdSeq(msgIdCreateDto);

			if (seq == null) {
				seq = 0;
			}
			seq = seq + 1;
			seqResult = StringUtils.leftPad(Integer.toString(seq), 6, "0");
			msgLayoutId = msgType + sendSystem + recvSystem + l3Cd + seqResult + "V" + version;
		} else {
			String l3Cd = msgIdCreateDto.getLv3Cd();
			if (l3Cd.length() > 2) {
				errMsg.append("\n어플리케이션코드 자릿수가 2자리이상입니다.");
				validation = false;
			}

			String fileId = msgIdCreateDto.getFileId();
			int fileIdSize = fileId.length();
			boolean fileSizeMax = false;
			if (fileIdSize > 8) {
				// throw new ServiceException(BxMessages.Error.FILE_SIZE_ERR);
				fileId = fileId.substring(0, 8);
				msgIdCreateDto.setFileId(fileId);
				fileSizeMax = true;
			} else if (fileIdSize == 8) {
				fileSizeMax = true;
			}
			fileId = StringUtils.rightPad(fileId, 8, "0");
			String version = Integer.toString(msgIdCreateDto.getMsgVersion());
			version = StringUtils.leftPad(version, 2, "0");
			msgIdCreateDto.setMsgNumber("");

			Integer seq = null;
			if (fileSizeMax) {
				seq = msglayoutbsDao.selectIdSeqFile(msgIdCreateDto);
			} else {
				seq = msglayoutbsDao.selectIdSeq(msgIdCreateDto);
			}

			if (seq == null) {
				seq = 0;
			}
			seq = seq + 1;
			seqResult = StringUtils.leftPad(Integer.toString(seq), 4, "0");
			msgLayoutId = msgType + l3Cd + fileId + seqResult + "V" + version;
		}

		if (!validation) {
			throw new ServiceException(BxMessages.Error.MSG_VALID_CHECK, errMsg.toString());
		}

		logger.debug(
				"$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		logger.debug(msgLayoutId);
		logger.debug(
				"$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");

		returnDto.setMsgLayoutId(msgLayoutId);
		returnDto.setSeq(seqResult);

		return returnDto;
	}
	
	/*
	public MsgIdCreateDto getMsgLayoutId(MsgIdCreateDto msgIdCreateDto, boolean validation, StringBuilder errMsg) {

		// MSG_TYPE 전문타입 IV 개별부
		// MSG_TYPE 전문타입 CH 공통헤더
		// MSG_TYPE 전문타입 STH 표준헤더
		// MSG_TYPE 전문타입 ISOH ISO8583헤더
		// MSG_TYPE 전문타입 ISOB ISO8583바디
		// MSG_TYPE 전문타입 BATH 배치Header
		// MSG_TYPE 전문타입 BATB 배치Body
		// MSG_TYPE 전문타입 BATT 배치Trailer

		MsgIdCreateDto returnDto = new MsgIdCreateDto();

		String msgType = null;


		errMsg.append(msgIdCreateDto.getChlDscd());
		errMsg.append(msgIdCreateDto.getTrxDscd());
		errMsg.append(msgIdCreateDto.getMsgDscd());
		if (msgIdCreateDto.getChlDscd().equals("INTERNAL")) {
			if (msgIdCreateDto.getTrxDscd().equals("ONLINE")) {
				if (msgIdCreateDto.getMsgDscd().equals("IV")) {
					msgType = "IB";
				} else if (msgIdCreateDto.getMsgDscd().equals("STH")) {
					msgType = "IH";
				}
			} else {
				if (msgIdCreateDto.getMsgDscd().equals("BATH")) {
					msgType = "BH";
				} else if (msgIdCreateDto.getMsgDscd().equals("BATB")) {
					msgType = "BD";
				} else {
					msgType = "BT";
				}
			}
		} else {
			if (msgIdCreateDto.getTrxDscd().equals("ONLINE")) {
				if (msgIdCreateDto.getMsgDscd().equals("IV")) {
					msgType = "EB";
				} else if (msgIdCreateDto.getMsgDscd().equals("CH")) {
					msgType = "EH";
				} else if (msgIdCreateDto.getMsgDscd().equals("ISOH")) {
					msgType = "SH";
				} else if (msgIdCreateDto.getMsgDscd().equals("ISOB")) {
					msgType = "SB";
				}
			} else {
				if (msgIdCreateDto.getMsgDscd().equals("BATH")) {
					msgType = "BH";
				} else if (msgIdCreateDto.getMsgDscd().equals("BATB")) {
					msgType = "BD";
				} else {
					msgType = "BT";
				}
			}
		}

		// 대외공통부: EH, 대외개별부: EB, ISO8583공통부: SH, ISO8583개별부: SB, 대내개별부: IB, 배치헤더: BH,
		// 배치데이터: BD, 배치트레일러: BT
		// ※ 전문ID 채번 규칙
		// - 대외공통부, ISO8583공통부: 전문타입2자리 + 기관업무구분8자리 + 어플리케이션코드2자리 + 일련번호4자리+ V + 전문버전2자리
		// - 대외개별부, ISO8583개별부: 전문타입2자리 + 기관업무구분8자리 + 어플리케이션코드2자리 + 전문번호4자리+ V + 전문버전2자리
		// - 대내개별부: 전문타입2자리 + 송수신시스템코드6자리 + 어플리케이션코드2자리 + 일련번호6자리+ V + 전문버전2자리
		// - 배치 헤더/데이터/트레일러: 전문타입2자리 + 어플리케이션코드2자리 + 파일ID8자리 + 일련번호4자리+ V + 전문버전2자리
		String msgLayoutId = null;
		String seqResult = null;
		if (msgType == null || "".equals(msgType)) {
			errMsg.append("\n전문타입이 유효하지 않습니다. 채널구분, 거래구분, 전문타입을 확인하여 주세요.");
			throw new ServiceException(BxMessages.Error.MSG_VALID_CHECK, errMsg.toString());
		}

		if (msgType.equals("EH") || msgType.equals("SH")) {

			String extBizCd = msgIdCreateDto.getExtrnlBizNm();

			if (extBizCd.length() > 8) {
				errMsg.append("\n기관업무구분코드 자릿수가 8자리이상입니다.");
				validation = false;
			}

			extBizCd = StringUtils.rightPad(extBizCd, 8, "0");
			String l3Cd = msgIdCreateDto.getLv3Cd();
			if (l3Cd.length() > 2) {
				errMsg.append("\n어플리케이션코드 자릿수가 2자리이상입니다.");
				validation = false;
			}
			String version = Integer.toString(msgIdCreateDto.getMsgVersion());
			version = StringUtils.leftPad(version, 2, "0");
			msgIdCreateDto.setFileId("");
			msgIdCreateDto.setMsgNumber("");
			msgIdCreateDto.setReceiveSysCd("");
			msgIdCreateDto.setSendSysCd("");
			Integer seq = msglayoutbsDao.selectIdSeq(msgIdCreateDto);

			if (seq == null) {
				seq = 0;
			}
			seq = seq + 1;
			seqResult = StringUtils.leftPad(Integer.toString(seq), 4, "0");
			msgLayoutId = msgType + extBizCd + l3Cd + seqResult + "V" + version;
		} else if (msgType.equals("EB") || msgType.equals("SB")) {

			String extBizCd = msgIdCreateDto.getExtrnlBizNm();
			extBizCd = StringUtils.rightPad(extBizCd, 8, "0");
			if (extBizCd.length() > 8) {
				errMsg.append("\n기관업무구분코드 자릿수가 8자리이상입니다.");
				validation = false;
			}
			String l3Cd = msgIdCreateDto.getLv3Cd();
			if (l3Cd.length() > 2) {
				errMsg.append("\n어플리케이션코드 자릿수가 2자리이상입니다.");
				validation = false;
			}
			String version = Integer.toString(msgIdCreateDto.getMsgVersion());
			version = StringUtils.leftPad(version, 2, "0");
			String msgNumber = msgIdCreateDto.getMsgNumber();
			if (msgNumber.length() > 4) {
				errMsg.append("\n전문번호 자릿수가 4자리이상입니다.");
				validation = false;
			}
			msgNumber = StringUtils.leftPad(msgNumber, 4, "0");
			msgLayoutId = msgType + extBizCd + l3Cd + msgNumber + "V" + version;
		} else if (msgType.equals("IB")) {

			String sendSystem = msgIdCreateDto.getSendSysCd();
			if (sendSystem.length() > 3) {
				errMsg.append("\n송신시스템코드가자릿수가 3자리이상입니다.");
				validation = false;
			}
			String recvSystem = msgIdCreateDto.getReceiveSysCd();
			if (recvSystem.length() > 3) {
				errMsg.append("\n수신시스템코드가자릿수가 3자리이상입니다.");
				validation = false;
			}
			String l3Cd = msgIdCreateDto.getLv3Cd();
			if (l3Cd.length() > 2) {
				errMsg.append("\n어플리케이션코드 자릿수가 2자리이상입니다.");
				validation = false;
			}
			String version = Integer.toString(msgIdCreateDto.getMsgVersion());
			version = StringUtils.leftPad(version, 2, "0");
			msgIdCreateDto.setMsgNumber("");
			Integer seq = msglayoutbsDao.selectIdSeq(msgIdCreateDto);

			if (seq == null) {
				seq = 0;
			}
			seq = seq + 1;
			seqResult = StringUtils.leftPad(Integer.toString(seq), 6, "0");
			msgLayoutId = msgType + sendSystem + recvSystem + l3Cd + seqResult + "V" + version;
		} else {
			String l3Cd = msgIdCreateDto.getLv3Cd();
			if (l3Cd.length() > 2) {
				errMsg.append("\n어플리케이션코드 자릿수가 2자리이상입니다.");
				validation = false;
			}

			String fileId = msgIdCreateDto.getFileId();
			int fileIdSize = fileId.length();
			boolean fileSizeMax = false;
			if (fileIdSize > 8) {
				// throw new ServiceException(BxMessages.Error.FILE_SIZE_ERR);
				fileId = fileId.substring(0, 8);
				msgIdCreateDto.setFileId(fileId);
				fileSizeMax = true;
			} else if (fileIdSize == 8) {
				fileSizeMax = true;
			}
			fileId = StringUtils.rightPad(fileId, 8, "0");
			String version = Integer.toString(msgIdCreateDto.getMsgVersion());
			version = StringUtils.leftPad(version, 2, "0");
			msgIdCreateDto.setMsgNumber("");

			Integer seq = null;
			if (fileSizeMax) {
				seq = msglayoutbsDao.selectIdSeqFile(msgIdCreateDto);
			} else {
				seq = msglayoutbsDao.selectIdSeq(msgIdCreateDto);
			}

			if (seq == null) {
				seq = 0;
			}
			seq = seq + 1;
			seqResult = StringUtils.leftPad(Integer.toString(seq), 4, "0");
			msgLayoutId = msgType + l3Cd + fileId + seqResult + "V" + version;
		}

		if (!validation) {
			throw new ServiceException(BxMessages.Error.MSG_VALID_CHECK, errMsg.toString());
		}

		logger.debug(
				"$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		logger.debug(msgLayoutId);
		logger.debug(
				"$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");

		returnDto.setMsgLayoutId(msgLayoutId);
		returnDto.setSeq(seqResult);

		return returnDto;
	}
	*/
/*
	private boolean validationMsgInfo(Sheet sheet, StringBuilder errMsg) {

		String layoutVersion = ExcelUtils.readValue(sheet, 5, "W");

		boolean isValid = true;

		String chlDscd = getCodeValueNm("CHL_DSCD", ExcelUtils.readValue(sheet, 5, "N"), "ko");
		isValid = nullCheck(errMsg, chlDscd, "\n채널구분코드가", isValid);
		String trxDscd = "";
		if (layoutVersion.equals("v20181011") || layoutVersion.equals("v20181016") || layoutVersion.equals("v20181112")
				|| layoutVersion.equals("v20181114") || layoutVersion.equals("v20181130")
				|| layoutVersion.equals("v20181211") || layoutVersion.equals("v20181226")
				|| layoutVersion.equals("v20190109")) {
			trxDscd = getCodeValueNm("TRAN_DSCD", ExcelUtils.readValue(sheet, 5, "T"), "ko");
		} else {
			trxDscd = getCodeValueNm("TRAN_DSCD", ExcelUtils.readValue(sheet, 5, "S"), "ko");
		}
		isValid = nullCheck(errMsg, trxDscd, "\n거래구분코드가", isValid);
		String appCdL1 = ExcelUtils.readValue(sheet, 8, "D");
		isValid = nullCheck(errMsg, appCdL1, "\nappCdL1", isValid);
		String appCdL2 = ExcelUtils.readValue(sheet, 8, "E");
		isValid = nullCheck(errMsg, appCdL2, "\nappCdL2", isValid);
		String appCdL3 = ExcelUtils.readValue(sheet, 8, "F");
		isValid = nullCheck(errMsg, appCdL3, "\nappCdL3", isValid);

		if (!isValid && !"PP".equals(appCdL1)) {
			errMsg.append("\nL1코드가 일치하지 않습니다.");
			isValid = false;
		}
		if (isValid && !appCdL2.equals(appCdL3.substring(0, 1))) {
			errMsg.append("\nL2코드가 일치하지 않습니다.");
			isValid = false;
		}

		int appCnt = appcdDao.selectAllCnt(appCdL3, null, null, "3", 0, null);
		if (appCnt == 0) {
			errMsg.append("\nL3코드가 존재하지 않습니다.");
			isValid = false;
		}

		String msgType = getCodeValueNm("MSG_TYPE", ExcelUtils.readValue(sheet, 7, "N"), "ko");
		isValid = nullCheck(errMsg, msgType, "\n메시지구분코드가", isValid);

		return isValid;
	}
*/
	private boolean validationMsgInfo(Sheet sheet, StringBuilder errMsg) {
		boolean isValid = true;
		String chlDscd = getCodeValueNm("CHL_DSCD", ExcelUtils.readValue(sheet, 5, "J"), "ko");
		isValid = nullCheck(errMsg, chlDscd, "\n채널구분코드가", isValid);
		String trxDscd = getCodeValueNm("TRAN_DSCD", ExcelUtils.readValue(sheet, 5, "M"), "ko");
		isValid = nullCheck(errMsg, trxDscd, "\n발생유형코드가", isValid);
		String appCdL1 = ExcelUtils.readValue(sheet, 6, "D");
		isValid = nullCheck(errMsg, appCdL1, "\nappCdL1", isValid);

		String msgType = getCodeValueNm("MSG_TYPE", ExcelUtils.readValue(sheet, 6, "J"), "ko");
		isValid = nullCheck(errMsg, msgType, "\n메시지구분코드가", isValid);

		return isValid;
	}
	private boolean nullCheck(StringBuilder errMsg, String code, String name, boolean isValid) {

		if (StringUtils.isEmpty(code)) {
			errMsg.append(name + " 비어있습니다.");
			isValid = false;
		}

		return isValid;
	}

	private void readDataIndividual(Sheet sheet, MsglayoutbsDto data, String layoutId) {
		data.setMsgLayoutId(layoutId);
		data.setMsgDesc(ExcelUtils.readValue(sheet, 8, "C"));
		data.setChlDscd(getCodeValueNm("CHL_DSCD", convertMsgEngToKor(ExcelUtils.readValue(sheet, 5, "I")), "ko")); 
		data.setLv1Cd(ExcelUtils.readValue(sheet, 6, "C"));
		data.setMsgDataVal(ExcelUtils.readValue(sheet, 6, "F"));
		data.setMsgDscd(getCodeValueNm("MSG_TYPE", convertMsgEngToKor(ExcelUtils.readValue(sheet, 6, "I")), "ko")); 
		data.setJobId(ExcelUtils.readValue(sheet, 6, "L"));
		data.setRegDttm(DateUtils.getCurrentDate(9));
		data.setRegManId(ExcelUtils.readValue(sheet, 3, "K"));
		data.setTrxDscd(getCodeValueNm("TRAN_DSCD", convertMsgEngToKor(ExcelUtils.readValue(sheet, 5, "L")), "ko")); 
		data.setMsgNm(ExcelUtils.readValue(sheet, 5, "F"));
		data.setRsrvFldVal1(ExcelUtils.readValue(sheet, 7, "C"));
		data.setRsrvFldVal2(ExcelUtils.readValue(sheet, 7, "F"));
		data.setCustApiYn(ExcelUtils.readValue(sheet, 7, "I"));	

	}
	
	
	/*
	private void readDataIndividual(Sheet sheet, MsglayoutbsDto data, String layoutId) {

		String layoutVersion = ExcelUtils.readValue(sheet, 5, "W");

		if (layoutVersion.equals("v20181011")) {
			data.setMsgLayoutId(layoutId);
			data.setMsgDesc(ExcelUtils.readValue(sheet, 11, "D"));
			data.setChlDscd(getCodeValueNm("CHL_DSCD", ExcelUtils.readValue(sheet, 5, "N"), "ko")); // 코드값매핑
			data.setDtoNm(ExcelUtils.readValue(sheet, 11, "Y"));
			data.setExtrnlBizNm(ExcelUtils.readValue(sheet, 11, "W"));
			data.setBitMapCrtnYn(ExcelUtils.readValue(sheet, 11, "AA"));
			data.setIso8583DataTypeCd(getCodeValueNm("ISO_DATA_TYPE", ExcelUtils.readValue(sheet, 11, "AC"), "ko")); // 코드값매핑
			data.setLv1Cd(ExcelUtils.readValue(sheet, 8, "D"));
			data.setLv2Cd(ExcelUtils.readValue(sheet, 8, "E"));
			data.setLv3Cd(ExcelUtils.readValue(sheet, 8, "F"));
			// data.setLv4Cd(ExcelUtils.readValue(sheet, 8, "G"));
			// data.setLv5Cd("");
			data.setMsgDataVal(ExcelUtils.readValue(sheet, 7, "J"));
			data.setMsgDscd(getCodeValueNm("MSG_TYPE", ExcelUtils.readValue(sheet, 7, "N"), "ko")); // 코드값매핑
			if (ExcelUtils.readValue(sheet, 11, "X") == null || ExcelUtils.readValue(sheet, 11, "X").equals("")
					|| ExcelUtils.readValue(sheet, 11, "X").equals("0")) {
				data.setMsgVersion(1);
			} else {
				data.setMsgVersion(Integer.parseInt(ExcelUtils.readValue(sheet, 11, "X")));
			}
			data.setJobId(ExcelUtils.readValue(sheet, 7, "T"));
			data.setRegDttm(DateUtils.getCurrentDate(9));
			data.setRegManId(ExcelUtils.readValue(sheet, 3, "T"));
			data.setTrxDscd(getCodeValueNm("TRAN_DSCD", ExcelUtils.readValue(sheet, 5, "T"), "ko")); // 코드값매핑
			data.setMsgNm(ExcelUtils.readValue(sheet, 5, "J"));
			data.setRsrvFldVal1(ExcelUtils.readValue(sheet, 10, "D"));
			data.setRsrvFldVal2(ExcelUtils.readValue(sheet, 10, "J"));
			data.setBitMapTypeCd(getCodeValueNm("ISO_BITMAP_TYPE_CD", ExcelUtils.readValue(sheet, 11, "AG"), "ko"));
		} else if (layoutVersion.equals("v20181016") || layoutVersion.equals("v20181112")
				|| layoutVersion.equals("v20181114") || layoutVersion.equals("v20181130")
				|| layoutVersion.equals("v20181211")) {
			data.setMsgLayoutId(layoutId);
			data.setMsgDesc(ExcelUtils.readValue(sheet, 11, "D"));
			data.setChlDscd(getCodeValueNm("CHL_DSCD", ExcelUtils.readValue(sheet, 5, "N"), "ko")); // 코드값매핑
			data.setDtoNm(ExcelUtils.readValue(sheet, 11, "Y"));
			data.setExtrnlBizNm(ExcelUtils.readValue(sheet, 11, "W"));
			data.setBitMapCrtnYn(ExcelUtils.readValue(sheet, 11, "AA"));
			data.setIso8583DataTypeCd(getCodeValueNm("ISO_DATA_TYPE", ExcelUtils.readValue(sheet, 11, "AC"), "ko")); // 코드값매핑
			data.setLv1Cd(ExcelUtils.readValue(sheet, 8, "D"));
			data.setLv2Cd(ExcelUtils.readValue(sheet, 8, "E"));
			data.setLv3Cd(ExcelUtils.readValue(sheet, 8, "F"));
			// data.setLv4Cd(ExcelUtils.readValue(sheet, 8, "G"));
			// data.setLv5Cd("");
			data.setMsgDataVal(ExcelUtils.readValue(sheet, 7, "J"));
			data.setMsgDscd(getCodeValueNm("MSG_TYPE", ExcelUtils.readValue(sheet, 7, "N"), "ko")); // 코드값매핑
			if (ExcelUtils.readValue(sheet, 11, "X") == null || ExcelUtils.readValue(sheet, 11, "X").equals("")
					|| ExcelUtils.readValue(sheet, 11, "X").equals("0")) {
				data.setMsgVersion(1);
			} else {
				data.setMsgVersion(Integer.parseInt(ExcelUtils.readValue(sheet, 11, "X")));
			}
			data.setJobId(ExcelUtils.readValue(sheet, 7, "T"));
			data.setRegDttm(DateUtils.getCurrentDate(9));
			data.setRegManId(ExcelUtils.readValue(sheet, 3, "T"));
			data.setTrxDscd(getCodeValueNm("TRAN_DSCD", ExcelUtils.readValue(sheet, 5, "T"), "ko")); // 코드값매핑
			data.setMsgNm(ExcelUtils.readValue(sheet, 5, "J"));
			data.setRsrvFldVal1(ExcelUtils.readValue(sheet, 10, "D"));
			data.setRsrvFldVal2(ExcelUtils.readValue(sheet, 10, "J"));
			data.setBitMapTypeCd(getCodeValueNm("ISO_BITMAP_TYPE_CD", ExcelUtils.readValue(sheet, 11, "AG"), "ko"));
			String shareYn = ExcelUtils.readValue(sheet, 10, "N");
			if (shareYn == null || shareYn.equals("")) {
				shareYn = "N";
				data.setSharedYn(shareYn);
			} else {
				data.setSharedYn(shareYn);
			}
		} else if (layoutVersion.equals("v20181226") || layoutVersion.equals("v20190109")) {
			data.setMsgLayoutId(layoutId);
			data.setMsgDesc(ExcelUtils.readValue(sheet, 11, "D"));
			data.setChlDscd(getCodeValueNm("CHL_DSCD", ExcelUtils.readValue(sheet, 5, "N"), "ko")); // 코드값매핑
			data.setDtoNm(ExcelUtils.readValue(sheet, 11, "Z"));
			data.setExtrnlBizNm(ExcelUtils.readValue(sheet, 11, "X"));
			data.setBitMapCrtnYn(ExcelUtils.readValue(sheet, 11, "AB"));
			data.setIso8583DataTypeCd(getCodeValueNm("ISO_DATA_TYPE", ExcelUtils.readValue(sheet, 11, "AD"), "ko")); // 코드값매핑
			data.setLv1Cd(ExcelUtils.readValue(sheet, 8, "D"));
			data.setLv2Cd(ExcelUtils.readValue(sheet, 8, "E"));
			data.setLv3Cd(ExcelUtils.readValue(sheet, 8, "F"));
			// data.setLv4Cd(ExcelUtils.readValue(sheet, 8, "G"));
			// data.setLv5Cd("");
			data.setMsgDataVal(ExcelUtils.readValue(sheet, 7, "J"));
			data.setMsgDscd(getCodeValueNm("MSG_TYPE", ExcelUtils.readValue(sheet, 7, "N"), "ko")); // 코드값매핑
			if (ExcelUtils.readValue(sheet, 11, "Y") == null || ExcelUtils.readValue(sheet, 11, "Y").equals("")
					|| ExcelUtils.readValue(sheet, 11, "Y").equals("0")) {
				data.setMsgVersion(1);
			} else {
				data.setMsgVersion(Integer.parseInt(ExcelUtils.readValue(sheet, 11, "Y")));
			}
			data.setJobId(ExcelUtils.readValue(sheet, 7, "T"));
			data.setRegDttm(DateUtils.getCurrentDate(9));
			data.setRegManId(ExcelUtils.readValue(sheet, 3, "T"));
			data.setTrxDscd(getCodeValueNm("TRAN_DSCD", ExcelUtils.readValue(sheet, 5, "T"), "ko")); // 코드값매핑
			data.setMsgNm(ExcelUtils.readValue(sheet, 5, "J"));
			data.setRsrvFldVal1(ExcelUtils.readValue(sheet, 10, "D"));
			data.setRsrvFldVal2(ExcelUtils.readValue(sheet, 10, "J"));
			data.setBitMapTypeCd(getCodeValueNm("ISO_BITMAP_TYPE_CD", ExcelUtils.readValue(sheet, 11, "AH"), "ko"));
			String shareYn = ExcelUtils.readValue(sheet, 10, "N");
			if (shareYn == null || shareYn.equals("")) {
				shareYn = "N";
				data.setSharedYn(shareYn);
			} else {
				data.setSharedYn(shareYn);
			}
			if (layoutVersion.equals("v20190109")) {
				if (data.getChlDscd().equals("INTERNAL") && data.getMsgDscd().equals("IV")) {
					String cusApiYn = ExcelUtils.readValue(sheet, 10, "T");
					if (cusApiYn == null || cusApiYn.equals("")) {
						cusApiYn = "N";
						data.setCustApiYn(cusApiYn);
					} else {
						data.setCustApiYn(cusApiYn);
					}
				}
			}
		} else {

			data.setMsgLayoutId(layoutId);
			data.setMsgDesc(ExcelUtils.readValue(sheet, 11, "D"));
			data.setChlDscd(getCodeValueNm("CHL_DSCD", ExcelUtils.readValue(sheet, 5, "N"), "ko")); // 코드값매핑
			data.setDtoNm(ExcelUtils.readValue(sheet, 11, "X"));
			data.setExtrnlBizNm(ExcelUtils.readValue(sheet, 11, "V"));
			data.setBitMapCrtnYn(ExcelUtils.readValue(sheet, 11, "Z"));
			data.setIso8583DataTypeCd(getCodeValueNm("ISO_DATA_TYPE", ExcelUtils.readValue(sheet, 11, "AB"), "ko")); // 코드값매핑
			data.setLv1Cd(ExcelUtils.readValue(sheet, 8, "D"));
			data.setLv2Cd(ExcelUtils.readValue(sheet, 8, "E"));
			data.setLv3Cd(ExcelUtils.readValue(sheet, 8, "F"));
			// data.setLv4Cd(ExcelUtils.readValue(sheet, 8, "G"));
			// data.setLv5Cd("");
			data.setMsgDataVal(ExcelUtils.readValue(sheet, 7, "J"));
			data.setMsgDscd(getCodeValueNm("MSG_TYPE", ExcelUtils.readValue(sheet, 7, "N"), "ko")); // 코드값매핑
			if (ExcelUtils.readValue(sheet, 11, "W") == null || ExcelUtils.readValue(sheet, 11, "W").equals("")
					|| ExcelUtils.readValue(sheet, 11, "W").equals("0")) {
				data.setMsgVersion(1);
			} else {
				data.setMsgVersion(Integer.parseInt(ExcelUtils.readValue(sheet, 11, "W")));
			}
			data.setJobId(ExcelUtils.readValue(sheet, 7, "S"));
			data.setRegDttm(DateUtils.getCurrentDate(9));
			data.setRegManId(ExcelUtils.readValue(sheet, 3, "S"));
			data.setTrxDscd(getCodeValueNm("TRAN_DSCD", ExcelUtils.readValue(sheet, 5, "S"), "ko")); // 코드값매핑
			data.setMsgNm(ExcelUtils.readValue(sheet, 5, "J"));
			data.setRsrvFldVal1(ExcelUtils.readValue(sheet, 10, "D"));
			data.setRsrvFldVal2(ExcelUtils.readValue(sheet, 10, "J"));
			data.setBitMapTypeCd(getCodeValueNm("ISO_BITMAP_TYPE_CD", ExcelUtils.readValue(sheet, 11, "AF"), "ko"));
		}
		return;
	}
	*/

	private int readDataGrid(Sheet sheet, MsglayoutbsDto data, int startIndex, String layoutId, boolean isValid,
			StringBuilder errMsg) {
		List<MsglayoutdtDto> layoutList = new ArrayList<>();

		int index = startIndex;

		String layoutVersion = ExcelUtils.readValue(sheet, 5, "W");

		List<String> parentsList = new ArrayList<String>();
		parentsList.add(layoutId);

		Map<String, String> ioKeyMap = new HashMap<String, String>();

		int seq = 1;
		for (; index < sheet.getPhysicalNumberOfRows(); index++) {
			if (ExcelUtils.readValue(sheet, index, "A").equals("계") || ExcelUtils.readValue(sheet, index, "B").equals("") || ExcelUtils.readValue(sheet, index, "B") == null) {
				break;
			}
			MsglayoutdtDto layout = new MsglayoutdtDto();
			
			layout.setMsgLayoutId(layoutId);
			layout.setMsgSeq(seq++);
			isValid = nullCheck(errMsg, ExcelUtils.readValue(sheet, index, "B"), "\n영문명", isValid);
			layout.setFldEngNm(ExcelUtils.readValue(sheet, index, "B"));
			//isValid = nullCheck(errMsg, ExcelUtils.readValue(sheet, index, "D"), "\n한글명", isValid);
			layout.setFldKorNm(ExcelUtils.readValue(sheet, index, "C"));
			String type = getCodeValueNm("DATA_TYPE", ExcelUtils.readValue(sheet, index, "D"), "ko");
			//isValid = nullCheck(errMsg, type, " \n데이터타입", isValid);
			layout.setDataTypeNm(type);
			
		
			if (!StringUtils.isEmpty(ExcelUtils.readValue(sheet, index, "E"))) {
				layout.setMsgLen(Integer.parseInt(ExcelUtils.readValue(sheet, index, "E")));
			}
			if (!StringUtils.isEmpty(ExcelUtils.readValue(sheet, index, "F"))) {
				layout.setDecimalLen(Integer.parseInt(ExcelUtils.readValue(sheet, index, "F")));	
			}
					
			layout.setFldRmk(ExcelUtils.readValue(sheet, index, "L"));// 비고
			layout.setArraySizeRefVal(ExcelUtils.readValue(sheet, index, "H"));// 배열참조
		//	String replKey = ExcelUtils.readValue(sheet, index, "O");
			String chlType = data.getChlDscd(); 
			String align = ExcelUtils.readValue(sheet, index, "J");
			if (StringUtils.isEmpty(align)) {
				if (type.equals("STRING") || type.equals("BYTEARRAY")) {
					align = "LEFT";
				} else if (type.equals("LONG") || type.equals("BIGDECIMAL")) {
					align = "RIGHT";
				} else {
					align = "";
				}
			}
			layout.setAlignNm(align);// 정렬
			layout.setChildDtoNm(ExcelUtils.readValue(sheet, index, "I"));// 하위IO명
			layout.setFillerVal(ExcelUtils.readValue(sheet, index, "K"));// Filler
/*
			if (!isValid) {
				throw new ServiceException(BxMessages.Error.MSG_VALID_CHECK, errMsg.toString());
			}
*/
			layoutList.add(layout);
		}
		data.setMsglayoutdtDto(layoutList);
		return index;
	}

//	private int readDataGrid(Sheet sheet, MsglayoutbsDto data, int startIndex, String layoutId, boolean isValid,
//			StringBuilder errMsg) {
//		List<MsglayoutdtDto> layoutList = new ArrayList<>();
//
//		int index = startIndex;
//
//		String layoutVersion = ExcelUtils.readValue(sheet, 5, "W");
//
//		List<String> parentsList = new ArrayList<String>();
//		parentsList.add(layoutId);
//
//		Map<String, String> ioKeyMap = new HashMap<String, String>();
//
//		int seq = 1;
//		for (; index < sheet.getPhysicalNumberOfRows(); index++) {
//			if (ExcelUtils.readValue(sheet, index, "B").equals("계")) {
//				break;
//			}
//			MsglayoutdtDto layout = new MsglayoutdtDto();
//			if (layoutVersion.equals("v20180802")) {
//				layout.setMsgLayoutId(layoutId);
//				layout.setMsgSeq(seq++);
//				isValid = nullCheck(errMsg, ExcelUtils.readValue(sheet, index, "C"), "\n영문명", isValid);
//				layout.setFldEngNm(ExcelUtils.readValue(sheet, index, "C"));
//				isValid = nullCheck(errMsg, ExcelUtils.readValue(sheet, index, "D"), "\n한글명", isValid);
//				layout.setFldKorNm(ExcelUtils.readValue(sheet, index, "D"));
//				String type = getCodeValueNm("DATA_TYPE", ExcelUtils.readValue(sheet, index, "F"), "ko");
//				isValid = nullCheck(errMsg, type, "\n데이터타입", isValid);
//				layout.setDataTypeNm(type);
//				layout.setMsgLen(Integer.parseInt(ExcelUtils.readValue(sheet, index, "G")));
//				layout.setDecimalLen(Integer.parseInt(ExcelUtils.readValue(sheet, index, "H")));
//				layout.setFldLvNo(Integer.parseInt(ExcelUtils.readValue(sheet, index, "I")));
//				layout.setChildDtoNm(ExcelUtils.readValue(sheet, index, "J"));
//				layout.setArraySizeRefVal(ExcelUtils.readValue(sheet, index, "K"));
//				layout.setBasicVal(ExcelUtils.readValue(sheet, index, "L"));
//				layout.setPrivacyDscd(getCodeValueNm("PRIVACY_CD", ExcelUtils.readValue(sheet, index, "M"), "ko"));
//				layout.setEncYn(ExcelUtils.readValue(sheet, index, "N"));
//				String align = ExcelUtils.readValue(sheet, index, "O");
//				if (StringUtils.isEmpty(align)) {
//					if (type.equals("STRING") || type.equals("BYTEARRAY")) {
//						align = "LEFT";
//					} else if (type.equals("LONG") || type.equals("BIGDECIMAL")) {
//						align = "RIGHT";
//					} else {
//						align = "";
//					}
//				}
//				layout.setAlignNm(align);
//				layout.setCdAttrNm(ExcelUtils.readValue(sheet, index, "P"));
//				String ioKey = getCodeValueNm("IO_KEY", ExcelUtils.readValue(sheet, index, "R"), "ko");
//				if (ioKeyMap.containsKey(ioKey)) {
//					errMsg.append("\n전문에 같은 IO Key가 존재할 수 없습니다.");
//					isValid = false;
//				}
//				layout.setIoKey(ioKey);// IO키
//				if (ioKey != null && !ioKey.equals("")) {
//					ioKeyMap.put(ioKey, ioKey);
//				}
//				layout.setFillerVal(ExcelUtils.readValue(sheet, index, "S"));
//				layout.setFldRmk(ExcelUtils.readValue(sheet, index, "T"));// 비고
//				layout.setExtrnlMsgNoYn(ExcelUtils.readValue(sheet, index, "V"));
//				layout.setExtrnlSrchKeyYn(ExcelUtils.readValue(sheet, index, "W"));
//				layout.setExtrnlMsgNoYn(ExcelUtils.readValue(sheet, index, "Z"));
//				// layout.setExtrnlOffsetYn(ExcelUtils.readValue(sheet, index, "W"));
//				layout.setIso8583SpecNo(ExcelUtils.readValue(sheet, index, "AA"));
//				layout.setIso8583FldCharSetCd(ExcelUtils.readValue(sheet, index, "AB"));// 필드타입
//				layout.setIso8583VariableYn(ExcelUtils.readValue(sheet, index, "AC"));
//				layout.setIso8583FldLenTypeCd(
//						getCodeValueNm("ISO_FLD_LEN_TYPE_CD", ExcelUtils.readValue(sheet, index, "AD"), "ko"));
//				layout.setIso8583LenFldInclYn(ExcelUtils.readValue(sheet, index, "AE"));
//				layout.setIso8583LenFldCopyYn(ExcelUtils.readValue(sheet, index, "AF"));
//				layout.setIso8583TrnsmsnDataLen(ExcelUtils.readValue(sheet, index, "AG") != null
//						&& !ExcelUtils.readValue(sheet, index, "AG").equals("")
//								? Integer.parseInt(ExcelUtils.readValue(sheet, index, "AG"))
//								: null);
//			} else if (layoutVersion.equals("v20180829")) {
//				layout.setMsgLayoutId(layoutId);
//				layout.setMsgSeq(seq++);
//				isValid = nullCheck(errMsg, ExcelUtils.readValue(sheet, index, "C"), "\n영문명", isValid);
//				layout.setFldEngNm(ExcelUtils.readValue(sheet, index, "C"));
//				isValid = nullCheck(errMsg, ExcelUtils.readValue(sheet, index, "D"), "\n한글명", isValid);
//				layout.setFldKorNm(ExcelUtils.readValue(sheet, index, "D"));
//				String type = getCodeValueNm("DATA_TYPE", ExcelUtils.readValue(sheet, index, "F"), "ko");
//				isValid = nullCheck(errMsg, type, "\n데이터타입", isValid);
//				layout.setDataTypeNm(type);
//				layout.setCfgDesc(ExcelUtils.readValue(sheet, index, "E"));
//				layout.setMsgLen(Integer.parseInt(ExcelUtils.readValue(sheet, index, "G")));
//				layout.setDecimalLen(Integer.parseInt(ExcelUtils.readValue(sheet, index, "H")));
//				layout.setFldLvNo(Integer.parseInt(ExcelUtils.readValue(sheet, index, "I")));
//				layout.setChildDtoNm(ExcelUtils.readValue(sheet, index, "J"));
//				layout.setArraySizeRefVal(ExcelUtils.readValue(sheet, index, "K"));
//				layout.setIndsYn(ExcelUtils.readValue(sheet, index, "L"));
//				layout.setBasicVal(ExcelUtils.readValue(sheet, index, "M"));
//				layout.setPrivacyDscd(getCodeValueNm("PRIVACY_CD", ExcelUtils.readValue(sheet, index, "N"), "ko"));
//				layout.setEncYn(ExcelUtils.readValue(sheet, index, "O"));
//				String align = ExcelUtils.readValue(sheet, index, "P");
//				if (StringUtils.isEmpty(align)) {
//					if (type.equals("STRING") || type.equals("BYTEARRAY")) {
//						align = "LEFT";
//					} else if (type.equals("LONG") || type.equals("BIGDECIMAL")) {
//						align = "RIGHT";
//					} else {
//						align = "";
//					}
//				}
//				layout.setAlignNm(align);
//				layout.setCdAttrNm(ExcelUtils.readValue(sheet, index, "Q"));
//				String ioKey = getCodeValueNm("IO_KEY", ExcelUtils.readValue(sheet, index, "R"), "ko");
//				if (ioKeyMap.containsKey(ioKey)) {
//					errMsg.append("\n전문에 같은 IO Key가 존재할 수 없습니다.");
//					isValid = false;
//				}
//				layout.setIoKey(ioKey);// IO키
//				if (ioKey != null && !ioKey.equals("")) {
//					ioKeyMap.put(ioKey, ioKey);
//				}
//				layout.setFillerVal(ExcelUtils.readValue(sheet, index, "S"));
//				layout.setFldRmk(ExcelUtils.readValue(sheet, index, "T"));// 비고
//				layout.setExtrnlMsgNoYn(ExcelUtils.readValue(sheet, index, "V"));
//				layout.setExtrnlSrchKeyYn(ExcelUtils.readValue(sheet, index, "W"));
//				layout.setExtrnlMsgNoYn(ExcelUtils.readValue(sheet, index, "Z"));
//				// layout.setExtrnlOffsetYn(ExcelUtils.readValue(sheet, index, "W"));
//				layout.setIso8583SpecNo(ExcelUtils.readValue(sheet, index, "AA"));
//				layout.setIso8583FldCharSetCd(ExcelUtils.readValue(sheet, index, "AB"));// 필드타입
//				layout.setIso8583VariableYn(ExcelUtils.readValue(sheet, index, "AC"));
//				layout.setIso8583FldLenTypeCd(
//						getCodeValueNm("ISO_FLD_LEN_TYPE_CD", ExcelUtils.readValue(sheet, index, "AD"), "ko"));
//				layout.setIso8583LenFldInclYn(ExcelUtils.readValue(sheet, index, "AE"));
//				layout.setIso8583LenFldCopyYn(ExcelUtils.readValue(sheet, index, "AF"));
//				layout.setIso8583TrnsmsnDataLen(ExcelUtils.readValue(sheet, index, "AG") != null
//						&& !ExcelUtils.readValue(sheet, index, "AG").equals("")
//								? Integer.parseInt(ExcelUtils.readValue(sheet, index, "AG"))
//								: null);
//			} else if (layoutVersion.equals("v20180910")) {
//				layout.setMsgLayoutId(layoutId);
//				layout.setMsgSeq(seq++);
//				isValid = nullCheck(errMsg, ExcelUtils.readValue(sheet, index, "C"), "\n영문명", isValid);
//				layout.setFldEngNm(ExcelUtils.readValue(sheet, index, "C"));
//				isValid = nullCheck(errMsg, ExcelUtils.readValue(sheet, index, "D"), "\n한글명", isValid);
//				layout.setFldKorNm(ExcelUtils.readValue(sheet, index, "D"));
//				String type = getCodeValueNm("DATA_TYPE", ExcelUtils.readValue(sheet, index, "F"), "ko");
//				isValid = nullCheck(errMsg, type, "\n데이터타입", isValid);
//				layout.setDataTypeNm(type);
//				layout.setCfgDesc(ExcelUtils.readValue(sheet, index, "E"));
//				layout.setMsgLen(Integer.parseInt(ExcelUtils.readValue(sheet, index, "G")));
//				layout.setDecimalLen(Integer.parseInt(ExcelUtils.readValue(sheet, index, "H")));
//				layout.setFldLvNo(Integer.parseInt(ExcelUtils.readValue(sheet, index, "I")));
//				layout.setChildDtoNm(ExcelUtils.readValue(sheet, index, "J"));
//				layout.setArraySizeRefVal(ExcelUtils.readValue(sheet, index, "K"));
//				layout.setIndsYn(ExcelUtils.readValue(sheet, index, "L"));
//				layout.setBasicVal(ExcelUtils.readValue(sheet, index, "M"));
//				layout.setPrivacyDscd(getCodeValueNm("PRIVACY_CD", ExcelUtils.readValue(sheet, index, "N"), "ko"));
//				layout.setEncYn(ExcelUtils.readValue(sheet, index, "O"));
//				String align = ExcelUtils.readValue(sheet, index, "P");
//				if (StringUtils.isEmpty(align)) {
//					if (type.equals("STRING") || type.equals("BYTEARRAY")) {
//						align = "LEFT";
//					} else if (type.equals("LONG") || type.equals("BIGDECIMAL")) {
//						align = "RIGHT";
//					} else {
//						align = "";
//					}
//				}
//				layout.setAlignNm(align);
//				layout.setCdAttrNm(ExcelUtils.readValue(sheet, index, "Q"));
//				String ioKey = getCodeValueNm("IO_KEY", ExcelUtils.readValue(sheet, index, "R"), "ko");
//				if (ioKeyMap.containsKey(ioKey)) {
//					errMsg.append("\n전문에 같은 IO Key가 존재할 수 없습니다.");
//					isValid = false;
//				}
//				layout.setIoKey(ioKey);// IO키
//				if (ioKey != null && !ioKey.equals("")) {
//					ioKeyMap.put(ioKey, ioKey);
//				}
//				layout.setFillerVal(ExcelUtils.readValue(sheet, index, "S"));
//				layout.setFldRmk(ExcelUtils.readValue(sheet, index, "T"));// 비고
//				layout.setExtrnlMsgNoYn(ExcelUtils.readValue(sheet, index, "V"));
//				layout.setExtrnlSrchKeyYn(ExcelUtils.readValue(sheet, index, "W"));
//				layout.setExtrnlMsgNoYn(ExcelUtils.readValue(sheet, index, "Z"));
//				// layout.setExtrnlOffsetYn(ExcelUtils.readValue(sheet, index, "W"));
//				layout.setIso8583SpecNo(ExcelUtils.readValue(sheet, index, "AA"));
//				layout.setIso8583FldCharSetCd(ExcelUtils.readValue(sheet, index, "AB"));// 필드타입
//				layout.setIso8583VariableYn(ExcelUtils.readValue(sheet, index, "AC"));
//				layout.setIso8583FldLenTypeCd(
//						getCodeValueNm("ISO_FLD_LEN_TYPE_CD", ExcelUtils.readValue(sheet, index, "AD"), "ko"));
//				layout.setIso8583LenFldLen(ExcelUtils.readValue(sheet, index, "AE") != null
//						&& !ExcelUtils.readValue(sheet, index, "AE").equals("")
//								? Integer.parseInt(ExcelUtils.readValue(sheet, index, "AE"))
//								: null);
//				layout.setIso8583LenFldInclYn(ExcelUtils.readValue(sheet, index, "AF"));
//				layout.setIso8583LenFldCopyYn(ExcelUtils.readValue(sheet, index, "AG"));
//				layout.setIso8583TrnsmsnDataLen(ExcelUtils.readValue(sheet, index, "AH") != null
//						&& !ExcelUtils.readValue(sheet, index, "AH").equals("")
//								? Integer.parseInt(ExcelUtils.readValue(sheet, index, "AH"))
//								: null);
//			} else if (layoutVersion.equals("v20181011") || layoutVersion.equals("v20181016")) {
//				layout.setMsgLayoutId(layoutId);
//				layout.setMsgSeq(seq++);
//				isValid = nullCheck(errMsg, ExcelUtils.readValue(sheet, index, "C"), "\n영문명", isValid);
//				layout.setFldEngNm(ExcelUtils.readValue(sheet, index, "C"));
//				isValid = nullCheck(errMsg, ExcelUtils.readValue(sheet, index, "D"), "\n한글명", isValid);
//				layout.setFldKorNm(ExcelUtils.readValue(sheet, index, "D"));
//				String type = getCodeValueNm("DATA_TYPE", ExcelUtils.readValue(sheet, index, "F"), "ko");
//				isValid = nullCheck(errMsg, type, "\n데이터타입", isValid);
//				layout.setDataTypeNm(type);
//				layout.setCfgDesc(ExcelUtils.readValue(sheet, index, "E"));
//				layout.setMsgLen(Integer.parseInt(ExcelUtils.readValue(sheet, index, "G")));
//				layout.setDecimalLen(Integer.parseInt(ExcelUtils.readValue(sheet, index, "H")));
//				layout.setFldLvNo(Integer.parseInt(ExcelUtils.readValue(sheet, index, "I")));
//				layout.setChildDtoNm(ExcelUtils.readValue(sheet, index, "J"));
//				layout.setArraySizeRefVal(ExcelUtils.readValue(sheet, index, "K"));
//				layout.setIndsYn(ExcelUtils.readValue(sheet, index, "L"));
//				layout.setBasicVal(ExcelUtils.readValue(sheet, index, "M"));
//				layout.setPrivacyDscd(getCodeValueNm("PRIVACY_CD", ExcelUtils.readValue(sheet, index, "N"), "ko"));
//				String chlType = data.getChlDscd();
//				String replKey = ExcelUtils.readValue(sheet, index, "O");
//				if (replKey != null && !replKey.equals("")) {
//					if (chlType.equals("EXTERNAL")) {
//						if (replKey.startsWith("(대내)")) {
//							errMsg.append("\n대외전문에 대내 개인정보식별자를 세팅할 수 없습니다.");
//							isValid = false;
//						} else {
//							layout.setReplKey(
//									getCodeValueNm("REPLACEKEY_EXT", ExcelUtils.readValue(sheet, index, "O"), "ko"));
//						}
//					} else {
//						if (replKey.startsWith("(대외)")) {
//							errMsg.append("\n대내전문에 대외 개인정보식별자를 세팅할 수 없습니다.");
//							isValid = false;
//						} else {
//							layout.setReplKey(
//									getCodeValueNm("REPLACEKEY_INT", ExcelUtils.readValue(sheet, index, "O"), "ko"));
//						}
//					}
//				}
//				layout.setEncYn(ExcelUtils.readValue(sheet, index, "P"));
//				String align = ExcelUtils.readValue(sheet, index, "Q");
//				if (StringUtils.isEmpty(align)) {
//					if (type.equals("STRING") || type.equals("BYTEARRAY")) {
//						align = "LEFT";
//					} else if (type.equals("LONG") || type.equals("BIGDECIMAL")) {
//						align = "RIGHT";
//					} else {
//						align = "";
//					}
//				}
//				layout.setAlignNm(align);
//				layout.setCdAttrNm(ExcelUtils.readValue(sheet, index, "R"));
//				String ioKey = getCodeValueNm("IO_KEY", ExcelUtils.readValue(sheet, index, "S"), "ko");
//				if (ioKeyMap.containsKey(ioKey)) {
//					errMsg.append("\n전문에 같은 IO Key가 존재할 수 없습니다.");
//					isValid = false;
//				}
//				layout.setIoKey(ioKey);// IO키
//				if (ioKey != null && !ioKey.equals("")) {
//					ioKeyMap.put(ioKey, ioKey);
//				}
//				layout.setFillerVal(ExcelUtils.readValue(sheet, index, "T"));
//				layout.setFldRmk(ExcelUtils.readValue(sheet, index, "U"));// 비고
//				layout.setExtrnlMsgNoYn(ExcelUtils.readValue(sheet, index, "W"));
//				layout.setExtrnlSrchKeyYn(ExcelUtils.readValue(sheet, index, "X"));
//				layout.setExtrnlMsgNoYn(ExcelUtils.readValue(sheet, index, "AA"));
//				// layout.setExtrnlOffsetYn(ExcelUtils.readValue(sheet, index, "W"));
//				layout.setIso8583SpecNo(ExcelUtils.readValue(sheet, index, "AB"));
//				layout.setIso8583FldCharSetCd(ExcelUtils.readValue(sheet, index, "AC"));// 필드타입
//				layout.setIso8583VariableYn(ExcelUtils.readValue(sheet, index, "AD"));
//				layout.setIso8583FldLenTypeCd(
//						getCodeValueNm("ISO_FLD_LEN_TYPE_CD", ExcelUtils.readValue(sheet, index, "AE"), "ko"));
//				layout.setIso8583LenFldLen(ExcelUtils.readValue(sheet, index, "AF") != null
//						&& !ExcelUtils.readValue(sheet, index, "AF").equals("")
//								? Integer.parseInt(ExcelUtils.readValue(sheet, index, "AF"))
//								: null);
//				layout.setIso8583LenFldInclYn(ExcelUtils.readValue(sheet, index, "AG"));
//				layout.setIso8583LenFldCopyYn(ExcelUtils.readValue(sheet, index, "AH"));
//				layout.setIso8583TrnsmsnDataLen(ExcelUtils.readValue(sheet, index, "AI") != null
//						&& !ExcelUtils.readValue(sheet, index, "AI").equals("")
//								? Integer.parseInt(ExcelUtils.readValue(sheet, index, "AI"))
//								: null);
//			} else if (layoutVersion.equals("v20181112")) {
//				layout.setMsgLayoutId(layoutId);
//				layout.setMsgSeq(seq++);
//				isValid = nullCheck(errMsg, ExcelUtils.readValue(sheet, index, "C"), "\n영문명", isValid);
//				layout.setFldEngNm(ExcelUtils.readValue(sheet, index, "C"));
//				isValid = nullCheck(errMsg, ExcelUtils.readValue(sheet, index, "D"), "\n한글명", isValid);
//				layout.setFldKorNm(ExcelUtils.readValue(sheet, index, "D"));
//				String type = getCodeValueNm("DATA_TYPE", ExcelUtils.readValue(sheet, index, "F"), "ko");
//				isValid = nullCheck(errMsg, type, "\n데이터타입", isValid);
//				layout.setDataTypeNm(type);
//				layout.setCfgDesc(ExcelUtils.readValue(sheet, index, "E"));
//				layout.setMsgLen(Integer.parseInt(ExcelUtils.readValue(sheet, index, "G")));
//				layout.setDecimalLen(Integer.parseInt(ExcelUtils.readValue(sheet, index, "H")));
//				layout.setFldLvNo(Integer.parseInt(ExcelUtils.readValue(sheet, index, "I")));
//				layout.setChildDtoNm(ExcelUtils.readValue(sheet, index, "J"));
//				layout.setArraySizeRefVal(ExcelUtils.readValue(sheet, index, "K"));
//				layout.setIndsYn(ExcelUtils.readValue(sheet, index, "L"));
//				layout.setBasicVal(ExcelUtils.readValue(sheet, index, "M"));
//				layout.setPrivacyDscd(getCodeValueNm("PRIVACY_CD", ExcelUtils.readValue(sheet, index, "N"), "ko"));
//				String chlType = data.getChlDscd();
//				String replKey = ExcelUtils.readValue(sheet, index, "O");
//				if (replKey != null && !replKey.equals("")) {
//					if (chlType.equals("EXTERNAL")) {
//						if (replKey.startsWith("(대내)")) {
//							errMsg.append("\n대외전문에 대내 개인정보식별자를 세팅할 수 없습니다.");
//							isValid = false;
//						} else {
//							layout.setReplKey(
//									getCodeValueNm("REPLACEKEY_EXT", ExcelUtils.readValue(sheet, index, "O"), "ko"));
//						}
//					} else {
//						if (replKey.startsWith("(대외)")) {
//							errMsg.append("\n대내전문에 대외 개인정보식별자를 세팅할 수 없습니다.");
//							isValid = false;
//						} else {
//							layout.setReplKey(
//									getCodeValueNm("REPLACEKEY_INT", ExcelUtils.readValue(sheet, index, "O"), "ko"));
//						}
//					}
//				}
//				layout.setEncYn(ExcelUtils.readValue(sheet, index, "P"));
//				String align = ExcelUtils.readValue(sheet, index, "Q");
//				if (StringUtils.isEmpty(align)) {
//					if (type.equals("STRING") || type.equals("BYTEARRAY")) {
//						align = "LEFT";
//					} else if (type.equals("LONG") || type.equals("BIGDECIMAL")) {
//						align = "RIGHT";
//					} else {
//						align = "";
//					}
//				}
//				layout.setAlignNm(align);
//				layout.setCdAttrNm(ExcelUtils.readValue(sheet, index, "R"));
//				String ioKey = getCodeValueNm("IO_KEY", ExcelUtils.readValue(sheet, index, "S"), "ko");
//				if (ioKeyMap.containsKey(ioKey)) {
//					errMsg.append("\n전문에 같은 IO Key가 존재할 수 없습니다.");
//					isValid = false;
//				}
//				layout.setIoKey(ioKey);// IO키
//				if (ioKey != null && !ioKey.equals("")) {
//					ioKeyMap.put(ioKey, ioKey);
//				}
//				// layout.setFillerVal(ExcelUtils.readValue(sheet, index, "T"));
//				layout.setFldRmk(ExcelUtils.readValue(sheet, index, "U"));// 비고
//				layout.setExtrnlMsgNoYn(ExcelUtils.readValue(sheet, index, "W"));
//				layout.setExtrnlSrchKeyYn(ExcelUtils.readValue(sheet, index, "X"));
//				layout.setExtrnlMsgNoYn(ExcelUtils.readValue(sheet, index, "AA"));
//				// layout.setExtrnlOffsetYn(ExcelUtils.readValue(sheet, index, "W"));
//				layout.setIso8583SpecNo(ExcelUtils.readValue(sheet, index, "AB"));
//				layout.setIso8583FldCharSetCd(ExcelUtils.readValue(sheet, index, "AC"));// 필드타입
//				layout.setIso8583VariableYn(ExcelUtils.readValue(sheet, index, "AD"));
//				layout.setIso8583FldLenTypeCd(
//						getCodeValueNm("ISO_FLD_LEN_TYPE_CD", ExcelUtils.readValue(sheet, index, "AE"), "ko"));
//				layout.setIso8583LenFldLen(ExcelUtils.readValue(sheet, index, "AF") != null
//						&& !ExcelUtils.readValue(sheet, index, "AF").equals("")
//								? Integer.parseInt(ExcelUtils.readValue(sheet, index, "AF"))
//								: null);
//				layout.setIso8583LenFldInclYn(ExcelUtils.readValue(sheet, index, "AG"));
//				layout.setIso8583LenFldCopyYn(ExcelUtils.readValue(sheet, index, "AH"));
//				layout.setIso8583TrnsmsnDataLen(ExcelUtils.readValue(sheet, index, "AI") != null
//						&& !ExcelUtils.readValue(sheet, index, "AI").equals("")
//								? Integer.parseInt(ExcelUtils.readValue(sheet, index, "AI"))
//								: null);
//			} else if (layoutVersion.equals("v20181114")) {
//				layout.setMsgLayoutId(layoutId);
//				layout.setMsgSeq(seq++);
//				isValid = nullCheck(errMsg, ExcelUtils.readValue(sheet, index, "C"), "\n영문명", isValid);
//				layout.setFldEngNm(ExcelUtils.readValue(sheet, index, "C"));
//				isValid = nullCheck(errMsg, ExcelUtils.readValue(sheet, index, "D"), "\n한글명", isValid);
//				layout.setFldKorNm(ExcelUtils.readValue(sheet, index, "D"));
//				String type = getCodeValueNm("DATA_TYPE", ExcelUtils.readValue(sheet, index, "F"), "ko");
//				isValid = nullCheck(errMsg, type, "\n데이터타입", isValid);
//				layout.setDataTypeNm(type);
//				layout.setCfgDesc(ExcelUtils.readValue(sheet, index, "E"));
//				layout.setMsgLen(Integer.parseInt(ExcelUtils.readValue(sheet, index, "G")));
//				layout.setDecimalLen(Integer.parseInt(ExcelUtils.readValue(sheet, index, "H")));
//				layout.setFldLvNo(Integer.parseInt(ExcelUtils.readValue(sheet, index, "I")));
//				layout.setChildDtoNm(ExcelUtils.readValue(sheet, index, "J"));
//				layout.setArraySizeRefVal(ExcelUtils.readValue(sheet, index, "K"));
//				layout.setIndsYn(ExcelUtils.readValue(sheet, index, "L"));
//				layout.setBasicVal(ExcelUtils.readValue(sheet, index, "M"));
//				layout.setPrivacyDscd(getCodeValueNm("PRIVACY_CD", ExcelUtils.readValue(sheet, index, "N"), "ko"));
//				String chlType = data.getChlDscd();
//				String replKey = ExcelUtils.readValue(sheet, index, "O");
//				if (replKey != null && !replKey.equals("")) {
//					if (chlType.equals("EXTERNAL")) {
//						if (replKey.startsWith("(대내)")) {
//							errMsg.append("\n대외전문에 대내 개인정보식별자를 세팅할 수 없습니다.");
//							isValid = false;
//						} else {
//							layout.setReplKey(
//									getCodeValueNm("REPLACEKEY_EXT", ExcelUtils.readValue(sheet, index, "O"), "ko"));
//						}
//					} else {
//						if (replKey.startsWith("(대외)")) {
//							errMsg.append("\n대내전문에 대외 개인정보식별자를 세팅할 수 없습니다.");
//							isValid = false;
//						} else {
//							layout.setReplKey(
//									getCodeValueNm("REPLACEKEY_INT", ExcelUtils.readValue(sheet, index, "O"), "ko"));
//						}
//					}
//				}
//				layout.setEncYn(ExcelUtils.readValue(sheet, index, "P"));
//				String align = ExcelUtils.readValue(sheet, index, "Q");
//				if (StringUtils.isEmpty(align)) {
//					if (type.equals("STRING") || type.equals("BYTEARRAY")) {
//						align = "LEFT";
//					} else if (type.equals("LONG") || type.equals("BIGDECIMAL")) {
//						align = "RIGHT";
//					} else {
//						align = "";
//					}
//				}
//				layout.setAlignNm(align);
//				layout.setCdAttrNm(ExcelUtils.readValue(sheet, index, "R"));
//				String ioKey = getCodeValueNm("IO_KEY", ExcelUtils.readValue(sheet, index, "S"), "ko");
//				if (ioKeyMap.containsKey(ioKey)) {
//					errMsg.append("\n전문에 같은 IO Key가 존재할 수 없습니다.");
//					isValid = false;
//				}
//				layout.setIoKey(ioKey);// IO키
//				if (ioKey != null && !ioKey.equals("")) {
//					ioKeyMap.put(ioKey, ioKey);
//				}
//				String korInclYn = ExcelUtils.readValue(sheet, index, "T");
//				if (korInclYn == null || korInclYn.equals("")) {
//					korInclYn = "N";
//				}
//				layout.setKorInclYn(korInclYn);
//				// layout.setFillerVal(ExcelUtils.readValue(sheet, index, "T"));
//				layout.setFldRmk(ExcelUtils.readValue(sheet, index, "U"));// 비고
//				layout.setExtrnlMsgNoYn(ExcelUtils.readValue(sheet, index, "W"));
//				layout.setExtrnlSrchKeyYn(ExcelUtils.readValue(sheet, index, "X"));
//				layout.setExtrnlMsgNoYn(ExcelUtils.readValue(sheet, index, "AA"));
//				// layout.setExtrnlOffsetYn(ExcelUtils.readValue(sheet, index, "W"));
//				layout.setIso8583SpecNo(ExcelUtils.readValue(sheet, index, "AB"));
//				layout.setIso8583FldCharSetCd(ExcelUtils.readValue(sheet, index, "AC"));// 필드타입
//				layout.setIso8583VariableYn(ExcelUtils.readValue(sheet, index, "AD"));
//				layout.setIso8583FldLenTypeCd(
//						getCodeValueNm("ISO_FLD_LEN_TYPE_CD", ExcelUtils.readValue(sheet, index, "AE"), "ko"));
//				layout.setIso8583LenFldLen(ExcelUtils.readValue(sheet, index, "AF") != null
//						&& !ExcelUtils.readValue(sheet, index, "AF").equals("")
//								? Integer.parseInt(ExcelUtils.readValue(sheet, index, "AF"))
//								: null);
//				layout.setIso8583LenFldInclYn(ExcelUtils.readValue(sheet, index, "AG"));
//				layout.setIso8583LenFldCopyYn(ExcelUtils.readValue(sheet, index, "AH"));
//				layout.setIso8583TrnsmsnDataLen(ExcelUtils.readValue(sheet, index, "AI") != null
//						&& !ExcelUtils.readValue(sheet, index, "AI").equals("")
//								? Integer.parseInt(ExcelUtils.readValue(sheet, index, "AI"))
//								: null);
//			} else if (layoutVersion.equals("v20181130")) {
//				layout.setMsgLayoutId(layoutId);
//				layout.setMsgSeq(seq++);
//				isValid = nullCheck(errMsg, ExcelUtils.readValue(sheet, index, "C"), "\n영문명", isValid);
//				layout.setFldEngNm(ExcelUtils.readValue(sheet, index, "C"));
//				isValid = nullCheck(errMsg, ExcelUtils.readValue(sheet, index, "D"), "\n한글명", isValid);
//				layout.setFldKorNm(ExcelUtils.readValue(sheet, index, "D"));
//				String type = getCodeValueNm("DATA_TYPE", ExcelUtils.readValue(sheet, index, "F"), "ko");
//				isValid = nullCheck(errMsg, type, "\n데이터타입", isValid);
//				layout.setDataTypeNm(type);
//				layout.setCfgDesc(ExcelUtils.readValue(sheet, index, "E"));
//				layout.setMsgLen(Integer.parseInt(ExcelUtils.readValue(sheet, index, "G")));
//				layout.setDecimalLen(Integer.parseInt(ExcelUtils.readValue(sheet, index, "H")));
//				layout.setFldLvNo(Integer.parseInt(ExcelUtils.readValue(sheet, index, "I")));
//				layout.setIndsYn(ExcelUtils.readValue(sheet, index, "J"));// 필수여부J
//				layout.setFldRmk(ExcelUtils.readValue(sheet, index, "K"));// 비고K
//				layout.setCdAttrNm(ExcelUtils.readValue(sheet, index, "L"));// 코드속성L
//				layout.setArraySizeRefVal(ExcelUtils.readValue(sheet, index, "M"));// 배열참조M
//				layout.setPrivacyDscd(getCodeValueNm("PRIVACY_CD", ExcelUtils.readValue(sheet, index, "N"), "ko"));// 개인정보N
//				String replKey = ExcelUtils.readValue(sheet, index, "O");
//				String chlType = data.getChlDscd();
//				if (replKey != null && !replKey.equals("")) {
//					if (chlType.equals("EXTERNAL")) {
//						if (replKey.startsWith("(대내)")) {
//							errMsg.append("\n대외전문에 대내 개인정보식별자를 세팅할 수 없습니다.");
//							isValid = false;
//						} else {
//							layout.setReplKey(
//									getCodeValueNm("REPLACEKEY_EXT", ExcelUtils.readValue(sheet, index, "O"), "ko"));
//						}
//					} else {
//						if (replKey.startsWith("(대외)")) {
//							errMsg.append("\n대내전문에 대외 개인정보식별자를 세팅할 수 없습니다.");
//							isValid = false;
//						} else {
//							layout.setReplKey(
//									getCodeValueNm("REPLACEKEY_INT", ExcelUtils.readValue(sheet, index, "O"), "ko"));
//						}
//					}
//				} // 개인정보식별자O
//				layout.setEncYn(ExcelUtils.readValue(sheet, index, "P"));// 암호화여부P
//				String align = ExcelUtils.readValue(sheet, index, "Q");
//				if (StringUtils.isEmpty(align)) {
//					if (type.equals("STRING") || type.equals("BYTEARRAY")) {
//						align = "LEFT";
//					} else if (type.equals("LONG") || type.equals("BIGDECIMAL")) {
//						align = "RIGHT";
//					} else {
//						align = "";
//					}
//				}
//				layout.setAlignNm(align);// 정렬Q
//				layout.setBasicVal(ExcelUtils.readValue(sheet, index, "R"));// 기본값R
//				String korInclYn = ExcelUtils.readValue(sheet, index, "S");
//				if (korInclYn == null || korInclYn.equals("")) {
//					korInclYn = "N";
//				}
//				layout.setKorInclYn(korInclYn);// 한글포함여부S
//				String ioKey = getCodeValueNm("IO_KEY", ExcelUtils.readValue(sheet, index, "T"), "ko");
//				if (ioKeyMap.containsKey(ioKey)) {
//					errMsg.append("\n전문에 같은 IO Key가 존재할 수 없습니다.");
//					isValid = false;
//				}
//				layout.setIoKey(ioKey);// IO키
//				if (ioKey != null && !ioKey.equals("")) {
//					ioKeyMap.put(ioKey, ioKey);
//				} // IO키T
//				layout.setChildDtoNm(ExcelUtils.readValue(sheet, index, "U"));// 하위IO명U
//
//				layout.setExtrnlMsgNoYn(ExcelUtils.readValue(sheet, index, "W"));
//				layout.setExtrnlSrchKeyYn(ExcelUtils.readValue(sheet, index, "X"));
//				layout.setExtrnlMsgNoYn(ExcelUtils.readValue(sheet, index, "AA"));
//				// layout.setExtrnlOffsetYn(ExcelUtils.readValue(sheet, index, "W"));
//				layout.setIso8583SpecNo(ExcelUtils.readValue(sheet, index, "AB"));
//				layout.setIso8583FldCharSetCd(ExcelUtils.readValue(sheet, index, "AC"));// 필드타입
//				layout.setIso8583VariableYn(ExcelUtils.readValue(sheet, index, "AD"));
//				layout.setIso8583FldLenTypeCd(
//						getCodeValueNm("ISO_FLD_LEN_TYPE_CD", ExcelUtils.readValue(sheet, index, "AE"), "ko"));
//				layout.setIso8583LenFldLen(ExcelUtils.readValue(sheet, index, "AF") != null
//						&& !ExcelUtils.readValue(sheet, index, "AF").equals("")
//								? Integer.parseInt(ExcelUtils.readValue(sheet, index, "AF"))
//								: null);
//				layout.setIso8583LenFldInclYn(ExcelUtils.readValue(sheet, index, "AG"));
//				layout.setIso8583LenFldCopyYn(ExcelUtils.readValue(sheet, index, "AH"));
//				layout.setIso8583TrnsmsnDataLen(ExcelUtils.readValue(sheet, index, "AI") != null
//						&& !ExcelUtils.readValue(sheet, index, "AI").equals("")
//								? Integer.parseInt(ExcelUtils.readValue(sheet, index, "AI"))
//								: null);
//			} else if (layoutVersion.equals("v20181211")) {
//				layout.setMsgLayoutId(layoutId);
//				layout.setMsgSeq(seq++);
//				isValid = nullCheck(errMsg, ExcelUtils.readValue(sheet, index, "C"), "\n영문명", isValid);
//				layout.setFldEngNm(ExcelUtils.readValue(sheet, index, "C"));
//				isValid = nullCheck(errMsg, ExcelUtils.readValue(sheet, index, "D"), "\n한글명", isValid);
//				layout.setFldKorNm(ExcelUtils.readValue(sheet, index, "D"));
//				String type = getCodeValueNm("DATA_TYPE", ExcelUtils.readValue(sheet, index, "F"), "ko");
//				isValid = nullCheck(errMsg, type, "\n데이터타입", isValid);
//				layout.setDataTypeNm(type);
//				layout.setCfgDesc(ExcelUtils.readValue(sheet, index, "E"));
//				layout.setMsgLen(Integer.parseInt(ExcelUtils.readValue(sheet, index, "G")));
//				layout.setDecimalLen(Integer.parseInt(ExcelUtils.readValue(sheet, index, "H")));
//				layout.setFldLvNo(Integer.parseInt(ExcelUtils.readValue(sheet, index, "I")));
//				layout.setIndsYn(ExcelUtils.readValue(sheet, index, "J"));// 필수여부J
//				layout.setFldRmk(ExcelUtils.readValue(sheet, index, "K"));// 비고K
//				layout.setCdAttrNm(ExcelUtils.readValue(sheet, index, "L"));// 코드속성L
//				layout.setArraySizeRefVal(ExcelUtils.readValue(sheet, index, "M"));// 배열참조M
//				layout.setPrivacyDscd(getCodeValueNm("PRIVACY_CD", ExcelUtils.readValue(sheet, index, "N"), "ko"));// 개인정보N
//				String replKey = ExcelUtils.readValue(sheet, index, "O");
//				String chlType = data.getChlDscd();
//				if (replKey != null && !replKey.equals("")) {
//					if (chlType.equals("EXTERNAL")) {
//						if (replKey.equals("고객번호") || replKey.equals("대체카드번호") || replKey.equals("대체계좌번호")) {
//							errMsg.append("\n대외전문에 대내 개인정보식별자를 세팅할 수 없습니다. 고객번호, 대체카드번호, 대체계좌번호는 대내전문 개인정보식별자입니다.");
//							isValid = false;
//						} else {
//							layout.setReplKey(
//									getCodeValueNm("REPLACEKEY_EXT", ExcelUtils.readValue(sheet, index, "O"), "ko"));
//						}
//					} else {
//						layout.setReplKey(
//								getCodeValueNm("REPLACEKEY_INT", ExcelUtils.readValue(sheet, index, "O"), "ko"));
//					}
//				} // 개인정보식별자O
//				layout.setEncYn(ExcelUtils.readValue(sheet, index, "P"));// 암호화여부P
//				String align = ExcelUtils.readValue(sheet, index, "Q");
//				if (StringUtils.isEmpty(align)) {
//					if (type.equals("STRING") || type.equals("BYTEARRAY")) {
//						align = "LEFT";
//					} else if (type.equals("LONG") || type.equals("BIGDECIMAL")) {
//						align = "RIGHT";
//					} else {
//						align = "";
//					}
//				}
//				layout.setAlignNm(align);// 정렬Q
//				layout.setBasicVal(ExcelUtils.readValue(sheet, index, "R"));// 기본값R
//				String korInclYn = ExcelUtils.readValue(sheet, index, "S");
//				if (korInclYn == null || korInclYn.equals("")) {
//					korInclYn = "N";
//				}
//				layout.setKorInclYn(korInclYn);// 한글포함여부S
//				String ioKey = getCodeValueNm("IO_KEY", ExcelUtils.readValue(sheet, index, "T"), "ko");
//				if (ioKeyMap.containsKey(ioKey)) {
//					errMsg.append("\n전문에 같은 IO Key가 존재할 수 없습니다.");
//					isValid = false;
//				}
//				layout.setIoKey(ioKey);// IO키
//				if (ioKey != null && !ioKey.equals("")) {
//					ioKeyMap.put(ioKey, ioKey);
//				} // IO키T
//				layout.setChildDtoNm(ExcelUtils.readValue(sheet, index, "U"));// 하위IO명U
//
//				layout.setExtrnlMsgNoYn(ExcelUtils.readValue(sheet, index, "W"));
//				layout.setExtrnlSrchKeyYn(ExcelUtils.readValue(sheet, index, "X"));
//				layout.setExtrnlMsgNoYn(ExcelUtils.readValue(sheet, index, "AA"));
//				// layout.setExtrnlOffsetYn(ExcelUtils.readValue(sheet, index, "W"));
//				layout.setIso8583SpecNo(ExcelUtils.readValue(sheet, index, "AB"));
//				layout.setIso8583FldCharSetCd(ExcelUtils.readValue(sheet, index, "AC"));// 필드타입
//				layout.setIso8583VariableYn(ExcelUtils.readValue(sheet, index, "AD"));
//				layout.setIso8583FldLenTypeCd(
//						getCodeValueNm("ISO_FLD_LEN_TYPE_CD", ExcelUtils.readValue(sheet, index, "AE"), "ko"));
//				layout.setIso8583LenFldLen(ExcelUtils.readValue(sheet, index, "AF") != null
//						&& !ExcelUtils.readValue(sheet, index, "AF").equals("")
//								? Integer.parseInt(ExcelUtils.readValue(sheet, index, "AF"))
//								: null);
//				layout.setIso8583LenFldInclYn(ExcelUtils.readValue(sheet, index, "AG"));
//				layout.setIso8583LenFldCopyYn(ExcelUtils.readValue(sheet, index, "AH"));
//				layout.setIso8583FldMaxLen(ExcelUtils.readValue(sheet, index, "AI") != null
//						&& !ExcelUtils.readValue(sheet, index, "AI").equals("")
//								? Integer.parseInt(ExcelUtils.readValue(sheet, index, "AI"))
//								: null);
//				layout.setIso8583TrnsmsnDataLen(ExcelUtils.readValue(sheet, index, "AJ") != null
//						&& !ExcelUtils.readValue(sheet, index, "AJ").equals("")
//								? Integer.parseInt(ExcelUtils.readValue(sheet, index, "AJ"))
//								: null);
//			} else if (layoutVersion.equals("v20181226") || layoutVersion.equals("v20190109")) {
//				layout.setMsgLayoutId(layoutId);
//				layout.setMsgSeq(seq++);
//				isValid = nullCheck(errMsg, ExcelUtils.readValue(sheet, index, "C"), "\n영문명", isValid);
//				layout.setFldEngNm(ExcelUtils.readValue(sheet, index, "C"));
//				isValid = nullCheck(errMsg, ExcelUtils.readValue(sheet, index, "D"), "\n한글명", isValid);
//				layout.setFldKorNm(ExcelUtils.readValue(sheet, index, "D"));
//				String type = getCodeValueNm("DATA_TYPE", ExcelUtils.readValue(sheet, index, "F"), "ko");
//				isValid = nullCheck(errMsg, type, "\n데이터타입", isValid);
//				layout.setDataTypeNm(type);
//				layout.setCfgDesc(ExcelUtils.readValue(sheet, index, "E"));
//				layout.setMsgLen(Integer.parseInt(ExcelUtils.readValue(sheet, index, "G")));
//				layout.setDecimalLen(Integer.parseInt(ExcelUtils.readValue(sheet, index, "H")));
//				layout.setFldLvNo(Integer.parseInt(ExcelUtils.readValue(sheet, index, "I")));
//				layout.setIndsYn(ExcelUtils.readValue(sheet, index, "J"));// 필수여부J
//				layout.setFldRmk(ExcelUtils.readValue(sheet, index, "K"));// 비고K
//				layout.setCdAttrNm(ExcelUtils.readValue(sheet, index, "L"));// 코드속성L
//				layout.setArraySizeRefVal(ExcelUtils.readValue(sheet, index, "M"));// 배열참조M
//				layout.setPrivacyDscd(getCodeValueNm("PRIVACY_CD", ExcelUtils.readValue(sheet, index, "N"), "ko"));// 개인정보N
//				String replKey = ExcelUtils.readValue(sheet, index, "O");
//				String chlType = data.getChlDscd();
//				if (replKey != null && !replKey.equals("")) {
//					if (chlType.equals("EXTERNAL")) {
//						if (replKey.equals("고객번호") || replKey.equals("대체카드번호") || replKey.equals("대체계좌번호")) {
//							errMsg.append("\n대외전문에 대내 개인정보식별자를 세팅할 수 없습니다. 고객번호, 대체카드번호, 대체계좌번호는 대내전문 개인정보식별자입니다.");
//							isValid = false;
//						} else {
//							layout.setReplKey(
//									getCodeValueNm("REPLACEKEY_EXT", ExcelUtils.readValue(sheet, index, "O"), "ko"));
//						}
//					} else {
//						layout.setReplKey(
//								getCodeValueNm("REPLACEKEY_INT", ExcelUtils.readValue(sheet, index, "O"), "ko"));
//					}
//				} // 개인정보식별자O
//				layout.setEncYn(ExcelUtils.readValue(sheet, index, "P"));// 암호화여부P
//				String align = ExcelUtils.readValue(sheet, index, "Q");
//				if (StringUtils.isEmpty(align)) {
//					if (type.equals("STRING") || type.equals("BYTEARRAY")) {
//						align = "LEFT";
//					} else if (type.equals("LONG") || type.equals("BIGDECIMAL")) {
//						align = "RIGHT";
//					} else {
//						align = "";
//					}
//				}
//				layout.setAlignNm(align);// 정렬Q
//				layout.setBasicVal(ExcelUtils.readValue(sheet, index, "R"));// 기본값R
//				String korInclYn = ExcelUtils.readValue(sheet, index, "S");
//				if (korInclYn == null || korInclYn.equals("")) {
//					korInclYn = "N";
//				}
//				layout.setKorInclYn(korInclYn);// 한글포함여부S
//				String ioKey = getCodeValueNm("IO_KEY", ExcelUtils.readValue(sheet, index, "T"), "ko");
//				if (ioKeyMap.containsKey(ioKey)) {
//					errMsg.append("\n전문에 같은 IO Key가 존재할 수 없습니다.");
//					isValid = false;
//				}
//				layout.setIoKey(ioKey);// IO키
//				if (ioKey != null && !ioKey.equals("")) {
//					ioKeyMap.put(ioKey, ioKey);
//				} // IO키T
//				layout.setChildDtoNm(ExcelUtils.readValue(sheet, index, "U"));// 하위IO명U
//				layout.setFillerVal(getCodeValueNm("FILLER_CD", ExcelUtils.readValue(sheet, index, "V"), "ko"));// Filler
//
//				if (data.getMsgDscd().equals("CH")) {
//					layout.setExtrnlMsgNoYn(ExcelUtils.readValue(sheet, index, "X"));
//				} else if (data.getMsgDscd().equals("ISOH") || data.getMsgDscd().equals("ISOB")) {
//					layout.setExtrnlMsgNoYn(ExcelUtils.readValue(sheet, index, "AB"));
//				}
//				layout.setExtrnlSrchKeyYn(ExcelUtils.readValue(sheet, index, "Y"));
//
//				// layout.setExtrnlOffsetYn(ExcelUtils.readValue(sheet, index, "W"));
//				layout.setIso8583SpecNo(ExcelUtils.readValue(sheet, index, "AC"));
//				layout.setIso8583FldCharSetCd(ExcelUtils.readValue(sheet, index, "AD"));// 필드타입
//				layout.setIso8583VariableYn(ExcelUtils.readValue(sheet, index, "AE"));
//				layout.setIso8583FldLenTypeCd(
//						getCodeValueNm("ISO_FLD_LEN_TYPE_CD", ExcelUtils.readValue(sheet, index, "AF"), "ko"));
//				layout.setIso8583LenFldLen(ExcelUtils.readValue(sheet, index, "AG") != null
//						&& !ExcelUtils.readValue(sheet, index, "AG").equals("")
//								? Integer.parseInt(ExcelUtils.readValue(sheet, index, "AG"))
//								: null);
//				layout.setIso8583LenFldInclYn(ExcelUtils.readValue(sheet, index, "AH"));
//				layout.setIso8583LenFldCopyYn(ExcelUtils.readValue(sheet, index, "AI"));
//				layout.setIso8583FldMaxLen(ExcelUtils.readValue(sheet, index, "AJ") != null
//						&& !ExcelUtils.readValue(sheet, index, "AJ").equals("")
//								? Integer.parseInt(ExcelUtils.readValue(sheet, index, "AJ"))
//								: null);
//				layout.setIso8583TrnsmsnDataLen(ExcelUtils.readValue(sheet, index, "AK") != null
//						&& !ExcelUtils.readValue(sheet, index, "AK").equals("")
//								? Integer.parseInt(ExcelUtils.readValue(sheet, index, "AK"))
//								: null);
//			}
//
//			if (!isValid) {
//				throw new ServiceException(BxMessages.Error.MSG_VALID_CHECK, errMsg.toString());
//			}
//
//			layoutList.add(layout);
//		}
//		data.setMsglayoutdtDto(layoutList);
//		return index;
//	}

	public String getCodeValueNm(String codeId, String codeValueNm, String locale) {

		CommCodeDto codeValueNmDto = codeDao.selectCommCodeValue(codeId, codeValueNm, locale);
		String codeValue = "";
		if (codeValueNmDto != null) {
			codeValue = codeValueNmDto.getCdVal();
		}

		return codeValue;

	}

	public String convertMsgEngToKor(String code) {
		// 일괄 엑셀 등록 템플릿 변경으로 인해 임시 유틸성 메서드			
		switch (code) {
			case "Internal":
				return "대내";
			case "External":
				return "대외";
			case "Online":
				return "온라인";
			case "Batch":
				return "배치";
			case "Non-Common Body":
				return "개별부";
			case "Common Header":
				return "공통헤더";
			case "Batch Header":
				return "배치Header";
			case "Batch Body":
				return "배치Body";
			case "Batch Trailer":
				return "배치Trailer";
			default:
				return null;
		}
	}
	
	public MsgIdCreateDto getMsgLayoutId(MsgIdCreateDto msgIdCreateDto) {

		// MSG_TYPE 전문타입 IV 개별부
		// MSG_TYPE 전문타입 CH 공통헤더
		// MSG_TYPE 전문타입 STH 표준헤더
		// MSG_TYPE 전문타입 ISOH ISO8583헤더
		// MSG_TYPE 전문타입 ISOB ISO8583바디
		// MSG_TYPE 전문타입 BATH 배치Header
		// MSG_TYPE 전문타입 BATB 배치Body
		// MSG_TYPE 전문타입 BATT 배치Trailer

		MsgIdCreateDto returnDto = new MsgIdCreateDto();

		String msgType = null;

		if (msgIdCreateDto.getChlDscd().equals("INTERNAL")) {
			if (msgIdCreateDto.getTrxDscd().equals("ONLINE")) {
				if (msgIdCreateDto.getMsgDscd().equals("IV")) {
					msgType = "IB";
				} else if (msgIdCreateDto.getMsgDscd().equals("STH")) {
					msgType = "IH";
				}
			} else {
				if (msgIdCreateDto.getMsgDscd().equals("BATH")) {
					msgType = "BH";
				} else if (msgIdCreateDto.getMsgDscd().equals("BATB")) {
					msgType = "BD";
				} else {
					msgType = "BT";
				}
			}
		} else {
			if (msgIdCreateDto.getTrxDscd().equals("ONLINE")) {
				if (msgIdCreateDto.getMsgDscd().equals("IV")) {
					msgType = "EB";
				} else if (msgIdCreateDto.getMsgDscd().equals("CH")) {
					msgType = "EH";
				} else if (msgIdCreateDto.getMsgDscd().equals("ISOH")) {
					msgType = "SH";
				} else if (msgIdCreateDto.getMsgDscd().equals("ISOB")) {
					msgType = "SB";
				}
			} else {
				if (msgIdCreateDto.getMsgDscd().equals("BATH")) {
					msgType = "BH";
				} else if (msgIdCreateDto.getMsgDscd().equals("BATB")) {
					msgType = "BD";
				} else {
					msgType = "BT";
				}
			}
		}

		// 대외공통부: EH, 대외개별부: EB, ISO8583공통부: SH, ISO8583개별부: SB, 대내개별부: IB, 배치헤더: BH,
		// 배치데이터: BD, 배치트레일러: BT
		// ※ 전문ID 채번 규칙
		// - 대외공통부, ISO8583공통부: 전문타입2자리 + 기관업무구분6자리 + 어플리케이션코드2자리 + 일련번호4자리+ V + 전문버전2자리
		// - 대외개별부, ISO8583개별부: 전문타입2자리 + 기관업무구분6자리 + 어플리케이션코드2자리 + 전문번호4자리+ V + 전문버전2자리
		// - 대내개별부: 전문타입2자리 + 송수신시스템코드6자리 + 어플리케이션코드2자리 + 일련번호4자리+ V + 전문버전2자리
		// - 배치 헤더/데이터/트레일러: 전문타입2자리 + 어플리케이션코드2자리 + 파일ID8자리 + 일련번호2자리+ V + 전문버전2자리
		String msgLayoutId = null;
		String seqResult = null;

		if (msgType.equals("EH") || msgType.equals("SH")) {

			String extBizCd = msgIdCreateDto.getExtrnlBizNm();
			extBizCd = StringUtils.rightPad(extBizCd, 8, "0");
			String l3Cd = msgIdCreateDto.getLv3Cd();
			String version = Integer.toString(msgIdCreateDto.getMsgVersion());
			version = StringUtils.leftPad(version, 2, "0");
			msgIdCreateDto.setFileId("");
			msgIdCreateDto.setMsgNumber("");
			msgIdCreateDto.setReceiveSysCd("");
			msgIdCreateDto.setSendSysCd("");
			Integer seq = msglayoutbsDao.selectIdSeq(msgIdCreateDto);

			if (seq == null) {
				seq = 0;
			}
			seq = seq + 1;
			seqResult = StringUtils.leftPad(Integer.toString(seq), 4, "0");
			msgLayoutId = msgType + extBizCd + l3Cd + seqResult + "V" + version;
		} else if (msgType.equals("EB") || msgType.equals("SB")) {

			String extBizCd = msgIdCreateDto.getExtrnlBizNm();
			extBizCd = StringUtils.rightPad(extBizCd, 8, "0");
			String l3Cd = msgIdCreateDto.getLv3Cd();
			String version = Integer.toString(msgIdCreateDto.getMsgVersion());
			version = StringUtils.leftPad(version, 2, "0");
			String msgNumber = msgIdCreateDto.getMsgNumber();
			msgNumber = StringUtils.leftPad(msgNumber, 4, "0");
			msgLayoutId = msgType + extBizCd + l3Cd + msgNumber + "V" + version;
		} else if (msgType.equals("IB")) {

			String sendSystem = msgIdCreateDto.getSendSysCd();
			String recvSystem = msgIdCreateDto.getReceiveSysCd();
			String l3Cd = msgIdCreateDto.getLv3Cd();
			String version = Integer.toString(msgIdCreateDto.getMsgVersion());
			version = StringUtils.leftPad(version, 2, "0");
			msgIdCreateDto.setMsgNumber("");
			Integer seq = msglayoutbsDao.selectIdSeq(msgIdCreateDto);

			if (seq == null) {
				seq = 0;
			}
			seq = seq + 1;
			seqResult = StringUtils.leftPad(Integer.toString(seq), 6, "0");
			msgLayoutId = msgType + sendSystem + recvSystem + l3Cd + seqResult + "V" + version;
		} else if (msgType.equals("IH")) {
			String sendSystem = msgIdCreateDto.getSendSysCd();
			String recvSystem = msgIdCreateDto.getReceiveSysCd();
			String l3Cd = msgIdCreateDto.getLv3Cd();
			String version = Integer.toString(msgIdCreateDto.getMsgVersion());
			version = StringUtils.leftPad(version, 2, "0");
			msgIdCreateDto.setMsgNumber("");
			Integer seq = msglayoutbsDao.selectIdSeq(msgIdCreateDto);

			if (seq == null) {
				seq = 0;
			}
			seq = seq + 1;
			seqResult = StringUtils.leftPad(Integer.toString(seq), 6, "0");
			msgLayoutId = msgType + sendSystem + recvSystem + l3Cd + seqResult + "V" + version;
		} else {
			String l3Cd = msgIdCreateDto.getLv3Cd();
			String fileId = msgIdCreateDto.getFileId();
			int fileIdSize = fileId.length();
			boolean fileSizeMax = false;
			if (fileIdSize > 8) {
				// throw new ServiceException(BxMessages.Error.FILE_SIZE_ERR);
				fileId = fileId.substring(0, 8);
				msgIdCreateDto.setFileId(fileId);
				fileSizeMax = true;
			} else if (fileIdSize == 8) {
				fileSizeMax = true;
			}
			fileId = StringUtils.rightPad(fileId, 8, "0");
			String version = Integer.toString(msgIdCreateDto.getMsgVersion());
			version = StringUtils.leftPad(version, 2, "0");
			msgIdCreateDto.setMsgNumber("");

			Integer seq = null;
			if (fileSizeMax) {
				seq = msglayoutbsDao.selectIdSeqFile(msgIdCreateDto);
			} else {
				seq = msglayoutbsDao.selectIdSeq(msgIdCreateDto);
			}

			if (seq == null) {
				seq = 0;
			}
			seq = seq + 1;
			seqResult = StringUtils.leftPad(Integer.toString(seq), 4, "0");
			msgLayoutId = msgType + l3Cd + fileId + seqResult + "V" + version;
		}

		//returnDto.setMsgLayoutId(msgLayoutId);
		returnDto.setSeq(seqResult);

		return returnDto;
	}

	public boolean msgDefaultValid(MsglayoutbsDto msgDto, boolean isValid, StringBuilder str) {

		List<MsglayoutdtDto> msgDtList = msgDto.getMsglayoutdtDto();

		for (MsglayoutdtDto dto : msgDtList) {
			String defaultValue = dto.getBasicVal();
			String dataType = dto.getDataTypeNm();
			String fieldEngNm = dto.getFldEngNm();
			String layoutId = dto.getMsgLayoutId();

			if (defaultValue != null && !defaultValue.equals("")) {
				if (dataType != null && !dataType.equals("")) {

					if (dataType.equals("LONG")) {
						try {
							Long.parseLong(defaultValue);
						} catch (NumberFormatException e) {
							isValid = false;
							str.append("\nLong 타입 기본값에는 정수 타입의 숫자만 세팅할 수 있습니다. - 전문ID [" + layoutId + "] 필드영문명: ["
									+ fieldEngNm + "]");
						}
					} else if (dataType.equals("BIGDECIMAL")) {
						try {
							Double.parseDouble(defaultValue);
						} catch (NumberFormatException e) {
							isValid = false;
							str.append("\nBIGDECIMAL 타입 기본값에는 실수 타입의 숫자만 세팅할 수 있습니다. - 전문ID [" + layoutId + "] 필드영문명: ["
									+ fieldEngNm + "]");
						}
					} else if (dataType.equals("LAYOUT")) {
						if (!defaultValue.equals("instance") && !defaultValue.equals("null")) {
							isValid = false;
							str.append("\nLAYOUT 타입 기본값에는 Class초기화 값(instance, null) 만 세팅할 수 있습니다. - 전문ID [" + layoutId
									+ "] 필드영문명: [" + fieldEngNm + "]");
						}
					}
				}
			}
		}

		return isValid;
	}

}
