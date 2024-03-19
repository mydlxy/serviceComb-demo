package com.ca.mfd.prc.mq.kafka.config;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.stereotype.Component;

@ConditionalOnExpression("#{'true'.equals(environment.getProperty('inkelink.core.mq.kafka.enable'))}")
@Component
public class KafkaSendResultHandler implements ProducerListener {
    private static final Logger log = LoggerFactory.getLogger(KafkaSendResultHandler.class);

    /**
     * kafka发送成功回调
     * @param producerRecord
     * @param recordMetadata
     */
    @Override
    public void onSuccess(ProducerRecord producerRecord, RecordMetadata recordMetadata) {
        String key = producerRecord.key().toString();
        String topic = producerRecord.topic();
        log.info("key：{}，topic：{}， 发送成功回调",key,topic);
    }

    @Override
    public void onError(ProducerRecord producerRecord,RecordMetadata recordMetadata, Exception exception) {
        String key = producerRecord.key().toString();
        String topic = producerRecord.topic();
        log.info("key：{}，topic：{}， 发送异常回调:{}",key,topic, exception.getMessage());
    }

}
