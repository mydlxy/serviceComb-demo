package com.ca.mfd.prc.core.message.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ChannelModelBase {


    private Long id;

    /**
     * 模板代码 如果是钉钉 请使用数字
     */
    private String tplCode;

    /**
     * 推送内容
     */
    private String contentPush;

    /**
     * 发送次数
     */
    private int sendTimes;

    /**
     * 状态(1.未发送;2.发送成功;3.发送失败;4重新发送)
     */
    private int status;

    /**
     * 发送时间
     */
    private Date sendDt;

    /**
     * 备注
     */
    private String remark;
}
