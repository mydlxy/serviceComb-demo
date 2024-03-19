package com.ca.mfd.prc.mq.rabbitmq.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.ca.mfd.prc.mq.rabbitmq.entity.QueueProperties;
import com.ca.mfd.prc.mq.rabbitmq.service.IRabbitMqQueueService;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.QueueInformation;
import org.springframework.amqp.rabbit.core.ChannelCallback;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.util.List;

/**
 * RabbitMQ 全局配置，SpringBoot 启动后会回调此类
 */
@ConditionalOnExpression("#{'true'.equals(environment.getProperty('inkelink.core.mq.rabbitmq.enable'))}")
@Component
public class RabbitMqQueueServiceImpl implements IRabbitMqQueueService {
    private static final Logger logger = LoggerFactory.getLogger(RabbitMqQueueServiceImpl.class);
    /**
     * MQ操作管理器
     */
    @Autowired
    private RabbitAdmin amqpAdmin;

    @Resource(name = "mfdExchange")
    DirectExchange mfdExchange;

    /**
     * 批量创建队列并绑定交换机
     *
     * @param queuePropertiesList
     */
    @Override
    public void createRabbitMqQueue(List<QueueProperties> queuePropertiesList) {
        StopWatch stopWatch = new StopWatch("MQ");
        stopWatch.start();

        /*StopWatch stopWatch = StopWatch.create("MQ");
        stopWatch.start();*/
        logger.info("初始化MQ配置");
        List<QueueProperties> modules = queuePropertiesList;
        if (CollUtil.isEmpty(modules)) {
            logger.info("未配置MQ");
            return;
        }
        for (QueueProperties module : modules) {
            try {
                Queue queue = genQueue(module.getChannelName());
                Exchange exchange = mfdExchange;//genQueueExchange(module);
                queueBindExchange(queue, exchange, module);
                //   bindProducer(module);
                //  bindConsumer(queue, exchange, module);
            } catch (Exception e) {
                logger.error("初始化失败", e);
            }
        }
        stopWatch.stop();
        logger.info("初始化MQ配置成功耗时: {}ms", stopWatch.getTotalTimeSeconds());
    }

    /**
     * 队列绑定交换机
     *
     * @param queue
     * @param exchange
     * @param module
     */
    private void queueBindExchange(Queue queue, Exchange exchange, QueueProperties module) {
        Binding binding = new Binding(queue.getName(),
                Binding.DestinationType.QUEUE,
                exchange.getName(),
                module.getTopic(),
                null);

        amqpAdmin.declareQueue(queue);
        amqpAdmin.declareExchange(exchange);
        amqpAdmin.declareBinding(binding);

        //     BindingBuilder.bind(queue).to(exchange).with(module.getTopic());
    }


    /**
     * 创建队列
     *
     * @param queueName
     * @return
     */
    private Queue genQueue(String queueName) {
        Queue queue = QueueBuilder
                .durable(queueName)//排外、自动删除默认是false、
                .build();
        return queue;
    }

    /**
     * 获取队列的消息数
     *
     * @param queueName
     * @return
     */
    @Override
    public Integer getQueueMessageCount(String queueName) {
        QueueInformation queueInformation = amqpAdmin.getQueueInfo(queueName);
        if (queueInformation != null) {
            AMQP.Queue.DeclareOk declareOk = amqpAdmin.getRabbitTemplate().execute(new ChannelCallback<AMQP.Queue.DeclareOk>() {
                public AMQP.Queue.DeclareOk doInRabbit(Channel channel) throws Exception {
                    return channel.queueDeclarePassive(queueName);
                }
            });
            return declareOk.getMessageCount();
        } else {
            return 0;
        }

    }

    /**
     * 删除队列
     *
     * @param queueName
     * @return
     */
    public boolean delQueue(String queueName) {
        QueueInformation queueInformation = amqpAdmin.getQueueInfo(queueName);
        if (queueInformation != null) {
            boolean result = amqpAdmin.deleteQueue(queueName);
            return result;
        }
        return false;
    }

}

