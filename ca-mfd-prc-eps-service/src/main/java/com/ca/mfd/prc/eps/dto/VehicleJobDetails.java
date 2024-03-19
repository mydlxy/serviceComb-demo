package com.ca.mfd.prc.eps.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * VehicleJobDetails
 *
 * @author eric.zhou
 * @since 1.0.0 2023-09-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(title = "VehicleJobDetails", description = "")
public class VehicleJobDetails implements Serializable {

    @Schema(description = "区域编码")
    private String lineCode = StringUtils.EMPTY;

    @Schema(description = "物料组件名称")
    private String materialName = StringUtils.EMPTY;

    @Schema(description = "物料组件编码")
    private String materialCode = StringUtils.EMPTY;

    @Schema(description = "执行码")
    private String job = StringUtils.EMPTY;
}