package com.ca.mfd.prc.core.integrated.config;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

/**
 * <p>功能描述: 用于远程请求的resttemplate</p>
 *
 * @author wujc
 * @version 2023年7月4日
 */
@Configuration
@ConditionalOnExpression("#{'true'.equals(environment.getProperty('inkelink.gateway.isOpenCaTokenCheck'))}")
public class CaRestConfiguration {
    @Autowired
    private IntegratedConfig integratedConfig;

    /**
     * <p>
     * 功能描述: 用于远程请求的resttemplate
     * </p>
     *
     * @param @return
     * @return RestTemplate
     * @author wujc
     * @version 2023年7月4日
     */
    @Bean("casdkrestemplate")
    @Lazy
    public RestTemplate casdkrestemplate() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        // 设置连接池获取连接的超时时间
        factory.setConnectionRequestTimeout(integratedConfig.getConnectionRequestTimeout());
        // 设置连接超时时间
        factory.setConnectTimeout(integratedConfig.getConnectTimeout());
        // 设置读取超时时间
        factory.setReadTimeout(integratedConfig.getReadTimeout());
        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom().setConnectionRequestTimeout(integratedConfig.getConnectionRequestTimeout()).setConnectTimeout(integratedConfig.getConnectTimeout())
                        .setSocketTimeout(integratedConfig.getReadTimeout()).build())
                .setMaxConnTotal(integratedConfig.getMaxConnTotal())
                .setMaxConnPerRoute(integratedConfig.getMaxConnPerRoute())
                .build();
        factory.setHttpClient(httpClient);
        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.setInterceptors(Collections.singletonList(new CaRestTempInterceptor()));
        return restTemplate;
    }
}