package com.ca.mfd.prc.pps.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * PlanRemarkInfo
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(title = "PlanRemarkInfo", description = "")
public class PlanRemarkInfo {

    @Schema(description = "ids")
    private List<String> ids;

    @Schema(description = "remark")
    private String remark;
}
