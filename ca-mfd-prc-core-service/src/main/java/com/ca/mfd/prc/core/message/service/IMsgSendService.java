package com.ca.mfd.prc.core.message.service;

import com.ca.mfd.prc.common.model.main.ReportQueue;
import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.core.message.dto.MessageContent;
import com.ca.mfd.prc.core.message.entity.MsgSendEntity;

import java.util.List;

/**
 * @author jay.he
 * @Description: 信息发送记录
 */
public interface IMsgSendService extends ICrudService<MsgSendEntity> {

    /**
     * 添加推送数据
     *
     * @param content
     */
    void addMessage(MessageContent content);

    /**
     * 重发
     *
     * @param ids
     */
    void restMessagePush(List<String> ids);

    /**
     * 发送普通消息
     *
     * @param model
     */
    void pushSimpleMessage(MessageContent model);

    /**
     * 发送打印消息
     *
     * @param model
     */
    void pushReportMessage(ReportQueue model);

}