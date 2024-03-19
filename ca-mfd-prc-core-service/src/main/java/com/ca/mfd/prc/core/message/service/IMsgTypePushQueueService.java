package com.ca.mfd.prc.core.message.service;

import com.ca.mfd.prc.core.message.entity.MsgPushEntity;

import java.util.List;

/**
 * @author jay.he
 * @Description: 不同类型发送消息到队列接口（比如email、钉钉等）
 */
public interface IMsgTypePushQueueService {
    void push(List<MsgPushEntity> request);

    String msgType();
}