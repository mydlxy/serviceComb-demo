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
 * @Description: 消息模板默认发送地址
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "消息模板默认发送地址")
@TableName("PRC_MSG_TEMPLATER_TO")
public class MsgTemplaterToEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_MSG_TEMPLATER_TO_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 模板主键id
     */
    @Schema(title = "模板主键id")
    @TableField("PRC_MSG_TEMPLATER_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long messageTemplaterId;

    /**
     * 接收对象
     */
    @Schema(title = "接收对象")
    @TableField("DISTINATION")
    private String distination;

    /**
     * 接收类型(1、地址2、用户外键) 存数字 DistinationType枚举
     */
    @Schema(title = "接收类型(1、地址2、用户外键)")
    @TableField("DISTINATION_TYPE")
    private Integer distinationType;

    /**
     * 发送方式(1.EMAIL;2.钉钉;3.短信) MethodType枚举 存的是1，2，3
     */
    @Schema(title = "发送方式(1.EMAIL;2.钉钉;3.短信)")
    @TableField("METHOD")
    private String method;


}