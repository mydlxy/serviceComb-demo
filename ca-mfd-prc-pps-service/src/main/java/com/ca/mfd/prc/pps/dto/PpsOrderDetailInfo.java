package com.ca.mfd.prc.pps.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * 订单信息
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-04
 */
@Data
@Schema(title = "PpsOrderDetailInfo", description = "")
public class PpsOrderDetailInfo {
    @Schema(description = "vin")
    private String vin = StringUtils.EMPTY;
    @Schema(description = "sn")
    private String sn = StringUtils.EMPTY;
    @Schema(description = "model")
    private String model = StringUtils.EMPTY;
    @Schema(description = "materialNo")
    private String materialNo = StringUtils.EMPTY;
    @Schema(description = "characteristic1")
    private String characteristic1 = StringUtils.EMPTY;
    @Schema(description = "characteristic2")
    private String characteristic2 = StringUtils.EMPTY;
    @Schema(description = "characteristic3")
    private String characteristic3 = StringUtils.EMPTY;
}
