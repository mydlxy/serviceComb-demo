package com.ca.mfd.prc.common.utils;

import java.util.Map;


public interface IApiPtformBaseService {

    String getapi(String path, Object pms, Map<String, String> heads);

    /**
     * http get 请求带上URL对象参数
     * @param path 请求路径
     * @param pms RequestParams
     * @param heads 请求头
     * @return 返回Get结果
     */
    String getApiEx(String path, Object pms, Map<String, String> heads);

    String postapi(String path, Object pms, Map<String, String> heads);

}
