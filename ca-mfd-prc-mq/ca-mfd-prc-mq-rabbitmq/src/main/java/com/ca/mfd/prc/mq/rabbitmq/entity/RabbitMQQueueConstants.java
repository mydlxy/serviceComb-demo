package com.ca.mfd.prc.mq.rabbitmq.entity;


public class RabbitMQQueueConstants {
    /**
     * 消息队列-test1
     **/
    public static final String TEST1_QUEUE = "mq.mfd.queue.test1";

    /**
     * 消息队列-test2
     **/
    public static final String TEST2_QUEUE = "mq.mfd.queue.test2";

    /**
     * 消息队列-test3
     **/
    public static final String TEST3_QUEUE = "mq.mfd.queue.test3";

    /**
     * 消息队列-test4
     **/
    public static final String TEST4_QUEUE = "mq.mfd.queue.test4";

    /**
     * 发送信息（比如邮件、飞书信息等）队列
     **/
    public static final String ADD_MESSAGE_QUEUE = "EQuality.Message.Service.Handlers.AddMessageHandler";
    // public static final String ADD_MESSAGE_QUEUE = "EQuality.Message.Service.Handlers.AddMessageLocalHandler";

    /**
     * 发送邮件消息队列
     **/
    public static final String SEND_EMAIL_QUEUE = "EQuality.Message.Service.Handlers.EmailHandler";
    // public static final String SEND_EMAIL_QUEUE = "EQuality.Message.Service.Handlers.EmailLocalHandler";

    /**
     * 发送打印信息队列
     **/
    public static final String ADD_REPORT_QUEUE = "EQuality.MES.Report.Host.Handlers.AddReportHandler";
    //public static final String ADD_REPORT_QUEUE = "EQuality.MES.Report.Host.Handlers.AddReportLocalHandler1";

    /**
     * 工位物料数据生成
     */
    public static final String ADD_LMSMATERIAL_QUEUE = "EQuality.MES.PM.Host.Handlers.AddLmsMaterialHandler";

    /**
     * 发送AS队列--订单报废
     **/
    public static final String ADD_AS_ORDERSCRAP_QUEUE = "EQuality.MES.PPS.Host.Handlers.AsOrderScrapHandler";

    /**
     * 发送AS队列--保留车
     **/
    public static final String ADD_AS_KEEPCAR_QUEUE = "EQuality.MES.PPS.Host.Handlers.AsKeepCarHandler";

    /**
     * 发送AS队列--待开工队列
     **/
    public static final String ADD_AS_QUEUESTART_QUEUE = "EQuality.MES.PPS.Host.Handlers.AsQueueStartHandler";

    /**
     * 发送LMS整车计划锁定
     */
    public static final String ADD_LMS_LOCKPLAN_QUEUE = "EQuality.MES.PPS.Host.Handlers.LmsLockPlanHandler";

    /**
     * 发送LMS整车计划锁定
     */
    public static final String ADD_PQS_ENTRY_REPORT_CJ_SUBSCRIBE = "EQuality.MES.PQS.Host.Handlers.EntryReportCJSubscribeHandler";

    /**
     * 订阅报工质结果，调整生产进度和状态
     **/
    public static final String ADD_AS_ENTRY_REPORT_PPSJ_SUBSCRIBE = "EQuality.MES.PPS.Host.Handlers.PPSJEntryReporPartsSubscribe";
}
