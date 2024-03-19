package com.ca.mfd.prc.pps.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * SplitModuleEntryPara
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-18
 */
@Data
@Schema(title = "SplitModuleEntryPara", description = "")
public class SplitModuleEntryPara {

    @Schema(description = "订单号")
    private String orderNo;
}
