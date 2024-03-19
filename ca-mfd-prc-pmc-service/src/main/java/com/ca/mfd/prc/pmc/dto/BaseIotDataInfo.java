package com.ca.mfd.prc.pmc.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.kafka.common.protocol.types.Field;

@Data
public class BaseIotDataInfo {

    @Schema(title = "")
    @JsonSerialize(using = ToStringSerializer.class)
    private long action;

    private int data_size;

    private String device_key;

    private String log_level;

    private String message_id;

    private String operation_by_id;

    private String operation_code;

    private String product_key;

    private String request_time;

    private String sequence_id;

    private String service_type;

    private String status_code;

    private String trans_id;

    private String rawdata;
}
