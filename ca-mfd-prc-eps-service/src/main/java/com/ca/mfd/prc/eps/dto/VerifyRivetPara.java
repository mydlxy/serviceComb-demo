package com.ca.mfd.prc.eps.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * VerifyRivetPara
 *
 * @author eric.zhou
 * @since 1.0.0 2023-09-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(title = "VerifyRivetPara", description = "")
public class VerifyRivetPara extends ProductWoDTO {

    @Schema(description = "料口条码")
    private String orificeBarcode = StringUtils.EMPTY;

    @Schema(description = "物料条码")
    private String barcode = StringUtils.EMPTY;

    @Schema(description = "料口位置")
    private String orificeLocation = StringUtils.EMPTY;
}