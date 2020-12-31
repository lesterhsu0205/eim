package eims.web.controller;

import java.util.List;

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

import eims.web.dto.table.DepolysysbsDto;
import eims.web.dto.ui.UiDepolysysbsOut;
import eims.web.service.DepolysysService;

@Controller
public class DepolysysController {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private DepolysysService depolysysService;


	@RequestMapping(value = "/depolysyss", method = RequestMethod.GET)
	public ResponseEntity<UiDepolysysbsOut> getDepolysyss(
			@RequestParam(value = "deploySysCd", required = false) String deploySysCd,
			@RequestParam(value = "deploySysNm", required = false) String deploySysNm,
			@RequestParam(value = "deploySysUrl", required = false) String deploySysUrl,
			@RequestParam(value = "deploySysDesc", required = false) String deploySysDesc,
			@RequestParam(value = "deploySysGrpCd", required = false) String deploySysGrpCd,
			@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
			@RequestParam(value = "pageSize", required = false, defaultValue = "1000") int pageSize) {
		logger.debug(
				"INPUT   deploySysCd : [{}],  deploySysNm : [{}],  deploySysUrl : [{}],  deploySysDesc : [{}],  deploySysGrpCd : [{}]",
				deploySysCd, deploySysNm, deploySysUrl, deploySysDesc, deploySysGrpCd);

		UiDepolysysbsOut out = depolysysService.getList(deploySysCd, deploySysNm, deploySysUrl, deploySysDesc,
				deploySysGrpCd, pageSize, pageNumber);

		logger.debug(" OUTPUT : {}", out.getTotalCnt());

		return new ResponseEntity<UiDepolysysbsOut>(out, HttpStatus.OK);
	}


	@RequestMapping(value = "/depolysyss/{deploySysCd}", method = RequestMethod.GET)
	public ResponseEntity<DepolysysbsDto> getDepolysys(
			@PathVariable(value = "deploySysCd", required = true) String deploySysCd) {
		logger.debug("INPUT   deploySysCd : [{}]", deploySysCd);

		DepolysysbsDto out = depolysysService.get(deploySysCd);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<DepolysysbsDto>(out, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/depolysyss/getlist", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<List<DepolysysbsDto>> getDepolysysGetlist(
			@RequestBody List<String> deploySysCdList) {

		List<DepolysysbsDto> out = depolysysService.getDeployCodeList(deploySysCdList);


		return new ResponseEntity<List<DepolysysbsDto>>(out, HttpStatus.OK);
	}


	@RequestMapping(value = "/depolysyss", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<Integer> addDepolysys(@RequestBody DepolysysbsDto depolysysbsDto) {
		logger.debug(" INPUT : DepolysysbsDto [{}]", depolysysbsDto);

		int out = depolysysService.add(depolysysbsDto);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<Integer>(out, HttpStatus.CREATED);
	}


	@RequestMapping(value = "/depolysyss", method = RequestMethod.PUT)
	public ResponseEntity<Integer> updateDepolysys(@RequestBody DepolysysbsDto depolysysbsDto) {
		logger.debug(" INPUT : DepolysysbsDto [{}]", depolysysbsDto);

		int out = depolysysService.update(depolysysbsDto);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<Integer>(out, HttpStatus.OK);
	}


	@RequestMapping(value = "/depolysyss/{deploySysCd}", method = RequestMethod.DELETE)
	public ResponseEntity<Integer> deleteDepolysys(
			@PathVariable(value = "deploySysCd", required = true) String deploySysCd) {
		logger.debug("INPUT   deploySysCd : [{}]", deploySysCd);

		int out = depolysysService.delete(deploySysCd);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<Integer>(out, HttpStatus.NO_CONTENT);
	}
}