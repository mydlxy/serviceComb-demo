package com.ca.mfd.prc.eps.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 电池信息
 *
 * @author inkelink
 * @since 1.0.0 2023-04-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "电池信息")
public class VehicleBatteryInfoDTO {
    @Schema(title = "Vin号")
    private String vin;

    @Schema(title = "电池编码")
    private String barCode;
}
