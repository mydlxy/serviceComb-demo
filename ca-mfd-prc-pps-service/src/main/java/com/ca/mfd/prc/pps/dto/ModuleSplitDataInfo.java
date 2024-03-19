package com.ca.mfd.prc.pps.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * ModuleSplitDataInfo
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-18
 */
@Data
@Schema(title = "ModuleSplitDataInfo", description = "")
public class ModuleSplitDataInfo {

    @Schema(description = "SplitNum")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer SplitNum = 0;

    @Schema(description = "DifferenceNum")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer DifferenceNum = 0;

}
