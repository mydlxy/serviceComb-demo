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
 * @Description: 信息推送记录
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "信息推送记录")
@TableName("PRC_MSG_PUSH")
public class MsgPushEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_MSG_PUSH_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id = Constant.DEFAULT_ID;

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
     * 发送方式 存的枚举数字 1，2，3(1.EMAIL;2.钉钉;3.短信) MethodType枚举
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
     * 默认主题
     */
    @Schema(title = "默认主题")
    @TableField("SUBJECT_DEFAULT")
    private String subjectDefault;

    /**
     * 默认内容
     */
    @Schema(title = "默认内容")
    @TableField("CONTENT_DEFAULT")
    private String contentDefault;

    /**
     * 推送主题
     */
    @Schema(title = "推送主题")
    @TableField("SUBJECT_PUSH")
    private String subjectPush;

    /**
     * 推送内容
     */
    @Schema(title = "推送内容")
    @TableField("CONTENT_PUSH")
    private String contentPush;

    /**
     * 是否模板替换
     */
    @Schema(title = "是否模板替换")
    @TableField("IS_REPLACE")
    private Integer isReplace;

    /**
     * 发送次数
     */
    @Schema(title = "发送次数")
    @TableField("SEND_TIMES")
    private int sendTimes;

    /**
     * 状态(1.未发送;2.发送成功;3.发送失败;4重新发送)
     */
    @Schema(title = "状态(1.未发送;2.发送成功;3.发送失败;4重新发送)")
    @TableField("STATUS")
    private Integer status;

    /**
     * 发送时间
     */
    @Schema(title = "发送时间")
    @TableField("SEND_DT")
    private Date sendDt;

    /**
     * 推送时间
     */
    @Schema(title = "推送时间")
    @TableField("PUSH_DT")
    private Date pushDt;

    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = "";
    /**
     * 发送消息主键id
     */
    @Schema(title = "发送消息主键id")
    @TableField("MESSAGE_SEND_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long messageSendId;


}