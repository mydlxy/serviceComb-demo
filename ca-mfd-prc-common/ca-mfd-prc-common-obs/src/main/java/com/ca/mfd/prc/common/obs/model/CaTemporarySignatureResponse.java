package com.ca.mfd.prc.common.obs.model;

import java.util.Map;

/**
 * 临时签名
 *
 * @author 李国伟
 * @date 2023/09/25
 */
public class CaTemporarySignatureResponse {

    /**
     * 签名地址
     */
    private String url;
    /**
     * 签名headers
     */
    private Map<String, String> headers;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}
