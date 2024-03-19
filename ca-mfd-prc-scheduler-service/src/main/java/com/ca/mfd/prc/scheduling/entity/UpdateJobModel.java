package com.ca.mfd.prc.scheduling.entity;

import lombok.Data;

@Data
public class UpdateJobModel {
    private ScheduleModel newModel;
    private ScheduleModel oldModel;
}
