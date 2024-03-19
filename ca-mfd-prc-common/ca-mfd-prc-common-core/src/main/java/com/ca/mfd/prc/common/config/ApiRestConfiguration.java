 package com.ca.mfd.prc.common.config;

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
 * 
 * <p>功能描述: ApiRestConfiguration.java</p>
 * @author   
 * @version  2023年7月5日
 */
@Configuration
@ConditionalOnExpression("#{'true'.equals(environment.getProperty('inkelink.apiplatform.enable'))}")
 public class ApiRestConfiguration {

     @Autowired
     private ApiPlatformConfig apiRemoteConfig;

    @Autowired
    private ApiBomPlatformConfig apiBomPlatformConfig;


    private RestTemplate getRestTemplate(BaseApiPlatformConfig apiRemoteConfig){
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectionRequestTimeout(apiRemoteConfig.getApiPlatform().getConnectionRequestTimeout());
        factory.setConnectTimeout(apiRemoteConfig.getApiPlatform().getConnectTimeout());
        factory.setReadTimeout(apiRemoteConfig.getApiPlatform().getReadTimeout());
        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectionRequestTimeout(apiRemoteConfig.getApiPlatform().getConnectionRequestTimeout())
                        .setConnectTimeout(apiRemoteConfig.getApiPlatform().getConnectTimeout())
                        .setSocketTimeout(apiRemoteConfig.getApiPlatform().getReadTimeout())
                        .build())
                .setMaxConnTotal(apiRemoteConfig.getApiPlatform().getMaxConnTotal())
                .setMaxConnPerRoute(apiRemoteConfig.getApiPlatform().getMaxConnPerRoute())
                .build();
        factory.setHttpClient(httpClient);
        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.setInterceptors(Collections.singletonList(new ApiRestTempInterceptor(apiRemoteConfig)));
        return restTemplate;
    }

     @Bean("apiPtRestTemplate")
     @Lazy
     public RestTemplate mpsdkrestemplate() {
         return getRestTemplate(apiRemoteConfig);
     }

    @Bean("apiPtBomRestTemplate")
    @Lazy
    public RestTemplate mpsdkBomrestemplate() {
        return getRestTemplate(apiBomPlatformConfig);
    }
}
