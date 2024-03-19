package com.ca.mfd.prc.pqs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "spc查询条件")
public class SpcQueryParamDto {
    @Schema(title = "筛选类型 时间区域:1 数值范围:2 数据标签:3")
    private String queryType;
    @Schema(title = "筛选值")
    private String queryValue;
    @Schema(title = "抽样方式，0.全部样本，1.固定抽样，2.每N抽1，3.每时抽N，4.剔除末尾")
    private String sampleType;
    @Schema(title = "抽样数量")
    private String sampleNum;
    @Schema(title = "是否舍弃余数")
    private Boolean isLoseTail;
    @Schema(title = "分组方式，SIZE.按组大小分组，GROUP.按组总数分组。")
    private String groupType;
    @Schema(title = "分组大小")
    private String groupSize;
    @Schema(title = "分组个数")
    private String groupLength;
    @Schema(title = "计算LCL、UCL时对sigma的幅度，N*sigma")
    private String clm;
    @Schema(title = "结果 all fail pass")
    private String resultType;
    @Schema(title = "特征值筛选起点")
    private String lowerFeatureValue;
    @Schema(title = "特征值筛选止点")
    private String upperFeatureValue;
    @Schema(title= "八项判异值")
    private String minitable;

}

