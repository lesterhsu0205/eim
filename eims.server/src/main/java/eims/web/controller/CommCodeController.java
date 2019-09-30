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

import eims.web.dto.table.CommCodeDto;
import eims.web.dto.ui.UiCommCodeOut;
import eims.web.service.CommCodeService;

@Controller
public class CommCodeController {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private CommCodeService commCodeService;

	@RequestMapping(value = "/codes", method = RequestMethod.GET)
	public ResponseEntity<UiCommCodeOut> getCommCodes(@RequestParam(value = "cdId", required = false) String cdId,
			@RequestParam(value = "cdNm", required = false) String cdNm,
			@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
			@RequestParam(value = "pageSize", required = false, defaultValue = "1000") int pageSize) {

		logger.debug(" INPUT : cdId : [{}], cdNm : [{}]", cdId, cdNm);

		UiCommCodeOut out = commCodeService.getList(cdId, cdNm, pageSize, pageNumber);

		logger.debug(" OUTPUT : {}", out.getTotalCnt());

		return new ResponseEntity<UiCommCodeOut>(out, HttpStatus.OK);
	}

	@RequestMapping(value = "/codes/common/{cdId}", method = RequestMethod.GET)
	public ResponseEntity<List<CommCodeDto>> getCodeList(@PathVariable(value = "cdId", required = true) String cdId) {
		logger.debug(" INPUT : cdId : [{}]", cdId);

		List<CommCodeDto> out = commCodeService.getList(cdId);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<List<CommCodeDto>>(out, HttpStatus.OK);
	}

	@RequestMapping(value = "/codes/{cdId}", method = RequestMethod.GET)
	public ResponseEntity<CommCodeDto> getCommCode(@PathVariable(value = "cdId", required = true) String cdId,
			@RequestParam(value = "cdVal", required = true) String cdVal,
			@RequestParam(value = "langCd", required = true) String langCd) {

		logger.debug(" INPUT : cdId : [{}], cdVal : [{}], langCd : [{}]", cdId, cdVal, langCd);

		CommCodeDto out = commCodeService.get(cdId, cdVal, langCd);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<CommCodeDto>(out, HttpStatus.OK);
	}

	@RequestMapping(value = "/codes", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<Integer> addCommCode(@RequestBody CommCodeDto commCodeDto) {

		logger.debug(" INPUT : CommCodeDto [{}]", commCodeDto);

		int out = commCodeService.add(commCodeDto);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<Integer>(out, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/codes", method = RequestMethod.PUT)
	public ResponseEntity<Integer> updateCommCode(@RequestBody CommCodeDto commCodeDto) {

		logger.debug(" INPUT : CommCodeDto [{}]", commCodeDto);

		int out = commCodeService.update(commCodeDto);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<Integer>(out, HttpStatus.OK);
	}

	@RequestMapping(value = "/codes/{cdId}", method = RequestMethod.DELETE)
	public ResponseEntity<Integer> deleteCommCode(@PathVariable(value = "cdId", required = true) String cdId,
			@RequestParam(value = "cdVal", required = true) String cdVal,
			@RequestParam(value = "langCd", required = true) String langCd) {

		logger.debug(" INPUT : cdId : [{}], cdVal : [{}], langCd : [{}]", cdId, cdVal, langCd);

		int out = commCodeService.delete(cdId, cdVal, langCd);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<Integer>(out, HttpStatus.NO_CONTENT);
	}
}
