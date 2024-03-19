package com.ca.mfd.prc.eps.enums;

/**
 * 值对应WoStatus
 *
 * @author eric.zhou
 * @date 2023/4/12
 */
public enum WoStatusEnum {

    Reset(0, "reset"),
    Ok(1, "Ok"),
    Ng(2, "Ng"),

    ByPass(3, "ByPass");

    private final int code;
    private final String description;

    WoStatusEnum(int code, String description) {
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
