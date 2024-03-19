package com.ca.mfd.prc.scheduling.entity;

import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Trigger;

import java.util.Date;

@Data
public class JobInfoModel extends BaseEntity {

    private Long id = Constant.DEFAULT_ID;

    /**
     * 任务组名
     */
    private String groupName;

    /**
     * 任务名称
     */
    private String name;

    /**
     * 下次执行时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date nextFireTime;

    /**
     * 上次执行时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date previousFireTime;

///// <summary>
///// 开始时间
///// </summary>
//public DateTime BeginTime { get; set; }

///// <summary>
///// 结束时间
///// </summary>
//public DateTime? EndTime { get; set; }

    /**
     * 上次执行的异常信息
     */
    private String lastErrMsg = StringUtils.EMPTY;

    /**
     * 任务状态
     */
    private Trigger.TriggerState triggerState;

    /**
     * 描述
     */
    private String description = StringUtils.EMPTY;

    private String requestHeader = StringUtils.EMPTY;

    /**
     * 如果是cron触发器就存cron值，如果是简单触发器就存间隔时间
     */
    // private String interval;
    private String cron = StringUtils.EMPTY;

    /**
     * 触发地址
     */
    private String triggerAddress = StringUtils.EMPTY;
    /**
     * 请求类型 1-GET，2-POST
     */
    private String requestType = StringUtils.EMPTY;

    /**
     * 已经执行的次数
     */
    private long runNumber = Constant.DEFAULT_USER_ID;
    private long jobType = Constant.DEFAULT_USER_ID;
    private String dataHandler = StringUtils.EMPTY;
    private String appId = StringUtils.EMPTY;

}
