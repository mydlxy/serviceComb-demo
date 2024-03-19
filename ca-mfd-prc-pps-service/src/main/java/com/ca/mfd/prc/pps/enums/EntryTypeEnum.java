package com.ca.mfd.prc.pps.enums;

/**
 * 工单类型
 *
 * @author eric.zhou
 * @date 2023/4/19
 */
public enum EntryTypeEnum {
    ShopEntry(1, "车间工单"),
    AreaEntry(2, "线体工单");

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
