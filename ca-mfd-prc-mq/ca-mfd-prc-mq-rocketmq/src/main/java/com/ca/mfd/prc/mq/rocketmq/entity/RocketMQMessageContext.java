package com.ca.mfd.prc.mq.rocketmq.entity;

import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class RocketMQMessageContext {

    /**
     * 消息通道ID
     *//*
    private Long notesId;*/

    /**
     * 内容（必传 如需传对象，可将对象转为json）
     */
    private String content;

    /**
     * 主题（必传）
     */
    private String topic;

    /**
     * 标签列表（可选）
     */
    private String tag;

    /**
     * 顺序消息的hashKey（可选）
     */
    private String orderlyHashKey;

    /**
     * 消息超时时间（毫秒 可选）
     */
    private long timeout = 5 * 1000L;

    /**
     * 迟延消息等级(跟rocketmq里面设置的等级对应， 可选)
     */
    private int delayLevel;

    public String getSendTopic() {
        String topic = this.getTopic();
        if (StringUtils.hasText(this.getTag())) {
            topic = topic + ":" + this.getTag();
        }
        return topic;
    }


}
