package com.ca.mfd.prc.core.main.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeLong;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author inkelink ${email}
 * @Description: 菜单项
 * @date 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "菜单项")
@TableName("PRC_PRM_MENU_ITEM")
public class SysMenuItemEntity extends BaseEntity {

    @JsonProperty("children")
    @TableField(exist = false)
    @Schema(title = "children")
    public List<SysMenuItemEntity> children;
    @Schema(title = "主键")
    @TableId(value = "PRC_PRM_MENU_ITEM_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;
    /**
     * 菜单
     */
    @Schema(title = "菜单")
    @TableField("PRC_PRM_MENU_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPrmMenuId = Constant.DEFAULT_ID;
    /**
     * 名称
     */
    @Schema(title = "名称")
    @TableField("ITEM_NAME")
    private String itemName = StringUtils.EMPTY;
    /**
     * URL
     */
    @Schema(title = "URL")
    @TableField("URL")
    private String url = StringUtils.EMPTY;
    /**
     * 位置
     */
    @Schema(title = "位置")
    @TableField("POSITION")
    private String position = StringUtils.EMPTY;
    /**
     * 打开方式（0.内嵌;1.弹出）
     */
    @Schema(title = "打开方式（0.内嵌;1.弹出）")
    @TableField("OPEN_TYPE")
    private Integer openType = 0;
    /**
     * 是否隐藏
     */
    @Schema(title = "是否隐藏")
    @TableField("IS_HIDE")
    private Boolean isHide = false;
    /**
     * 图标
     */
    @Schema(title = "图标")
    @TableField("ICON")
    private String icon = StringUtils.EMPTY;
    @Schema(title = "菜单权限Id")
    @TableField(exist = false)
    private Long prcPrmMenuPermissionId;

}
