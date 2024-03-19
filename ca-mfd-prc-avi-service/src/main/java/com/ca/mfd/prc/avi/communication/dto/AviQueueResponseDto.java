/**
 * Copyright (c) 2023 依柯力 All rights reserved.
 * <p>
 * https://www.inkelink.com
 * <p>
 * 版权所有，侵权必究！
 */

package com.ca.mfd.prc.avi.communication.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.logging.log4j.util.Strings;

/**
 * AVI队列响应数据
 *
 * @author inkelink
 * @since 1.0.0
 */
@Schema(description = "AVI队列响应数据")
@Data
public class AviQueueResponseDto {
    @Schema(title = "标识ID")
    @JsonProperty("Id")
    private String id = Strings.EMPTY;
    @Schema(title = "订阅者代码")
    @JsonProperty("Code")
    private String code = Strings.EMPTY;
    @Schema(title = "Vin码")
    @JsonProperty("Vin")
    private String vin = Strings.EMPTY;

    @Schema(title = "顺序号")
    @JsonProperty("SequenceNo")
    private String sequenceNo = Strings.EMPTY;
    @Schema(title = "1-预内饰一线；2-预内饰二线")
    @JsonProperty("Direction")
    private String direction = Strings.EMPTY;

    @Schema(title = "备用字段1")
    @JsonProperty("Space01")
    private String space01 = Strings.EMPTY;
    @Schema(title = "备用字段2")
    @JsonProperty("Space02")
    private String space02 = Strings.EMPTY;
    @Schema(title = "备用字段3")
    @JsonProperty("Space03")
    private String space03 = Strings.EMPTY;
    @Schema(title = "备用字段4")
    @JsonProperty("Space04")
    private String space04 = Strings.EMPTY;

}
