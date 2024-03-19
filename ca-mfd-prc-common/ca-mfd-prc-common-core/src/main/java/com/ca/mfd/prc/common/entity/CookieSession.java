package com.ca.mfd.prc.common.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * CookieSession信息
 *
 * @author inkelink eric.zhou@hg2mes.com
 * @date 2023-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "CookieSession")
public class CookieSession implements Serializable {
    private static final long serialVersionUID = 1L;
    @Schema(title = "session 编号")
    private String sessionGuid;

    @Schema(title = "RememberType")
    private Integer rememberType = 0;

}

