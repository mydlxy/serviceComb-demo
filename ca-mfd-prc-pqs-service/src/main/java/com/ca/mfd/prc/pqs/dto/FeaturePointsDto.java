package com.ca.mfd.prc.pqs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "featurnPoints对象")
public class FeaturePointsDto {

    private List<FeatureValueDto> points;

    private int featureLength;

    private float mean;

    private float sigma;

    private float min;

    private float max;

    private float LCL;

    private float UCL;

    private float LSL;

    private float USL;

    private List<PairDto> histogramPoints;

    private  float DX;

    private List<PairDto> nPoints;



}

