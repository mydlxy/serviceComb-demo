package com.ca.mfd.prc.pqs.enums;

/**
 * ConditionRelation enum
 *
 * @author cwy
 * @date 2023/3/24
 */
public enum InspectionEntryType {

    /**
     * 来料检验
     */
    IQC(1),
    /**
     * 成品入库
     */
    CPJ(2),
    /**
     * 首件检验
     */
    SJ(10),
    /**
     * 末件检验
     */
    MJ(11),
    /**
     * 报工检验
     */
    REPORTING(12),
    /**
     * 生产巡检
     */
    SCXJ(13),
    /**
     * 原料抽检
     */
    MaterialCJ(20),
    /**
     * 成品抽检
     */
    ProductCJ(21),
    /**
     * 原料评审
     */
    MaterialAudit(30),
    /**
     * 生产不合格评审
     */
    ProductAudit(31),
    /**
     * 客退品评审
     */
    ReturnAudit(32),
    /**
     * 区域巡检
     */
    QYXJ(32);

    private final int value;

    InspectionEntryType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
