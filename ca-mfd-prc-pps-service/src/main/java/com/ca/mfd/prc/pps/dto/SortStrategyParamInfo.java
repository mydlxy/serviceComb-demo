package com.ca.mfd.prc.pps.dto;


import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * SortStrategyParamInfo
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(title = "SortStrategyParamInfo", description = "")
public class SortStrategyParamInfo {

    @Schema(description = "FeatureName")
    private String featureName;

    @Schema(description = "Value")
    private String value;

    @Schema(description = "Quantity")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer quantity = 0;

    @Schema(description = "BeginIndex")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer beginIndex = 0;

    @Schema(description = "EndIndex")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer endIndex = 0;
}
