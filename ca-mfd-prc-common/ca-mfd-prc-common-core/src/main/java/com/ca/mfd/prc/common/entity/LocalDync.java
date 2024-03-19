package com.ca.mfd.prc.common.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * LocalDync
 *
 * @author inkelink
 * @date 2023-08-17
 */
@Data
@Schema(title = "localDync", description = "localDync")
public class LocalDync implements Serializable {
    @Schema(description = "CookieSession")
    private CookieSession cookieSession;
    @Schema(description = "UserInfo")
    private OnlineUserDTO userInfo;
    @Schema(description = "Result")
    private Boolean result;
}
