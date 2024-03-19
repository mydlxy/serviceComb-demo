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
 * @Description: 制动盘跳动量记录实体
 * @author inkelink
 * @date 2024年02月15日
 * @变更说明 BY inkelink At 2024年02月15日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "制动盘跳动量记录")
@TableName("PRC_PQS_ES_BRAKE_DISC_RUNOUT")
public class PqsEsBrakeDiscRunoutEntity extends BaseEntity {

    /**
     * ID
     */
    @Schema(title = "ID")
    @TableId(value = "PRC_PQS_ES_BRAKE_DISC_RUNOUT_ID", type = IdType.INPUT)
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
     * 左前(大)
     */
    @Schema(title = "左前(大)")
    @TableField("LEFT_FRONT_BIG")
    private String leftFrontBig = StringUtils.EMPTY;


    /**
     * 左前(小)
     */
    @Schema(title = "左前(小)")
    @TableField("LEFT_FRONT_SMALL")
    private String leftFrontSmall = StringUtils.EMPTY;


    /**
     * 左前跳动量
     */
    @Schema(title = "左前跳动量")
    @TableField("LEFT_FRONT_RUNOUT")
    private String leftFrontRunout = StringUtils.EMPTY;


    /**
     * 右前(大)
     */
    @Schema(title = "右前(大)")
    @TableField("RIGHT_FRONT_BIG")
    private String rightFrontBig = StringUtils.EMPTY;


    /**
     * 右前(小)
     */
    @Schema(title = "右前(小)")
    @TableField("RIGHT_FRONT_SMALL")
    private String rightFrontSmall = StringUtils.EMPTY;


    /**
     * 右前跳动量
     */
    @Schema(title = "右前跳动量")
    @TableField("RIGHT_FRONT_RUNOUT")
    private String rightFrontRunout = StringUtils.EMPTY;


    /**
     * 是否合格
     */
    @Schema(title = "是否合格")
    @TableField("QUALIFIED_FLAG")
    private Boolean qualifiedFlag = false;


    /**
     * 测试人
     */
    @Schema(title = "测试人")
    @TableField("TESTER")
    private String tester = StringUtils.EMPTY;


}