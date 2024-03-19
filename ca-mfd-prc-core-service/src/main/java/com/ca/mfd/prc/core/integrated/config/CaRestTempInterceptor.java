package com.ca.mfd.prc.core.integrated.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * <p>功能描述: CaRestTempInterceptor.java</p>
 *
 * @author wujc
 * @version 2023年7月4日
 */
public class CaRestTempInterceptor implements ClientHttpRequestInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(CaRestTempInterceptor.class);


    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {
        /*********************************************************
         * 如果统一门户需要添加头标识时可以使用
         try {
         ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
         if(attributes!=null) {
         HttpHeaders headers = request.getHeaders();
         if(request!=null) {
         long curstr=System.currentTimeMillis();
         headers.add("mpsdktag", MpSecurityAppCode.createtag(mpsdkconfig.getAppcode(), mpsdkconfig.getAppsecert(), curstr));
         h

         }
         }
         }catch (Exception e) {
         logger.error("gray-resttemplate RequestContextHolder.requestAttributes is null:{}", e.getMessage());
         }
         ***********************************************************/
        traceRequest(request, body);
        ClientHttpResponse response = execution.execute(request, body);
        //traceResponse(response);
        return response;
    }


    private void traceRequest(HttpRequest request, byte[] body) throws IOException {
        if (logger.isInfoEnabled()) {
            logger.info("===========================request begin================================================");
            logger.info("URI         : {}", request.getURI());
            logger.info("Method      : {}", request.getMethod());
            logger.info("Headers     : {}", request.getHeaders());
            logger.info("Request body: {}", new String(body, StandardCharsets.UTF_8));
            logger.info("==========================request end================================================");
        }
    }
}
