package eims.web.controller;

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

import eims.web.dto.table.SrsysbsDto;
import eims.web.dto.ui.UiSrsysbsOut;
import eims.web.service.SrsysService;

@Controller
public class SrsysController {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private SrsysService srsysService;

	@RequestMapping(value = "/srsyss", method = RequestMethod.GET)
	public ResponseEntity<UiSrsysbsOut> getSrsyss(@RequestParam(value = "sysCd", required = false) String sysCd, @RequestParam(value = "sysNm", required = false) String sysNm, @RequestParam(value = "sysCdDesc", required = false) String sysCdDesc,
			@RequestParam(value = "crgManNm", required = false) String crgManNm,
			@RequestParam(value = "noncoreYn", required = false) String noncoreYn,
			@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
			@RequestParam(value = "pageSize", required = false, defaultValue = "1000") int pageSize) {
		logger.debug("INPUT   sysCd : [{}],  sysNm : [{}],  sysCdDesc : [{}]", sysCd, sysNm, sysCdDesc);

		UiSrsysbsOut out = srsysService.getList(sysCd, sysNm, sysCdDesc, crgManNm, noncoreYn, pageSize, pageNumber);

		logger.debug(" OUTPUT : {}", out.getTotalCnt());

		return new ResponseEntity<UiSrsysbsOut>(out, HttpStatus.OK);
	}

	@RequestMapping(value = "/srsyss/{sysCd}", method = RequestMethod.GET)
	public ResponseEntity<SrsysbsDto> getSrsys(@PathVariable(value = "sysCd", required = true) String sysCd) {

		logger.debug("INPUT   sysCd : [{}]", sysCd);
		SrsysbsDto out;

		out = srsysService.get(sysCd);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<SrsysbsDto>(out, HttpStatus.OK);
	}

	@RequestMapping(value = "/srsyss", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<Integer> addSrsys(@RequestBody SrsysbsDto srsysbsDto) {

		logger.debug(" INPUT : SrsysbsDto [{}]", srsysbsDto);

		int out;

		out = srsysService.add(srsysbsDto);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<Integer>(out, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/srsyss", method = RequestMethod.PUT)
	public ResponseEntity<Integer> updateSrsys(@RequestBody SrsysbsDto srsysbsDto) {

		logger.debug(" INPUT : SrsysbsDto [{}]", srsysbsDto);

		int out;

		out = srsysService.update(srsysbsDto);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<Integer>(out, HttpStatus.OK);
	}

	@RequestMapping(value = "/srsyss/{sysCd}", method = RequestMethod.DELETE)
	public ResponseEntity<Integer> deleteSrsys(@PathVariable(value = "sysCd", required = true) String sysCd) {

		logger.debug("INPUT   sysCd : [{}]", sysCd);

		int out;

		out = srsysService.delete(sysCd);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<Integer>(out, HttpStatus.NO_CONTENT);
	}
}