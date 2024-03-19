package com.ca.mfd.prc.common.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


/**
 * <p>功能描述: ApiRestTempInterceptor.java</p>
 *
 * @author wujc
 * @version 2023年7月5日
 */
public class ApiRestTempInterceptor implements ClientHttpRequestInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(ApiRestTempInterceptor.class);


    private BaseApiPlatformConfig apiplatconfig;

    public ApiRestTempInterceptor(BaseApiPlatformConfig apiplatconfig) {
        this.apiplatconfig = apiplatconfig;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {
//        try {
//            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//            if (attributes != null) {
//                HttpHeaders headers = request.getHeaders();
//                HttpServletRequest currequest = attributes.getRequest();
//
//                /** apipt appkey*/
//                headers.add("appKey", apiplatconfig.getApiAppKey());
//                /** apipt userid */
//                String userId = ObjectUtil.tranString(currequest.getHeader(ParaConstants.X_USERID));
//                userId = StrUtil.isNotBlank(userId) ? userId : ObjectUtil.tranString(currequest.getParameter(ParaConstants.X_USERID));
//                if (StrUtil.isNotBlank(userId)) {
//                    headers.add(ParaConstants.X_USERID, userId);
//                }
//                /** apipt lang */
//                String lang = ObjectUtil.tranString(currequest.getHeader(ParaConstants.X_LANGUAGE));
//                lang = StrUtil.isNotBlank(lang) ? lang : ObjectUtil.tranString(currequest.getParameter(ParaConstants.X_LANGUAGE));
//                if (StrUtil.isNotBlank(lang)) {
//                    headers.add(ParaConstants.X_LANGUAGE, lang);
//                }
//
//            }
//
//        } catch (Exception e) {
//            logger.error("gray-resttemplate RequestContextHolder.requestAttributes is null:{}", e.getMessage());
//        }
        traceRequest(request, body);
        ClientHttpResponse response = execution.execute(request, body);
        return response;
    }


    private void traceRequest(HttpRequest request, byte[] body) throws IOException {
        if (logger.isInfoEnabled()) {
            logger.info("===========================apiplatform  begin================================================");
            logger.info("URI         : {}", request.getURI());
            logger.info("Method      : {}", request.getMethod());
            logger.info("Headers     : {}", request.getHeaders());
            logger.info("Request body: {}", new String(body, StandardCharsets.UTF_8));
            logger.info("==========================apiplatform end================================================");
        }
    }
 /*
     private void traceResponse(ClientHttpResponse response) throws IOException {
         StringBuilder inputStringBuilder = new StringBuilder();
         try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getBody(), StandardCharsets.UTF_8))) {
             String line = bufferedReader.readLine();
             while (line != null) {
                 inputStringBuilder.append(line);
                 inputStringBuilder.append('\n');
                 line = bufferedReader.readLine();
             }
         }
         if(logger.isInfoEnabled()) {
             logger.info("============================response begin==========================================");
             logger.info("Status code  : {}", response.getStatusCode());
             logger.info("Status text  : {}", response.getStatusText());
             logger.info("Headers      : {}", response.getHeaders());
             logger.info("Response body: {}", inputStringBuilder.toString());
             logger.info("=======================response end=================================================");
         }
     }*/

}