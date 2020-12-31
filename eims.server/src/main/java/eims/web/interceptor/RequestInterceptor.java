package eims.web.interceptor;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.Cas30ServiceTicketValidator;
import org.jasig.cas.client.validation.TicketValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.initech.eam.api.NXContext;
import com.initech.eam.api.NXUserAPI;
import com.initech.eam.base.APIException;
import com.initech.eam.nls.CookieManager;
import com.initech.eam.smartenforcer.SECode;

import eims.ServiceContext;
import eims.web.constants.BxConstants;
import eims.web.constants.SystemProperties;
import eims.web.dto.UserInfo;

@SuppressWarnings("unchecked")
public class RequestInterceptor implements HandlerInterceptor {

	final Logger logger = LoggerFactory.getLogger(getClass());

	/***
	 * [SERVICE CONFIGURATION]
	 ***********************************************************************/
	private String SERVICE_NAME = "Web";
	private String SERVER_URL = "http://eims.bccard.com";
	private String SERVER_PORT = "9100";
	private String ASCP_URL = SERVER_URL + ":" + SERVER_PORT + "/index.html#!/main/blank";
	// private String ASCP_URL = SERVER_URL + ":" + SERVER_PORT +
	// "/Web/sso/login_exec.jsp";
	/*************************************************************************************************/

	/***
	 * [SSO CONFIGURATION]**]
	 ***********************************************************************/
	private String NLS_URL = "http://bcsso.bccard.com";
	private String NLS_PORT = "80";
	private String NLS_LOGIN_URL = NLS_URL + ":" + NLS_PORT + "/nls3/clientLogin.jsp";
	private String NLS_LOGOUT_URL = NLS_URL + ":" + NLS_PORT + "/nls3/NCLogout.jsp";
	private String NLS_ERROR_URL = NLS_URL + ":" + NLS_PORT + "/nls3/error.jsp";
	private static String ND_URL1 = "http://bcsso.bccard.com:5480";
	private static String ND_URL2 = "http://bcsso.bccard.com:5480";

	private static Vector PROVIDER_LIST = new Vector();

    private static final int COOKIE_SESSTION_TIME_OUT = 3000000;

	private String TOA = "1";
	private String SSO_DOMAIN = ".bccard.com";

	private static final int timeout = 1000000;
	private static NXContext context = null;

	private String casUrl = "https://sso.lbtwsys.com:8443/cas/";
	private String meUrl = "http://localhost:8089/";
	public static final String CONST_CAS_ASSERTION = "_const_cas_loname_";
	private Cas30ServiceTicketValidator ticketValidator;
	
	// Added
	private String ssoId;

