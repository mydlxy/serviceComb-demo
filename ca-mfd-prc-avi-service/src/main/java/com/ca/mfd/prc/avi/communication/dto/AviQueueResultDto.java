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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * AVI队列响应数据
 *
 * @author inkelink
 * @since 1.0.0
 */
@Schema(description = "AVI队列响应数据")
@Data
public class AviQueueResultDto<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    @Schema(title = "错误码:200代表成功，其他为失败")
    @JsonProperty("Code")
    private Integer code = 0;

    @Schema(title = "返回消息")
    @JsonProperty("Message")
    private String message = Strings.EMPTY;

    @Schema(title = "接口调用标识")
    @JsonProperty("Success")
    private Boolean success = false;

    @Schema(title = "数据集合")
    @JsonProperty("Data")
    private List<T> data = new ArrayList<>();
}
