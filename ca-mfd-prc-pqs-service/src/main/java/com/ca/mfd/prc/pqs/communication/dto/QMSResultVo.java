/**
 * Copyright (c) 2023 依柯力 All rights reserved.
 * <p>
 * https://www.inkelink.com
 * <p>
 * 版权所有，侵权必究！
 */

package com.ca.mfd.prc.pqs.communication.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.logging.log4j.util.Strings;

/**
 * QMS响应数据
 *
 * @author inkelink
 * @since 1.0.0
 */
@Schema(description = "QMS响应数据")
@Data
public class QMSResultVo {

    @JsonProperty("Code")
    private Integer code;
    @JsonProperty("Message")
    private String message = Strings.EMPTY;
    @JsonProperty("Success")
    private Boolean success;


}
