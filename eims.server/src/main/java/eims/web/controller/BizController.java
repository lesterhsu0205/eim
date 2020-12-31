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

import eims.web.dto.table.BizcdDto;
import eims.web.dto.ui.UiBizcdOut;
import eims.web.service.BizService;

@Controller
public class BizController {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private BizService bizService;


	@RequestMapping(value = "/bizs", method = RequestMethod.GET)
	public ResponseEntity<UiBizcdOut> getBizs(@RequestParam(value = "bizCd", required = false) String bizCd,
			@RequestParam(value = "bizCdNm", required = false) String bizCdNm,
			@RequestParam(value = "bizCdDesc", required = false) String bizCdDesc,
			@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
			@RequestParam(value = "pageSize", required = false, defaultValue = "1000") int pageSize) {
		logger.debug("INPUT   bizCd : [{}],  bizCdNm : [{}],  bizCdDesc : [{}]", bizCd, bizCdNm, bizCdDesc);

		UiBizcdOut out = bizService.getList(bizCd, bizCdNm, bizCdDesc, pageSize, pageNumber);

		logger.debug(" OUTPUT : {}", out.getTotalCnt());

		return new ResponseEntity<UiBizcdOut>(out, HttpStatus.OK);
	}


	@RequestMapping(value = "/bizs/{bizCd}", method = RequestMethod.GET)
	public ResponseEntity<BizcdDto> getBiz(@PathVariable(value = "bizCd", required = true) String bizCd) {

		logger.debug("INPUT   bizCd : [{}]", bizCd);
		BizcdDto out;

		out = bizService.get(bizCd);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<BizcdDto>(out, HttpStatus.OK);
	}


	@RequestMapping(value = "/bizs", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<Integer> addBiz(@RequestBody BizcdDto bizcdDto) {

		logger.debug(" INPUT : BizcdDto [{}]", bizcdDto);

		int out;

		out = bizService.add(bizcdDto);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<Integer>(out, HttpStatus.CREATED);
	}


	@RequestMapping(value = "/bizs", method = RequestMethod.PUT)
	public ResponseEntity<Integer> updateBiz(@RequestBody BizcdDto bizcdDto) {

		logger.debug(" INPUT : BizcdDto [{}]", bizcdDto);

		int out;

		out = bizService.update(bizcdDto);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<Integer>(out, HttpStatus.OK);
	}


	@RequestMapping(value = "/bizs/{bizCd}", method = RequestMethod.DELETE)
	public ResponseEntity<Integer> deleteBiz(@PathVariable(value = "bizCd", required = true) String bizCd) {

		logger.debug("INPUT   bizCd : [{}]", bizCd);

		int out;

		out = bizService.delete(bizCd);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<Integer>(out, HttpStatus.NO_CONTENT);
	}
}