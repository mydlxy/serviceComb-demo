package com.ca.mfd.prc.mq.rabbitmq.entity;

import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.util.Date;

@Data
public class MQLogInformation {
    private String topic;
    private String channelName;
    private String content;
    private Long noteId;
    /**
     * 报错信息的堆栈
     */
   // private String stackTrace;
    /**
     * 报错信息的message
     */
    private String errMessage;




    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date startTime;
    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date overTime;
    /**
     * 消耗时间 s
     */
    private double expendTime;
    private String className;
    private String nameSpace;
}

