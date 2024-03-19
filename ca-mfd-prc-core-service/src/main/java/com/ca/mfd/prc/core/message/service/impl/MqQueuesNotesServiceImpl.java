package com.ca.mfd.prc.core.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ca.mfd.prc.common.dto.IdsModel;
import com.ca.mfd.prc.common.enums.ConditionDirection;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.model.base.dto.DataDto;
import com.ca.mfd.prc.common.model.base.dto.SortDto;
import com.ca.mfd.prc.common.redis.RedisUtils;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.core.message.entity.MqQueuesNotesEntity;
import com.ca.mfd.prc.core.message.mapper.IMqQueuesNotesMapper;
import com.ca.mfd.prc.core.message.service.IMqQueuesNotesService;
import com.ca.mfd.prc.core.message.service.IMqQueuesTopicService;
import com.ca.mfd.prc.mq.rabbitmq.entity.QueueProperties;
import com.ca.mfd.prc.mq.rabbitmq.entity.RabbitMQContext;
import com.ca.mfd.prc.mq.rabbitmq.remote.app.core.entity.SysQueueNoteEntity;
import com.ca.mfd.prc.mq.rabbitmq.remote.app.core.provider.RabbitMqSysQueueNoteProvider;
import com.ca.mfd.prc.mq.rabbitmq.service.IRabbitMqProducerService;
import com.ca.mfd.prc.mq.rabbitmq.service.IRabbitMqQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author jay.he
 * @Description: 主题
 * @date 2023年09月11日
 */
@Service
public class MqQueuesNotesServiceImpl extends AbstractCrudServiceImpl<IMqQueuesNotesMapper, MqQueuesNotesEntity> implements IMqQueuesNotesService {

    public final static String REDIS_KEY_MQ_CREATE_QUEUE = "CA:Created:Queue:%s";
    @Autowired
    IMqQueuesTopicService mqQueuesTopicService;
    @Lazy
    @Autowired
    IRabbitMqQueueService rabbitMqQueueService;
    @Autowired
    RabbitMqSysQueueNoteProvider sysQueueNoteService;
    @Lazy
    @Autowired
    IRabbitMqProducerService producerService;
    @Autowired
    RedisUtils redisUtils;

    /* *//**
     * 删除totalMinute分钟以前的队列的异常数据
     * @param queueNoteId
     * @param totalMinute
     *//*
    @Override
    public void removeNoUseQueue(Long queueNoteId, Integer totalMinute) {

    }*/

    /**
     * 轮询队列 将数据库表中待发送的MQ消息，发送到消息队列中
     *
     * @param groupName
     */
    @Override
    public void pollQueue(String groupName) {
        DataDto sysQueueNoteDataDto = new DataDto();
        sysQueueNoteDataDto.getConditions().add(new ConditionDto("GROUP_NAME", groupName, ConditionOper.Equal));
        sysQueueNoteDataDto.getSorts().add(new SortDto("CREATION_DATE", ConditionDirection.DESC));

        List<SysQueueNoteEntity> sysQueueNoteEntityList = sysQueueNoteService.getdata(sysQueueNoteDataDto);
        if (!CollectionUtils.isEmpty(sysQueueNoteEntityList)) {
            //发送rabbitmq消息队列
            QueryWrapper<MqQueuesNotesEntity> queuesNotesEntityQueryWrapper = new QueryWrapper<>();
            LambdaQueryWrapper<MqQueuesNotesEntity> lambdaQueuesNotesEntityQueryWrapper = queuesNotesEntityQueryWrapper.lambda();
            lambdaQueuesNotesEntityQueryWrapper.eq(MqQueuesNotesEntity::getGroupName, groupName);
            lambdaQueuesNotesEntityQueryWrapper.eq(MqQueuesNotesEntity::getStatus, 1);
            List<MqQueuesNotesEntity> mqQueuesNotesEntityList = this.getData(queuesNotesEntityQueryWrapper, false);
            if (!CollectionUtils.isEmpty(mqQueuesNotesEntityList)) {
                for (SysQueueNoteEntity eachSysQueueNoteEntity : sysQueueNoteEntityList) {
                    for (MqQueuesNotesEntity eachMqQueuesNotesEntity : mqQueuesNotesEntityList) {
                        RabbitMQContext rabbitMQContext = new RabbitMQContext();
                        rabbitMQContext.setNotesId(eachMqQueuesNotesEntity.getId());
                        rabbitMQContext.setGroupName(eachMqQueuesNotesEntity.getGroupName());
                        rabbitMQContext.setTopic(eachMqQueuesNotesEntity.getTopic());
                        rabbitMQContext.setContent(eachSysQueueNoteEntity.getContent());
                        producerService.send(rabbitMQContext);
                    }
                }
            }
            //删除sysQueueNoteService的记录
            IdsModel idsModel = new IdsModel();
            List<Long> ids = sysQueueNoteEntityList.stream().map(SysQueueNoteEntity::getId).collect(Collectors.toList());
            idsModel.setIds(new String[ids.size()]);
            ids.stream().map(Object::toString).collect(Collectors.toList()).toArray(idsModel.getIds());

            sysQueueNoteService.delete(idsModel);
        }

    }

