package com.ca.mfd.prc.common.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * OpenData
 *
 * @author inkelink
 * @date 2023-08-17
 */
@Schema(title = "OpenData", description = "OpenData")
@Data
public class OpenData implements Serializable {
    @Schema(description = "openType")
    private Integer openType = 10;
    @Schema(description = "openId")
    private String openId = "";
    @Schema(description = "accessToken")
    private String accessToken = "";
    @Schema(description = "refreshToken")
    private String refreshToken = "";
    @Schema(description = "expiresIn")
    private Long expiresIn = 36000L;
    @Schema(description = "scope")
    private String scope = "";
    @Schema(description = "unionCode")
    private String unionCode = "";
    @Schema(description = "extData")
    private String extData = "";
}
