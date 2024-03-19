package com.ca.mfd.prc.pps.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * LmsWeOnlineQueueDTO
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-18
 */
@Data
@Schema(title = "LmsWeOnlineQueueDTO", description = "")
public class LmsWeOnlineQueueDTO {

    @Schema(description = "车辆识别码")
    private String vin;

    @Schema(description = "物料编码")
    private String materialCode;

    @Schema(description = "唯一码ID")
    private String uniqueCode;
}
