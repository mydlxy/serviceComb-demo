package com.ca.mfd.prc.common.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * AllSession
 *
 * @author inkelink
 * @date 2023-08-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "AllSession")
public class AllSession implements Serializable {
    @Schema(title = "当前时间")
    private PrmLocalSession localData;
    @Schema(title = "当前时间")
    private RemoteSession remoteData;
    @Schema(title = "当前时间")
    private CookieSession cookieData;
    @Schema(title = "当前时间")
    private Boolean isRegiste;
    @Schema(title = "当前时间")
    private Boolean isException;
    @Schema(title = "当前时间")
    private Boolean needBind;
}
