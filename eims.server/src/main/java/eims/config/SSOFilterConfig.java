package eims.config;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.jasig.cas.client.util.HttpServletRequestWrapperFilter;
import org.jasig.cas.client.validation.Cas30ProxyReceivingTicketValidationFilter;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.Maps;

import eims.web.constants.BxConstants;
import eims.web.utils.InitSSL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class SSOFilterConfig {
	final Logger logger = LoggerFactory.getLogger(getClass());

	
	@Bean
	public FilterRegistrationBean singleSignOutFilter() {
		logger.debug("Filter Init - singleSignOutFilter");
		FilterRegistrationBean filter = new FilterRegistrationBean();
		filter.setFilter(new SingleSignOutFilter());
		filter.addUrlPatterns("/logout");
		filter.setName("CAS Single Sign Out Filter");
		filter.setOrder(1);
		Map<String, String> initParameters = Maps.newHashMap();
		initParameters.put("casServerUrlPrefix", "https://sso.lbtwsys.com:8443/cas/logout");
		filter.setInitParameters(initParameters);
		return filter;			
	}
	
	
	@Bean
	public FilterRegistrationBean authenticationFilter() {
		logger.debug("Filter Init - authenticationFilter");
		FilterRegistrationBean filter = new FilterRegistrationBean();
		initSSL();
		filter.setFilter(new AuthenticationFilter());
		filter.addUrlPatterns("/sso");
		filter.setName("CAS Authentication Filter");
		filter.setOrder(1);
		Map<String, String> initParameters = Maps.newHashMap();
		initParameters.put("casServerLoginUrl", "https://sso.lbtwsys.com:8443/cas/login");
//		initParameters.put("serverName", "http://10.244.106  .189:28080/eims/ssotest");
		if(BxConstants.Default.IS_SERVER) {
			initParameters.put("serverName", "http://10.244.106.189:28080");
		} else {
			initParameters.put("serverName", "http://localhost:8089");
		}
		filter.setInitParameters(initParameters);
		return filter;			
	}
	
	@Bean
	public FilterRegistrationBean cas30ProxyReceivingTicketValidationFilter() {
		logger.debug("Filter Init - cas30ProxyReceivingTicketValidationFilter");
		initSSL();
		FilterRegistrationBean filter = new FilterRegistrationBean();
		filter.setFilter(new Cas30ProxyReceivingTicketValidationFilter());
		filter.addUrlPatterns("/sso");
		filter.setName("CAS Validation Filter");
		filter.setOrder(2);
		Map<String, String> initParameters = Maps.newHashMap();
		initParameters.put("casServerUrlPrefix", "https://sso.lbtwsys.com:8443/cas");
		if(BxConstants.Default.IS_SERVER) {
			initParameters.put("serverName", "http://10.244.106.189:28080");
		} else {
			initParameters.put("serverName", "http://localhost:8089");
		}
		initParameters.put("redirectAfterValidation", "true");
		initParameters.put("useSession", "true");
		initParameters.put("authn_method", "mfa-duo");
		filter.setInitParameters(initParameters);
		return filter;			
	}
	
	@Bean
	public FilterRegistrationBean httpServletRequestWrapperFilter() {
		logger.debug("Filter Init - httpServletRequestWrapperFilter");
		initSSL();
		FilterRegistrationBean filter = new FilterRegistrationBean();
		filter.setFilter(new HttpServletRequestWrapperFilter());
		filter.addUrlPatterns("/sso");
		filter.setName("CAS HttpServletRequest Wrapper Filter");
		filter.setOrder(3);
		return filter;			
	}

	@Bean
	public ServletListenerRegistrationBean<SingleSignOutHttpSessionListener> listenerRegistrationBean() {
		logger.debug("SingleSignOutHttpSessionListener Init - SingleSignOutHttpSessionListener");
		ServletListenerRegistrationBean<SingleSignOutHttpSessionListener> bean = new ServletListenerRegistrationBean<SingleSignOutHttpSessionListener>();
		bean.setListener(new SingleSignOutHttpSessionListener());
		return bean;
	}
	
	
	private static void initSSL() {
		System.out.println("initSSL start..");
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}
		} };

		try {
			HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
				@Override
				public boolean verify(String hostname, SSLSession sslSession) {
					return true;
				}
			});
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new SecureRandom());

			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			HttpsURLConnection.setFollowRedirects(true);
		} catch (Exception e) {
			System.out.println("initSSL Exception..");
			e.printStackTrace();
		}
	}

}
