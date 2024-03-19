package com.ca.mfd.prc.common.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * ConditionDirection enum
 *
 * @author cwy
 * @date 2023/3/24
 */
public enum ConditionDirection {

    /**
     * 默认无
     */
    NONE(0, "默认无"),
    /**
     * 正序
     */
    @JsonProperty("asc")
    ASC(1, "正序"),
    /**
     * 倒叙
     */
    @JsonProperty("desc")
    DESC(2, "倒叙");

    private final int value;
    private final String description;

    ConditionDirection(int value, String description) {

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
