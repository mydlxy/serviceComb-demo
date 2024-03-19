package com.ca.mfd.prc.core.prm.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author eric.zhou
 */
@Schema(title = "UserLoginLock", description = "UserLoginLock")
@Data
public class UserLoginLock implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(title = "客户端浏览器信息")
    private String userAgent;

    @Schema(title = "登录IP")
    private String loginIp;

    @Schema(title = "登录用户名")
    private String userName;

    @Schema(title = "登录次数")
    private Integer lgoinCount;

}


