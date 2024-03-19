package com.ca.mfd.prc.pm.dto;

import lombok.Data;

import java.util.List;

@Data
public class ManyStationMaterialRelaDTO {
    private List<String> materialNos;
    private String workstationCode;
}
