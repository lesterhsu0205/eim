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
import eims.web.dao.MappingfuncbsDao;
import eims.web.dto.table.MappingfuncbsDto;
import eims.web.dto.ui.UiMappingfuncbsOut;
import eims.web.exception.ServiceException;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class MappingfuncService {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private MappingfuncbsDao mappingfuncbsDao;


	public UiMappingfuncbsOut getList(String mappingFuncNm, int argsCnt, String guideDesc, int pageSize,
			int pageNumber) {
		UiMappingfuncbsOut out = new UiMappingfuncbsOut();

		int totalCount = mappingfuncbsDao.selectAllCnt(mappingFuncNm, argsCnt, guideDesc);

		List<MappingfuncbsDto> mappingfuncbsList = mappingfuncbsDao.selectAll(mappingFuncNm, argsCnt, guideDesc,
				pageSize, pageNumber);

		if (mappingfuncbsList == null) {
			mappingfuncbsList = new ArrayList<MappingfuncbsDto>();
		}

		if (totalCount > 0) {
			out.setTotalCnt(totalCount);
			out.setMappingfuncbsOutList(mappingfuncbsList);
		}

		return out;
	}


	public MappingfuncbsDto get(String mappingFuncNm) {
		MappingfuncbsDto out = mappingfuncbsDao.selectMappingfuncbs(mappingFuncNm);

		if (out == null) {
			throw new ServiceException(BxMessages.Error.NOT_FOUNDED, mappingFuncNm);
		}
		return out;
	}


	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public int add(MappingfuncbsDto in) {
		MappingfuncbsDto curMappingfuncbsInfo = mappingfuncbsDao.selectMappingfuncbs(in.getMappingFuncNm());

		if (curMappingfuncbsInfo != null) {
			throw new ServiceException(BxMessages.Error.DUPLICATE_KEY, in.getMappingFuncNm());
		}

		return mappingfuncbsDao.insertMappingfuncbs(in);
	}


	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public int update(MappingfuncbsDto in) {
		int out;

		out = mappingfuncbsDao.updateMappingfuncbs(in);
		if (out == 0) {
			throw new ServiceException(BxMessages.Error.NOT_FOUNDED, in.getMappingFuncNm());
		}

		return out;
	}


	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public int delete(String mappingFuncNm) {
		int result;

		result = mappingfuncbsDao.deleteMappingfuncbs(mappingFuncNm);
		if (result == 0) {
			throw new ServiceException(BxMessages.Error.NOT_FOUNDED, mappingFuncNm);
		}
		return result;

	}

}
