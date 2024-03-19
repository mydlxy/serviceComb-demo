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
 * @Description: 四车门关闭速度测试实体
 * @author inkelink
 * @date 2024年02月15日
 * @变更说明 BY inkelink At 2024年02月15日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "四车门关闭速度测试")
@TableName("PRC_PQS_ES_DOOR_CLOSING_SPEED_TEST")
public class PqsEsDoorClosingSpeedTestEntity extends BaseEntity {

    /**
     * ID
     */
    @Schema(title = "ID")
    @TableId(value = "PRC_PQS_ES_DOOR_CLOSING_SPEED_TEST_ID", type = IdType.INPUT)
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
     * 左前门1次测试值
     */
    @Schema(title = "左前门1次测试值")
    @TableField("LEFT_FRONT_DOOR_ONCE")
    private Double leftFrontDoorOnce = 0D;


    /**
     * 左前门2次测试值
     */
    @Schema(title = "左前门2次测试值")
    @TableField("LEFT_FRONT_DOOR_TWICE")
    private Double leftFrontDoorTwice = 0D;


    /**
     * 左前门3次测试值
     */
    @Schema(title = "左前门3次测试值")
    @TableField("LEFT_FRONT_DOOR_THIRD")
    private Double leftFrontDoorThird = 0D;


    /**
     * 左后门1次测试值
     */
    @Schema(title = "左后门1次测试值")
    @TableField("LEFT_REAR_DOOR_ONCE")
    private Double leftRearDoorOnce = 0D;


    /**
     * 左后门2次测试值
     */
    @Schema(title = "左后门2次测试值")
    @TableField("LEFT_REAR_DOOR_TWICE")
    private Double leftRearDoorTwice = 0D;


    /**
     * 左后门3次测试值
     */
    @Schema(title = "左后门3次测试值")
    @TableField("LEFT_REAR_DOOR_THIRD")
    private Double leftRearDoorThird = 0D;


    /**
     * 右前门1次测试值
     */
    @Schema(title = "右前门1次测试值")
    @TableField("RIGHT_FRONT_DOOR_ONCE")
    private Double rightFrontDoorOnce = 0D;


    /**
     * 右前门2次测试值
     */
    @Schema(title = "右前门2次测试值")
    @TableField("RIGHT_FRONT_DOOR_TWICE")
    private Double rightFrontDoorTwice = 0D;


    /**
     * 右前门3次测试值
     */
    @Schema(title = "右前门3次测试值")
    @TableField("RIGHT_FRONT_DOOR_THIRD")
    private Double rightFrontDoorThird = 0D;


    /**
     * 右后门1次测试值
     */
    @Schema(title = "右后门1次测试值")
    @TableField("RIGHT_REAR_DOOR_ONCE")
    private Double rightRearDoorOnce = 0D;


    /**
     * 右后门2次测试值
     */
    @Schema(title = "右后门2次测试值")
    @TableField("RIGHT_REAR_DOOR_TWICE")
    private Double rightRearDoorTwice = 0D;


    /**
     * 右后门3次测试值
     */
    @Schema(title = "右后门3次测试值")
    @TableField("RIGHT_REAR_DOOR_THIRD")
    private Double rightRearDoorThird = 0D;

    /**
     * 是否合格
     */
    @Schema(title = "是否合格")
    @TableField("QUALIFIED_FLAG")
    private Boolean qualifiedFlag = false;


}