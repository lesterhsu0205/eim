package eims.web.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eims.web.constants.BxMessages;
import eims.web.dao.TrxcdDao;
import eims.web.dto.table.TrxcdDto;
import eims.web.dto.ui.UiTrxcdOut;
import eims.web.exception.ServiceException;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class TrxService {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private TrxcdDao trxcdDao;


	public UiTrxcdOut getList(String trxCd, String trxCdNm, String mngSysCd, String trxCdDesc, int pageSize,
			int pageNumber) {
		UiTrxcdOut out = new UiTrxcdOut();

		int totalCount = trxcdDao.selectAllCnt(trxCd, trxCdNm, mngSysCd, trxCdDesc);

		List<TrxcdDto> trxcdList = trxcdDao.selectAll(trxCd, trxCdNm, mngSysCd, trxCdDesc, pageSize, pageNumber);

		if (trxcdList == null) {
			trxcdList = new ArrayList<TrxcdDto>();
		}

		if (totalCount > 0) {
			out.setTotalCnt(totalCount);
			out.setTrxcdOutList(trxcdList);
		}

		return out;
	}


	public TrxcdDto get(String trxCd, String mngSysCd) {
		TrxcdDto out = trxcdDao.selectTrxcd(trxCd, mngSysCd);

		if (out == null) {
			throw new ServiceException(BxMessages.Error.NOT_FOUNDED, trxCd, mngSysCd);
		}
		return out;
	}


	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public int add(TrxcdDto in) {
		String mngSysCd = in.getMngSysCd() ;
		
		if(mngSysCd.equals("FW")) {
			mngSysCd = "COO" ;
			in.setMngSysCd(mngSysCd);
		}
		
		TrxcdDto curTrxcdInfo = trxcdDao.selectTrxcd(in.getTrxCd(), mngSysCd);

		if (curTrxcdInfo != null) {
			throw new ServiceException(BxMessages.Error.DUPLICATE_KEY, in.getTrxCd(), in.getMngSysCd());
		}

		return trxcdDao.insertTrxcd(in);
	}


	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public int update(TrxcdDto in) {
		int out;
		
		String mngSysCd = in.getMngSysCd() ;
		
		if(mngSysCd.equals("FW")) {
			mngSysCd = "COO" ;
			in.setMngSysCd(mngSysCd);
		}

		out = trxcdDao.updateTrxcd(in);
		if (out == 0) {
			throw new ServiceException(BxMessages.Error.NOT_FOUNDED, in.getTrxCd(), in.getMngSysCd());
		}

		return out;
	}


	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public int delete(String trxCd, String mngSysCd) {
		int result;
		
		
		if(mngSysCd.equals("FW")) {
			mngSysCd = "COO" ;
		}

		result = trxcdDao.deleteTrxcd(trxCd, mngSysCd);
		if (result == 0) {
			throw new ServiceException(BxMessages.Error.NOT_FOUNDED, trxCd, mngSysCd);
		}
		return result;

	}

}
