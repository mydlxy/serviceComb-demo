package com.ca.mfd.prc.pm.dto;


import com.baomidou.mybatisplus.annotation.TableField;
import com.ca.mfd.prc.common.constant.Constant;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;


/**
 *
 * @Description: 设备能力实体
 * @author inkelink
 * @date 2023年10月13日
 * @变更说明 BY inkelink At 2023年10月13日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "设备能力")
public class CmcPmEquipmentPowerVo extends CmcPmBusinessBaseVo {

    /**
     * 设备能力ID
     */
    @Schema(title = "设备能力ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long cmcPmEquipmentPowerId = Constant.DEFAULT_ID;


    /**
     * 设备ID
     */
    @Schema(title = "设备ID")
    @TableField("CMC_PM_EQUIPMENT_ID")
    private Long cmcPmEquipmentId = Constant.DEFAULT_ID;


    /**
     * 工作区域ID
     */
    @Schema(title = "工作区域ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long cmcPmAreaId = Constant.DEFAULT_ID;


    /**
     * 工作中心ID
     */
    @Schema(title = "工作中心ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long cmcPmWorkCenterId = Constant.DEFAULT_ID;


    /**
     * 工作单元ID
     */
    @Schema(title = "工作单元ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long cmcPmWorkUnitId = Constant.DEFAULT_ID;


    /**
     * 工位号
     */
    @Schema(title = "工位号")
    private String cmcPmWorkUnitCode = StringUtils.EMPTY;


    /**
     * 设备能力
     */
    @Schema(title = "设备能力")
    private String powerType = StringUtils.EMPTY;


    /**
     * 标准值
     */
    @Schema(title = "标准值")
    private BigDecimal standardValue = BigDecimal.valueOf(0);


    /**
     * 最大值
     */
    @Schema(title = "最大值")
    private BigDecimal maxValue = BigDecimal.valueOf(0);


    /**
     * 最小值
     */
    @Schema(title = "最小值")
    private BigDecimal minValue = BigDecimal.valueOf(0);


    /**
     * 单位
     */
    @Schema(title = "单位")
    private String unit = StringUtils.EMPTY;


    /**
     * 备注
     */
    @Schema(title = "备注")
    private String remark = StringUtils.EMPTY;



    @Schema(title =  "平均值")
    private String attribute1 = StringUtils.EMPTY;


}