package eims.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import eims.web.dao.CommCodeDao;
import eims.web.dto.table.MsgIdCreateDto;
import eims.web.dto.table.MsgLayoutEffectDto;
import eims.web.dto.table.MsglayoutbsDto;
import eims.web.dto.table.MsglayoutbsFileUploadDto;
import eims.web.dto.table.MsglayoutbsListDto;
import eims.web.dto.ui.UiMsgLayoutIdOut;
import eims.web.dto.ui.UiMsglayoutbsOut;
import eims.web.excel.view.LayoutExcelView;
import eims.web.service.MsglayoutService;

@Controller
public class MsglayoutController {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private MsglayoutService msglayoutService;
	@Autowired
	private CommCodeDao codeDao;

	@RequestMapping(value = "/msglayouts", method = RequestMethod.GET)
	public ResponseEntity<UiMsglayoutbsOut> getMsglayouts(@RequestParam(value = "msgNm", required = false) String msgNm,
			@RequestParam(value = "msgNmSub", required = false) String msgNmSub,
			@RequestParam(value = "msgVersion", required = false, defaultValue = "0") int msgVersion,
			@RequestParam(value = "msgDscd", required = false) String msgDscd,
			@RequestParam(value = "regManId", required = false) String regManId,
			@RequestParam(value = "regDttm", required = false) String regDttm,
			@RequestParam(value = "msgDataVal", required = false) String msgDataVal,
			@RequestParam(value = "msgDesc", required = false) String msgDesc,
			@RequestParam(value = "extrnlBizNm", required = false) String extrnlBizNm,
			@RequestParam(value = "dtoNm", required = false) String dtoNm,
			@RequestParam(value = "msgLayoutId", required = false) String msgLayoutId,
			@RequestParam(value = "appTypeCd", required = false) String appTypeCd,
			@RequestParam(value = "trxCd", required = false) String trxCd,
			@RequestParam(value = "appNm", required = false) String appNm,
			@RequestParam(value = "lv1Cd", required = false) String lv1Cd,
			@RequestParam(value = "lv2Cd", required = false) String lv2Cd,
			@RequestParam(value = "lv3Cd", required = false) String lv3Cd,
			@RequestParam(value = "lv4Cd", required = false) String lv4Cd,
			@RequestParam(value = "lv5Cd", required = false) String lv5Cd,
			@RequestParam(value = "trxDscd", required = false) String trxDscd,
			@RequestParam(value = "bitMapCrtnYn", required = false) String bitMapCrtnYn,
			@RequestParam(value = "jobId", required = false) String jobId,
			@RequestParam(value = "chlDscd", required = false) String chlDscd,
			@RequestParam(value = "iso8583DataTypeCd", required = false) String iso8583DataTypeCd,
			@RequestParam(value = "crtnSeq", required = false, defaultValue = "0") int crtnSeq,
			@RequestParam(value = "rsrvFldVal1", required = false) String rsrvFldVal1,
			@RequestParam(value = "rsrvFldVal2", required = false) String rsrvFldVal2,
			@RequestParam(value = "rsrvFldVal3", required = false) String rsrvFldVal3,
			@RequestParam(value = "bitMapTypeCd", required = false) String bitMapTypeCd,
			@RequestParam(value = "cfgDesc", required = false) String cfgDesc,
			@RequestParam(value = "indsYn", required = false) String indsYn,
			@RequestParam(value = "workStatusCd", required = false) String workStatusCd,
			@RequestParam(value = "custApiYn", required = false) String custApiYn,
			@RequestParam(value = "msgRvsNo", required = false, defaultValue = "0") int msgRvsNo,
			@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
			@RequestParam(value = "pageSize", required = false, defaultValue = "1000") int pageSize) {
		logger.debug(
				"INPUT   msgNm : [{}],  msgVersion : [{}],  msgDscd : [{}],  regManId : [{}],  regDttm : [{}],  extrnlMsgNo : [{}],  msgDesc : [{}],  extrnlBizNm : [{}],  dtoNm : [{}],  msgLayoutId : [{}],  appTypeCd : [{}],  trxCd : [{}],  appNm : [{}],  lv1Cd : [{}],  lv2Cd : [{}],  lv3Cd : [{}],  lv4Cd : [{}],  lv5Cd : [{}],  trxDscd : [{}]",
				msgNm, msgVersion, msgDscd, regManId, regDttm, msgDataVal, msgDesc, extrnlBizNm, dtoNm, msgLayoutId,
				appTypeCd, trxCd, appNm, lv1Cd, lv2Cd, lv3Cd, lv4Cd, lv5Cd, trxDscd, chlDscd);

		UiMsglayoutbsOut out = msglayoutService.getList(msgNm, msgNmSub, msgVersion, msgDscd, regManId, regDttm, msgDataVal,
				msgDesc, extrnlBizNm, dtoNm, msgLayoutId, appTypeCd, trxCd, appNm, lv1Cd, lv2Cd, lv3Cd, lv4Cd, lv5Cd,
				trxDscd, bitMapCrtnYn, jobId, chlDscd, iso8583DataTypeCd, crtnSeq, rsrvFldVal1, rsrvFldVal2, rsrvFldVal3, 
				bitMapTypeCd, cfgDesc, indsYn, workStatusCd, custApiYn, msgRvsNo, pageSize, pageNumber);

		logger.debug(" OUTPUT : {}", out.getTotalCnt());

		return new ResponseEntity<UiMsglayoutbsOut>(out, HttpStatus.OK);
	}

