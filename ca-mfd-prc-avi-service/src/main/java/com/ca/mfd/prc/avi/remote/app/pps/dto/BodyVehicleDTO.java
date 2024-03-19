package com.ca.mfd.prc.avi.remote.app.pps.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * BodyVehicleDTO
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-18
 */
@Data
@Schema(title = "BodyVehicleDTO", description = "")
public class BodyVehicleDTO {

    @Schema(description = "生产顺序号")
    private Integer displayNo = 0;

    @Schema(description = "车辆识别码")
    private String tpsCode;

    @Schema(description = "VIN")
    private String vin;

    @Schema(description = "车型")
    private String vehicleModel;
}
