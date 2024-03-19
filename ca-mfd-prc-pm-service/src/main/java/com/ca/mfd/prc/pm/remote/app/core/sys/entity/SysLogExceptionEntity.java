package com.ca.mfd.prc.pm.remote.app.core.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.ca.mfd.prc.common.convert.JsonDeserializeLong;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * @author inkelink ${email}
 * @Description: 系统异常日志
 * @date 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "系统异常日志")
@TableName("PRC_SYS_LOG_EXCEPTION")
public class SysLogExceptionEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_SYS_LOG_EXCEPTION_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 来源
     */
    @Schema(title = "来源")
    @TableField("APP")
    private String app = StringUtils.EMPTY;

    /**
     * 等级
     */
    @Schema(title = "等级")
    @TableField("LEVEL")
    private String level = StringUtils.EMPTY;

    /**
     * 消息
     */
    @Schema(title = "消息")
    @TableField("MESSAGE")
    private String message = StringUtils.EMPTY;

    /**
     * Logger
     */
    @Schema(title = "Logger")
    @TableField("LOGGER")
    private String logger = StringUtils.EMPTY;

    /**
     * 内容
     */
    @Schema(title = "内容")
    @TableField("CONTENT")
    private String content = StringUtils.EMPTY;

    /**
     * 日志记录时间
     */
    @Schema(title = "日志记录时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    @TableField("LOG_DT")
    private Date logDt = new Date();

    /**
     * 发送标记
     */
    @Schema(title = "发送标记")
    @TableField("IS_SEND")
    private Boolean isSend = false;

}
