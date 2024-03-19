package com.ca.mfd.prc.pps.communication.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

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
