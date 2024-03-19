package com.ca.mfd.prc.pps.communication.remote.app.core.sys.entity;

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
 * @Description: 服务操作日志
 * @date 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "服务操作日志")
@TableName("PRC_SYS_SERVICE_OPER_LOG")
public class SysServiceOperLogEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_SYS_SERVICE_OPER_LOG_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 服务外键
     */
    @Schema(title = "服务外键")
    @TableField("PRC_SYS_SERVICE_MANAGER_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long prcSysServiceManagerId = Constant.DEFAULT_ID;

    /**
     * 操作人ID
     */
    @Schema(title = "操作人ID")
    @TableField("OPER_USER_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long operUserId = Constant.DEFAULT_ID;

    /**
     * 操作人
     */
    @Schema(title = "操作人")
    @TableField("OPER_USER_NAME")
    private String operUserName = StringUtils.EMPTY;

    /**
     * 执行时间
     */
    @Schema(title = "执行时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    @TableField("OPER_DT")
    private Date operDt = new Date();

    /**
     * 描述
     */
    @Schema(title = "描述")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;

}
