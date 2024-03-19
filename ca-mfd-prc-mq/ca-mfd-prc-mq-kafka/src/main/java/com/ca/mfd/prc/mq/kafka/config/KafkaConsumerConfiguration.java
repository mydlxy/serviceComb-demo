package com.ca.mfd.prc.mq.kafka.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;

@Data
@ConditionalOnExpression("#{'true'.equals(environment.getProperty('inkelink.core.mq.kafka.enable'))}")
@Configuration
public class KafkaConsumerConfiguration {

    @Value("${inkelink.core.mq.kafka.consumer.group-id}")
    private String groupId;

    @Value("${inkelink.core.mq.kafka.consumer.enable-auto-commit:#{false}}")
    private boolean enableAutoCommitConfig;

    @Value("${inkelink.core.mq.kafka.consumer.auto-offset-reset:latest}")
    private String autoOffsetResetConfig;

    @Value("${inkelink.core.mq.kafka.consumer.key-deserializer:}")
    private String keyDeserializer;

    @Value("${inkelink.core.mq.kafka.consumer.value-deserializer:}")
    private String valueDeserializer;

    @Value("${inkelink.core.mq.kafka.consumer.max-poll-records:100}")
    private Integer maxPollRecordsConfig;


}
