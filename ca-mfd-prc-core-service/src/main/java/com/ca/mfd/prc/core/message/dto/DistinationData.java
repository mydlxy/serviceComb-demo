package com.ca.mfd.prc.core.message.dto;

import lombok.Data;

@Data
public class DistinationData {

    /**
     * 接收对象
     */
    private String distination;

    /**
     * 消息推送方式
     */
    private int method;
}
