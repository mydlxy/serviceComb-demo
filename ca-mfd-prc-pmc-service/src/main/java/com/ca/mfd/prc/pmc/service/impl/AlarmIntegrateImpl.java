package com.ca.mfd.prc.pmc.service.impl;

import com.ca.mfd.prc.common.utils.SpringContextUtils;
import com.ca.mfd.prc.pmc.service.IAlarmIntegrate;
import com.ca.mfd.prc.pmc.service.IPmcAlarmComponentAlarmService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author inkelink
 * @date 2023年4月4日
 */
@Service
public class AlarmIntegrateImpl implements IAlarmIntegrate {
    private static final Logger logger = LoggerFactory.getLogger(AlarmIntegrateImpl.class);

    @Override
    @Async
    public void start() {
        IPmcAlarmComponentAlarmService pmcAlarmComponentAlarmService = SpringContextUtils.getBean(IPmcAlarmComponentAlarmService.class);
        pmcAlarmComponentAlarmService.data1();
        pmcAlarmComponentAlarmService.saveChange();

    }
}
