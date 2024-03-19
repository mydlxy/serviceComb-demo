package com.ca.mfd.prc.core.message.handler.kafka;


import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.mq.kafka.entity.KafkaMessageContext;
import com.ca.mfd.prc.mq.kafka.entity.KafkaTopicConstants;
import com.ca.mfd.prc.mq.rabbitmq.entity.RabbitMQContext;
import com.ca.mfd.prc.mq.rocketmq.utils.RocketMqProducerHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.Optional;

//push模式
@Component
@ConditionalOnExpression("#{'true'.equals(environment.getProperty('inkelink.core.mq.kafka.enable'))}")
public class KafkaConsumer {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);
    /**
     * 消费字符串
     *
     * @param record
     */
    @KafkaListener(topics = KafkaTopicConstants.TOPIC_TEST1)
    public void topicTest1Handler(ConsumerRecord<String, String> record, Acknowledgment ack) {
        Optional<String> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            String message = kafkaMessage.get();
            KafkaMessageContext context = JsonUtils.parseObject(message, KafkaMessageContext.class);
           // KafkaMessageContext context = JSONUtil.toBean(message, KafkaMessageContext.class);
            logger.info("收到kafka消息：" + message);
        }
        //   ack.acknowledge();
    }

    @KafkaListener(topics = {KafkaTopicConstants.TOPIC_TEST2, KafkaTopicConstants.TOPIC_TEST3})
    public void topicTest2Handler(ConsumerRecord<String, String> record, Acknowledgment ack) {
        Optional<String> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            String message = kafkaMessage.get();
         //   KafkaMessageContext context = JSONUtil.toBean(message, KafkaMessageContext.class);
            KafkaMessageContext context = JsonUtils.parseObject(message, KafkaMessageContext.class);
            logger.info("收到kafka消息：" + message);
        }
        //    ack.acknowledge();
    }

    /**
     * 一次消费多条记录
     *
     * @param records
     */
   /* @KafkaListener(topics = KafkaTopicConstants.TOPIC_TEST2)
    public void topicTest2Handler(ConsumerRecords<?, ?> records, Acknowledgment ack) {
        for (ConsumerRecord<?, ?> record : records) {
            Optional<?> kafkaMessage = Optional.ofNullable(record.value());
            if (kafkaMessage.isPresent()) {
                Object message = kafkaMessage.get();
                // log.info("----------------- record =" + record);
                log.info("收到kafka消息：" + message);
            }
        }
        ack.acknowledge();
    }*/


}
