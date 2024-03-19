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
 * @Description: 空调出风口风量及温度测试记录实体
 * @author inkelink
 * @date 2024年02月15日
 * @变更说明 BY inkelink At 2024年02月15日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "空调出风口风量及温度测试记录")
@TableName("PRC_PQS_ES_AIR_COND_COOLING_CAPACITY_AIR_VOLUME_TEST")
public class PqsEsAirCondCoolingCapacityAirVolumeTestEntity extends BaseEntity {

    /**
     * ID
     */
    @Schema(title = "ID")
    @TableId(value = "PRC_PQS_ES_AIR_COND_COOLING_CAPACITY_AIR_VOLUME_TEST_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * VIN(后九位)
     */
    @Schema(title = "VIN(后九位)")
    @TableField("VIN")
    private String vin = StringUtils.EMPTY;


    /**
     * 车型
     */
    @Schema(title = "车型")
    @TableField("VEHICLE_MODEL")
    private String vehicleModel = StringUtils.EMPTY;


    /**
     * 日期
     */
    @Schema(title = "日期")
    @TableField("TEST_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date testDate;


    /**
     * 环境温度
     */
    @Schema(title = "环境温度")
    @TableField("AMBIENT_TEMPERATURE")
    private String ambientTemperature = StringUtils.EMPTY;


    /**
     * C度
     */
    @Schema(title = "C度")
    @TableField("C_DEGREES")
    private String cDegrees = StringUtils.EMPTY;


    /**
     * 中左
     */
    @Schema(title = "中左")
    @TableField("MID_LEFT")
    private String midLeft = StringUtils.EMPTY;


    /**
     * 中右
     */
    @Schema(title = "中右")
    @TableField("MID_RIGHT")
    private String midRight = StringUtils.EMPTY;

    /**
     * 是否合格
     */
    @Schema(title = "是否合格")
    @TableField("QUALIFIED_FLAG")
    private Boolean qualifiedFlag = false;


    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;


}