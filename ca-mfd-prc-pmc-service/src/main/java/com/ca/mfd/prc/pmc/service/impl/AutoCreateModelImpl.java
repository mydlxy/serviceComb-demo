package com.ca.mfd.prc.pmc.service.impl;

import com.ca.mfd.prc.common.utils.SpringContextUtils;
import com.ca.mfd.prc.pmc.service.IAutoCreateModel;
import com.ca.mfd.prc.pmc.service.IPmcAlarmComponentAlarmService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author inkelink
 * @date 2023年4月4日
 */
@Service
public class AutoCreateModelImpl implements IAutoCreateModel {
    @Override
    @Async
    public void start() {
        IPmcAlarmComponentAlarmService pmcAlarmComponentAlarmService = SpringContextUtils.getBean(IPmcAlarmComponentAlarmService.class);
        pmcAlarmComponentAlarmService.autoDealModel();
        pmcAlarmComponentAlarmService.saveChange();
    }
}
