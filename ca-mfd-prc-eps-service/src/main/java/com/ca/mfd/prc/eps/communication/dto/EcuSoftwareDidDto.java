package com.ca.mfd.prc.eps.communication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
@Schema(description= "配置字")
public class EcuSoftwareDidDto {



    @Schema(title = "did")
    private String did = StringUtils.EMPTY;
    @Schema(title = "描述")
    private String description = StringUtils.EMPTY;
    @Schema(title = "配置码")
    private String analysisValue = StringUtils.EMPTY;


}
