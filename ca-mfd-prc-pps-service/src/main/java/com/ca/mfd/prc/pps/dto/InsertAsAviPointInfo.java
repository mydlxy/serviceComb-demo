package com.ca.mfd.prc.pps.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * InsertAsAviPointInfo
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-18
 */
@Data
@Schema(title = "InsertAsAviPointInfo", description = "")
public class InsertAsAviPointInfo {

    @Schema(description = "PlanNo")
    private String planNo = StringUtils.EMPTY;

    @Schema(description = "Vin")
    private String vin = StringUtils.EMPTY;

    @Schema(description = "LineCode")
    private String lineCode = StringUtils.EMPTY;

    @Schema(description = "AviCode")
    private String aviCode = StringUtils.EMPTY;

    @Schema(description = "触发类型(1：正常通过;2：车辆SET OUT;3：车辆SET IN;X：车辆下线;S：车辆上线 )")
    private String scanType = "1";

    @Schema(description = "报工数量（合格）")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer qualifiedCount = 1;

    @Schema(description = "报工数量（不合格）")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer badCount = 0;
}
