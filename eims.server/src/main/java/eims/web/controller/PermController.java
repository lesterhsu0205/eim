package eims.web.controller;

import javax.servlet.http.HttpSession;

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

import eims.web.dto.table.PermDto;
import eims.web.dto.ui.UiPermOut;
import eims.web.service.PermService;

@Controller
public class PermController {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private PermService permService;


	@RequestMapping(value = "/perms", method = RequestMethod.GET)
	public ResponseEntity<UiPermOut> getPerms(@RequestParam(value = "permId", required = false) String permId,
			@RequestParam(value = "permNm", required = false) String permNm,
			@RequestParam(value = "permDesc", required = false) String permDesc,
			@RequestParam(value = "permTypeCd", required = false) String permTypeCd,
			@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
			@RequestParam(value = "pageSize", required = false, defaultValue = "1000") int pageSize) {
		logger.debug("INPUT   permId : [{}],  permNm : [{}],  permDesc : [{}],  permTypeCd : [{}]", permId, permNm,
				permDesc, permTypeCd);

		UiPermOut out = permService.getList(permId, permNm, permDesc, permTypeCd, pageSize, pageNumber);

		logger.debug(" OUTPUT : {}", out.getTotalCnt());

		return new ResponseEntity<UiPermOut>(out, HttpStatus.OK);
	}


	@RequestMapping(value = "/perms/{permId}", method = RequestMethod.GET)
	public ResponseEntity<PermDto> getPerm(@PathVariable(value = "permId", required = true) String permId,
			HttpSession session) {

		logger.debug(" INPUT : permId : [{}]", permId);

		PermDto out = permService.get(permId);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<PermDto>(out, HttpStatus.OK);
	}


	@RequestMapping(value = "/perms", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<Integer> addPerm(@RequestBody PermDto permDto, HttpSession session) {

		logger.debug(" INPUT : PermDto [{}]", permDto);

		int out = permService.add(permDto);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<Integer>(out, HttpStatus.CREATED);
	}


	@RequestMapping(value = "/perms", method = RequestMethod.PUT)
	public ResponseEntity<Integer> updatePerm(@RequestBody PermDto permDto, HttpSession session) {

		logger.debug(" INPUT : PermDto [{}]", permDto);

		int out = permService.update(permDto);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<Integer>(out, HttpStatus.OK);
	}


	@RequestMapping(value = "/perms/{permId}", method = RequestMethod.DELETE)
	public ResponseEntity<Integer> deletePerm(@PathVariable(value = "permId", required = true) String permId,
			HttpSession session) {

		logger.debug(" INPUT : permId : [{}]", permId);

		int out = permService.delete(permId);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<Integer>(out, HttpStatus.NO_CONTENT);
	}
}
