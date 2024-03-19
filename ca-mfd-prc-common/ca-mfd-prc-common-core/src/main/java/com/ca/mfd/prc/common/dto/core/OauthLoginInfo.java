package com.ca.mfd.prc.common.dto.core;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 门户-统一门户登录请求参数
 *
 * @author mason
 * @date 2023-09-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "门户-统一门户登录请求参数")
public class OauthLoginInfo {
    @Schema(title = "工号")
    private String username;
    @Schema(title = "密码")
    private String password;
    @Schema(title = "验证码")
    private String captchaCode;
    @Schema(title = "验证码Token")
    private String captchaToken;
    @Schema(title = "固写password")
    private String grant_type="password";
    @Schema(title = "固写all")
    private String scope="all";

}
