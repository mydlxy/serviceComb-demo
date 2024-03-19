package com.ca.mfd.prc.core.prm.entity;


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
 * @Description: 按钮配置
 * @date 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "按钮配置")
@TableName("PRC_DC_BUTTON_CONFIG")
public class DcButtonConfigEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_DC_BUTTON_CONFIG_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    @Schema(title = "页面外键")
    @TableField("PRC_DC_PAGE_CONFIG_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcDcPageConfigId;

    @Schema(title = "按钮名称")
    @TableField("BUTTON_NAME")
    private String buttonName = StringUtils.EMPTY;

    @Schema(title = "前端标识")
    @TableField("BUTTON_COLOR")
    private String buttonColor = StringUtils.EMPTY;

    @Schema(title = "权限代码")
    @TableField("AUTHORIZATION_CODE")
    private String authorizationCode = StringUtils.EMPTY;

    @Schema(title = "显示位置")
    @TableField("SHOW_SITE")
    private String showSite = StringUtils.EMPTY;

    @Schema(title = "触发组件标识")
    @TableField("COMPONENT_KEY")
    private String componentKey = StringUtils.EMPTY;

    @Schema(title = "组件参数")
    @TableField("COMPONENT_PARA")
    private String componentPara = StringUtils.EMPTY;

    @Schema(title = "显示顺序号")
    @TableField("DISPLAY_NO")
    private Integer displayNo;

    @Schema(title = "别名")
    @TableField("BUTTON_KEY")
    private String buttonKey = StringUtils.EMPTY;
}
