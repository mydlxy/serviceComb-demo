package com.ca.mfd.prc.pm.communication.mq;

import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.pm.communication.dto.MidAsLineCalendarReq;
import com.ca.mfd.prc.pm.communication.entity.MidApiLogEntity;
import com.ca.mfd.prc.pm.communication.service.IMidAsLineCalendarService;
import com.ca.mfd.prc.pm.communication.service.IMidAsShiftService;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author inkelink
 * @Description: AS班次信息
 * @date 2023年10月09日
 * @变更说明 BY inkelink At 2023年10月09日
 */
@Component
@ConditionalOnExpression("#{'true'.equals(environment.getProperty('inkelink.pm.mq.asmq.enable'))}")
@RocketMQMessageListener(topic = "${inkelink.pm.mq.asmq.consumer-shc.topic:}"
        , consumerGroup = "${inkelink.pm.mq.asmq.consumer-shc.group:}")
public class AsShcShiftMqConsumerHandler implements RocketMQListener<String> {
    private static final Logger logger = LoggerFactory.getLogger(AsShcShiftMqConsumerHandler.class);

//    @RocketMQMessageListener(topic = ""
//            , consumerGroup = "${inkelink.pm.mq.asmq.consumer.groupDefault:}"
//            , nameServer = "${inkelink.pm.mq.asmq.name-server:}"
//            , accessKey = "${inkelink.pm.mq.asmq.access-key:}"
//            , secretKey = "${inkelink.pm.mq.asmq.secret-key:}")

    @Autowired
    IMidAsShiftService midAsShiftService;

    @Value("${inkelink.pm.mq.asmq.consumer-shc.save-open:}")
    private String saveOpen;

    @Override
    public void onMessage(String message) {
// "updateCalendar":"1",
//"updateTime": “2023-12-01T17:24:43.141”

        logger.info("===========AsShcShiftMqConsumerHandler---start:" + message);
        if (StringUtils.isBlank(message)) {
            message = "";
        }
        MidApiLogEntity apilog = midAsShiftService.saveAsShfShift(message);
        if (StringUtils.endsWithIgnoreCase(saveOpen, "1")) {
            midAsShiftService.excute(apilog.getId().toString());
        }
        logger.info("===========AsShcShiftMqConsumerHandler---end:" + message);
    }

}