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
import eims.web.dao.PermDao;
import eims.web.dto.table.PermDto;
import eims.web.dto.ui.UiPermOut;
import eims.web.exception.ServiceException;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class PermService {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private PermDao permDao;


	public UiPermOut getList(String permId, String permNm, String permDesc, String permTypeCd, int pageSize,
			int pageNumber) {
		UiPermOut out = new UiPermOut();

		int totalCount = permDao.selectAllCnt(permId, permNm, permDesc, permTypeCd);

		List<PermDto> permList = permDao.selectAll(permId, permNm, permDesc, permTypeCd, pageSize, pageNumber);

		if (permList == null) {
			permList = new ArrayList<PermDto>();
		}

		if (totalCount > 0) {
			out.setTotalCnt(totalCount);
			out.setPermDtoList(permList);
		}

		return out;
	}


	public PermDto get(String permId) {
		PermDto permDto = permDao.selectPerm(permId);

		if (permDto == null) {
			throw new ServiceException(BxMessages.Error.NOT_FOUNDED, permId);
		}

		return permDto;
	}


	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public int add(PermDto in) {
		PermDto curPermInfo = permDao.selectPerm(in.getPermId());

		if (curPermInfo != null) {
			throw new ServiceException(BxMessages.Error.DUPLICATE_KEY, in.getPermId());
		}

		return permDao.insertPerm(in);
	}


	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public int update(PermDto in) {
		int out = permDao.updatePerm(in);

		if (out == 0) {
			throw new ServiceException(BxMessages.Error.NOT_FOUNDED, in.getPermId());
		}

		return out;
	}


	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public int delete(String permId) {
		int out = permDao.deletePerm(permId);

		if (out == 0) {
			throw new ServiceException(BxMessages.Error.NOT_FOUNDED, permId);
		}

		return out;
	}
}
