package com.ca.mfd.prc.pps.communication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Data
@Schema(description= "单车配置字实体")
public class SoftwareConfigDto {

    //控制器类型
    @Schema(title = "控制器类型")
    private String ecuCode = StringUtils.EMPTY;
    //控制器数据集合
    @Schema(title = "控制器数据集合")
    private List<EcuSoftwareDidDto> ecuConfigList;

}