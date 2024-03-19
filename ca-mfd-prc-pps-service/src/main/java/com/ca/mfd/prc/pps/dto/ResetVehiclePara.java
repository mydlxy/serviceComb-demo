package com.ca.mfd.prc.pps.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * 重置参数
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(title = "重置参数", description = "")
public class ResetVehiclePara {

    @Schema(description = "vin")
    private String vin = StringUtils.EMPTY;

    @Schema(description = "shopCode")
    private String shopCode = StringUtils.EMPTY;
}
