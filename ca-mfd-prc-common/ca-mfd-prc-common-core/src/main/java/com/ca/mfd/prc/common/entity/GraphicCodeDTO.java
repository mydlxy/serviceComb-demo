package com.ca.mfd.prc.common.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 门户-获取用户信息返回数据
 *
 * @author mason
 * @date 2023-10-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "门户-图形验证码")
public class GraphicCodeDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    @Schema(title = "图形验证码")
    private String captchaCode;
    @Schema(title = "验证码token")
    private String captchaToken;
}
