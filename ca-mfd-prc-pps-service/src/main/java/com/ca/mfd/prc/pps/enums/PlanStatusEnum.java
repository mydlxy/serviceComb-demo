package com.ca.mfd.prc.pps.enums;

/**
 * 计划状态类型
 * 整车： 0 未上线  1 半锁定  2 锁定 3 已下发 20 生产开始  30 生产完成  98 报废
 * 冲压： 1 未生产  2 待报工 3 正在生产 4 已经完成
 *
 * @author eric.zhou
 * @date 2023/4/19
 */
public enum PlanStatusEnum {
    NoSplit(1, "未拆分"),
    Split(2, "已拆分，生产排序"),
    CreatePub(3, "发布，生成订单"),
    ProduceStart(20, "整车:生产开始"),
    ProduceEnd(30, "整车:生产完成"),
    Scrap(98, "订单已报废");

    private final int code;
    private final String description;

    PlanStatusEnum(int code, String description) {
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
