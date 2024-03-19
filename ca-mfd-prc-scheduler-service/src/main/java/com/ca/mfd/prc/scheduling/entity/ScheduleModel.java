package com.ca.mfd.prc.scheduling.entity;

import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;


@Data
public class ScheduleModel {
    /**
     * 任务名称
     */
    private String jobName = StringUtils.EMPTY;

    /**
     * 任务分组
     */
    private String jobGroup = StringUtils.EMPTY;

    /**
     * 全局唯一标识
     */
    private String appId = StringUtils.EMPTY;

    /**
     * 任务类型 目前就是URL
     */
    private JobTypeEnum jobType = JobTypeEnum.URL;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date beginTime = new Date();

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date endTime ;

    /**
     * Cron表达式
     */
    //   [DisplayFormat(ConvertEmptyStringToNull = false)]
    private String cron = StringUtils.EMPTY;

    /**
     * 执行次数（默认无限循环）
     */
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer runTimes = 0;

    /**
     * 执行间隔时间，单位秒（如果有Cron，则IntervalSecond失效 前端始终传cron，该字段是后端处理）
     */
    private Integer intervalSecond;

    /**
     * 触发器类型
     */
    private TriggerTypeEnum triggerType = TriggerTypeEnum.CRON;

    /**
     * 描述
     */
    //    [DisplayFormat(ConvertEmptyStringToNull = false)]
    private String description = StringUtils.EMPTY;

    private MailMessageEnum mailMessage;//email类型才需要

    /**
     * 请求url
     */
    //    [DisplayFormat(ConvertEmptyStringToNull = false)]
    private String requestUrl = StringUtils.EMPTY;

    /**
     * 请求参数（Post，Put请求用）
     */
    private String requestParameters = StringUtils.EMPTY; //没用，POST，PUT是requestType

    private String dataHandler = StringUtils.EMPTY;//没用

    /// <summary>
    /// Headers(可以包含如：Authorization授权认证)
    /// 格式：{"Authorization":"userpassword.."}
    /// </summary>
    //public string Headers { get; set; }
    private String requestHeader = StringUtils.EMPTY;//.NET版没传

    /**
     * 请求类型
     */
    private RequestTypeEnum requestType = RequestTypeEnum.GET;
}
