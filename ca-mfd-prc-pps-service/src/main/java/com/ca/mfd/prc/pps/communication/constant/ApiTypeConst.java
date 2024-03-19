package com.ca.mfd.prc.pps.communication.constant;

/**
 * Oss客户端常量
 */
public interface ApiTypeConst {
    // as车辆信息
    String AS_VEHICLE_PLAN = "MOM-AS-01";
    // as班次
    String AS_SHC_SHIFT = "MOM-AS-02";
    // as批次计划
    String AS_BATH_PLAN = "MOM-AS-03";
    // as车间计划
    String AS_SHOP_PLAN = "MOM-AS-08";
    // as车辆实际过点
    String AS_AVI_POINT = "MOM-AS-09";
    // as保留车辆
    String AS_KEEP_CAR = "MOM-AS-10";
    // as报废车辆
    String AS_ORDER_SCRAP = "MOM-AS-11";
    // as WBS/PBS在制
    String AS_WBS_PBS = "MOM-AS-12";
    // as 批次进度反馈
    String AS_BATCH_PIECES = "MOM-AS-15";
    // as 待开工队列
    String AS_QUEUE_START = "MOM-AS-16";


    // LMS 车辆队列
    String LMS_QUEUE_START = "MOM-LMS-01";

    // LMS 整车计划锁定
    String LMS_LOCKPLAN = "MOM-LMS-02";

    //LMS工位物料-获取凭证
    public static  final  String LMS_OBTAIN_SIGTRUE ="MOM-LMS-03";

    //LMS工位物料-获取工位物料数据
    public static  final  String LMS_WORKSTATION_DATA ="MOM-LMS-04";
    // LMS 焊装上线队列
    String LMS_WEQUEUE_START = "MOM-LMS-07";

    String LMS_PART_PLAN = "MOM-LMS-08";

    // QMS 质量管理-缺陷库
    String PMS_ICC_DATA = "MOM-MDM-01";
    // QMS 质量管理-缺陷等级
    String PMS_ICC_CATEGORY = "MOM-MDM-02";

    //主数据 国家
    String MAIN_COUNTRY = "MOM-MDM-03";
    //主数据 班次
    String MAIN_SHIFT = "MOM-MDM-04";
    //整车车型
    String BOM_VEHICLE_MODE = "MOM-BOM-05";
    //车型代码
    String BOM_VEHICLE_CODE = "MOM-BOM-11";
    //物料使用处
    String BOM_MATERIAL_USE = "MOM-BOM-06";

    //软件BOM清单
    String BOM_SOFTWARE_LIST = "MOM-BOM-09";
    //软件BOM配置字
    String BOM_SOFTWARE_CONFIG = "MOM-BOM-10";
    //伊点通
    String YIDIANTONG = "YIDIANTONG";
}
