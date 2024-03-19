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
 * @Description: 整车质量轴荷测试实体
 * @author inkelink
 * @date 2024年02月15日
 * @变更说明 BY inkelink At 2024年02月15日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "整车质量轴荷测试")
@TableName("PRC_PQS_ES_VEHICLE_MASS_AXLE_LOAD_TEST")
public class PqsEsVehicleMassAxleLoadTestEntity extends BaseEntity {

    /**
     * ID
     */
    @Schema(title = "ID")
    @TableId(value = "PRC_PQS_ES_VEHICLE_MASS_AXLE_LOAD_TEST_ID", type = IdType.INPUT)
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
     * VIN
     */
    @Schema(title = "VIN")
    @TableField("VIN")
    private String vin = StringUtils.EMPTY;

    /**
     * 车型
     */
    @Schema(title = "车型")
    @TableField("VEHICLE_MODEL")
    private String vehicleModel = StringUtils.EMPTY;


    /**
     * 车辆配置
     */
    @Schema(title = "车辆配置")
    @TableField("VEHICLE_CONFIGURATION")
    private String vehicleConfiguration = StringUtils.EMPTY;



    /**
     * 前轴荷
     */
    @Schema(title = "前轴荷")
    @TableField("FRONT_AXLE_LOAD")
    private String frontAxleLoad = StringUtils.EMPTY;


    /**
     * 后轴荷
     */
    @Schema(title = "后轴荷")
    @TableField("REAR_AXLE_LOAD")
    private String rearAxleLoad = StringUtils.EMPTY;


    /**
     * 是否合格1
     */
    @Schema(title = "是否合格1")
    @TableField("QUALIFIED_OR_NOT_1")
    private Boolean qualifiedOrNot1 = false;


    /**
     * LF左前
     */
    @Schema(title = "LF左前")
    @TableField("LEFT_FRONT")
    private String leftFront = StringUtils.EMPTY;


    /**
     * RF右前
     */
    @Schema(title = "RF右前")
    @TableField("RIGHT_FRONT")
    private String rightFront = StringUtils.EMPTY;


    /**
     * LR左后
     */
    @Schema(title = "LR左后")
    @TableField("LEFT_REAR")
    private String leftRear = StringUtils.EMPTY;


    /**
     * RR右后
     */
    @Schema(title = "RR右后")
    @TableField("RIGHT_REAR")
    private String rightRear = StringUtils.EMPTY;


    /**
     * 总重量
     */
    @Schema(title = "总重量")
    @TableField("TOTAL_WEIGHT")
    private BigDecimal totalWeight = BigDecimal.valueOf(0);


    /**
     * 是否合格2
     */
    @Schema(title = "是否合格2")
    @TableField("QUALIFIED_OR_NOT_2")
    private Boolean qualifiedOrNot2 = false;


}