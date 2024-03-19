package com.ca.mfd.prc.common.enums;

/**
 * ConditionRelation enum
 *
 * @author cwy
 * @date 2023/3/24
 */
public enum ConditionRelation {

    /**
     * 默认无
     */
    None(0, "默认无"),
    /**
     * 与
     */
    And(1, "与"),
    /**
     * 或
     */
    Or(2, "或");

    private final int value;
    private final String description;

    ConditionRelation(int value, String description) {

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
