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

import eims.web.dto.table.MappingfuncbsDto;
import eims.web.dto.ui.UiMappingfuncbsOut;
import eims.web.service.MappingfuncService;

@Controller
public class MappingfuncController {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private MappingfuncService mappingfuncService;


	@RequestMapping(value = "/mappingfuncs", method = RequestMethod.GET)
	public ResponseEntity<UiMappingfuncbsOut> getMappingfuncs(
			@RequestParam(value = "mappingFuncNm", required = false) String mappingFuncNm,
			@RequestParam(value = "argsCnt", required = false, defaultValue = "0") int argsCnt,
			@RequestParam(value = "guideDesc", required = false) String guideDesc,
			@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
			@RequestParam(value = "pageSize", required = false, defaultValue = "1000") int pageSize) {
		logger.debug("INPUT   mappingFuncNm : [{}],  argsCnt : [{}],  guideDesc : [{}]", mappingFuncNm, argsCnt,
				guideDesc);

		UiMappingfuncbsOut out = mappingfuncService.getList(mappingFuncNm, argsCnt, guideDesc, pageSize, pageNumber);

		logger.debug(" OUTPUT : {}", out.getTotalCnt());

		return new ResponseEntity<UiMappingfuncbsOut>(out, HttpStatus.OK);
	}


	@RequestMapping(value = "/mappingfuncs/{mappingFuncNm}", method = RequestMethod.GET)
	public ResponseEntity<MappingfuncbsDto> getMappingfunc(
			@PathVariable(value = "mappingFuncNm", required = true) String mappingFuncNm) {

		logger.debug("INPUT   mappingFuncNm : [{}]", mappingFuncNm);
		MappingfuncbsDto out;

		out = mappingfuncService.get(mappingFuncNm);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<MappingfuncbsDto>(out, HttpStatus.OK);
	}


	@RequestMapping(value = "/mappingfuncs", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<Integer> addMappingfunc(@RequestBody MappingfuncbsDto mappingfuncbsDto) {

		logger.debug(" INPUT : MappingfuncbsDto [{}]", mappingfuncbsDto);

		int out;

		out = mappingfuncService.add(mappingfuncbsDto);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<Integer>(out, HttpStatus.CREATED);
	}


	@RequestMapping(value = "/mappingfuncs", method = RequestMethod.PUT)
	public ResponseEntity<Integer> updateMappingfunc(@RequestBody MappingfuncbsDto mappingfuncbsDto) {

		logger.debug(" INPUT : MappingfuncbsDto [{}]", mappingfuncbsDto);

		int out;

		out = mappingfuncService.update(mappingfuncbsDto);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<Integer>(out, HttpStatus.OK);
	}


	@RequestMapping(value = "/mappingfuncs/{mappingFuncNm}", method = RequestMethod.DELETE)
	public ResponseEntity<Integer> deleteMappingfunc(
			@PathVariable(value = "mappingFuncNm", required = true) String mappingFuncNm) {

		logger.debug("INPUT   mappingFuncNm : [{}]", mappingFuncNm);

		int out;

		out = mappingfuncService.delete(mappingFuncNm);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<Integer>(out, HttpStatus.NO_CONTENT);
	}
}