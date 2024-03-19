package com.ca.mfd.prc.pps.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * BodyShopStartWorkPara
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-18
 */
@Data
@Schema(title = "BodyShopStartWorkPara", description = "")
public class BodyShopStartWorkPara {

    @Schema(description = "吊牌条码")
    private String tagNo;

    @Schema(description = "工位编码")
    private String workstationCode;

    @Schema(description = "车型")
    private String model;

    @Schema(description = "计划号")
    private String planNo;
}
