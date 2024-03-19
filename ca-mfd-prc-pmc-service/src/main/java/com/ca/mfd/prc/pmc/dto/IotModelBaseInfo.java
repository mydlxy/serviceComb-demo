package com.ca.mfd.prc.pmc.dto;

import lombok.Data;
import org.apache.kafka.common.protocol.types.Field;

@Data
public class IotModelBaseInfo {

    private String code;

    private IotModelDataInfo data;

    private String requestId;

    private String message;
}
