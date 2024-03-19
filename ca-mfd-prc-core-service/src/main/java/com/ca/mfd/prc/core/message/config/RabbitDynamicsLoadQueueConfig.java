package com.ca.mfd.prc.core.message.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.core.message.entity.MqQueuesNotesEntity;
import com.ca.mfd.prc.core.message.service.IMqQueuesNotesService;
import com.ca.mfd.prc.mq.rabbitmq.entity.QueueProperties;
import com.ca.mfd.prc.mq.rabbitmq.entity.RabbitMQQueueConstants;
import com.ca.mfd.prc.mq.rabbitmq.service.IRabbitMqQueueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * RabbitMQ 全局配置，SpringBoot 启动后会回调此类
 */
@Component
@ConditionalOnExpression("#{'true'.equals(environment.getProperty('inkelink.core.mq.rabbitmq.enable'))}")
public class RabbitDynamicsLoadQueueConfig implements SmartInitializingSingleton {
    private static final Logger logger = LoggerFactory.getLogger(RabbitDynamicsLoadQueueConfig.class);

    @Autowired
    IMqQueuesNotesService mqQueuesNotesService;
    @Autowired
    IRabbitMqQueueService rabbitMqQueueService;

    public List<QueueProperties> getQueueByDb() {
        List<QueueProperties> queuePropertiesList = new ArrayList<>();
        QueryWrapper<MqQueuesNotesEntity> queryWrapper = new QueryWrapper<>();
        //  queryWrapper.eq("STATUS", 1);
        queryWrapper.ge("CREATION_DATE", "2023-09-01");//todo 测试用
        List<MqQueuesNotesEntity> mqQueuesNotesEntities = mqQueuesNotesService.getData(queryWrapper, false);
        if (!CollectionUtils.isEmpty(mqQueuesNotesEntities)) {
            for (MqQueuesNotesEntity entity : mqQueuesNotesEntities) {
                QueueProperties queueProperties = new QueueProperties();
                queueProperties.setTopic(entity.getTopic());
                queueProperties.setChannelName(entity.getChannelName());
                queuePropertiesList.add(queueProperties);
            }
        }
        return queuePropertiesList;
    }

    @Override
    public void afterSingletonsInstantiated() {
        rabbitMqQueueService.createRabbitMqQueue(getQueueByConstants());
        rabbitMqQueueService.createRabbitMqQueue(getQueueByDb());
    }

    private List<QueueProperties> getQueueByConstants() {
        List<QueueProperties> queuePropertiesList = new ArrayList<>();
        Class rabbitMQQueueConstantsClass = RabbitMQQueueConstants.class;
        //获取所有的public修饰的成员变量
        Field[] fields = rabbitMQQueueConstantsClass.getFields();
        if (fields != null && fields.length > 0) {
            for (Field eachField : fields) {
                // eachField.setAccessible(true);
                if (eachField.getType().toString().endsWith("java.lang.String")
                        && Modifier.isStatic(eachField.getModifiers())
                        && Modifier.isFinal(eachField.getModifiers())
                        && Modifier.isPublic(eachField.getModifiers())) {
                    try {
                        Object value = eachField.get(RabbitMQQueueConstants.class);
                        if (value != null && StringUtils.hasText(value.toString())) {
                            QueueProperties queueProperties = new QueueProperties();
                            queueProperties.setChannelName(value.toString());
                            queueProperties.setTopic(value + ".Handler");
                            queuePropertiesList.add(queueProperties);
                        }
                    } catch (IllegalAccessException e) {
                        logger.error(e.getMessage());
                    }
                }
            }
        }
        return queuePropertiesList;

    }
}

