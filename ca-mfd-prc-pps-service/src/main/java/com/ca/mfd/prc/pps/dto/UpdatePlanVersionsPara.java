package com.ca.mfd.prc.pps.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * 更新版本模型
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(title = "更新版本模型", description = "")
public class UpdatePlanVersionsPara {

    @Schema(description = "计划标识")
    private String planId = StringUtils.EMPTY;

    @Schema(description = "版本")
    private String version;
}
