package com.ca.mfd.prc.pm.communication.mq;

import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.pm.communication.dto.MidAsLineCalendarReq;
import com.ca.mfd.prc.pm.communication.entity.MidApiLogEntity;
import com.ca.mfd.prc.pm.communication.service.IMidAsLineCalendarService;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author inkelink
 * @Description: AS线体日历
 * @date 2023年10月09日
 * @变更说明 BY inkelink At 2023年10月09日
 */
@Component
@ConditionalOnExpression("#{'true'.equals(environment.getProperty('inkelink.pm.mq.asmq.enable'))}")
@RocketMQMessageListener(topic = "${inkelink.pm.mq.asmq.consumer-linecal.topic:}"
        , consumerGroup = "${inkelink.pm.mq.asmq.consumer-linecal.group:}")
public class AsLineCalendarMqConsumerHandler implements RocketMQListener<String> {
    private static final Logger logger = LoggerFactory.getLogger(AsLineCalendarMqConsumerHandler.class);

//    @RocketMQMessageListener(topic = ""
//            , consumerGroup = "${inkelink.pm.mq.asmq.consumer.groupDefault:}"
//            , nameServer = "${inkelink.pm.mq.asmq.name-server:}"
//            , accessKey = "${inkelink.pm.mq.asmq.access-key:}"
//            , secretKey = "${inkelink.pm.mq.asmq.secret-key:}")

    @Autowired
    IMidAsLineCalendarService midAsLineCalendarService;

    @Value("${inkelink.pm.mq.asmq.consumer-linecal.save-open:}")
    private String saveOpen;

    @Override
    public void onMessage(String message) {
        // this.messageHandle(message);
//        "organizationCode": " CQY",
//        "workshopCode": " BTS1 ",
//        "lineCode": “BTS1_W1”,
//        "updateTime": “2023-12-01T17:24:43.141”

        logger.info("===========AsLineCalendarMqConsumerHandler---start:" + message);
        if (StringUtils.isBlank(message)) {
            message = "{}";
        }
        MidAsLineCalendarReq req = JsonUtils.parseObject(message, MidAsLineCalendarReq.class);
        List<MidAsLineCalendarReq> reqs = new ArrayList<>();
        reqs.add(req);
        MidApiLogEntity apilog = midAsLineCalendarService.saveAsLineCalendar(reqs);
        if (StringUtils.endsWithIgnoreCase(saveOpen, "1")) {
            midAsLineCalendarService.excute(apilog.getId().toString());
        }
        logger.info("===========AsLineCalendarMqConsumerHandler----end:" + message);
    }

}
