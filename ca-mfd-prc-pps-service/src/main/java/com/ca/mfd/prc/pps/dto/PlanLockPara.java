package com.ca.mfd.prc.pps.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author eric.zhou
 * @Description: PlanLockPara
 * @date 2023年6月1日
 * @变更说明 BY eric.zhou At 2023年6月1日
 */
@Data
@Schema(title = "PlanLockPara", description = "")
public class PlanLockPara {
    @Schema(description = "planNos")
    private List<String> planNos;

    @Schema(description = "workstationCode")
    private String workstationCode;
}
