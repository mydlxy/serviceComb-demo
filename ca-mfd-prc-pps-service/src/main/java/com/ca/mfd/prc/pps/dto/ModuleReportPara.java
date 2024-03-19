package com.ca.mfd.prc.pps.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * ModuleReportPara
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-18
 */
@Data
@Schema(title = "ModuleReportPara", description = "")
public class ModuleReportPara {

    @Schema(description = "线体编码")
    private String lineCode;

    @Schema(description = "模组类型")
    private String moduleCode;

    @Schema(description = "模组条码")
    private String moduleBarcode;

    @Schema(description = "报工状态 1 合格 2 不合格")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer reoprtStatus = 0;
}
