package com.ca.mfd.prc.eps.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * 载具
 *
 * @author eric.zhou
 * @since 1.0.0 2023-09-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(title = "载具", description = "")
public class BindCarrierPara implements Serializable {

    @Schema(description = "载具条码")
    private String barcode = StringUtils.EMPTY;

    @Schema(description = "载具条码")
    private String carrierCode = StringUtils.EMPTY;

}