	@RequestMapping(value = "/msglayouts/{msgLayoutId:.+}", method = RequestMethod.GET)
	public ResponseEntity<MsglayoutbsDto> getMsglayout(
			@PathVariable(value = "msgLayoutId", required = true) String msgLayoutId) {
		logger.debug("INPUT   msgLayoutId : [{}]", msgLayoutId);

		MsglayoutbsDto out = msglayoutService.get(msgLayoutId);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<MsglayoutbsDto>(out, HttpStatus.OK);
	}

	@RequestMapping(value = "/msglayouts", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<UiMsgLayoutIdOut> addMsglayout(@RequestBody MsglayoutbsDto msglayoutbsDto) {
		logger.debug(" INPUT : MsglayoutbsDto [{}]", msglayoutbsDto);

		String out = msglayoutService.add(msglayoutbsDto, "SCREEN");
		UiMsgLayoutIdOut dto = new UiMsgLayoutIdOut() ;
		dto.setMsgLayoutId(out);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<UiMsgLayoutIdOut>(dto, HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/msglayoutstemp", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<UiMsgLayoutIdOut> msglayoutTempAdd(@RequestBody MsglayoutbsDto msglayoutbsDto) {
		logger.debug(" INPUT : MsglayoutbsDto [{}]", msglayoutbsDto);

		String out = msglayoutService.addTemp(msglayoutbsDto, "SCREEN");
		UiMsgLayoutIdOut dto = new UiMsgLayoutIdOut() ;
		dto.setMsgLayoutId(out);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<UiMsgLayoutIdOut>(dto, HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/msglayoutstemp", method = RequestMethod.PUT)
	public ResponseEntity<Integer> msglayoutTempUpdate(@RequestBody MsglayoutbsDto msglayoutbsDto) {
		logger.debug(" INPUT : MsglayoutbsDto [{}]", msglayoutbsDto);

		int out = msglayoutService.updateTemp(msglayoutbsDto);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<Integer>(out, HttpStatus.OK);
	}

	@RequestMapping(value = "/msglayoutslist", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<MsglayoutbsListDto> getMsgLayoutList(@RequestBody List<String> msgLayoutList) {

		MsglayoutbsListDto out = msglayoutService.getList(msgLayoutList);

		return new ResponseEntity<MsglayoutbsListDto>(out, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/msglayouts", method = RequestMethod.PUT)
	public ResponseEntity<Integer> updateMsglayout(@RequestBody MsglayoutbsDto msglayoutbsDto) {
		logger.debug(" INPUT : MsglayoutbsDto [{}]", msglayoutbsDto);

		int out = msglayoutService.update(msglayoutbsDto);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<Integer>(out, HttpStatus.OK);
	}

	@RequestMapping(value = "/msglayouts/{msgLayoutId:.+}", method = RequestMethod.DELETE)
	public ResponseEntity<Integer> deleteMsglayout(
			@PathVariable(value = "msgLayoutId", required = true) String msgLayoutId) {
		logger.debug("INPUT   msgLayoutId : [{}]", msgLayoutId);

		int out = msglayoutService.delete(msgLayoutId);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<Integer>(out, HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/msglayouts/{msgLayoutId:.+}/effects", method = RequestMethod.GET)
	public ResponseEntity<List<MsgLayoutEffectDto>> getMsglayoutEffects(
			@PathVariable(value = "msgLayoutId", required = true) String msgLayoutId,
			@RequestParam(value = "intrfcId", required = false) String intrfcId,
			@RequestParam(value = "intrfcNm", required = false) String intrfcNm) {
		logger.debug("INPUT   msgLayoutId : [{}]", msgLayoutId);

		List<MsgLayoutEffectDto> out = msglayoutService.getEffects(msgLayoutId, intrfcId, intrfcNm);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<List<MsgLayoutEffectDto>>(out, HttpStatus.OK);
	}

	@RequestMapping(value = "/msglayouts/excelexport", method = RequestMethod.POST, consumes = "application/json")
	public ModelAndView excelExport(@RequestBody MsglayoutbsDto msglayoutbsDto,
			HttpServletResponse response) {

		logger.debug("msgLayoutId : [{}]", msglayoutbsDto.getMsgLayoutId());

		MsglayoutbsDto out = msglayoutService.get(msglayoutbsDto.getMsgLayoutId());
		
		Map<String, Object> data = new HashMap<>();

		data.put("msgLayoutId", msglayoutbsDto.getMsgLayoutId());
		data.put("data", out);

		LayoutExcelView ev = new LayoutExcelView(codeDao);
		return new ModelAndView(ev, data);
	}

	@RequestMapping(value = "/msglayouts/fileuploads", method = RequestMethod.POST)
	public ResponseEntity<MsglayoutbsFileUploadDto> fileUpload(
			@RequestPart(name = "msglayoutFile", required = false) MultipartFile msglayoutFile) {

		MsglayoutbsFileUploadDto resultDto;
		
		//msglayoutService.fileUpload(msglayoutFile);
		//msglayoutService.AllFileUpload(msglayoutFile);
		msglayoutService.makeDeployFile(msglayoutFile);
		resultDto = null;
		logger.debug(" OUTPUT : {}", resultDto);

		return new ResponseEntity<MsglayoutbsFileUploadDto>(resultDto, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/msglayouts/extrnlMsgs", method = RequestMethod.GET)
	public ResponseEntity<UiMsglayoutbsOut> getMsglayoutExtrnlMsgs(
			@RequestParam(value = "regManId", required = false) String regManId,
			@RequestParam(value = "msgDataVal", required = false) String msgDataVal,
			@RequestParam(value = "extrnlBizNm", required = false) String extrnlBizNm,
			@RequestParam(value = "msgLayoutId", required = false) String msgLayoutId,
			@RequestParam(value = "trxDscd", required = false) String trxDscd,
			@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
			@RequestParam(value = "pageSize", required = false, defaultValue = "1000") int pageSize) {

		UiMsglayoutbsOut out = msglayoutService.getListExtrnlMsg(regManId, msgDataVal, extrnlBizNm, msgLayoutId,
				trxDscd, pageSize, pageNumber);

		logger.debug(" OUTPUT : {}", out.getTotalCnt());

		return new ResponseEntity<UiMsglayoutbsOut>(out, HttpStatus.OK);
	}

	@RequestMapping(value = "/msglayouts/msgidcreate", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<MsgIdCreateDto> getMsgLayoutId(@RequestBody MsgIdCreateDto msgIdCreateDto) {
		logger.debug(" INPUT : msgIdCreateDto [{}]", msgIdCreateDto);

		MsgIdCreateDto out = msglayoutService.getMsgLayoutId(msgIdCreateDto);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<MsgIdCreateDto>(out, HttpStatus.OK);
	}
}