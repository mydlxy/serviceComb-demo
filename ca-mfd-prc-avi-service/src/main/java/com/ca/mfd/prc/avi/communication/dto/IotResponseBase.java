package com.ca.mfd.prc.avi.communication.dto;

import lombok.Data;

@Data
public class IotResponseBase {
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
    private int service_type;
    private String status_code;
    private String trans_id;
    private String rawdata;
}
