package com.ca.mfd.prc.pps.enums;

/**
 * 订单大类类型
 * ：1：整车  2：电池包  3：压铸  4：机加   5：冲压  6：电池上盖
 *
 * @author eric.zhou
 * @date 2023/4/19
 */
public enum OrderCategoryEnum {
    Vehicle(1, "整车"),
    Battery(2, "电池包"),
    PressureCasting(3, "压铸"),
    Machining(4, "机加"),
    Stamping(5, "冲压"),
    CoverPlate(6, "电池上盖"),
    SparePart(7, "备件"),
    Module(8, "预成组");

    private final int code;
    private final String description;

    OrderCategoryEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int code() {
        return this.code;
    }

    public String codeString() {
        return String.valueOf(this.code);
    }

    public String description() {
        return this.description;
    }
}
