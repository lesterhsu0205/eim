package eims.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import eims.web.dto.ui.UiMaskOut;
import eims.web.dto.ui.UiSrsysbsOut;
import eims.web.service.MaskService;

@Controller
public class MaskController {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private MaskService maskService;

	@RequestMapping(value = "/maskCd", method = RequestMethod.GET)
	public ResponseEntity<UiMaskOut> getMaskcd() {

		UiMaskOut out = maskService.getList();

		logger.debug(" OUTPUT : {}", out.getTotalCnt());

		return new ResponseEntity<UiMaskOut>(out, HttpStatus.OK);
	}
}