package com.ca.mfd.prc.pmc.dto;

import lombok.Data;

@Data
public class DefinitionInfo {
    private String app_id;

    private String timestamp;

    private String trans_id;

    private String token;

    private String requestId;

    private DefinitionItems data;
}
