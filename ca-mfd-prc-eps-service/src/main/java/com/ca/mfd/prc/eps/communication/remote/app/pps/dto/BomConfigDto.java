package com.ca.mfd.prc.eps.communication.remote.app.pps.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Data
@Schema(description= "单车配置字实体")
public class BomConfigDto {
    //软件发布单
    @Schema(title = "软件发布单")
    private String changeCode = StringUtils.EMPTY;
    //控制器数据集合
    @Schema(title = "控制器数据集合")
    private List<SoftwareConfigDto> softwareConfigList;

}
