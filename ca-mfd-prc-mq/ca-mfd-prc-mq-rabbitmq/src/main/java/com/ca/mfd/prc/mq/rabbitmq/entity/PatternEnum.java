package com.ca.mfd.prc.mq.rabbitmq.entity;

public enum PatternEnum {
    /**
     * 普通
     */
    General(1),
    /**
     * 顺序
     */
    Sequence(2),
    /**
     * 自动重试（添加到队列最后）
     */
    AutoRetry(3);

    private final int value;

    PatternEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static PatternEnum find(int value){
        for (PatternEnum eachValue : PatternEnum.values()) {
            if(eachValue.value() == value){
                return eachValue;
            }
        }
        //根据自身的业务 查不到可以返回null，或者抛出异常。
        return null;
    }


}
