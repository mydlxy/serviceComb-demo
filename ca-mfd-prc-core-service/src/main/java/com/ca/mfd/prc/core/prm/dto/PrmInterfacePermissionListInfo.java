package com.ca.mfd.prc.core.prm.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ca.mfd.prc.core.prm.entity.PrmInterfacePermissionEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author eric.zhou
 */
@Schema(title = "接口权限表", description = "接口权限表")
@Data
public class PrmInterfacePermissionListInfo extends PrmInterfacePermissionEntity {
    private static final long serialVersionUID = 1L;


    @Schema(description = "代码")
    @TableField(exist = false)
    private String permissionCode;

    @Schema(description = "名称")
    @TableField(exist = false)
    private String permissionName;

    @Schema(description = "描述")
    @TableField(exist = false)
    private String description;

    @Schema(description = "模块")
    @TableField(exist = false)
    private String model;

}

