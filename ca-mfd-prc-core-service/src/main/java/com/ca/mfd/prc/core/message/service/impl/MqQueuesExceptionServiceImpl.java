package com.ca.mfd.prc.core.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.core.message.entity.MqQueuesExceptionEntity;
import com.ca.mfd.prc.core.message.entity.MqQueuesNotesEntity;
import com.ca.mfd.prc.core.message.handler.RabbitMQHandler;
import com.ca.mfd.prc.core.message.mapper.IMqQueuesExceptionMapper;
import com.ca.mfd.prc.core.message.service.IMqQueuesExceptionService;
import com.ca.mfd.prc.core.message.service.IMqQueuesNotesService;
import com.ca.mfd.prc.mq.rabbitmq.entity.RabbitMQContext;
import com.ca.mfd.prc.mq.rabbitmq.service.IRabbitMqProducerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jay.he
 * @Description: 主题
 * @date 2023年09月11日
 */
@Service
public class MqQueuesExceptionServiceImpl extends AbstractCrudServiceImpl<IMqQueuesExceptionMapper, MqQueuesExceptionEntity> implements IMqQueuesExceptionService {
    private static final Logger logger = LoggerFactory.getLogger(MqQueuesExceptionServiceImpl.class);
    @Autowired
    IMqQueuesNotesService mqQueuesNotesService;
    @Lazy
    @Autowired
    IRabbitMqProducerService producerService;

    /**
     * 自动重试错误队列
     *
     * @param queueNoteId
     */
    @Override
    public void autoRetry(Long queueNoteId) throws JsonProcessingException {
        MqQueuesNotesEntity mqQueuesNotesEntity = mqQueuesNotesService.get(queueNoteId);
        if (mqQueuesNotesEntity == null) {
            logger.info("自动重试错误队列，队列id不存在！" + queueNoteId);
            return;
        }
        QueryWrapper<MqQueuesExceptionEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<MqQueuesExceptionEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(MqQueuesExceptionEntity::getNotesId, queueNoteId);
        lambdaQueryWrapper.orderByDesc(MqQueuesExceptionEntity::getCreationDate);
        List<MqQueuesExceptionEntity> mqQueuesExceptionEntityList = this.getData(queryWrapper, false);
        if (!CollectionUtils.isEmpty(mqQueuesExceptionEntityList)) {
            for (MqQueuesExceptionEntity each : mqQueuesExceptionEntityList) {
                //发送消息到rabbitmq
                RabbitMQContext rabbitMQContext = new RabbitMQContext();
                rabbitMQContext.setNotesId(mqQueuesNotesEntity.getId());
                rabbitMQContext.setGroupName(rabbitMQContext.getGroupName());
                rabbitMQContext.setTopic(mqQueuesNotesEntity.getTopic());
                rabbitMQContext.setContent(each.getContent());
                producerService.send(rabbitMQContext);
            }
            List<Long> ids = mqQueuesExceptionEntityList.stream().map(MqQueuesExceptionEntity::getId).collect(Collectors.toList());
            this.delete(ids.toArray(new Long[0]), true);
        }
    }
}