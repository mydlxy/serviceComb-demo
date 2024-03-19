package com.ca.mfd.prc.mq.rocketmq.entity;

import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class RocketMQMessageContent {

    /**
     * 内容（必传）
     */
    private String content;

    public RocketMQMessageContent() {

    }

    public RocketMQMessageContent(String content) {
        this.content = content;
    }


}
