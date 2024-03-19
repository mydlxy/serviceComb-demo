package com.ca.mfd.prc.core.integrated.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * 功能描述: 门户集成相关配置
 * </p>
 *
 * @author mason
 * @version 2023年9月20日
 */
@RefreshScope
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "inkelink.gateway.integrated",ignoreUnknownFields = true)
@Data
public class IntegratedConfig {
    /**
     * 连接池获取连接的超时时间
     */
    private Integer connectionRequestTimeout;

    /**
     * 连接超时时间
     */
    private Integer connectTimeout;

    /**
     * 读取超时时间
     */
    private Integer readTimeout;

    /**
     * 连接池最大连接数
     */
    private Integer maxConnTotal;

    /**
     * 每个路由最大连接数
     */
    private Integer maxConnPerRoute;

    /**
     * API生命周期Appkey
     */
    private String appkey;
    /**
     * 统一门户appcode
     */
    private String appCode;

    /**
     * 用户令牌校验url
     */
    private String checkTokenUrl;

    /**
     * 根据工号获取用户信息url
     */
    private String queryUserUrl;

    /**
     * 根据工号获取用户多组织信息url
     */
    private String queryUserOrgUrl;

    /**
     * 根据工号获取用户权限列表url
     */
    private String queryUserPermissionUrl;

    /**
     * 图形验证码
     */
    private String getGraphicCodeUrl;

    /**
     * 统一门户登录
     */
    private String oauthLoginUrl;

    /**
     * 查询部门及下级部门
     */
    private String departmentQueryUrl;
}


