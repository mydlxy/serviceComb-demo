package com.ca.mfd.prc.mq.kafka.config;


import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@ConditionalOnExpression("#{'true'.equals(environment.getProperty('inkelink.core.mq.kafka.enable'))}")
@Configuration
public class ProducerFactoryBuilder {

    @Autowired
    private KafkaConfiguration kafkaConfiguration;

    @Autowired
    private KafkaProducerConfiguration kafkaProducerConfiguration;

    @Autowired
    private KafkaSendResultHandler producerListener;


    /**
     * 生产者配置
     *
     * @return 配置
     */
    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>(11);
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfiguration.getBootstrapServers());

        props.put(ProducerConfig.ACKS_CONFIG, kafkaProducerConfiguration.getAcks());
        props.put(ProducerConfig.RETRIES_CONFIG, kafkaProducerConfiguration.getRetries());
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, kafkaProducerConfiguration.getBatchSize());
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, kafkaProducerConfiguration.getBufferMemory());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, kafkaProducerConfiguration.getKeySerializer());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, kafkaProducerConfiguration.getValueSerializer());
        if (StringUtils.hasText(kafkaConfiguration.getUserName()) && StringUtils.hasText(kafkaConfiguration.getPassword())) {
            //设置sasl认证
            //props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_PLAINTEXT");
            props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, kafkaProducerConfiguration.getSaslSsl());
            //SASL鉴权方式
            props.put(SaslConfigs.SASL_MECHANISM,kafkaProducerConfiguration.getSaslMechanism());
            //设置jaas账号和密码
            props.put(SaslConfigs.SASL_JAAS_CONFIG, "org.apache.kafka.common.security.plain.PlainLoginModule required username='" + kafkaConfiguration.getUserName() + "' password='" + kafkaConfiguration.getPassword() + "';");
            if(StringUtils.hasText(kafkaProducerConfiguration.getSslTruststoreLocation())){
                //设置ssl|truststore文件位置
                props.put("ssl.truststore.location",kafkaProducerConfiguration.getSslTruststoreLocation());
                //设置 truststore密码
                props.put("ssl.truststore.password",kafkaProducerConfiguration.getSslTruststorePassword());
                //域名不校验
                props.put("ssl.endpoint.identification.algorithm",kafkaProducerConfiguration.getSslEndpointIdentificationAlgorithm());
            }
        }

        return props;
    }


    /**
     * Producer Template 配置
     */
    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        Map<String, Object> stringObjectMap = producerConfigs();
        DefaultKafkaProducerFactory<String, String> objectObjectDefaultKafkaProducerFactory = new DefaultKafkaProducerFactory<>(stringObjectMap);
        KafkaTemplate<String, String> kafkaTemplate = new KafkaTemplate<>(objectObjectDefaultKafkaProducerFactory);
        kafkaTemplate.setProducerListener(producerListener);
        return kafkaTemplate;
    }
}
