package com.ca.mfd.prc.eps.remote.app.pps.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
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
 * @Description: 订单替换日志
 * @date 2023-04-28
 * @变更说明 BY inkelink At 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "订单替换日志")
@TableName("PRC_PPS_ORDER_CHANGE_LOG")
public class PpsOrderChangeLogEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_PPS_ORDER_CHANGE_LOG_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 源订单号
     */
    @Schema(title = "源订单号")
    @TableField("SOURCE_ORDER_NO")
    private String sourceOrderNo = StringUtils.EMPTY;

    /**
     * 源订TPS
     */
    @Schema(title = "源订TPS")
    @TableField("SOURCE_SN")
    private String sourceSn = StringUtils.EMPTY;

    /**
     * 目标订单号
     */
    @Schema(title = "目标订单号")
    @TableField("DES_ORDER_NO")
    private String desOrderNo = StringUtils.EMPTY;

    /**
     * 目标tps
     */
    @Schema(title = "目标tps")
    @TableField("DES_SN")
    private String desSn = StringUtils.EMPTY;

    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;

    /**
     * 操作时间
     */
    @Schema(title = "操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    @TableField("OPER_DT")
    private Date operDt = new Date();

}
