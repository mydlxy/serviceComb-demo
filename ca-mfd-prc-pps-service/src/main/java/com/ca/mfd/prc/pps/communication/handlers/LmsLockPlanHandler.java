package com.ca.mfd.prc.pps.communication.handlers;

import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.mq.rabbitmq.annotation.MesRabbitListener;
import com.ca.mfd.prc.mq.rabbitmq.entity.PatternEnum;
import com.ca.mfd.prc.mq.rabbitmq.entity.RabbitMQContext;
import com.ca.mfd.prc.mq.rabbitmq.entity.RabbitMQQueueConstants;
import com.ca.mfd.prc.pps.communication.dto.LmsLockPlanDto;
import com.ca.mfd.prc.pps.communication.service.IMidApiLogService;
import com.ca.mfd.prc.pps.communication.service.IPartPlanService;
import com.ca.mfd.prc.pps.dto.LmsPartPlanDTO;
import com.rabbitmq.client.Channel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * @author inkelink
 * @Description: LMS锁定整车计划
 * @date 2023年10月28日
 * @变更说明 BY inkelink At 2023年10月28日
 */
@ConditionalOnExpression("#{'true'.equals(environment.getProperty('inkelink.pps.mq.lmslockplan.enable'))}")
@Component
public class LmsLockPlanHandler {
    private static final Logger logger = LoggerFactory.getLogger(LmsLockPlanHandler.class);

    @Autowired
    private IMidApiLogService midApiLogService;

    @Autowired
    private IPartPlanService partPlanService;

    @MesRabbitListener(queues = RabbitMQQueueConstants.ADD_LMS_LOCKPLAN_QUEUE, pattern = PatternEnum.AutoRetry)
    public void addMessageQueue(Message message, Channel channel) throws IOException {
        String strMessage = StringUtils.toEncodedString(message.getBody(), StandardCharsets.UTF_8);
        logger.info("LMS锁定整车计划addMessageQueue消费信息：" + strMessage);
        RabbitMQContext rabbitMQContext = JsonUtils.parseObject(strMessage, RabbitMQContext.class);
        if (rabbitMQContext != null) {
            try {
                LmsLockPlanDto model = JsonUtils.parseObject(rabbitMQContext.getContent(), LmsLockPlanDto.class);
                List<LmsLockPlanDto> datas = Arrays.asList(model);
                midApiLogService.sendLmsLockPlan(datas);
            } catch (Exception e) {
                logger.error("LMS锁定整车计划addMessageQueue消费信息失败", e);
                throw e;
            }
        }
    }
}
