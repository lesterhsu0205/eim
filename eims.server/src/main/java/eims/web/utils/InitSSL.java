package eims.web.utils;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class InitSSL implements Filter{
	public InitSSL() {
		Init();
	}
	
	private static void Init() {
		System.out.println("initSSL start..");
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
           	public java.security.cert.X509Certificate[] getAcceptedIssuers() { return null; }
           	public void checkClientTrusted(X509Certificate[] certs, String authType) { }
           	public void checkServerTrusted(X509Certificate[] certs, String authType) { }
          }
        };

        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
           	 @Override
           	 public boolean verify(String hostname, SSLSession sslSession) {
           		 return true;
           	 }
            });
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null,  trustAllCerts, new SecureRandom());

            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setFollowRedirects(true);
        }catch(Exception e) {
        	System.out.println("initSSL Exception..");
        	e.printStackTrace();
        }
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}
}
