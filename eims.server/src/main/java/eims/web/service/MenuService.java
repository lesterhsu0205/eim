package eims.web.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eims.web.constants.BxMessages;
import eims.web.dao.MenuDao;
import eims.web.dto.table.MenuDto;
import eims.web.dto.ui.UiMenuTreeInfo;
import eims.web.exception.ServiceException;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class MenuService {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private MenuDao menuDao;

	public List<UiMenuTreeInfo> getTreeList() {
		List<UiMenuTreeInfo> out = menuDao.selectAll();

		return out;
	}

	public MenuDto get(String menuId) {
		MenuDto menuDto = menuDao.selectMenu(menuId);

		if (menuDto == null) {
			throw new ServiceException(BxMessages.Error.NOT_FOUNDED, menuId);
		}

		return menuDto;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public int add(MenuDto in) {
		MenuDto curMenuInfo = menuDao.selectMenu(in.getMenuId());

		if (curMenuInfo != null) {
			throw new ServiceException(BxMessages.Error.DUPLICATE_KEY, in.getMenuId());
		}

		return menuDao.insertMenu(in);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public int update(MenuDto in) {
		int out = menuDao.updateMenu(in);

		if (out == 0) {
			throw new ServiceException(BxMessages.Error.NOT_FOUNDED, in.getMenuId());
		}

		return out;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public int delete(String menuId) {
		int out = menuDao.deleteMenu(menuId);

		if (out == 0) {
			throw new ServiceException(BxMessages.Error.NOT_FOUNDED, menuId);
		}

		return out;
	}
}
