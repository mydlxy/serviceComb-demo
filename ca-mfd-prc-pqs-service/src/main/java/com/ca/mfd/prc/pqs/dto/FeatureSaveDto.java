package com.ca.mfd.prc.pqs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "featurn返回进出参对象")
public class FeatureSaveDto {

    private FeatureInfo featureInfo;

    private String fileName;

    private String resultName;



}

