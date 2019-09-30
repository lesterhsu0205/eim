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

import eims.web.dto.table.AppcdDto;
import eims.web.dto.ui.UiAppcdOut;
import eims.web.service.AppService;

@Controller
public class AppController {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private AppService appService;


	@RequestMapping(value = "/apps", method = RequestMethod.GET)
	public ResponseEntity<UiAppcdOut> getApps(@RequestParam(value = "appCd", required = false) String appCd,
			@RequestParam(value = "appCdNm", required = false) String appCdNm,
			@RequestParam(value = "parentAppCd", required = false) String parentAppCd,
			@RequestParam(value = "lvCd", required = false) String lvCd,
			@RequestParam(value = "alignOrderNo", required = false, defaultValue = "0") int alignOrderNo,
			@RequestParam(value = "appCdDesc", required = false) String appCdDesc,
			@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
			@RequestParam(value = "pageSize", required = false, defaultValue = "1000") int pageSize) {
		logger.debug(
				"INPUT   appCd : [{}],  appCdNm : [{}],  parentAppCd : [{}],  lvCd : [{}],  alignOrderNo : [{}],  appCdDesc : [{}]",
				appCd, appCdNm, parentAppCd, lvCd, alignOrderNo, appCdDesc);

		UiAppcdOut out = appService.getList(appCd, appCdNm, parentAppCd, lvCd, alignOrderNo, appCdDesc, pageSize,
				pageNumber);

		logger.debug(" OUTPUT : {}", out.getTotalCnt());

		return new ResponseEntity<UiAppcdOut>(out, HttpStatus.OK);
	}


	@RequestMapping(value = "/apps/{appCd}", method = RequestMethod.GET)
	public ResponseEntity<AppcdDto> getApp(@PathVariable(value = "appCd", required = true) String appCd,
			@RequestParam(value = "parentAppCd", required = false) String parentAppCd,
			@RequestParam(value = "lvCd", required = false) String lvCd) {

		logger.debug("INPUT   appCd : [{}],  parentAppCd : [{}],  lvCd : [{}]", appCd, parentAppCd, lvCd);
		AppcdDto out;

		out = appService.get(appCd, parentAppCd, lvCd);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<AppcdDto>(out, HttpStatus.OK);
	}


	@RequestMapping(value = "/apps", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<Integer> addApp(@RequestBody AppcdDto appcdDto) {

		logger.debug(" INPUT : AppcdDto [{}]", appcdDto);

		int out;

		out = appService.add(appcdDto);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<Integer>(out, HttpStatus.CREATED);
	}


	@RequestMapping(value = "/apps", method = RequestMethod.PUT)
	public ResponseEntity<Integer> updateApp(@RequestBody AppcdDto appcdDto) {

		logger.debug(" INPUT : AppcdDto [{}]", appcdDto);

		int out;

		out = appService.update(appcdDto);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<Integer>(out, HttpStatus.OK);
	}


	@RequestMapping(value = "/apps/{appCd}", method = RequestMethod.DELETE)
	public ResponseEntity<Integer> deleteApp(@PathVariable(value = "appCd", required = true) String appCd,
			@RequestParam(value = "parentAppCd", required = false) String parentAppCd,
			@RequestParam(value = "lvCd", required = false) String lvCd) {

		logger.debug("INPUT   appCd : [{}],  parentAppCd : [{}],  lvCd : [{}]", appCd, parentAppCd, lvCd);

		int out;

		out = appService.delete(appCd, parentAppCd, lvCd);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<Integer>(out, HttpStatus.NO_CONTENT);
	}
}