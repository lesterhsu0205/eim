package eims.web.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import eims.ServiceContext;
import eims.web.constants.BxCode;
import eims.web.constants.BxConstants;
import eims.web.constants.BxMessages;
import eims.web.dao.AppcdDao;
import eims.web.dao.BizcdDao;
import eims.web.dao.CommCodeDao;
import eims.web.dao.DepolysysbsDao;
import eims.web.dao.ExtrnlinstcdDao;
import eims.web.dao.IntrfccombsDao;
import eims.web.dao.IntrfcdeployhisthsDao;
import eims.web.dao.IntrfcdeploysysdtDao;
import eims.web.dao.IntrfcmsglayoutdtDao;
import eims.web.dao.IntrfcroutinfodtDao;
import eims.web.dao.IntrfcsrsysdtDao;
import eims.web.dao.MetabsDao;
import eims.web.dao.MsglayoutbsDao;
import eims.web.dao.MsglayoutdtDao;
import eims.web.dao.SrsysbsDao;
import eims.web.dao.TrxcdDao;
import eims.web.dto.IntrfcDeploy;
import eims.web.dto.IntrfcDeployResponse;
import eims.web.dto.IntrfcDeployResponseList;
import eims.web.dto.IntrfcDeployResponseResult;
import eims.web.dto.IntrfcInfo;
import eims.web.dto.IntrfcdeployInfoDto;
import eims.web.dto.table.ActionhisthsDto;
import eims.web.dto.table.CommCodeDto;
import eims.web.dto.table.DepolysysbsDto;
import eims.web.dto.table.ExtrnlinstcdDto;
import eims.web.dto.table.IntrfcIdCreateDto;
import eims.web.dto.table.IntrfccombsDetailCCDto;
import eims.web.dto.table.IntrfccombsDetailEAIDto;
import eims.web.dto.table.IntrfccombsDetailFEPDto;
import eims.web.dto.table.IntrfccombsDetailMCIDto;
import eims.web.dto.table.IntrfccombsDto;
import eims.web.dto.table.IntrfccombsListDto;
import eims.web.dto.table.IntrfccombsMappingDto;
import eims.web.dto.table.IntrfccombsRawDataDto;
import eims.web.dto.table.IntrfcdeployhisthsDto;
import eims.web.dto.table.IntrfcdeploysysdtDto;
import eims.web.dto.table.IntrfcmsglayoutdtDto;
import eims.web.dto.table.IntrfcroutinfodtDto;
import eims.web.dto.table.IntrfcsrsysdtDto;
import eims.web.dto.table.MetabsDto;
import eims.web.dto.table.MsgIdCreateDto;
import eims.web.dto.table.MsgInsertDto;
import eims.web.dto.table.MsglayoutbsDto;
import eims.web.dto.table.MsglayoutdtDto;
import eims.web.dto.ui.UiIntrfcDeployResponse;
import eims.web.dto.ui.UiIntrfccombsOut;
import eims.web.dto.ui.UiIntrfcdeployhisthsOut;
import eims.web.excel.drm.DrmUtil;
import eims.web.excel.drm.exception.FasooDrmException;
import eims.web.excel.drm.exception.FileTypeException;
import eims.web.exception.ServiceException;
import eims.web.utils.DateUtils;
import eims.web.utils.ExcelUtils;
import eims.web.utils.JsonUtils;
import eims.web.utils.RestUtils;
import eims.web.utils.UidUtils;

@Service
// @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class IntrfccomService {

	final Logger logger = LoggerFactory.getLogger(getClass());

	private static final Map<String, String> deployCodeMap = new HashMap<String, String>();
	static {
		deployCodeMap.put("COO", "COO");
		deployCodeMap.put("COB", "COB");
	}

	@Autowired
	private IntrfccombsDao intrfccombsDao;
	@Autowired
	private IntrfcsrsysdtDao intrfcsrsysdtDao;
	@Autowired
	private IntrfcmsglayoutdtDao intrfcmsglayoutdtDao;
	@Autowired
	private MsglayoutService msglayoutService;
	@Autowired
	private MsglayoutbsDao msglayoutbsDao;
	@Autowired
	private MsglayoutdtDao msglayoutdtDao;
	@Autowired
	private SequenceService seqService;
	@Autowired
	private IntrfcdeployhisthsDao intrfcdeployhisthsDao;
	@Autowired
	private IntrfcdeploysysdtDao intrfcdeploysysdtDao;
	@Autowired
	private ActionhistService actionhistService;
	@Autowired
	private IntrfcroutinfodtDao intrfcroutinfodtDao;
	@Autowired
	private DepolysysService depolysysService;
	@Autowired
	private CommCodeDao codeDao;
	@Autowired
	private AppcdDao appcdDao;
	@Autowired
	private SrsysbsDao srsysbsDao;
	@Autowired
	private BizcdDao bizCdDao;
	@Autowired
	private ExtrnlinstcdDao extrnlinstcdDao;
	@Autowired
	private TrxcdDao trxcdDao;
	@Autowired
	private MetabsDao metabsDao;
	@Autowired
	private DepolysysbsDao deployDao;

	public UiIntrfccombsOut getList(String intrfcId, String intrfcNm, String intrfcNmSub, String intrfcWayCd, String workStatusCd,
			String regManId, String regDttm, String msgTrnsfrmYn, String trxCd, String bizCd, String instCd,
			String trxDscd, String intrfcTypeCd, String lv1Cd, String lv2Cd, String lv3Cd, String lv4Cd, String lv5Cd,
			String syncAsyncDscd, String srTypeCd, String rqstExtrnlMsgNo, String rspsExtrnlMsgNo, String sysCdS,
			String sysCdR, String msgLayoutId, String trxTypeDscd, String viewId, String execEnvDscd, int pageSize, int pageNumber) {
		UiIntrfccombsOut out = new UiIntrfccombsOut();

		if (sysCdS != null && !sysCdS.equals("")) {
			sysCdS = sysCdS.toUpperCase();
		}
		if (sysCdR != null && !sysCdR.equals("")) {
			sysCdR = sysCdR.toUpperCase();
		}
		if (bizCd != null && !bizCd.equals("")) {
			bizCd = bizCd.toUpperCase();
		}

		int totalCount = intrfccombsDao.selectAllCnt(intrfcId, intrfcNm, intrfcNmSub, intrfcWayCd, workStatusCd, regManId, regDttm,
				msgTrnsfrmYn, trxCd, bizCd, instCd, trxDscd, intrfcTypeCd, lv1Cd, lv2Cd, lv3Cd, lv4Cd, lv5Cd,
				syncAsyncDscd, srTypeCd, rqstExtrnlMsgNo, rspsExtrnlMsgNo, sysCdS, sysCdR, msgLayoutId, trxTypeDscd,viewId,execEnvDscd);

		List<IntrfccombsListDto> intrfccombsList = intrfccombsDao.selectAll(intrfcId, intrfcNm, intrfcNmSub, intrfcWayCd,
				workStatusCd, regManId, regDttm, msgTrnsfrmYn, trxCd, bizCd, instCd, trxDscd, intrfcTypeCd, lv1Cd,
				lv2Cd, lv3Cd, lv4Cd, lv5Cd, syncAsyncDscd, srTypeCd, rqstExtrnlMsgNo, rspsExtrnlMsgNo, sysCdS, sysCdR,
				msgLayoutId, trxTypeDscd, viewId, execEnvDscd, pageSize, pageNumber);

		if (intrfccombsList == null) {
			intrfccombsList = new ArrayList<IntrfccombsListDto>();
		}

		if (totalCount > 0) {
			out.setTotalCnt(totalCount);
			out.setIntrfccombsOutList(intrfccombsList);
		}

		return out;
	}

	public UiIntrfccombsOut getFebList(String intrfcId, String intrfcNm, String intrfcNmSub, String intrfcWayCd, String workStatusCd,
			String regManId, String regDttm, String msgTrnsfrmYn, String trxCd, String bizCd, String instCd,
			String trxDscd, String intrfcTypeCd, String lv1Cd, String lv2Cd, String lv3Cd, String lv4Cd, String lv5Cd,
			String syncAsyncDscd, String srTypeCd, String rqstExtrnlMsgNo, String rspsExtrnlMsgNo, String sysCdS,
			String sysCdR, String msgLayoutId, String trxTypeDscd, String viewId, int pageSize, int pageNumber) {
		UiIntrfccombsOut out = new UiIntrfccombsOut();

		if (sysCdS != null && !sysCdS.equals("")) {
			sysCdS = sysCdS.toUpperCase();
		}
		if (sysCdR != null && !sysCdR.equals("")) {
			sysCdR = sysCdR.toUpperCase();
		}
		if (bizCd != null && !bizCd.equals("")) {
			bizCd = bizCd.toUpperCase();
		}

		int totalCount = intrfccombsDao.selectFebAllCnt(intrfcId, intrfcNm, intrfcNmSub, intrfcWayCd, workStatusCd, regManId, regDttm,
				msgTrnsfrmYn, trxCd, bizCd, instCd, trxDscd, intrfcTypeCd, lv1Cd, lv2Cd, lv3Cd, lv4Cd, lv5Cd,
				syncAsyncDscd, srTypeCd, rqstExtrnlMsgNo, rspsExtrnlMsgNo, sysCdS, sysCdR, msgLayoutId, trxTypeDscd, viewId);

		List<IntrfccombsListDto> intrfccombsList = intrfccombsDao.selectFebAll(intrfcId, intrfcNm, intrfcNmSub, intrfcWayCd,
				workStatusCd, regManId, regDttm, msgTrnsfrmYn, trxCd, bizCd, instCd, trxDscd, intrfcTypeCd, lv1Cd,
				lv2Cd, lv3Cd, lv4Cd, lv5Cd, syncAsyncDscd, srTypeCd, rqstExtrnlMsgNo, rspsExtrnlMsgNo, sysCdS, sysCdR,
				msgLayoutId, trxTypeDscd, viewId, pageSize, pageNumber);

		if (intrfccombsList == null) {
			intrfccombsList = new ArrayList<IntrfccombsListDto>();
		}

		if (totalCount > 0) {
			out.setTotalCnt(totalCount);
			out.setIntrfccombsOutList(intrfccombsList);
		}

		return out;
	}
	
	public List<String> getDeployAllList(String intrfType) {
		
		List<String> result = new ArrayList<String>();
		
		result = intrfccombsDao.selectDeployAllInterface(intrfType);
		return result;
		
	}
	
	public IntrfccombsDto get(String intrfcId) {
		IntrfccombsDto out = intrfccombsDao.selectIntrfccombs(intrfcId);

		if (out == null) {
			throw new ServiceException(BxMessages.Error.NOT_FOUNDED, intrfcId);
		}

		String intrfcDate = out.getRegDttm();
		boolean isMsgLayoutTran = false;
		List<String> msgLayoutTranList = new ArrayList<String>();

		List<IntrfcsrsysdtDto> intrfcsrsysdtDto = intrfcsrsysdtDao.selectIntrfcsrsysdt(intrfcId);
		out.setIntrfcsrsysdtDto(intrfcsrsysdtDto);

		List<IntrfcroutinfodtDto> IntrfcroutinfodtDtoList = intrfcroutinfodtDao.selectIntrfcroutinfodt(intrfcId);
		out.setIntrfcroutinfodtDto(IntrfcroutinfodtDtoList);

		List<IntrfcmsglayoutdtDto> intrfcmsglayoutdtDto = intrfcmsglayoutdtDao.selectIntrfcmsglayoutdt(intrfcId);
		for (IntrfcmsglayoutdtDto intfDto : intrfcmsglayoutdtDto) {
			try {
				MsglayoutbsDto msglayoutbsDto = msglayoutService.get(intfDto.getMsgLayoutId());
				String msgDate = msglayoutbsDto.getRegDttm();

				if (intrfcDate.compareTo(msgDate) < 0) {
					isMsgLayoutTran = true;
					msgLayoutTranList.add(intfDto.getMsgLayoutId());
				}

				intfDto.setMsglayoutbsDto(msglayoutbsDto);
			} catch (ServiceException e) {
				intfDto.setMsglayoutbsDto(null);
			}

		}

		if (isMsgLayoutTran) {
			out.setMsgLayoutTranYn("Y");
			out.setMsgLayoutTranList(msgLayoutTranList);
		} else {
			out.setMsgLayoutTranYn("N");
		}

		List<IntrfcdeploysysdtDto> intrfcdeploysysdtDto = intrfcdeploysysdtDao.selectIntrfcdeploysysdt(intrfcId);
		List<IntrfcdeploysysdtDto> intrfcdeploysysdtDtoResult = new ArrayList<IntrfcdeploysysdtDto>();
		for (IntrfcdeploysysdtDto dto : intrfcdeploysysdtDto) {
			String resultCd = dto.getDeployResultCd();
			if (resultCd != null && !resultCd.equals("")) {
				dto.setDeployResultCd(null);
			}
			intrfcdeploysysdtDtoResult.add(dto);
		}

		out.setIntrfcdeploysysdtDto(intrfcdeploysysdtDtoResult);

		List<IntrfcdeployhisthsDto> intrfcdeployhisthsDto = intrfcdeployhisthsDao.selectIntrfcdeployhisths(intrfcId);
		out.setIntrfcdeployhisthsDto(intrfcdeployhisthsDto);

		out.setIntrfcmsglayoutdtDto(intrfcmsglayoutdtDto);

		IntrfccombsRawDataDto rawDataDto = null;
		String jsonRawData = out.getRawData();
		if (jsonRawData != null && !jsonRawData.equals("")) {
			try {
				rawDataDto = JsonUtils.jsonToObect(jsonRawData, IntrfccombsRawDataDto.class);
			} catch (JsonParseException e) {
				logger.error("{}", e);
				throw new ServiceException(BxMessages.Error.SYSTEM_EXCEPTION);
			} catch (JsonMappingException e) {
				logger.error("{}", e);
				throw new ServiceException(BxMessages.Error.SYSTEM_EXCEPTION);
			} catch (IOException e) {
				logger.error("{}", e);
				throw new ServiceException(BxMessages.Error.SYSTEM_EXCEPTION);
			}
		}

		if (rawDataDto != null) {
			out.setIntrfccombsMappingReqDto(rawDataDto.getIntrfccombsMappingReqDto());
			out.setIntrfccombsMappingResDto(rawDataDto.getIntrfccombsMappingResDto());
			if (out.getIntrfcTypeCd().equals("MCI")) {
				logger.debug("INTRFCID : {} MCI DTO Parsed : {}", intrfcId, rawDataDto.getMciDto());
				out.setMciDto(rawDataDto.getMciDto());
			} else if (out.getIntrfcTypeCd().equals("EAI_I") || out.getIntrfcTypeCd().equals("EAI_E")) {
				out.setEaiDto(rawDataDto.getEaiDto());
				if (out.getIntrfcWayCd() != null && out.getIntrfcWayCd().equals("FILETOFILE")) {
					out.setIntrfcMsgFieldEncodingDto(rawDataDto.getIntrfcMsgFieldEncodingDto());
				}
			} else if (out.getIntrfcTypeCd().equals("CC")) {
				out.setCcDto(rawDataDto.getCcDto());
			} else {
				logger.debug("rawDataDto.getFepDto().getIntrfDesc()----{}", rawDataDto.getFepDto().getIntrfDesc());
				out.setFepDto(rawDataDto.getFepDto());
			}
		}

		out.setRawData(null);

		return out;
	}

	public void ioNmValidation(MsglayoutbsDto msglayout, String intrfcType, String trxDscd, String chlDscd2) {

		List<MsglayoutdtDto> dtoList = msglayout.getMsglayoutdtDto();
		String chlDscd = msglayout.getChlDscd();
		StringBuilder str = new StringBuilder();
		boolean isValid = true;

		if (intrfcType.equals("FEP")) {
			if (trxDscd.equals("ONLINE")) {
				if (chlDscd.equals("EXTERNAL")) {
					if (msglayout.getDtoNm() == null || msglayout.getDtoNm().equals("")) {
						str.append("\n대외전문IO명이 비어있습니다. [전문아이디: " + msglayout.getMsgLayoutId() + "]");
						isValid = false;
					}
					for (MsglayoutdtDto dto : dtoList) {
						if (dto.getDataTypeNm().equals("LAYOUT")) {
							if (dto.getChildDtoNm() == null || dto.getChildDtoNm().equals("")) {
								str.append("\n하위IO명이 비어있습니다. [전문아이디: " + msglayout.getMsgLayoutId() + "], [필드영문명: "
										+ dto.getFldEngNm() + "]");
								isValid = false;
							}
						}
					}
				} else {
					if (msglayout.getMsgDataVal() == null || msglayout.getMsgDataVal().equals("")) {
						str.append("\nIO명이 비어있습니다. [전문아이디: " + msglayout.getMsgLayoutId() + "]");
						isValid = false;
					}
					for (MsglayoutdtDto dto : dtoList) {
						if (dto.getDataTypeNm().equals("LAYOUT")) {
							if (dto.getChildDtoNm() == null || dto.getChildDtoNm().equals("")) {
								str.append("\n하위IO명이 비어있습니다. [전문아이디: " + msglayout.getMsgLayoutId() + "], [필드영문명: "
										+ dto.getFldEngNm() + "]");
								isValid = false;
							}

						}
					}
				}
			}
		} else if (intrfcType.equals("EAI_E")) {

			if (chlDscd.equals("EXTERNAL")) {
				if (msglayout.getDtoNm() == null || msglayout.getDtoNm().equals("")) {
					str.append("\n대외전문IO명이 비어있습니다. [전문아이디: " + msglayout.getMsgLayoutId() + "]");
					isValid = false;
				}
				for (MsglayoutdtDto dto : dtoList) {
					if (dto.getDataTypeNm().equals("LAYOUT")) {
						if (dto.getChildDtoNm() == null || dto.getChildDtoNm().equals("")) {
							str.append("\n하위IO명이 비어있습니다. [전문아이디: " + msglayout.getMsgLayoutId() + "], [필드영문명: "
									+ dto.getFldEngNm() + "]");
							isValid = false;
						}
					}
				}
			} else {
				if (msglayout.getMsgDataVal() == null || msglayout.getMsgDataVal().equals("")) {
					str.append("\nIO명이 비어있습니다. [전문아이디: " + msglayout.getMsgLayoutId() + "]");
					isValid = false;
				}
				for (MsglayoutdtDto dto : dtoList) {
					if (dto.getDataTypeNm().equals("LAYOUT")) {
						if (dto.getChildDtoNm() == null || dto.getChildDtoNm().equals("")) {
							str.append("\n하위IO명이 비어있습니다. [전문아이디: " + msglayout.getMsgLayoutId() + "], [필드영문명: "
									+ dto.getFldEngNm() + "]");
							isValid = false;
						}

					}
				}
			}

		} else if (intrfcType.equals("EAI_I") || intrfcType.equals("MCI") || intrfcType.equals("CC")) {

			if (chlDscd.equals("EXTERNAL")) {
				if (msglayout.getDtoNm() == null || msglayout.getDtoNm().equals("")) {
					str.append("\n대외전문IO명이 비어있습니다. [전문아이디: " + msglayout.getMsgLayoutId() + "]");
					isValid = false;
				}
				for (MsglayoutdtDto dto : dtoList) {
					if (dto.getDataTypeNm().equals("LAYOUT")) {
						if (dto.getChildDtoNm() == null || dto.getChildDtoNm().equals("")) {
							str.append("\n하위IO명이 비어있습니다. [전문아이디: " + msglayout.getMsgLayoutId() + "], [필드영문명: "
									+ dto.getFldEngNm() + "]");
							isValid = false;
						}
					}
				}
			} else {
				if (msglayout.getMsgDataVal() == null || msglayout.getMsgDataVal().equals("")) {
					str.append("\nIO명이 비어있습니다. [전문아이디: " + msglayout.getMsgLayoutId() + "]");
					isValid = false;
				}
				for (MsglayoutdtDto dto : dtoList) {
					if (dto.getDataTypeNm().equals("LAYOUT")) {
						if (dto.getChildDtoNm() == null || dto.getChildDtoNm().equals("")) {
							str.append("\n하위IO명이 비어있습니다. [전문아이디: " + msglayout.getMsgLayoutId() + "], [필드영문명: "
									+ dto.getFldEngNm() + "]");
							isValid = false;
						}

					}
				}
			}

		}

		if (!isValid) {
			throw new ServiceException(BxMessages.Error.EXT_IONM_NULL_EXCEPTION, str.toString());
		}

	}
	
	public String convertTermEngToKor(String code) {
		// 일괄 엑셀 등록 템플릿 변경으로 인해 임시 유틸성 메서드			
		switch (code) {
			case "Sync":
				return "동기";
			case "Async":
				return "비동기";
			default:
				return null;
		}
	}
	
	public String validationInterface(IntrfccombsDto in, boolean screenYn) {

		String workStatus = "";

		List<MsglayoutbsDto> sendReqList = new ArrayList<MsglayoutbsDto>();
		List<MsglayoutbsDto> sendResList = new ArrayList<MsglayoutbsDto>();
		List<MsglayoutbsDto> recvReqList = new ArrayList<MsglayoutbsDto>();
		List<MsglayoutbsDto> recvResList = new ArrayList<MsglayoutbsDto>();

		List<String> ioValidList = new ArrayList<String>();

		String intrfcType = in.getIntrfcTypeCd();
		String trxDscd = in.getTrxDscd();
		int msgSize = 0;
		logger.debug("###100##");
		if (in.getIntrfcmsglayoutdtDto() != null) {
			for (IntrfcmsglayoutdtDto dto : in.getIntrfcmsglayoutdtDto()) {

				if (intrfcType.equals("FEP") && trxDscd.equals("ONLINE")) {
					if (dto.getSysCd() != null) {
						if (dto.getSysCd().equals("COO")) {
							ioValidList.add(dto.getMsgLayoutId());
						}
					}
				} else if (intrfcType.equals("EAI_E")) {
					if (dto.getSysCd() != null) {
						if (dto.getSysCd().equals("COB"))
							ioValidList.add(dto.getMsgLayoutId());
					}
				} else if (intrfcType.equals("EAI_I") || intrfcType.equals("MCI") || intrfcType.equals("CC")) {
					logger.debug(
							"============================================================================================================1");
					if (dto.getSysCd() != null) {
						logger.debug(
								"============================================================================================================2");
						logger.debug(dto.getSysCd());
						logger.debug(dto.toString());
						if (dto.getSysCd().equals("COO") || dto.getSysCd().equals("COB")) {
							logger.debug(
									"============================================================================================================");
							logger.debug(dto.getMsglayoutbsDto().getMsgLayoutId());
							logger.debug(dto.getMsglayoutbsDto().toString());
							logger.debug(
									"============================================================================================================");
							ioValidList.add(dto.getMsgLayoutId());
						}
					}
				}

				String srType = dto.getSrTypeCd();
				String reqResType = dto.getRqstRspsTypeCd();

				if (srType.equals("SEND")) {
					if (reqResType.equals("REQUEST")) {
						sendReqList.add(dto.getMsglayoutbsDto());
						msgSize++;
					} else if (reqResType.equals("RESPONSE")) {
						sendResList.add(dto.getMsglayoutbsDto());
						msgSize++;
					}
				} else if (srType.equals("RECEIVE")) {
					if (reqResType.equals("REQUEST")) {
						recvReqList.add(dto.getMsglayoutbsDto());
						msgSize++;
					} else if (reqResType.equals("RESPONSE")) {
						recvResList.add(dto.getMsglayoutbsDto());
						msgSize++;
					}
				}
			}
		}

		iokeyValid(sendReqList, "송신요청전문");
		iokeyValid(sendResList, "송신응답전문");
		iokeyValid(recvReqList, "수신요청전문");
		iokeyValid(recvResList, "수신응답전문");

		replKeyValid(sendReqList, "송신요청전문", in, screenYn);
		replKeyValid(sendResList, "송신응답전문", in, screenYn);
		replKeyValid(recvReqList, "수신요청전문", in, screenYn);
		replKeyValid(recvResList, "수신응답전문", in, screenYn);

		for (String layoutId : ioValidList) {
			logger.debug("layoutId: " + layoutId);

			ioNmValidation(msglayoutService.get(layoutId), intrfcType, trxDscd, "");
		}

		if (screenYn) {
			if (in.getIntrfcTypeCd().equals("FEP") && in.getTrxDscd().equals("BATCH")) {
				workStatus = "WORK_COMP";
			} else {
				if (msgSize <= 0) {
					if (in.getIntrfcTypeCd().equals("EAI_I") && in.getTrxDscd().equals("BATCH")) {
						workStatus = "WORK_COMP";
					} else {
						workStatus = "WORKING";
					}
				} else {

					logger.debug("###102##");
					workStatus = "WORK_COMP";
				}
			}

		}

		return workStatus;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String add(IntrfccombsDto in, boolean screenYn) {
		// IntrfccombsDto curIntrfccombsInfo =
		// intrfccombsDao.selectIntrfccombs(in.getIntrfcId());
		ActionhisthsDto actionhisthsDto = new ActionhisthsDto();

		in.setRegManId(ServiceContext.getUserInfo().getUserId());
		in.setRegDttm(DateUtils.getCurrentDate(9));

		// if (screenYn) {
		// if (curIntrfccombsInfo != null) {
		// throw new ServiceException(BxMessages.Error.DUPLICATE_KEY, in.getIntrfcId());
		// }
		// }

		IntrfccombsRawDataDto rawDataDto = new IntrfccombsRawDataDto();
		String infTypeCd = in.getIntrfcTypeCd();

		if (infTypeCd.equals("MCI")) {
			rawDataDto.setMciDto(in.getMciDto());
		} else if (infTypeCd.equals("CC")) {
			rawDataDto.setCcDto(in.getCcDto());
		} else if (infTypeCd.equals("EAI_I") || infTypeCd.equals("EAI_E")) {
			rawDataDto.setEaiDto(in.getEaiDto());
			if (in.getIntrfcWayCd() != null && in.getIntrfcWayCd().equals("FILETOFILE")) {
				List<String> validWrapperList = new ArrayList<>();
				StringBuilder str = new StringBuilder();
				if (in.getEaiDto().getReqWrapperDtoNm() != null && !in.getEaiDto().getReqWrapperDtoNm().equals("")) {
					validWrapperList.add(in.getEaiDto().getReqWrapperDtoNm());
				}
				if (in.getEaiDto().getResWrapperDtoNm() != null && !in.getEaiDto().getResWrapperDtoNm().equals("")) {
					validWrapperList.add(in.getEaiDto().getResWrapperDtoNm());
				}
				boolean isValid = validationIoNmWrapper(in, validWrapperList, str);
				if (!isValid) {
					throw new ServiceException(BxMessages.Error.WRAPPER_IO_DUP, str.toString());
				}

				rawDataDto.setIntrfcMsgFieldEncodingDto(in.getIntrfcMsgFieldEncodingDto());
			}
		} else {
			if (in.getTrxDscd().equals("ONLINE")) {
				List<String> validWrapperList = new ArrayList<>();
				StringBuilder str = new StringBuilder();
				if (in.getFepDto().getReqWrapperDtoNm() != null && !in.getFepDto().getReqWrapperDtoNm().equals("")) {
					validWrapperList.add(in.getFepDto().getReqWrapperDtoNm());
				}
				if (in.getFepDto().getResWrapperDtoNm() != null && !in.getFepDto().getResWrapperDtoNm().equals("")) {
					validWrapperList.add(in.getFepDto().getResWrapperDtoNm());
				}
				boolean isValid = validationIoNmWrapper(in, validWrapperList, str);
				if (!isValid) {
					throw new ServiceException(BxMessages.Error.WRAPPER_IO_DUP, str.toString());
				}
			}
			rawDataDto.setFepDto(in.getFepDto());
		}

		String msgTranYn = in.getMsgTrnsfrmYn();
		if (msgTranYn == null || msgTranYn.equals("") || msgTranYn.equals("N")) {
			in.setIntrfccombsMappingReqDto(null);
			in.setIntrfccombsMappingResDto(null);
		}

		if (in.getIntrfccombsMappingReqDto() != null) {
			rawDataDto.setIntrfccombsMappingReqDto(in.getIntrfccombsMappingReqDto());
		}
		if (in.getIntrfccombsMappingResDto() != null) {
			rawDataDto.setIntrfccombsMappingResDto(in.getIntrfccombsMappingResDto());
		}

		String rawDataJson = null;

		try {
			rawDataJson = JsonUtils.objectToJson(rawDataDto);
			logger.debug(
					"===========================rawDataJson======================================================");
			logger.debug(rawDataJson);
			logger.debug(
					"===========================rawDataJson======================================================");
		} catch (JsonParseException e) {
			logger.error("{}", e);
			throw new ServiceException(BxMessages.Error.SYSTEM_EXCEPTION);
		} catch (JsonMappingException e) {
			logger.error("{}", e);
			throw new ServiceException(BxMessages.Error.SYSTEM_EXCEPTION);
		} catch (IOException e) {
			logger.error("{}", e);
			throw new ServiceException(BxMessages.Error.SYSTEM_EXCEPTION);
		}

		in.setRawData(rawDataJson);

		boolean isWorking = false;

		if (in.getIntrfcmsglayoutdtDto() != null) {
			for (IntrfcmsglayoutdtDto dto : in.getIntrfcmsglayoutdtDto()) {
				String msgWorkStatus = dto.getMsglayoutbsDto().getWorkStatusCd();
				if (msgWorkStatus != null && msgWorkStatus.equals("WORKING")) {
					MsglayoutbsDto msgDto = msglayoutbsDao.selectMsglayoutbs(dto.getMsglayoutbsDto().getMsgLayoutId());
					if (msgDto != null && msgDto.getWorkStatusCd().equals("WORKING")) {
						isWorking = true;
					}
				}
			}
		}

		String workStatus = null;

		if (!isWorking) {
			workStatus = validationInterface(in, screenYn);
			// cusApiYnValid(in);
		}

		if (screenYn && !isWorking) {
			in.setWorkStatusCd(workStatus);
		}
		if (screenYn && isWorking) {
			in.setWorkStatusCd("WORKING");
		}

		if(infTypeCd != null) {
			if ( infTypeCd.equals("EAI_I") || infTypeCd.equals("FEP")) {
				in.setWorkStatusCd("WORK_COMP");
			}
		}
		
		String intrfcIdCreate = null;
		
		IntrfcIdCreateDto id = new IntrfcIdCreateDto();

		id.setIntrfcTypeCd(infTypeCd);
		id.setLv3Cd(in.getLv3Cd());
		for (IntrfcsrsysdtDto intrfcsrsysdtDto : in.getIntrfcsrsysdtDto()) {
			if (intrfcsrsysdtDto.getSrTypeCd().equals("SEND")) {
				id.setSendSysCd(intrfcsrsysdtDto.getSysCd());
			} else if (intrfcsrsysdtDto.getSrTypeCd().equals("RECEIVE")) {
				id.setReceiveSysCd(intrfcsrsysdtDto.getSysCd());
			}
		}
		id.setTrxDscd(in.getTrxDscd());
		IntrfcIdCreateDto dto = this.getInterfaceId(id);
		dto.setIntrfcId(in.getIntrfcId());
		intrfcIdCreate = dto.getIntrfcId();
		in.setIntrfcId(intrfcIdCreate);
		in.setCrtnSeq(dto.getSeq());

        if(in.getViewId() == null || in.getViewId().equals("") ) {
        	in.setViewId("NONE");
        }
        
		int insertRes = intrfccombsDao.insertIntrfccombs(in);

		for (IntrfcsrsysdtDto intrfcsrsysdtDto : in.getIntrfcsrsysdtDto()) {
			if (screenYn) {
				intrfcsrsysdtDto.setIntrfcId(intrfcIdCreate);
			}
			intrfcsrsysdtDao.insertIntrfcsrsysdt(intrfcsrsysdtDto);
		}

		if (in.getIntrfcdeploysysdtDto() != null) {
			for (IntrfcdeploysysdtDto intrfcdeploysysdtDto : in.getIntrfcdeploysysdtDto()) {
				if (screenYn) {
					intrfcdeploysysdtDto.setIntrfcId(intrfcIdCreate);
				}
				intrfcdeploysysdtDao.insertIntrfcdeploysysdt(intrfcdeploysysdtDto);
			}
		}
		if (in.getIntrfcroutinfodtDto() != null) {
			for (IntrfcroutinfodtDto intrfcroutinfodtDto : in.getIntrfcroutinfodtDto()) {
				if (screenYn) {
					intrfcroutinfodtDto.setIntrfcId(intrfcIdCreate);
				}
				intrfcroutinfodtDao.insertIntrfcroutinfodt(intrfcroutinfodtDto);
			}
		}

		Map<String, String> aleadyInsert = new HashMap<String, String>();

		intrfcmsglayoutdtDao.deleteIntrfcmsglayoutdt(in.getIntrfcId());

		if (in.getIntrfcmsglayoutdtDto() != null) {
			for (IntrfcmsglayoutdtDto layoutDto : in.getIntrfcmsglayoutdtDto()) {

				int totalCount = 0;
				if (screenYn) {
					layoutDto.setIntrfcId(intrfcIdCreate);
				}

				if (layoutDto.getMsgLayoutId() != null && !layoutDto.getMsgLayoutId().equals(""))
					totalCount = msglayoutbsDao.selectAllCnt(null, null, 0, null, null, null, null, null, null, null,
							layoutDto.getMsgLayoutId(), null, null, null, null, null, null, null, null, null, null,
							null, null, null, null, null, null, null, null, null, null, null, null, 0);

				logger.debug(
						"============================================================================================================");
				logger.debug("total : {}", totalCount);
				logger.debug(layoutDto.getMsgLayoutId());
				logger.debug(
						"============================================================================================================");

				if (totalCount == 0) { // 조회된 메시지가 없을 경우 등록으로 처리
					// if (!aleadyInsert.containsKey(layoutDto.getMsgLayoutId())) {
					// String seq = seqService.createPrefixGuid(BxConstants.Guid.MESSAGE);
					// layoutDto.setMsgLayoutId(seq);
					// layoutDto.getMsglayoutbsDto().setMsgLayoutId(seq);
					// msglayoutService.add(layoutDto.getMsglayoutbsDto());
					// intrfcmsglayoutdtDao.insertIntrfcmsglayoutdt(layoutDto);
					// aleadyInsert.put(layoutDto.getMsgLayoutId(), "Insert");
					// }
					throw new ServiceException(BxMessages.Error.NOT_FOUNDED);

				} else { // 조회된 기존 메시지가 있을 경우, 업데이터 형식으로 처리
					// if (!aleadyInsert.containsKey(layoutDto.getMsgLayoutId())) {
					// msglayoutService.update(layoutDto.getMsglayoutbsDto());
					intrfcmsglayoutdtDao.insertIntrfcmsglayoutdt(layoutDto);
					// aleadyInsert.put(layoutDto.getMsgLayoutId(), "Update");
					// }
				}
			}
		}

		actionhisthsDto.setHstDscd("INTERFACE");
		actionhisthsDto.setItemDesc(in.getIntrfcNm());
		actionhisthsDto.setItemId(in.getIntrfcId());
		actionhisthsDto.setUserId(ServiceContext.getUserInfo().getUserId());
		actionhisthsDto.setWorkCttCd("CREATE");
		actionhisthsDto.setWorkDttm(DateUtils.getCurrentDate(10));

		actionhistService.add(actionhisthsDto);

		return in.getIntrfcId();
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String addDefinition(IntrfccombsDto in, boolean screenYn) {
		ActionhisthsDto actionhisthsDto = new ActionhisthsDto();
		in.setRegDttm(DateUtils.getCurrentDate(9));

		IntrfccombsRawDataDto rawDataDto = new IntrfccombsRawDataDto();
		String infTypeCd = in.getIntrfcTypeCd();
		if (infTypeCd.equals("MCI")) {
			rawDataDto.setMciDto(in.getMciDto());
		} else if (infTypeCd.equals("CC")) {
			rawDataDto.setCcDto(in.getCcDto());
		} else if (infTypeCd.equals("EAI_I") || infTypeCd.equals("EAI_E")) {
			rawDataDto.setEaiDto(in.getEaiDto());
		} else {
			if (in.getTrxDscd().equals("ONLINE")) {
				List<String> validWrapperList = new ArrayList<>();
				StringBuilder str = new StringBuilder();
				if (in.getFepDto().getReqWrapperDtoNm() != null && !in.getFepDto().getReqWrapperDtoNm().equals("")) {
					validWrapperList.add(in.getFepDto().getReqWrapperDtoNm());
				}
				if (in.getFepDto().getResWrapperDtoNm() != null && !in.getFepDto().getResWrapperDtoNm().equals("")) {
					validWrapperList.add(in.getFepDto().getResWrapperDtoNm());
				}
				boolean isValid = validationIoNmWrapper(in, validWrapperList, str);
				if (!isValid) {
					throw new ServiceException(BxMessages.Error.WRAPPER_IO_DUP, str.toString());
				}
			}
			rawDataDto.setFepDto(in.getFepDto());
		}

		String rawDataJson = null;

		try {
			rawDataJson = JsonUtils.objectToJson(rawDataDto);
			logger.debug(
					"===========================rawDataJson======================================================");
			logger.debug(rawDataJson);
			logger.debug(
					"===========================rawDataJson======================================================");
		} catch (JsonParseException e) {
			logger.error("{}", e);
			throw new ServiceException(BxMessages.Error.SYSTEM_EXCEPTION);
		} catch (JsonMappingException e) {
			logger.error("{}", e);
			throw new ServiceException(BxMessages.Error.SYSTEM_EXCEPTION);
		} catch (IOException e) {
			logger.error("{}", e);
			throw new ServiceException(BxMessages.Error.SYSTEM_EXCEPTION);
		}

		in.setRawData(rawDataJson);

		boolean isWorking = false;

		String intrfcIdCreate = null;
		IntrfcIdCreateDto id = new IntrfcIdCreateDto();

		id.setIntrfcTypeCd(infTypeCd);
		id.setLv1Cd(in.getLv1Cd());
		for (IntrfcsrsysdtDto intrfcsrsysdtDto : in.getIntrfcsrsysdtDto()) {
			if (intrfcsrsysdtDto.getSrTypeCd().equals("SEND")) {
				id.setSendSysCd(intrfcsrsysdtDto.getSysCd());
			} else if (intrfcsrsysdtDto.getSrTypeCd().equals("RECEIVE")) {
				id.setReceiveSysCd(intrfcsrsysdtDto.getSysCd());
			}
		}

		id.setTrxDscd(in.getTrxDscd());

		for (IntrfcsrsysdtDto intrfcsrsysdtDto : in.getIntrfcsrsysdtDto()) {
			intrfcsrsysdtDao.insertIntrfcsrsysdt(intrfcsrsysdtDto);
		}

		int insertRes = intrfccombsDao.insertIntrfccombs(in);
		IntrfcdeploysysdtDto intrfcdeploysysdtDto = new IntrfcdeploysysdtDto();
		intrfcdeploysysdtDto.setDeploySysCd("MCA");
		intrfcdeploysysdtDto.setDeploySysNm("MCA");
		intrfcdeploysysdtDto.setDeployUrl("http://10.133.214.136:21700/EIMS-SUPPORT/eims/addmci");
		intrfcdeploysysdtDto.setIntrfcId(in.getIntrfcId());
		intrfcdeploysysdtDao.insertIntrfcdeploysysdt(intrfcdeploysysdtDto);

		intrfcdeploysysdtDto.setDeploySysCd("CBK");
		intrfcdeploysysdtDto.setDeploySysNm("CBK");
		intrfcdeploysysdtDto.setDeployUrl("http://10.133.214.130:17100/bxmAdmin/eims");
		intrfcdeploysysdtDto.setIntrfcId(in.getIntrfcId());
		intrfcdeploysysdtDao.insertIntrfcdeploysysdt(intrfcdeploysysdtDto);
		
		actionhisthsDto.setHstDscd("INTERFACE");
		actionhisthsDto.setItemDesc(in.getIntrfcNm());
		actionhisthsDto.setItemId(in.getIntrfcId());
		actionhisthsDto.setUserId(ServiceContext.getUserInfo().getUserId());
		actionhisthsDto.setWorkCttCd("CREATE");
		actionhisthsDto.setWorkDttm(DateUtils.getCurrentDate(10));

		actionhistService.add(actionhisthsDto);

		return in.getIntrfcId();
	}

	private void cusApiYnValid(IntrfccombsDto dto) {
		String intrfcType = dto.getIntrfcTypeCd();
		List<IntrfcsrsysdtDto> srCdList = dto.getIntrfcsrsysdtDto();
		String sysCd1 = null;
		String sysCd2 = null;
		boolean isCusApiYn = false;
		List<String> cusApiYnMsg = new ArrayList<String>();

		List<IntrfcmsglayoutdtDto> msgList = dto.getIntrfcmsglayoutdtDto();

		for (IntrfcmsglayoutdtDto msg : msgList) {
			if (msg.getMsglayoutbsDto().getCustApiYn() != null && msg.getMsglayoutbsDto().getCustApiYn().equals("Y")) {
				if (!cusApiYnMsg.contains(msg.getMsgLayoutId())) {
					cusApiYnMsg.add(msg.getMsgLayoutId());
					isCusApiYn = true;
				}
			}
		}

		if (intrfcType.equals("MCI") && isCusApiYn) {
			for (IntrfcsrsysdtDto srcd : srCdList) {
				if (srcd.getSysCd().equals("COO") || srcd.getSysCd().equals("COB")) {
					sysCd1 = srcd.getSysCd();
				} else if (srcd.getSysCd().equals("OAG")) {
					sysCd2 = srcd.getSysCd();
				}
			}
			if (sysCd1 == null || sysCd2 == null) {
				throw new ServiceException(BxMessages.Error.CUS_API_YN_CHECK_ERR, "전문ID: " + cusApiYnMsg);
			}
		} else if (!intrfcType.equals("MCI") && isCusApiYn) {
			throw new ServiceException(BxMessages.Error.CUS_API_YN_CHECK_ERR, "전문ID: " + cusApiYnMsg);
		}
	}

	private void iokeyValid(List<MsglayoutbsDto> sendReqList, String string) {
		if (sendReqList.size() > 0) {
			Map<String, String> sendReqMap = new HashMap<>();
			for (MsglayoutbsDto dto : sendReqList) {

				logger.debug("dtoNm : {}", dto.getMsgLayoutId());

				if (dto.getMsglayoutdtDto() != null) {

					for (MsglayoutdtDto dtDto : dto.getMsglayoutdtDto()) {

						int depth = dtDto.getFldLvNo();
						String ioKey = dtDto.getIoKey();

						if (ioKey != null && !ioKey.equals("")) {
							if (depth > 0) {
								throw new ServiceException(BxMessages.Error.IO_KEY_DEPTH_EXCEPTION,
										"전문아이디: " + dtDto.getMsgLayoutId());
							}

							if (dtDto.getDataTypeNm().equals("LAYOUT")) {
								throw new ServiceException(BxMessages.Error.IO_KEY_LAYOUT_EXCEPTION,
										"전문아이디: " + dtDto.getMsgLayoutId(), "필드영문명: " + dtDto.getFldEngNm());
							}

							if (sendReqMap.containsKey(ioKey)) {
								throw new ServiceException(BxMessages.Error.IO_KEY_DUP_EXCEPTION,
										"전문아이디: " + dtDto.getMsgLayoutId(), "송수신요청응답 타입: " + string,
										"IOKey : " + ioKey);
							}

							sendReqMap.put(ioKey, ioKey);
						}
					}
				}

			}
		}

	}

	private void replKeyValid(List<MsglayoutbsDto> sendReqList, String string, IntrfccombsDto in, boolean screenYn) {
		if (sendReqList.size() > 0) {
			int replKeySize = 0;
			int privacySize = 0;

			for (MsglayoutbsDto dto : sendReqList) {

				logger.debug("dtoNmReplKey : {}", dto.getMsgLayoutId());

				if (dto.getMsgLayoutId() == null) {
					continue;
				}

				String chlDscd = dto.getChlDscd();
				String trxDscd = dto.getTrxDscd();

				if (dto.getMsglayoutdtDto() != null) {

					for (MsglayoutdtDto dtDto : dto.getMsglayoutdtDto()) {

						String privacyDscd = dtDto.getPrivacyDscd();
						String replKey = dtDto.getReplKey();

						if (privacyDscd != null && !privacyDscd.equals("") && !privacyDscd.equals("NONE")) {
							privacySize++;
						}

						if (replKey != null && !replKey.equals("")) {

							if (chlDscd.equals("EXTERNAL") && trxDscd.equals("ONLINE")) {
								if (dtDto.getDataTypeNm().equals("LAYOUT")) {
									throw new ServiceException(BxMessages.Error.REPL_KEY_LAYOUT_EXCEPTION,
											"전문아이디: " + dtDto.getMsgLayoutId(), "필드영문명: " + dtDto.getFldEngNm());
								}
								if (replKeySize > 0) {
									throw new ServiceException(BxMessages.Error.REPL_KEY_DUP_EXCEPTION,
											"전문아이디: " + dtDto.getMsgLayoutId(), "송수신요청응답 타입: " + string,
											"개인정보식별자 : " + replKey);
								}
								if (privacyDscd == null || privacyDscd.equals("") || privacyDscd.equals("NONE")) {
									throw new ServiceException(BxMessages.Error.REPL_KEY_PRIVACY_EXCEPTION2,
											"전문아이디: " + dtDto.getMsgLayoutId(), "송수신요청응답 타입: " + string,
											"개인정보식별자 : " + replKey);
								}

							} else if (chlDscd.equals("INTERNAL") && trxDscd.equals("ONLINE")) {
								if (dtDto.getDataTypeNm().equals("LAYOUT")) {
									throw new ServiceException(BxMessages.Error.REPL_KEY_LAYOUT_EXCEPTION,
											"전문아이디: " + dtDto.getMsgLayoutId(), "필드영문명: " + dtDto.getFldEngNm());
								}
								if (replKeySize > 0) {
									throw new ServiceException(BxMessages.Error.REPL_KEY_DUP_EXCEPTION,
											"전문아이디: " + dtDto.getMsgLayoutId(), "송수신요청응답 타입: " + string,
											"개인정보식별자 : " + replKey);
								}
							}

							replKeySize++;
						}

					}
				}

				if (chlDscd.equals("INTERNAL") && trxDscd.equals("ONLINE")) {
					if (privacySize < replKeySize) {
						throw new ServiceException(BxMessages.Error.REPL_KEY_PRIVACY_EXCEPTION4,
								"전문아이디: " + dto.getMsgLayoutId(), "송수신요청응답 타입: " + string);
					}
				}

				if (screenYn) {
					StringBuilder str = new StringBuilder();
					boolean arrayValid = validationArrayRef(dto, str);
					if (!arrayValid) {
						throw new ServiceException(BxMessages.Error.ARRAY_REF_VALIDATION, str.toString());
					}
				}

			}
			// if (privacySize > 0 && replKeySize <= 0) {
			// throw new ServiceException(BxMessages.Error.REPL_KEY_PRIVACY_EXCEPTION,
			// "송수신요청응답 타입: " + string);
			// }

			if (in.getIntrfcTypeCd().equals("FEP") && replKeySize > 0) {
				// FEP인 경우 개인정보제공사유가 비어있으면 안됨
				String privacyUse = in.getFepDto().getIntrfcUse();
				if (privacyUse == null || privacyUse.equals("")) {
					throw new ServiceException(BxMessages.Error.REPL_KEY_PRIVACY_EXCEPTION3, "송수신요청응답 타입: " + string);
				}
			}
		}

	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public int update(IntrfccombsDto in, boolean screenYn) {

		ActionhisthsDto actionhisthsDto = new ActionhisthsDto();

		IntrfccombsDto dtointrfc = intrfccombsDao.selectIntrfccombs(in.getIntrfcId());
		if (dtointrfc == null) {
			throw new ServiceException(BxMessages.Error.NOT_FOUNDED, in.getIntrfcId());
		}

		in.setRegManId(ServiceContext.getUserInfo().getUserId());
		in.setRegDttm(DateUtils.getCurrentDate(9));

		IntrfccombsRawDataDto rawDataDto = new IntrfccombsRawDataDto();
		String infTypeCd = in.getIntrfcTypeCd();

		if (infTypeCd.equals("MCI")) {
			rawDataDto.setMciDto(in.getMciDto());
		} else if (infTypeCd.equals("CC")) {
			rawDataDto.setCcDto(in.getCcDto());
		} else if (infTypeCd.equals("EAI_I") || infTypeCd.equals("EAI_E")) {
			rawDataDto.setEaiDto(in.getEaiDto());
			if (in.getIntrfcWayCd() != null && in.getIntrfcWayCd().equals("FILETOFILE")) {
				List<String> validWrapperList = new ArrayList<>();
				StringBuilder str = new StringBuilder();
				if (in.getEaiDto().getReqWrapperDtoNm() != null && !in.getEaiDto().getReqWrapperDtoNm().equals("")) {
					validWrapperList.add(in.getEaiDto().getReqWrapperDtoNm());
				}
				if (in.getEaiDto().getResWrapperDtoNm() != null && !in.getEaiDto().getResWrapperDtoNm().equals("")) {
					validWrapperList.add(in.getEaiDto().getResWrapperDtoNm());
				}
				boolean isValid = validationIoNmWrapper(in, validWrapperList, str);
				if (!isValid) {
					throw new ServiceException(BxMessages.Error.WRAPPER_IO_DUP, str.toString());
				}

				rawDataDto.setIntrfcMsgFieldEncodingDto(in.getIntrfcMsgFieldEncodingDto());
			}
		} else {
			if (in.getTrxDscd().equals("ONLINE")) {
				List<String> validWrapperList = new ArrayList<>();
				StringBuilder str = new StringBuilder();
				if (in.getFepDto().getReqWrapperDtoNm() != null && !in.getFepDto().getReqWrapperDtoNm().equals("")) {
					validWrapperList.add(in.getFepDto().getReqWrapperDtoNm());
				}
				if (in.getFepDto().getResWrapperDtoNm() != null && !in.getFepDto().getResWrapperDtoNm().equals("")) {
					validWrapperList.add(in.getFepDto().getResWrapperDtoNm());
				}
				boolean isValid = validationIoNmWrapper(in, validWrapperList, str);
				if (!isValid) {
					throw new ServiceException(BxMessages.Error.WRAPPER_IO_DUP, str.toString());
				}
			}

			rawDataDto.setFepDto(in.getFepDto());
		}

		if (in.getIntrfccombsMappingReqDto() != null) {
			rawDataDto.setIntrfccombsMappingReqDto(in.getIntrfccombsMappingReqDto());
		}
		if (in.getIntrfccombsMappingResDto() != null) {
			rawDataDto.setIntrfccombsMappingResDto(in.getIntrfccombsMappingResDto());
		}

		if (in.getMsgTrnsfrmYn() == null || in.getMsgTrnsfrmYn().equals("") || in.getMsgTrnsfrmYn().equals("N")) {
			rawDataDto.setIntrfccombsMappingReqDto(null);
			rawDataDto.setIntrfccombsMappingResDto(null);
		}

		String rawDataJson = null;

		try {
			rawDataJson = JsonUtils.objectToJson(rawDataDto);
			logger.debug(
					"===========================rawDataJson======================================================");
			logger.debug(rawDataJson);
			logger.debug(
					"===========================rawDataJson======================================================");
		} catch (JsonParseException e) {
			logger.error("{}", e);
			throw new ServiceException(BxMessages.Error.SYSTEM_EXCEPTION);
		} catch (JsonMappingException e) {
			logger.error("{}", e);
			throw new ServiceException(BxMessages.Error.SYSTEM_EXCEPTION);
		} catch (IOException e) {
			logger.error("{}", e);
			throw new ServiceException(BxMessages.Error.SYSTEM_EXCEPTION);
		}

		in.setRawData(rawDataJson);

		boolean isWorking = false;
		
		if (in.getIntrfcmsglayoutdtDto() != null) {
			logger.debug("##1##");
			for (IntrfcmsglayoutdtDto dto : in.getIntrfcmsglayoutdtDto()) {
				logger.debug("##2##");
				String msgWorkStatus = dto.getMsglayoutbsDto().getWorkStatusCd();
				if (msgWorkStatus != null && msgWorkStatus.equals("WORKING")) {
					logger.debug("##3##");
					MsglayoutbsDto msgDto = msglayoutbsDao.selectMsglayoutbs(dto.getMsglayoutbsDto().getMsgLayoutId());
					if (msgDto != null && msgDto.getWorkStatusCd().equals("WORKING")) {
						logger.debug("##4##");
						isWorking = true;
					}
				}
			}
		}

		String workStatus = null;

		if (!isWorking) {
			workStatus = validationInterface(in, screenYn);
			// cusApiYnValid(in);
		}

		if (screenYn && !isWorking) {
			in.setWorkStatusCd(workStatus);
		}
		if (screenYn && isWorking) {
			in.setWorkStatusCd("WORKING");
		}
		
		in.setCrtnSeq(dtointrfc.getCrtnSeq());
		
        if(in.getViewId() == null || in.getViewId().equals("") ) {
        	in.setViewId("NONE");
        }
        
		int out = intrfccombsDao.updateIntrfccombs(in);

		if (out == 0) {
			throw new ServiceException(BxMessages.Error.NOT_FOUNDED, in.getIntrfcId());
		}

		intrfcsrsysdtDao.deleteIntrfcsrsysdt(in.getIntrfcId());
		for (IntrfcsrsysdtDto intrfcsrsysdtDto : in.getIntrfcsrsysdtDto()) {
			intrfcsrsysdtDao.insertIntrfcsrsysdt(intrfcsrsysdtDto);
		}

		if (screenYn) {
			intrfcdeploysysdtDao.deleteIntrfcdeploysysdt(in.getIntrfcId());
			if (in.getIntrfcdeploysysdtDto() != null) {
				for (IntrfcdeploysysdtDto intrfcdeploysysdtDto : in.getIntrfcdeploysysdtDto()) {
					intrfcdeploysysdtDao.insertIntrfcdeploysysdt(intrfcdeploysysdtDto);
				}
			}
		}
		// else {
		// List<IntrfcdeploysysdtDto> listDeploySys =
		// intrfcdeploysysdtDao.selectIntrfcdeploysysdt(in.getIntrfcId()) ;
		// if(listDeploySys.size() > 0) {
		// intrfcdeploysysdtDao.deleteIntrfcdeploysysdt(in.getIntrfcId());
		// if (in.getIntrfcdeploysysdtDto() != null) {
		// for (IntrfcdeploysysdtDto intrfcdeploysysdtDto :
		// in.getIntrfcdeploysysdtDto()) {
		// intrfcdeploysysdtDao.insertIntrfcdeploysysdt(intrfcdeploysysdtDto);
		// }
		// }
		// }
		// }

		intrfcroutinfodtDao.deleteIntrfcroutinfodt(in.getIntrfcId());
		if (in.getIntrfcroutinfodtDto() != null) {
			for (IntrfcroutinfodtDto intrfcroutinfodtDto : in.getIntrfcroutinfodtDto()) {
				intrfcroutinfodtDao.insertIntrfcroutinfodt(intrfcroutinfodtDto);
			}
		}
		Map<String, String> aleadyInsert = new HashMap<String, String>();

		intrfcmsglayoutdtDao.deleteIntrfcmsglayoutdt(in.getIntrfcId());

		if (in.getIntrfcmsglayoutdtDto() != null) {
			for (IntrfcmsglayoutdtDto layoutDto : in.getIntrfcmsglayoutdtDto()) {

				int totalCount = 0;

				if (layoutDto.getMsgLayoutId() != null && !layoutDto.getMsgLayoutId().equals(""))
					totalCount = msglayoutbsDao.selectAllCnt(null, null, 0, null, null, null, null, null, null, null,
							layoutDto.getMsgLayoutId(), null, null, null, null, null, null, null, null, null, null,
							null, null, null, null, null, null, null, null, null, null, null, null, 0);

				if (totalCount == 0) { // 조회된 메시지가 없을 경우 등록으로 처리
					// if (!aleadyInsert.containsKey(layoutDto.getMsgLayoutId())) {
					// String seq = seqService.createPrefixGuid(BxConstants.Guid.MESSAGE);
					// layoutDto.setMsgLayoutId(seq);
					// layoutDto.getMsglayoutbsDto().setMsgLayoutId(seq);
					// msglayoutService.add(layoutDto.getMsglayoutbsDto());
					// intrfcmsglayoutdtDao.insertIntrfcmsglayoutdt(layoutDto);
					// aleadyInsert.put(layoutDto.getMsgLayoutId(), "Insert");
					// }
					throw new ServiceException(BxMessages.Error.NOT_FOUNDED);

				} else { // 조회된 기존 메시지가 있을 경우, 업데이터 형식으로 처리
					// if (!aleadyInsert.containsKey(layoutDto.getMsgLayoutId())) {
					// msglayoutService.update(layoutDto.getMsglayoutbsDto());
					intrfcmsglayoutdtDao.insertIntrfcmsglayoutdt(layoutDto);
					// aleadyInsert.put(layoutDto.getMsgLayoutId(), "Update");
					// }
				}
			}
		}

		actionhisthsDto.setHstDscd("INTERFACE");
		actionhisthsDto.setItemDesc(in.getIntrfcNm());
		actionhisthsDto.setItemId(in.getIntrfcId());
		actionhisthsDto.setUserId(ServiceContext.getUserInfo().getUserId());
		actionhisthsDto.setWorkCttCd("UPDATE");
		actionhisthsDto.setWorkDttm(DateUtils.getCurrentDate(10));

		actionhistService.add(actionhisthsDto);

		return out;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public int updateDefinition(IntrfccombsDto in, boolean screenYn) {

		ActionhisthsDto actionhisthsDto = new ActionhisthsDto();

		IntrfccombsDto dtointrfc = intrfccombsDao.selectIntrfccombs(in.getIntrfcId());
		if (dtointrfc == null) {
			throw new ServiceException(BxMessages.Error.NOT_FOUNDED, in.getIntrfcId());
		}

		in.setRegManId(ServiceContext.getUserInfo().getUserId());
		in.setRegDttm(DateUtils.getCurrentDate(9));

		IntrfccombsRawDataDto rawDataDto = new IntrfccombsRawDataDto();
		String infTypeCd = in.getIntrfcTypeCd();

		if (infTypeCd.equals("MCI")) {
			rawDataDto.setMciDto(in.getMciDto());
		} else if (infTypeCd.equals("CC")) {
			rawDataDto.setCcDto(in.getCcDto());
		} else if (infTypeCd.equals("EAI_I") || infTypeCd.equals("EAI_E")) {
			rawDataDto.setEaiDto(in.getEaiDto());
			if (in.getIntrfcWayCd() != null && in.getIntrfcWayCd().equals("FILETOFILE")) {
				List<String> validWrapperList = new ArrayList<>();
				StringBuilder str = new StringBuilder();
				if (in.getEaiDto().getReqWrapperDtoNm() != null && !in.getEaiDto().getReqWrapperDtoNm().equals("")) {
					validWrapperList.add(in.getEaiDto().getReqWrapperDtoNm());
				}
				if (in.getEaiDto().getResWrapperDtoNm() != null && !in.getEaiDto().getResWrapperDtoNm().equals("")) {
					validWrapperList.add(in.getEaiDto().getResWrapperDtoNm());
				}
				boolean isValid = validationIoNmWrapper(in, validWrapperList, str);
				if (!isValid) {
					throw new ServiceException(BxMessages.Error.WRAPPER_IO_DUP, str.toString());
				}

				rawDataDto.setIntrfcMsgFieldEncodingDto(in.getIntrfcMsgFieldEncodingDto());
			}
		} else {
			if (in.getTrxDscd().equals("ONLINE")) {
				List<String> validWrapperList = new ArrayList<>();
				StringBuilder str = new StringBuilder();
				if (in.getFepDto().getReqWrapperDtoNm() != null && !in.getFepDto().getReqWrapperDtoNm().equals("")) {
					validWrapperList.add(in.getFepDto().getReqWrapperDtoNm());
				}
				if (in.getFepDto().getResWrapperDtoNm() != null && !in.getFepDto().getResWrapperDtoNm().equals("")) {
					validWrapperList.add(in.getFepDto().getResWrapperDtoNm());
				}
				boolean isValid = validationIoNmWrapper(in, validWrapperList, str);
				if (!isValid) {
					throw new ServiceException(BxMessages.Error.WRAPPER_IO_DUP, str.toString());
				}
			}

			rawDataDto.setFepDto(in.getFepDto());
		}

		if (in.getIntrfccombsMappingReqDto() != null) {
			rawDataDto.setIntrfccombsMappingReqDto(in.getIntrfccombsMappingReqDto());
		}
		if (in.getIntrfccombsMappingResDto() != null) {
			rawDataDto.setIntrfccombsMappingResDto(in.getIntrfccombsMappingResDto());
		}

		if (in.getMsgTrnsfrmYn() == null || in.getMsgTrnsfrmYn().equals("") || in.getMsgTrnsfrmYn().equals("N")) {
			rawDataDto.setIntrfccombsMappingReqDto(null);
			rawDataDto.setIntrfccombsMappingResDto(null);
		}

		String rawDataJson = null;

		try {
			rawDataJson = JsonUtils.objectToJson(rawDataDto);
			logger.debug(
					"===========================rawDataJson======================================================");
			logger.debug(rawDataJson);
			logger.debug(
					"===========================rawDataJson======================================================");
		} catch (JsonParseException e) {
			logger.error("{}", e);
			throw new ServiceException(BxMessages.Error.SYSTEM_EXCEPTION);
		} catch (JsonMappingException e) {
			logger.error("{}", e);
			throw new ServiceException(BxMessages.Error.SYSTEM_EXCEPTION);
		} catch (IOException e) {
			logger.error("{}", e);
			throw new ServiceException(BxMessages.Error.SYSTEM_EXCEPTION);
		}

		in.setRawData(rawDataJson);

		boolean isWorking = false;

		if (in.getIntrfcmsglayoutdtDto() != null) {
			for (IntrfcmsglayoutdtDto dto : in.getIntrfcmsglayoutdtDto()) {
				String msgWorkStatus = dto.getMsglayoutbsDto().getWorkStatusCd();
				if (msgWorkStatus != null && msgWorkStatus.equals("WORKING")) {
					MsglayoutbsDto msgDto = msglayoutbsDao.selectMsglayoutbs(dto.getMsglayoutbsDto().getMsgLayoutId());
					if (msgDto != null && msgDto.getWorkStatusCd().equals("WORKING")) {
						isWorking = true;
					}
				}
			}
		}

		String workStatus = null;

		in.setCrtnSeq(dtointrfc.getCrtnSeq());

		int out = intrfccombsDao.updateIntrfccombs(in);

		if (out == 0) {
			throw new ServiceException(BxMessages.Error.NOT_FOUNDED, in.getIntrfcId());
		}

		intrfcsrsysdtDao.deleteIntrfcsrsysdt(in.getIntrfcId());
		for (IntrfcsrsysdtDto intrfcsrsysdtDto : in.getIntrfcsrsysdtDto()) {
			intrfcsrsysdtDao.insertIntrfcsrsysdt(intrfcsrsysdtDto);
		}

		if (screenYn) {
			intrfcdeploysysdtDao.deleteIntrfcdeploysysdt(in.getIntrfcId());
			if (in.getIntrfcdeploysysdtDto() != null) {
				for (IntrfcdeploysysdtDto intrfcdeploysysdtDto : in.getIntrfcdeploysysdtDto()) {
					intrfcdeploysysdtDao.insertIntrfcdeploysysdt(intrfcdeploysysdtDto);
				}
			}
		}

		intrfcroutinfodtDao.deleteIntrfcroutinfodt(in.getIntrfcId());
		if (in.getIntrfcroutinfodtDto() != null) {
			for (IntrfcroutinfodtDto intrfcroutinfodtDto : in.getIntrfcroutinfodtDto()) {
				intrfcroutinfodtDao.insertIntrfcroutinfodt(intrfcroutinfodtDto);
			}
		}
		Map<String, String> aleadyInsert = new HashMap<String, String>();

		intrfcmsglayoutdtDao.deleteIntrfcmsglayoutdt(in.getIntrfcId());

		if (in.getIntrfcmsglayoutdtDto() != null) {
			for (IntrfcmsglayoutdtDto layoutDto : in.getIntrfcmsglayoutdtDto()) {

				int totalCount = 0;

				if (layoutDto.getMsgLayoutId() != null && !layoutDto.getMsgLayoutId().equals(""))
					totalCount = msglayoutbsDao.selectAllCnt(null, null, 0, null, null, null, null, null, null, null,
							layoutDto.getMsgLayoutId(), null, null, null, null, null, null, null, null, null, null,
							null, null, null, null, null, null, null, null, null, null, null, null, 0);

				if (totalCount == 0) { // 조회된 메시지가 없을 경우 등록으로 처리
					// if (!aleadyInsert.containsKey(layoutDto.getMsgLayoutId())) {
					// String seq = seqService.createPrefixGuid(BxConstants.Guid.MESSAGE);
					// layoutDto.setMsgLayoutId(seq);
					// layoutDto.getMsglayoutbsDto().setMsgLayoutId(seq);
					// msglayoutService.add(layoutDto.getMsglayoutbsDto());
					// intrfcmsglayoutdtDao.insertIntrfcmsglayoutdt(layoutDto);
					// aleadyInsert.put(layoutDto.getMsgLayoutId(), "Insert");
					// }
					throw new ServiceException(BxMessages.Error.NOT_FOUNDED);

				} else { // 조회된 기존 메시지가 있을 경우, 업데이터 형식으로 처리
					// if (!aleadyInsert.containsKey(layoutDto.getMsgLayoutId())) {
					// msglayoutService.update(layoutDto.getMsglayoutbsDto());
					intrfcmsglayoutdtDao.insertIntrfcmsglayoutdt(layoutDto);
					// aleadyInsert.put(layoutDto.getMsgLayoutId(), "Update");
					// }
				}
			}
		}

		actionhisthsDto.setHstDscd("INTERFACE");
		actionhisthsDto.setItemDesc(in.getIntrfcNm());
		actionhisthsDto.setItemId(in.getIntrfcId());
		actionhisthsDto.setUserId(ServiceContext.getUserInfo().getUserId());
		actionhisthsDto.setWorkCttCd("UPDATE");
		actionhisthsDto.setWorkDttm(DateUtils.getCurrentDate(10));

		actionhistService.add(actionhisthsDto);

		return out;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public int delete(String intrfcId) {

		ActionhisthsDto actionhisthsDto = new ActionhisthsDto();

		IntrfccombsDto intfInfo = intrfccombsDao.selectIntrfccombs(intrfcId);

		intrfcmsglayoutdtDao.deleteIntrfcmsglayoutdt(intrfcId);
		intrfcsrsysdtDao.deleteIntrfcsrsysdt(intrfcId);
		intrfcdeploysysdtDao.deleteIntrfcdeploysysdt(intrfcId);
		intrfcdeployhisthsDao.deleteIntrfcdeployhisths(intrfcId);
		intrfcroutinfodtDao.deleteIntrfcroutinfodt(intrfcId);
		int out = intrfccombsDao.deleteIntrfccombs(intrfcId);

		if (out == 0) {
			throw new ServiceException(BxMessages.Error.NOT_FOUNDED, intrfcId);
		}

		actionhisthsDto.setHstDscd("INTERFACE");
		actionhisthsDto.setItemDesc(intfInfo.getIntrfcNm());
		actionhisthsDto.setItemId(intrfcId);
		actionhisthsDto.setUserId(ServiceContext.getUserInfo().getUserId());
		actionhisthsDto.setWorkCttCd("DELETE");
		actionhisthsDto.setWorkDttm(DateUtils.getCurrentDate(10));

		actionhistService.add(actionhisthsDto);

		return out;
	}

	// @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public UiIntrfcDeployResponse deploy(IntrfccombsDto inUi) {
		IntrfcDeploy deployDto = new IntrfcDeploy();
		IntrfcInfo info = new IntrfcInfo();
		IntrfccombsDto in = this.get(inUi.getIntrfcId());

		if (in.getWorkStatusCd().equals("WORKING")) {
			throw new ServiceException(BxMessages.Error.DEPLOY_STATUS_ERR);
		}

		// String msgTranYn = in.getMsgTrnsfrmYn();
		String trxDscd = in.getTrxDscd();
		validationInterface(in, true);
		// cusApiYnValid(in);

		String compulsionDeployYn = inUi.getCompulsionDeployYn();
		String mappingYn = in.getMsgTrnsfrmYn();

		if (mappingYn != null && mappingYn.equals("Y")) {
			if (compulsionDeployYn != null && !compulsionDeployYn.equals("")) {
				if (compulsionDeployYn.equals("N")) {
					if (trxDscd.equals("ONLINE")) {
						String intrfcDate = in.getRegDttm();
						boolean isMsgLayoutTran = false;
						StringBuilder msgLayoutTranList = new StringBuilder();

						List<IntrfcmsglayoutdtDto> intrfcmsglayoutdtDto = in.getIntrfcmsglayoutdtDto();

						msgLayoutTranList.append("\n전문ID: ");
						for (IntrfcmsglayoutdtDto intfDto : intrfcmsglayoutdtDto) {
							try {
								MsglayoutbsDto msglayoutbsDto = msglayoutService.get(intfDto.getMsgLayoutId());
								String msgDate = msglayoutbsDto.getRegDttm();

								if (intrfcDate.compareTo(msgDate) < 0) {
									isMsgLayoutTran = true;
									msgLayoutTranList.append("[" + intfDto.getMsgLayoutId() + "] ");
								}

								intfDto.setMsglayoutbsDto(msglayoutbsDto);
							} catch (ServiceException e) {
								intfDto.setMsglayoutbsDto(null);
							}

						}

						if (isMsgLayoutTran) {
							throw new ServiceException(BxMessages.Error.MSG_TRANS_MAPPING_EXCEPTION, in.getIntrfcId());
						}
					}

				}
			}
		}

		for (IntrfcmsglayoutdtDto dto : in.getIntrfcmsglayoutdtDto()) {

			boolean isValidMsg = true;
			StringBuilder str = new StringBuilder();
			isValidMsg = msglayoutService.msgDefaultValid(dto.getMsglayoutbsDto(), isValidMsg, str);
			if (!isValidMsg) {
				throw new ServiceException(BxMessages.Error.MSG_DEFAULT_VALUE_SET_ERR, str.toString());
			}

			boolean isIoNmValid = msglayoutService.validationIoNm(dto.getMsglayoutbsDto(), str);
			if (!isIoNmValid) {
				throw new ServiceException(BxMessages.Error.IO_NM_DUP_ERR, str.toString());
			}
		}

		String infTypeCd = in.getIntrfcTypeCd();

		if (infTypeCd.equals("EAI_I") || infTypeCd.equals("EAI_E")) {
			if (in.getIntrfcWayCd() != null && in.getIntrfcWayCd().equals("FILETOFILE")) {
				List<String> validWrapperList = new ArrayList<>();
				StringBuilder str = new StringBuilder();
				if (in.getEaiDto().getReqWrapperDtoNm() != null && !in.getEaiDto().getReqWrapperDtoNm().equals("")) {
					validWrapperList.add(in.getEaiDto().getReqWrapperDtoNm());
				}
				if (in.getEaiDto().getResWrapperDtoNm() != null && !in.getEaiDto().getResWrapperDtoNm().equals("")) {
					validWrapperList.add(in.getEaiDto().getResWrapperDtoNm());
				}
				boolean isValid = validationIoNmWrapper(in, validWrapperList, str);
				if (!isValid) {
					throw new ServiceException(BxMessages.Error.WRAPPER_IO_DUP, str.toString());
				}
			}
		} else if (infTypeCd.equals("FEP")) {
			if (in.getTrxDscd().equals("ONLINE")) {
				List<String> validWrapperList = new ArrayList<>();
				StringBuilder str = new StringBuilder();
				if (in.getFepDto().getReqWrapperDtoNm() != null && !in.getFepDto().getReqWrapperDtoNm().equals("")) {
					validWrapperList.add(in.getFepDto().getReqWrapperDtoNm());
				}
				if (in.getFepDto().getResWrapperDtoNm() != null && !in.getFepDto().getResWrapperDtoNm().equals("")) {
					validWrapperList.add(in.getFepDto().getResWrapperDtoNm());
				}
				boolean isValid = validationIoNmWrapper(in, validWrapperList, str);
				if (!isValid) {
					throw new ServiceException(BxMessages.Error.WRAPPER_IO_DUP, str.toString());
				}
			}
		}

		IntrfcdeployhisthsDto deployHistList = intrfcdeployhisthsDao.selectIntrfcdeployhisthsOneRow(in.getIntrfcId());

		int deployVersion = 1;

		if (deployHistList != null) {
			deployVersion = deployHistList.getDeployVersion() + 1;
		}

		deployDataSet(info, in);

		deployDto.setDeployStatus("DEPLOY");
		deployDto.setDeployer(ServiceContext.getUserInfo().getUserId());
		deployDto.setDeployTime(DateUtils.getCurrentDate(9));
		deployDto.setIntrfcId(in.getIntrfcId());
		deployDto.setIntrfccombsDto(info);
		deployDto.setDeployVersion(Integer.toString(deployVersion));

		String deployStatus = "SUCCESS";

		String deployJson = "";
		IntrfcDeployResponse deployResult = null;
		UiIntrfcDeployResponse uiResponse = new UiIntrfcDeployResponse();
		List<IntrfcdeployInfoDto> deplInfoList = new ArrayList<IntrfcdeployInfoDto>();
		List<IntrfcDeployResponse> intrfcDeployResponse = new ArrayList<IntrfcDeployResponse>();

		for (IntrfcdeploysysdtDto deploySys : in.getIntrfcdeploysysdtDto()) {
			try {
				if (!deploySys.getDeployUrl().equals("NotReady")) {
					IntrfcdeployInfoDto deplInfo = new IntrfcdeployInfoDto();
					String deploySysCode = deploySys.getDeploySysCd();

					// if (deploySysCode.equals("MCI_I") || deploySysCode.equals("MCI_E")) {
					// deploySysCode = "MCI";
					// }

					deployDto.setDeploySysCd(deploySysCode);
					deployJson = JsonUtils.objectToJson(deployDto);
					deplInfo.setDeploySysCd(deploySys.getDeploySysCd());
					deplInfo.setDeployUrl(deploySys.getDeployUrl());
					deplInfo.setJsonData(deployJson);
					deplInfoList.add(deplInfo);
				} 
			} catch (Exception e) {

				StringBuilder deplSysBuilder = new StringBuilder();
				int i = 1;
				for (IntrfcdeployInfoDto depl : deplInfoList) {
					deplSysBuilder.append(depl.getDeploySysCd());
					if (i < deplInfoList.size()) {
						deplSysBuilder.append("/");
					}

					i++;
				}

				IntrfcdeployhisthsDto intrfcdeployhisths = new IntrfcdeployhisthsDto();
				intrfcdeployhisths.setIntrfcId(in.getIntrfcId());
				intrfcdeployhisths.setDeployDttm(DateUtils.getCurrentDate(9));
				intrfcdeployhisths.setDeploySysCd(deplSysBuilder.toString());
				intrfcdeployhisths.setDeployResultCd("FAIL");
				intrfcdeployhisths.setDeployVersion(0);
				intrfcdeployhisths.setRawData("");
				intrfcdeployhisthsDao.insertIntrfcdeployhisths(intrfcdeployhisths);

				// 히스토리 쌓기
				ActionhisthsDto actionhisthsDto = new ActionhisthsDto();
				actionhisthsDto.setHstDscd("INTERFACE");
				actionhisthsDto.setItemDesc(in.getIntrfcNm());
				actionhisthsDto.setItemId(in.getIntrfcId());
				actionhisthsDto.setUserId(ServiceContext.getUserInfo().getUserId());
				actionhisthsDto.setWorkCttCd("DEPLOYFAIL");
				actionhisthsDto.setWorkDttm(DateUtils.getCurrentDate(10));
				actionhistService.add(actionhisthsDto);

				logger.error("", e);
				throw new ServiceException(BxMessages.Error.SYSTEM_EXCEPTION);
			}
		}

		Map<String, String> failCodeMap = new HashMap<String, String>();
		String failStatus = "SUCCESS";
		logger.debug("deplInfoList.size: {}", deplInfoList.size());
		for (IntrfcdeployInfoDto depl : deplInfoList) {
			try {
				byte[] resData = null;

				System.out.println("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz");
				System.out.println(new String(depl.getJsonData().getBytes(), "UTF-8"));
				System.out.println("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz");

				String resString = null;
				logger.debug(
						"!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!Not Ready!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				deployStatus = "SUCCESS";
				resData = RestUtils.sendRestPost(depl.getDeployUrl(), depl.getJsonData().getBytes("UTF-8"),
						byte[].class);
				resString = new String(resData, "UTF-8");
				logger.debug(
						"##########################################################################################");
				logger.debug(
						"##########################################################################################");
				logger.debug(
						"##########################################################################################");
				logger.debug(resString);
				logger.debug(
						"##########################################################################################");
				logger.debug(
						"##########################################################################################");
				logger.debug(
						"##########################################################################################");
				deployResult = JsonUtils.jsonToObect(resString, IntrfcDeployResponse.class);

				if (deployResult.getDeployStatus().equals("FAIL") || deployResult.getDeployStatus().equals("")
						|| deployResult.getDeployStatus() == null) {

					deployResult.setDeployStatus("FAIL");
					deployStatus = "FAIL";
					failStatus = "FAIL";
					failCodeMap.put(depl.getDeploySysCd(), "sysCd");
				}

				deployResult.setSystemCd(depl.getDeploySysCd());

				// String deploySysCd = deployResult.getSystemCd();
				// if (deploySysCd != null && !deploySysCd.equals("")) {
				// if (deploySysCd.equals("MCI")) {
				// deployResult.setSystemCd(depl.getDeploySysCd());
				// }
				// }
				//
				// if(deploySysCd == null || deploySysCd.equals("")) {
				// deployResult.setSystemCd(depl.getDeploySysCd());
				// }else if() {
				//
				// }

				intrfcDeployResponse.add(deployResult);
				// uiResponse.getIntrfcDeployResponse().add(deployResult);
			} catch (Exception e) {
				logger.error("{}", e);
				deployStatus = "FAIL";
				failStatus = "FAIL";
				IntrfcDeployResponse res = new IntrfcDeployResponse();
				res.setDeployStatus("FAIL");
				res.setMessage(e.getMessage());
				res.setSystemCd(depl.getDeploySysCd());
				intrfcDeployResponse.add(res);
				failCodeMap.put(depl.getDeploySysCd(), "sysCd");
				// uiResponse.getIntrfcDeployResponse().add(res);
			}
		}

		// deployStatus = "SUCCESS";

		/*
		 * 이전 배포 히스토리가 없을 경우, 실패 시 삭제 처리 데이터 생성 후 쏨 이전 배포 히스토리가 있을 경우, 성공한 시스템에만 이전버전
		 * 데이터 쏴줌
		 */
		// IntrfcdeployhisthsDto deployHistList =
		// intrfcdeployhisthsDao.selectIntrfcdeployhisthsOneRow(in.getIntrfcId());
		if (deployStatus.equals("FAIL") || failStatus.equals("FAIL")) {

			if (deployHistList == null) {
				// 디플로이 히스토리가 없을 경우 삭제 데이터 전송
				for (IntrfcdeployInfoDto depl : deplInfoList) {
					try {
						deployDto.setDeployStatus("DELETE");
						// deployDto.setIntrfccombsDto(null);

						String deploySysCode = depl.getDeploySysCd();

						if (!failCodeMap.containsKey(deploySysCode)) {
							deployDto.setDeploySysCd(deploySysCode);
							String deleteData = JsonUtils.objectToJson(deployDto);
							RestUtils.sendRestPost(depl.getDeployUrl(), deleteData.getBytes("UTF-8"), byte[].class);
						} else {
							logger.debug("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
							logger.debug(deploySysCode);
							logger.debug("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
						}
						// resString = new String(resData, "UTF-8");
						// IntrfcDeployResponse reDeployResult = JsonUtils.jsonToObect(resString,
						// IntrfcDeployResponse.class);
					} catch (Exception e) {
						// throw new ServiceException(BxMessages.Error.SYSTEM_EXCEPTION);
						logger.error("{}", e);
					}
				}
			} else {
				// 디플로이 히스토리가 있을 경우 마지막 배포 히스토리 전송
				IntrfccombsDto deployDtoHist = new IntrfccombsDto();
				try {
					// deployDtoHist = new IntrfccombsDto();
					String historyRawData = deployHistList.getRawData();
					deployDtoHist = JsonUtils.jsonToObect(historyRawData, deployDtoHist.getClass());
				} catch (Exception e) {
					logger.error("{}", e);
				}

				IntrfcDeploy deployDto2 = new IntrfcDeploy();
				IntrfcInfo info2 = new IntrfcInfo();

				deployDataSet(info2, deployDtoHist);

				deployDto2.setDeployStatus("ROLLBACK");
				deployDto2.setDeployer(ServiceContext.getUserInfo().getUserId());
				deployDto2.setDeployTime(DateUtils.getCurrentDate(9));
				deployDto2.setIntrfcId(deployDtoHist.getIntrfcId());
				deployDto2.setIntrfccombsDto(info2);
				deployDto2.setDeployVersion(Integer.toString(deployHistList.getDeployVersion()));

				for (IntrfcdeployInfoDto depl : deplInfoList) {
					try {

						String deploySysCode = depl.getDeploySysCd();

						if (!failCodeMap.containsKey(deploySysCode)) {
							deployDto2.setDeploySysCd(deploySysCode);
							String rollbackData = JsonUtils.objectToJson(deployDto2);
							RestUtils.sendRestPost(depl.getDeployUrl(), rollbackData.getBytes("UTF-8"), byte[].class);
						} else {
							logger.debug("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
							logger.debug(deploySysCode);
							logger.debug("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
						}
					} catch (Exception e) {
						logger.error("{}", e);
					}
				}

			}

			StringBuilder deplSysBuilder = new StringBuilder();
			int i = 1;
			for (IntrfcdeployInfoDto depl : deplInfoList) {
				deplSysBuilder.append(depl.getDeploySysCd());
				if (i < deplInfoList.size()) {
					deplSysBuilder.append("/");
				}

				i++;
			}

			String resultRawData = null;
			try {
				IntrfcDeployResponseList list = new IntrfcDeployResponseList();
				List<IntrfcDeployResponseResult> resultList = new ArrayList<IntrfcDeployResponseResult>();

				for (IntrfcDeployResponse dto : intrfcDeployResponse) {
					IntrfcDeployResponseResult resultDto = new IntrfcDeployResponseResult();
					String deploySyscd = dto.getSystemCd();
					DepolysysbsDto deploySysDto = deployDao.selectDepolysysbs(deploySyscd);

					resultDto.setSystemCd(deploySyscd);
					resultDto.setDeployStatus(dto.getDeployStatus());
					resultDto.setMessage(dto.getMessage());

					if (deploySysDto != null) {
						resultDto.setSystemNm(deploySysDto.getDeploySysNm());
						resultDto.setSystemUrl(deploySysDto.getDeploySysUrl());
					}
					resultList.add(resultDto);
				}

				list.setIntrfcDeployResponseList(resultList);
				resultRawData = JsonUtils.objectToJson(list);
				logger.debug("----------------------resultRawData--------------------------");
				logger.debug(resultRawData);
				logger.debug("----------------------resultRawData--------------------------");
			} catch (JsonParseException e) {
				logger.error("{}", e);
			} catch (JsonMappingException e) {
				logger.error("{}", e);
			} catch (IOException e) {
				logger.error("{}", e);
			}

			IntrfcdeployhisthsDto intrfcdeployhisths = new IntrfcdeployhisthsDto();
			intrfcdeployhisths.setIntrfcId(in.getIntrfcId());
			intrfcdeployhisths.setDeployDttm(DateUtils.getCurrentDate(9));
			intrfcdeployhisths.setDeploySysCd(deplSysBuilder.toString());
			intrfcdeployhisths.setDeployResultCd("FAIL");
			intrfcdeployhisths.setDeployVersion(0);
			intrfcdeployhisths.setRawData("");
			intrfcdeployhisths.setResultRawData(resultRawData);
			intrfcdeployhisthsDao.insertIntrfcdeployhisths(intrfcdeployhisths);

			// 히스토리 쌓기
			ActionhisthsDto actionhisthsDto = new ActionhisthsDto();
			actionhisthsDto.setHstDscd("INTERFACE");
			actionhisthsDto.setItemDesc(in.getIntrfcNm());
			actionhisthsDto.setItemId(in.getIntrfcId());
			actionhisthsDto.setUserId(ServiceContext.getUserInfo().getUserId());
			actionhisthsDto.setWorkCttCd("DEPLOYFAIL");
			actionhisthsDto.setWorkDttm(DateUtils.getCurrentDate(10));
			actionhistService.add(actionhisthsDto);

		} else {

			// 히스토리 쌓기
			ActionhisthsDto actionhisthsDto = new ActionhisthsDto();
			actionhisthsDto.setHstDscd("INTERFACE");
			actionhisthsDto.setItemDesc(in.getIntrfcNm());
			actionhisthsDto.setItemId(in.getIntrfcId());
			actionhisthsDto.setUserId(ServiceContext.getUserInfo().getUserId());
			actionhisthsDto.setWorkCttCd("DEPLOY");
			actionhisthsDto.setWorkDttm(DateUtils.getCurrentDate(10));
			actionhistService.add(actionhisthsDto);

			String histJsonData = null;
			StringBuilder deplSysBuilder = new StringBuilder();
			int i = 1;
			for (IntrfcdeployInfoDto depl : deplInfoList) {
				deplSysBuilder.append(depl.getDeploySysCd());
				if (i < deplInfoList.size()) {
					deplSysBuilder.append("/");
				}

				i++;
			}

			String resultRawData = null;
			try {
				IntrfcDeployResponseList list = new IntrfcDeployResponseList();
				List<IntrfcDeployResponseResult> resultList = new ArrayList<IntrfcDeployResponseResult>();

				for (IntrfcDeployResponse dto : intrfcDeployResponse) {
					IntrfcDeployResponseResult resultDto = new IntrfcDeployResponseResult();
					String deploySyscd = dto.getSystemCd();
					DepolysysbsDto deploySysDto = deployDao.selectDepolysysbs(deploySyscd);

					resultDto.setSystemCd(deploySyscd);
					resultDto.setDeployStatus(dto.getDeployStatus());
					resultDto.setMessage(dto.getMessage());

					if (deploySysDto != null) {
						resultDto.setSystemNm(deploySysDto.getDeploySysNm());
						resultDto.setSystemUrl(deploySysDto.getDeploySysUrl());
					}
					resultList.add(resultDto);
				}

				list.setIntrfcDeployResponseList(resultList);
				resultRawData = JsonUtils.objectToJson(list);
				logger.debug("----------------------resultRawData--------------------------");
				logger.debug(resultRawData);
				logger.debug("----------------------resultRawData--------------------------");
			} catch (JsonParseException e) {
				logger.error("{}", e);
			} catch (JsonMappingException e) {
				logger.error("{}", e);
			} catch (IOException e) {
				logger.error("{}", e);
			}

			IntrfcdeployhisthsDto intrfcdeployhisths = new IntrfcdeployhisthsDto();
			intrfcdeployhisths.setIntrfcId(in.getIntrfcId());
			intrfcdeployhisths.setDeployDttm(DateUtils.getCurrentDate(9));
			intrfcdeployhisths.setDeployResultCd("SUCCESS");
			intrfcdeployhisths.setDeploySysCd(deplSysBuilder.toString());
			if (deployHistList != null) {
				intrfcdeployhisths.setDeployVersion(deployHistList.getDeployVersion() + 1);
			} else {
				intrfcdeployhisths.setDeployVersion(1);
			}
			String rawData = null;
			try {
				rawData = JsonUtils.objectToJson(in);
			} catch (Exception e) {
				logger.error("{}", e);
			}
			intrfcdeployhisths.setRawData(rawData);
			intrfcdeployhisths.setResultRawData(resultRawData);
			intrfcdeployhisthsDao.insertIntrfcdeployhisths(intrfcdeployhisths);

			try {
				intrfccombsDao.updateIntrfccombsStatus(in.getIntrfcId(), BxCode.WorkStatusCd.DEPLOY_COMP.toString());
			} catch (Exception e) {
				logger.error("===========================!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				logger.error("{}", e);
				logger.error("===========================!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			}

			if(BxConstants.Default.IS_SERVER) {
				//String deployPath = "C:\\linebank\\temp\\";
				String deployPath = "/logs/jboss/opseim01_18080/eims_logs/deploy/lbtw_deploy_interface/deploy.interface/";
				String deployFileName = deployPath + inUi.getIntrfcId() + ".json";
				File deployFile = new File(deployFileName);
				FileWriter writer = null;
				try {
					writer = new FileWriter(deployFile, false);
					writer.write(JsonUtils.objectToJson(in));
					writer.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					logger.error("{}", e);
				}
			}
		}  		 
		
		uiResponse.setIntrfcDeployResponse(intrfcDeployResponse);
		
		return uiResponse;
	}

	public UiIntrfcdeployhisthsOut getDeployHistory(String intrfcId, String deployResultCd, int pageSize,
			int pageNumber) {
		UiIntrfcdeployhisthsOut out = new UiIntrfcdeployhisthsOut();

		int totalCount = intrfcdeployhisthsDao.selectAllCnt(intrfcId, deployResultCd);

		List<IntrfcdeployhisthsDto> intrfcdeployhisthsList = intrfcdeployhisthsDao.selectAll(intrfcId, deployResultCd,
				pageSize, pageNumber);

		if (intrfcdeployhisthsList == null) {
			intrfcdeployhisthsList = new ArrayList<IntrfcdeployhisthsDto>();
		}

		if (totalCount > 0) {
			out.setTotalCnt(totalCount);
			out.setIntrfcdeployhisthsOutList(intrfcdeployhisthsList);
		}

		return out;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public int reDeploy(IntrfcdeployhisthsDto in) {

		IntrfcdeployhisthsDto deployHistList = intrfcdeployhisthsDao.selectIntrfcdeployhisthsRaw(in.getIntrfcId(),
				in.getDeployVersion());

		IntrfccombsDto deployDtoHist = null;
		String deployStatus = "SUCCESS";
		List<IntrfcDeployResponse> intrfcDeployResponse = new ArrayList<IntrfcDeployResponse>();
		String deploySystems = null;
		int deployVersion = 0;
		IntrfcDeploy deployDto = new IntrfcDeploy();
		try {
			deployDtoHist = new IntrfccombsDto();
			String historyRawData = deployHistList.getRawData();
			deployDtoHist = JsonUtils.jsonToObect(historyRawData, deployDtoHist.getClass());

			deploySystems = deployHistList.getDeploySysCd();
			String[] deploySystemList = StringUtils.split(deploySystems, "/");
			IntrfcDeployResponse deployResult = null;

			IntrfcInfo info = new IntrfcInfo();

			deployDataSet(info, deployDtoHist);

			IntrfcdeployhisthsDto deployHistListOneRow = intrfcdeployhisthsDao.selectIntrfcdeployhisthsOneRow(in.getIntrfcId());

			if (deployHistListOneRow != null) {
				deployVersion = deployHistListOneRow.getDeployVersion() + 1;
			}

			deployDto.setDeployStatus("DEPLOY");
			
			deployDto.setDeployer(ServiceContext.getUserInfo().getUserId());
			deployDto.setDeployTime(DateUtils.getCurrentDate(9));
			deployDto.setIntrfcId(in.getIntrfcId());
			deployDto.setIntrfccombsDto(info);
			deployDto.setDeployVersion(Integer.toString(deployVersion));

			for (String deploySystem : deploySystemList) {

				String deploySysCode = deploySystem;

				// if (deploySysCode.equals("MCI_I") || deploySysCode.equals("MCI_E")) {
				// deploySysCode = "MCI";
				// }

				deployDto.setDeploySysCd(deploySysCode);
				String reDeployData = JsonUtils.objectToJson(deployDto);

				System.out.println(reDeployData);

				String deployUrl = intrfcdeploysysdtDao.selectDeploySystemUrl(in.getIntrfcId(), deploySystem);
				if (!deployUrl.equals("NotReady")) {
					byte[] resData = null;
					resData = RestUtils.sendRestPost(deployUrl, reDeployData.getBytes("UTF-8"), byte[].class);
					String resString = null;
					resString = new String(resData, "UTF-8");

					deployResult = JsonUtils.jsonToObect(resString, IntrfcDeployResponse.class);

					if (deployResult.getDeployStatus().equals("FAIL") || deployResult.getDeployStatus().equals("")
							|| deployResult.getDeployStatus() == null) {
						deployStatus = "FAIL";
					} else {
						intrfcDeployResponse.add(deployResult);
					}
				}

			}
		} catch (Exception e) {

			String resultRawData = null;
			try {
				IntrfcDeployResponseList list = new IntrfcDeployResponseList();
				List<IntrfcDeployResponseResult> resultList = new ArrayList<IntrfcDeployResponseResult>();

				for (IntrfcDeployResponse dto : intrfcDeployResponse) {
					IntrfcDeployResponseResult resultDto = new IntrfcDeployResponseResult();
					String deploySyscd = dto.getSystemCd();
					DepolysysbsDto deploySysDto = deployDao.selectDepolysysbs(deploySyscd);

					resultDto.setSystemCd(deploySyscd);
					resultDto.setDeployStatus(dto.getDeployStatus());
					resultDto.setMessage(dto.getMessage());

					if (deploySysDto != null) {
						resultDto.setSystemNm(deploySysDto.getDeploySysNm());
						resultDto.setSystemUrl(deploySysDto.getDeploySysUrl());
					}
					resultList.add(resultDto);
				}

				list.setIntrfcDeployResponseList(resultList);
				resultRawData = JsonUtils.objectToJson(list);
				logger.debug("----------------------resultRawData--------------------------");
				logger.debug(resultRawData);
				logger.debug("----------------------resultRawData--------------------------");
			} catch (JsonParseException e1) {
				logger.error("{}", e1);
			} catch (JsonMappingException e1) {
				logger.error("{}", e1);
			} catch (IOException e1) {
				logger.error("{}", e1);
			}

			IntrfcdeployhisthsDto intrfcdeployhisths = new IntrfcdeployhisthsDto();
			intrfcdeployhisths.setIntrfcId(in.getIntrfcId());
			intrfcdeployhisths.setDeployDttm(DateUtils.getCurrentDate(9));
			intrfcdeployhisths.setDeployResultCd("FAIL_" + in.getDeployVersion());
			intrfcdeployhisths.setDeploySysCd(deploySystems);
			intrfcdeployhisths.setDeployVersion(0);
			intrfcdeployhisths.setRawData("");
			intrfcdeployhisths.setResultRawData(resultRawData);
			intrfcdeployhisthsDao.insertIntrfcdeployhisths(intrfcdeployhisths);

			// 히스토리 쌓기
			ActionhisthsDto actionhisthsDto = new ActionhisthsDto();
			actionhisthsDto.setHstDscd("INTERFACE");
			actionhisthsDto.setItemDesc(deployDtoHist.getIntrfcNm());
			actionhisthsDto.setItemId(in.getIntrfcId());
			actionhisthsDto.setUserId(ServiceContext.getUserInfo().getUserId());
			actionhisthsDto.setWorkCttCd("REDEPLOYFAIL");
			actionhisthsDto.setWorkDttm(DateUtils.getCurrentDate(10));
			actionhistService.add(actionhisthsDto);

			logger.error("{}", e);
			// throw new ServiceException(BxMessages.Error.SYSTEM_EXCEPTION);
		}

		if (deployStatus.equals("FAIL")) {

			String resultRawData = null;
			try {
				IntrfcDeployResponseList list = new IntrfcDeployResponseList();
				List<IntrfcDeployResponseResult> resultList = new ArrayList<IntrfcDeployResponseResult>();

				for (IntrfcDeployResponse dto : intrfcDeployResponse) {
					IntrfcDeployResponseResult resultDto = new IntrfcDeployResponseResult();
					String deploySyscd = dto.getSystemCd();
					DepolysysbsDto deploySysDto = deployDao.selectDepolysysbs(deploySyscd);

					resultDto.setSystemCd(deploySyscd);
					resultDto.setDeployStatus(dto.getDeployStatus());
					resultDto.setMessage(dto.getMessage());

					if (deploySysDto != null) {
						resultDto.setSystemNm(deploySysDto.getDeploySysNm());
						resultDto.setSystemUrl(deploySysDto.getDeploySysUrl());
					}
					resultList.add(resultDto);
				}

				list.setIntrfcDeployResponseList(resultList);
				resultRawData = JsonUtils.objectToJson(list);
				logger.debug("----------------------resultRawData--------------------------");
				logger.debug(resultRawData);
				logger.debug("----------------------resultRawData--------------------------");
			} catch (JsonParseException e1) {
				logger.error("{}", e1);
			} catch (JsonMappingException e1) {
				logger.error("{}", e1);
			} catch (IOException e1) {
				logger.error("{}", e1);
			}

			IntrfcdeployhisthsDto intrfcdeployhisths = new IntrfcdeployhisthsDto();
			intrfcdeployhisths.setIntrfcId(in.getIntrfcId());
			intrfcdeployhisths.setDeployDttm(DateUtils.getCurrentDate(9));
			intrfcdeployhisths.setDeployResultCd("FAIL_" + in.getDeployVersion());
			intrfcdeployhisths.setDeploySysCd(deploySystems);
			intrfcdeployhisths.setDeployVersion(0);
			intrfcdeployhisths.setRawData("");
			intrfcdeployhisths.setResultRawData(resultRawData);
			intrfcdeployhisthsDao.insertIntrfcdeployhisths(intrfcdeployhisths);

			// 히스토리 쌓기
			ActionhisthsDto actionhisthsDto = new ActionhisthsDto();
			actionhisthsDto.setHstDscd("INTERFACE");
			actionhisthsDto.setItemDesc(deployDtoHist.getIntrfcNm());
			actionhisthsDto.setItemId(in.getIntrfcId());
			actionhisthsDto.setUserId(ServiceContext.getUserInfo().getUserId());
			actionhisthsDto.setWorkCttCd("REDEPLOYFAIL");
			actionhisthsDto.setWorkDttm(DateUtils.getCurrentDate(10));
			actionhistService.add(actionhisthsDto);

			// throw new ServiceException(BxMessages.Error.REDEPLOY_EXCEPTION);
		} else {

			String resultRawData = null;
			try {
				IntrfcDeployResponseList list = new IntrfcDeployResponseList();
				List<IntrfcDeployResponseResult> resultList = new ArrayList<IntrfcDeployResponseResult>();

				for (IntrfcDeployResponse dto : intrfcDeployResponse) {
					IntrfcDeployResponseResult resultDto = new IntrfcDeployResponseResult();
					String deploySyscd = dto.getSystemCd();
					DepolysysbsDto deploySysDto = deployDao.selectDepolysysbs(deploySyscd);

					resultDto.setSystemCd(deploySyscd);
					resultDto.setDeployStatus(dto.getDeployStatus());
					resultDto.setMessage(dto.getMessage());

					if (deploySysDto != null) {
						resultDto.setSystemNm(deploySysDto.getDeploySysNm());
						resultDto.setSystemUrl(deploySysDto.getDeploySysUrl());
					}
					resultList.add(resultDto);
				}

				list.setIntrfcDeployResponseList(resultList);
				resultRawData = JsonUtils.objectToJson(list);
				logger.debug("----------------------resultRawData--------------------------");
				logger.debug(resultRawData);
				logger.debug("----------------------resultRawData--------------------------");
			} catch (JsonParseException e1) {
				logger.error("{}", e1);
			} catch (JsonMappingException e1) {
				logger.error("{}", e1);
			} catch (IOException e1) {
				logger.error("{}", e1);
			}

			IntrfcdeployhisthsDto intrfcdeployhisths = new IntrfcdeployhisthsDto();
			intrfcdeployhisths.setIntrfcId(in.getIntrfcId());
			intrfcdeployhisths.setDeployDttm(DateUtils.getCurrentDate(9));
			intrfcdeployhisths.setDeployResultCd("SUCCESS_" + in.getDeployVersion());
			intrfcdeployhisths.setDeploySysCd(deploySystems);
			intrfcdeployhisths.setDeployVersion(deployVersion);
			String rawData = null;
			try {
				rawData = JsonUtils.objectToJson(deployDtoHist);
			} catch (Exception e) {
				logger.error("{}", e);
			}
			intrfcdeployhisths.setRawData(rawData);
			intrfcdeployhisths.setResultRawData(resultRawData);
			intrfcdeployhisthsDao.insertIntrfcdeployhisths(intrfcdeployhisths);

			// 히스토리 쌓기
			ActionhisthsDto actionhisthsDto = new ActionhisthsDto();
			actionhisthsDto.setHstDscd("INTERFACE");
			actionhisthsDto.setItemDesc(deployDtoHist.getIntrfcNm());
			actionhisthsDto.setItemId(in.getIntrfcId());
			actionhisthsDto.setUserId(ServiceContext.getUserInfo().getUserId());
			actionhisthsDto.setWorkCttCd("REDEPLOY");
			actionhisthsDto.setWorkDttm(DateUtils.getCurrentDate(10));
			actionhistService.add(actionhisthsDto);
			
			if(BxConstants.Default.IS_SERVER) {
				//String deployPath = "C:\\linebank\\temp\\";
				String deployPath = "/logs/jboss/opseim01_18080/eims_logs/deploy/lbtw_deploy_interface/deploy.interface/";
				String deployFileName = deployPath + in.getIntrfcId() + ".json";
				File deployFile = new File(deployFileName);
				FileWriter writer = null;
				try {
					writer = new FileWriter(deployFile, false);
					writer.write(JsonUtils.objectToJson(deployDtoHist));
					writer.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					logger.error("{}", e);
				}
			}
		}

		int updateResult = update(deployDtoHist, true);

		return updateResult;
	}

	private void deployDataSet(IntrfcInfo info, IntrfccombsDto deployDtoHist) {
		info.setBizCd(deployDtoHist.getBizCd());
		info.setInstCd(deployDtoHist.getInstCd());
		info.setIntrfccombsMappingReqDto(deployDtoHist.getIntrfccombsMappingReqDto());
		info.setIntrfccombsMappingResDto(deployDtoHist.getIntrfccombsMappingResDto());
		info.setIntrfcId(deployDtoHist.getIntrfcId());
		info.setIntrfcmsglayoutdtDto(deployDtoHist.getIntrfcmsglayoutdtDto());
		info.setIntrfcNm(deployDtoHist.getIntrfcNm());
		info.setIntrfcsrsysdtDto(deployDtoHist.getIntrfcsrsysdtDto());
		info.setIntrfcTypeCd(deployDtoHist.getIntrfcTypeCd());
		info.setIntrfcWayCd(deployDtoHist.getIntrfcWayCd());
		info.setLv1Cd(deployDtoHist.getLv1Cd());
		info.setLv2Cd(deployDtoHist.getLv2Cd());
		info.setLv3Cd(deployDtoHist.getLv3Cd());
		info.setLv4Cd(deployDtoHist.getLv4Cd());
		info.setLv5Cd(deployDtoHist.getLv5Cd());
		info.setMsgTrnsfrmYn(deployDtoHist.getMsgTrnsfrmYn());
		info.setRegDttm(deployDtoHist.getRegDttm());
		info.setRegManId(deployDtoHist.getRegManId());
		info.setTrxCd(deployDtoHist.getTrxCd());
		info.setTrxDscd(deployDtoHist.getTrxDscd());
		info.setWorkStatusCd(deployDtoHist.getWorkStatusCd());
		info.setSyncAsyncDscd(deployDtoHist.getSyncAsyncDscd());
		info.setSrTypeCd(deployDtoHist.getSrTypeCd());
		info.setRqstExtrnlMsgNo(deployDtoHist.getRqstExtrnlMsgNo());
		info.setRspsExtrnlMsgNo(deployDtoHist.getRspsExtrnlMsgNo());
		info.setRspsYn(deployDtoHist.getRspsYn());
		info.setTrxTypeDscd(deployDtoHist.getTrxTypeDscd());
		info.setIntrfcroutinfodtDto(deployDtoHist.getIntrfcroutinfodtDto());
		if (deployDtoHist.getIntrfcTypeCd().equals("MCI")) {
			IntrfccombsDetailMCIDto mciDto = deployDtoHist.getMciDto();
			if (mciDto.getBackupAprvStat() == null || mciDto.getBackupAprvStat().equals("")) {
				mciDto.setBackupAprvStat("");
			}
			info.setMciDetailDto(mciDto);
			info.setEaiDetailDto(null);
			info.setFepDetailDto(null);
			info.setCcDetailDto(null);
		} else if (deployDtoHist.getIntrfcTypeCd().equals("CC")) {
			IntrfccombsDetailCCDto ccDto = deployDtoHist.getCcDto();
			info.setMciDetailDto(null);
			info.setEaiDetailDto(null);
			info.setFepDetailDto(null);
			info.setCcDetailDto(ccDto);
		} else if (deployDtoHist.getIntrfcTypeCd().equals("EAI_I") || deployDtoHist.getIntrfcTypeCd().equals("EAI_E")) {
			IntrfccombsDetailEAIDto eaiDto = deployDtoHist.getEaiDto();
			if (info.getIntrfcWayCd().equals("APTOAP")) {
				if (eaiDto.getBackupAprvStat() == null || eaiDto.getBackupAprvStat().equals("")) {
					eaiDto.setBackupAprvStat("");
				}
			} else if (info.getIntrfcWayCd().equals("APTODB")) {
				if (eaiDto.getBackupAprvStat() == null || eaiDto.getBackupAprvStat().equals("")) {
					eaiDto.setBackupAprvStat("");
				}
				if (eaiDto.getErrSkipYn() == null || eaiDto.getErrSkipYn().equals("")) {
					eaiDto.setErrSkipYn("N");
				}
			} else if (info.getIntrfcWayCd().equals("FILETOFILE")) {
				if (eaiDto.getBackupAprvStat() == null || eaiDto.getBackupAprvStat().equals("")) {
					eaiDto.setBackupAprvStat("");
				}
				if (eaiDto.getPrivacyInclYn() == null || eaiDto.getPrivacyInclYn().equals("")) {
					eaiDto.setPrivacyInclYn("N");
				}
				if (eaiDto.getEncTargetYn() == null || eaiDto.getEncTargetYn().equals("")) {
					eaiDto.setEncTargetYn("N");
				}

			} else if (info.getIntrfcWayCd().equals("DBTODB")) {
				if (eaiDto.getBackupAprvStat() == null || eaiDto.getBackupAprvStat().equals("")) {
					eaiDto.setBackupAprvStat("");
				}
				if (eaiDto.getErrSkipYn() == null || eaiDto.getErrSkipYn().equals("")) {
					eaiDto.setErrSkipYn("N");
				}
				if (eaiDto.getPrivacyInclYn() == null || eaiDto.getPrivacyInclYn().equals("")) {
					eaiDto.setPrivacyInclYn("N");
				}
				if (eaiDto.getEncTargetYn() == null || eaiDto.getEncTargetYn().equals("")) {
					eaiDto.setEncTargetYn("N");
				}
				if(!deployDtoHist.getExecEnvDscd().isEmpty()) {
					info.setExecEnvDscd(deployDtoHist.getExecEnvDscd());
				}
			}

			info.setEaiDetailDto(eaiDto);
			info.setFepDetailDto(null);
			info.setMciDetailDto(null);
			info.setCcDetailDto(null);
			if (deployDtoHist.getIntrfcWayCd().equals("FILETOFILE")) {
				if (deployDtoHist.getIntrfcMsgFieldEncodingDto() != null) {
					info.setIntrfcMsgFieldEncodingDto(deployDtoHist.getIntrfcMsgFieldEncodingDto());
				}
			}
		} else {
			IntrfccombsDetailFEPDto fepDto = deployDtoHist.getFepDto();

			if (info.getTrxDscd().equals("ONLINE")) {
				if (fepDto.getTimeOutProcMode() == null || fepDto.getTimeOutProcMode().equals("")) {
					fepDto.setTimeOutProcMode("NONE");
				}
				if (fepDto.getDelayRspsYn() == null || fepDto.getDelayRspsYn().equals("")) {
					fepDto.setDelayRspsYn("N");
				}
				if (fepDto.getTrnsmsnErrResYn() == null || fepDto.getTrnsmsnErrResYn().equals("")) {
					fepDto.setTrnsmsnErrResYn("N");
				}
				if (fepDto.getBackupAprvStat() == null || fepDto.getBackupAprvStat().equals("")) {
					fepDto.setBackupAprvStat("");
				}
			}

			info.setFepDetailDto(fepDto);
			info.setMciDetailDto(null);
			info.setEaiDetailDto(null);
			info.setCcDetailDto(null);	
			info.setProtocolDscd(deployDtoHist.getProtocolDscd());
			info.setHttpMethod(deployDtoHist.getHttpMethod());
			info.setContextUrl(deployDtoHist.getContextUrl());
			info.setRequestMediaType(deployDtoHist.getRequestMediaType());
			info.setResponseMediaType(deployDtoHist.getResponseMediaType());

		}
	}

	public IntrfccombsDto getPreDeployIntrfc(String intrfcId, int deployVersion) {

		IntrfcdeployhisthsDto deployHistList = intrfcdeployhisthsDao.selectIntrfcdeployhisthsRaw(intrfcId,
				deployVersion);

		IntrfccombsDto deployDtoHist = null;
		try {
			deployDtoHist = new IntrfccombsDto();
			String historyRawData = deployHistList.getRawData();
			deployDtoHist = JsonUtils.jsonToObect(historyRawData, deployDtoHist.getClass());
		} catch (Exception e) {
			logger.error("{}", e);
			throw new ServiceException(BxMessages.Error.SYSTEM_EXCEPTION);
		}
		return deployDtoHist;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public IntrfcIdCreateDto getInterfaceId(IntrfcIdCreateDto in) {

		// □ + □□□□□□ + □□ + □ + □□□□□
		// ① ② ③ ④ ⑤
		//
		// ① : 채널통합구분코드(1)
		// ② : 송수신 시스템코드(6)
		// ③ : 어플리케이션코드(2)
		// ④ : 온라인/배치 구분코드(1)
		// ⑤ : 일련번호(5)

		// 채널통합구분코드 : M(MCI), E(EAI), F(FEP)
		// 송수신 시스템코드 : 송신/수신 시스템코드 3자리
		// 어플리케이션코드 : AA MAP의 상품처리 L3 업무코드 2자리 단, 단위시스템간 인터페이스 인 경우에는 “00”으로 셋팅
		// 온라인/배치 구분코드 : O(온라인), B(배치)
		// 일련번호 : 어플리케이션코드 기준의 일련번호

		String trxInfoIntWay = "";
		String intrfcTypeCd = in.getIntrfcTypeCd();
		String chlDscd = null;

		if (intrfcTypeCd == null || intrfcTypeCd.equals("")) {
			throw new ServiceException(BxMessages.Error.SYSTEM_EXCEPTION);
		}

		if (intrfcTypeCd.equals("MCI")) {
			chlDscd = "M";
		} else if (intrfcTypeCd.equals("FEP")) {
			chlDscd = "F";
		} else if (intrfcTypeCd.equals("CC")) {
			chlDscd = "B";
		} else {
			chlDscd = "E";
		}

		String sysInfo = in.getSendSysCd() + in.getReceiveSysCd();

		String appInfo = in.getLv3Cd();

		if (in.getTrxDscd().equals(BxCode.interfaceWay.ONLINE.toString())) {
			trxInfoIntWay = "O";
		} else {
			trxInfoIntWay = "B";
		}

		String intrfcIdPre = chlDscd + sysInfo + appInfo + trxInfoIntWay;
		Integer intrfcIdSeq = intrfccombsDao.selectIdSeq(appInfo);
		String seq;

		if (intrfcIdSeq == null) {
			intrfcIdSeq = 0;
		}

		intrfcIdSeq = intrfcIdSeq + 1;

		seq = String.format("%05d", intrfcIdSeq);

		String intrfcId = intrfcIdPre + seq;

		IntrfcIdCreateDto intrfcIdCreateDto = new IntrfcIdCreateDto();
		// intrfcIdCreateDto.setIntrfcId(intrfcId);
		intrfcIdCreateDto.setSeq(intrfcIdSeq);

		return intrfcIdCreateDto;
	}

	public MsglayoutbsDto getPreLayout(String intrfcId, int deployVersion, String msgLayoutId, String srTypeCd,
			String rqstRspsTypeCd, Integer layoutSeq) {

		IntrfcdeployhisthsDto deployHistList = intrfcdeployhisthsDao.selectIntrfcdeployhisthsRaw(intrfcId,
				deployVersion);

		MsglayoutbsDto returnMsg = null;

		IntrfccombsDto deployDtoHist = null;
		try {
			deployDtoHist = new IntrfccombsDto();
			String historyRawData = deployHistList.getRawData();
			deployDtoHist = JsonUtils.jsonToObect(historyRawData, deployDtoHist.getClass());

			List<IntrfcmsglayoutdtDto> deployMsgList = deployDtoHist.getIntrfcmsglayoutdtDto();
			for (IntrfcmsglayoutdtDto deployMsg : deployMsgList) {
				// if (deployMsg.getMsglayoutbsDto().getMsgLayoutId().equals(msgLayoutId)
				// && deployMsg.getSrTypeCd().equals(srTypeCd)
				// && deployMsg.getRqstRspsTypeCd().equals(rqstRspsTypeCd)) {
				// returnMsg = deployMsg.getMsglayoutbsDto();
				// }
				if (deployMsg.getRqstRspsSeq() == layoutSeq && deployMsg.getSrTypeCd().equals(srTypeCd)
						&& deployMsg.getRqstRspsTypeCd().equals(rqstRspsTypeCd)) {
					returnMsg = deployMsg.getMsglayoutbsDto();
				}
			}
		} catch (Exception e) {
			logger.error("{}", e);
			throw new ServiceException(BxMessages.Error.SYSTEM_EXCEPTION);
		}

		if (returnMsg == null) {
			throw new ServiceException(BxMessages.Error.NOT_FOUNDED);
		}

		return returnMsg;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public int fileUpload(MultipartFile intrfcFile, String intrfcTypeCd) {

		logger.debug("intrfcTypeCd: {}", intrfcTypeCd);

		String uid = UidUtils.genUUID();

		String inFile = "/tmp/" + uid + ".xlsx";
		String outFile = "/tmp/" + uid + ".decrypted.xlsx";
		IntrfccombsDto intrfccombsDto = new IntrfccombsDto();

		Workbook workbook = null;
		try (FileOutputStream out = new FileOutputStream(inFile)) {
			out.write(intrfcFile.getBytes());
			out.close();

			DrmUtil.decrypt(inFile, outFile);
			File decryptedInputFile = Optional.ofNullable(new File(outFile)).filter(File::exists).filter(File::canRead)
					.orElseThrow(() -> new IOException(outFile));
			workbook = WorkbookFactory.create(decryptedInputFile);

			if (intrfcTypeCd.equals("MCI")) {
				Sheet sheet = workbook.getSheetAt(2);

				String title = ExcelUtils.readValue(sheet, 2, "B");
				int titleYn = title.indexOf("MCI");

				if (titleYn < 0) {
					throw new ServiceException(BxMessages.Error.FILE_UPLOAD_INTERFACE_TYPE);
				}

				readMciData(sheet, intrfccombsDto, workbook);

				workbook.close();

			} else if (intrfcTypeCd.equals("CC")) {
				Sheet sheet = workbook.getSheetAt(2);

				String title = ExcelUtils.readValue(sheet, 2, "B");
				int titleYn = title.indexOf("센터컷");

				if (titleYn < 0) {
					throw new ServiceException(BxMessages.Error.FILE_UPLOAD_INTERFACE_TYPE);
				}

				readCcData(sheet, intrfccombsDto, workbook);

				workbook.close();

			} else if (intrfcTypeCd.equals("EAI_I") || intrfcTypeCd.equals("EAI_E")) {

				readEaiData(intrfccombsDto, workbook, intrfcTypeCd);

				workbook.close();
			} else {
				readFepData(intrfccombsDto, workbook);
				workbook.close();
			}

		} catch (FileNotFoundException e) {
			logger.error("{}", e);
			throw new ServiceException(BxMessages.Error.FILE_NOT_EXISTED);
		} catch (IOException e) {
			logger.error("{}", e);
			throw new ServiceException(BxMessages.Error.SYSTEM_EXCEPTION);
		} catch (FasooDrmException e) {
			logger.error("{}", e);
			throw new ServiceException(BxMessages.Error.DRM_EXCEPTION);
		} catch (FileTypeException e) {
			logger.error("{}", e);
			throw new ServiceException(BxMessages.Error.SYSTEM_EXCEPTION);
		} catch (EncryptedDocumentException e) {
			logger.error("{}", e);
			throw new ServiceException(BxMessages.Error.SYSTEM_EXCEPTION);
		} catch (InvalidFormatException e) {
			logger.error("{}", e);
			throw new ServiceException(BxMessages.Error.SYSTEM_EXCEPTION);
		} finally {
			new File(inFile).delete();
			new File(outFile).delete();
			if (workbook != null) {
				try {
					workbook.close();
				} catch (IOException e) {
					logger.error("{}", e);
				}
			}
		}

		return 0;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	protected void readFepData(IntrfccombsDto intrfccombsDto, Workbook workbook) {

		Sheet sheet = workbook.getSheetAt(2);
		boolean insertYn = true;

		String title = ExcelUtils.readValue(sheet, 2, "B");
		int titleYn = title.indexOf("FEP");

		String sendSyscd = "";
		String recvSysCd = "";

		if (titleYn < 0) {
			throw new ServiceException(BxMessages.Error.FILE_UPLOAD_INTERFACE_TYPE);
		}

		Map<String, MsgInsertDto> msgResultMap = new HashMap<String, MsgInsertDto>();

		if (sheet.getSheetName().equals("대외온라인")) {

			String fileVer = ExcelUtils.readValue(sheet, 1, "S");
			StringBuilder errMsg = new StringBuilder();
			boolean isValid = true;

			String appCdL1 = ExcelUtils.readValue(sheet, 13, "E");
			String appCdL2 = ExcelUtils.readValue(sheet, 13, "F");
			String appCdL3 = ExcelUtils.readValue(sheet, 13, "G");
			sendSyscd = ExcelUtils.readValue(sheet, 8, "E");
			recvSysCd = ExcelUtils.readValue(sheet, 8, "L");
			String trxDscd = getCodeValueNm("TRAN_DSCD", ExcelUtils.readValue(sheet, 7, "E"), "ko");

			isValid = intrfcValidation(isValid, errMsg, appCdL1, appCdL2, appCdL3, sendSyscd, recvSysCd, trxDscd);

			if (!isValid) {
				errMsg.append("\n시트명: " + sheet.getSheetName());
				throw new ServiceException(BxMessages.Error.INTRFC_VALID_CHECK, errMsg.toString());
			}

			IntrfcIdCreateDto interfaceIdDto = new IntrfcIdCreateDto();
			interfaceIdDto.setIntrfcTypeCd("FEP");
			interfaceIdDto.setLv1Cd(ExcelUtils.readValue(sheet, 13, "E"));
			interfaceIdDto.setLv2Cd(ExcelUtils.readValue(sheet, 13, "F"));
			interfaceIdDto.setLv3Cd(ExcelUtils.readValue(sheet, 13, "G"));
			interfaceIdDto.setSendSysCd(ExcelUtils.readValue(sheet, 8, "E"));
			interfaceIdDto.setReceiveSysCd(ExcelUtils.readValue(sheet, 8, "L"));
			// interfaceIdDto.setSyncAsyncDscd(getCodeValueNm("SYNC_DSCD",
			// ExcelUtils.readValue(sheet, 6, "L"), "ko"));
			interfaceIdDto.setTrxDscd(trxDscd);
			String interfaceId = this.getInterfaceId(interfaceIdDto).getIntrfcId();

			String intrfcIdDoc = ExcelUtils.readValue(sheet, 5, "E");
			insertYn = true;

			if (intrfcIdDoc != null && !intrfcIdDoc.equals("")) {
				interfaceId = intrfcIdDoc;
				insertYn = false;
			}

			intrfccombsDto.setIntrfcId(interfaceId);
			intrfccombsDto.setIntrfcNm(ExcelUtils.readValue(sheet, 5, "L"));
			intrfccombsDto.setWorkStatusCd("WORKING");
			intrfccombsDto.setRegManId(ExcelUtils.readValue(sheet, 3, "L"));
			intrfccombsDto.setRegDttm(DateUtils.getCurrentDate(9));
			intrfccombsDto.setMsgTrnsfrmYn(ExcelUtils.readValue(sheet, 7, "L"));
			intrfccombsDto.setTrxDscd(trxDscd);
			intrfccombsDto.setIntrfcTypeCd("FEP");
			intrfccombsDto.setLv1Cd(ExcelUtils.readValue(sheet, 13, "E"));
			intrfccombsDto.setLv2Cd(ExcelUtils.readValue(sheet, 13, "F"));
			intrfccombsDto.setLv3Cd(ExcelUtils.readValue(sheet, 13, "G"));
			intrfccombsDto.setSyncAsyncDscd(getCodeValueNm("SYNC_DSCD_FEP", ExcelUtils.readValue(sheet, 6, "L"), "ko"));
			intrfccombsDto.setSrTypeCd(getCodeValueNm("SENC_RECV_DSCD", ExcelUtils.readValue(sheet, 6, "E"), "ko"));

			List<IntrfcsrsysdtDto> srSysDtoList = new ArrayList<IntrfcsrsysdtDto>();
			IntrfcsrsysdtDto srSysDto = new IntrfcsrsysdtDto();
			IntrfccombsDetailFEPDto fepDto = new IntrfccombsDetailFEPDto();
			Map<String, String> msgMap = new HashMap<String, String>();
			List<IntrfcmsglayoutdtDto> intrfcMsgLayoutDtoList = new ArrayList<IntrfcmsglayoutdtDto>();
			int startIndex = 15;
			List<IntrfccombsMappingDto> intrfccombsMappingReqDto = new ArrayList<IntrfccombsMappingDto>();
			List<IntrfccombsMappingDto> intrfccombsMappingResDto = new ArrayList<IntrfccombsMappingDto>();
			List<IntrfcroutinfodtDto> intrfcroutinfodtDto = new ArrayList<IntrfcroutinfodtDto>();

			srSysDto.setSysCd(ExcelUtils.readValue(sheet, 8, "E"));
			srSysDto.setSysNm(ExcelUtils.readValue(sheet, 9, "E"));
			srSysDto.setCrgManNm(ExcelUtils.readValue(sheet, 10, "E"));
			srSysDto.setIntrfcId(interfaceId);
			srSysDto.setSrSeq(1);
			srSysDto.setSrTypeCd("SEND");
			srSysDtoList.add(srSysDto);

			srSysDto = new IntrfcsrsysdtDto();
			srSysDto.setSysCd(ExcelUtils.readValue(sheet, 8, "L"));
			srSysDto.setSysNm(ExcelUtils.readValue(sheet, 9, "L"));
			srSysDto.setCrgManNm(ExcelUtils.readValue(sheet, 10, "L"));
			srSysDto.setIntrfcId(interfaceId);
			srSysDto.setSrSeq(1);
			srSysDto.setSrTypeCd("RECEIVE");

			String trxCd = ExcelUtils.readValue(sheet, 11, "E");
			if (!trxCd.equals("") && trxCd != null) {
				int i = trxcdDao.selectAllCnt(trxCd, null, null, null);
				if (i == 0) {
					errMsg.append("\n존재하지 않는 거래코드입니다.");
					isValid = false;
				}
			}

			srSysDto.setTrxCd(trxCd);
			srSysDtoList.add(srSysDto);

			if (!isValid) {
				errMsg.append("\n시트명: " + sheet.getSheetName());
				throw new ServiceException(BxMessages.Error.INTRFC_VALID_CHECK, errMsg.toString());
			}

			for (int i = 19; i < 22; i++) {
				if (!ExcelUtils.readValue(sheet, i, "C").equals("") && ExcelUtils.readValue(sheet, i, "C") != null) {
					IntrfcroutinfodtDto dto = new IntrfcroutinfodtDto();
					dto.setSeq(i - 18);
					dto.setIntrfcId(interfaceId);
					dto.setLenFldOffsetLen(Integer.parseInt(ExcelUtils.readValue(sheet, i, "C")));
					dto.setFldDataLen(Integer.parseInt(ExcelUtils.readValue(sheet, i, "F")));
					dto.setCndtCd(getCodeValueNm("CONDITION_CD", ExcelUtils.readValue(sheet, i, "G"), "ko"));// 컨디션
					dto.setFldCfgVal(ExcelUtils.readValue(sheet, i, "H"));
					dto.setRutngDesc(ExcelUtils.readValue(sheet, i, "K"));// 설명
					intrfcroutinfodtDto.add(dto);
				}
			}
			if (intrfcroutinfodtDto.size() > 0) {
				intrfccombsDto.setIntrfcroutinfodtDto(intrfcroutinfodtDto);
			}

			fepDto.setIntrfcUse(ExcelUtils.readValue(sheet, 14, "E"));
			fepDto.setIntrfDesc(ExcelUtils.readValue(sheet, 15, "E"));

			String instCd = ExcelUtils.readValue(sheet, 24, "E");
			ExtrnlinstcdDto extrnlinstcdDto = extrnlinstcdDao.selectExtrnlinstcd(instCd, "INST");

			if (instCd != null && !instCd.equals("")) {
				if (extrnlinstcdDto == null) {
					errMsg.append("\n존재하지않는 기관코드입니다. ");
					isValid = false;
				}
			}

			String bizCd = ExcelUtils.readValue(sheet, 25, "E");

			if (bizCd != null && !bizCd.equals("")) {
				if (bizCdDao.selectBizcd(bizCd) == null) {
					errMsg.append("\n존재하지않는 기관업무코드입니다. ");
					isValid = false;
				}
			}

			intrfccombsDto.setInstCd(instCd);
			intrfccombsDto.setRqstExtrnlMsgNo(ExcelUtils.readValue(sheet, 24, "H"));
			intrfccombsDto.setRspsExtrnlMsgNo(ExcelUtils.readValue(sheet, 24, "L"));
			intrfccombsDto.setBizCd(bizCd);

			if (!isValid) {
				errMsg.append("\n시트명: " + sheet.getSheetName());
				throw new ServiceException(BxMessages.Error.INTRFC_VALID_CHECK, errMsg.toString());
			}

			fepDto.setTimeOut(ExcelUtils.readValue(sheet, 25, "H"));
			fepDto.setTimeOutProcMode(getCodeValueNm("TIMEOUT_PROC_CD", ExcelUtils.readValue(sheet, 25, "L"), "ko"));
			fepDto.setDelayRspsYn(ExcelUtils.readValue(sheet, 26, "E"));
			fepDto.setCurrIntrfcIdentifier(ExcelUtils.readValue(sheet, 26, "H"));
			fepDto.setPrivacyInclYn(ExcelUtils.readValue(sheet, 26, "L"));
			fepDto.setEncTargetYn(ExcelUtils.readValue(sheet, 27, "E"));
			fepDto.setTrnsmsnErrResYn(ExcelUtils.readValue(sheet, 27, "H"));
			fepDto.setBackupAprvStat(getCodeValueNm("BACKUP_CD", ExcelUtils.readValue(sheet, 27, "L"), "ko"));
			fepDto.setReqWrapperDtoNm(ExcelUtils.readValue(sheet, 28, "E"));
			fepDto.setResWrapperDtoNm(ExcelUtils.readValue(sheet, 28, "H"));
			fepDto.setRelationTrxCd(ExcelUtils.readValue(sheet, 28, "L"));

			if (fileVer.equals("v20180911") || fileVer.equals("")) {

				Map<String, String> msgInsertMap = new HashMap<String, String>();
				int mapGridSize = 33;

				for (int j = 0; j < 4; j++) {
					if (j == 0) {
						for (int i = 1; i < 4; i++) {
							if (ExcelUtils.readValue(sheet, i + mapGridSize, "C") != null
									&& !ExcelUtils.readValue(sheet, i + mapGridSize, "C").equals("")) {

								this.mappingSetting(interfaceId, workbook, sheet, i, startIndex, intrfcMsgLayoutDtoList,
										msgMap, "SEND_REQ_", "SEND", "REQUEST", "C", "E", mapGridSize, 8, msgInsertMap,
										msgResultMap);
							}
						}
					} else if (j == 1) {
						for (int i = 1; i < 4; i++) {
							if (ExcelUtils.readValue(sheet, i + mapGridSize, "G") != null
									&& !ExcelUtils.readValue(sheet, i + mapGridSize, "G").equals("")) {

								this.mappingSetting(interfaceId, workbook, sheet, i, startIndex, intrfcMsgLayoutDtoList,
										msgMap, "SEND_RES_", "SEND", "RESPONSE", "G", "E", mapGridSize, 8, msgInsertMap,
										msgResultMap);
							}
						}
					} else if (j == 2) {
						for (int i = 1; i < 4; i++) {
							if (ExcelUtils.readValue(sheet, i + mapGridSize, "I") != null
									&& !ExcelUtils.readValue(sheet, i + mapGridSize, "I").equals("")) {

								this.mappingSetting(interfaceId, workbook, sheet, i, startIndex, intrfcMsgLayoutDtoList,
										msgMap, "RECV_REQ_", "RECEIVE", "REQUEST", "I", "L", mapGridSize, 8,
										msgInsertMap, msgResultMap);
							}
						}
					} else if (j == 3) {
						for (int i = 1; i < 4; i++) {
							if (ExcelUtils.readValue(sheet, i + mapGridSize, "M") != null
									&& !ExcelUtils.readValue(sheet, i + mapGridSize, "M").equals("")) {

								this.mappingSetting(interfaceId, workbook, sheet, i, startIndex, intrfcMsgLayoutDtoList,
										msgMap, "RECV_RES_", "RECEIVE", "RESPONSE", "M", "L", mapGridSize, 8,
										msgInsertMap, msgResultMap);
							}
						}
					}
				}

				intrfccombsDto.setIntrfcmsglayoutdtDto(intrfcMsgLayoutDtoList);

				if (intrfccombsDto.getMsgTrnsfrmYn().equals("Y")) {
					Sheet reqMappingSheet = workbook.getSheet(ExcelUtils.readValue(sheet, 41, "C"));
					Sheet resMappingSheet = workbook.getSheet(ExcelUtils.readValue(sheet, 41, "I"));

					intrfccombsMappingReqDto = this.readMapping(reqMappingSheet, msgMap, intrfccombsDto,
							intrfccombsMappingReqDto, "SEND_REQ_", "RECV_REQ_", "REQUEST");
					intrfccombsMappingResDto = this.readMapping(resMappingSheet, msgMap, intrfccombsDto,
							intrfccombsMappingResDto, "RECV_RES_", "SEND_RES_", "RESPONSE");
					// ExcelUtils.readValue(reqMappingSheet, 5, "V")
				}

				List<IntrfccombsMappingDto> intrfccombsMappingReqDtoResult = new ArrayList<IntrfccombsMappingDto>();
				List<IntrfccombsMappingDto> intrfccombsMappingResDtoResult = new ArrayList<IntrfccombsMappingDto>();
				for (IntrfccombsMappingDto dto : intrfccombsMappingReqDto) {
					if (dto.getReqResTypeCd().equals("REQUEST")) {
						intrfccombsMappingReqDtoResult.add(dto);
					}
				}
				for (IntrfccombsMappingDto dto : intrfccombsMappingResDto) {
					if (dto.getReqResTypeCd().equals("RESPONSE")) {
						intrfccombsMappingResDtoResult.add(dto);
					}
				}

				intrfccombsDto.setIntrfccombsMappingReqDto(intrfccombsMappingReqDtoResult);
				intrfccombsDto.setIntrfccombsMappingResDto(intrfccombsMappingResDtoResult);

				intrfccombsDto.setIntrfcsrsysdtDto(srSysDtoList);
				intrfccombsDto.setFepDto(fepDto);

			} else if (fileVer.equals("v20181005")) {

				fepDto.setCommNetworkIntrfcYn(ExcelUtils.readValue(sheet, 29, "E"));
				Map<String, String> msgInsertMap = new HashMap<String, String>();
				int mapGridSize = 34;

				for (int j = 0; j < 4; j++) {
					if (j == 0) {
						for (int i = 1; i < 4; i++) {
							if (ExcelUtils.readValue(sheet, i + mapGridSize, "C") != null
									&& !ExcelUtils.readValue(sheet, i + mapGridSize, "C").equals("")) {

								this.mappingSetting(interfaceId, workbook, sheet, i, startIndex, intrfcMsgLayoutDtoList,
										msgMap, "SEND_REQ_", "SEND", "REQUEST", "C", "E", mapGridSize, 8, msgInsertMap,
										msgResultMap);
							}
						}
					} else if (j == 1) {
						for (int i = 1; i < 4; i++) {
							if (ExcelUtils.readValue(sheet, i + mapGridSize, "G") != null
									&& !ExcelUtils.readValue(sheet, i + mapGridSize, "G").equals("")) {

								this.mappingSetting(interfaceId, workbook, sheet, i, startIndex, intrfcMsgLayoutDtoList,
										msgMap, "SEND_RES_", "SEND", "RESPONSE", "G", "E", mapGridSize, 8, msgInsertMap,
										msgResultMap);
							}
						}
					} else if (j == 2) {
						for (int i = 1; i < 4; i++) {
							if (ExcelUtils.readValue(sheet, i + mapGridSize, "I") != null
									&& !ExcelUtils.readValue(sheet, i + mapGridSize, "I").equals("")) {

								this.mappingSetting(interfaceId, workbook, sheet, i, startIndex, intrfcMsgLayoutDtoList,
										msgMap, "RECV_REQ_", "RECEIVE", "REQUEST", "I", "L", mapGridSize, 8,
										msgInsertMap, msgResultMap);
							}
						}
					} else if (j == 3) {
						for (int i = 1; i < 4; i++) {
							if (ExcelUtils.readValue(sheet, i + mapGridSize, "M") != null
									&& !ExcelUtils.readValue(sheet, i + mapGridSize, "M").equals("")) {

								this.mappingSetting(interfaceId, workbook, sheet, i, startIndex, intrfcMsgLayoutDtoList,
										msgMap, "RECV_RES_", "RECEIVE", "RESPONSE", "M", "L", mapGridSize, 8,
										msgInsertMap, msgResultMap);
							}
						}
					}
				}

				intrfccombsDto.setIntrfcmsglayoutdtDto(intrfcMsgLayoutDtoList);

				if (intrfccombsDto.getMsgTrnsfrmYn().equals("Y")) {
					Sheet reqMappingSheet = workbook.getSheet(ExcelUtils.readValue(sheet, 42, "C"));
					Sheet resMappingSheet = workbook.getSheet(ExcelUtils.readValue(sheet, 42, "I"));

					intrfccombsMappingReqDto = this.readMapping(reqMappingSheet, msgMap, intrfccombsDto,
							intrfccombsMappingReqDto, "SEND_REQ_", "RECV_REQ_", "REQUEST");
					intrfccombsMappingResDto = this.readMapping(resMappingSheet, msgMap, intrfccombsDto,
							intrfccombsMappingResDto, "RECV_RES_", "SEND_RES_", "RESPONSE");
					// ExcelUtils.readValue(reqMappingSheet, 5, "V")
				}

				List<IntrfccombsMappingDto> intrfccombsMappingReqDtoResult = new ArrayList<IntrfccombsMappingDto>();
				List<IntrfccombsMappingDto> intrfccombsMappingResDtoResult = new ArrayList<IntrfccombsMappingDto>();
				for (IntrfccombsMappingDto dto : intrfccombsMappingReqDto) {
					if (dto.getReqResTypeCd().equals("REQUEST")) {
						intrfccombsMappingReqDtoResult.add(dto);
					}
				}
				for (IntrfccombsMappingDto dto : intrfccombsMappingResDto) {
					if (dto.getReqResTypeCd().equals("RESPONSE")) {
						intrfccombsMappingResDtoResult.add(dto);
					}
				}

				intrfccombsDto.setIntrfccombsMappingReqDto(intrfccombsMappingReqDtoResult);
				intrfccombsDto.setIntrfccombsMappingResDto(intrfccombsMappingResDtoResult);

				intrfccombsDto.setIntrfcsrsysdtDto(srSysDtoList);
				intrfccombsDto.setFepDto(fepDto);
			}

		} else if (sheet.getSheetName().equals("대외배치")) {
			String trxDscd = getCodeValueNm("TRAN_DSCD", ExcelUtils.readValue(sheet, 6, "L"), "ko");

			String fileVer = ExcelUtils.readValue(sheet, 1, "S");

			StringBuilder errMsg = new StringBuilder();
			boolean isValid = true;

			String appCdL1 = ExcelUtils.readValue(sheet, 12, "E");
			String appCdL2 = ExcelUtils.readValue(sheet, 12, "F");
			String appCdL3 = ExcelUtils.readValue(sheet, 12, "G");
			sendSyscd = ExcelUtils.readValue(sheet, 7, "E");
			recvSysCd = ExcelUtils.readValue(sheet, 7, "L");

			isValid = intrfcValidation(isValid, errMsg, appCdL1, appCdL2, appCdL3, sendSyscd, recvSysCd, trxDscd);

			if (!isValid) {
				errMsg.append("\n시트명: " + sheet.getSheetName());
				throw new ServiceException(BxMessages.Error.INTRFC_VALID_CHECK, errMsg.toString());
			}

			IntrfcIdCreateDto interfaceIdDto = new IntrfcIdCreateDto();
			interfaceIdDto.setIntrfcTypeCd("FEP");
			interfaceIdDto.setLv1Cd(ExcelUtils.readValue(sheet, 12, "E"));
			interfaceIdDto.setLv2Cd(ExcelUtils.readValue(sheet, 12, "F"));
			interfaceIdDto.setLv3Cd(ExcelUtils.readValue(sheet, 12, "G"));
			interfaceIdDto.setSendSysCd(ExcelUtils.readValue(sheet, 7, "E"));
			interfaceIdDto.setReceiveSysCd(ExcelUtils.readValue(sheet, 7, "L"));
			// interfaceIdDto.setSyncAsyncDscd(getCodeValueNm("SYNC_DSCD",
			// ExcelUtils.readValue(sheet, 6, "L"), "ko"));
			interfaceIdDto.setTrxDscd(trxDscd);
			String interfaceId = this.getInterfaceId(interfaceIdDto).getIntrfcId();

			String intrfcIdDoc = ExcelUtils.readValue(sheet, 5, "E");
			insertYn = true;

			if (intrfcIdDoc != null && !intrfcIdDoc.equals("")) {
				interfaceId = intrfcIdDoc;
				insertYn = false;
			}

			intrfccombsDto.setIntrfcId(interfaceId);
			intrfccombsDto.setIntrfcNm(ExcelUtils.readValue(sheet, 5, "L"));
			intrfccombsDto.setWorkStatusCd("WORKING");
			intrfccombsDto.setRegManId(ExcelUtils.readValue(sheet, 3, "L"));
			intrfccombsDto.setRegDttm(DateUtils.getCurrentDate(9));
			intrfccombsDto.setTrxDscd(trxDscd);
			intrfccombsDto.setIntrfcTypeCd("FEP");
			intrfccombsDto.setLv1Cd(ExcelUtils.readValue(sheet, 12, "E"));
			intrfccombsDto.setLv2Cd(ExcelUtils.readValue(sheet, 12, "F"));
			intrfccombsDto.setLv3Cd(ExcelUtils.readValue(sheet, 12, "G"));
			intrfccombsDto.setSrTypeCd(getCodeValueNm("SENC_RECV_DSCD", ExcelUtils.readValue(sheet, 6, "E"), "ko"));

			List<IntrfcsrsysdtDto> srSysDtoList = new ArrayList<IntrfcsrsysdtDto>();
			IntrfcsrsysdtDto srSysDto = new IntrfcsrsysdtDto();
			IntrfccombsDetailFEPDto fepDto = new IntrfccombsDetailFEPDto();
			Map<String, String> msgMap = new HashMap<String, String>();
			List<IntrfcmsglayoutdtDto> intrfcMsgLayoutDtoList = new ArrayList<IntrfcmsglayoutdtDto>();
			int startIndex = 15;
			List<IntrfccombsMappingDto> intrfccombsMappingReqDto = new ArrayList<IntrfccombsMappingDto>();
			List<IntrfccombsMappingDto> intrfccombsMappingResDto = new ArrayList<IntrfccombsMappingDto>();
			List<IntrfcroutinfodtDto> intrfcroutinfodtDto = new ArrayList<IntrfcroutinfodtDto>();

			srSysDto.setSysCd(ExcelUtils.readValue(sheet, 7, "E"));
			srSysDto.setSysNm(ExcelUtils.readValue(sheet, 8, "E"));
			srSysDto.setIntrfcId(interfaceId);
			srSysDto.setSrSeq(1);
			srSysDto.setSrTypeCd("SEND");
			srSysDto.setFilePath(ExcelUtils.readValue(sheet, 9, "E"));
			srSysDto.setCrgManNm(ExcelUtils.readValue(sheet, 10, "E"));
			srSysDtoList.add(srSysDto);

			srSysDto = new IntrfcsrsysdtDto();
			srSysDto.setSysCd(ExcelUtils.readValue(sheet, 7, "L"));
			srSysDto.setSysNm(ExcelUtils.readValue(sheet, 8, "L"));
			srSysDto.setIntrfcId(interfaceId);
			srSysDto.setSrSeq(1);
			srSysDto.setSrTypeCd("RECEIVE");
			srSysDto.setFilePath(ExcelUtils.readValue(sheet, 9, "L"));
			srSysDto.setCrgManNm(ExcelUtils.readValue(sheet, 10, "L"));
			srSysDtoList.add(srSysDto);

			fepDto.setIntrfcUse(ExcelUtils.readValue(sheet, 13, "E"));
			fepDto.setIntrfDesc(ExcelUtils.readValue(sheet, 14, "E"));

			// 전송정보
			fepDto.setFileId(ExcelUtils.readValue(sheet, 17, "E"));// 파일ID
			String dupTrnsmsnAllwYn = ExcelUtils.readValue(sheet, 17, "H"); // 중복전송허용여부
			if (dupTrnsmsnAllwYn == null || dupTrnsmsnAllwYn.equals("")) {
				dupTrnsmsnAllwYn = "N";
			}
			fepDto.setDupTrnsmsnAllwYn(dupTrnsmsnAllwYn);// 중복전송허용여부
			fepDto.setRecordSize(ExcelUtils.readValue(sheet, 17, "L"));// 레코드사이즈
			String recordSep = getCodeValueNm("RECORD_SEPARATOR_CD", ExcelUtils.readValue(sheet, 18, "E"), "ko");
			if (recordSep == null || recordSep.equals("")) {
				recordSep = "LF";
			}
			fepDto.setRecordSeparator(recordSep);
			fepDto.setFileNm(ExcelUtils.readValue(sheet, 18, "H"));// 파일명

			String instCd = ExcelUtils.readValue(sheet, 21, "E");
			ExtrnlinstcdDto extrnlinstcdDto = extrnlinstcdDao.selectExtrnlinstcd(instCd, "INST");

			if (instCd != null && !instCd.equals("")) {
				if (extrnlinstcdDto == null) {
					errMsg.append("\n존재하지않는 기관코드입니다. ");
					isValid = false;
				}
			}

			String bizCd = ExcelUtils.readValue(sheet, 21, "H");

			if (bizCd != null && !bizCd.equals("")) {
				if (bizCdDao.selectBizcd(bizCd) == null) {
					errMsg.append("\n존재하지않는 기관업무코드입니다. ");
					isValid = false;
				}
			}

			intrfccombsDto.setInstCd(instCd);
			intrfccombsDto.setBizCd(bizCd);
			fepDto.setCurrIntrfcIdentifier(ExcelUtils.readValue(sheet, 21, "L"));
			fepDto.setPrivacyInclYn(ExcelUtils.readValue(sheet, 22, "E"));
			if (fileVer.equals("v20181012")) {
				fepDto.setFileTranIntrfcId(ExcelUtils.readValue(sheet, 22, "H"));
			} else {
				// fepDto.setDupFileProcMode(getCodeValueNm("DUP_FILE_PROC_CD",
				// ExcelUtils.readValue(sheet, 22, "H"), "ko"));
				fepDto.setFileTranIntrfcId(ExcelUtils.readValue(sheet, 23, "H"));
			}
			fepDto.setOccurCycle(getCodeValueNm("GEN_CYCLE_CD", ExcelUtils.readValue(sheet, 22, "L"), "ko"));
			fepDto.setEncTargetYn(ExcelUtils.readValue(sheet, 23, "E"));

			Map<String, String> msgInsertMap = new HashMap<String, String>();

			int mapGridSize = 28;

			for (int j = 0; j < 4; j++) {
				if (j == 0) {
					for (int i = 1; i < 6; i++) {
						if (ExcelUtils.readValue(sheet, i + mapGridSize, "C") != null
								&& !ExcelUtils.readValue(sheet, i + mapGridSize, "C").equals("")) {

							this.mappingSetting(interfaceId, workbook, sheet, i, startIndex, intrfcMsgLayoutDtoList,
									msgMap, "SEND_REQ_", "SEND", "REQUEST", "C", "E", mapGridSize, 7, msgInsertMap,
									msgResultMap);
						}
					}
				} else if (j == 1) {
					for (int i = 1; i < 4; i++) {
						if (ExcelUtils.readValue(sheet, i + mapGridSize, "G") != null
								&& !ExcelUtils.readValue(sheet, i + mapGridSize, "G").equals("")) {

							this.mappingSetting(interfaceId, workbook, sheet, i, startIndex, intrfcMsgLayoutDtoList,
									msgMap, "SEND_RES_", "SEND", "RESPONSE", "G", "E", mapGridSize, 7, msgInsertMap,
									msgResultMap);
						}
					}
				} else if (j == 2) {
					for (int i = 1; i < 4; i++) {
						if (ExcelUtils.readValue(sheet, i + mapGridSize, "I") != null
								&& !ExcelUtils.readValue(sheet, i + mapGridSize, "I").equals("")) {

							this.mappingSetting(interfaceId, workbook, sheet, i, startIndex, intrfcMsgLayoutDtoList,
									msgMap, "RECV_REQ_", "RECEIVE", "REQUEST", "I", "L", mapGridSize, 7, msgInsertMap,
									msgResultMap);
						}
					}
				} else if (j == 3) {
					for (int i = 1; i < 4; i++) {
						if (ExcelUtils.readValue(sheet, i + mapGridSize, "M") != null
								&& !ExcelUtils.readValue(sheet, i + mapGridSize, "M").equals("")) {

							this.mappingSetting(interfaceId, workbook, sheet, i, startIndex, intrfcMsgLayoutDtoList,
									msgMap, "RECV_RES_", "RECEIVE", "RESPONSE", "M", "L", mapGridSize, 7, msgInsertMap,
									msgResultMap);
						}
					}
				}
			}

			intrfccombsDto.setIntrfcmsglayoutdtDto(intrfcMsgLayoutDtoList);

			intrfccombsDto.setIntrfcsrsysdtDto(srSysDtoList);
			intrfccombsDto.setFepDto(fepDto);
		}

		// for (String key : msgResultMap.keySet()) {
		// MsgInsertDto msginsertDto = msgResultMap.get(key);
		// boolean insertYnmsg = msginsertDto.isInsertYn();
		// MsglayoutbsDto msgDto = msginsertDto.getMsglayoutbsDto();
		//
		// if (insertYnmsg) {
		// msglayoutService.add(msgDto, "FILE");
		// } else {
		// msglayoutService.update(msgDto);
		// }
		// }

		if (insertYn) {

			List<String> deployCodeList = new ArrayList<String>();
			String deploySysCd = "FEP";
			String deploySysCdSim = "SIM";
			String deploySysCdRush = "RSH";
			String deploySysCdE2E = "E2E";

			deployCodeList.add(deploySysCd);
			deployCodeList.add(deploySysCdSim);
			deployCodeList.add(deploySysCdRush);
			deployCodeList.add(deploySysCdE2E);

			if (deployCodeMap.containsKey(sendSyscd)) {
				deployCodeList.add(sendSyscd);
			}
			if (deployCodeMap.containsKey(recvSysCd)) {
				deployCodeList.add(recvSysCd);
			}

			IntrfcdeploysysdtDto intrfcDeployDto;
			int deploySeq = 1;
			List<IntrfcdeploysysdtDto> intrfcdeploysysdtDto = new ArrayList<IntrfcdeploysysdtDto>();

			for (String code : deployCodeList) {
				DepolysysbsDto dto = deployDao.selectDepolysysbs(code);
				if (dto != null) {
					intrfcDeployDto = new IntrfcdeploysysdtDto();

					intrfcDeployDto.setIntrfcId(intrfccombsDto.getIntrfcId());
					intrfcDeployDto.setDeploySysCd(dto.getDeploySysCd());
					intrfcDeployDto.setDeploySysNm(dto.getDeploySysNm());
					intrfcDeployDto.setDeploySysSeq(deploySeq);
					intrfcDeployDto.setDeployUrl(dto.getDeploySysUrl());
					intrfcdeploysysdtDto.add(intrfcDeployDto);

					deploySeq++;
				}
			}

			if (intrfcdeploysysdtDto.size() > 0) {
				intrfccombsDto.setIntrfcdeploysysdtDto(intrfcdeploysysdtDto);
			}

			this.add(intrfccombsDto, false);
		} else {

			this.update(intrfccombsDto, false);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	protected void readFepDataDummy(IntrfccombsDto intrfccombsDto, Workbook workbook, String intrfcTypeCd) {
		Sheet sheet = workbook.getSheet("Interface List_FEP(D)");
		boolean insertYn = true;

		String sendSyscd = "";
		String recvSysCd = "";

		Map<String, MsgInsertDto> msgResultMap = new HashMap<String, MsgInsertDto>();

		int rowNumber = 5;
		String interfaceId = "";
		while (true) {
			String endPoint = ExcelUtils.readValue(sheet, rowNumber, "A");
			String appCodeCheck = ExcelUtils.readValue(sheet, rowNumber, "B");
			if (endPoint.equals("end") || appCodeCheck.equals("") || appCodeCheck == null) {
				break;
			}

			if (intrfcTypeCd.equals("FEP")) {
				StringBuilder errMsg = new StringBuilder();
				boolean isValid = true;
				// 공통
				String appCdL1 = ExcelUtils.readValue(sheet, rowNumber, "B");
				sendSyscd = ExcelUtils.readValue(sheet, rowNumber, "N");
				recvSysCd = ExcelUtils.readValue(sheet, rowNumber, "Q");

				String trxDscd = ExcelUtils.readValue(sheet, rowNumber, "F").toUpperCase();

				IntrfcIdCreateDto interfaceIdDto = new IntrfcIdCreateDto();
				interfaceIdDto.setIntrfcTypeCd("FEP");
				interfaceIdDto.setLv1Cd(ExcelUtils.readValue(sheet, rowNumber, "B"));
				interfaceIdDto.setSendSysCd(ExcelUtils.readValue(sheet, rowNumber, "N"));
				interfaceIdDto.setReceiveSysCd(ExcelUtils.readValue(sheet, rowNumber, "Q"));
				interfaceIdDto.setTrxDscd(trxDscd);

				interfaceId = ExcelUtils.readValue(sheet, rowNumber, "D");

				intrfccombsDto.setIntrfcId(interfaceId);
				intrfccombsDto.setIntrfcNm(ExcelUtils.readValue(sheet, rowNumber, "E"));
				intrfccombsDto.setWorkStatusCd("WORKING");
				intrfccombsDto.setRegManId(ExcelUtils.readValue(sheet, rowNumber, "AE"));
				intrfccombsDto.setRegDttm(DateUtils.getCurrentDate(9));
				intrfccombsDto.setTrxDscd(trxDscd);
				intrfccombsDto.setIntrfcTypeCd("FEP");
				intrfccombsDto.setLv1Cd(appCdL1);

				String fepType = ExcelUtils.readValue(sheet, rowNumber, "G");
				if (fepType.equals("AP to AP")) {
					fepType = "APTOAP";
				} else if (fepType.equals("DB to DB")) {
					fepType = "DBTODB";
				} else if (fepType.equals("File to File")) {
					fepType = "FILETOFILE";
				} else {

				}
				intrfccombsDto.setIntrfcWayCd(fepType);

				// 송신시스템
				List<IntrfcsrsysdtDto> srSysDtoList = new ArrayList<IntrfcsrsysdtDto>();
				IntrfcsrsysdtDto srSysDto = new IntrfcsrsysdtDto();
				IntrfccombsDetailFEPDto fepDto = new IntrfccombsDetailFEPDto();
				Map<String, String> msgMap = new HashMap<String, String>();
				List<IntrfcmsglayoutdtDto> intrfcMsgLayoutDtoList = new ArrayList<IntrfcmsglayoutdtDto>();
				int startIndex = 15;
				List<IntrfccombsMappingDto> intrfccombsMappingReqDto = new ArrayList<IntrfccombsMappingDto>();
				List<IntrfccombsMappingDto> intrfccombsMappingResDto = new ArrayList<IntrfccombsMappingDto>();
				List<IntrfcroutinfodtDto> intrfcroutinfodtDto = new ArrayList<IntrfcroutinfodtDto>();

				srSysDto.setSysCd(ExcelUtils.readValue(sheet, rowNumber, "N"));
				srSysDto.setSysNm(ExcelUtils.readValue(sheet, rowNumber, "O"));
				srSysDto.setCrgManNm(ExcelUtils.readValue(sheet, rowNumber, "P"));
				srSysDto.setIntrfcId(interfaceId);
				srSysDto.setSrSeq(1);
				srSysDto.setSrTypeCd("SEND");
				srSysDtoList.add(srSysDto);

				// 수신시스템
				srSysDto = new IntrfcsrsysdtDto();
				srSysDto.setSysCd(ExcelUtils.readValue(sheet, rowNumber, "Q"));
				srSysDto.setSysNm(ExcelUtils.readValue(sheet, rowNumber, "R"));
				srSysDto.setCrgManNm(ExcelUtils.readValue(sheet, rowNumber, "S"));
				srSysDto.setIntrfcId(interfaceId);
				srSysDto.setSrSeq(1);
				srSysDto.setSrTypeCd("RECEIVE");

				fepDto.setIntrfDesc(ExcelUtils.readValue(sheet, rowNumber, "AG"));

				// 대외정보
				String instCd = ExcelUtils.readValue(sheet, rowNumber, "I");
				ExtrnlinstcdDto extrnlinstcdDto = extrnlinstcdDao.selectExtrnlinstcd(instCd, "INST");
				// 기관명 추가 해야함
				// 프로토콜 추가 해야함
				// 인코딩 추가 해야함
				// SPEC 문서 추가해야함
				intrfccombsDto.setInstCd(instCd);

				if (!isValid) {
					errMsg.append("\n시트명: " + sheet.getSheetName());
					throw new ServiceException(BxMessages.Error.INTRFC_VALID_CHECK, errMsg.toString());
				}

				// APTOAP
				if (fepType.equals("APTOAP")) {
					intrfccombsDto.setSyncAsyncDscd(
							getCodeValueNm("SYNC_DSCD_FEP", convertTermEngToKor(ExcelUtils.readValue(sheet, rowNumber, "U")), "ko"));
					srSysDto.setTrxCd(ExcelUtils.readValue(sheet, rowNumber, "T"));
					fepDto.setTimeOut(ExcelUtils.readValue(sheet, rowNumber, "W"));
					intrfccombsDto.setMsgTrnsfrmYn(ExcelUtils.readValue(sheet, rowNumber, "X"));
					intrfccombsDto.setRspsYn(ExcelUtils.readValue(sheet, rowNumber, "V"));
				} else if (fepType.equals("FILETOFILE")) {
					fepDto.setSendFileNm(ExcelUtils.readValue(sheet, rowNumber, "Y"));// 파일명
					fepDto.setSendFilePath(ExcelUtils.readValue(sheet, rowNumber, "Z"));// 송신파일경로
					fepDto.setRecvFilePath(ExcelUtils.readValue(sheet, rowNumber, "AA"));// 파일경로
					fepDto.setOccurCycle(ExcelUtils.readValue(sheet, rowNumber, "AB"));// 발생주기
				}

				srSysDtoList.add(srSysDto);
				intrfccombsDto.setFepDto(fepDto);
				intrfccombsDto.setIntrfcsrsysdtDto(srSysDtoList);
			}

			int checkYn = intrfccombsDao.checkIntrfcid(interfaceId);

			if (checkYn == 0) {
				this.addDefinition(intrfccombsDto, false);
			} else {
				this.updateDefinition(intrfccombsDto, false);
			}

			rowNumber++;
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	protected void readEaiData(IntrfccombsDto intrfccombsDto, Workbook workbook, String intrfcTypeCd) {

		Sheet sheet = workbook.getSheetAt(2);
		boolean insertYn = true;
		String sendSyscd = "";
		String recvSysCd = "";

		Map<String, MsgInsertDto> msgResultMap = new HashMap<String, MsgInsertDto>();

		if (intrfcTypeCd.equals("EAI_I")) {

			String title = ExcelUtils.readValue(sheet, 2, "B");
			int titleYn = title.indexOf("EAI");

			if (titleYn < 0) {
				throw new ServiceException(BxMessages.Error.FILE_UPLOAD_INTERFACE_TYPE);
			}

			if (sheet.getSheetName().equals("대내온라인_APTOAP")) {

				StringBuilder errMsg = new StringBuilder();
				boolean isValid = true;

				String appCdL1 = ExcelUtils.readValue(sheet, 14, "E");
				String appCdL2 = ExcelUtils.readValue(sheet, 14, "G");
				String appCdL3 = ExcelUtils.readValue(sheet, 14, "H");
				sendSyscd = ExcelUtils.readValue(sheet, 8, "E");
				recvSysCd = ExcelUtils.readValue(sheet, 8, "M");
				String trxDscd = getCodeValueNm("TRAN_DSCD", ExcelUtils.readValue(sheet, 7, "E"), "ko");

				isValid = intrfcValidation(isValid, errMsg, appCdL1, appCdL2, appCdL3, sendSyscd, recvSysCd, trxDscd);

				if (!isValid) {
					errMsg.append("\n시트명: " + sheet.getSheetName());
					throw new ServiceException(BxMessages.Error.INTRFC_VALID_CHECK, errMsg.toString());
				}

				String intrfcWayCd = getCodeValueNm("INTRFC_WAY_CD", ExcelUtils.readValue(sheet, 7, "M"), "ko");

				IntrfcIdCreateDto interfaceIdDto = new IntrfcIdCreateDto();
				interfaceIdDto.setIntrfcTypeCd("EAI_I");
				interfaceIdDto.setLv1Cd(appCdL1);
				interfaceIdDto.setLv2Cd(appCdL2);
				interfaceIdDto.setLv3Cd(appCdL3);
				interfaceIdDto.setSendSysCd(sendSyscd);
				interfaceIdDto.setReceiveSysCd(recvSysCd);
				// interfaceIdDto.setSyncAsyncDscd(getCodeValueNm("SYNC_DSCD",
				// ExcelUtils.readValue(sheet, 6, "L"), "ko"));
				interfaceIdDto.setTrxDscd(trxDscd);
				String interfaceId = this.getInterfaceId(interfaceIdDto).getIntrfcId();

				String intrfcIdDoc = ExcelUtils.readValue(sheet, 5, "E");
				insertYn = true;

				if (intrfcIdDoc != null && !intrfcIdDoc.equals("")) {
					interfaceId = intrfcIdDoc;
					insertYn = false;
				}

				intrfccombsDto.setIntrfcId(interfaceId);
				intrfccombsDto.setIntrfcNm(ExcelUtils.readValue(sheet, 5, "M"));
				intrfccombsDto.setWorkStatusCd("WORKING");
				intrfccombsDto.setRegManId(ExcelUtils.readValue(sheet, 3, "M"));
				intrfccombsDto.setRegDttm(DateUtils.getCurrentDate(9));
				intrfccombsDto.setMsgTrnsfrmYn(ExcelUtils.readValue(sheet, 12, "E"));
				intrfccombsDto.setTrxDscd(trxDscd);
				intrfccombsDto.setIntrfcTypeCd(intrfcTypeCd);
				intrfccombsDto.setLv1Cd(ExcelUtils.readValue(sheet, 14, "E"));
				intrfccombsDto.setLv2Cd(ExcelUtils.readValue(sheet, 14, "G"));
				intrfccombsDto.setLv3Cd(ExcelUtils.readValue(sheet, 14, "H"));
				intrfccombsDto.setSyncAsyncDscd(getCodeValueNm("SYNC_DSCD", ExcelUtils.readValue(sheet, 6, "M"), "ko"));
				intrfccombsDto.setSrTypeCd(getCodeValueNm("SENC_RECV_DSCD", ExcelUtils.readValue(sheet, 6, "E"), "ko"));
				intrfccombsDto.setIntrfcWayCd(intrfcWayCd);

				List<IntrfcsrsysdtDto> srSysDtoList = new ArrayList<IntrfcsrsysdtDto>();
				IntrfcsrsysdtDto srSysDto = new IntrfcsrsysdtDto();
				IntrfccombsDetailEAIDto eaiDto = new IntrfccombsDetailEAIDto();
				Map<String, String> msgMap = new HashMap<String, String>();
				List<IntrfcmsglayoutdtDto> intrfcMsgLayoutDtoList = new ArrayList<IntrfcmsglayoutdtDto>();
				int startIndex = 15;
				List<IntrfccombsMappingDto> intrfccombsMappingReqDto = new ArrayList<IntrfccombsMappingDto>();
				List<IntrfccombsMappingDto> intrfccombsMappingResDto = new ArrayList<IntrfccombsMappingDto>();

				srSysDto.setSysCd(ExcelUtils.readValue(sheet, 8, "E"));
				srSysDto.setSysNm(ExcelUtils.readValue(sheet, 9, "E"));
				srSysDto.setCrgManNm(ExcelUtils.readValue(sheet, 10, "E"));
				srSysDto.setIntrfcId(interfaceId);
				srSysDto.setSrSeq(1);
				srSysDto.setSrTypeCd("SEND");
				srSysDtoList.add(srSysDto);

				srSysDto = new IntrfcsrsysdtDto();
				srSysDto.setSysCd(ExcelUtils.readValue(sheet, 8, "M"));
				srSysDto.setSysNm(ExcelUtils.readValue(sheet, 9, "M"));
				srSysDto.setCrgManNm(ExcelUtils.readValue(sheet, 10, "M"));
				srSysDto.setIntrfcId(interfaceId);
				srSysDto.setSrSeq(1);
				srSysDto.setSrTypeCd("RECEIVE");

				String trxCd = ExcelUtils.readValue(sheet, 11, "M");
				if (!trxCd.equals("") && trxCd != null) {
					int i = trxcdDao.selectAllCnt(trxCd, null, null, null);
					if (i == 0) {
						errMsg.append("\n존재하지 않는 거래코드입니다.");
						isValid = false;
					}
				}

				if (!isValid) {
					errMsg.append("\n시트명: " + sheet.getSheetName());
					throw new ServiceException(BxMessages.Error.INTRFC_VALID_CHECK, errMsg.toString());
				}

				srSysDto.setTrxCd(trxCd);
				srSysDtoList.add(srSysDto);

				// eaiDto.setIntrfcUse(ExcelUtils.readValue(sheet, 15, "E"));
				eaiDto.setIntrfDesc(ExcelUtils.readValue(sheet, 16, "E"));

				eaiDto.setTimeOut(ExcelUtils.readValue(sheet, 19, "E")); // 타임아웃
				eaiDto.setOccurCycle(getCodeValueNm("GEN_CYCLE_CD", ExcelUtils.readValue(sheet, 19, "I"), "ko"));// 발생주기
				eaiDto.setDayOccurCnt(ExcelUtils.readValue(sheet, 19, "M"));// 일발생건수
				eaiDto.setPrivacyInclYn(ExcelUtils.readValue(sheet, 20, "E"));// 개인정보포함여부
				eaiDto.setCurrIntrfcIdentifier(ExcelUtils.readValue(sheet, 20, "I"));// 현행인터페이스식별자
				eaiDto.setBackupAprvStat(getCodeValueNm("BACKUP_CD", ExcelUtils.readValue(sheet, 20, "M"), "ko"));// 백업승인상태확인
				eaiDto.setEncTargetYn(ExcelUtils.readValue(sheet, 21, "E"));// 암호화대상여부

				intrfccombsDto.setIntrfcsrsysdtDto(srSysDtoList);
				intrfccombsDto.setEaiDto(eaiDto);
				int mapGridSize = 26;
				Map<String, String> msgInsertMap = new HashMap<String, String>();

				for (int j = 0; j < 4; j++) {
					if (j == 0) {
						for (int i = 1; i < 4; i++) {
							if (ExcelUtils.readValue(sheet, i + mapGridSize, "C") != null
									&& !ExcelUtils.readValue(sheet, i + mapGridSize, "C").equals("")) {

								this.mappingSetting(interfaceId, workbook, sheet, i, startIndex, intrfcMsgLayoutDtoList,
										msgMap, "SEND_REQ_", "SEND", "REQUEST", "C", "E", mapGridSize, 8, msgInsertMap,
										msgResultMap);
							}
						}
					} else if (j == 1) {
						for (int i = 1; i < 4; i++) {
							if (ExcelUtils.readValue(sheet, i + mapGridSize, "G") != null
									&& !ExcelUtils.readValue(sheet, i + mapGridSize, "G").equals("")) {

								this.mappingSetting(interfaceId, workbook, sheet, i, startIndex, intrfcMsgLayoutDtoList,
										msgMap, "SEND_RES_", "SEND", "RESPONSE", "G", "E", mapGridSize, 8, msgInsertMap,
										msgResultMap);
							}
						}
					} else if (j == 2) {
						for (int i = 1; i < 4; i++) {
							if (ExcelUtils.readValue(sheet, i + mapGridSize, "J") != null
									&& !ExcelUtils.readValue(sheet, i + mapGridSize, "J").equals("")) {

								this.mappingSetting(interfaceId, workbook, sheet, i, startIndex, intrfcMsgLayoutDtoList,
										msgMap, "RECV_REQ_", "RECEIVE", "REQUEST", "J", "M", mapGridSize, 8,
										msgInsertMap, msgResultMap);
							}
						}
					} else if (j == 3) {
						for (int i = 1; i < 4; i++) {
							if (ExcelUtils.readValue(sheet, i + mapGridSize, "N") != null
									&& !ExcelUtils.readValue(sheet, i + mapGridSize, "N").equals("")) {

								this.mappingSetting(interfaceId, workbook, sheet, i, startIndex, intrfcMsgLayoutDtoList,
										msgMap, "RECV_RES_", "RECEIVE", "RESPONSE", "N", "M", mapGridSize, 8,
										msgInsertMap, msgResultMap);
							}
						}
					}
				}

				intrfccombsDto.setIntrfcmsglayoutdtDto(intrfcMsgLayoutDtoList);

				if (intrfccombsDto.getMsgTrnsfrmYn().equals("Y")) {
					Sheet reqMappingSheet = workbook.getSheet(ExcelUtils.readValue(sheet, 34, "C"));
					Sheet resMappingSheet = workbook.getSheet(ExcelUtils.readValue(sheet, 34, "J"));

					intrfccombsMappingReqDto = this.readMapping(reqMappingSheet, msgMap, intrfccombsDto,
							intrfccombsMappingReqDto, "SEND_REQ_", "RECV_REQ_", "REQUEST");
					intrfccombsMappingResDto = this.readMapping(resMappingSheet, msgMap, intrfccombsDto,
							intrfccombsMappingResDto, "RECV_RES_", "SEND_RES_", "RESPONSE");
				}
				List<IntrfccombsMappingDto> intrfccombsMappingReqDtoResult = new ArrayList<IntrfccombsMappingDto>();
				List<IntrfccombsMappingDto> intrfccombsMappingResDtoResult = new ArrayList<IntrfccombsMappingDto>();
				for (IntrfccombsMappingDto dto : intrfccombsMappingReqDto) {
					if (dto.getReqResTypeCd().equals("REQUEST")) {
						intrfccombsMappingReqDtoResult.add(dto);
					}
				}
				for (IntrfccombsMappingDto dto : intrfccombsMappingResDto) {
					if (dto.getReqResTypeCd().equals("RESPONSE")) {
						intrfccombsMappingResDtoResult.add(dto);
					}
				}

				intrfccombsDto.setIntrfccombsMappingReqDto(intrfccombsMappingReqDtoResult);
				intrfccombsDto.setIntrfccombsMappingResDto(intrfccombsMappingResDtoResult);

			} else if (sheet.getSheetName().equals("대내온라인_APTODB")) {
				String intrfcWayCd = getCodeValueNm("INTRFC_WAY_CD", ExcelUtils.readValue(sheet, 7, "M"), "ko");

				StringBuilder errMsg = new StringBuilder();
				boolean isValid = true;

				String appCdL1 = ExcelUtils.readValue(sheet, 13, "E");
				String appCdL2 = ExcelUtils.readValue(sheet, 13, "G");
				String appCdL3 = ExcelUtils.readValue(sheet, 13, "H");
				sendSyscd = ExcelUtils.readValue(sheet, 8, "E");
				recvSysCd = ExcelUtils.readValue(sheet, 8, "M");
				String trxDscd = getCodeValueNm("TRAN_DSCD", ExcelUtils.readValue(sheet, 7, "E"), "ko");

				isValid = intrfcValidation(isValid, errMsg, appCdL1, appCdL2, appCdL3, sendSyscd, recvSysCd, trxDscd);

				if (!isValid) {
					errMsg.append("\n시트명: " + sheet.getSheetName());
					throw new ServiceException(BxMessages.Error.INTRFC_VALID_CHECK, errMsg.toString());
				}

				IntrfcIdCreateDto interfaceIdDto = new IntrfcIdCreateDto();
				interfaceIdDto.setIntrfcTypeCd("EAI_I");
				interfaceIdDto.setLv1Cd(ExcelUtils.readValue(sheet, 13, "E"));
				interfaceIdDto.setLv2Cd(ExcelUtils.readValue(sheet, 13, "G"));
				interfaceIdDto.setLv3Cd(ExcelUtils.readValue(sheet, 13, "H"));
				interfaceIdDto.setSendSysCd(ExcelUtils.readValue(sheet, 8, "E"));
				interfaceIdDto.setReceiveSysCd(ExcelUtils.readValue(sheet, 8, "M"));
				interfaceIdDto.setTrxDscd(trxDscd);
				String interfaceId = this.getInterfaceId(interfaceIdDto).getIntrfcId();

				String intrfcIdDoc = ExcelUtils.readValue(sheet, 5, "E");
				insertYn = true;

				if (intrfcIdDoc != null && !intrfcIdDoc.equals("")) {
					interfaceId = intrfcIdDoc;
					insertYn = false;
				}

				intrfccombsDto.setIntrfcId(interfaceId);
				intrfccombsDto.setIntrfcNm(ExcelUtils.readValue(sheet, 5, "M"));
				intrfccombsDto.setWorkStatusCd("WORKING");
				intrfccombsDto.setRegManId(ExcelUtils.readValue(sheet, 3, "M"));
				intrfccombsDto.setRegDttm(DateUtils.getCurrentDate(9));
				intrfccombsDto.setTrxDscd(trxDscd);
				intrfccombsDto.setIntrfcTypeCd(intrfcTypeCd);
				intrfccombsDto.setLv1Cd(ExcelUtils.readValue(sheet, 13, "E"));
				intrfccombsDto.setLv2Cd(ExcelUtils.readValue(sheet, 13, "G"));
				intrfccombsDto.setLv3Cd(ExcelUtils.readValue(sheet, 13, "H"));
				intrfccombsDto.setSyncAsyncDscd(getCodeValueNm("SYNC_DSCD", ExcelUtils.readValue(sheet, 6, "M"), "ko"));
				intrfccombsDto.setSrTypeCd(getCodeValueNm("SENC_RECV_DSCD", ExcelUtils.readValue(sheet, 6, "E"), "ko"));
				intrfccombsDto.setIntrfcWayCd(intrfcWayCd);
				intrfccombsDto.setRspsYn(ExcelUtils.readValue(sheet, 11, "E"));

				List<IntrfcsrsysdtDto> srSysDtoList = new ArrayList<IntrfcsrsysdtDto>();
				IntrfcsrsysdtDto srSysDto = new IntrfcsrsysdtDto();
				IntrfccombsDetailEAIDto eaiDto = new IntrfccombsDetailEAIDto();
				Map<String, String> msgMap = new HashMap<String, String>();
				List<IntrfcmsglayoutdtDto> intrfcMsgLayoutDtoList = new ArrayList<IntrfcmsglayoutdtDto>();
				int startIndex = 15;

				srSysDto.setSysCd(ExcelUtils.readValue(sheet, 8, "E"));
				srSysDto.setSysNm(ExcelUtils.readValue(sheet, 9, "E"));
				srSysDto.setCrgManNm(ExcelUtils.readValue(sheet, 10, "E"));
				srSysDto.setIntrfcId(interfaceId);
				srSysDto.setSrSeq(1);
				srSysDto.setSrTypeCd("SEND");
				srSysDtoList.add(srSysDto);

				srSysDto = new IntrfcsrsysdtDto();
				srSysDto.setSysCd(ExcelUtils.readValue(sheet, 8, "M"));
				srSysDto.setSysNm(ExcelUtils.readValue(sheet, 9, "M"));
				srSysDto.setCrgManNm(ExcelUtils.readValue(sheet, 10, "M"));
				srSysDto.setIntrfcId(interfaceId);
				srSysDto.setSrSeq(1);
				srSysDto.setSrTypeCd("RECEIVE");
				srSysDtoList.add(srSysDto);

				// eaiDto.setIntrfcUse(ExcelUtils.readValue(sheet, 14, "E"));
				eaiDto.setIntrfDesc(ExcelUtils.readValue(sheet, 15, "E"));

				eaiDto.setRecvDbActionType(
						getCodeValueNm("RECV_DB_ACT_TYPE", ExcelUtils.readValue(sheet, 18, "E"), "ko"));// 수신DB동작유형
				eaiDto.setLobColNm(ExcelUtils.readValue(sheet, 18, "I"));// 랍컬럼명

				if (!ExcelUtils.readValue(sheet, 18, "M").equals("") && ExcelUtils.readValue(sheet, 18, "M") != null) {
					eaiDto.setSearchProcCnt(Integer.parseInt(ExcelUtils.readValue(sheet, 18, "M")));// 조회처리건수
				}
				eaiDto.setErrSkipYn(ExcelUtils.readValue(sheet, 19, "E"));// 에러스킵여부
				eaiDto.setRecvDbQuery(ExcelUtils.readValue(sheet, 19, "I"));// 수신디비쿼리문

				eaiDto.setTimeOut(ExcelUtils.readValue(sheet, 23, "E")); // 타임아웃
				eaiDto.setOccurCycle(getCodeValueNm("GEN_CYCLE_CD", ExcelUtils.readValue(sheet, 23, "I"), "ko"));// 발생주기
				eaiDto.setDayOccurCnt(ExcelUtils.readValue(sheet, 23, "M"));// 일발생건수
				eaiDto.setPrivacyInclYn(ExcelUtils.readValue(sheet, 24, "E"));// 개인정보포함여부
				eaiDto.setCurrIntrfcIdentifier(ExcelUtils.readValue(sheet, 24, "I"));// 현행인터페이스식별자
				eaiDto.setBackupAprvStat(getCodeValueNm("BACKUP_CD", ExcelUtils.readValue(sheet, 24, "M"), "ko"));// 백업승인상태확인
				eaiDto.setEncTargetYn(ExcelUtils.readValue(sheet, 25, "E"));// 암호화대상여부

				intrfccombsDto.setIntrfcsrsysdtDto(srSysDtoList);
				intrfccombsDto.setEaiDto(eaiDto);

				int mapGridSize = 30;

				Map<String, String> msgInsertMap = new HashMap<String, String>();

				for (int j = 0; j < 2; j++) {
					if (j == 0) {
						for (int i = 1; i < 4; i++) {
							if (ExcelUtils.readValue(sheet, i + mapGridSize, "C") != null
									&& !ExcelUtils.readValue(sheet, i + mapGridSize, "C").equals("")) {

								this.mappingSetting(interfaceId, workbook, sheet, i, startIndex, intrfcMsgLayoutDtoList,
										msgMap, "SEND_REQ_", "SEND", "REQUEST", "C", "E", mapGridSize, 8, msgInsertMap,
										msgResultMap);
							}
						}
					} else if (j == 1) {
						for (int i = 1; i < 4; i++) {
							if (ExcelUtils.readValue(sheet, i + mapGridSize, "J") != null
									&& !ExcelUtils.readValue(sheet, i + mapGridSize, "J").equals("")) {

								this.mappingSetting(interfaceId, workbook, sheet, i, startIndex, intrfcMsgLayoutDtoList,
										msgMap, "SEND_RES_", "SEND", "RESPONSE", "J", "E", mapGridSize, 8, msgInsertMap,
										msgResultMap);
							}
						}
					}
				}

				intrfccombsDto.setIntrfcmsglayoutdtDto(intrfcMsgLayoutDtoList);

			} else if (sheet.getSheetName().equals("대내배치_DBTODB")) {

				String intrfcWayCd = getCodeValueNm("INTRFC_WAY_CD", ExcelUtils.readValue(sheet, 7, "E"), "ko");

				StringBuilder errMsg = new StringBuilder();
				boolean isValid = true;

				String appCdL1 = ExcelUtils.readValue(sheet, 12, "E");
				String appCdL2 = ExcelUtils.readValue(sheet, 12, "G");
				String appCdL3 = ExcelUtils.readValue(sheet, 12, "H");
				sendSyscd = ExcelUtils.readValue(sheet, 8, "E");
				recvSysCd = ExcelUtils.readValue(sheet, 8, "M");
				String trxDscd = getCodeValueNm("TRAN_DSCD", ExcelUtils.readValue(sheet, 6, "M"), "ko");

				isValid = intrfcValidation(isValid, errMsg, appCdL1, appCdL2, appCdL3, sendSyscd, recvSysCd, trxDscd);

				if (!isValid) {
					errMsg.append("\n시트명: " + sheet.getSheetName());
					throw new ServiceException(BxMessages.Error.INTRFC_VALID_CHECK, errMsg.toString());
				}

				IntrfcIdCreateDto interfaceIdDto = new IntrfcIdCreateDto();
				interfaceIdDto.setIntrfcTypeCd("EAI_I");
				interfaceIdDto.setLv1Cd(ExcelUtils.readValue(sheet, 12, "E"));
				interfaceIdDto.setLv2Cd(ExcelUtils.readValue(sheet, 12, "G"));
				interfaceIdDto.setLv3Cd(ExcelUtils.readValue(sheet, 12, "H"));
				interfaceIdDto.setSendSysCd(ExcelUtils.readValue(sheet, 8, "E"));
				interfaceIdDto.setReceiveSysCd(ExcelUtils.readValue(sheet, 8, "M"));
				interfaceIdDto.setTrxDscd(trxDscd);
				String interfaceId = this.getInterfaceId(interfaceIdDto).getIntrfcId();

				String intrfcIdDoc = ExcelUtils.readValue(sheet, 5, "E");
				insertYn = true;

				if (intrfcIdDoc != null && !intrfcIdDoc.equals("")) {
					interfaceId = intrfcIdDoc;
					insertYn = false;
				}

				intrfccombsDto.setIntrfcId(interfaceId);
				intrfccombsDto.setIntrfcNm(ExcelUtils.readValue(sheet, 5, "M"));
				intrfccombsDto.setWorkStatusCd("WORKING");
				intrfccombsDto.setSrTypeCd(getCodeValueNm("SENC_RECV_DSCD", ExcelUtils.readValue(sheet, 6, "E"), "ko"));
				intrfccombsDto.setIntrfcWayCd(intrfcWayCd);
				intrfccombsDto.setIntrfcTypeCd(intrfcTypeCd);
				intrfccombsDto.setRegManId(ExcelUtils.readValue(sheet, 3, "M"));
				intrfccombsDto.setRegDttm(DateUtils.getCurrentDate(9));
				intrfccombsDto.setTrxDscd(trxDscd);
				intrfccombsDto.setLv1Cd(ExcelUtils.readValue(sheet, 12, "E"));
				intrfccombsDto.setLv2Cd(ExcelUtils.readValue(sheet, 12, "G"));
				intrfccombsDto.setLv3Cd(ExcelUtils.readValue(sheet, 12, "H"));

				List<IntrfcsrsysdtDto> srSysDtoList = new ArrayList<IntrfcsrsysdtDto>();
				IntrfcsrsysdtDto srSysDto = new IntrfcsrsysdtDto();
				IntrfccombsDetailEAIDto eaiDto = new IntrfccombsDetailEAIDto();
				Map<String, String> msgMap = new HashMap<String, String>();
				List<IntrfcmsglayoutdtDto> intrfcMsgLayoutDtoList = new ArrayList<IntrfcmsglayoutdtDto>();
				int startIndex = 15;

				srSysDto.setSysCd(ExcelUtils.readValue(sheet, 8, "E"));
				srSysDto.setSysNm(ExcelUtils.readValue(sheet, 9, "E"));
				srSysDto.setCrgManNm(ExcelUtils.readValue(sheet, 10, "E"));
				srSysDto.setIntrfcId(interfaceId);
				srSysDto.setSrSeq(1);
				srSysDto.setSrTypeCd("SEND");
				srSysDtoList.add(srSysDto);

				srSysDto = new IntrfcsrsysdtDto();
				srSysDto.setSysCd(ExcelUtils.readValue(sheet, 8, "M"));
				srSysDto.setSysNm(ExcelUtils.readValue(sheet, 9, "M"));
				srSysDto.setCrgManNm(ExcelUtils.readValue(sheet, 10, "M"));
				srSysDto.setIntrfcId(interfaceId);
				srSysDto.setSrSeq(1);
				srSysDto.setSrTypeCd("RECEIVE");
				srSysDtoList.add(srSysDto);

				// eaiDto.setIntrfcUse(ExcelUtils.readValue(sheet, 13, "E"));
				eaiDto.setIntrfDesc(ExcelUtils.readValue(sheet, 14, "E"));

				eaiDto.setRecvDbActionType(
						getCodeValueNm("RECV_DB_ACT_TYPE", ExcelUtils.readValue(sheet, 17, "E"), "ko"));// 수신DB동작유형
				eaiDto.setLobColNm(ExcelUtils.readValue(sheet, 17, "I"));// 랍컬럼명

				if (!ExcelUtils.readValue(sheet, 17, "M").equals("") && ExcelUtils.readValue(sheet, 17, "M") != null) {
					eaiDto.setSearchProcCnt(Integer.parseInt(ExcelUtils.readValue(sheet, 17, "M")));// 조회처리건수
				}

				eaiDto.setErrSkipYn(ExcelUtils.readValue(sheet, 18, "E"));// 에러스킵여부
				eaiDto.setSendDbQuery(ExcelUtils.readValue(sheet, 19, "E"));// 송신DB쿼리문
				eaiDto.setRecvDbQuery(ExcelUtils.readValue(sheet, 19, "I"));// 수신디비쿼리문

				eaiDto.setPrivacyInclYn(ExcelUtils.readValue(sheet, 23, "E"));// 개인정보포함여부
				eaiDto.setCurrIntrfcIdentifier(ExcelUtils.readValue(sheet, 23, "I"));// 현행인터페이스식별자
				eaiDto.setBackupAprvStat(getCodeValueNm("BACKUP_CD", ExcelUtils.readValue(sheet, 23, "M"), "ko"));// 백업승인상태확인
				eaiDto.setOccurCycle(getCodeValueNm("GEN_CYCLE_CD", ExcelUtils.readValue(sheet, 24, "E"), "ko"));// 발생주기
				eaiDto.setDayOccurCnt(ExcelUtils.readValue(sheet, 24, "I"));// 일발생건수
				eaiDto.setEncTargetYn(ExcelUtils.readValue(sheet, 24, "M"));// 암호화대상여부

				intrfccombsDto.setIntrfcsrsysdtDto(srSysDtoList);
				intrfccombsDto.setEaiDto(eaiDto);

			} else if (sheet.getSheetName().equals("대내배치_FILETOFILE")) {
				String intrfcWayCd = getCodeValueNm("INTRFC_WAY_CD", ExcelUtils.readValue(sheet, 7, "E"), "ko");
				String fileVer = ExcelUtils.readValue(sheet, 1, "S");
				StringBuilder errMsg = new StringBuilder();
				boolean isValid = true;

				String appCdL1 = ExcelUtils.readValue(sheet, 12, "E");
				String appCdL2 = ExcelUtils.readValue(sheet, 12, "F");
				String appCdL3 = ExcelUtils.readValue(sheet, 12, "G");
				sendSyscd = ExcelUtils.readValue(sheet, 8, "E");
				recvSysCd = ExcelUtils.readValue(sheet, 8, "L");
				String trxDscd = getCodeValueNm("TRAN_DSCD", ExcelUtils.readValue(sheet, 6, "L"), "ko");

				isValid = intrfcValidation(isValid, errMsg, appCdL1, appCdL2, appCdL3, sendSyscd, recvSysCd, trxDscd);

				if (!isValid) {
					errMsg.append("\n시트명: " + sheet.getSheetName());
					throw new ServiceException(BxMessages.Error.INTRFC_VALID_CHECK, errMsg.toString());
				}

				IntrfcIdCreateDto interfaceIdDto = new IntrfcIdCreateDto();
				interfaceIdDto.setIntrfcTypeCd("EAI_I");
				interfaceIdDto.setLv1Cd(ExcelUtils.readValue(sheet, 12, "E"));
				interfaceIdDto.setLv2Cd(ExcelUtils.readValue(sheet, 12, "F"));
				interfaceIdDto.setLv3Cd(ExcelUtils.readValue(sheet, 12, "G"));
				interfaceIdDto.setSendSysCd(ExcelUtils.readValue(sheet, 8, "E"));
				interfaceIdDto.setReceiveSysCd(ExcelUtils.readValue(sheet, 8, "L"));
				interfaceIdDto.setTrxDscd(trxDscd);
				String interfaceId = this.getInterfaceId(interfaceIdDto).getIntrfcId();

				String intrfcIdDoc = ExcelUtils.readValue(sheet, 5, "E");
				insertYn = true;

				if (intrfcIdDoc != null && !intrfcIdDoc.equals("")) {
					interfaceId = intrfcIdDoc;
					insertYn = false;
				}

				intrfccombsDto.setIntrfcId(interfaceId);
				intrfccombsDto.setIntrfcNm(ExcelUtils.readValue(sheet, 5, "L"));
				intrfccombsDto.setWorkStatusCd("WORKING");
				intrfccombsDto.setSrTypeCd(getCodeValueNm("SENC_RECV_DSCD", ExcelUtils.readValue(sheet, 6, "E"), "ko"));
				intrfccombsDto.setIntrfcWayCd(intrfcWayCd);
				intrfccombsDto.setIntrfcTypeCd(intrfcTypeCd);
				intrfccombsDto.setRegManId(ExcelUtils.readValue(sheet, 3, "L"));
				intrfccombsDto.setRegDttm(DateUtils.getCurrentDate(9));
				intrfccombsDto.setTrxDscd(trxDscd);
				intrfccombsDto.setLv1Cd(ExcelUtils.readValue(sheet, 12, "E"));
				intrfccombsDto.setLv2Cd(ExcelUtils.readValue(sheet, 12, "F"));
				intrfccombsDto.setLv3Cd(ExcelUtils.readValue(sheet, 12, "G"));

				List<IntrfcsrsysdtDto> srSysDtoList = new ArrayList<IntrfcsrsysdtDto>();
				IntrfcsrsysdtDto srSysDto = new IntrfcsrsysdtDto();
				IntrfccombsDetailEAIDto eaiDto = new IntrfccombsDetailEAIDto();
				Map<String, String> msgMap = new HashMap<String, String>();
				List<IntrfcmsglayoutdtDto> intrfcMsgLayoutDtoList = new ArrayList<IntrfcmsglayoutdtDto>();
				int startIndex = 15;

				srSysDto.setSysCd(ExcelUtils.readValue(sheet, 8, "E"));
				srSysDto.setSysNm(ExcelUtils.readValue(sheet, 9, "E"));
				srSysDto.setCrgManNm(ExcelUtils.readValue(sheet, 10, "E"));
				srSysDto.setIntrfcId(interfaceId);
				srSysDto.setSrSeq(1);
				srSysDto.setSrTypeCd("SEND");
				srSysDtoList.add(srSysDto);

				srSysDto = new IntrfcsrsysdtDto();
				srSysDto.setSysCd(ExcelUtils.readValue(sheet, 8, "L"));
				srSysDto.setSysNm(ExcelUtils.readValue(sheet, 9, "L"));
				srSysDto.setCrgManNm(ExcelUtils.readValue(sheet, 10, "L"));
				srSysDto.setIntrfcId(interfaceId);
				srSysDto.setSrSeq(1);
				srSysDto.setSrTypeCd("RECEIVE");
				srSysDtoList.add(srSysDto);

				// eaiDto.setIntrfcUse(ExcelUtils.readValue(sheet, 13, "E"));
				eaiDto.setIntrfDesc(ExcelUtils.readValue(sheet, 14, "E"));

				// 송신
				eaiDto.setSendFilePath(ExcelUtils.readValue(sheet, 17, "E"));// 파일경로
				eaiDto.setSendFileNm(ExcelUtils.readValue(sheet, 17, "H"));// 파일명
				eaiDto.setSendTranPostProc(
						getCodeValueNm("TRAN_POST_PROC", ExcelUtils.readValue(sheet, 17, "L"), "ko"));// 전송후처리유형
				eaiDto.setSendTranPostBackPath(ExcelUtils.readValue(sheet, 18, "E"));// 전송후백업경로

				// 수신
				eaiDto.setRecvFilePath(ExcelUtils.readValue(sheet, 21, "E"));// 파일경로
				eaiDto.setRecvFileNm(ExcelUtils.readValue(sheet, 21, "H"));// 파일명
				eaiDto.setRecvTranPostFinCreateYn(ExcelUtils.readValue(sheet, 21, "L"));// 전송후핀파일생성여부
				eaiDto.setRecvTranPostScript(ExcelUtils.readValue(sheet, 22, "E"));// 전송후후행스크립트

				eaiDto.setOccurCycle(getCodeValueNm("GEN_CYCLE_CD", ExcelUtils.readValue(sheet, 25, "E"), "ko"));// 발생주기
				eaiDto.setPrivacyInclYn(ExcelUtils.readValue(sheet, 25, "H"));// 개인정보포함여부
				eaiDto.setBackupAprvStat(getCodeValueNm("BACKUP_CD", ExcelUtils.readValue(sheet, 25, "L"), "ko"));// 백업승인상태확인
				eaiDto.setEncTargetYn(ExcelUtils.readValue(sheet, 26, "E"));// 암호화대상여부
				eaiDto.setDayOccurCnt(ExcelUtils.readValue(sheet, 26, "H"));// 일발생건수
				eaiDto.setCurrIntrfcIdentifier(ExcelUtils.readValue(sheet, 26, "L"));
				eaiDto.setReqWrapperDtoNm(ExcelUtils.readValue(sheet, 27, "E"));
				eaiDto.setResWrapperDtoNm(ExcelUtils.readValue(sheet, 27, "H"));

				if (fileVer.equals("v20180911") || fileVer.equals("")) {
					intrfccombsDto.setIntrfcsrsysdtDto(srSysDtoList);
					intrfccombsDto.setEaiDto(eaiDto);
					int mapGridSize = 32;

					Map<String, String> msgInsertMap = new HashMap<String, String>();

					for (int j = 0; j < 4; j++) {
						if (j == 0) {
							for (int i = 1; i < 6; i++) {
								if (ExcelUtils.readValue(sheet, i + mapGridSize, "C") != null
										&& !ExcelUtils.readValue(sheet, i + mapGridSize, "C").equals("")) {

									this.mappingSetting(interfaceId, workbook, sheet, i, startIndex,
											intrfcMsgLayoutDtoList, msgMap, "SEND_REQ_", "SEND", "REQUEST", "C", "E",
											mapGridSize, 8, msgInsertMap, msgResultMap);
								}
							}
						} else if (j == 1) {
							for (int i = 1; i < 6; i++) {
								if (ExcelUtils.readValue(sheet, i + mapGridSize, "G") != null
										&& !ExcelUtils.readValue(sheet, i + mapGridSize, "G").equals("")) {

									this.mappingSetting(interfaceId, workbook, sheet, i, startIndex,
											intrfcMsgLayoutDtoList, msgMap, "SEND_RES_", "SEND", "RESPONSE", "G", "E",
											mapGridSize, 8, msgInsertMap, msgResultMap);
								}
							}
						} else if (j == 2) {
							for (int i = 1; i < 6; i++) {
								if (ExcelUtils.readValue(sheet, i + mapGridSize, "I") != null
										&& !ExcelUtils.readValue(sheet, i + mapGridSize, "I").equals("")) {

									this.mappingSetting(interfaceId, workbook, sheet, i, startIndex,
											intrfcMsgLayoutDtoList, msgMap, "RECV_REQ_", "RECEIVE", "REQUEST", "I", "L",
											mapGridSize, 8, msgInsertMap, msgResultMap);
								}
							}
						} else if (j == 3) {
							for (int i = 1; i < 6; i++) {
								if (ExcelUtils.readValue(sheet, i + mapGridSize, "M") != null
										&& !ExcelUtils.readValue(sheet, i + mapGridSize, "M").equals("")) {

									this.mappingSetting(interfaceId, workbook, sheet, i, startIndex,
											intrfcMsgLayoutDtoList, msgMap, "RECV_RES_", "RECEIVE", "RESPONSE", "M",
											"L", mapGridSize, 8, msgInsertMap, msgResultMap);
								}
							}
						}
					}
				} else if (fileVer.equals("v20180927")) {
					String recordSep = getCodeValueNm("RECORD_SEPARATOR_CD", ExcelUtils.readValue(sheet, 27, "L"),
							"ko");
					if (recordSep == null || recordSep.equals("")) {
						recordSep = "LF";
					}
					eaiDto.setRecordSeparator(recordSep);
					eaiDto.setRecordSize(ExcelUtils.readValue(sheet, 28, "E"));

					eaiDto.setSendTranBeforeScript(ExcelUtils.readValue(sheet, 18, "H"));
					eaiDto.setSendTranBeforeScriptType(
							getCodeValueNm("TRAN_BEFORE_SCRIPT_TYPE", ExcelUtils.readValue(sheet, 18, "L"), "ko"));
					eaiDto.setRecvTranPostScriptType(
							getCodeValueNm("TRAN_POST_SCRIPT_TYPE", ExcelUtils.readValue(sheet, 22, "H"), "ko"));

					intrfccombsDto.setIntrfcsrsysdtDto(srSysDtoList);
					intrfccombsDto.setEaiDto(eaiDto);
					int mapGridSize = 33;

					Map<String, String> msgInsertMap = new HashMap<String, String>();

					for (int j = 0; j < 4; j++) {
						if (j == 0) {
							for (int i = 1; i < 6; i++) {
								if (ExcelUtils.readValue(sheet, i + mapGridSize, "C") != null
										&& !ExcelUtils.readValue(sheet, i + mapGridSize, "C").equals("")) {

									this.mappingSetting(interfaceId, workbook, sheet, i, startIndex,
											intrfcMsgLayoutDtoList, msgMap, "SEND_REQ_", "SEND", "REQUEST", "C", "E",
											mapGridSize, 8, msgInsertMap, msgResultMap);
								}
							}
						} else if (j == 1) {
							for (int i = 1; i < 6; i++) {
								if (ExcelUtils.readValue(sheet, i + mapGridSize, "G") != null
										&& !ExcelUtils.readValue(sheet, i + mapGridSize, "G").equals("")) {

									this.mappingSetting(interfaceId, workbook, sheet, i, startIndex,
											intrfcMsgLayoutDtoList, msgMap, "SEND_RES_", "SEND", "RESPONSE", "G", "E",
											mapGridSize, 8, msgInsertMap, msgResultMap);
								}
							}
						} else if (j == 2) {
							for (int i = 1; i < 6; i++) {
								if (ExcelUtils.readValue(sheet, i + mapGridSize, "I") != null
										&& !ExcelUtils.readValue(sheet, i + mapGridSize, "I").equals("")) {

									this.mappingSetting(interfaceId, workbook, sheet, i, startIndex,
											intrfcMsgLayoutDtoList, msgMap, "RECV_REQ_", "RECEIVE", "REQUEST", "I", "L",
											mapGridSize, 8, msgInsertMap, msgResultMap);
								}
							}
						} else if (j == 3) {
							for (int i = 1; i < 6; i++) {
								if (ExcelUtils.readValue(sheet, i + mapGridSize, "M") != null
										&& !ExcelUtils.readValue(sheet, i + mapGridSize, "M").equals("")) {

									this.mappingSetting(interfaceId, workbook, sheet, i, startIndex,
											intrfcMsgLayoutDtoList, msgMap, "RECV_RES_", "RECEIVE", "RESPONSE", "M",
											"L", mapGridSize, 8, msgInsertMap, msgResultMap);
								}
							}
						}
					}
				}

				intrfccombsDto.setIntrfcmsglayoutdtDto(intrfcMsgLayoutDtoList);

			}
		} else if (intrfcTypeCd.equals("EAI_E")) {
			String fileVer = ExcelUtils.readValue(sheet, 1, "S");
			String title = ExcelUtils.readValue(sheet, 2, "B");
			int titleYn = title.indexOf("대외파일전송");

			if (titleYn < 0) {
				throw new ServiceException(BxMessages.Error.FILE_UPLOAD_INTERFACE_TYPE);
			}

			String intrfcWayCd = getCodeValueNm("INTRFC_WAY_CD", ExcelUtils.readValue(sheet, 7, "E"), "ko");

			StringBuilder errMsg = new StringBuilder();
			boolean isValid = true;

			String appCdL1 = ExcelUtils.readValue(sheet, 12, "E");
			String appCdL2 = ExcelUtils.readValue(sheet, 12, "F");
			String appCdL3 = ExcelUtils.readValue(sheet, 12, "G");
			sendSyscd = ExcelUtils.readValue(sheet, 8, "E");
			recvSysCd = ExcelUtils.readValue(sheet, 8, "L");
			String trxDscd = getCodeValueNm("TRAN_DSCD", ExcelUtils.readValue(sheet, 6, "L"), "ko");

			isValid = intrfcValidation(isValid, errMsg, appCdL1, appCdL2, appCdL3, sendSyscd, recvSysCd, trxDscd);

			if (!isValid) {
				errMsg.append("\n시트명: " + sheet.getSheetName());
				throw new ServiceException(BxMessages.Error.INTRFC_VALID_CHECK, errMsg.toString());
			}

			IntrfcIdCreateDto interfaceIdDto = new IntrfcIdCreateDto();
			interfaceIdDto.setIntrfcTypeCd("EAI_E");
			interfaceIdDto.setLv1Cd(ExcelUtils.readValue(sheet, 12, "E"));
			interfaceIdDto.setLv2Cd(ExcelUtils.readValue(sheet, 12, "F"));
			interfaceIdDto.setLv3Cd(ExcelUtils.readValue(sheet, 12, "G"));
			interfaceIdDto.setSendSysCd(ExcelUtils.readValue(sheet, 8, "E"));
			interfaceIdDto.setReceiveSysCd(ExcelUtils.readValue(sheet, 8, "L"));
			interfaceIdDto.setTrxDscd(trxDscd);
			String interfaceId = this.getInterfaceId(interfaceIdDto).getIntrfcId();

			String intrfcIdDoc = ExcelUtils.readValue(sheet, 5, "E");
			insertYn = true;

			if (intrfcIdDoc != null && !intrfcIdDoc.equals("")) {
				interfaceId = intrfcIdDoc;
				insertYn = false;
			}

			intrfccombsDto.setIntrfcId(interfaceId);
			intrfccombsDto.setIntrfcNm(ExcelUtils.readValue(sheet, 5, "L"));
			intrfccombsDto.setWorkStatusCd("WORKING");
			intrfccombsDto.setSrTypeCd(getCodeValueNm("SENC_RECV_DSCD", ExcelUtils.readValue(sheet, 6, "E"), "ko"));
			intrfccombsDto.setIntrfcWayCd(intrfcWayCd);
			intrfccombsDto.setIntrfcTypeCd(intrfcTypeCd);
			intrfccombsDto.setRegManId(ExcelUtils.readValue(sheet, 3, "L"));
			intrfccombsDto.setRegDttm(DateUtils.getCurrentDate(9));
			intrfccombsDto.setTrxDscd(trxDscd);
			intrfccombsDto.setLv1Cd(ExcelUtils.readValue(sheet, 12, "E"));
			intrfccombsDto.setLv2Cd(ExcelUtils.readValue(sheet, 12, "F"));
			intrfccombsDto.setLv3Cd(ExcelUtils.readValue(sheet, 12, "G"));

			List<IntrfcsrsysdtDto> srSysDtoList = new ArrayList<IntrfcsrsysdtDto>();
			IntrfcsrsysdtDto srSysDto = new IntrfcsrsysdtDto();
			IntrfccombsDetailEAIDto eaiDto = new IntrfccombsDetailEAIDto();
			Map<String, String> msgMap = new HashMap<String, String>();
			List<IntrfcmsglayoutdtDto> intrfcMsgLayoutDtoList = new ArrayList<IntrfcmsglayoutdtDto>();
			int startIndex = 15;

			srSysDto.setSysCd(ExcelUtils.readValue(sheet, 8, "E"));
			srSysDto.setSysNm(ExcelUtils.readValue(sheet, 9, "E"));
			srSysDto.setCrgManNm(ExcelUtils.readValue(sheet, 10, "E"));
			srSysDto.setIntrfcId(interfaceId);
			srSysDto.setSrSeq(1);
			srSysDto.setSrTypeCd("SEND");
			srSysDtoList.add(srSysDto);

			srSysDto = new IntrfcsrsysdtDto();
			srSysDto.setSysCd(ExcelUtils.readValue(sheet, 8, "L"));
			srSysDto.setSysNm(ExcelUtils.readValue(sheet, 9, "L"));
			srSysDto.setCrgManNm(ExcelUtils.readValue(sheet, 10, "L"));
			srSysDto.setIntrfcId(interfaceId);
			srSysDto.setSrSeq(1);
			srSysDto.setSrTypeCd("RECEIVE");
			srSysDtoList.add(srSysDto);

			// eaiDto.setIntrfcUse(ExcelUtils.readValue(sheet, 13, "E"));
			eaiDto.setIntrfDesc(ExcelUtils.readValue(sheet, 14, "E"));

			String instCd = ExcelUtils.readValue(sheet, 17, "E");
			ExtrnlinstcdDto extrnlinstcdDto = extrnlinstcdDao.selectExtrnlinstcd(instCd, "INST");

			if (instCd != null && !instCd.equals("")) {
				if (extrnlinstcdDto == null) {
					errMsg.append("\n존재하지않는 기관코드입니다. ");
					isValid = false;
				}
			}

			if (!isValid) {
				errMsg.append("\n시트명: " + sheet.getSheetName());
				throw new ServiceException(BxMessages.Error.INTRFC_VALID_CHECK, errMsg.toString());
			}

			// 대외상세정보
			intrfccombsDto.setInstCd(instCd);// 기관코드
			eaiDto.setFepTranIntrfcId(ExcelUtils.readValue(sheet, 17, "H"));// 일괄전송인터페이스ID

			// 송신
			eaiDto.setSendFilePath(ExcelUtils.readValue(sheet, 20, "E"));// 파일경로
			eaiDto.setSendFileNm(ExcelUtils.readValue(sheet, 20, "H"));// 파일명
			eaiDto.setSendTranPostProc(getCodeValueNm("TRAN_POST_PROC", ExcelUtils.readValue(sheet, 20, "L"), "ko"));// 전송후처리유형
			eaiDto.setSendTranPostBackPath(ExcelUtils.readValue(sheet, 21, "E"));// 전송후백업경로

			// 수신
			eaiDto.setRecvFilePath(ExcelUtils.readValue(sheet, 24, "E"));// 파일경로
			eaiDto.setRecvFileNm(ExcelUtils.readValue(sheet, 24, "H"));// 파일명
			eaiDto.setRecvTranPostFinCreateYn(ExcelUtils.readValue(sheet, 24, "L"));// 전송후핀파일생성여부
			eaiDto.setRecvTranPostScript(ExcelUtils.readValue(sheet, 25, "E"));// 전송후후행스크립트

			eaiDto.setOccurCycle(getCodeValueNm("GEN_CYCLE_CD", ExcelUtils.readValue(sheet, 28, "E"), "ko"));// 발생주기
			eaiDto.setPrivacyInclYn(ExcelUtils.readValue(sheet, 28, "H"));// 개인정보포함여부
			eaiDto.setBackupAprvStat(getCodeValueNm("BACKUP_CD", ExcelUtils.readValue(sheet, 28, "L"), "ko"));// 백업승인상태확인
			eaiDto.setEncTargetYn(ExcelUtils.readValue(sheet, 29, "E"));// 암호화대상여부
			eaiDto.setDayOccurCnt(ExcelUtils.readValue(sheet, 29, "H"));// 일발생건수

			eaiDto.setCurrIntrfcIdentifier(ExcelUtils.readValue(sheet, 29, "L"));
			eaiDto.setReqWrapperDtoNm(ExcelUtils.readValue(sheet, 30, "E"));
			eaiDto.setResWrapperDtoNm(ExcelUtils.readValue(sheet, 30, "H"));

			if (fileVer.equals("v20180911") || fileVer.equals("")) {
				intrfccombsDto.setIntrfcsrsysdtDto(srSysDtoList);
				intrfccombsDto.setEaiDto(eaiDto);
				int mapGridSize = 35;

				Map<String, String> msgInsertMap = new HashMap<String, String>();

				for (int j = 0; j < 4; j++) {
					if (j == 0) {
						for (int i = 1; i < 6; i++) {
							if (ExcelUtils.readValue(sheet, i + mapGridSize, "C") != null
									&& !ExcelUtils.readValue(sheet, i + mapGridSize, "C").equals("")) {

								this.mappingSetting(interfaceId, workbook, sheet, i, startIndex, intrfcMsgLayoutDtoList,
										msgMap, "SEND_REQ_", "SEND", "REQUEST", "C", "E", mapGridSize, 8, msgInsertMap,
										msgResultMap);
							}
						}
					} else if (j == 1) {
						for (int i = 1; i < 6; i++) {
							if (ExcelUtils.readValue(sheet, i + mapGridSize, "G") != null
									&& !ExcelUtils.readValue(sheet, i + mapGridSize, "G").equals("")) {

								this.mappingSetting(interfaceId, workbook, sheet, i, startIndex, intrfcMsgLayoutDtoList,
										msgMap, "SEND_RES_", "SEND", "RESPONSE", "G", "E", mapGridSize, 8, msgInsertMap,
										msgResultMap);
							}
						}
					} else if (j == 2) {
						for (int i = 1; i < 6; i++) {
							if (ExcelUtils.readValue(sheet, i + mapGridSize, "I") != null
									&& !ExcelUtils.readValue(sheet, i + mapGridSize, "I").equals("")) {

								this.mappingSetting(interfaceId, workbook, sheet, i, startIndex, intrfcMsgLayoutDtoList,
										msgMap, "RECV_REQ_", "RECEIVE", "REQUEST", "I", "L", mapGridSize, 8,
										msgInsertMap, msgResultMap);
							}
						}
					} else if (j == 3) {
						for (int i = 1; i < 6; i++) {
							if (ExcelUtils.readValue(sheet, i + mapGridSize, "M") != null
									&& !ExcelUtils.readValue(sheet, i + mapGridSize, "M").equals("")) {

								this.mappingSetting(interfaceId, workbook, sheet, i, startIndex, intrfcMsgLayoutDtoList,
										msgMap, "RECV_RES_", "RECEIVE", "RESPONSE", "M", "L", mapGridSize, 8,
										msgInsertMap, msgResultMap);
							}
						}
					}
				}
			} else if (fileVer.equals("v20180927")) {
				String recordSep = getCodeValueNm("RECORD_SEPARATOR_CD", ExcelUtils.readValue(sheet, 30, "L"), "ko");
				if (recordSep == null || recordSep.equals("")) {
					recordSep = "LF";
				}
				eaiDto.setRecordSeparator(recordSep);
				eaiDto.setRecordSize(ExcelUtils.readValue(sheet, 31, "E"));

				eaiDto.setSendTranBeforeScript(ExcelUtils.readValue(sheet, 21, "H"));
				eaiDto.setSendTranBeforeScriptType(
						getCodeValueNm("TRAN_BEFORE_SCRIPT_TYPE", ExcelUtils.readValue(sheet, 21, "L"), "ko"));
				eaiDto.setRecvTranPostScriptType(
						getCodeValueNm("TRAN_POST_SCRIPT_TYPE", ExcelUtils.readValue(sheet, 25, "H"), "ko"));

				intrfccombsDto.setIntrfcsrsysdtDto(srSysDtoList);
				intrfccombsDto.setEaiDto(eaiDto);
				int mapGridSize = 36;

				Map<String, String> msgInsertMap = new HashMap<String, String>();

				for (int j = 0; j < 4; j++) {
					if (j == 0) {
						for (int i = 1; i < 6; i++) {
							if (ExcelUtils.readValue(sheet, i + mapGridSize, "C") != null
									&& !ExcelUtils.readValue(sheet, i + mapGridSize, "C").equals("")) {

								this.mappingSetting(interfaceId, workbook, sheet, i, startIndex, intrfcMsgLayoutDtoList,
										msgMap, "SEND_REQ_", "SEND", "REQUEST", "C", "E", mapGridSize, 8, msgInsertMap,
										msgResultMap);
							}
						}
					} else if (j == 1) {
						for (int i = 1; i < 6; i++) {
							if (ExcelUtils.readValue(sheet, i + mapGridSize, "G") != null
									&& !ExcelUtils.readValue(sheet, i + mapGridSize, "G").equals("")) {

								this.mappingSetting(interfaceId, workbook, sheet, i, startIndex, intrfcMsgLayoutDtoList,
										msgMap, "SEND_RES_", "SEND", "RESPONSE", "G", "E", mapGridSize, 8, msgInsertMap,
										msgResultMap);
							}
						}
					} else if (j == 2) {
						for (int i = 1; i < 6; i++) {
							if (ExcelUtils.readValue(sheet, i + mapGridSize, "I") != null
									&& !ExcelUtils.readValue(sheet, i + mapGridSize, "I").equals("")) {

								this.mappingSetting(interfaceId, workbook, sheet, i, startIndex, intrfcMsgLayoutDtoList,
										msgMap, "RECV_REQ_", "RECEIVE", "REQUEST", "I", "L", mapGridSize, 8,
										msgInsertMap, msgResultMap);
							}
						}
					} else if (j == 3) {
						for (int i = 1; i < 6; i++) {
							if (ExcelUtils.readValue(sheet, i + mapGridSize, "M") != null
									&& !ExcelUtils.readValue(sheet, i + mapGridSize, "M").equals("")) {

								this.mappingSetting(interfaceId, workbook, sheet, i, startIndex, intrfcMsgLayoutDtoList,
										msgMap, "RECV_RES_", "RECEIVE", "RESPONSE", "M", "L", mapGridSize, 8,
										msgInsertMap, msgResultMap);
							}
						}
					}
				}
			}
			intrfccombsDto.setIntrfcmsglayoutdtDto(intrfcMsgLayoutDtoList);

		}

		// for (String key : msgResultMap.keySet()) {
		// MsgInsertDto msginsertDto = msgResultMap.get(key);
		// boolean insertYnmsg = msginsertDto.isInsertYn();
		// MsglayoutbsDto msgDto = msginsertDto.getMsglayoutbsDto();
		//
		// if (insertYnmsg) {
		// msglayoutService.add(msgDto, "FILE");
		// } else {
		// msglayoutService.update(msgDto);
		// }
		// }

		if (insertYn) {

			List<String> deployCodeList = new ArrayList<String>();
			String deploySysCd = "EAI";
			String deploySysCdSim = "SIM";
			String deploySysCdRush = "RSH";
			String deploySysCdE2E = "E2E";

			deployCodeList.add(deploySysCd);
			deployCodeList.add(deploySysCdSim);
			deployCodeList.add(deploySysCdRush);
			deployCodeList.add(deploySysCdE2E);

			if (deployCodeMap.containsKey(sendSyscd)) {
				deployCodeList.add(sendSyscd);
			}
			if (deployCodeMap.containsKey(recvSysCd)) {
				deployCodeList.add(recvSysCd);
			}

			IntrfcdeploysysdtDto intrfcDeployDto;
			int deploySeq = 1;
			List<IntrfcdeploysysdtDto> intrfcdeploysysdtDto = new ArrayList<IntrfcdeploysysdtDto>();

			for (String code : deployCodeList) {
				DepolysysbsDto dto = deployDao.selectDepolysysbs(code);
				if (dto != null) {
					intrfcDeployDto = new IntrfcdeploysysdtDto();

					intrfcDeployDto.setIntrfcId(intrfccombsDto.getIntrfcId());
					intrfcDeployDto.setDeploySysCd(dto.getDeploySysCd());
					intrfcDeployDto.setDeploySysNm(dto.getDeploySysNm());
					intrfcDeployDto.setDeploySysSeq(deploySeq);
					intrfcDeployDto.setDeployUrl(dto.getDeploySysUrl());
					intrfcdeploysysdtDto.add(intrfcDeployDto);

					deploySeq++;
				}
			}

			if (intrfcdeploysysdtDto.size() > 0) {
				intrfccombsDto.setIntrfcdeploysysdtDto(intrfcdeploysysdtDto);
			}

			this.add(intrfccombsDto, false);
		} else {
			IntrfccombsDto dto = intrfccombsDao.selectIntrfccombs(intrfccombsDto.getIntrfcId());

			IntrfccombsRawDataDto rawDataDto = null;
			String jsonRawData = dto.getRawData();
			if (jsonRawData != null && !jsonRawData.equals("")) {
				try {
					rawDataDto = JsonUtils.jsonToObect(jsonRawData, IntrfccombsRawDataDto.class);
				} catch (JsonParseException e) {
					logger.error("{}", e);
					throw new ServiceException(BxMessages.Error.SYSTEM_EXCEPTION);
				} catch (JsonMappingException e) {
					logger.error("{}", e);
					throw new ServiceException(BxMessages.Error.SYSTEM_EXCEPTION);
				} catch (IOException e) {
					logger.error("{}", e);
					throw new ServiceException(BxMessages.Error.SYSTEM_EXCEPTION);
				}
			}

			if (rawDataDto.getIntrfcMsgFieldEncodingDto() != null) {
				intrfccombsDto.setIntrfcMsgFieldEncodingDto(rawDataDto.getIntrfcMsgFieldEncodingDto());
			}

			this.update(intrfccombsDto, false);
		}
	}

	private boolean intrfcValidation(boolean isValid, StringBuilder errMsg, String appCdL1, String appCdL2,
			String appCdL3, String sendSyscd, String recvSysCd, String trxDscd) {

		isValid = nullCheck(errMsg, appCdL1, "\nappCdL1", isValid);
		isValid = nullCheck(errMsg, appCdL2, "\nappCdL2", isValid);
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

		isValid = nullCheck(errMsg, trxDscd, "\n거래유형코드가", isValid);

		if (isValid) {
			if (!trxDscd.equals("ONLINE") && !trxDscd.equals("BATCH")) {
				errMsg.append("\n거래유형코드는 온라인, 배치 중 하나만 선택되어야 합니다.");
				isValid = false;
			}

		}

		return isValid;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	protected void readCcData(Sheet sheet, IntrfccombsDto intrfccombsDto, Workbook workbook) {

		StringBuilder errMsg = new StringBuilder();
		boolean isValid = true;
		Map<String, MsgInsertDto> msgResultMap = new HashMap<String, MsgInsertDto>();

		IntrfcIdCreateDto interfaceIdDto = new IntrfcIdCreateDto();

		String appCdL1 = ExcelUtils.readValue(sheet, 11, "E");
		String appCdL2 = ExcelUtils.readValue(sheet, 11, "G");
		String appCdL3 = ExcelUtils.readValue(sheet, 11, "H");
		String sendSyscd = ExcelUtils.readValue(sheet, 7, "E");
		String recvSysCd = ExcelUtils.readValue(sheet, 7, "M");
		String trxDscd = getCodeValueNm("TRAN_DSCD", ExcelUtils.readValue(sheet, 6, "E"), "ko");

		isValid = intrfcValidation(isValid, errMsg, appCdL1, appCdL2, appCdL3, sendSyscd, recvSysCd, trxDscd);

		if (!isValid) {
			errMsg.append("\n시트명: " + sheet.getSheetName());
			throw new ServiceException(BxMessages.Error.INTRFC_VALID_CHECK, errMsg.toString());
		}

		interfaceIdDto.setIntrfcTypeCd("CC");
		interfaceIdDto.setLv1Cd(appCdL1);
		interfaceIdDto.setLv2Cd(appCdL2);
		interfaceIdDto.setLv3Cd(appCdL3);
		interfaceIdDto.setSendSysCd(sendSyscd);
		interfaceIdDto.setReceiveSysCd(recvSysCd);
		interfaceIdDto.setTrxDscd(trxDscd);

		String interfaceId = this.getInterfaceId(interfaceIdDto).getIntrfcId();

		String intrfcIdDoc = ExcelUtils.readValue(sheet, 5, "E");
		boolean insertYn = true;

		if (intrfcIdDoc != null && !intrfcIdDoc.equals("")) {
			interfaceId = intrfcIdDoc;
			insertYn = false;
		}

		intrfccombsDto.setIntrfcId(interfaceId);
		intrfccombsDto.setIntrfcNm(ExcelUtils.readValue(sheet, 5, "M"));
		intrfccombsDto.setWorkStatusCd("WORKING");
		intrfccombsDto.setRegManId(ExcelUtils.readValue(sheet, 3, "M"));
		intrfccombsDto.setRegDttm(DateUtils.getCurrentDate(9));
		intrfccombsDto.setTrxDscd(trxDscd);
		intrfccombsDto.setIntrfcTypeCd("CC");
		intrfccombsDto.setLv1Cd(ExcelUtils.readValue(sheet, 11, "E"));
		intrfccombsDto.setLv2Cd(ExcelUtils.readValue(sheet, 11, "G"));
		intrfccombsDto.setLv3Cd(ExcelUtils.readValue(sheet, 11, "H"));
		intrfccombsDto.setTrxTypeDscd(getCodeValueNm("TRX_TYPE_DSCD_CC", ExcelUtils.readValue(sheet, 6, "M"), "ko"));// 거래유형
		List<IntrfcsrsysdtDto> srSysDtoList = new ArrayList<IntrfcsrsysdtDto>();
		IntrfcsrsysdtDto srSysDto = new IntrfcsrsysdtDto();
		srSysDto.setSysCd(ExcelUtils.readValue(sheet, 7, "E"));
		srSysDto.setSysNm(ExcelUtils.readValue(sheet, 8, "E"));
		srSysDto.setCrgManNm(ExcelUtils.readValue(sheet, 9, "E"));
		srSysDto.setIntrfcId(interfaceId);
		srSysDto.setSrSeq(1);
		srSysDto.setSrTypeCd("SEND");
		srSysDtoList.add(srSysDto);

		srSysDto = new IntrfcsrsysdtDto();
		srSysDto.setSysCd(ExcelUtils.readValue(sheet, 7, "M"));
		srSysDto.setSysNm(ExcelUtils.readValue(sheet, 8, "M"));
		srSysDto.setCrgManNm(ExcelUtils.readValue(sheet, 9, "M"));
		srSysDto.setIntrfcId(interfaceId);
		srSysDto.setSrSeq(1);
		srSysDto.setSrTypeCd("RECEIVE");

		String trxCd = ExcelUtils.readValue(sheet, 10, "M");
		if (!trxCd.equals("") && trxCd != null) {
			int i = trxcdDao.selectAllCnt(trxCd, null, null, null);
			if (i == 0) {
				errMsg.append("\n존재하지 않는 거래코드입니다.");
				isValid = false;
			}
		}

		srSysDto.setTrxCd(trxCd);
		srSysDtoList.add(srSysDto);

		if (!isValid) {
			errMsg.append("\n시트명: " + sheet.getSheetName());
			throw new ServiceException(BxMessages.Error.INTRFC_VALID_CHECK, errMsg.toString());
		}

		intrfccombsDto.setIntrfcsrsysdtDto(srSysDtoList);

		IntrfccombsDetailCCDto ccDto = new IntrfccombsDetailCCDto();

		ccDto.setIntrfDesc(ExcelUtils.readValue(sheet, 13, "E"));
		intrfccombsDto.setCcDto(ccDto);

		if (!isValid) {
			errMsg.append("\n시트명: " + sheet.getSheetName());
			throw new ServiceException(BxMessages.Error.INTRFC_VALID_CHECK, errMsg.toString());
		}

		Map<String, String> msgMap = new HashMap<String, String>();

		List<IntrfcmsglayoutdtDto> intrfcMsgLayoutDtoList = new ArrayList<IntrfcmsglayoutdtDto>();
		int startIndex = 15;
		int mapGridSize = 17;
		Map<String, String> msgInsertMap = new HashMap<String, String>();

		for (int j = 0; j < 2; j++) {
			if (j == 0) {
				for (int i = 1; i < 4; i++) {
					if (ExcelUtils.readValue(sheet, i + mapGridSize, "C") != null
							&& !ExcelUtils.readValue(sheet, i + mapGridSize, "C").equals("")) {

						this.mappingSetting(interfaceId, workbook, sheet, i, startIndex, intrfcMsgLayoutDtoList, msgMap,
								"SEND_REQ_", "SEND", "REQUEST", "C", "E", mapGridSize, 7, msgInsertMap, msgResultMap);
					}
				}
			} else if (j == 1) {
				for (int i = 1; i < 4; i++) {
					if (ExcelUtils.readValue(sheet, i + mapGridSize, "J") != null
							&& !ExcelUtils.readValue(sheet, i + mapGridSize, "J").equals("")) {

						this.mappingSetting(interfaceId, workbook, sheet, i, startIndex, intrfcMsgLayoutDtoList, msgMap,
								"SEND_RES_", "SEND", "RESPONSE", "J", "E", mapGridSize, 7, msgInsertMap, msgResultMap);
					}
				}
			}
		}

		intrfccombsDto.setIntrfcmsglayoutdtDto(intrfcMsgLayoutDtoList);

		intrfccombsDto.setIntrfccombsMappingReqDto(null);
		intrfccombsDto.setIntrfccombsMappingResDto(null);

		// for (String key : msgResultMap.keySet()) {
		//
		// logger.debug("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
		// logger.debug(key);
		// logger.debug("{}", msgResultMap.size());
		// logger.debug("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
		//
		// MsgInsertDto msginsertDto = msgResultMap.get(key);
		// boolean insertYnmsg = msginsertDto.isInsertYn();
		// MsglayoutbsDto msgDto = msginsertDto.getMsglayoutbsDto();
		//
		// if (insertYnmsg) {
		// msglayoutService.add(msgDto, "FILE");
		// } else {
		// msglayoutService.update(msgDto);
		// }
		// }

		if (insertYn) {
			// TODO
			List<String> deployCodeList = new ArrayList<String>();
			String deploySysCdSim = "SIM";
			String deploySysCdRush = "RSH";
			String deploySysCdE2E = "E2E";

			deployCodeList.add(deploySysCdSim);
			deployCodeList.add(deploySysCdRush);
			deployCodeList.add(deploySysCdE2E);

			if (deployCodeMap.containsKey(sendSyscd)) {
				deployCodeList.add(sendSyscd);
			}
			if (deployCodeMap.containsKey(recvSysCd)) {
				if (!sendSyscd.equals(recvSysCd)) {
					deployCodeList.add(recvSysCd);
				}
			}

			IntrfcdeploysysdtDto intrfcDeployDto;
			int deploySeq = 1;
			List<IntrfcdeploysysdtDto> intrfcdeploysysdtDto = new ArrayList<IntrfcdeploysysdtDto>();

			for (String code : deployCodeList) {
				DepolysysbsDto dto = deployDao.selectDepolysysbs(code);
				if (dto != null) {
					intrfcDeployDto = new IntrfcdeploysysdtDto();

					intrfcDeployDto.setIntrfcId(intrfccombsDto.getIntrfcId());
					intrfcDeployDto.setDeploySysCd(dto.getDeploySysCd());
					intrfcDeployDto.setDeploySysNm(dto.getDeploySysNm());
					intrfcDeployDto.setDeploySysSeq(deploySeq);
					intrfcDeployDto.setDeployUrl(dto.getDeploySysUrl());
					intrfcdeploysysdtDto.add(intrfcDeployDto);

					deploySeq++;
				}
			}

			if (intrfcdeploysysdtDto.size() > 0) {
				intrfccombsDto.setIntrfcdeploysysdtDto(intrfcdeploysysdtDto);
			}

			this.add(intrfccombsDto, false);
		} else {
			this.update(intrfccombsDto, false);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	protected void readMciData(Sheet sheet, IntrfccombsDto intrfccombsDto, Workbook workbook) {

		StringBuilder errMsg = new StringBuilder();
		boolean isValid = true;
		Map<String, MsgInsertDto> msgResultMap = new HashMap<String, MsgInsertDto>();
		Map<String, MsgInsertDto> msgResultMap2 = new HashMap<String, MsgInsertDto>();

		IntrfcIdCreateDto interfaceIdDto = new IntrfcIdCreateDto();

		String appCdL1 = ExcelUtils.readValue(sheet, 13, "E");
		String appCdL2 = ExcelUtils.readValue(sheet, 13, "G");
		String appCdL3 = ExcelUtils.readValue(sheet, 13, "H");
		String sendSyscd = ExcelUtils.readValue(sheet, 8, "E");
		String recvSysCd = ExcelUtils.readValue(sheet, 8, "M");

		isValid = intrfcValidation(isValid, errMsg, appCdL1, appCdL2, appCdL3, sendSyscd, recvSysCd, "ONLINE");

		if (!isValid) {
			errMsg.append("\n시트명: " + sheet.getSheetName());
			throw new ServiceException(BxMessages.Error.INTRFC_VALID_CHECK, errMsg.toString());
		}

		interfaceIdDto.setIntrfcTypeCd("MCI");
		interfaceIdDto.setLv1Cd(appCdL1);
		interfaceIdDto.setLv2Cd(appCdL2);
		interfaceIdDto.setLv3Cd(appCdL3);
		interfaceIdDto.setSendSysCd(sendSyscd);
		interfaceIdDto.setReceiveSysCd(recvSysCd);
		interfaceIdDto.setTrxDscd("ONLINE");

		String interfaceId = this.getInterfaceId(interfaceIdDto).getIntrfcId();

		String intrfcIdDoc = ExcelUtils.readValue(sheet, 5, "E");
		boolean insertYn = true;

		if (intrfcIdDoc != null && !intrfcIdDoc.equals("")) {
			interfaceId = intrfcIdDoc;
			insertYn = false;
		}

		intrfccombsDto.setIntrfcId(interfaceId);
		intrfccombsDto.setIntrfcNm(ExcelUtils.readValue(sheet, 5, "M"));
		intrfccombsDto.setWorkStatusCd("WORKING");
		intrfccombsDto.setRegManId(ExcelUtils.readValue(sheet, 3, "M"));
		intrfccombsDto.setRegDttm(DateUtils.getCurrentDate(9));
		intrfccombsDto.setMsgTrnsfrmYn(ExcelUtils.readValue(sheet, 6, "E"));
		intrfccombsDto.setTrxDscd("ONLINE");
		intrfccombsDto.setIntrfcTypeCd("MCI");
		intrfccombsDto.setLv1Cd(ExcelUtils.readValue(sheet, 13, "E"));
		intrfccombsDto.setLv2Cd(ExcelUtils.readValue(sheet, 13, "G"));
		intrfccombsDto.setLv3Cd(ExcelUtils.readValue(sheet, 13, "H"));
		intrfccombsDto.setTrxTypeDscd(getCodeValueNm("TRX_TYPE_DSCD", ExcelUtils.readValue(sheet, 7, "M"), "ko"));// 거래유형
		intrfccombsDto.setSyncAsyncDscd(getCodeValueNm("SYNC_DSCD", ExcelUtils.readValue(sheet, 6, "M"), "ko"));
		intrfccombsDto.setSrTypeCd(getCodeValueNm("SENC_RECV_DSCD", ExcelUtils.readValue(sheet, 6, "E"), "ko"));
		intrfccombsDto.setRspsYn(ExcelUtils.readValue(sheet, 11, "E"));
		List<IntrfcsrsysdtDto> srSysDtoList = new ArrayList<IntrfcsrsysdtDto>();
		IntrfcsrsysdtDto srSysDto = new IntrfcsrsysdtDto();
		srSysDto.setSysCd(ExcelUtils.readValue(sheet, 8, "E"));
		srSysDto.setSysNm(ExcelUtils.readValue(sheet, 9, "E"));
		srSysDto.setCrgManNm(ExcelUtils.readValue(sheet, 10, "E"));
		srSysDto.setIntrfcId(interfaceId);
		srSysDto.setSrSeq(1);
		srSysDto.setSrTypeCd("SEND");
		srSysDtoList.add(srSysDto);

		srSysDto = new IntrfcsrsysdtDto();
		srSysDto.setSysCd(ExcelUtils.readValue(sheet, 8, "M"));
		srSysDto.setSysNm(ExcelUtils.readValue(sheet, 9, "M"));
		srSysDto.setCrgManNm(ExcelUtils.readValue(sheet, 10, "M"));
		srSysDto.setIntrfcId(interfaceId);
		srSysDto.setSrSeq(1);
		srSysDto.setSrTypeCd("RECEIVE");

		String trxCd = ExcelUtils.readValue(sheet, 11, "M");
		if (!trxCd.equals("") && trxCd != null) {
			int i = trxcdDao.selectAllCnt(trxCd, null, null, null);
			if (i == 0) {
				errMsg.append("\n존재하지 않는 거래코드입니다.");
				isValid = false;
			}
		}

		srSysDto.setTrxCd(trxCd);
		srSysDtoList.add(srSysDto);

		if (!isValid) {
			errMsg.append("\n시트명: " + sheet.getSheetName());
			throw new ServiceException(BxMessages.Error.INTRFC_VALID_CHECK, errMsg.toString());
		}

		intrfccombsDto.setIntrfcsrsysdtDto(srSysDtoList);

		IntrfccombsDetailMCIDto mciDto = new IntrfccombsDetailMCIDto();

		// mciDto.setIntrfcUse(ExcelUtils.readValue(sheet, 14, "E"));
		mciDto.setIntrfDesc(ExcelUtils.readValue(sheet, 15, "E"));

		mciDto.setTimeOut(ExcelUtils.readValue(sheet, 18, "E"));// 타임아웃
		mciDto.setOccurCycle(getCodeValueNm("GEN_CYCLE_CD", ExcelUtils.readValue(sheet, 18, "I"), "ko"));// 발생주기
		mciDto.setDayOccurCnt(ExcelUtils.readValue(sheet, 18, "M"));// 일발생건수
		mciDto.setPrivacyInclYn(ExcelUtils.readValue(sheet, 19, "E"));// 개인정보포함여부
		mciDto.setCurrIntrfcIdentifier(ExcelUtils.readValue(sheet, 19, "I"));// 현행인터페이스식별자
		mciDto.setBackupAprvStat(getCodeValueNm("BACKUP_CD", ExcelUtils.readValue(sheet, 19, "M"), "ko"));// 백업승인상태확인
		mciDto.setEncTargetYn(ExcelUtils.readValue(sheet, 20, "E"));// 암호화대상여부

		intrfccombsDto.setMciDto(mciDto);

		if (!isValid) {
			errMsg.append("\n시트명: " + sheet.getSheetName());
			throw new ServiceException(BxMessages.Error.INTRFC_VALID_CHECK, errMsg.toString());
		}

		Map<String, String> msgMap = new HashMap<String, String>();

		List<IntrfcmsglayoutdtDto> intrfcMsgLayoutDtoList = new ArrayList<IntrfcmsglayoutdtDto>();
		int startIndex = 15;
		int mapGridSize = 25;
		Map<String, String> msgInsertMap = new HashMap<String, String>();

		for (int j = 0; j < 4; j++) {
			if (j == 0) {
				for (int i = 1; i < 4; i++) {
					if (ExcelUtils.readValue(sheet, i + mapGridSize, "C") != null
							&& !ExcelUtils.readValue(sheet, i + mapGridSize, "C").equals("")) {

						msgResultMap = this.mappingSetting(interfaceId, workbook, sheet, i, startIndex,
								intrfcMsgLayoutDtoList, msgMap, "SEND_REQ_", "SEND", "REQUEST", "C", "E", mapGridSize,
								8, msgInsertMap, msgResultMap);
					}
				}
			} else if (j == 1) {
				for (int i = 1; i < 4; i++) {
					if (ExcelUtils.readValue(sheet, i + mapGridSize, "G") != null
							&& !ExcelUtils.readValue(sheet, i + mapGridSize, "G").equals("")) {

						msgResultMap = this.mappingSetting(interfaceId, workbook, sheet, i, startIndex,
								intrfcMsgLayoutDtoList, msgMap, "SEND_RES_", "SEND", "RESPONSE", "G", "E", mapGridSize,
								8, msgInsertMap, msgResultMap);
					}
				}
			} else if (j == 2) {
				for (int i = 1; i < 4; i++) {
					if (ExcelUtils.readValue(sheet, i + mapGridSize, "J") != null
							&& !ExcelUtils.readValue(sheet, i + mapGridSize, "J").equals("")) {

						msgResultMap = this.mappingSetting(interfaceId, workbook, sheet, i, startIndex,
								intrfcMsgLayoutDtoList, msgMap, "RECV_REQ_", "RECEIVE", "REQUEST", "J", "M",
								mapGridSize, 8, msgInsertMap, msgResultMap);
					}
				}
			} else if (j == 3) {
				for (int i = 1; i < 4; i++) {
					if (ExcelUtils.readValue(sheet, i + mapGridSize, "N") != null
							&& !ExcelUtils.readValue(sheet, i + mapGridSize, "N").equals("")) {

						msgResultMap = this.mappingSetting(interfaceId, workbook, sheet, i, startIndex,
								intrfcMsgLayoutDtoList, msgMap, "RECV_RES_", "RECEIVE", "RESPONSE", "N", "M",
								mapGridSize, 8, msgInsertMap, msgResultMap);
					}
				}
			}
		}

		intrfccombsDto.setIntrfcmsglayoutdtDto(intrfcMsgLayoutDtoList);
		List<IntrfccombsMappingDto> intrfccombsMappingReqDto = new ArrayList<IntrfccombsMappingDto>();
		List<IntrfccombsMappingDto> intrfccombsMappingResDto = new ArrayList<IntrfccombsMappingDto>();

		if (intrfccombsDto.getMsgTrnsfrmYn().equals("Y")) {
			Sheet reqMappingSheet = workbook.getSheet(ExcelUtils.readValue(sheet, 33, "C"));
			Sheet resMappingSheet = workbook.getSheet(ExcelUtils.readValue(sheet, 33, "J"));

			intrfccombsMappingReqDto = this.readMapping(reqMappingSheet, msgMap, intrfccombsDto,
					intrfccombsMappingReqDto, "SEND_REQ_", "RECV_REQ_", "REQUEST");
			intrfccombsMappingResDto = this.readMapping(resMappingSheet, msgMap, intrfccombsDto,
					intrfccombsMappingResDto, "RECV_RES_", "SEND_RES_", "RESPONSE");
			// ExcelUtils.readValue(reqMappingSheet, 5, "V")
		}

		List<IntrfccombsMappingDto> intrfccombsMappingReqDtoResult = new ArrayList<IntrfccombsMappingDto>();
		List<IntrfccombsMappingDto> intrfccombsMappingResDtoResult = new ArrayList<IntrfccombsMappingDto>();
		for (IntrfccombsMappingDto dto : intrfccombsMappingReqDto) {
			if (dto.getReqResTypeCd().equals("REQUEST")) {
				intrfccombsMappingReqDtoResult.add(dto);
			}
		}
		for (IntrfccombsMappingDto dto : intrfccombsMappingResDto) {
			if (dto.getReqResTypeCd().equals("RESPONSE")) {
				intrfccombsMappingResDtoResult.add(dto);
			}
		}

		intrfccombsDto.setIntrfccombsMappingReqDto(intrfccombsMappingReqDtoResult);
		intrfccombsDto.setIntrfccombsMappingResDto(intrfccombsMappingResDtoResult);

		// intrfccombsDto.setEaiDto(eaiDto);
		// intrfccombsDto.setFepDto(fepDto);
		int i = 0;

		// for (String key : msgResultMap.keySet()) {
		// MsgInsertDto msginsertDto = msgResultMap.get(key);
		// boolean insertYnmsg = msginsertDto.isInsertYn();
		// MsglayoutbsDto msgDto = msginsertDto.getMsglayoutbsDto();
		//
		// if (insertYnmsg) {
		// if(i > 0) {
		// throw new ServiceException(BxMessages.Error.ALREADY_USE_MSG, "테스트중") ;
		// }
		// msglayoutService.add(msgDto, "FILE");
		// i ++ ;
		// } else {
		// msglayoutService.update(msgDto);
		// }
		// }

		if (insertYn) {
			List<String> deployCodeList = new ArrayList<String>();
			//String deploySysCdInternal = "MCI_I";
			String deploySysCdInternal = "MCA";
			String deploySysCdExternal = "MCI_E";
			String deploySysCdSim = "SIM";
			String deploySysCdRush = "RSH";
			String deploySysCdE2E = "E2E";

			deployCodeList.add(deploySysCdInternal);
			deployCodeList.add(deploySysCdExternal);
			deployCodeList.add(deploySysCdSim);
			deployCodeList.add(deploySysCdRush);
			deployCodeList.add(deploySysCdE2E);
 
			if (deployCodeMap.containsKey(sendSyscd)) {
				deployCodeList.add(sendSyscd);
			}
			if (deployCodeMap.containsKey(recvSysCd)) {
				deployCodeList.add(recvSysCd);
			}

			IntrfcdeploysysdtDto intrfcDeployDto;
			int deploySeq = 1;
			List<IntrfcdeploysysdtDto> intrfcdeploysysdtDto = new ArrayList<IntrfcdeploysysdtDto>();

			for (String code : deployCodeList) {
				DepolysysbsDto dto = deployDao.selectDepolysysbs(code);
				if (dto != null) {
					intrfcDeployDto = new IntrfcdeploysysdtDto();

					intrfcDeployDto.setIntrfcId(intrfccombsDto.getIntrfcId());
					intrfcDeployDto.setDeploySysCd(dto.getDeploySysCd());
					intrfcDeployDto.setDeploySysNm(dto.getDeploySysNm());
					intrfcDeployDto.setDeploySysSeq(deploySeq);
					intrfcDeployDto.setDeployUrl(dto.getDeploySysUrl());
					intrfcdeploysysdtDto.add(intrfcDeployDto);

					deploySeq++;
				}
			}

			if (intrfcdeploysysdtDto.size() > 0) {
				intrfccombsDto.setIntrfcdeploysysdtDto(intrfcdeploysysdtDto);
			}

			this.add(intrfccombsDto, false);
		} else {
			this.update(intrfccombsDto, false);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	protected void readMciDataDummy(Sheet sheet, IntrfccombsDto intrfccombsDto, Workbook workbook) {

		StringBuilder errMsg = new StringBuilder();
		boolean isValid = true;

		IntrfcIdCreateDto interfaceIdDto = new IntrfcIdCreateDto();
		int rowNumber = 5;
		while (true) {
			String endPoint = ExcelUtils.readValue(sheet, rowNumber, "A");
			String appCdL1 = ExcelUtils.readValue(sheet, rowNumber, "B");
			
			System.out.println("endPoint : " + endPoint + ", appCdL1 : "+ appCdL1);
			if (endPoint.equals("end")) {
				break;
			}
			
			if(appCdL1.equals("") || appCdL1 == null) {
				break;
			}
			
			boolean insertYn = true;
			String sendSyscd = ExcelUtils.readValue(sheet, rowNumber, "F");
			String recvSysCd = ExcelUtils.readValue(sheet, rowNumber, "I");

			if (!isValid) {
				errMsg.append("\n시트명: " + sheet.getSheetName());
				throw new ServiceException(BxMessages.Error.INTRFC_VALID_CHECK, errMsg.toString());
			}
			interfaceIdDto.setIntrfcTypeCd("MCI");
			interfaceIdDto.setLv1Cd(appCdL1);
			interfaceIdDto.setSendSysCd(sendSyscd);
			interfaceIdDto.setReceiveSysCd(recvSysCd);
			interfaceIdDto.setTrxDscd("ONLINE");
			String interfaceId = ExcelUtils.readValue(sheet, rowNumber, "D");
			intrfccombsDto.setIntrfcId(interfaceId);
			intrfccombsDto.setIntrfcNm(ExcelUtils.readValue(sheet, rowNumber, "E"));
			intrfccombsDto.setWorkStatusCd("WORKING");
			intrfccombsDto.setRegManId(ExcelUtils.readValue(sheet, rowNumber, "S"));
			intrfccombsDto.setRegDttm(DateUtils.getCurrentDate(9));
			intrfccombsDto.setMsgTrnsfrmYn(ExcelUtils.readValue(sheet, rowNumber, "P"));
			intrfccombsDto.setTrxDscd("ONLINE");
			intrfccombsDto.setIntrfcTypeCd("MCI");
			intrfccombsDto.setLv1Cd(appCdL1);
			intrfccombsDto.setSyncAsyncDscd(getCodeValueNm("SYNC_DSCD", ExcelUtils.readValue(sheet, rowNumber, "M"), "ko"));
			intrfccombsDto.setRspsYn(ExcelUtils.readValue(sheet, rowNumber, "N"));
			intrfccombsDto.setRegManId(ExcelUtils.readValue(sheet, rowNumber, "T"));
			
			List<IntrfcsrsysdtDto> srSysDtoList = new ArrayList<IntrfcsrsysdtDto>();
			IntrfcsrsysdtDto srSysDto = new IntrfcsrsysdtDto();
			srSysDto.setSysCd(ExcelUtils.readValue(sheet, rowNumber, "F"));
			srSysDto.setSysNm(ExcelUtils.readValue(sheet, rowNumber, "G"));
			srSysDto.setCrgManNm(ExcelUtils.readValue(sheet, rowNumber, "H"));
			srSysDto.setIntrfcId(interfaceId);
			srSysDto.setSrSeq(1);
			srSysDto.setSrTypeCd("SEND");
			srSysDtoList.add(srSysDto);
			srSysDto = new IntrfcsrsysdtDto();
			srSysDto.setSysCd(ExcelUtils.readValue(sheet, rowNumber, "I"));
			srSysDto.setSysNm(ExcelUtils.readValue(sheet, rowNumber, "J"));
			srSysDto.setCrgManNm(ExcelUtils.readValue(sheet, rowNumber, "K"));
			srSysDto.setIntrfcId(interfaceId);
			srSysDto.setSrSeq(1);
			srSysDto.setSrTypeCd("RECEIVE");

			String trxCd = ExcelUtils.readValue(sheet, rowNumber, "L");
			srSysDto.setTrxCd(trxCd);
			srSysDtoList.add(srSysDto);

			if (!isValid) {
				errMsg.append("\n시트명: " + sheet.getSheetName());
				throw new ServiceException(BxMessages.Error.INTRFC_VALID_CHECK, errMsg.toString());
			}

			intrfccombsDto.setIntrfcsrsysdtDto(srSysDtoList);

			IntrfccombsDetailMCIDto mciDto = new IntrfccombsDetailMCIDto();

			mciDto.setIntrfDesc(ExcelUtils.readValue(sheet, rowNumber, "V"));

			mciDto.setTimeOut(ExcelUtils.readValue(sheet, rowNumber, "O"));// 타임아웃

			intrfccombsDto.setMciDto(mciDto);
			if (!isValid) {
				errMsg.append("\n시트명: " + sheet.getSheetName());
				throw new ServiceException(BxMessages.Error.INTRFC_VALID_CHECK, errMsg.toString());
			}

			
			int checkYn = intrfccombsDao.checkIntrfcid(interfaceId);

			if (checkYn == 0) {
				this.addDefinition(intrfccombsDto, false);
			} else {
				this.updateDefinition(intrfccombsDto, false);
			}
			
			rowNumber++;
		}

	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	protected void readEaiDataDummy(IntrfccombsDto intrfccombsDto, Workbook workbook, String intrfcTypeCd) {

		Sheet sheet = workbook.getSheet("Interface List_EAI(D)");

		boolean insertYn = true;
		String sendSyscd = "";
		String recvSysCd = "";

		Map<String, MsgInsertDto> msgResultMap = new HashMap<String, MsgInsertDto>();

		int rowNumber = 5;

		logger.debug("###baejh eai test 1111 ###");
		String interfaceId = "";
		while (true) {
			String eaiType = ExcelUtils.readValue(sheet, rowNumber, "G");
			if (eaiType.equals("AP to AP")) {
				eaiType = "APTOAP";
			} else if (eaiType.equals("DB to DB")) {
				eaiType = "DBTODB";
			} else if (eaiType.equals("File to File")) {
				eaiType = "FILETOFILE";
			} else {

			}
			
			logger.debug("###baejh eai test eaiType :  " + eaiType);
			String endPoint = ExcelUtils.readValue(sheet, rowNumber, "B");
			logger.debug("###  eaiType : " + eaiType);
			if (endPoint.equals("end") || eaiType.equals("") || eaiType == null) {
				logger.debug("break :  " + eaiType);
				break;
			}
			
			logger.debug("###baejh eai test 2222  ");

			if (intrfcTypeCd.equals("EAI_I")) {
				if (eaiType.equals("APTOAP")) {

					logger.debug("###baejh eai test 333  ");
					StringBuilder errMsg = new StringBuilder();
					boolean isValid = true;
					String appCdL1 = ExcelUtils.readValue(sheet, rowNumber, "B");
					sendSyscd = ExcelUtils.readValue(sheet, rowNumber, "H");
					recvSysCd = ExcelUtils.readValue(sheet, rowNumber, "K");

					IntrfcIdCreateDto interfaceIdDto = new IntrfcIdCreateDto();
					interfaceIdDto.setIntrfcTypeCd("EAI_I");
					interfaceIdDto.setLv1Cd(appCdL1);
					interfaceIdDto.setSendSysCd(sendSyscd);
					interfaceIdDto.setReceiveSysCd(recvSysCd);

					// 공통
					insertYn = true;
					interfaceId = ExcelUtils.readValue(sheet, rowNumber, "D");
					intrfccombsDto.setIntrfcTypeCd("EAI_I");
					intrfccombsDto.setIntrfcId(interfaceId);
					intrfccombsDto.setIntrfcNm(ExcelUtils.readValue(sheet, rowNumber, "E"));
					intrfccombsDto.setWorkStatusCd("WORKING");
					intrfccombsDto.setIntrfcWayCd(eaiType);
					intrfccombsDto.setTrxDscd(ExcelUtils.readValue(sheet, rowNumber, "F").toUpperCase());
					intrfccombsDto.setRegManId(ExcelUtils.readValue(sheet, rowNumber, "J"));
					intrfccombsDto.setRegDttm(DateUtils.getCurrentDate(9));
					intrfccombsDto.setIntrfcTypeCd(intrfcTypeCd);
					intrfccombsDto.setLv1Cd(appCdL1);

					List<IntrfcsrsysdtDto> srSysDtoList = new ArrayList<IntrfcsrsysdtDto>();
					IntrfcsrsysdtDto srSysDto = new IntrfcsrsysdtDto();
					IntrfccombsDetailEAIDto eaiDto = new IntrfccombsDetailEAIDto();
					Map<String, String> msgMap = new HashMap<String, String>();

					// 송신정보
					srSysDto.setSysCd(ExcelUtils.readValue(sheet, rowNumber, "H"));
					srSysDto.setSysNm(ExcelUtils.readValue(sheet, rowNumber, "I"));
					srSysDto.setCrgManNm(ExcelUtils.readValue(sheet, rowNumber, "J"));
					srSysDto.setIntrfcId(interfaceId);
					srSysDto.setSrSeq(1);
					srSysDto.setSrTypeCd("SEND");
					srSysDtoList.add(srSysDto);

					// 수신정보
					srSysDto = new IntrfcsrsysdtDto();
					srSysDto.setSysCd(ExcelUtils.readValue(sheet, rowNumber, "K"));
					srSysDto.setSysNm(ExcelUtils.readValue(sheet, rowNumber, "L"));
					srSysDto.setCrgManNm(ExcelUtils.readValue(sheet, rowNumber, "M"));
					srSysDto.setIntrfcId(interfaceId);
					srSysDto.setSrSeq(1);
					srSysDto.setSrTypeCd("RECEIVE");

					// APTOAP 속성
					String trxCd = ExcelUtils.readValue(sheet, rowNumber, "N");

					srSysDto.setTrxCd(trxCd);
					srSysDtoList.add(srSysDto);
					intrfccombsDto.setSyncAsyncDscd(getCodeValueNm("SYNC_DSCD", convertTermEngToKor(ExcelUtils.readValue(sheet, rowNumber, "O")), "ko"));
					intrfccombsDto.setRspsYn(ExcelUtils.readValue(sheet, rowNumber, "P"));
					intrfccombsDto.setMsgTrnsfrmYn(ExcelUtils.readValue(sheet, rowNumber, "R"));
					eaiDto.setTimeOut(ExcelUtils.readValue(sheet, rowNumber, "Q")); // 타임아웃

					// 공통정보
					intrfccombsDto.setRegManId(ExcelUtils.readValue(sheet, rowNumber, "AB"));
					eaiDto.setIntrfDesc(ExcelUtils.readValue(sheet, rowNumber, "AC"));

					intrfccombsDto.setIntrfcsrsysdtDto(srSysDtoList);
					intrfccombsDto.setEaiDto(eaiDto);

				} else if (eaiType.equals("DBTODB")) {
					

					logger.debug("###baejh eai test 444  ");
					StringBuilder errMsg = new StringBuilder();
					boolean isValid = true;
					String appCdL1 = ExcelUtils.readValue(sheet, rowNumber, "B");
					sendSyscd = ExcelUtils.readValue(sheet, rowNumber, "H");
					recvSysCd = ExcelUtils.readValue(sheet, rowNumber, "K");

					IntrfcIdCreateDto interfaceIdDto = new IntrfcIdCreateDto();
					interfaceIdDto.setIntrfcTypeCd("EAI_I");
					interfaceIdDto.setLv1Cd(appCdL1);
					interfaceIdDto.setSendSysCd(sendSyscd);
					interfaceIdDto.setReceiveSysCd(recvSysCd);

					// 공통
					insertYn = true;
					intrfccombsDto.setIntrfcTypeCd("EAI_I");
					interfaceId = ExcelUtils.readValue(sheet, rowNumber, "D");
					intrfccombsDto.setIntrfcId(interfaceId);
					intrfccombsDto.setIntrfcNm(ExcelUtils.readValue(sheet, rowNumber, "E"));
					intrfccombsDto.setWorkStatusCd("WORKING");
					intrfccombsDto.setIntrfcWayCd(eaiType);
					intrfccombsDto.setTrxDscd(ExcelUtils.readValue(sheet, rowNumber, "F").toUpperCase());
					intrfccombsDto.setRegManId(ExcelUtils.readValue(sheet, rowNumber, "J"));
					intrfccombsDto.setRegDttm(DateUtils.getCurrentDate(9));
					intrfccombsDto.setIntrfcTypeCd(intrfcTypeCd);
					intrfccombsDto.setLv1Cd(appCdL1);

					List<IntrfcsrsysdtDto> srSysDtoList = new ArrayList<IntrfcsrsysdtDto>();
					IntrfcsrsysdtDto srSysDto = new IntrfcsrsysdtDto();
					IntrfccombsDetailEAIDto eaiDto = new IntrfccombsDetailEAIDto();
					Map<String, String> msgMap = new HashMap<String, String>();

					// 송신정보
					srSysDto.setSysCd(ExcelUtils.readValue(sheet, rowNumber, "H"));
					srSysDto.setSysNm(ExcelUtils.readValue(sheet, rowNumber, "I"));
					srSysDto.setCrgManNm(ExcelUtils.readValue(sheet, rowNumber, "J"));
					srSysDto.setIntrfcId(interfaceId);
					srSysDto.setSrSeq(1);
					srSysDto.setSrTypeCd("SEND");
					srSysDtoList.add(srSysDto);

					// 수신정보
					srSysDto = new IntrfcsrsysdtDto();
					srSysDto.setSysCd(ExcelUtils.readValue(sheet, rowNumber, "K"));
					srSysDto.setSysNm(ExcelUtils.readValue(sheet, rowNumber, "L"));
					srSysDto.setCrgManNm(ExcelUtils.readValue(sheet, rowNumber, "M"));
					srSysDto.setIntrfcId(interfaceId);
					srSysDto.setSrSeq(1);
					srSysDto.setSrTypeCd("RECEIVE");

					srSysDtoList.add(srSysDto);

					// 공통정보
					intrfccombsDto.setRegManId(ExcelUtils.readValue(sheet, rowNumber, "AB"));
					eaiDto.setIntrfDesc(ExcelUtils.readValue(sheet, rowNumber, "AC"));

					// DBTODB
					eaiDto.setLobColNm(ExcelUtils.readValue(sheet, rowNumber, "W"));// 랍컬럼명
					eaiDto.setSearchProcCnt(Integer.parseInt(ExcelUtils.readValue(sheet, rowNumber, "X")));// 조회처리건수
					eaiDto.setErrSkipYn(ExcelUtils.readValue(sheet, rowNumber, "Y"));// 에러스킵여부

					intrfccombsDto.setIntrfcsrsysdtDto(srSysDtoList);
					intrfccombsDto.setEaiDto(eaiDto);

				} else if (eaiType.equals("FILETOFILE")) {

					logger.debug("###baejh eai test 555  ");
					StringBuilder errMsg = new StringBuilder();
					boolean isValid = true;
					String appCdL1 = ExcelUtils.readValue(sheet, rowNumber, "B");
					sendSyscd = ExcelUtils.readValue(sheet, rowNumber, "H");
					recvSysCd = ExcelUtils.readValue(sheet, rowNumber, "K");

					IntrfcIdCreateDto interfaceIdDto = new IntrfcIdCreateDto();
					interfaceIdDto.setIntrfcTypeCd("EAI_I");
					interfaceIdDto.setLv1Cd(appCdL1);
					interfaceIdDto.setSendSysCd(sendSyscd);
					interfaceIdDto.setReceiveSysCd(recvSysCd);

					// 공통
					insertYn = true;
					intrfccombsDto.setIntrfcTypeCd("EAI_I");
					interfaceId = ExcelUtils.readValue(sheet, rowNumber, "D");
					intrfccombsDto.setIntrfcId(interfaceId);
					intrfccombsDto.setIntrfcNm(ExcelUtils.readValue(sheet, rowNumber, "E"));
					intrfccombsDto.setWorkStatusCd("WORKING");
					intrfccombsDto.setIntrfcWayCd(eaiType);
					intrfccombsDto.setTrxDscd(ExcelUtils.readValue(sheet, rowNumber, "F").toUpperCase());
					intrfccombsDto.setRegManId(ExcelUtils.readValue(sheet, rowNumber, "J"));
					intrfccombsDto.setRegDttm(DateUtils.getCurrentDate(9));
					intrfccombsDto.setIntrfcTypeCd(intrfcTypeCd);
					intrfccombsDto.setLv1Cd(appCdL1);

					List<IntrfcsrsysdtDto> srSysDtoList = new ArrayList<IntrfcsrsysdtDto>();
					IntrfcsrsysdtDto srSysDto = new IntrfcsrsysdtDto();
					IntrfccombsDetailEAIDto eaiDto = new IntrfccombsDetailEAIDto();
					Map<String, String> msgMap = new HashMap<String, String>();

					// 송신정보
					srSysDto.setSysCd(ExcelUtils.readValue(sheet, rowNumber, "H"));
					srSysDto.setSysNm(ExcelUtils.readValue(sheet, rowNumber, "I"));
					srSysDto.setCrgManNm(ExcelUtils.readValue(sheet, rowNumber, "J"));
					srSysDto.setIntrfcId(interfaceId);
					srSysDto.setSrSeq(1);
					srSysDto.setSrTypeCd("SEND");
					srSysDtoList.add(srSysDto);

					// 수신정보
					srSysDto = new IntrfcsrsysdtDto();
					srSysDto.setSysCd(ExcelUtils.readValue(sheet, rowNumber, "K"));
					srSysDto.setSysNm(ExcelUtils.readValue(sheet, rowNumber, "L"));
					srSysDto.setCrgManNm(ExcelUtils.readValue(sheet, rowNumber, "M"));
					srSysDto.setIntrfcId(interfaceId);
					srSysDto.setSrSeq(1);
					srSysDto.setSrTypeCd("RECEIVE");

					srSysDtoList.add(srSysDto);

					// FILETOFILE
					eaiDto.setSendFileNm(ExcelUtils.readValue(sheet, rowNumber, "S"));// 파일명
					eaiDto.setSendFilePath(ExcelUtils.readValue(sheet, rowNumber, "T"));// 송신파일경로
					eaiDto.setRecvFilePath(ExcelUtils.readValue(sheet, rowNumber, "U"));// 파일경로
					eaiDto.setOccurCycle(ExcelUtils.readValue(sheet, rowNumber, "V"));// 발생주기

					intrfccombsDto.setIntrfcsrsysdtDto(srSysDtoList);
					intrfccombsDto.setEaiDto(eaiDto);
					//////
				}
			}

			int checkYn = intrfccombsDao.checkIntrfcid(interfaceId);

			if (checkYn == 0) {
				this.addDefinition(intrfccombsDto, false);
			} else {
				this.updateDefinition(intrfccombsDto, false);
			}

			rowNumber++;
		}
	}

	public boolean validationArrayRef(MsglayoutbsDto msglayoutbsDto, StringBuilder str) {

		List<MsglayoutdtDto> dtDto = msglayoutbsDto.getMsglayoutdtDto();
		Map<String, String> dtDtoMap = new HashMap<String, String>();
		boolean isValid = true;
		String msgDscd = msglayoutbsDto.getMsgDscd();

		for (MsglayoutdtDto dto : dtDto) {
			dtDtoMap.put(dto.getFldEngNm(), dto.getDataTypeNm());
		}

		for (MsglayoutdtDto dto : dtDto) {
			String fldEngNm = dto.getFldEngNm();
			String dataType = dto.getDataTypeNm();
			String arrayRef = dto.getArraySizeRefVal();

			if (dataType.equals("LAYOUT")) {
				if (!msgDscd.equals("ISOH") && !msgDscd.equals("ISOB")) {
					if (arrayRef != null && !arrayRef.equals("")) {
						if (isValid) {
							if (isInteger(arrayRef)) {
								if (Integer.parseInt(arrayRef) < 1) {
									str.append("\n배열참조 세팅 값은 0보다 큰 값이 세팅되어야 합니다. 전문ID: ["
											+ msglayoutbsDto.getMsgLayoutId() + "] 필드영문명: [" + fldEngNm + "]");
									isValid = false;
								}
							} else {
								if (dtDtoMap.containsKey(arrayRef)) {
									String fldDataType = dtDtoMap.get(arrayRef);
									if (!fldDataType.equals("INTEGER")) {
										str.append("\n배열참조값의 세팅되는 필드의 타입은 INTEGER타입이어야 합니다. 전문ID: ["
												+ msglayoutbsDto.getMsgLayoutId() + "] 필드영문명: [" + fldEngNm + "]");
										isValid = false;
									}
								} else {
									str.append("\n배열참조 세팅된 문자열이 전문 Layout 내에 존재하지 않는 필드입니다. 전문ID: ["
											+ msglayoutbsDto.getMsgLayoutId() + "] 필드영문명: [" + fldEngNm + "]");
									isValid = false;
								}
							}
						}
					}

				}

			}
		}

		return isValid;
	}

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

	protected boolean nullCheck(StringBuilder errMsg, String code, String name, boolean isValid) {

		logger.debug("field [{}] : value [{}]", name, code);
		if (StringUtils.isEmpty(code)) {
			errMsg.append("\n" + name + " 비어있습니다.");
			isValid = false;
		}

		return isValid;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	private Map<String, MsgInsertDto> mappingSetting(String interfaceId, Workbook workbook, Sheet sheet, int i,
			int startIndex, List<IntrfcmsglayoutdtDto> intrfcMsgLayoutDtoList, Map<String, String> msgMap,
			String mapData, String sendRecvType, String reqResType, String excelSheetCd, String sysCdCd,
			int mapGridSize, int sysCdRowNum, Map<String, String> msgInsertMap,
			Map<String, MsgInsertDto> msgResultMap) {

		StringBuilder errMsg = new StringBuilder();
		boolean isValid = true;

		IntrfcmsglayoutdtDto intrfcmsglayoutdtDtoSend = new IntrfcmsglayoutdtDto();
		intrfcmsglayoutdtDtoSend.setIntrfcId(interfaceId);

		Sheet sheetMsg = workbook.getSheet(ExcelUtils.readValue(sheet, i + mapGridSize, excelSheetCd));

		logger.debug("--------Sheet_Name : " + sheetMsg.getSheetName());

		isValid = validationMsgInfo(sheetMsg, errMsg);
		if (!isValid) {
			throw new ServiceException(BxMessages.Error.MSG_VALID_CHECK, errMsg.toString());
		}

		String layoutVersion = ExcelUtils.readValue(sheetMsg, 5, "W");
		String chlDscd = getCodeValueNm("CHL_DSCD", ExcelUtils.readValue(sheetMsg, 5, "N"), "ko");
		String trxDscd = layoutVersion.equals("v20181011") || layoutVersion.equals("v20181016")
				|| layoutVersion.equals("v20181112") || layoutVersion.equals("v20181114")
				|| layoutVersion.equals("v20181130") || layoutVersion.equals("v20181211")
				|| layoutVersion.equals("v20181226") || layoutVersion.equals("v20190109")
						? getCodeValueNm("TRAN_DSCD", ExcelUtils.readValue(sheetMsg, 5, "T"), "ko")
						: getCodeValueNm("TRAN_DSCD", ExcelUtils.readValue(sheetMsg, 5, "S"), "ko");
		String msgType = getCodeValueNm("MSG_TYPE", ExcelUtils.readValue(sheetMsg, 7, "N"), "ko");

		if (chlDscd.equals("INTERNAL")) {
			if (trxDscd.equals("ONLINE")) {
				if (!msgType.equals("IV")) {
					errMsg.append("\n");
					errMsg.append("대내 온라인 타입에 올 수 없는 전문 타입입니다.");
					isValid = false;

				}
				String sendSyscd = ExcelUtils.readValue(sheetMsg, 10, "D");
				String recvSysCd = ExcelUtils.readValue(sheetMsg, 10, "J");
				isValid = nullCheck(errMsg, sendSyscd, "송신시스템코드가", isValid);
				isValid = nullCheck(errMsg, recvSysCd, "수신시스템코드가", isValid);

				int send = srsysbsDao.selectAllCnt(sendSyscd, null, null, null, null);
				int recv = srsysbsDao.selectAllCnt(recvSysCd, null, null, null, null);

				if (send == 0) {
					errMsg.append("\n");
					errMsg.append("송신시스템코드가 존재하지 않습니다.");
					isValid = false;
				}
				if (recv == 0) {
					errMsg.append("\n");
					errMsg.append("수신시스템코드가 존재하지 않습니다.");
					isValid = false;
				}

			} else {
				if (!msgType.equals("BATH") && !msgType.equals("BATB") && !msgType.equals("BATT")) {
					errMsg.append("\n");
					errMsg.append("대내 배치 타입에 올 수 없는 전문 타입입니다.");
					isValid = false;
				}
				String fileId = layoutVersion.equals("v20181011") || layoutVersion.equals("v20181016")
						|| layoutVersion.equals("v20181112") || layoutVersion.equals("v20181114")
						|| layoutVersion.equals("v20181130") || layoutVersion.equals("v20181211")
						|| layoutVersion.equals("v20181226") || layoutVersion.equals("v20190109")
								? ExcelUtils.readValue(sheetMsg, 7, "T")
								: ExcelUtils.readValue(sheetMsg, 7, "S");
				isValid = nullCheck(errMsg, fileId, "파일아이디가", isValid);
				if (fileId.length() > 8) {
					fileId = fileId.substring(0, 8);
					// errMsg.append("\n");
					// errMsg.append("파일ID 사이즈가 8 이상입니다.");
					// isValid = false;
				}
			}
		} else {
			if (trxDscd.equals("ONLINE")) {
				if (!msgType.equals("IV") && !msgType.equals("CH") && !msgType.equals("ISOH")
						&& !msgType.equals("ISOB")) {
					errMsg.append("\n");
					errMsg.append("대외 온라인 타입에 올 수 없는 전문 타입입니다.");
					isValid = false;
				}

				if (msgType.equals("IV") || msgType.equals("ISOB")) {
					String extBizCd = "";
					if (layoutVersion.equals("v20181011") || layoutVersion.equals("v20181016")
							|| layoutVersion.equals("v20181112") || layoutVersion.equals("v20181114")
							|| layoutVersion.equals("v20181130") || layoutVersion.equals("v20181211")) {
						extBizCd = ExcelUtils.readValue(sheetMsg, 11, "W");
					} else if (layoutVersion.equals("v20181226") || layoutVersion.equals("v20190109")) {
						extBizCd = ExcelUtils.readValue(sheetMsg, 11, "X");
					} else {
						extBizCd = ExcelUtils.readValue(sheetMsg, 11, "V");
					}
					isValid = nullCheck(errMsg, extBizCd, "기관업무코드가", isValid);
					if (!isValid) {
						if (bizCdDao.selectBizcd(extBizCd) == null) {
							errMsg.append("\n");
							errMsg.append("기관업무코드가 존재하지 않습니다. ");
							isValid = false;
						}
					}
					String msgNmAndDto = ExcelUtils.readValue(sheetMsg, 7, "J");
					isValid = nullCheck(errMsg, msgNmAndDto, "전문번호가", isValid);
					String msgVer = "";
					if (layoutVersion.equals("v20181011") || layoutVersion.equals("v20181016")
							|| layoutVersion.equals("v20181112") || layoutVersion.equals("v20181114")
							|| layoutVersion.equals("v20181130") || layoutVersion.equals("v20181211")) {
						msgVer = ExcelUtils.readValue(sheetMsg, 11, "X");
					} else if (layoutVersion.equals("v20181226") || layoutVersion.equals("v20190109")) {
						msgVer = ExcelUtils.readValue(sheetMsg, 11, "Y");
					} else {
						msgVer = ExcelUtils.readValue(sheetMsg, 11, "W");
					}
					isValid = nullCheck(errMsg, msgVer, "전문버전이", isValid);
				} else {
					String extBizCd = "";
					if (layoutVersion.equals("v20181011") || layoutVersion.equals("v20181016")
							|| layoutVersion.equals("v20181112") || layoutVersion.equals("v20181114")
							|| layoutVersion.equals("v20181130") || layoutVersion.equals("v20181211")) {
						extBizCd = ExcelUtils.readValue(sheetMsg, 11, "W");
					} else if (layoutVersion.equals("v20181226") || layoutVersion.equals("v20190109")) {
						extBizCd = ExcelUtils.readValue(sheetMsg, 11, "X");
					} else {
						extBizCd = ExcelUtils.readValue(sheetMsg, 11, "V");
					}
					isValid = nullCheck(errMsg, extBizCd, "기관업무코드가", isValid);
					if (!isValid) {
						if (bizCdDao.selectBizcd(extBizCd) == null) {
							errMsg.append("\n");
							errMsg.append("기관업무코드가 존재하지 않습니다. ");
							isValid = false;
						}
					}
				}

			} else {
				if (!msgType.equals("BATH") && !msgType.equals("BATB") && !msgType.equals("BATT")) {
					errMsg.append("\n");
					errMsg.append("대내 배치 타입에 올 수 없는 전문 타입입니다.");
					isValid = false;
				}
				String msgVer = "";
				if (layoutVersion.equals("v20181011") || layoutVersion.equals("v20181016")
						|| layoutVersion.equals("v20181112") || layoutVersion.equals("v20181114")
						|| layoutVersion.equals("v20181130") || layoutVersion.equals("v20181211")) {
					msgVer = ExcelUtils.readValue(sheetMsg, 11, "X");
				} else if (layoutVersion.equals("v20181226") || layoutVersion.equals("v20190109")) {
					msgVer = ExcelUtils.readValue(sheetMsg, 11, "Y");
				} else {
					msgVer = ExcelUtils.readValue(sheetMsg, 11, "W");
				}
				isValid = nullCheck(errMsg, msgVer, "전문버전이", isValid);
				String fileId = layoutVersion.equals("v20181011") || layoutVersion.equals("v20181016")
						|| layoutVersion.equals("v20181112") || layoutVersion.equals("v20181114")
						|| layoutVersion.equals("v20181130") || layoutVersion.equals("v20181211")
						|| layoutVersion.equals("v20181226") || layoutVersion.equals("v20190109")
								? ExcelUtils.readValue(sheetMsg, 7, "T")
								: ExcelUtils.readValue(sheetMsg, 7, "S");
				isValid = nullCheck(errMsg, fileId, "파일아이디가", isValid);
				// if (fileId.length() > 8) {
				// errMsg.append("\n");
				// errMsg.append("파일ID 사이즈가 8 이상입니다.");
				// isValid = false;
				// }
			}
		}

		if (!isValid) {
			throw new ServiceException(BxMessages.Error.MSG_VALID_CHECK, errMsg.toString());
		}

		MsgIdCreateDto msgId = new MsgIdCreateDto();

		String layOutIdDoc = ExcelUtils.readValue(sheetMsg, 5, "D");
		boolean insertYn = true;

		msgId.setChlDscd(getCodeValueNm("CHL_DSCD", ExcelUtils.readValue(sheetMsg, 5, "N"), "ko"));
		msgId.setExtrnlBizNm(ExcelUtils.readValue(sheetMsg, 11, "V"));
		msgId.setFileId(layoutVersion.equals("v20181011") || layoutVersion.equals("v20181016")
				|| layoutVersion.equals("v20181112") || layoutVersion.equals("v20181114")
				|| layoutVersion.equals("v20181130") || layoutVersion.equals("v20181211")
				|| layoutVersion.equals("v20181226") || layoutVersion.equals("v20190109")
						? ExcelUtils.readValue(sheetMsg, 7, "T")
						: ExcelUtils.readValue(sheetMsg, 7, "S"));
		msgId.setLv3Cd(ExcelUtils.readValue(sheetMsg, 8, "F"));
		msgId.setMsgDscd(getCodeValueNm("MSG_TYPE", ExcelUtils.readValue(sheetMsg, 7, "N"), "ko"));
		msgId.setMsgNumber(ExcelUtils.readValue(sheetMsg, 7, "J"));
		if (layoutVersion.equals("v20181011") || layoutVersion.equals("v20181016") || layoutVersion.equals("v20181112")
				|| layoutVersion.equals("v20181114") || layoutVersion.equals("v20181130")
				|| layoutVersion.equals("v20181211")) {
			if (ExcelUtils.readValue(sheetMsg, 11, "X") == null || ExcelUtils.readValue(sheetMsg, 11, "X").equals("")
					|| ExcelUtils.readValue(sheetMsg, 11, "X").equals("0")) {
				msgId.setMsgVersion(1);
			} else {
				msgId.setMsgVersion(Integer.parseInt(ExcelUtils.readValue(sheetMsg, 11, "X")));
			}

			msgId.setTrxDscd(getCodeValueNm("TRAN_DSCD", ExcelUtils.readValue(sheetMsg, 5, "T"), "ko"));

		} else if (layoutVersion.equals("v20181226") || layoutVersion.equals("v20190109")) {
			if (ExcelUtils.readValue(sheetMsg, 11, "Y") == null || ExcelUtils.readValue(sheetMsg, 11, "Y").equals("")
					|| ExcelUtils.readValue(sheetMsg, 11, "Y").equals("0")) {
				msgId.setMsgVersion(1);
			} else {
				msgId.setMsgVersion(Integer.parseInt(ExcelUtils.readValue(sheetMsg, 11, "Y")));
			}

			msgId.setTrxDscd(getCodeValueNm("TRAN_DSCD", ExcelUtils.readValue(sheetMsg, 5, "T"), "ko"));

		} else {
			if (ExcelUtils.readValue(sheetMsg, 11, "W") == null || ExcelUtils.readValue(sheetMsg, 11, "W").equals("")
					|| ExcelUtils.readValue(sheetMsg, 11, "W").equals("0")) {
				msgId.setMsgVersion(1);
			} else {
				msgId.setMsgVersion(Integer.parseInt(ExcelUtils.readValue(sheetMsg, 11, "W")));
			}

			msgId.setTrxDscd(getCodeValueNm("TRAN_DSCD", ExcelUtils.readValue(sheetMsg, 5, "S"), "ko"));

		}
		msgId.setReceiveSysCd(ExcelUtils.readValue(sheetMsg, 10, "J"));
		msgId.setSendSysCd(ExcelUtils.readValue(sheetMsg, 10, "D"));

		MsgIdCreateDto msgIdDto = msglayoutService.getMsgLayoutId(msgId, isValid, errMsg);

		if (!isValid) {
			throw new ServiceException(BxMessages.Error.MSG_VALID_CHECK, errMsg.toString());
		}

		String layoutId = msgIdDto.getMsgLayoutId();

		if (layoutId.length() != 19) {
			errMsg.append("\n");
			errMsg.append("레이아웃 아이디 사이즈가 맞지 않습니다. [" + layoutId + "]");
			throw new ServiceException(BxMessages.Error.MSG_VALID_CHECK, errMsg.toString());
		}

		// MsglayoutbsDto data = new MsglayoutbsDto();
		MsglayoutbsDto dataResult = new MsglayoutbsDto();
		if (layOutIdDoc != null && !layOutIdDoc.equals("")) {
			layoutId = layOutIdDoc;
			insertYn = false;
		} else {
			if (msgIdDto.getSeq() == null) {
				dataResult.setCrtnSeq(null);
			} else {
				dataResult.setCrtnSeq(Integer.parseInt(msgIdDto.getSeq()));
			}
		}

		MsgInsertDto msginsertDto = new MsgInsertDto();

		if (insertYn) {
			if (!msgInsertMap.containsKey(sheetMsg.getSheetName())) {
				logger.debug("-------------------------add--------------------------------");
				readDataIndividual(sheetMsg, dataResult, layoutId);
				readDataGrid(sheetMsg, dataResult, startIndex, layoutId, isValid, errMsg);

				StringBuilder str = new StringBuilder();
				boolean arrayValid = validationArrayRef(dataResult, str);
				if (!arrayValid) {
					throw new ServiceException(BxMessages.Error.ARRAY_REF_VALIDATION, str.toString());
				}

				msginsertDto.setInsertYn(insertYn);
				msginsertDto.setMsglayoutbsDto(dataResult);
				msgResultMap.put(dataResult.getMsgLayoutId(), msginsertDto);
				msglayoutService.add(dataResult, "FILE");
				msgInsertMap.put(sheetMsg.getSheetName(), layoutId);
			}
		} else {
			if (!msgInsertMap.containsKey(sheetMsg.getSheetName())) {
				logger.debug("-------------------------update--------------------------------");
				readDataIndividual(sheetMsg, dataResult, layoutId);
				readDataGrid(sheetMsg, dataResult, startIndex, layoutId, isValid, errMsg);

				StringBuilder str = new StringBuilder();
				boolean arrayValid = validationArrayRef(dataResult, str);
				if (!arrayValid) {
					throw new ServiceException(BxMessages.Error.ARRAY_REF_VALIDATION, str.toString());
				}

				msginsertDto.setInsertYn(insertYn);
				msginsertDto.setMsglayoutbsDto(dataResult);
				msgResultMap.put(dataResult.getMsgLayoutId(), msginsertDto);
				msglayoutService.update(dataResult);
				msgInsertMap.put(sheetMsg.getSheetName(), layoutId);
			}
		}

		if (!isValid) {
			throw new ServiceException(BxMessages.Error.MSG_VALID_CHECK, errMsg.toString());
		}

		intrfcmsglayoutdtDtoSend.setMsglayoutbsDto(dataResult);
		intrfcmsglayoutdtDtoSend.setRqstRspsSeq(i);
		intrfcmsglayoutdtDtoSend.setRqstRspsTypeCd(reqResType);
		intrfcmsglayoutdtDtoSend.setSrSeq(1);
		intrfcmsglayoutdtDtoSend.setSrTypeCd(sendRecvType);
		intrfcmsglayoutdtDtoSend.setSysCd(ExcelUtils.readValue(sheet, sysCdRowNum, sysCdCd));

		if (msgInsertMap.containsKey(sheetMsg.getSheetName())) {
			intrfcmsglayoutdtDtoSend.setMsgLayoutId(msgInsertMap.get(sheetMsg.getSheetName()));
			msgMap.put(mapData + i, msgInsertMap.get(sheetMsg.getSheetName()));
		} else {
			intrfcmsglayoutdtDtoSend.setMsgLayoutId(layoutId);
			msgMap.put(mapData + i, layoutId);
		}
		intrfcMsgLayoutDtoList.add(intrfcmsglayoutdtDtoSend);
		return msgResultMap;
	}

	private List<IntrfccombsMappingDto> readMapping(Sheet mappingSheet, Map<String, String> msgMap,
			IntrfccombsDto intrfccombsDto, List<IntrfccombsMappingDto> intrfccombsMappingDto, String srcSeqData,
			String targetSeqData, String reqResType) {

		String intrfcType = intrfccombsDto.getIntrfcTypeCd();
		String trxDscd = intrfccombsDto.getTrxDscd();
		String mappingVersion = ExcelUtils.readValue(mappingSheet, 1, "AE");

		int index = 5;
		int mappingStartIndex = 5;
		for (; index < mappingSheet.getPhysicalNumberOfRows(); index++) {
			String flag = ExcelUtils.readValue(mappingSheet, index, "B");
			logger.debug(flag);
			if (flag.equals("END")) {
				break;
			}
		}

		int srcSeq = 0;
		String preSrcMsgId = null;
		String pretargetMsgId = null;
		int mapSeq = 1;
		int targetSeq = 1;
		MsglayoutbsDto msgDto = null;
		for (mappingStartIndex = 5; mappingStartIndex < index; mappingStartIndex++) {
			IntrfccombsMappingDto mappingDto = new IntrfccombsMappingDto();

			if (mappingVersion != null && mappingVersion.equals("v20181113")) {

				if (!ExcelUtils.readValue(mappingSheet, mappingStartIndex, "Y").equals("")) {
					srcSeq = Integer.parseInt(ExcelUtils.readValue(mappingSheet, mappingStartIndex, "Y"));
					preSrcMsgId = msgMap.get(srcSeqData + srcSeq);
					logger.debug("mappingStartIndex {}", mappingStartIndex);
					logger.debug("preSrcMsgId: {}", preSrcMsgId);
				}
				if (!ExcelUtils.readValue(mappingSheet, mappingStartIndex, "L").equals("")) {
					targetSeq = Integer.parseInt(ExcelUtils.readValue(mappingSheet, mappingStartIndex, "L"));
					pretargetMsgId = msgMap.get(targetSeqData + targetSeq);
					logger.debug("mappingStartIndex {}", mappingStartIndex);
					logger.debug("pretargetMsgId: {}", pretargetMsgId);
					for (IntrfcmsglayoutdtDto dto : intrfccombsDto.getIntrfcmsglayoutdtDto()) {
						if (dto.getMsgLayoutId().equals(pretargetMsgId)) {
							msgDto = dto.getMsglayoutbsDto();
							break;
						}
					}
				}

				mappingDto.setMappingSeq(mapSeq);

				String mapTypeString = ExcelUtils.readValue(mappingSheet, mappingStartIndex, "Z");
				String mapType = "";
				if (mapTypeString.equals("변환없음")) {
					mapType = "NONE";
				} else {
					mapType = getCodeValueNm("MAPPING_DSCD", ExcelUtils.readValue(mappingSheet, mappingStartIndex, "Z"),
							"ko");
				}

				if (mapType == null || "".equals(mapType)) {
					mapType = "";
				}
				mappingDto.setMappingTypeCd(mapType);
				mappingDto.setReqResTypeCd(reqResType);
				if (mappingDto.getMappingTypeCd().equals("PROPT")) {
					mappingDto.setSrcData(
							preSrcMsgId + "." + ExcelUtils.readValue(mappingSheet, mappingStartIndex, "AA"));
				} else if (mappingDto.getMappingTypeCd().equals("")) {
					mappingDto.setMappingTypeCd("NONE");
					mappingDto.setSrcData(ExcelUtils.readValue(mappingSheet, mappingStartIndex, "AA"));
				} else {
					mappingDto.setSrcData(ExcelUtils.readValue(mappingSheet, mappingStartIndex, "AA"));
				}

				if (msgDto.getMsglayoutdtDto() != null) {
					for (MsglayoutdtDto dto : msgDto.getMsglayoutdtDto()) {
						if (ExcelUtils.readValue(mappingSheet, mappingStartIndex, "N").equals(dto.getFldEngNm())
								&& Integer.parseInt(ExcelUtils.readValue(mappingSheet, mappingStartIndex, "U")) == dto
										.getFldLvNo()) {
							mappingDto.setTargetData(dto.getFldUnqId());
						}
					}
				}

				mappingDto.setWideHalfCharCngCd(getCodeValueNm("WIDE_HALF_CHAR_CD",
						ExcelUtils.readValue(mappingSheet, mappingStartIndex, "V"), "ko"));
				mappingDto.setEncCd(getCodeValueNm("ENC_CD_MAPPING",
						ExcelUtils.readValue(mappingSheet, mappingStartIndex, "W"), "ko"));
				mappingDto.setFldEncoding(getCodeValueNm("FIELD_ENCODING_ONL",
						ExcelUtils.readValue(mappingSheet, mappingStartIndex, "X"), "ko"));

				mappingDto.setTargetDataSeq(mapSeq);
				intrfccombsMappingDto.add(mappingDto);
				mapSeq++;

			} else {

				if (!ExcelUtils.readValue(mappingSheet, mappingStartIndex, "V").equals("")) {
					srcSeq = Integer.parseInt(ExcelUtils.readValue(mappingSheet, mappingStartIndex, "V"));
					preSrcMsgId = msgMap.get(srcSeqData + srcSeq);
					logger.debug("mappingStartIndex {}", mappingStartIndex);
					logger.debug("preSrcMsgId: {}", preSrcMsgId);
				}
				if (!ExcelUtils.readValue(mappingSheet, mappingStartIndex, "L").equals("")) {
					targetSeq = Integer.parseInt(ExcelUtils.readValue(mappingSheet, mappingStartIndex, "L"));
					pretargetMsgId = msgMap.get(targetSeqData + targetSeq);
					logger.debug("mappingStartIndex {}", mappingStartIndex);
					logger.debug("pretargetMsgId: {}", pretargetMsgId);
					for (IntrfcmsglayoutdtDto dto : intrfccombsDto.getIntrfcmsglayoutdtDto()) {
						if (dto.getMsgLayoutId().equals(pretargetMsgId)) {
							msgDto = dto.getMsglayoutbsDto();
							break;
						}
					}
				}

				mappingDto.setMappingSeq(mapSeq);

				String mapTypeString = ExcelUtils.readValue(mappingSheet, mappingStartIndex, "W");
				String mapType = "";
				if (mapTypeString.equals("변환없음")) {
					mapType = "NONE";
				} else {
					mapType = getCodeValueNm("MAPPING_DSCD", ExcelUtils.readValue(mappingSheet, mappingStartIndex, "W"),
							"ko");
				}

				if (mapType == null || "".equals(mapType)) {
					mapType = "";
				}
				mappingDto.setMappingTypeCd(mapType);
				mappingDto.setReqResTypeCd(reqResType);
				if (mappingDto.getMappingTypeCd().equals("PROPT")) {
					mappingDto
							.setSrcData(preSrcMsgId + "." + ExcelUtils.readValue(mappingSheet, mappingStartIndex, "X"));
				} else if (mappingDto.getMappingTypeCd().equals("")) {
					mappingDto.setMappingTypeCd("NONE");
					mappingDto.setSrcData(ExcelUtils.readValue(mappingSheet, mappingStartIndex, "X"));
				} else {
					mappingDto.setSrcData(ExcelUtils.readValue(mappingSheet, mappingStartIndex, "X"));
				}

				if (msgDto.getMsglayoutdtDto() != null) {
					for (MsglayoutdtDto dto : msgDto.getMsglayoutdtDto()) {
						if (ExcelUtils.readValue(mappingSheet, mappingStartIndex, "N").equals(dto.getFldEngNm())
								&& Integer.parseInt(ExcelUtils.readValue(mappingSheet, mappingStartIndex, "U")) == dto
										.getFldLvNo()) {
							mappingDto.setTargetData(dto.getFldUnqId());
						}
					}
				}

				mappingDto.setTargetDataSeq(mapSeq);
				intrfccombsMappingDto.add(mappingDto);
				mapSeq++;
			}
		}

		return intrfccombsMappingDto;

	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	private void readDataIndividual(Sheet sheet, MsglayoutbsDto data, String layoutId) {

		logger.debug("SSSSSSSSSSSSSSSSSSSSSSSSheetNamessssssssssssssssssssssssssssssssssssss");
		logger.debug("{}", sheet.getSheetName());
		logger.debug("SSSSSSSSSSSSSSSSSSSSSSSSheetNamessssssssssssssssssssssssssssssssssssss");
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

		data.setWorkStatusCd("WORK_COMP");

		return;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	private int readDataGrid(Sheet sheet, MsglayoutbsDto data, int startIndex, String layoutId, boolean isValid,
			StringBuilder errMsg) {
		List<MsglayoutdtDto> layoutList = new ArrayList<>();
		String msgDscd = data.getMsgDscd();

		int index = startIndex;

		String layoutVersion = ExcelUtils.readValue(sheet, 5, "W");
		List<String> layoutValList = new ArrayList<String>();

		List<String> parentsList = new ArrayList<String>();
		parentsList.add(layoutId);
		Map<String, MsglayoutdtDto> layoutTypeMap = new HashMap<String, MsglayoutdtDto>();
		Map<String, String> ioKeyMap = new HashMap<String, String>();
		int replKeyCnt = 0;

		int seq = 1;
		for (; index < sheet.getPhysicalNumberOfRows(); index++) {
			if (ExcelUtils.readValue(sheet, index, "B").equals("계")) {
				break;
			}
			MsglayoutdtDto layout = new MsglayoutdtDto();

			if (layoutVersion.equals("v20180802")) {
				layout.setMsgLayoutId(layoutId);
				layout.setMsgSeq(seq++);
				isValid = nullCheck(errMsg, ExcelUtils.readValue(sheet, index, "C"), "영문명", isValid);
				layout.setFldEngNm(ExcelUtils.readValue(sheet, index, "C"));
				isValid = nullCheck(errMsg, ExcelUtils.readValue(sheet, index, "D"), "한글명", isValid);
				layout.setFldKorNm(ExcelUtils.readValue(sheet, index, "D"));
				String type = getCodeValueNm("DATA_TYPE", ExcelUtils.readValue(sheet, index, "F"), "ko");
				isValid = nullCheck(errMsg, type, "데이터타입", isValid);
				layout.setDataTypeNm(type);
				layout.setMsgLen(Integer.parseInt(ExcelUtils.readValue(sheet, index, "G")));
				layout.setDecimalLen(Integer.parseInt(ExcelUtils.readValue(sheet, index, "H")));

				boolean isValid2 = metaCheck(layout, errMsg, data);
				if (!isValid2) {
					isValid = false;
				}

				layout.setFldLvNo(Integer.parseInt(ExcelUtils.readValue(sheet, index, "I")));
				layout.setChildDtoNm(ExcelUtils.readValue(sheet, index, "J"));
				layout.setArraySizeRefVal(ExcelUtils.readValue(sheet, index, "K"));
				layout.setBasicVal(ExcelUtils.readValue(sheet, index, "L"));
				layout.setPrivacyDscd(getCodeValueNm("PRIVACY_CD", ExcelUtils.readValue(sheet, index, "M"), "ko"));
				layout.setEncYn(ExcelUtils.readValue(sheet, index, "N"));
				String align = ExcelUtils.readValue(sheet, index, "O");
				if (StringUtils.isEmpty(align)) {
					if (type.equals("STRING") || type.equals("BYTEARRAY")) {
						align = "LEFT";
					} else if (type.equals("LONG") || type.equals("BIGDECIMAL")) {
						align = "RIGHT";
					} else {
						align = "";
					}
				}
				layout.setAlignNm(align);
				layout.setCdAttrNm(ExcelUtils.readValue(sheet, index, "P"));
				String ioKey = getCodeValueNm("IO_KEY", ExcelUtils.readValue(sheet, index, "R"), "ko");
				if (ioKeyMap.containsKey(ioKey)) {
					errMsg.append("\n전문에 같은 IO Key가 존재할 수 없습니다.");
					isValid = false;
				}
				layout.setIoKey(ioKey);// IO키
				if (ioKey != null && !ioKey.equals("")) {
					ioKeyMap.put(ioKey, ioKey);
				}
				layout.setFillerVal(ExcelUtils.readValue(sheet, index, "S"));
				layout.setFldRmk(ExcelUtils.readValue(sheet, index, "T"));// 비고
				layout.setExtrnlMsgNoYn(ExcelUtils.readValue(sheet, index, "V"));
				layout.setExtrnlSrchKeyYn(ExcelUtils.readValue(sheet, index, "W"));
				layout.setExtrnlMsgNoYn(ExcelUtils.readValue(sheet, index, "Z"));
				// layout.setExtrnlOffsetYn(ExcelUtils.readValue(sheet, index, "W"));
				layout.setIso8583SpecNo(ExcelUtils.readValue(sheet, index, "AA"));
				layout.setIso8583FldCharSetCd(ExcelUtils.readValue(sheet, index, "AB"));// 필드타입
				layout.setIso8583VariableYn(ExcelUtils.readValue(sheet, index, "AC"));
				layout.setIso8583FldLenTypeCd(
						getCodeValueNm("ISO_FLD_LEN_TYPE_CD", ExcelUtils.readValue(sheet, index, "AD"), "ko"));
				layout.setIso8583LenFldInclYn(ExcelUtils.readValue(sheet, index, "AE"));
				layout.setIso8583LenFldCopyYn(ExcelUtils.readValue(sheet, index, "AF"));
				layout.setIso8583TrnsmsnDataLen(ExcelUtils.readValue(sheet, index, "AG") != null
						&& !ExcelUtils.readValue(sheet, index, "AG").equals("")
								? Integer.parseInt(ExcelUtils.readValue(sheet, index, "AG"))
								: null);
			} else if (layoutVersion.equals("v20180829")) {
				layout.setMsgLayoutId(layoutId);
				layout.setMsgSeq(seq++);
				isValid = nullCheck(errMsg, ExcelUtils.readValue(sheet, index, "C"), "영문명", isValid);
				layout.setFldEngNm(ExcelUtils.readValue(sheet, index, "C"));
				isValid = nullCheck(errMsg, ExcelUtils.readValue(sheet, index, "D"), "한글명", isValid);
				layout.setFldKorNm(ExcelUtils.readValue(sheet, index, "D"));
				String type = getCodeValueNm("DATA_TYPE", ExcelUtils.readValue(sheet, index, "F"), "ko");
				isValid = nullCheck(errMsg, type, "데이터타입", isValid);
				layout.setDataTypeNm(type);
				layout.setCfgDesc(ExcelUtils.readValue(sheet, index, "E"));
				layout.setMsgLen(Integer.parseInt(ExcelUtils.readValue(sheet, index, "G")));
				layout.setDecimalLen(Integer.parseInt(ExcelUtils.readValue(sheet, index, "H")));

				boolean isValid2 = metaCheck(layout, errMsg, data);
				if (!isValid2) {
					isValid = false;
				}

				layout.setFldLvNo(Integer.parseInt(ExcelUtils.readValue(sheet, index, "I")));
				layout.setChildDtoNm(ExcelUtils.readValue(sheet, index, "J"));
				layout.setArraySizeRefVal(ExcelUtils.readValue(sheet, index, "K"));
				layout.setIndsYn(ExcelUtils.readValue(sheet, index, "L"));
				layout.setBasicVal(ExcelUtils.readValue(sheet, index, "M"));
				layout.setPrivacyDscd(getCodeValueNm("PRIVACY_CD", ExcelUtils.readValue(sheet, index, "N"), "ko"));
				layout.setEncYn(ExcelUtils.readValue(sheet, index, "O"));
				String align = ExcelUtils.readValue(sheet, index, "P");
				if (StringUtils.isEmpty(align)) {
					if (type.equals("STRING") || type.equals("BYTEARRAY")) {
						align = "LEFT";
					} else if (type.equals("LONG") || type.equals("BIGDECIMAL")) {
						align = "RIGHT";
					} else {
						align = "";
					}
				}
				layout.setAlignNm(align);
				layout.setCdAttrNm(ExcelUtils.readValue(sheet, index, "Q"));
				String ioKey = getCodeValueNm("IO_KEY", ExcelUtils.readValue(sheet, index, "R"), "ko");
				if (ioKeyMap.containsKey(ioKey)) {
					errMsg.append("\n전문에 같은 IO Key가 존재할 수 없습니다.");
					isValid = false;
				}
				layout.setIoKey(ioKey);// IO키
				if (ioKey != null && !ioKey.equals("")) {
					ioKeyMap.put(ioKey, ioKey);
				}
				layout.setFillerVal(ExcelUtils.readValue(sheet, index, "S"));
				layout.setFldRmk(ExcelUtils.readValue(sheet, index, "T"));// 비고
				layout.setExtrnlMsgNoYn(ExcelUtils.readValue(sheet, index, "V"));
				layout.setExtrnlSrchKeyYn(ExcelUtils.readValue(sheet, index, "W"));
				layout.setExtrnlMsgNoYn(ExcelUtils.readValue(sheet, index, "Z"));
				// layout.setExtrnlOffsetYn(ExcelUtils.readValue(sheet, index, "W"));
				layout.setIso8583SpecNo(ExcelUtils.readValue(sheet, index, "AA"));
				layout.setIso8583FldCharSetCd(ExcelUtils.readValue(sheet, index, "AB"));// 필드타입
				layout.setIso8583VariableYn(ExcelUtils.readValue(sheet, index, "AC"));
				layout.setIso8583FldLenTypeCd(
						getCodeValueNm("ISO_FLD_LEN_TYPE_CD", ExcelUtils.readValue(sheet, index, "AD"), "ko"));
				layout.setIso8583LenFldInclYn(ExcelUtils.readValue(sheet, index, "AE"));
				layout.setIso8583LenFldCopyYn(ExcelUtils.readValue(sheet, index, "AF"));
				layout.setIso8583TrnsmsnDataLen(ExcelUtils.readValue(sheet, index, "AG") != null
						&& !ExcelUtils.readValue(sheet, index, "AG").equals("")
								? Integer.parseInt(ExcelUtils.readValue(sheet, index, "AG"))
								: null);
			} else if (layoutVersion.equals("v20180910")) {
				layout.setMsgLayoutId(layoutId);
				layout.setMsgSeq(seq++);
				isValid = nullCheck(errMsg, ExcelUtils.readValue(sheet, index, "C"), "영문명", isValid);
				layout.setFldEngNm(ExcelUtils.readValue(sheet, index, "C"));
				isValid = nullCheck(errMsg, ExcelUtils.readValue(sheet, index, "D"), "한글명", isValid);
				layout.setFldKorNm(ExcelUtils.readValue(sheet, index, "D"));
				String type = ExcelUtils.readValue(sheet, index, "F");
				isValid = nullCheck(errMsg, type, "데이터타입", isValid);
				layout.setDataTypeNm(type);
				layout.setCfgDesc(ExcelUtils.readValue(sheet, index, "E"));
				layout.setMsgLen(Integer.parseInt(ExcelUtils.readValue(sheet, index, "G")));
				layout.setDecimalLen(Integer.parseInt(ExcelUtils.readValue(sheet, index, "H")));

				boolean isValid2 = metaCheck(layout, errMsg, data);
				if (!isValid2) {
					isValid = false;
				}

				layout.setFldLvNo(Integer.parseInt(ExcelUtils.readValue(sheet, index, "I")));
				layout.setChildDtoNm(ExcelUtils.readValue(sheet, index, "J"));
				layout.setArraySizeRefVal(ExcelUtils.readValue(sheet, index, "K"));
				layout.setIndsYn(ExcelUtils.readValue(sheet, index, "L"));
				layout.setBasicVal(ExcelUtils.readValue(sheet, index, "M"));
				layout.setPrivacyDscd(getCodeValueNm("PRIVACY_CD", ExcelUtils.readValue(sheet, index, "N"), "ko"));
				layout.setEncYn(ExcelUtils.readValue(sheet, index, "O"));
				String align = ExcelUtils.readValue(sheet, index, "P");
				if (StringUtils.isEmpty(align)) {
					if (type.equals("STRING") || type.equals("BYTEARRAY")) {
						align = "LEFT";
					} else if (type.equals("LONG") || type.equals("BIGDECIMAL")) {
						align = "RIGHT";
					} else {
						align = "";
					}
				}
				layout.setAlignNm(align);
				layout.setCdAttrNm(ExcelUtils.readValue(sheet, index, "Q"));
				String ioKey = getCodeValueNm("IO_KEY", ExcelUtils.readValue(sheet, index, "R"), "ko");
				if (ioKeyMap.containsKey(ioKey)) {
					errMsg.append("\n전문에 같은 IO Key가 존재할 수 없습니다.");
					isValid = false;
				}
				layout.setIoKey(ioKey);// IO키
				if (ioKey != null && !ioKey.equals("")) {
					ioKeyMap.put(ioKey, ioKey);
				}
				layout.setFillerVal(ExcelUtils.readValue(sheet, index, "S"));
				layout.setFldRmk(ExcelUtils.readValue(sheet, index, "T"));// 비고
				layout.setExtrnlMsgNoYn(ExcelUtils.readValue(sheet, index, "V"));
				layout.setExtrnlSrchKeyYn(ExcelUtils.readValue(sheet, index, "W"));
				layout.setExtrnlMsgNoYn(ExcelUtils.readValue(sheet, index, "Z"));
				// layout.setExtrnlOffsetYn(ExcelUtils.readValue(sheet, index, "W"));
				layout.setIso8583SpecNo(ExcelUtils.readValue(sheet, index, "AA"));
				layout.setIso8583FldCharSetCd(ExcelUtils.readValue(sheet, index, "AB"));// 필드타입
				layout.setIso8583VariableYn(ExcelUtils.readValue(sheet, index, "AC"));
				layout.setIso8583FldLenTypeCd(
						getCodeValueNm("ISO_FLD_LEN_TYPE_CD", ExcelUtils.readValue(sheet, index, "AD"), "ko"));
				layout.setIso8583LenFldLen(ExcelUtils.readValue(sheet, index, "AE") != null
						&& !ExcelUtils.readValue(sheet, index, "AE").equals("")
								? Integer.parseInt(ExcelUtils.readValue(sheet, index, "AE"))
								: null);
				layout.setIso8583LenFldInclYn(ExcelUtils.readValue(sheet, index, "AF"));
				layout.setIso8583LenFldCopyYn(ExcelUtils.readValue(sheet, index, "AG"));
				layout.setIso8583TrnsmsnDataLen(ExcelUtils.readValue(sheet, index, "AH") != null
						&& !ExcelUtils.readValue(sheet, index, "AH").equals("")
								? Integer.parseInt(ExcelUtils.readValue(sheet, index, "AH"))
								: null);

			} else if (layoutVersion.equals("v20181011") || layoutVersion.equals("v20181016")) {
				layout.setMsgLayoutId(layoutId);
				layout.setMsgSeq(seq++);
				isValid = nullCheck(errMsg, ExcelUtils.readValue(sheet, index, "C"), "영문명", isValid);
				layout.setFldEngNm(ExcelUtils.readValue(sheet, index, "C"));
				isValid = nullCheck(errMsg, ExcelUtils.readValue(sheet, index, "D"), "한글명", isValid);
				layout.setFldKorNm(ExcelUtils.readValue(sheet, index, "D"));
				String type = getCodeValueNm("DATA_TYPE", ExcelUtils.readValue(sheet, index, "F"), "ko");
				isValid = nullCheck(errMsg, type, "데이터타입", isValid);
				layout.setDataTypeNm(type);
				layout.setCfgDesc(ExcelUtils.readValue(sheet, index, "E"));
				layout.setMsgLen(Integer.parseInt(ExcelUtils.readValue(sheet, index, "G")));
				layout.setDecimalLen(Integer.parseInt(ExcelUtils.readValue(sheet, index, "H")));

				boolean isValid2 = metaCheck(layout, errMsg, data);
				if (!isValid2) {
					isValid = false;
				}

				layout.setFldLvNo(Integer.parseInt(ExcelUtils.readValue(sheet, index, "I")));
				layout.setChildDtoNm(ExcelUtils.readValue(sheet, index, "J"));
				layout.setArraySizeRefVal(ExcelUtils.readValue(sheet, index, "K"));
				layout.setIndsYn(ExcelUtils.readValue(sheet, index, "L"));
				layout.setBasicVal(ExcelUtils.readValue(sheet, index, "M"));
				String privacyCd = getCodeValueNm("PRIVACY_CD", ExcelUtils.readValue(sheet, index, "N"), "ko");
				layout.setPrivacyDscd(getCodeValueNm("PRIVACY_CD", ExcelUtils.readValue(sheet, index, "N"), "ko"));
				// replKeyCnt
				// 개인정보식별자
				String chlType = data.getChlDscd();
				String replKey = ExcelUtils.readValue(sheet, index, "O");
				if (replKey != null && !replKey.equals("")) {

					if (replKeyCnt > 0) {
						errMsg.append("\n하나의 전문에는 하나의 개인정보식별자만 존재할 수 있습니다.");
						isValid = false;
					}

					if (chlType.equals("EXTERNAL")) {
						if (replKey.startsWith("(대내)")) {
							errMsg.append("\n대외전문에 대내 개인정보식별자를 세팅할 수 없습니다.");
							isValid = false;
						}
					} else {
						if (replKey.startsWith("(대외)")) {
							errMsg.append("\n대내전문에 대외 개인정보식별자를 세팅할 수 없습니다.");
							isValid = false;
						}
					}

					// if (isValid) {
					// if (privacyCd == null || privacyCd.equals("")) {
					// errMsg.append("\n개인정보가 존재하지 않는 컬럼에 개인정보식별자를 세팅 할 수 없습니다.");
					// isValid = false;
					// }
					// }

					if (isValid) {
						if (chlType.equals("EXTERNAL")) {
							layout.setReplKey(
									getCodeValueNm("REPLACEKEY_EXT", ExcelUtils.readValue(sheet, index, "O"), "ko"));
							replKeyCnt++;
						} else {
							layout.setReplKey(
									getCodeValueNm("REPLACEKEY_INT", ExcelUtils.readValue(sheet, index, "O"), "ko"));
							replKeyCnt++;
						}
					}
				}

				layout.setEncYn(ExcelUtils.readValue(sheet, index, "P"));
				String align = ExcelUtils.readValue(sheet, index, "Q");
				if (type.equals("LONG") || type.equals("BIGDECIMAL")) {
					align = "RIGHT";
				}
				if (StringUtils.isEmpty(align)) {
					if (type.equals("STRING") || type.equals("BYTEARRAY")) {
						align = "LEFT";
					} else if (type.equals("LONG") || type.equals("BIGDECIMAL")) {
						align = "RIGHT";
					} else {
						align = "";
					}
				}
				layout.setAlignNm(align);
				layout.setCdAttrNm(ExcelUtils.readValue(sheet, index, "R"));
				String ioKey = getCodeValueNm("IO_KEY", ExcelUtils.readValue(sheet, index, "S"), "ko");
				if (ioKeyMap.containsKey(ioKey)) {
					errMsg.append("\n전문에 같은 IO Key가 존재할 수 없습니다.");
					isValid = false;
				}
				layout.setIoKey(ioKey);// IO키
				if (ioKey != null && !ioKey.equals("")) {
					ioKeyMap.put(ioKey, ioKey);
				}
				layout.setFillerVal(ExcelUtils.readValue(sheet, index, "T"));
				layout.setFldRmk(ExcelUtils.readValue(sheet, index, "U"));// 비고
				layout.setExtrnlMsgNoYn(ExcelUtils.readValue(sheet, index, "W"));
				layout.setExtrnlSrchKeyYn(ExcelUtils.readValue(sheet, index, "X"));
				layout.setExtrnlMsgNoYn(ExcelUtils.readValue(sheet, index, "AA"));
				// layout.setExtrnlOffsetYn(ExcelUtils.readValue(sheet, index, "W"));
				layout.setIso8583SpecNo(ExcelUtils.readValue(sheet, index, "AB"));
				layout.setIso8583FldCharSetCd(ExcelUtils.readValue(sheet, index, "AC"));// 필드타입
				layout.setIso8583VariableYn(ExcelUtils.readValue(sheet, index, "AD"));
				layout.setIso8583FldLenTypeCd(
						getCodeValueNm("ISO_FLD_LEN_TYPE_CD", ExcelUtils.readValue(sheet, index, "AE"), "ko"));
				layout.setIso8583LenFldLen(ExcelUtils.readValue(sheet, index, "AF") != null
						&& !ExcelUtils.readValue(sheet, index, "AF").equals("")
								? Integer.parseInt(ExcelUtils.readValue(sheet, index, "AF"))
								: null);
				layout.setIso8583LenFldInclYn(ExcelUtils.readValue(sheet, index, "AG"));
				layout.setIso8583LenFldCopyYn(ExcelUtils.readValue(sheet, index, "AH"));
				layout.setIso8583TrnsmsnDataLen(ExcelUtils.readValue(sheet, index, "AI") != null
						&& !ExcelUtils.readValue(sheet, index, "AI").equals("")
								? Integer.parseInt(ExcelUtils.readValue(sheet, index, "AI"))
								: null);

				// if (isValid) {
				// if (layout.getDataTypeNm().equals("LAYOUT")) {
				// if (!msgDscd.equals("ISOH") && !msgDscd.equals("ISOB")) {
				// String arrayRef = layout.getArraySizeRefVal();
				// if (arrayRef == null || arrayRef.equals("")) {
				// errMsg.append("\nLAYOUT 타입의 경우 배열참조값은 필수입력 되어야 합니다. 영문명 [" +
				// layout.getFldEngNm() + "]");
				// isValid = false;
				// }
				// }
				// }
				// }

			} else if (layoutVersion.equals("v20181112")) {
				layout.setMsgLayoutId(layoutId);
				layout.setMsgSeq(seq++);
				isValid = nullCheck(errMsg, ExcelUtils.readValue(sheet, index, "C"), "영문명", isValid);
				layout.setFldEngNm(ExcelUtils.readValue(sheet, index, "C"));
				isValid = nullCheck(errMsg, ExcelUtils.readValue(sheet, index, "D"), "한글명", isValid);
				layout.setFldKorNm(ExcelUtils.readValue(sheet, index, "D"));
				String type = getCodeValueNm("DATA_TYPE", ExcelUtils.readValue(sheet, index, "F"), "ko");
				isValid = nullCheck(errMsg, type, "데이터타입", isValid);
				layout.setDataTypeNm(type);
				layout.setCfgDesc(ExcelUtils.readValue(sheet, index, "E"));
				layout.setMsgLen(Integer.parseInt(ExcelUtils.readValue(sheet, index, "G")));
				layout.setDecimalLen(Integer.parseInt(ExcelUtils.readValue(sheet, index, "H")));

				boolean isValid2 = metaCheck(layout, errMsg, data);
				if (!isValid2) {
					isValid = false;
				}

				layout.setFldLvNo(Integer.parseInt(ExcelUtils.readValue(sheet, index, "I")));
				layout.setChildDtoNm(ExcelUtils.readValue(sheet, index, "J"));
				layout.setArraySizeRefVal(ExcelUtils.readValue(sheet, index, "K"));
				layout.setIndsYn(ExcelUtils.readValue(sheet, index, "L"));
				layout.setBasicVal(ExcelUtils.readValue(sheet, index, "M"));
				String privacyCd = getCodeValueNm("PRIVACY_CD", ExcelUtils.readValue(sheet, index, "N"), "ko");
				layout.setPrivacyDscd(getCodeValueNm("PRIVACY_CD", ExcelUtils.readValue(sheet, index, "N"), "ko"));
				// replKeyCnt
				// 개인정보식별자
				String chlType = data.getChlDscd();
				String replKey = ExcelUtils.readValue(sheet, index, "O");
				if (replKey != null && !replKey.equals("")) {

					if (replKeyCnt > 0) {
						errMsg.append("\n하나의 전문에는 하나의 개인정보식별자만 존재할 수 있습니다.");
						isValid = false;
					}

					if (chlType.equals("EXTERNAL")) {
						if (replKey.startsWith("(대내)")) {
							errMsg.append("\n대외전문에 대내 개인정보식별자를 세팅할 수 없습니다.");
							isValid = false;
						}
					} else {
						if (replKey.startsWith("(대외)")) {
							errMsg.append("\n대내전문에 대외 개인정보식별자를 세팅할 수 없습니다.");
							isValid = false;
						}
					}

					// if (isValid) {
					// if (privacyCd == null || privacyCd.equals("")) {
					// errMsg.append("\n개인정보가 존재하지 않는 컬럼에 개인정보식별자를 세팅 할 수 없습니다.");
					// isValid = false;
					// }
					// }

					if (isValid) {
						if (chlType.equals("EXTERNAL")) {
							layout.setReplKey(
									getCodeValueNm("REPLACEKEY_EXT", ExcelUtils.readValue(sheet, index, "O"), "ko"));
							replKeyCnt++;
						} else {
							layout.setReplKey(
									getCodeValueNm("REPLACEKEY_INT", ExcelUtils.readValue(sheet, index, "O"), "ko"));
							replKeyCnt++;
						}
					}
				}

				layout.setEncYn(ExcelUtils.readValue(sheet, index, "P"));
				String align = ExcelUtils.readValue(sheet, index, "Q");
				if (StringUtils.isEmpty(align)) {
					if (type.equals("STRING") || type.equals("BYTEARRAY")) {
						align = "LEFT";
					} else if (type.equals("LONG") || type.equals("BIGDECIMAL")) {
						align = "RIGHT";
					} else {
						align = "";
					}
				}
				layout.setAlignNm(align);
				layout.setCdAttrNm(ExcelUtils.readValue(sheet, index, "R"));
				String ioKey = getCodeValueNm("IO_KEY", ExcelUtils.readValue(sheet, index, "S"), "ko");
				if (ioKeyMap.containsKey(ioKey)) {
					errMsg.append("\n전문에 같은 IO Key가 존재할 수 없습니다.");
					isValid = false;
				}
				layout.setIoKey(ioKey);// IO키
				if (ioKey != null && !ioKey.equals("")) {
					ioKeyMap.put(ioKey, ioKey);
				}
				// layout.setFillerVal(ExcelUtils.readValue(sheet, index, "T"));
				layout.setFldRmk(ExcelUtils.readValue(sheet, index, "U"));// 비고
				layout.setExtrnlMsgNoYn(ExcelUtils.readValue(sheet, index, "W"));
				layout.setExtrnlSrchKeyYn(ExcelUtils.readValue(sheet, index, "X"));
				layout.setExtrnlMsgNoYn(ExcelUtils.readValue(sheet, index, "AA"));
				// layout.setExtrnlOffsetYn(ExcelUtils.readValue(sheet, index, "W"));
				layout.setIso8583SpecNo(ExcelUtils.readValue(sheet, index, "AB"));
				layout.setIso8583FldCharSetCd(ExcelUtils.readValue(sheet, index, "AC"));// 필드타입
				layout.setIso8583VariableYn(ExcelUtils.readValue(sheet, index, "AD"));
				layout.setIso8583FldLenTypeCd(
						getCodeValueNm("ISO_FLD_LEN_TYPE_CD", ExcelUtils.readValue(sheet, index, "AE"), "ko"));
				layout.setIso8583LenFldLen(ExcelUtils.readValue(sheet, index, "AF") != null
						&& !ExcelUtils.readValue(sheet, index, "AF").equals("")
								? Integer.parseInt(ExcelUtils.readValue(sheet, index, "AF"))
								: null);
				layout.setIso8583LenFldInclYn(ExcelUtils.readValue(sheet, index, "AG"));
				layout.setIso8583LenFldCopyYn(ExcelUtils.readValue(sheet, index, "AH"));
				layout.setIso8583TrnsmsnDataLen(ExcelUtils.readValue(sheet, index, "AI") != null
						&& !ExcelUtils.readValue(sheet, index, "AI").equals("")
								? Integer.parseInt(ExcelUtils.readValue(sheet, index, "AI"))
								: null);

				// if (isValid) {
				// if (layout.getDataTypeNm().equals("LAYOUT")) {
				// if (!msgDscd.equals("ISOH") && !msgDscd.equals("ISOB")) {
				// String arrayRef = layout.getArraySizeRefVal();
				// if (arrayRef == null || arrayRef.equals("")) {
				// errMsg.append("\nLAYOUT 타입의 경우 배열참조값은 필수입력 되어야 합니다. 영문명 [" +
				// layout.getFldEngNm() + "]");
				// isValid = false;
				// }
				// }
				// }
				// }

			} else if (layoutVersion.equals("v20181114")) {
				layout.setMsgLayoutId(layoutId);
				layout.setMsgSeq(seq++);
				isValid = nullCheck(errMsg, ExcelUtils.readValue(sheet, index, "C"), "영문명", isValid);
				layout.setFldEngNm(ExcelUtils.readValue(sheet, index, "C"));
				isValid = nullCheck(errMsg, ExcelUtils.readValue(sheet, index, "D"), "한글명", isValid);
				layout.setFldKorNm(ExcelUtils.readValue(sheet, index, "D"));
				String type = getCodeValueNm("DATA_TYPE", ExcelUtils.readValue(sheet, index, "F"), "ko");
				isValid = nullCheck(errMsg, type, "데이터타입", isValid);
				layout.setDataTypeNm(type);
				layout.setCfgDesc(ExcelUtils.readValue(sheet, index, "E"));
				layout.setMsgLen(Integer.parseInt(ExcelUtils.readValue(sheet, index, "G")));
				layout.setDecimalLen(Integer.parseInt(ExcelUtils.readValue(sheet, index, "H")));

				boolean isValid2 = metaCheck(layout, errMsg, data);
				if (!isValid2) {
					isValid = false;
				}

				layout.setFldLvNo(Integer.parseInt(ExcelUtils.readValue(sheet, index, "I")));
				layout.setChildDtoNm(ExcelUtils.readValue(sheet, index, "J"));
				layout.setArraySizeRefVal(ExcelUtils.readValue(sheet, index, "K"));
				layout.setIndsYn(ExcelUtils.readValue(sheet, index, "L"));
				layout.setBasicVal(ExcelUtils.readValue(sheet, index, "M"));
				String privacyCd = getCodeValueNm("PRIVACY_CD", ExcelUtils.readValue(sheet, index, "N"), "ko");
				layout.setPrivacyDscd(getCodeValueNm("PRIVACY_CD", ExcelUtils.readValue(sheet, index, "N"), "ko"));
				// replKeyCnt
				// 개인정보식별자
				String chlType = data.getChlDscd();
				String replKey = ExcelUtils.readValue(sheet, index, "O");
				if (replKey != null && !replKey.equals("")) {

					if (replKeyCnt > 0) {
						errMsg.append("\n하나의 전문에는 하나의 개인정보식별자만 존재할 수 있습니다.");
						isValid = false;
					}

					if (chlType.equals("EXTERNAL")) {
						if (replKey.startsWith("(대내)")) {
							errMsg.append("\n대외전문에 대내 개인정보식별자를 세팅할 수 없습니다.");
							isValid = false;
						}
					} else {
						if (replKey.startsWith("(대외)")) {
							errMsg.append("\n대내전문에 대외 개인정보식별자를 세팅할 수 없습니다.");
							isValid = false;
						}
					}

					// if (isValid) {
					// if (privacyCd == null || privacyCd.equals("")) {
					// errMsg.append("\n개인정보가 존재하지 않는 컬럼에 개인정보식별자를 세팅 할 수 없습니다.");
					// isValid = false;
					// }
					// }

					if (isValid) {
						if (chlType.equals("EXTERNAL")) {
							layout.setReplKey(
									getCodeValueNm("REPLACEKEY_EXT", ExcelUtils.readValue(sheet, index, "O"), "ko"));
							replKeyCnt++;
						} else {
							layout.setReplKey(
									getCodeValueNm("REPLACEKEY_INT", ExcelUtils.readValue(sheet, index, "O"), "ko"));
							replKeyCnt++;
						}
					}
				}

				layout.setEncYn(ExcelUtils.readValue(sheet, index, "P"));
				String align = ExcelUtils.readValue(sheet, index, "Q");
				if (StringUtils.isEmpty(align)) {
					if (type.equals("STRING") || type.equals("BYTEARRAY")) {
						align = "LEFT";
					} else if (type.equals("LONG") || type.equals("BIGDECIMAL")) {
						align = "RIGHT";
					} else {
						align = "";
					}
				}
				layout.setAlignNm(align);
				layout.setCdAttrNm(ExcelUtils.readValue(sheet, index, "R"));
				String ioKey = getCodeValueNm("IO_KEY", ExcelUtils.readValue(sheet, index, "S"), "ko");
				if (ioKeyMap.containsKey(ioKey)) {
					errMsg.append("\n전문에 같은 IO Key가 존재할 수 없습니다.");
					isValid = false;
				}
				layout.setIoKey(ioKey);// IO키
				if (ioKey != null && !ioKey.equals("")) {
					ioKeyMap.put(ioKey, ioKey);
				}
				String korInclYn = ExcelUtils.readValue(sheet, index, "T");
				if (korInclYn == null || korInclYn.equals("")) {
					korInclYn = "N";
				}
				layout.setKorInclYn(korInclYn);
				// layout.setFillerVal(ExcelUtils.readValue(sheet, index, "T"));
				layout.setFldRmk(ExcelUtils.readValue(sheet, index, "U"));// 비고
				layout.setExtrnlMsgNoYn(ExcelUtils.readValue(sheet, index, "W"));
				layout.setExtrnlSrchKeyYn(ExcelUtils.readValue(sheet, index, "X"));
				layout.setExtrnlMsgNoYn(ExcelUtils.readValue(sheet, index, "AA"));
				// layout.setExtrnlOffsetYn(ExcelUtils.readValue(sheet, index, "W"));
				layout.setIso8583SpecNo(ExcelUtils.readValue(sheet, index, "AB"));
				layout.setIso8583FldCharSetCd(ExcelUtils.readValue(sheet, index, "AC"));// 필드타입
				layout.setIso8583VariableYn(ExcelUtils.readValue(sheet, index, "AD"));
				layout.setIso8583FldLenTypeCd(
						getCodeValueNm("ISO_FLD_LEN_TYPE_CD", ExcelUtils.readValue(sheet, index, "AE"), "ko"));
				layout.setIso8583LenFldLen(ExcelUtils.readValue(sheet, index, "AF") != null
						&& !ExcelUtils.readValue(sheet, index, "AF").equals("")
								? Integer.parseInt(ExcelUtils.readValue(sheet, index, "AF"))
								: null);
				layout.setIso8583LenFldInclYn(ExcelUtils.readValue(sheet, index, "AG"));
				layout.setIso8583LenFldCopyYn(ExcelUtils.readValue(sheet, index, "AH"));
				layout.setIso8583TrnsmsnDataLen(ExcelUtils.readValue(sheet, index, "AI") != null
						&& !ExcelUtils.readValue(sheet, index, "AI").equals("")
								? Integer.parseInt(ExcelUtils.readValue(sheet, index, "AI"))
								: null);

				// if (isValid) {
				// if (layout.getDataTypeNm().equals("LAYOUT")) {
				// if (!msgDscd.equals("ISOH") && !msgDscd.equals("ISOB")) {
				// String arrayRef = layout.getArraySizeRefVal();
				// if (arrayRef == null || arrayRef.equals("")) {
				// errMsg.append("\nLAYOUT 타입의 경우 배열참조값은 필수입력 되어야 합니다. 영문명 [" +
				// layout.getFldEngNm() + "]");
				// isValid = false;
				// }
				// }
				// }
				// }

			} else if (layoutVersion.equals("v20181130")) {
				layout.setMsgLayoutId(layoutId);
				layout.setMsgSeq(seq++);
				isValid = nullCheck(errMsg, ExcelUtils.readValue(sheet, index, "C"), "영문명", isValid);
				layout.setFldEngNm(ExcelUtils.readValue(sheet, index, "C"));
				isValid = nullCheck(errMsg, ExcelUtils.readValue(sheet, index, "D"), "한글명", isValid);
				layout.setFldKorNm(ExcelUtils.readValue(sheet, index, "D"));
				String type = getCodeValueNm("DATA_TYPE", ExcelUtils.readValue(sheet, index, "F"), "ko");
				isValid = nullCheck(errMsg, type, "데이터타입", isValid);
				layout.setDataTypeNm(type);
				layout.setCfgDesc(ExcelUtils.readValue(sheet, index, "E"));
				layout.setMsgLen(Integer.parseInt(ExcelUtils.readValue(sheet, index, "G")));
				layout.setDecimalLen(Integer.parseInt(ExcelUtils.readValue(sheet, index, "H")));

				boolean isValid2 = metaCheck(layout, errMsg, data);
				if (!isValid2) {
					isValid = false;
				}

				layout.setFldLvNo(Integer.parseInt(ExcelUtils.readValue(sheet, index, "I")));

				layout.setIndsYn(ExcelUtils.readValue(sheet, index, "J"));// 필수여부J
				layout.setFldRmk(ExcelUtils.readValue(sheet, index, "K"));// 비고K
				layout.setCdAttrNm(ExcelUtils.readValue(sheet, index, "L"));// 코드속성L
				layout.setArraySizeRefVal(ExcelUtils.readValue(sheet, index, "M"));// 배열참조M
				layout.setPrivacyDscd(getCodeValueNm("PRIVACY_CD", ExcelUtils.readValue(sheet, index, "N"), "ko"));// 개인정보N
				String chlType = data.getChlDscd();
				String replKey = ExcelUtils.readValue(sheet, index, "O");
				if (replKey != null && !replKey.equals("")) {

					if (replKeyCnt > 0) {
						errMsg.append("\n하나의 전문에는 하나의 개인정보식별자만 존재할 수 있습니다.");
						isValid = false;
					}

					if (chlType.equals("EXTERNAL")) {
						if (replKey.startsWith("(대내)")) {
							errMsg.append("\n대외전문에 대내 개인정보식별자를 세팅할 수 없습니다.");
							isValid = false;
						}
					} else {
						if (replKey.startsWith("(대외)")) {
							errMsg.append("\n대내전문에 대외 개인정보식별자를 세팅할 수 없습니다.");
							isValid = false;
						}
					}

					if (isValid) {
						if (chlType.equals("EXTERNAL")) {
							layout.setReplKey(
									getCodeValueNm("REPLACEKEY_EXT", ExcelUtils.readValue(sheet, index, "O"), "ko"));
							replKeyCnt++;
						} else {
							layout.setReplKey(
									getCodeValueNm("REPLACEKEY_INT", ExcelUtils.readValue(sheet, index, "O"), "ko"));
							replKeyCnt++;
						}
					}
				} // 개인정보식별자O
				layout.setEncYn(ExcelUtils.readValue(sheet, index, "P"));// 암호화여부P
				String align = ExcelUtils.readValue(sheet, index, "Q");
				if (StringUtils.isEmpty(align)) {
					if (type.equals("STRING") || type.equals("BYTEARRAY")) {
						align = "LEFT";
					} else if (type.equals("LONG") || type.equals("BIGDECIMAL")) {
						align = "RIGHT";
					} else {
						align = "";
					}
				}
				layout.setAlignNm(align);// 정렬Q
				layout.setBasicVal(ExcelUtils.readValue(sheet, index, "R"));// 기본값R
				String korInclYn = ExcelUtils.readValue(sheet, index, "S");
				if (korInclYn == null || korInclYn.equals("")) {
					korInclYn = "N";
				}
				layout.setKorInclYn(korInclYn);// 한글포함여부S
				String ioKey = getCodeValueNm("IO_KEY", ExcelUtils.readValue(sheet, index, "T"), "ko");
				if (ioKeyMap.containsKey(ioKey)) {
					errMsg.append("\n전문에 같은 IO Key가 존재할 수 없습니다.");
					isValid = false;
				}
				layout.setIoKey(ioKey);
				if (ioKey != null && !ioKey.equals("")) {
					ioKeyMap.put(ioKey, ioKey);
				} // IO키T
				layout.setChildDtoNm(ExcelUtils.readValue(sheet, index, "U"));// 하위IO명U

				layout.setExtrnlMsgNoYn(ExcelUtils.readValue(sheet, index, "W"));
				layout.setExtrnlSrchKeyYn(ExcelUtils.readValue(sheet, index, "X"));
				layout.setExtrnlMsgNoYn(ExcelUtils.readValue(sheet, index, "AA"));
				layout.setIso8583SpecNo(ExcelUtils.readValue(sheet, index, "AB"));
				layout.setIso8583FldCharSetCd(ExcelUtils.readValue(sheet, index, "AC"));// 필드타입
				layout.setIso8583VariableYn(ExcelUtils.readValue(sheet, index, "AD"));
				layout.setIso8583FldLenTypeCd(
						getCodeValueNm("ISO_FLD_LEN_TYPE_CD", ExcelUtils.readValue(sheet, index, "AE"), "ko"));
				layout.setIso8583LenFldLen(ExcelUtils.readValue(sheet, index, "AF") != null
						&& !ExcelUtils.readValue(sheet, index, "AF").equals("")
								? Integer.parseInt(ExcelUtils.readValue(sheet, index, "AF"))
								: null);
				layout.setIso8583LenFldInclYn(ExcelUtils.readValue(sheet, index, "AG"));
				layout.setIso8583LenFldCopyYn(ExcelUtils.readValue(sheet, index, "AH"));
				layout.setIso8583TrnsmsnDataLen(ExcelUtils.readValue(sheet, index, "AI") != null
						&& !ExcelUtils.readValue(sheet, index, "AI").equals("")
								? Integer.parseInt(ExcelUtils.readValue(sheet, index, "AI"))
								: null);

				// if (isValid) {
				// if (layout.getDataTypeNm().equals("LAYOUT")) {
				// if (!msgDscd.equals("ISOH") && !msgDscd.equals("ISOB")) {
				// String arrayRef = layout.getArraySizeRefVal();
				// if (arrayRef == null || arrayRef.equals("")) {
				// errMsg.append("\nLAYOUT 타입의 경우 배열참조값은 필수입력 되어야 합니다. 영문명 [" +
				// layout.getFldEngNm() + "]");
				// isValid = false;
				// }
				// }
				// }
				// }

			} else if (layoutVersion.equals("v20181211")) {
				layout.setMsgLayoutId(layoutId);
				layout.setMsgSeq(seq++);
				isValid = nullCheck(errMsg, ExcelUtils.readValue(sheet, index, "C"), "영문명", isValid);
				layout.setFldEngNm(ExcelUtils.readValue(sheet, index, "C"));
				isValid = nullCheck(errMsg, ExcelUtils.readValue(sheet, index, "D"), "한글명", isValid);
				layout.setFldKorNm(ExcelUtils.readValue(sheet, index, "D"));
				String type = getCodeValueNm("DATA_TYPE", ExcelUtils.readValue(sheet, index, "F"), "ko");
				isValid = nullCheck(errMsg, type, "데이터타입", isValid);
				layout.setDataTypeNm(type);
				layout.setCfgDesc(ExcelUtils.readValue(sheet, index, "E"));
				layout.setMsgLen(Integer.parseInt(ExcelUtils.readValue(sheet, index, "G")));
				layout.setDecimalLen(Integer.parseInt(ExcelUtils.readValue(sheet, index, "H")));

				boolean isValid2 = metaCheck(layout, errMsg, data);
				if (!isValid2) {
					isValid = false;
				}

				layout.setFldLvNo(Integer.parseInt(ExcelUtils.readValue(sheet, index, "I")));

				layout.setIndsYn(ExcelUtils.readValue(sheet, index, "J"));// 필수여부J
				layout.setFldRmk(ExcelUtils.readValue(sheet, index, "K"));// 비고K
				layout.setCdAttrNm(ExcelUtils.readValue(sheet, index, "L"));// 코드속성L
				layout.setArraySizeRefVal(ExcelUtils.readValue(sheet, index, "M"));// 배열참조M
				layout.setPrivacyDscd(getCodeValueNm("PRIVACY_CD", ExcelUtils.readValue(sheet, index, "N"), "ko"));// 개인정보N
				String chlType = data.getChlDscd();
				String replKey = ExcelUtils.readValue(sheet, index, "O");
				if (replKey != null && !replKey.equals("")) {

					if (replKeyCnt > 0) {
						errMsg.append("\n하나의 전문에는 하나의 개인정보식별자만 존재할 수 있습니다.");
						isValid = false;
					}

					if (chlType.equals("EXTERNAL")) {
						if (replKey.equals("고객번호") || replKey.equals("대체카드번호") || replKey.equals("대체계좌번호")) {
							errMsg.append("\n대외전문에 대내 개인정보식별자를 세팅할 수 없습니다. 고객번호, 대체카드번호, 대체계좌번호는 대내전문 개인정보식별자입니다.");
							isValid = false;
						}
					}

					if (isValid) {
						if (chlType.equals("EXTERNAL")) {
							layout.setReplKey(
									getCodeValueNm("REPLACEKEY_EXT", ExcelUtils.readValue(sheet, index, "O"), "ko"));
							replKeyCnt++;
						} else {
							layout.setReplKey(
									getCodeValueNm("REPLACEKEY_INT", ExcelUtils.readValue(sheet, index, "O"), "ko"));
							replKeyCnt++;
						}
					}
				} // 개인정보식별자O
				layout.setEncYn(ExcelUtils.readValue(sheet, index, "P"));// 암호화여부P
				String align = ExcelUtils.readValue(sheet, index, "Q");
				if (StringUtils.isEmpty(align)) {
					if (type.equals("STRING") || type.equals("BYTEARRAY")) {
						align = "LEFT";
					} else if (type.equals("LONG") || type.equals("BIGDECIMAL")) {
						align = "RIGHT";
					} else {
						align = "";
					}
				}
				layout.setAlignNm(align);// 정렬Q
				layout.setBasicVal(ExcelUtils.readValue(sheet, index, "R"));// 기본값R
				String korInclYn = ExcelUtils.readValue(sheet, index, "S");
				if (korInclYn == null || korInclYn.equals("")) {
					korInclYn = "N";
				}
				layout.setKorInclYn(korInclYn);// 한글포함여부S
				String ioKey = getCodeValueNm("IO_KEY", ExcelUtils.readValue(sheet, index, "T"), "ko");
				if (ioKeyMap.containsKey(ioKey)) {
					errMsg.append("\n전문에 같은 IO Key가 존재할 수 없습니다.");
					isValid = false;
				}
				layout.setIoKey(ioKey);
				if (ioKey != null && !ioKey.equals("")) {
					ioKeyMap.put(ioKey, ioKey);
				} // IO키T
				layout.setChildDtoNm(ExcelUtils.readValue(sheet, index, "U"));// 하위IO명U

				layout.setExtrnlMsgNoYn(ExcelUtils.readValue(sheet, index, "W"));
				layout.setExtrnlSrchKeyYn(ExcelUtils.readValue(sheet, index, "X"));
				layout.setExtrnlMsgNoYn(ExcelUtils.readValue(sheet, index, "AA"));
				layout.setIso8583SpecNo(ExcelUtils.readValue(sheet, index, "AB"));
				layout.setIso8583FldCharSetCd(ExcelUtils.readValue(sheet, index, "AC"));// 필드타입
				layout.setIso8583VariableYn(ExcelUtils.readValue(sheet, index, "AD"));
				layout.setIso8583FldLenTypeCd(
						getCodeValueNm("ISO_FLD_LEN_TYPE_CD", ExcelUtils.readValue(sheet, index, "AE"), "ko"));
				layout.setIso8583LenFldLen(ExcelUtils.readValue(sheet, index, "AF") != null
						&& !ExcelUtils.readValue(sheet, index, "AF").equals("")
								? Integer.parseInt(ExcelUtils.readValue(sheet, index, "AF"))
								: null);
				layout.setIso8583LenFldInclYn(ExcelUtils.readValue(sheet, index, "AG"));
				layout.setIso8583LenFldCopyYn(ExcelUtils.readValue(sheet, index, "AH"));
				layout.setIso8583FldMaxLen(ExcelUtils.readValue(sheet, index, "AI") != null
						&& !ExcelUtils.readValue(sheet, index, "AI").equals("")
								? Integer.parseInt(ExcelUtils.readValue(sheet, index, "AI"))
								: null);
				layout.setIso8583TrnsmsnDataLen(ExcelUtils.readValue(sheet, index, "AJ") != null
						&& !ExcelUtils.readValue(sheet, index, "AJ").equals("")
								? Integer.parseInt(ExcelUtils.readValue(sheet, index, "AJ"))
								: null);

				// if (isValid) {
				// if (layout.getDataTypeNm().equals("LAYOUT")) {
				// if (!msgDscd.equals("ISOH") && !msgDscd.equals("ISOB")) {
				// String arrayRef = layout.getArraySizeRefVal();
				// if (arrayRef == null || arrayRef.equals("")) {
				// errMsg.append("\nLAYOUT 타입의 경우 배열참조값은 필수입력 되어야 합니다. 영문명 [" +
				// layout.getFldEngNm() + "]");
				// isValid = false;
				// }
				// }
				// }
				// }

			} else if (layoutVersion.equals("v20181226") || layoutVersion.equals("v20190109")) {
				logger.debug("77777777777777777777777777777777777777777");
				layout.setMsgLayoutId(layoutId);
				layout.setMsgSeq(seq++);
				isValid = nullCheck(errMsg, ExcelUtils.readValue(sheet, index, "C"), "영문명", isValid);
				layout.setFldEngNm(ExcelUtils.readValue(sheet, index, "C"));
				isValid = nullCheck(errMsg, ExcelUtils.readValue(sheet, index, "D"), "한글명", isValid);
				layout.setFldKorNm(ExcelUtils.readValue(sheet, index, "D"));
				String type = getCodeValueNm("DATA_TYPE", ExcelUtils.readValue(sheet, index, "F"), "ko");
				isValid = nullCheck(errMsg, type, "데이터타입", isValid);
				layout.setDataTypeNm(type);
				layout.setCfgDesc(ExcelUtils.readValue(sheet, index, "E"));
				layout.setMsgLen(Integer.parseInt(ExcelUtils.readValue(sheet, index, "G")));
				layout.setDecimalLen(Integer.parseInt(ExcelUtils.readValue(sheet, index, "H")));

				boolean isValid2 = metaCheck(layout, errMsg, data);
				if (!isValid2) {
					isValid = false;
				}

				layout.setFldLvNo(Integer.parseInt(ExcelUtils.readValue(sheet, index, "I")));

				layout.setIndsYn(ExcelUtils.readValue(sheet, index, "J"));// 필수여부J
				layout.setFldRmk(ExcelUtils.readValue(sheet, index, "K"));// 비고K
				layout.setCdAttrNm(ExcelUtils.readValue(sheet, index, "L"));// 코드속성L
				layout.setArraySizeRefVal(ExcelUtils.readValue(sheet, index, "M"));// 배열참조M
				layout.setPrivacyDscd(getCodeValueNm("PRIVACY_CD", ExcelUtils.readValue(sheet, index, "N"), "ko"));// 개인정보N
				String chlType = data.getChlDscd();
				String replKey = ExcelUtils.readValue(sheet, index, "O");
				if (replKey != null && !replKey.equals("")) {

					if (replKeyCnt > 0) {
						errMsg.append("\n하나의 전문에는 하나의 개인정보식별자만 존재할 수 있습니다.");
						isValid = false;
					}

					if (chlType.equals("EXTERNAL")) {
						if (replKey.equals("고객번호") || replKey.equals("대체카드번호") || replKey.equals("대체계좌번호")) {
							errMsg.append("\n대외전문에 대내 개인정보식별자를 세팅할 수 없습니다. 고객번호, 대체카드번호, 대체계좌번호는 대내전문 개인정보식별자입니다.");
							isValid = false;
						}
					}

					if (isValid) {
						if (chlType.equals("EXTERNAL")) {
							layout.setReplKey(
									getCodeValueNm("REPLACEKEY_EXT", ExcelUtils.readValue(sheet, index, "O"), "ko"));
							replKeyCnt++;
						} else {
							layout.setReplKey(
									getCodeValueNm("REPLACEKEY_INT", ExcelUtils.readValue(sheet, index, "O"), "ko"));
							replKeyCnt++;
						}
					}
				} // 개인정보식별자O
				layout.setEncYn(ExcelUtils.readValue(sheet, index, "P"));// 암호화여부P
				String align = ExcelUtils.readValue(sheet, index, "Q");
				if (StringUtils.isEmpty(align)) {
					if (type.equals("STRING") || type.equals("BYTEARRAY")) {
						align = "LEFT";
					} else if (type.equals("LONG") || type.equals("BIGDECIMAL")) {
						align = "RIGHT";
					} else {
						align = "";
					}
				}
				layout.setAlignNm(align);// 정렬Q
				layout.setBasicVal(ExcelUtils.readValue(sheet, index, "R"));// 기본값R
				String korInclYn = ExcelUtils.readValue(sheet, index, "S");
				if (korInclYn == null || korInclYn.equals("")) {
					korInclYn = "N";
				}
				layout.setKorInclYn(korInclYn);// 한글포함여부S
				String ioKey = getCodeValueNm("IO_KEY", ExcelUtils.readValue(sheet, index, "T"), "ko");
				if (ioKeyMap.containsKey(ioKey)) {
					errMsg.append("\n전문에 같은 IO Key가 존재할 수 없습니다.");
					isValid = false;
				}
				layout.setIoKey(ioKey);
				if (ioKey != null && !ioKey.equals("")) {
					ioKeyMap.put(ioKey, ioKey);
				} // IO키T
				layout.setChildDtoNm(ExcelUtils.readValue(sheet, index, "U"));// 하위IO명U
				layout.setFillerVal(getCodeValueNm("FILLER_CD", ExcelUtils.readValue(sheet, index, "V"), "ko"));// Filler

				if (data.getMsgDscd().equals("CH")) {
					layout.setExtrnlMsgNoYn(ExcelUtils.readValue(sheet, index, "X"));
				} else if (data.getMsgDscd().equals("ISOH") || data.getMsgDscd().equals("ISOB")) {
					layout.setExtrnlMsgNoYn(ExcelUtils.readValue(sheet, index, "AB"));
				}
				layout.setExtrnlSrchKeyYn(ExcelUtils.readValue(sheet, index, "Y"));
				layout.setIso8583SpecNo(ExcelUtils.readValue(sheet, index, "AC"));
				layout.setIso8583FldCharSetCd(ExcelUtils.readValue(sheet, index, "AD"));// 필드타입
				layout.setIso8583VariableYn(ExcelUtils.readValue(sheet, index, "AE"));
				layout.setIso8583FldLenTypeCd(
						getCodeValueNm("ISO_FLD_LEN_TYPE_CD", ExcelUtils.readValue(sheet, index, "AF"), "ko"));
				layout.setIso8583LenFldLen(ExcelUtils.readValue(sheet, index, "AG") != null
						&& !ExcelUtils.readValue(sheet, index, "AG").equals("")
								? Integer.parseInt(ExcelUtils.readValue(sheet, index, "AG"))
								: null);
				layout.setIso8583LenFldInclYn(ExcelUtils.readValue(sheet, index, "AH"));
				layout.setIso8583LenFldCopyYn(ExcelUtils.readValue(sheet, index, "AI"));
				layout.setIso8583FldMaxLen(ExcelUtils.readValue(sheet, index, "AJ") != null
						&& !ExcelUtils.readValue(sheet, index, "AJ").equals("")
								? Integer.parseInt(ExcelUtils.readValue(sheet, index, "AJ"))
								: null);
				layout.setIso8583TrnsmsnDataLen(ExcelUtils.readValue(sheet, index, "AK") != null
						&& !ExcelUtils.readValue(sheet, index, "AK").equals("")
								? Integer.parseInt(ExcelUtils.readValue(sheet, index, "AK"))
								: null);

				// if (isValid) {
				// if (layout.getDataTypeNm().equals("LAYOUT")) {
				// if (!msgDscd.equals("ISOH") && !msgDscd.equals("ISOB")) {
				// String arrayRef = layout.getArraySizeRefVal();
				// if (arrayRef == null || arrayRef.equals("")) {
				// errMsg.append("\nLAYOUT 타입의 경우 배열참조값은 필수입력 되어야 합니다. 영문명 [" +
				// layout.getFldEngNm() + "]");
				// isValid = false;
				// }
				// }
				// }
				// }

			}

			if (layout.getFldLvNo() == 0) {
				if (parentsList.size() > 1) {
					parentsList.clear();
					parentsList.add(layoutId);
				}
				layout.setFldUnqId(layoutId + "." + layout.getFldEngNm());
			}
			if (layout.getDataTypeNm().equals(BxCode.DataType.LAYOUT.toString())) {
				parentsList.add(layout.getFldEngNm());
			}

			int depth = layout.getFldLvNo();

			if (layout.getFldLvNo() == 0) {
				layout.setParentFldNm("");
				layout.setFldUnqId(layoutId + "." + layout.getFldEngNm());
			} else {
				StringBuilder parentsIds = new StringBuilder();
				for (int i = 0; i < depth + 1; i++) {
					parentsIds.append(parentsList.get(i));
					if (i != depth) {
						parentsIds.append(".");
					}
				}
				layout.setParentFldNm(parentsIds.toString());
				layout.setFldUnqId(parentsIds.toString() + "." + layout.getFldEngNm());
			}
			layoutList.add(layout);
			layoutValList.add(layout.getFldUnqId() + "@" + layout.getMsgSeq());
			if (layout.getDataTypeNm().equals(BxCode.DataType.LAYOUT.toString())) {
				layoutTypeMap.put(layout.getFldUnqId(), layout);
			}
		}

		Map<String, String> validMap = new HashMap<String, String>();

		for (int i = 0; i < layoutValList.size(); i++) {
			String iString = layoutValList.get(i).substring(0, layoutValList.get(i).indexOf("@"));
			if (validMap.containsKey(iString)) {
				continue;
			}

			for (int j = 0; j < layoutValList.size(); j++) {
				String jString = layoutValList.get(j).substring(0, layoutValList.get(j).indexOf("@"));
				String iIndex = layoutValList.get(i).split("@")[1];
				String jIndex = layoutValList.get(j).split("@")[1];
				if (i == j) {
					logger.debug("logger");
				} else if (iString.equals(jString)) {
					errMsg.append(iIndex + "번째 영문명 " + iString + "과 " + jIndex + "번째 영문명 " + jString + "이 중복됩니다.");
					validMap.put(iString, jString);
					isValid = false;
				}
			}
		}

		if (!isValid) {
			errMsg.append("\n시트명: " + sheet.getSheetName());
			throw new ServiceException(BxMessages.Error.INTRFC_VALID_CHECK, errMsg.toString());
		}

		int msgTotLength = 0;
		int result = 0;
		for (MsglayoutdtDto dto : layoutList) {
			// logger.debug("****msgLenTotal***** : {}", msgTotLength);
			if (!dto.getDataTypeNm().equals("LAYOUT")) {
				msgTotLength = getMsgTotalLen(msgTotLength, dto.getMsgLen(), dto, layoutTypeMap, result);
			}

		}

		data.setRsrvFldVal3(Integer.toString(msgTotLength));
		data.setMsglayoutdtDto(layoutList);
		return index;
	}

	public boolean metaCheck(MsglayoutdtDto layout, StringBuilder errMsg, MsglayoutbsDto data) {

		boolean isValid = true;
		String msgDscd = data.getMsgDscd();
		String chlDscd = data.getChlDscd();
		String cusApiYn = data.getCustApiYn();

		if (msgDscd.equals("IV") && chlDscd.equals("INTERNAL")) {
			if (cusApiYn != null && cusApiYn.equals("Y")) {
				return isValid;
			}
		}

		if (!layout.getDataTypeNm().equals("LAYOUT")) {

			if (!layout.getFldKorNm().equals("") && layout.getFldKorNm() != null) {

				MetabsDto metabsDto = metabsDao.selectMetabs(layout.getFldKorNm());

				if (metabsDto == null) {

					isValid = metaEndNumCheck(layout, layout.getFldKorNm());

					if (!isValid) {
						errMsg.append(
								"\n필드 한글명 [ " + layout.getFldKorNm() + " ]에 대한 메타정보가 존재하지 않습니다. 메타정보를 다시 확인하여 주세요.");
						isValid = false;
					}
				} else {

					String dataType = layout.getDataTypeNm();

					if (dataType.equals("BYTEARRAY")) {
						if (!metabsDto.getMetaEngNm().equals(layout.getFldEngNm())
								|| !metabsDto.getMetaKorNm().equals(layout.getFldKorNm())
								|| !metabsDto.getDataTypeNm().equals(layout.getDataTypeNm())) {
							errMsg.append("\n메타 불일치가 존재합니다. 메타정보를 확인하여주세요.");
							errMsg.append("\n *설계서 메타정보 - 영문명 [" + layout.getFldEngNm() + "]  한글명 ["
									+ layout.getFldKorNm() + "]  타입 [" + layout.getDataTypeNm() + "]  길이 ["
									+ layout.getMsgLen() + "]  소수점 [" + layout.getDecimalLen() + "]");
							errMsg.append("\n *동기화된 메타정보 - 영문명 [" + metabsDto.getMetaEngNm() + "]  한글명 ["
									+ metabsDto.getMetaKorNm() + "]  타입 [" + metabsDto.getDataTypeNm() + "]  길이 ["
									+ metabsDto.getMetaLen() + "]  소수점 [" + metabsDto.getDecimalLen() + "]");
							isValid = false;
						}
					} else {
						if (!metabsDto.getMetaEngNm().equals(layout.getFldEngNm())
								|| !metabsDto.getMetaKorNm().equals(layout.getFldKorNm())
								|| !metabsDto.getDataTypeNm().equals(layout.getDataTypeNm())
								|| metabsDto.getMetaLen() != layout.getMsgLen()
								|| metabsDto.getDecimalLen() != layout.getDecimalLen()) {
							errMsg.append("\n메타 불일치가 존재합니다. 메타정보를 확인하여주세요.");
							errMsg.append("\n *설계서 메타정보 - 영문명 [" + layout.getFldEngNm() + "]  한글명 ["
									+ layout.getFldKorNm() + "]  타입 [" + layout.getDataTypeNm() + "]  길이 ["
									+ layout.getMsgLen() + "]  소수점 [" + layout.getDecimalLen() + "]");
							errMsg.append("\n *동기화된 메타정보 - 영문명 [" + metabsDto.getMetaEngNm() + "]  한글명 ["
									+ metabsDto.getMetaKorNm() + "]  타입 [" + metabsDto.getDataTypeNm() + "]  길이 ["
									+ metabsDto.getMetaLen() + "]  소수점 [" + metabsDto.getDecimalLen() + "]");
							isValid = false;
						}
					}

				}

			}
		}

		return isValid;
	}

	public boolean metaEndNumCheck(MsglayoutdtDto layout, String metaKorNm) {
		boolean isValid = true;

		int alen = metaKorNm.length();
		String lastIndexStr = "";
		String metaName = "";

		for (int i = 0; i < alen; i++) {
			String str = metaKorNm.substring(alen - i - 1);
			boolean b = isInteger(str);
			if (b) {
				lastIndexStr = str;
			} else {
				metaName = metaKorNm.substring(0, metaKorNm.lastIndexOf(lastIndexStr));
				break;
			}
		}

		if (lastIndexStr.equals("")) {
			return false;
		}
		if (metaName == null || metaName.equals("")) {
			return false;
		}

		MetabsDto metabsDto = metabsDao.selectMetabs(metaName);

		if (metabsDto == null) {
			return false;
		}

		String metaEngNm = layout.getFldEngNm();

		int englen = metaEngNm.length();
		String lastIndexStrEng = "";
		String metaEngNameResult = "";

		for (int i = 0; i < alen; i++) {
			String str = metaEngNm.substring(englen - i - 1);
			boolean b = isInteger(str);
			if (b) {
				lastIndexStrEng = str;
			} else {
				metaEngNameResult = metaEngNm.substring(0, metaEngNm.lastIndexOf(lastIndexStrEng));
				break;
			}
		}

		if (lastIndexStrEng.equals("")) {
			return false;
		}
		if (metaEngNameResult == null || metaEngNameResult.equals("")) {
			return false;
		}
		if (!lastIndexStr.equals(lastIndexStrEng)) {
			return false;
		}

		if (!metabsDto.getMetaEngNm().equals(metaEngNameResult) || !metabsDto.getMetaKorNm().equals(metaName)
				|| !metabsDto.getDataTypeNm().equals(layout.getDataTypeNm())
				|| metabsDto.getMetaLen() != layout.getMsgLen()
				|| metabsDto.getDecimalLen() != layout.getDecimalLen()) {
			return false;
		}

		return true;
	}

	public int getMultiplyNum(String parentDtoNm) {

		return 0;

	}

	public int getMsgTotalLen(int msgTotLength, int msgLen, MsglayoutdtDto dto,
			Map<String, MsglayoutdtDto> layoutTypeMap, int result) {

		int fldDepth = dto.getFldLvNo();
		String parentDtoNm = dto.getParentFldNm();
		int multiply = 0;
		int msgLenData = msgLen;

		if (fldDepth > 0) {
			for (int i = 0; i < fldDepth; i++) {
				MsglayoutdtDto msgDto = layoutTypeMap.get(parentDtoNm);
				if (isInteger(msgDto.getArraySizeRefVal())) {
					multiply = Integer.parseInt(msgDto.getArraySizeRefVal());
				} else {
					multiply = 1;
				}
				msgLenData = msgLenData * multiply;

				if (msgDto.getParentFldNm() != null && !msgDto.getParentFldNm().equals("")) {
					parentDtoNm = msgDto.getParentFldNm();
				}
			}
			msgTotLength = msgTotLength + msgLenData;
			return msgTotLength;
		} else {
			msgTotLength = msgTotLength + msgLen;
			return msgTotLength;
		}

		// if (dto.getFldLvNo() > 0 || dto.getDataTypeNm().equals("LAYOUT")) {
		// int multipl = 0;
		// MsglayoutdtDto msgDto = layoutTypeMap.get(dto.getParentFldNm());
		// String arraySize = msgDto.getArraySizeRefVal();
		// boolean isInt = isInteger(arraySize);
		//
		// if (isInt) {
		// multipl = Integer.parseInt(arraySize);
		// } else {
		// multipl = 1;
		// }
		// msgLen = msgLen * multipl;
		//// msgTotLength = msgTotLength + msgLen;
		//
		// if (dto.getFldLvNo() > 1) {
		// msgLen = getMsgTotalLen(msgTotLength, msgLen,
		// layoutTypeMap.get(dto.getParentFldNm()), layoutTypeMap, result);
		// }
		//
		// return msgLen ;
		//
		// } else {
		// msgTotLength = msgTotLength + msgLen;
		// return msgTotLength;
		// }
		//// logger.debug("dtoname : {}", dto.getFldEngNm());
		//// logger.debug("****msgLenTotal : {}", msgTotLength);

	}

	public boolean isInteger(String data) {

		try {
			Integer.parseInt(data);
		} catch (NumberFormatException e) {
			return false;
		} catch (NullPointerException e) {
			return false;
		}

		return true;

	}

	public String getCodeValueNm(String codeId, String codeValueNm, String locale) {

		CommCodeDto codeValueNmDto = codeDao.selectCommCodeValue(codeId, codeValueNm, locale);
		String codeValue = "";
		if (codeValueNmDto != null) {
			codeValue = codeValueNmDto.getCdVal();
		}

		return codeValue;

	}

	public String getTerInfInfo(String intrfcId, String deployVersion, String flag) {
		return flag;

	}

	public IntrfcDeployResponseList getDeployHistoryResult(String intrfcId, Integer deployVersion, String deployDttm,
			String deployResultCd) {

		IntrfcDeployResponseList out = new IntrfcDeployResponseList();

		String rawDataResult = intrfcdeployhisthsDao.selectIntrfcdeployhisthsResultData(intrfcId, deployVersion,
				deployDttm, deployResultCd);

		if (rawDataResult != null && !rawDataResult.equals("")) {
			try {
				out = JsonUtils.jsonToObect(rawDataResult, out.getClass());
			} catch (JsonParseException e) {
				throw new ServiceException(BxMessages.Error.SYSTEM_EXCEPTION);
			} catch (JsonMappingException e) {
				throw new ServiceException(BxMessages.Error.SYSTEM_EXCEPTION);
			} catch (IOException e) {
				throw new ServiceException(BxMessages.Error.SYSTEM_EXCEPTION);
			}
		}

		logger.debug(rawDataResult);

		return out;
	}

	public boolean validationIoNmWrapper(IntrfccombsDto intrfcDto, List<String> wrapperList, StringBuilder str) {

		boolean isValid = true;
		String intrfcType = intrfcDto.getIntrfcTypeCd();
		String trxDscd = intrfcDto.getTrxDscd();
		String intrfcId = intrfcDto.getIntrfcId();

		if (trxDscd.equals("ONLINE") && intrfcType.equals("FEP")) {
			// 대내개별부제외
			for (String ioNm : wrapperList) {
				if (ioNm != null && !ioNm.equals("")) {
					List<String> msgDupList = msglayoutbsDao.selectSameIONmInternalWrapper(ioNm, null);
					List<String> msgDupListExt = msglayoutbsDao.selectSameIONmExternal(ioNm, null);
					List<String> childDupList = msglayoutdtDao.selectSameIONmChildNm(ioNm, null);
					if (msgDupList != null && msgDupList.size() > 0) {
						str.append("\n세팅한 전문IO명이 이미 존재합니다. 인터페이스ID [" + intrfcId + "], WrapperIO명 [" + ioNm
								+ "], 사용중인 전문ID [" + msgDupList + "]");
						isValid = false;
					}
					if (msgDupListExt != null && msgDupListExt.size() > 0) {
						str.append("\n세팅한 전문IO명이 이미 존재합니다. 인터페이스ID [" + intrfcId + "], WrapperIO명 [" + ioNm
								+ "], 사용중인 전문ID [" + msgDupListExt + "]");
						isValid = false;
					}
					if (childDupList != null && childDupList.size() > 0) {
						str.append("\n세팅한 전문IO명이 하위IO명에 이미 존재합니다. 인터페이스ID [" + intrfcId + "], WrapperIO명 [" + ioNm
								+ "], 사용중인 전문ID [" + childDupList + "]");
						isValid = false;
					}
				}
			}
		} else {
			for (String ioNm : wrapperList) {
				if (ioNm != null && !ioNm.equals("")) {
					List<String> msgDupList = msglayoutbsDao.selectSameIONmInternal(ioNm, null);
					List<String> msgDupListExt = msglayoutbsDao.selectSameIONmExternal(ioNm, null);
					List<String> childDupList = msglayoutdtDao.selectSameIONmChildNm(ioNm, null);
					if (msgDupList != null && msgDupList.size() > 0) {
						str.append("\n세팅한 전문IO명이 이미 존재합니다. 인터페이스ID [" + intrfcId + "], WrapperIO명 [" + ioNm
								+ "], 사용중인 전문ID [" + msgDupList + "]");
						isValid = false;
					}
					if (msgDupListExt != null && msgDupListExt.size() > 0) {
						str.append("\n세팅한 전문IO명이 이미 존재합니다. 인터페이스ID [" + intrfcId + "], WrapperIO명 [" + ioNm
								+ "], 사용중인 전문ID [" + msgDupListExt + "]");
						isValid = false;
					}
					if (childDupList != null && childDupList.size() > 0) {
						str.append("\n세팅한 전문IO명이 하위IO명에 이미 존재합니다. 인터페이스ID [" + intrfcId + "], WrapperIO명 [" + ioNm
								+ "], 사용중인 전문ID [" + childDupList + "]");
						isValid = false;
					}
				}
			}
		}

		return isValid;
	}

}
