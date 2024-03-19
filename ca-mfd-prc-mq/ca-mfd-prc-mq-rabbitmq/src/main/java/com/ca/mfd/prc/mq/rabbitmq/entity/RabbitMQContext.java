package com.ca.mfd.prc.mq.rabbitmq.entity;

import lombok.Data;

@Data
public class RabbitMQContext {

    /**
     * 分组
     */
    //   private String groupName;

    /**
     * 通道(队列名称)
     */
    // private String channelName;

    /**
     * 消息通道ID
     */
    private Long notesId;

    /**
     * 分组
     */
    private String groupName;

    /**
     * 主题
     */
    private String topic;

    /**
     * 内容（如果要发送对象，用JSONUtil.toJsonStr将对象转成json字符即可）
     */
    private String content;

    /**
     * 重试次数（框架处理需要，业务端不需要维护）
     */
    private int retryCount;


    /*@Data
    public static class MessageContent {
        private String Subject;
        private String Content;
        private String Source;
        private Data PushDt;
        private String DistinationType;
        private String Distination;
        private String DistinationName;
        private String TargetId;

    }*/

}
