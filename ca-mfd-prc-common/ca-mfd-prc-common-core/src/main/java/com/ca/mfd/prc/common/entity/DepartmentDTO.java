package com.ca.mfd.prc.common.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 门户-查询部门及下级部门返回参数
 *
 * @author mason
 * @date 2023-09-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "门户-查询部门及下级部门返回参数")
public class DepartmentDTO {
    @Schema(title = "部门ID")
    private String departmentId;
    @Schema(title = "部门编码")
    private String departmentCode;
    @Schema(title = "部门名称")
    private String departmentName;
    @Schema(title = "上级部门ID")
    private String parentId;
}
