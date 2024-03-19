package com.ca.mfd.prc.avi.communication.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

@Data
@Schema(description= "下发AVI队列")
public class AviQueueRequestDto {

    @Schema(title = "订阅者代码")
    @JsonAlias(value = {"code","Code"})
    //@JsonProperty("Code")
    private String code = StringUtils.EMPTY;

    @Schema(title = "拉取数量 范围1-20")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    @JsonAlias(value = {"count","Count"})
    //@JsonProperty("Count")
    private Integer count = 0;


}
