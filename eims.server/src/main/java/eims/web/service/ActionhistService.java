package eims.web.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eims.web.dao.ActionhisthsDao;
import eims.web.dao.IntrfccombsDao;
import eims.web.dto.table.ActionhisthsDto;
import eims.web.dto.table.IntrfccombsDto;
import eims.web.dto.ui.UiActionhisthsOut;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class ActionhistService {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ActionhisthsDao actionhisthsDao;

	@Autowired
	private IntrfccombsDao intrfccombsDao;

	public UiActionhisthsOut getList(String hstDscd, String itemId, String workCttCd, String workDttmFrom, String workDttmTo, String userId,
			String itemDesc, int pageSize, int pageNumber) {
		UiActionhisthsOut out = new UiActionhisthsOut();

		int totalCount = actionhisthsDao.selectAllCnt(hstDscd, itemId, workCttCd, workDttmFrom, workDttmTo, userId, itemDesc);

		List<ActionhisthsDto> actionhisthsList = actionhisthsDao.selectAll(hstDscd, itemId, workCttCd, workDttmFrom, workDttmTo, userId,
				itemDesc, pageSize, pageNumber);

		for (ActionhisthsDto action : actionhisthsList) {
			if (action.getHstDscd().equals("INTERFACE")) {
				String interfaceId = action.getItemId();
				IntrfccombsDto intrfccombsDto = intrfccombsDao.selectIntrfccombs(interfaceId);
				if (intrfccombsDto != null) {
					action.setIntrfcType(intrfccombsDto.getIntrfcTypeCd());
				}
			}
		}

		if (totalCount > 0) {
			out.setTotalCnt(totalCount);
			out.setActionhisthsOutList(actionhisthsList);
		}

		return out;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public int add(ActionhisthsDto in) {

		return actionhisthsDao.insertActionhisths(in);
	}
}
