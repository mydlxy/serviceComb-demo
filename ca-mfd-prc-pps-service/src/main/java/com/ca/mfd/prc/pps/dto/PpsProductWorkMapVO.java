package com.ca.mfd.prc.pps.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * PpsProductWorkMapVO
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(title = "PpsProductWorkMapVO", description = "")
public class PpsProductWorkMapVO {
    @Schema(description = "text")
    private String text;
    @Schema(description = "Value")
    private String value;
    @Schema(description = "Label")
    private String label;
    @Schema(description = "ErpWorkCode")
    private String erpWorkCode;
    @Schema(description = "ErpWorkName")
    private String erpWorkName;


}
