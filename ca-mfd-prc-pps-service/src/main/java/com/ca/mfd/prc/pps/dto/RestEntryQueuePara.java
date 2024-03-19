package com.ca.mfd.prc.pps.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * RestEntryQueuePara
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-18
 */
@Data
@Schema(title = "RestEntryQueuePara", description = "")
public class RestEntryQueuePara {

    @Schema(description = "订阅码")
    private String subScriubCode;

    @Schema(description = "车型")
    private String model;

    @Schema(description = "VIN码")
    private String sn;
}
