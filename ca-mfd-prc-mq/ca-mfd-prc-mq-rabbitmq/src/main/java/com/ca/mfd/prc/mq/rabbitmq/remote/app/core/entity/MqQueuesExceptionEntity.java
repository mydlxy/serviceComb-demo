package com.ca.mfd.prc.mq.rabbitmq.remote.app.core.entity;

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
import org.apache.commons.lang3.StringUtils;

/**
 * @author jay.he
 * @Description: 消息执行异常记录
 * @date 2023年09月11日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "消息执行异常记录")
@TableName("PRC_MQ_QUEUES_EXCEPTION")
public class MqQueuesExceptionEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_MQ_QUEUES_EXCEPTION_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 通道表主键ID
     */
    @Schema(title = "通道表主键ID")
    @TableField("PRC_MQ_QUEUES_NOTES_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long notesId = Constant.DEFAULT_ID;

    /**
     * 异常堆栈
     */
    @Schema(title = "异常堆栈")
    @TableField("STACK_TRACE")
    private String stackTrace = StringUtils.EMPTY;

    /**
     * 异常信息
     */
    @Schema(title = "异常信息")
    @TableField("MESSAGE")
    private String message = StringUtils.EMPTY;

    /**
     * 参数信息
     */
    @Schema(title = "参数信息")
    @TableField("CONTENT")
    private String content = StringUtils.EMPTY;

    /**
     * 用户信息
     */
    @Schema(title = "用户信息")
    @TableField("USER_INFO")
    private String userInfo = StringUtils.EMPTY;

}