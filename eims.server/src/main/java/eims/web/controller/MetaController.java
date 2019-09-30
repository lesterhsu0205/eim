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

import eims.web.dto.table.MetaEffectDto;
import eims.web.dto.table.MetabsDto;
import eims.web.dto.ui.UiMetabsOut;
import eims.web.service.MetaService;

@Controller
public class MetaController {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private MetaService metaService;

	@RequestMapping(value = "/metas", method = RequestMethod.GET)
	public ResponseEntity<UiMetabsOut> getMetas(@RequestParam(value = "metaEngNm", required = false) String metaEngNm, @RequestParam(value = "metaKorNm", required = false) String metaKorNm, @RequestParam(value = "dataTypeNm", required = false) String dataTypeNm,
			@RequestParam(value = "metaLen", required = false, defaultValue = "0") int metaLen, @RequestParam(value = "decimalLen", required = false, defaultValue = "0") int decimalLen,
			@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
			@RequestParam(value = "pageSize", required = false, defaultValue = "1000") int pageSize) {
		logger.debug("INPUT   metaEngNm : [{}],  metaKorNm : [{}],  dataTypeNm : [{}],  metaLen : [{}],  decimalLen : [{}]", metaEngNm, metaKorNm, dataTypeNm, metaLen, decimalLen);

		UiMetabsOut out = metaService.getList(metaEngNm, metaKorNm, dataTypeNm, metaLen, decimalLen, pageSize, pageNumber);

		logger.debug(" OUTPUT : {}", out.getTotalCnt());

		return new ResponseEntity<UiMetabsOut>(out, HttpStatus.OK);
	}

	@RequestMapping(value = "/metas/{metaEngNm}", method = RequestMethod.GET)
	public ResponseEntity<MetabsDto> getMeta(@PathVariable(value = "metaEngNm", required = true) String metaEngNm) {
		logger.debug("INPUT   metaEngNm : [{}]", metaEngNm);

		MetabsDto out = metaService.get(metaEngNm);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<MetabsDto>(out, HttpStatus.OK);
	}

	@RequestMapping(value = "/metas", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<Integer> addMeta(@RequestBody MetabsDto metabsDto) {
		logger.debug(" INPUT : MetabsDto [{}]", metabsDto);

		int out = metaService.add(metabsDto);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<Integer>(out, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/metas", method = RequestMethod.PUT)
	public ResponseEntity<Integer> updateMeta(@RequestBody MetabsDto metabsDto) {
		logger.debug(" INPUT : MetabsDto [{}]", metabsDto);

		int out = metaService.update(metabsDto);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<Integer>(out, HttpStatus.OK);
	}

	@RequestMapping(value = "/metas/{metaEngNm}", method = RequestMethod.DELETE)
	public ResponseEntity<Integer> deleteMeta(@PathVariable(value = "metaEngNm", required = true) String metaEngNm) {
		logger.debug("INPUT   metaEngNm : [{}]", metaEngNm);

		int out = metaService.delete(metaEngNm);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<Integer>(out, HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/metas/effects", method = RequestMethod.GET)
	public ResponseEntity<List<MetaEffectDto>> getMetaEffect(@RequestParam(value = "metaEngNm", required = false) String metaEngNm, @RequestParam(value = "metaKorNm", required = false) String metaKorNm, @RequestParam(value = "len", required = false, defaultValue = "0") int len,
			@RequestParam(value = "scaleVal", required = false) String scaleVal, @RequestParam(value = "dataTypeNm", required = false) String dataTypeNm,
			@RequestParam(value = "intrfcId", required = false) String intrfcId,
			@RequestParam(value = "intrfcNm", required = false) String intrfcNm) {
		logger.debug("INPUT   metaEngNm : [{}],  metaKorNm : [{}],  len : [{}],  scaleVal : [{}],  dataTypeNm : [{}]", metaEngNm, metaKorNm, len, scaleVal, dataTypeNm);

		List<MetaEffectDto> out = metaService.getEffectList(metaEngNm, metaKorNm, len, scaleVal, dataTypeNm, intrfcId, intrfcNm);

		return new ResponseEntity<List<MetaEffectDto>>(out, HttpStatus.OK);
	}

	@RequestMapping(value = "/metas/syncs", method = RequestMethod.GET)
	public ResponseEntity<Integer> getMetaSync() {
		boolean roleCheck = true ;
		int out = metaService.syncMeta(roleCheck);
		return new ResponseEntity<Integer>(out, HttpStatus.OK);
	}

}