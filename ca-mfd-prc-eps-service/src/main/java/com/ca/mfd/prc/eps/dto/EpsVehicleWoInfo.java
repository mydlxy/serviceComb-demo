package com.ca.mfd.prc.eps.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * EpsVehicleWoInfo
 *
 * @author eric.zhou
 * @since 1.0.0 2023-09-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(title = "EpsVehicleWoInfo", description = "")
public class EpsVehicleWoInfo implements Serializable {

    @Schema(description = "条码")
    private String barcode = StringUtils.EMPTY;

    @Schema(description = "流水号")
    private String sequenceNo = StringUtils.EMPTY;

}