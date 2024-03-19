package com.ca.mfd.prc.scheduling.entity;

public enum MailMessageEnum {
    NONE(0),
    ERR(1),
    ALL(2);

    private final int value;

    MailMessageEnum(int value) {
        this.value = value;
    }

    public static MailMessageEnum find(String value) {
        for (MailMessageEnum eachValue : MailMessageEnum.values()) {
            if (eachValue.value() == Integer.parseInt(value)) {
                return eachValue;
            }
        }
        //根据自身的业务 查不到可以返回null，或者抛出异常。
        return null;
    }

    public int value() {
        return this.value;
    }
}
