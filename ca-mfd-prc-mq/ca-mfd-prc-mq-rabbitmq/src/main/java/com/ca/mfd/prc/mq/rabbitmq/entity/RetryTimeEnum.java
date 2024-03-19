package com.ca.mfd.prc.mq.rabbitmq.entity;

public enum RetryTimeEnum {
    /**
     * 秒
     */
    SECOND("s", 1),
    /**
     * 分钟
     */
    MINUTE("m", 60),
    /**
     * 小时
     */
    HOUR("h", 60 * 60),
    /**
     * 天
     */
    DAY("d", 60 * 60 * 24);

    private final String code;
    private final int value;

    RetryTimeEnum(String code, int value) {
        this.code = code;
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public String code() {
        return this.code;
    }

    public static RetryTimeEnum findByCode(String code) {
        for (RetryTimeEnum eachValue : RetryTimeEnum.values()) {
            if (code.equals(eachValue.code())) {
                return eachValue;
            }
        }
        //根据自身的业务 查不到可以返回null，或者抛出异常。
        return null;
    }

}
