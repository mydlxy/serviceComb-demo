package com.ca.mfd.prc.pps.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 订单处理
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-04
 */
@Data
@Schema(title = "OrderProcessInfo", description = "")
public class OrderProcessInfo {
    @Schema(description = "planIds")
    private List<String> planIds;
    @Schema(description = "processId")
    private String processId;
}