    /**
     * 创建订阅（创建消息队列并绑定交换器）
     */
    @Override
    public void createSubscribe() {
        QueryWrapper<MqQueuesNotesEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<MqQueuesNotesEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(MqQueuesNotesEntity::getStatus, 1);
        List<MqQueuesNotesEntity> queuesNotesEntities = this.getData(queryWrapper, false);
        if (!CollectionUtils.isEmpty(queuesNotesEntities)) {
            Map<String, List<MqQueuesNotesEntity>> inputDataGroup = queuesNotesEntities.stream().collect(Collectors.groupingBy(c -> c.getGroupName()));
            Iterator<Map.Entry<String, List<MqQueuesNotesEntity>>> iteratorShopCalendar = inputDataGroup.entrySet().iterator();
            while (iteratorShopCalendar.hasNext()) {
                Map.Entry<String, List<MqQueuesNotesEntity>> entry = iteratorShopCalendar.next();
                String key = entry.getKey();//groupName
                List<MqQueuesNotesEntity> value = entry.getValue();//某一个主题下面的所有队列
                String redisKey = String.format(REDIS_KEY_MQ_CREATE_QUEUE, key);
                //   Map<String, Object> redisMap = redisUtils.hGetAll(redisKey);//当前主题对应的redis中的队列
                if (!CollectionUtils.isEmpty(value)) {
                    List<QueueProperties> queuePropertiesList = new ArrayList<>();
                    List<MqQueuesNotesEntity> preCreateQueueList = new ArrayList<>();
                    for (MqQueuesNotesEntity each : value) {
                        Object obj = redisUtils.hGet(redisKey, String.valueOf(each.getId()));
                        if (obj == null) {
                            //如果redis中没有，说明该队列还没创建
                            QueueProperties queueProperties = new QueueProperties();
                            queueProperties.setChannelName(each.getChannelName());
                            queueProperties.setTopic(each.getTopic());
                            queuePropertiesList.add(queueProperties);
                            preCreateQueueList.add(each);
                        }
                    }
                    if (!CollectionUtils.isEmpty(queuePropertiesList)) {
                        rabbitMqQueueService.createRabbitMqQueue(queuePropertiesList);
                        //将创建的信息存储到redis
                        for (MqQueuesNotesEntity each : preCreateQueueList) {
                            redisUtils.hSet(redisKey, String.valueOf(each.getId()), each.getChannelName());
                            //   redisMap.put(String.valueOf(each.getId()), each.getChannelName());
                        }
                        //  redisUtils.hmSet(redisKey, redisMap);
                    }
                }
            }
        }
    }

    /**
     * 更新队列消息数量
     */
    @Override
    public void updateQueueMessageCount() {
        QueryWrapper<MqQueuesNotesEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<MqQueuesNotesEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(MqQueuesNotesEntity::getStatus, 1);
        List<MqQueuesNotesEntity> queuesNotesEntities = this.getData(queryWrapper, false);
        if (!CollectionUtils.isEmpty(queuesNotesEntities)) {
            for (MqQueuesNotesEntity entity : queuesNotesEntities) {
                Integer count = rabbitMqQueueService.getQueueMessageCount(entity.getChannelName());
                LambdaUpdateWrapper<MqQueuesNotesEntity> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.set(MqQueuesNotesEntity::getNumber, count);
                updateWrapper.eq(MqQueuesNotesEntity::getId, entity.getId());
                update(updateWrapper);
            }
            saveChange();
        }
    }

    /**
     * 获得队列消息数量
     *
     * @param queueName
     * @return
     */
    @Override
    public Integer getQueueMessageCount(String queueName) {
        Integer count = rabbitMqQueueService.getQueueMessageCount(queueName);
        return count;
    }

}