package com.ca.mfd.prc.pmc.dto;

import lombok.Data;
import org.apache.kafka.common.protocol.types.Field;

@Data
public class IotModelPropertiesInfo {

    private String accessMethod;

    private Boolean necessary;

    private String name;

    private Integer influxFlag;

    private String description;

    private String key;
}
