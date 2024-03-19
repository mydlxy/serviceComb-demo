package com.ca.mfd.prc.common.enums;

/**
 * 打印目标类型
 *
 * @author eric.zhou
 * @date 2023/4/17
 */
public enum MessageTargetType {
    UnKnown(0, ""),
    ILS(1, ""),
    PQS(2, ""),
    PPS(3, ""),
    EPS(4, ""),
    PMC(5, ""),
    AVI(6, ""),
    OT(7, ""),
    Andon(8, ""),
    System(9, ""),
    EAM(10, ""),
    QUC(11, "法规证书"),
    Exception(99, "");


    private final int code;
    private final String description;

    MessageTargetType(int code, String description) {
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
