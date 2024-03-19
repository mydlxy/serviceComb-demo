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

/**
 * 响应数据
 *
 * @author inkelink
 * @since 1.0.0
 */
@Schema(description = "响应")
@Data
public class ApiResultVo {

    @JsonProperty("Status")
    private String status;
    @JsonProperty("Message")
    private String message ;
    @JsonProperty("SeqId")
    private Long seqId;

}
