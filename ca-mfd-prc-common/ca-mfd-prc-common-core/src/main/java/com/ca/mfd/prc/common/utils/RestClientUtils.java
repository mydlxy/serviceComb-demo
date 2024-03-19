/**
 * Copyright (c) 2023 依柯力 All rights reserved.
 * <p>
 * https://www.inkelink.com
 * <p>
 * 版权所有，侵权必究！
 */

package com.ca.mfd.prc.common.utils;

import cn.hutool.json.JSONObject;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.entity.MiddleRequest;
import com.ca.mfd.prc.common.entity.MiddleResponse;
import com.ca.mfd.prc.common.enums.ServiceMethodRequest;
import com.ca.mfd.prc.common.exception.ExceptionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;

/**
 * rest工具类
 *
 * @author eric.zhou
 */
@Component
public class RestClientUtils {

    private static final Logger logger = LoggerFactory.getLogger(RestClientUtils.class);
    private static final String REST_NAME_HTTP = "http";
    private static final String REST_NAME_HTTPS = "https";

    private static final String REST_NAME_SCHEME_HTTP = "http://";
    private static final String REST_NAME_SCHEME_LB = "lb://";

    @Autowired
    private RestTemplateUtil restTemplateUtil;

    @Autowired
    private IdentityHelper identityHelper;

    /**
     * 默认单体模型请求
     *
     * @param request 请求对象
     * @return 返回对象
     */
    public MiddleResponse executeAsync(MiddleRequest request) {
        MiddleResponse rsp = new MiddleResponse();
        String interfaceUrl = buildInterFaceUrl(request);
        String responseContent = "";
        try {
            RestTemplate restTemplate = restTemplateUtil.getRestTemplate();
            Map sendData = request.getParams();
            HttpHeaders headers = buildHeaders(request);
            ResponseEntity<String> body;
            if (request.getServiceTypeEnum() == ServiceMethodRequest.Post) {

                HttpEntity requestEntity = new HttpEntity<>(sendData, headers);
                body = restTemplate.postForEntity(interfaceUrl, requestEntity, String.class);
            } else {
                HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(headers);
                if (sendData != null) {
                    for (Object key : sendData.keySet()) {
                        String pmName = key.toString();
                        StringBuilder sb = new StringBuilder();
                        if (interfaceUrl.contains("?")) {
                            sb.append(interfaceUrl).append("&").append(pmName).append("={").append(pmName).append("}");
                        } else {
                            sb.append(interfaceUrl).append("?").append(pmName).append("={").append(pmName).append("}");
                        }
                        interfaceUrl = sb.toString();
                    }
                    body = restTemplate.exchange(interfaceUrl, HttpMethod.GET, requestEntity, String.class, sendData);
                }else {
                    body = restTemplate.exchange(interfaceUrl, HttpMethod.GET, requestEntity, String.class);
                }
            }
            responseContent = body.getBody();
            setResponse(rsp, responseContent, interfaceUrl, body);
        } catch (Exception e) {
            logger.info("接口返回结果：{}:{}堆栈{}",interfaceUrl,e.getMessage(),e.getStackTrace());
            String error = ExceptionUtils.getErrorMessage(e);
            logger.error("", e);
            rsp.setBody(responseContent);
            rsp.setErrorMessage(error);
        }
        return rsp;
    }

    private static String buildInterFaceUrl(MiddleRequest request) {
        String interfaceUrl = request.getUseRoutName();
        if (!interfaceUrl.startsWith(REST_NAME_HTTP) && !interfaceUrl.startsWith(REST_NAME_HTTPS)) {
            String path = com.ca.mfd.prc.common.utils.StringUtils.trimStart(interfaceUrl.trim(), "/");
            if (!StringUtils.isBlank(request.getServiceDomainKey())) {
                String serviceDomain = request.getServiceDomainKey().trim();
                if (serviceDomain.startsWith(REST_NAME_HTTP) || serviceDomain.startsWith(REST_NAME_HTTPS)) {
                    interfaceUrl = serviceDomain + "/" + path.trim();
                } else {
                    interfaceUrl = REST_NAME_SCHEME_LB + serviceDomain + "/" + path.trim();
                }
                logger.info("ServiceDomain为空的地址：{}" , interfaceUrl);
            } else {
                interfaceUrl = REST_NAME_SCHEME_HTTP + path.trim();
                logger.info("ServiceDomain为空的地址：{}" , interfaceUrl);
            }
        }
        return interfaceUrl;
    }

    private HttpHeaders buildHeaders(MiddleRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(Constant.APPLICATION_JSON_UTF8);
        headers.add("user-agent", Constant.EQUALITY);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        headers.add("Connection", "keep-alive");
        if (Boolean.TRUE.equals(request.getAttachToken())) {
            HttpServletRequest webRequest = HttpContextUtils.getHttpServletRequest();
            headers.add("Authorization", "Bearer " + identityHelper.getHeaderToken(webRequest));
        } else {
            if (!StringUtils.isBlank(request.getOverrideToken())) {
                headers.add("Authorization", "Bearer " + request.getOverrideToken());
            }
        }
        if (Boolean.TRUE.equals(request.getDefalutEqualityService())) {
            headers.add("x-from", "EQualityService");
        }
        Map<String, String> header = request.getHeader();

        if (header != null) {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                headers.add(entry.getKey(), entry.getValue());
            }
        }
        return headers;
    }

    private void setResponse(MiddleResponse rsp, String responseContent, String interfaceUrl, ResponseEntity<String> body) {
        logger.info("{}:接口返回结果：{}{}", interfaceUrl, body.getStatusCodeValue(), responseContent);
        if (body.getStatusCode() == HttpStatus.OK) {
            rsp = JsonUtils.parseObject(responseContent, MiddleResponse.class);
            if (Objects.nonNull(rsp)) {
                rsp.setBody(responseContent);
                if (!StringUtils.isBlank(responseContent) && StringUtils.isBlank(rsp.getErrorMessage())) {
                    JSONObject rspJson = JsonUtils.parseObject(responseContent, JSONObject.class);
                    if(!MapUtils.isEmpty(rspJson)){
                        rsp.setErrorMessage(rspJson.getStr("message", ""));
                    }
                }
            }

        } else {
            rsp.setErrorMessage(body.getBody());
            rsp.setCode(body.getStatusCode().value());
        }
    }
}