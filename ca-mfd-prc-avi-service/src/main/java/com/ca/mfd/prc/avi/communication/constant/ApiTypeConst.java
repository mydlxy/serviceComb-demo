package com.ca.mfd.prc.avi.communication.constant;

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

    // LMS 车辆队列
    String LMS_QUEUE_START = "MOM-LMS-01";

    // LMS 整车计划锁定
    String LMS_LOCKPLAN = "MOM-LMS-02";

    //LMS工位物料-获取凭证
    String LMS_OBTAIN_SIGTRUE = "MOM-LMS-03";

    // QMS 质量管理-缺陷库
    String PMS_ICC_DATA = "MOM-MDM-01";
    // QMS 质量管理-缺陷等级
    String PMS_ICC_CATEGORY = "MOM-MDM-02";


    //主数据 国家
    String MAIN_COUNTRY = "MOM-MDM-03";
    //主数据 班次
    String MAIN_SHIFT = "MOM-MDM-04";
    //下发电检整车信息
    String DJ_CARINFO = "MOM-DJ-01";
    //下发软件信息
    String DJ_ECU_CARINFO = "MOM-DJ-02";
    //接收电检整车结果信息
    String DJ_CARINFO_RESULT = "MOM-DJ-03";
    //接收电检软件结果信息
    String DJ_ECU_CARINFO_RESULT = "MOM-DJ-04";
    //下发电检过点信息
    String DJ_SITEINFO = "MOM-DJ-05";
    //接收电检整车结果信息
    String DJ_SITEINFO_RESULT = "MOM-DJ-06";

    //下发EP数据接口
    String DJ_EPINFO_RESULT = "MOM-DJ-07";

    //接收IOT变化上报的数据
    String IOT_QUEUE_START = "MOM-IOT-01";

    // AGV AVI 队列发送
    String AGV_QUEUE_START = "MOM-AGV-01";

    // AGV AVI 队列确认
    String AGV_QUEUE_CONFIRM = "MOM-AGV-02";

    // AVI 队列发送
    String AVI_QUEUE_START = "MOM-AVI-01";

    // AVI 队列确认
    String AVI_QUEUE_CONFIRM = "MOM-AVI-02";
}
