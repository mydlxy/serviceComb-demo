package com.ca.mfd.prc.core.message.handler;

import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.core.message.dto.MessageContent;
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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 *
 * @Description: AddMessageHandler
 * @author inkelink
 * @date 2023年10月09日
 * @变更说明 BY inkelink At 2023年10月09日
 */
@ConditionalOnExpression("#{'true'.equals(environment.getProperty('inkelink.core.mq.rabbitmq.enable'))}")
@Component
public class AddMessageHandler {
    private static final Logger logger = LoggerFactory.getLogger(AddMessageHandler.class);
    @Autowired
    IMsgSendService msgSendService;

    @MesRabbitListener(queues = RabbitMQQueueConstants.ADD_MESSAGE_QUEUE, pattern = PatternEnum.AutoRetry)
    public void addMessageQueue(Message message, Channel channel) throws IOException {
        // String strMessage = Base64.getEncoder().encodeToString(message.getBody());
        String strMessage = org.apache.commons.lang3.StringUtils.toEncodedString(message.getBody(), StandardCharsets.UTF_8);
        logger.info("addMessageQueue消费信息：" + strMessage);
        RabbitMQContext rabbitMQContext = JsonUtils.parseObject(strMessage, RabbitMQContext.class);
        //  RabbitMQContext rabbitMQContext = JSONUtil.toBean(strMessage, RabbitMQContext.class);
        if (rabbitMQContext != null) {
            MessageContent messageContent = JsonUtils.parseObject(rabbitMQContext.getContent(), MessageContent.class);
            //  MessageContent messageContent = JSONUtil.toBean(rabbitMQContext.getContent(), MessageContent.class);
            if (messageContent != null) {
                try {
                    msgSendService.addMessage(messageContent);
                } catch (Exception ex) {
                    ex.getMessage();
                }

            }
        }

    }

}
