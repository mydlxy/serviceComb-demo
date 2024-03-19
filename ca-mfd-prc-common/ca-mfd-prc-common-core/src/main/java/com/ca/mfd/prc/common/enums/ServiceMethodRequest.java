package com.ca.mfd.prc.common.enums;

/**
 * ServiceMethodRequest enum
 *
 * @author cwy
 * @date 2023/3/24
 */
public enum ServiceMethodRequest {
    /**
     * Post
     */
    Post(1, "Post"),
    /**
     * Get
     */
    Get(2, "Get");

    private final int code;
    private final String description;

    ServiceMethodRequest(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int code() {
        return this.code;
    }

    public String description() {
        return this.description;
    }
}
