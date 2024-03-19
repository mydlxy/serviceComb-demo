package com.ca.mfd.prc.core.prm.enums;

/**
 * LoginStyle enum
 *
 * @author eric.zhou
 * @date 2023/7/25
 */
public enum LoginStyle {
    MesLogin(1, "mes defalut Login"),

    AdLogin(2, "AD login"),

    AuthOpenLogin(3, "AuthOpenLogin");

    private final int value;
    private final String description;

    LoginStyle(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int value() {
        return this.value;
    }

    public String description() {
        return this.description;
    }
}
