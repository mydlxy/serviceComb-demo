package com.ca.mfd.prc.pps.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * OutsourceEntryAreaDTO
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-18
 */
@Data
@Schema(title = "OutsourceEntryAreaDTO", description = "")
public class OutsourceEntryAreaDTO {

    @Schema(description = "线体编码")
    private String lineCode;

    @Schema(description = "线体名")
    private String lineName;
}
