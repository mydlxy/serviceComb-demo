package com.ca.mfd.prc.scheduling.entity;

public enum TriggerTypeEnum {
    CRON(1),
    SIMPLE(2);

    private final int value;

    TriggerTypeEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}

