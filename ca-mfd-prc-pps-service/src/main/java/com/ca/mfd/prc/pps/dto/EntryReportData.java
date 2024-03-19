package com.ca.mfd.prc.pps.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 冲压返修报工
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-04
 */
@Data
@Schema(title = "冲压返修报工", description = "")
public class EntryReportData {

    @Schema(description = "报工条码")
    private List<String> barcode;
}
