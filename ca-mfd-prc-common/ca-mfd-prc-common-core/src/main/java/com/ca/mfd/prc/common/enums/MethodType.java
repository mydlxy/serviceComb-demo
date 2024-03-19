package com.ca.mfd.prc.common.enums;

/**
 * 方法类型
 *
 * @author eric.zhou
 * @date 2023/4/17
 */
public enum MethodType {
    Email(1, "邮箱"),
    DingDing(2, "钉钉"),

    Sms(3, "短信"),
    EnterpriseWechat(4, "企业微信"),
    DingDingGroup(5, "钉钉机器人"),
    FeiShu(6, "飞书"),
    FeiShuGroup(7, "飞书机器人"),
    WxWorkGroup(8, "企业微信群推"),
    WxOpen(9, "微信公众号"),

    SiteMsg(99, "站内信");

    private final int code;
    private final String description;

    MethodType(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int code() {
        return this.code;
    }

    public String description() {
        return this.description;
    }

    public static MethodType findByCode(int code) {
        for (MethodType eachValue : MethodType.values()) {
            if (eachValue.code() == code) {
                return eachValue;
            }
        }
        return null;
    }

    public static MethodType findByName(String name) {
        for (MethodType eachValue : MethodType.values()) {
            if (eachValue.name().equals(name)) {
                return eachValue;
            }
        }
        return null;
    }

}
