package com.ca.mfd.prc.core.message.handler.rocketmq;


import com.ca.mfd.prc.mq.rocketmq.annotation.MesRocketMqConsumer;
import com.ca.mfd.prc.mq.rocketmq.entity.RocketMqTopicConstants;
import com.ca.mfd.prc.mq.rocketmq.handler.IRocketMqConsumer;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

//push模式
@MesRocketMqConsumer(topic = RocketMqTopicConstants.TOPIC_TEST2)
@Component
@ConditionalOnExpression("#{'true'.equals(environment.getProperty('inkelink.core.mq.rocketmq.enable'))}")
public class RocketMqConsumer2 implements IRocketMqConsumer {
    private static final Logger logger = LoggerFactory.getLogger(RocketMqConsumer2.class);
    @Override
    public void onMessage(String message) {
        // String body = new String(message.getBody(), StandardCharsets.UTF_8);
        //  RocketMQMessageContent rocketMQContent = JSONUtil.toBean(body, RocketMQMessageContent.class);
        logger.info("RocketMqConsumer2 Received message : " + message);
    }


}
