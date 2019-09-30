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

import eims.web.dto.table.TrxcdDto;
import eims.web.dto.ui.UiTrxcdOut;
import eims.web.service.TrxService;

@Controller
public class TrxController {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private TrxService trxService;


	@RequestMapping(value = "/trxs", method = RequestMethod.GET)
	public ResponseEntity<UiTrxcdOut> getTrxs(@RequestParam(value = "trxCd", required = false) String trxCd,
			@RequestParam(value = "trxCdNm", required = false) String trxCdNm,
			@RequestParam(value = "mngSysCd", required = false) String mngSysCd,
			@RequestParam(value = "trxCdDesc", required = false) String trxCdDesc,
			@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
			@RequestParam(value = "pageSize", required = false, defaultValue = "1000") int pageSize) {
		logger.debug("INPUT   trxCd : [{}],  trxCdNm : [{}],  mngSysCd : [{}],  trxCdDesc : [{}]", trxCd, trxCdNm,
				mngSysCd, trxCdDesc);

		UiTrxcdOut out = trxService.getList(trxCd, trxCdNm, mngSysCd, trxCdDesc, pageSize, pageNumber);

		logger.debug(" OUTPUT : {}", out.getTotalCnt());

		return new ResponseEntity<UiTrxcdOut>(out, HttpStatus.OK);
	}


	@RequestMapping(value = "/trxs/{trxCd}", method = RequestMethod.GET)
	public ResponseEntity<TrxcdDto> getTrx(@PathVariable(value = "trxCd", required = true) String trxCd,
			@RequestParam(value = "mngSysCd", required = false) String mngSysCd) {

		logger.debug("INPUT   trxCd : [{}],  mngSysCd : [{}]", trxCd, mngSysCd);
		TrxcdDto out;

		out = trxService.get(trxCd, mngSysCd);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<TrxcdDto>(out, HttpStatus.OK);
	}


	@RequestMapping(value = "/trxs", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<Integer> addTrx(@RequestBody TrxcdDto trxcdDto) {

		logger.debug(" INPUT : TrxcdDto [{}]", trxcdDto);

		int out;

		out = trxService.add(trxcdDto);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<Integer>(out, HttpStatus.CREATED);
	}


	@RequestMapping(value = "/trxs", method = RequestMethod.PUT)
	public ResponseEntity<Integer> updateTrx(@RequestBody TrxcdDto trxcdDto) {

		logger.debug(" INPUT : TrxcdDto [{}]", trxcdDto);

		int out;

		out = trxService.update(trxcdDto);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<Integer>(out, HttpStatus.OK);
	}


	@RequestMapping(value = "/trxs/{trxCd}", method = RequestMethod.DELETE)
	public ResponseEntity<Integer> deleteTrx(@PathVariable(value = "trxCd", required = true) String trxCd,
			@RequestParam(value = "mngSysCd", required = false) String mngSysCd) {

		logger.debug("INPUT   trxCd : [{}],  mngSysCd : [{}]", trxCd, mngSysCd);

		int out;

		out = trxService.delete(trxCd, mngSysCd);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<Integer>(out, HttpStatus.NO_CONTENT);
	}
}