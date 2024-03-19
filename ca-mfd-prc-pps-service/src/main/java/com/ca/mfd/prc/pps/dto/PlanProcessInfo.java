package com.ca.mfd.prc.pps.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * PlanProcessInfo
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(title = "PlanProcessInfo", description = "")
public class PlanProcessInfo {

    @Schema(description = "planIds")
    private List<String> planIds;

    @Schema(description = "processId")
    private String processId;
}
