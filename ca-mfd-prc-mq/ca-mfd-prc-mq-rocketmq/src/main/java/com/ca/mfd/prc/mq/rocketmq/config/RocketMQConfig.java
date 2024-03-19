package com.ca.mfd.prc.mq.rocketmq.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.rocketmq.acl.common.AclClientRPCHook;
import org.apache.rocketmq.acl.common.SessionCredentials;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.remoting.RPCHook;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.messaging.converter.CompositeMessageConverter;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.util.StringUtils;

import java.util.List;

@ConditionalOnExpression("#{'true'.equals(environment.getProperty('inkelink.core.mq.rocketmq.enable'))}")
@Configuration
public class RocketMQConfig {
    @Value("${inkelink.core.mq.rocketmq.name-server:}")
    private String nameServer;
    @Value("${inkelink.core.mq.rocketmq.producer.group:}")
    private String producerGroup;
    @Value("${inkelink.core.mq.rocketmq.producer.send-message-timeout:}")
    private int sendMessageTimeout;
    @Value("${inkelink.core.mq.rocketmq.producer.max-message-size:}")
    private int maxMessageSize;
    @Value("${inkelink.core.mq.rocketmq.producer.retry-times-when-send-failed:}")
    private int retryTimesWhenSendFailed;
    @Value("${inkelink.core.mq.rocketmq.producer.retry-times-when-send-async-failed:}")
    private int retryTimesWhenSendAsyncFailed;
    @Value("${inkelink.core.mq.rocketmq.producer.compress-message-body-threshold:}")
    private int compressMessageBodyThreshold;
    @Value("${inkelink.core.mq.rocketmq.producer.retry-next-server:}")
    private boolean retryNextServer;
    /* @Value("${inkelink.core.mq.rocketmq.producer.retry-next-server:}")
     private boolean retryNextServer;*/
    @Value("${inkelink.core.mq.rocketmq.access-key:}")
    private String accessKey;
    @Value("${inkelink.core.mq.rocketmq.secret-key:}")
    private String secretKey;

    @Bean
    public DefaultMQProducer defaultRocketMQProducer() {
        DefaultMQProducer mqProducer = new DefaultMQProducer(getAclRPCHook());

        mqProducer.setNamesrvAddr(nameServer);
        mqProducer.setProducerGroup(producerGroup);
        mqProducer.setSendMsgTimeout(sendMessageTimeout);
        mqProducer.setMaxMessageSize(maxMessageSize);
        mqProducer.setRetryTimesWhenSendFailed(retryTimesWhenSendFailed);
        mqProducer.setRetryTimesWhenSendAsyncFailed(retryTimesWhenSendAsyncFailed);
        mqProducer.setCompressMsgBodyOverHowmuch(compressMessageBodyThreshold);
        mqProducer.setRetryAnotherBrokerWhenNotStoreOK(retryNextServer);

        return mqProducer;
    }

  /*  @Bean
    public MappingJackson2MessageConverter rocketMQMappingJackson2MessageConverter(ObjectMapper objectMapper) {
        MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();
        messageConverter.setObjectMapper(objectMapper);
        return messageConverter;
    }*/

 /*   @Bean
    @Primary
    public RocketMQMessageConverter createRocketMQMessageConverter() {
        RocketMQMessageConverter converter = new RocketMQMessageConverter();
        CompositeMessageConverter compositeMessageConverter = (CompositeMessageConverter) converter.getMessageConverter();
        List<MessageConverter> messageConverterList = compositeMessageConverter.getConverters();
        for (MessageConverter messageConverter : messageConverterList) {
            if (messageConverter instanceof MappingJackson2MessageConverter) {
                MappingJackson2MessageConverter jackson2MessageConverter = (MappingJackson2MessageConverter) messageConverter;
                ObjectMapper objectMapper = jackson2MessageConverter.getObjectMapper();
                objectMapper.registerModules(new JavaTimeModule());
            }
        }
        return converter;
    }*/

    @Bean
    public RocketMQTemplate rocketMQTemplate(DefaultMQProducer mqProducer, ObjectMapper objectMapper) {
        RocketMQTemplate rocketMQTemplate = new RocketMQTemplate();
       // rocketMQTemplate.setMessageConverter(rocketMQMappingJackson2MessageConverter(objectMapper));
        rocketMQTemplate.setProducer(mqProducer);

        return rocketMQTemplate;
    }

    RPCHook getAclRPCHook() {
        if (StringUtils.hasText(accessKey) && StringUtils.hasText(secretKey)) {
            return new AclClientRPCHook(new SessionCredentials(accessKey, secretKey));
        } else {
            return null;
        }

    }

}
