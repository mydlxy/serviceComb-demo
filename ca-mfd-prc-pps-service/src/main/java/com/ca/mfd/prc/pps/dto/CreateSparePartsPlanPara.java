package com.ca.mfd.prc.pps.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * CreateSparePartsPlanPara
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-18
 */
@Data
@Schema(title = "CreateSparePartsPlanPara", description = "")
public class CreateSparePartsPlanPara {

    @Schema(description = "MaterialNo")
    private String materialNo;

    @Schema(description = "MaterialName")
    private String materialName;

    @Schema(description = "LineCode")
    private String lineCode;

    @Schema(description = "EndAvi")
    private String endAvi;

    @Schema(description = "PlanQty")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer planQty = 0;

}