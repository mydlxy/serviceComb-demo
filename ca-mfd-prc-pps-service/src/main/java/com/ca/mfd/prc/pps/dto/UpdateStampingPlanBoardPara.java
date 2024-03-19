package com.ca.mfd.prc.pps.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 更新版本模型
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(title = "更新版本模型", description = "")
public class UpdateStampingPlanBoardPara {

    @Schema(description = "生产计划编号")
    @JsonAlias(value = {"planNo", "PlanNo"})
    private String planNo;

    @Schema(description = "板材批次号")
    @JsonAlias(value = {"batchCode", "BatchCode"})
    private String batchCode;
}
