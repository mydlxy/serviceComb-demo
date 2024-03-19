package com.ca.mfd.prc.pqs.handlers;

import cn.hutool.json.JSONUtil;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.mq.rabbitmq.annotation.MesRabbitListener;
import com.ca.mfd.prc.mq.rabbitmq.entity.PatternEnum;
import com.ca.mfd.prc.mq.rabbitmq.entity.RabbitMQContext;
import com.ca.mfd.prc.mq.rabbitmq.entity.RabbitMQQueueConstants;
import com.ca.mfd.prc.pqs.dto.CreateProcessEntryInfo;
import com.ca.mfd.prc.pqs.dto.EntryReportPartsDto;
import com.ca.mfd.prc.pqs.service.IPqsEntryProcessService;
import com.rabbitmq.client.Channel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 消费抽检报工，生成抽检工单
 *
 * @author edwards.qu
 */
@ConditionalOnExpression("#{'true'.equals(environment.getProperty('inkelink.pqs.mq.entryreportcjsubscribe.enable'))}")
@Component
public class AddEntryReportCJSubscribeHandler {

    @Autowired
    private IPqsEntryProcessService pqsEntryProcessService;
    private static final Logger logger = LoggerFactory.getLogger(AddEntryReportCJSubscribeHandler.class);

    @MesRabbitListener(queues = RabbitMQQueueConstants.ADD_PQS_ENTRY_REPORT_CJ_SUBSCRIBE, pattern = PatternEnum.AutoRetry)
    public void addMessageQueue(Message message, Channel channel) throws IOException {

        String strMessage = StringUtils.toEncodedString(message.getBody(), StandardCharsets.UTF_8);
        logger.info("---------订阅抽检报工，生成抽检工单的消费信息：" + strMessage);
        RabbitMQContext rabbitMQContext = JSONUtil.toBean(strMessage, RabbitMQContext.class);
        if (rabbitMQContext != null) {
            EntryReportPartsDto entryReport = JsonUtils.parseObject(rabbitMQContext.getContent(), EntryReportPartsDto.class);
            // 非抽检工单不处理
            if (entryReport != null && StringUtils.isNotEmpty(entryReport.getQualityCheckType())) {
                CreateProcessEntryInfo info = new CreateProcessEntryInfo();
                BeanUtils.copyProperties(entryReport, info);
                info.setEntryType(entryReport.getQualityCheckType());
                pqsEntryProcessService.createProcessEntry(info);
                pqsEntryProcessService.saveChange();
            }
        }
    }
}
