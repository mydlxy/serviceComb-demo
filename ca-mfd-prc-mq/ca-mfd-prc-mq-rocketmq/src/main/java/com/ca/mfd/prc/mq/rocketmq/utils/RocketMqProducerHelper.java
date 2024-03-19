package com.ca.mfd.prc.mq.rocketmq.utils;

import cn.hutool.json.JSONUtil;
import com.ca.mfd.prc.mq.rocketmq.entity.RocketMQMessageContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 发送消息
 */
//@Slf4j
@Component
@ConditionalOnExpression("#{'true'.equals(environment.getProperty('inkelink.core.mq.rocketmq.enable'))}")
public class RocketMqProducerHelper {
    private static final Logger logger = LoggerFactory.getLogger(RocketMqProducerHelper.class);
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    private void validRocketMQContext(RocketMQMessageContext rocketMQContext) {
        if (rocketMQContext == null) {
            throw new IllegalArgumentException("消息队列对象不能为空！");
        }
        if (!StringUtils.hasText(rocketMQContext.getTopic())) {
            throw new IllegalArgumentException("消息队列对象Topic不能为空！");
        }
        if (!StringUtils.hasText(rocketMQContext.getContent())) {
            throw new IllegalArgumentException("消息队列内容不能为空！");
        }
    }

    /**
     * 同步发送可靠消息
     *
     * @param rocketMQContext
     */
    public void convertAndSend(RocketMQMessageContext rocketMQContext) {
        validRocketMQContext(rocketMQContext);
        // rocketMQTemplate.syncSend(rocketMQContext.getSendTopic(), rocketMQContext.getContent());
        //  rocketMQTemplate.convertAndSend(rocketMQContext.getSendTopic(), JSONUtil.toJsonStr(rocketMQContext.getContent()));
        rocketMQTemplate.convertAndSend(rocketMQContext.getSendTopic(), rocketMQContext.getContent());
        logger.info("发送消息：" + JSONUtil.toJsonStr(rocketMQContext.getContent()));
    }

    /**
     * 异步发送可靠消息
     */
    public void asyncSend(RocketMQMessageContext rocketMQContext) {
        validRocketMQContext(rocketMQContext);

        rocketMQTemplate.asyncSend(rocketMQContext.getSendTopic(), rocketMQContext.getContent(), new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                // 成功回调
                logger.info("asyncSend回调结果: " + sendResult);
            }

            @Override
            public void onException(Throwable throwable) {
                // 异常回调
                logger.info("asyncSend回调异常：" + throwable);
            }
        });
    }

    /**
     * 发送单向消息
     *
     * @param rocketMQContext
     */
    public void sendOneWay(RocketMQMessageContext rocketMQContext) {
        validRocketMQContext(rocketMQContext);
        rocketMQTemplate.sendOneWay(rocketMQContext.getSendTopic(), rocketMQContext.getContent());
    }

    /**
     * 同步发送顺序消息
     *
     * @param rocketMQContext
     */
    public void syncSendOrderly(RocketMQMessageContext rocketMQContext) {
        validRocketMQContext(rocketMQContext);
        rocketMQTemplate.syncSendOrderly(rocketMQContext.getSendTopic(), rocketMQContext.getContent(), rocketMQContext.getOrderlyHashKey());
    }

    /**
     * 异步发送顺序消息
     */
    public void asyncSendOrderly(RocketMQMessageContext rocketMQContext) {
        validRocketMQContext(rocketMQContext);

        rocketMQTemplate.asyncSendOrderly(rocketMQContext.getSendTopic(), rocketMQContext.getContent(), rocketMQContext.getOrderlyHashKey()
                , new SendCallback() {
                    @Override
                    public void onSuccess(SendResult sendResult) {
                        // 成功回调
                        logger.info("asyncSend回调结果: " + sendResult);
                    }

                    @Override
                    public void onException(Throwable throwable) {
                        // 异常回调
                        logger.info("asyncSend回调异常：" + throwable);
                    }
                });
    }

    /**
     * 发送单向顺序消息
     *
     * @param rocketMQContext
     */
    public void sendOneWayOrderly(RocketMQMessageContext rocketMQContext) {
        validRocketMQContext(rocketMQContext);
        rocketMQTemplate.sendOneWayOrderly(rocketMQContext.getSendTopic(), rocketMQContext.getContent(), rocketMQContext.getOrderlyHashKey());
    }


    /**
     * 发送迟延消息
     *
     * @param rocketMQContext
     */
    public void syncSendDelay(RocketMQMessageContext rocketMQContext) {
        validRocketMQContext(rocketMQContext);
        GenericMessage message = new GenericMessage(rocketMQContext.getContent());
       /* if (rocketMQContext.getTimeout() == 0l) {
            rocketMQContext.setTimeout(5 * 1000);
        }*/
        rocketMQTemplate.syncSend(rocketMQContext.getSendTopic(), message, rocketMQContext.getTimeout(), rocketMQContext.getDelayLevel());

    }


}

