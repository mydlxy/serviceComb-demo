package com.ca.mfd.prc.pqs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "spc对象")
public class SpcDto implements Cloneable{
    private String featureName;

    private Float lowerLimit;

    private Float upperLimit;

    @Override
    public SpcDto clone() throws CloneNotSupportedException {
        return (SpcDto) super.clone();
    }

    private List<FeatureValueDto> featureValueList;

    private String unit;

    private String vin;

    private SpcQueryParamDto operationParam;

}

