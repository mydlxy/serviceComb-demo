package com.ca.mfd.prc.core.message.dto;

import lombok.Data;

@Data
public class EmailModel extends ChannelModelBase {

    /**
     * 接收对象
     */
    private String distination;

    /**
     * 推送主题
     */
    private String subjectPush;
}
