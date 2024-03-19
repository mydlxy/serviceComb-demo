package com.ca.mfd.prc.mq.kafka.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;

@Data
@ConditionalOnExpression("#{'true'.equals(environment.getProperty('inkelink.core.mq.kafka.enable'))}")
@Configuration
public class KafkaConfiguration {
    @Value("${inkelink.core.mq.kafka.server-host:}")
    private String bootstrapServers;

    @Value("${inkelink.core.mq.kafka.username:}")
    private String userName;

    @Value("${inkelink.core.mq.kafka.password:}")
    private String password;


}