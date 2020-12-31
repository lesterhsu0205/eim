package eims.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

//import com.nexacro17.xapi.data.PlatformData;
//import com.nexacro17.xapi.data.VariableList;

import eims.web.dao.CommCodeDao;
import eims.web.dto.IntrfcDeployResponseList;
import eims.web.dto.IntrfcFileImportErrInfo;
import eims.web.dto.IntrfcInfoExportDto;
import eims.web.dto.table.IntrfcIdCreateDto;
import eims.web.dto.table.IntrfccombsDto;
import eims.web.dto.table.IntrfcdeployhisthsDto;
import eims.web.dto.table.MsglayoutbsDto;
import eims.web.dto.table.UserDto;
import eims.web.dto.ui.UiIntrfcDeployResponse;
import eims.web.dto.ui.UiIntrfcIdOut;
import eims.web.dto.ui.UiIntrfccombsOut;
import eims.web.dto.ui.UiIntrfcdeployhisthsOut;
import eims.web.excel.view.InterfaceExcelView;
import eims.web.excel.view.InterfaceListExcelView;
import eims.web.service.IntrfccomExtService;
import eims.web.service.IntrfccomService;
import eims.web.service.UserService;

@Controller
public class IntrfccomController {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private IntrfccomService intrfccomService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private IntrfccomExtService intrfccomExtService; 
	
	@Autowired
	private CommCodeDao codeDao;

	@RequestMapping(value = "/intrfccoms", method = RequestMethod.GET)
	public ResponseEntity<UiIntrfccombsOut> getIntrfccoms(
			@RequestParam(value = "intrfcId", required = false) String intrfcId,
			@RequestParam(value = "intrfcNm", required = false) String intrfcNm,
			@RequestParam(value = "intrfcNmSub", required = false) String intrfcNmSub,
			@RequestParam(value = "intrfcWayCd", required = false) String intrfcWayCd,
			@RequestParam(value = "workStatusCd", required = false) String workStatusCd,
			@RequestParam(value = "regManId", required = false) String regManId,
			@RequestParam(value = "regDttm", required = false) String regDttm,
			@RequestParam(value = "msgTrnsfrmYn", required = false) String msgTrnsfrmYn,
			@RequestParam(value = "trxCd", required = false) String trxCd,
			@RequestParam(value = "bizCd", required = false) String bizCd,
			@RequestParam(value = "instCd", required = false) String instCd,
			@RequestParam(value = "trxDscd", required = false) String trxDscd,
			@RequestParam(value = "intrfcTypeCd", required = false) String intrfcTypeCd,
			@RequestParam(value = "lv1Cd", required = false) String lv1Cd,
			@RequestParam(value = "lv2Cd", required = false) String lv2Cd,
			@RequestParam(value = "lv3Cd", required = false) String lv3Cd,
			@RequestParam(value = "lv4Cd", required = false) String lv4Cd,
			@RequestParam(value = "lv5Cd", required = false) String lv5Cd,
			@RequestParam(value = "syncAsyncDscd", required = false) String syncAsyncDscd,
			@RequestParam(value = "srTypeCd", required = false) String srTypeCd,
			@RequestParam(value = "rqstExtrnlMsgNo", required = false) String rqstExtrnlMsgNo,
			@RequestParam(value = "rspsExtrnlMsgNo", required = false) String rspsExtrnlMsgNo,
			@RequestParam(value = "sysCdS", required = false) String sysCdS, //�넚�떊�떆�뒪�뀥肄붾뱶
			@RequestParam(value = "sysCdR", required = false) String sysCdR, //�닔�떊�떆�뒪�뀥肄붾뱶
			@RequestParam(value = "msgLayoutId", required = false) String msgLayoutId, //�닔�떊�떆�뒪�뀥肄붾뱶
			@RequestParam(value = "trxTypeDscd", required = false) String trxTypeDscd,
			@RequestParam(value = "viewId", required = false) String viewId,
			@RequestParam(value = "execEnvDscd", required = false) String execEnvDscd,
			@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
			@RequestParam(value = "pageSize", required = false, defaultValue = "1000") int pageSize) {
		logger.debug(
				"INPUT   intrfcId : [{}],  intrfcNm : [{}],  intrfcNmSub : [{}],  intrfcWayCd : [{}],  workStatusCd : [{}],  regManId : [{}],  regDttm : [{}],  msgTrnsfrmYn : [{}],  trxCd : [{}],  bizCd : [{}],  instCd : [{}],  trxTypeCd : [{}], rawData : [{}],  intrfcTypeCd : [{}],  lv1Cd : [{}],  lv2Cd : [{}],  lv3Cd : [{}],  lv4Cd : [{}],  lv5Cd : [{}]",
				intrfcId, intrfcNm, intrfcNmSub, intrfcWayCd, workStatusCd, regManId, regDttm, msgTrnsfrmYn, trxCd, bizCd, instCd, trxDscd, intrfcTypeCd, lv1Cd, lv2Cd, lv3Cd, lv4Cd, lv5Cd);

		UiIntrfccombsOut out = intrfccomService.getList(intrfcId, intrfcNm, intrfcNmSub, intrfcWayCd, workStatusCd, regManId, regDttm, msgTrnsfrmYn, trxCd, bizCd, instCd, trxDscd, intrfcTypeCd, lv1Cd, lv2Cd, lv3Cd, lv4Cd, lv5Cd, syncAsyncDscd, srTypeCd, rqstExtrnlMsgNo, rspsExtrnlMsgNo, sysCdS, sysCdR,
				msgLayoutId, trxTypeDscd, viewId, execEnvDscd, pageSize, pageNumber);

		logger.debug(" OUTPUT : {}", out.getTotalCnt());

		return new ResponseEntity<UiIntrfccombsOut>(out, HttpStatus.OK);
	}

