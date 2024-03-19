package com.ca.mfd.prc.pps.remote.app.core.sys.entity;

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
import org.apache.commons.lang3.StringUtils;

/**
 * @author inkelink ${email}
 * @Description: 菜单版本管理
 * @date 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "菜单版本管理")
@TableName("PRC_PRM_MENU_ITEM_VERSION")
public class SysMenuItemVersionEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_PRM_MENU_ITEM_VERSION_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 名称
     */
    @Schema(title = "名称")
    @TableField("MENU_NAME")
    private String menuName = StringUtils.EMPTY;

    /**
     * 描述
     */
    @Schema(title = "描述")
    @TableField("MENU_ITEM_CONTENT")
    private String menuItemContent = StringUtils.EMPTY;

    /**
     * 菜单当前版本
     */
    @Schema(title = "菜单当前版本")
    @TableField("VERSION")
    private Integer version = 0;

    /**
     * 菜单ID
     */
    @Schema(title = "菜单ID")
    @TableField("PRC_PRM_MENU_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPrmMenuId = Constant.DEFAULT_ID;

    /**
     * 菜单项树
     */
    @Schema(title = "菜单项树")
    @TableField("MENU_ITEM_TREE")
    private String menuItemTree = StringUtils.EMPTY;

}
