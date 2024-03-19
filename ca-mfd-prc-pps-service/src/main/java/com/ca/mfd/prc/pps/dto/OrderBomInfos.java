package com.ca.mfd.prc.pps.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 订单
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-04
 */
@Data
@Schema(title = "工单", description = "")
public class OrderBomInfos {

    @Schema(description = "计划标识")
    private String planNo;

    @Schema(description = "版本")
    private String orderNo;
}
