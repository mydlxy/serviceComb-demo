package com.ca.mfd.prc.pps.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author eric.zhou
 * @Description: PmVersion的feign接口
 * @date 2023年6月1日
 * @变更说明 BY eric.zhou At 2023年6月1日
 */
@Data
@Schema(title = "VehicleChangeTpsInfo", description = "")
public class VehicleChangeTpsInfo {

    @Schema(description = "目标订单", example = "")
    private String desOrderNo;

    @Schema(description = "目标TPS码", example = "")
    private String desTpsCode;

    @Schema(description = "替换订单号", example = "")
    private String changeOrderNo;

    @Schema(description = "替换TPS码", example = "")
    private String changTpsCode;
}
