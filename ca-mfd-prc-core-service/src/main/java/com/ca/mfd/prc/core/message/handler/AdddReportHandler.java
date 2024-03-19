package com.ca.mfd.prc.core.message.handler;

import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.core.report.dto.AddReportQueueDTO;
import com.ca.mfd.prc.core.report.dto.ReportQueueDTO;
import com.ca.mfd.prc.core.report.service.IRptSendService;
import com.ca.mfd.prc.mq.rabbitmq.annotation.MesRabbitListener;
import com.ca.mfd.prc.mq.rabbitmq.entity.PatternEnum;
import com.ca.mfd.prc.mq.rabbitmq.entity.RabbitMQContext;
import com.ca.mfd.prc.mq.rabbitmq.entity.RabbitMQQueueConstants;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 *
 * @Description: AdddReportHandler
 * @author inkelink
 * @date 2024年03月11日
 */
@ConditionalOnExpression("#{'true'.equals(environment.getProperty('inkelink.core.mq.rabbitmq.enable'))}")
@Component
public class AdddReportHandler {
    private static final Logger logger = LoggerFactory.getLogger(AdddReportHandler.class);
    @Autowired
    IRptSendService iRptSendService;

    @MesRabbitListener(queues = RabbitMQQueueConstants.ADD_REPORT_QUEUE, pattern = PatternEnum.AutoRetry)
    public void addReportQueue(Message message, Channel channel) throws IOException {
        String strMessage = org.apache.commons.lang3.StringUtils.toEncodedString(message.getBody(), StandardCharsets.UTF_8);
        logger.info("addReportQueue消费信息：" + strMessage);
        RabbitMQContext rabbitMQContext = JsonUtils.parseObject(strMessage, RabbitMQContext.class);
        if (rabbitMQContext != null) {
            AddReportQueueDTO reportQueueDTO = JsonUtils.parseObject(rabbitMQContext.getContent(), AddReportQueueDTO.class);
            if (reportQueueDTO != null) {
                try {
                    ReportQueueDTO dto=new ReportQueueDTO();
                    BeanUtils.copyProperties(reportQueueDTO,dto);
                    dto.setPrintDt(new Date(reportQueueDTO.getPrintDt()));
                    iRptSendService.addReportQueue(dto);
                } catch (Exception ex) {
                    ex.getMessage();
                }

            }
        }

    }

}
