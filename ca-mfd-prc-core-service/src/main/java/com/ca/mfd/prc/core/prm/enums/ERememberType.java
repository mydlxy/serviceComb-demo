package com.ca.mfd.prc.core.prm.enums;

/**
 * LoginStyle enum
 *
 * @author eric.zhou
 * @date 2023/7/25
 */
public enum ERememberType {
    Normal(4, "Normal"),
    OneDay(24, "OneDay"),
    OneWeek(168, "OneWeek"),
    TwoWeek(336, "TwoWeek"),
    OneMonth(720, "OneMonth"),
    TwoMonth(1440, "TwoMonth"),
    OneYear(8760, "OneYear");

    private final int value;
    private final String description;

    ERememberType(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int value() {
        return this.value;
    }

    public String description() {
        return this.description;
    }
}
