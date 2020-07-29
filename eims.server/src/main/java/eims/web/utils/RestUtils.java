package eims.web.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

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
	private static final int LEN_BUFFER = 4 * 1024;

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


	/*
	public static <T> byte[] sendRestPost(String url, byte[] request, Class<T> responseType) throws Exception {
		logger.debug("sendRestPost");
		
		int readSec = 60*60 ;
		int connSec = 60*60 ;
		
		StringBuffer result= new StringBuffer();
		HttpURLConnection httConn = null;
		byte[] responsebytes = new byte[0];
		
		try {
			initSSL();
		    httConn = (HttpURLConnection)new URL(url).openConnection();
			httConn.setDoOutput(true);
	        addRequestProperty(httConn, "UTF-8");
	        httConn.setConnectTimeout((int)connSec);
	        httConn.setReadTimeout((int)readSec);
	        logger.debug("URL : " + url); 

	        dataOutStream.write(request);
            dataOutStream.flush();

            DataInputStream dataInStream = new DataInputStream(httConn.getInputStream());
            
            responsebytes = readInputStream(dataInStream);
            
	        httConn.disconnect();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("telegram : " + request);
		System.out.println("result : " + result);
		return responsebytes;

	}
	*/
	
    private static byte[] readInputStream(InputStream stream) throws Exception {
		
		ByteArrayOutputStream bout = null;
		byte[] record = null;
		try {
            bout = new ByteArrayOutputStream();
            byte[] outBuffer = new byte[LEN_BUFFER];
            int readLen = 0;
            do {
                bout.write(outBuffer, 0, readLen);
                readLen = stream.read(outBuffer);
            } while (readLen > 0);
            bout.flush();
            record = bout.toByteArray();
		} finally {
		    close(bout);
		}
		return record;
	}
    
    private static void addRequestProperty(HttpURLConnection httConn, String reqEncoding) {
        httConn.addRequestProperty("Content-Type", "application/json;charset="+reqEncoding);    
    }
    
	private static void close(HttpURLConnection closeable) {
        try {
            if(closeable != null) {
                closeable.disconnect();
            }
        } catch(Exception e) {
            logger.error("close error", e);
        }
    }
    
	private static void close(Closeable... closeables) {
        if (closeables == null) return; 
        for (Closeable closeable : closeables) {
            if (closeable == null) continue; 
            try {
                closeable.close(); 
            } catch (Exception e) {
                logger.error("close error", e);
            }
        }
    }
	
	public static <T> T postForObject(URI uri, Object request, Class<T> responseType) {
	//	initSSL();
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

   	public static void initSSL() {
		logger.debug("initSSL start..");
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
           	public java.security.cert.X509Certificate[] getAcceptedIssuers() { return null; }
           	public void checkClientTrusted(X509Certificate[] certs, String authType) { }
           	public void checkServerTrusted(X509Certificate[] certs, String authType) { }
          }
        };
        logger.debug("initSSL : 1111");
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

				@Override
				public boolean verify(String arg0, SSLSession arg1) {
					// TODO Auto-generated method stub
					 logger.debug("initSSL : 222");
					return true;
				}
            	

            });            
            logger.debug("initSSL : 333");
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null,  trustAllCerts, new SecureRandom());

            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setFollowRedirects(true);
        }catch(Exception e) {
        	logger.info("initSSL Exception..", e);
        	//e.printStackTrace();
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
