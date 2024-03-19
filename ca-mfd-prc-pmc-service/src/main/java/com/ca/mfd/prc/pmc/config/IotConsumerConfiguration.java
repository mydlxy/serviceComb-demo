package com.ca.mfd.prc.pmc.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;

@Data
@ConditionalOnExpression("#{'true'.equals(environment.getProperty('inkelink.iotplatset.enable'))}")
@Configuration
public class IotConsumerConfiguration {

    /**
     * Iot应用ID
     */
    @Value("${inkelink.iotplatset.appId:}")
    private String appId;

    /**
     * Iot应用密钥
     */
    @Value("${inkelink.iotplatset.appsecret}")
    private String appsecret;

    /**
     * 获取IOT物模型地址
     */
    @Value("${inkelink.iotplatset.iotmodelurl}")
    private String iotmodelurl;

}
