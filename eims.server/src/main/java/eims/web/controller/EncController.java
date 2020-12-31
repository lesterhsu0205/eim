package eims.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import eims.web.dto.ui.UiEncOut;
import eims.web.dto.ui.UiMaskOut;
import eims.web.service.EncService;

@Controller
public class EncController {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private EncService encService;

	@RequestMapping(value = "/encCd", method = RequestMethod.GET)
	public ResponseEntity<UiEncOut> getEnckcd() {

		UiEncOut out = encService.getList();

		logger.debug(" OUTPUT : {}", out.getTotalCnt());

		return new ResponseEntity<UiEncOut>(out, HttpStatus.OK);
	}
}