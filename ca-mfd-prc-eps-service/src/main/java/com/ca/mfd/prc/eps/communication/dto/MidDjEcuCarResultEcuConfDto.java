package com.ca.mfd.prc.eps.communication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author inkelink
 * @Description: 电检软件结果信息ecuConf
 * @date 2024年2月19日
 */
@Data
@Schema(description = "电检软件结果信息ecuConf")
public class MidDjEcuCarResultEcuConfDto {

    @Schema(title = "DID")
    private String did = StringUtils.EMPTY;

    @Schema(title = "配置码")
    private String analysisValue = StringUtils.EMPTY;

    @Schema(title = "描述")
    private String description = StringUtils.EMPTY;

}
