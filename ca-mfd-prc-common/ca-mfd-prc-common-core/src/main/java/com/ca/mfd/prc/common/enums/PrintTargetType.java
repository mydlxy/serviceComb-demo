package com.ca.mfd.prc.common.enums;

/**
 * PrintTargetType enum
 *
 * @author luowenbing
 * @date 2023/4/08
 */
public enum PrintTargetType {
    UnKnown(0, ""),
    AssemblyInstructionSheet(1, "装配指示单"),
    SPS(2, ""),
    KITTING(3, ""),
    INTJIS(4, ""),
    EXTJIS(5, ""),
    PACKPartOn(6, ""),
    PackOffLineMaterial(7, ""),
    AnnouncementCer(8, "合格证书打印"),
    AnnouncementCoc(9, "一致性证书打印"),
    AnnouncementEnv(10, "环保证书打印"),
    AnnouncementOutCoc(11, "出口一致性证书打印"),
    EnergyLabel(12, "能耗标识证书打印"),
    Other(99, "");
    private final int code;
    private final String description;

    PrintTargetType(int code, String description) {
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
