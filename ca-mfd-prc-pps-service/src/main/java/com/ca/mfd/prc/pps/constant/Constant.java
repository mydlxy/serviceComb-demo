package com.ca.mfd.prc.pps.constant;

/**
 * Constant
 *
 * @author inkelink
 */
public interface Constant {

    /**
     * 零部件工单拆分
     */
     static final Object planLockEntryReckon = new Object();

    /**
     * 焊装车间数据权限判断
     */
    String BODY_SHOP_DATA = "BODY_SHOP_DATA";

    /**
     * 涂装车间数据权限判断
     */
    String PAINT_SHOP_DATA = "PAINT_SHOP_DATA";
    /**
     * 总装车间数据权限判断
     */
    String ASSEMBLY_SHOP_DATA = "ASSEMBLY_SHOP_DATA";

    /**
     * Pack车间数据权限判断
     */
    String PACK_SHOP_DATA = "PACK_SHOP_DATA";

    /**
     * PDI车间数据权限判断
     */
    String PDI_SHOP_DATA = "PDI_SHOP_DATA";

    String SEPARATOR_COMMA = ",";

    String AS_ORDER_SCRAP_QUEUE = "AS_ORDER_SCRAP_QUEUE";

}
