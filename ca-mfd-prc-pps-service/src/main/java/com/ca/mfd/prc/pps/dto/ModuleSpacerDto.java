package com.ca.mfd.prc.pps.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * 隔热垫
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-10-18
 */
@Data
@Schema(title = "隔热垫", description = "")
public class ModuleSpacerDto {

    @Schema(description = "隔热垫编码")
    @JsonProperty("SpacerCode")
    private String spacerCode = StringUtils.EMPTY;

    @Schema(description = "隔热垫数量")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    @JsonProperty("SpacerNum")
    private Integer spacerNum;

}
