package com.ca.mfd.prc.pmc.remote.app.core.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeLong;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author inkelink ${email}
 * @Description: 菜单权限
 * @date 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "菜单权限")
@TableName("PRC_PRM_MENU_PERMISSION")
public class SysMenuPermissionEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_PRM_MENU_PERMISSION_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 权限ID
     */
    @Schema(title = "权限ID")
    @TableField("PRC_PRM_PERMISSION_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPrmPermissionId = Constant.DEFAULT_ID;

    /**
     * 菜单项ID
     */
    @Schema(title = "菜单项ID")
    @TableField("PRC_PRM_MENU_ITEM_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPrmMenuItemId = Constant.DEFAULT_ID;

}
