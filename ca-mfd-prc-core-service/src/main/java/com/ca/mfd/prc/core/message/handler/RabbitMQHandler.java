package com.ca.mfd.prc.core.message.handler;

import com.ca.mfd.prc.common.model.main.ReportQueue;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.mq.rabbitmq.annotation.MesRabbitListener;
import com.ca.mfd.prc.mq.rabbitmq.entity.PatternEnum;
import com.ca.mfd.prc.mq.rabbitmq.entity.RabbitMQContext;
import com.ca.mfd.prc.mq.rabbitmq.entity.RabbitMQQueueConstants;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 *
 * @Description: RabbitMQHandler
 * @author inkelink
 * @date 2023年10月09日
 * @变更说明 BY inkelink At 2023年10月09日
 */
@ConditionalOnExpression("#{'true'.equals(environment.getProperty('inkelink.core.mq.rabbitmq.enable'))}")
@Component
public class RabbitMQHandler {
    private static final Logger logger = LoggerFactory.getLogger(RabbitMQHandler.class);
/*    @Autowired
    IMsgSendService msgSendService;
    @Autowired
    IMsgPushService msgPushService;
    @Autowired
    IMsgEmailConfigService msgEmailConfigService;
    @Autowired
    MailChannel mailChannel;*/

    @MesRabbitListener(queues = RabbitMQQueueConstants.TEST1_QUEUE, pattern = PatternEnum.AutoRetry)
    public void test1Queue(Message message, Channel channel) throws IOException {
        //   new String(message.getBody())
        String strMessage = org.apache.commons.lang3.StringUtils.toEncodedString(message.getBody(), StandardCharsets.UTF_8);
        int a = 1;
        int b = 0;
        // int c = a / b;
        String str = "";
    }

    @MesRabbitListener(queues = RabbitMQQueueConstants.TEST2_QUEUE, pattern = PatternEnum.General)
    public void test2Queue(Message message, Channel channel) throws IOException {
        //   new String(message.getBody())

        String str = "";
    }

    @MesRabbitListener(queues = RabbitMQQueueConstants.TEST3_QUEUE, pattern = PatternEnum.General)
    public void test3Queue(Message message, Channel channel) throws IOException {
        //   new String(message.getBody())
        String str = "";
    }

    @MesRabbitListener(queues = RabbitMQQueueConstants.TEST4_QUEUE, pattern = PatternEnum.AutoRetry)
    public void test4Queue(Message message, Channel channel) throws IOException {
        //   new String(message.getBody())
        String str = "";
    }

    /**
     * 打印模板处理示例
     *
     * @param message
     * @param channel
     * @throws IOException
     */
    //  @MesRabbitListener(queues = RabbitMQQueueConstants.ADD_REPORT_QUEUE, pattern = PatternEnum.AutoRetry)
    public void addReportQueue(Message message, Channel channel) throws IOException {
     //   String strMessage = Base64.getEncoder().encodeToString(message.getBody());;]
        String strMessage = org.apache.commons.lang3.StringUtils.toEncodedString(message.getBody(), StandardCharsets.UTF_8);
        logger.info("addReportQueue消费信息：" + strMessage);
        RabbitMQContext rabbitMQContext = JsonUtils.parseObject(strMessage, RabbitMQContext.class);
       // RabbitMQContext rabbitMQContext = JSONUtil.toBean(strMessage, RabbitMQContext.class);
        if (rabbitMQContext != null) {
            ReportQueue reportQueue = JsonUtils.parseObject(rabbitMQContext.getContent(), ReportQueue.class);
            if (reportQueue != null) {
                try {
                    //  todo
                } catch (Exception ex) {
                    ex.getMessage();
                }

            }
        }

    }

}