	static {
		List<String> serverurlList = new ArrayList<String>();
		serverurlList.add(ND_URL1);
		serverurlList.add(ND_URL2);

		context = new NXContext(serverurlList, timeout);
		CookieManager.setEncStatus(true);

		PROVIDER_LIST.add("bcsso.bccard.com");
		PROVIDER_LIST.add("bcsso.bccard.com");
	}

	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		
		logger.debug("afterCompletion HttpServletRequest : arg0 {}", arg0);	
		logger.debug("afterCompletion HttpServletResponse : arg1 {}", arg1);	
		logger.debug("afterCompletion Object : arg2 {}", arg2);	
		logger.debug("afterCompletion Exception : arg3 {}", arg3);	
	}

	@Override
	public void postHandle(HttpServletRequest req, HttpServletResponse res, Object arg2, ModelAndView arg3)
			throws Exception {

		res.setHeader("Cache-Control", "no-store");
		res.setHeader("Pragma", "no-cache");
		res.setDateHeader("Expires", 0);
		if (req.getProtocol().equals("HTTP/1.1")) {
			res.setHeader("Cache-Control", "no-cache");
		}

		logger.debug("------------------------------------------------------------------");
		logger.info(" UserId : {}", ServiceContext.getUserInfo().getUserId());
		logger.info(" RoleId : {}", ServiceContext.getUserInfo().getRoleId());
		logger.info(" Locale : {}", ServiceContext.getUserInfo().getLocale());
		ServiceContext.clear();
		logger.info(" END : Request = {}, {}", req.getRequestURI(), req.getMethod());
		logger.debug("------------------------------------------------------------------");

	}

	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object arg2) throws Exception {

		logger.debug("------------------------------------------------------------------");
		logger.info(" START : Request = {}, {}", req.getRequestURI(), req.getMethod());
		logger.debug("------------------------------------------------------------------");

		
		//final HttpSession session = req.getSession(false);
		final HttpSession session = req.getSession();

		logger.debug("------------------ SESSION : [{}] --------------------", session);

		 
		if(req.getRequestURI().equals("/eims/interfaceListApi") || req.getRequestURI().equals("/interfaceListApi")  ) {
		  UserInfo userInfo = new UserInfo();
		  userInfo.setUserId("eimsadmin");
	      userInfo.setLocale("ko");
		  userInfo.setRoleId("Administrator");
		  session.setAttribute(BxConstants.Session.USER_INFO, userInfo);
		  session.setAttribute(BxConstants.Session.IS_USER_LOGIN, true);
		  ServiceContext.setUserInfo(userInfo);			
		  return true;
	   }
		
		if(req.getRequestURI().equals("/sso")  || req.getRequestURI().equals("/eims/sso") || req.getRequestURI().equals("/eims/eims/sso") ) {
		  UserInfo userInfo = new UserInfo();
		  userInfo.setUserId("eimsadmin");		  
		  userInfo.setLocale("en");
		  userInfo.setRoleId("Administrator");
		  session.setAttribute(BxConstants.Session.USER_INFO, userInfo);
		  session.setAttribute(BxConstants.Session.IS_USER_LOGIN, true);
		  ServiceContext.setUserInfo(userInfo);			
		  return true;
	   }
		
	   System.out.println("req : " + req.getRequestURI().toString() );
	   return loginProcess(req, res);
	}

	private boolean loginProcess(HttpServletRequest req, HttpServletResponse res) throws Exception {
		final HttpSession session = req.getSession();
		
		UserInfo userInfo = (UserInfo) session.getAttribute(BxConstants.Session.USER_INFO);

		if (userInfo == null) {
			logger.debug("### userInfo : {}", userInfo);	
			logger.debug("### res : {}", res);	
			//res.sendError(440);
			res.setStatus(440);
			res.setHeader("isSession", "-1");
			return false;
		}
		  
		ServiceContext.setUserInfo(userInfo);

		logger.debug(userInfo.toString());
		logger.debug("isUserLogin : {}",
				((Boolean) session.getAttribute(BxConstants.Session.IS_USER_LOGIN)).toString());

		return true;
	}

	private boolean ssoProcess(HttpServletRequest req, HttpServletResponse res) throws Exception {
		CookieManager.setEncStatus(true);
		ssoId = getSsoId(req);
		logger.debug("### SSOID : {}", ssoId);

		if (ssoId == null) {
			logger.debug("### Go to Login page.");
			goLoginPage(res);
			return false;
		} else {
			String retCode = getEamSessionCheck(req, res);
			logger.debug("### RETCODE : {}", retCode);

			if (!retCode.equals("0")) {
				logger.debug("REQCODE != 0");
				goErrorPage(res, Integer.parseInt(retCode));
				return false;
			}

			final HttpSession session = req.getSession();

			UserInfo userInfo = (UserInfo) session.getAttribute(BxConstants.Session.USER_INFO);
			if (userInfo == null) {
				userInfo = new UserInfo();
				userInfo.setUserId(ssoId);
				userInfo.setLocale(SystemProperties.get(BxConstants.Keys.MESSAGE_LOCALE));

				if (isAdmin(ssoId)) {
					userInfo.setRoleId(SystemProperties.get(BxConstants.Keys.ADMIN_ROLE_ID));
				} else {
					userInfo.setRoleId(SystemProperties.get(BxConstants.Keys.DEFAULT_ROLE_ID));
				}

				session.setAttribute(BxConstants.Session.USER_INFO, userInfo);
				session.setAttribute(BxConstants.Session.IS_USER_LOGIN, false);
				ServiceContext.setUserInfo(userInfo);
			} else {
				ServiceContext.setUserInfo(userInfo);
			}

			logger.debug(userInfo.toString());
			logger.debug("isUserLogin : {}",
					((Boolean) session.getAttribute(BxConstants.Session.IS_USER_LOGIN)).toString());
			// saveAccessHistory(req, "0");
		}
		return true;
	}

	private boolean isAdmin(String ssoId) {
		String[] adminList = { "20180038", "admin", "20180050" };
		for (String admin : adminList) {
			if (admin.equals(ssoId)) {
				return true;
			}
		}

		return false;
	}

	private String getSsoId(HttpServletRequest request) {
		String sso_id = null;
		sso_id = CookieManager.getCookieValue(SECode.USER_ID, request);
		return sso_id;
	}

	private void goLoginPage(HttpServletResponse response) throws Exception {
		CookieManager.addCookie(SECode.USER_URL, ASCP_URL, SSO_DOMAIN, response);
		CookieManager.addCookie(SECode.R_TOA, TOA, SSO_DOMAIN, response);
		response.sendRedirect(NLS_LOGIN_URL);
	}

	private String getEamSessionCheck(HttpServletRequest request, HttpServletResponse response) {
		String retCode = "";
		System.out.println("getEamSessionCheck 11111");
		try {

			System.out.println("getEamSessionCheck 2222");
			retCode = CookieManager.verifyNexessCookie(request, response, 10, COOKIE_SESSTION_TIME_OUT, PROVIDER_LIST);

			System.out.println("getEamSessionCheck 3333 retCode = " + retCode);
		} catch (Exception npe) {
			System.out.println("getEamSessionCheck 4444");
			logger.error("{}", npe);
		}
		System.out.println("getEamSessionCheck 5555");
		return retCode;
	}

	private void goErrorPage(HttpServletResponse response, int error_code) throws Exception {
		CookieManager.removeNexessCookie(SSO_DOMAIN, response);
		CookieManager.addCookie(SECode.USER_URL, ASCP_URL, SSO_DOMAIN, response);
		response.sendRedirect(NLS_ERROR_URL + "?errorCode=" + error_code);
	}

}
