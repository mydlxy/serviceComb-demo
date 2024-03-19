package com.ca.mfd.prc.core.communication.constant;

/**
 * Oss客户端常量
 */
public interface ApiTypeConst {
    // as车辆信息
    public static final String AS_VEHICLE_PLAN = "MOM-AS-01";
    // as班次
    public static final String AS_SHC_SHIFT = "MOM-AS-02";
    // as批次计划
    public static final String AS_BATH_PLAN = "MOM-AS-03";
    // as车间计划
    public static final String AS_SHOP_PLAN = "MOM-AS-08";
    // as车辆实际过点
    public static final String AS_AVI_POINT = "MOM-AS-09";
    // as保留车辆
    public static final String AS_KEEP_CAR = "MOM-AS-10";
    // as报废车辆
    public static final String AS_ORDER_SCRAP = "MOM-AS-11";
    // as WBS/PBS在制
    public static final String AS_WBS_PBS = "MOM-AS-12";

    // QMS 质量管理-缺陷库
    public static final String PMS_ICC_DATA = "MOM-MDM-01";
    // QMS 质量管理-缺陷等级
    public static final String PMS_ICC_CATEGORY = "MOM-MDM-02";


    //主数据 国家
    public static final String MAIN_COUNTRY = "MOM-MDM-03";
    //主数据 班次
    public static final String MAIN_SHIFT = "MOM-MDM-04";
}
