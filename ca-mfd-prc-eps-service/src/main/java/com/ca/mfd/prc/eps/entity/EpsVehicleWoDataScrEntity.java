package com.ca.mfd.prc.eps.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
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

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author inkelink ${email}
 * @Description: 工艺数据，拧紧
 * @date 2023-04-28
 * @变更说明 BY inkelink At 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "工艺数据，拧紧")
@TableName("PRC_EPS_VEHICLE_WO_DATA_SCR")
public class EpsVehicleWoDataScrEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_EPS_VEHICLE_WO_DATA_SCR_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 数据编号
     */
    @Schema(title = "数据编号")
    @TableField("PRC_EPS_VEHICLE_WO_DATA_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long prcEpsVehicleWoDataId = Constant.DEFAULT_ID;

    /**
     * 工艺编号
     */
    @Schema(title = "工艺编号")
    @TableField("PRC_EPS_VEHICLE_WO_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long prcEpsVehicleWoId = Constant.DEFAULT_ID;

    /**
     * JOB号
     */
    @Schema(title = "JOB号")
    @TableField("JOB_NO")
    private String jobNo = StringUtils.EMPTY;

    /**
     * 批次号
     */
    @Schema(title = "批次号")
    @TableField("PSET_NO")
    private String psetNo = StringUtils.EMPTY;

    /**
     * 批次大小
     */
    @Schema(title = "批次大小")
    @TableField("BATCH_SIZE")
    private String batchSize = StringUtils.EMPTY;

    /**
     * 批次数量
     */
    @Schema(title = "批次数量")
    @TableField("BATCH_COUNT")
    private String batchCount = StringUtils.EMPTY;

    /**
     * 批次状态
     */
    @Schema(title = "批次状态")
    @TableField("BATCH_STATUS")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer batchStatus = 0;

    /**
     * 扭矩状态(可空)
     */
    @Schema(title = "扭矩状态")
    @TableField("TORQUE_STATUS")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer torqueStatus = 0;

    /**
     * 扭矩最小值(可空)
     */
    @Schema(title = "扭矩最小值")
    @TableField("TORQUE_MIN")
    private BigDecimal torqueMin = BigDecimal.ZERO;

    /**
     * 扭矩最大值(可空)
     */
    @Schema(title = "扭矩最大值")
    @TableField("TORQUE_MAX")
    private BigDecimal torqueMax = BigDecimal.ZERO;

    /**
     * 扭矩目标值(可空)
     */
    @Schema(title = "扭矩目标值")
    @TableField("TORQUE_TARGET")
    private BigDecimal torqueTarget = BigDecimal.ZERO;

    /**
     * 扭矩(可空)
     */
    @Schema(title = "扭矩")
    @TableField("TORQUE")
    private BigDecimal torque = BigDecimal.ZERO;

    /**
     * 角度状态(可空)
     */
    @Schema(title = "角度状态")
    @TableField("ANGLE_STATUS")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer angleStatus = 0;

    /**
     * 角度最小值(可空)
     */
    @Schema(title = "角度最小值")
    @TableField("ANGLE_MIN")
    private BigDecimal angleMin = BigDecimal.ZERO;

    /**
     * 角度最大值(可空)
     */
    @Schema(title = "角度最大值")
    @TableField("ANGLE_MAX")
    private BigDecimal angleMax = BigDecimal.ZERO;

    /**
     * 角度目标值(可空)
     */
    @Schema(title = "角度目标值")
    @TableField("ANGLE_TARGET")
    private BigDecimal angleTarget = BigDecimal.ZERO;

    /**
     * 角度(可空)
     */
    @Schema(title = "角度")
    @TableField("ANGLE")
    private BigDecimal angle = BigDecimal.ZERO;

    /**
     * 拧紧号
     */
    @Schema(title = "拧紧号")
    @TableField("TIGHTENING_ID")
    private String tighteningId = StringUtils.EMPTY;

    /**
     * 拧紧状态(可空)
     */
    @Schema(title = "拧紧状态")
    @TableField("TIGHTENING_STATUS")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer tighteningStatus = 0;


    /**
     * 数据类型(可空)
     */
    @Schema(title = "数据类型")
    @TableField("SOURCE_TYPE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer sourceType = 0;

    /**
     * 执行时间(可空)
     */
    @Schema(title = "执行时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    @TableField("TIME_STAMP")
    private Date timeStamp;


    /**
     * 拧紧顺序号(可空)
     */
    @Schema(title = "拧紧顺序号")
    @TableField("SEQUENCE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer sequence = 0;

}
