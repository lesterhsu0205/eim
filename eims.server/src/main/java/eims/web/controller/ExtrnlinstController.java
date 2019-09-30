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

import eims.web.dto.table.ExtrnlinstcdDto;
import eims.web.dto.ui.UiExtrnlinstcdOut;
import eims.web.service.ExtrnlinstService;

@Controller
public class ExtrnlinstController {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ExtrnlinstService extrnlinstService;


	@RequestMapping(value = "/extrnlinsts", method = RequestMethod.GET)
	public ResponseEntity<UiExtrnlinstcdOut> getExtrnlinsts(
			@RequestParam(value = "instCd", required = false) String instCd,
			@RequestParam(value = "instCdNm", required = false) String instCdNm,
			@RequestParam(value = "instDstnctnVal", required = false) String instDstnctnVal,
			@RequestParam(value = "instCdDesc", required = false) String instCdDesc,
			@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
			@RequestParam(value = "pageSize", required = false, defaultValue = "1000") int pageSize) {
		logger.debug("INPUT   instCd : [{}],  instCdNm : [{}],  instDstnctnVal : [{}],  instCdDesc : [{}]", instCd,
				instCdNm, instDstnctnVal, instCdDesc);

		UiExtrnlinstcdOut out = extrnlinstService.getList(instCd, instCdNm, instDstnctnVal, instCdDesc, pageSize,
				pageNumber);

		logger.debug(" OUTPUT : {}", out.getTotalCnt());

		return new ResponseEntity<UiExtrnlinstcdOut>(out, HttpStatus.OK);
	}


	@RequestMapping(value = "/extrnlinsts/{instCd}", method = RequestMethod.GET)
	public ResponseEntity<ExtrnlinstcdDto> getExtrnlinst(@PathVariable(value = "instCd", required = true) String instCd,
			@RequestParam(value = "instDstnctnVal", required = false) String instDstnctnVal) {

		logger.debug("INPUT   instCd : [{}],  instDstnctnVal : [{}]", instCd, instDstnctnVal);
		ExtrnlinstcdDto out;

		out = extrnlinstService.get(instCd, instDstnctnVal);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<ExtrnlinstcdDto>(out, HttpStatus.OK);
	}


	@RequestMapping(value = "/extrnlinsts", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<Integer> addExtrnlinst(@RequestBody ExtrnlinstcdDto extrnlinstcdDto) {

		logger.debug(" INPUT : ExtrnlinstcdDto [{}]", extrnlinstcdDto);

		int out;

		out = extrnlinstService.add(extrnlinstcdDto);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<Integer>(out, HttpStatus.CREATED);
	}


	@RequestMapping(value = "/extrnlinsts", method = RequestMethod.PUT)
	public ResponseEntity<Integer> updateExtrnlinst(@RequestBody ExtrnlinstcdDto extrnlinstcdDto) {

		logger.debug(" INPUT : ExtrnlinstcdDto [{}]", extrnlinstcdDto);

		int out;

		out = extrnlinstService.update(extrnlinstcdDto);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<Integer>(out, HttpStatus.OK);
	}


	@RequestMapping(value = "/extrnlinsts/{instCd}", method = RequestMethod.DELETE)
	public ResponseEntity<Integer> deleteExtrnlinst(@PathVariable(value = "instCd", required = true) String instCd,
			@RequestParam(value = "instDstnctnVal", required = false) String instDstnctnVal) {

		logger.debug("INPUT   instCd : [{}],  instDstnctnVal : [{}]", instCd, instDstnctnVal);

		int out;

		out = extrnlinstService.delete(instCd, instDstnctnVal);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<Integer>(out, HttpStatus.NO_CONTENT);
	}
}