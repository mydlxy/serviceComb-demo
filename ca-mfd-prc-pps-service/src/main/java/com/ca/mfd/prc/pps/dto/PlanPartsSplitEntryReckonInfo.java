package com.ca.mfd.prc.pps.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * PlanPartsSplitEntryReckonInfo
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-10-24
 */
@Data
@Schema(title = "PlanPartsSplitEntryReckonInfo", description = "计划拆分结果模型")
public class PlanPartsSplitEntryReckonInfo {
    /**
     * 计划编号
     */
    @Schema(description = "计划编号")
    private String planNo;

    /**
     * 工序编号
     */
    @Schema(description = "工序编号")
    private String processCode;

    /**
     * 工序描述
     */
    @Schema(description = "工序描述")
    private String processName;

    /**
     * 计划锁定数量
     */
    @Schema(description = "计划锁定数量")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private int lockCount;

    /**
     * 工序下的工作区域
     */
    @Schema(description = "工序下的工作区域")
    private List<ProcessArea> processAreas = new ArrayList<>();
}
