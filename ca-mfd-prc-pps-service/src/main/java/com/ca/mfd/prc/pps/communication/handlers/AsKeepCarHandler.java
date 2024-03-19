package com.ca.mfd.prc.pps.communication.handlers;

import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.common.utils.SpringContextUtils;
import com.ca.mfd.prc.mq.rabbitmq.annotation.MesRabbitListener;
import com.ca.mfd.prc.mq.rabbitmq.entity.PatternEnum;
import com.ca.mfd.prc.mq.rabbitmq.entity.RabbitMQContext;
import com.ca.mfd.prc.mq.rabbitmq.entity.RabbitMQQueueConstants;
import com.ca.mfd.prc.pps.communication.dto.AsKeepCarDto;
import com.ca.mfd.prc.pps.communication.service.IMidApiLogService;
import com.rabbitmq.client.Channel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author inkelink
 * @Description: AS保留车
 * @date 2023年10月09日
 * @变更说明 BY inkelink At 2023年10月09日
 */
@ConditionalOnExpression("#{'true'.equals(environment.getProperty('inkelink.pps.mq.askeepcar.enable'))}")
@Component
public class AsKeepCarHandler {
    private static final Logger logger = LoggerFactory.getLogger(AsKeepCarHandler.class);

    @MesRabbitListener(queues = RabbitMQQueueConstants.ADD_AS_KEEPCAR_QUEUE, pattern = PatternEnum.AutoRetry)
    public void addMessageQueue(Message message, Channel channel) throws IOException {
        String strMessage = StringUtils.toEncodedString(message.getBody(), StandardCharsets.UTF_8);
        logger.info("AS订单报废addMessageQueue消费信息：" + strMessage);
        RabbitMQContext rabbitMQContext = JsonUtils.parseObject(strMessage, RabbitMQContext.class);
        if (rabbitMQContext != null) {
            try {//TODO 正式会删除 try..catch
                List<AsKeepCarDto> datas = JsonUtils.parseArray(rabbitMQContext.getContent(), AsKeepCarDto.class);
                IMidApiLogService midApiLogService = SpringContextUtils.getBean(IMidApiLogService.class);
                midApiLogService.sendKeepCar(datas);
                //midApiLogService.saveChange();
            } catch (Exception e) {
                logger.error("AS订单报废addMessageQueue消费信息失败", e);
            }
        }
    }
}
