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

import java.util.Date;

/**
 * @author jay.he
 * @Description: 信息发送记录
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "信息发送记录")
@TableName("PRC_MSG_SEND")
public class MsgSendEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_MSG_SEND_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 发送方式 存的枚举name 比如Email,FeiShu(1.EMAIL;2.钉钉;3.短信) MethodType枚举
     */
    @Schema(title = "发送方式(1.EMAIL;2.钉钉;3.短信)")
    @TableField("METHOD")
    private String method;

    /**
     * 发送方
     */
    @Schema(title = "发送方")
    @TableField("SOURCE")
    private String source;

    /**
     * 接收对象
     */
    @Schema(title = "接收对象")
    @TableField("DISTINATION")
    private String distination;

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
     * 发送次数
     */
    @Schema(title = "发送次数")
    @TableField("SEND_TIMES")
    private int sendTimes;

    /**
     * 状态(1.未处理;2.已处理;3.处理失败4.重复处理)
     */
    @Schema(title = "状态(1.未处理;2.已处理;3.处理失败4.重复处理)")
    @TableField("STATUS")
    private Integer status;

    /**
     * 发送时间
     */
    @Schema(title = "发送时间")
    @TableField("SEND_DT")
    private Date sendDt;

    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = "";

    /**
     * 触发id 对应业务表主键id
     */
    @Schema(title = "触发id")
    @TableField("TARGET_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long targetId;

    /**
     * 触发目标 存枚举数字 枚举MessageTargetType 邮件
     */
    @Schema(title = "触发目标")
    @TableField("TARGET_TYPE")
    private Integer targetType;

    /**
     * 模板代码
     */
    @Schema(title = "模板代码")
    @TableField("TPL_CODE")
    private String tplCode;

    /**
     * 传入参数
     */
    @Schema(title = "传入参数")
    @TableField("PARAMETERS")
    private String parameters;

    /**
     * 推送时间
     */
    @Schema(title = "推送时间")
    @TableField("PUSH_DT")
    private Date pushDt;

    /**
     * 接收类型(1、地址2、用户外键) DistinationType枚举
     */
    @Schema(title = "接收类型(1、地址2、用户外键)")
    @TableField("DISTINATION_TYPE")
    private Integer distinationType;

    /**
     * 接收对象JSON
     */
    @Schema(title = "接收对象JSON")
    @TableField("DISTINATION_JSON")
    private String distinationJson = "{}";

    /**
     * 接收对象名
     */
    @Schema(title = "接收对象名")
    @TableField("DISTINATION_NAME")
    private String distinationName;

}