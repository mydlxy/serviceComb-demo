package com.ca.mfd.prc.common.enums;

/**
 * 工单类型
 *
 * @author luowenbing
 * @date 2023/4/19
 */
public enum EntryTypeEnum {
    ShopEntry(1, ""),
    AreaEntry(2, "");

    private final int code;
    private final String description;

    EntryTypeEnum(int code, String description) {
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
