package com.ca.mfd.prc.pps.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * 冲压返修报工
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-04
 */
@Data
@Schema(title = "冲压返修报工", description = "")
public class RepairReportInfo {

    @Schema(description = "报工数量")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer productCount;

    @Schema(description = "报工条码")
    private String barcode = StringUtils.EMPTY;
}
