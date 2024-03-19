/*
package com.ca.mfd.prc.mq.kafka.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class KafkaConfig {

    */
/**
     * 定义一个KafkaAdmin的bean，可以自动检测集群中是否存在topic，不存在则创建
     * @return
     *//*

    @Bean
    public KafkaAdmin kafkaAdmin(){
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG,"10.23.1.152:9092");

        return new KafkaAdmin(configs);
    }


}
*/
