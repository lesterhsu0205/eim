package eims.web.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eims.web.constants.BxMessages;
import eims.web.dao.CommCodeDao;
import eims.web.dto.table.CommCodeDto;
import eims.web.dto.ui.UiCommCodeOut;
import eims.web.exception.ServiceException;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class CommCodeService {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private CommCodeDao commCodeDao;


	public UiCommCodeOut getList(String cdId, String cdNm, int pageSize, int pageNumber) {
		UiCommCodeOut out = new UiCommCodeOut();
		int totalCount = commCodeDao.selectAllCnt(cdId, cdNm);

		if (totalCount > 0) {
			out.setTotalCnt(totalCount);
			out.setCommCodeDtoList(commCodeDao.selectAll(cdId, cdNm, pageSize, pageNumber));
		}

		return out;
	}


	public CommCodeDto get(String cdId, String cdVal, String langCd) {
		CommCodeDto comCdDto = commCodeDao.selectCommCode(cdId, cdVal, langCd);

		if (comCdDto == null) {
			throw new ServiceException(BxMessages.Error.NOT_FOUNDED, cdId, cdVal, langCd);
		}

		return comCdDto;
	}


	public List<CommCodeDto> getList(String cdId) {
		List<CommCodeDto> out = commCodeDao.selectCommonCodeList(cdId);

		return out;
	}


	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public int add(CommCodeDto in) {
		CommCodeDto curCommCodeInfo = commCodeDao.selectCommCode(in.getCdId(), in.getCdVal(), in.getLangCd());

		if (curCommCodeInfo != null) {
			throw new ServiceException(BxMessages.Error.DUPLICATE_KEY, in.getCdId(), in.getCdVal(), in.getLangCd());
		}

		return commCodeDao.insertCommCode(in);
	}


	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public int update(CommCodeDto in) {
		int out = commCodeDao.updateCommCode(in);

		if (out == 0) {
			throw new ServiceException(BxMessages.Error.NOT_FOUNDED, in.getCdId(), in.getCdVal(), in.getLangCd());
		}

		return out;

	}


	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public int delete(String cdId, String cdVal, String langCd) {
		int out = commCodeDao.deleteCommCode(cdId, cdVal, langCd);

		if (out == 0) {
			throw new ServiceException(BxMessages.Error.NOT_FOUNDED, cdId, cdVal, langCd);
		}

		return out;
	}
}
