package com.ca.mfd.prc.pqs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "featurnInfos对象")
public class FeatureInfoDtos {
    private String dateStampDay;
    private String featureName;
    private float LSL;
    private float USL;
    private int featureLength;
    private int groupResize;
    private List<FeatureValueDto> featureGPoints;
    private float mean;
    private float groupMean;
    private float sigma;
    private float groupSigma;
    private float sigmaC;
    private float sigmaP;
    private float LCL;
    private float UCL;
    private double CP;
    private double CPL;
    private double CPU;
    private double CPK;
    private double PP;
    private double PPL;
    private double PPU;
    private double PPK;
    private float[] groupMeanList;
    private float[] groupRList;
    private float[] groupStdList;
    private List<String> firstTimePoints;
    private float gLCL;
    private float gUCL;
    private float[] mr;
    private List<String> result;
    private double xbarRLcl;
    private double xbarRUcl;
    private double xbarRMean;
    private double xbarSLcl;
    private double xbarSUcl;
    private double xbarSMean;
    private double stdLcl;
    private double stdUcl;
    private double rLcl;
    private double rUcl;
    private float iLcl;
    private float iUcl;
    private float mrLcl;
    private float mrUcl;
    private float mrMean;
    private float stdMean;
    private float rMean;
    private double sigmaStd;
    private double sigmaR;


}
