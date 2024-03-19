package com.ca.mfd.prc.pm.dto;

import lombok.Data;

import java.util.List;

@Data
public class WorkshopCodeMaterialRelaDTO {
    private List<String> materialNos;
    private String shopCode;
}
