package com.ca.mfd.prc.core.message.handler.rocketmq;


import com.ca.mfd.prc.mq.rocketmq.entity.RocketMqTopicConstants;
import com.ca.mfd.prc.mq.rocketmq.handler.BaseRocketMqConsumerGroupHandler;
import com.ca.mfd.prc.mq.rocketmq.handler.IRocketMqConsumer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.util.List;

//push模式
@Component
@ConditionalOnExpression("#{'true'.equals(environment.getProperty('inkelink.core.mq.rocketmq.enable'))}")
@RocketMQMessageListener(topic = ""
        , consumerGroup = "${inkelink.core.mq.rocketmq.consumer.groupSystem1}"
        , nameServer = "${inkelink.core.mq.rocketmq.name-server}"
        , accessKey = "${inkelink.core.mq.rocketmq.access-key:}"
        , secretKey = "${inkelink.core.mq.rocketmq.secret-key:}")
public class RocketMqConsumerGroupHandler2 extends BaseRocketMqConsumerGroupHandler implements RocketMQListener<String>, RocketMQPushConsumerLifecycleListener {
   // private static final Logger logger = LoggerFactory.getLogger(RocketMqConsumerGroupHandler1.class);
    public RocketMqConsumerGroupHandler2(List<IRocketMqConsumer> actions) {
        this.consumers.addAll(actions);
    }

    @Override
    public void onMessage(String message) {
        //   this.messageHandle(message);
    }

    @SneakyThrows
    @Override
    public void prepareStart(DefaultMQPushConsumer consumer) {
        this.basePrepareStart(consumer, true);

        consumer.subscribe(RocketMqTopicConstants.TOPIC_TEST2, "*");
        consumer.subscribe(RocketMqTopicConstants.TOPIC_TEST3, "*");
    }

}
