package com.ca.mfd.prc.common.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 门户-统一门户登录返回参数
 *
 * @author mason
 * @date 2023-09-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "门户-统一门户登录返回参数")
public class OauthLoginDTO {
    @Schema(title = "token")
    private String access_token;
    @Schema(title = "用户refreshToken")
    private String refresh_token;
    @Schema(title = "token类型")
    private String token_type;
}
