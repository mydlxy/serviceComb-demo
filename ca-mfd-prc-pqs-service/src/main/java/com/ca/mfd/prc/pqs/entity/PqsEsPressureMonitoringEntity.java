package com.ca.mfd.prc.pqs.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.utils.UUIDUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 *
 * @Description: 轮胎气压监测实体
 * @author inkelink
 * @date 2024年02月15日
 * @变更说明 BY inkelink At 2024年02月15日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "轮胎气压监测")
@TableName("PRC_PQS_ES_PRESSURE_MONITORING")
public class PqsEsPressureMonitoringEntity extends BaseEntity {

    /**
     * ID
     */
    @Schema(title = "ID")
    @TableId(value = "PRC_PQS_ES_PRESSURE_MONITORING_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 日期
     */
    @Schema(title = "日期")
    @TableField("TEST_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date testDate;


    /**
     * 车型
     */
    @Schema(title = "车型")
    @TableField("VEHICLE_MODEL")
    private String vehicleModel = StringUtils.EMPTY;


    /**
     * VIN
     */
    @Schema(title = "VIN")
    @TableField("VIN")
    private String vin = StringUtils.EMPTY;


    /**
     * 左前轮-显示值（Kpa）
     */
    @Schema(title = "左前轮-显示值（Kpa）")
    @TableField("LEFT_FRONT_SURFACE_DISPLAY")
    private String leftFrontSurfaceDisplay = StringUtils.EMPTY;


    /**
     * 左前轮-实际值（Kpa）
     */
    @Schema(title = "左前轮-实际值（Kpa）")
    @TableField("LEFT_FRONT_ACTUAL_VALUE")
    private String leftFrontActualValue = StringUtils.EMPTY;


    /**
     * 左后轮-显示值（Kpa）
     */
    @Schema(title = "左后轮-显示值（Kpa）")
    @TableField("LEFT_REAR_SURFACE_DISPLAY")
    private String leftRearSurfaceDisplay = StringUtils.EMPTY;


    /**
     * 左后轮-实际值（Kpa）
     */
    @Schema(title = "左后轮-实际值（Kpa）")
    @TableField("LEFT_REAR_ACTUAL_VALUE")
    private String leftRearActualValue = StringUtils.EMPTY;


    /**
     * 右前轮-显示值（Kpa）
     */
    @Schema(title = "右前轮-显示值（Kpa）")
    @TableField("RIGHT_FRONT_SURFACE_DISPLAY")
    private String rightFrontSurfaceDisplay = StringUtils.EMPTY;


    /**
     * 右前轮-实际值（Kpa）
     */
    @Schema(title = "右前轮-实际值（Kpa）")
    @TableField("RIGHT_FRONT_ACTUAL_VALUE")
    private String rightFrontActualValue = StringUtils.EMPTY;


    /**
     * 右后轮-显示值（Kpa）
     */
    @Schema(title = "右后轮-显示值（Kpa）")
    @TableField("RIGHT_REAR_SURFACE_DISPLAY")
    private String rightRearSurfaceDisplay = StringUtils.EMPTY;


    /**
     * 右后轮-实际值（Kpa）
     */
    @Schema(title = "右后轮-实际值（Kpa）")
    @TableField("RIGHT_REAR_ACTUAL_VALUE")
    private String rightRearActualValue = StringUtils.EMPTY;


}