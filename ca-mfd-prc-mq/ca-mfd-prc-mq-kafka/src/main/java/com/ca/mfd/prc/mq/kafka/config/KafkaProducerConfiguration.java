package com.ca.mfd.prc.mq.kafka.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;

@Data
@ConditionalOnExpression("#{'true'.equals(environment.getProperty('inkelink.core.mq.kafka.enable'))}")
@Configuration
public class KafkaProducerConfiguration {

    @Value("${inkelink.core.mq.kafka.producer.retries:0}")
    private Integer retries;

    @Value("${inkelink.core.mq.kafka.producer.batch-size:16384}")
    private Integer batchSize;

    @Value("${inkelink.core.mq.kafka.producer.buffer-memory:33554432}")
    private Integer bufferMemory;

    @Value("${inkelink.core.mq.kafka.producer.acks:all}")
    private String acks;

    @Value("${inkelink.core.mq.kafka.producer.key-serializer:}")
    private String keySerializer;

    @Value("${inkelink.core.mq.kafka.producer.value-serializer:}")
    private String valueSerializer;

    /**
     * SASL鉴权方式
     */
    @Value("${inkelink.core.mq.kafka.producer.sasl-mechanism:PLAIN}")
    private String saslMechanism;

    /**
     * 加密协议，目前支持SASL_SSL协议
     */
    @Value("${inkelink.core.mq.kafka.producer.security-protocol:SASL_SSL}")
    private String saslSsl;

    /**
     * ssl truststore文件位置
     */
    @Value("${inkelink.core.mq.kafka.producer.ssl-truststore-location:}")
    private String sslTruststoreLocation;
    /**
     * ssl truststore密码
     */
    @Value("${inkelink.core.mq.kafka.producer.ssl-truststore-password:}")
    private String  sslTruststorePassword;
    /**
     * 域名不校验
     */
    @Value("${inkelink.core.mq.kafka.producer.ssl-endpoint-identification-algorithm:}")
    private String sslEndpointIdentificationAlgorithm;

}
