package com.ca.mfd.prc.pps.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * CutEntityPara
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-18
 */
@Data
@Schema(title = "CutEntityPara", description = "")
public class CutEntityPara {

    @Schema(description = "线体代码")
    private String lineCode;

    @Schema(description = "订阅码")
    private String subScriubCode;

    @Schema(description = "切换车辆")
    private String sn;
}
