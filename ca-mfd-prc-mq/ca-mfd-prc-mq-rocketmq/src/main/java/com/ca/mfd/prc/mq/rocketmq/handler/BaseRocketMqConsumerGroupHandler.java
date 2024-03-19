package com.ca.mfd.prc.mq.rocketmq.handler;

import com.ca.mfd.prc.mq.rocketmq.annotation.MesRocketMqConsumer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.annotation.Annotation;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

//push模式
@Component
@ConditionalOnExpression("#{'true'.equals(environment.getProperty('inkelink.core.mq.rocketmq.enable'))}")
public class BaseRocketMqConsumerGroupHandler {
    @Value("${spring.application.name:}")
    private String appServiceName = "";
    @Value("${server.port:}")
    private String port = "";
    private static final Logger logger = LoggerFactory.getLogger(BaseRocketMqConsumerGroupHandler.class);
    private final static Integer MAX_RECONSUME_TIMES = 3;//最大重试次数

    protected final List<IRocketMqConsumer> consumers = new ArrayList<>();

    protected List<IRocketMqConsumer> getRocketMqConsumer(MessageExt message) {
        List<IRocketMqConsumer> result = new ArrayList<>();
        if (!CollectionUtils.isEmpty(consumers)) {
            for (IRocketMqConsumer eachConsumer : consumers) {
                Annotation[] annotations = eachConsumer.getClass().getAnnotations();
                if (annotations != null && annotations.length > 0) {
                    for (int i = 0; i < annotations.length; i++) {
                        Annotation annotation = annotations[i];
                        if (annotation instanceof MesRocketMqConsumer) {
                            MesRocketMqConsumer mesRocketMQMessageListener = (MesRocketMqConsumer) annotation;
                            if (mesRocketMQMessageListener.topic().equals(message.getTopic())) {
                                result.add(eachConsumer);
                            }
                            break;
                        }
                    }
                }

            }
        }

        return result;
    }

    @SneakyThrows
    protected void basePrepareStart(DefaultMQPushConsumer consumer) {
        basePrepareStart(consumer, false);
        // consumer = new DefaultMQPushConsumer(getAclRPCHook());

      /*  consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_TIMESTAMP);
        consumer.setNamespace("");
        consumer.setInstanceName(appServiceName + port + this.getClass().getSimpleName());
        consumer.setMaxReconsumeTimes(MAX_RECONSUME_TIMES);//最大重试次数

        consumer.registerMessageListener((List<MessageExt> messages, ConsumeConcurrentlyContext context) -> {
            messageHandle(messages);
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });*/
    }

    @SneakyThrows
    protected void basePrepareStart(DefaultMQPushConsumer consumer, boolean orderly) {
        // consumer = new DefaultMQPushConsumer(getAclRPCHook());
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_TIMESTAMP);
        consumer.setNamespace("");
        consumer.setInstanceName(appServiceName + port + this.getClass().getSimpleName());
        consumer.setMaxReconsumeTimes(MAX_RECONSUME_TIMES);//最大重试次数

        if (orderly) {
            consumer.registerMessageListener((List<MessageExt> messages, ConsumeOrderlyContext context) -> {
                messageHandle(messages);
                return ConsumeOrderlyStatus.SUCCESS;
            });
        } else {
            consumer.registerMessageListener((List<MessageExt> messages, ConsumeConcurrentlyContext context) -> {
                messageHandle(messages);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            });
        }

    }

    private void messageHandle(List<MessageExt> messages) {
        if (!CollectionUtils.isEmpty(messages)) {
            for (MessageExt message : messages) {
                int reconsumeTimes = message.getReconsumeTimes();
                String body = new String(message.getBody(), StandardCharsets.UTF_8);

                if (reconsumeTimes > 0) {
                    logger.info(this.getClass().getName() + " 第" + reconsumeTimes + "次重试消费完成:" + body);
                } else {
                    logger.info(this.getClass().getName() + " 初次消费完成:" + body);
                }
                List<IRocketMqConsumer> rocketMqConsumers = this.getRocketMqConsumer(message);
                if (!CollectionUtils.isEmpty(rocketMqConsumers)) {
                    for (IRocketMqConsumer eachConsumer : rocketMqConsumers) {
                        try {
                            // RocketMQMessageContent rocketMQMessageContent = JSONUtil.toBean(body, RocketMQMessageContent.class);
                            eachConsumer.onMessage(body);
                        } catch (Exception ex) {
                            if (reconsumeTimes >= MAX_RECONSUME_TIMES) {
                                String errMessage = String.format("消息队列消费失败，重试已达到最大次数，topic:%s，body:%s", message.getTopic(), body);
                                logger.error(errMessage);
                            } else {
                                throw ex;
                            }
                        }
                    }
                }
            }
        }
    }

    /*RPCHook getAclRPCHook() {
        if (StringUtils.hasText(accessKey) && StringUtils.hasText(secretKey)) {
            return new AclClientRPCHook(new SessionCredentials(accessKey, secretKey));
        } else {
            return null;
        }
    }*/

}
