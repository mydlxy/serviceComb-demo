package com.ca.mfd.prc.mq.rabbitmq.service.impl;


import cn.hutool.core.util.IdUtil;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.mq.rabbitmq.entity.RabbitMQContext;
import com.ca.mfd.prc.mq.rabbitmq.entity.RabbitMQExchangeConstants;
import com.ca.mfd.prc.mq.rabbitmq.service.IRabbitMqProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 生产者实现类
 */
@ConditionalOnExpression("#{'true'.equals(environment.getProperty('inkelink.core.mq.rabbitmq.enable'))}")
@Component
public class RabbitMqProducerServiceImpl implements IRabbitMqProducerService {

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 交换机
     */
    private String exchange = RabbitMQExchangeConstants.MFD_INFO_EXCHANGE;

    /**
     * 路由
     */
    // private String routingKey;
    @Override
    public void send(RabbitMQContext msg) {
        if (msg == null) {
            throw new IllegalArgumentException("消息队列不能为空！");
        }
        if (!StringUtils.hasText(msg.getTopic())) {
            throw new IllegalArgumentException("消息队列Topic不能为空！");
        }
        if (!StringUtils.hasText(msg.getContent())) {
            throw new IllegalArgumentException("消息队列Content不能为空！");
        }
        if (msg.getNotesId() == null || msg.getNotesId() <= 0) {
            throw new IllegalArgumentException("消息队列通道ID不能为空！");
        }
        if (!StringUtils.hasText(msg.getGroupName())) {
            throw new IllegalArgumentException("消息队列groupName不能为空！");
        }
        MessagePostProcessor messagePostProcessor = (message) -> {
            MessageProperties messageProperties = message.getMessageProperties();
            messageProperties.setMessageId(IdUtil.randomUUID());
            messageProperties.setTimestamp(new Date());
            messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);//消息的持久化
            return message;
        };

       /* MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentEncoding("UTF-8");
        messageProperties.setContentType("text/plain");
        String data = JSONUtil.toJsonStr(msg);
        Message message = new Message(data.getBytes(StandardCharsets.UTF_8), messageProperties);*/

        // 指定消息类型
      /*  MessageProperties props = MessagePropertiesBuilder.newInstance()
                .setContentType(MessageProperties.CONTENT_TYPE_JSON).build();*/

        //   String message = new ObjectMapper().writeValueAsString(msg);
        //  String message = JSONUtil.toJsonStr(msg);
        String message = JsonUtils.toJsonString(msg);
        // rabbitTemplate.convertAndSend(this.exchange, routingKey, message, messagePostProcessor);
        rabbitTemplate.convertAndSend(this.exchange, msg.getTopic(), message, messagePostProcessor);
        /*rabbitTemplate.convertAndSend(this.exchange,
                routingKey, new ObjectMapper().writeValueAsString(msg));*/
    }

}


