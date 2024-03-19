package com.ca.mfd.prc.common.utils.yidiantong;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 简易客户端
 */
@Data
public class Client {
    /**
     * http请求工具
     */
    private static HttpTool httpTool = new HttpTool();

    /**
     * 请求url
     */
    private String url;

    /**
     * 平台提供的appKey
     */
    private String appId;

    /**
     * 平台提供的私钥
     */
    private String privateKey;


    /**
     * 创建一个客户端
     *
     * @param url        请求url
     * @param appId      平台提供的appKey
     * @param privateKey 平台提供的私钥
     */
    public Client(String url, String appId, String privateKey) {
        this.url = url;
        this.appId = appId;
        this.privateKey = privateKey;
    }

    /**
     * 发送请求
     *
     * @param requestBuilder 请求信息
     * @return 返回结果
     */
    public String execute(RequestBuilder requestBuilder) {
        RequestInfo requestInfo = requestBuilder.build(appId, privateKey);
        HttpTool.HTTPMethod httpMethod = requestInfo.getHttpMethod();
        boolean postJson = requestInfo.isPostJson();
        Map<String, ?> form = requestInfo.getForm();
        Map<String, String> header = requestInfo.getHeader();
        String responseData ;
        // 发送请求
        try {
            if (httpMethod == HttpTool.HTTPMethod.POST && postJson) {
                responseData = httpTool.requestJson(url, JSON.toJSONString(form), header);
            } else {
                responseData = httpTool.request(url, form, header, httpMethod);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return responseData;
    }




    public static class RequestBuilder {
        private static final String DEFAULT_VERSION = "1.0";
        private String method;
        private String customerSecret;
        private String customerCode;
        private String version = DEFAULT_VERSION;
        private Map<String, Object> bizContent;
        private HttpTool.HTTPMethod httpMethod;
        private Map<String, String> header;

        /**
         * 设置客户编号
         *
         * @param customerCode 客户编号
         * @return 返回RequestBuilder
         */
        public RequestBuilder customerCode(String customerCode){
            this.customerCode = customerCode;
            return this;
        }
        /**
         * 设置客户秘钥
         *
         * @param customerSecret 客户秘钥
         * @return 返回RequestBuilder
         */
        public RequestBuilder customerSecret(String customerSecret){
            this.customerSecret = customerSecret;
            return this;
        }

        /**
         * 设置方法名
         *
         * @param method 方法名
         * @return 返回RequestBuilder
         */
        public RequestBuilder method(String method) {
            this.method = method;
            return this;
        }

        /**
         * 设置版本号
         *
         * @param version 版本号
         * @return 返回RequestBuilder
         */
        public RequestBuilder version(String version) {
            this.version = version;
            return this;
        }

        /**
         * 设置业务参数
         *
         * @param bizContent 业务参数
         * @return 返回RequestBuilder
         */
        public RequestBuilder bizContent(Map<String, Object> bizContent) {
            this.bizContent = bizContent;
            return this;
        }

        /**
         * 设置请求方法
         *
         * @param httpMethod 请求方法
         * @return 返回RequestBuilder
         */
        public RequestBuilder httpMethod(HttpTool.HTTPMethod httpMethod) {
            this.httpMethod = httpMethod;
            return this;
        }

        /**
         * 设置请求头
         *
         * @param header 请求头
         * @return 返回RequestBuilder
         */
        public RequestBuilder header(Map<String, String> header) {
            this.header = header;
            return this;
        }


        public RequestInfo build(String appId, String privateKey) {
            // 公共请求参数
            Map<String, String> params = new HashMap<String, String>();
            params.put("app_id", appId);
            params.put("customer_code", customerCode);
            params.put("customer_secret", customerSecret);
            params.put("method", method);
            if (version != null) {
                params.put("version", version);
            }
            params.put("format", Constants.FORMAT_JSON);
            params.put("charset", Constants.CHARSET_UTF8);
            params.put("sign_type", Constants.SIGN_TYPE_RSA2);
            params.put("timestamp", new SimpleDateFormat(Constants.DATE_TIME_FORMAT).format(new Date()));
            // 业务参数
            params.put("biz_content", JSON.toJSONString(bizContent == null ? Collections.emptyMap() : bizContent));

            String content = Signature.getSignContent(params);
            String sign ;
            try {
                sign = Signature.rsa256Sign(content, privateKey, Constants.CHARSET_UTF8);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            params.put("sign", sign);

            RequestInfo requestInfo = new RequestInfo();
            requestInfo.setMethod(method);
            requestInfo.setVersion(version);
            requestInfo.setForm(params);
            requestInfo.setHeader(header);
            requestInfo.setHttpMethod(httpMethod);
            return requestInfo;
        }
    }

    @Data
    public static class RequestInfo {
        private String url;
        private String method;
        private String version;
        private boolean postJson;
        private Map<String, ?> form;
        private Map<String, String> header;
        private HttpTool.HTTPMethod httpMethod;
    }

    public static void main(String[] args) {
        String form = "{\"url\"}";
    }
}
