package com.ca.mfd.prc.pps.enums;

/**
 * 工单状态类型
 * 整车： 0 未上线  1 半锁定  2 锁定 3 已下发 20 生产开始  30 生产完成  98 报废
 * 冲压： 1 未生产  2 待报工 3 正在生产 4 已经完成
 *
 * @author eric.zhou
 * @date 2023/4/19
 */
public enum EntryStatusEnum {
    OffLine(0, "整车:未上线"),
    NoProduce(1, "整车:半锁定；冲压:未生产"),
    Lock(2, "整车:锁定；冲压:待报工"),
    CreatePub(3, "整车:已下发；冲压:正在生产"),
    ProduceOk(4, "冲压:生产完成"),
    ProduceStart(20, "整车:生产开始"),
    ProduceEnd(30, "整车:生产完成"),
    Scrap(98, "订单已报废");

    private final int code;
    private final String description;

    EntryStatusEnum(int code, String description) {
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
