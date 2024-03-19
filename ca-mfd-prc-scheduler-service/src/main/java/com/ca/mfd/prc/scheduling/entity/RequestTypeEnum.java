package com.ca.mfd.prc.scheduling.entity;

public enum RequestTypeEnum {
    NONE(0),
    GET(1),
    POST(2),
    PUT(4),
    DELETE(8);

    private final int value;

    RequestTypeEnum(int value) {
        this.value = value;
    }

    public static RequestTypeEnum find(int value) {
        for (RequestTypeEnum eachValue : RequestTypeEnum.values()) {
            if (eachValue.value() == value) {
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
