package com.ca.mfd.prc.common.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 门户-获取用户权限列表返回数据
 *
 * @author mason
 * @date 2023-09-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "门户-获取用户权限列表返回数据")
public class QueryUserPermissionDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(title = "按钮权限")
    private List<String> btnPermission;

    @Schema(title = "资源权限")
    private List<String> uriPermission;
   
}
