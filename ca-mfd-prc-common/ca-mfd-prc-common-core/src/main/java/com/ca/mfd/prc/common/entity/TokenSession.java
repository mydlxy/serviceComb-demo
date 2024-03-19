package com.ca.mfd.prc.common.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * TokenDataInfo信息
 *
 * @author inkelink eric.zhou@hg2mes.com
 * @date 2023-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "TokenDataInfo信息")
public class TokenSession implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(title = "CookieSession")
    private String apiMd5;

    @Schema(title = "CookieSession")
    private Boolean result = true;

    @Schema(title = "CookieSession")
    private String meessage = "成功";

}

