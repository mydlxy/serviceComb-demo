package com.ca.mfd.prc.pqs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;



@Data
@Schema(description = "featurn返回对象")
public class FeatureResDto {

    private FeaturePointsDto featurePoints;

    private FeatureInfoDtos featureInfos;

    private float minY;

    private float maxY;



}

