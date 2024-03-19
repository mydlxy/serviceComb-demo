package com.ca.mfd.prc.pm.communication.constant;

/**
 * Oss客户端常量
 */
public interface ApiTypeConst {

    //LMS 工位物料凭证
    String LMS_WORKSTATOS_MATERIALSIGTRUE = "MOM-LMS-03";

    //LMS 工位物料
    String LMS_WORKSTATOS_MATERIAL = "MOM-LMS-04";

    // as车辆信息
    String AS_VEHICLE_PLAN = "MOM-AS-01";
    // as班次
    String AS_SHC_SHIFT = "MOM-AS-02";
    // as车间日历
    String AS_SHOP_CALENDAR = "MOM-AS-13";
    // as线体日历
    String AS_LINE_CALENDAR = "MOM-AS-14";
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

    // QMS 质量管理-缺陷库
    String PMS_ICC_DATA = "MOM-QMS-01";
    // QMS 质量管理-缺陷等级
    String PMS_ICC_CATEGORY = "MOM-QMS-02";


    //主数据 国家
    String MAIN_COUNTRY = "MOM-MDM-01";
    //主数据 班次
    String MAIN_SHIFT = "MOM-MAIN-02";

    //物料主数据
    String BOM_MATERIAL_MAIN = "MOM-BOM-01";
    //特征主数据
    String BOM_CHARACTERISTICS_MAIN = "MOM-BOM-02";
    //单车特征
    String BOM_CHARACTERISTICS = "MOM-BOM-03";
    //单车bom
    String BOM_MATERIAL = "MOM-BOM-04";
    //整车车型
    String BOM_VEHICLE_MODE = "MOM-MAIN-05";
    //整车车型
    String BOM_MBOM = "MOM-BOM-12";
    //物料使用处
    String BOM_MATERIAL_USE = "MOM-MAIN-06";
    //零件BOM
    String BOM_PART = "MOM-MAIN-07";
    //颜色代码库
    String BOM_COLOR_BASE = "MOM-BOM-08";
    //拧紧工艺
    String TIGHTEN_WO = "TIGHTEN_WO";
    //拧紧数据
    String TIGHTEN_TIGHTEN = "TIGHTEN_TIGHTEN";
    //拧紧曲线
    String TIGHTEN_CURVE = "TIGHTEN_CURVE";

}
