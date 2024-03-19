package com.ca.mfd.prc.common.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 门户-获取用户信息返回数据
 *
 * @author mason
 * @date 2023-09-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "门户-获取用户信息返回数据")
public class QueryUserDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    @Schema(title = "用户ID")
    private Long userId;
    @Schema(title = "登录账号")
    private String loginId;
    @Schema(title = "用户名")
    private String userName;
    @Schema(title = "部门ID")
    private String departmentId;
}
