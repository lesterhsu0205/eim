package eims.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import eims.ServiceContext;
import eims.web.constants.BxConstants;
import eims.web.constants.BxMessages;
import eims.web.dto.CommonResponse;
import eims.web.dto.LoginUserInfo;
import eims.web.dto.SessionInfo;
import eims.web.dto.UserInfo;
import eims.web.exception.ServiceException;
import eims.web.service.RoleService;
import eims.web.service.SessionService;
import eims.web.service.UserService;

@Controller
public class MainController {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private SessionService sessionService;

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;


	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<LoginUserInfo> main(HttpSession session, HttpServletResponse response,
			@RequestBody SessionInfo in) {

		logger.debug("userId = [{}], locale = [{}]", in.getUserId(), in.getLocale());

		UserInfo userInfo = ServiceContext.getUserInfo();
		if (userInfo == null) {
			userInfo = new UserInfo();
			userInfo.setLocale(in.getLocale());
			userInfo.setUserId(in.getUserId());

			ServiceContext.setUserInfo(userInfo);
 
			session.setAttribute(BxConstants.Session.USER_INFO, userInfo);
			session.setAttribute(BxConstants.Session.IS_USER_LOGIN, true);
			// 세션2시간
			session.setMaxInactiveInterval(3600*2); 
		}

		CommonResponse userAuth = sessionService.authenticateUser(in, session);

		// 사용자 인증 오류
		if (userAuth.isHasError()) {
			logger.error(" authenticate error ");
			throw new ServiceException(BxMessages.Error.FAIL_LOGIN, in.getUserId());
		}

		LoginUserInfo loginUserInfo = sessionService.getMainHome(session, in.getLocale());
		loginUserInfo.setPermList(roleService.getPermList(loginUserInfo.getUserDto().getRoleId()));
		
		return new ResponseEntity<LoginUserInfo>(loginUserInfo, HttpStatus.OK);
	}


	@RequestMapping(value = "/bxmlogin", method = RequestMethod.GET)
	public String bxmLogin(@RequestParam(value = "userId", required = false) String userId,
			@RequestParam(value = "userPassword", required = false) String userPassword, HttpSession session,
			HttpServletResponse response) {

		logger.debug(" INPUT : userId : [{}], userPassword : [{}]", userId, userPassword);
		UserInfo userInfo = ServiceContext.getUserInfo();
		if (userInfo == null) {
			userInfo = new UserInfo();
			userInfo.setUserId(userId);
			userInfo.setLocale("ko");
			userInfo.setRoleId("Developer");

			session.setAttribute(BxConstants.Session.USER_INFO, userInfo);
			session.setAttribute(BxConstants.Session.IS_USER_LOGIN, true);
			ServiceContext.setUserInfo(userInfo);
		}

		return "redirect:index.html#!/main/blank";
	}


	@RequestMapping(value = "/userInfo", method = RequestMethod.GET)
	public ResponseEntity<LoginUserInfo> userInfo(HttpSession session, HttpServletResponse response) {

		String locale = (String) session.getAttribute(BxConstants.Session.LOCALE);

		LoginUserInfo loginUserInfo = sessionService.getMainHome(session, locale);

		return new ResponseEntity<LoginUserInfo>(loginUserInfo, HttpStatus.OK);
	}


	@RequestMapping(value = "/logininfosso", method = RequestMethod.GET)
	public ResponseEntity<LoginUserInfo> getUserInfo(HttpSession session, HttpServletResponse response) {

		LoginUserInfo loginUserInfo = new LoginUserInfo();

		UserInfo userInfo = (UserInfo) session.getAttribute(BxConstants.Session.USER_INFO);
		ServiceContext.setUserInfo(userInfo);

		if (userInfo != null) {
			loginUserInfo.setUserDto(userService.get(userInfo.getUserId()));
			loginUserInfo.setMenuList(roleService.getMenuList(userInfo.getRoleId()));
		
			SessionInfo userSession = new SessionInfo();
			userSession.setUserId(userInfo.getUserId());
			userSession.setLocale(userInfo.getLocale());
			loginUserInfo.setSessionInfo(userSession);
			loginUserInfo.setPermList(roleService.getPermList(userInfo.getRoleId()));
			
			
		}

		return new ResponseEntity<LoginUserInfo>(loginUserInfo, HttpStatus.OK);
	}


//	@RequestMapping(value = "/changePwd", method = RequestMethod.POST)
//	public ResponseEntity<Integer> changePwd(@RequestBody ChangePwdInfo in) {
//
//		logger.debug("userId = {}", in.getUserId());
//		CommonResponse userAuth = sessionService.authenticateUserForChangePwd(in);
//
//		if (userAuth.isHasError()) {
//			logger.error(" authenticate error ");
//			return new ResponseEntity<Integer>(0, HttpStatus.UNAUTHORIZED);
//		}
//
//		return new ResponseEntity<Integer>(0, HttpStatus.OK);
//	}

	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public ResponseEntity<String> logout(HttpServletRequest req) {

		String out = null;
		logger.info("logout");

		HttpSession session = req.getSession(false);
		if (session != null) {
			out = "Logout Success";
			session.invalidate();
			logger.info("logout session invalidate");
		}

		return new ResponseEntity<String>(out, HttpStatus.OK);
	}

}
