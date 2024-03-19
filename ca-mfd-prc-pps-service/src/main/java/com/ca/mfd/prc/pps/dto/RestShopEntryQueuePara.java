package com.ca.mfd.prc.pps.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * RestShopEntryQueuePara
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-18
 */
@Data
@Schema(title = "RestShopEntryQueuePara", description = "")
public class RestShopEntryQueuePara {

    @Schema(description = "订阅码")
    private String shopCode;

    @Schema(description = "车型")
    private String model;

    @Schema(description = "VIN码")
    private String sn;
}
