package com.ca.mfd.prc.pqs.enums;

/**
 * ConditionRelation enum
 *
 * @author cwy
 * @date 2023/3/24
 */
public enum RepairEntryStaus {

    /**
     * 创建
     */
    Created(1),
    /**
     * 进行中
     */
    InPorcess(2),
    /**
     * 已完成
     */
    Completed(97),
    /**
     * 已删除
     */
    Deleted(98),
    /**
     * 已关闭
     */
    Closed(99);

    private final int value;

    RepairEntryStaus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
