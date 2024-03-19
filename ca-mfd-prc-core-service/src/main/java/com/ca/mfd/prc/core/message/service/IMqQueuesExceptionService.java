package com.ca.mfd.prc.core.message.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.core.message.entity.MqQueuesExceptionEntity;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * @author jay.he
 * @Description: 通道异常日志
 * @date 2023年09月11日
 */
public interface IMqQueuesExceptionService extends ICrudService<MqQueuesExceptionEntity> {

    /**
     * 自动重试错误队列
     *
     * @param queueNoteId
     */
    void autoRetry(Long queueNoteId) throws JsonProcessingException;

}