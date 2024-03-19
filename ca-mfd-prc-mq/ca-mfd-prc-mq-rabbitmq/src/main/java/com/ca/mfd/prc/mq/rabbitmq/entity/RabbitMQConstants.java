package com.ca.mfd.prc.mq.rabbitmq.entity;


public class RabbitMQConstants {
    /**
     * 消息队列日志key
     */
    public final static String REDIS_KEY_MQ_QUEUE_LOG = "CA:RabbitMQ:Queue:Log";
    /**
     * 一个队列日志最大条数
     */
    public final static Integer REDIS_KEY_MQ_QUEUE_MAX_NUM = 10;

    /**
     * 发送消息前数据整理操作的MQ的groupname
     */
    public final static String GROUP_NAME_ADD_MESSAGE = "MSG_AddMessage";

    /**
     * email类型消息的groupname
     */
    public final static String GROUP_NAME_MSG_EMAIL = "MSG_EMAIL";

    /**
     * 发送打印消息的MQ的groupname
     */
    public final static String GROUP_NAME_REPORT_ADD_QUEUE = "Report_AddQueue";

    /**
     * 发送AS处理消息的MQ的groupname
     */
    public final static String GROUP_NAME_AS_ORDERSCRAP_QUEUE = "AS_ORDERSCRAP_Queue";

    /**
     * 发送AS处理消息的MQ的groupname
     */
    public final static String GROUP_NAME_AS_KEEPCAR_QUEUE = "AS_KEEPCAR_Queue";
    /**
     * 发送AS处理消息的MQ的groupname(待开工队列)
     */
    public final static String GROUP_NAME_AS_QUEUESTART_QUEUE = "AS_QUEUESTART_Queue";

    /**
     * 发送Lms处理消息的MQ的groupname(整车锁定计划)
     */
    public final static String GROUP_NAME_LMS_LOCKPLAN_QUEUE = "LMS_LOCKPLAN_Queue";
    /**
     * 发送Lms处理消息的MQ的groupname(工位物料)
     */
    public final static String GROUP_NAME_LMS_WORKSTATIONMATERIAL_SIGTRUE = "LMS_WORKSTATIONMATERIAL_SIGTRUE";
}
