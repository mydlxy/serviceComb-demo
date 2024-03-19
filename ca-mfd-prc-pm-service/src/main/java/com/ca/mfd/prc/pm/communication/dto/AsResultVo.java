/**
 * Copyright (c) 2023 依柯力 All rights reserved.
 * <p>
 * https://www.inkelink.com
 * <p>
 * 版权所有，侵权必究！
 */

package com.ca.mfd.prc.pm.communication.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * AS响应数据
 *
 * @author inkelink
 * @since 1.0.0
 */
@Schema(description = "AS响应数据")
@Data
public class AsResultVo {

    @JsonProperty("code")
    private String code;
    @JsonProperty("msg")
    private String msg;

    @JsonProperty("datas")
    private Object datas;
}
