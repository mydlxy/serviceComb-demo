package com.ca.mfd.prc.pm.remote.app.core.sys.entity;

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

import java.math.BigDecimal;

/**
 * @author inkelink ${email}
 * @Description: 第三方接口交互记录
 * @date 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "第三方接口交互记录")
@TableName("PRC_SYS_INTERFACE_LOG")
public class SysInterfaceLogEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_SYS_INTERFACE_LOG_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 对接系统
     */
    @Schema(title = "对接系统")
    @TableField("SYSTEM_NAME")
    private String systemName = StringUtils.EMPTY;

    /**
     * 车间名
     */
    @Schema(title = "车间名")
    @TableField("PATH")
    private String path = StringUtils.EMPTY;

    /**
     * 车间ID
     */
    @Schema(title = "车间ID")
    @TableField("METHOD")
    private String method = StringUtils.EMPTY;

    /**
     * 线体名
     */
    @Schema(title = "线体名")
    @TableField("REQUEST")
    private String request = StringUtils.EMPTY;

    /**
     * 线体ID
     */
    @Schema(title = "线体ID")
    @TableField("RESPONSE")
    private String response = StringUtils.EMPTY;

    /**
     * 异常记录
     */
    @Schema(title = "异常记录")
    @TableField("EXCEPTION_LOG")
    private String exceptionLog = StringUtils.EMPTY;

    /**
     * 工位名
     */
    @Schema(title = "工位名")
    @TableField("TYPE")
    private Integer type = 0;

    /**
     * 响应时间
     */
    @Schema(title = "响应时间")
    @TableField("TIME")
    private BigDecimal time;

    /**
     * 状态 1 成功  2 失败
     */
    @Schema(title = "状态1成功2失败")
    @TableField("STATUS")
    private Integer status;

}
