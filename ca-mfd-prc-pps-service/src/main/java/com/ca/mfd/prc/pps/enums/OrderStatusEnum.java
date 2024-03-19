package com.ca.mfd.prc.pps.enums;

/**
 * 订单状态类型
 * 0 未上线  1 未拆分  3已下发，拆分 5生产完成
 * 10入焊装 19出焊装 20入涂装 29出涂装  30入总装 39出总装 40入PDI  49出PDI
 * 98订单已报废 99入库 990出库
 *
 * @author eric.zhou
 * @date 2023/4/19
 */
public enum OrderStatusEnum {
    NoSplit(1, "未拆分"),
    CreatePub(3, "已拆分，生成订单"),
    ProduceStart(4, "生产开始"),
    ProduceOk(5, "生产完成"),
    InBodyShop(10, "入焊装"),
    OutBodyShop(19, "出焊装"),
    InBdBodyShop(191, "入立库（焊）"),
    OutBdBodyShop(192, "出立库（焊）"),
    InPaintShop(20, "入涂装"),
    OutPaintShop(29, "出涂装"),
    InBdPaintShop(291, "入立库（涂）"),
    OutBdPaintShop(292, "出立库（涂）"),
    InAssemblyShop(30, "入总装"),
    OutAssemblyShop(39, "出总装"),
    InPdi(40, "入PDI"),
    OutPdi(49, "出PDI"),
    Cancel(90, "订单作废"),
    Scrap(98, "订单已报废"),
    InWarehouse(99, "入库"),
    OutWarehouse(990, "出库");

    private final int code;
    private final String description;

    OrderStatusEnum(int code, String description) {
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
