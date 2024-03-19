package com.ca.mfd.prc.core.message.service.impl;

import com.ca.mfd.prc.common.enums.MethodType;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.core.message.dto.EmailModel;
import com.ca.mfd.prc.core.message.entity.MsgPushEntity;
import com.ca.mfd.prc.core.message.service.IMsgTypePushQueueService;
import com.ca.mfd.prc.mq.rabbitmq.entity.RabbitMQConstants;
import com.ca.mfd.prc.mq.rabbitmq.remote.app.core.IRabbitSysQueueNoteService;
import com.ca.mfd.prc.mq.rabbitmq.remote.app.core.entity.SysQueueNoteEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jay.he
 * @Description: 发邮件-发送消息到队列
 */
@Service
public class MsgTypePushQueueEmailServiceImpl implements IMsgTypePushQueueService {

    @Autowired
    IRabbitSysQueueNoteService sysQueueNoteService;

    @Override
    public void push(List<MsgPushEntity> request) {
        for (MsgPushEntity entity : request) {
            EmailModel emailModel = new EmailModel();
            emailModel.setSubjectPush(entity.getSubjectPush());
            emailModel.setDistination(entity.getDistination());
            emailModel.setContentPush(entity.getContentPush());
            emailModel.setSendTimes(entity.getSendTimes());
            emailModel.setSendDt(entity.getSendDt());
            emailModel.setStatus(entity.getStatus());
            emailModel.setRemark(entity.getRemark());
            emailModel.setId(entity.getId());

            SysQueueNoteEntity sysQueueNoteEntity = new SysQueueNoteEntity();
            sysQueueNoteEntity.setGroupName(RabbitMQConstants.GROUP_NAME_MSG_EMAIL);
            sysQueueNoteEntity.setContent(JsonUtils.toJsonString(emailModel));
            sysQueueNoteService.addSimpleMessage(sysQueueNoteEntity);
        }
    }

    @Override
    public String msgType() {
        return MethodType.Email.name();
    }
}