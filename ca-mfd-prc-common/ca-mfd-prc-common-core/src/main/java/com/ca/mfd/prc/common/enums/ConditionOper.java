package com.ca.mfd.prc.common.enums;

/**
 * ConditionOper enum
 *
 * @author cwy
 * @date 2023/3/24
 */
public enum ConditionOper {


    None(0, "默认无"),

    Equal(1, "等于"),

    Unequal(2, "不等于"),

    GreaterThan(3, "大于"),

    GreaterThanEqual(4, "大于等于"),

    LessThanEqual(5, "小于等于"),

    LessThan(6, "小于"),

    LeftLike(7, "左模糊"),

    RightLike(8, "右模糊"),

    AllLike(9, "全模糊"),

    Exclusive(10, "不包含"),

    In(11, "在范围中"),

    NotIn(12, "不在范围中"),

    Exists(13, "存在"),

    NotExists(14, "不存在");

    private final int value;
    private final String description;

    ConditionOper(int value, String description) {

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
