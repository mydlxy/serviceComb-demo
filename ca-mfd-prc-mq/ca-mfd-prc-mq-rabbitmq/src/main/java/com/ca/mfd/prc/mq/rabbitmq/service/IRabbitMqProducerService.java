package com.ca.mfd.prc.mq.rabbitmq.service;


import com.ca.mfd.prc.mq.rabbitmq.entity.RabbitMQContext;

public interface IRabbitMqProducerService {

    /**
     * 发送消息
     *
     * @param message
     */
    void send(RabbitMQContext message);

}
