package com.ca.mfd.prc.pmc.communication.handlers.kafka;

import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.mq.kafka.entity.KafkaMessageContext;
import com.ca.mfd.prc.mq.kafka.entity.KafkaTopicConstants;
import com.ca.mfd.prc.pmc.service.IPmcAlarmComponentAlarmService;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@ConditionalOnExpression("#{'true'.equals(environment.getProperty('inkelink.core.mq.kafka.enable'))}")
public class KafkaConsumer {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    @Autowired
    IPmcAlarmComponentAlarmService pmcAlarmComponentAlarmService;

    @KafkaListener(topics = KafkaTopicConstants.TOPIC_IOT_TOMOM)
    public void topicIotToMomHandler(ConsumerRecord<String, String> record, Acknowledgment ack) {
        Optional<String> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            String message = kafkaMessage.get();
            KafkaMessageContext context = JsonUtils.parseObject(message, KafkaMessageContext.class);
            logger.info("接收到IOT推送的设备报警数据:" + message);
            if(StringUtils.isNotBlank(message)){
                pmcAlarmComponentAlarmService.analysisIotData(message);
            }
        }
    }
}
