package com.ca.mfd.prc.mq.kafka.entity;

import lombok.Data;

@Data
public class KafkaMessageContext {

    /**
     * 内容（必传）
     */
    private String content;

    /**
     * 主题（必传）
     */
    private String topic;

    /**
     * 消息key（可选）
     */
    private String key;

}
