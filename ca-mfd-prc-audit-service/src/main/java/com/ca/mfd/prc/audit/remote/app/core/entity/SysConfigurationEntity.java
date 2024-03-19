package com.ca.mfd.prc.audit.remote.app.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeBoolean;
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
 * @Description: 系统配置
 * @date 2023-08-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "系统配置")
@TableName("PRC_SYS_CONFIGURATION")
public class SysConfigurationEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_SYS_CONFIGURATION_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 值
     */
    @Schema(title = "值")
    @TableField("VALUE")
    private String value = StringUtils.EMPTY;

    /**
     * 显示值
     */
    @Schema(title = "显示值")
    @TableField("TEXT")
    private String text = StringUtils.EMPTY;

    /**
     * 顺序号
     */
    @Schema(title = "顺序号")
    @TableField("DISPLAY_NO")
    private Integer displayNo = -1;

    /**
     * 分类
     */
    @Schema(title = "分类")
    @TableField("CATEGORY")
    private String category = StringUtils.EMPTY;

    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;

    /**
     * 是否隐藏
     */
    @Schema(title = "是否隐藏")
    @TableField("IS_HIDE")
    @JsonDeserialize(using = JsonDeserializeBoolean.class)
    private Boolean isHide = false;

    /**
     * 组名
     */
    @Schema(title = "组名")
    @TableField("GROUP_NAME")
    private String groupName = StringUtils.EMPTY;

    /**
     * 参数json
     */
    @Schema(title = "参数json")
    @TableField("PARAMS_JSON")
    private String paramsJson = StringUtils.EMPTY;

}
