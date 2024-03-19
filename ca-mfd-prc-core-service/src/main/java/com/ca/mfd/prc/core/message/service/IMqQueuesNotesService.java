package com.ca.mfd.prc.core.message.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.core.message.entity.MqQueuesNotesEntity;

/**
 * @author jay.he
 * @Description: 通道
 * @date 2023年09月11日
 */
public interface IMqQueuesNotesService extends ICrudService<MqQueuesNotesEntity> {


    /* *//**
     * 删除totalMinute分钟以前的队列
     * @param queueNoteId
     * @param totalMinute
     *//*
    void removeNoUseQueue(Long queueNoteId,Integer totalMinute);*/

    /**
     * 轮询队列 将数据库表中待发送的MQ消息，发送到消息队列中
     *
     * @param groupName
     */
    void pollQueue(String groupName);

    /**
     * 创建订阅（创建消息队列并绑定交换器）
     */
    void createSubscribe();

    /**
     * 更新队列消息数量
     */
    void updateQueueMessageCount();

    /**
     * 获得队列消息数量
     */
    Integer getQueueMessageCount(String queueName);

}