package com.ca.mfd.prc.pqs.communication.dto;

import lombok.Data;

import java.util.List;

@Data
public class LmsSupplierResultVo {
    private String code;
    private String msg;
    private List<LmsSupplierDto> data;
    private Boolean success;
}
