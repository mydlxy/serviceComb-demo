package com.ca.mfd.prc.common.enums;

/**
 * 接收类型(1、地址2、用户外键)
 *
 * @author eric.zhou
 * @date 2023/4/17
 */
public enum DistinationType {
    Address(1, ""),
    UserId(2, "");

    private final int code;
    private final String description;

    DistinationType(int code, String description) {
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
