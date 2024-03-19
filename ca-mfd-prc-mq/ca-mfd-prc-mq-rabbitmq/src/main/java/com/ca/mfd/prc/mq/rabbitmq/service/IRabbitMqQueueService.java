package com.ca.mfd.prc.mq.rabbitmq.service;

import com.ca.mfd.prc.mq.rabbitmq.entity.QueueProperties;

import java.util.List;

public interface IRabbitMqQueueService {

    /**
     * 批量创建队列并绑定交换机
     * @param queuePropertiesList
     */
    void createRabbitMqQueue(List<QueueProperties> queuePropertiesList);

    /**
     * 获取队列的消息数
     * @param queueName
     * @return
     */
    Integer getQueueMessageCount(String queueName);

    /**
     * 删除队列
     * @param queueName
     * @return
     */
    boolean delQueue(String queueName);

}
