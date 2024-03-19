package com.ca.mfd.prc.pps.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * AffirmModuleEntryPara
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-18
 */
@Data
@Schema(title = "AffirmModuleEntryPara", description = "")
public class AffirmModuleEntryPara {

    @Schema(description = "工单号")
    private String entryNo;

    @Schema(description = "线体编码")
    private String lineCode;

}
