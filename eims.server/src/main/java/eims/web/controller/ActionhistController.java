package eims.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import eims.web.dto.table.ActionhisthsDto;
import eims.web.dto.ui.UiActionhisthsOut;
import eims.web.service.ActionhistService;

@Controller
public class ActionhistController {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ActionhistService actionhistService;

	@RequestMapping(value = "/actionhists", method = RequestMethod.GET)
	public ResponseEntity<UiActionhisthsOut> getActionhists(
			@RequestParam(value = "hstDscd", required = false) String hstDscd,
			@RequestParam(value = "itemId", required = false) String itemId,
			@RequestParam(value = "workCttCd", required = false) String workCttCd,
			@RequestParam(value = "workDttmFrom", required = false) String workDttmFrom,
			@RequestParam(value = "workDttmTo", required = false) String workDttmTo,
			@RequestParam(value = "userId", required = false) String userId,
			@RequestParam(value = "itemDesc", required = false) String itemDesc,
			@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
			@RequestParam(value = "pageSize", required = false, defaultValue = "1000") int pageSize) {

		UiActionhisthsOut out = actionhistService.getList(hstDscd, itemId, workCttCd, workDttmFrom, workDttmTo, userId, itemDesc, pageSize, pageNumber);

		logger.debug(" OUTPUT : {}", out.getTotalCnt());

		return new ResponseEntity<UiActionhisthsOut>(out, HttpStatus.OK);
	}

	@RequestMapping(value = "/actionhists", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<Integer> addActionhist(@RequestBody ActionhisthsDto actionhisthsDto) {
		logger.debug(" INPUT : ActionhisthsDto [{}]", actionhisthsDto);

		int out = actionhistService.add(actionhisthsDto);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<Integer>(out, HttpStatus.CREATED);
	}

}