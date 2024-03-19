package com.ca.mfd.prc.eps.enums;

/**
 * 工艺缓存
 *
 * @author eric.zhou
 * @date 2023/4/12
 */
public enum CacheWoQueueOperType {
    load(0, "装载工艺"),
    anewload(1, "重新装载工艺"),
    clear(2, "清除工艺"),
    wostatus(3, "修改工艺状态");

    private final int code;
    private final String description;

    CacheWoQueueOperType(int code, String description) {
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
