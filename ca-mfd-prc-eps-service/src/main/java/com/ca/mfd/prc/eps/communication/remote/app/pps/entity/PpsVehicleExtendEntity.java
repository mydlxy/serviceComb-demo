package com.ca.mfd.prc.eps.communication.remote.app.pps.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeLong;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * @author eric.zhou
 * @Description: 车辆关键扩展信息实体
 * @date 2023年08月21日
 * @变更说明 BY eric.zhou At 2023年08月21日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "车辆关键扩展信息")
@TableName("PRC_PPS_VEHICLE_EXTEND")
public class PpsVehicleExtendEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PPS_VEHICLE_EXTEND_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 车辆VIN号
     */
    @Schema(title = "车辆VIN号")
    @TableField("VIN")
    private String vin = StringUtils.EMPTY;


    /**
     * 硬件版本
     */
    @Schema(title = "硬件版本")
    @TableField("HARDWARE_VERSION")
    private String hardwareVersion = StringUtils.EMPTY;


    /**
     * 软件版本
     */
    @Schema(title = "软件版本")
    @TableField("SOFTWARE_VERSION")
    private String softwareVersion = StringUtils.EMPTY;


}