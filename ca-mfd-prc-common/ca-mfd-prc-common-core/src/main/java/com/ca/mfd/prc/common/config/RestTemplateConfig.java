package com.ca.mfd.prc.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate配置类
 *
 * @author eric.zhou@hg2mes.com
 * @date 2023/4/7
 */
@Configuration
public class RestTemplateConfig {

    /**
     * 连接超时（默认1分钟）（1 * 60 * 1000）
     */
    @Value("${inkelink.rest.connectTimeout:60000}")
    private int connectTimeout;

    /**
     * 读超时（默认3分钟）（3 * 60 * 1000）
     */
    @Value("${inkelink.rest.readTimeout:180000}")
    private int readTimeout;

//    @LoadBalanced
//    @Bean

    /**
     * 新的RestTemplate（调用帶IP的请求）
     */
    public RestTemplate newRestTemplate() {
        return new RestTemplate(httpRequestFactory());
    }

    /**
     * 新的RestTemplate（调用帶服务名的请求）
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(httpRequestFactory());
    }

    @Bean
    public ClientHttpRequestFactory httpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(readTimeout);
        factory.setConnectTimeout(connectTimeout);
        return factory;
    }

}
