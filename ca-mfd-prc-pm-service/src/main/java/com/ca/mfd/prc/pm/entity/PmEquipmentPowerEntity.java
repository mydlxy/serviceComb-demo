package com.ca.mfd.prc.pm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ca.mfd.prc.common.constant.Constant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 *
 * @Description: 设备能力实体
 * @author inkelink
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "设备能力")
@TableName("PRC_PM_EQUIPMENT_POWER")
public class PmEquipmentPowerEntity extends PmBaseEntity {

    /**
     * 设备能力ID
     */
    @Schema(title = "设备能力ID")
    @TableId(value = "PRC_PM_EQUIPMENT_POWER_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 设备ID
     */
    @Schema(title = "设备ID")
    @TableField("PRC_PM_EQUIPMENT_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPmEquipmentId = Constant.DEFAULT_ID;



    /**
     * 车间外键
     */
    @Schema(title = "车间外键")
    @TableField("PRC_PM_WORKSHOP_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPmWorkshopId = Constant.DEFAULT_ID;


    /**
     * 生产线ID
     */
    @Schema(title = "生产线ID")
    @TableField("PRC_PM_LINE_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPmLineId = Constant.DEFAULT_ID;


    /**
     * 工位ID
     */
    @Schema(title = "工位ID")
    @TableField("PRC_PM_WORKSTATION_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPmWorkstationId = Constant.DEFAULT_ID;


    /**
     * 工位号
     */
    @Schema(title = "工位号")
    @TableField("PRC_PM_WORKSTATION_CODE")
    private String workstationCode = StringUtils.EMPTY;


    /**
     * 设备能力
     */
    @Schema(title = "设备能力")
    @TableField("POWER_TYPE")
    private String powerType = StringUtils.EMPTY;


    /**
     * 标准值
     */
    @Schema(title = "标准值")
    @TableField("STANDARD_VALUE")
    private BigDecimal standardValue = BigDecimal.valueOf(0);


    /**
     * 最大值
     */
    @Schema(title = "最大值")
    @TableField("MAX_VALUE")
    private BigDecimal maxValue = BigDecimal.valueOf(0);


    /**
     * 最小值
     */
    @Schema(title = "最小值")
    @TableField("MIN_VALUE")
    private BigDecimal minValue = BigDecimal.valueOf(0);


    /**
     * 单位
     */
    @Schema(title = "单位")
    @TableField("UNIT")
    private String unit = StringUtils.EMPTY;


    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;

    @TableField(exist = false)
    @JsonIgnore
    private String workshopCode;

    @TableField(exist = false)
    @JsonIgnore
    private String workshopName;

    @TableField(exist = false)
    @JsonIgnore
    private String lineCode;

    @TableField(exist = false)
    @JsonIgnore
    private String lineName;


    @TableField(exist = false)
    @JsonIgnore
    private String workstationName;


    @JsonIgnore
    @TableField(exist = false)
    private String equipmentCode;

    @JsonIgnore
    @TableField(exist = false)
    private String equipmentName;

}