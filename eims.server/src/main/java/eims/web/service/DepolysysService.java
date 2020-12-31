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
import eims.web.dao.DepolysysbsDao;
import eims.web.dto.table.DepolysysbsDto;
import eims.web.dto.ui.UiDepolysysbsOut;
import eims.web.exception.ServiceException;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class DepolysysService {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private DepolysysbsDao depolysysbsDao;


	public UiDepolysysbsOut getList(String deploySysCd, String deploySysNm, String deploySysUrl, String deploySysDesc,
			String deploySysGrpCd, int pageSize, int pageNumber) {
		UiDepolysysbsOut out = new UiDepolysysbsOut();

		int totalCount = depolysysbsDao.selectAllCnt(deploySysCd, deploySysNm, deploySysUrl, deploySysDesc,
				deploySysGrpCd);

		List<DepolysysbsDto> depolysysbsList = depolysysbsDao.selectAll(deploySysCd, deploySysNm, deploySysUrl,
				deploySysDesc, deploySysGrpCd, pageSize, pageNumber);

		if (depolysysbsList == null) {
			depolysysbsList = new ArrayList<DepolysysbsDto>();
		}

		if (totalCount > 0) {
			out.setTotalCnt(totalCount);
			out.setDepolysysbsOutList(depolysysbsList);
		}

		return out;
	}


	public DepolysysbsDto get(String deploySysCd) {
		DepolysysbsDto out = depolysysbsDao.selectDepolysysbs(deploySysCd);

		if (out == null) {
			throw new ServiceException(BxMessages.Error.NOT_FOUNDED, deploySysCd);
		}

		return out;
	}


	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public int add(DepolysysbsDto in) {
		DepolysysbsDto curDepolysysbsInfo = depolysysbsDao.selectDepolysysbs(in.getDeploySysCd());

		if (curDepolysysbsInfo != null) {
			throw new ServiceException(BxMessages.Error.DUPLICATE_KEY, in.getDeploySysCd());
		}

		return depolysysbsDao.insertDepolysysbs(in);
	}


	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public int update(DepolysysbsDto in) {
		int out = depolysysbsDao.updateDepolysysbs(in);

		if (out == 0) {
			throw new ServiceException(BxMessages.Error.NOT_FOUNDED, in.getDeploySysCd());
		}

		return out;
	}


	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public int delete(String deploySysCd) {
		int out = depolysysbsDao.deleteDepolysysbs(deploySysCd);

		if (out == 0) {
			throw new ServiceException(BxMessages.Error.NOT_FOUNDED, deploySysCd);
		}

		return out;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public List<DepolysysbsDto> getDeployCodeList(List<String> deploySysCdList) {
		List<DepolysysbsDto> result = new ArrayList<DepolysysbsDto>() ;
		
		for(String deployCd : deploySysCdList) {
			DepolysysbsDto dto = depolysysbsDao.selectDepolysysbs(deployCd) ;
			if(dto != null) {
				result.add(dto) ;
			}
		}
		
		
		return result;
	}
}
