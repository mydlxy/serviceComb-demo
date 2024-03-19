package com.ca.mfd.prc.scheduling.entity;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Trigger;

@Data
public class TriggerStateModel {
    private String nextFireTime = StringUtils.EMPTY;
    private Trigger.TriggerState triggerState;
}