	@RequestMapping(value = "/interfaceListApi", method = RequestMethod.GET)
	public ResponseEntity<UiIntrfccombsOut> getInterfaceListApi(
			@RequestParam(value = "intrfcId", required = false) String intrfcId,
			@RequestParam(value = "intrfcNm", required = false) String intrfcNm,
			@RequestParam(value = "intrfcNmSub", required = false) String intrfcNmSub,
			@RequestParam(value = "intrfcWayCd", required = false) String intrfcWayCd,
			@RequestParam(value = "workStatusCd", required = false) String workStatusCd,
			@RequestParam(value = "regManId", required = false) String regManId,
			@RequestParam(value = "regDttm", required = false) String regDttm,
			@RequestParam(value = "msgTrnsfrmYn", required = false) String msgTrnsfrmYn,
			@RequestParam(value = "trxCd", required = false) String trxCd,
			@RequestParam(value = "bizCd", required = false) String bizCd,
			@RequestParam(value = "instCd", required = false) String instCd,
			@RequestParam(value = "trxDscd", required = false) String trxDscd,
			@RequestParam(value = "intrfcTypeCd", required = false) String intrfcTypeCd,
			@RequestParam(value = "lv1Cd", required = false) String lv1Cd,
			@RequestParam(value = "lv2Cd", required = false) String lv2Cd,
			@RequestParam(value = "lv3Cd", required = false) String lv3Cd,
			@RequestParam(value = "lv4Cd", required = false) String lv4Cd,
			@RequestParam(value = "lv5Cd", required = false) String lv5Cd,
			@RequestParam(value = "syncAsyncDscd", required = false) String syncAsyncDscd,
			@RequestParam(value = "srTypeCd", required = false) String srTypeCd,
			@RequestParam(value = "rqstExtrnlMsgNo", required = false) String rqstExtrnlMsgNo,
			@RequestParam(value = "rspsExtrnlMsgNo", required = false) String rspsExtrnlMsgNo,
			@RequestParam(value = "sysCdS", required = false) String sysCdS, //�넚�떊�떆�뒪�뀥肄붾뱶
			@RequestParam(value = "sysCdR", required = false) String sysCdR, //�닔�떊�떆�뒪�뀥肄붾뱶
			@RequestParam(value = "msgLayoutId", required = false) String msgLayoutId, //�닔�떊�떆�뒪�뀥肄붾뱶
			@RequestParam(value = "trxTypeDscd", required = false) String trxTypeDscd,
			@RequestParam(value = "viewId", required = false) String viewId,
			@RequestParam(value = "execEnvDscd", required = false) String execEnvDscd,
			@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
			@RequestParam(value = "pageSize", required = false, defaultValue = "9999999") int pageSize) {
		logger.debug(
				"INPUT   intrfcId : [{}],  intrfcNm : [{}],  intrfcWayCd : [{}],  workStatusCd : [{}],  regManId : [{}],  regDttm : [{}],  msgTrnsfrmYn : [{}],  trxCd : [{}],  bizCd : [{}],  instCd : [{}],  trxTypeCd : [{}], rawData : [{}],  intrfcTypeCd : [{}],  lv1Cd : [{}],  lv2Cd : [{}],  lv3Cd : [{}],  lv4Cd : [{}],  lv5Cd : [{}]",
				intrfcId, intrfcNm, intrfcWayCd, workStatusCd, regManId, regDttm, msgTrnsfrmYn, trxCd, bizCd, instCd, trxDscd, intrfcTypeCd, lv1Cd, lv2Cd, lv3Cd, lv4Cd, lv5Cd);

		UiIntrfccombsOut out = new UiIntrfccombsOut();
		if(intrfcTypeCd.equals("MCA") || intrfcTypeCd.equals("SUI-enc") ) {
			intrfcTypeCd = "MCI";
			out = intrfccomService.getList(intrfcId, intrfcNm, intrfcNmSub, intrfcWayCd, workStatusCd, regManId, regDttm, msgTrnsfrmYn, trxCd, bizCd, instCd, trxDscd, intrfcTypeCd, lv1Cd, lv2Cd, lv3Cd, lv4Cd, lv5Cd, syncAsyncDscd, srTypeCd, rqstExtrnlMsgNo, rspsExtrnlMsgNo, sysCdS, sysCdR,
					msgLayoutId, trxTypeDscd, viewId, execEnvDscd,  pageSize,	pageNumber);
			
		} else if(intrfcTypeCd.equals("FEB")) {
			intrfcTypeCd = "FEP";
			sysCdS = "FEB";
			sysCdR = "FEB";
			out = intrfccomService.getFebList(intrfcId, intrfcNm, intrfcNmSub, intrfcWayCd, workStatusCd, regManId, regDttm, msgTrnsfrmYn, trxCd, bizCd, instCd, trxDscd, intrfcTypeCd, lv1Cd, lv2Cd, lv3Cd, lv4Cd, lv5Cd, syncAsyncDscd, srTypeCd, rqstExtrnlMsgNo, rspsExtrnlMsgNo, sysCdS, sysCdR,
					msgLayoutId, trxTypeDscd, viewId, pageSize, pageNumber);
		} else if(intrfcTypeCd.equals("EAI")){
			if(intrfcId != null) {
				if (intrfcId.substring(0,1).equals("F")) {
					intrfcTypeCd = "FEP";
					sysCdS = "FEB";
					sysCdR = "FEB";
					out = intrfccomService.getFebList(intrfcId, intrfcNm, intrfcNmSub, intrfcWayCd, workStatusCd, regManId, regDttm, msgTrnsfrmYn, trxCd, bizCd, instCd, trxDscd, intrfcTypeCd, lv1Cd, lv2Cd, lv3Cd, lv4Cd, lv5Cd, syncAsyncDscd, srTypeCd, rqstExtrnlMsgNo, rspsExtrnlMsgNo, sysCdS, sysCdR,
							msgLayoutId, trxTypeDscd, viewId, pageSize, pageNumber);
				} else {
					out = intrfccomService.getList(intrfcId, intrfcNm, intrfcNmSub, intrfcWayCd, workStatusCd, regManId, regDttm, msgTrnsfrmYn, trxCd, bizCd, instCd, trxDscd, intrfcTypeCd, lv1Cd, lv2Cd, lv3Cd, lv4Cd, lv5Cd, syncAsyncDscd, srTypeCd, rqstExtrnlMsgNo, rspsExtrnlMsgNo, sysCdS, sysCdR,
							msgLayoutId, trxTypeDscd, viewId, execEnvDscd, pageSize, pageNumber);
				}
			} else {
				out = intrfccomService.getList(intrfcId, intrfcNm, intrfcNmSub, intrfcWayCd, workStatusCd, regManId, regDttm, msgTrnsfrmYn, trxCd, bizCd, instCd, trxDscd, intrfcTypeCd, lv1Cd, lv2Cd, lv3Cd, lv4Cd, lv5Cd, syncAsyncDscd, srTypeCd, rqstExtrnlMsgNo, rspsExtrnlMsgNo, sysCdS, sysCdR,
						msgLayoutId, trxTypeDscd, viewId, execEnvDscd, pageSize, pageNumber);
			}

		} else {
			out = intrfccomService.getList(intrfcId, intrfcNm, intrfcNmSub, intrfcWayCd, workStatusCd, regManId, regDttm, msgTrnsfrmYn, trxCd, bizCd, instCd, trxDscd, intrfcTypeCd, lv1Cd, lv2Cd, lv3Cd, lv4Cd, lv5Cd, syncAsyncDscd, srTypeCd, rqstExtrnlMsgNo, rspsExtrnlMsgNo, sysCdS, sysCdR,
					msgLayoutId, trxTypeDscd, viewId, execEnvDscd, pageSize, pageNumber);
		}
		
		logger.debug(" OUTPUT : {}", out.getTotalCnt());
		return new ResponseEntity<UiIntrfccombsOut>(out, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/intrfccoms/{intrfcId}", method = RequestMethod.GET)
	public ResponseEntity<IntrfccombsDto> getIntrfccom(@PathVariable(value = "intrfcId", required = true) String intrfcId) {
		logger.debug("INPUT   intrfcId : [{}]", intrfcId);

		IntrfccombsDto out = intrfccomService.get(intrfcId);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<IntrfccombsDto>(out, HttpStatus.OK);
	}

	@RequestMapping(value = "/intrfccoms", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<UiIntrfcIdOut> addIntrfccom(@RequestBody IntrfccombsDto intrfccombsDto) {
		logger.debug(" INPUT : IntrfccombsDto [{}]", intrfccombsDto);
		boolean screenYn = true;
		String out = intrfccomService.add(intrfccombsDto, screenYn);
		UiIntrfcIdOut dto = new UiIntrfcIdOut() ;
		dto.setIntrfcId(out);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<UiIntrfcIdOut>(dto, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/intrfccoms", method = RequestMethod.PUT)
	public ResponseEntity<Integer> updateIntrfccom(@RequestBody IntrfccombsDto intrfccombsDto) {
		logger.debug(" INPUT : IntrfccombsDto [{}]", intrfccombsDto);

		int out = intrfccomService.update(intrfccombsDto, true);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<Integer>(out, HttpStatus.OK);
	}

	@RequestMapping(value = "/intrfccoms/{intrfcId}", method = RequestMethod.DELETE)
	public ResponseEntity<Integer> deleteIntrfccom(@PathVariable(value = "intrfcId", required = true) String intrfcId) {
		logger.debug("INPUT   intrfcId : [{}]", intrfcId);

		int out = intrfccomService.delete(intrfcId);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<Integer>(out, HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/intrfccoms/deploy", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<UiIntrfcDeployResponse> deployIntrfccom(@RequestBody IntrfccombsDto intrfccombsDto) {
		logger.debug(" INPUT : IntrfccombsDto [{}]", intrfccombsDto);

		UiIntrfcDeployResponse out = intrfccomService.deploy(intrfccombsDto);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<UiIntrfcDeployResponse>(out, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/intrfccoms/deployAll", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<UiIntrfcDeployResponse> deployAllIntrfccom() {
				
		List<String> intrfcList = new ArrayList<String>();
		String intrfType = "MCI";
		UiIntrfcDeployResponse out = null;
		intrfcList = intrfccomService.getDeployAllList(intrfType);
		logger.debug(" INPUT : intrfcList [{}]", intrfcList);
		
		for (String intrfcId : intrfcList) {
			IntrfccombsDto intrfccombsDto = intrfccomService.get(intrfcId); 
			logger.debug(" INPUT : intrfcId [{}]", intrfccombsDto);
			out = intrfccomService.deploy(intrfccombsDto);	
			logger.debug(" OUTPUT : {}", out);
		}
		
		
		return new ResponseEntity<UiIntrfcDeployResponse>(HttpStatus.OK);
	}
	

	@RequestMapping(value = "/intrfccoms/deployhistorys", method = RequestMethod.GET)
	public ResponseEntity<UiIntrfcdeployhisthsOut> getIntrfcdeployhists(@RequestParam(value = "intrfcId", required = false) String intrfcId,
			@RequestParam(value = "deployResultCd", required = false) String deployResultCd,
			@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
			@RequestParam(value = "pageSize", required = false, defaultValue = "999999") int pageSize) {
		logger.debug("INPUT   intrfcId : [{}]", intrfcId);

		UiIntrfcdeployhisthsOut out = intrfccomService.getDeployHistory(intrfcId, deployResultCd, pageSize, pageNumber);

		logger.debug(" OUTPUT : {}", out.getTotalCnt());

		return new ResponseEntity<UiIntrfcdeployhisthsOut>(out, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/intrfccoms/deployhistoryresults", method = RequestMethod.GET)
	public ResponseEntity<IntrfcDeployResponseList> getIntrfcdeployhistResult(
			@RequestParam(value = "intrfcId", required = false) String intrfcId,
			@RequestParam(value = "deployVersion", required = false, defaultValue = "0") Integer deployVersion,
			@RequestParam(value = "deployDttm", required = false) String deployDttm,
			@RequestParam(value = "deployResultCd", required = false) String deployResultCd) {
		logger.debug("INPUT   intrfcId : [{}]", intrfcId);

		IntrfcDeployResponseList out = intrfccomService.getDeployHistoryResult(intrfcId, deployVersion, deployDttm, deployResultCd);

		return new ResponseEntity<IntrfcDeployResponseList>(out, HttpStatus.OK);
	}

	@RequestMapping(value = "/intrfccoms/redeploy", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<Integer> reDeploy(@RequestBody IntrfcdeployhisthsDto intrfcdeployhisthsDto) {
		logger.debug(" INPUT : intrfcdeployhisthsDto [{}]", intrfcdeployhisthsDto);

		int out = intrfccomService.reDeploy(intrfcdeployhisthsDto);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<Integer>(out, HttpStatus.OK);
	}

	@RequestMapping(value = "/intrfccoms/deploy/{intrfcId}", method = RequestMethod.GET)
	public ResponseEntity<IntrfccombsDto> getPreDeployIntrfc(@PathVariable(value = "intrfcId", required = true) String intrfcId, @RequestParam(value = "deployVersion", defaultValue = "0", required = false) int deployVersion) {
		logger.debug("INPUT   intrfcId : [{}]", intrfcId);

		IntrfccombsDto out = intrfccomService.getPreDeployIntrfc(intrfcId, deployVersion);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<IntrfccombsDto>(out, HttpStatus.OK);
	}

	@RequestMapping(value = "/intrfccoms/intrfcidcreate", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<IntrfcIdCreateDto> getInterfaceId(@RequestBody IntrfcIdCreateDto intrfcIdCreateDto) {
		logger.debug(" INPUT : itrfcIdCreateDto [{}]", intrfcIdCreateDto);

		IntrfcIdCreateDto out = intrfccomService.getInterfaceId(intrfcIdCreateDto);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<IntrfcIdCreateDto>(out, HttpStatus.OK);
	}

	@RequestMapping(value = "/intrfccoms/layoutdiff", method = RequestMethod.GET)
	public ResponseEntity<MsglayoutbsDto> getLayoutdiff(
			@RequestParam(value = "intrfcId", required = false) String intrfcId,
			@RequestParam(value = "deployVersion", defaultValue = "0", required = false) int deployVersion,
			@RequestParam(value = "msgLayoutId", required = false) String msgLayoutId,
			@RequestParam(value = "srTypeCd", required = false) String srTypeCd, //�넚�닔�떊���엯肄붾뱶
			@RequestParam(value = "rqstRspsTypeCd", required = false) String rqstRspsTypeCd, //�슂泥��쓳�떟���엯肄붾뱶
			@RequestParam(value = "layoutSeq", required = false) Integer layoutSeq //�슂泥��쓳�떟���엯肄붾뱶
	) {

		MsglayoutbsDto out = intrfccomService.getPreLayout(intrfcId, deployVersion, msgLayoutId, srTypeCd, rqstRspsTypeCd, layoutSeq);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<MsglayoutbsDto>(out, HttpStatus.OK);
	}

	@RequestMapping(value = "/intrfccoms/fileuploads", method = RequestMethod.POST)
	public ResponseEntity<Integer> fileUpload(
			@RequestPart(name = "intrfcFile", required = false) MultipartFile intrfcFile, @RequestPart(name = "intrfcTypeCd", required = false) String intrfcFileUploadDto) {

		int out;

		logger.debug("interfaceTypeCode: {}", intrfcFileUploadDto);

//		out = intrfccomService.fileUpload(intrfcFile, intrfcFileUploadDto.getIntrfcTypeCd());
		out = intrfccomService.fileUpload(intrfcFile, intrfcFileUploadDto);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<Integer>(out, HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/intrfccoms/import/intrfcfiles", method = RequestMethod.POST)
	public ResponseEntity<List<IntrfcFileImportErrInfo>> importIntrfcFiles(MultipartHttpServletRequest intrfcFiles
			, @RequestPart(name = "intrfcTypeCd", required = false) String intrfcTypeCd) {

		List<IntrfcFileImportErrInfo> out;
		
		List<MultipartFile> files = intrfcFiles.getFiles("intrfcFile");
		logger.debug("interfaceTypeCode : {}", intrfcTypeCd);
		logger.debug("Files : {} ", files );

		out = intrfccomExtService.importIntrfcFiles(files, intrfcTypeCd);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<List<IntrfcFileImportErrInfo>>(out, HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/intrfccoms/import/definition", method = RequestMethod.POST)
	public ResponseEntity<List<IntrfcFileImportErrInfo>> importDefinition(MultipartHttpServletRequest intrfcFiles
			, @RequestPart(name = "intrfcTypeCd", required = false) String intrfcTypeCd) {
		logger.debug("interfaceTypeCode : {}", intrfcTypeCd);
		List<IntrfcFileImportErrInfo> out = null;
		List<MultipartFile> files = intrfcFiles.getFiles("intrfcFile");
		logger.debug("interfaceTypeCode : {}", intrfcTypeCd);
		logger.debug("Files : {} ", files );

		out = intrfccomExtService.importDefinition(files,intrfcTypeCd);
		
		return new ResponseEntity<List<IntrfcFileImportErrInfo>>(out, HttpStatus.CREATED);
	}
	

	@RequestMapping(value = "/intrfccoms/excelexport", method = RequestMethod.POST, consumes = "application/json")
	public ModelAndView excelExport(@RequestBody IntrfccombsDto intrfccombsDto,
			HttpServletResponse response) {

		logger.debug("intrfccombsDto : [{}]", intrfccombsDto.getIntrfcId());

		IntrfccombsDto interfaceDto = intrfccomService.get(intrfccombsDto.getIntrfcId());
		Map<String, Object> data = new HashMap<>();
		
		String userId = interfaceDto.getRegManId() ;
		UserDto userDto = null ;
		try {
			userDto = userService.get(userId) ; 
		}catch(Exception e) {
			logger.error("{}", e);
		}
		String userNm = null ;
		if(userDto == null) {
			userNm = "�솉湲몃룞" ;
		} else {
			userNm = userDto.getUserNm() ;
		}

		data.put("intrfcId", intrfccombsDto.getIntrfcId());
		data.put("intrfcDto", interfaceDto);
		data.put("intrfcType", interfaceDto.getIntrfcTypeCd());
		data.put("trxDscd", interfaceDto.getTrxDscd());
		data.put("l3cd", interfaceDto.getLv3Cd()) ;
		data.put("userNm", userNm) ;

		InterfaceExcelView ev = new InterfaceExcelView(codeDao);
		return new ModelAndView(ev, data);
	}

	@RequestMapping(value = "/intrfccoms/export/intrfcinfos", method = RequestMethod.GET)
	public ModelAndView getIntrfcInfos(
			@RequestParam(value = "intrfcId", required = false) String intrfcId,
			@RequestParam(value = "intrfcNm", required = false) String intrfcNm,
			@RequestParam(value = "intrfcWayCd", required = false) String intrfcWayCd,
			@RequestParam(value = "workStatusCd", required = false) String workStatusCd,
			@RequestParam(value = "regManId", required = false) String regManId,
			@RequestParam(value = "regDttm", required = false) String regDttm,
			@RequestParam(value = "msgTrnsfrmYn", required = false) String msgTrnsfrmYn,
			@RequestParam(value = "trxCd", required = false) String trxCd,
			@RequestParam(value = "bizCd", required = false) String bizCd,
			@RequestParam(value = "instCd", required = false) String instCd,
			@RequestParam(value = "trxDscd", required = false) String trxDscd,
			@RequestParam(value = "intrfcTypeCd", required = false) String intrfcTypeCd,
			@RequestParam(value = "lv1Cd", required = false) String lv1Cd,
			@RequestParam(value = "lv2Cd", required = false) String lv2Cd,
			@RequestParam(value = "lv3Cd", required = false) String lv3Cd,
			@RequestParam(value = "lv4Cd", required = false) String lv4Cd,
			@RequestParam(value = "lv5Cd", required = false) String lv5Cd,
			@RequestParam(value = "syncAsyncDscd", required = false) String syncAsyncDscd,
			@RequestParam(value = "srTypeCd", required = false) String srTypeCd,
			@RequestParam(value = "rqstExtrnlMsgNo", required = false) String rqstExtrnlMsgNo,
			@RequestParam(value = "rspsExtrnlMsgNo", required = false) String rspsExtrnlMsgNo,
			@RequestParam(value = "sysCdS", required = false) String sysCdS, //�넚�떊�떆�뒪�뀥肄붾뱶
			@RequestParam(value = "sysCdR", required = false) String sysCdR, //�닔�떊�떆�뒪�뀥肄붾뱶
			@RequestParam(value = "msgLayoutId", required = false) String msgLayoutId, //�닔�떊�떆�뒪�뀥肄붾뱶
			@RequestParam(value = "trxTypeDscd", required = false) String trxTypeDscd) {
		
		logger.debug(
				"INPUT   intrfcId : [{}],  intrfcNm : [{}],  intrfcWayCd : [{}],  workStatusCd : [{}],  regManId : [{}],  regDttm : [{}],  msgTrnsfrmYn : [{}],  trxCd : [{}],  bizCd : [{}],  instCd : [{}],  trxTypeCd : [{}], intrfcTypeCd : [{}],  lv1Cd : [{}],  lv2Cd : [{}],  lv3Cd : [{}],  lv4Cd : [{}],  lv5Cd : [{}]",
				intrfcId, intrfcNm, intrfcWayCd, workStatusCd, regManId, regDttm, msgTrnsfrmYn, trxCd, bizCd, instCd, trxDscd, intrfcTypeCd, lv1Cd, lv2Cd, lv3Cd, lv4Cd, lv5Cd);

		List<IntrfcInfoExportDto> out = intrfccomExtService.getIntrfcInfos(intrfcId, intrfcNm, intrfcWayCd, workStatusCd, regManId, regDttm, msgTrnsfrmYn, trxCd, bizCd, instCd, trxDscd, intrfcTypeCd, lv1Cd, lv2Cd, lv3Cd, lv4Cd, lv5Cd, syncAsyncDscd, srTypeCd, rqstExtrnlMsgNo, rspsExtrnlMsgNo, sysCdS, sysCdR,
				msgLayoutId, trxTypeDscd);
		
		Map<String, Object> data = new HashMap<>();
		data.put("intrfcType", intrfcTypeCd);
		data.put("intrfcInfos", out);
		
		InterfaceListExcelView ev = new InterfaceListExcelView(codeDao);
		return new ModelAndView(ev, data);	
	}
	
	@RequestMapping(value = "/intrfccoms/deploy/terminal", method = RequestMethod.GET)
	public ResponseEntity<String> getTerInterface(@RequestParam(value = "filter", required = false) String filter,
			@RequestParam(value = "model", required = false) String model,
			HttpServletResponse response) {
				return null;
//		logger.debug("Terminal deploy---");
//
//		String intrfcId = null;
//		String deployVersion = null;
//		String xmlData = null;
//		try {
//
//			if (filter != null && !filter.equals("")) {
//				if (filter.indexOf("^") > -1) {
//					intrfcId = filter.substring(0, filter.indexOf("^"));
//					deployVersion = filter.substring(filter.indexOf("^") + 1);
//					xmlData = intrfccomService.getTerInfInfo(intrfcId, deployVersion, "filter");
//				} else {
//					intrfcId = filter;
//					deployVersion = "LAST";
//					xmlData = intrfccomService.getTerInfInfo(intrfcId, deployVersion, "filter");
//				}
//			} else if (model != null && !model.equals("")) {
//				if (model.indexOf("^") > -1) {
//					intrfcId = model.substring(0, model.indexOf("^"));
//					deployVersion = model.substring(model.indexOf("^") + 1);
//					xmlData = intrfccomService.getTerInfInfo(intrfcId, deployVersion, "model");
//				} else {
//					intrfcId = model;
//					deployVersion = "LAST";
//					xmlData = intrfccomService.getTerInfInfo(intrfcId, deployVersion, "model");
//				}
//			}
//		} catch (Exception e) {
//			logger.error("{}", e);
//			PlatformData outPlatformData = new PlatformData();
//			VariableList messageVariable = new VariableList();
//			messageVariable.add("ErrorCode", -1);
//			messageVariable.add("ErrorMsg", e.getMessage());
//			outPlatformData.setVariableList(messageVariable);
//			xmlData = outPlatformData.saveXml();
//		}
//		HttpHeaders header = new HttpHeaders();
//		header.setContentType(MediaType.TEXT_XML);
//		response.setContentType("text/xml");
//
//		return new ResponseEntity<String>(xmlData, HttpStatus.OK);

	}

}