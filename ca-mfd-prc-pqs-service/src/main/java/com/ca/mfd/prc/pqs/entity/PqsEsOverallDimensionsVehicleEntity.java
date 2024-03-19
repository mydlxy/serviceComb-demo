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
 * @Description: 整车外廓尺寸实体
 * @author inkelink
 * @date 2024年02月15日
 * @变更说明 BY inkelink At 2024年02月15日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "整车外廓尺寸")
@TableName("PRC_PQS_ES_OVERALL_DIMENSIONS_VEHICLE")
public class PqsEsOverallDimensionsVehicleEntity extends BaseEntity {

    /**
     * ID
     */
    @Schema(title = "ID")
    @TableId(value = "PRC_PQS_ES_OVERALL_DIMENSIONS_VEHICLE_ID", type = IdType.INPUT)
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
     * 外廓尺寸-长
     */
    @Schema(title = "外廓尺寸-长")
    @TableField("OVERALL_LONG")
    private BigDecimal overallLong = BigDecimal.valueOf(0);


    /**
     * 外廓尺寸-宽
     */
    @Schema(title = "外廓尺寸-宽")
    @TableField("OVERALL_WIDE")
    private BigDecimal overallWide = BigDecimal.valueOf(0);


    /**
     * 外廓尺寸-高
     */
    @Schema(title = "外廓尺寸-高")
    @TableField("OVERALL_HIGHT")
    private BigDecimal overallHight = BigDecimal.valueOf(0);


    /**
     * 是否合格1
     */
    @Schema(title = "是否合格1")
    @TableField("QUALIFIED_OR_NOT_1")
    private Boolean qualifiedOrNot1 = false;


    /**
     * 轮距前
     */
    @Schema(title = "轮距前")
    @TableField("FRONT_TRACK_WIDTH")
    private BigDecimal frontTrackWidth = BigDecimal.valueOf(0);


    /**
     * 轮距后
     */
    @Schema(title = "轮距后")
    @TableField("AFTER_TRACK_WIDTH")
    private BigDecimal afterTrackWidth = BigDecimal.valueOf(0);


    /**
     * 轴距左
     */
    @Schema(title = "轴距左")
    @TableField("WHEELBASE_LEFT")
    private BigDecimal wheelbaseLeft = BigDecimal.valueOf(0);


    /**
     * 轴距右
     */
    @Schema(title = "轴距右")
    @TableField("WHEELBASE_RIGHT")
    private BigDecimal wheelbaseRight = BigDecimal.valueOf(0);


    /**
     * 前悬
     */
    @Schema(title = "前悬")
    @TableField("FRONT_OVERHANG")
    private BigDecimal frontOverhang = BigDecimal.valueOf(0);


    /**
     * 后悬
     */
    @Schema(title = "后悬")
    @TableField("REAR_OVERHANG")
    private BigDecimal rearOverhang = BigDecimal.valueOf(0);


    /**
     * 是否合格2
     */
    @Schema(title = "是否合格2")
    @TableField("QUALIFIED_OR_NOT_2")
    private Boolean qualifiedOrNot2 = false;


}