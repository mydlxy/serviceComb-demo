package com.ca.mfd.prc.pqs.enums;

/**
 * 激活缺陷日志状态
 *
 * @author inkelink
 * @date 2023/8/21
 */
public enum PqsLogicStatusEnum {
    ACTIVATED(1, "已激活"),
    RECOVERED(2, "已修复"),
    NOT_FOUND(3, "未发现"),
    QUALIFIED(4, "合格"),
    DISQUALIFICATION(5, "不合格");

    private final int code;
    private final String description;

    PqsLogicStatusEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 根据code获取description
     *
     * @param code
     * @return
     */
    public static String getValue(int code) {

        PqsLogicStatusEnum[] pqsLogicStatusEnums = values();
        for (PqsLogicStatusEnum pqsLogicStatusEnum : pqsLogicStatusEnums) {
            if (pqsLogicStatusEnum.code() == code) {
                return pqsLogicStatusEnum.description();
            }
        }

        return null;
    }

    public int code() {
        return this.code;
    }

    public String description() {
        return this.description;
    }
}
