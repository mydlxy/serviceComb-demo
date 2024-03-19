package com.ca.mfd.prc.pps.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author eric.zhou
 * @Description: PackBranchPlanLockPara
 * @date 2023年6月1日
 * @变更说明 BY eric.zhou At 2023年6月1日
 */
@Data
@Schema(title = "PackBranchPlanLockPara", description = "")
public class PackBranchPlanLockPara {
    @Schema(description = "planNo")
    private String planNo;

    @Schema(description = "planQty")
    private Integer planQty;
}
