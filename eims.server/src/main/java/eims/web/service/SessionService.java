package eims.web.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eims.ServiceContext;
import eims.web.constants.BxConstants;
import eims.web.constants.BxMessages;
import eims.web.dao.MenuDao;
import eims.web.dao.PermDao;
import eims.web.dao.RoleDao;
import eims.web.dao.UserDao;
import eims.web.dto.CommonResponse;
import eims.web.dto.LoginUserInfo;
import eims.web.dto.SessionInfo;
import eims.web.dto.UserInfo;
import eims.web.dto.table.ChangePwdInfo;
import eims.web.dto.table.RoleDto;
import eims.web.dto.table.UserDto;
import eims.web.dto.ui.UiMenuTreeInfo;
import eims.web.exception.BXRuntimeSystemException;
import eims.web.exception.ServiceException;
import eims.web.utils.CryptoUtils;

@Service
public class SessionService {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private UserDao userDao;

	@Autowired
	private MenuDao menuDao;

	@Autowired
	private PermDao permDao;

	@Autowired
	private RoleDao roleDao;

	public CommonResponse authenticateUser(SessionInfo sessionInfo, HttpSession session) {

		UserDto userDto = userDao.selectUser(sessionInfo.getUserId());
		// 존재하지 않은 ID
		if (userDto == null) {
			throw new ServiceException(BxMessages.Error.INVALID_ID);
		}

		try {
			// 비밀번호 불일치
			if (!userDto.getUserPwd().equals(CryptoUtils.instance().encrypt(sessionInfo.getUserPassword(), userDto.getUserId()))) {
				logger.debug("result pw=[{}], userPwd=[{}]", userDto.getUserPwd(), sessionInfo.getUserPassword());
				throw new ServiceException(BxMessages.Error.INVALID_PASSWORD);
			}
		} catch (Exception e) {
			throw new ServiceException(BxMessages.Error.INVALID_PASSWORD);
		}

//		List<PermDto> permList = permDao.selectPermListByRole(userDto.getRoleId());

		CommonResponse response = new CommonResponse();
		response.setHasError(false);

		UserInfo userInfo = new UserInfo();
		userInfo.setUserId(sessionInfo.getUserId());
		userInfo.setLocale(sessionInfo.getLocale());
		userInfo.setRoleId(userDto.getRoleId());

		session.setAttribute(BxConstants.Session.USER_ID, sessionInfo.getUserId());
		session.setAttribute(BxConstants.Session.LOCALE, sessionInfo.getLocale());
		session.setAttribute(BxConstants.Session.ROLE_ID, userDto.getRoleId());

		session.setAttribute(BxConstants.Session.USER_INFO, userInfo);
		session.setAttribute(BxConstants.Session.IS_USER_LOGIN, true);
		ServiceContext.setUserInfo(userInfo);

		return response;
	}

	public CommonResponse authenticateUserForChangePwd(ChangePwdInfo userInfo) {
		logger.debug("authenticateUserForChangePwd Start {}", userInfo.getUserId());

		UserDto userDto = userDao.selectUser(userInfo.getUserId());
		// 존재하지 않은 ID
		if (userDto == null) {
			throw new ServiceException(BxMessages.Error.INVALID_ID);
		}

		String decryptedPassword = null;
		try {
			decryptedPassword = CryptoUtils.instance().encrypt(userInfo.getUserPwd(), userInfo.getUserId());
		} catch (Exception e) {
			throw new ServiceException(BxMessages.Error.SYSTEM_EXCEPTION);
		}

//		 비밀번호 불일치
		if (!userDto.getUserPwd().equals(decryptedPassword)) {
			logger.debug("result pw=[{}], userPwd=[{}]", userDto.getUserPwd(), decryptedPassword);
			throw new ServiceException(BxMessages.Error.INVALID_PASSWORD);
		}

		CommonResponse response = new CommonResponse();
		response.setHasError(false);

		return response;
	}

	public LoginUserInfo getMainHome(HttpSession session, String locale) {

		if (session == null) {
			throw new BXRuntimeSystemException(BxMessages.Error.INVALID_SESSION.getMessage());
		}

		String userId = (String) session.getAttribute(BxConstants.Session.USER_ID);
		LoginUserInfo loginUserInfo = new LoginUserInfo();

		SessionInfo user = new SessionInfo();
		user.setUserId(userId);
		user.setLocale(locale);

		loginUserInfo.setSessionInfo(user);

		UserDto userDto = userDao.selectUser(userId);
		RoleDto roleDto = roleDao.selectRole(userDto.getRoleId());

		userDto.setRoleId(roleDto.getRoleId());
		loginUserInfo.setUserDto(userDto);

		String roleId = (String) session.getAttribute(BxConstants.Session.ROLE_ID);
		List<UiMenuTreeInfo> menuList = menuDao.selectMenuListByRole(roleId);
		
		loginUserInfo.setMenuList(menuList);

		return loginUserInfo;
	}
}
