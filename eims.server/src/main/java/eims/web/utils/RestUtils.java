package eims.web.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import bxt.utils.ExceptionUtils;
import eims.web.constants.BxConstants;
import eims.web.constants.SystemProperties;

public class RestUtils {
	private static final Logger logger = LoggerFactory.getLogger(RestUtils.class);


	public static <T> void sendRest(String httpMethodType, List<String> urlList, String contextPath, Object request,
			Class<T> responseType) {

		switch (HttpMethodTypeEnum.getByCode(httpMethodType)) {
		case POST:
			for (String url : urlList) {
				StringBuilder sb = new StringBuilder();
				sb.append(url).append("/").append(contextPath);
				URI uri = URI.create(sb.toString());

				postForObject(uri, request, responseType);
			}
			break;

		case DELETE:
			for (String url : urlList) {
				StringBuilder sb = new StringBuilder();
				sb.append(url).append("/").append(contextPath);
				URI uri = URI.create(sb.toString());

				deleteForObject(uri, request, responseType);
			}
			break;

		default:
			return;
		}

	}


	public static <T> T sendRestPost(String url, Object request, Class<T> responseType) {
		logger.debug("sendRestPost");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		@SuppressWarnings("unchecked")
		HttpEntity param = new HttpEntity(request, headers);

		URI uri = URI.create(url);
		return postForObject(uri, param, responseType);

	}


	public static <T> T postForObject(URI uri, Object request, Class<T> responseType) {
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory() ;
		
		String readTimeout = SystemProperties.get(BxConstants.Keys.DEPLOY_READ_TIMEOUT) ;
		String connTimeout = SystemProperties.get(BxConstants.Keys.DEPLOY_CONN_TIMEOUT) ;
		int readSec = 60 ;
		int connSec = 60 ;
		
//		if(readTimeout != null && !readTimeout.equals("")) {
//			readSec = Integer.parseInt(readTimeout) ;
//		}
//		if(connTimeout != null && !connTimeout.equals("")) {
//			connSec = Integer.parseInt(connTimeout) ;
//		}
		
		factory.setConnectTimeout(connSec*1000);
		factory.setReadTimeout(readSec*1000);
		RestTemplate restTemplate = new RestTemplate(factory);

		try {

			T result = restTemplate.postForObject(uri, request, responseType);
			logger.debug("### result : [{}] ###", result);

			return result;
		} catch (Throwable throwable) {
			logger.error("exception occured. {}", ExceptionUtils.instance().printException(throwable));
			throw throwable;
		}
	}


	public static <T> ResponseEntity<T> deleteForObject(URI uri, Object request, Class<T> responseType) {
		RestTemplate restTemplate = new RestTemplate();

		try {

			HttpEntity<Object> requestEntity = new HttpEntity<Object>(request);

			ResponseEntity<T> result = restTemplate.exchange(uri, HttpMethod.DELETE, requestEntity, responseType);
			logger.debug("### result : [{}] ###", result);

			return result;
		} catch (Throwable throwable) {
			logger.error("exception occured. {}", ExceptionUtils.instance().printException(throwable));
			throw throwable;
		}
	}


	public static String getServerWsdl(String wsdlUrl) {

		if (StringUtils.isEmpty(wsdlUrl)) {
			return null;
		}

		StringBuilder sb = new StringBuilder();
		BufferedReader rd = null;
		HttpURLConnection connection = null;
		try {
			URL url = new URL(wsdlUrl);
			connection = (HttpURLConnection) url.openConnection();

			rd = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
			String line;
			while ((line = rd.readLine()) != null) {
				sb.append(line).append("\n");
			}

		} catch (IOException e) {
			logger.error("{}", e);
		} finally {
			if (rd != null) {
				try {
					rd.close();
				} catch (IOException e) {
					logger.error("{}", e);
				}
			}

			if (connection != null) {
				connection.disconnect();
			}
		}

		return sb.toString();
	}
}
