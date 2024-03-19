package com.ca.mfd.prc.gateway.utils;

public class JsonResponseUtils {

    public static final String BLOCK_FLOW_ERROR = "{\"code\": -1, \"data\": null, \"msg\": \"系统限流\"}";
    public static final String AUTH_UNLOGIN_ERROR = "{\"code\": -1, \"data\": null, \"msg\": \"未授权\"}";
    public static final String AUTH_EXP_ERROR = "{\"code\": -1, \"data\": null, \"msg\": \"授权过期\"}";
    public static final String AUTH_PARAM_ERROR = "{\"code\": -1, \"data\": null, \"msg\": \"参数异常\"}";
    public static final String NOT_URL_AUTH = "{\"code\": -1, \"data\": null, \"msg\": \"没有此接口权限\"}";
    public static final String API_AUTH_ERROR = "{\"code\": -1, \"data\": null, \"msg\": \"API平台转发请求验证失败\"}";

}