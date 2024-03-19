package com.ca.mfd.prc.pqs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "spc特征对象")
public class FeatureValueDto {
    private Float featureValue;

    private String conditionValue;

    private Boolean isSpcParam;

}
