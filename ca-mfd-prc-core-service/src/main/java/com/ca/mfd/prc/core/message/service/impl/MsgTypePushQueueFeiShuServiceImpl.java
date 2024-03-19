package com.ca.mfd.prc.core.message.service.impl;

import com.ca.mfd.prc.common.enums.MethodType;
import com.ca.mfd.prc.core.message.entity.MsgPushEntity;
import com.ca.mfd.prc.core.message.service.IMsgTypePushQueueService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jay.he
 * @Description: 发钉钉消息-发送消息到队列
 */
@Service
public class MsgTypePushQueueFeiShuServiceImpl implements IMsgTypePushQueueService {
    private final static String channelGroup = "FeiShu";

    @Override
    public void push(List<MsgPushEntity> request) {

    }

    @Override
    public String msgType() {
        return MethodType.FeiShu.name();
    }
}