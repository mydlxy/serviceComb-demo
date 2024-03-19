package com.ca.mfd.prc.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.FileCopyUtils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;
import javax.net.ssl.TrustManager;
import javax.net.ssl.SSLContext;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Map;

public class HttpsHelper {
  private HttpsHelper() {
    throw new IllegalStateException("Utility class");
  }

  private static final Logger logger = LoggerFactory.getLogger(HttpsHelper.class);

  /**
   * .获取url文件
   */
  public static byte[] getUrlFile(String httpsUrl)
          throws Exception {
    logger.info("getUrlFile:" + httpsUrl);
    byte[] result = null;
    URL url = new URL(httpsUrl);
    if (httpsUrl.startsWith("https")) {
      trustAllHttpsCertificates();
      HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
          return hostname.equalsIgnoreCase(session.getPeerHost());
        }
      });
      HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
      connection.setDoOutput(true);
      result = FileCopyUtils.copyToByteArray(connection.getInputStream());
      connection.disconnect();
    } else {
      java.net.HttpURLConnection connection = (java.net.HttpURLConnection) url.openConnection();
      // connection.setDoOutput(true);
      result = FileCopyUtils.copyToByteArray(connection.getInputStream());
      connection.disconnect();
    }
    return result;
  }

  /**
   * .获取url结果支持https
   * get请求data为Map<String, Object>
   * post请求data为任意对象
   */
  public static String getRequest(String httpsUrl, HttpMethod method,Map<String, Object> urlData, Object bodyData,Map<String,String> headers)
          throws Exception {
    logger.info("getString:" + httpsUrl);
    if (httpsUrl.startsWith("https")) {
      trustAllHttpsCertificates();
      HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
          return hostname.equalsIgnoreCase(session.getPeerHost());
        }
      });
    }
    if (method == HttpMethod.GET && urlData != null) {
      StringBuilder sb = new StringBuilder();
      if (urlData != null && !urlData.isEmpty()) {
        for (Map.Entry<String, Object> msg : urlData.entrySet()) {
          if (sb.length() > 0) {
            sb.append("&");
          }
          sb.append(msg.getKey()).append("=").append(msg.getValue() == null ? "" : msg.getValue().toString());
        }
        httpsUrl = httpsUrl + (!httpsUrl.contains("?") ? "?" : "") + sb;
      }
    }
    URI uri = new URI(httpsUrl);
    //SimpleClientHttpRequestFactory httpRequestFactory = new SimpleClientHttpRequestFactory();
    ClientHttpRequestFactory httpRequestFactory = SpringContextUtils.getBean(ClientHttpRequestFactory.class);
    ClientHttpRequest request = httpRequestFactory.createRequest(uri, method);
    if (bodyData != null) {
      request.getBody().write(JsonUtils.toJsonString(bodyData).getBytes(StandardCharsets.UTF_8));
    }
    request.getHeaders().add("user-agent",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
                    + "(KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
    request.getHeaders().setContentType(MediaType.APPLICATION_JSON);
    if (headers != null && !headers.isEmpty()) {
      for (Map.Entry<String, String> hd : headers.entrySet()) {
        request.getHeaders().add(hd.getKey(), hd.getValue());
      }
    }
    ClientHttpResponse response = request.execute();
    String result = new String(FileCopyUtils.copyToByteArray(response.getBody()), "UTF-8");
    return result;
  }

  private static void trustAllHttpsCertificates()
          throws NoSuchAlgorithmException, KeyManagementException {
    TrustManager[] trustAllCerts = new TrustManager[1];
    trustAllCerts[0] = new TrustAllManager();
    SSLContext sc = SSLContext.getInstance("TLSv1.2");
    sc.init(null, trustAllCerts, null);
    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
  }

  private static class TrustAllManager implements X509TrustManager {
    @Override
    public X509Certificate[] getAcceptedIssuers() {
      return null;
    }

    @Override
    public void checkServerTrusted(X509Certificate[] certs, String authType) {
    }

    @Override
    public void checkClientTrusted(X509Certificate[] certs, String authType) {
    }

    public boolean isServerTrusted(X509Certificate[] certs) {
      return true;
    }

    public boolean isClientTrusted(X509Certificate[] certs) {
      return true;
    }
  }
}
