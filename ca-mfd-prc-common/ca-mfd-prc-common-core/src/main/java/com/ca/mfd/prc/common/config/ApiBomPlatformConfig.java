 package com.ca.mfd.prc.common.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

 /**
  *
  * <p>功能描述: ApiPlatformConfig.java</p>
  * @author
  * @version  2023年7月5日
  */
 @Configuration
 @EnableConfigurationProperties
 @ConfigurationProperties(prefix = "inkelink.apiplatform-bom", ignoreUnknownFields = true)
 public class ApiBomPlatformConfig extends BaseApiPlatformConfig {


 }
