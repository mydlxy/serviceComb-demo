package com.ca.mfd.prc.mq.kafka.utils;

import cn.hutool.json.JSONUtil;
import com.ca.mfd.prc.mq.kafka.entity.KafkaMessageContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 发送消息
 */
@Component
@ConditionalOnExpression("#{'true'.equals(environment.getProperty('inkelink.core.mq.kafka.enable'))}")
public class KafkaProducerHelper {

    /*@Autowired
    private KafkaTemplate<String, KafkaMessageContext> kafkaTemplate;*/
/*    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;*/

    @Autowired
    private KafkaTemplate<String, String> stringKafkaTemplate;

    private void validKafkaMessageContext(KafkaMessageContext kafkaMessageContext) {
        if (kafkaMessageContext == null) {
            throw new IllegalArgumentException("消息队列对象不能为空！");
        }
        if (!StringUtils.hasText(kafkaMessageContext.getTopic())) {
            throw new IllegalArgumentException("消息队列对象Topic不能为空！");
        }
        if (kafkaMessageContext.getContent() == null || !StringUtils.hasText(kafkaMessageContext.getContent())) {
            throw new IllegalArgumentException("消息队列对象Content不能为空！");
        }
    }

    /**
     * 发送字符消息
     *
     * @param context
     */
    public void convertAndSend(KafkaMessageContext context) {
        validKafkaMessageContext(context);
        stringKafkaTemplate.send(context.getTopic(), context.getKey(), JSONUtil.toJsonStr(context));
    }

}

