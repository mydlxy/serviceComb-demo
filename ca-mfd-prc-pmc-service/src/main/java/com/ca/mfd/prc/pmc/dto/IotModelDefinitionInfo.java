package com.ca.mfd.prc.pmc.dto;

import lombok.Data;

import java.util.List;

@Data
public class IotModelDefinitionInfo {
    /**
     * 注释说明
     */
    private List<IotModelPropertiesInfo> properties;
}
