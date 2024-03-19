package com.ca.mfd.prc.core.message.handler;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.core.message.channel.MailChannel;
import com.ca.mfd.prc.core.message.dto.EmailModel;
import com.ca.mfd.prc.core.message.entity.MsgEmailConfigEntity;
import com.ca.mfd.prc.core.message.entity.MsgPushEntity;
import com.ca.mfd.prc.core.message.service.IMsgEmailConfigService;
import com.ca.mfd.prc.core.message.service.IMsgPushService;
import com.ca.mfd.prc.core.message.service.IMsgSendService;
import com.ca.mfd.prc.mq.rabbitmq.annotation.MesRabbitListener;
import com.ca.mfd.prc.mq.rabbitmq.entity.PatternEnum;
import com.ca.mfd.prc.mq.rabbitmq.entity.RabbitMQContext;
import com.ca.mfd.prc.mq.rabbitmq.entity.RabbitMQQueueConstants;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

/**
 *
 * @Description: EmailHandler
 * @author inkelink
 * @date 2023年10月09日
 * @变更说明 BY inkelink At 2023年10月09日
 */
@ConditionalOnExpression("#{'true'.equals(environment.getProperty('inkelink.core.mq.rabbitmq.enable'))}")
@Component
public class EmailHandler {
    private static final Logger logger = LoggerFactory.getLogger(EmailHandler.class);
    @Autowired
    IMsgSendService msgSendService;
    @Autowired
    IMsgPushService msgPushService;
    @Autowired
    IMsgEmailConfigService msgEmailConfigService;
    @Autowired
    MailChannel mailChannel;

    @MesRabbitListener(queues = RabbitMQQueueConstants.SEND_EMAIL_QUEUE, pattern = PatternEnum.AutoRetry)
    public void sendEmailQueue(Message message, Channel channel) throws IOException {
       // String strMessage = Base64.getEncoder().encodeToString(message.getBody());
        String strMessage = org.apache.commons.lang3.StringUtils.toEncodedString(message.getBody(), StandardCharsets.UTF_8);
        logger.info("sendEmailQueue消费信息：" + strMessage);
        RabbitMQContext rabbitMQContext = JsonUtils.parseObject(strMessage, RabbitMQContext.class);
       // RabbitMQContext rabbitMQContext = JSONUtil.toBean(strMessage, RabbitMQContext.class);
        if (rabbitMQContext != null) {
            EmailModel model = JsonUtils.parseObject(rabbitMQContext.getContent(), EmailModel.class);
           // EmailModel model = JSONUtil.toBean(rabbitMQContext.getContent(), EmailModel.class);
            if (model != null) {
                List<MsgEmailConfigEntity> msgEmailConfigEntities = msgEmailConfigService.getData(new ArrayList<>(), false);
                if (CollectionUtils.isEmpty(msgEmailConfigEntities)) {
                    throw new InkelinkException("没有配置发送邮箱！");
                }
                MsgEmailConfigEntity msgEmailConfigEntity = msgEmailConfigEntities.get(0);
                try {
                    mailChannel.sendMail(model.getDistination()
                            , msgEmailConfigEntity.getEmailTitle() + model.getSubjectPush()
                            , model.getContentPush()
                            , msgEmailConfigEntity.getEmailAccount()
                            , msgEmailConfigEntity.getEmailHost()
                            , msgEmailConfigEntity.getEmailPassword()
                            , String.valueOf(msgEmailConfigEntity.getEmailPort()));
                    model.setStatus(2);
                } catch (Exception ex) {
                    logger.error("消息邮件通知发生异常: " + ex.getMessage() + "=====to:" + model.getDistination());
                    model.setStatus(3);
                    model.setRemark(ex.getMessage());
                }
                updateMessagePush(model);
            }
        }

    }

    private void updateMessagePush(EmailModel item) {
        item.setSendTimes(item.getSendTimes() + 1);
        item.setSendDt(new Date());
        LambdaUpdateWrapper<MsgPushEntity> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(MsgPushEntity::getSendTimes, item.getSendTimes());
        updateWrapper.set(MsgPushEntity::getSendDt, item.getSendDt());
        updateWrapper.set(MsgPushEntity::getStatus, item.getStatus());
        updateWrapper.set(MsgPushEntity::getRemark, item.getRemark());
        updateWrapper.eq(MsgPushEntity::getId, item.getId());
        msgPushService.update(updateWrapper);
        msgPushService.saveChange();
    }


}
