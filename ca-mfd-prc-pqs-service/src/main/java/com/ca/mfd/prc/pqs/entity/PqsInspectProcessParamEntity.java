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

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @Description: 过程参数残扭抽检表实体
 * @author inkelink
 * @date 2024年02月15日
 * @变更说明 BY inkelink At 2024年02月15日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "过程参数残扭抽检表")
@TableName("PRC_PQS_INSPECT_PROCESS_PARAM")
public class PqsInspectProcessParamEntity extends BaseEntity {

    /**
     * ID
     */
    @Schema(title = "ID")
    @TableId(value = "PRC_PQS_INSPECT_PROCESS_PARAM_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 抽查序号
     */
    @Schema(title = "抽查序号")
    @TableField("SPOT_CHECK_SERIAL_NUMBER")
    private String spotCheckSerialNumber = StringUtils.EMPTY;


    /**
     * 工艺序号
     */
    @Schema(title = "工艺序号")
    @TableField("WO_DISPLAY_NO")
    private String woDisplayNo = StringUtils.EMPTY;


    /**
     * 适合车辆
     */
    @Schema(title = "适合车辆")
    @TableField("VEHICLE_MODEL")
    private String vehicleModel = StringUtils.EMPTY;


    /**
     * 链接点
     */
    @Schema(title = "链接点")
    @TableField("CONNECT_POINT")
    private String connectPoint = StringUtils.EMPTY;


    /**
     * 标件数量
     */
    @Schema(title = "标件数量")
    @TableField("NUMBER_STANDARD_PARTS")
    private Integer numberStandardParts = 0;


    /**
     * 关重特性
     */
    @Schema(title = "关重特性")
    @TableField("CRITICAL_CHARACTERISTIC")
    private String criticalCharacteristic = StringUtils.EMPTY;


    /**
     * 扭矩等级
     */
    @Schema(title = "扭矩等级")
    @TableField("TORQUE_LEVEL")
    private String torqueLevel = StringUtils.EMPTY;


    /**
     * 拧紧点类型
     */
    @Schema(title = "拧紧点类型")
    @TableField("TIGHTENING_POINT_TYPE")
    private String tighteningPointType = StringUtils.EMPTY;


    /**
     * 动态扭矩（N.m）
     */
    @Schema(title = "动态扭矩（N.m）")
    @TableField("DYNAMIC_TORQUE")
    private String dynamicTorque = StringUtils.EMPTY;


    /**
     * 动态扭矩公差（N.m）
     */
    @Schema(title = "动态扭矩公差（N.m）")
    @TableField("DYNAMIC_TORQUE_TOLERANCE")
    private BigDecimal dynamicTorqueTolerance = BigDecimal.valueOf(0);


    /**
     * 动态扭矩基值（N.m）
     */
    @Schema(title = "动态扭矩基值（N.m）")
    @TableField("DYNAMIC_TORQUE_BASELINE")
    private BigDecimal dynamicTorqueBaseline = BigDecimal.valueOf(0);


    /**
     * 残余扭矩（N.m）上限值
     */
    @Schema(title = "残余扭矩（N.m）上限值")
    @TableField("UPPER_LIMIT_RESIDUAL_TORQUE")
    private BigDecimal upperLimitResidualTorque = BigDecimal.valueOf(0);


    /**
     * 残余扭矩（N.m）下限值
     */
    @Schema(title = "残余扭矩（N.m）下限值")
    @TableField("LOWER_LIMIT_RESIDUAL_TORQUE")
    private BigDecimal lowerLimitResidualTorque = BigDecimal.valueOf(0);


    /**
     * 拧紧工位
     */
    @Schema(title = "拧紧工位")
    @TableField("WORKSTATION_CODE")
    private String workstationCode = StringUtils.EMPTY;


}