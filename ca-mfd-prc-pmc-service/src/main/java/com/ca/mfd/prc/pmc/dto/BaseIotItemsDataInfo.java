package com.ca.mfd.prc.pmc.dto;

import lombok.Data;
import org.apache.kafka.common.protocol.types.Field;

@Data
public class BaseIotItemsDataInfo {

    private String messageId;

    private String params;
}
