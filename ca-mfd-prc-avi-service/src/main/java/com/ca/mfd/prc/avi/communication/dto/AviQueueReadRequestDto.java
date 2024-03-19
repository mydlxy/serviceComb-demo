package com.ca.mfd.prc.avi.communication.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Data
@Schema(description= "下发AVI队列")
public class AviQueueReadRequestDto {

    @Schema(title = "订阅者代码")
    @JsonAlias(value = {"id","ID"})
    //@JsonProperty("Code")
    private List<String> id = new ArrayList<>();

}
