package com.ca.mfd.prc.scheduling.entity;

public enum JobTypeEnum {
    NONE(0),
    URL(1),
    EMAIL(2),
    MQTT(3),
    RABBITMQ(4);

    private final int value;

    JobTypeEnum(int value) {
        this.value = value;
    }

    public static JobTypeEnum find(int value) {
        for (JobTypeEnum eachValue : JobTypeEnum.values()) {
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
