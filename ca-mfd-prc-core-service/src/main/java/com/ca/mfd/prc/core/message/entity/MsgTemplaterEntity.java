package com.ca.mfd.prc.core.message.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author jay.he
 * @Description: 信息模板
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "信息模板")
@TableName("PRC_MSG_TEMPLATER")
public class MsgTemplaterEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_MSG_TEMPLATER_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 模板代码
     */
    @Schema(title = "模板代码")
    @TableField("CODE")
    private String code;

    /**
     * 名称
     */
    @Schema(title = "名称")
    @TableField("NAME")
    private String name;

    /**
     * 模板参数样例
     */
    @Schema(title = "模板参数样例")
    @TableField("PARAMETER_DEMO")
    private String parameterDemo;

    /**
     * 主题
     */
    @Schema(title = "主题")
    @TableField("SUBJECT")
    private String subject;

    /**
     * 内容
     */
    @Schema(title = "内容")
    @TableField("CONTENT")
    private String content;

    /**
     * 是否有效
     */
    @Schema(title = "是否有效")
    @TableField("IS_ENABLE")
    private boolean enableFlag;
    //  private boolean isEnable;

    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark;


}