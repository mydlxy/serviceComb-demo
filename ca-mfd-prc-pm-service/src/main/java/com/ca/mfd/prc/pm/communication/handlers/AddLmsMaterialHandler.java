package com.ca.mfd.prc.pm.communication.handlers;

import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.common.utils.SpringContextUtils;
import com.ca.mfd.prc.mq.rabbitmq.annotation.MesRabbitListener;
import com.ca.mfd.prc.mq.rabbitmq.entity.PatternEnum;
import com.ca.mfd.prc.mq.rabbitmq.entity.RabbitMQContext;
import com.ca.mfd.prc.mq.rabbitmq.entity.RabbitMQQueueConstants;
import com.ca.mfd.prc.pm.communication.dto.MidLmsSigtrueVo;
import com.ca.mfd.prc.pm.service.IPmWorkstationMaterialSubService;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@ConditionalOnExpression("#{'true'.equals(environment.getProperty('inkelink.core.mq.rabbitmq.enable'))}")
@Component
public class AddLmsMaterialHandler {
    private static final Logger logger = LoggerFactory.getLogger(AddLmsMaterialHandler.class);

    @MesRabbitListener(queues = RabbitMQQueueConstants.ADD_LMSMATERIAL_QUEUE, pattern = PatternEnum.AutoRetry)
    public void addMessageQueue(Message message, Channel channel) throws IOException {
        String strMessage = org.apache.commons.lang3.StringUtils.toEncodedString(message.getBody(), StandardCharsets.UTF_8);
        logger.info("addMessageQueue消费信息：" + strMessage);
        RabbitMQContext rabbitMQContext = JsonUtils.parseObject(strMessage, RabbitMQContext.class);
        if (rabbitMQContext != null) {
            List<MidLmsSigtrueVo> model = JsonUtils.parseArray(rabbitMQContext.getContent(), MidLmsSigtrueVo.class);
            IPmWorkstationMaterialSubService service = SpringContextUtils.getBean(IPmWorkstationMaterialSubService.class);
            for (MidLmsSigtrueVo items : model) {
                service.getPmWorkstationMaterial(items.getSigtrue());
            }
        }
    }
}
