package com.ca.mfd.prc.eps.enums;

/**
 * 值对应WoType类型的值，没有工艺的生产数据1000开始标识比如  模组检测没有工艺
 *
 * @author eric.zhou
 * @date 2023/4/12
 */
public enum WoDataEnum {

    EpsVehicleWoDataScr(1, "拧紧工艺"),
    EpsVehicleWoDataTrc(2, "追溯工艺"),
    EpsVehicleBms(3, "车辆电检数据"),
    EpsVehicleEqument(7, "车辆设备数据"),
    EpsVehicleWoDataShoot(28, "拍摄追溯");

    private final int code;
    private final String description;

    WoDataEnum(int code, String description) {
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
