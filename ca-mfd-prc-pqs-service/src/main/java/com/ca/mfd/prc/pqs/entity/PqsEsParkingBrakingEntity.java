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
 * @Description: 驻车制动实体
 * @author inkelink
 * @date 2024年02月15日
 * @变更说明 BY inkelink At 2024年02月15日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "驻车制动")
@TableName("PRC_PQS_ES_PARKING_BRAKING")
public class PqsEsParkingBrakingEntity extends BaseEntity {

    /**
     * ID
     */
    @Schema(title = "ID")
    @TableId(value = "PRC_PQS_ES_PARKING_BRAKING_ID", type = IdType.INPUT)
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
     * 上坡齿数/5S是否溜坡
     */
    @Schema(title = "上坡齿数/5S是否溜坡")
    @TableField("NUMBER_OF_UPHILL_TEETH")
    private String numberOfUphillTeeth = StringUtils.EMPTY;


    /**
     * 是否合格1
     */
    @Schema(title = "是否合格1")
    @TableField("QUALIFIED_FLAG_1")
    private Boolean qualifiedFlag1 = false;


    /**
     * 下坡齿数/5S是否溜坡
     */
    @Schema(title = "下坡齿数/5S是否溜坡")
    @TableField("NUMBER_OF_DOWNHILL_TEETH")
    private String numberOfDownhillTeeth = StringUtils.EMPTY;


    /**
     * 是否合格2
     */
    @Schema(title = "是否合格2")
    @TableField("QUALIFIED_FLAG_2")
    private Boolean qualifiedFlag2 = false;


    /**
     * 测量人
     */
    @Schema(title = "测量人")
    @TableField("SURVEYOR")
    private String surveyor = StringUtils.EMPTY;


}