package com.ca.mfd.prc.mq.kafka.config;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ConditionalOnExpression("#{'true'.equals(environment.getProperty('inkelink.core.mq.kafka.enable'))}")
@Configuration
public class ConsumerFactoryBuilder {

    @Autowired
    private KafkaConfiguration kafkaConfiguration;

    @Autowired
    private KafkaConsumerConfiguration kafkaConsumerConfiguration;

    @Autowired
    private KafkaListenerConfiguration kafkaListenerConfiguration;

    /**
     * 消费者配置
     *
     * @return properties
     */
    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new ConcurrentHashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfiguration.getBootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaConsumerConfiguration.getGroupId());
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, kafkaConsumerConfiguration.isEnableAutoCommitConfig());

        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaConsumerConfiguration.getAutoOffsetResetConfig());
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, kafkaConsumerConfiguration.getMaxPollRecordsConfig());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, kafkaConsumerConfiguration.getKeyDeserializer());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, kafkaConsumerConfiguration.getValueDeserializer());

        if (StringUtils.hasText(kafkaConfiguration.getUserName()) && StringUtils.hasText(kafkaConfiguration.getPassword())) {
            //设置sasl认证
            props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_PLAINTEXT");
            props.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
            props.put(SaslConfigs.SASL_JAAS_CONFIG, "org.apache.kafka.common.security.plain.PlainLoginModule required username='" + kafkaConfiguration.getUserName() + "' password='" + kafkaConfiguration.getPassword() + "';");
        }
        return props;
    }

    /**
     * kafka消费者工厂
     */
    @Bean
    public ConsumerFactory<Object, Object> consumerFactory() {
        return new DefaultKafkaConsumerFactory(consumerConfigs());
    }


    /**
     * 监听工厂
     */
    @Bean
    KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<Object, Object>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        //开启批量处理
        //  factory.setBatchListener(kafkaListenerConfiguration.getBatchListener());
        //  factory.getContainerProperties().setPollTimeout(kafkaListenerConfiguration.getPollTimeout());
        return factory;
    }

}
